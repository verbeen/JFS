package jfs.service.services;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jfs.service.sessions.Session;
import jfs.transferdata.transferobjects.*;
import jfs.transferdata.transferobjects.enums.ResultTypeDTO;
import jfs.transferdata.transferobjects.enums.UserTypeDTO;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Path("/offers")
@Api(value = "/offers")
public class JobOfferWebService {
    @Inject
    private JobOfferService jobOfferService;

    @POST
    @Path("/add")
    @ApiOperation(value = "Add offer",
        notes = "A offer will be created.")
    @Consumes("application/json")
    @Produces("application/json")
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
    @Path("/addmore")
    @ApiOperation(value = "Add multiple offers",
        notes = "Multiple offers will be created.")
    @Consumes("application/json")
    @Produces("application/json")
    public Boolean addOffers(CreateJobOffersDTO offerDTO) {
        if (offerDTO.companyId != null) {
            Session session = SessionService.sessions.get(offerDTO.token);
            if (session != null && session.type == UserTypeDTO.COMPANY) {
                return this.jobOfferService.addOffers(offerDTO.jobOffers, offerDTO.companyId);
            }
        }
        return false;
    }

    @POST
    @Path("/getall")
    @ApiOperation(value = "Get all offers",
        notes = "Returns an array of all offers.")
    @Consumes("application/json")
    @Produces("application/json")
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
    @Path("/search/text")
    @ApiOperation(value = "Search by text",
        notes = "Returns an array of offers that match exactly a specific string.")
    @Consumes("application/json")
    @Produces("application/json")
    public JobOfferListDTO searchText(String term) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchText(term);
        return list;
    }

    @POST
    @Path("/search/recent")
    @ApiOperation(value = "Recent offers",
        notes = "Returns an array of offers.")
    @Consumes("application/json")
    @Produces("application/json")
    public JobOfferListDTO getRecent(Integer amount) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.searchRecent(amount);
        return list;
    }

    @POST
    @Path("/search")
    @ApiOperation(value = "Search offer",
        notes = "Returns an array of offers that match a set of specified criteria")
    @Consumes("application/json")
    @Produces("application/json")
    public JobOfferListDTO search(SearchDTO searchDTO) {
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.search(searchDTO);
        return list;
    }

    @GET
    @Path("{id}")
    @ApiOperation(value = "Get offer",
        notes = "Returns one specific offer.")
    public JobOfferDTO getById(@PathParam("id") String id) {
        return this.jobOfferService.getById(id);
    }
}
