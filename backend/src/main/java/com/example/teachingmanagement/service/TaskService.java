package com.example.teachingmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.TaskRepository;


@Service
public class TaskService {

    private final TaskRepository repository;
    private final ActivityService activityService;

    public TaskService(TaskRepository repository, ActivityService activityService) {
        this.repository = repository;
        this.activityService = activityService;
    }

    public Task save(Task task) {
        return repository.save(task);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task createTaskForActivity(Long activityId, Task task) {
        Activity activity = activityService.findById(activityId);
        task.setActivity(activity);
        return repository.save(task);
    }

   public Task findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
   }

   public List<Task> findByActivityId(Long activityId) {
        return repository.findByActivityId(activityId);
   }

   public void deleteTask(Long taskId) {
        repository.deleteById(taskId);
   }
}
