/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
import java.util.List;

public interface IRepository<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
}
