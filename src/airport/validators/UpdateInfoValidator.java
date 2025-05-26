/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.validators;

import airport.controller.utils.Response;
import airport.controller.utils.Status;
import airport.model.Passenger;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author User
 */
public class UpdateInfoValidator {

    public static Response validate(Passenger passenger) {
        if (passenger == null) {
            return new Response("El pasajero no puede ser null", Status.BAD_REQUEST);
        }

        if (passenger.getFirstname() == null || passenger.getFirstname().trim().isEmpty()) {
            return new Response("Falta el nombre", Status.BAD_REQUEST);
        }

        if (passenger.getLastname() == null || passenger.getLastname().trim().isEmpty()) {
            return new Response("Falta el apellido", Status.BAD_REQUEST);
        }

        if (passenger.getCountry() == null || passenger.getCountry().trim().isEmpty()) {
            return new Response("Falta el país", Status.BAD_REQUEST);
        }

        if (passenger.getBirthDate() == null) {
            return new Response("Fecha de nacimiento inválida", Status.BAD_REQUEST);
        }

        int age = Period.between(passenger.getBirthDate(), LocalDate.now()).getYears();
        if (age < 0 || age > 150) {
            return new Response("Edad inválida calculada", Status.BAD_REQUEST);
        }

        if (passenger.getPhone() <= 0 || String.valueOf(passenger.getPhone()).length() > 11) {
            return new Response("Teléfono inválido", Status.BAD_REQUEST);
        }

        if (passenger.getCountryPhoneCode() < 0 || String.valueOf(passenger.getCountryPhoneCode()).length() > 3) {
            return new Response("Indicativo telefónico inválido", Status.BAD_REQUEST);
        }

        return new Response("Validación exitosa", Status.OK);
    }
}
