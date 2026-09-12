<div align="center">
# 🧠 LogIQ - Core Backend Engine
 
### AI-Powered Observability & Debugging Platform API
 
This is the **Backend** repository for LogIQ, built with Spring Boot 3 and Java 17. It serves as the central processing node, handling real-time telemetry streaming via WebSockets, robust authentication with JWT, and AI-powered log analysis.
 
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](#-license)
 
</div>

---

## 🔗 Related Repository

This project is the backend core engine for the LogIQ platform. 
**Frontend Client:** 👉 [LogIQ React Frontend](https://github.com/shan2003034/logiq-frontend) 

---
 
## 📖 About The Backend
 
The LogIQ backend is a high-performance RESTful API and message broker built on **Spring Boot**. It is designed to receive, process, and store telemetry data from distributed microservices using the LogIQ custom SDK. 

This engine securely handles user authentication, manages project metadata, processes live server health streams via WebSockets, and acts as the secure bridge to our AI Neural Engine for automated code debugging.
 
- ⚡ Asynchronous telemetry processing with zero bottleneck
- 🔒 Secure Role-Based Access Control (RBAC) using Spring Security & JWT
- 📡 WebSocket message broker for real-time dashboard updates
- 🧠 Secure integration with Large Language Models (LLMs) for AI fixes

---
 
## ✨ Features
 
- 🔐 **Authentication & Security:** Robust JWT-based authentication system supporting User/Owner and Developer roles.
- 📡 **Real-Time Data Broker:** Utilizes WebSockets (STOMP) to broadcast live health metrics and incoming exception logs to connected UI clients.
- 🗄️ **Data Persistence & Retrieval:** Optimized JPA repositories to efficiently store and query large volumes of project logs via MySQL.
- 🤖 **AI Processing Node:** Securely structures prompt payloads containing stack traces and communicates with AI APIs to fetch debugging suggestions.
- 📧 **Automated Mailer:** Spring Mail integration to handle system alerts, user registrations, and team collaboration invitations.
- ⚙️ **SDK Endpoint Handler:** Dedicated, lightweight endpoints designed specifically to consume data seamlessly from the `logiq-spring-boot-starter` SDK.

---
 
## 🛠️ Tech Stack
 
| Layer | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.x |
| **Security** | Spring Security, JSON Web Tokens (JWT) |
| **Database** | MySQL 8 |
| **ORM** | Spring Data JPA / Hibernate |
| **Real-Time Messaging** | WebSockets (SockJS, STOMP) |
| **Build Tool** | Apache Maven |
 
---
 
## 🏗️ Backend Architecture
 
~~~mermaid
flowchart TB
    subgraph External Clients ["Data Sources"]
        SDK["LogIQ SDK<br/>(Microservices)"]
    end
 
    subgraph Spring Boot Backend ["LogIQ Core Engine"]
        SEC["Spring Security<br/>(JWT Filter)"]
        
        subgraph Controllers ["REST & WS Controllers"]
            API["REST APIs<br/>(Auth, Logs, Projects)"]
            WS["WebSocket Handler<br/>(STOMP Broker)"]
        end
        
        subgraph Services ["Business Logic"]
            AuthSvc["Auth & User Service"]
            LogSvc["Telemetry & Log Service"]
            AISvc["AI Integration Service"]
        end
        
        subgraph Data Access ["Repositories"]
            JPA["Spring Data JPA"]
        end
    end
    
    subgraph External Services ["External Providers"]
        DB[("MySQL Database")]
        LLM["AI Provider API<br/>(e.g., OpenAI)"]
        SMTP["SMTP Mail Server"]
    end

    SDK -->|"Async HTTP Posts"| API
    SEC --> Controllers
    API --> Services
    WS --> Services
    
    AuthSvc --> JPA
    LogSvc --> JPA
    LogSvc --> WS
    AISvc --> LLM
    AuthSvc --> SMTP
    
    JPA <--> DB
~~~
 
---
 
## 🚀 Getting Started
 
### Prerequisites
 
- ☕ JDK 17 or higher
- 📦 Apache Maven 3.9+
- 🐬 MySQL 8 server running

### Installation & Setup
 
1. **Clone the repository**
~~~bash
git clone https://github.com/ByteCodeLK/LogIQ-Backend.git
cd LogIQ-Backend
~~~
 
2. **Setup the Database**
Log into your MySQL server and create the database:
~~~sql
CREATE DATABASE logiq_db;
~~~
 
3. **Configure Environment Properties**
Navigate to `src/main/resources/application.properties` and configure your credentials:
~~~properties
# Server Port
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/logiq_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update

# JWT Security Key
logiq.app.jwtSecret=YOUR_SUPER_SECRET_JWT_KEY_HERE
logiq.app.jwtExpirationMs=86400000

# AI Provider API Key (If applicable)
logiq.ai.apiKey=YOUR_AI_API_KEY
~~~
 
4. **Build and Run**
Compile the project and start the Spring Boot server using Maven:
~~~bash
mvn clean install
mvn spring-boot:run
~~~
The backend will start running on `http://localhost:8080`.

---
 
## 👨‍💻 Author
 
**Prasanna Lakshan**
 
- 🌐 Portfolio: [https://prasanna-lakshan.vercel.app/](https://prasanna-lakshan.vercel.app/)
- 💼 LinkedIn: [https://www.linkedin.com/in/prasannalakshan](https://www.linkedin.com/in/prasannalakshan)
- 🐙 GitHub: [https://github.com/shan2003034](https://github.com/shan2003034)

</div>
