package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.JobTypeDTO;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDTO {
    public String userId;
    public JobTypeDTO type;
    public String location;
    public String skills;
    public long lastView;

    public StudentSubscriptionsDTO() {
    }

    public StudentSubscriptionsDTO(String userId, JobTypeDTO type, String location, String skills, long lastView) {
        this.userId = userId;
        this.type = type;
        this.location = location;
        this.skills = skills;
        this.lastView = lastView;
    }
}
