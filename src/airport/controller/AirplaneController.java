/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.Plane;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
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
