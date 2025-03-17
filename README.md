# Warehouse Management System

## Introduction

This project implements a Warehouse Management System for handling articles and products. The system allows loading articles and products from files, managing inventory, and selling products while updating stock accordingly. This software provides endpoints to fetch available products, sell products, and manage inventory.

The main features of the system include:
- **Inventory Management**: Loading and managing articles.
- **Product Management**: Creating products from articles with specified quantities and prices.
- **Product Sales**: Selling products and updating inventory.

## Technologies Used

- **Java 17**
- **Spring Boot 3.x** (for REST APIs)
- **Spring Data JPA** (for database interaction)
- **H2 Database** (in-memory database for testing)
- **Jackson** (for JSON processing)
- **SLF4J** (for logging)
- **Lombok** (for simplifying POJOs)
- **Spring Boot Starter Web** (for building web applications)
- **Spring Boot Starter Test** (for testing)

## Project Structure

1. **Controllers**: Handle HTTP requests and return responses. Contains RESTful API endpoints for product management and inventory.
2. **Services**: Contains the business logic for managing products, inventory, and sales.
3. **Repositories**: Interfaces for database interactions using Spring Data JPA.
4. **Models**: Represent entities for products, articles, and other necessary data objects.
5. **DTOs**: Data Transfer Objects (DTOs) used for API responses and input validation.
6. **Exceptions**: Custom exceptions to handle scenarios like insufficient stock or product not found.

## Architecture Diagrams

### Class Diagram

A class diagram has been created to visualize the structure of the system and how the classes are related to each other. The diagram includes the key components such as `Product`, `Article`, `ProductService`, and `InventoryService`, and how they interact within the system.

You can find the class diagram in the `docs/diagrams/class_diagram.puml` file.

### Sequence Diagram

The sequence diagram for the **`getAvailableProducts`** and **`sellProduct`** methods has been created to demonstrate the flow of requests between the controller, service, and repository layers.

For example, You can find the sequence diagrams for getAvailableProducts in the `docs/diagrams/sequence_diagram_getAvailableProducts.puml` file.

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.x or higher (for building the project)
- IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code
- H2 Database is used for in-memory testing

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/warehouse.git
    ```

2. Navigate to the project folder and build the project using Maven:

   ```bash
   mvn clean install
    ```
3. Run the application:

   ```bash
   mvn spring-boot:run
    ```
   The application will run on http://localhost:8080.

## API Documentation

This API provides functionality for managing products, articles, and inventory within a warehouse system. Below are the available endpoints to interact with the system.

### Base URL

All the API endpoints are relative to the base URL:  

`http://localhost:8080/api/`

---
### 1. Get Available Products

### **Endpoint** `GET /products/available`

### **Description**

Fetch all available products and their quantities, based on the current inventory stock.

### **Response**

```json
[
  {
    "name": "Dining Chair",
    "price": 100.0,
    "containArticles": [
      {
        "artId": "1",
        "name": "leg",
        "stock": 12
      },
      {
        "artId": "2",
        "name": "screw",
        "stock": 17
      }
    ],
    "availableQuantity": 10
  }
]
```

### 2. Sell Product

### **Endpoint** `POST /api/products/sell/{productName}`

### **Description**

Sell a product, which decreases the stock of the related articles.

### **Request Parameters** `productName` (Path parameter)

### **Response**

```json
{
  "name": "Dining Chair",
  "price": 100.0,
  "containArticles": [
    {
      "artId": "1",
      "name": "leg",
      "stock": 8
    },
    {
      "artId": "2",
      "name": "screw",
      "stock": 16
    }
  ],
  "availableQuantity": 8
}
```

### 3. Load Data (articles and products) 

### **Endpoint** `POST /api/data/loadData`

Example: `/api/data/loadData?articlesFilePath=src/main/resources/data/inventory.json&productsFilePath=src/main/resources/data/products.json`

### **Description**

Load articles and products from the provided file paths.

### **Request Parameters**

`articlesFilePath`: Path to the `inventory.json` file containing articles.

`productsFilePath`: Path to the `products.json` file containing products.

### **Response**

```json
{
  "message": "Articles and Products loaded successfully!"
}
```

### 4. Get All Articles in Inventory

### **Endpoint** `GET /api/inventory/`

### **Description**

Fetch all articles in the inventory.

### **Response**

```json
[
   {
      "artId": "1",
      "name": "leg",
      "stock": 12
   },
   {
      "artId": "2",
      "name": "screw",
      "stock": 17
   }
]

```

## Database Configuration

The project uses H2 Database for in-memory data storage, which is configured in the `application.properties` file

You can access the H2 console at `http://localhost:8080/h2-console` with the following credentials:
- **URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: (none)

## Error Handling

### **1. ProductNotFoundException**

Raised when a product cannot be found in the database.

### **Response**

```json
{
   "message": "Product not found: <productName>",
   "details": ["The requested product is not available in the inventory."],
   "statusCode": 404
}
```

### **2. InsufficientStockException**

Raised when there is insufficient stock to sell the product.

### **Response**

```json
{
   "message": "Not enough stock for article: <articleId>",
   "details": ["Stock is not enough to complete the order."],
   "statusCode": 400
}
```

## Conclusion

This warehouse management system is designed to handle the basic functionality of managing products, articles, and inventory. The system can load articles and products from files, update product quantities based on inventory, and handle product sales by updating the stock.

### Key Points:

- The system uses a simple in-memory H2 database for storage and testing.
- The API endpoints provide functionality for loading data, retrieving products, and selling products.
- Error handling is implemented for cases where products or stock are not available.
- Unit tests are written for core functionality to ensure the system works as expected.





