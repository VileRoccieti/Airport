/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author User
 */
import airport.model.Location;
import interfaces.ILocationRepository;
import org.json.*;

import java.io.*;
import java.util.*;

public class LocationRepository implements ILocationRepository {

    private final File file = new File("json/locations.json");
    private final List<Location> locations = new ArrayList<>();

    public LocationRepository() {
        loadFromFile();
    }

    @Override
    public void save(Location location) {
        if (findById(location.getId()) != null) {
            throw new IllegalArgumentException("La ubicación ya existe");
        }
        locations.add(location);
        saveToFile();
    }

    @Override
    public Location findById(String id) {
        for (Location loc : locations) {
            if (loc.getId().equalsIgnoreCase(id)) {
                return loc;
            }
        }
        return null;
    }

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations); // retornamos copia
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                if (!obj.has("airportId")) {
                    System.err.println("Ubicación ignorada por falta de 'airportId': " + obj);
                    continue;
                }

                String id = obj.getString("airportId");
                String name = obj.optString("airportName", "UNKNOWN");
                String city = obj.optString("airportCity", "UNKNOWN");
                String country = obj.optString("airportCountry", "UNKNOWN");
                double lat = obj.optDouble("airportLatitude", 0);
                double lon = obj.optDouble("airportLongitude", 0);

                Location location = new Location(id, name, city, country, lat, lon);
                locations.add(location);
            }
        } catch (IOException e) {
            System.err.println("Error al leer locations.json: " + e.getMessage());
        }
    }

    private void saveToFile() {
        JSONArray array = new JSONArray();

        for (Location loc : locations) {
            JSONObject obj = new JSONObject();
            obj.put("airportId", loc.getAirportId());
            obj.put("airportName", loc.getAirportName());
            obj.put("airportCity", loc.getAirportCity());
            obj.put("airportCountry", loc.getAirportCountry());
            obj.put("airportLatitude", loc.getAirportLatitude());
            obj.put("airportLongitude", loc.getAirportLongitude());
            array.put(obj);
        }

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(array.toString(4));
        } catch (IOException e) {
            System.err.println("Error al guardar ubicaciones: " + e.getMessage());
        }
    }
}
