package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.JobTypeDTO;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDTO {
    public String userId;
    public JobTypeDTO types;
    public String location;
    public String skills;
    public long lastView;

    public StudentSubscriptionsDTO() {
    }

    public StudentSubscriptionsDTO(String userId, JobTypeDTO types, String location, String skills, long lastView) {
        this.userId = userId;
        this.types = types;
        this.location = location;
        this.skills = skills;
        this.lastView = lastView;
    }
}
