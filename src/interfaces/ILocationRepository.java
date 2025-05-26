/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
import airport.model.Location;
import java.util.List;

public interface ILocationRepository {
    void save(Location location);
    Location findById(String id);
    List<Location> findAll();
}
