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
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
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
public class SeznamNabidekPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelDetail1, tableModelHlavni1;
    protected TabulkaModelDetail tabulkaModelDetail1;
    protected TabulkaModelHlavni tabulkaModelHlavni1;
    protected ListSelectionModel lsmDetail1, lsmHlavni1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaNabidka1 tNab1;
    protected TridaSeznamNabidek1 tSeznam1;
    protected ArrayList<TridaNabidka1> arTN1;
    protected ArrayList<TridaSeznamNabidek1> arSeznam1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public SeznamNabidekPanel1() {
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
        arTN1 = new ArrayList<TridaNabidka1>();
        tNab1 = new TridaNabidka1();

        arSeznam1 = new ArrayList<TridaSeznamNabidek1>();
        tSeznam1 = new TridaSeznamNabidek1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

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

        tabulkaHlavni.setPreferredScrollableViewportSize(new Dimension(500, 300));
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
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravit.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        jBVycistitFiltr.setActionCommand("VycistiFiltrObjednavka1");
        jBTisk.addActionListener(alUdalosti);
        jBZobrazPDF.addActionListener(alUdalosti);
        jBSmazat.addActionListener(alUdalosti);
        VyhledatButton1.setActionCommand("Hledat");
        jBTisk.setActionCommand("Tisk");
        jBUpravit.setActionCommand("Upravit");
        jBSmazat.setActionCommand("Smazat");
        jBZobrazPDF.setActionCommand("zobrazPDF");
        jCBZakaznik.setActionCommand("Hledat");
        jCBRokDodani.setActionCommand("Hledat");

    }

    protected void nastavTabulkuDetail1() {
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
        column.setPreferredWidth(60);

        column = tabulkaDetail.getColumnModel().getColumn(7);
        column.setPreferredWidth(120);

        column = tabulkaDetail.getColumnModel().getColumn(8);
        column.setPreferredWidth(100);

        column = tabulkaDetail.getColumnModel().getColumn(9);
        column.setPreferredWidth(30);

        column = tabulkaDetail.getColumnModel().getColumn(10);
        column.setPreferredWidth(115);

        column = tabulkaDetail.getColumnModel().getColumn(11);
        column.setPreferredWidth(40);*/

        //  zrusPosluchaceUdalostiTabulky();
        if (arSeznam1.size() > 0) {
            getDataTabulkaDetail1();
            tabulkaModelDetail1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void nastavTabulkuHlavni1() {
      /*  TableColumn column = null;
        column = tabulkaHlavni.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);

        column = tabulkaHlavni.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulkaHlavni.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulkaHlavni.getColumnModel().getColumn(3);
        column.setPreferredWidth(90);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

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

    protected void getDataTabulkaHlavni1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arSeznam1.clear();
        tabulkaModelHlavni1.oznamZmenu();

        try {
            String dotaz
                    = "SELECT seznam_nabidek_id, seznam_nabidek_datum, "
                    + "seznam_nabidek_komu, x.pocet,  "
                    + "CASE WHEN seznam_nabidek_bindata IS NULL THEN '' ELSE 'ANO' END AS dataPDF, "
                    + "seznam_nabidek_predmet, seznam_nabidek_veta, seznam_nabidek_vystavil  "
                    + " FROM spolecne.seznam_nabidek "
                    + " LEFT JOIN ( "
                    + " SELECT vazba_seznam_nabidky_seznam_id, COUNT(vazba_seznam_nabidky_nabidky_id) AS pocet "
                    + " FROM spolecne.vazba_seznam_nabidky GROUP BY vazba_seznam_nabidky_seznam_id) AS x "
                    + " ON x.vazba_seznam_nabidky_seznam_id = seznam_nabidek_id "
                    + "WHERE seznam_nabidek_zakaznik_id IS NOT NULL ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() != 0) {
                dotaz += "AND seznam_nabidek_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT (YEAR FROM seznam_nabidek_datum) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString();
            }
            if ((jTFDatumOd.getText().trim().length() > 0) && (jTFDatumDo.getText().trim().length() > 0)) {
                dotaz += "AND seznam_nabidek_datum BETWEEN " + TextFunkce1.osetriZapisTextDB1(jTFDatumOd.getText().trim()) + " AND " + TextFunkce1.osetriZapisTextDB1(jTFDatumDo.getText().trim()) + " ";
            }
            dotaz += "GROUP BY seznam_nabidek_id, seznam_nabidek_datum, seznam_nabidek_komu, seznam_nabidek_bindata, seznam_nabidek_predmet, seznam_nabidek_vystavil, seznam_nabidek_veta, x.pocet "
                    + "ORDER BY seznam_nabidek_datum";
            // System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tSeznam1 = new TridaSeznamNabidek1();
                tSeznam1.setIdSeznam(q.getInt(1));
                tSeznam1.setDatumVystaveni(q.getDate(2));// datum vystaveni
                tSeznam1.setPrijemce((q.getString(3) == null) ? "" : q.getString(3));
                tSeznam1.setPocetNabidek(q.getInt(4));
                tSeznam1.setDataPDF((q.getString(5) == null) ? "" : q.getString(5));
                tSeznam1.setPredmet((q.getString(6) == null) ? "" : q.getString(6));
                tSeznam1.setVeta((q.getString(7) == null) ? "" : q.getString(7));
                tSeznam1.setVystavitel((q.getString(8) == null) ? "" : q.getString(8));
                arSeznam1.add(tSeznam1);
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arSeznam1.size() + "");
        int index[][] = new int[arSeznam1.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[arSeznam1.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < arSeznam1.size(); row++) {
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

    protected void getDataTabulkaDetail1() {

        // boolean datumDodani = false;
        // boolean datumExpedice = false;
        // boolean datumObjednani = false;
        if (tabulkaDetail.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaDetail.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTN1.clear();

        tabulkaModelDetail1.oznamZmenu();

        try {
            String dotaz = "";
            pocetKusuObjednavky = 0;

            dotaz = "SELECT nabidky_id, "
                    + "nabidky_datum, vykresy_nazev, "
                    + "vykresy_cislo, "
                    + "vykresy_revize, "
                    + "nabidky_pocet_objednanych_kusu, "
                    + "nabidky_cena_za_kus, "
                    + "CASE WHEN nabidky_povrch_uprava = 0 THEN '' ELSE btrim(to_char(nabidky_povrch_uprava,'FM99990D00'),'.,') END AS uprava, "
                    + "meny_zkratka, "
                    + "CASE WHEN typ_materialu_nazev IS NULL THEN typ_materialu_norma "
                    + "WHEN typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                    + "ELSE typ_materialu_nazev || ' - ' || typ_materialu_norma END AS material, "
                    + "polotovary_nazev, "
                    + "btrim(to_char(nabidky_mnozstvi_materialu,'FM99990D9999'),'.,'), "
                    + "CASE WHEN nabidky_ukoncena IS FALSE THEN 'aktivní' ELSE 'ukončená' END AS stav, "
                    + "COALESCE(vykresy_bindata_nazev,''), vazba_seznam_nabidky_poradi, vykresy_id "
                    + "FROM spolecne.nabidky "
                    + "LEFT JOIN(SELECT vykresy_bindata_id, vykresy_bindata_nazev "
                    + "FROM spolecne.vykresy_bindata) bd "
                    + "ON nabidky_cislo_vykresu = bd.vykresy_bindata_id "
                    + "CROSS JOIN spolecne.typ_materialu "
                    + "CROSS JOIN spolecne.polotovary "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_seznam_nabidky "
                    + "WHERE vazba_seznam_nabidky_seznam_id = " + arSeznam1.get(tabulkaHlavni.getSelectedRow()).getIdSeznam() + " "
                    + "AND polotovary_typ_materialu = typ_materialu_id "
                    + "AND meny_id = nabidky_mena_id "
                    + "AND meny.meny_id = nabidky.nabidky_mena_id "
                    + "AND polotovary_id = nabidky_polotovar_id "
                    + "AND vazba_seznam_nabidky_nabidky_id = nabidky_id "
                    + "AND vykresy.vykresy_id = nabidky_cislo_vykresu "
                    + "ORDER BY vazba_seznam_nabidky_poradi, vykresy_cislo, vykresy_revize ASC  ";

            //System.out.println(dotaz);
            ResultSet nabidka = PripojeniDB.dotazS(dotaz);
            while (nabidka.next()) {
                tNab1 = new TridaNabidka1();
                tNab1.setIdNabidky(new Long(nabidka.getLong(1)));
                tNab1.setPoradi(nabidka.getInt(15));
                tNab1.setDatum(nabidka.getDate(2));
                tNab1.setIdVykres(nabidka.getInt(16));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setNazev(SQLFunkceObecne.osetriCteniString(nabidka.getString(3)));
                tv1.setIdVykres(nabidka.getInt(16));
                tv1.setCislo(SQLFunkceObecne.osetriCteniString(nabidka.getString(4)));
                tv1.setRevize(SQLFunkceObecne.osetriCteniString(nabidka.getString(5)));
                tv1.setPoznamky(SQLFunkceObecne.osetriCteniString(nabidka.getString(14)));
                tNab1.setTv1(tv1);
                tNab1.setPocetObjednanychKusu(nabidka.getInt(6));
                try {
                    tNab1.setCenaKus(nf2.format(nf2.parse(SQLFunkceObecne.osetriCteniString(nabidka.getString(7)).replace(".", ","))));
                } catch (Exception e) {
                    tNab1.setCenaKus(SQLFunkceObecne.osetriCteniString(nabidka.getString(7)));
                }
                tNab1.setPovrchUprava(SQLFunkceObecne.osetriCteniString(nabidka.getString(8)));
                tNab1.setPopisMena(SQLFunkceObecne.osetriCteniString(nabidka.getString(9)));
                tNab1.setPopisMaterial(SQLFunkceObecne.osetriCteniString(nabidka.getString(10)));
                tNab1.setPopisPolotovar(SQLFunkceObecne.osetriCteniString(nabidka.getString(11)));
                tNab1.setMaterialMnozstvi(SQLFunkceObecne.osetriCteniString(nabidka.getString(12)));
                tNab1.setPopisAktivni(SQLFunkceObecne.osetriCteniString(nabidka.getString(13)));
                arTN1.add(tNab1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        int index[][] = new int[arTN1.size()][tabulkaModelDetail1.getColumnCount()];
        int index2[][] = new int[arTN1.size()][tabulkaModelDetail1.getColumnCount()];

        for (int row = 0; row < arTN1.size(); row++) {
            index2[row][4] = 1;
            index2[row][5] = 1;
            index2[row][6] = 2;
        }
        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelDetail1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaDetail.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }

    } //konec getDataTabulkaObjednavka1

    protected void vycistiFiltrObjednavka1() {
        jTFDatumOd.setText("");
        jTFDatumDo.setText("");
    }

    private void tiskSeznam(TridaSeznamNabidek1 seznamTisknout, boolean tisk) {
        seznamTisknout.selectData();
        TridaZakaznik1 zakaznikTisk = new TridaZakaznik1();
        zakaznikTisk.selectData(seznamTisknout.getIdZakaznik());
        String reportSource = "";
        if (zakaznikTisk.getIdStat() == 1) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekCesky.jrxml";
        } else if (zakaznikTisk.getIdStat() == 4 || zakaznikTisk.getIdStat() == 7) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekBBHUSA.jrxml";
        } else if (zakaznikTisk.getIdStat() == 2 || zakaznikTisk.getIdStat() == 3 || zakaznikTisk.getIdStat() == 6) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekLeica1.jrxml";
        } else {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekBBHUSA.jrxml";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("prijemce", seznamTisknout.getPrijemce());
        params.put("predmet", seznamTisknout.getPredmet());
        params.put("info", seznamTisknout.getVeta());

        if (zakaznikTisk.getIdStat() == 2 || zakaznikTisk.getIdStat() == 3 || zakaznikTisk.getIdStat() == 6) {
            params.put("datum", ("Pilsen, den " + df.format(seznamTisknout.getDatumVystaveni())));
        } else if (zakaznikTisk.getIdStat() == 4 || zakaznikTisk.getIdStat() == 5 || zakaznikTisk.getIdStat() == 7|| zakaznikTisk.getIdStat() == 8) {
            params.put("datum", ("Pilsen, " + df.format(seznamTisknout.getDatumVystaveni())));
        } else {
            params.put("datum", ("V Plzni, dne " + df.format(seznamTisknout.getDatumVystaveni())));
        }
        params.put("seznam_vystavil", seznamTisknout.getVystavitel());
        params.put("firma", zakaznikTisk.getNazev());
        params.put("adresa_ulice", zakaznikTisk.getAdresa());
        params.put("adresa_psc_mesto", zakaznikTisk.getPsc() + " " + zakaznikTisk.getMesto());
        params.put("logo", HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "mikron.jpg");
       
     

        try {
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

        params.put("seznam_id", Long.valueOf(seznamTisknout.getIdSeznam()));
        params.put("zakaznik_id", seznamTisknout.getIdZakaznik());

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            //  JasperViewer.viewReport(jasperPrint1);
            if (tisk == true) {
                JasperPrintManager.printReport(jasperPrint1, true);
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "seznam.pdf");
            }
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void zobrazitPDF(TridaSeznamNabidek1 seznam) {
        String nazevSouboru = "nabidka.pdf";
        try {
            ResultSet soubor = PripojeniDB.dotazS("SELECT seznam_nabidek_bindata "
                    + "FROM spolecne.seznam_nabidek "
                    + "WHERE seznam_nabidek_id = " + seznam.getIdSeznam());
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

    private void smazatSeznam1(TridaSeznamNabidek1 seznam) {
        int rc;
        try {
            rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
            String dotaz = "DELETE FROM spolecne.vazba_seznam_nabidky WHERE "
                    + "vazba_seznam_nabidky_seznam_id = " + seznam.getIdSeznam();
            int a = PripojeniDB.dotazIUD(dotaz);
            dotaz = "DELETE FROM spolecne.seznam_nabidek WHERE "
                    + "seznam_nabidek_id = " + seznam.getIdSeznam();
            a = PripojeniDB.dotazIUD(dotaz);

        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        }
        rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        //tabulkaModelObjednavka1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
    }

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Ks",
            "Cena/ks",
            "Měna",
            "Povrch ",
            "Materiál",
            "Polotovar",
            "Množství [m]",
            "Stav"
        };

        public void pridejSadu() {
            // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTN1.size());
            //  updateZaznamyObjednavka1();
            if (arTN1.size() > 0) {
                tabulkaDetail.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaNabidka1 tObj) {
            arTN1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulkaDetail.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arTN1.remove(tabulkaDetail.getSelectedRow());
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
            return arTN1.size();
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

        public boolean nastavHodnotuNaVybrane(TridaNabidka1 bt) {
            // System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaDetail.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulkaDetail.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaNabidka1 nastavPruv, int pozice) {
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
                tNab1 = arTN1.get(row);
                switch (col) {
                    case (0): {
                        if (tNab1.getDatum() != null) {
                            return (df.format(tNab1.getDatum()));
                        } else {
                            return "";
                        }
                    }
                    case (1): {
                        return (tNab1.getTv1().getNazev());

                    }
                    case (2): {
                        return (tNab1.getTv1().getCislo());

                    }
                    case (3): {
                        return (tNab1.getTv1().getRevize());

                    }
                    case (4): {
                        return (tNab1.getPocetObjednanychKusu() + " ks" + "  ");

                    }
                    case (5): {
                        return (tNab1.getCenaKus());
                    }
                    case (6): {
                        return (tNab1.getPopisMena());
                    }
                    case (7): {
                        return (tNab1.getPovrchUprava());
                    }
                    case (8): {
                        return (tNab1.getPopisMaterial());
                    }
                    case (9): {
                        return (tNab1.getPopisPolotovar());
                    }
                    case (10): {
                        return (tNab1.getMaterialMnozstvi());
                    }
                    case (11): {
                        return (tNab1.getPopisAktivni());
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
            "Vystavil",
            "Příjemce",
            "Počet položek",
            "PDF"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arSeznam1.size());
            //  updateZaznamyObjednavka1();
            if (arSeznam1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arSeznam1.remove(tabulkaHlavni.getSelectedRow());
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
            return arSeznam1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        public boolean nastavHodnotuNaVybrane(TridaSeznamNabidek1 bt) {
            return nastavHodnotuNaPozici(bt, tabulkaHlavni.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaSeznamNabidek1 nastavPruv, int pozice) {
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
                tSeznam1 = arSeznam1.get(row);
                switch (col) {
                    case (0): {
                        return (df.format(tSeznam1.getDatumVystaveni()));
                    }
                    case (1): {
                        return tSeznam1.getVystavitel();
                    }
                    case (2): {
                        return tSeznam1.getPrijemce();
                    }
                    case (3): {
                        return tSeznam1.getPocetNabidek();
                    }
                    case (4): {
                        return (tSeznam1.getDataPDF());
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
                    tiskSeznam(arSeznam1.get(tabulkaHlavni.getSelectedRow()), true);
                }
            }
            if (e.getActionCommand().equals("zobrazPDF")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    zobrazitPDF(arSeznam1.get(tabulkaHlavni.getSelectedRow()));
                }
            }

            if (e.getActionCommand().equals("Smazat")) {
                System.out.println("Smazat");
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    smazatSeznam1(arSeznam1.get(tabulkaHlavni.getSelectedRow()));
                }
            }
            if (e.getActionCommand().equals("Upravit")) {
                if (tabulkaHlavni.getSelectedRow() > -1) {
                    SeznamNabidekFrame1 edit = new SeznamNabidekFrame1(arSeznam1.get(tabulkaHlavni.getSelectedRow()));
                }
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
                    if (arSeznam1.size() > 0) {
                        nastavTabulkuDetail1();
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
        jBVycistitFiltr = new javax.swing.JButton();
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
        jBZobrazPDF1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPFaktury = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPObjednavky = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        jBVycistitFiltr.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jBVycistitFiltr, gridBagConstraints);

        jLabel3.setText("Rok vytvoření seznamu :");
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

        jLabel16.setText("Vytvořeno od");
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

        jBTisk.setText("Tisk nabidky");
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

        jBUpravit.setText("Upravit seznam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravit, gridBagConstraints);

        jBSmazat.setText("Smazat seznam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazat, gridBagConstraints);

        jBZobrazPDF1.setText("Uložit PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazPDF1, gridBagConstraints);

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
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 20);
        jPanel2.add(jSPFaktury, gridBagConstraints);

        jSPObjednavky.setPreferredSize(new java.awt.Dimension(602, 402));

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
        gridBagConstraints.weightx = 0.75;
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
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JLabel PocetZaznamuLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisk;
    private javax.swing.JButton jBUpravit;
    private javax.swing.JButton jBVycistitFiltr;
    private javax.swing.JButton jBZobrazPDF;
    private javax.swing.JButton jBZobrazPDF1;
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
