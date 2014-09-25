package kvaddakopter.maps;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

/**
 * Created by per on 2014-09-25.
 */
public class RouteMarker {


    /**
     * Factory for creating Rotue markers.
     *
     * @param coordinate       For the new Marker
     * @param routeNr          The current nb in coordinate array If looping - Use 1 if you want a start marker (Green) and
     *                         if routeNb == totalNrOfMarkers  --> marker will be red.
     *                         otherwise it will be Yellow
     * @param totalNrOfMarkers The length of the coordinate array.
     * @return Marker
     */
    public static Marker create(double[] coordinate, int routeNr, int totalNrOfMarkers) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLong(coordinate[0], coordinate[1]));
        markerOptions.title(String.valueOf(routeNr + 1));
        markerOptions.icon(RouteMarker.figureOutIconType(routeNr+1, totalNrOfMarkers));
        return new Marker(markerOptions);
    }


    /**
     * Uses counters to figure out what icon to use for the marker.
     *
     * @param currentCount
     * @param totalCount
     * @return
     */
    private static String figureOutIconType(int currentCount, int totalCount) {
        String type = "gps-marker.png";
        if (currentCount == 1) type = "gps-marker_start.png";
        if (currentCount == totalCount) type = "gps-marker_end.png";
        return type;
    }
}
