/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Flight;
import airport.model.Location;
import airport.model.Plane;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import airport.validators.FlightValidator;
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
        try {
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);
            int hour = Integer.parseInt(hourStr);
            int minute = Integer.parseInt(minuteStr);
            int durationHour = Integer.parseInt(durationHourStr);
            int durationMinute = Integer.parseInt(durationMinuteStr);
            int scaleHour = scaleDurationHourStr.isBlank() ? 0 : Integer.parseInt(scaleDurationHourStr);
            int scaleMinute = scaleDurationMinuteStr.isBlank() ? 0 : Integer.parseInt(scaleDurationMinuteStr);

            LocalDateTime departure = LocalDateTime.of(year, month, day, hour, minute);

            Plane plane = new Plane(planeId, "", "", 100, "");
            Location dep = new Location(departureLocationId, "", "", "", 0, 0);
            Location arr = new Location(arrivalLocationId, "", "", "", 0, 0);
            Location scale = (scaleLocationId == null || scaleLocationId.isBlank())
                    ? null
                    : new Location(scaleLocationId, "", "", "", 0, 0);

            Flight flight = (scale == null)
                    ? new Flight(id, plane, dep, arr, departure, durationHour, durationMinute)
                    : new Flight(id, plane, dep, scale, arr, departure, durationHour, durationMinute, scaleHour, scaleMinute);

            Response validation = FlightValidator.validate(flight);
            if (validation.getStatus() != Status.OK) {
                return validation;
            }

            File file = new File("json/flights.json");
            JSONArray flightsArray;

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

            JSONObject newFlight = new JSONObject();
            newFlight.put("id", flight.getId());
            newFlight.put("plane", planeId);
            newFlight.put("departureLocation", departureLocationId);
            newFlight.put("arrivalLocation", arrivalLocationId);
            newFlight.put("scaleLocation", scale == null ? JSONObject.NULL : scaleLocationId);
            newFlight.put("departureDate", departure.toString());
            newFlight.put("arrivalDate", flight.calculateArrivalDate().toString());
            newFlight.put("hoursDurationArrival", flight.getHoursDurationArrival());
            newFlight.put("minutesDurationArrival", flight.getMinutesDurationArrival());
            newFlight.put("hoursDurationScale", flight.getHoursDurationScale());
            newFlight.put("minutesDurationScale", flight.getMinutesDurationScale());

            flightsArray.put(newFlight);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(flightsArray.toString(4));
            }

            return new Response("Vuelo creado correctamente", Status.CREATED, flight);

        } catch (Exception e) {
            return new Response("Error al procesar el vuelo: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
