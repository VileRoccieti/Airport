/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
import airport.model.Passenger;
import java.util.List;

public interface IPassengerRepository {
    void save(Passenger passenger);
    Passenger findById(long id);
    List<Passenger> findAll();
}
