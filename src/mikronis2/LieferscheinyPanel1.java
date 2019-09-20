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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
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
public class LieferscheinyPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelObjednavka1, tableModelLieferschein;
    protected TabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected TabulkaModelLiefer1 tabulkaModelLiefer1;
    protected ListSelectionModel lsmObjednavka1, lsmLieferschein;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaObjednavka1 tObj1;
    protected TridaLieferschein1 tLief1;
    protected ArrayList<TridaObjednavka1> arTO1;
    protected ArrayList<TridaLieferschein1> arLief1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public LieferscheinyPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuLiefer();
        nastavTabulkuObjednavka1();

        nastavPosluchaceUdalostiOvladace();

        getInfoZaznam();

        // System.out.println("dotaz : SELECT " + SQLFunkceObecne.selectTXTPole("current_date;"));
        //PosledniLieferscheinTextField1.setText(SQLFunkceObecne.selectTXTPole("lieferscheiny_cislo_lieferscheinu FROM spolecne.lieferscheiny "
        //        + "ORDER BY substring(lieferscheiny_cislo_lieferscheinu from '...$') DESC, substring(lieferscheiny_cislo_lieferscheinu from 1 for 4) DESC  LIMIT 1"));
        this.setVisible(true);
    }

    protected void nastavParametry() {
        arTO1 = new ArrayList<TridaObjednavka1>();
        tObj1 = new TridaObjednavka1();

        arLief1 = new ArrayList<TridaLieferschein1>();
        tLief1 = new TridaLieferschein1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelObjednavka1 = new TabulkaModelObjednavka1();

        tabulkaDetail.setModel(tabulkaModelObjednavka1);
        tabulkaDetail.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulkaDetail.getSelectionModel();
        tableModelObjednavka1 = tabulkaDetail.getModel();

        tabulkaDetail.setPreferredScrollableViewportSize(new Dimension(800, 300));

        tabulkaModelLiefer1 = new TabulkaModelLiefer1();

        tabulkaHlavni.setModel(tabulkaModelLiefer1);
        tabulkaHlavni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaHlavni.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmLieferschein = tabulkaHlavni.getSelectionModel();
        tableModelLieferschein = tabulkaHlavni.getModel();

        tabulkaHlavni.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmLieferschein.removeListSelectionListener(lslUdalosti);
        tableModelLieferschein.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelLieferschein.addTableModelListener(tmlUdalosti);
        lsmLieferschein.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {
        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jBFixovat.addActionListener(alUdalosti);
        jBZrusitFixaci.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravit.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBTisk.addActionListener(alUdalosti);
        jBTiskExpedice.addActionListener(alUdalosti);
        jBUlozPDF.addActionListener(alUdalosti);
        jBZobrazPDF.addActionListener(alUdalosti);
        jBSmazat.addActionListener(alUdalosti);
        VyhledatButton1.setActionCommand("Hledat");

        jBTisk.setActionCommand("TiskFaktura");
        jBTiskExpedice.setActionCommand("TiskExpedice");
        jBUpravit.setActionCommand("UpravitDodaciList");
        jBSmazat.setActionCommand("SmazatLiefer");
        jBFixovat.setActionCommand("FixovatFakturu");
        jBZobrazPDF.setActionCommand("zobrazPDF");
        jBUlozPDF.setActionCommand("ulozPDF");
        jBZrusitFixaci.setActionCommand("zrusitFixaci");
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
        if (arLief1.size() > 0) {
            getDataTabulkaObjednavka1();
            tabulkaModelObjednavka1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void nastavTabulkuLiefer() {
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
        column.setPreferredWidth(115);

        column = tabulkaHlavni.getColumnModel().getColumn(5);
        column.setPreferredWidth(130);

        column = tabulkaHlavni.getColumnModel().getColumn(6);
        column.setPreferredWidth(90);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaLiefer1();
        tabulkaModelLiefer1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaLiefer1();
        tabulkaModelLiefer1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arLief1.size() > 0) {
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

    protected void getDataTabulkaLiefer1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arLief1.clear();
        tabulkaModelLiefer1.oznamZmenu();

        boolean osetritDatumOd = TextFunkce1.osetriDatum(jTFDatumOd.getText().trim());
        boolean osetritDatumDo = TextFunkce1.osetriDatum(jTFDatumDo.getText().trim());

        try {
            String dotaz = "SELECT * FROM (SELECT lieferscheiny_id, "
                    + "lieferscheiny_cislo_lieferscheinu, "
                    + "lieferscheiny_datum_vystaveni, "
                    + "COUNT(objednavky_id), "
                    + "lieferscheiny_vystavil, "
                    + "lieferscheiny_poznamka, "
                    + "lieferscheiny_fixovany, "
                    + "CASE WHEN lieferscheiny_fixovany IS FALSE THEN 'otevřený'  "
                    + "ELSE 'fixovaný' END AS stav,  "
                    + "CASE WHEN lieferscheiny_bindata IS NULL THEN '' "
                    + "ELSE 'ANO' END AS dataPDF, "
                    + "substring(lieferscheiny_cislo_lieferscheinu from '..$') as rok, "
                    + "COALESCE(lieferscheiny_dodaci_adresa,0), "
                    + "lieferscheiny_zakaznik_id "
                    + "FROM spolecne.lieferscheiny "
                    + "CROSS JOIN spolecne.objednavky "
                    + "CROSS JOIN spolecne.vazba_lieferscheiny_objednavky "
                    + "WHERE vazba_lieferscheiny_objednavky_objednavky_id = objednavky_id "
                    + "AND vazba_lieferscheiny_objednavky_lieferscheiny_id = lieferscheiny_id ";
            if (jTFCisloLieferschein.getText().trim().length() > 0) {
                dotaz += "AND lieferscheiny_cislo_lieferscheinu = " + TextFunkce1.osetriZapisTextDB1(jTFCisloLieferschein.getText().trim()) + " ";
            }
            if (osetritDatumOd) {
                dotaz += "AND lieferscheiny_datum_vystaveni >= " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " ";
            }
            if (osetritDatumDo) {
                dotaz += "AND lieferscheiny_datum_vystaveni <= " + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND lieferscheiny_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM lieferscheiny_datum_vystaveni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            dotaz += "AND lieferscheiny_fixovany IS FALSE "
                    + "GROUP BY lieferscheiny_id,  "
                    + "lieferscheiny_cislo_lieferscheinu,  "
                    + "lieferscheiny_datum_vystaveni,  "
                    + "lieferscheiny_vystavil,  "
                    + "lieferscheiny_poznamka,  "
                    + "lieferscheiny_fixovany, "
                    + "stav,   "
                    + "lieferscheiny_bindata, "
                    + "lieferscheiny_dodaci_adresa,"
                    + "lieferscheiny_zakaznik_id "
                    + "ORDER BY lieferscheiny_cislo_lieferscheinu ASC) AS l_otevreny "
                    + "UNION SELECT * FROM (SELECT lieferscheiny_id, "
                    + "lieferscheiny_cislo_lieferscheinu, "
                    + "lieferscheiny_datum_vystaveni, "
                    + "COUNT(lieferscheiny_polozky_fix_polozka),  "
                    + "lieferscheiny_vystavil,  "
                    + "lieferscheiny_poznamka,  "
                    + "lieferscheiny_fixovany,  "
                    + "CASE WHEN lieferscheiny_fixovany IS FALSE THEN 'otevřený'   "
                    + "ELSE 'fixovaný' END AS stav,  "
                    + "CASE WHEN lieferscheiny_bindata IS NULL THEN '' "
                    + "ELSE 'ANO' END AS dataPDF, "
                    + "substring(lieferscheiny_cislo_lieferscheinu from '..$') as rok, "
                    + "COALESCE(lieferscheiny_dodaci_adresa,0),"
                    + "lieferscheiny_zakaznik_id "
                    + "FROM spolecne.lieferscheiny  "
                    + "CROSS JOIN spolecne.objednavky  "
                    + "CROSS JOIN spolecne.lieferscheiny_polozky_fix "
                    + "WHERE lieferscheiny_polozky_fix_objednavky_id = objednavky_id  "
                    + "AND lieferscheiny_polozky_fix_id = lieferscheiny_id  "
                    + "AND lieferscheiny_fixovany IS TRUE ";
            if (jTFCisloLieferschein.getText().trim().length() > 0) {
                dotaz += "AND lieferscheiny_cislo_lieferscheinu = " + TextFunkce1.osetriZapisTextDB1(jTFCisloLieferschein.getText().trim()) + " ";
            }
            if (osetritDatumOd) {
                dotaz += "AND lieferscheiny_datum_vystaveni >= " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " ";
            }
            if (osetritDatumDo) {
                dotaz += "AND lieferscheiny_datum_vystaveni <= " + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM lieferscheiny_datum_vystaveni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND lieferscheiny_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            dotaz += "GROUP BY lieferscheiny_id,  "
                    + "lieferscheiny_cislo_lieferscheinu,  "
                    + "lieferscheiny_datum_vystaveni,  "
                    + "lieferscheiny_vystavil,  "
                    + "lieferscheiny_poznamka,  "
                    + "lieferscheiny_fixovany, "
                    + "stav, "
                    + "lieferscheiny_bindata, "
                    + "lieferscheiny_dodaci_adresa, "
                    + "lieferscheiny_zakaznik_id "
                    + "ORDER BY lieferscheiny_cislo_lieferscheinu ASC) AS l_fixovany "
                    + "ORDER BY rok, lieferscheiny_cislo_lieferscheinu ";
            //  System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tLief1 = new TridaLieferschein1();
                tLief1.setIdLieferschein(q.getInt(1));
                tLief1.setCisloLieferschein((q.getString(2) == null) ? "" : q.getString(2));// cislo faktury
                tLief1.setDatumVystaveni(q.getDate(3));// datum vystaveni
                tLief1.setPocetPolozek(q.getInt(4));
                tLief1.setVystavitel((q.getString(5) == null) ? "" : q.getString(5)); // vystavitel
                tLief1.setPoznamky((q.getString(6) == null) ? "" : q.getString(6)); // poznamka      
                tLief1.setFixovana(q.getBoolean(7)); // fixovana
                tLief1.setStavLieferschein((q.getString(8) == null) ? "" : q.getString(8)); // stav
                tLief1.setPdf((q.getString(9) == null) ? "" : q.getString(9)); // pdf    
                tLief1.setIdAdresa(q.getInt(11));
                tLief1.setIdZakaznik(q.getInt(12));
                arLief1.add(tLief1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arLief1.size() + "");
        int index[][] = new int[arLief1.size()][tabulkaModelLiefer1.getColumnCount()];
        int index2[][] = new int[arLief1.size()][tabulkaModelLiefer1.getColumnCount()];

        for (int row = 0; row < arLief1.size(); row++) {
            index2[row][2] = 2;
            index2[row][5] = 2;
            index2[row][6] = 2;
        }

        Color backColor = new Color(216, 232, 249);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelLiefer1.getColumnCount(); i++) {
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
            if (arLief1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {

                pocetKusuObjednavky = 0;

                dotaz = "SELECT objednavky_id, "
                        + "null, "
                        + "objednavky_pocet_objednanych_kusu, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky, "
                        + "objednavky_cena_za_kus, "
                        + "objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus AS cena_celkem, "
                        + "vykresy_id,"
                        + "objednavky_datum_objednani "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vazba_lieferscheiny_objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE vazba_lieferscheiny_objednavky_lieferscheiny_id = " + arLief1.get(tabulkaHlavni.getSelectedRow()).getIdLieferschein() + " "
                        + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND vazba_lieferscheiny_objednavky_objednavky_id = objednavky_id "
                        + "ORDER BY vazba_lieferscheiny_objednavky_poradi , vykresy_cislo,vykresy_revize";
            } else {
                dotaz = "SELECT objednavky_id, "
                        + "lieferscheiny_polozky_fix_polozka, "
                        + "lieferscheiny_polozky_fix_kusu, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky, "
                        + "lieferscheiny_polozky_fix_cena_za_kus, "
                        + "lieferscheiny_polozky_fix_kusu * lieferscheiny_polozky_fix_cena_za_kus AS cena_celkem, "
                        + "vykresy_id, "
                        + "objednavky_datum_objednani "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.lieferscheiny_polozky_fix "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE lieferscheiny_polozky_fix_id = " + arLief1.get(tabulkaHlavni.getSelectedRow()).getIdLieferschein() + " "
                        + "AND vykresy.vykresy_id = lieferscheiny_polozky_fix_cislo_vykresu "
                        + "AND lieferscheiny_polozky_fix_objednavky_id = objednavky_id "
                        + "ORDER BY lieferscheiny_polozky_fix_polozka ASC, vykresy_je_realny DESC, lieferscheiny_polozky_fix_polozka";
            }
            //System.out.println(dotaz);
            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            int poradi = 1;
            while (objednavka1.next()) {
                tObj1 = new TridaObjednavka1();
                tObj1.setId(new Long(objednavka1.getLong(1)));
                if (arLief1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {
                    tObj1.setPoradi(poradi);
                } else {
                    tObj1.setPoradi(objednavka1.getInt(2));
                }

                tObj1.setPocetObjednanychKusu(objednavka1.getInt(3));
                tObj1.setNazevSoucasti((objednavka1.getString(4) == null) ? "" : objednavka1.getString(4));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(objednavka1.getInt(10));
                tv1.setCislo((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                tv1.setRevize((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                tObj1.setTv1(tv1);
                tObj1.setIdVykres(objednavka1.getInt(10));
                tObj1.setCisloObjednavky((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                tObj1.setCenaKus(nf2.format(nf2.parse(objednavka1.getString(8).replace(".", ",")))); // cena
                tObj1.setDatumObjednani(objednavka1.getDate(11));
                arTO1.add(tObj1);
                poradi++;

            }// konec while

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        int index[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index2[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];

        for (int row = 0; row < arTO1.size(); row++) {
            index[row][7] = 1;
            index2[row][0] = 1;
            index2[row][5] = 1;
            index2[row][7] = 1;
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
        jTFCisloLieferschein.setText("");
    }

    private void zobrazitLieferschein(TridaLieferschein1 liefer) {
        String nazevSouboru = "lieferschein.pdf";
        try {
            ResultSet soubor = PripojeniDB.dotazS("SELECT lieferscheiny_bindata "
                    + "FROM spolecne.lieferscheiny "
                    + "WHERE lieferscheiny_id = " + liefer.getIdLieferschein());
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

    private void tiskLieferschein(TridaLieferschein1 liefer) {
       
        tLief1.selectData(liefer.getIdLieferschein());
        TridaZakaznik1 tz1 = new TridaZakaznik1(tLief1.getIdZakaznik());
        String reportSource = "";
        int pocetPolozek = 0;

        Map<String, Object> params = new HashMap<String, Object>();

        if (tz1.getIdStat() == 2 || tz1.getIdStat() == 3 || tz1.getIdStat() == 6) {
            params.put("lieferschein", "L I E F E R S C H E I N - Nr.: " + tLief1.getCisloLieferschein());
            if (tLief1.isFixovana() == true) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixDE.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinDE.jrxml";
            }
        } else if (tz1.getIdStat() == 4 || tz1.getIdStat() == 7 || tz1.getIdStat() == 8) {
            params.put("lieferschein", tLief1.getCisloLieferschein());
            if (tz1.getIdZakaznik() == 12) {
                if (tLief1.isFixovana() == true) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixEN.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinEN.jrxml";
                }
            } else {
                if (tLief1.isFixovana() == true) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixENUni.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinENUni.jrxml";
                }
            }
        } else if (tz1.getIdStat() == 1 || tz1.getIdStat() == 5) {
            params.put("lieferschein", tLief1.getCisloLieferschein());
            if (tLief1.isFixovana() == true) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixCZ.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinCZ.jrxml";
            }

        }

        Vector vsZakaznikLiefer1 = new Vector();

        try {
            String dotaz = "";
            if (liefer.getIdAdresa() != 0) {
                dotaz = "SELECT subjekty_trhu_nazev, "
                        + "dodaci_adresy_adresa, "
                        + "dodaci_adresy_mesto, "
                        + "dodaci_adresy_psc, "
                        + "staty_nazev, "
                        + "COALESCE(subjekty_trhu_telefony, ' ') as telefon, "
                        + "dodaci_adresy_popis "
                        + "FROM spolecne.subjekty_trhu "
                        + "CROSS JOIN spolecne.staty "
                        + "CROSS JOIN spolecne.dodaci_adresy "
                        + "WHERE staty.staty_id = dodaci_adresy_stat_id "
                        + "AND subjekty_trhu_id = dodaci_adresy_subjekt_trhu "
                        + "AND dodaci_adresy_id = " + liefer.getIdAdresa();

                //System.out.println(dotaz);
                ResultSet pruvodka1 = PripojeniDB.dotazS(dotaz);
                while (pruvodka1.next()) {
                    vsZakaznikLiefer1.add(pruvodka1.getString(1));             //nazev firmy
                    vsZakaznikLiefer1.add(pruvodka1.getString(2));             //adresa
                    vsZakaznikLiefer1.add(pruvodka1.getString(3));             //mesto
                    vsZakaznikLiefer1.add(pruvodka1.getString(4));             //psc
                    vsZakaznikLiefer1.add(pruvodka1.getString(5));             //stat
                    vsZakaznikLiefer1.add(pruvodka1.getString(6));             //tel
                    vsZakaznikLiefer1.add(pruvodka1.getString(7));             //pozn
                }// konec while
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String dotaz = "";
            if (tLief1.isFixovana()) {
                dotaz = "SELECT COUNT(lieferscheiny_polozky_fix_polozka) "
                        + "FROM spolecne.lieferscheiny_polozky_fix WHERE lieferscheiny_polozky_fix_id = " + tLief1.getIdLieferschein();
            } else {
                dotaz = "SELECT COUNT(vazba_lieferscheiny_objednavky_objednavky_id) "
                        + "FROM spolecne.vazba_lieferscheiny_objednavky WHERE vazba_lieferscheiny_objednavky_lieferscheiny_id = " + tLief1.getIdLieferschein();
            }
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                pocetPolozek = q.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tLief1.getNazev1Radek().isEmpty() == false) {
            params.put("firma", tLief1.getNazev1Radek());
            params.put("firmaPokr", tLief1.getNazev2Radek());
        } else {
            params.put("firma", tz1.getNazev());
            params.put("firmaPokr", "");
        }
        if (tLief1.getAdresa().isEmpty() == false) {
            params.put("adresa_ulice", tLief1.getAdresa());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_ulice", (String) vsZakaznikLiefer1.get(1));
            } else {
                params.put("adresa_ulice", tz1.getAdresa());
            }
        }
        if (tLief1.getMesto().isEmpty() == false && tLief1.getPsc().isEmpty() == false) {
            params.put("adresa_psc_mesto", tLief1.getPsc() + " " + tLief1.getMesto());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_psc_mesto", (String) vsZakaznikLiefer1.get(3) + " " + (String) vsZakaznikLiefer1.get(2));
            } else {
                params.put("adresa_psc_mesto", tz1.getPsc() + " " + tz1.getMesto());
            }
        }
        if (tLief1.getStat().isEmpty() == false) {
            params.put("adresa_stat", tLief1.getStat());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_stat", (String) vsZakaznikLiefer1.get(4));
            } else {
                params.put("adresa_stat", SQLFunkceObecne2.selectStringPole("SELECT staty_nazev FROM spolecne.staty WHERE staty_id = " + tz1.getIdStat()));
            }
        }
        if (tLief1.getKontaktTelefon().isEmpty() == false) {
            params.put("telefonni_cislo", tLief1.getKontaktTelefon());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("telefonni_cislo", (String) vsZakaznikLiefer1.get(5));
            } else {
                params.put("telefonni_cislo", tz1.getTelefony());
            }
        }
        if (tLief1.getKontaktOsoba().isEmpty() == false) {
            params.put("kontakt", tLief1.getKontaktOsoba());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("kontakt", (String) vsZakaznikLiefer1.get(6));
            } else {
                params.put("kontakt", "");
            }
        }

        params.put("datum_vystaveni", df.format(tLief1.getDatumVystaveni()));
        params.put("vystavil", tLief1.getVystavitel());
        params.put("pocetPolozek", pocetPolozek);

        try {
            ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                    + "subjekty_trhu_adresa, "
                    + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                    + "'Czech Republic', "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_faxy "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = 19");
            while (mikronF.next()) {
                params.put("firma_mikron", mikronF.getString(1));
                params.put("adresa_ulice_mikron", mikronF.getString(2));
                params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                params.put("stat_mikron", mikronF.getString(4));
                params.put("telefon", mikronF.getString(5));
                params.put("fax", mikronF.getString(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("hruba_vaha", tLief1.getHrubaVaha() + " kg");
        params.put("cista_vaha", tLief1.getCistaVaha() + " kg");
        params.put("com_code", ((tLief1.getComCode().isEmpty()) ? "---" : tLief1.getComCode()));

        params.put("lieferschein_id", tLief1.getIdLieferschein());

        //System.out.println("Tisk Liefer : " + tLief1.getIdLieferschein());

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            // JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void tiskExpedice(TridaLieferschein1 liefer) {

        tLief1.selectData(liefer.getIdLieferschein());
        TridaZakaznik1 tz1 = new TridaZakaznik1(tLief1.getIdZakaznik());
        String reportSource = "";
        int pocetPolozek = 0;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lieferschein", tLief1.getCisloLieferschein());
        reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinExpedice.jrxml";

        Vector vsZakaznikLiefer1 = new Vector();

        try {
            String dotaz = "";
            if (liefer.getIdAdresa() != 0) {
                dotaz = "SELECT subjekty_trhu_nazev, "
                        + "dodaci_adresy_adresa, "
                        + "dodaci_adresy_mesto, "
                        + "dodaci_adresy_psc, "
                        + "staty_nazev, "
                        + "COALESCE(subjekty_trhu_telefony, ' ') as telefon, "
                        + "dodaci_adresy_popis "
                        + "FROM spolecne.subjekty_trhu "
                        + "CROSS JOIN spolecne.staty "
                        + "CROSS JOIN spolecne.dodaci_adresy "
                        + "WHERE staty.staty_id = dodaci_adresy_stat_id "
                        + "AND subjekty_trhu_id = dodaci_adresy_subjekt_trhu "
                        + "AND dodaci_adresy_id = " + liefer.getIdAdresa();

                //System.out.println(dotaz);
                ResultSet pruvodka1 = PripojeniDB.dotazS(dotaz);
                while (pruvodka1.next()) {
                    vsZakaznikLiefer1.add(pruvodka1.getString(1));             //nazev firmy
                    vsZakaznikLiefer1.add(pruvodka1.getString(2));             //adresa
                    vsZakaznikLiefer1.add(pruvodka1.getString(3));             //mesto
                    vsZakaznikLiefer1.add(pruvodka1.getString(4));             //psc
                    vsZakaznikLiefer1.add(pruvodka1.getString(5));             //stat
                    vsZakaznikLiefer1.add(pruvodka1.getString(6));             //tel
                    vsZakaznikLiefer1.add(pruvodka1.getString(7));             //pozn
                }// konec while
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String dotaz = "";
            if (tLief1.isFixovana()) {
                dotaz = "SELECT COUNT(lieferscheiny_polozky_fix_polozka) "
                        + "FROM spolecne.lieferscheiny_polozky_fix WHERE lieferscheiny_polozky_fix_id = " + tLief1.getIdLieferschein();
            } else {
                dotaz = "SELECT COUNT(vazba_lieferscheiny_objednavky_objednavky_id) "
                        + "FROM spolecne.vazba_lieferscheiny_objednavky WHERE vazba_lieferscheiny_objednavky_lieferscheiny_id = " + tLief1.getIdLieferschein();
            }
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                pocetPolozek = q.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tLief1.getNazev1Radek().isEmpty() == false) {
            params.put("firma", tLief1.getNazev1Radek());
            params.put("firmaPokr", tLief1.getNazev2Radek());
        } else {
            params.put("firma", tz1.getNazev());
            params.put("firmaPokr", "");
        }
        if (tLief1.getAdresa().isEmpty() == false) {
            params.put("adresa_ulice", tLief1.getAdresa());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_ulice", (String) vsZakaznikLiefer1.get(1));
            } else {
                params.put("adresa_ulice", tz1.getAdresa());
            }
        }
        if (tLief1.getMesto().isEmpty() == false && tLief1.getPsc().isEmpty() == false) {
            params.put("adresa_psc_mesto", tLief1.getPsc() + " " + tLief1.getMesto());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_psc_mesto", (String) vsZakaznikLiefer1.get(3) + " " + (String) vsZakaznikLiefer1.get(2));
            } else {
                params.put("adresa_psc_mesto", tz1.getPsc() + " " + tz1.getMesto());
            }
        }
        if (tLief1.getStat().isEmpty() == false) {
            params.put("adresa_stat", tLief1.getStat());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_stat", (String) vsZakaznikLiefer1.get(4));
            } else {
                params.put("adresa_stat", SQLFunkceObecne2.selectStringPole("SELECT staty_nazev FROM spolecne.staty WHERE staty_id = " + tz1.getIdStat()));
            }
        }
        if (tLief1.getKontaktTelefon().isEmpty() == false) {
            params.put("telefonni_cislo", tLief1.getKontaktTelefon());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("telefonni_cislo", (String) vsZakaznikLiefer1.get(5));
            } else {
                params.put("telefonni_cislo", tz1.getTelefony());
            }
        }
        if (tLief1.getKontaktOsoba().isEmpty() == false) {
            params.put("kontakt", tLief1.getKontaktOsoba());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("kontakt", (String) vsZakaznikLiefer1.get(6));
            } else {
                params.put("kontakt", "");
            }
        }

        params.put("datum_vystaveni", df.format(tLief1.getDatumVystaveni()));
        params.put("vystavil", tLief1.getVystavitel());
        params.put("pocetPolozek", pocetPolozek);

        try {
            ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                    + "subjekty_trhu_adresa, "
                    + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                    + "'Czech Republic', "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_faxy "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = 19");
            while (mikronF.next()) {
                params.put("firma_mikron", mikronF.getString(1));
                params.put("adresa_ulice_mikron", mikronF.getString(2));
                params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                params.put("stat_mikron", mikronF.getString(4));
                params.put("telefon", mikronF.getString(5));
                params.put("fax", mikronF.getString(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("hruba_vaha", tLief1.getHrubaVaha() + " kg");
        params.put("cista_vaha", tLief1.getCistaVaha() + " kg");
        params.put("com_code", ((tLief1.getComCode().isEmpty()) ? "---" : tLief1.getComCode()));

        params.put("lieferschein_id", tLief1.getIdLieferschein());

        System.out.println("Tisk Liefer : " + tLief1.getIdLieferschein());

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            // JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void lieferscheinUlozeniTemp(TridaLieferschein1 liefer) {

        tLief1.selectData(liefer.getIdLieferschein());
        TridaZakaznik1 tz1 = new TridaZakaznik1(tLief1.getIdZakaznik());
        String reportSource = "";
        int pocetPolozek = 0;

        Map<String, Object> params = new HashMap<String, Object>();

        if (tz1.getIdStat() == 2 || tz1.getIdStat() == 3 || tz1.getIdStat() == 6) {
            params.put("lieferschein", "L I E F E R S C H E I N - Nr.: " + tLief1.getCisloLieferschein());
            if (tLief1.isFixovana() == true) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixDE.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinDE.jrxml";
            }
        } else if (tz1.getIdStat() == 4 || tz1.getIdStat() == 7 || tz1.getIdStat() == 8) {
            params.put("lieferschein", tLief1.getCisloLieferschein());
            if (tz1.getIdZakaznik() == 12) {
                if (tLief1.isFixovana() == true) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixEN.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinEN.jrxml";
                }
            } else {
                if (tLief1.isFixovana() == true) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixENUni.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinENUni.jrxml";
                }
            }
        } else if (tz1.getIdStat() == 1 || tz1.getIdStat() == 5) {
            params.put("lieferschein", tLief1.getCisloLieferschein());
            if (tLief1.isFixovana() == true) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinFixCZ.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinCZ.jrxml";
            }

        }

        Vector vsZakaznikLiefer1 = new Vector();

        try {
            String dotaz = "";
            if (liefer.getIdAdresa() != 0) {
                dotaz = "SELECT subjekty_trhu_nazev, "
                        + "dodaci_adresy_adresa, "
                        + "dodaci_adresy_mesto, "
                        + "dodaci_adresy_psc, "
                        + "staty_nazev, "
                        + "COALESCE(subjekty_trhu_telefony, ' ') as telefon, "
                        + "dodaci_adresy_popis "
                        + "FROM spolecne.subjekty_trhu "
                        + "CROSS JOIN spolecne.staty "
                        + "CROSS JOIN spolecne.dodaci_adresy "
                        + "WHERE staty.staty_id = dodaci_adresy_stat_id "
                        + "AND subjekty_trhu_id = dodaci_adresy_subjekt_trhu "
                        + "AND dodaci_adresy_id = " + liefer.getIdAdresa();

                //System.out.println(dotaz);
                ResultSet pruvodka1 = PripojeniDB.dotazS(dotaz);
                while (pruvodka1.next()) {
                    vsZakaznikLiefer1.add(pruvodka1.getString(1));             //nazev firmy
                    vsZakaznikLiefer1.add(pruvodka1.getString(2));             //adresa
                    vsZakaznikLiefer1.add(pruvodka1.getString(3));             //mesto
                    vsZakaznikLiefer1.add(pruvodka1.getString(4));             //psc
                    vsZakaznikLiefer1.add(pruvodka1.getString(5));             //stat
                    vsZakaznikLiefer1.add(pruvodka1.getString(6));             //tel
                    vsZakaznikLiefer1.add(pruvodka1.getString(7));             //pozn
                }// konec while
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String dotaz = "";
            if (tLief1.isFixovana()) {
                dotaz = "SELECT COUNT(lieferscheiny_polozky_fix_polozka) "
                        + "FROM spolecne.lieferscheiny_polozky_fix WHERE lieferscheiny_polozky_fix_id = " + tLief1.getIdLieferschein();
            } else {
                dotaz = "SELECT COUNT(vazba_lieferscheiny_objednavky_objednavky_id) "
                        + "FROM spolecne.vazba_lieferscheiny_objednavky WHERE vazba_lieferscheiny_objednavky_lieferscheiny_id = " + tLief1.getIdLieferschein();
            }
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                pocetPolozek = q.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tLief1.getNazev1Radek().isEmpty() == false) {
            params.put("firma", tLief1.getNazev1Radek());
            params.put("firmaPokr", tLief1.getNazev2Radek());
        } else {
            params.put("firma", tz1.getNazev());
            params.put("firmaPokr", "");
        }
        if (tLief1.getAdresa().isEmpty() == false) {
            params.put("adresa_ulice", tLief1.getAdresa());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_ulice", (String) vsZakaznikLiefer1.get(1));
            } else {
                params.put("adresa_ulice", tz1.getAdresa());
            }
        }
        if (tLief1.getMesto().isEmpty() == false && tLief1.getPsc().isEmpty() == false) {
            params.put("adresa_psc_mesto", tLief1.getPsc() + " " + tLief1.getMesto());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_psc_mesto", (String) vsZakaznikLiefer1.get(3) + " " + (String) vsZakaznikLiefer1.get(2));
            } else {
                params.put("adresa_psc_mesto", tz1.getPsc() + " " + tz1.getMesto());
            }
        }
        if (tLief1.getStat().isEmpty() == false) {
            params.put("adresa_stat", tLief1.getStat());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("adresa_stat", (String) vsZakaznikLiefer1.get(4));
            } else {
                params.put("adresa_stat", SQLFunkceObecne2.selectStringPole("SELECT staty_nazev FROM spolecne.staty WHERE staty_id = " + tz1.getIdStat()));
            }
        }
        if (tLief1.getKontaktTelefon().isEmpty() == false) {
            params.put("telefonni_cislo", tLief1.getKontaktTelefon());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("telefonni_cislo", (String) vsZakaznikLiefer1.get(5));
            } else {
                params.put("telefonni_cislo", tz1.getTelefony());
            }
        }
        if (tLief1.getKontaktOsoba().isEmpty() == false) {
            params.put("kontakt", tLief1.getKontaktOsoba());
        } else {
            if (liefer.getIdAdresa() != 0) {
                params.put("kontakt", (String) vsZakaznikLiefer1.get(6));
            } else {
                params.put("kontakt", "");
            }
        }

        params.put("datum_vystaveni", df.format(tLief1.getDatumVystaveni()));
        params.put("poznamky", tLief1.getPoznamky());
        params.put("vystavil", tLief1.getVystavitel());
        params.put("pocetPolozek", pocetPolozek);

        try {
            ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                    + "subjekty_trhu_adresa, "
                    + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                    + "'Czech Republic', "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_faxy "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = 19");
            while (mikronF.next()) {
                params.put("firma_mikron", mikronF.getString(1));
                params.put("adresa_ulice_mikron", mikronF.getString(2));
                params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                params.put("stat_mikron", mikronF.getString(4));
                params.put("telefon", mikronF.getString(5));
                params.put("fax", mikronF.getString(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("hruba_vaha", tLief1.getHrubaVaha() + " kg");
        params.put("cista_vaha", tLief1.getCistaVaha() + " kg");
        params.put("com_code", ((tLief1.getComCode().isEmpty()) ? "---" : tLief1.getComCode()));

        params.put("lieferschein_id", tLief1.getIdLieferschein());

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);

            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "lieferschein.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void smazatLieferschein(TridaLieferschein1 liefer) {
        int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání liedfercheinu",
                "Opravdu chcete smazat lieferschein ? ", 1);
        if (ud == JOptionPane.YES_OPTION) {
            try {
                String dotaz = "DELETE FROM spolecne.vazba_lieferscheiny_objednavky WHERE "
                        + "vazba_lieferscheiny_objednavky_lieferscheiny_id = " + liefer.getIdLieferschein();
                int a = PripojeniDB.dotazIUD(dotaz);
                dotaz = "DELETE FROM spolecne.lieferscheiny WHERE "
                        + "lieferscheiny_id = " + liefer.getIdLieferschein();
                a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getDataTabulkaLiefer1();
        }
    }

    private void lieferscheinUploadDB(TridaLieferschein1 liefer) {
        // System.out.println("Updatuji doklad " + dokladCislo);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "lieferschein.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.lieferscheiny SET lieferscheiny_bindata = ? WHERE lieferscheiny_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, liefer.getIdLieferschein());
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fixovatLieferschein(TridaLieferschein1 liefer) {
        String dotaz = "UPDATE spolecne.lieferscheiny "
                + "SET lieferscheiny_fixovany = TRUE "
                + "WHERE lieferscheiny_id = " + liefer.getIdLieferschein();
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TridaObjednavka1 obj = new TridaObjednavka1();
            for (int i = 0; i < arTO1.size(); i++) {
                obj = arTO1.get(i);
                dotaz = "INSERT INTO spolecne.lieferscheiny_polozky_fix( "
                        + "lieferscheiny_polozky_fix_id, lieferscheiny_polozky_fix_polozka, lieferscheiny_polozky_fix_kusu, "
                        + "lieferscheiny_polozky_fix_cislo_vykresu, lieferscheiny_polozky_fix_objednavky_id,  "
                        + "lieferscheiny_polozky_fix_cena_za_kus, lieferscheiny_polozky_fix_poznamky) "
                        + "VALUES (" + liefer.getIdLieferschein() + ", " + (i + 1) + ", " + obj.getPocetObjednanychKusu() + ", "
                        + obj.getIdVykres() + ", " + obj.getId() + ", " + obj.getCenaKus().replaceAll(",", ".") + ", NULL "
                        + ") ";
                int a = PripojeniDB.dotazIUD(dotaz);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zrusitFixaci(TridaLieferschein1 liefer) {
        boolean fixovany = false;
        String dotaz = "SELECT lieferscheiny_fixovany "
                + "FROM spolecne.lieferscheiny "
                + "WHERE lieferscheiny_id = " + liefer.getIdLieferschein();
        try {
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                fixovany = q.getBoolean(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fixovany == true) {
            int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte zrušení fixace",
                    "Opravdu chcete zrušit fixaci ? ", 1);
            if (ud == JOptionPane.YES_OPTION) {
                dotaz = "UPDATE spolecne.lieferscheiny "
                        + "SET lieferscheiny_fixovany = FALSE "
                        + "WHERE lieferscheiny_id = " + liefer.getIdLieferschein();
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dotaz = "DELETE FROM spolecne.lieferscheiny_polozky_fix "
                        + "WHERE lieferscheiny_polozky_fix_id = " + liefer.getIdLieferschein();
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getInfoZaznam() {
        try {

            String posledniZaznam = "";
            ResultSet q = PripojeniDB.dotazS(
                    "SELECT lieferscheiny_cislo_lieferscheinu "
                    + "FROM spolecne.lieferscheiny "
                    + "ORDER BY substring(lieferscheiny_cislo_lieferscheinu from '...$') DESC, substring(lieferscheiny_cislo_lieferscheinu from 1 for 4) DESC  LIMIT 1 ");

            while (q.next()) {
                posledniZaznam = posledniZaznam + q.getString(1);

            }
            PosledniLieferscheinTextField1.setText(posledniZaznam);
        } // konec try
        catch (Exception ex) {
            ex.printStackTrace();
            PripojeniDB.vyjimkaS(ex);
        } // konec catch
    }

    protected class TabulkaModelObjednavka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Pos",
            "Ks",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",
            "Datum objednání",
            "Cena/ks",};

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
                        return (tObj1.getPocetObjednanychKusu() + " ks");

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
                        return (tObj1.getCenaKus());
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

    protected class TabulkaModelLiefer1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo",
            "Datum vystavení",
            "Počet položek",
            "Vystavil",
            "Poznámky",
            "Stav",
            "PDF"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arLief1.size());
            //  updateZaznamyObjednavka1();
            if (arLief1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arLief1.remove(tabulkaHlavni.getSelectedRow());
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
            return arLief1.size();
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
                tLief1 = arLief1.get(row);
                switch (col) {
                    case (0): {
                        return (tLief1.getCisloLieferschein());
                    }
                    case (1): {
                        if (tLief1.getDatumVystaveni() != null) {
                            return (df.format(tLief1.getDatumVystaveni()));
                        } else {
                            return "";
                        }
                    }
                    case (2): {
                        return (tLief1.getPocetPolozek());
                    }
                    case (3): {
                        return (tLief1.getVystavitel());

                    }
                    case (4): {
                        return (tLief1.getPoznamky());

                    }
                    case (5): {
                        return (tLief1.getStavLieferschein());
                    }
                    case (6): {
                        return (tLief1.getPdf());
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
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("FiltrujPruvodky1")
                    || e.getActionCommand().equals("VycistiFiltrObjednavka1") || e.getActionCommand().equals("Hledat")) {
                if (e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                    vycistiFiltrObjednavka1();
                }
                refreshDataHlavni();
                getInfoZaznam();
            }
            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }

            if (e.getActionCommand().equals("TiskFaktura")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    tiskLieferschein(arLief1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("TiskExpedice")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    tiskExpedice(arLief1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("zobrazPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zobrazitLieferschein(arLief1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("FixovatFakturu")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    if (arLief1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {
                        fixovatLieferschein(arLief1.get(tabulkaHlavni.getSelectedRow()));
                    }
                }
            }
            if (e.getActionCommand().equals("zrusitFixaci")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zrusitFixaci(arLief1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("SmazatLiefer")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    smazatLieferschein(arLief1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("UpravitDodaciList")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    if (arLief1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {
                        LieferscheinFrame lf = new LieferscheinFrame(arLief1.get(tabulkaHlavni.getSelectedRow()));
                    } else {
                        JednoducheDialogy1.errAno(new JFrame(), "Chyba při úpravě dodacího listu", "Nelze upravovat fixované dodací listy!");
                    }
                }
            }

            if (e.getActionCommand().equals("ulozPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    lieferscheinUlozeniTemp(arLief1.get(tabulkaHlavni.getSelectedRow()));
                    lieferscheinUploadDB(arLief1.get(tabulkaHlavni.getSelectedRow()));
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
            if (e.getSource() == lsmLieferschein) {
                if (e.getValueIsAdjusting() == false) {
                    if (arLief1.size() > 0) {
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
        jLabel22 = new javax.swing.JLabel();
        PosledniLieferscheinTextField1 = new javax.swing.JTextField();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFCisloLieferschein = new javax.swing.JTextField();
        jTFDatumOd = new javax.swing.JTextField();
        jTFDatumDo = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jBTisk = new javax.swing.JButton();
        jBZobrazPDF = new javax.swing.JButton();
        jBUlozPDF = new javax.swing.JButton();
        jBFixovat = new javax.swing.JButton();
        jBZrusitFixaci = new javax.swing.JButton();
        jBUpravit = new javax.swing.JButton();
        jBSmazat = new javax.swing.JButton();
        jBTiskExpedice = new javax.swing.JButton();
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

        jLabel3.setText("Rok objednání :");
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

        jLabel22.setText("Poslední vystavený lieferschein :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel22, gridBagConstraints);

        PosledniLieferscheinTextField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(PosledniLieferscheinTextField1, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo dodacího listu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel16.setText("Datum vystavení od");
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFCisloLieferschein, gridBagConstraints);
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

        jBTisk.setText("Tisk dodacího listu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBTisk, gridBagConstraints);

        jBZobrazPDF.setText("Zobrazit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazPDF, gridBagConstraints);

        jBUlozPDF.setText("Uložit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUlozPDF, gridBagConstraints);

        jBFixovat.setText("Fixovat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBFixovat, gridBagConstraints);

        jBZrusitFixaci.setText("Zrušit fixaci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZrusitFixaci, gridBagConstraints);

        jBUpravit.setText("Upravit dodací list");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravit, gridBagConstraints);

        jBSmazat.setText("Smazat dodací list");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazat, gridBagConstraints);

        jBTiskExpedice.setText("Tisk expedice");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBTiskExpedice, gridBagConstraints);

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
    private javax.swing.JTextField PosledniLieferscheinTextField1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBFixovat;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisk;
    private javax.swing.JButton jBTiskExpedice;
    private javax.swing.JButton jBUlozPDF;
    private javax.swing.JButton jBUpravit;
    private javax.swing.JButton jBZobrazPDF;
    private javax.swing.JButton jBZrusitFixaci;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPFaktury;
    private javax.swing.JScrollPane jSPObjednavky;
    private javax.swing.JTextField jTFCisloLieferschein;
    private javax.swing.JTextField jTFDatumDo;
    private javax.swing.JTextField jTFDatumOd;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
