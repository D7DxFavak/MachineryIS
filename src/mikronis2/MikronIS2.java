/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.*;
import javax.swing.JPanel;

/**
 *
 * @author Favak
 */
public class MikronIS2 {

    public static HlavniRamec ramecAplikace;
    public static PripojeniDB pripojeniDB;
    protected static GraphicsEnvironment ge;
    protected static GraphicsDevice gd;
    protected static GraphicsConfiguration gc;
    protected static int indexZakaznika;
    protected static int indexLieferschein;
    protected static int indexMaterial;
    protected static int indexVykres;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        pripojeniDB = new PripojeniDB();     
         
        //args[1] = "jdbc:postgresql://IP:port/db1?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
      
        // args[3] = "username";
        // args[5] = "password";
      
        int rc = pripojeniDB.navazSpojeniDB(args[1], args[3], args[5]);

        if (rc == 1) {
            JednoducheDialogy1.errAno(new JPanel(), "Selhání připojení",
                    "Selhalo připojení k databázi. Pravděpodobně bylo zadáno chybné jméno nebo heslo\n"
                    + "nebo byl detekován pokus o současné navázání více než jednoho spojení k databázi");
            return;
        }

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        gc = gd.getDefaultConfiguration();
        indexZakaznika = 0;
        indexLieferschein = 0;
        indexMaterial = 0;
        indexVykres = 0;

        final int left = Toolkit.getDefaultToolkit().getScreenInsets(gc).left;
        final int right = Toolkit.getDefaultToolkit().getScreenInsets(gc).right;
        final int top = Toolkit.getDefaultToolkit().getScreenInsets(gc).top;
        final int bottom = Toolkit.getDefaultToolkit().getScreenInsets(gc).bottom;

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width - left - right;
        final int height = screenSize.height - top - bottom;

        ramecAplikace = new HlavniRamec(gc);
        ramecAplikace.setViewPorts();
        ramecAplikace.setSize(width, height);
        ramecAplikace.nactiNastaveniSirek();
    }
}
