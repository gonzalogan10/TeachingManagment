package com.example.teachingmanagement.service;

import com.example.teachingmanagement.repository.ReportRepository;
import com.example.teachingmanagement.repository.ActivityRepository;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.ActivityLoad;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoadCalculationService {

    private final ReportRepository reportRepository;
    private final ActivityRepository activityRepository;

    public LoadCalculationService(ReportRepository reportRepository, ActivityRepository activityRepository) {
        this.reportRepository = reportRepository;
        this.activityRepository = activityRepository;
    }

    /**
     * Find all reports related to a specific activity.
     * @param activityId
     * @return
     */
    public List<Report> findReportsForActivity(Long activityId) {
        activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        return reportRepository.findByActivityId(activityId);
    }

    /**
     * Calculate load for a given activity, taking into account hours, students, and proportion from reports.
     * @param activityId, the id of the activity
     * @return the calculated load as an ActivityLoad
     */
     public ActivityLoad calculateLoad(Long activityId) {
        List<Report> reports = findReportsForActivity(activityId);

        double hours = calculateTotalHours(reports);
        int students = calculateTotalStudents(reports);
        double proportion = calculateTotalProportion(reports);

        return new ActivityLoad(activityId, hours, students, proportion);
    }

    public double calculateTotalHours(List<Report> reports) {
        double total = 0;
        for (Report report : reports) {
            total += parseHours(report.getTimeSpent());
        }
        return total;
    }

    public int calculateTotalStudents(List<Report> reports) {
        int total = 0;
        for (Report report : reports) {
            total += report.getNumStudents();
        }
        return total;
    }

    public double calculateTotalProportion(List<Report> reports) {
        double total = 0;
        for (Report report : reports) {
            total += report.getProportion();
        }
        return total;
    }

    private double parseHours(String timeSpent) {
        if (timeSpent == null) {
            return 0;
        }
        String numeric = timeSpent.toLowerCase().replace("h", "").replace("hours", "").replace("hour", "").trim();
        numeric = numeric.replace(',', '.');
        if (numeric.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(numeric);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
}