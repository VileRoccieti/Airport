/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class AddToFlightController {
    public static Response verifyPassengerExists(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return new Response("El ID está vacío", Status.BAD_REQUEST);
        }

        long id;
        try {
            id = Long.parseLong(idStr.trim());
        } catch (NumberFormatException e) {
            return new Response("El ID debe ser numérico", Status.BAD_REQUEST);
        }

        File file = new File("json/passengers.json");
        if (!file.exists()) {
            return new Response("Archivo passengers.json no encontrado", Status.INTERNAL_SERVER_ERROR);
        }

        try (InputStream is = new FileInputStream(file)) {
            JSONTokener tokener = new JSONTokener(is);
            JSONArray array = new JSONArray(tokener);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.has("id") && obj.getLong("id") == id) {
                    return new Response("Pasajero encontrado", Status.OK, obj);
                }
            }

            return new Response("Pasajero no encontrado", Status.NOT_FOUND);

        } catch (Exception e) {
            return new Response("Error al leer el archivo de pasajeros", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
