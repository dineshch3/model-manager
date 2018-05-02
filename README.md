# Model Manager
A microservice application to manage the life cycle of a Machine Learning Model.

This application is configured for Service Discovery and Configuration with the JHipster-Registry.
On launch, it will refuse to start if it is not able to connect to the JHipster-Registry at
[http://localhost:8761](http://localhost:8761).

## Development

To start your application in the dev profile, simply run:

    ./mvnw


## Building for production

To optimize the modelmanager application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war


## Testing

To launch your application's tests, run:

    ./mvnw clean test


## Using Docker

Docker-compose configurations are available in the [src/main/docker](src/main/docker) folder to launch required services.

For example, to start a postgresql database in a docker container, run:

    docker-compose -f src/main/docker/postgresql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/postgresql.yml down

You can also fully dockerize the application and all the services that it depends on.
To achieve this, first build a docker image of the app by running:

    ./mvnw verify -Pprod dockerfile:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d


## Continuous Integration

To configure CI for the project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate
configuration files for a number of Continuous Integration systems.

