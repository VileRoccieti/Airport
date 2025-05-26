/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
import airport.model.Plane;
import java.util.List;

public interface IPlaneRepository {
    void save(Plane plane);
    Plane findById(String id);
    List<Plane> findAll();
}
