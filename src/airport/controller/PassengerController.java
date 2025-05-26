/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.model.Passenger;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import airport.validators.PassengerValidator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author plobb
 */
public class PassengerController {

    public static Response createPassenger(
            String idStr,
            String firstName,
            String lastName,
            String yearStr,
            String monthStr,
            String dayStr,
            String phoneCodeStr,
            String phoneStr,
            String country) {

        Passenger passenger;
        try {
            long id = Long.parseLong(idStr.trim());
            int year = Integer.parseInt(yearStr.trim());
            int month = Integer.parseInt(monthStr.trim());
            int day = Integer.parseInt(dayStr.trim());
            LocalDate birthDate = LocalDate.of(year, month, day);
            int phoneCode = Integer.parseInt(phoneCodeStr.trim());
            long phone = Long.parseLong(phoneStr.trim());

            passenger = new Passenger(id, firstName, lastName, birthDate, phoneCode, phone, country);

        } catch (Exception e) {
            return new Response("Datos inválidos: " + e.getMessage(), Status.BAD_REQUEST);
        }

// VALIDAR
        Response validation = PassengerValidator.validate(passenger);
        if (validation.getStatus() != Status.OK) {
            return validation;
        }

        long id;
        int year, month, day, phoneCode;
        long phone;
        try {
            id = Long.parseLong(idStr.trim());
        } catch (NumberFormatException e) {
            return new Response("ID inválido, debe ser numérico", Status.BAD_REQUEST);
        }
        try {
            year = Integer.parseInt(yearStr.trim());
            month = Integer.parseInt(monthStr.trim());
            day = Integer.parseInt(dayStr.trim());
        } catch (NumberFormatException e) {
            return new Response("Fecha inválida, año, mes y día deben ser números", Status.BAD_REQUEST);
        }
        try {
            phoneCode = Integer.parseInt(phoneCodeStr.trim());
            phone = Long.parseLong(phoneStr.trim());
        } catch (NumberFormatException e) {
            return new Response("Teléfono o indicativo inválido", Status.BAD_REQUEST);
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return new Response("Fecha de nacimiento inválida", Status.BAD_REQUEST);
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 0 || age > 150) {
            return new Response("Edad inválida calculada", Status.BAD_REQUEST);
        }


        try {
            savePassengerToFile(passenger);
        } catch (IOException e) {
            return new Response("Error al guardar el pasajero: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Pasajero creado exitosamente", Status.CREATED, passenger);
    }

    private static void savePassengerToFile(Passenger passenger) throws IOException {
        File dir = new File("json");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "passengers.json");
        JSONArray passengersArray;

        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                JSONTokener tokener = new JSONTokener(fr);
                passengersArray = new JSONArray(tokener);
            } catch (Exception e) {
                passengersArray = new JSONArray();
            }
        } else {
            passengersArray = new JSONArray();
        }

        for (int i = 0; i < passengersArray.length(); i++) {
            JSONObject obj = passengersArray.getJSONObject(i);
            if (obj.getLong("id") == passenger.getId()) {
                throw new IOException("El pasajero con ID " + passenger.getId() + " ya existe.");
            }
        }

        Map<String, Object> orderedMap = new LinkedHashMap<>();
        orderedMap.put("id", passenger.getId());
        orderedMap.put("firstname", passenger.getFirstname());
        orderedMap.put("lastname", passenger.getLastname());
        orderedMap.put("birthDate", passenger.getBirthDate().toString());
        orderedMap.put("countryPhoneCode", passenger.getCountryPhoneCode());
        orderedMap.put("phone", passenger.getPhone());
        orderedMap.put("country", passenger.getCountry());

        JSONObject passengerJson = new JSONObject(orderedMap);

        passengersArray.put(passengerJson);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(passengersArray.toString(4));
        }
    }
}
