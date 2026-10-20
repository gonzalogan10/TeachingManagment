package com.example.teachingmanagement.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.teachingmanagement.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.solvers LEFT JOIN FETCH t.mainSolver WHERE t.activity.id = :activityId")
    List<Task> findByActivityId(@Param("activityId") Long activityId);
}
