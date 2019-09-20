/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRenderer1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.*;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;
import java.util.Vector;

/**
 *
 * @author Favak
 */
public class ZamestnanciPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelDetail1, tableModelHlavni1;
    protected TabulkaModelDetail tabulkaModelDetail1;
    protected TabulkaModelHlavni tabulkaModelHlavni1;
    protected ListSelectionModel lsmDetail1, lsmHlavni1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected WinUdalosti winUdalosti; 
    protected Vector vrPrace1;
    protected Vector vsPrace1;
    protected Vector vrPraceTisk1;
    protected Vector vsPraceTisk1;
    protected Vector vrPraceDetail1;
    protected Vector vsPraceDetail1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZamestnanci;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;
    protected int indexOznaceno;
    protected String dobaPraceBezPresahu = "";
    protected String dobaPracePresah = "";

    /**
     * Creates new form ObjednavkyPanel
     */
    public ZamestnanciPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuHlavni1();
        nastavTabulkuDetail1();

        nastavPosluchaceUdalostiOvladace();
        this.setVisible(true);
    }

    protected void nastavParametry() {
        
        vrPrace1 = new Vector();
        vsPrace1 = new Vector();  
        
        vrPraceDetail1 = new Vector();
        vsPraceDetail1 = new Vector();   
        
        vrPraceTisk1 = new Vector();
        vsPraceTisk1 = new Vector();   

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);
        
        try {
            ResultSet data = PripojeniDB.dotazS("SELECT ('01.' || to_char(current_timestamp, 'MM.YYYY')), to_char(current_timestamp, 'DD.MM.YYYY')");
            while (data.next()) {
                PraceOdTextField1.setText(data.getString(1));
                PraceDoTextField1.setText(data.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void nastavParametryTabulek() {
        tabulkaModelDetail1 = new TabulkaModelDetail();

        tabulkaDetail.setModel(tabulkaModelDetail1);
        tabulkaDetail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmDetail1 = tabulkaDetail.getSelectionModel();
        tableModelDetail1 = tabulkaDetail.getModel();

        tabulkaDetail.setPreferredScrollableViewportSize(new Dimension(800, 300));

        tabulkaModelHlavni1 = new TabulkaModelHlavni();

        tabulkaHlavni.setModel(tabulkaModelHlavni1);
        tabulkaHlavni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaHlavni.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmHlavni1 = tabulkaHlavni.getSelectionModel();
        tableModelHlavni1 = tabulkaHlavni.getModel();

        tabulkaHlavni.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmHlavni1.removeListSelectionListener(lslUdalosti);
        tableModelHlavni1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelHlavni1.addTableModelListener(tmlUdalosti);
        lsmHlavni1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        jBVycistitFiltr.addActionListener(alUdalosti);
        ZamestnanecComboBox1.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);        
        NovyZamestnanecButton1.addActionListener(alUdalosti);
        UpravitUdajeButton1.addActionListener(alUdalosti);
        TiskVypisuButton1.addActionListener(alUdalosti);
        TiskKartaButton.addActionListener(alUdalosti);
        KonecPraceButton1.addActionListener(alUdalosti);

        jBVycistitFiltr.setActionCommand("VycistiFiltrObjednavka1");
        VyhledatButton1.setActionCommand("Hledat");      
        NovyZamestnanecButton1.setActionCommand("NovyZamestnanec");
        UpravitUdajeButton1.setActionCommand("UpravitUdaje");
        ZamestnanecComboBox1.setActionCommand("Hledat");
        TiskVypisuButton1.setActionCommand("TiskVypisu");
        TiskKartaButton.setActionCommand("TiskKarta");
        KonecPraceButton1.setActionCommand("KonecPrace");      
    }

    protected void nastavTabulkuDetail1() {
        /*TableColumn column = null;
        column = tabulkaDetail.getColumnModel().getColumn(0);
        column.setPreferredWidth(80);

        column = tabulkaDetail.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulkaDetail.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulkaDetail.getColumnModel().getColumn(3);
        column.setPreferredWidth(30);

        column = tabulkaDetail.getColumnModel().getColumn(4);
        column.setPreferredWidth(115);*/

        zrusPosluchaceUdalostiTabulky();
        if (vrPrace1.size() > 0) {
            //TODO : getDataTabulkaDetail1(WIDTH);
            tabulkaModelDetail1.pridejSadu();
        }
        nastavPosluchaceUdalostiTabulky();


    }// konec nastavTabulkuBT1

    protected void nastavTabulkuHlavni1() {
       /* TableColumn column = null;
        column = tabulkaHlavni.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);

        column = tabulkaHlavni.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulkaHlavni.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulkaHlavni.getColumnModel().getColumn(3);
        column.setPreferredWidth(90);

        column = tabulkaHlavni.getColumnModel().getColumn(4);
        column.setPreferredWidth(90);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void nastavTridyObsluhyUdalosti() {
        tmlUdalosti = new TMLUdalosti();
        lslUdalosti = new LSLUdalosti();
        alUdalosti = new ALUdalosti();
        flUdalosti = new FLUdalosti();
        winUdalosti = new WinUdalosti();
    }

      protected ArrayList getSirkaSloupcu() {
        ArrayList out = new ArrayList();       
        for (int i = 0; i < tabulkaHlavni.getColumnCount(); i++) {
            out.add(tabulkaHlavni.getColumnModel().getColumn(i).getPreferredWidth());
        }
        for (int i = 0; i < tabulkaDetail.getColumnCount(); i++) {
            out.add(tabulkaDetail.getColumnModel().getColumn(i).getPreferredWidth());
        }
        return out;
    }

    protected void setSirkaSloupcu(ArrayList in) {
         for (int i = 0; i < tabulkaHlavni.getColumnCount(); i++) {
            tabulkaHlavni.getColumnModel().getColumn(i).setPreferredWidth((Integer) in.get(i));
        }
        for (int i = tabulkaHlavni.getColumnCount(); i < tabulkaDetail.getColumnCount() + tabulkaHlavni.getColumnCount(); i++) {
            tabulkaDetail.getColumnModel().getColumn(i - tabulkaHlavni.getColumnCount()).setPreferredWidth((Integer) in.get(i));
        }
    }

    protected void initRoletky() {
        roletkaModelZamestnanci = new RoletkaUniverzalRozsirenaModel1(
                "SELECT zamestnanci_id, zamestnanci_jmeno || ' ' || zamestnanci_prijmeni FROM spolecne.zamestnanci WHERE zamestnanci_aktivni IS TRUE "
                + "ORDER BY zamestnanci_prijmeni", "Neurčen", null,
                "V databázi nejsou zadáni zaměstnanci", 0); // bylo ...vs_id
        ZamestnanecComboBox1.setModel(roletkaModelZamestnanci);
    }

    protected void getDataTabulkaHlavni() {
        Vector vsPracePom1 = new Vector();
        Vector vrPracePom1 = new Vector();
        vrPrace1.removeAllElements();
        vsPrace1.removeAllElements();
        vrPraceTisk1.removeAllElements();
        vsPraceTisk1.removeAllElements();
        tabulkaModelHlavni1.oznamZmenu();
        boolean osetritDatumOd = TextFunkce1.osetriDatum(PraceOdTextField1.getText());
        boolean osetritDatumDo = TextFunkce1.osetriDatum(PraceDoTextField1.getText());
        System.out.println("Datumy : " + osetritDatumOd + " " + osetritDatumDo );
        long celkovyCasTermin = 0;
        Vector pruvodkyIdPomocne = new Vector();
        try {
            String dotaz = "";
            if (VykresCisloTextField1.getText().trim().length() > 0) {
                dotaz = "SELECT * FROM (SELECT zamestnanci_stroje_transakce_pruvodky_id, MIN(zamestnanci_stroje_transakce_log_timestamp) AS minimumTimestamp "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "CROSS JOIN spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE zamestnanci_stroje_transakce_zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " ";
                if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                    dotaz += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' ";
                }
                dotaz += "AND vykresy_cislo LIKE '%" + VykresCisloTextField1.getText().trim() + "%' "
                        + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                        + "AND vykresy_id = pruvodky_cislo_vykresu ";
                dotaz += "GROUP BY zamestnanci_stroje_transakce_pruvodky_id) s "
                        + "ORDER BY s.minimumTimestamp ASC ";
            } else {
                dotaz = "SELECT * FROM (SELECT zamestnanci_stroje_transakce_pruvodky_id, MIN(zamestnanci_stroje_transakce_log_timestamp) AS minimumTimestamp "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " ";
                if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                    dotaz += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' ";
                }
                dotaz += "GROUP BY zamestnanci_stroje_transakce_pruvodky_id) s "
                        + "ORDER BY s.minimumTimestamp ASC ";
            }

            ResultSet pruvodkyTransakce = PripojeniDB.dotazS(dotaz);
            while (pruvodkyTransakce.next()) {
                pruvodkyIdPomocne.add(pruvodkyTransakce.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pruvodkyIdPomocne.size(); i++) {
            if (((Integer) pruvodkyIdPomocne.get(i)).intValue() != 150) {
                Vector strojeIdPomocne = new Vector();
                try {
                    ResultSet strojeTransakce = PripojeniDB.dotazS(
                            "SELECT DISTINCT(zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id  "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Integer) pruvodkyIdPomocne.get(i) + " "
                            + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                            + "ORDER BY zamestnanci_stroje_transakce_stroje_id ASC");
                    while (strojeTransakce.next()) {
                        strojeIdPomocne.add(strojeTransakce.getInt(1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long time135 = 0;
                long time246 = 0;
                long time135Tisk = 0;
                long time246Tisk = 0;
                String aktivni = "NE";
                String datumZacatek = "";
                for (int z = 0; z < strojeIdPomocne.size(); z++) {
                    time135Tisk = 0;
                    time246Tisk = 0;
                    vrPracePom1.removeAllElements();
                    vsPracePom1.removeAllElements();
                    try {
                        ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                                "SELECT zamestnanci_stroje_transakce_id, "
                                + "zamestnanci_stroje_transakce_druh_id, "
                                + "stroje_id, "
                                + "stroje_nazev, "
                                + "zamestnanci_id, "
                                + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                                + "zamestnanci_stroje_transakce_log_timestamp, "
                                + "current_timestamp, "
                                + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YY HH24:MI') AS datum, "
                                + "pruvodky_id, "
                                + "pruvodky_nazev, "
                                + "vykresy_cislo, "
                                + "(pruvodky_pocet_kusu + pruvodky_pocet_kusu_sklad) as pocet "
                                + "FROM spolecne.zamestnanci_stroje_transakce "
                                + "CROSS JOIN spolecne.zamestnanci "
                                + "CROSS JOIN spolecne.stroje "
                                + "CROSS JOIN spolecne.pruvodky "
                                + "CROSS JOIN spolecne.vykresy "
                                + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Integer) pruvodkyIdPomocne.get(i) + " "
                                + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                                + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                                + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                                + "AND vykresy_id = pruvodky_cislo_vykresu "
                                + "AND stroje_id = " + (Integer) strojeIdPomocne.get(z) + " "
                                + "AND zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                                + "ORDER BY  zamestnanci_stroje_transakce_log_timestamp ASC");

                        while (transakceZamestnanci.next()) {
                            vsPracePom1 = new Vector();
                            vsPracePom1.add(transakceZamestnanci.getLong(1));    // transakce
                            vsPracePom1.add(transakceZamestnanci.getInt(2));     // druh
                            vsPracePom1.add(transakceZamestnanci.getInt(3));     // stroj id
                            vsPracePom1.add(transakceZamestnanci.getString(4));  // stroj nazev
                            vsPracePom1.add(transakceZamestnanci.getInt(5));     // zamestnanec id
                            vsPracePom1.add(transakceZamestnanci.getString(6));  // zamestnanec jmeno
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(7)); // cas
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(8)); // nyni cas
                            vsPracePom1.add(transakceZamestnanci.getString(9));  // datum
                            vsPracePom1.add(transakceZamestnanci.getLong(10));    // pruvodky id
                            vsPracePom1.add(transakceZamestnanci.getString(11));  // pruvodky_nazev
                            vsPracePom1.add(transakceZamestnanci.getString(12));  // cislo vykresu
                            vsPracePom1.add(transakceZamestnanci.getInt(13));     // pocet kusu
                            vrPracePom1.add(vsPracePom1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int indexTransakce = 0; indexTransakce < vrPracePom1.size(); indexTransakce++) {

                        if ((indexTransakce % 2 == 0) && (indexTransakce != vrPracePom1.size() - 1)) {
                            time135 = ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime();
                            time135Tisk = ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime();
                        } else if (indexTransakce % 2 == 1) {
                            time246 += (((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime() - time135);
                            time246Tisk += (((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime() - time135Tisk);
                            time135 = 0;
                            time135Tisk = 0;
                        } else if ((indexTransakce == vrPracePom1.size() - 1) && (indexTransakce % 2 == 0)) {
                            time246 += ((((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(7)).getTime() - ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime()));
                            time246Tisk += ((((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(7)).getTime() - ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime()));
                            aktivni = "ANO";
                        }

                    }
                    if (z == 0) {
                        datumZacatek = (String) ((Vector) vrPracePom1.get(0)).get(8);
                    }
                    Timestamp celkovyCas = new Timestamp(time246Tisk);
                    //celkovyCasTermin += time246Tisk;
                    int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                    int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                    String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                    vsPraceTisk1 = new Vector();
                    // vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8));
                    // System.out.println((String) ((Vector) vrPracePom1.get(0)).get(11) + " x " + (String) ((Vector) vrPracePom1.get(0)).get(8));
                    vsPraceTisk1.add(datumZacatek);
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(8));
                    vsPraceTisk1.add((Long) ((Vector) vrPracePom1.get(0)).get(9)); // pruvodka id
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // pruvodka nazev
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(11)); // cislo vykresu
                    vsPraceTisk1.add((Integer) ((Vector) vrPracePom1.get(0)).get(12)); // pocet kusu
                    if (dnu > 0) {
                        vsPraceTisk1.add((24 * dnu + hodin) + ":" + minsec);
                    } else {
                        vsPraceTisk1.add(hodin + ":" + minsec);
                    }
                    vsPraceTisk1.add(aktivni);
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(3));
                    vrPraceTisk1.add(vsPraceTisk1);
                }
                Timestamp celkovyCas = new Timestamp(time246);
                celkovyCasTermin += time246;
                int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                vsPrace1 = new Vector();
                // vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8));
                // System.out.println((String) ((Vector) vrPracePom1.get(0)).get(11) + " x " + (String) ((Vector) vrPracePom1.get(0)).get(8));
                vsPrace1.add(datumZacatek);
                vsPrace1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(8));
                vsPrace1.add((Long) ((Vector) vrPracePom1.get(0)).get(9)); // pruvodka id
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // pruvodka nazev
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(11)); // cislo vykresu
                vsPrace1.add((Integer) ((Vector) vrPracePom1.get(0)).get(12)); // pocet kusu
                if (dnu > 0) {
                    vsPrace1.add((24 * dnu + hodin) + ":" + minsec);
                } else {
                    vsPrace1.add(hodin + ":" + minsec);
                }
                vsPrace1.add(aktivni);
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(3));
                vrPrace1.add(vsPrace1);

            }
            if (((Integer) pruvodkyIdPomocne.get(i)).intValue() == 150) {
                Vector strojeIdPomocne = new Vector();
                try {
                    ResultSet strojeTransakce = PripojeniDB.dotazS(
                            "SELECT DISTINCT(zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id  "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Integer) pruvodkyIdPomocne.get(i) + " "
                            + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                            + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' "
                            + "ORDER BY zamestnanci_stroje_transakce_stroje_id ASC");
                    while (strojeTransakce.next()) {
                        strojeIdPomocne.add(strojeTransakce.getInt(1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long time135 = 0;
                long time246 = 0;
                long time135Tisk = 0;
                long time246Tisk = 0;
                String aktivni = "NE";
                String datumZacatek = "";
                for (int z = 0; z < strojeIdPomocne.size(); z++) {
                    vrPracePom1.removeAllElements();
                    vsPracePom1.removeAllElements();
                    time135Tisk = 0;
                    time246Tisk = 0;
                    try {
                        ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                                "SELECT zamestnanci_stroje_transakce_id, "
                                + "zamestnanci_stroje_transakce_druh_id, "
                                + "stroje_id, "
                                + "stroje_nazev, "
                                + "zamestnanci_id, "
                                + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                                + "zamestnanci_stroje_transakce_log_timestamp, "
                                + "current_timestamp, "
                                + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YY HH24:MI') AS datum, "
                                + "pruvodky_id, "
                                + "pruvodky_nazev, "
                                + "vykresy_cislo, "
                                + "(pruvodky_pocet_kusu + pruvodky_pocet_kusu_sklad) as pocet "
                                + "FROM spolecne.zamestnanci_stroje_transakce "
                                + "CROSS JOIN spolecne.zamestnanci "
                                + "CROSS JOIN spolecne.stroje "
                                + "CROSS JOIN spolecne.pruvodky "
                                + "CROSS JOIN spolecne.vykresy "
                                + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Integer) pruvodkyIdPomocne.get(i) + " "
                                + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                                + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                                + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                                + "AND vykresy_id = pruvodky_cislo_vykresu "
                                + "AND stroje_id = " + (Integer) strojeIdPomocne.get(z) + " "
                                + "AND zamestnanci_id = " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                                + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                                + PraceDoTextField1.getText() + "' "
                                + "ORDER BY  zamestnanci_stroje_transakce_log_timestamp ASC");
                        while (transakceZamestnanci.next()) {
                            vsPracePom1 = new Vector();
                            vsPracePom1.add(transakceZamestnanci.getLong(1));    // transakce
                            vsPracePom1.add(transakceZamestnanci.getInt(2));     // druh
                            vsPracePom1.add(transakceZamestnanci.getInt(3));     // stroj id
                            vsPracePom1.add(transakceZamestnanci.getString(4));  // stroj nazev
                            vsPracePom1.add(transakceZamestnanci.getInt(5));     // zamestnanec id
                            vsPracePom1.add(transakceZamestnanci.getString(6));  // zamestnanec jmeno
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(7)); // cas
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(8)); // nyni cas
                            vsPracePom1.add(transakceZamestnanci.getString(9));  // datum
                            vsPracePom1.add(transakceZamestnanci.getLong(10));    // pruvodky id
                            vsPracePom1.add(transakceZamestnanci.getString(11));  // pruvodky_nazev
                            vsPracePom1.add(transakceZamestnanci.getString(12));  // cislo vykresu
                            vsPracePom1.add(transakceZamestnanci.getInt(13));     // pocet kusu
                            vrPracePom1.add(vsPracePom1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int indexTransakce = 0; indexTransakce < vrPracePom1.size(); indexTransakce++) {

                        if ((indexTransakce % 2 == 0) && (indexTransakce != vrPracePom1.size() - 1)) {
                            time135 = ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime();
                            time135Tisk = ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime();
                        } else if (indexTransakce % 2 == 1) {
                            time246 += (((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime() - time135);
                            time246Tisk += (((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime() - time135Tisk);
                            time135 = 0;
                            time135Tisk = 0;
                        } else if ((indexTransakce == vrPracePom1.size() - 1) && (indexTransakce % 2 == 0)) {
                            time246 += ((((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(7)).getTime() - ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime()));
                            time246Tisk += ((((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(7)).getTime() - ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(6)).getTime()));
                            aktivni = "ANO";
                        }

                    }
                    if (z == 0) {
                        datumZacatek = (String) ((Vector) vrPracePom1.get(0)).get(8);
                    }
                    Timestamp celkovyCas = new Timestamp(time246Tisk);
                    //celkovyCasTermin += time246Tisk;
                    int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                    int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                    String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                    vsPraceTisk1 = new Vector();
                    // vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8));
                    // System.out.println((String) ((Vector) vrPracePom1.get(0)).get(11) + " x " + (String) ((Vector) vrPracePom1.get(0)).get(8));
                    vsPraceTisk1.add(datumZacatek);
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(8));
                    vsPraceTisk1.add((Long) ((Vector) vrPracePom1.get(0)).get(9)); // pruvodka id
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // pruvodka nazev
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(11)); // cislo vykresu
                    vsPraceTisk1.add((Integer) ((Vector) vrPracePom1.get(0)).get(12)); // pocet kusu
                    if (dnu > 0) {
                        vsPraceTisk1.add((24 * dnu + hodin) + ":" + minsec);
                    } else {
                        vsPraceTisk1.add(hodin + ":" + minsec);
                    }
                    vsPraceTisk1.add(aktivni);
                    vsPraceTisk1.add((String) ((Vector) vrPracePom1.get(0)).get(3));
                    vrPraceTisk1.add(vsPraceTisk1);
                }
                Timestamp celkovyCas = new Timestamp(time246);
                celkovyCasTermin += time246;
                int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                vsPrace1 = new Vector();
                // vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8));
                // System.out.println((String) ((Vector) vrPracePom1.get(0)).get(11) + " x " + (String) ((Vector) vrPracePom1.get(0)).get(8));
                vsPrace1.add(datumZacatek);
                vsPrace1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(8));
                vsPrace1.add((Long) ((Vector) vrPracePom1.get(0)).get(9)); // pruvodka id
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // pruvodka nazev
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(11)); // cislo vykresu
                vsPrace1.add((Integer) ((Vector) vrPracePom1.get(0)).get(12)); // pocet kusu
                if (dnu > 0) {
                    vsPrace1.add((24 * dnu + hodin) + ":" + minsec);
                } else {
                    vsPrace1.add(hodin + ":" + minsec);
                }
                vsPrace1.add(aktivni);
                vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(3));
                vrPrace1.add(vsPrace1);
            }
        }
        // Timestamp nula = new Timestamp(-3600000 + 82800000);
        // System.out.println("nula : " + nula.toString());


        Timestamp celkovyCasTerminTimestamp = new Timestamp(celkovyCasTermin - 3600000);
        // System.out.println("cas presah : " + celkovyCasTerminTimestamp.toString());
        int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCasTerminTimestamp)).intValue() - 1;
        int hodin = 0;
        //  if(dnu > 0) {
        hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCasTerminTimestamp)).intValue();
        /* }
         else {
         hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCasTerminTimestamp)).intValue() - 1;
         }*/

        String minsec = new SimpleDateFormat("mm").format(celkovyCasTerminTimestamp);
        if (dnu > 0) {
            DobaPracePresahyTextField1.setText((24 * dnu + hodin) + ":" + minsec);
            dobaPracePresah = (dnu * 24 + hodin) + ":" + minsec;
        } else {
            DobaPracePresahyTextField1.setText(hodin + ":" + minsec);
            dobaPracePresah = hodin + ":" + minsec;
        }

        tabulkaModelHlavni1.oznamZmenu();
        Vector vsCasPom = new Vector();
        Vector vrCasPom = new Vector();
        osetritDatumOd = TextFunkce1.osetriDatum(PraceOdTextField1.getText());
        osetritDatumDo = TextFunkce1.osetriDatum(PraceDoTextField1.getText());

        Vector stroje = new Vector();
        try {
            String dotaz = "SELECT DISTINCT ON(zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id "
                    + "FROM spolecne.zamestnanci_stroje_transakce "
                    + "WHERE zamestnanci_stroje_transakce_zamestnanci_id  = " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " ";
            if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                dotaz += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date)BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                        + PraceDoTextField1.getText() + "' ";
            }
            dotaz += "ORDER BY zamestnanci_stroje_transakce_stroje_id ASC ";

            ResultSet strojeTransakce = PripojeniDB.dotazS(dotaz);
            while (strojeTransakce.next()) {
                stroje.add(strojeTransakce.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long timeKonc = 0;
        for (int i = 0; i < stroje.size(); i++) {
            vrCasPom.removeAllElements();
            vsCasPom.removeAllElements();
            int pocetTransakci = 0;
            try {
                String dotaz = "SELECT zamestnanci_stroje_transakce_log_timestamp, "
                        + "current_timestamp "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_zamestnanci_id  = " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                        + "AND zamestnanci_stroje_transakce_stroje_id = " + stroje.get(i) + " ";
                if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                    dotaz += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date)BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' ";
                }
                dotaz += "ORDER BY zamestnanci_stroje_transakce_log_timestamp ASC ";
                ResultSet pruvodkyTransakce = PripojeniDB.dotazS(dotaz);

                while (pruvodkyTransakce.next()) {
                    vsCasPom = new Vector();
                    vsCasPom.add(pruvodkyTransakce.getTimestamp(1));
                    vsCasPom.add(pruvodkyTransakce.getTimestamp(2));
                    vrCasPom.add(vsCasPom);
                    pocetTransakci++;
                    //  System.out.println("cas : " + pruvodkyTransakce.getTimestamp(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pocetTransakci % 2 == 1) {
                vsCasPom = new Vector();
                vsCasPom.add(((java.sql.Timestamp) ((Vector) vrCasPom.get(vrCasPom.size() - 1)).get(1)));
                vsCasPom.add(((java.sql.Timestamp) ((Vector) vrCasPom.get(vrCasPom.size() - 1)).get(1)));
                vrCasPom.add(vsCasPom);
            }
            long time135 = 0;
            long time246 = 0;
            for (int indexTransakce = 0; indexTransakce < vrCasPom.size(); indexTransakce++) {
                if ((indexTransakce % 2 == 0) && (indexTransakce != vrCasPom.size() - 1)) {
                    time135 = ((java.sql.Timestamp) ((Vector) vrCasPom.get(indexTransakce)).get(0)).getTime();
                } else if (indexTransakce % 2 == 1) {
                    time246 += (((java.sql.Timestamp) ((Vector) vrCasPom.get(indexTransakce)).get(0)).getTime() - time135);
                    time135 = 0;
                } else if ((indexTransakce == vrPracePom1.size() - 1) && (indexTransakce % 2 == 0)) {
                    time246 += ((((java.sql.Timestamp) ((Vector) vrCasPom.get(indexTransakce)).get(1)).getTime() - ((java.sql.Timestamp) ((Vector) vrCasPom.get(indexTransakce)).get(0)).getTime()));
                }
            }
            timeKonc += time246;
            //System.out.println(stroje.get(i) + " stroje cas bez presahu : " + time246);
        }
        Timestamp celkovyCasTerminTimestampBezPresahu = new Timestamp(timeKonc - 3600000);
        //System.out.println("cas bez presahu : " + celkovyCasTerminTimestampBezPresahu.toString());

        int dnuBP = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCasTerminTimestampBezPresahu)).intValue() - 1;
        int hodinBP = 0;
        //if(dnuBP > 0) {
        hodinBP = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCasTerminTimestampBezPresahu)).intValue();
        /*}
         else {
         hodinBP = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCasTerminTimestampBezPresahu)).intValue() -1;
         }*/
        //int hodinBP = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCasTerminTimestampBezPresahu)).intValue() - 1;
        String minsecBP = new SimpleDateFormat("mm").format(celkovyCasTerminTimestampBezPresahu);

        if (dnuBP > 0) {
            DobaPraceBezPresahutextField1.setText((24 * dnuBP + hodinBP) + ":" + minsecBP);
            dobaPraceBezPresahu = (dnuBP * 24 + hodinBP) + ":" + minsecBP;
        } else {
            DobaPraceBezPresahutextField1.setText(hodinBP + ":" + minsecBP);
            dobaPraceBezPresahu = hodinBP + ":" + minsecBP;
        }

        int index[][] = new int[vrPrace1.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[vrPrace1.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < vrPrace1.size(); row++) {
            index[row][6] = 1;
            index2[row][5] = 1;
        }

        Color backColor = new Color(188, 247, 188);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelHlavni1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaHlavni.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }

        PocetZaznamuLabel1.setText(vrPrace1.size() + "");
    } //konec getDataTabulkaObjednavka1

    protected void getDataTabulkaDetail1(long pruvodka_id) {
        Vector vsPracePom1 = new Vector();
        Vector vrPracePom1 = new Vector();
        vrPraceDetail1.removeAllElements();
        vsPraceDetail1.removeAllElements();
        tabulkaModelDetail1.oznamZmenu();

        Vector strojeIdPomocne = new Vector();
        try {
            ResultSet strojeTransakce = PripojeniDB.dotazS(
                    "SELECT DISTINCT(zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id  "
                    + "FROM spolecne.zamestnanci_stroje_transakce "
                    + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                    + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                    + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                    + PraceDoTextField1.getText() + "' "
                    + "ORDER BY zamestnanci_stroje_transakce_stroje_id ASC");
            while (strojeTransakce.next()) {
                strojeIdPomocne.add(strojeTransakce.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int z = 0; z < strojeIdPomocne.size(); z++) {
            vrPracePom1.removeAllElements();
            vsPracePom1.removeAllElements();
            try {
                ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                        "SELECT zamestnanci_stroje_transakce_id, "
                        + "zamestnanci_stroje_transakce_druh_id, "
                        + "stroje_id, "
                        + "stroje_nazev, "
                        + "zamestnanci_stroje_transakce_log_timestamp, "
                        + "current_timestamp, "
                        + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YY') AS datum, "
                        + "vykresy_cislo || ' ' || COALESCE(vykresy_revize, '') AS vykres, "
                        + "(pruvodky_pocet_kusu + pruvodky_pocet_kusu_sklad) as pocet "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "CROSS JOIN spolecne.stroje "
                        + "CROSS JOIN spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                        + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                        + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                        + "AND vykresy_id = pruvodky_cislo_vykresu "
                        + "AND stroje_id = " + (Integer) strojeIdPomocne.get(z) + " "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                        + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                        + PraceDoTextField1.getText() + "' "
                        + "ORDER BY zamestnanci_stroje_transakce_log_timestamp ASC");
                while (transakceZamestnanci.next()) {
                    vsPracePom1 = new Vector();
                    vsPracePom1.add(transakceZamestnanci.getLong(1));    // transakce
                    vsPracePom1.add(transakceZamestnanci.getInt(2));     // druh
                    vsPracePom1.add(transakceZamestnanci.getInt(3));     // stroj id
                    vsPracePom1.add(transakceZamestnanci.getString(4));  // stroj nazev                   
                    vsPracePom1.add(transakceZamestnanci.getTimestamp(5)); // cas
                    vsPracePom1.add(transakceZamestnanci.getTimestamp(6)); // nyni cas
                    vsPracePom1.add(transakceZamestnanci.getString(7));  // datum
                    vsPracePom1.add(transakceZamestnanci.getString(8));  // cislo vykresu
                    vsPracePom1.add(transakceZamestnanci.getInt(9));     // pocet kusu
                    vrPracePom1.add(vsPracePom1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            long time135 = 0;
            long time246 = 0;
            String aktivni = "NE";

            for (int indexTransakce = 0; indexTransakce < vrPracePom1.size(); indexTransakce++) {
                //System.out.println("transakceIndex  " + indexTransakce);
                if ((indexTransakce % 2 == 0) && (indexTransakce != vrPracePom1.size() - 1)) {
                    time135 = ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(4)).getTime();
                } else if (indexTransakce % 2 == 1) {
                    time246 += (((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(4)).getTime() - time135);
                    time135 = 0;
                } else if ((indexTransakce == vrPracePom1.size() - 1) && (indexTransakce % 2 == 0)) {
                    time246 += ((((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(5)).getTime() - ((java.sql.Timestamp) ((Vector) vrPracePom1.get(indexTransakce)).get(4)).getTime()));
                    aktivni = "ANO";
                }
            }
            Timestamp celkovyCas = new Timestamp(time246);
            int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
            int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
            String minsec = new SimpleDateFormat("mm").format(celkovyCas);

            Timestamp casNaKus = null;
            if ((Integer) ((Vector) vrPracePom1.get(0)).get(8) > 0) {
                casNaKus = new Timestamp(time246 / (Integer) ((Vector) vrPracePom1.get(0)).get(8));
            } else {
                casNaKus = new Timestamp(0);
            }

            int hodinNaKus = Integer.valueOf(new SimpleDateFormat("HH").format(casNaKus)).intValue() - 1;
            String minsecNaKus = new SimpleDateFormat("mm:ss").format(casNaKus);

            vsPraceDetail1 = new Vector();
            vsPraceDetail1.add((String) ((Vector) vrPracePom1.get(0)).get(3));
            vsPraceDetail1.add((String) ((Vector) vrPracePom1.get(0)).get(7));
            vsPraceDetail1.add((Integer) ((Vector) vrPracePom1.get(0)).get(8));
            if (dnu > 0) {
                vsPraceDetail1.add((24 * dnu + hodin) + ":" + minsec);
            } else {
                vsPraceDetail1.add(hodin + ":" + minsec);
            }
            vsPraceDetail1.add(hodinNaKus + ":" + minsecNaKus);
            vsPraceDetail1.add(aktivni);
            vrPraceDetail1.add(vsPraceDetail1);
        }


        tabulkaModelDetail1.oznamZmenu();

        int index[][] = new int[vrPraceDetail1.size()][tabulkaModelDetail1.getColumnCount()];
        int index2[][] = new int[vrPraceDetail1.size()][tabulkaModelDetail1.getColumnCount()];

        for (int row = 0; row < vrPraceDetail1.size(); row++) {
            index[row][3] = 1;

        }
        Color backColor = new Color(188, 247, 188);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelDetail1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaDetail.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
    }

     protected void nastavVyberTabulkyZanestnanecDetail1() {
        if (tabulkaHlavni.getSelectedRow() >= 0) {
            long id = ((Long) tabulkaModelHlavni1.getValueAt(tabulkaHlavni.getSelectedRow(), 2)).longValue();
            getDataTabulkaDetail1(id);
        }
    }
    
    protected void vycistiFiltrObjednavka1() {
        PraceOdTextField1.setText("");
        PraceDoTextField1.setText("");
        VykresCisloTextField1.setText("");
    }

    private void tiskKarta() {
        int zamestnanec = (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo();
        String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "ZamestnanecKarta1.jrxml";
        TridaZamestnanci1 tz1 = new TridaZamestnanci1();
        tz1.selectData(zamestnanec);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("zamestnanec_id", zamestnanec + "");
        params.put("jmeno", tz1.getJmeno());
        params.put("prijmeni", tz1.getPrijmeni());
        params.put("logo", HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "mikron.jpg");

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1 =
                    JasperFillManager.fillReport(
                    jasperReport, params, new JREmptyDataSource());
            //  JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");


        } catch (JRException ex) {
            ex.printStackTrace();





        }
    }

    private void tiskVypisu() {
        System.out.println("Tisk Vypis");
        String deleteDotaz = "DELETE FROM spolecne.tiskzamestnanci";
        try {
            int a = PripojeniDB.dotazIUD(deleteDotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int zamestnanec = (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo();
        int tiskzamestnanci_id = 1;
        for (int i = 0; i < vrPraceTisk1.size(); i++) {
            String dotaz = "INSERT INTO spolecne.tiskzamestnanci( "
                    + "tiskzamestnanci_id, tiskzamestnanci_datum_od, tiskzamestnanci_datum_do,tiskzamestnanci_pruvodka_id, tiskzamestnanci_vykres_cislo, "
                    + "tiskzamestnanci_pocet_kusu, tiskzamestnanci_doba_prace, tiskzamestnanci_stroj) "
                    + "VALUES (" + tiskzamestnanci_id + ", '" + (String) ((Vector) vrPraceTisk1.get(i)).get(0) + "', '"
                    + (String) ((Vector) vrPraceTisk1.get(i)).get(1) + "', "
                    + (Long) ((Vector) vrPraceTisk1.get(i)).get(2) + ", '"
                    + (String) ((Vector) vrPraceTisk1.get(i)).get(4) + "', " + (Integer) ((Vector) vrPraceTisk1.get(i)).get(5) + ", '"
                    + (String) ((Vector) vrPraceTisk1.get(i)).get(6) + "', '" + (String) ((Vector) vrPraceTisk1.get(i)).get(8) + "')";
            //System.out.println("Ulozeni pruvodky " + dotaz);
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tiskzamestnanci_id++;
        }
        String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "ZamestnanecVypis1.jrxml";

         TridaZamestnanci1 tz1 = new TridaZamestnanci1();
        tz1.selectData(zamestnanec);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("zamestnanec_id", zamestnanec + "");
        params.put("zamestnanec_jmeno", tz1.getJmeno()
                + " " + tz1.getPrijmeni());
        params.put("termin_od", (String) ((Vector) vrPraceTisk1.get(0)).get(0));
        params.put("termin_do", (String) ((Vector) vrPraceTisk1.get(vrPraceTisk1.size() - 1)).get(0));
        params.put("pracePresahy", DobaPracePresahyTextField1.getText().trim());
        params.put("praceBezPresahu", DobaPraceBezPresahutextField1.getText().trim());
        params.put("pracePresahyTime", dobaPracePresah);
        params.put("praceBezPresahuTime", dobaPraceBezPresahu);

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1 =
                    JasperFillManager.fillReport(
                    jasperReport, params, PripojeniDB.con);
            //JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");


        } catch (JRException ex) {
            ex.printStackTrace();
        }


    }

    private void konecPrace() {

        if (tabulkaHlavni.getSelectedRow() >= 0) {

            long transakce_id = -1;
            long pruvodka_id = ((Long) tabulkaModelHlavni1.getValueAt(tabulkaHlavni.getSelectedRow(), 1)).longValue();
            Vector strojePom = new Vector();
            try {
                String dotaz = "SELECT zamestnanci_stroje_transakce_stroje_id FROM "
                        + "(SELECT DISTINCT ON (zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_druh_id "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_zamestnanci_id =  " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                        + "AND zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                        + "ORDER BY zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_id DESC "
                        + ") AS t "
                        + "WHERE  (t.zamestnanci_stroje_transakce_druh_id = 1 OR t.zamestnanci_stroje_transakce_druh_id = 3 OR t.zamestnanci_stroje_transakce_druh_id = 5) ";
                ResultSet id = PripojeniDB.dotazS(dotaz);
                while (id.next()) {
                    strojePom.add(id.getInt(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(zamestnanci_stroje_transakce_id) FROM spolecne.zamestnanci_stroje_transakce");
                while (id.next()) {
                    transakce_id = id.getLong(1) + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < strojePom.size(); i++) {
                String dotaz = "INSERT INTO spolecne.zamestnanci_stroje_transakce( "
                        + "zamestnanci_stroje_transakce_id, zamestnanci_stroje_transakce_zamestnanci_id, "
                        + "zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_druh_id, "
                        + "zamestnanci_stroje_transakce_pruvodky_id) "
                        + "VALUES (" + transakce_id + ", " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo()
                        + ", " + (Integer) strojePom.get(i) + ", "
                        + 4 + ", " + pruvodka_id + ")";
                transakce_id++;
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            strojePom = new Vector();
            int pauzaPocet = 0;
            try {
                String dotaz = "SELECT zamestnanci_stroje_transakce_stroje_id FROM "
                        + "(SELECT DISTINCT ON (zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_druh_id "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_zamestnanci_id =  " +(Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo() + " "
                        + "AND zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                        + "ORDER BY zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_id DESC "
                        + ") AS t "
                        + "WHERE  (t.zamestnanci_stroje_transakce_druh_id = 2) ";
                ResultSet id = PripojeniDB.dotazS(dotaz);

                while (id.next()) {
                    strojePom.add(id.getInt(1));
                    pauzaPocet++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pauzaPocet > 0) {
                try {
                    ResultSet id = PripojeniDB.dotazS("SELECT MAX(zamestnanci_stroje_transakce_id) FROM spolecne.zamestnanci_stroje_transakce");
                    while (id.next()) {
                        transakce_id = id.getLong(1) + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < strojePom.size(); i++) {
                    String dotaz = "INSERT INTO spolecne.zamestnanci_stroje_transakce( "
                            + "zamestnanci_stroje_transakce_id, zamestnanci_stroje_transakce_zamestnanci_id, "
                            + "zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_druh_id, "
                            + "zamestnanci_stroje_transakce_pruvodky_id) "
                            + "VALUES (" + transakce_id + ", " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo()
                            + ", " + (Integer) strojePom.get(i) + ", "
                            + 3 + ", " + pruvodka_id + ")";
                    transakce_id++;
                    try {
                        int a = PripojeniDB.dotazIUD(dotaz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dotaz = "INSERT INTO spolecne.zamestnanci_stroje_transakce( "
                            + "zamestnanci_stroje_transakce_id, zamestnanci_stroje_transakce_zamestnanci_id, "
                            + "zamestnanci_stroje_transakce_stroje_id, zamestnanci_stroje_transakce_druh_id, "
                            + "zamestnanci_stroje_transakce_pruvodky_id) "
                            + "VALUES (" + transakce_id + ", " + (Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo()
                            + ", " + (Integer) strojePom.get(i) + ", "
                            + 4 + ", " + pruvodka_id + ")";
                    transakce_id++;
                    try {
                        int a = PripojeniDB.dotazIUD(dotaz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    protected class TabulkaModelHlavni extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum od",
            "Datum do",
            "Průvodka",
            "Název",
            "Výkres",
            "Počet kusů",
            "Doba práce",
            "Aktivní"};

        public void pridejSadu() {


            fireTableRowsInserted(0, vrPrace1.size());
            //  updateZaznamyObjednavka1();
            if (vrPrace1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            vrPrace1.remove(tabulkaHlavni.getSelectedRow());
            fireTableRowsDeleted(tabulkaHlavni.getSelectedRow(), tabulkaHlavni.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrObjednavka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(tabulkaHlavni.getSelectedRow(), tabulkaHlavni.getSelectedRow());
        }//konec oznamUpdateRadky

        public void oznamUpdateRadkyPozice(int pozice) {
            fireTableRowsUpdated(pozice, pozice);
        }//konec oznamUpdateRadky

        @Override
        public int getColumnCount() {
            return columnNames.length;
//        return vs.size();
        }//konec getRowCount*/

        @Override
        public int getRowCount() {
            return vrPrace1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        /* @Override
         public Class getColumnClass(int col) {
         if (col == 1) {
         return Boolean.class;
         }
         if (col == 2 || col == 3) {
         return Integer.class;
         }
         return getValueAt(0, col).getClass();//String.class;
         }*/
        public boolean nastavHodnotuNaVybrane(TridaObjednavka1 bt) {

            return nastavHodnotuNaPozici(bt, tabulkaHlavni.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaObjednavka1 nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            try {
                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int col) {
            // System.out.println("getValueAt PruvFram");
            try {
                //  System.out.println("class " + (((Vector) vrObjednavka1.elementAt(row)).elementAt(col)).getClass());
                return (((Vector) vrPrace1.elementAt(row)).elementAt(col));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            // System.out.println("setValueAt PruvFram");
        }//konec setValueAt
    }//konec modelu tabulky

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Stroj",
            "Výkres",
            "Počet kusů",
            "Doba práce",
            "Čas na kus",
            "Aktivní"};

        public void pridejSadu() {


            fireTableRowsInserted(0, vrPraceDetail1.size());
            //  updateZaznamyObjednavka1();
            if (vrPraceDetail1.size() > 0) {
                tabulkaDetail.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            vrPraceDetail1.remove(tabulkaDetail.getSelectedRow());
            fireTableRowsDeleted(tabulkaDetail.getSelectedRow(), tabulkaDetail.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrObjednavka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(tabulkaDetail.getSelectedRow(), tabulkaDetail.getSelectedRow());
        }//konec oznamUpdateRadky

        public void oznamUpdateRadkyPozice(int pozice) {
            fireTableRowsUpdated(pozice, pozice);
        }//konec oznamUpdateRadky

        @Override
        public int getColumnCount() {
            return columnNames.length;
//        return vs.size();
        }//konec getRowCount*/

        @Override
        public int getRowCount() {
            return vrPraceDetail1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        /* @Override
         public Class getColumnClass(int col) {
         if (col == 1) {
         return Boolean.class;
         }
         if (col == 2 || col == 3) {
         return Integer.class;
         }
         return getValueAt(0, col).getClass();//String.class;
         }*/
        public boolean nastavHodnotuNaVybrane(TridaObjednavka1 bt) {

            return nastavHodnotuNaPozici(bt, tabulkaDetail.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaObjednavka1 nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            try {

                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {

            return false;

        }

        @Override
        public Object getValueAt(int row, int col) {
            // System.out.println("getValueAt PruvFram");
            try {
                //  System.out.println("class " + (((Vector) vrObjednavka1.elementAt(row)).elementAt(col)).getClass());
                return (((Vector) vrPraceDetail1.elementAt(row)).elementAt(col));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            // System.out.println("setValueAt PruvFram");
        }//konec setValueAt
    }//konec modelu tabulky

    class ALUdalosti implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //  System.out.println("action : " + e.getActionCommand());
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("FiltrujPruvodky1") || e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                if (e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                    vycistiFiltrObjednavka1();
                }
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaHlavni();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }
            if (e.getActionCommand().equals("Hledat")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaHlavni();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }
            
             if (e.getActionCommand().equals("TiskKarta")) {

                tiskKarta();
            }
            if (e.getActionCommand().equals("KonecPrace")) {

                konecPrace();
            }
            if (e.getActionCommand().equals("Vyhledat")) {
                getDataTabulkaHlavni();
                //initZamestnanec();
            }
            if (e.getActionCommand().equals("TiskVypisu")) {
                tiskVypisu();
            }
            if (e.getActionCommand().equals("NovyZamestnanec")) {
                ZamestnanecFrame1 zamestnanec = new ZamestnanecFrame1(false);
            }
            if (e.getActionCommand().equals("UpravitUdaje")) {
                ZamestnanecFrame1 zamestnanec = new ZamestnanecFrame1((Integer) ((DvojiceCisloRetez) roletkaModelZamestnanci.getSelectedItem()).cislo(), true);
            }
              if (e.getActionCommand().equals("KonecPrace")) {

                konecPrace();
            }

        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {
            

            if (tme.getSource() == tableModelDetail1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {           
            if (e.getSource() == lsmHlavni1) {               
                if (e.getValueIsAdjusting() == false) {                   
                    if (vrPrace1.size() > 0) {   
                        nastavVyberTabulkyZanestnanecDetail1();
                    }
                }
            }//konec if (getSource ...)

        }//konec valueChanged
    } //konec LSLUdalosti

    class FLUdalosti implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            System.out.println("vybrana radkasss focus");
            ((JTextField) e.getSource()).selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
        }
    }//konec FLUdalosti

    class WinUdalosti implements WindowListener {

        public void windowClosing(WindowEvent e) {
            //displayMessage("WindowListener method called: windowClosing.");

            //A pause so user can see the message before
            //the window actually closes.
            ActionListener task = new ActionListener() {
                boolean alreadyDisposed = false;

                public void actionPerformed(ActionEvent e) {
                    if (!alreadyDisposed) {
                        alreadyDisposed = true;
                        // frame.dispose();
                    } else { //make sure the program exits
                        System.exit(0);
                    }
                }
            };
        }

        public void windowClosed(WindowEvent e) {
            zrusPosluchaceUdalostiTabulky();
            getDataTabulkaHlavni();
            tabulkaModelHlavni1.pridejSadu();
            nastavPosluchaceUdalostiTabulky();
            ListSelectionModel selectionModel =
                    tabulkaHlavni.getSelectionModel();
            selectionModel.setSelectionInterval(indexOznaceno, indexOznaceno);
            tabulkaHlavni.scrollRectToVisible(tabulkaHlavni.getCellRect(indexOznaceno, 0, false));


        }

        public void windowOpened(WindowEvent e) {
            //displayMessage("WindowListener method called: windowOpened.");
        }

        public void windowIconified(WindowEvent e) {
            // displayMessage("WindowListener method called: windowIconified.");
        }

        public void windowDeiconified(WindowEvent e) {
            // displayMessage("WindowListener method called: windowDeiconified.");
        }

        public void windowActivated(WindowEvent e) {
            // displayMessage("WindowListener method called: windowActivated.");
        }

        public void windowDeactivated(WindowEvent e) {
            // displayMessage("WindowListener method called: windowDeactivated.");
        }

        public void windowGainedFocus(WindowEvent e) {
            // displayMessage("WindowFocusListener method called: windowGainedFocus.");
        }

        public void windowLostFocus(WindowEvent e) {
            // displayMessage("WindowFocusListener method called: windowLostFocus.");
        }

        public void windowStateChanged(WindowEvent e) {
            // displayMessage("WindowStateListener method called: windowStateChanged.");
        }

        void displayMessage(String msg) {
            System.out.println(msg);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        JPFiltrTop = new javax.swing.JPanel();
        jBVycistitFiltr = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        PocetZaznamuLabel1 = new javax.swing.JLabel();
        JCheckBoxZobrazovatVseObjednavka1 = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        PraceDoTextField1 = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        ZamestnanecComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        PraceOdTextField1 = new javax.swing.JTextField();
        VykresCisloTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        NovyZamestnanecButton1 = new javax.swing.JButton();
        TiskKartaButton = new javax.swing.JButton();
        UpravitUdajeButton1 = new javax.swing.JButton();
        TiskVypisuButton1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        KonecPraceButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPHlavni = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPDetail = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        DobaPracePresahyTextField1 = new javax.swing.JTextField();
        DobaPraceBezPresahutextField1 = new javax.swing.JTextField();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        jBVycistitFiltr.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jBVycistitFiltr, gridBagConstraints);

        jLabel2.setText("Počet záznamů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel2, gridBagConstraints);

        PocetZaznamuLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(PocetZaznamuLabel1, gridBagConstraints);

        JCheckBoxZobrazovatVseObjednavka1.setText("Zobrazovat vše");
        JPFiltrTop.add(JCheckBoxZobrazovatVseObjednavka1, new java.awt.GridBagConstraints());

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel16.setText("do :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(PraceDoTextField1, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(VyhledatButton1, gridBagConstraints);

        jLabel20.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jLabel1.setText("Zaměstnanec :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel1, gridBagConstraints);

        ZamestnanecComboBox1.setMaximumRowCount(12);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(ZamestnanecComboBox1, gridBagConstraints);

        jLabel3.setText("Práce od :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        jPFiltry.add(PraceOdTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 150;
        jPFiltry.add(VykresCisloTextField1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        NovyZamestnanecButton1.setText("Nový zaměstnanec");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(NovyZamestnanecButton1, gridBagConstraints);

        TiskKartaButton.setText("Tisk karty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(TiskKartaButton, gridBagConstraints);

        UpravitUdajeButton1.setText("Upravit údaje");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(UpravitUdajeButton1, gridBagConstraints);

        TiskVypisuButton1.setText("Tisk výpisu");
        TiskVypisuButton1.setPreferredSize(new java.awt.Dimension(109, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(TiskVypisuButton1, gridBagConstraints);

        jButton1.setText("jButton1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jButton1, gridBagConstraints);

        KonecPraceButton1.setText("Ukončit práci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(KonecPraceButton1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        tabulkaHlavni.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jSPHlavni.setViewportView(tabulkaHlavni);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 20);
        jPanel2.add(jSPHlavni, gridBagConstraints);

        tabulkaDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jSPDetail.setViewportView(tabulkaDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 10);
        jPanel2.add(jSPDetail, gridBagConstraints);

        jLabel4.setText("Celková doba práce v  termínu (přesahy):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 10);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Celková doba práce v termínu (bez přesahů) : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 60, 10);
        jPanel2.add(jLabel5, gridBagConstraints);

        DobaPracePresahyTextField1.setEditable(false);
        DobaPracePresahyTextField1.setBackground(new java.awt.Color(188, 247, 188));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(DobaPracePresahyTextField1, gridBagConstraints);

        DobaPraceBezPresahutextField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 60, 10);
        jPanel2.add(DobaPraceBezPresahutextField1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1315, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DobaPraceBezPresahutextField1;
    private javax.swing.JTextField DobaPracePresahyTextField1;
    private javax.swing.JCheckBox JCheckBoxZobrazovatVseObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JButton KonecPraceButton1;
    private javax.swing.JButton NovyZamestnanecButton1;
    private javax.swing.JLabel PocetZaznamuLabel1;
    private javax.swing.JTextField PraceDoTextField1;
    private javax.swing.JTextField PraceOdTextField1;
    private javax.swing.JButton TiskKartaButton;
    private javax.swing.JButton TiskVypisuButton1;
    private javax.swing.JButton UpravitUdajeButton1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JTextField VykresCisloTextField1;
    private javax.swing.JComboBox ZamestnanecComboBox1;
    private javax.swing.JButton jBVycistitFiltr;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPDetail;
    private javax.swing.JScrollPane jSPHlavni;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
