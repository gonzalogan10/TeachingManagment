package com.example.teachingmanagement;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.repository.ActivityRepository;
import com.example.teachingmanagement.repository.CourseRepository;

@DataJpaTest
class ActivityRepositoryTest {

	  @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        course = courseRepository.save(new Course("GJAE", "GJAE2025"));
    }

    @Test
    void createActivityForCourse() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);

        Activity saved = activityRepository.save(activity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCourse().getId()).isEqualTo(course.getId());
    }

    @Test
    void findActivitiesByCourseId() {
        activityRepository.save(new Activity("Lecture 1", ActivityType.LECTURE, course));
        activityRepository.save(new Activity("Exam 1", ActivityType.EXAM, course));

        List<Activity> list = activityRepository.findByCourseId(course.getId());
		assertThat(list).hasSize(2);
    }

    @Test
    void findActivityById() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);
        Activity saved = activityRepository.save(activity);

        Optional<Activity> found = activityRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Lecture 1");
    }

    @Test
    void findAllActivities() {
        activityRepository.save(new Activity("Lecture 1", ActivityType.LECTURE, course));
        activityRepository.save(new Activity("Exam 1", ActivityType.EXAM, course));

        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void updateActivity() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);
        Activity saved = activityRepository.save(activity);

        saved.setName("Lecture 1 Updated");
        Activity updated = activityRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("Lecture 1 Updated");
    }

    @Test
    void deleteActivity() {
        Activity activity = new Activity("Lecture 1", ActivityType.LECTURE, course);
        Activity saved = activityRepository.save(activity);

        activityRepository.deleteById(saved.getId());

        Optional<Activity> found = activityRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }
}