/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
import airport.model.Flight;
import java.util.List;

public interface IFlightRepository {
    void save(Flight flight);
    Flight findById(String id);
    List<Flight> findAll();
}
