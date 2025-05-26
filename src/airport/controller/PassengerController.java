/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Passenger;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import interfaces.IPassengerRepository;
import interfaces.IValidator;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author plobb
 */
public class PassengerController {

    private final IValidator<Passenger> validator;
    private final IPassengerRepository repository;

    public PassengerController(IValidator<Passenger> validator, IPassengerRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    public Response createPassenger(
            String idStr,
            String firstName,
            String lastName,
            String yearStr,
            String monthStr,
            String dayStr,
            String phoneCodeStr,
            String phoneStr,
            String country) {

        Passenger passenger;
        try {
            long id = Long.parseLong(idStr.trim());
            int year = Integer.parseInt(yearStr.trim());
            int month = Integer.parseInt(monthStr.trim());
            int day = Integer.parseInt(dayStr.trim());
            LocalDate birthDate = LocalDate.of(year, month, day);
            int phoneCode = Integer.parseInt(phoneCodeStr.trim());
            long phone = Long.parseLong(phoneStr.trim());

            int age = Period.between(birthDate, LocalDate.now()).getYears();
            if (age < 0 || age > 150) {
                return new Response("Edad inválida calculada", Status.BAD_REQUEST);
            }

            passenger = new Passenger(id, firstName, lastName, birthDate, phoneCode, phone, country);
        } catch (Exception e) {
            return new Response("Datos inválidos: " + e.getMessage(), Status.BAD_REQUEST);
        }

        Response validation = validator.validate(passenger);
        if (!validation.isSuccess()) {
            return validation;
        }

        try {
            repository.save(passenger);
        } catch (Exception e) {
            return new Response("Error al guardar el pasajero: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Pasajero creado exitosamente", Status.CREATED, passenger);
    }
}

