package jfs.data.dataobjects;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class StudentProfileDO extends DataObject {

    public String name;
    public String email;
    public String organization;
    public String address;
    public String skills;
    public String experience;
    public String resume; //URL!
    public String courseDetails;

    public StudentProfileDO(String user_id, String name, String email, String organization, String address, String skills, String experience, String resume, String courseDetails) {
        super(user_id);
        this.name = name;
        this.email = email;
        this.organization = organization;
        this.address = address;
        this.skills = skills;
        this.experience = experience;
        this.resume = resume;
        this.courseDetails = courseDetails;
    }
}