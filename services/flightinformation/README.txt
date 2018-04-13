Flight Service
==============
This service provides information about airlines, airports and flight connections. It is implemented as a Spring Boot application with a REST API for retrieving the data in JSON format.

### Running the application locally
A Maven Wrapper is provided which can be used to build and start the application.
Build the application: ./mvnw clean install 
Run the application: ./mvnw spring-boot:run

The raw airlines, airports and flight connections data are plain csv files which can be downloaded from https://openflights.org/data.html and uploaded into the service. For this purpose the service provides a graphical user interface which can be used for uploading data.
The UI is locally accessed via the following URL: http://localhost:8083/csvUpload

## Running the application on Cloud Foundry
When running on Cloud Foundry the application is bound to Mongo DB service specified in the manifest.yml
The upload interface is reached by replacing the localhost by the corresponding host.

## Create User Provided Service
For connecting against the Atlas MongoDB, create a User Provided Service (if not happend yet).
First adapt the file educama-flight-information-database-service.json.
If connecting to a local database, provide the URI and username / password for this instance.

Then create the User Provided Service with the following command:
cf cups flight-service-database -p educama-flight-information-database-service.json
