package com.sms.repository;

import com.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find student by enrollment number
    Optional<Student> findByEnrollmentNo(String enrollmentNo);

    // Check if student with enrollment number exists
    boolean existsByEnrollmentNo(String enrollmentNo);

    // Search students by name or course with pagination
    Page<Student> findByNameContainingIgnoreCaseOrCourseContainingIgnoreCase(String name, String course, Pageable pageable);
}
