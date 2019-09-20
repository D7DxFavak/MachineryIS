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

/**
 *
 * @author Favak
 */
public class SkladPrebytkuPanel1 extends javax.swing.JPanel {

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
    protected TridaSkladTransakce tMatTrans1;
    protected TridaVyrobek tVyrobek1;
    protected ArrayList<TridaSkladTransakce> arPT1;
    protected ArrayList<TridaVyrobek> arVyrobky;
    protected ArrayList vsSkladVyskladnitPolozka1;
    protected ArrayList vrSkladVyskladnitPolozka1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;
    protected int indexOznaceno;

    /**
     * Creates new form ObjednavkyPanel
     */
    public SkladPrebytkuPanel1() {
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
        arPT1 = new ArrayList<TridaSkladTransakce>();
        vrSkladVyskladnitPolozka1 = new ArrayList();
        vsSkladVyskladnitPolozka1 = new ArrayList();
        tMatTrans1 = new TridaSkladTransakce();

        arVyrobky = new ArrayList<TridaVyrobek>();
        tVyrobek1 = new TridaVyrobek();

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
        jCBZakaznik.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBZobrazitSoubor.addActionListener(alUdalosti);
        jBNaskladnit.addActionListener(alUdalosti);
        jBVyskladnit.addActionListener(alUdalosti);
        jBUmisteni.addActionListener(alUdalosti);

        jBVycistitFiltr.setActionCommand("VycistiFiltrObjednavka1");
        VyhledatButton1.setActionCommand("Hledat");
        jBZobrazitSoubor.setActionCommand("ZobrazitVykres");
        jBNaskladnit.setActionCommand("Naskladnit");
        jBVyskladnit.setActionCommand("Vyskladnit");
        jCBZakaznik.setActionCommand("Hledat");
        jBUmisteni.setActionCommand("UpravitUmisteni");

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
        column.setPreferredWidth(115);*/

        //  zrusPosluchaceUdalostiTabulky();
        if (arVyrobky.size() > 0) {
            getDataTabulkaDetail1();
            tabulkaModelDetail1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();


    }// konec nastavTabulkuBT1

    protected void nastavTabulkuHlavni1() {
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
        column.setPreferredWidth(90);*/

        refreshDataHlavni();

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

    private void upravitUmisteni() {
        UpravaUmisteniFrame1 uprava = new UpravaUmisteniFrame1(arVyrobky.get(tabulkaHlavni.getSelectedRow()).getVykresCislo());
        uprava.addWindowListener(winUdalosti);
    }

    private void zobrazitVykres() {
        String nazevVykresu = "";
        try {
            ResultSet vykres = PripojeniDB.dotazS("SELECT vykresy_bindata_nazev, vykresy_bindata_data "
                    + "FROM spolecne.vykresy_bindata "
                    + "WHERE vykresy_bindata_nazev = " + TextFunkce1.osetriZapisTextDB1(arVyrobky.get(tabulkaHlavni.getSelectedRow()).getNazevSouboru()) + " "
                    + " ");
            vykres.last();
            if (vykres.getRow() > 0) {
                vykres.beforeFirst();
                File soubor1 = null;
                while (vykres.next()) {
                    nazevVykresu = vykres.getString(1);
                    soubor1 = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevVykresu);
                    FileOutputStream fos = new FileOutputStream(soubor1);
                    byte[] buffer;
                    buffer = vykres.getBytes(2);
                    fos.write(buffer);
                    fos.close();
                }// konec while


                if (soubor1 != null) {
                    Desktop.getDesktop().open(soubor1);
                }

            } // konec if
        }//konec try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arVyrobky.size() > 0) {
            tabulkaHlavni.setRowSelectionInterval(0, 0);
            nastavTabulkuDetail1();
        }
    }

    protected void initRoletky() {
        roletkaModelZakaznici = new RoletkaUniverzalRozsirenaModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_popis FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_popis", "Neurčen", null,
                "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBZakaznik.setModel(roletkaModelZakaznici);

    }

    protected void getDataTabulkaHlavni1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arVyrobky.clear();
        vrSkladVyskladnitPolozka1.clear();
        tabulkaModelHlavni1.oznamZmenu();

