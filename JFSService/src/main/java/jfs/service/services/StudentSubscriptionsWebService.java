package jfs.service.services;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiOperation;
import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.JobOfferListDTO;
import jfs.transferdata.transferobjects.StudentSubscriptionsDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Created by Hulk-A on 13.11.2015.
 *
 * Wrapper for studentSubscriptionsService. Accessible through @Path
 *
 */
@Path("/studentsubscriptions")
public class StudentSubscriptionsWebService {
    @Inject
    StudentSubscriptionsService studentSubscriptionsService;

    /**
     * Add a student subscription
     */
    @POST
    @Path("/add") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Add a student subscription", notes = "Returns boolean for success")
    public Boolean addStudentSubscriptions(StudentSubscriptionsDTO subscriptionsDTO){
        Boolean result = this.studentSubscriptionsService.addStudentSubscriptions(subscriptionsDTO);
        return result;
    }

    /**
     * Get a student subscription
     */
    @POST
    @Path("/get") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Get a student subscription", notes = "Returns a student subscription")
    public StudentSubscriptionsDTO getStudentSubscriptions(String token){
        Session session = SessionService.sessions.get(token);
        if(session != null){
            return this.studentSubscriptionsService.getStudentSubscriptions(session.userId);
        }
        else{
            return null;
        }
    }

    /**
     * Update a student subscription
     */
    @POST
    @Path("/update") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Update a student subscription", notes = "Returns boolean for success")
    public Boolean updateStudentSubscriptions(StudentSubscriptionsDTO subscriptionsDTO){
        return this.studentSubscriptionsService.updateStudentSubscriptions(subscriptionsDTO.userId, subscriptionsDTO);
    }

    /**
     * Update last view field for a student subscription
     */
    @POST
    @Path("/updateLastView") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Update the lastView field of a student subscription", notes = "Returns boolean for success")
    public Boolean updateLastView(String userId, long lastView){
        return this.studentSubscriptionsService.updateLastView(userId, lastView);
    }

    /**
     * Get all job offers that fit a student subscription
     */
    @POST
    @Path("/checkSubscriptions") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Search for job offers by a student subscription", notes = "Returns a list of job offers that match the student subscription criteria")
    public JobOfferListDTO checkSubscriptions(String token){
        Session session = SessionService.sessions.get(token);
        if(session != null){
            //1. get job offers by userId -> type, location, skills
            JobOfferListDTO list = new JobOfferListDTO();
            list.offers = this.studentSubscriptionsService.checkSubscriptions(session.userId);

            Date myDate = new Date();

            //2. update last view
            this.updateLastView(session.userId, myDate.getTime());
            return list;
        }
        else{
            return null;
        }
    }
}