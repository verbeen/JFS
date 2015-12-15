package jfs.data.dataobjects;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class StudentProfileDO extends DataObject {

    /*
    FROM DATA MODEL:
    DONE
    user_id: The unique id of the user entity of type student connected to this profile.
    name: The first last name of the user.
    email: The email address the user wants to communicates with, this defaults to the email address
    used for logging in, but can be changed.
    organization: Recent organization(s) the student has worked at.
    address
    skills: A list or description of all the skills the student possesses.
    experience
    resume: A file containing the resume of the student. This can be uploaded by the student and is
    restricted to the PDF format
    courseDetails
     */

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