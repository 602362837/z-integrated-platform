# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

**语言要求: 全程使用中文进行思考和交流，包括思考过程、代码注释、提交信息等。**

## Project Overview

A centralized **User Center Management System** (一体化平台用户管理系统) providing unified user management capabilities for multiple applications. The system supports application management, organization management, platform user management, and application user management.

## Technology Stack

### Backend
- Spring Boot 3.2.5 / Java 21
- MyBatis Plus 3.5.7 (ORM)
- MySQL 8.0 / Redis 7.0
- Spring Security (BCrypt password encryption)

### Frontend
- Vue 3 + Vite 7.3.1
- Element Plus 2.13.3
- Vue Router 4

## Development Commands

### Backend (Maven)
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/user-center-1.0.0-SNAPSHOT.jar
```

Or run directly via IDE using `UserCenterApplication.java`

### Frontend
```bash
cd z-integrated-platform-web

# Install dependencies
npm install

# Development server (port 5173)
npm run dev

# Production build
npm run build
```

### Infrastructure
```bash
# Start MySQL (3306) and Redis (6379)
docker-compose up -d
```

## Architecture

Standard layered architecture:
- **Controller Layer**: REST endpoints in `src/main/java/com/platform/usercenter/controller/`
- **Service Layer**: Business logic in `service/impl/`
- **Mapper Layer**: MyBatis Plus data access in `mapper/`
- **Entity Layer**: Domain models in `entity/`

API Base URL: `http://localhost:8080/api/v1`

### Key API Endpoints
- `/apps` - Application CRUD
- `/orgs` - Organization management (tree structure)
- `/platform-users` - Platform user management
- `/app-users` - Application user management with sync/mapping

### Response Format
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## Default Credentials
- Username: `admin`
- Password: `admin123`

## Notes

- All entities use logical deletion (via `deleted` field)
- Frontend proxies `/api` requests to backend (configured in `vite.config.js`)
- E2E tests available using Playwright (`e2e-test.spec.ts`)
- Database initialization: `sql/init.sql`
