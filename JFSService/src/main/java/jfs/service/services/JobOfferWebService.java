package jfs.service.services;


import jfs.data.dataobjects.JobOfferMetricsDO;
import jfs.service.events.jobevents.*;
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
    public ActionResultDTO addOffer(CreateJobOfferDTO offerDTO){
        ActionResultDTO result = new ActionResultDTO();
        if(offerDTO.companyId != null) {
            Session session = SessionService.sessions.get(offerDTO.token);
            if(session == null){
                result.type = ResultTypeDTO.not_logged_in;
            }
            else if (session.type != UserTypeDTO.COMPANY) {
                result.type = ResultTypeDTO.wrong_user_type;
            }
            else{
                result.hasSucceeded = this.jobOfferService.addOffer(offerDTO.jobOffer, offerDTO.companyId);
                if(result.hasSucceeded){
                    result.type = ResultTypeDTO.success;
                }else{
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
    public JobOfferListDTO getAllOffers(String token){
        if(token != null && token != "") {
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
    public JobOfferListDTO searchText(String term){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchText(term);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/search/recent") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO getRecent(Integer amount){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchRecent(amount);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/search") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO search(SearchDTO searchDTO){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.search(searchDTO);
        this.jobListViewEvent.fire(new JobListViewEvent(list.offers));
        return list;
    }

    @POST
    @Path("/metrics/company") @Consumes("application/json") @Produces("application/json")
    public List<JobOfferMetricsDTO> getMetricsByCompany(String token){
        String companyId = SessionService.sessions.get(token).userId;
        if(token == null){
            return null;
        }else{
            return this.metricsService.getJobOfferMetricsByCompany(companyId);
        }
    }

    @GET
    @Path("{id}")
    public JobOfferDTO getById(@PathParam("id") String id){
        JobOfferDTO offerDTO = this.jobOfferService.getById(id);
        if(offerDTO != null){
            this.jobDetailViewEvent.fire(new JobDetailViewEvent(id));
        }
        return offerDTO;
    }
}
