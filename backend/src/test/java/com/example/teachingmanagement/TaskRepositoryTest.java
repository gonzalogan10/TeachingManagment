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
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.ActivityRepository;
import com.example.teachingmanagement.repository.CourseRepository;
import com.example.teachingmanagement.repository.TaskRepository;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Activity activity;
    private Task task;
    
    @BeforeEach
    void setUp() {
        course = courseRepository.save(new Course("GJAE", "GJAE2025"));
        activity = activityRepository.save(new Activity("Lecture 1", ActivityType.LECTURE, course));
        task = new Task("Implementation", activity);
    }

    @Test
    void createTaskForActivity() {
        Task saved = taskRepository.save(task);
        assertThat(saved.getActivity().getId()).isEqualTo(activity.getId());
    }

    @Test
    void findTaskById() {
        Task saved = taskRepository.save(task);

        Optional<Task> found = taskRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found).hasValueSatisfying(t -> assertThat(t.getTitle()).isEqualTo("Implementation"));
    }

    @Test
    void findAllTasks() {
        taskRepository.save(task);
        taskRepository.save(new Task("Testing", activity));

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(2);
    }

    @Test
    void updateTask() {
        Task saved = taskRepository.save(task);

        saved.setTitle("Implementation Updated");
        Task updated = taskRepository.save(saved);

        assertThat(updated.getTitle()).isEqualTo("Implementation Updated");
    }

    @Test
    void deleteTask() {
        Task saved = taskRepository.save(task);

        taskRepository.deleteById(saved.getId());

        Optional<Task> found = taskRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }
}