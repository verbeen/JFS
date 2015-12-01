package jfs.transferdata.transferobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDTO {
    public String userId;
    public String types;
    public String location;
    public String skills;

    public StudentSubscriptionsDTO() {
    }

    public StudentSubscriptionsDTO(String userId, String types, String location, String skills) {
        this.userId = userId;
        this.types = types;
        this.location = location;
        this.skills = skills;
    }
}
