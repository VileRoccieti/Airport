/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import airport.model.Flight;
import airport.model.Location;
import airport.model.Plane;
import interfaces.IFlightRepository;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author User
 */
public class FlightRepository implements IFlightRepository {

    private final File file = new File("json/flights.json");
    private final List<Flight> flights = new ArrayList<>();

    public FlightRepository() {
        loadFromFile();
    }

    @Override
    public void save(Flight flight) {
        if (findById(flight.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un vuelo con ese ID.");
        }
        flights.add(flight);
        saveToFile();
    }

    @Override
    public Flight findById(String id) {
        for (Flight f : flights) {
            if (f.getId().equalsIgnoreCase(id)) return f;
        }
        return null;
    }

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>(flights);
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String id = obj.getString("id");
                String planeId = obj.getString("plane");
                String departureId = obj.getString("departureLocation");
                String arrivalId = obj.getString("arrivalLocation");
                String scaleId = obj.opt("scaleLocation") == JSONObject.NULL ? null : obj.optString("scaleLocation", null);

                LocalDateTime departureDate = LocalDateTime.parse(obj.getString("departureDate"));

                int hArrival = obj.optInt("hoursDurationArrival", 0);
                int mArrival = obj.optInt("minutesDurationArrival", 0);
                int hScale = obj.optInt("hoursDurationScale", 0);
                int mScale = obj.optInt("minutesDurationScale", 0);

                Plane plane = new Plane(planeId);
                Location dep = new Location(departureId);
                Location arr = new Location(arrivalId);
                Location scale = (scaleId != null) ? new Location(scaleId) : null;

                Flight flight = (scale == null)
                        ? new Flight(id, plane, dep, arr, departureDate, hArrival, mArrival)
                        : new Flight(id, plane, dep, scale, arr, departureDate, hArrival, mArrival, hScale, mScale);

                flights.add(flight);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar vuelos: " + e.getMessage());
        }
    }

    private void saveToFile() {
        JSONArray array = new JSONArray();
        for (Flight f : flights) {
            JSONObject obj = new JSONObject();
            obj.put("id", f.getId());
            obj.put("plane", f.getPlane().getId());
            obj.put("departureLocation", f.getDepartureLocation().getId());
            obj.put("arrivalLocation", f.getArrivalLocation().getId());
            obj.put("departureDate", f.getDepartureDate().toString());
            obj.put("hoursDurationArrival", f.getHoursDurationArrival());
            obj.put("minutesDurationArrival", f.getMinutesDurationArrival());
            obj.put("hoursDurationScale", f.getHoursDurationScale());
            obj.put("minutesDurationScale", f.getMinutesDurationScale());

            if (f.getScaleLocation() != null) {
                obj.put("scaleLocation", f.getScaleLocation().getId());
            } else {
                obj.put("scaleLocation", JSONObject.NULL);
            }

            array.put(obj);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            System.err.println("Error al guardar vuelos: " + e.getMessage());
        }
    }
}
