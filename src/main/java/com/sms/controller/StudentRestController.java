package com.sms.controller;

import com.sms.dto.StudentDto;
import com.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    @Autowired
    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Get all students (with optional sorting and searching)
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Use PageRequest to fetch all records sorted
        PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        
        List<StudentDto> students;
        if (search != null && !search.trim().isEmpty()) {
            students = studentService.searchStudents(search, pageable).getContent();
        } else {
            students = studentService.getAllStudents(pageable).getContent();
        }
        return ResponseEntity.ok(students);
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get student by enrollment number
    @GetMapping("/enrollment/{enrollmentNo}")
    public ResponseEntity<StudentDto> getStudentByEnrollmentNo(@PathVariable String enrollmentNo) {
        return studentService.getStudentByEnrollmentNo(enrollmentNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Search students
    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> searchStudents(@RequestParam String query) {
        List<StudentDto> students = studentService.searchStudents(query);
        return ResponseEntity.ok(students);
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDto student) {
        try {
            StudentDto savedStudent = studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("enrollmentNo", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Update student details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDetails) {
        try {
            StudentDto updatedStudent = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("enrollmentNo", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
