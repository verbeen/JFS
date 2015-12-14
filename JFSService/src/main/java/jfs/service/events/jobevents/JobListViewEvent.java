package jfs.service.events.jobevents;

import jfs.transferdata.transferobjects.JobOfferDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 14-12-2015.
 */
public class JobListViewEvent {
    public List<JobOfferDTO> jobOffers = new ArrayList<JobOfferDTO>();

    public JobListViewEvent(List<JobOfferDTO> jobOffers) {
        this.jobOffers = jobOffers;
    }
}
