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
import airport.model.Plane;

public class PlaneValidator {

    public static Response validate(Plane plane) {
        if (plane == null) {
            return Response.error("El avión no puede ser null");
        }

        if (plane.getId() == null || !plane.getId().matches("[A-Z]{2}\\d{5}")) {
            return Response.error("El ID del avión debe tener el formato XX99999");
        }

        if (plane.getManufacturer() == null || plane.getManufacturer().trim().isEmpty()) {
            return Response.error("El fabricante no puede estar vacío");
        }

        if (plane.getModel() == null || plane.getModel().trim().isEmpty()) {
            return Response.error("El modelo no puede estar vacío");
        }

        if (plane.getCapacity() <= 0) {
            return Response.error("La capacidad debe ser mayor que 0");
        }

        return Response.ok("Avión válido");
    }
}
