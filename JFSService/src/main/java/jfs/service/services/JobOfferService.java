package jfs.service.services;

import javafx.util.Pair;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.dataobjects.enums.JobType;
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
        offer.website = offerDTO.website;

        offer.userId = userId;

        return offer;
    }

    public JobOfferDTO getById(String id){
        JobOfferDO offerDO = this.jobOfferStore.getById(id);
        JobOfferDTO offer = this.createOfferDTO(offerDO);
        return offer;
    }

    public Boolean addOffer(JobOfferDTO offerDTO, String userId){
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
        if(searchDTO.duration != 0){
            pairs.add(new Pair("duraction", searchDTO.duration));
        }
        if(searchDTO.function != null && searchDTO.function != ""){
            pairs.add(new Pair("function", searchDTO.function));
        }
        if(searchDTO.validity != 0){
            pairs.add(new Pair("validity", searchDTO.validity));
        }
        if(searchDTO.jobType != null){
            pairs.add(new Pair("jobType", JobType.valueOf(searchDTO.jobType.name())));
        }
        List<JobOfferDO> doList = this.jobOfferStore.getJobOffers(pairs);
        return this.createOfferDTOList(doList);
    }

    private JobOfferDTO createOfferDTO(JobOfferDO offerDO){
        return new JobOfferDTO(
                offerDO._id, offerDO.userId , offerDO.name, offerDO.function, offerDO.description,
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
