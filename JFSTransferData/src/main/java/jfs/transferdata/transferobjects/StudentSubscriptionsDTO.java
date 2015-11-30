package jfs.transferdata.transferobjects;

/**
 * Created by Hulk-A on 30.11.2015.
 */
public class StudentSubscriptionsDTO {
    public String userId;
    public String subTypes;
    public String subLocation;
    public String subSkills;

    public StudentSubscriptionsDTO() {
    }

    public StudentSubscriptionsDTO(String userId, String subTypes, String subLocation, String subSkills) {
        this.userId = userId;
        this.subTypes = subTypes;
        this.subLocation = subLocation;
        this.subSkills = subSkills;
    }
}
