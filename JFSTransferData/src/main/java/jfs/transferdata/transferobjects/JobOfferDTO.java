package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.JobTypeDTO;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferDTO {
    public String companyId;
    public String name;
    public String function;
    public String description;
    public String task;
    public long duration;
    public long validUntil;
    public long startDate;
    public String location;
    public String website;
    public JobTypeDTO type;

    public JobOfferDTO(){

    }

    public JobOfferDTO(String companyId, String name, String function, String description, String task, long duration, long validUntil, long startDate, String location, String website, JobTypeDTO type) {
        this.companyId = companyId;
        this.name = name;
        this.function = function;
        this.description = description;
        this.task = task;
        this.duration = duration;
        this.validUntil = validUntil;
        this.startDate = startDate;
        this.location = location;
        this.website = website;
        this.type = type;
    }
}
