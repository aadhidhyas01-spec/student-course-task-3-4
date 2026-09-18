package com.example.demo.controller;

import com.example.demo.entity.Marks;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.MarksRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/marks")
public class MarksController {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public MarksController(MarksRepository marksRepository,
                           StudentRepository studentRepository,
                           CourseRepository courseRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMarksById(@PathVariable Long id) {
        return marksRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "Marks not found")));
    }

    @PostMapping
    public ResponseEntity<?> createMarks(@RequestBody Marks marks) {
        if (marks.getStudentId() == null ||
                !studentRepository.existsById(marks.getStudentId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Student not found"));
        }

        if (marks.getCourseId() == null ||
                !courseRepository.existsById(marks.getCourseId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Course not found"));
        }

        if (marks.getMarks() == null || marks.getMarks() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Marks cannot be negative"));
        }

        if (marks.getTotalMarks() == null || marks.getTotalMarks() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Total marks must be non-negative"));
        }

        if (marks.getMarks() > marks.getTotalMarks()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Marks cannot be greater than total marks"));
        }

        return ResponseEntity.ok(marksRepository.save(marks));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMarks(@PathVariable Long id,
                                         @RequestBody Marks updatedMarks) {
        return marksRepository.findById(id).map(existing -> {
            Long studentId = updatedMarks.getStudentId() != null
                    ? updatedMarks.getStudentId() : existing.getStudentId();
            Long courseId = updatedMarks.getCourseId() != null
                    ? updatedMarks.getCourseId() : existing.getCourseId();
            Integer newMarks = updatedMarks.getMarks() != null
                    ? updatedMarks.getMarks() : existing.getMarks();
            Integer newTotalMarks = updatedMarks.getTotalMarks() != null
                    ? updatedMarks.getTotalMarks() : existing.getTotalMarks();

            if (!studentRepository.existsById(studentId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Student not found"));
            }

            if (!courseRepository.existsById(courseId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Course not found"));
            }

            if (newMarks == null || newMarks < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Marks cannot be negative"));
            }

            if (newTotalMarks == null || newTotalMarks < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Total marks must be non-negative"));
            }

            if (newMarks > newTotalMarks) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Marks cannot be greater than total marks"));
            }

            existing.setStudentId(studentId);
            existing.setCourseId(courseId);
            if (updatedMarks.getExamName() != null) {
                existing.setExamName(updatedMarks.getExamName());
            }
            existing.setMarks(newMarks);
            existing.setTotalMarks(newTotalMarks);

            return ResponseEntity.ok(marksRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.status(404)
                .body(Map.of("message", "Marks not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarks(@PathVariable Long id) {
        if (!marksRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Marks not found"));
        }

        marksRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Marks deleted successfully"));
    }
}
