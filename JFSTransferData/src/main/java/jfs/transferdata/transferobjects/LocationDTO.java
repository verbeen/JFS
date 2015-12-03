package jfs.transferdata.transferobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepika on 12/3/2015.
 */
public class LocationDTO {
    public List<String> coordinates;
    public String type="Point";

    public LocationDTO(){
        coordinates = new ArrayList<>();
        type="Point";
    }
}
