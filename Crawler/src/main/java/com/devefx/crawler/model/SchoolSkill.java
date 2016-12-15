package com.devefx.crawler.model;

import java.util.Set;

public class SchoolSkill {

    private int id;
    private int schoolId;
    private String name;
    private String description;
    private Set<String> includeSkills;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if (description.length() < 3) {
            return;
        }
        this.description = description;
    }
    public Set<String> getIncludeSkills() {
        return includeSkills;
    }
    public void setIncludeSkills(Set<String> includeSkills) {
        this.includeSkills = includeSkills;
    }
}
