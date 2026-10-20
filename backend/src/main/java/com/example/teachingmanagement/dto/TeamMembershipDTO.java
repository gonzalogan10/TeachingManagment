package com.example.teachingmanagement.dto;

public class TeamMembershipDTO {
    private Long id;
    private StudentDTO student;

    public TeamMembershipDTO() {}

    public TeamMembershipDTO(Long id, StudentDTO student) {
        this.id = id;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }
}
