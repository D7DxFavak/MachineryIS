/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRenderer1;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
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
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.*;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class PotvrzeniPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelObjednavka1, tableModelHlavni1;
    protected TabulkaModelDetail tabulkaModelObjednavka1;
    protected TabulkaModelHlavni tabulkaModelHlavni1;
    protected ListSelectionModel lsmObjednavka1, lsmFaktura1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaObjednavka1 tObj1;
    protected TridaPotvrzeni1 tPot1;
    protected ArrayList<TridaObjednavka1> arTO1;
    protected ArrayList<TridaPotvrzeni1> arPot1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public PotvrzeniPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuHlavni1();
        nastavTabulkuObjednavka1();

        nastavPosluchaceUdalostiOvladace();
        this.setVisible(true);


    }

    protected void nastavParametry() {
        arTO1 = new ArrayList<TridaObjednavka1>();
        tObj1 = new TridaObjednavka1();

        arPot1 = new ArrayList<TridaPotvrzeni1>();
        tPot1 = new TridaPotvrzeni1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelObjednavka1 = new TabulkaModelDetail();

        tabulkaDetail.setModel(tabulkaModelObjednavka1);
        tabulkaDetail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulkaDetail.getSelectionModel();
        tableModelObjednavka1 = tabulkaDetail.getModel();

        tabulkaDetail.setPreferredScrollableViewportSize(new Dimension(800, 300));

        tabulkaModelHlavni1 = new TabulkaModelHlavni();

        tabulkaHlavni.setModel(tabulkaModelHlavni1);
        tabulkaHlavni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaHlavni.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmFaktura1 = tabulkaHlavni.getSelectionModel();
        tableModelHlavni1 = tabulkaHlavni.getModel();

        tabulkaHlavni.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmFaktura1.removeListSelectionListener(lslUdalosti);
        tableModelHlavni1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelHlavni1.addTableModelListener(tmlUdalosti);
        lsmFaktura1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravit.addActionListener(alUdalosti);
        jBSmazat.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBTisk.addActionListener(alUdalosti);
        jBZobrazPDF.addActionListener(alUdalosti);
        VyhledatButton1.setActionCommand("Hledat");
        jBTisk.setActionCommand("Tisk");
        jBUpravit.setActionCommand("Upravit");
        jBSmazat.setActionCommand("Smazat");
        jBZobrazPDF.setActionCommand("zobrazPDF");
        jCBZakaznik.setActionCommand("Hledat");
        jCBRokDodani.setActionCommand("Hledat");

    }

    protected void nastavTabulkuObjednavka1() {
      /*  TableColumn column = null;
        column = tabulkaDetail.getColumnModel().getColumn(0);
        column.setPreferredWidth(80);

        column = tabulkaDetail.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);
        
        column = tabulkaDetail.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulkaDetail.getColumnModel().getColumn(3);
        column.setPreferredWidth(30);

        column = tabulkaDetail.getColumnModel().getColumn(4);
        column.setPreferredWidth(115);

        column = tabulkaDetail.getColumnModel().getColumn(5);
        column.setPreferredWidth(40);

        column = tabulkaDetail.getColumnModel().getColumn(6);
        column.setPreferredWidth(60);*/

        //  zrusPosluchaceUdalostiTabulky();
        if (arPot1.size() > 0) {
            getDataTabulkaObjednavka1();
            tabulkaModelObjednavka1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();


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
        column.setPreferredWidth(90);*/

          refreshDataHlavni();

    }// konec nastavTabulkuBT1

     protected void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arPot1.size() > 0) {
            tabulkaHlavni.setRowSelectionInterval(0, 0);
            nastavTabulkuObjednavka1();
        }
    }
    
    protected void nastavTridyObsluhyUdalosti() {
        tmlUdalosti = new TMLUdalosti();
        lslUdalosti = new LSLUdalosti();
        alUdalosti = new ALUdalosti();
        flUdalosti = new FLUdalosti();
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
//            System.out.println("nastav detail : " + (Integer) in.get(i));
            tabulkaDetail.getColumnModel().getColumn(i - tabulkaHlavni.getColumnCount()).setPreferredWidth((Integer) in.get(i));
        }
    }

    protected void initRoletky() {
        roletkaModelZakaznici = new RoletkaUniverzalRozsirenaModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_popis FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_popis", "Neurčen", null,
                "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBZakaznik.setModel(roletkaModelZakaznici);

        roletkaModelRoky = new RoletkaUniverzalRozsirenaModel1(
                "SELECT DISTINCT cast(date_part('year', objednavky_termin_dodani) AS integer), date_part('year', objednavky_termin_dodani) AS roky FROM spolecne.objednavky ORDER BY roky", "Neurčen", null,
                "V databázi nejsou žádné objednávky", 0); // bylo ...vs_id

        Vector vR = SQLFunkceObecne2.getVDvojicCisloRetez("SELECT * FROM (SELECT DISTINCT cast(date_part('year', pruvodky_termin_dokonceni) AS integer) as intpart, "
                + "date_part('year', pruvodky_termin_dokonceni) AS roky FROM spolecne.pruvodky ) AS x "
                + "WHERE x.intpart = cast(date_part('year', current_date) AS integer)");
        if (vR.size() == 1) {
            roletkaModelRoky.setSelectedItem(vR.get(0));
        }

        jCBRokDodani.setModel(roletkaModelRoky);
    }

    protected void getDataTabulkaHlavni1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arPot1.clear();
        tabulkaModelHlavni1.oznamZmenu();

        try {
            String dotaz =
                    "SELECT potvrzeni_id, "
                    + "potvrzeni_datum, "
                    + "COUNT(objednavky_id), "
                    + "potvrzeni_vystavil, "
                    + "CASE WHEN potvrzeni_bindata IS NULL THEN '' "
                    + "ELSE 'ANO' END AS dataPDF "
                    + "FROM spolecne.potvrzeni "
                    + "CROSS JOIN spolecne.objednavky "
                    + "CROSS JOIN spolecne.vazba_potvrzeni_objednavky "
                    + "WHERE vazba_potvrzeni_objednavky_objednavky_id = objednavky_id "
                    + "AND vazba_potvrzeni_objednavky_potvrzeni_id = potvrzeni_id ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND potvrzeni_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM potvrzeni_datum) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString();
            }
            if ((jTFDatumOd.getText().trim().length() > 0) && (jTFDatumDo.getText().trim().length() > 0)) {
                dotaz += "AND potvrzeni_datum BETWEEN " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " AND " + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            dotaz += "GROUP BY potvrzeni_id, potvrzeni_datum, potvrzeni_vystavil, potvrzeni_bindata "
                    + "ORDER BY potvrzeni_datum ASC";
            // System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tPot1 = new TridaPotvrzeni1();
                tPot1.setIdPotvrzeni(q.getInt(1));
                tPot1.setDatumPotvrzeni(q.getDate(2));// datum vystaveni
                tPot1.setPocetObjednavek(q.getInt(3));
                tPot1.setVystavitel((q.getString(4) == null) ? "" : q.getString(4));
                tPot1.setDataPDF((q.getString(5) == null) ? "" : q.getString(5));
                arPot1.add(tPot1);
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arPot1.size() + "");
        int index[][] = new int[arPot1.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[arPot1.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < arPot1.size(); row++) {
            index2[row][1] = 2;
            index2[row][3] = 2;
        }

        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelHlavni1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaHlavni.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }

    } //konec getDataTabulkaObjednavka1

    protected void getDataTabulkaObjednavka1() {


        // boolean datumDodani = false;
        // boolean datumExpedice = false;
        // boolean datumObjednani = false;

        if (tabulkaDetail.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaDetail.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();

        tabulkaModelObjednavka1.oznamZmenu();

        try {
            String dotaz = "";
            pocetKusuObjednavky = 0;

            dotaz = "SELECT objednavky_id, "
                    + "vazba_potvrzeni_objednavky_poradi, "
                    + "objednavky_datum_objednani, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_nazev_soucasti, "
                    + "vykresy_cislo, "
                    + "COALESCE(vykresy_revize, '') as revize, "
                    + "objednavky_cislo_objednavky, "
                    + "vazba_potvrzeni_datum, "
                    + "vykresy_id "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vazba_potvrzeni_objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE vazba_potvrzeni_objednavky_potvrzeni_id = " + arPot1.get(tabulkaHlavni.getSelectedRow()).getIdPotvrzeni() + " "
                    + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "AND vazba_potvrzeni_objednavky_objednavky_id = objednavky_id "
                    + "ORDER BY vazba_potvrzeni_objednavky_poradi, objednavky_cislo_objednavky ASC";

            //System.out.println(dotaz);
            int i = 1;
            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            while (objednavka1.next()) {
                tObj1 = new TridaObjednavka1();
                tObj1.setId(new Long(objednavka1.getLong(1)));
                tObj1.setPoradi(i);
                tObj1.setDatumObjednani(objednavka1.getDate(3));
                tObj1.setPocetObjednanychKusu(objednavka1.getInt(4));
                tObj1.setNazevSoucasti((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(objednavka1.getInt(10));
                tv1.setCislo((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                tv1.setRevize((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                tObj1.setTv1(tv1);
                tObj1.setIdVykres(objednavka1.getInt(10));
                tObj1.setCisloObjednavky((objednavka1.getString(8) == null) ? "" : objednavka1.getString(8));
                tObj1.setDatumDodani(objednavka1.getDate(9));

                arTO1.add(tObj1);
                i++;
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        int index[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index2[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];

        for (int row = 0; row < arTO1.size(); row++) {
            index2[row][1] = 1;
        }
        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelObjednavka1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaDetail.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }

    } //konec getDataTabulkaObjednavka1

    protected void vycistiFiltrObjednavka1() {
        jTFDatumOd.setText("");
        jTFDatumDo.setText("");
    }

    private void tiskPotvrzeni(TridaPotvrzeni1 potvrzeniTisknout, boolean tisk) {
        potvrzeniTisknout.selectData();
        TridaZakaznik1 zakaznikTisk = new TridaZakaznik1();
        String stat = "";
        zakaznikTisk.selectData(potvrzeniTisknout.getIdZakaznik());
        String reportSource = "";

        Map<String, Object> params = new HashMap<String, Object>();

        if (zakaznikTisk.getIdStat() == 1) {
            df = java.text.DateFormat.getDateInstance();
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniCZ.jrxml";
            params.put("datum", (df.format(potvrzeniTisknout.getDatumPotvrzeni())));
        } else if (zakaznikTisk.getIdStat() == 4 || zakaznikTisk.getIdStat() == 7 || zakaznikTisk.getIdStat() == 8) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniBBHUSA.jrxml";
            df = java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            params.put("datum", (df.format(potvrzeniTisknout.getDatumPotvrzeni())));
        } else {
            df = java.text.DateFormat.getDateInstance();
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniDE.jrxml";
            params.put("datum", ("Pilsen, den " + df.format(potvrzeniTisknout.getDatumPotvrzeni())));
        }


        params.put("prijemce", potvrzeniTisknout.getPrijemce());
        params.put("predmet", potvrzeniTisknout.getPredmet());
        params.put("info", potvrzeniTisknout.getUvodVeta());
        params.put("dodatek", potvrzeniTisknout.getDodatek()); // upravit
        
        params.put("logo" , HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "Logo_mikron.png");
        if (zakaznikTisk.getIdStat() == 8) {
        } else if (zakaznikTisk.getIdStat() == 4) {
        } else {
        }
        params.put("fakturu_vystavil", potvrzeniTisknout.getVystavitel());
        params.put("firma", zakaznikTisk.getNazev());

        if (potvrzeniTisknout.getAdresa().isEmpty() == false) {
            params.put("adresa_ulice", potvrzeniTisknout.getAdresa());
        } else {
            params.put("adresa_ulice", zakaznikTisk.getAdresa());
        }
        if (potvrzeniTisknout.getMesto().isEmpty() == false && potvrzeniTisknout.getPsc().isEmpty() == false) {
            params.put("adresa_psc_mesto", potvrzeniTisknout.getPsc() + " " + potvrzeniTisknout.getMesto());
        } else {
            params.put("adresa_psc_mesto", zakaznikTisk.getPsc() + " " + zakaznikTisk.getMesto());
        }
        int idStat = 0;
        if (potvrzeniTisknout.getIdStat() > 0) {
            idStat = (tPot1.getIdStat());
        } else {
            idStat = zakaznikTisk.getIdStat();
        }

        params.put("zakaznik_id_number", zakaznikTisk.getVatNr());


        try {

            ResultSet q = PripojeniDB.dotazS("SELECT staty_nazev FROM spolecne.staty WHERE staty_id = " + idStat);
            while (q.next()) {
                stat = q.getString(1);
            }
            params.put("adresa_stat", stat);
            ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                    + "subjekty_trhu_adresa, "
                    + "'CZ - ' || subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                    + "'Czech Republic', "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_faxy, "
                    + "subjekty_trhu_e_maily "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = 19");
            while (mikronF.next()) {
                params.put("firma_mikron", mikronF.getString(1));
                params.put("adresa_ulice_mikron", mikronF.getString(2));
                params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                params.put("stat_mikron", mikronF.getString(4));
                params.put("telefon", "Tel.: " + mikronF.getString(5));
                params.put("fax", "Fax: " + mikronF.getString(6));
                params.put("email", "e-mail: " + mikronF.getString(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("potvrzeni_id", Long.valueOf(potvrzeniTisknout.getIdPotvrzeni()));
        params.put("zakaznik_id", potvrzeniTisknout.getIdZakaznik());

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1 =
                    JasperFillManager.fillReport(
                    jasperReport, params, PripojeniDB.con);
            //  JasperViewer.viewReport(jasperPrint1);
            if (tisk == true) {
                JasperPrintManager.printReport(jasperPrint1, true);
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "potvrzeni.pdf");
            }
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        df = java.text.DateFormat.getDateInstance();
    }

    private void zobrazitPDF(TridaPotvrzeni1 potvrzeni) {
        String nazevSouboru = "potvrzeni.pdf";
        try {
            ResultSet soubor = PripojeniDB.dotazS("SELECT potvrzeni_bindata "
                    + "FROM spolecne.potvrzeni "
                    + "WHERE potvrzeni_id = " + potvrzeni.getIdPotvrzeni());
            soubor.last();
            if (soubor.getRow() > 0) {
                soubor.beforeFirst();
                File soubor1;
                while (soubor.next()) {
                    soubor1 = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevSouboru);
                    FileOutputStream fos = new FileOutputStream(soubor1);
                    byte[] buffer;
                    buffer = soubor.getBytes(1);
                    fos.write(buffer);
                    fos.close();
                }// konec while
                if (nazevSouboru.toLowerCase().contains(".pdf")) {
                    soubor1 = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevSouboru);
                    Desktop.getDesktop().open(soubor1);
                }
            } // konec if
        }//konec try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void smazatPotvrzeni(TridaPotvrzeni1 potvrzeni) {
        int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání potvrzení",
                "Opravdu chcete smazat potvrzení objednávek ? ", 1);
        if (ud == JOptionPane.YES_OPTION) {
          //  System.out.println("Smazat potvrzeni : " + potvrzeni.getIdPotvrzeni());
            try {
                String dotaz = "DELETE FROM spolecne.vazba_potvrzeni_objednavky WHERE "
                        + "vazba_potvrzeni_objednavky_potvrzeni_id = " + potvrzeni.getIdPotvrzeni();
                int a = PripojeniDB.dotazIUD(dotaz);
                dotaz = "DELETE FROM spolecne.potvrzeni WHERE "
                        + "potvrzeni_id = " + potvrzeni.getIdPotvrzeni();;
                a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            zrusPosluchaceUdalostiTabulky();
            getDataTabulkaHlavni1();
            //tabulkaModelObjednavka1.pridejSadu();
            nastavPosluchaceUdalostiTabulky();
        }
    }

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Pos",
            "Ks",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",
            "Datum objednání",
            "Datum dodání"
        };

        public void pridejSadu() {
            // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTO1.size());
            //  updateZaznamyObjednavka1();
            if (arTO1.size() > 0) {
                tabulkaDetail.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaObjednavka1 tObj) {
            arTO1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulkaDetail.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arTO1.remove(tabulkaDetail.getSelectedRow());
            fireTableRowsDeleted(tabulkaDetail.getSelectedRow(), tabulkaDetail.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (arTO1size() > 0)
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
            return arTO1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        @Override
        public Class getColumnClass(int col) {
            return String.class;

        }

        public boolean nastavHodnotuNaVybrane(TridaObjednavka1 bt) {
            System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaDetail.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulkaDetail.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaObjednavka1 nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            try {

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            /*
             * if (col == 1 || col == 5 || col == 5 || col == 12) { return true;
             * } else {
             */
            return false;
            //}
        }

        @Override
        public Object getValueAt(int row, int col) {
            try {
                tObj1 = arTO1.get(row);
                switch (col) {
                    case (0): {
                        return (tObj1.getPoradi());
                    }
                    case (1): {
                        return (tObj1.getPocetObjednanychKusu() + " ks" + "  ");

                    }
                    case (2): {
                        return (tObj1.getNazevSoucasti());

                    }
                    case (3): {
                        return (tObj1.getTv1().getCislo());

                    }
                    case (4): {
                        return (tObj1.getTv1().getRevize());

                    }
                    case (5): {
                        return (tObj1.getCisloObjednavky());
                    }
                    case (6): {
                        if (tObj1.getDatumObjednani() != null) {
                            return (df.format(tObj1.getDatumObjednani()));
                        } else {
                            return "";
                        }

                    }
                    case (7): {
                        if (tObj1.getDatumDodani()!= null) {
                            return (df.format(tObj1.getDatumDodani()));
                        } else {
                            return "";
                        }

                    }
                    default: {
                        return null;
                    }
                }//konec switch
            }//konec try
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
        }//konec setValueAt
    }//konec modelu tabulky

    protected class TabulkaModelHlavni extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum",
            "Počet položek",
            "Vystavil",
            "PDF"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arPot1.size());
            //  updateZaznamyObjednavka1();
            if (arPot1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arPot1.remove(tabulkaHlavni.getSelectedRow());
            fireTableRowsDeleted(tabulkaHlavni.getSelectedRow(), tabulkaHlavni.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrFaktura1size() > 0)
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
            return arPot1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        public boolean nastavHodnotuNaVybrane(TridaFaktura1 bt) {
            return nastavHodnotuNaPozici(bt, tabulkaHlavni.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaFaktura1 nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            try {
                /*
                 * v = new Vector();
                 *
                 * v.add(new Long(nastavPruv.pruvodky_id)); v.add(new
                 * String(nastavPruv.pruvodky_nazev)); v.add(new
                 * Integer(nastavPruv.pruvodky_cislo_vykresu));
                 * v.add(df.format(nastavPruv.pruvodky_termin_dokonceni));
                 * v.add(nastavPruv.pruvodky_pocet_kusu);
                 * v.add(nastavPruv.pruvodky_pocet_kusu_sklad);
                 * v.add(nastavPruv.pruvodky_pocet_kusu_polotovar);
                 * v.add(nastavPruv.poznamky);
                 *
                 * vrFaktura1.setElementAt(v, pozice);
                 *
                 * oznamUpdateRadkyPozice(pozice);
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
                tPot1 = arPot1.get(row);
                switch (col) {
                    case (0): {
                        return (df.format(tPot1.getDatumPotvrzeni()));
                    }
                    case (1): {
                        return tPot1.getPocetObjednavek();
                    }
                    case (2): {
                        return tPot1.getVystavitel();
                    }
                    case (3): {
                        return (tPot1.getDataPDF());

                    }
                    default: {
                        return null;
                    }
                }//konec switch
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
                getDataTabulkaHlavni1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }
            if (e.getActionCommand().equals("Hledat")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaHlavni1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }


            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }

            if (e.getActionCommand().equals("Tisk")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    tiskPotvrzeni(arPot1.get(tabulkaHlavni.getSelectedRow()), true);
                }
            }
            if (e.getActionCommand().equals("zobrazPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zobrazitPDF(arPot1.get(tabulkaHlavni.getSelectedRow()));
                }
            }

            if (e.getActionCommand().equals("Smazat")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    smazatPotvrzeni(arPot1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("Upravit")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    PotvrzeniFrame1 edit = new PotvrzeniFrame1(arPot1.get(tabulkaHlavni.getSelectedRow()));
                }
            }

        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {
           

            if (tme.getSource() == tableModelObjednavka1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmFaktura1) {
                if (e.getValueIsAdjusting() == false) {
                    if (arPot1.size() > 0) {
                        nastavTabulkuObjednavka1();
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
        JButtonVycistiFiltrObjednavka1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCBRokDodani = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        PocetZaznamuLabel1 = new javax.swing.JLabel();
        jPFiltry = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFDatumOd = new javax.swing.JTextField();
        jTFDatumDo = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jBTisk = new javax.swing.JButton();
        jBZobrazPDF = new javax.swing.JButton();
        jBUpravit = new javax.swing.JButton();
        jBSmazat = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPFaktury = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPObjednavky = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

        jLabel3.setText("Rok potvrzení :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        JPFiltrTop.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jCBRokDodani, gridBagConstraints);

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

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel16.setText("Potvrzeno od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);

        jLabel17.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFDatumOd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFDatumDo, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(VyhledatButton1, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBZakaznik.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBTisk.setText("Tisk potvrzení");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBTisk, gridBagConstraints);

        jBZobrazPDF.setText("Zobrazit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazPDF, gridBagConstraints);

        jBUpravit.setText("Upravit potvrzení");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravit, gridBagConstraints);

        jBSmazat.setText("Smazat potvrzení");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazat, gridBagConstraints);

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
        jSPFaktury.setViewportView(tabulkaHlavni);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 20);
        jPanel2.add(jSPFaktury, gridBagConstraints);

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
        jSPObjednavky.setViewportView(tabulkaDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 10);
        jPanel2.add(jSPObjednavky, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1315, Short.MAX_VALUE)
            .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JLabel PocetZaznamuLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisk;
    private javax.swing.JButton jBUpravit;
    private javax.swing.JButton jBZobrazPDF;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPFaktury;
    private javax.swing.JScrollPane jSPObjednavky;
    private javax.swing.JTextField jTFDatumDo;
    private javax.swing.JTextField jTFDatumOd;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
