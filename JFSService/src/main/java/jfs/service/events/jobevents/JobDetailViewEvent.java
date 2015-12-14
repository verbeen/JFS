package jfs.service.events.jobevents;

/**
 * Created by lpuddu on 14-12-2015.
 */
public class JobDetailViewEvent {
    public String jobOfferId;

    public JobDetailViewEvent(String jobOfferId) {
        this.jobOfferId = jobOfferId;
    }
}
