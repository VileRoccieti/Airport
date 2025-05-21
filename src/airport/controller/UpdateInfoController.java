/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller;

import airport.Passenger;
import airport.controller.utils.Response;
import airport.controller.utils.Status;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ArrayList;

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

        // Validaciones básicas
        if (idStr == null || idStr.trim().isEmpty()) {
            return new Response("Falta el ID", Status.BAD_REQUEST);
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            return new Response("Falta el nombre", Status.BAD_REQUEST);
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return new Response("Falta el apellido", Status.BAD_REQUEST);
        }
        if (yearStr == null || yearStr.trim().isEmpty()) {
            return new Response("Falta el año", Status.BAD_REQUEST);
        }
        if (monthStr == null || monthStr.trim().isEmpty()) {
            return new Response("Falta el mes", Status.BAD_REQUEST);
        }
        if (dayStr == null || dayStr.trim().isEmpty()) {
            return new Response("Falta el día", Status.BAD_REQUEST);
        }
        if (phoneCodeStr == null || phoneCodeStr.trim().isEmpty()) {
            return new Response("Falta el indicativo telefónico", Status.BAD_REQUEST);
        }
        if (phoneStr == null || phoneStr.trim().isEmpty()) {
            return new Response("Falta el teléfono", Status.BAD_REQUEST);
        }
        if (country == null || country.trim().isEmpty()) {
            return new Response("Falta el país", Status.BAD_REQUEST);
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

        File file = new File("passengers.txt");
        List<Passenger> passengers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length >= 7) {
                    long currentId = Long.parseLong(parts[0].trim());
                    String[] names = parts[1].split(" ");
                    String fn = names[0];
                    String ln = names.length > 1 ? names[1] : "";
                    LocalDate bd = LocalDate.parse(parts[2].trim());
                    String phoneFull = parts[4];
                    String[] phoneParts = phoneFull.split(" ");
                    int pc = Integer.parseInt(phoneParts[0].replace("+", ""));
                    long ph = Long.parseLong(phoneParts[1]);
                    String ctry = parts[5];

                    passengers.add(new Passenger(currentId, fn, ln, bd, pc, ph, ctry));
                }
            }
        } catch (IOException e) {
            return new Response("Error leyendo archivo: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        Passenger passengerToUpdate = null;
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                passengerToUpdate = p;
                break;
            }
        }

        if (passengerToUpdate == null) {
            return new Response("Pasajero con ID " + id + " no encontrado.", Status.NOT_FOUND);
        }

        passengerToUpdate.setFirstname(firstName);
        passengerToUpdate.setLastname(lastName);
        passengerToUpdate.setBirthDate(birthDate);
        passengerToUpdate.setCountryPhoneCode(phoneCode);
        passengerToUpdate.setPhone(phone);
        passengerToUpdate.setCountry(country);

        List<String> lines = new ArrayList<String>();
        for (Passenger p : passengers) {
            int calculatedAge = Period.between(p.getBirthDate(), LocalDate.now()).getYears();
            String phoneFormatted = "+" + p.getCountryPhoneCode() + " " + p.getPhone();
            String line = String.format("%d, %s %s, %s, %d, %s, %s, AB123",
                    p.getId(), p.getFirstname(), p.getLastname(),
                    p.getBirthDate().toString(), calculatedAge, phoneFormatted, p.getCountry());
            lines.add(line);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (String l : lines) {
                pw.println(l);
            }
        } catch (IOException e) {
            return new Response("Error escribiendo archivo: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Datos actualizados correctamente", Status.OK);
    }
}
