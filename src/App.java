import javax.swing.SwingUtilities;
import com.app.components.MDI;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MDI::new);
    }
}
