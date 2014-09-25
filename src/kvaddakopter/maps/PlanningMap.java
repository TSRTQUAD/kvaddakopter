package kvaddakopter.maps;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;

import java.util.ArrayList;

/**
 * Used as a high-level representation of the Google Map used for planning.
 */
public class PlanningMap implements MapComponentInitializedListener {


    /**
     * View that represent the map view.
     */
    private GoogleMapView mapView;


    /**
     * The object representing the Map itself
     */
    private GoogleMap map;


    /**
     * The position where the maps should be centered at startup.
     */


    /**
     * Constructor
     *
     * @param mapView
     */
    public PlanningMap(GoogleMapView mapView) {
        this.map = new GoogleMap();
        this.mapView = mapView;
        this.mapView.addMapInializedListener(this);

    }


    @Override
    public void mapInitialized() {
        LatLong mapStartingPosition = new LatLong(58.409719, 15.622071);
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(mapStartingPosition)
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(18);

        map = mapView.createMap(mapOptions);


    }


    /**
     * Uses vector of coordinates and add navigation points at these locations.
     *
     * @param coordinates
     */
    public void addNavigationPoint(double[][] coordinates) {
        //Add markers to the map
        for (int i = 0; i < coordinates.length; i++) {
            map.addMarker( RouteMarker.create(coordinates[i], i, coordinates.length) );
        }
    }
}
