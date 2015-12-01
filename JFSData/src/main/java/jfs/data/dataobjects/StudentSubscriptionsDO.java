package jfs.data.dataobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDO extends DataObject {

    public String types;
    public String location;
    public String skills;

    public StudentSubscriptionsDO() {
    }

    public StudentSubscriptionsDO(String userId, String types, String location, String skills) {
        super(userId);
        this.types = types;
        this.location = location;
        this.skills = skills;
    }
}
