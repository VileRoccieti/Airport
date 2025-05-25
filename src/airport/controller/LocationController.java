/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.Location;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author plobb
 */
public class LocationController {
    private static final String DIRECTORY = System.getProperty("user.dir") + File.separator + "json";
    private static final String FILE_PATH = DIRECTORY + File.separator + "locations.json";

    private static final List<Location> locations = new ArrayList<>();

    public static Response createLocation(String id, String name, String city, String country, String latitudeStr, String longitudeStr) {
        try {
            loadLocationsFromFile();
        } catch (IOException e) {
            return new Response("Error cargando ubicaciones previas: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        if (id == null || id.trim().isEmpty()) {
            return new Response("ID del aeropuerto no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (name == null || name.trim().isEmpty()) {
            return new Response("Nombre del aeropuerto no puede estar vacío.", Status.BAD_REQUEST);
        }
        if (city == null || city.trim().isEmpty()) {
            return new Response("Ciudad no puede estar vacía.", Status.BAD_REQUEST);
        }
        if (country == null || country.trim().isEmpty()) {
            return new Response("País no puede estar vacío.", Status.BAD_REQUEST);
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
        } catch (NumberFormatException e) {
            return new Response("Latitud o longitud inválida.", Status.BAD_REQUEST);
        }

        if (latitude < -90 || latitude > 90) {
            return new Response("Latitud fuera de rango (-90 a 90).", Status.BAD_REQUEST);
        }
        if (longitude < -180 || longitude > 180) {
            return new Response("Longitud fuera de rango (-180 a 180).", Status.BAD_REQUEST);
        }

        if (existsLocationId(id)) {
            return new Response("Ya existe una ubicación con ese ID.", Status.BAD_REQUEST);
        }

        Location location = new Location(id, name, city, country, latitude, longitude);
        locations.add(location);

        try {
            saveLocationsToFile();
        } catch (IOException e) {
            return new Response("Error al guardar la ubicación: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Ubicación creada exitosamente.", Status.CREATED, location);
    }

    private static boolean existsLocationId(String id) {
        return locations.stream().anyMatch(loc -> loc.getAirportId().equalsIgnoreCase(id));
    }

    public static void loadLocationsFromFile() throws IOException {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String json = jsonBuilder.toString().trim();
            if (json.isEmpty() || json.equals("[]")) {
                return;
            }

            org.json.JSONArray jsonArray = new org.json.JSONArray(json);
            locations.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject obj = jsonArray.getJSONObject(i);
                Location loc = new Location(
                        obj.getString("airportId"),
                        obj.getString("airportName"),
                        obj.getString("airportCity"),
                        obj.getString("airportCountry"),
                        obj.getDouble("airportLatitude"),
                        obj.getDouble("airportLongitude")
                );
                locations.add(loc);
            }
        }
    }

    private static void saveLocationsToFile() throws IOException {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        org.json.JSONArray jsonArray = new org.json.JSONArray();
        for (Location loc : locations) {
            org.json.JSONObject obj = new org.json.JSONObject();
            obj.put("airportId", loc.getAirportId());
            obj.put("airportName", loc.getAirportName());
            obj.put("airportCity", loc.getAirportCity());
            obj.put("airportCountry", loc.getAirportCountry());
            obj.put("airportLatitude", loc.getAirportLatitude());
            obj.put("airportLongitude", loc.getAirportLongitude());
            jsonArray.put(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(jsonArray.toString(4));
        }
    }
}
