#!/bin/bash

# Include utility function
shopt -s expand_aliases
source "$(dirname ${BASH_SOURCE[0]})/../common/helpers.sh"
SCRIPT_DIR="$(git_root)/scripts/docker"

docker-compose -f ${SCRIPT_DIR}/docker-compose.yml stop
