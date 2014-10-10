package kvaddakopter.gui.controllers;


import com.lynden.gmapsfx.GoogleMapView;
import java.net.URL;
import java.util.ResourceBundle;

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
     * EXCLUDING MAP EVENTS THIS IS HANDLED BY THE MAP ABSTRACTION CLASS.
     * Used to add all event listeners in the planning tab.
     */
    private void setEventHandlers() {

        // Event triggered when clicking "Save mission" button.
        this.btnSaveMission.setOnAction(e -> {
            //Save the mission!!!
        });

    }


}