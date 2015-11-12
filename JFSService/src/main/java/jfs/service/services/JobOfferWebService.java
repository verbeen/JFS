package jfs.service.services;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.stores.JobOfferStore;
import jfs.service.transferobjects.JobOfferListDTO;

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
    @Path("/text") @Consumes("application/json") @Produces("application/json")
    public JobOfferListDTO quickSearch(String term){
        JobOfferListDTO list = new JobOfferListDTO();
        list.offers = this.jobOfferService.quickSearch(term);
        return list;
    }
}
