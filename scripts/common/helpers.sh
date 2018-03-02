#!/bin/bash

shopt -s expand_aliases

###############################################################################
#                                                                             #
#                           GENERAL HELPER FUNCTIONS                          #
#                                                                             #
###############################################################################

# Print a message in green color with timestamp
msg() {
    nc='\033[0m'
    green='\033[0;32m'
    echo -e "${green}$(date +'%Y-%m-%d %H:%M:%S')\t$*${nc}";
}

# Print a message in red color
warn() {
    nc='\033[0m'
    red='\033[0;31m'
    echo -e "${red}$*${nc}";
}

# Execute any command and verify that exit code is 0. Print assertion message otherwise.
# Shortcut for try <assertion message> <command ...>
asserted() {
    try "Execution of command failed: $*" "$@"
}

# Execute any command hiding all output
silent() {
    "$@" >& /dev/null
    return $?
}

# Execute any command hiding error output
quiet() {
    "$@" 2> /dev/null
    return $?
}

# Usage: try <message> <command with args ...>
# Execute any command, print message and abort if command's exit code is non-zero
try() {
    message="$1"
    shift
    "$@"
    if [[ $? !=  0 ]]; then
        warn "$message";
        exit 200;
    fi
}

# Usage: try_subsh <message> <command with args ...>
# This executes a command in a subshell, which avoids any impact on the current shell.
try_subsh() {
    message="$1"
    shift
    (
        "$@"
    )
    if [[ $? !=  0 ]]; then
        warn "$message";
        exit 201;
    fi
}

# Verifies that the number of arguments to a function/script is exactly x.
# Use the alias with one arg (expected number of args).
_args_count() {
    message="Excpected $2 argument(s), found $1.";
    [[ $3 ]] && {
        message="$3";
    }
    try "$message" [ $1 -eq $2 ];
}
alias args_count='_args_count $# ';

# Verifies that the number of arguments to a function/script is at least x.
# Use the alias with one arg (expected minimum number of args).
_min_args_count() {
    message="Excpected $2 argument(s), found $1.";
    [[ $3 ]] && {
        message="$3";
    }
    try "$message" [ $1 -ge $2 ];
}
alias min_args_count='_min_args_count $# ';

# Make sure we are in a git repo
git_assert_is_repo() {
    try "Working dir is not inside a git repo!" silent git rev-parse --is-inside-work-tree;
}

# Checks if current working dir is clean. If not, it fails with non-zero exit code.
git_assert_is_clean() {
    try "Working directory not clean. Aborting." silent git diff --exit-code;
}

# Make sure given revision exists
git_assert_rev_exists() {
    args_count 1;
    try "Revision $1 does not exist. Aborting." silent [ "$(git cat-file -t $1)" == "commit" ];
}

# Checks out a revision with git if the working tree is clean.
git_checkout() {
    args_count 1;
    git_assert_is_clean;
    try "Could not checkout revision $1." silent git checkout "$1";
}

# Print the root directory of the current git project
git_root() {
    git rev-parse --show-toplevel;
}

# Determine latest tag and export it as LATEST_TAG. Abort if no valid tag was found.
git_tag_latest() {
    LATEST_TAG="$(git describe --tags $(git log --pretty=format:'%H' -n 1))";
    git_assert_rev_exists "$LATEST_TAG";
    export LATEST_TAG;
}

# Wait for background jobs to finish before moving on
sync_bg_jobs() {
    FAIL=0;
    for job in $(jobs -p)
    do
        msg "Synchronize with job $job";
        wait $job || {
            let "FAIL+=1";
        }
    done
    if [[ $FAIL != 0 ]]; then
        warn "$FAIL job(s) failed.";
    else
        msg "SUCCESS.";
    fi
}

# Run job in background
bg_job() {
    name="$1";
    shift;
    msg "Running in background: $name";
    "$@" &
    msg "PID $!";
}

# Assert that file does not exist.
file_assert_not_exists() {
    args_count 1;
    target="$1";

    try "File already exists $1." [ ! -e "$target" ];
}

# Assert that file exists
file_assert_exists() {
    args_count 1;
    target="$1";

    try "File does not exist $1." [ -f "$target" ];
}

# Assert that directory exists
dir_assert_exists() {
    args_count 1;
    target="$1";

    try "Directory does not exist $1." [ -d "$target" ];
}

# reads the specified property from a given property file
read_property() {
    args_count 2;
    property_file=$1
    property_key=$2

    awk -F"=" "/$property_key/ "'{print $2}' "$property_file"
}

#
# Validates if the file exists at the given URL.
#
validate_url() {
    args_count 3;
    local url="$1";
    local user="$2";
    local password="$3";

    if ! [[ `wget -S --http-user="$user" --http-password="$password" --spider "$url" 2>&1 | grep 'HTTP/1.1 200 OK'` ]]; then
        echo "No File exists on the given URL $url.";
        exit 2;
    fi
}
