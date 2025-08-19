import javax.swing.SwingUtilities;
import com.app.components.MDI;
import com.app.config.DBConnection;

public class App {
    public static void main(String[] args) throws Exception {
        DBConnection.con.createStatement();
        SwingUtilities.invokeLater(MDI::new);
    }
}