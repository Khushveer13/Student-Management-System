package com.sms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for Student request and response payloads.
 * Decouples the REST API contract from the JPA database entity structure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudentDto {

    private Long id;

    @NotBlank(message = "Enrollment number is required")
    private String enrollmentNo;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Course is required")
    private String course;
}
