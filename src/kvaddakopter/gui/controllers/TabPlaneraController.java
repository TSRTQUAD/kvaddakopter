package kvaddakopter.gui.controllers;


import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import kvaddakopter.maps.PlanningMap;


public class TabPlaneraController implements Initializable {


    /**
     * The GUI representation of the Google Map.
     */
    @FXML
    private GoogleMapView mapView;


    /**
     * High level API for interacting with the Map
     */
    private PlanningMap planningMap;


    /**
     * GUI Button "Spara uppdrag". Should trigger function to save the project.
     */
    @FXML
    private Button btnSaveMission;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.planningMap = new PlanningMap(this.mapView);
        this.setEventHandlers();
    }


    /**
     * Used to add all event listeners in the planning tab.
     */
    private void setEventHandlers() {

        // Event triggered when clicking "Save mission" button.
        this.btnSaveMission.setOnAction(e -> {
            double coordinates[][] = {
                    {58.407866, 15.622300},
                    {58.408300, 15.622150},
                    {58.408500, 15.622075},
                    {58.408600, 15.622035},
                    {58.408745, 15.622005}
            };
            this.planningMap.addNavigationPoint(coordinates);
        });
    }


}