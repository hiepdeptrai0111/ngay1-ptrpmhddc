package com.example.ngay1ptrpmhddc.controller;

import com.example.ngay1ptrpmhddc.entity.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @GetMapping
    public List<Course> getCourses() {
        Course course1 = new Course(1L, "Lập trình Java cơ bản", 3, 40, 12);
        Course course2 = new Course(2L, "Cơ sở dữ liệu", 4, 35, 0);
        return Arrays.asList(course1, course2);
    }
}
