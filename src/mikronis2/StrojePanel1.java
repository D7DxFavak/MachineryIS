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
import java.util.Vector;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author Favak
 */
public class StrojePanel1 extends javax.swing.JPanel {

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
    protected Vector vrPraceDetail1;
    protected Vector vsPraceDetail1;
    protected Vector vsZakaznikLiefer1;
    protected Vector v;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelStroje;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;
    protected int indexOznaceno;
    protected long celkovyCasTermin;
    protected String dobaPracePresah = "";

    /**
     * Creates new form ObjednavkyPanel
     */
    public StrojePanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuStroje1();
        nastavTabulkuStrojeDetail1();

        nastavPosluchaceUdalostiOvladace();
        this.setVisible(true);
    }

    protected void nastavParametry() {
        vrPrace1 = new Vector();
        vsPrace1 = new Vector();

        vrPraceDetail1 = new Vector();
        vsPraceDetail1 = new Vector();

        v = new Vector();

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

    protected void initRoletky() {
        roletkaModelStroje = new RoletkaUniverzalRozsirenaModel1(
                "SELECT stroje_id, stroje_nazev FROM spolecne.stroje "
                + "ORDER BY stroje_druh_stroje, stroje_nazev", "Neurčen", null,
                "V databázi nejsou zadány stroje", 0); // bylo ...vs_id
        StrojComboBox.setModel(roletkaModelStroje);

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
        StrojComboBox.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        getDetailStrojButton.addActionListener(alUdalosti);

        UpravitUdajeButton1.addActionListener(alUdalosti);
        TiskVypisuButton1.addActionListener(alUdalosti);

        jBVycistitFiltr.setActionCommand("VycistiFiltr");
        VyhledatButton1.setActionCommand("Hledat");
        getDetailStrojButton.setActionCommand("DetailStroje");
        UpravitUdajeButton1.setActionCommand("UpravitUdaje");
        StrojComboBox.setActionCommand("Hledat");
        TiskVypisuButton1.setActionCommand("TiskVypisu");

    }

    protected void nastavTridyObsluhyUdalosti() {
        tmlUdalosti = new TMLUdalosti();
        lslUdalosti = new LSLUdalosti();
        alUdalosti = new ALUdalosti();
        flUdalosti = new FLUdalosti();
        winUdalosti = new WinUdalosti();
    }

    protected ArrayList getSirkaSloupcu() {
        ArrayList out = new ArrayList();
        for (int i = 0; i < tabulkaDetail.getColumnCount(); i++) {
            out.add(tabulkaDetail.getColumnModel().getColumn(i).getPreferredWidth());
        }
        return out;
    }

    protected void setSirkaSloupcu(ArrayList in) {
        for (int i = 0; i < tabulkaDetail.getColumnCount(); i++) {
            tabulkaDetail.getColumnModel().getColumn(i).setPreferredWidth((Integer) in.get(i));
        }
    }

    private void tiskVypisu() {
        String deleteDotaz = "DELETE FROM spolecne.tiskstroje";
        try {
            int a = PripojeniDB.dotazIUD(deleteDotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strojJmeno = ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).toString();
        int tiskstroje_id = 1;
        for (int i = 0; i < vrPrace1.size(); i++) {
            String dotaz = "INSERT INTO spolecne.tiskstroje( "
                    + "tiskstroje_id, tiskstroje_datum_od, tiskstroje_datum_do, tiskstroje_doba_prace, tiskstroje_vykres_cislo, "
                    + "tiskstroje_zamestnanec, tiskstroje_kusy) "
                    + "VALUES (" + tiskstroje_id + ", '" + (String) ((Vector) vrPrace1.get(i)).get(0) + "', '"
                    + (String) ((Vector) vrPrace1.get(i)).get(1) + "', '"
                    + (String) ((Vector) vrPrace1.get(i)).get(2) + "', '"
                    + (String) ((Vector) vrPrace1.get(i)).get(4) + "', '" + (String) ((Vector) vrPrace1.get(i)).get(6) + "', "
                    + (String) ((Vector) vrPrace1.get(i)).get(5) + ")";
            // System.out.println("Ulozeni stroj historie " + dotaz);
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tiskstroje_id++;
        }
        String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "strojVypis1.jrxml";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("stroj_jmeno", strojJmeno);
        params.put("termin_od", (String) ((Vector) vrPrace1.get(0)).get(0));
        params.put("termin_do", (String) ((Vector) vrPrace1.get(vrPrace1.size() - 1)).get(1));
        params.put("pracePresahyTime", dobaPracePresah);

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            //JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");

        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void getDetailInfoStroj() {
        /*  String IPaddress = (String) ((Vector) vrStroje1.get(StrojComboBox.getSelectedIndex())).get(2);
         if ((IPaddress.contains("192.168"))) {
         int index = 0;
         String datumOd = (String) ((Vector) vrPrace1.get(tabulkaHlavni.getSelectedRow())).get(0);
         String datumDo = (String) ((Vector) vrPrace1.get(tabulkaHlavni.getSelectedRow())).get(1);
         //TODO :DetailInfoStrojFrame1 detailInfo = new DetailInfoStrojFrame1(datumOd, datumDo, IPaddress);

         }*/
    }

    protected void nastavTabulkuStroje1() {

        /* TableColumn column = null;
        column = tabulkaHlavni.getColumnModel().getColumn(0);
        column.setPreferredWidth(130);

        column = tabulkaHlavni.getColumnModel().getColumn(1);
        column.setPreferredWidth(130);

        column = tabulkaHlavni.getColumnModel().getColumn(2);
        column.setPreferredWidth(80);

        column = tabulkaHlavni.getColumnModel().getColumn(3);
        column.setPreferredWidth(130);

        column = tabulkaHlavni.getColumnModel().getColumn(4);
        column.setPreferredWidth(120);

        column = tabulkaHlavni.getColumnModel().getColumn(5);
        column.setPreferredWidth(50);

        column = tabulkaHlavni.getColumnModel().getColumn(6);
        column.setPreferredWidth(130);

        column = tabulkaHlavni.getColumnModel().getColumn(7);
        column.setPreferredWidth(50);*/
        refreshTabulkaHlavni();

    }// konec nastavTabulkuBT1

    private void refreshTabulkaHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaStroje1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
    }

    protected void nastavTabulkuStrojeDetail1() {

        /* TableColumn column = null;
        column = tabulkaDetail.getColumnModel().getColumn(0);
        column.setPreferredWidth(105);

        column = tabulkaDetail.getColumnModel().getColumn(1);
        column.setPreferredWidth(105);

        column = tabulkaDetail.getColumnModel().getColumn(2);
        column.setPreferredWidth(105);*/
        zrusPosluchaceUdalostiTabulky();
        // TODO : vymyslet data
        /*getDataTabulkaStroje1();
         tabulkaModelZamestnanec1.pridejSadu();
         nastavPosluchaceUdalostiTabulky();*/

    }// konec nastavTabulkuBT1

    protected void nastavVyberTabulkyStrojeDetail1() {
        System.out.println("Tabulka sel Row : " + tabulkaHlavni.getSelectedRow());
        if (tabulkaHlavni.getSelectedRow() >= 0) {
            //long id = ((Long) ((Vector) vrPrace1.get(jTableStroje1.getSelectedRow())).get(7)).longValue();
            getDataTabulkaStrojeDetail1(((Long) ((Vector) vrPrace1.get(tabulkaHlavni.getSelectedRow())).get(8)).longValue(),
                    ((Integer) ((Vector) vrPrace1.get(tabulkaHlavni.getSelectedRow())).get(9)).intValue());
        }
    }

    protected void getDataTabulkaStroje1() {
        celkovyCasTermin = 0;
        zrusPosluchaceUdalostiTabulky();
        System.out.println("get data tabulka stroje");
        Vector vsPracePom1 = new Vector();
        Vector vrPracePom1 = new Vector();
        vrPrace1.removeAllElements();
        vsPrace1.removeAllElements();
        tabulkaModelHlavni1.oznamZmenu();
        boolean osetritDatumOd = TextFunkce1.osetriDatum(PraceOdTextField1.getText());
        boolean osetritDatumDo = TextFunkce1.osetriDatum(PraceDoTextField1.getText());

        Vector pruvodkyIdPomocne = new Vector();
        try {
            String dotazPruvodka = "";
            if (VykresCisloTextField1.getText().trim().length() > 0) {
                dotazPruvodka = "SELECT * FROM (SELECT zamestnanci_stroje_transakce_pruvodky_id, MIN(zamestnanci_stroje_transakce_log_timestamp) AS minimumTimestamp "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "CROSS JOIN spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id > 50000002";
                if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                    dotazPruvodka += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' ";
                }
                dotazPruvodka += "AND vykresy_cislo LIKE '%" + VykresCisloTextField1.getText().trim() + "%' "
                        + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                        + "AND vykresy_id = pruvodky_cislo_vykresu ";

                dotazPruvodka += "GROUP BY zamestnanci_stroje_transakce_pruvodky_id) s "
                        + "ORDER BY s.minimumTimestamp ASC ";
            } else {
                dotazPruvodka = "SELECT * FROM (SELECT zamestnanci_stroje_transakce_pruvodky_id, MIN(zamestnanci_stroje_transakce_log_timestamp) AS minimumTimestamp "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id > 50000002 ";
                if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                    dotazPruvodka += "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' ";
                }
                dotazPruvodka += "GROUP BY zamestnanci_stroje_transakce_pruvodky_id) s "
                        + "ORDER BY s.minimumTimestamp ASC ";
            }
            // System.out.println("dotaz stroje : " + dotazPruvodka);
            ResultSet pruvodkyTransakce = PripojeniDB.dotazS(dotazPruvodka);
            while (pruvodkyTransakce.next()) {
                pruvodkyIdPomocne.add(pruvodkyTransakce.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pruvodkyIdPomocne.size(); i++) {
            if (((Long) pruvodkyIdPomocne.get(i)).longValue() != 150) {
                Vector zamestnanciIdPomocne = new Vector();
                try {
                    ResultSet strojeTransakce = PripojeniDB.dotazS(
                            "SELECT DISTINCT(zamestnanci_stroje_transakce_zamestnanci_id) zamestnanci_stroje_transakce_zamestnanci_id  "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Long) pruvodkyIdPomocne.get(i) + " "
                            + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                            + "ORDER BY zamestnanci_stroje_transakce_zamestnanci_id ASC");
                    while (strojeTransakce.next()) {
                        zamestnanciIdPomocne.add(strojeTransakce.getInt(1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long time135 = 0;
                long time246 = 0;
                String aktivni = "NE";
                //System.out.println("pomidPomSize " + pruvodkyIdPomocne.size());
                // System.out.println("zamidPomSize " + zamestnanciIdPomocne.size());
                for (int z = 0; z < zamestnanciIdPomocne.size(); z++) {
                    time135 = 0;
                    time246 = 0;
                    vrPracePom1.removeAllElements();
                    vsPracePom1.removeAllElements();
                    try {
                        ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                                "SELECT zamestnanci_stroje_transakce_id, "
                                + "zamestnanci_stroje_transakce_druh_id, "
                                + "zamestnanci_id, "
                                + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                                + "zamestnanci_stroje_transakce_log_timestamp, "
                                + "current_timestamp, "
                                + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YYYY HH24:MI') AS datum, "
                                + "pruvodky_id, "
                                + "pruvodky_nazev, "
                                + "vykresy_cislo, "
                                + "(pruvodky_pocet_kusu + pruvodky_pocet_kusu_sklad) as pocet "
                                + "FROM spolecne.zamestnanci_stroje_transakce "
                                + "CROSS JOIN spolecne.zamestnanci "
                                + "CROSS JOIN spolecne.pruvodky "
                                + "CROSS JOIN spolecne.vykresy "
                                + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Long) pruvodkyIdPomocne.get(i) + " "
                                + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                                + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                                + "AND vykresy_id = pruvodky_cislo_vykresu "
                                + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + (Integer) zamestnanciIdPomocne.get(z) + " "
                                + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                                + "ORDER BY  zamestnanci_stroje_transakce_log_timestamp ASC");
                        while (transakceZamestnanci.next()) {
                            vsPracePom1 = new Vector();
                            vsPracePom1.add(transakceZamestnanci.getLong(1));    // transakce
                            vsPracePom1.add(transakceZamestnanci.getInt(2));     // druh
                            vsPracePom1.add(transakceZamestnanci.getInt(3));     // zamestnanec id
                            vsPracePom1.add(transakceZamestnanci.getString(4));  // zamestnanec jmeno
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(5)); // cas
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(6)); // nyni cas
                            vsPracePom1.add(transakceZamestnanci.getString(7));  // datum
                            vsPracePom1.add(transakceZamestnanci.getLong(8));    // pruvodky id
                            vsPracePom1.add(transakceZamestnanci.getString(9));  // pruvodky_nazev
                            vsPracePom1.add(transakceZamestnanci.getString(10));  // cislo vykresu
                            vsPracePom1.add(transakceZamestnanci.getString(11));  // pocetkusu

                            vrPracePom1.add(vsPracePom1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int indexTransakce = 0; indexTransakce < vrPracePom1.size(); indexTransakce++) {
                        // System.out.println("transakceIndex  " + indexTransakce);
                        if (indexTransakce == 0) {
                            // System.out.println("transakce 0  " + (String) ((Vector) vrPracePom1.get(indexTransakce)).get(3));
                            // zamestnanecJmeno = (String) ((Vector) vrPracePom1.get(indexTransakce)).get(5);
                            // strojNazev = (String) ((Vector) vrPracePom1.get(indexTransakce)).get(3);
                        }
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
                    celkovyCasTermin += time246;

                    int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                    int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                    String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                    vsPrace1 = new Vector();
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(6)); // zacatek
                    if (aktivni.equals("ANO")) {
                        vsPrace1.add(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(((java.sql.Timestamp) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(5)).getTime()));
                    } else {
                        vsPrace1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(6)); //konec
                    }
                    if (dnu > 0) {
                        vsPrace1.add((24 * dnu + hodin) + ":" + minsec);
                    } else {
                        vsPrace1.add(hodin + ":" + minsec);
                    }
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8)); // pruvodka
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(9)); // vykres
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // kusy
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(3)); // zamestnanec
                    vsPrace1.add(aktivni);
                    vsPrace1.add(((Long) ((Vector) vrPracePom1.get(0)).get(7)).longValue());
                    vsPrace1.add(((Integer) ((Vector) vrPracePom1.get(0)).get(2)).intValue());
                    vrPrace1.add(vsPrace1);
                }
            } else if (((Long) pruvodkyIdPomocne.get(i)).longValue() == 150) {

                Vector zamestnanciIdPomocne = new Vector();
                try {
                    ResultSet strojeTransakce = PripojeniDB.dotazS(
                            "SELECT DISTINCT(zamestnanci_stroje_transakce_zamestnanci_id) zamestnanci_stroje_transakce_zamestnanci_id  "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Long) pruvodkyIdPomocne.get(i) + " "
                            + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                            + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                            + PraceDoTextField1.getText() + "' "
                            + "ORDER BY zamestnanci_stroje_transakce_zamestnanci_id ASC");
                    while (strojeTransakce.next()) {
                        zamestnanciIdPomocne.add(strojeTransakce.getInt(1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long time135 = 0;
                long time246 = 0;
                String aktivni = "NE";
                //System.out.println("pomidPomSize " + pruvodkyIdPomocne.size());
                // System.out.println("zamidPomSize " + zamestnanciIdPomocne.size());
                for (int z = 0; z < zamestnanciIdPomocne.size(); z++) {
                    time135 = 0;
                    time246 = 0;
                    vrPracePom1.removeAllElements();
                    vsPracePom1.removeAllElements();
                    try {
                        ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                                "SELECT zamestnanci_stroje_transakce_id, "
                                + "zamestnanci_stroje_transakce_druh_id, "
                                + "zamestnanci_id, "
                                + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                                + "zamestnanci_stroje_transakce_log_timestamp, "
                                + "current_timestamp, "
                                + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YYYY HH24:MI') AS datum, "
                                + "pruvodky_id, "
                                + "pruvodky_nazev, "
                                + "vykresy_cislo, "
                                + "(pruvodky_pocet_kusu + pruvodky_pocet_kusu_sklad) as pocet "
                                + "FROM spolecne.zamestnanci_stroje_transakce "
                                + "CROSS JOIN spolecne.zamestnanci "
                                + "CROSS JOIN spolecne.pruvodky "
                                + "CROSS JOIN spolecne.vykresy "
                                + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + (Long) pruvodkyIdPomocne.get(i) + " "
                                + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                                + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                                + "AND vykresy_id = pruvodky_cislo_vykresu "
                                + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + (Integer) zamestnanciIdPomocne.get(z) + " "
                                + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                                + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                                + PraceDoTextField1.getText() + "' "
                                + "ORDER BY  zamestnanci_stroje_transakce_log_timestamp ASC");
                        while (transakceZamestnanci.next()) {
                            vsPracePom1 = new Vector();
                            vsPracePom1.add(transakceZamestnanci.getLong(1));    // transakce
                            vsPracePom1.add(transakceZamestnanci.getInt(2));     // druh
                            vsPracePom1.add(transakceZamestnanci.getInt(3));     // zamestnanec id
                            vsPracePom1.add(transakceZamestnanci.getString(4));  // zamestnanec jmeno
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(5)); // cas
                            vsPracePom1.add(transakceZamestnanci.getTimestamp(6)); // nyni cas
                            vsPracePom1.add(transakceZamestnanci.getString(7));  // datum
                            vsPracePom1.add(transakceZamestnanci.getLong(8));    // pruvodky id
                            vsPracePom1.add(transakceZamestnanci.getString(9));  // pruvodky_nazev
                            vsPracePom1.add(transakceZamestnanci.getString(10));  // cislo vykresu
                            vsPracePom1.add(transakceZamestnanci.getString(11));  // pocetkusu

                            vrPracePom1.add(vsPracePom1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int indexTransakce = 0; indexTransakce < vrPracePom1.size(); indexTransakce++) {
                        // System.out.println("transakceIndex  " + indexTransakce);
                        if (indexTransakce == 0) {
                            // System.out.println("transakce 0  " + (String) ((Vector) vrPracePom1.get(indexTransakce)).get(3));
                            // zamestnanecJmeno = (String) ((Vector) vrPracePom1.get(indexTransakce)).get(5);
                            // strojNazev = (String) ((Vector) vrPracePom1.get(indexTransakce)).get(3);
                        }
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
                    celkovyCasTermin += time246;

                    int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                    int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                    String minsec = new SimpleDateFormat("mm").format(celkovyCas);

                    vsPrace1 = new Vector();
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(6)); // zacatek
                    if (aktivni.equals("ANO")) {
                        vsPrace1.add(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(((java.sql.Timestamp) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(5)).getTime()));
                    } else {
                        vsPrace1.add((String) ((Vector) vrPracePom1.get(vrPracePom1.size() - 1)).get(6)); //konec
                    }
                    if (dnu > 0) {
                        vsPrace1.add((24 * dnu + hodin) + ":" + minsec);
                    } else {
                        vsPrace1.add(hodin + ":" + minsec);
                    }
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(8)); // pruvodka
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(9)); // vykres
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(10)); // kusy
                    vsPrace1.add((String) ((Vector) vrPracePom1.get(0)).get(3)); // zamestnanec
                    vsPrace1.add(aktivni);
                    vsPrace1.add(((Long) ((Vector) vrPracePom1.get(0)).get(7)).longValue());
                    vsPrace1.add(((Integer) ((Vector) vrPracePom1.get(0)).get(2)).intValue());
                    vrPrace1.add(vsPrace1);
                }
            }
        }

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
            dobaPracePresah = (dnu * 24 + hodin) + ":" + minsec;
        } else {
            dobaPracePresah = hodin + ":" + minsec;
        }

        tabulkaModelHlavni1.oznamZmenu();
        int index[][] = new int[vrPrace1.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[vrPrace1.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < vrPrace1.size(); row++) {
            index[row][2] = 1;
        }
        Color backColor = new Color(188, 247, 188);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelHlavni1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaHlavni.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
        PocetZaznamuLabel1.setText(vrPrace1.size() + "");
        DobaPraceTextField1.setText(dobaPracePresah);
        nastavPosluchaceUdalostiTabulky();
    } //konec getDataTabulkaObjednavka1

    protected void getDataTabulkaStrojeDetail1(long pruvodka_id, int zamestnanec_id) {
        Vector vsPracePom1 = new Vector();
        Vector vrPracePom1 = new Vector();
        vrPraceDetail1.removeAllElements();
        vsPraceDetail1.removeAllElements();
        tabulkaModelDetail1.oznamZmenu();

        //for (int z = 0; z < zamestnanciIdPomocne.size(); z++) {
        vrPracePom1.removeAllElements();
        vsPracePom1.removeAllElements();
        if (pruvodka_id != 150) {
            try {
                ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                        "SELECT "
                        + "druhy_transakce_popis, "
                        + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YY HH24:MI ') AS datum, "
                        + "vykresy_cislo, "
                        + "zamestnanci_stroje_transakce_id "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "CROSS JOIN spolecne.stroje "
                        + "CROSS JOIN spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.druhy_transakce "
                        + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                        + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                        + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                        + "AND vykresy_id = pruvodky_cislo_vykresu "
                        + "AND druhy_transakce_id = zamestnanci_stroje_transakce_druh_id "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + zamestnanec_id /*(Integer) zamestnanciIdPomocne.get(z)*/ + " "
                        + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                        + "ORDER BY zamestnanci_stroje_transakce_log_timestamp ASC");
                while (transakceZamestnanci.next()) {
                    vsPracePom1 = new Vector();
                    vsPracePom1.add(transakceZamestnanci.getString(3));  // cislo vykresu
                    vsPracePom1.add(transakceZamestnanci.getString(1));  // nazev druh
                    vsPracePom1.add(transakceZamestnanci.getString(2));  // datum
                    //  vsPracePom1.add(transakceZamestnanci.getInt(4));  // id
                    vrPracePom1.add(vsPracePom1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                        "SELECT "
                        + "druhy_transakce_popis, "
                        + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'DD.MM.YY HH24:MI ') AS datum, "
                        + "vykresy_cislo, "
                        + "zamestnanci_stroje_transakce_id "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "CROSS JOIN spolecne.stroje "
                        + "CROSS JOIN spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.druhy_transakce "
                        + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + pruvodka_id + " "
                        + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                        + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                        + "AND vykresy_id = pruvodky_cislo_vykresu "
                        + "AND druhy_transakce_id = zamestnanci_stroje_transakce_druh_id "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + zamestnanec_id /*(Integer) zamestnanciIdPomocne.get(z)*/ + " "
                        + "AND zamestnanci_stroje_transakce_stroje_id = " + ((DvojiceCisloRetez) roletkaModelStroje.getSelectedItem()).cislo() + " "
                        + "AND cast(zamestnanci_stroje_transakce_log_timestamp AS date) BETWEEN '" + PraceOdTextField1.getText() + "' AND '"
                        + PraceDoTextField1.getText() + "' "
                        + "ORDER BY zamestnanci_stroje_transakce_log_timestamp ASC");
                while (transakceZamestnanci.next()) {
                    vsPracePom1 = new Vector();
                    vsPracePom1.add(transakceZamestnanci.getString(3));  // cislo vykresu
                    vsPracePom1.add(transakceZamestnanci.getString(1));  // nazev druh
                    vsPracePom1.add(transakceZamestnanci.getString(2));  // datum
                    //  vsPracePom1.add(transakceZamestnanci.getInt(4));  // id
                    vrPracePom1.add(vsPracePom1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        vrPraceDetail1 = vrPracePom1;
        tabulkaModelDetail1.oznamZmenu();
    }

    protected void vycistiFiltr1() {
        PraceOdTextField1.setText("");
        PraceDoTextField1.setText("");
    }

    protected class TabulkaModelHlavni extends AbstractTableModel {

        protected final String[] columnNames = {
            "Začátek",
            "Konec",
            "Čas",
            "Průvodka",
            "Výkres",
            "Ks",
            "Zaměstnanec",
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

                //oznamUpdateRadkyPozice(pozice);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 1 || col == 5 || col == 5 || col == 12) {
                return true;
            } else {
                return false;
            }
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
            "Výkres",
            "Akce stroje",
            "Čas"//,
        /* "ID"*/
        };

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
                /*   v = new Vector();

                 v.add(new Long(nastavPruv.pruvodky_id));
                 v.add(new String(nastavPruv.pruvodky_nazev));
                 v.add(new Integer(nastavPruv.pruvodky_cislo_vykresu));
                 v.add(df.format(nastavPruv.pruvodky_termin_dokonceni));
                 v.add(nastavPruv.pruvodky_pocet_kusu);
                 v.add(nastavPruv.pruvodky_pocet_kusu_sklad);
                 v.add(nastavPruv.pruvodky_pocet_kusu_polotovar);
                 v.add(nastavPruv.poznamky);

                 vrPrace1.setElementAt(v, pozice);

                 oznamUpdateRadkyPozice(pozice);
                 */
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 1 || col == 5 || col == 5 || col == 12) {
                return true;
            } else {
                return false;
            }
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
                    vycistiFiltr1();
                }
                refreshTabulkaHlavni();
            }
            if (e.getActionCommand().equals("Hledat")) {
                refreshTabulkaHlavni();
            }

            if (e.getActionCommand().equals("TiskVypisu")) {

                tiskVypisu();
            }
            if (e.getActionCommand().equals("Vyhledat")) {
                refreshTabulkaHlavni();
            }
            if (e.getActionCommand().equals("getDetail")) {
                getDetailInfoStroj();
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
                    nastavVyberTabulkyStrojeDetail1();
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
            refreshTabulkaHlavni();
            ListSelectionModel selectionModel
                    = tabulkaHlavni.getSelectionModel();
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
        StrojComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        PraceOdTextField1 = new javax.swing.JTextField();
        VykresCisloTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        getDetailStrojButton = new javax.swing.JButton();
        UpravitUdajeButton1 = new javax.swing.JButton();
        TiskVypisuButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPHlavni = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPDetail = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        DobaPraceTextField1 = new javax.swing.JTextField();

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

        jLabel1.setText("Stroj :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel1, gridBagConstraints);

        StrojComboBox.setMaximumRowCount(12);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(StrojComboBox, gridBagConstraints);

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

        VykresCisloTextField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 150;
        jPFiltry.add(VykresCisloTextField1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        getDetailStrojButton.setText("Detail ze stroje");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(getDetailStrojButton, gridBagConstraints);

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
        gridBagConstraints.gridheight = 2;
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

        jLabel4.setText("Celková doba práce v  termínu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 10);
        jPanel2.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 10);
        jPanel2.add(DobaPraceTextField1, gridBagConstraints);

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
    private javax.swing.JTextField DobaPraceTextField1;
    private javax.swing.JCheckBox JCheckBoxZobrazovatVseObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JLabel PocetZaznamuLabel1;
    private javax.swing.JTextField PraceDoTextField1;
    private javax.swing.JTextField PraceOdTextField1;
    private javax.swing.JComboBox StrojComboBox;
    private javax.swing.JButton TiskVypisuButton1;
    private javax.swing.JButton UpravitUdajeButton1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JTextField VykresCisloTextField1;
    private javax.swing.JButton getDetailStrojButton;
    private javax.swing.JButton jBVycistitFiltr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPDetail;
    private javax.swing.JScrollPane jSPHlavni;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
