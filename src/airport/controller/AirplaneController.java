/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Plane;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import interfaces.IPlaneRepository;
import interfaces.IValidator;

/**
 *
 * @author plobb
 */
public class AirplaneController {

    private final IValidator<Plane> validator;
    private final IPlaneRepository repository;

    public AirplaneController(IValidator<Plane> validator, IPlaneRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    public Response createAirplane(String id, String brand, String model, String maxCapacityStr, String airline) {
        Plane plane;
        try {
            int maxCapacity = Integer.parseInt(maxCapacityStr.trim());
            plane = new Plane(id.trim(), brand.trim(), model.trim(), maxCapacity, airline.trim());
        } catch (Exception e) {
            return Response.error("Error al procesar los datos: " + e.getMessage());
        }

        Response validation = validator.validate(plane);
        if (!validation.isSuccess()) return validation;

        try {
            repository.save(plane);
        } catch (Exception e) {
            return Response.error("Error al guardar el avión: " + e.getMessage());
        }

        return new Response("Avión creado exitosamente", Status.CREATED, plane);
    }
}
