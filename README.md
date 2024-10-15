# Microservices Authentication and Data Management System

This project demonstrates a microservices architecture with two services: an authentication service and a data management service. The system is designed to provide secure user authentication and manage customer data.

## Project Structure

```
.
├── auth-service/
│   └── ... (authentication service files)
├── data-service/
│   └── ... (data service files)
├── docker-compose.yml
└── README.md
```

## Services

### Auth Service

The authentication service is responsible for user registration, login, and token validation. It runs on port 8081.

Key features:
- User registration
- User login with JWT token generation
- Token validation

### Data Service

The data service manages customer information and is protected by JWT authentication. It runs on port 8080.

Key features:
- CRUD operations for customer data
- JWT token validation for secure access

## Technologies Used

- Java 21
- Spring Boot 3.3.4
- Spring Security
- JSON Web Tokens (JWT)
- H2 Database
- Docker
- Gradle

## Prerequisites

- Java Development Kit (JDK) 21
- Docker and Docker Compose
- Gradle

## Getting Started

1. Clone the repository:
   ```
   git clone <repository-url>
   cd <project-directory>
   ```

2. Build the projects:
   ```
   cd auth-service
   ./gradlew build
   cd ../data-service
   ./gradlew build
   cd ..
   ```

3. Start the services using Docker Compose:
   ```
   docker-compose up --build
   ```

4. The services will be available at:
    - Auth Service: http://localhost:8081
    - Data Service: http://localhost:8080

## API Endpoints

### Auth Service

- `POST /account/register`: Register a new user
- `POST /account/token`: Get authentication token
- `POST /account/validate`: Validate token

### Data Service

- `GET /api/customers`: Get all customers
- `POST /api/customers`: Create a new customer
- `GET /api/customers/{id}`: Get a customer by ID
- `PUT /api/customers/{id}`: Update a customer
- `DELETE /api/customers/{id}`: Delete a customer

Note: All Data Service endpoints require a valid JWT token in the Authorization header.

## Testing

You can use tools like Postman or curl to test the APIs. Here's an example workflow:

1. Register a user:
   ```
   POST http://localhost:8081/account/register
   Body: {"name": "John Doe", "email": "john@example.com", "password": "password123"}
   ```

2. Get a token:
   ```
   POST http://localhost:8081/account/token
   Body: {"email": "john@example.com", "password": "password123"}
   ```

3. Use the token to access the Data Service:
   ```
   GET http://localhost:8080/api/customers
   Header: Authorization: Bearer <your-token>
   ```
