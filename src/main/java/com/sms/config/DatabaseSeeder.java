package com.sms.config;

import com.sms.model.Student;
import com.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final StudentRepository studentRepository;

    @Autowired
    public DatabaseSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.count() == 0) {
            Student s1 = Student.builder()
                    .enrollmentNo("ENR-2026-001")
                    .name("Amelia Bennett")
                    .birthDate(LocalDate.of(2004, 3, 12))
                    .email("amelia.b@edusphere.com")
                    .course("Computer Science")
                    .build();

            Student s2 = Student.builder()
                    .enrollmentNo("ENR-2026-002")
                    .name("Lucas Chen")
                    .birthDate(LocalDate.of(2003, 8, 25))
                    .email("lucas.c@edusphere.com")
                    .course("Data Science & AI")
                    .build();

            Student s3 = Student.builder()
                    .enrollmentNo("ENR-2026-003")
                    .name("Sophia Martinez")
                    .birthDate(LocalDate.of(2005, 11, 5))
                    .email("sophia.m@edusphere.com")
                    .course("Cyber Security")
                    .build();

            Student s4 = Student.builder()
                    .enrollmentNo("ENR-2026-004")
                    .name("Marcus Thompson")
                    .birthDate(LocalDate.of(2004, 5, 19))
                    .email("marcus.t@edusphere.com")
                    .course("Software Engineering")
                    .build();

            Student s5 = Student.builder()
                    .enrollmentNo("ENR-2026-005")
                    .name("Emily Watson")
                    .birthDate(LocalDate.of(2003, 1, 30))
                    .email("emily.w@edusphere.com")
                    .course("Business Administration")
                    .build();

            Student s6 = Student.builder()
                    .enrollmentNo("ENR-2026-006")
                    .name("Aaron Vance")
                    .birthDate(LocalDate.of(2004, 10, 12))
                    .email("aaron.v@edusphere.com")
                    .course("Computer Science")
                    .build();

            studentRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6));
            log.info("Demo data successfully seeded into database.");
        }
    }
}
