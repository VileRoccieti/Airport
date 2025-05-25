/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.Flight;
import airport.Location;
import airport.Plane;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class FlightsController {

    public static Response createFlight(
            String id,
            String planeId,
            String departureLocationId,
            String arrivalLocationId,
            String scaleLocationId,
            String yearStr,
            String monthStr,
            String dayStr,
            String hourStr,
            String minuteStr,
            String durationHourStr,
            String durationMinuteStr,
            String scaleDurationHourStr,
            String scaleDurationMinuteStr
    ) {
        if (id.isBlank() || planeId.isBlank() || departureLocationId.isBlank() || arrivalLocationId.isBlank()
                || yearStr.isBlank() || monthStr.isBlank() || dayStr.isBlank()
                || hourStr.isBlank() || minuteStr.isBlank()
                || durationHourStr.isBlank() || durationMinuteStr.isBlank()) {
            return new Response("Todos los campos obligatorios deben estar completos", Status.BAD_REQUEST);
        }

        File file = new File("json/flights.json");
        JSONArray flightsArray;

        try {
            if (file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    flightsArray = new JSONArray(new JSONTokener(is));
                }
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
                flightsArray = new JSONArray();
            }

            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject existingFlight = flightsArray.getJSONObject(i);
                if (existingFlight.getString("id").equals(id)) {
                    return new Response("Ya existe un vuelo con ese ID", Status.BAD_REQUEST);
                }
            }

            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            int hour = Integer.parseInt(hourStr);
            int minute = Integer.parseInt(minuteStr);

            int durationHour = Integer.parseInt(durationHourStr);
            int durationMinute = Integer.parseInt(durationMinuteStr);

            int scaleHour = scaleDurationHourStr.isEmpty() ? 0 : Integer.parseInt(scaleDurationHourStr);
            int scaleMinute = scaleDurationMinuteStr.isEmpty() ? 0 : Integer.parseInt(scaleDurationMinuteStr);

            LocalDateTime departureDate = LocalDateTime.of(year, month, day, hour, minute);

            LocalDateTime arrivalDate = departureDate
                    .plusHours(durationHour)
                    .plusMinutes(durationMinute)
                    .plusHours(scaleHour)
                    .plusMinutes(scaleMinute);

            JSONObject newFlight = new JSONObject();
            newFlight.put("id", id);
            newFlight.put("plane", planeId);
            newFlight.put("departureLocation", departureLocationId);
            newFlight.put("arrivalLocation", arrivalLocationId);
            newFlight.put("scaleLocation", scaleLocationId == null || scaleLocationId.isBlank() ? JSONObject.NULL : scaleLocationId);
            newFlight.put("departureDate", departureDate.toString());
            newFlight.put("arrivalDate", arrivalDate.toString());
            newFlight.put("hoursDurationArrival", durationHour);
            newFlight.put("minutesDurationArrival", durationMinute);
            newFlight.put("hoursDurationScale", scaleHour);
            newFlight.put("minutesDurationScale", scaleMinute);

            flightsArray.put(newFlight);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(flightsArray.toString(4));
            }

            return new Response("Vuelo creado correctamente", Status.CREATED);

        } catch (Exception e) {
            return new Response("Error al procesar el vuelo: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
