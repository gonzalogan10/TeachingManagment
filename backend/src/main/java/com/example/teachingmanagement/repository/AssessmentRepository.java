package com.example.teachingmanagement.repository;

import com.example.teachingmanagement.model.Assessment;
import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.model.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByMembership(TeamMembership membership);

    List<Assessment> findByMembershipIn(List<TeamMembership> memberships);

    @Query("SELECT COUNT(a) > 0 FROM Assessment a WHERE a.criterion.id = :criterionId AND a.membership.team.id = :teamId")
    boolean existsByCriterionAndTeam(@Param("criterionId") Long criterionId, @Param("teamId") Long teamId);

    @Query("SELECT COUNT(a) > 0 FROM Assessment a WHERE a.criterion.id = :criterionId AND a.membership.team.id = :teamId AND a.activityId = :activityId")
    boolean existsByCriterionAndTeamAndActivity(@Param("criterionId") Long criterionId, @Param("teamId") Long teamId, @Param("activityId") Long activityId);

    @Query("SELECT DISTINCT a FROM Assessment a LEFT JOIN FETCH a.membership m LEFT JOIN FETCH m.student LEFT JOIN FETCH a.criterion WHERE a.criterion.id = :criterionId AND a.membership.team.id = :teamId")
    List<Assessment> findByCriterionAndTeam(@Param("criterionId") Long criterionId, @Param("teamId") Long teamId);

    @Query("SELECT DISTINCT a FROM Assessment a LEFT JOIN FETCH a.membership m LEFT JOIN FETCH m.student LEFT JOIN FETCH a.criterion WHERE a.criterion.id = :criterionId AND a.membership.team.id = :teamId AND a.activityId = :activityId")
    List<Assessment> findByCriterionAndTeamAndActivity(@Param("criterionId") Long criterionId, @Param("teamId") Long teamId, @Param("activityId") Long activityId);

    @Query("SELECT DISTINCT a FROM Assessment a LEFT JOIN FETCH a.membership m LEFT JOIN FETCH m.student LEFT JOIN FETCH a.criterion WHERE a.membership.team.id = :teamId")
    List<Assessment> findByTeamIdWithDetails(@Param("teamId") Long teamId);

    @Query("SELECT DISTINCT a FROM Assessment a LEFT JOIN FETCH a.membership m LEFT JOIN FETCH m.student LEFT JOIN FETCH a.criterion WHERE a.membership.team.id = :teamId AND a.activityId = :activityId")
    List<Assessment> findByTeamAndActivityWithDetails(@Param("teamId") Long teamId, @Param("activityId") Long activityId);
}