TASK 3 + TASK 4 - SPRING BOOT MODULES

Package used in this folder:
com.example.demo

IMPORTANT:
1. Copy the entity, repository and controller files into your existing Spring Boot project.
2. Change "com.example.demo" in the package/import lines to your project's package name if different.
3. These controllers expect these existing classes in your project:
   - StudentRepository extends JpaRepository<Student, Long>
   - CourseRepository extends JpaRepository<Course, Long>
4. Make sure spring-boot-starter-web and spring-boot-starter-data-jpa are already in pom.xml.

FILES
src/main/java/com/example/demo/entity/Enrollment.java
src/main/java/com/example/demo/entity/EnrollmentStatus.java
src/main/java/com/example/demo/repository/EnrollmentRepository.java
src/main/java/com/example/demo/controller/EnrollmentController.java
src/main/java/com/example/demo/entity/Marks.java
src/main/java/com/example/demo/repository/MarksRepository.java
src/main/java/com/example/demo/controller/MarksController.java

TASK 3 - ENROLLMENT
POST /enrollments
GET /enrollments
GET /enrollments/{id}
PUT /enrollments/{id}
DELETE /enrollments/{id}

Example POST JSON:
{
  "studentId": 1,
  "courseId": 101,
  "enrollmentDate": "2026-09-17",
  "status": "ACTIVE"
}

Validation messages:
Student not found
Course not found
Student already enrolled

TASK 4 - MARKS
POST /marks
GET /marks
GET /marks/{id}
PUT /marks/{id}
DELETE /marks/{id}

Example POST JSON:
{
  "studentId": 1,
  "courseId": 101,
  "examName": "Internal 1",
  "marks": 78,
  "totalMarks": 100
}

Validation messages:
Student not found
Course not found
Marks cannot be negative
Marks cannot be greater than total marks

POSTMAN QUICK TESTS
1. Valid enrollment -> POST /enrollments
2. Invalid student -> studentId 99999
3. Invalid course -> courseId 99999
4. Duplicate enrollment -> send the same studentId + courseId again
5. Get all -> GET /enrollments
6. Update -> PUT /enrollments/1
7. Delete -> DELETE /enrollments/1
8. Valid marks -> POST /marks
9. Invalid marks -> marks 105, totalMarks 100
10. Negative marks -> marks -5
11. Get all -> GET /marks
12. Update -> PUT /marks/1
13. Delete -> DELETE /marks/1
