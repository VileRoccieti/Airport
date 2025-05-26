/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.validators;

import airport.model.Passenger;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import interfaces.IValidator;

/**
 *
 * @author User
 */
public class PassengerValidator implements IValidator<Passenger> {
    @Override
    public Response validate(Passenger passenger) {
        if (passenger == null) {
            return Response.error("El pasajero no puede ser null");
        }

        long id = passenger.getId();
        if (id < 0 || String.valueOf(id).length() > 15) {
            return Response.error("El ID debe ser mayor o igual a 0 y de hasta 15 dígitos");
        }

        if (passenger.getFirstname() == null || passenger.getFirstname().trim().isEmpty()) {
            return Response.error("El nombre no puede estar vacío");
        }

        if (passenger.getLastname() == null || passenger.getLastname().trim().isEmpty()) {
            return Response.error("El apellido no puede estar vacío");
        }

        if (passenger.getCountry() == null || passenger.getCountry().trim().isEmpty()) {
            return Response.error("La nacionalidad no puede estar vacía");
        }

        if (passenger.getBirthDate() == null) {
            return Response.error("La fecha de nacimiento no puede estar vacía");
        }

        int code = passenger.getCountryPhoneCode();
        if (code < 0 || String.valueOf(code).length() > 3) {
            return Response.error("El código de país debe ser ≥ 0 y de hasta 3 dígitos");
        }

        long phone = passenger.getPhone();
        if (phone < 0 || String.valueOf(phone).length() > 11) {
            return Response.error("El número de teléfono debe ser ≥ 0 y de hasta 11 dígitos");
        }

        return Response.ok("Pasajero registrado");
    }
}
