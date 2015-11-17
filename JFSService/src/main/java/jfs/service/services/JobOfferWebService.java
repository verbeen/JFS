package jfs.service.services;


import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.*;
import jfs.transferdata.transferobjects.enums.UserTypeDTO;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Path("/offers")
public class JobOfferWebService {
    @Inject
    private JobOfferService jobOfferService;

    @POST
    @Path("/add") @Consumes("application/json") @Produces("application/json")
    public Boolean addOffer(CreateJobOfferDTO offerDTO){
        if(offerDTO.companyId != null) {
            Session session = SessionService.sessions.get(offerDTO.token);
            if (session != null && session.type == UserTypeDTO.COMPANY) {
                return this.jobOfferService.addOffer(offerDTO.jobOffer, offerDTO.companyId);
            }
        }
        return false;
    }

    @POST
    @Path("/addmore") @Consumes("application/json") @Produces("application/json")
    public Boolean addOffers(CreateJobOffersDTO offerDTO){
        if(offerDTO.companyId != null) {
            Session session = SessionService.sessions.get(offerDTO.token);
            if (session != null && session.type == UserTypeDTO.COMPANY) {
                return this.jobOfferService.addOffers(offerDTO.jobOffers, offerDTO.companyId);
            }
        }
        return false;
    }

    @POST
    @Path("/getall") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO getAllOffers(String token){
        if(token != null && token != "") {
            Session session = SessionService.sessions.get(token);
            if (session != null) {
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
        return list;
    }

    @POST
    @Path("/search/recent") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO getRecent(Integer amount){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchRecent(amount);
        return list;
    }

    @POST
    @Path("/search") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO search(SearchDTO searchDTO){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.search(searchDTO);
        return list;
    }

    @GET
    @Path("{id}")
    public JobOfferDTO getById(@PathParam("id") String id){
        return this.jobOfferService.getById(id);
    }
}
