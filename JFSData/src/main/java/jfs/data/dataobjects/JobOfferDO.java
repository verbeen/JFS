package jfs.data.dataobjects;

import jfs.data.dataobjects.enums.JobType;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class JobOfferDO extends DataObject{
    public String userId;
    public String name;
    public String function;
    public String description;
    public String task;
    public Long duration;
    public Long validUntil;
    public Long startDate;
    public String address;
    public String website;
    public String contactEmail;
    public JobType type;
    public LocationDO location;
}
