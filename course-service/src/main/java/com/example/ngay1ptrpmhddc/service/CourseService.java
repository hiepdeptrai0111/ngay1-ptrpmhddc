package com.example.ngay1ptrpmhddc.service;

import com.example.ngay1ptrpmhddc.dto.CourseDTO;
import com.example.ngay1ptrpmhddc.entity.Course;
import com.example.ngay1ptrpmhddc.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
        return mapToDTO(course);
    }

    public CourseDTO create(CourseDTO dto) {
        if (courseRepository.existsByTenMonHocIgnoreCase(dto.getTenMonHoc())) {
            throw new IllegalArgumentException("Ten mon hoc da ton tai: " + dto.getTenMonHoc());
        }

        Course course = mapToEntity(dto);
        course.setSoChoConLai(course.getSoChoToiDa()); // Luôn đặt số chỗ còn lại = số chỗ tối đa

        Course savedCourse = courseRepository.save(course);
        return mapToDTO(savedCourse);
    }

    public CourseDTO update(Long id, CourseDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));

        if (courseRepository.existsByTenMonHocIgnoreCaseAndIdNot(dto.getTenMonHoc(), id)) {
            throw new IllegalArgumentException("Ten mon hoc da ton tai: " + dto.getTenMonHoc());
        }

        course.setTenMonHoc(dto.getTenMonHoc());
        course.setSoTinChi(dto.getSoTinChi());
        course.setSoChoToiDa(dto.getSoChoToiDa());
        // Không sửa soChoConLai

        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NoSuchElementException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    private CourseDTO mapToDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getTenMonHoc(),
                course.getSoTinChi(),
                course.getSoChoToiDa(),
                course.getSoChoConLai()
        );
    }

    private Course mapToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setTenMonHoc(dto.getTenMonHoc());
        course.setSoTinChi(dto.getSoTinChi());
        course.setSoChoToiDa(dto.getSoChoToiDa());
        course.setSoChoConLai(dto.getSoChoConLai());
        return course;
    }
}
