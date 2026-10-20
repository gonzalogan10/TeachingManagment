package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CourseService courseService;

    private Course course;

     @BeforeEach
    void setUp() {
        course = courseService.save(new Course("GJAE", "GJAE2025"));
    }

    @Test
    void saveActivity() {
        Activity activity = new Activity("Test activity", ActivityType.LECTURE, course);
        Activity saved = activityService.save(activity);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void createForCourse() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);

        Activity saved = activityService.createActivityForCourse(course.getId(), activity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCourse().getId()).isEqualTo(course.getId());
    }

    @Test
    void findAll() {
        assertThat(activityService.findAll()).isNotNull();
    }

    @Test
    void findById() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);
        Activity saved = activityService.save(activity);

        Activity found = activityService.findById(saved.getId());
        assertThat(found).isNotNull();
    }
}