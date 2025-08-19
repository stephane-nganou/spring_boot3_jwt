# Spring Boot 3 JWT Authentication for Bookshop API

## Overview
This project is a robust Spring Boot 3 application designed to provide a secure and scalable backend for a bookshop management system. It leverages JSON Web Token (JWT) authentication to ensure secure user access and integrates modern Spring framework features to deliver a solid foundation for enterprise-grade applications. The primary goal is to demonstrate best practices in building a secure, maintainable, and extensible RESTful API using Spring Boot and Spring Security.

This project serves as a reference implementation for developers looking to understand and apply:
- **Spring Security**: Implementing secure authentication and authorization with JWT.
- **Spring Data JPA with Pagination**: Efficient data retrieval and management using JPA and pagination.
- **Swagger and OpenAPI**: Comprehensive API documentation for seamless integration and testing.
- **Exception Handling**: Structured and consistent error management for improved reliability.
- **Clean Code Principles**: Writing readable, modular, and maintainable code.
- **SOLID Principles**: Designing code that is scalable, maintainable, and adheres to object-oriented design principles.
- **Testing Best Practices**: Write comprehensives Init- and Integration Tests. May be E2E Tests with Karate will be included.

## Features
- **JWT-based Authentication**: Secure user authentication and session management using JSON Web Tokens.
- **Role-based Authorization**: Fine-grained access control for different user roles (e.g., admin, customer).
- **Spring Data JPA**: Simplified database operations with support for pagination and sorting.
- **Swagger UI Integration**: Interactive API documentation for easy exploration and testing of endpoints.
- **Global Exception Handling**: Centralized error handling for consistent API responses.
- **Clean Architecture**: Modular project structure following clean code and SOLID principles.
- **Database Support**: Configurable for popular relational databases (PostgreSQL).
- **Logging**: Integrated logging for debugging and monitoring application behavior.

## Project Status
This project is actively maintained and under development. A complementary frontend application built with Angular is currently in progress to provide a complete full-stack solution. The frontend will consume this API to offer a user-friendly interface for bookshop operations.

## Getting Started

### Prerequisites
- **Java 21 or later**: Ensure you have the correct JDK version installed.
- **Maven 3.8.x or later**: For dependency management and building the project.
- **Database**: A relational database (PostgreSQL) configured with the appropriate credentials.
- **IDE**: IntelliJ IDEA, Eclipse, or any IDE with Spring Boot support.
- **Postman or cURL or Karate**: For testing API endpoints.

### Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/stephane-nganou/spring_boot3_jwt.git
   cd spring_boot3_jwt
   ```
2. **Configure Application Properties**:
   - Update the `application-dev.yml` and/or `application.yml` file in `src/main/resources` with your database credentials and JWT secret key.
   - Example configuration:
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/bookshop_db
         username: your-username
         password: your-password
       jpa:
         hibernate:
           ddl-auto: update
     jwt:
       secret: your-secure-jwt-secret
       expiration: 86400000 # 24 hours in milliseconds
     ```
3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. **Access Swagger UI**:
   - Open `http://localhost:8080/swagger-ui.html` in your browser to explore the API documentation.

### Usage
- **Authentication**: Use the `/api/v1/auth/` endpoint to obtain a JWT token by providing valid user credentials.
- **API Endpoints**: Access protected endpoints by including the JWT token in the `Authorization` header (e.g., `Bearer <token>`).
- **Example Request**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "password"}'
  ```

## Project Structure
```
spring_boot3_jwt_bookshop/
├── src/
│   ├── main/
│   │   ├── java/com/masteranything/security
│   │   │   ├── config/          # Spring Security and JWT configuration
│   │   │   ├── controller/      # REST API controllers
│   │   │   ├── dao/          # JPA entities
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   ├── service/         # Business logic and service layer
│   │   │   ├── exception/       # Custom exception handling
│   │   │   └── dto/             # Data Transfer Objects
│   │   │   └── util/            # Util for the whole project
│   │   ├── resources/
│   │   │   ├── application.yml  # Application configuration
│   │   │   └── static/          # Static resources (if any)
│   │   │   └── templates/       # to hold some html file template
│   └── test/                    # Unit and integration tests
├── pom.xml                      # Maven dependencies and build configuration
└── README.md                    # Project documentation
```

## Future Enhancements
The following features are planned for future releases:
- **CI/CD Pipeline**: Configuration for automated testing and deployment using tools like GitHub Actions or Jenkins.
- **Cloud Deployment**: Step-by-step guides for deploying the application to AWS (e.g., using Elastic Beanstalk or ECS).
- **Frontend Integration**: Completion of the Angular frontend application to provide a complete full-stack experience.
- **Performance Optimization**: Caching mechanisms (e.g., Redis) and query optimization for improved performance.
- **Unit and Integration Tests**: Expanded test coverage for controllers, services, and repositories.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request with a detailed description of your changes.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For questions or feedback, please reach out to [your-email@example.com](mailto:your-email@example.com) or open an issue on the GitHub repository.