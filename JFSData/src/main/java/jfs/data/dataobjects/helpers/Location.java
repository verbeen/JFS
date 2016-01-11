package jfs.data.dataobjects.helpers;

/**
 * Created by lpuddu on 11-1-2016.
 */
public class Location {
    public String address;
    public GeoJSONPoint coordinates;

    public Location(){

    }

    public Location(String address, GeoJSONPoint coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }
}
