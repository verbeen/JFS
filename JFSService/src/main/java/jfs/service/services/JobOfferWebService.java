package jfs.service.services;


import jfs.transferdata.transferobjects.CreateJobOfferDTO;
import jfs.transferdata.transferobjects.CreateJobOffersDTO;
import jfs.transferdata.transferobjects.JobOfferListDTO;
import jfs.transferdata.transferobjects.SearchDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
        if(offerDTO.companyId != null && offerDTO.companyId.equals(SessionService.sessions.get(offerDTO.token))) {
            return this.jobOfferService.addOffer(offerDTO.jobOffer, offerDTO.companyId);
        } else {
            return false;
        }
    }

    @POST
    @Path("/addmore") @Consumes("application/json") @Produces("application/json")
    public Boolean addOffers(CreateJobOffersDTO offerDTO){
        if(offerDTO.companyId != null && offerDTO.companyId.equals(SessionService.sessions.get(offerDTO.token))) {
            return this.jobOfferService.addOffers(offerDTO.jobOffers, offerDTO.companyId);
        } else {
            return false;
        }
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
    public JobOfferListDTO getRecent(int amount){
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
}
