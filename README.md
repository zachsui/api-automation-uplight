# Openverse API - Test Automation Framework

## Introduction

Welcome to the Openverse API Test Automation Framework. This framework is designed to facilitate automated testing of the [Openverse API](https://api.openverse.engineering/v1/) using modern and efficient tools and libraries. It is built with a focus on maintainability, scalability, and ease of use. 

## Technologies Used

- **Java 21**: The core programming language for the framework.
- **Maven**: A build automation tool used for project management.
- **JUnit**: A testing framework for writing and running tests.
- **Rest Assured**: A Java library for testing and validating RESTful APIs.
- **MailSlurp**: An email testing tool used for email validation and automation.


### API Tests Structure

Under package org.uplight.qe test folders, we have following api tests
- BaseSetup, this is base test setup to prepare the test data and tokens
- AuthRegisterTest is test collection of end point: **POST /v1/auth_tokens/register/**
- AuthTokenTest is test collection of end point: **POST /v1/auth_tokens/token/** 
- AudioSearchTest is test collection of end point: **GET /v1/audio/**
- AudioStatsTest is test collectiion of end point: **GET /v1/audio/stats/**

## Features

- **HTTP Client and Validation**: Utilizing Rest Assured for seamless HTTP requests and response validations.
- **Utility Helper Classes**: A set of helper classes designed to:
  - Generate test data.
  - Manipulate and trim strings.
  - Prepare authentication tokens.
  - Fetch links from received emails.
- **Email Handling**: Integration with MailSlurp to manage email interactions, using the MailSlurp API key to receive and handle emails.

## Getting Started

### Prerequisites

Ensure you have the following installed on your development machine:
- Java 21
- Maven
- An IDE of your choice (IntelliJ IDEA, Eclipse, etc.)


### Bonus point
- Inside of AudioSearchTest, we have uti function called **isValidUUID4()** and related assertions to test each id fetched from the http response is a valid UUID4 string.
- Automate the email verification process while keeping your email credentials stored securely: I am using [MailSlurp REST API](https://docs.mailslurp.com/api/) to handle all the email receive transactions, and I am using a private fields for the api access key for the mail slurp so the credentials are stored securely.

### Run the API Automation test

- Run each test method or test class as indenpendent with JUnit
- or run all tests or certain test with maven commands
```bash

// run all tests
mvn clean test


//or for a certain test collection
mvn clean test -Dtest=AuthRegisterTest

// or for a certain test method
mvn test -Dtest=AuthRegisterTest#audioSearchWithInvalidToken
