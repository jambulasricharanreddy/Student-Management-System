# 🎓 Smart Student Hub — Student Management System

A modern, full-featured **Student Management System** web application built with React + Spring Boot.

## ✨ Features

- 📊 **Dashboard** — Real-time stats, charts (attendance, dept distribution)
- 👨‍🎓 **Student Management** — Add, edit, search, filter students
- 👩‍🏫 **Lecturer Management** — Faculty records and assignment
- 📚 **Course Management** — Course catalog and enrollment
- ✅ **Attendance Tracking** — Manual + QR-code based attendance
- 📝 **Marks & Results** — Grades, grade letters, subject-wise marks
- 📅 **Timetable** — Weekly class schedule
- 📋 **Assignments** — Post and track submissions
- 💼 **Internships** — Student internship records
- 💰 **Fee Management** — Payment tracking and dues
- 📖 **Library** — Book inventory and loan management
- 🎉 **Events** — Campus events and registrations
- 🗣️ **Grievances** — Student complaint resolution
- 🏅 **Certificates** — Bonafide, transfer, character certs
- ⚙️ **Settings** — Profile, password, notification preferences

## 🛠️ Tech Stack

| Layer     | Technology                                  |
|-----------|---------------------------------------------|
| Frontend  | React 19, Vite, React Router v7, Recharts   |
| Backend   | Spring Boot 3.3.4, Java 21, Spring Security |
| Database  | MySQL 8+                                    |
| Auth      | JWT (JJWT 0.12.6)                           |
| Docs      | SpringDoc OpenAPI / Swagger UI              |

## 🚀 Getting Started

### Frontend (works standalone with mock data)
```bash
cd frontend
npm install
npm run dev
# → http://localhost:5173
```
Login: `admin@university.edu` / `admin123`

### Backend (requires MySQL)
1. Create a MySQL database:
   ```sql
   CREATE DATABASE smart_student_hub;
   ```
2. Import the schema:
   ```bash
   mysql -u root -p smart_student_hub < database/schema.sql
   ```
3. Run the backend:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   # → http://localhost:8080
   ```

## 📁 Project Structure

```
Student Management System/
├── frontend/       # Vite + React 19 app
├── backend/        # Spring Boot REST API
└── database/       # MySQL schema (schema.sql)
```

## 📄 API Documentation
Once backend is running: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
