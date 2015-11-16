package jfs.transferdata.transferobjects;


import jfs.transferdata.transferobjects.enums.JobTypeDTO;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class SearchDTO {
    public String function;
    public long duration;
    public long validity; //TODO don't care about validity
    public JobTypeDTO jobType;
}
