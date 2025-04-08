import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * Clase principal para ejecutar la aplicación
 */
public class SistemaAgenda {
    public static void main(String[] args) {
        // Configurar el aspecto de la interfaz gráfica
        try {
            // Intentar usar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al configurar el aspecto visual: " + e.getMessage());
        }
        
        // Ejecutar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AgendaUI agendaUI = new AgendaUI();
                agendaUI.setVisible(true);
            }
        });
    }
}
