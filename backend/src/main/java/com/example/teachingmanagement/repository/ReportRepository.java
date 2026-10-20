package com.example.teachingmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findBySolver(Solver solver);

    List<Report> findByTask(Task task);

    @Query("select r from Report r where r.task.activity.id = :activityId")
    List<Report> findByActivityId(@Param("activityId") Long activityId);

}