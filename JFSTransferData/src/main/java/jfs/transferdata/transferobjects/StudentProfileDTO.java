package jfs.transferdata.transferobjects;

/**
 * Created by Hulk-A on 16.11.2015.
 */
public class StudentProfileDTO {
    public String user_id;
    public String name;
    public String email;
    public String organization;
    public String address;
    public String skills;
    public String experience;

    public String course_details;


    public StudentProfileDTO() {
    }

    //lightweight constructor
    public StudentProfileDTO(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    public StudentProfileDTO(String user_id, String name, String email, String organization, String address, String skills, String experience, String course_details) {
        this.user_id = user_id;
        this.name = name;
        this.email = email; //logic if empty or if set to user_id (default) or something else will be done in upper layers //TODO Delete altogether?
        this.organization = organization;
        this.address = address;
        this.skills = skills;
        this.experience = experience;
        this.course_details = course_details;
    }
}
