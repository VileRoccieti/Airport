/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.Location;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author plobb
 */
public class LocationController {
    private static final String FILE_PATH = "locations.txt";

    public static Response createLocation(String id, String name, String city, String country, String latitudeStr, String longitudeStr) {
        try {
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

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                String line = String.format("%s, %s, %s, %s, %.6f, %.6f",
                        id, name, city, country, latitude, longitude);
                writer.write(line);
                writer.newLine();
            }

            return new Response("Ubicación creada exitosamente.", Status.CREATED, location);
        } catch (Exception e) {
            return new Response("Error inesperado al crear la ubicación.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean existsLocationId(String id) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",\\s*");
                if (parts.length != 6) {
                    continue;
                }
                String fileId = parts[0].trim();
                if (fileId.equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // En caso de error, se asume que no existe
        }

        return false;
    }
}
