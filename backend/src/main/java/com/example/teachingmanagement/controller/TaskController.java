package com.example.teachingmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;
    private final CourseService courseService;
    private final ActivityService activityService;

    public TaskController(TaskService service, CourseService courseService, ActivityService activityService) {
        this.service = service;
        this.courseService = courseService;
        this.activityService = activityService;
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        Long courseId = task.getActivity().getCourse().getId();
        task.getActivity().setCourse(courseService.findById(courseId));
        Long activityId = task.getActivity().getId();
        task.setActivity(activityService.findById(activityId));
        return service.save(task);
    }

    @GetMapping
    public List<Task> getAll() {
        return service.findAll();
    }

    /**
     * Create a task for a specific activity
     * PathVariable is used to extract values from the URL path and convert them into Long parameters
     * @param activityId
     * @param task
     * @return
     */
    @PostMapping("/activity/{activityId}")
    public Task createTaskForActivity( @PathVariable Long activityId, @RequestBody Task task) {
        return service.createTaskForActivity(activityId, task);
    }

    /**
     * Get all tasks for a specific activity
     * @param activityId
     * @return
     */
    @GetMapping("/activity/{activityId}")
    public List<Task> getTasksByActivity(@PathVariable Long activityId) {
        return service.findByActivityId(activityId);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        service.deleteTask(taskId);
    }
}
