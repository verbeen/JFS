package jfs.service.services;

import jfs.transferdata.transferobjects.StudentProfileDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Hulk-A on 13.11.2015.
 *
 * Wrapper for studentProfileService. Accessible through @Path
 *
 */
@Path("/studentprofile")
public class StudentProfileWebService {
    @Inject
    StudentProfileService studentProfileService;

    //Add a student profile
    @POST
    @Path("/add") @Consumes("application/json") @Produces("application/json")
    public Boolean addStudentProfile(StudentProfileDTO profileDTO){
        Boolean result = this.studentProfileService.addStudentProfile(profileDTO);
        return result;
    }

    //Get a student profile
    @POST
    @Path("/get") @Consumes("application/json") @Produces("application/json")
    public StudentProfileDTO getStudentProfile(String userId){
        StudentProfileDTO result = this.studentProfileService.getStudentProfile(userId);
        return result;
    }
}
