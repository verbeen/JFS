package jfs.transferdata.transferobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDTO {
    public String userId;
    public String types;
    public String location;
    public String skills;
    public long lastView;

    public StudentSubscriptionsDTO() {
    }

    public StudentSubscriptionsDTO(String userId, String types, String location, String skills, long lastView) {
        this.userId = userId;
        this.types = types;
        this.location = location;
        this.skills = skills;
        this.lastView = lastView;
    }
}
