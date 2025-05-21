/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.controller.utils.Response;
import airport.controller.utils.Status;

/**
 *
 * @author plobb
 */
public class AirplaneController {

    public static Response createAirplane(String id, String brand, String model, String maxCapacityStr, String airline) {
        if (id == null || id.trim().isEmpty()) {
            return new Response("Falta el ID del avión", Status.BAD_REQUEST);
        }

        if (brand == null || brand.trim().isEmpty()) {
            return new Response("Falta la marca del avión", Status.BAD_REQUEST);
        }

        if (model == null || model.trim().isEmpty()) {
            return new Response("Falta el modelo del avión", Status.BAD_REQUEST);
        }

        if (maxCapacityStr == null || maxCapacityStr.trim().isEmpty()) {
            return new Response("Falta la capacidad máxima", Status.BAD_REQUEST);
        }

        if (airline == null || airline.trim().isEmpty()) {
            return new Response("Falta la aerolínea", Status.BAD_REQUEST);
        }

        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(maxCapacityStr.trim());
            if (maxCapacity <= 0) {
                return new Response("La capacidad máxima debe ser un número positivo", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("La capacidad máxima debe ser un número válido", Status.BAD_REQUEST);
        }

        return new Response("Avión creado exitosamente", Status.CREATED);
    }
}
