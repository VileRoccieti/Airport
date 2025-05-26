/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Location;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import interfaces.ILocationRepository;
import interfaces.IValidator;


/**
 *
 * @author plobb
 */
public class LocationController {

    private final IValidator<Location> validator;
    private final ILocationRepository repository;

    public LocationController(IValidator<Location> validator, ILocationRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    public Response createLocation(
            String id, String name, String city, String country, String latStr, String lonStr
    ) {
        double lat, lon;
        try {
            lat = Double.parseDouble(latStr.trim());
            lon = Double.parseDouble(lonStr.trim());
        } catch (NumberFormatException e) {
            return Response.error("Latitud o longitud inválidas");
        }

        Location location = new Location(id.trim(), name.trim(), city.trim(), country.trim(), lat, lon);

        Response validation = validator.validate(location);
        if (!validation.isSuccess()) return validation;

        try {
            repository.save(location);
        } catch (Exception e) {
            return Response.error("Error al guardar ubicación: " + e.getMessage());
        }

        return new Response("Ubicación registrada correctamente", Status.CREATED, location);
    }
}
