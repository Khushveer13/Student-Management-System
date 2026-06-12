package com.sms.service;

import com.sms.dto.StudentDto;
import com.sms.mapper.StudentMapper;
import com.sms.model.Student;
import com.sms.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Layer containing business logic for Student entity operations.
 * Operates on StudentDto objects and maps them to database Entity structures.
 */
@Service
@Transactional
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Get all students.
     */
    public List<StudentDto> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all students paginated.
     */
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        log.info("Fetching paginated students: {}", pageable);
        return studentRepository.findAll(pageable)
                .map(studentMapper::toDto);
    }

    /**
     * Get student by database ID.
     */
    public Optional<StudentDto> getStudentById(Long id) {
        log.info("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .map(studentMapper::toDto);
    }

    /**
     * Get student by unique enrollment number.
     */
    public Optional<StudentDto> getStudentByEnrollmentNo(String enrollmentNo) {
        log.info("Fetching student by enrollment number: {}", enrollmentNo);
        return studentRepository.findByEnrollmentNo(enrollmentNo)
                .map(studentMapper::toDto);
    }

    /**
     * Register a new student record.
     */
    public StudentDto addStudent(StudentDto studentDto) {
        log.info("Attempting to add student: {}", studentDto.getEnrollmentNo());
        if (studentDto.getId() == null && studentRepository.existsByEnrollmentNo(studentDto.getEnrollmentNo())) {
            log.error("Add student failed: Enrollment number {} already exists", studentDto.getEnrollmentNo());
            throw new IllegalArgumentException("Student with enrollment number " + studentDto.getEnrollmentNo() + " already exists.");
        }
        
        Student studentEntity = studentMapper.toEntity(studentDto);
        Student savedStudent = studentRepository.save(studentEntity);
        log.info("Student successfully added with ID: {}", savedStudent.getId());
        return studentMapper.toDto(savedStudent);
    }

    /**
     * Update existing student details.
     */
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        log.info("Attempting to update student details for ID: {}", id);
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update student failed: Student not found with ID {}", id);
                    return new IllegalArgumentException("Student not found with id: " + id);
                });

        // Check if enrollment number is being changed and if new enrollment number already exists
        if (!existingStudent.getEnrollmentNo().equals(studentDto.getEnrollmentNo()) &&
                studentRepository.existsByEnrollmentNo(studentDto.getEnrollmentNo())) {
            log.error("Update student failed: New enrollment number {} is already in use", studentDto.getEnrollmentNo());
            throw new IllegalArgumentException("Enrollment number " + studentDto.getEnrollmentNo() + " is already in use.");
        }

        existingStudent.setEnrollmentNo(studentDto.getEnrollmentNo());
        existingStudent.setName(studentDto.getName());
        existingStudent.setBirthDate(studentDto.getBirthDate());
        existingStudent.setEmail(studentDto.getEmail());
        existingStudent.setCourse(studentDto.getCourse());

        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Student details successfully updated for ID: {}", id);
        return studentMapper.toDto(updatedStudent);
    }

    /**
     * Delete a student by database ID.
     */
    public void deleteStudent(Long id) {
        log.warn("Attempting to delete student with ID: {}", id);
        if (!studentRepository.existsById(id)) {
            log.error("Delete student failed: Student not found with ID {}", id);
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
        log.info("Student successfully deleted for ID: {}", id);
    }

    /**
     * Search students by keyword (Name or Course).
     */
    public List<StudentDto> searchStudents(String query) {
        log.info("Searching students by query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.findByNameContainingIgnoreCaseOrCourseContainingIgnoreCase(query, query, Pageable.unpaged())
                .getContent().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Search students by keyword paginated.
     */
    public Page<StudentDto> searchStudents(String query, Pageable pageable) {
        log.info("Searching paginated students by query: {} with config: {}", query, pageable);
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents(pageable);
        }
        return studentRepository.findByNameContainingIgnoreCaseOrCourseContainingIgnoreCase(query, query, pageable)
                .map(studentMapper::toDto);
    }
}
