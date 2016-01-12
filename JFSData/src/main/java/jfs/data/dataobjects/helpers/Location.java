package jfs.data.dataobjects.helpers;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lpuddu on 11-1-2016.
 */
public class Location {
    public String address;
    public List<Double> coordinates;

    public Location(){

    }

    public Location(String address, double lat, double lng) {
        this.address = address;
        this.coordinates = Arrays.asList(lng, lat);
    }
}
