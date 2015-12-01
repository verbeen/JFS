package jfs.service.services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import jfs.transferdata.transferobjects.JobOfferListDTO;
import jfs.transferdata.transferobjects.SearchDTO;
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

    @POST
    @Path("/update") @Consumes("application/json") @Produces("application/json")
    public Boolean updateStudentSubscriptions(String userId, StudentSubscriptionsDTO subscriptionsDTO){
        return this.studentSubscriptionsService.updateStudentSubscriptions(userId, subscriptionsDTO);
    }

    //might not be needed...
    @POST
    @Path("/updateLastView") @Consumes("application/json") @Produces("application/json")
    public Boolean updateLastView(String userId, long lastView){
        return this.studentSubscriptionsService.updateLastView(userId, lastView);
    }

    @POST
    @Path("/checkSubscriptions") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO checkSubscriptions(StudentSubscriptionsDTO studentSubscriptionsDTO){
        //1. get job offers by userId -> type, location, skills
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.studentSubscriptionsService.checkSubscriptions(studentSubscriptionsDTO);

        //2. update last view
        this.updateLastView(studentSubscriptionsDTO.userId, studentSubscriptionsDTO.lastView);
        return list;
    }

}
