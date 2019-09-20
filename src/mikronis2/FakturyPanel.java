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
import java.util.List;
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
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.*;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

public class FakturyPanel extends javax.swing.JPanel {

    protected TableModel tableModelObjednavka1, tableModelFaktura1;
    protected TabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected TabulkaModelFaktura1 tabulkaModelFaktura1;
    protected ListSelectionModel lsmObjednavka1, lsmFaktura1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaObjednavka1 tObj1;
    protected TridaDodatek1 tDod1;
    protected TridaFaktura1 tFak1;
    protected ArrayList<TridaObjednavka1> arTO1;
    protected ArrayList<TridaDodatek1> arDO1;
    protected ArrayList<TridaFaktura1> arFak1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    public FakturyPanel() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuFaktura1();
        nastavTabulkuObjednavka1();

        nastavPosluchaceUdalostiOvladace();

        getInfoZaznam();

        this.setVisible(true);

    }

    protected void nastavParametry() {
        arTO1 = new ArrayList<>();
        tObj1 = new TridaObjednavka1();

        arFak1 = new ArrayList<>();
        tFak1 = new TridaFaktura1();

        arDO1 = new ArrayList<>();
        tDod1 = new TridaDodatek1();

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

        tabulkaModelFaktura1 = new TabulkaModelFaktura1();

        tabulkaHlavni.setModel(tabulkaModelFaktura1);
        tabulkaHlavni.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaHlavni.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmFaktura1 = tabulkaHlavni.getSelectionModel();
        tableModelFaktura1 = tabulkaHlavni.getModel();

        tabulkaHlavni.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmFaktura1.removeListSelectionListener(lslUdalosti);
        tableModelFaktura1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelFaktura1.addTableModelListener(tmlUdalosti);
        lsmFaktura1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jBFixovatFaktura.addActionListener(alUdalosti);
        jBSmazatFaktura.addActionListener(alUdalosti);
        jBZrusitFixaci.addActionListener(alUdalosti);
        jBNovaFaktura.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravitFaktura.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBTiskFaktura.addActionListener(alUdalosti);
        jBUlozPDF.addActionListener(alUdalosti);
        jBZobrazPDF.addActionListener(alUdalosti);
        VyhledatButton1.setActionCommand("Hledat");

        jBTiskFaktura.setActionCommand("TiskFaktura");
        jBUpravitFaktura.setActionCommand("UpravitFakturu");
        jBSmazatFaktura.setActionCommand("SmazatFakturu");
        jBFixovatFaktura.setActionCommand("FixovatFakturu");
        jBZobrazPDF.setActionCommand("zobrazPDF");
        jBUlozPDF.setActionCommand("ulozPDF");
        jBZrusitFixaci.setActionCommand("zrusitFixaci");
        jBNovaFaktura.setActionCommand("pridatFakturu");
        jCBZakaznik.setActionCommand("Hledat");
        jCBRokDodani.setActionCommand("Hledat");

    }

    protected void nastavTabulkuObjednavka1() {
       /* TableColumn column = null;
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

        /*
         * column = tabulka.getColumnModel().getColumn(18);
         * column.setPreferredWidth(110);
         */
        //  zrusPosluchaceUdalostiTabulky();
        if (arFak1.size() > 0) {
            getDataTabulkaObjednavka1();
            tabulkaModelObjednavka1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void nastavTabulkuFaktura1() {
        /*TableColumn column = null;
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
        column.setPreferredWidth(90);

        column = tabulkaHlavni.getColumnModel().getColumn(7);
        column.setPreferredWidth(90);*/

        refreshDataHlavni();

    }// konec nastavTabulkuBT1

    protected void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaFaktura1();
        tabulkaModelFaktura1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arFak1.size() > 0) {
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

        //roletkaModelRoky.setSelectedIndex(roletkaModelRoky.getSize() - 1);
        jCBRokDodani.setModel(roletkaModelRoky);
    }

    protected void getDataTabulkaFaktura1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arFak1.clear();
        tabulkaModelFaktura1.oznamZmenu();

        boolean osetritDatumOd = TextFunkce1.osetriDatum(jTFDatumOd.getText().trim());
        boolean osetritDatumDo = TextFunkce1.osetriDatum(jTFDatumDo.getText().trim());

        try {
            String dotaz
                    = "SELECT * FROM ( "
                    + "SELECT faktury_id, "
                    + "faktura_cislo_faktury, "
                    + "EXTRACT (YEAR FROM faktury_datum_vystaveni) AS rok, "
                    + "CASE when faktura_cislo_faktury like '%/' THEN (substring(faktura_cislo_faktury from 1 for (position('/' in faktura_cislo_faktury)-1)) ) "
                    + "ELSE faktura_cislo_faktury "
                    + "END AS cislo,   "
                    + "faktury_datum_vystaveni,   "
                    + "faktury_datum_plneni, faktury_datum_splatnosti,"
                    + "lieferscheiny_cislo_lieferscheinu,  "
                    + "CASE WHEN cena_celkem IS NULL THEN faktury_cena  "
                    + "ELSE cena_celkem + COALESCE(dodatek_celkem,0) END AS cena,  "
                    + "CASE WHEN pocet_objednavek IS NULL THEN faktury_polozky  "
                    + "ELSE pocet_objednavek END AS pocet,    "
                    + "faktury_fixovana, "
                    + "CASE WHEN faktury_fixovana IS FALSE THEN 'otevřená'  ELSE 'fixovaná' END AS stav, "
                    + "CASE WHEN faktury_bindata IS NULL THEN '' ELSE 'ANO' END AS dataPDF , faktury_zakaznik_id, faktury_lieferschein_nr "
                    + "FROM spolecne.faktury   "
                    + "LEFT JOIN (SELECT lieferscheiny_id, lieferscheiny_cislo_lieferscheinu FROM spolecne.lieferscheiny ) lf "
                    + "ON faktury_lieferschein_nr = lf.lieferscheiny_id   "
                    + "LEFT JOIN (SELECT COUNT(objednavky_id) AS pocet_objednavek, "
                    + "SUM(objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus) AS cena_celkem, "
                    + "vazba_faktury_objednavky_faktury_id "
                    + "FROM spolecne.vazba_faktury_objednavky   "
                    + "CROSS JOIN spolecne.objednavky   "
                    + "WHERE objednavky_id = vazba_faktury_objednavky_objednavky_id "
                    + "GROUP BY vazba_faktury_objednavky_faktury_id) ob "
                    + "ON ob.vazba_faktury_objednavky_faktury_id = faktury_id "
                    + "LEFT JOIN (SELECT COALESCE(SUM(vazba_faktury_dodatky_cena_za_kus * vazba_faktury_dodatky_pocet),0) as dodatek_celkem, vazba_faktury_dodatky_faktury_id "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "GROUP BY vazba_faktury_dodatky_faktury_id) dod "
                    + "ON dod.vazba_faktury_dodatky_faktury_id = faktury_id "
                    + "WHERE faktury_fixovana IS FALSE ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND faktury_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM faktury_datum_vystaveni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            if (osetritDatumOd) {
                dotaz += "AND faktury_datum_vystaveni >= " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " ";
            }
            if (osetritDatumDo) {
                dotaz += "AND faktury_datum_vystaveni <=" + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            dotaz += "GROUP BY faktury_id, faktura_cislo_faktury, faktury_datum_vystaveni,  "
                    + "faktury_datum_plneni, lieferscheiny_cislo_lieferscheinu, faktury_fixovana,  "
                    + "faktury_bindata, cena_celkem, pocet_objednavek, faktury_cena, faktury_polozky, faktury.faktury_zakaznik_id, faktury_lieferschein_nr , dod.dodatek_celkem  "
                    + "ORDER BY rok, cislo ASC ) as f_otevrena  "
                    + "UNION SELECT * FROM ( "
                    + "SELECT faktury_id,  faktura_cislo_faktury,   "
                    + "EXTRACT (YEAR FROM faktury_datum_vystaveni) AS rok, "
                    + "CASE when faktura_cislo_faktury like '%/' THEN (substring(faktura_cislo_faktury from 1 for (position('/' in faktura_cislo_faktury)-1)) ) "
                    + "ELSE faktura_cislo_faktury "
                    + "END AS cislo,   "
                    + "faktury_datum_vystaveni,  faktury_datum_plneni, faktury_datum_splatnosti, "
                    + "lieferscheiny_cislo_lieferscheinu,  "
                    + "CASE WHEN cena_celkem IS NULL THEN faktury_cena  "
                    + "ELSE cena_celkem + COALESCE(dodatek_celkem,0) END AS cena,  "
                    + "CASE WHEN pocet_objednavek IS NULL THEN faktury_polozky  "
                    + "ELSE pocet_objednavek END AS pocet,  "
                    + "faktury_fixovana,   "
                    + "CASE WHEN faktury_fixovana IS FALSE THEN 'otevřená'  ELSE 'fixovaná' END AS stav, "
                    + "CASE WHEN faktury_bindata IS NULL THEN '' ELSE 'ANO' END AS dataPDF, faktury_zakaznik_id, faktury_lieferschein_nr  "
                    + "FROM spolecne.faktury  "
                    + "LEFT JOIN (SELECT lieferscheiny_id, lieferscheiny_cislo_lieferscheinu "
                    + "FROM spolecne.lieferscheiny ) lf "
                    + "ON faktury_lieferschein_nr = lf.lieferscheiny_id    "
                    + "LEFT JOIN (SELECT COUNT(faktury_polozky_fix_polozka) AS pocet_objednavek, "
                    + "SUM(faktury_polozky_fix_kusu * faktury_polozky_fix_cena_za_kus) AS cena_celkem, "
                    + "faktury_polozky_fix_id "
                    + "FROM spolecne.faktury_polozky_fix    "
                    + "GROUP BY faktury_polozky_fix_id) ob "
                    + "ON ob.faktury_polozky_fix_id = faktury_id   "
                    + "LEFT JOIN (SELECT COALESCE(SUM(vazba_faktury_dodatky_cena_za_kus * vazba_faktury_dodatky_pocet),0) as dodatek_celkem, vazba_faktury_dodatky_faktury_id "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "GROUP BY vazba_faktury_dodatky_faktury_id) dod "
                    + "ON dod.vazba_faktury_dodatky_faktury_id = faktury_id "
                    + "WHERE faktury_fixovana IS TRUE ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND faktury_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM faktury_datum_vystaveni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            if (osetritDatumOd) {
                dotaz += "AND faktury_datum_vystaveni >= " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " ";
            }
            if (osetritDatumDo) {
                dotaz += "AND faktury_datum_vystaveni <=" + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            dotaz += "GROUP BY faktury_id, faktura_cislo_faktury, faktury_datum_vystaveni,  "
                    + "faktury_datum_plneni, lieferscheiny_cislo_lieferscheinu, faktury_fixovana, "
                    + "faktury_bindata , cena_celkem, pocet_objednavek , faktury_cena,faktury_polozky, faktury.faktury_zakaznik_id, faktury_lieferschein_nr , dod.dodatek_celkem   "
                    + "ORDER BY rok, cislo ASC ) as f_fixovana  "
                    + "ORDER BY rok, cislo ASC  ";
            //System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tFak1 = new TridaFaktura1();
                tFak1.setIdFaktury(q.getInt(1));
                tFak1.setCisloFaktury((q.getString(2) == null) ? "" : q.getString(2));// cislo faktury
                tFak1.setDatumVystaveni(q.getDate(5));// datum vystaveni
                tFak1.setDatumPlneni(q.getDate(6));// datum plneni
                tFak1.setDatumSplatnosti(q.getDate(7));// datum splatnosti
                tFak1.setCisloLieferschein((q.getString(8) == null) ? "" : q.getString(8)); // lieferschein
                try {
                    tFak1.setCena(nf2.format(nf2.parse(((q.getString(9) == null) ? "" : q.getString(9)).replace(".", ",")))); // celkova suma                                 
                } catch (Exception e) {
                    tFak1.setCena(((q.getString(9) == null) ? "" : q.getString(9)));
                }
                tFak1.setPocetPolozek(q.getInt(10)); // pocet polozek
                tFak1.setStavFaktury((q.getString(12) == null) ? "" : q.getString(12)); // stav
                tFak1.setPdf((q.getString(13) == null) ? "" : q.getString(13)); // pdf
                tFak1.setFixovana(q.getBoolean(11)); // fixovana
                tFak1.setIdZakaznik(q.getInt(14));
                tFak1.setIdLieferschein(q.getInt(15));
                arFak1.add(tFak1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arFak1.size() + "");
        int index[][] = new int[arFak1.size()][tabulkaModelFaktura1.getColumnCount()];
        int index2[][] = new int[arFak1.size()][tabulkaModelFaktura1.getColumnCount()];

        for (int row = 0; row < arFak1.size(); row++) {
            index[row][4] = 1;
            index2[row][4] = 1;
            index2[row][5] = 1;
            index2[row][6] = 2;
            index2[row][7] = 2;
        }

        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelFaktura1.getColumnCount(); i++) {
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
        arDO1.clear();

        tabulkaModelObjednavka1.oznamZmenu();

        try {
            String dotaz = "";
            if (arFak1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {

                pocetKusuObjednavky = 0;

                dotaz = "SELECT objednavky_id, "
                        + "vazba_faktury_objednavky_poradi, "
                        + "objednavky_pocet_objednanych_kusu, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky, "
                        + "objednavky_cena_za_kus, "
                        + "objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus AS cena_celkem, "
                        + "vykresy_id,"
                        + "objednavky_datum_objednani, "
                        + "meny_zkratka "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vazba_faktury_objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.meny "
                        + "WHERE vazba_faktury_objednavky_faktury_id = " + arFak1.get(tabulkaHlavni.getSelectedRow()).getIdFaktury() + " "
                        + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND vazba_faktury_objednavky_objednavky_id = objednavky_id "
                        + "AND meny_id = objednavky_mena_id "
                        + "ORDER BY vazba_faktury_objednavky_poradi ASC, vykresy_je_realny DESC, vykresy_cislo";
            } else {
                dotaz = "SELECT objednavky_id, "
                        + "faktury_polozky_fix_polozka, "
                        + "faktury_polozky_fix_kusu, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky, "
                        + "faktury_polozky_fix_cena_za_kus, "
                        + "faktury_polozky_fix_kusu * faktury_polozky_fix_cena_za_kus AS cena_celkem, "
                        + "vykresy_id, "
                        + "objednavky_datum_objednani, "
                        + "meny_zkratka "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.faktury_polozky_fix "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.meny "
                        + "WHERE faktury_polozky_fix_id = " + arFak1.get(tabulkaHlavni.getSelectedRow()).getIdFaktury() + " "
                        + "AND vykresy.vykresy_id = faktury_polozky_fix_kusu_cislo_vykresu "
                        + "AND faktury_polozky_fix_objednavky_id = objednavky_id "
                        + "AND meny_id = objednavky_mena_id "
                        + "ORDER BY faktury_polozky_fix_polozka ASC, vykresy_je_realny DESC, faktury_polozky_fix_polozka";
            }
            //System.out.println(dotaz);
            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            while (objednavka1.next()) {
                tObj1 = new TridaObjednavka1();
                tObj1.setId(new Long(objednavka1.getLong(1)));
                tObj1.setPoradi(objednavka1.getInt(2));
                tObj1.setPocetObjednanychKusu(objednavka1.getInt(3));
                tObj1.setNazevSoucasti((objednavka1.getString(4) == null) ? "" : objednavka1.getString(4));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(objednavka1.getInt(10));
                tv1.setCislo((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                tv1.setRevize((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                tv1.setNazev((objednavka1.getString(4) == null) ? "" : objednavka1.getString(4));
                tObj1.setTv1(tv1);
                tObj1.setIdVykres(objednavka1.getInt(10));
                tObj1.setCisloObjednavky((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                tObj1.setCenaKus(nf2.format(nf2.parse(objednavka1.getString(8).replace(".", ",")))); // cena
                tObj1.setDatumObjednani(objednavka1.getDate(11));
                tObj1.setPopisMena((objednavka1.getString(12) == null) ? "" : objednavka1.getString(12));
                arTO1.add(tObj1);

            }// konec while

            ResultSet q = PripojeniDB.dotazS("SELECT vazba_faktury_dodatky_faktury_id, vazba_faktury_dodatky_dodatky_id,"
                    + "vazba_faktury_dodatky_text, vazba_faktury_dodatky_pocet, vazba_faktury_dodatky_cena_za_kus, "
                    + "vazba_faktury_dodatky_mena_id, vazba_faktury_dodatky_poznamky, "
                    + "vazba_faktury_dodatky_poradi "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "WHERE vazba_faktury_dodatky_faktury_id = " + arFak1.get(tabulkaHlavni.getSelectedRow()).getIdFaktury() + " "
                    + "ORDER BY vazba_faktury_dodatky_dodatky_id");
            while (q.next()) {
                tDod1 = new TridaDodatek1();
                tDod1.setIdFaktura(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tDod1.setIdDodatek(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tDod1.setText(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                tDod1.setPocet(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                tDod1.setCenaKus(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tDod1.setIdMena(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                tDod1.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                tDod1.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
                arDO1.add(tDod1);
            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        //System.out.println("ARDO : " + arDO1.size());
        int index[][] = new int[arTO1.size() + arDO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index2[][] = new int[arTO1.size() + arDO1.size()][tabulkaModelObjednavka1.getColumnCount()];

        for (int row = 0; row < arTO1.size() + arDO1.size(); row++) {
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
        jTFCisloFaktury.setText("");
    }

    private void tiskFaktura(TridaFaktura1 ft) {
        // int zakaznik_id = ((Integer) ((Vector) vrZakaznik1.get(ZakaznikComboBox1.getSelectedIndex())).get(0)).intValue();

        TridaFaktura1 ftisk = new TridaFaktura1();

        ftisk.selectData(ft.getIdFaktury());
        TridaZakaznik1 zakaznikTisknout = new TridaZakaznik1(ftisk.getIdZakaznik());
        // Vector fakturaDetail = new Vector();
        int pocetPolozek = 0;
        String lieferscheinDatum = "";

        try {
            ResultSet lieferDatum = PripojeniDB.dotazS("SELECT to_char(lieferscheiny_datum_vystaveni,'DD.MM.YYYY') "
                    + "FROM spolecne.lieferscheiny "
                    + "CROSS JOIN spolecne.faktury "
                    + "WHERE lieferscheiny_id = faktury_lieferschein_nr "
                    + "AND faktury_id = " + ftisk.getIdFaktury());
            while (lieferDatum.next()) {
                lieferscheinDatum = lieferDatum.getString(1);
            }
            String dotaz = "";
            if (ftisk.isFixovana()) {
                dotaz = "SELECT COUNT(faktury_polozky_fix_polozka) "
                        + "FROM spolecne.faktury_polozky_fix WHERE faktury_polozky_fix_id = " + ftisk.getIdFaktury();
            } else {
                dotaz = "SELECT COUNT(vazba_faktury_objednavky_objednavky_id) "
                        + "FROM spolecne.vazba_faktury_objednavky WHERE vazba_faktury_objednavky_faktury_id = " + ftisk.getIdFaktury();
            }
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                pocetPolozek = q.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reportSource = "";

        if (ftisk.getJazyk().isEmpty()) {
            if (zakaznikTisknout.getIdStat() == 2 || zakaznikTisknout.getIdStat() == 3 || zakaznikTisknout.getIdStat() == 6) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaDE.jrxml";
            } else if (zakaznikTisknout.getIdStat() == 4 || zakaznikTisknout.getIdStat() == 7 || zakaznikTisknout.getIdStat() == 8) {
                if (zakaznikTisknout.getIdZakaznik() == 12) {
                    if (ftisk.isDeklarovana()) {
                        reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENDeklarace.jrxml";
                    } else {
                        reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaEN.jrxml";
                    }
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENUni.jrxml";
                }
            } else { //if (zakaznikTisknout.getIdStat() == 1) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaCZ.jrxml";
            }
        } else {
            if (ftisk.getJazyk().equals("CZ")) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaCZ.jrxml";
            } else if (ftisk.getJazyk().equals("ENBBH")) {
                if (ftisk.isDeklarovana()) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENDeklarace.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaEN.jrxml";
                }
            } else if (ftisk.getJazyk().equals("EN")) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENUni.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaDE.jrxml";
            }
        }

        String kontakt = "";
        String dodAdresa1 = "";
        String dodAdresa2 = "";
        String dodAdresa3 = "";

        if (zakaznikTisknout.getIdZakaznik() == 12) {
            try {
                ResultSet qa = PripojeniDB.dotazS(
                        "SELECT COALESCE(lieferscheiny_dodaci_adresa,-1) "
                        + "FROM spolecne.lieferscheiny "
                        + "WHERE lieferscheiny_id = " + ftisk.getIdLieferschein());
                while (qa.next()) {
                    if (qa.getInt(1) > 0) {
                        // System.out.println("dod adresa : " + qa.getInt(1));
                        ResultSet q = PripojeniDB.dotazS("SELECT "
                                + "dodaci_adresy_popis, dodaci_adresy_adresa, "
                                + "dodaci_adresy_mesto, dodaci_adresy_psc, "
                                + "staty_nazev, dodaci_adresy_popis "
                                + "FROM spolecne.dodaci_adresy "
                                + "CROSS JOIN spolecne.staty "
                                + "WHERE staty_id = dodaci_adresy_stat_id "
                                + "AND dodaci_adresy_id = " + qa.getInt(1));
                        while (q.next()) {

                            dodAdresa1 = ((q.getString(2) == null) ? "" : q.getString(2));
                            dodAdresa2 = ((q.getString(3) == null) ? "" : q.getString(3))
                                    + ", " + ((q.getString(4) == null) ? "" : q.getString(4));
                            dodAdresa3 = ((q.getString(5) == null) ? "" : q.getString(5));
                            kontakt = ((q.getString(6) == null) ? "" : q.getString(6));
                        }
                    } else {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cislo_faktury", ftisk.getCisloFaktury());
        params.put("dic", new String("CZ45348693"));
        params.put("datum_vystaveni", df.format(ftisk.getDatumVystaveni()));
        params.put("datum_plneni", df.format(ftisk.getDatumPlneni())); // upravit
        params.put("datum_splatnost", df.format(ftisk.getDatumSplatnosti()));
        params.put("bankovni_ucet", zakaznikTisknout.getMikronBank());
        params.put("iban", zakaznikTisknout.getMikronIban());
        if (zakaznikTisknout.getIdStat() == 2 || zakaznikTisknout.getIdStat() == 3 || zakaznikTisknout.getIdStat() == 6) {
            params.put("lieferschein", "Lieferschein Nr.: " + ftisk.getCisloLieferschein() + " vom " + lieferscheinDatum);
        } else {
            params.put("lieferschein", ftisk.getCisloLieferschein());
        }
        params.put("fakturu_vystavil", ftisk.getVystavitel());
        params.put("pocetPolozek", pocetPolozek);
        if (ftisk.getAdresa().isEmpty()) {
            params.put("adresa_ulice", zakaznikTisknout.getAdresa());
        } else {
            params.put("adresa_ulice", ftisk.getAdresa());
        }
        if (ftisk.getMesto().isEmpty() || ftisk.getPsc().isEmpty()) {
            params.put("adresa_psc_mesto", zakaznikTisknout.getPsc() + " " + zakaznikTisknout.getMesto());
        } else {
            params.put("adresa_psc_mesto", ftisk.getPsc() + " " + ftisk.getMesto());
        }
        if (ftisk.getNazev1Radek().isEmpty()) {
            if (ftisk.getZakaznikJmenoFirmy().isEmpty() == false) {
                params.put("firma", ftisk.getZakaznikJmenoFirmy());
            } else {
                if (zakaznikTisknout.getPrvniRadek().isEmpty() == false) {
                    params.put("firma", zakaznikTisknout.getPrvniRadek());
                } else {
                    params.put("firma", zakaznikTisknout.getNazev());
                }
            }
        } else {
            params.put("firma", ftisk.getNazev1Radek());
        }
        if (ftisk.getNazev2Radek().isEmpty()) {
            if (zakaznikTisknout.getDruhyRadek().isEmpty() == false) {
                params.put("firmaPokr", zakaznikTisknout.getDruhyRadek());
            }
        } else {
            params.put("firmaPokr", ftisk.getNazev2Radek());
        }
        if (ftisk.getStat().isEmpty()) {
            params.put("adresa_stat", SQLFunkceObecne2.selectStringPole("SELECT staty_nazev FROM spolecne.staty  WHERE staty_id = " + zakaznikTisknout.getIdStat()));
        } else {
            params.put("adresa_stat", ftisk.getStat());
        }
        if ((zakaznikTisknout.getIdZakaznik() == 6) || (zakaznikTisknout.getIdZakaznik() == 5)) {
            params.put("telVat", "VAT-No.:");
            params.put("telefonni_cislo", (String) zakaznikTisknout.getVatNr());
        } else {
            params.put("telVat", "Tel.:");
            //params.put("telefonni_cislo", (String) zakaznikTisknout.getTelefony());
            params.put("telefonni_cislo", (ftisk.getKontaktTelefon().isEmpty() ? (String) zakaznikTisknout.getTelefony() : ftisk.getKontaktTelefon()));
        }

        //params.put("telefonni_cislo", (String) zakaznikTisknout.get(8));
        if (zakaznikTisknout.getIdZakaznik() != 12) {
            params.put("ust_id_nr", (String) zakaznikTisknout.getUstNr());
        }

        params.put("celkova_cena", ftisk.getCena());
        params.put("kontakt", ((ftisk.getKontakt().isEmpty()) ? kontakt : ftisk.getKontakt()));
        if (zakaznikTisknout.getIdZakaznik() == 12) {
            // System.out.println("Dodadresa : " + (ftisk.getAdresaFakturace().isEmpty()) + ", " + ftisk.getAdresaFakturace());
            params.put("dodAdresa1", ((ftisk.getAdresaFakturace().isEmpty()) ? dodAdresa1 : ftisk.getAdresaFakturace()));
            params.put("dodAdresa2", ((ftisk.getMestoFakturace().isEmpty()) ? dodAdresa2 : ftisk.getPscFakturace() + ", " + ftisk.getMestoFakturace()));
            params.put("dodAdresa3", ((ftisk.getStatFakturace().isEmpty()) ? dodAdresa3 : ftisk.getStatFakturace()));
            //params.put("kontakt", ((ftisk.getKontakt().isEmpty()) ? kontakt : ftisk.getKontakt()));
            //  System.out.println("adreas : " + dodAdresa1 + " x " + dodAdresa2 + " x " + dodAdresa3 + " x " + kontakt);
            params.put("nakl_list", ftisk.getCisloAwb());
            params.put("pocet_kusu", ftisk.getPocetKusu());
            params.put("hruba_vaha", ((ftisk.getHrubaVaha() == null) ? "---" : ftisk.getHrubaVaha()) + " kg");
            params.put("cista_vaha", ((ftisk.getCistaVaha() == null) ? "---" : ftisk.getCistaVaha()) + " kg");
            params.put("dopravce", ftisk.getDopravce());
            params.put("com_code", ((ftisk.getComCode() == null) ? "---" : ftisk.getComCode()));

        }

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

        params.put("id_faktura", ftisk.getIdFaktury());
        params.put("zakaznik_id", ftisk.getIdZakaznik());
        params.put("mena", arTO1.get(0).getPopisMena());
        if (ftisk.getVatNr().isEmpty()) {
            params.put("vatId", zakaznikTisknout.getVatNr());
        } else {
            params.put("vatId", ftisk.getVatNr());
        }
        params.put("paymentMethod", "NET30");

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            //  JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void zobrazitFaktura(TridaFaktura1 faktura) {
        String nazevSouboru = "faktura.pdf";
        try {
            ResultSet soubor = PripojeniDB.dotazS("SELECT faktury_bindata "
                    + "FROM spolecne.faktury "
                    + "WHERE faktury_id = " + faktura.getIdFaktury());
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

    private void fixovatFakturu(TridaFaktura1 faktura) {
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        String dotaz = "UPDATE spolecne.faktury "
                + "SET faktury_fixovana = TRUE "
                + "WHERE faktury_id = " + faktura.getIdFaktury();
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TridaObjednavka1 obj = new TridaObjednavka1();
            for (int i = 0; i < arTO1.size(); i++) {
                obj = arTO1.get(i);

                dotaz = "INSERT INTO spolecne.faktury_polozky_fix( "
                        + "faktury_polozky_fix_id, faktury_polozky_fix_polozka, faktury_polozky_fix_kusu, "
                        + "faktury_polozky_fix_kusu_cislo_vykresu, faktury_polozky_fix_objednavky_id,  "
                        + "faktury_polozky_fix_cena_za_kus, faktury_polozky_fix_poznamky, "
                        + "faktury_polozky_fix_poradi, faktury_polozky_fix_cislo_vykresu, "
                        + "faktury_polozky_fix_revize_vykresu, faktury_polozky_fix_nazev_soucasti, "
                        + "faktury_polozky_fix_datum_objednani, faktury_polozky_fix_cislo_objednavky) "
                        + "VALUES (" + faktura.getIdFaktury() + ", " + (i + 1) + ", " + obj.getPocetObjednanychKusu() + ", "
                        + obj.getIdVykres() + ", " + obj.getId() + ", " + nf2.parse(obj.getCenaKus().replaceAll(" ", "")) + ", NULL, "
                        + obj.getPoradi() + ", " + TextFunkce1.osetriZapisTextDB1(obj.getTv1().getCislo()) + ", "
                        + TextFunkce1.osetriZapisTextDB1(obj.getTv1().getRevize()) + ", "
                        + TextFunkce1.osetriZapisTextDB1(obj.getTv1().getNazev()) + ", "
                        + TextFunkce1.osetriZapisDatumDB1(obj.getDatumObjednani()) + ", "
                        + TextFunkce1.osetriZapisTextDB1(obj.getCisloObjednavky()) + ") ";
                // System.out.println(dotaz);
                int a = PripojeniDB.dotazIUD(dotaz);
            }

            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        }
    }

    private void zrusitFixaci(TridaFaktura1 faktura) {
        if (faktura.isFixovana() == true) {
            int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte zrušení fixace",
                    "Opravdu chcete zrušit fixaci ? ", 1);
            if (ud == JOptionPane.YES_OPTION) {
                try {
                    int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
                    String dotaz = "UPDATE spolecne.faktury "
                            + "SET faktury_fixovana = FALSE "
                            + "WHERE faktury_id = " + faktura.getIdFaktury();
                    int a = PripojeniDB.dotazIUD(dotaz);

                    dotaz = "DELETE FROM spolecne.faktury_polozky_fix "
                            + "WHERE faktury_polozky_fix_id = " + faktura.getIdFaktury();
                    a = PripojeniDB.dotazIUD(dotaz);
                    rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
                } catch (Exception e) {
                    int rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                    e.printStackTrace();
                }
            }
        }
    }

    private void smazatFakturu(TridaFaktura1 faktura) {
        int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání faktury",
                "Opravdu chcete smazat fakturu ? ", 1);
        if (ud == JOptionPane.YES_OPTION) {
            try {
                int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
                String dotaz = "DELETE FROM spolecne.vazba_faktury_objednavky WHERE "
                        + "vazba_faktury_objednavky_faktury_id = " + faktura.getIdFaktury();
                int a = PripojeniDB.dotazIUD(dotaz);
                dotaz = "DELETE FROM spolecne.vazba_faktury_dodatky WHERE "
                        + "vazba_faktury_dodatky_faktury_id = " + faktura.getIdFaktury();
                a = PripojeniDB.dotazIUD(dotaz);
                dotaz = "DELETE FROM spolecne.faktury WHERE "
                        + "faktury_id = " + faktura.getIdFaktury();
                a = PripojeniDB.dotazIUD(dotaz);
                rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            } catch (Exception e) {
                int rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
            }
            zrusPosluchaceUdalostiTabulky();
            getDataTabulkaFaktura1();
            nastavPosluchaceUdalostiTabulky();
        }
    }

    private void fakturaUlozeniTemp(TridaFaktura1 ft) {
        // int zakaznik_id = ((Integer) ((Vector) vrZakaznik1.get(ZakaznikComboBox1.getSelectedIndex())).get(0)).intValue();
        TridaFaktura1 ftisk = new TridaFaktura1();

        ftisk.selectData(ft.getIdFaktury());
        TridaZakaznik1 zakaznikTisknout = new TridaZakaznik1(ftisk.getIdZakaznik());
        // Vector fakturaDetail = new Vector();
        int pocetPolozek = 0;
        String lieferscheinDatum = "";

        try {
            ResultSet lieferDatum = PripojeniDB.dotazS("SELECT to_char(lieferscheiny_datum_vystaveni,'DD.MM.YYYY') "
                    + "FROM spolecne.lieferscheiny "
                    + "CROSS JOIN spolecne.faktury "
                    + "WHERE lieferscheiny_id = faktury_lieferschein_nr "
                    + "AND faktury_id = " + ftisk.getIdFaktury());
            while (lieferDatum.next()) {
                lieferscheinDatum = lieferDatum.getString(1);
            }
            String dotaz = "";
            if (ftisk.isFixovana()) {
                dotaz = "SELECT COUNT(faktury_polozky_fix_polozka) "
                        + "FROM spolecne.faktury_polozky_fix WHERE faktury_polozky_fix_id = " + ftisk.getIdFaktury();
            } else {
                dotaz = "SELECT COUNT(vazba_faktury_objednavky_objednavky_id) "
                        + "FROM spolecne.vazba_faktury_objednavky WHERE vazba_faktury_objednavky_faktury_id = " + ftisk.getIdFaktury();
            }
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                pocetPolozek = q.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reportSource = "";

        if (zakaznikTisknout.getIdStat() == 2 || zakaznikTisknout.getIdStat() == 3 || zakaznikTisknout.getIdStat() == 6) {
            if (ftisk.isFixovana()) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaDEFix.jrxml";
            } else {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaDE.jrxml";
            }

        } else if (zakaznikTisknout.getIdStat() == 4 || zakaznikTisknout.getIdStat() == 7  || zakaznikTisknout.getIdStat() == 8) {
            if (zakaznikTisknout.getIdZakaznik() == 12) {
                if (ftisk.isDeklarovana()) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENDeklarace.jrxml";
                } else {
                    if (ftisk.isFixovana()) {
                        reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENFix.jrxml";
                    } else {
                        reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaEN.jrxml";
                    }
                }
            } else {
                if (ftisk.isFixovana()) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENUniFix.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENUni.jrxml";
                }
            }
        } else if (zakaznikTisknout.getIdStat() == 1) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaCZ.jrxml";
        }

        String kontakt = "";
        String dodAdresa1 = "";
        String dodAdresa2 = "";
        String dodAdresa3 = "";

        if (zakaznikTisknout.getIdZakaznik() == 12) {
            try {
                ResultSet qa = PripojeniDB.dotazS(
                        "SELECT COALESCE(lieferscheiny_dodaci_adresa,-1) "
                        + "FROM spolecne.lieferscheiny "
                        + "WHERE lieferscheiny_id = " + ftisk.getIdLieferschein());
                while (qa.next()) {
                    if (qa.getInt(1) > 0) {
                        // System.out.println("dod adresa : " + qa.getInt(1));
                        ResultSet q = PripojeniDB.dotazS("SELECT "
                                + "dodaci_adresy_popis, dodaci_adresy_adresa, "
                                + "dodaci_adresy_mesto, dodaci_adresy_psc, "
                                + "staty_nazev, dodaci_adresy_popis "
                                + "FROM spolecne.dodaci_adresy "
                                + "CROSS JOIN spolecne.staty "
                                + "WHERE staty_id = dodaci_adresy_stat_id "
                                + "AND dodaci_adresy_id = " + qa.getInt(1));
                        while (q.next()) {

                            dodAdresa1 = ((q.getString(2) == null) ? "" : q.getString(2));
                            dodAdresa2 = ((q.getString(3) == null) ? "" : q.getString(3))
                                    + ", " + ((q.getString(4) == null) ? "" : q.getString(4));
                            dodAdresa3 = ((q.getString(5) == null) ? "" : q.getString(5));
                            kontakt = ((q.getString(6) == null) ? "" : q.getString(6));
                        }
                    } else {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cislo_faktury", ftisk.getCisloFaktury());
        params.put("dic", new String("CZ45348693"));
        params.put("datum_vystaveni", df.format(ftisk.getDatumVystaveni()));
        params.put("datum_plneni", df.format(ftisk.getDatumPlneni())); // upravit
        params.put("datum_splatnost", df.format(ftisk.getDatumSplatnosti()));
        params.put("bankovni_ucet", zakaznikTisknout.getMikronBank());
        params.put("iban", zakaznikTisknout.getMikronIban());
        if (zakaznikTisknout.getIdStat() == 2 || zakaznikTisknout.getIdStat() == 3 || zakaznikTisknout.getIdStat() == 6) {
            params.put("lieferschein", "Lieferschein Nr.: " + ftisk.getCisloLieferschein() + " vom " + lieferscheinDatum);
        } else {
            params.put("lieferschein", ftisk.getCisloLieferschein());
        }
        params.put("fakturu_vystavil", ftisk.getVystavitel());
        params.put("pocetPolozek", pocetPolozek);
        params.put("adresa_ulice", zakaznikTisknout.getAdresa());
        params.put("adresa_psc_mesto", zakaznikTisknout.getPsc() + " " + zakaznikTisknout.getMesto());
        if (ftisk.getNazev1Radek().isEmpty()) {
            if (ftisk.getZakaznikJmenoFirmy().isEmpty() == false) {
                params.put("firma", ftisk.getZakaznikJmenoFirmy());
            } else {
                if (zakaznikTisknout.getPrvniRadek().isEmpty() == false) {
                    params.put("firma", zakaznikTisknout.getPrvniRadek());
                } else {
                    params.put("firma", zakaznikTisknout.getNazev());
                }
            }
        } else {
            params.put("firma", ftisk.getNazev1Radek());
        }
        if (ftisk.getNazev2Radek().isEmpty()) {
            if (zakaznikTisknout.getDruhyRadek().isEmpty() == false) {
                params.put("firmaPokr", zakaznikTisknout.getDruhyRadek());
            }
        } else {
            params.put("firmaPokr", ftisk.getNazev2Radek());
        }
        params.put("adresa_stat", ftisk.getStat());
        if ((zakaznikTisknout.getIdZakaznik() == 6) || (zakaznikTisknout.getIdZakaznik() == 5)) {
            params.put("telVat", "VAT-No.:");
            params.put("telefonni_cislo", (String) zakaznikTisknout.getVatNr());
        } else {
            params.put("telVat", "Tel.:");
            params.put("telefonni_cislo", (String) zakaznikTisknout.getTelefony());
        }

        //params.put("telefonni_cislo", (String) zakaznikTisknout.get(8));
        if (zakaznikTisknout.getIdZakaznik() != 12) {
            params.put("ust_id_nr", (String) zakaznikTisknout.getUstNr());
        }

        params.put("celkova_cena", ftisk.getCena());
         params.put("kontakt", ((ftisk.getKontakt().isEmpty()) ? kontakt : ftisk.getKontakt()));
        if (zakaznikTisknout.getIdZakaznik() == 12) {
            // System.out.println("Dodadresa : " + (ftisk.getAdresaFakturace().isEmpty()) + ", " + ftisk.getAdresaFakturace());
            params.put("dodAdresa1", ((ftisk.getAdresaFakturace().isEmpty()) ? dodAdresa1 : ftisk.getAdresaFakturace()));
            params.put("dodAdresa2", ((ftisk.getMestoFakturace().isEmpty()) ? dodAdresa2 : ftisk.getPscFakturace() + ", " + ftisk.getMestoFakturace()));
            params.put("dodAdresa3", ((ftisk.getStatFakturace().isEmpty()) ? dodAdresa3 : ftisk.getStatFakturace()));
           
            //  System.out.println("adreas : " + dodAdresa1 + " x " + dodAdresa2 + " x " + dodAdresa3 + " x " + kontakt);
            params.put("nakl_list", ftisk.getCisloAwb());
            params.put("pocet_kusu", ftisk.getPocetKusu());
            params.put("hruba_vaha", ((ftisk.getHrubaVaha() == null) ? "---" : ftisk.getHrubaVaha()) + " kg");
            params.put("cista_vaha", ((ftisk.getCistaVaha() == null) ? "---" : ftisk.getCistaVaha()) + " kg");
            params.put("dopravce", ftisk.getDopravce());
            params.put("com_code", ((ftisk.getComCode() == null) ? "---" : ftisk.getComCode()));

        }

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

        params.put("id_faktura", ftisk.getIdFaktury());
        params.put("zakaznik_id", ftisk.getIdZakaznik());
        params.put("mena", arTO1.get(0).getPopisMena());

        if (ftisk.getVatNr().isEmpty()) {
            params.put("vatId", zakaznikTisknout.getVatNr());
        } else {
            params.put("vatId", ftisk.getVatNr());
        }

        params.put("paymentMethod", "NET30");

        try {
           
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);

            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "doklad.pdf");

            //JasperViewer.viewReport(jasperPrint1, false);
            //JasperPrintManager.printReport(jasperPrint1, true);
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void fakturaUploadDB(TridaFaktura1 faktura) {
        // System.out.println("Updatuji doklad " + dokladCislo);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "doklad.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.faktury SET faktury_bindata = ? WHERE faktury_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, faktura.getIdFaktury());
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getInfoZaznam() {
        try {
            String posledniZaznam = "";
            /* ResultSet q = PripojeniDB.dotazS(
             "SELECT *  FROM (SELECT faktura_cislo_faktury "
             + "FROM spolecne.faktury "
             + "CROSS JOIN spolecne.subjekty_trhu "
             + "CROSS JOIN spolecne.staty "
             + "WHERE subjekty_trhu_id = faktury_zakaznik_id "
             + "AND staty_id = subjekty_trhu_stat_id "
             + "AND staty_tuzemsko = FALSE "
             + "ORDER BY substring(faktura_cislo_faktury from '...$') DESC, substring(faktura_cislo_faktury from 1 for 3) DESC LIMIT 1) AS x "
             + "UNION SELECT ' - ' UNION "
             + "SELECT * FROM (SELECT faktura_cislo_faktury "
             + "FROM spolecne.faktury "
             + "CROSS JOIN spolecne.subjekty_trhu "
             + "CROSS JOIN spolecne.staty "
             + "WHERE subjekty_trhu_id = faktury_zakaznik_id "
             + "AND staty_id = subjekty_trhu_stat_id "
             + "AND staty_tuzemsko = TRUE "
             + "ORDER BY substring(faktura_cislo_faktury from '...$') DESC, substring(faktura_cislo_faktury from 1 for 3) DESC LIMIT 1) AS y");
             while (q.next()) {
             posledniZaznam = posledniZaznam + q.getString(1);

             }*/
            ResultSet q = PripojeniDB.dotazS(
                    "SELECT faktura_cislo_faktury "
                    + "FROM spolecne.faktury "
                    + "CROSS JOIN spolecne.subjekty_trhu "
                    + "CROSS JOIN spolecne.staty "
                    + "WHERE subjekty_trhu_id = faktury_zakaznik_id "
                    + "AND staty_id = subjekty_trhu_stat_id "
                    + "AND staty_tuzemsko = FALSE "
                    + "ORDER BY substring(faktura_cislo_faktury from '...$') DESC, substring(faktura_cislo_faktury from 1 for 3) DESC LIMIT 1 ");
            while (q.next()) {
                posledniZaznam = posledniZaznam + q.getString(1);

            }
            posledniZaznam += " - ";
            ResultSet r = PripojeniDB.dotazS(
                    "SELECT faktura_cislo_faktury "
                    + "FROM spolecne.faktury "
                    + "CROSS JOIN spolecne.subjekty_trhu "
                    + "CROSS JOIN spolecne.staty "
                    + "WHERE subjekty_trhu_id = faktury_zakaznik_id "
                    + "AND staty_id = subjekty_trhu_stat_id "
                    + "AND staty_tuzemsko = TRUE "
                    + "ORDER BY faktury_datum_vystaveni DESC LIMIT 1");
            while (r.next()) {
                posledniZaznam = posledniZaznam + r.getString(1);

            }
            PosledniFakturaTextField1.setText(posledniZaznam);
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
            return arTO1.size() + arDO1.size();
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
            // System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaDetail.getSelectedRow());
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
                if (row < arTO1.size()) {
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
                } else {
                    tDod1 = arDO1.get(row - arTO1.size());
                    switch (col) {
                        case (0): {
                            return (tDod1.getPoradi());
                        }
                        case (1): {
                            return (tDod1.getPocet());
                        }
                        case (2): {
                            return (tDod1.getText());
                        }
                        case (3): {
                            return ("");
                        }
                        case (4): {
                            return ("");
                        }
                        case (5): {
                            return (null);
                        }
                        case (6): {
                            return ("");
                        }
                        case (7): {
                            return (tDod1.getCenaKus());
                        }
                        default: {
                            return null;
                        }
                    }
                }
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

    protected class TabulkaModelFaktura1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo faktury",
            "D. Vystavení",
            "D. Plnění",
            "D. Splatnosti",
            "Lieferschein",
            "Celková hodnota",
            "Počet položek",
            "Stav",
            "PDF"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arFak1.size());
            //  updateZaznamyObjednavka1();
            if (arFak1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arFak1.remove(tabulkaHlavni.getSelectedRow());
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
            return arFak1.size();
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
                tFak1 = arFak1.get(row);
                switch (col) {
                    case (0): {
                        return (tFak1.getCisloFaktury());
                    }
                    case (1): {
                        if (tFak1.getDatumVystaveni() != null) {
                            return (df.format(tFak1.getDatumVystaveni()));
                        } else {
                            return "";
                        }
                    }
                    case (2): {
                        if (tFak1.getDatumPlneni() != null) {
                            return (df.format(tFak1.getDatumPlneni()));
                        } else {
                            return "";
                        }

                    }
                    case (3): {
                        if (tFak1.getDatumSplatnosti()!= null) {
                            return (df.format(tFak1.getDatumSplatnosti()));
                        } else {
                            return "";
                        }

                    }
                    case (4): {
                        return (tFak1.getCisloLieferschein());

                    }
                    case (5): {
                        return (tFak1.getCena());

                    }
                    case (6): {
                        return (tFak1.getPocetPolozek());
                    }
                    case (7): {
                        return (tFak1.getStavFaktury());
                    }
                    case (8): {
                        return (tFak1.getPdf());
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
                    tiskFaktura(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("zobrazPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zobrazitFaktura(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("FixovatFakturu")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    if (arFak1.get(tabulkaHlavni.getSelectedRow()).isFixovana() == false) {
                        fixovatFakturu(arFak1.get(tabulkaHlavni.getSelectedRow()));
                    }
                }
            }
            if (e.getActionCommand().equals("zrusitFixaci")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zrusitFixaci(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("SmazatFakturu")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    smazatFakturu(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("ulozPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    fakturaUlozeniTemp(arFak1.get(tabulkaHlavni.getSelectedRow()));
                    fakturaUploadDB(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("UpravitFakturu")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    FakturaFrame1 upravaFaktury = new FakturaFrame1(arFak1.get(tabulkaHlavni.getSelectedRow()));
                }
            }

            /*
             * if (e.getActionCommand().equals("ExportVybraneBT1")) {
             * exportujVybraneBT1(); }
             */
        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {
            //System.out.println("Objednavky Psdfsdfanel 2");

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
                    if (arFak1.size() > 0) {
                        nastavTabulkuObjednavka1();
                    }
                }
            }//konec if (getSource ...)

        }//konec valueChanged
    } //konec LSLUdalosti

    class FLUdalosti implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            //System.out.println("vybrana radkasss focus");
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
        PosledniFakturaTextField1 = new javax.swing.JTextField();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFCisloFaktury = new javax.swing.JTextField();
        jTFDatumOd = new javax.swing.JTextField();
        jTFDatumDo = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jBTiskFaktura = new javax.swing.JButton();
        jBZobrazPDF = new javax.swing.JButton();
        jBUlozPDF = new javax.swing.JButton();
        jBFixovatFaktura = new javax.swing.JButton();
        jBZrusitFixaci = new javax.swing.JButton();
        jBUpravitFaktura = new javax.swing.JButton();
        jBSmazatFaktura = new javax.swing.JButton();
        jBNovaFaktura = new javax.swing.JButton();
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

        jLabel22.setText("Poslední vystavená faktura :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel22, gridBagConstraints);

        PosledniFakturaTextField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(PosledniFakturaTextField1, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo faktury :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel16.setText("Fakturováno od");
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
        jPFiltry.add(jTFCisloFaktury, gridBagConstraints);
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

        jBTiskFaktura.setText("Tisk faktury");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBTiskFaktura, gridBagConstraints);

        jBZobrazPDF.setText("Zobrazit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazPDF, gridBagConstraints);

        jBUlozPDF.setText("Uložit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUlozPDF, gridBagConstraints);

        jBFixovatFaktura.setText("Fixovat fakturu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBFixovatFaktura, gridBagConstraints);

        jBZrusitFixaci.setText("Zrušit fixaci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZrusitFixaci, gridBagConstraints);

        jBUpravitFaktura.setText("Upravit fakturu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravitFaktura, gridBagConstraints);

        jBSmazatFaktura.setText("Smazat fakturu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazatFaktura, gridBagConstraints);

        jBNovaFaktura.setText("Přidat fakturu bez objednávek");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaFaktura, gridBagConstraints);

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
    private javax.swing.JTextField PosledniFakturaTextField1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBFixovatFaktura;
    private javax.swing.JButton jBNovaFaktura;
    private javax.swing.JButton jBSmazatFaktura;
    private javax.swing.JButton jBTiskFaktura;
    private javax.swing.JButton jBUlozPDF;
    private javax.swing.JButton jBUpravitFaktura;
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
    private javax.swing.JTextField jTFCisloFaktury;
    private javax.swing.JTextField jTFDatumDo;
    private javax.swing.JTextField jTFDatumOd;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
