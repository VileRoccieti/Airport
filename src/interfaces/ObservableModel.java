/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author User
 */
public interface ObservableModel {
    void addObserver(TableObserver observer);
    void removeObserver(TableObserver observer);
    void notifyObservers();
}
