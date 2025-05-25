/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class AddToFlightController {

    public static Response addPassengerToFlight(String passengerIdStr, String flightId) {
        if (passengerIdStr == null || passengerIdStr.trim().isEmpty()) {
            return new Response("El ID está vacío", Status.BAD_REQUEST);
        }

        long passengerId;
        try {
            passengerId = Long.parseLong(passengerIdStr.trim());
        } catch (NumberFormatException e) {
            return new Response("El ID debe ser numérico", Status.BAD_REQUEST);
        }

        Response verifyPassengerResponse = verifyPassengerExists(passengerIdStr);
        if (verifyPassengerResponse.getStatus() != Status.OK) {
            return verifyPassengerResponse;
        }

        File file = new File("json/flights.json");
        if (!file.exists()) {
            return new Response("Archivo flights.json no encontrado.", Status.INTERNAL_SERVER_ERROR);
        }

        try (InputStream is = new FileInputStream(file)) {
            JSONTokener tokener = new JSONTokener(is);
            JSONArray flightsArray = new JSONArray(tokener);

            boolean flightFound = false;

            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flight = flightsArray.getJSONObject(i);

                if (flight.getString("id").equals(flightId)) {
                    flightFound = true;

                    if (!flight.has("registeredPassengers") || flight.getString("registeredPassengers").isEmpty()) {
                        flight.put("registeredPassengers", String.valueOf(passengerId));
                    } else {
                        String existing = flight.getString("registeredPassengers");
                        List<String> ids = new ArrayList<>(Arrays.asList(existing.split(",")));

                        if (ids.contains(String.valueOf(passengerId))) {
                            return new Response("El pasajero ya está registrado en este vuelo.", Status.BAD_REQUEST);
                        }

                        ids.add(String.valueOf(passengerId));
                        flight.put("registeredPassengers", String.join(",", ids));
                    }

                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(flightsArray.toString(4));
                    }

                    return new Response("Pasajero agregado correctamente al vuelo.", Status.OK);
                }
            }

            if (!flightFound) {
                return new Response("Vuelo no encontrado.", Status.NOT_FOUND);
            }

        } catch (Exception e) {
            return new Response("Error al procesar el archivo flights.json: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Error desconocido.", Status.INTERNAL_SERVER_ERROR);
    }

    public static Response verifyPassengerExists(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return new Response("El ID está vacío", Status.BAD_REQUEST);
        }

        long id;
        try {
            id = Long.parseLong(idStr.trim());
        } catch (NumberFormatException e) {
            return new Response("El ID debe ser numérico", Status.BAD_REQUEST);
        }

        File file = new File("json/passengers.json");
        if (!file.exists()) {
            return new Response("Archivo passengers.json no encontrado", Status.INTERNAL_SERVER_ERROR);
        }

        try (InputStream is = new FileInputStream(file)) {
            JSONTokener tokener = new JSONTokener(is);
            JSONArray array = new JSONArray(tokener);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.has("id") && obj.getLong("id") == id) {
                    return new Response("Pasajero encontrado", Status.OK, obj);
                }
            }

            return new Response("Pasajero no encontrado", Status.NOT_FOUND);

        } catch (Exception e) {
            return new Response("Error al leer el archivo de pasajeros", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
