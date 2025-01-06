# TechStack API
TechStack API is a Spring Boot application that provides a RESTful API for managing user accounts, products, and addresses.

## Features
- User authentication using JWT
- User management
- Product management
- Account management

## Getting Started
To get started with the TechStack API, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/804simba/techstack-api.git
   ```

2. Navigate to the project directory:
   ```bash
   cd techstack-api
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Start the application:
   ```bash
   mvn spring-boot:run
   ```

5. Access the API documentation:
   ```bash
   http://localhost:8080/swagger-ui.html
   ```
   You can access the API postman collection [here](https://drive.google.com/file/d/1zX2CK1Xz2BK5BRulqLik5UgzUBAeRbgX/view?usp=drive_link).

## Usage
The TechStack API provides the following endpoints:

### User Management
- `POST /api/v1/users`: Create a new user
- `GET /api/v1/users`: Get all users

### Product Management
- `GET /api/v1/products`: Get all products

### Account Management
- `POST /api/v1/accounts`: Create a new account
- `PUT /api/v1/accounts/{accountId}/activate`: Activate an account
- `GET /api/v1/accounts/{accountId}`: Get an account
- `POST /api/v1/accounts/{accountId}/search`: Search accounts
- `GET /api/v1/accounts/{userId}/accounts`: Get user accounts
- `DELETE /api/v1/accounts/{accountId}`: Delete an account

### Authentication
- `POST /api/v1/auth/login`: Login with email and password
