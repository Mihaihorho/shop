# Shop Management API

This is a Spring Boot based backend application for managing a shop. 
It provides an API for managing products and orders.

## Features

- Add, find, update, and delete products.
- Add, find, complete, cancel, and delete orders.
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
- `DELETE /products/{id}`: Delete a product by ID

### Orders

- `GET /orders`: Get all orders
- `GET /orders/{id}`: Get an orders by ID
- `POST /orders`: Add a new orders
  - Body example:
    {
        "productId": 1,
        "quantity": 10
    }
- `PUT /orders/{id}/complete`: Update an order as completed by ID
- `DELETE /orders/{id}/cancel`: Update an order as cancelled by ID
- `DELETE /orders/{id}`: Delete a product by ID