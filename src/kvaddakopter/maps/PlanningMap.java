package kvaddakopter.maps;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import netscape.javascript.JSObject;

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


    private ArrayList<GPSCoordinate> placedMarkers;


    /**
     * Constructor
     *
     * @param mapView A valid GoogleMapView obtained from the GUI XML.
     */
    public PlanningMap(GoogleMapView mapView) {
        this.map = new GoogleMap();
        this.mapView = mapView;
        this.mapView.addMapInializedListener(this);
        this.placedMarkers = new ArrayList<GPSCoordinate>();
    }


    /**
     * WHEN MAP IS READY THIS RUNS ONCE.
     */
    @Override
    public void mapInitialized() {

        this.createMapWithStartLocation(58.409719, 15.622071);

        this.addMapEventListeners();

    }


    /**
     * Sets alla event listeners for the map.
     */
    private void addMapEventListeners() {

        //EVENT FOR USER CLICKED MAP
        this.map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            GPSCoordinate coord = new GPSCoordinate(ll.getLatitude(), ll.getLongitude());
            this.addNavigationPoint(coord);
        });
    }


    /**
     * Used to initialize the wanted map with given options.
     *
     * @param startLat  Map center start Latitude.
     * @param startLong Map center start Longitude.
     * @return GoogleMap instance
     */
    private void createMapWithStartLocation(double startLat, double startLong) {
        LatLong mapStartingPosition = new LatLong(startLat, startLong);
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(mapStartingPosition)
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(17);
        this.map = mapView.createMap(mapOptions);
    }


    /**
     * Add a navigation point to the given GPS Coordinate.
     *
     * @param coordinate
     */
    public void addNavigationPoint(GPSCoordinate coordinate) {
        Marker marker = RouteMarker.create(coordinate.getLatitude(), coordinate.getLongitude(), MapMarkerEnum.NAVIGATION_NORMAL);
        this.placedMarkers.add(coordinate);
        map.addMarker(marker);
    }


    /**
     * Uses vector of coordinates and add navigation points at these locations.
     *
     * @param pointList Array of GPSCoordinates
     */
    public void addListOfNavigationPoints(ArrayList<GPSCoordinate> pointList) {
        pointList.forEach(this::addNavigationPoint);
    }


    /**
     * Returns an array of all placed markers
     * @return
     */
    public ArrayList<GPSCoordinate> allPlacedCoordinates() {
        return this.placedMarkers;
    }

}
