# Shop Management API

This is a Spring Boot based backend application for managing a shop. 
It provides a role-based API with basic authentication for managing products and orders.

## Features

- Add, find, update, and delete products.
- Add, find, complete, cancel, and delete orders.
- Add users with roles
- Unit tests for service layer.

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- JUnit and Mockito for testing

## Getting Started

- git clone https://github.com/Mihaihorho/shop.git
- cd shop
- mvn spring-boot:run

### Running Tests
- mvn test

## API Endpoints

Default user with ADMIN rights is "admin" with the password "admin"

### Users

- `POST /users`: Add a new user
    - Body example:
      {
      "username": "test",
      "password": "test",
      "role": "READ"
      }

#### Possible Roles: READ, WRITE, ADMIN

Note - all requests are based on roles:

- `GET`: ADMIN, READ, WRITE
- `POST`, `PUT`: ADMIN, WRITE
- `DELETE`, `POST` (users): ADMIN

### Products

- `GET /products`: Get all products
- `GET /products/{id}`: Get a product by ID
- `POST /products`: Add a new product
  - Body example:
    {
        "name": "Apple",
        "price": 19.12,
        "stock": 1
    }
- `PUT /products/{id}`: Update a product by ID
  - Body example:
    {
        "name": "Apple",
        "price": 19.12,
        "stock": 1
    }
- `PUT /products/{id}/name?newName=<Name>`: Update a product name by ID
- `PUT /products/{id}/price?newPrice=<Price>`: Update a product price by ID
- `PUT /products/{id}/stock?newStock=<Stock>`: Update a product stock by ID
- `DELETE /products/{id}`: Delete a product by ID

### Orders

- `GET /orders`: Get all orders
- `GET /orders/{id}`: Get an order by ID
- `POST /orders`: Add a new order
  - Body example:
    {
        "productId": 1,
        "quantity": 10
    }
- `PUT /orders/{id}/complete`: Update an order as completed by ID
- `DELETE /orders/{id}/cancel`: Update an order as cancelled by ID
- `DELETE /orders/{id}`: Delete an order by ID