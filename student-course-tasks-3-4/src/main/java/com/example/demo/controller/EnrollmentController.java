package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository,
                                StudentRepository studentRepository,
                                CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        return enrollmentRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "Enrollment not found")));
    }

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody Enrollment enrollment) {
        if (enrollment.getStudentId() == null ||
                !studentRepository.existsById(enrollment.getStudentId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student not found"));
        }

        if (enrollment.getCourseId() == null ||
                !courseRepository.existsById(enrollment.getCourseId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Course not found"));
        }

        if (enrollmentRepository
                .findByStudentIdAndCourseId(enrollment.getStudentId(), enrollment.getCourseId())
                .isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student already enrolled"));
        }

        return ResponseEntity.ok(enrollmentRepository.save(enrollment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnrollment(@PathVariable Long id,
                                              @RequestBody Enrollment updatedEnrollment) {
        return enrollmentRepository.findById(id).map(existing -> {
            if (updatedEnrollment.getStudentId() != null &&
                    !studentRepository.existsById(updatedEnrollment.getStudentId())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Student not found"));
            }

            if (updatedEnrollment.getCourseId() != null &&
                    !courseRepository.existsById(updatedEnrollment.getCourseId())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Course not found"));
            }

            if (updatedEnrollment.getStudentId() != null &&
                    updatedEnrollment.getCourseId() != null &&
                    ( !updatedEnrollment.getStudentId().equals(existing.getStudentId()) ||
                      !updatedEnrollment.getCourseId().equals(existing.getCourseId()) ) &&
                    enrollmentRepository
                            .findByStudentIdAndCourseId(updatedEnrollment.getStudentId(), updatedEnrollment.getCourseId())
                            .isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Student already enrolled"));
            }

            if (updatedEnrollment.getStudentId() != null) {
                existing.setStudentId(updatedEnrollment.getStudentId());
            }
            if (updatedEnrollment.getCourseId() != null) {
                existing.setCourseId(updatedEnrollment.getCourseId());
            }
            if (updatedEnrollment.getEnrollmentDate() != null) {
                existing.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
            }
            if (updatedEnrollment.getStatus() != null) {
                existing.setStatus(updatedEnrollment.getStatus());
            }

            return ResponseEntity.ok(enrollmentRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.status(404)
                .body(Map.of("message", "Enrollment not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id) {
        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Enrollment not found"));
        }

        enrollmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Enrollment deleted successfully"));
    }
}
