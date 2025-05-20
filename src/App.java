import javax.swing.SwingUtilities;
import com.app.components.auth.Login;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(Login::new);
    }
}
