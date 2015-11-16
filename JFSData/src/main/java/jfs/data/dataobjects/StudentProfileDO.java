package jfs.data.dataobjects;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class StudentProfileDO {

    /*
    FROM DATA MODEL:
    DONE
    user_id: The unique id of the user entity of type student connected to this profile.
    name: The first last name of the user.
    email: The email address the user wants to communicates with, this defaults to the email address
    used for logging in, but can be changed.
    organization: Recent organization(s) the student has worked at.
    address
    skills: A list or description of all the skills the student possesses. //TODO could be done as a list though I prefer a description, let's discuss
    experience

    TODO Think about how and if we implement "blob" storage
    resume: A file containing the resume of the student. This can be uploaded by the student and is
    restricted to the PDF format

    course_details
     */

    public String user_id;
    public String name;
    public String email;
    public String organization;
    public String address;
    public String skills;
    public String experience;

    public String course_details;


    public StudentProfileDO() {
    }

    //lightweight constructor
    public StudentProfileDO(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    public StudentProfileDO(String user_id, String name, String email, String organization, String address, String skills, String experience, String course_details) {
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