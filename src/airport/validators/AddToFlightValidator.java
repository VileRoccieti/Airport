/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.validators;

import airport.controller.utils.Response;
import airport.controller.utils.Status;

/**
 *
 * @author User
 */
public class AddToFlightValidator {

    public static Response validate(String passengerIdStr, String flightId) {
        if (passengerIdStr == null || passengerIdStr.trim().isEmpty()) {
            return new Response("El ID del pasajero está vacío", Status.BAD_REQUEST);
        }

        try {
            Long.parseLong(passengerIdStr.trim());
        } catch (NumberFormatException e) {
            return new Response("El ID del pasajero debe ser numérico", Status.BAD_REQUEST);
        }

        if (flightId == null || flightId.trim().isEmpty()) {
            return new Response("El ID del vuelo está vacío", Status.BAD_REQUEST);
        }

        return new Response("Validación correcta", Status.OK);
    }
}
