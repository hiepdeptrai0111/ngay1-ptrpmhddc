package com.example.ngay1ptrpmhddc.controller;

import com.example.ngay1ptrpmhddc.dto.CourseDTO;
import com.example.ngay1ptrpmhddc.service.CourseService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGetCourseByIdNotFound() {
        when(courseService.getById(99L)).thenThrow(new NoSuchElementException("Course not found"));

        assertThrows(NoSuchElementException.class, () -> {
            courseController.getCourseById(99L);
        });
    }

    @Test
    void testCreateCourseValidationFailed() {
        CourseDTO invalidDto = new CourseDTO(null, "", 3, 40, null); // tenMonHoc is empty

        Set<ConstraintViolation<CourseDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        
        boolean hasTenMonHocError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("tenMonHoc"));
        assertTrue(hasTenMonHocError);
    }

    @Test
    void testDeleteCourseSuccess() {
        doNothing().when(courseService).delete(1L);

        ResponseEntity<Void> response = courseController.deleteCourse(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService, times(1)).delete(1L);
    }
}
