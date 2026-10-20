package com.example.teachingmanagement.controller;

import java.util.List;

import com.example.teachingmanagement.model.ActivityLoad;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.service.LoadCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class LoadCalculationController {

    private final LoadCalculationService loadService;

    public LoadCalculationController(LoadCalculationService loadService) {
        this.loadService = loadService;
    }

    /**
     * Get all reports for a specific activity
     * @param activityId
     * @return list of reports related to the activity
     */
    @GetMapping("/{activityId}/reports")
    public List<Report> getReportsForActivity(@PathVariable Long activityId) {
        return loadService.findReportsForActivity(activityId);
    }

    /**
     * Calculate the total load for a specific activity
     * Returns hours, students, and proportion totals
     * @param activityId
     * @return ActivityLoad with calculated totals
     */
    @GetMapping("/{activityId}/load")
    public ActivityLoad calculateLoad(@PathVariable Long activityId) {
        return loadService.calculateLoad(activityId);
    }

    /**
     * Get total hours for a specific activity
     * @param activityId
     * @return total hours spent on the activity
     */
    @GetMapping("/{activityId}/load/hours")
    public double calculateTotalHours(@PathVariable Long activityId) {
        return loadService.calculateTotalHours(loadService.findReportsForActivity(activityId));
    }

    /**
     * Get total students for a specific activity
     * @param activityId
     * @return total students evaluated in the activity
     */
    @GetMapping("/{activityId}/load/students")
    public int calculateTotalStudents(@PathVariable Long activityId) {
        return loadService.calculateTotalStudents(loadService.findReportsForActivity(activityId));
    }

    /**
     * Get total proportion for a specific activity
     * @param activityId
     * @return total proportion of work on the activity
     */
    @GetMapping("/{activityId}/load/proportion")
    public double calculateTotalProportion(@PathVariable Long activityId) {
        return loadService.calculateTotalProportion(loadService.findReportsForActivity(activityId));
    }
}