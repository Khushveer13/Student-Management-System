package com.sms.controller;

import com.sms.dto.StudentDto;
import com.sms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final StudentService studentService;

    @Autowired
    public DashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        List<StudentDto> allStudents = studentService.getAllStudents();

        // 1. Total Enrolled Students
        long totalStudents = allStudents.size();

        // 2. Total Unique Courses
        long totalCourses = allStudents.stream()
                .map(StudentDto::getCourse)
                .distinct()
                .count();

        // 3. Average Age calculation
        LocalDate now = LocalDate.now();
        double averageAge = allStudents.stream()
                .mapToInt(student -> Period.between(student.getBirthDate(), now).getYears())
                .average()
                .orElse(0.0);

        // 4. Course Distribution for Chart.js (Doughnut Chart)
        Map<String, Long> courseDistribution = allStudents.stream()
                .collect(Collectors.groupingBy(StudentDto::getCourse, Collectors.counting()));

        // 5. Recent 5 Enrolled Students (sorted by ID descending)
        List<StudentDto> recentStudents = allStudents.stream()
                .sorted(Comparator.comparing(StudentDto::getId).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Model attributes
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("pageTitle", "Dashboard Overview");
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("averageAge", averageAge);
        model.addAttribute("courseDistribution", courseDistribution);
        model.addAttribute("recentStudents", recentStudents);

        return "dashboard";
    }
}
