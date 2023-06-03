# Service for finding friends to go anywhere

## Description
A poster app where you can suggest an event, from an exhibition to a movie, and find people to attend it together.</br>
Consists of 2 parts:
1. The main part is responsible for CRUD operations for categories, events, users, and interaction with the database
2. Statistics service stores the number of views and allows you to make various selections to analyze the application's performance

## Stack
Spring Boot, Apache Maven, Project Lombok, Hibernate, PostgreSQL, Docker

## Design pattern
Data Transfer Object (DTO Pattern)

## Deploy
- **mvn clean package**
- **docker-compose up**

## API
Description is available in *postman-and-api-specs*

## TEST 
Postman tests are available in *postman-and-api-specs*