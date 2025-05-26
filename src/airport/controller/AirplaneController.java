/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Plane;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import airport.validators.PlaneValidator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class AirplaneController {

    private static final String FILE_PATH = "json/planes.json";

    public static Response createAirplane(String id, String brand, String model, String maxCapacityStr, String airline) {
        Plane plane;
        try {
            int maxCapacity = Integer.parseInt(maxCapacityStr.trim());
            plane = new Plane(id, brand, model, maxCapacity, airline);
        } catch (Exception e) {
            return new Response("Capacidad inválida: " + e.getMessage(), Status.BAD_REQUEST);
        }

        Response validation = PlaneValidator.validate(plane);
        if (validation.getStatus() != Status.OK) {
            return validation;
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

        JSONArray airplanesArray;
        File file = new File(FILE_PATH);

        try {
            if (file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    JSONTokener tokener = new JSONTokener(is);
                    airplanesArray = new JSONArray(tokener);
                }
            } else {
                file.getParentFile().mkdirs();
                airplanesArray = new JSONArray();
            }
        } catch (IOException e) {
            return new Response("Error al leer el archivo de aviones: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        for (int i = 0; i < airplanesArray.length(); i++) {
            JSONObject obj = airplanesArray.getJSONObject(i);
            if (obj.getString("id").equals(id)) {
                return new Response("Ya existe un avión con ese ID", Status.BAD_REQUEST);
            }
        }

        Plane newPlane = new Plane(id, brand, model, maxCapacity, airline);

        Map<String, Object> orderedMap = new LinkedHashMap<>();
        orderedMap.put("id", id);
        orderedMap.put("brand", brand);
        orderedMap.put("model", model);
        orderedMap.put("maxCapacity", maxCapacity);
        orderedMap.put("airline", airline);
        orderedMap.put("numFlights", 0);

        JSONObject airplaneObject = new JSONObject(orderedMap);

        airplanesArray.put(airplaneObject);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(airplanesArray.toString(4));
        } catch (IOException e) {
            return new Response("Error al guardar el archivo de aviones: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Avión creado exitosamente", Status.CREATED, newPlane);
    }
}
