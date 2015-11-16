package jfs.data.dataobjects;

import jfs.data.dataobjects.enums.JobType;
import org.bson.BsonDocument;
import org.bson.Document;

import java.time.Duration;

/**
 * Created by lpuddu on 10-11-2015.
 */
public class JobOfferDO {
    public String id;
    public String userId;
    public String name;
    public String function;
    public String description;
    public String task;
    public Long duration;
    public Long validUntil;
    public Long startDate;
    public String location;
    public String website;
    public JobType type;
}