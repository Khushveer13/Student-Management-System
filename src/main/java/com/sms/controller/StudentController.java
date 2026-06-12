package com.sms.controller;

import com.sms.dto.StudentDto;
import com.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // List all students & search query (with pagination and sorting)
    @GetMapping
    public String listStudents(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                               @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                               Model model) {
        Page<StudentDto> studentPage;
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.trim().isEmpty()) {
            studentPage = studentService.searchStudents(search, pageable);
            model.addAttribute("searchQuery", search);
        } else {
            studentPage = studentService.getAllStudents(pageable);
        }

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        model.addAttribute("activePage", "students");
        model.addAttribute("pageTitle", "Students Database");
        return "students/list";
    }

    // Show add student form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new StudentDto());
        model.addAttribute("activePage", "add-student");
        model.addAttribute("pageTitle", "Register Student");
        return "students/form";
    }

    // Process add student request
    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") StudentDto student,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("activePage", "add-student");
            model.addAttribute("pageTitle", "Register Student");
            return "students/form";
        }

        try {
            studentService.addStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student registered successfully!");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            result.rejectValue("enrollmentNo", "error.student", e.getMessage());
            model.addAttribute("activePage", "add-student");
            model.addAttribute("pageTitle", "Register Student");
            return "students/form";
        }
    }

    // Show edit student form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            StudentDto student = studentService.getStudentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
            model.addAttribute("student", student);
            model.addAttribute("activePage", "students");
            model.addAttribute("pageTitle", "Edit Student Record");
            return "students/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students";
        }
    }

    // Process edit student request
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("student") StudentDto student,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("activePage", "students");
            model.addAttribute("pageTitle", "Edit Student Record");
            return "students/form";
        }

        try {
            studentService.updateStudent(id, student);
            redirectAttributes.addFlashAttribute("successMessage", "Student record updated successfully!");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            result.rejectValue("enrollmentNo", "error.student", e.getMessage());
            model.addAttribute("activePage", "students");
            model.addAttribute("pageTitle", "Edit Student Record");
            return "students/form";
        }
    }

    // Process delete student request
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student record deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/students";
    }
}