        //nacist data
        try {
            String dotaz =
                    "SELECT * FROM (SELECT * FROM (SELECT sklady_vyrobky_transakce_umisteni_id, sklady_vyrobky_transakce_vykres_cislo, sklady_vyrobky_umisteni_nazev, "
                    + "COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                    + "FROM logistika.sklady_vyrobky_transakce "
                    + "CROSS JOIN logistika.sklady_vyrobky_transakce_druhy "
                    + "CROSS JOIN logistika.sklady_vyrobky_umisteni "
                    + "WHERE sklady_vyrobky_transakce_druh_id = sklady_vyrobky_transakce_druhy_id "
                    + "AND sklady_vyrobky_transakce_druhy_je_prijem IS TRUE "
                    + "AND sklady_vyrobky_transakce_umisteni_id = sklady_vyrobky_umisteni_id "
                    + "GROUP BY sklady_vyrobky_transakce_vykres_cislo, sklady_vyrobky_umisteni_nazev, sklady_vyrobky_transakce_umisteni_id "
                    + "ORDER BY sklady_vyrobky_transakce_vykres_cislo ) t "
                    + "INNER JOIN (SELECT DISTINCT ON (vykresy_cislo) vykresy_cislo "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_zakaznik_id =  " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo();
            if (jTFCisloVykres.getText().trim().isEmpty() == false) {
                dotaz += "AND vykresy_cislo ILIKE '%" + jTFCisloVykres.getText().trim() + "%' ";
            }
            if (jTFNazevVykres.getText().trim().isEmpty() == false) {
                dotaz += "AND vykresy_nazev ILIKE '%" + jTFNazevVykres.getText().trim() + "%' ";
            }
            dotaz += " ) x "
                    + "ON x.vykresy_cislo = t.sklady_vyrobky_transakce_vykres_cislo "
                    + "LEFT JOIN (SELECT vykresy_cislo, SUM(pruvodky_pocet_kusu_sklad) + SUM(pruvodky_pocet_kusu) - SUM(objednavky_pocet_objednanych_kusu) AS prebytky_vyroba "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.pruvodky "
                    + "WHERE objednavky_cislo_vykresu = vykresy_id "
                    + "AND pruvodky_cislo_vykresu = vykresy_id "
                    + "AND pruvodky_objednavky_id = objednavky_id "
                    + "AND objednavky_datum_expedice IS NULL "
                    + "AND pruvodky_vyrobeno_kusu IS NULL "
                    + "GROUP BY vykresy_cislo) o "
                    + "ON t.sklady_vyrobky_transakce_vykres_cislo = o.vykresy_cislo "
                    + "LEFT JOIN (SELECT vykresy_cislo, COUNT(objednavky_pocet_objednanych_kusu) AS Pocet_objednavek, SUM(objednavky_pocet_objednanych_kusu)  AS Pocet_kusu  "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE objednavky_datum_expedice IS NULL "
                    + "AND objednavky_cislo_vykresu = vykresy_id "
                    + "GROUP BY vykresy_cislo) o1 "
                    + "ON t.sklady_vyrobky_transakce_vykres_cislo = o1.vykresy_cislo) AS sklad "
                    + "LEFT JOIN (SELECT vykresy_cislo, MAX(ramec_objednavky_datum_objednani) as datum "
                    + "FROM spolecne.ramec_objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE ramec_objednavky_cislo_vykresu = vykresy_id GROUP BY vykresy_cislo) maxDatum "
                    + "ON sklad.sklady_vyrobky_transakce_vykres_cislo = maxDatum.vykresy_cislo "
                    + "LEFT JOIN ( "
                    + "SELECT vykresy_cislo, "
                    + "ramec_objednavky_odvolavka_kusu AS kusy, "
                    + "ramec_objednavky_datum_objednani AS datum "
                    + "FROM spolecne.ramec_objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE ramec_objednavky_cislo_vykresu = vykresy_id "
                    + "ORDER BY ramec_objednavky_datum_objednani ASC) r1 "
                    + "ON (sklad.sklady_vyrobky_transakce_vykres_cislo = r1.vykresy_cislo AND "
                    + "r1.datum = maxDatum.datum) "
                    + "LEFT JOIN(SELECT vykresy_cislo, vykresy_bindata_nazev "
                    + "FROM spolecne.vykresy_bindata "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE vykresy_bindata_id = vykresy_id) bd "
                    + "ON sklad.sklady_vyrobky_transakce_vykres_cislo = bd.vykresy_cislo ";
            //System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tVyrobek1 = new TridaVyrobek();
                tVyrobek1.setIdUmisteni(q.getInt(1));
                tVyrobek1.setVykresCislo((q.getString(2) == null) ? "" : q.getString(2));
                tVyrobek1.setNazevUmisteni((q.getString(3) == null) ? "" : q.getString(3));
                tVyrobek1.setPocetKusu(q.getInt(4));
                tVyrobek1.setPrebytekVyroba(q.getInt(7));
                tVyrobek1.setPocetObjednavek(q.getInt(9));
                tVyrobek1.setPocetObjednanychKusu(q.getInt(10));
                tVyrobek1.setMinimumKusu(q.getInt(14));
                tVyrobek1.setNazevSouboru((q.getString(17) == null) ? "" : q.getString(17));
                arVyrobky.add(tVyrobek1);

            }// konec while

            dotaz =
                    "SELECT * FROM (SELECT sklady_vyrobky_transakce_vykres_cislo, sklady_vyrobky_transakce_umisteni_id,  "
                    + "COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                    + "FROM logistika.sklady_vyrobky_transakce "
                    + "CROSS JOIN logistika.sklady_vyrobky_transakce_druhy "
                    + "WHERE sklady_vyrobky_transakce_druh_id = sklady_vyrobky_transakce_druhy_id "
                    + "AND sklady_vyrobky_transakce_druhy_je_prijem IS FALSE "
                    + "GROUP BY sklady_vyrobky_transakce_vykres_cislo, sklady_vyrobky_transakce_umisteni_id  "
                    + "ORDER BY sklady_vyrobky_transakce_vykres_cislo) t "
                    + "INNER JOIN (SELECT DISTINCT ON (vykresy_cislo) vykresy_cislo "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_zakaznik_id =  " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ) x "
                    + "ON x.vykresy_cislo = t.sklady_vyrobky_transakce_vykres_cislo ";
            ResultSet vyskladnit1 = PripojeniDB.dotazS(dotaz);
            while (vyskladnit1.next()) {
                vsSkladVyskladnitPolozka1 = new ArrayList();

                vsSkladVyskladnitPolozka1.add(vyskladnit1.getString(1)); // cislo_vykresu
                vsSkladVyskladnitPolozka1.add(new Integer(vyskladnit1.getInt(2))); // umisteni_id
                vsSkladVyskladnitPolozka1.add(new Integer(vyskladnit1.getInt(3))); // pocet kusu
                vrSkladVyskladnitPolozka1.add(vsSkladVyskladnitPolozka1);

            }// konec while
            int plus = 0;
            int minus = 0;
            for (int iPlus = 0; iPlus < arVyrobky.size(); iPlus++) {
                for (int iMinus = 0; iMinus < vrSkladVyskladnitPolozka1.size(); iMinus++) {
                    if (((String) ((ArrayList) vrSkladVyskladnitPolozka1.get(iMinus)).get(0)).equals(arVyrobky.get(iPlus).getVykresCislo())) {
                        //if (((Integer) ((ArrayList) vrSkladVyskladnitPolozka1.get(iMinus)).get(1)).intValue() == arVyrobky.get(iPlus).getIdUmisteni()) {
                            plus = arVyrobky.get(iPlus).getPocetKusu();
                            minus = ((Integer) ((ArrayList) vrSkladVyskladnitPolozka1.get(iMinus)).get(2));
                            arVyrobky.get(iPlus).setPocetKusu(plus - minus);
                       // }
                    }
                }
            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arVyrobky.size() + "");
        int index[][] = new int[arVyrobky.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[arVyrobky.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < arVyrobky.size(); row++) {
            index[row][3] = 1;
            index2[row][2] = 1;
            index2[row][3] = 1;
            index2[row][4] = 1;
            index2[row][5] = 1;
            index2[row][6] = 1;
            index2[row][7] = 2;

            if (arVyrobky.get(row).getPocetKusu() < arVyrobky.get(row).getMinimumKusu()) {
                index[row][6] = 1;
            }

        }
        Color backColor = new Color(255, 204, 204);
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
        arPT1.clear();

        tabulkaModelDetail1.oznamZmenu();

        try {
            String dotaz =
                    "SELECT sklady_vyrobky_transakce_id, "
                    + "sklady_vyrobky_umisteni_nazev, "
                    + "sklady_vyrobky_transakce_druhy_nazev, "
                    + "sklady_vyrobky_transakce_pocet_mj, "
                    + "to_char(sklady_vyrobky_transakce_log_timestamp, 'DD.MM.YYYY') "
                    + "FROM logistika.sklady_vyrobky_transakce "
                    + "CROSS JOIN logistika.sklady_vyrobky_umisteni "
                    + "CROSS JOIN logistika.sklady_vyrobky_transakce_druhy "
                    + "WHERE sklady_vyrobky_transakce_vykres_cislo = " + TextFunkce1.osetriZapisTextDB1(arVyrobky.get(tabulkaHlavni.getSelectedRow()).getVykresCislo()) + " "
                    + "AND sklady_vyrobky_transakce_umisteni_id = sklady_vyrobky_umisteni_id "
                    + "AND sklady_vyrobky_transakce_druh_id = sklady_vyrobky_transakce_druhy_id "
                    + "ORDER BY sklady_vyrobky_transakce_log_timestamp DESC";
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tMatTrans1 = new TridaSkladTransakce();
                tMatTrans1.setIdTransakce(new Long(q.getLong(1))); // transakce id
                tMatTrans1.setRozmer((q.getString(2) == null) ? "" : q.getString(2)); // nazev
                tMatTrans1.setPopisTransakce((q.getString(3) == null) ? "" : q.getString(3)); // transakce
                tMatTrans1.setPocetKusu(q.getBigDecimal(4)); // pocet kusu
                tMatTrans1.setDatum(df.parse(q.getString(5))); // timestamp               
                arPT1.add(tMatTrans1);
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catc

        int index[][] = new int[arPT1.size()][tabulkaModelDetail1.getColumnCount()];
        int index2[][] = new int[arPT1.size()][tabulkaModelDetail1.getColumnCount()];

        for (int row = 0; row < arPT1.size(); row++) {
            index2[row][1] = 1;
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
        jTFUmisteniSklad.setText("");
    }

    private void naskladnit() {

        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladVyrobkuFrame1 naskladnit = new SkladVyrobkuFrame1(200, arVyrobky.get(tabulkaHlavni.getSelectedRow()));

            naskladnit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 naskladnit = new SkladMaterialuFrame1(200, false);
            naskladnit.addWindowListener(winUdalosti);

        }
    }

    private void vyskladnit() {
        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladVyrobkuFrame1 vyskladnit = new SkladVyrobkuFrame1(100, arVyrobky.get(tabulkaHlavni.getSelectedRow()));
            vyskladnit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 vyskladnit = new SkladMaterialuFrame1(100, false);
            vyskladnit.addWindowListener(winUdalosti);
        }
    }

    private void upravit() {
        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladVyrobkuFrame1 upravit = new SkladVyrobkuFrame1(300, arVyrobky.get(tabulkaHlavni.getSelectedRow()));
            upravit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 upravit = new SkladMaterialuFrame1(300, false);
            upravit.addWindowListener(winUdalosti);
        }
    }

    private void novyRozmer() {
        SkladMaterialuFrame1 novy = new SkladMaterialuFrame1(400, true);
        novy.addWindowListener(winUdalosti);
    }

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo transakce",
            "Umístění",
            "Druh transakce",
            "Počet kusů",
            "Datum"
        };

        public void pridejSadu() {
            // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arPT1.size());
            //  updateZaznamyObjednavka1();
            if (arPT1.size() > 0) {
                tabulkaDetail.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaSkladTransakce tObj) {
            arPT1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulkaDetail.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arPT1.remove(tabulkaDetail.getSelectedRow());
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
            return arPT1.size();
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

        public boolean nastavHodnotuNaVybrane(TridaSkladTransakce bt) {
            // System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaDetail.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulkaDetail.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaSkladTransakce nastavPruv, int pozice) {
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
                tMatTrans1 = arPT1.get(row);
                switch (col) {
                    case (0): {
                        return (tMatTrans1.getIdTransakce());
                    }
                    case (1): {
                        return (tMatTrans1.getRozmer());

                    }
                    case (2): {
                        return (tMatTrans1.getPopisTransakce());

                    }
                    case (3): {
                        return (tMatTrans1.getPocetKusu());

                    }
                    case (4): {
                        try {
                            return df.format(tMatTrans1.getDatum());
                        } catch (Exception e) {
                            return null;
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
            "Číslo výkresu",
            "Umístění",
            "Počet kusů na skladě",
            "Vyráběno navíc",
            "Počet objednávek",
            "Celkem objednáno kusů",
            "Minimální stav",
            "Soubor"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arVyrobky.size());
            //  updateZaznamyObjednavka1();
            if (arVyrobky.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arVyrobky.remove(tabulkaHlavni.getSelectedRow());
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
            return arVyrobky.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        public boolean nastavHodnotuNaVybrane(TridaPolotovar1 bt) {
            return nastavHodnotuNaPozici(bt, tabulkaHlavni.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaPolotovar1 nastavPruv, int pozice) {
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
                return false;
            } else {
                return false;
            }
        }

        @Override
        public Object getValueAt(int row, int col) {
            // System.out.println("getValueAt PruvFram");
            try {
                tVyrobek1 = arVyrobky.get(row);
                switch (col) {
                    case (0): {
                        return tVyrobek1.getVykresCislo();
                    }
                    case (1): {
                        return tVyrobek1.getNazevUmisteni();
                    }
                    case (2): {
                        return tVyrobek1.getPocetKusu();
                    }
                    case (3): {
                        return tVyrobek1.getPrebytekVyroba();
                    }
                    case (4): {
                        return tVyrobek1.getPocetObjednavek();
                    }
                    case (5): {
                        return tVyrobek1.getPocetObjednanychKusu();
                    }
                    case (6): {
                        return tVyrobek1.getMinimumKusu();
                    }
                    case (7): {
                        return tVyrobek1.getNazevSouboru();
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
                refreshDataHlavni();
            }
            if (e.getActionCommand().equals("Hledat")) {
                refreshDataHlavni();
            }

            if (e.getActionCommand().equals("ZobrazitVykres")) {
                zobrazitVykres();
            }

            if (e.getActionCommand().equals("Upravit")) {
                upravit();
            }
            if (e.getActionCommand().equals("UpravitUmisteni")) {
                upravitUmisteni();
            }
            if (e.getActionCommand().equals("Naskladnit")) {
                naskladnit();
            }
            if (e.getActionCommand().equals("Vyskladnit")) {
                vyskladnit();
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
                    if (arVyrobky.size() > 0) {
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
            getDataTabulkaHlavni1();
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
        jCBAktivni = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTFUmisteniSklad = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTFNazevVykres = new javax.swing.JTextField();
        jTFCisloVykres = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jBNaskladnit = new javax.swing.JButton();
        jBZobrazitSoubor = new javax.swing.JButton();
        jBVyskladnit = new javax.swing.JButton();
        jBUmisteni = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPHlavni = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPDetail = new javax.swing.JScrollPane();
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

        jCBAktivni.setText("Filtr aktivní materiály");
        JPFiltrTop.add(jCBAktivni, new java.awt.GridBagConstraints());

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel16.setText("Umístění ve skladu :");
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
        jPFiltry.add(jTFUmisteniSklad, gridBagConstraints);

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel1, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        jLabel3.setText("Název :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPFiltry.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 150;
        jPFiltry.add(jTFNazevVykres, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 150;
        jPFiltry.add(jTFCisloVykres, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBNaskladnit.setText("Přidat do skladu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNaskladnit, gridBagConstraints);

        jBZobrazitSoubor.setText("Zobrazit soubor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazitSoubor, gridBagConstraints);

        jBVyskladnit.setText("Vyskladnit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBVyskladnit, gridBagConstraints);

        jBUmisteni.setText("Upravit umístění");
        jBUmisteni.setPreferredSize(new java.awt.Dimension(109, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUmisteni, gridBagConstraints);

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 10);
        jPanel2.add(jSPDetail, gridBagConstraints);

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
    private javax.swing.JButton jBNaskladnit;
    private javax.swing.JButton jBUmisteni;
    private javax.swing.JButton jBVycistitFiltr;
    private javax.swing.JButton jBVyskladnit;
    private javax.swing.JButton jBZobrazitSoubor;
    private javax.swing.JCheckBox jCBAktivni;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPDetail;
    private javax.swing.JScrollPane jSPHlavni;
    private javax.swing.JTextField jTFCisloVykres;
    private javax.swing.JTextField jTFNazevVykres;
    private javax.swing.JTextField jTFUmisteniSklad;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
