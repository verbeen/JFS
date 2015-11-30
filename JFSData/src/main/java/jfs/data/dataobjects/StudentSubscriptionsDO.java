package jfs.data.dataobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDO extends DataObject {

    public String subTypes;
    public String subLocation;
    public String subSkills;

    public StudentSubscriptionsDO() {
    }

    public StudentSubscriptionsDO(String userId, String subTypes, String subLocation, String subSkills) {
        super(userId);
        this.subTypes = subTypes;
        this.subLocation = subLocation;
        this.subSkills = subSkills;
    }
}
