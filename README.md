# Movie Wish List
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=bugs)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=mrmlb94_movie-wish-list&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=mrmlb94_movie-wish-list)

# 🎬 Movie Wish List

[![CI Pipeline](https://github.com/mrmlb94/movie-wish-list/actions/workflows/ci.yml/badge.svg)](https://github.com/mrmlb94/movie-wish-list/actions/workflows/ci.yml)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)

[![Coverage Status](https://coveralls.io/repos/github/mrmlb94/movie-wish-list/badge.svg?branch=master)](https://coveralls.io/github/mrmlb94/movie-wish-list?branch=master)
[![codecov](https://codecov.io/gh/mrmlb94/movie-wish-list/branch/master/graph/badge.svg)](https://codecov.io/gh/mrmlb94/movie-wish-list)

A production-grade web application for managing your movie wish list with military-grade testing and quality assurance.

🌐 **Live Demo**: [https://mrmlb.onrender.com](https://mrmlb.onrender.com)

## 🎯 Overview

Movie Wish List is a Spring Boot web application that allows users to track films they want to watch. Built with enterprise-level quality standards, this project demonstrates professional software engineering practices including comprehensive testing, continuous integration, and automated quality gates.

## ✨ Features

- 📝 Create, read, update, and delete movie entries
- 🏷️ Tag movies with custom categories (e.g., Action, Comedy)
- ✅ Mark movies as watched/unwatched
- 💾 Persistent storage with MongoDB Atlas (cloud database)
- 🎨 Responsive web interface with dynamic API integration
- 📊 RESTful API endpoints
- 🏥 Health monitoring with Spring Boot Actuator
- 🐳 Fully containerized with Docker Compose
- ☁️ Production deployment on Render.com
- 🔄 Automatic environment detection (localhost/production)

## 🛠 Technology Stack

### Backend
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.5.6** - Enterprise application framework
- **MongoDB** - NoSQL database for flexible data storage
- **Spring Data MongoDB** - Data access layer
- **Thymeleaf** - Server-side template engine
- **Spring Boot Actuator** - Production-ready monitoring

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Testcontainers** - Integration testing with containerized MongoDB
- **Selenium WebDriver** - Browser automation for E2E tests
- **PIT (Mutation Testing)** - Validates test quality (100% threshold)
- **JaCoCo** - Code coverage analysis (100% threshold)

### DevOps & Cloud
- **Docker & Docker Compose** - Containerization
- **Maven** - Build automation and dependency management
- **GitHub Actions** - CI/CD pipeline
- **Render.com**
- **Codecov & Coveralls** - Coverage reporting

## 🏗 Architecture

```
movie-wish-list/
├── src/
│   ├── main/
│   │   ├── java/com/example/movie/wish/list/
│   │   │   ├── controller/             # REST controllers
│   │   │   ├── service/                # Business logic
│   │   │   ├── repository/             # Data access
│   │   │   ├── model/                  # Domain entities
│   │   │   └── MovieWishListApplication.java
│   │   └── resources/
│   │       ├── static/                 # Static 
│   │       ├── templates/              # Thymeleaf 
│   │       └── application.properties  # Configurations
│   └── test/
│       └── java/com/example/movie/wish/list/
│           ├── unit/                   # Unit tests
│           ├── it/                     # Integration tests (IT)
│           └── e2e/                    # End-to-end tests (E2ETest)
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## 🚀 Getting Started

### Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/mrmlb94/movie-wish-list.git
   cd movie-wish-list
   ```

2. **Start MongoDB with Docker Compose**
   ```bash
   docker compose up -d mongodb
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the application**
    - Web UI: `http://localhost:8080`

### Quick Start with Docker

```bash
docker-compose up --build
```

## 🧪 Testing Strategy

This project implements a **three-tier testing pyramid** with strict quality gates:

### 1. Unit Tests
- **Framework**: JUnit 5 + Mockito
- **Coverage**: 100% required (JaCoCo)
- **Mutation Coverage**: 100% required (PIT)
- **Execution**: `./mvnw test`

### 2. Integration Tests
- **Framework**: Spring Boot Test + Testcontainers
- **Database**: Real MongoDB instance in container
- **Naming**: `*IT.java`
- **Execution**: `./mvnw verify`

### 3. End-to-End Tests
- **Framework**: Selenium WebDriver
- **Browsers**: Chrome (auto-managed by WebDriverManager)
- **Naming**: `*E2ETest.java`
- **Execution**: `./mvnw verify`

### Run All Tests

```bash
# Unit tests only
./mvnw test

# Integration + E2E tests
./mvnw verify

# All tests with coverage
./mvnw clean verify

# Mutation testing
./mvnw org.pitest:pitest-maven:mutationCoverage
```

### Test Reports

After running tests, view reports at:
- **Unit Test Results**: `target/surefire-reports/index.html`
- **Integration Test Results**: `target/failsafe-reports/index.html`
- **Code Coverage**: `target/site/jacoco/index.html`
- **Mutation Coverage**: `target/pit-reports/index.html`

## 🔄 CI/CD Pipeline

Our GitHub Actions pipeline ensures code quality on every commit:

### Pipeline Stages

1. **Environment Setup**
    - Ubuntu Linux with Docker
    - Java 21 (Temurin distribution)
    - Maven dependency caching

2. **Unit Testing**
    - Execute all unit tests
    - Generate JaCoCo coverage report
    - Enforce 100% coverage threshold

3. **Mutation Testing**
    - Run PIT mutation analysis
    - Enforce 100% mutation coverage
    - Verify tests actually catch defects

4. **Integration Testing**
    - Start Docker Compose services
    - Execute integration tests
    - Execute E2E browser tests
    - Stop and cleanup containers

5. **Coverage Reporting**
    - Upload to Codecov
    - Upload to Coveralls
    - Generate detailed reports

6. **Docker Build**
    - Build production Docker image
    - Tag with commit SHA

### Trigger Conditions

- ✅ Push to `master` branch
- ✅ Pull requests to `main` or `develop`

## 📊 Code Quality

### Quality Metrics

| Metric            | Threshold | Tool     |
|-------------------|-----------|----------|
| Code Coverage     | 100%      | JaCoCo   |
| Mutation Coverage | 100%      | PIT      |
| Build Success     | Required  | Maven    |
| Integration Tests | All Pass  | Failsafe |
| E2E Tests         | All Pass  | Selenium |

### Quality Gates

- ❌ **Pipeline fails** if code coverage < 100%
- ❌ **Pipeline fails** if mutation coverage < 100%
- ❌ **Pipeline fails** if any test fails
- ❌ **Pipeline fails** if Docker build fails

## 📖 API Documentation

### Health Check Endpoint

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

### Movie Endpoints

Documentation coming soon. Access Swagger UI at `http://localhost:8080/swagger-ui.html` (if enabled).

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Write tests** for your changes (100% coverage required)
4. **Ensure all tests pass** (`./mvnw verify`)
5. **Run mutation tests** (`./mvnw org.pitest:pitest-maven:mutationCoverage`)
6. **Commit your changes** (`git commit -m 'Add amazing feature'`)
7. **Push to the branch** (`git push origin feature/amazing-feature`)
8. **Open a Pull Request**

### Code Standards

- Follow Java naming conventions
- Write comprehensive unit tests
- Maintain 100% code coverage
- Ensure mutation tests pass
- Document public APIs
- Update README for new features

## 🐛 Known Issues & Limitations

- **Render Free Tier**: Service spins down after 15 minutes of inactivity (30-50 second cold start)
- **MongoDB Atlas Free Tier**: Limited to 512MB storage
- **Browser Compatibility**: Tested on Chrome


## 👥 Authors

- **mrmlb94** - *Initial work* - [GitHub Profile](https://github.com/mrmlb94)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Testcontainers for simplified integration testing
- PIT for mutation testing capabilities
- The open-source community

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/mrmlb94/movie-wish-list/issues)
- **Discussions**: [GitHub Discussions](https://github.com/mrmlb94/movie-wish-list/discussions)

---

**⭐ If you find this project useful, please consider giving it a star!**

Made with ❤️ by [mrmlb94](https://github.com/mrmlb94)