package jfs.data.dataobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDO extends DataObject {

    public String types;
    public String location;
    public String skills;
    public long lastView;

    public StudentSubscriptionsDO(String userId, String types, String location, String skills, long lastView) {
        super(userId);
        this.types = types;
        this.location = location;
        this.skills = skills;
        this.lastView = lastView;
    }
}
