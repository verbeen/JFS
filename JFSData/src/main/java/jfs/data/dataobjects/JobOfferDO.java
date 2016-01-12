package jfs.data.dataobjects;

import jfs.data.dataobjects.enums.JobType;
import jfs.data.dataobjects.helpers.Location;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class JobOfferDO extends DataObject{
    public String userId;
    public String name;
    public String function;
    public String description;
    public String task;
    public String skills;
    public Long duration;
    public Long validUntil;
    public Long startDate;
    public Location location;
    public String website;
    public String contactEmail;
    public JobType type;
}
