/*
 * HlavniRamec.java
 *
 * Created on 10. duben 2007, 21:11
 */
package mikronis2;

import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import javax.swing.UIManager;

// neco 
/**
 *
 * @author dave
 */
public class HlavniRamec extends javax.swing.JFrame {

    protected ActionListener alUdalosti;

    protected ObjednavkyPanel objednavka1;
    protected Termin1Panel termin1;
    protected Termin2Panel termin2;
    protected SkladPrebytkuPanel1 skladVyrobku1;
    protected SkladMaterialuPanel1 skladMaterialu1;
    protected VykresyPanel1 vykresy1;
    protected FakturyPanel faktury1;
    protected LieferscheinyPanel1 liefersheiny1;
    protected ZamestnanciPanel1 zamestnanci1;
    protected StrojePanel1 stroje1;
    protected StrojePrehled strojePrehled1;
    protected RamcoveObjednavkyPanel1 ramcovaObjednavka1;
    protected PruvodkyPanel pruv1;
    //protected KanbanPanel1 kanban1; 
    protected NabidkyPanel nabidka1;
    //protected StatistikyPanel1 statistiky1; 
    protected PotvrzeniPanel1 potvrzeni1;
    protected SeznamNabidekPanel1 seznamNab1;
    protected KooperacePanel1 kooperace1;
    public static final String oddelovac = System.getProperty("file.separator");
    public static final String loaderAdresar = System.getProperty("user.home") + oddelovac + ".mikrondata";
    public static final String mikronAdresar = System.getProperty("user.home") + oddelovac + ".MikronIS2_Loader";
    public static final String libAdresar = "lib";
    public static final String tiskAdresar = "tisk";
    public static final String konfiguraceAdresar = ".konfigurace";
    public static final String lineSeparator = System.getProperty("line.separator");
    public static String pdfReaderPath;
    public static String pictureViewerPath;
    public static String autoCadPath;

    /**
     * Creates new form HlavniRamec
     */
    public HlavniRamec(GraphicsConfiguration gc) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //  UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        nastavTridyObsluhyUdalosti();
        nastavTlacitka();
        nastavPosluchaceUdalosti();
        nactiNastaveniTabulek();
        vymazatTemp();

