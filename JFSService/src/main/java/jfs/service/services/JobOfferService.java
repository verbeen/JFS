package jfs.service.services;

import jfs.data.dataobjects.JobOfferDO;
import jfs.data.stores.JobOfferStore;

import javax.ejb.Singleton;
import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 */
@Singleton
public class JobOfferService {
    private JobOfferStore jobOfferStore = JobOfferStore.store;

    public List<JobOfferDO> quickSearch(String term){
        return this.jobOfferStore.getJobOffersText(term);
    }
}
