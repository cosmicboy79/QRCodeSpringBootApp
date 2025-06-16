# QRCodeSpringBootApp

"Spring Boot App" for a QR Code generator.

This is one of those "studying projects" in which I learn about Spring
Boot by building an small application and by improving it, bit a bit,
over time.

It is composed of two services built with Spring Boot:

1. REST Application
2. Web Application

See below details on how to start each of them.

This project uses [Maven](https://maven.apache.org/what-is-maven.html)
as build tool.

In order to build it, just run the well-known Maven command:

```
mvn clean install
```

To build without executing automated tests:

```
mvn clean install -DskipTests
```

Alternatively, Gradle can also be used:

```
gradle clean build
```

Java 21 is used for this project.

## Starting Spring Boot REST Application

Open a terminal window or tab and execute the following Maven command
in the project's root directory:

```
mvn -pl restapp spring-boot:run
```

Alternatively, the following Gradle command can also be used:

```
gradle :restapp:bootRun
```

The following REST operations, as defined in the OpenAPI specification,
will be exposed on HTTP `localhost:9090`:

- api/v1/qrcode/health
- api/v1/qrcode/generate

## Starting Spring Boot Web Application

Open a terminal window or tab and execute the following Maven command
in the project's root directory:

```
mvn -pl webapp spring-boot:run
```

Alternatively, the following Gradle command can also be used:

```
gradle :webapp:bootRun
```

Open the browser and go to the following address:

```
http://localhost:8080/qrcode
```

The Web Application is a client of the REST Application (see previous
section). If the Web Application is started but the REST Application
is not up and running, then the Web Application's main page will show
a message about it.

As soon as the REST Application is started, the Web Application's
main page is correctly set for the user.

## Structure

This project is structured in 3 directories:

### `openapi-spec`

Contains the [OpenAPI](https://www.openapis.org/what-is-openapi)
specification of the REST entry points exposed by the REST layer.

It also uses **openapi-generator-maven-plugin** to create Java POJO's
out of it.

### `restapp`

The Spring Boot REST Application.

### `webapp`

The Spring Boot Web Application.

The HTML page is templated with
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html#what-is-thymeleaf).
