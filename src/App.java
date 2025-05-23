import javax.swing.SwingUtilities;

import com.app.components.MDI;
import com.app.components.auth.Login;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(MDI::new);
    }
}
