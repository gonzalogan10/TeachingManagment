package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.service.CourseService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course("GJAE", "GJAE2025");
    }

    @Test
    void saveCourse() {
        Course saved = courseService.save(course);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findAll() {
        assertThat(courseService.findAll()).isNotNull();
    }

    @Test
    void findById() {
        Course saved = courseService.save(course);
        Course found = courseService.findById(saved.getId());
        assertThat(found).isNotNull();
    }
}