package jfs.service.services;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Pair;
import jfs.data.stores.JobOfferStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.SearchDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Singleton
public class JobOfferService {
    private JobOfferStore jobOfferStore = JobOfferStore.store;
    private static final String googleApiKey = "AIzaSyANAUSC2OFBrJXnqwzMeJtqU1JNnA47xto";

    private JobOfferDO createOfferDO(JobOfferDTO offerDTO, String userId){
        JobOfferDO offer = new JobOfferDO();

        offer.description = offerDTO.description;
        offer.duration = offerDTO.duration;
        offer.function = offerDTO.function;
        offer.location = offerDTO.location;
        offer.name = offerDTO.name;
        offer.task = offerDTO.task;
        offer.type = JobType.valueOf(offerDTO.type.name());
        offer.validUntil = offerDTO.validUntil;
        offer.startDate = offerDTO.startDate;
        offer.contactEmail = offerDTO.contactEmail;
        offer.website = offerDTO.website;

        offer.userId = userId;
        offer.latitude = offerDTO.latitude;
        offer.longitude = offerDTO.longitude;

        return offer;
    }

    public JobOfferDTO getById(String id){
        JobOfferDO offerDO = this.jobOfferStore.getById(id);
        JobOfferDTO offer = this.createOfferDTO(offerDO);
        return offer;
    }

    public Boolean addOffer(JobOfferDTO offerDTO, String userId){
        String location = offerDTO.location;
        String latitude;
        String longitude;
         // Call GeoCoding API
        GeoApiContext context = new GeoApiContext();
        context.setApiKey(googleApiKey);

        try {

            GeocodingResult[] results = GeocodingApi.newRequest(context).address(location).await();
            if (results != null) {
                if (results[0].geometry != null) {
                    latitude = String.valueOf(results[0].geometry.location.lat);
                    longitude = String.valueOf(results[0].geometry.location.lng);
                    offerDTO.latitude = latitude;
                    offerDTO.longitude = longitude;
                }
            }
        } catch (Exception ex) {

        }
        // update
        return this.jobOfferStore.addOffer(this.createOfferDO(offerDTO, userId));
    }

    public Boolean addOffers(List<JobOfferDTO> offerDTOs, String userId){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        for(JobOfferDTO offerDTO : offerDTOs){
            offers.add(this.createOfferDO(offerDTO, userId));
        }
        return this.jobOfferStore.addOffers(offers);
    }

    public List<JobOfferDTO> searchText(String term){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getJobOffersText(term);
        return this.createOfferDTOList(offerDOs);
    }

    public List<JobOfferDTO> searchRecent(int amount){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getJobOffersRecent(amount);
        return this.createOfferDTOList(offerDOs);
    }

    public List<JobOfferDTO> getAllOffers(){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getAllOffers();
        return this.createOfferDTOList(offerDOs);
    }

    public List<JobOfferDTO> search(SearchDTO searchDTO){
        ArrayList<Pair<String, Object>> pairs = new ArrayList<Pair<String, Object>>();
        if(searchDTO.location != null && !"".equals(searchDTO.location)){
            pairs.add(new Pair("location", searchDTO.location));
        }
        if(searchDTO.type != null){
            pairs.add(new Pair("type", searchDTO.type.name()));
        }
        List<JobOfferDO> doList = this.jobOfferStore.getJobOffers(pairs);
        return this.createOfferDTOList(doList);
    }

    private JobOfferDTO createOfferDTO(JobOfferDO offerDO){
        return new JobOfferDTO(
                offerDO._id, offerDO.userId, offerDO.contactEmail, offerDO.name, offerDO.function, offerDO.description,
                offerDO.task, offerDO.duration, offerDO.validUntil, offerDO.startDate,
                offerDO.location, offerDO.website, JobTypeDTO.valueOf(offerDO.type.name())
        );
    }

    private List<JobOfferDTO> createOfferDTOList(List<JobOfferDO> offerDOs){
        List<JobOfferDTO> offers = new ArrayList<JobOfferDTO>();
        for(JobOfferDO offerDO : offerDOs){
            offers.add(this.createOfferDTO(offerDO));
        }
        return offers;
    }
}
