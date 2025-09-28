# Device Manager Service

A Spring Boot application for managing devices, using MySQL as the database. Includes RESTful APIs.

## API Endpoints

| HTTP Method | Path                | Description                                   | Request Body         | Query Params         |
|-------------|---------------------|-----------------------------------------------|----------------------|----------------------|
| POST        | /api/devices        | Create a new device                           | DeviceRequest (JSON) |                      |
| GET         | /api/devices/{id}   | Get a device by its ID                        |                      |                      |
| GET         | /api/devices        | Get all devices, or filter by brand or state  |                      | brand, state         |
| PUT         | /api/devices/{id}   | Update a device (full update)                 | DeviceRequest (JSON) |                      |
| PATCH       | /api/devices/{id}   | Partially update a device                     | DeviceRequest (JSON) |                      |
| DELETE      | /api/devices/{id}   | Delete a device by its ID                     |                      |                      |

**Query Parameters for GET /api/devices:**
- `brand` (optional): Filter devices by brand.
- `state` (optional): Filter devices by state (e.g., AVAILABLE, INACTIVE, IN_USE).

**Request/Response Example Types:**
- DeviceRequest: Contains fields like name, brand, and state.
- DeviceResponse: Contains id, name, brand, state, and creationTime.

## Prerequisites

- Java 21 https://jdk.java.net/21/)
- Gradle (https://gradle.org/install/) or use the included `gradlew`/`gradlew.bat` wrapper
- Docker & Docker Compose (for running the MySQL database and/or the application)

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

## Running the Application with Docker

You can run the Device Manager application itself in a Docker container. This is useful for deployment or to avoid installing Java/Gradle locally.

### 1. Build the Docker image

```
docker build -t device-manager .
```

### 2. Run the application container

Make sure the MySQL database is running (see above). Then run:

```
docker run -d --name device-manager-app \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/devicemanager \
  -e SPRING_DATASOURCE_USERNAME=devicemanager_user \
  -e SPRING_DATASOURCE_PASSWORD=N7v$k2!pQz8@wL1rT6#eX9sB4^uJ0mYd \
  -p 8080:8080 \
  device-manager
```

- On Windows/Mac, `host.docker.internal` allows the container to access the host's MySQL instance.
- On Linux, use your host's IP address instead of `host.docker.internal`.

### 3. (Optional) Run with Docker Compose

You can run both the app and the database together using Docker Compose:

```
docker-compose up -d --build
```

This will build the app image (if needed) and start both containers. The app will be available at [http://localhost:8080](http://localhost:8080).

## Building and Running the Application

You can build and run the application using Gradle:

```
./gradlew build
./gradlew bootRun
```

The application will start on [http://localhost:8080](http://localhost:8080).

## Environment Configuration

Application configuration can be found in `src/main/resources/application.yml`. You can adjust database connection settings and other properties as needed.

## Running Tests

To run the tests:

```
./gradlew test
```

Or on Windows:
```
gradlew.bat test
```

## Continuous Integration (CI) with GitHub Actions

This project uses GitHub Actions for continuous integration. Every time you push a commit or open a pull request to the `master` branch, an automated workflow will:

- Check out the repository code
- Set up JDK 21
- Build the project using Gradle
- Run all tests

You can find the workflow configuration in `.github/workflows/build.yml`. Build and test results will be visible on the GitHub repository under the [Actions tab](https://github.com/naichenardi/device-manager/actions).

## Contributing

Contributions are welcome! Please open issues or submit pull requests for improvements or bug fixes.

---
