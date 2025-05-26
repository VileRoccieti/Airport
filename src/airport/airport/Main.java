package airport.airport;

import airport.view.AirportFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer FlatDarkLaf como Look and Feel
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException ex) {
                System.err.println("No se pudo cargar FlatDarkLaf: " + ex.getMessage());
            }

            // Crear y mostrar el frame
            AirportFrame frame = new AirportFrame();
            frame.setVisible(true);
        });
    }
}