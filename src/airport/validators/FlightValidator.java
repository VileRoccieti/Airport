/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.validators;

/**
 *
 * @author User
 */
import airport.controller.utils.Response;
import airport.model.Flight;
import interfaces.IValidator;

import java.time.LocalDateTime;

public class FlightValidator implements IValidator<Flight> {

    @Override
    public Response validate(Flight flight) {
        if (flight == null) {
            return Response.error("Vuelo nulo");
        }

        if (flight.getId() == null || flight.getId().isBlank()) {
            return Response.error("ID del vuelo vacío");
        }

        if (flight.getPlane() == null || flight.getPlane().getId() == null || flight.getPlane().getId().isBlank()) {
            return Response.error("Avión inválido o sin ID");
        }

        if (flight.getDepartureLocation() == null || flight.getDepartureLocation().getId().isBlank()) {
            return Response.error("Ubicación de salida inválida");
        }

        if (flight.getArrivalLocation() == null || flight.getArrivalLocation().getId().isBlank()) {
            return Response.error("Ubicación de llegada inválida");
        }

        if (flight.getDepartureDate() == null) {
            return Response.error("Fecha de salida vacía");
        }

        if (flight.getFlightDurationMinutes() <= 0) {
            return Response.error("Duración de vuelo inválida");
        }

        if (flight.getScaleLocation() != null && flight.getStopDurationMinutes() <= 0) {
            return Response.error("Duración de escala inválida");
        }

        return Response.ok("Vuelo válido", flight);
    }
}
