package jfs.transferdata.transferobjects;

/**
 * Created by Hulk-A on 16.11.2015.
 */
public class StudentProfileDTO {
    public String userId;
    public String name;
    public String email;
    public String organization;
    public String address;
    public String skills;
    public String experience;
    public String resume;
    public String courseDetails;


    public StudentProfileDTO() {
    }

    //lightweight constructor
    public StudentProfileDTO(String userId, String name) {
        this.userId = userId;
        this.name = name;

    }

    public StudentProfileDTO(String userId, String name, String email, String organization, String address, String skills, String experience, String resume, String courseDetails) {
        this.userId = userId;
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
