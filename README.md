# 🚀 TasktrackeAPI

A REST API built with **Spring Boot** for managing tasks: creation, completion, and listing.

![Architecture Diagram](./arch.png)

---

## 📘 Key Features

- ✅ **GET /tasks** – Returns all tasks
- 📝 **POST /tasks** – Creates a new task
- ☑️ **PUT /tasks/{id}/complete** – Marks a task as completed

---

## 🧩 Project Structure

```
tasktrackeAPI/
├── src/
│   ├── main/java/com/tasktracker/
│   │   ├── TaskTrackerApplication.java
│   │   ├── controller/TaskController.java
│   │   ├── service/TaskService + TaskServiceImpl
│   │   ├── repository/TaskRepository.java
│   │   ├── model/Task.java
│   │   └── dto/TaskCreateDto, TaskResponseDto
│   └── resources/application.properties
├── pom.xml
└── README.md
```

---

## ▶️ Getting Started

### Prerequisites

- Java 17
- Maven

### Setup

```bash
git clone https://github.com/mirko1987/tasktrackeAPI.git
cd tasktrackeAPI
mvn clean install
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/tasks`

---

## 📊 API Examples

### ➕ Create a Task

```http
POST /tasks
Content-Type: application/json

{
  "title": "Study Spring Boot",
  "description": "Follow the official guide"
}
```

### ✅ Complete a Task

```http
PUT /tasks/1/complete
```

### 📄 Get All Tasks

```http
GET /tasks
```

---

## 🧪 Testing

### Unit Tests
Run all unit tests with:

```bash
mvn test
```

### Manual API Testing

Start the application first:
```bash
mvn spring-boot:run
```

#### ✅ **Core API Tests**

**1. Get all tasks (empty state):**
```bash
curl -s http://localhost:8080/tasks | python3 -m json.tool
# Expected: []
```

**2. Create a new task:**
```bash
curl -s -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Test Task", "description": "Testing the API"}' | python3 -m json.tool
# Expected: {"id": 1, "title": "Test Task", "description": "Testing the API", "completed": false}
```

**3. Get all tasks (with data):**
```bash
curl -s http://localhost:8080/tasks | python3 -m json.tool
# Expected: [{"id": 1, "title": "Test Task", "description": "Testing the API", "completed": false}]
```

**4. Complete a task:**
```bash
curl -s -X PUT http://localhost:8080/tasks/1/complete | python3 -m json.tool
# Expected: {"id": 1, "title": "Test Task", "description": "Testing the API", "completed": true}
```

#### ⚠️ **Error Handling Tests**

**1. Invalid input (empty title) - Returns 400:**
```bash
curl -s -w "HTTP Status: %{http_code}\n" -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "", "description": "Should fail"}'
# Expected: HTTP Status: 400
```

**2. Non-existent task - Returns 404:**
```bash
curl -s -w "HTTP Status: %{http_code}\n" -X PUT http://localhost:8080/tasks/999/complete
# Expected: HTTP Status: 404
```

#### 🌐 **CORS Testing**

**Test CORS preflight request:**
```bash
curl -s -X OPTIONS \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -v http://localhost:8080/tasks
# Expected: Access-Control-Allow-Origin: *
```

---