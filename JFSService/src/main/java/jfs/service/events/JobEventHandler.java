package jfs.service.events;

import jfs.data.stores.JobOfferMetricsStore;
import jfs.service.events.jobevents.JobDetailViewEvent;
import jfs.service.events.jobevents.JobListViewEvent;
import jfs.service.events.jobevents.annotations.*;
import jfs.transferdata.transferobjects.JobOfferDTO;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 14-12-2015.
 *
 * EventHandler for JobOfferMetricsStore
 *
 */
@Singleton @Asynchronous
public class JobEventHandler {
    private JobOfferMetricsStore store = JobOfferMetricsStore.jobOfferMetricsStore;


    public void viewJobList(@Observes @JobListView JobListViewEvent listViews){
        List<String> ids = new ArrayList<String>();
        for(JobOfferDTO offer : listViews.jobOffers){
            ids.add(offer.offerId);
        }
        this.store.incrementListViewCountMany(ids);
    }

    public void viewJobDetail(@Observes @JobDetailView JobDetailViewEvent jobDetailView){
        this.store.incrementDetailViewCount(jobDetailView.jobOfferId);
    }
}
