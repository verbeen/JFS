package jfs.data.dataobjects.helpers;

/**
 * Created by lpuddu on 11-1-2016.
 */
public class GeoJSONPoint {
    public String type = "Point";
    public Double[] coordinates;

    public GeoJSONPoint(){

    }

    public GeoJSONPoint(Double lat, Double lng){
        this.coordinates = new Double[] { lat, lng };
    }

    public GeoJSONPoint(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
