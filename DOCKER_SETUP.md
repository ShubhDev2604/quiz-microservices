# Docker Setup Guide

## Prerequisites

Before running `docker-compose up`, ensure Docker Desktop is running.

## Common Issues and Solutions

### Issue 1: Docker Desktop Not Running

**Error Message:**
```
unable to get image 'quiz-microservices-service-registry': error during connect: Get "http://%2F%2F.%2Fpipe%2FdockerDesktopLinuxEngine/v1.51/images/...": open //./pipe/dockerDesktopLinuxEngine: The system cannot find the file specified.
```

**Solution:**
1. **Start Docker Desktop**
   - Open Docker Desktop application
   - Wait for it to fully start (whale icon should be steady, not animated)
   - You should see "Docker Desktop is running" in the system tray

2. **Verify Docker is Running**
   ```powershell
   docker ps
   ```
   If this works, Docker is running correctly.

3. **If Docker Desktop Won't Start:**
   - Restart Docker Desktop
   - Check Windows WSL 2 is installed (Docker Desktop requires WSL 2)
   - Restart your computer if needed

### Issue 2: Docker Compose Version Warning

**Warning Message:**
```
the attribute `version` is obsolete, it will be ignored
```

**Status:** ✅ Fixed - The `version` field has been removed from `docker-compose.yml` as it's no longer needed in newer Docker Compose versions.

### Issue 3: Port Conflicts

If you get port already in use errors:

**Check what's using the ports:**
```powershell
# Check port 5432 (PostgreSQL)
netstat -ano | findstr :5432

# Check port 8761 (Eureka)
netstat -ano | findstr :8761

# Check port 9411 (Zipkin)
netstat -ano | findstr :9411
```

**Kill the process:**
```powershell
taskkill /PID <PID_NUMBER> /F
```

### Issue 4: WSL 2 Not Installed

Docker Desktop on Windows requires WSL 2.

**Check if WSL 2 is installed:**
```powershell
wsl --list --verbose
```

**Install WSL 2 if needed:**
```powershell
wsl --install
```

Then restart your computer and start Docker Desktop again.

## Step-by-Step Setup

1. **Install Docker Desktop**
   - Download from: https://www.docker.com/products/docker-desktop
   - Install and restart your computer

2. **Start Docker Desktop**
   - Launch Docker Desktop from Start Menu
   - Wait for it to fully start (whale icon steady)

3. **Verify Installation**
   ```powershell
   docker --version
   docker-compose --version
   docker ps
   ```

4. **Navigate to Project Directory**
   ```powershell
   cd "D:\Personal Projects\Microservices\quiz-microservices"
   ```

5. **Build and Start Services**
   ```powershell
   docker-compose up --build
   ```

6. **Check Services are Running**
   ```powershell
   docker-compose ps
   ```

## Useful Docker Commands

```powershell
# View running containers
docker ps

# View all containers (including stopped)
docker ps -a

# View logs for a specific service
docker-compose logs quiz-service
docker-compose logs question-service

# View logs for all services
docker-compose logs

# Stop all services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v

# Rebuild specific service
docker-compose build quiz-service

# Restart a specific service
docker-compose restart quiz-service
```

## Troubleshooting Docker Desktop

### Docker Desktop Won't Start

1. **Check WSL 2:**
   ```powershell
   wsl --status
   ```

2. **Update WSL:**
   ```powershell
   wsl --update
   ```

3. **Restart WSL:**
   ```powershell
   wsl --shutdown
   ```

4. **Restart Docker Desktop**

### Docker Desktop Keeps Crashing

1. Increase Docker Desktop memory allocation:
   - Open Docker Desktop Settings
   - Go to Resources → Advanced
   - Increase Memory (recommended: at least 4GB)
   - Apply & Restart

2. Check Windows updates
3. Restart your computer

### Cannot Connect to Docker Daemon

1. Ensure Docker Desktop is running
2. Restart Docker Desktop
3. Run as Administrator if needed

## Next Steps

Once Docker is running successfully:

1. Run `docker-compose up --build`
2. Wait for all services to start (check logs)
3. Access:
   - Eureka: http://localhost:8761
   - Zipkin: http://localhost:9411
   - API Gateway: http://localhost:8765

For more details, see [README.md](README.md) and [QUICKSTART.md](QUICKSTART.md)
