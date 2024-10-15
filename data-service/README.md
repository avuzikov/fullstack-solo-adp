# Data Service

This service is part of the Customer Management Microservices project. It handles CRUD operations for customer data.

## Features

- Customer data management (Create, Read, Update, Delete)
- Secured endpoints using OAuth2
- Running on port 8080

## Endpoints

- `GET /api`: Service health check
- `GET /api/customers`: Retrieve all customers
- `GET /api/customers/{id}`: Retrieve a specific customer
- `POST /api/customers`: Create a new customer
- `PUT /api/customers/{id}`: Update an existing customer
- `DELETE /api/customers/{id}`: Delete a customer

## Security

This service uses OAuth2 for securing endpoints. Ensure you have a valid access token from the Auth Service before making requests to protected endpoints.