<div align="center">
<br>
<a href="https://linkedin.com/in/anton-solianyk-906453221">
  <img src="https://img.shields.io/badge/🔗%20LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn">
</a>

  <a href="mailto:solyanicks@gmail.com">
    <img src="https://img.shields.io/badge/Email-solyanicks%40gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Gmail">
  </a>
</div>


<h1 align="center">
🔐 JWT Spring Security template
</h1>

<p align="center">
Spring Boot template with core Spring JWT-based Security setup. 
The implementation includes <strong>access</strong> and <strong>refresh</strong> tokens. Template utilizes modern 
libraries and approaches to securing application.
</p>

---

<p align="center">
  <a href="#-technologies-used" ><strong>Technologies used</strong></a> •
  <a href="#-demo" ><strong>Demo</strong></a> •
  <a href="#-how-to-use" ><strong>How to use</strong></a> 
</p>

---

## Technologies used
Spring Boot — core backend framework.  
Spring Security — authentication and authorization system.  
Spring Data JPA + Hibernate — ORM and data persistence.    
MySQL — relational database.  
Flyway — data migration tool.  
JJWT — jwt encoding and decoding.  
Gradle — build automation tool.  
Docker & Docker Compose — containerization and orchestration of Spring Boot app and MySQL instance.

---

## Demo
![Image](https://github.com/user-attachments/assets/d96105f4-4fcb-4968-a1b4-7a7af31acf4a)

## How to use

---
* Have Docker installed on your local machine
---
* Clone the repository to your local machine 
```bash
git clone https://github.com/antonio-backnotfront/security-jwt-template.git
```
---
* Create __.env__ file in the root folder (on the same level as build.gradle) following the structure specified 
in <a href="https://github.com/antonio-backnotfront/security-jwt-template/blob/main/.env.example">__.env.example__</a> file:
```
security-jwt-template/
├── .env.example  
```
---

- Run docker compose
```bash
docker compose up --build
```
---

- Access API endpoints under localhost:8080 and MySQL instance under localhost:3306.
---
- Authentication is performed via `/api/auth/login` and `/api/auth/register`.
---
- Access tokens are required for secured endpoints via the `Authorization: Bearer <token>` header.
--- 
- To refresh tokens, use the `/api/auth/refresh` endpoint with your refresh token inside `Authorization: Bearer <token>` header.
---


Copyright (c) 2025 Anton Solianyk