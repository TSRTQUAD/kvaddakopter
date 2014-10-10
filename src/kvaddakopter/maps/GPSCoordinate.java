package kvaddakopter.maps;

import java.util.ArrayList;
import java.util.List;

/**
 * Object is used to represent a GPS-coordinate
 * Created by per on 2014-09-29.
 */
public class GPSCoordinate {


    private final double latitude;


    private final double longitude;


    public GPSCoordinate(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return this.latitude;
    }


    public double getLongitude() {
        return this.longitude;
    }


    public List coordAsArray() {
        List coords = new ArrayList<Double>();
        coords.add(this.latitude);
        coords.add(this.longitude);
        return coords;
    }
}
