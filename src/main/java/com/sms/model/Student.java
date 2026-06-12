package com.sms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * JPA Entity representing a Student record in the database.
 * Uses Lombok annotations to eliminate boilerplates (getters, setters, constructors).
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_no", unique = true, nullable = false)
    private String enrollmentNo;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String course;
}
