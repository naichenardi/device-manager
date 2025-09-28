# Device Manager Service

A Spring Boot application for managing devices, using MySQL or PostgreSQL as the database. Includes RESTful APIs and OpenAPI (Swagger) documentation.

## Prerequisites

- Java 21 (https://adoptopenjdk.net/ or https://jdk.java.net/21/)
- Gradle (https://gradle.org/install/) or use the included `gradlew`/`gradlew.bat` wrapper
- Docker & Docker Compose (for running the MySQL database)

## Running the Database

Start the MySQL database using Docker Compose:

```
docker-compose up -d
```

This will start a MySQL 8.4 container with the following credentials:
- Database: `devicemanager`
- User: `devicemanager_user`
- Password: `N7v$k2!pQz8@wL1rT6#eX9sB4^uJ0mYd`
- Root password: `rootpassword`

The database will be available on port 3306.

## Building and Running the Application

You can build and run the application using Gradle:

```
./gradlew build
./gradlew bootRun
```

Or on Windows:
```
gradlew.bat build
gradlew.bat bootRun
```

The application will start on [http://localhost:8080](http://localhost:8080).

## Running Tests

To run the tests:

```
./gradlew test
```

Or on Windows:
```
gradlew.bat test
```

## API Documentation

Once the application is running, access the OpenAPI/Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Configuration

Application configuration is in `src/main/resources/application.yml`. Adjust database connection settings there if needed.

## Dependencies

- Spring Boot
- Spring Data JPA
- MySQL Connector / PostgreSQL Driver
- Springdoc OpenAPI UI
- JUnit (for testing)

---

Feel free to contribute or open issues for improvements!
