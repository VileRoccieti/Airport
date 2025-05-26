/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author User
 */
import airport.model.Passenger;
import interfaces.IPassengerRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PassengerRepository implements IPassengerRepository {

    private final File file = new File("json/passengers.json");
    private final Map<Long, Passenger> passengers = new TreeMap<>();

    public PassengerRepository() {
        loadFromFile();
    }

    @Override
    public void save(Passenger passenger) {
        if (passengers.containsKey(passenger.getId())) {
            throw new IllegalArgumentException("Ya existe un pasajero con ese ID.");
        }

        passengers.put(passenger.getId(), passenger);
        saveToFile();
    }

    @Override
    public Passenger findById(long id) {
        return passengers.get(id);
    }

    @Override
    public List<Passenger> findAll() {
        return new ArrayList<>(passengers.values());
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileReader fr = new FileReader(file)) {
            JSONTokener tokener = new JSONTokener(fr);
            JSONArray array = new JSONArray(tokener);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                long id = obj.getLong("id");
                String firstname = obj.getString("firstname");
                String lastname = obj.getString("lastname");
                LocalDate birthDate = LocalDate.parse(obj.getString("birthDate"));
                int code = obj.getInt("countryPhoneCode");
                long phone = obj.getLong("phone");
                String country = obj.getString("country");

                Passenger p = new Passenger(id, firstname, lastname, birthDate, code, phone, country);
                passengers.put(id, p);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar pasajeros: " + e.getMessage());
        }
    }

    private void saveToFile() {
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }

        JSONArray array = new JSONArray();

        for (Passenger p : passengers.values()) {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("firstname", p.getFirstname());
            obj.put("lastname", p.getLastname());
            obj.put("birthDate", p.getBirthDate().toString());
            obj.put("countryPhoneCode", p.getCountryPhoneCode());
            obj.put("phone", p.getPhone());
            obj.put("country", p.getCountry());
            array.put(obj);
        }

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(array.toString(4));
        } catch (IOException e) {
            System.err.println("Error al guardar pasajeros: " + e.getMessage());
        }
    }
}
