package jfs.data.dataobjects;

import jfs.data.dataobjects.enums.JobType;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDO extends DataObject {

    public JobType type;
    public String location;
    public String skills;
    public long lastView;

    public StudentSubscriptionsDO(String userId, JobType type, String location, String skills, long lastView) {
        super(userId);
        this.type = type;
        this.location = location;
        this.skills = skills;
        this.lastView = lastView;
    }
}
