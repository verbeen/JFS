package jfs.transferdata.transferobjects;

import jfs.transferdata.transferobjects.enums.JobTypeDTO;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class JobOfferDTO {
    public String offerId;
    public String companyId;
    public String contactEmail;
    public String name;
    public String function;
    public String description;
    public String task;
    public String skills;
    public long duration;
    public long validUntil;
    public long startDate;
    public String address;
    public Double lat;
    public Double lng;
    public String website;
    public JobTypeDTO type;

    public JobOfferDTO(){

    }

    public JobOfferDTO(String offerId, String companyId, String contactEmail, String name, String function, String description, String task, String skills, long duration, long validUntil, long startDate, String address, Double lat, Double lng, String website, JobTypeDTO type) {
        this.offerId = offerId;
        this.companyId = companyId;
        this.contactEmail = contactEmail;
        this.name = name;
        this.function = function;
        this.description = description;
        this.task = task;
        this.skills = skills;
        this.duration = duration;
        this.validUntil = validUntil;
        this.startDate = startDate;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.website = website;
        this.type = type;
    }
}
