package jfs.service.transferobjects;

import jfs.data.dataobjects.enums.JobType;

import java.time.Duration;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferDTO {
    public String name;
    public String function;
    public String description;
    public String task;
    public Duration duration;
    public long validUntil;
    public String location;
    public String website;
    public JobType type;
}
