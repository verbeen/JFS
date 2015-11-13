package jfs.service.services;

import javafx.util.Pair;
import jfs.data.dataobjects.JobOfferDO;
import jfs.data.stores.JobOfferStore;
import jfs.service.transferobjects.JobOfferDTO;
import jfs.service.transferobjects.SearchDTO;

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
        offer.type = offerDTO.type;
        offer.validUntil = offerDTO.validUntil;
        offer.website = offerDTO.website;

        offer.userId = userId;

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

    public List<JobOfferDO> searchText(String term){
        return this.jobOfferStore.getJobOffersText(term);
    }

    public List<JobOfferDO> searchRecent(int amount){
        return this.jobOfferStore.getJobOffersRecent(amount);
    }

    public List<JobOfferDO> search(SearchDTO searchDTO){
        ArrayList<Pair<String, Object>> pairs = new ArrayList<Pair<String, Object>>();
        if(searchDTO.duration != 0){
            pairs.add(new Pair("duraction", searchDTO.duration));
        }
        if(searchDTO.function != null || searchDTO.function != ""){
            pairs.add(new Pair("function", searchDTO.function));
        }
        if(searchDTO.validity != 0){
            pairs.add(new Pair("validity", searchDTO.validity));
        }
        if(searchDTO.jobType != null){
            pairs.add(new Pair("jobType", searchDTO.jobType));
        }
        return this.jobOfferStore.getJobOffers(pairs);
    }
}
