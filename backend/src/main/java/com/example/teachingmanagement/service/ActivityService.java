package com.example.teachingmanagement.service;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CourseService courseService;

    public ActivityService(ActivityRepository a, CourseService c) {
        this.activityRepository = a;
        this.courseService = c;
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
    }

    public Activity createActivityForCourse(Long courseId, Activity activity) {
        if(activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        Course course = courseService.findById(courseId);
        activity.setCourse(course);
        // Don't set ID manually, let the database handle it
        activity.setId(null);
        return activityRepository.save(activity);
    }

    public List<Activity> findByCourseId(Long courseId) {
        return activityRepository.findByCourseId(courseId);
    }

    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }
}
