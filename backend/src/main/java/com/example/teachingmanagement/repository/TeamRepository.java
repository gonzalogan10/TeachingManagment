package com.example.teachingmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.teachingmanagement.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long>  {
    List<Team> findByCourseId(Long courseId);
}