        this.setSize(gc.getBounds().getSize());

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                setExtendedState(MAXIMIZED_BOTH);
            }
        });

        this.setVisible(true);

    }//konec konstruktoru

    private void nastavTlacitka() {
        jRadioButtonLokal.setSelected(true);
        jRadioButtonRemote.setSelected(false);
    }

    protected void nastavTridyObsluhyUdalosti() {
        alUdalosti = new ALUdalosti();
    }

    protected void nastavPosluchaceUdalosti() {
        jRadioButtonLokal.addActionListener(alUdalosti);
        jRadioButtonRemote.addActionListener(alUdalosti);

        jRadioButtonLokal.setActionCommand("pripojeni_lokal");
        jRadioButtonRemote.setActionCommand("pripojeni_remote");
    }

    class ALUdalosti implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("pripojeni_lokal")) {
                jRadioButtonRemote.setSelected(false);
                //System.out.println("Lokal");
            }

            if (e.getActionCommand().equals("pripojeni_remote")) {
                jRadioButtonLokal.setSelected(false);
                //System.out.println("Remote");
            }
        }
    } //konec ALUdalosti

    protected void setViewPorts() {

        pruv1 = new PruvodkyPanel();
        objednavka1 = new ObjednavkyPanel();
        termin1 = new Termin1Panel();
        termin2 = new Termin2Panel();
        skladVyrobku1 = new SkladPrebytkuPanel1();
        skladMaterialu1 = new SkladMaterialuPanel1();
        nabidka1 = new NabidkyPanel();
        faktury1 = new FakturyPanel();
        vykresy1 = new VykresyPanel1();
        liefersheiny1 = new LieferscheinyPanel1();
        potvrzeni1 = new PotvrzeniPanel1();
        seznamNab1 = new SeznamNabidekPanel1();
        zamestnanci1 = new ZamestnanciPanel1();
        stroje1 = new StrojePanel1();
        strojePrehled1 = new StrojePrehled();
        ramcovaObjednavka1 = new RamcoveObjednavkyPanel1();
        kooperace1 = new KooperacePanel1();
        /*
         * 
         *
         * 
         * kanban1 = new KanbanPanel1(); 
         * statistiky1 = new StatistikyPanel1(); 
         *
         */
        JScrollPanePruvodky.setViewportView(pruv1);
        JScrollPaneObjednavky.setViewportView(objednavka1);
        jScrollPaneTermin1.setViewportView(termin1);
        jScrollPaneTermin2.setViewportView(termin2);
        jScrollPaneSklad1.setViewportView(skladVyrobku1);
        jScrollPaneSklad2.setViewportView(skladMaterialu1);
        jScrollPaneNabidky.setViewportView(nabidka1);
        jScrollPaneVykresy1.setViewportView(vykresy1);
        jScrollPaneFaktury1.setViewportView(faktury1);
        jScrollPaneLieferscheiny1.setViewportView(liefersheiny1);
        jScrollPanePotvrzeni.setViewportView(potvrzeni1);
        jScrollPaneSeznamNabidek1.setViewportView(seznamNab1);
        jScrollPaneZamestnanci.setViewportView(zamestnanci1);
        jScrollPaneZamestnanci.setViewportView(zamestnanci1);
        jScrollPaneStroje1.setViewportView(stroje1);
        jScrollPaneStrojePrehled1.setViewportView(strojePrehled1);
        jScrollPaneRamcoveObjednavky1.setViewportView(ramcovaObjednavka1);
        jScrollPaneKooperace.setViewportView(kooperace1);
        /*         
         * jScrollPaneKanban.setViewportView(kanban1);        
         */
    }

    protected void nactiNastaveniTabulek() {
        try {
            File f = new File(loaderAdresar + oddelovac + konfiguraceAdresar + oddelovac + "nastaveni.ini");
            BufferedReader input = new BufferedReader(new FileReader(f));
            String line = null;
            int indexTabulky = 0;
            while ((line = input.readLine()) != null) {
                switch (indexTabulky) {
                    case 0:
                        pdfReaderPath = line.substring(0, line.length());
                        break;
                    case 1:
                        pictureViewerPath = line.substring(0, line.length());
                        break;
                    case 2:
                        autoCadPath = line.substring(0, line.length());
                        break;
                }
                indexTabulky++;
            }
            /*
             * for (int i = 0; i < st1.JTableSubjektyTrhu.getColumnCount(); i++)
             * { System.out.println();
             * //st1.JTableSubjektyTrhu.getColumnModel().getColumn(i).setPreferredWidth(60);
             * }
             */
            input.close();
        } catch (Exception e) {
        }

    }

    private void vymazatTemp() {
        try {
            File adresar = new File(mikronAdresar);
            File smazat;
            String[] soubory = adresar.list();
            for (int i = 0; i < soubory.length; i++) {
                if ((soubory[i].toLowerCase().contains(".jpg")) || (soubory[i].toLowerCase().contains(".pdf")) || (soubory[i].toLowerCase().contains(".tif"))
                        || (soubory[i].toLowerCase().contains(".bmp"))
                        || (soubory[i].toLowerCase().contains(".dxf")) || (soubory[i].toLowerCase().contains(".dwg"))) {
                    smazat = new File(mikronAdresar + oddelovac + soubory[i]);
                    boolean s = smazat.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ulozNastaveniTabulek() {
        //System.out.println("ulozit tabulky");
        try {

            File f = new File(loaderAdresar + oddelovac + konfiguraceAdresar
                    + oddelovac + "tabulky.ini");
            FileOutputStream fos = new FileOutputStream(f);
            Writer out = new OutputStreamWriter(fos);

            ArrayList sirky = pruv1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = vykresy1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size(); i++) {
                out.write(sirky.get(i)
                        + ";");
            }
            out.write(lineSeparator);
            sirky
                    = zamestnanci1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size();
                    i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = skladVyrobku1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky
                    = skladMaterialu1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }

            out.write(lineSeparator);
            sirky = ramcovaObjednavka1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky
                    = objednavka1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size();
                    i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = termin1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = termin2.getSirkaSloupcu();
            for (int i = 0; i < sirky.size(); i++) {
                out.write(sirky.get(i)
                        + ";");
            }
            out.write(lineSeparator);
            sirky
                    = faktury1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size();
                    i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = liefersheiny1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky
                    = stroje1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = nabidka1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size(); i++) {
                out.write(sirky.get(i)
                        + ";");
            }
            out.write(lineSeparator);
            sirky
                    = potvrzeni1.getSirkaSloupcu();
            for (int i = 0; i < sirky.size();
                    i++) {
                out.write(sirky.get(i) + ";");
            }
            out.write(lineSeparator);
            sirky = seznamNab1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }

            out.write(lineSeparator);
            sirky = kooperace1.getSirkaSloupcu();
            for (int i = 0; i
                    < sirky.size(); i++) {
                out.write(sirky.get(i) + ";");
            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void nactiNastaveniSirek() {
        try {

            File f = new File(loaderAdresar + oddelovac + konfiguraceAdresar + oddelovac + "tabulky.ini");
            BufferedReader input = new BufferedReader(new FileReader(f));
            String line = null;
            int indexTabulky = 0;
            int pocIndex = 0;
            int index = 0;
            int konecIndex = 0;
            ArrayList sirky = new ArrayList();
            while ((line = input.readLine()) != null) {
                switch (indexTabulky) {
                    case 0:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0)
                                    && (index <= pruv1.tabulkaModelPruvodka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex, konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        pruv1.setSirkaSloupcu(sirky);
                        break;
                    case 1:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= vykresy1.tabulkaModelVykres.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        vykresy1.setSirkaSloupcu(sirky);
                        break;
                    case 2:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= zamestnanci1.tabulkaModelHlavni1.getColumnCount()
                                    + zamestnanci1.tabulkaModelDetail1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        zamestnanci1.setSirkaSloupcu(sirky);
                        break;
                    case 3:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= skladVyrobku1.tabulkaModelHlavni1.getColumnCount()
                                    + skladVyrobku1.tabulkaModelDetail1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        skladVyrobku1.setSirkaSloupcu(sirky);
                        break;
                    case 4:
                        pocIndex
                                = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= skladMaterialu1.tabulkaModelHlavni1.getColumnCount()
                                    + skladMaterialu1.tabulkaModelDetail1.getColumnCount()
                                    + skladMaterialu1.tabulkaModelPruvodky1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        skladMaterialu1.setSirkaSloupcu(sirky);
                        break;
                    /**/
 /* case 6:
                     pocIndex = 0;
                     konecIndex = line.indexOf(";");
                     index = 0;
                     sirky =
                     new ArrayList();
                     while (konecIndex <= line.length()) {
                     if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                     <= kanban1.tabulkaModelKanbanObj1.getColumnCount()
                     + kanban1.tabulkaModelObjednavka1.getColumnCount())) {
                     sirky.add(Integer.valueOf(line.substring(pocIndex,
                     konecIndex)).intValue());
                     pocIndex = konecIndex + 1;
                     konecIndex =
                     line.indexOf(";", konecIndex + 1);
                     index++;
                     } else {
                     break;
                     }

                     }
                     kanban1.setSirkaSloupcu(sirky);
                     break;*/
                    case 6:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= objednavka1.tabulkaModelObjednavka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        objednavka1.setSirkaSloupcu(sirky);
                        break;
                    case 7:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= termin1.tabulkaModelObjednavka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        termin1.setSirkaSloupcu(sirky);
                        break;
                    case 8:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= termin2.tabulkaModelObjednavka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        termin2.setSirkaSloupcu(sirky);
                        break;
                    case 9:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= faktury1.tabulkaModelObjednavka1.getColumnCount()
                                    + faktury1.tabulkaModelFaktura1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        faktury1.setSirkaSloupcu(sirky);
                        break;
                    case 10:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= liefersheiny1.tabulkaModelObjednavka1.getColumnCount()
                                    + liefersheiny1.tabulkaModelLiefer1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        liefersheiny1.setSirkaSloupcu(sirky);
                        break;
                    case 11:
                        pocIndex
                                = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= stroje1.tabulkaModelHlavni1.getColumnCount()
                                    + stroje1.tabulkaModelDetail1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        stroje1.setSirkaSloupcu(sirky);
                        break;
                    case 12:
                        pocIndex
                                = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= nabidka1.tabulkaModelNabidka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        nabidka1.setSirkaSloupcu(sirky);
                        break;
                    case 13:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= potvrzeni1.tabulkaModelHlavni1.getColumnCount()
                                    + potvrzeni1.tabulkaModelObjednavka1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        potvrzeni1.setSirkaSloupcu(sirky);
                        break;
                    case 14:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= seznamNab1.tabulkaModelHlavni1.getColumnCount()
                                    + seznamNab1.tabulkaModelDetail1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        seznamNab1.setSirkaSloupcu(sirky);
                        break;
                    case 15:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= kooperace1.tabulkaModelKooperace1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        kooperace1.setSirkaSloupcu(sirky);
                        break;
                    case 5:
                        pocIndex = 0;
                        konecIndex = line.indexOf(";");
                        index = 0;
                        sirky = new ArrayList();
                        while (konecIndex <= line.length()) {
                            if ((konecIndex >= 0) && (pocIndex >= 0) && (index
                                    <= ramcovaObjednavka1.tabulkaModelHlavni1.getColumnCount()
                                    + ramcovaObjednavka1.tabulkaModelDetail1.getColumnCount())) {
                                sirky.add(Integer.valueOf(line.substring(pocIndex,
                                        konecIndex)).intValue());
                                pocIndex = konecIndex + 1;
                                konecIndex
                                        = line.indexOf(";", konecIndex + 1);
                                index++;
                            } else {
                                break;
                            }

                        }
                        ramcovaObjednavka1.setSirkaSloupcu(sirky);
                        break;
                }
                indexTabulky++;
            }

            input.close();

        } catch (Exception e) {
            e.printStackTrace();
            ulozNastaveniTabulek();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        JSP_ScrollZalozky = new javax.swing.JScrollPane();
        JTP_Zalozky = new javax.swing.JTabbedPane();
        JScrollPanePruvodky = new javax.swing.JScrollPane();
        jScrollPaneVykresy1 = new javax.swing.JScrollPane();
        jScrollPaneZamestnanci = new javax.swing.JScrollPane();
        jScrollPaneSklad1 = new javax.swing.JScrollPane();
        jScrollPaneSklad2 = new javax.swing.JScrollPane();
        jScrollPaneRamcoveObjednavky1 = new javax.swing.JScrollPane();
        JScrollPaneObjednavky = new javax.swing.JScrollPane();
        jScrollPaneTermin1 = new javax.swing.JScrollPane();
        jScrollPaneTermin2 = new javax.swing.JScrollPane();
        jScrollPaneFaktury1 = new javax.swing.JScrollPane();
        jScrollPaneLieferscheiny1 = new javax.swing.JScrollPane();
        jScrollPaneStroje1 = new javax.swing.JScrollPane();
        jScrollPaneStrojePrehled1 = new javax.swing.JScrollPane();
        jScrollPaneNabidky = new javax.swing.JScrollPane();
        jScrollPaneStatistiky = new javax.swing.JScrollPane();
        jScrollPanePotvrzeni = new javax.swing.JScrollPane();
        jScrollPaneSeznamNabidek1 = new javax.swing.JScrollPane();
        jScrollPaneKooperace = new javax.swing.JScrollPane();
        JMB_ListaMenu = new javax.swing.JMenuBar();
        JMHlavniMenu = new javax.swing.JMenu();
        JMNastaveniMenu1 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonLokal = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonRemote = new javax.swing.JRadioButtonMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPredvolby = new javax.swing.JMenuItem();
        jMIUlozit = new javax.swing.JMenuItem();
        jMINacist = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMIZakaznikNovy = new javax.swing.JMenuItem();
        jMenuItemZakaznik = new javax.swing.JMenuItem();

        FormListener formListener = new FormListener();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MikronIS 2016");
        setIconImage(getIconImage());
        setName(""); // NOI18N

        JTP_Zalozky.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        JTP_Zalozky.setMinimumSize(new java.awt.Dimension(103, 287));
        JTP_Zalozky.setPreferredSize(new java.awt.Dimension(103, 266));
        JTP_Zalozky.addTab("Průvodky", JScrollPanePruvodky);
        JTP_Zalozky.addTab("Výkresy", jScrollPaneVykresy1);
        JTP_Zalozky.addTab("Zaměstnanci", jScrollPaneZamestnanci);
        JTP_Zalozky.addTab("Přebytky", jScrollPaneSklad1);
        JTP_Zalozky.addTab("Materiál", jScrollPaneSklad2);
        JTP_Zalozky.addTab("Rámcové objednávky", jScrollPaneRamcoveObjednavky1);
        JTP_Zalozky.addTab("Objednávky", JScrollPaneObjednavky);
        JTP_Zalozky.addTab("Termíny 1", jScrollPaneTermin1);
        JTP_Zalozky.addTab("Termíny 2", jScrollPaneTermin2);
        JTP_Zalozky.addTab("Faktury", jScrollPaneFaktury1);
        JTP_Zalozky.addTab("Lieferscheiny", jScrollPaneLieferscheiny1);
        JTP_Zalozky.addTab("Stroje historie", jScrollPaneStroje1);
        JTP_Zalozky.addTab("Stroje", jScrollPaneStrojePrehled1);
        JTP_Zalozky.addTab("Nabídky", jScrollPaneNabidky);
        JTP_Zalozky.addTab("Statistiky", jScrollPaneStatistiky);
        JTP_Zalozky.addTab("Potvrzení", jScrollPanePotvrzeni);
        JTP_Zalozky.addTab("Seznamy nabídek", jScrollPaneSeznamNabidek1);
        JTP_Zalozky.addTab("Kooperace", jScrollPaneKooperace);

        JSP_ScrollZalozky.setViewportView(JTP_Zalozky);

        JMHlavniMenu.setText("Menu");
        JMB_ListaMenu.add(JMHlavniMenu);

        JMNastaveniMenu1.setText("Nastavení");

        jMenu1.setText("Připojení");

        jRadioButtonLokal.setSelected(true);
        jRadioButtonLokal.setText("Lokální připojení");
        jMenu1.add(jRadioButtonLokal);

        jRadioButtonRemote.setSelected(true);
        jRadioButtonRemote.setText("Vzdálený přístup");
        jMenu1.add(jRadioButtonRemote);
        jMenu1.add(jSeparator1);

        jMenuItemPredvolby.setText("Předvolby");
        jMenu1.add(jMenuItemPredvolby);

        JMNastaveniMenu1.add(jMenu1);

        jMIUlozit.setText("Uložit nastavení sloupců");
        jMIUlozit.addMouseListener(formListener);
        jMIUlozit.addItemListener(formListener);
        jMIUlozit.addActionListener(formListener);
        JMNastaveniMenu1.add(jMIUlozit);

        jMINacist.setText("Načíst nastavení sloupců");
        jMINacist.addMouseListener(formListener);
        JMNastaveniMenu1.add(jMINacist);

        JMB_ListaMenu.add(JMNastaveniMenu1);

        jMenu2.setText("Zákazníci");

        jMIZakaznikNovy.setText("Nový zákazník");
        jMIZakaznikNovy.addActionListener(formListener);
        jMenu2.add(jMIZakaznikNovy);

        jMenuItemZakaznik.setText("Upravit zákazníka");
        jMenuItemZakaznik.addMouseListener(formListener);
        jMenuItemZakaznik.addActionListener(formListener);
        jMenu2.add(jMenuItemZakaznik);

        JMB_ListaMenu.add(jMenu2);

        setJMenuBar(JMB_ListaMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JSP_ScrollZalozky, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JSP_ScrollZalozky, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("Mikron IS");

        pack();
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.MouseListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == jMIUlozit) {
                HlavniRamec.this.jMIUlozitActionPerformed(evt);
            }
            else if (evt.getSource() == jMIZakaznikNovy) {
                HlavniRamec.this.jMIZakaznikNovyActionPerformed(evt);
            }
            else if (evt.getSource() == jMenuItemZakaznik) {
                HlavniRamec.this.jMenuItemZakaznikActionPerformed(evt);
            }
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getSource() == jMIUlozit) {
                HlavniRamec.this.jMIUlozitItemStateChanged(evt);
            }
        }

        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == jMIUlozit) {
                HlavniRamec.this.jMIUlozitMouseClicked(evt);
            }
            else if (evt.getSource() == jMINacist) {
                HlavniRamec.this.jMINacistMouseClicked(evt);
            }
            else if (evt.getSource() == jMenuItemZakaznik) {
                HlavniRamec.this.jMenuItemZakaznikMouseClicked(evt);
            }
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        public void mousePressed(java.awt.event.MouseEvent evt) {
        }

        public void mouseReleased(java.awt.event.MouseEvent evt) {
        }
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemZakaznikMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemZakaznikMouseClicked

        ZakaznikFrame1 uprava = new ZakaznikFrame1(false);

    }//GEN-LAST:event_jMenuItemZakaznikMouseClicked

    private void jMenuItemZakaznikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemZakaznikActionPerformed

        ZakaznikFrame1 uprava = new ZakaznikFrame1(false);

    }//GEN-LAST:event_jMenuItemZakaznikActionPerformed

    private void jMINacistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMINacistMouseClicked
        nactiNastaveniSirek();
    }//GEN-LAST:event_jMINacistMouseClicked

    private void jMIUlozitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMIUlozitMouseClicked
        ulozNastaveniTabulek();
    }//GEN-LAST:event_jMIUlozitMouseClicked

    private void jMIUlozitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jMIUlozitItemStateChanged
    }//GEN-LAST:event_jMIUlozitItemStateChanged

    private void jMIUlozitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIUlozitActionPerformed
        ulozNastaveniTabulek();
    }//GEN-LAST:event_jMIUlozitActionPerformed

    private void jMIZakaznikNovyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIZakaznikNovyActionPerformed
        ZakaznikFrame1 novy = new ZakaznikFrame1(true);
    }//GEN-LAST:event_jMIZakaznikNovyActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JMenuBar JMB_ListaMenu;
    protected javax.swing.JMenu JMHlavniMenu;
    protected javax.swing.JMenu JMNastaveniMenu1;
    protected javax.swing.JScrollPane JSP_ScrollZalozky;
    protected javax.swing.JScrollPane JScrollPaneObjednavky;
    protected javax.swing.JScrollPane JScrollPanePruvodky;
    protected javax.swing.JTabbedPane JTP_Zalozky;
    protected javax.swing.JMenuItem jMINacist;
    protected javax.swing.JMenuItem jMIUlozit;
    protected javax.swing.JMenuItem jMIZakaznikNovy;
    protected javax.swing.JMenu jMenu1;
    protected javax.swing.JMenu jMenu2;
    protected javax.swing.JMenuItem jMenuItemPredvolby;
    protected javax.swing.JMenuItem jMenuItemZakaznik;
    protected javax.swing.JPopupMenu jPopupMenu1;
    protected javax.swing.JRadioButtonMenuItem jRadioButtonLokal;
    protected javax.swing.JRadioButtonMenuItem jRadioButtonRemote;
    protected javax.swing.JScrollPane jScrollPaneFaktury1;
    protected javax.swing.JScrollPane jScrollPaneKooperace;
    protected javax.swing.JScrollPane jScrollPaneLieferscheiny1;
    protected javax.swing.JScrollPane jScrollPaneNabidky;
    protected javax.swing.JScrollPane jScrollPanePotvrzeni;
    protected javax.swing.JScrollPane jScrollPaneRamcoveObjednavky1;
    protected javax.swing.JScrollPane jScrollPaneSeznamNabidek1;
    protected javax.swing.JScrollPane jScrollPaneSklad1;
    protected javax.swing.JScrollPane jScrollPaneSklad2;
    protected javax.swing.JScrollPane jScrollPaneStatistiky;
    protected javax.swing.JScrollPane jScrollPaneStroje1;
    protected javax.swing.JScrollPane jScrollPaneStrojePrehled1;
    protected javax.swing.JScrollPane jScrollPaneTermin1;
    protected javax.swing.JScrollPane jScrollPaneTermin2;
    protected javax.swing.JScrollPane jScrollPaneVykresy1;
    protected javax.swing.JScrollPane jScrollPaneZamestnanci;
    protected javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
