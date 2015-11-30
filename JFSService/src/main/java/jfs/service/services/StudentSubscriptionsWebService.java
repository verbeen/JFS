package jfs.service.services;

import jfs.transferdata.transferobjects.StudentProfileDTO;
import jfs.transferdata.transferobjects.StudentSubscriptionsDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Hulk-A on 13.11.2015.
 */
@Path("/studentsubscriptions")
public class StudentSubscriptionsWebService {
    @Inject
    StudentSubscriptionsService studentSubscriptionsService;

    @POST
    @Path("/add") @Consumes("application/json") @Produces("application/json")
    public Boolean addStudentSubscriptions(StudentSubscriptionsDTO subscriptionsDTO){
        Boolean result = this.studentSubscriptionsService.addStudentSubscriptions(subscriptionsDTO);
        return result;
    }
    @POST
    @Path("/get") @Consumes("application/json") @Produces("application/json")
    public StudentSubscriptionsDTO getStudentSubscriptions(String userId){
        StudentSubscriptionsDTO result = this.studentSubscriptionsService.getStudentSubscriptions(userId);
        return result;
    }

    //TODO update
}
