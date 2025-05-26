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

import java.time.LocalDateTime;

public class FlightValidator {

    public static Response validate(Flight flight) {
        if (flight == null) {
            return Response.error("El vuelo no puede ser null");
        }

        if (flight.getId() == null || !flight.getId().matches("[A-Z]{3}\\d{3}")) {
            return Response.error("El ID del vuelo debe tener el formato XXX999");
        }

        if (flight.getPlane() == null) {
            return Response.error("El avión debe estar seleccionado");
        }

        if (flight.getDepartureLocation() == null || flight.getArrivalLocation() == null) {
            return Response.error("Las localizaciones de salida y llegada son obligatorias");
        }

        if (flight.getDepartureDate() == null || flight.getDepartureDate().isBefore(LocalDateTime.now())) {
            return Response.error("La fecha de salida debe ser válida y no anterior a la fecha actual");
        }

        if (flight.getFlightDurationMinutes() <= 0) {
            return Response.error("La duración del vuelo debe ser mayor que 0");
        }

        if (flight.getStopLocation() == null && flight.getStopDurationMinutes() > 0) {
            return Response.error("Si no hay escala, el tiempo de escala debe ser 0");
        }

        return Response.ok("Vuelo válido");
    }
}
