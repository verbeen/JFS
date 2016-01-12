package jfs.transferdata.transferobjects;


import jfs.transferdata.transferobjects.enums.JobTypeDTO;

import java.util.List;

/**
 * Created by lpuddu on 12-11-2015.
 */
public class SearchDTO {
    public int amount;
    public List<Double> coordinates;
    public int radius;
    public String skills;
    public JobTypeDTO type;
}
