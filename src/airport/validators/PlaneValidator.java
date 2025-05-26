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
import interfaces.IValidator;

public class PlaneValidator implements IValidator<Plane> {
    @Override
    public Response validate(Plane plane) {
        if (plane == null) return Response.error("Avión inválido");
        if (plane.getId() == null || !plane.getId().matches("[A-Z]{2}\\d{5}")) {
            return Response.error("ID del avión inválido (formato XX99999)");
        }
        if (plane.getBrand() == null || plane.getBrand().isBlank()) {
            return Response.error("Marca vacía");
        }
        if (plane.getModel() == null || plane.getModel().isBlank()) {
            return Response.error("Modelo vacío");
        }
        if (plane.getAirline() == null || plane.getAirline().isBlank()) {
            return Response.error("Aerolínea vacía");
        }
        if (plane.getMaxCapacity() <= 0) {
            return Response.error("Capacidad máxima inválida");
        }
        return Response.ok("Avión válido", plane);
    }
}
