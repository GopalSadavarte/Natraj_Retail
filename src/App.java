import javax.swing.SwingUtilities;

import com.app.components.MDI;
// import com.app.components.auth.Login;
import com.app.config.DBConnection;

public class App {
    public static void main(String[] args) {
        DBConnection.createConnection();
        SwingUtilities.invokeLater(MDI::new);
    }
}
