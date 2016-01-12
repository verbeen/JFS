package jfs.service.services;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Location;
import jfs.data.stores.JobOfferStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.SearchDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;
import org.bson.Document;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 *
 * Job offer service wrapper for jobOfferStore
 */
@Singleton
public class JobOfferService {
    private JobOfferStore jobOfferStore = JobOfferStore.store;

    //Used for creating an JobOfferDO out of an JobOfferDTO
    private JobOfferDO createOfferDO(JobOfferDTO offerDTO, String userId){
        JobOfferDO offer = new JobOfferDO();

        offer.description = offerDTO.description;
        offer.duration = offerDTO.duration;
        offer.function = offerDTO.function;
        offer.location = new Location(offerDTO.address, offerDTO.lat, offerDTO.lng);
        offer.name = offerDTO.name;
        offer.task = offerDTO.task;
        offer.skills = offerDTO.skills;
        offer.type = JobType.valueOf(offerDTO.type.name());
        offer.validUntil = offerDTO.validUntil;
        offer.startDate = offerDTO.startDate;
        offer.contactEmail = offerDTO.contactEmail;
        offer.website = offerDTO.website;
        offer.userId = userId;

        return offer;
    }

    //Get JobOfferDTO by id
    //Query the jobOfferStore and create an JobOfferDTO
    public JobOfferDTO getById(String id){
        JobOfferDO offerDO = this.jobOfferStore.getById(id);
        JobOfferDTO offer = this.createOfferDTO(offerDO);
        return offer;
    }

    //Add an job offer from an JobOfferDTO and userId
    //Returns boolean for success
    public Boolean addOffer(JobOfferDTO offerDTO, String userId){
        return this.jobOfferStore.addOffer(this.createOfferDO(offerDTO, userId), userId);
    }

    //Add a list of job offers with List<JobOfferDTO
    //Returns boolean for success
    public Boolean addOffers(List<JobOfferDTO> offerDTOs, String userId){
        List<JobOfferDO> offers = new ArrayList<JobOfferDO>();
        for(JobOfferDTO offerDTO : offerDTOs){
            offers.add(this.createOfferDO(offerDTO, userId));
        }
        return this.jobOfferStore.addOffers(offers, userId);
    }

    //Get job offers by seach term as List<JobOfferDTO>
    public List<JobOfferDTO> searchText(String term){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getJobOffersText(term);
        return this.createOfferDTOList(offerDOs);
    }

    //Get recent job offers limited by amount as List<JobOfferDTO>
    public List<JobOfferDTO> searchRecent(int amount){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getJobOffersRecent(amount);
        return this.createOfferDTOList(offerDOs);
    }

    /***
     * Get all available job offers as List<JobOfferDTO>
     */
    public List<JobOfferDTO> getAllOffers(){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getAllOffers();
        return this.createOfferDTOList(offerDOs);
    }

    public List<JobOfferDTO> getAllOffersCompany(String companyId){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getAllOffersCompany(companyId);
        return this.createOfferDTOList(offerDOs);
    }

    /***
     * Search for specific job offers by search criteria specified in SearchDTO
     * @param searchDTO contains search terms to be used.
     * @return Returns a list of job offers as List<JobOfferDTO>
     */
    public List<JobOfferDTO> search(SearchDTO searchDTO){
        Document document = new Document();
        if(searchDTO.coordinates != null && searchDTO.coordinates.size() == 2 && searchDTO.radius != 0){
            this.jobOfferStore.appendGeoQuery(document, searchDTO.coordinates.get(0), searchDTO.coordinates.get(1), searchDTO.radius);
        }
        if(searchDTO.skills != null && !"".equals(searchDTO.skills)){
            this.jobOfferStore.appendSkillsQuery(document, searchDTO.skills);
        }
        if(searchDTO.type != null && searchDTO.type != JobTypeDTO.all){
            this.jobOfferStore.appendTypeQuery(document, searchDTO.type.name());
        }
        if(searchDTO.address != null && searchDTO.address != ""){
            this.jobOfferStore.appendAddressQuery(document, searchDTO.address);
        }

        List<JobOfferDO> doList = this.jobOfferStore.getJobOffers(document, searchDTO.amount);
        return this.createOfferDTOList(doList);
    }

    /***
     * Create a JobOfferDTO out of a JobOfferDO
     */
    private JobOfferDTO createOfferDTO(JobOfferDO offerDO){
        List<Double> coordinates = offerDO.location.coordinates;
        return new JobOfferDTO(
                offerDO._id, offerDO.userId, offerDO.contactEmail, offerDO.name,
                offerDO.function, offerDO.description, offerDO.task, offerDO.skills,
                offerDO.duration, offerDO.validUntil, offerDO.startDate,
                offerDO.location.address, coordinates.get(1), coordinates.get(0),
                offerDO.website, JobTypeDTO.valueOf(offerDO.type.name())
        );
    }

    /***
     * Create a List<JobOfferDTO> out of List<JobOfferDO>
     * @param offerDOs list of offerDOs with which the result will be populated
     * @return list of JobOfferDTO, each populated with data from the original list.
     */
    public List<JobOfferDTO> createOfferDTOList(List<JobOfferDO> offerDOs){
        List<JobOfferDTO> offers = new ArrayList<JobOfferDTO>();
        for(JobOfferDO offerDO : offerDOs){
            offers.add(this.createOfferDTO(offerDO));
        }
        return offers;
    }

    public boolean delete(String jobOfferId){
        boolean result = this.jobOfferStore.delete(jobOfferId);
        return result;
    }
}
