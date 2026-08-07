package com.example.ngay1ptrpmhddc.repository;

import com.example.ngay1ptrpmhddc.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTenMonHocIgnoreCase(String tenMonHoc);
    boolean existsByTenMonHocIgnoreCaseAndIdNot(String tenMonHoc, Long id);
}
