# Pet Store Community

Pet store service application provides simple REST calls that can be used to exercise writing automated testing of API.

## Prerequisites

- Install [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- Install [IntelliJ community edition](https://www.jetbrains.com/idea/download/?section=windows)
- Install [Postman](https://www.postman.com/) to perform REST API calls or use the IntelliJ plugin [Restful API Tool](https://plugins.jetbrains.com/plugin/22446-restful-api-tool)

Review the following materials that were used in this project
- [Spring Boot](https://spring.io/guides/gs/spring-boot) to run the REST services.
- [Google Gson](https://github.com/google/gson) to handle JSON data
- [Mockito](https://site.mockito.org/) mock services and repository
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#overview) for running test

## Project structure

```
- petstorecommunity
  - src
    - main
	  - java
	  - resources
	- test
	  - functionaltests
	  - unitetests
	  - services
  - datastore
    - application
    - original
  - .gitignore
  - pom.xml
  - README.md
```

## Getting started

- Clone the repository or download from [here](https://github.com/bkeenan26/petstorecommunity).
- From IntelliJ, import (File > New > Project from Existing Sources) by selecting the project folder.
- Set the project with Java 17
- Run the spring application 
  * Press Shift+F10 or
  * use the play/run icon of the PetstoreserviceApplication.java file
- The *Console* tab shows the output Spring log messages. By default, the built-in Apache Tomcot server is listening on port 8080. Open your Web browser and go to http://localhost:8080/hello. If everything is setup correct, you should see your application response with ```Hello World!``` in the browser.