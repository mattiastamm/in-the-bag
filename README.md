## 🏕️ The Story Behind *InTheBag*

Disc golf is a rapidly growing sport, yet many of the digital tools available to players focus solely on tracking discs rather than helping them optimize their collections. Existing platforms often lack personalized recommendations, creating a gap for users—particularly beginners—who need guidance when building a well-rounded bag.

**InTheBag** was developed as part of a thesis project to address this need. The application enables players to organize their disc inventory and receive tailored suggestions for improving their setups. It combines standard disc management features with a rule-based Suggestion Engine, which leverages flight number logic and has been validated through expert input.

The system was built using React, Spring Boot, and PostgreSQL, and deployed using Docker and AWS EC2. It was tested through both unit and exploratory methods to ensure functional reliability. The final result is a web application that not only meets the expectations of existing disc golf tools but also introduces meaningful innovation to the disc golf software ecosystem.

**The application is live and accessible at:**  
[http://13.61.174.204/](http://13.61.174.204/)



# Local Development Setup Guide for `in-the-bag`

This guide walks you through setting up your environment to build and run the project locally using Docker.

---

## Setup: Install Required Tools

> 💡 **Note:** If any of the tools below are already installed on your system, you can skip the corresponding step. Ensure each tool is properly configured and accessible from the command line before proceeding.

These tools are necessary to build, run, and containerize the project.

### 1. Install Docker (includes Docker Compose)
Used to build and run containers for backend, frontend, and database.

📦 [Download Docker Desktop](https://www.docker.com/products/docker-desktop)

---

### 2. Install Git
Required to clone the project repository from GitHub.

📦 [Download Git](https://git-scm.com/download/win)

---

### 3. Install Java 17 (JDK)
Required to compile the Java backend into a `.jar` file.

📦 [Download Temurin (Java 17)](https://adoptium.net/en-GB/temurin/releases/)

---

### Verify Tool Installation (Optional)

Run the following commands to confirm each tool is correctly installed:

```bash
docker --version
git --version
java -version
```

---

## Project Setup: Clone and Build the Backend

### 4. Clone the repository and navigate into it
Open Git Bash, CMD, or PowerShell and run:

```bash
git clone https://github.com/mattiastamm/in-the-bag.git
cd in-the-bag
```

Ensure you're in the folder that contains `docker-compose.yml`.

---

### 5. Build the backend `.jar` file
Compile the Java backend without running tests:

```bash
cd in-the-bag-backend
./mvnw clean package -DskipTests
cd ..
```

This creates the `.jar` file in `target/`, which Docker will use when building the backend image.

---

## Container Setup: Build and Run the Application

### 6. Start all containers (frontend, backend, DB, etc.)

```bash
docker-compose up --build
```

This builds Docker images and runs containers defined in `docker-compose.yml`.

---

### 7. Access the frontend in your browser
Once the containers are running, open your browser and go to:

```text
http://localhost:3000
```

The frontend application should now be live and accessible on port `3000`.

---

## Cleanup: Stop All Running Containers

### 8. Shut down everything cleanly
When you're done, stop all services and remove the containers:

```bash
docker-compose down
```

This ensures all containers and related networks are properly cleaned up.

---

## You're All Set!
You now have a fully running development environment using Docker.
