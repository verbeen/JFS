package jfs.service.services;

import com.mongodb.BasicDBObject;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Pair;
import jfs.data.stores.JobOfferStore;
import jfs.transferdata.transferobjects.JobOfferDTO;
import jfs.transferdata.transferobjects.SearchDTO;
import jfs.transferdata.transferobjects.enums.JobTypeDTO;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 *
 * Job offer service wrapper for jobOfferStore
 *
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
        offer.location = offerDTO.location;
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

    //Get all available job offers as List<JobOfferDTO>
    public List<JobOfferDTO> getAllOffers(){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getAllOffers();
        return this.createOfferDTOList(offerDOs);
    }

    public List<JobOfferDTO> getAllOffersCompany(String companyId){
        List<JobOfferDO> offerDOs = this.jobOfferStore.getAllOffersCompany(companyId);
        return this.createOfferDTOList(offerDOs);
    }

    //Search for specific job offers by search criteria specified in SearchDTO
    //Returns a list of job offers as List<JobOfferDTO>
    public List<JobOfferDTO> search(SearchDTO searchDTO){
        ArrayList<Pair<String, Object>> pairs = new ArrayList<Pair<String, Object>>();
        if(searchDTO.location != null && !"".equals(searchDTO.location)){
            pairs.add(new Pair("location", new BasicDBObject("$regex", searchDTO.location)));
        }
        if(searchDTO.skills != null && !"".equals(searchDTO.skills)){
            /*
                For easier creation of the search string and array is created
                which also removes all whitespaces before or after commas that are seperating the skills
                \\Q...\\E is used to quote string in Regex. This is necessary in order to not take e.g. the + character as a command
                | is used to combine all skills by OR
            */
            List<String> myList = Arrays.asList(searchDTO.skills.split("\\s*,\\s*"));

            String skillsRegex = "\\Q";
            if (myList.size() > 1) {
                for (int i = 0; i < myList.size(); i++){
                    skillsRegex += myList.get(i);
                    if (i != myList.size() -1) {
                        skillsRegex += "\\E|\\Q";
                    }
                }
            }else{
                skillsRegex += myList.get(0);
            }
            skillsRegex += "\\E";

            pairs.add(new Pair("skills", new BasicDBObject("$regex", skillsRegex)));
        }
        if(searchDTO.type != null && searchDTO.type != JobTypeDTO.all){
            pairs.add(new Pair("type", searchDTO.type.name()));
        }
        List<JobOfferDO> doList = this.jobOfferStore.getJobOffers(pairs);
        return this.createOfferDTOList(doList);
    }

    //Create an JobOfferDTO out of an JobOfferDO
    private JobOfferDTO createOfferDTO(JobOfferDO offerDO){
        return new JobOfferDTO(
                offerDO._id, offerDO.userId, offerDO.contactEmail, offerDO.name, offerDO.function, offerDO.description,
                offerDO.task, offerDO.skills, offerDO.duration, offerDO.validUntil, offerDO.startDate,
                offerDO.location, offerDO.website, JobTypeDTO.valueOf(offerDO.type.name())
        );
    }

    //Create a List<JobOfferDTO> out of List<JobOfferDO>
    public List<JobOfferDTO> createOfferDTOList(List<JobOfferDO> offerDOs){
        List<JobOfferDTO> offers = new ArrayList<JobOfferDTO>();
        for(JobOfferDO offerDO : offerDOs){
            offers.add(this.createOfferDTO(offerDO));
        }
        return offers;
    }

    //Delete a job offer by jobOfferId
    //Returns boolean for success
    public boolean delete(String jobOfferId){
        boolean result = this.jobOfferStore.delete(jobOfferId);
        return result;
    }
}
