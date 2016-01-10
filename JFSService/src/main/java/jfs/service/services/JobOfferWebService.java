package jfs.service.services;


import jfs.data.dataobjects.JobOfferMetricsDO;
import jfs.service.events.jobevents.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jfs.service.events.jobevents.annotations.*;
import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.*;
import jfs.transferdata.transferobjects.enums.ResultTypeDTO;
import jfs.transferdata.transferobjects.enums.UserTypeDTO;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Path("/offers")
@Api(value = "/offers")
public class JobOfferWebService {
    @Inject
    private JobOfferService jobOfferService;
    @Inject
    private MetricsService metricsService;

    @Inject @JobApply
    private Event<JobApplyEvent> jobApplyEvent;
    @Inject @JobDetailView
    private Event<JobDetailViewEvent> jobDetailViewEvent;
    @Inject @JobListView
    private Event<JobListViewEvent> jobListViewEvent;

    @POST
    @Path("/add") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Add offer", notes = "A offer will be created.")
    public ActionResultDTO addOffer(CreateJobOfferDTO offerDTO){
        ActionResultDTO result = new ActionResultDTO();
        if (offerDTO.companyId != null) {
            Session session = SessionService.sessions.get(offerDTO.token);
            if (session == null) {
                result.type = ResultTypeDTO.not_logged_in;
            } else if (session.type != UserTypeDTO.COMPANY) {
                result.type = ResultTypeDTO.wrong_user_type;
            } else {
                result.hasSucceeded = this.jobOfferService.addOffer(offerDTO.jobOffer, offerDTO.companyId);
                if (result.hasSucceeded) {
                    result.type = ResultTypeDTO.success;
                } else {
                    result.type = ResultTypeDTO.data_error;
                }
            }
            /*if (session != null && session.type == UserTypeDTO.COMPANY) {
                return this.jobOfferService.addOffer(offerDTO.jobOffer, offerDTO.companyId);
            }*/
        }
        //return false;
        return result;
    }

    @POST
    @Path("/addmulti") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Add multiple offers", notes = "Multiple offers will be created. For formatting of the required CSV file, see the manual.")
    public Boolean addOffers(CreateJobOffersDTO offersDTO){
        if(offersDTO.companyId != null) {
            Session session = SessionService.sessions.get(offersDTO.token);
            if (session != null && session.type == UserTypeDTO.COMPANY) {
                for(JobOfferDTO offer : offersDTO.jobOffers){
                    offer.companyId = offersDTO.companyId;
                }
                return this.jobOfferService.addOffers(offersDTO.jobOffers, offersDTO.companyId);
            }
        }
        return false;
    }

    @POST
    @Path("/getall") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Get all offers", notes = "Returns an array of all offers.")
    public JobOfferListDTO getAllOffers(String token) {
        if (token != null && token != "") {
            Session session = SessionService.sessions.get(token);
            if (session != null && session.type == UserTypeDTO.ADMIN) {
                JobOfferListDTO list = new JobOfferListDTO();
                list.offers = this.jobOfferService.getAllOffers();
                return list;
            }
        }
        return null;
    }

    @POST
    @Path("/search/text") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Search by text", notes = "Returns an array of offers that match exactly a specific string.")
    public JobOfferListDTO searchText(String term) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchText(term);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/search/recent") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Recent offers", notes = "Returns an array of offers.")
    public JobOfferListDTO getRecent(Integer amount) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchRecent(amount);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/search") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Search offer", notes = "Returns an array of offers that match a set of specified criteria")
    public JobOfferListDTO search(SearchDTO searchDTO) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.search(searchDTO);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/metrics/company") @Consumes("application/json") @Produces("application/json")
    @ApiOperation(value = "Get job metrics", notes = "Returns all available metrics of all the job offers created by the logged in user. Will return null if the user is not logged in or the user account is not of the Company type.")
    public List<JobOfferMetricsDTO> getMetricsByCompany(String token){
        Session session = SessionService.sessions.get(token);
        if(session == null || session.type != UserTypeDTO.COMPANY){
            return null;
        }else{
            return this.metricsService.getJobOfferMetricsByCompany(session.userId);
        }
    }

    @GET @Path("{id}")
    @ApiOperation(value = "Get offer", notes = "Returns one specific offer.")
    public JobOfferDTO getById(@PathParam("id") String id) {
        JobOfferDTO offerDTO = this.jobOfferService.getById(id);
        if(offerDTO != null){
            this.jobDetailViewEvent.fire(new JobDetailViewEvent(id));
        }
        return offerDTO;
    }
}
