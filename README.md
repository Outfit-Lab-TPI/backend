# 🧥 OutfitLab Backend

## 🚀 Quick Start

### 1️⃣ Setup Environment Variables

```bash
# Copy environment template
cp .env.example .env

# Edit .env with your values
nano .env
```

### 2️⃣ Run with Docker (Recommended)

```bash
# Start full stack (Backend + PostgreSQL + Redis)
docker compose up --build -d

# Check status
docker compose ps

# View logs
docker compose logs -f outfitlab-backend
```

### 3️⃣ Test API

```bash
# Health check
curl http://localhost:8080/actuator/health

# Test endpoint
curl http://localhost:8080/api/users/1
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default | Example |
|---|---|---|---|
| `POSTGRES_DB` | Database name | `outfitlab` | `outfitlab` |
| `POSTGRES_USER` | App database user | - | `outfitlab_user` |
| `POSTGRES_PASSWORD` | App database password | - | `secure_pass_123` |
| `SPRING_SECURITY_USER` | Admin username | `admin` | `admin` |
| `SPRING_SECURITY_PASSWORD` | Admin password | - | `secure_admin_pass` |

### Spring Profiles

- **`default`**: Local development (no database)
- **`docker`**: Docker environment (full stack)

## 📊 Architecture

```
📤 Upload 2D Image → 🤖 AI/3D Processing → 📦 3D Model → ☁️ Storage → 🌐 API → 💻 Frontend
```

## 🛠️ Development

### Local Development (without Docker)

```bash
# Run with mock data
./mvnw spring-boot:run

# Access: http://localhost:8080
```

### Docker Development

```bash
# Rebuild after code changes
docker compose up --build

# View specific service logs
docker compose logs outfitlab-backend

# Reset everything
docker compose down -v
docker compose up --build
```

## 🔐 Security

- All sensitive data in `.env` file
- `.env` is git-ignored for security
- Use `.env.example` as template

## 📋 API Endpoints

| Endpoint | Method | Description |
|---|---|---|
| `/api/users/{id}` | GET | Get user by ID |
| `/actuator/health` | GET | Health check |
| `/actuator/metrics` | GET | Application metrics |

## 🗄️ Database

- **PostgreSQL 15** for main data
- **Redis 7** for caching and queues
- Auto-migrations with Hibernate DDL

## 📦 Tech Stack

- **Spring Boot 3.5.6** + Java 17
- **PostgreSQL 15** + **Redis 7**
- **Docker** + **Docker Compose**
- **Maven** for dependency management
- **Actuator** for monitoring

## 🐳 Docker Services

| Service | Port | Description |
|---|---|---|
| `outfitlab-backend` | 8080 | Main Spring Boot API |
| `postgres` | 5432 | PostgreSQL Database |
| `redis` | 6379 | Redis Cache/Queue |

---

**Status**: ✅ Listo para el deploy rey