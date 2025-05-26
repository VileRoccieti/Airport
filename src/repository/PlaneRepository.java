/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author User
 */
import airport.model.Plane;
import interfaces.IPlaneRepository;
import org.json.*;
import java.io.*;
import java.util.*;

public class PlaneRepository implements IPlaneRepository {

    private final File file = new File("json/planes.json");
    private final List<Plane> planes = new ArrayList<>();

    public PlaneRepository() {
        loadFromFile();
    }

    @Override
    public void save(Plane plane) {
        if (findById(plane.getId()) != null) {
            throw new IllegalArgumentException("El ID del avión ya existe.");
        }
        planes.add(plane);
        saveToFile();
    }

    @Override
    public Plane findById(String id) {
        for (Plane p : planes) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Plane> findAll() {
        return new ArrayList<>(planes); // copia defensiva
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String id = obj.getString("id");
                String brand = obj.optString("brand", "UNKNOWN");
                String model = obj.optString("model", "UNKNOWN");
                int maxCapacity = obj.optInt("maxCapacity", 0);
                String airline = obj.optString("airline", "UNKNOWN");

                Plane plane = new Plane(id, brand, model, maxCapacity, airline);
                planes.add(plane);
            }
        } catch (IOException e) {
            System.err.println("Error al leer planes: " + e.getMessage());
        }
    }

    private void saveToFile() {
        JSONArray array = new JSONArray();
        for (Plane p : planes) {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("brand", p.getBrand());
            obj.put("model", p.getModel());
            obj.put("maxCapacity", p.getMaxCapacity());
            obj.put("airline", p.getAirline());
            array.put(obj);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            System.err.println("Error al guardar aviones: " + e.getMessage());
        }
    }
}
