package com.example.ngay1ptrpmhddc.service;

import com.example.ngay1ptrpmhddc.dto.CourseDTO;
import com.example.ngay1ptrpmhddc.entity.Course;
import com.example.ngay1ptrpmhddc.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private CourseDTO courseDTO;
    private Course course;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO(1L, "Java", 3, 40, null);
        course = new Course(1L, "Java", 3, 40, 40);
    }

    @Test
    void testCreateCourseSuccess() {
        when(courseRepository.existsByTenMonHocIgnoreCase("Java")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDTO result = courseService.create(courseDTO);

        assertNotNull(result);
        assertEquals("Java", result.getTenMonHoc());
        assertEquals(40, result.getSoChoConLai()); // Kiểm tra soChoConLai = soChoToiDa
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourseDuplicateName() {
        when(courseRepository.existsByTenMonHocIgnoreCase("Java")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            courseService.create(courseDTO);
        });

        assertEquals("Ten mon hoc da ton tai: Java", exception.getMessage());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testUpdateCourseSuccess() {
        Course existingCourse = new Course(1L, "Java Old", 3, 40, 20); // soChoConLai dang la 20
        CourseDTO updateDto = new CourseDTO(1L, "Java New", 4, 50, null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.existsByTenMonHocIgnoreCaseAndIdNot("Java New", 1L)).thenReturn(false);
        
        Course updatedCourse = new Course(1L, "Java New", 4, 50, 20);
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDTO result = courseService.update(1L, updateDto);

        assertNotNull(result);
        assertEquals("Java New", result.getTenMonHoc());
        assertEquals(50, result.getSoChoToiDa());
        assertEquals(20, result.getSoChoConLai()); // Không làm thay đổi soChoConLai
    }

    @Test
    void testUpdateCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            courseService.update(1L, courseDTO);
        });
    }

    @Test
    void testDeleteCourseSuccess() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        courseService.delete(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourseNotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> {
            courseService.delete(1L);
        });
    }
}
