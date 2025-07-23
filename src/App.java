import java.sql.Connection;
import javax.swing.SwingUtilities;
import com.app.components.MDI;
import com.app.config.DBConnection;

public class App {
    public static void main(String[] args) {
        Connection c = DBConnection.con;
        SwingUtilities.invokeLater(MDI::new);
    }
}