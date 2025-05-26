package airport.airport;

import airport.controller.*;
import airport.validators.*;
import airport.view.AirportFrame;

import airport.model.*;

import interfaces.*;
import repository.*;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.setProperty("flatlaf.useNativeLibrary", "false");
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                System.err.println("No se pudo cargar FlatDarkLaf: " + ex.getMessage());
            }

            IValidator<Passenger> passengerValidator = new PassengerValidator();
            IValidator<Plane> planeValidator = new PlaneValidator();
            IValidator<Location> locationValidator = new LocationValidator();
            IValidator<Flight> flightValidator = new FlightValidator();

            IPassengerRepository passengerRepo = new PassengerRepository();
            IPlaneRepository planeRepo = new PlaneRepository();
            ILocationRepository locationRepo = new LocationRepository();
            IFlightRepository flightRepo = new FlightRepository();

            PassengerController passengerController = new PassengerController(passengerValidator, passengerRepo);
            AirplaneController airplaneController = new AirplaneController(planeValidator, planeRepo);
            LocationController locationController = new LocationController(locationValidator, locationRepo);
            FlightsController flightsController = new FlightsController(flightValidator, flightRepo);

            AirportFrame frame = new AirportFrame(
                passengerController,
                airplaneController,
                locationController,
                flightsController
            );

            frame.setVisible(true);
        });
    }
}
