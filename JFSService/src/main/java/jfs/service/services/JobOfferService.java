package jfs.service.services;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.mongodb.BasicDBObject;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Pair;
import jfs.data.stores.JobOfferStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.SearchDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;
import org.bson.Document;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collections;
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
        offer.address = offerDTO.address;
        offer.name = offerDTO.name;
        offer.task = offerDTO.task;
        offer.type = JobType.valueOf(offerDTO.type.name());
        offer.validUntil = offerDTO.validUntil;
        offer.startDate = offerDTO.startDate;
        offer.contactEmail = offerDTO.contactEmail;
        offer.website = offerDTO.website;

        offer.userId = userId;

        // set the geo-location
        offer.location.coordinates = offerDTO.location.coordinates;
        return offer;
    }

    public JobOfferDTO getById(String id){
        JobOfferDO offerDO = this.jobOfferStore.getById(id);
        JobOfferDTO offer = this.createOfferDTO(offerDO);
        return offer;
    }

    public Boolean addOffer(JobOfferDTO offerDTO, String userId){
        String address = offerDTO.address;
        List<Double> coordinates = this.getGeoCoordinates(address);
        offerDTO.location.coordinates = coordinates;
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
        Document locationQuery = null;
        List<Double> coordinates = null;

        if (searchDTO.address != null && searchDTO.radius != 0) {
            // get the coordinates for the address
            coordinates = getGeoCoordinates(searchDTO.address);
            //remove the address from searchDTO
            searchDTO.address = null;
        }

        ArrayList<Pair<String, Object>> pairs = new ArrayList<Pair<String, Object>>();
        if(searchDTO.address != null && !"".equals(searchDTO.address)) {
            pairs.add(new Pair("address", searchDTO.address));
        }
        if(searchDTO.type != null){
            pairs.add(new Pair("type", searchDTO.type.name()));
        }

        List<JobOfferDO> doList = this.jobOfferStore.getJobOffers(pairs, coordinates, searchDTO.radius);
        return this.createOfferDTOList(doList);
    }

    private JobOfferDTO createOfferDTO(JobOfferDO offerDO){

        JobOfferDTO jobOfferDTO =  new JobOfferDTO(
                offerDO._id, offerDO.userId, offerDO.contactEmail, offerDO.name, offerDO.function, offerDO.description,
                offerDO.task, offerDO.duration, offerDO.validUntil, offerDO.startDate,
                offerDO.address, offerDO.website, JobTypeDTO.valueOf(offerDO.type.name()));
        List<Double> coords = offerDO.location.coordinates;
        Collections.reverse(coords);
        jobOfferDTO.location.coordinates = coords;

        return jobOfferDTO;
    }

    private List<JobOfferDTO> createOfferDTOList(List<JobOfferDO> offerDOs){
        List<JobOfferDTO> offers = new ArrayList<JobOfferDTO>();
        for(JobOfferDO offerDO : offerDOs){
            offers.add(this.createOfferDTO(offerDO));
        }
        return offers;
    }

    private List<Double> getGeoCoordinates(String address) {
        // Call GeoCoding API
        GeoApiContext context = new GeoApiContext();
        context.setApiKey(googleApiKey);

        try {

            GeocodingResult[] results = GeocodingApi.newRequest(context).address(address).await();
            if (results != null) {
                if (results[0].geometry != null) {
                    double latitude = results[0].geometry.location.lat;
                    double longitude = results[0].geometry.location.lng;

                    List<Double> coordinates = new ArrayList<>();
                    coordinates.add(longitude);
                    coordinates.add(latitude);

                    return coordinates;
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }


}
