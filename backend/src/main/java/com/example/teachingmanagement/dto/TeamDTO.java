package com.example.teachingmanagement.dto;

import java.util.List;

public class TeamDTO {
    private Long id;
    private String name;
    private List<TeamMembershipDTO> memberships;

    public TeamDTO() {}

    public TeamDTO(Long id, String name, List<TeamMembershipDTO> memberships) {
        this.id = id;
        this.name = name;
        this.memberships = memberships;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamMembershipDTO> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<TeamMembershipDTO> memberships) {
        this.memberships = memberships;
    }
}
