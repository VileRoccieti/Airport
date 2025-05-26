/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.controller.utils.Response;
import airport.controller.utils.Status;
import airport.model.Passenger;
import airport.validators.UpdateInfoValidator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class UpdateInfoController {

    public static Response validateAndUpdate(
            String idStr,
            String firstName,
            String lastName,
            String yearStr,
            String monthStr,
            String dayStr,
            String phoneCodeStr,
            String phoneStr,
            String country) {

        long id;
        int year, month, day, phoneCode;
        long phone;
        LocalDate birthDate;

        try {
            id = Long.parseLong(idStr.trim());
            year = Integer.parseInt(yearStr.trim());
            month = Integer.parseInt(monthStr.trim());
            day = Integer.parseInt(dayStr.trim());
            phoneCode = Integer.parseInt(phoneCodeStr.trim());
            phone = Long.parseLong(phoneStr.trim());
            birthDate = LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            return new Response("Error al interpretar los datos numéricos o fecha: " + e.getMessage(), Status.BAD_REQUEST);
        }

        Passenger passenger = new Passenger(id, firstName, lastName, birthDate, phoneCode, phone, country);
      
        Response validation = UpdateInfoValidator.validate(passenger);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        File file = new File("json/passengers.json");
        if (!file.exists()) {
            return new Response("Archivo passengers.json no encontrado", Status.INTERNAL_SERVER_ERROR);
        }

        JSONArray passengersArray;
        try (InputStream is = new FileInputStream(file)) {
            passengersArray = new JSONArray(new JSONTokener(is));
        } catch (IOException e) {
            return new Response("Error leyendo archivo JSON: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        boolean found = false;
        for (int i = 0; i < passengersArray.length(); i++) {
            JSONObject p = passengersArray.getJSONObject(i);
            if (p.getLong("id") == id) {
                p.put("firstname", firstName);
                p.put("lastname", lastName);
                p.put("birthDate", birthDate.toString());
                p.put("countryPhoneCode", phoneCode);
                p.put("phone", phone);
                p.put("country", country);
                found = true;
                break;
            }
        }

        if (!found) {
            return new Response("Pasajero con ID " + id + " no encontrado.", Status.NOT_FOUND);
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(passengersArray.toString(4));
        } catch (IOException e) {
            return new Response("Error escribiendo archivo JSON: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Datos actualizados correctamente", Status.OK);
    }
}