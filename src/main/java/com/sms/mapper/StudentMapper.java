package com.sms.mapper;

import com.sms.dto.StudentDto;
import com.sms.model.Student;
import org.springframework.stereotype.Component;

/**
 * Mapper component to convert between Student entity and StudentDto.
 */
@Component
public class StudentMapper {

    /**
     * Map Student Entity to StudentDto.
     */
    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        return StudentDto.builder()
                .id(student.getId())
                .enrollmentNo(student.getEnrollmentNo())
                .name(student.getName())
                .birthDate(student.getBirthDate())
                .email(student.getEmail())
                .course(student.getCourse())
                .build();
    }

    /**
     * Map StudentDto to Student Entity.
     */
    public Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }
        return Student.builder()
                .id(dto.getId())
                .enrollmentNo(dto.getEnrollmentNo())
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .course(dto.getCourse())
                .build();
    }
}
