package com.example.teachingmanagement.repository;

import com.example.teachingmanagement.model.Activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /**
     * Return a list of activities for a given course ID.
     * @param <Activity>
     * @param courseId
     */
    List<Activity> findByCourseId(Long courseId);
}
