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
import interfaces.IFlightRepository;
import interfaces.IValidator;
import java.time.LocalDateTime;

/**
 *
 * @author plobb
 */
public class FlightsController {

    private final IValidator<Flight> validator;
    private final IFlightRepository repository;

    public FlightsController(IValidator<Flight> validator, IFlightRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    public Response createFlight(
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

            Plane plane = new Plane(planeId);
            Location dep = new Location(departureLocationId);
            Location arr = new Location(arrivalLocationId);
            Location scale = (scaleLocationId == null || scaleLocationId.isBlank())
                    ? null
                    : new Location(scaleLocationId);

            Flight flight = (scale == null)
                    ? new Flight(id, plane, dep, arr, departure, durationHour, durationMinute)
                    : new Flight(id, plane, dep, scale, arr, departure, durationHour, durationMinute, scaleHour, scaleMinute);

            Response validation = validator.validate(flight);
            if (!validation.isSuccess()) return validation;

            repository.save(flight);

            return new Response("Vuelo creado correctamente", Status.CREATED, flight);

        } catch (Exception e) {
            return new Response("Error al procesar el vuelo: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
