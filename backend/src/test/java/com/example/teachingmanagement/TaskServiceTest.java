package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.TaskService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ActivityService activityService;

    private Course course;
    private Activity activity;
    private Task task;

    @BeforeEach
    void setUp() {
        course = courseService.save(new Course("GJAE", "GJAE2025"));
        activity = activityService.createActivityForCourse(course.getId(), new Activity("Lecture 1", ActivityType.LECTURE, course));
        task = new Task("Test task", activity);
    }

    @Test
    void saveTask() {
        Task saved = taskService.save(task);
        assertThat(saved.getId()).isNotNull();
    }

     @Test
    void createForActivity() {
        Task saved = taskService.createTaskForActivity(activity.getId(), task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getActivity().getId()).isEqualTo(activity.getId());
    }

    @Test
    void findAll() {
        assertThat(taskService.findAll()).isNotNull();
    }

    @Test
    void findById() {
        Task saved = taskService.save(task);
        Task found = taskService.findById(saved.getId());
        assertThat(found).isNotNull();
    }
}