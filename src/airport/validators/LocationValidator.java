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
import airport.model.Location;
import interfaces.IValidator;

public class LocationValidator implements IValidator<Location> {

    @Override
    public Response validate(Location location) {
        if (location == null) {
            return Response.error("La localización no puede ser null");
        }

        if (location.getId() == null || !location.getId().matches("[A-Z]{3}")) {
            return Response.error("El ID debe tener 3 letras mayúsculas");
        }

        if (location.getCity() == null || location.getCity().trim().isEmpty()) {
            return Response.error("La ciudad no puede estar vacía");
        }

        if (location.getCountry() == null || location.getCountry().trim().isEmpty()) {
            return Response.error("El país no puede estar vacío");
        }

        double lat = location.getLatitude();
        if (lat < -90 || lat > 90 || !isDecimalPrecisionValid(lat, 4)) {
            return Response.error("Latitud inválida (rango -90 a 90, máx. 4 decimales)");
        }

        double lon = location.getLongitude();
        if (lon < -180 || lon > 180 || !isDecimalPrecisionValid(lon, 4)) {
            return Response.error("Longitud inválida (rango -180 a 180, máx. 4 decimales)");
        }

        return Response.ok("Localización válida");
    }

    private static boolean isDecimalPrecisionValid(double value, int maxDecimals) {
        String[] parts = String.valueOf(value).split("\\.");
        return parts.length < 2 || parts[1].length() <= maxDecimals;
    }
}
