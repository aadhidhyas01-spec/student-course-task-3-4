# Student Course Tasks 3 & 4 — Spring Boot code modules

This repository contains two sets of Java/Spring Boot controller, entity and repository snippets. It is **not a complete standalone Spring Boot application**; it is intended to be copied into an existing project that already defines `Student`, `Course`, `StudentRepository` and `CourseRepository`.

## Task 3 — Course enrolment

REST endpoints:
- `POST /enrollments`
- `GET /enrollments`
- `GET /enrollments/{id}`
- `PUT /enrollments/{id}`
- `DELETE /enrollments/{id}`

The controller checks that the student and course exist and prevents duplicate enrolment of the same student in the same course.

Example request:
```json
{
  "studentId": 1,
  "courseId": 101,
  "enrollmentDate": "2026-09-17",
  "status": "ACTIVE"
}
```

## Task 4 — Student marks

REST endpoints:
- `POST /marks`
- `GET /marks`
- `GET /marks/{id}`
- `PUT /marks/{id}`
- `DELETE /marks/{id}`

The controller validates the student and course references and checks that marks are between zero and total marks.

Example request:
```json
{
  "studentId": 1,
  "courseId": 101,
  "examName": "Internal 1",
  "marks": 78,
  "totalMarks": 100
}
```

## Required dependencies and existing classes

The target Spring Boot project must include:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `StudentRepository extends JpaRepository<Student, Long>`
- `CourseRepository extends JpaRepository<Course, Long>`

The Java package in the provided source is `com.example.demo`. Change package and import declarations if your project uses a different package.

## Postman testing

Test valid create requests, missing student/course IDs, duplicate enrolments, invalid marks, GET all, GET by ID, PUT and DELETE. The detailed examples are in `student-course-tasks-3-4/README.txt`.

## Important

This repository does not include a `pom.xml`, a complete Spring Boot application entry point or the Student/Course classes it depends on. Add it to your existing project before attempting to run it.
