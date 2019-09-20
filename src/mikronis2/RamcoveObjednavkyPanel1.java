/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRenderer1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaRamcovaObjednavka;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class RamcoveObjednavkyPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelDetail1, tableModelHlavni1;
    protected TabulkaModelDetail tabulkaModelDetail1;
    protected TabulkaModelHlavni tabulkaModelHlavni1;
    protected ListSelectionModel lsmDetail1, lsmHlavni1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaRamcovaObjednavka tRObj1;
    protected ArrayList<TridaRamcovaObjednavka> arTRO1;
    protected TridaObjednavka1 tObj1;
    protected ArrayList<TridaObjednavka1> arTO1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public RamcoveObjednavkyPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuHlavni1();

        nastavPosluchaceUdalostiOvladace();

        this.setVisible(true);


    }

    protected void nastavParametry() {
        arTO1 = new ArrayList<TridaObjednavka1>();
        tObj1 = new TridaObjednavka1();

        arTRO1 = new ArrayList<TridaRamcovaObjednavka>();
        tRObj1 = new TridaRamcovaObjednavka();

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

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jCheckBoxFiltrZdrojObjednavka1.addActionListener(alUdalosti);
        OdebratButton1.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBNovaObjednavka.addActionListener(alUdalosti);
        jBEditObjednavka.addActionListener(alUdalosti);
        jBNovaObjednavkaStavajici.addActionListener(alUdalosti);
        OdebratButton1.setActionCommand("OdebratObjednavka");
        VyhledatButton1.setActionCommand("Hledat");
        jCBZakaznik.setActionCommand("Hledat");
        jCBRokDodani.setActionCommand("Hledat");
        jBNovaObjednavka.setActionCommand("NovaObjednavka");
        jBEditObjednavka.setActionCommand("EditObjednavka");
        jBNovaObjednavkaStavajici.setActionCommand("NovaObjednavkaStavajici");

    }

    protected void nastavTabulkuHlavni1() {

       /* TableColumn column = null;
        column = tabulkaHlavni.getColumnModel().getColumn(0);
        column.setPreferredWidth(110);

        column = tabulkaHlavni.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulkaHlavni.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulkaHlavni.getColumnModel().getColumn(3);
        column.setPreferredWidth(46);

        column = tabulkaHlavni.getColumnModel().getColumn(4);
        column.setPreferredWidth(110);

        column = tabulkaHlavni.getColumnModel().getColumn(5);
        column.setPreferredWidth(70);

        column = tabulkaHlavni.getColumnModel().getColumn(6);
        column.setPreferredWidth(70);

        column = tabulkaHlavni.getColumnModel().getColumn(7);
        column.setPreferredWidth(70);

        column = tabulkaHlavni.getColumnModel().getColumn(8);
        column.setPreferredWidth(70);

        column = tabulkaHlavni.getColumnModel().getColumn(9);
        column.setPreferredWidth(70);

        column = tabulkaHlavni.getColumnModel().getColumn(10);
        column.setPreferredWidth(70);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1

    protected void nastavTabulkuDetail1() {

       /* TableColumn column = null;
        column = tabulkaDetail.getColumnModel().getColumn(0);
        column.setPreferredWidth(65);

        column = tabulkaDetail.getColumnModel().getColumn(1);
        column.setPreferredWidth(110);

        column = tabulkaDetail.getColumnModel().getColumn(2);
        column.setPreferredWidth(145);

        column = tabulkaDetail.getColumnModel().getColumn(3);
        column.setPreferredWidth(115);

        column = tabulkaDetail.getColumnModel().getColumn(4);
        column.setPreferredWidth(115);

        column = tabulkaDetail.getColumnModel().getColumn(5);
        column.setPreferredWidth(55);

        column = tabulkaDetail.getColumnModel().getColumn(6);
        column.setPreferredWidth(90);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaDetail1();
        tabulkaModelDetail1.pridejSadu();
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

        arTRO1.clear();
        tabulkaModelHlavni1.oznamZmenu();

        BigDecimal hodnotaZakazek = new BigDecimal(0);

        //nacist data
        try {

            String dotazRamc = "SELECT * FROM (SELECT ramec_objednavky_id, "
                    + "ramec_objednavky_datum_objednani, ramec_objednavky_nazev_soucasti, "
                    + "vykresy_id, vykresy_cislo, "
                    + "vykresy_revize, "
                    + "ramec_objednavky_cislo_objednavky, "
                    + "ramec_objednavky_pocet_objednanych_kusu, "
                    + "ramec_objednavky_odvolavka_kusu, "
                    + "ramec_objednavky_cena_za_kus, "
                    + "meny_zkratka, "
                    + "ramec_objednavky_poznamka "
                    + "FROM spolecne.ramec_objednavky "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE meny.meny_id = ramec_objednavky.ramec_objednavky_mena_id "
                    + "AND vykresy.vykresy_id = ramec_objednavky.ramec_objednavky_cislo_vykresu ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() != 0) {
                dotazRamc += "AND ramec_objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (CisloVykresuTextField1.getText().length() > 0) {
                dotazRamc += "AND vykresy.vykresy_cislo LIKE '%" + CisloVykresuTextField1.getText().trim() + "%' ";

            }
            if (NazevObjednavkyTextField1.getText().length() > 0) {
                dotazRamc += "AND ramec_objednavky_nazev_soucasti LIKE '%" + NazevObjednavkyTextField1.getText().trim() + "%' ";

            }
            if (CisloObjednavkyTextField1.getText().length() > 0) {
                dotazRamc += "AND ramec_objednavky_cislo_objednavky = '" + CisloObjednavkyTextField1.getText().trim() + "' ";

            }
            if (jTFTerminObjednaniOd1.getText().length() > 0) {
                dotazRamc += "AND ramec_objednavky_datum_objednani >= '" + jTFTerminObjednaniOd1.getText().trim() + "' ";

            }
            if (jTFTerminObjednaniDo1.getText().length() > 0) {
                dotazRamc += "AND ramec_objednavky_datum_objednani <= '" + jTFTerminObjednaniDo1.getText().trim() + "' ";

            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotazRamc += "AND EXTRACT(YEAR FROM ramec_objednavky_datum_objednani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            dotazRamc += "ORDER BY vykresy.vykresy_cislo ASC ) AS ramc "
                    + "LEFT JOIN ( SELECT vazba_objednavky_ramec_ramec_id, SUM (objednavky_pocet_objednanych_kusu) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec_objednavky_id = objednavky_id GROUP BY vazba_objednavky_ramec_ramec_id ) AS kusy "
                    + " ON ramc.ramec_objednavky_id = kusy.vazba_objednavky_ramec_ramec_id";
            ResultSet q = PripojeniDB.dotazS(dotazRamc);

            while (q.next()) {
                tRObj1 = new TridaRamcovaObjednavka();
                tRObj1.setId(q.getInt(1));
                tRObj1.setDatumObjednani(q.getDate(2));
                tRObj1.setNazevSoucasti(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(q.getInt(4));
                tv1.setCislo(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tv1.setRevize(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                tRObj1.setTv1(tv1);
                tRObj1.setIdVykres(q.getInt(4));
                tRObj1.setCisloObjednavky(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                tRObj1.setCelkemKusu(q.getInt(8));
                tRObj1.setObjednanoKusu(q.getInt(14));
                tRObj1.setOdvolavkaKusu(q.getInt(9));
                tRObj1.setCenaKus(q.getBigDecimal(10));
                tRObj1.setMenaZkratka(SQLFunkceObecne.osetriCteniString(q.getString(11)));
                tRObj1.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(12)));
                arTRO1.add(tRObj1);
                //System.out.println(q.getBigDecimal(10).multiply(new BigDecimal(q.getInt(14))) + "");
                hodnotaZakazek = hodnotaZakazek.add(q.getBigDecimal(10).multiply(new BigDecimal(q.getInt(14))));

            }// konec while            

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        jLPocetPolozek.setText(arTRO1.size() + "");
        jLHodnotaZakazek.setText(nf2.format(hodnotaZakazek));
        int index[][] = new int[arTRO1.size()][tabulkaHlavni.getColumnCount()];
        int index2[][] = new int[arTRO1.size()][tabulkaHlavni.getColumnCount()];
        int index3[][] = new int[arTRO1.size()][tabulkaHlavni.getColumnCount()];

        for (int row = 0; row < arTRO1.size(); row++) {

            index2[row][5] = 1;
            index2[row][6] = 1;
            index2[row][7] = 1;
            index2[row][8] = 1;
            index2[row][9] = 2;
            index[row][8] = 1;
            index3[row][7] = 1;
        }

        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelHlavni1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaHlavni.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, index3, backColor, frontColor));
        }
    } //konec getDataTabulkaHlavni1

    protected void getDataTabulkaDetail1() {
        if (tabulkaDetail.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaDetail.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();
        tabulkaModelDetail1.oznamZmenu();

        try {
            String dotaz =
                    "SELECT objednavky_id, vazba_objednavky_ramec_abruf_cislo, "
                    + "objednavky_datum_objednani, objednavky_nazev_soucasti, "
                    + "vykresy_id, vykresy_cislo, "
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_kusu_navic, "
                    + "objednavky_datum_expedice "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                    + "WHERE objednavky_id = vazba_objednavky_ramec_objednavky_id "
                    + "AND vazba_objednavky_ramec_ramec_id = " + arTRO1.get(tabulkaHlavni.getSelectedRow()).getId() + " "
                    + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "ORDER BY objednavky_datum_objednani ASC";

            ResultSet q = PripojeniDB.dotazS(dotaz);
            int abruf = 1;
            while (q.next()) {
                tObj1 = new TridaObjednavka1();
                tObj1.setId(q.getLong(1));
                tObj1.setPoradi(abruf);
                tObj1.setDatumObjednani(q.getDate(3));
                tObj1.setNazevSoucasti(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(q.getInt(5));
                tv1.setCislo(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                tObj1.setTv1(tv1);
                tObj1.setIdVykres(q.getInt(5));
                tObj1.setCisloObjednavky(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                tObj1.setPocetObjednanychKusu(q.getInt(8));
                tObj1.setKusuNavic(q.getInt(9));
                tObj1.setDatumExpedice(q.getDate(10));

                arTO1.add(tObj1);
                abruf++;
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catc
        int index[][] = new int[arTO1.size()][tabulkaModelDetail1.getColumnCount()];
        int index2[][] = new int[arTO1.size()][tabulkaModelDetail1.getColumnCount()];

        for (int row = 0; row < arTO1.size(); row++) {

            index2[row][0] = 2;
            index2[row][5] = 2;
            index2[row][6] = 2;
        }

        Color backColor = new Color(216, 232, 249);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelDetail1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaDetail.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
        tabulkaModelDetail1.oznamZmenu();


    } //konec getDataTabulkaHlavni1

    protected void vycistiFiltrObjednavka1() {
        CisloObjednavkyTextField1.setText("");
        NazevObjednavkyTextField1.setText("");
        roletkaModelZakaznici.setDataOId(0);
        roletkaModelRoky.setDataOId(0);
        jTFTerminObjednaniOd1.setText("");
        jTFTerminObjednaniDo1.setText("");
        CisloVykresuTextField1.setText("");
    }

    public void vytvorObjednavkuExistujici() {
        RamcovaObjednavkaFrame1 novaObjednavka = new RamcovaObjednavkaFrame1(arTRO1.get(tabulkaHlavni.getSelectedRow()).getId(), false, "Nová objednávka na základě stávající", "Uložit objednávku");
    }

    public void editObjednavka() {
        RamcovaObjednavkaFrame1 novaObjednavka = new RamcovaObjednavkaFrame1(arTRO1.get(tabulkaHlavni.getSelectedRow()).getId(), true, "Upravit objednávku", "Uložit úpravu");
    }

    protected class TabulkaModelHlavni extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum objednání",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",
            "Celkem",
            "Objednáno",
            "Odvolávka",
            "Cena/ks",
            "Měna",
            "Poznámka",};

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTRO1.size());
            //  updateZaznamyObjednavka1();
            if (arTRO1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arTRO1.remove(tabulkaHlavni.getSelectedRow());
            fireTableRowsDeleted(tabulkaHlavni.getSelectedRow(), tabulkaHlavni.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrSkladPolozka1size() > 0)
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
            return arTRO1.size();
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

        public boolean nastavHodnotuNaVybrane(TridaRamcovaObjednavka bt) {
            System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaHlavni.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulkaHlavni.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaRamcovaObjednavka nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            return true;
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            /*  if (col == 1 || col == 5 || col == 5 || col == 12) {
             return true;
             } else {*/
            return false;
            // }
        }

        @Override
        public Object getValueAt(int row, int col) {

            // System.out.println("getValueAt PruvFram");
            try {
                tRObj1 = arTRO1.get(row);
                switch (col) {
                    case (0): {
                        try {
                            return df.format(tRObj1.getDatumObjednani());
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    case (1): {
                        return (tRObj1.getNazevSoucasti());

                    }
                    case (2): {
                        return (tRObj1.getTv1().getCislo());

                    }
                    case (3): {
                        return (tRObj1.getTv1().getRevize());

                    }
                    case (4): {
                        return (tRObj1.getCisloObjednavky());
                    }
                    case (5): {
                        return (tRObj1.getCelkemKusu() + " ks");
                    }
                    case (6): {
                        return (tRObj1.getObjednanoKusu() + " ks");
                    }
                    case (7): {
                        return (tRObj1.getOdvolavkaKusu() + " ks");
                    }
                    case (8): {
                        return nf2.format(tRObj1.getCenaKus());
                    }
                    case (9): {
                        return (tRObj1.getMenaZkratka());
                    }
                    case (10): {
                        return (tRObj1.getPoznamka());
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
            // System.out.println("setValueAt PruvFram");           
        }//konec setValueAt
    }//konec modelu tabulky

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Odvolání",
            "Datum objednání",
            "Název součásti",
            "Číslo výkresu",
            "Číslo objednávky",
            "Ks",
            "Ks navíc",
            "Expedice"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTO1.size());
            //  updateZaznamyObjednavka1();
            if (arTO1.size() > 0) {
                tabulkaDetail.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arTO1.remove(tabulkaDetail.getSelectedRow());
            fireTableRowsDeleted(tabulkaDetail.getSelectedRow(), tabulkaDetail.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrSkladPolozka1size() > 0)
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

        /* @Override
         public Class getColumnClass(int col) {
         System.out.println("trida sloupce col je " + getValueAt(0, col).getClass());
         if (col == 1) {
         return Boolean.class;
         }
         if (col == 2 || col == 3) {
         return Integer.class;
         }
         return getValueAt(0, col).getClass();//String.class;
         }*/
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
            return false;
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
                        if (tObj1.getDatumObjednani() != null) {
                            return (df.format(tObj1.getDatumObjednani()));
                        }
                    }
                    case (2): {
                        return (tObj1.getNazevSoucasti());
                    }
                    case (3): {
                        return (tObj1.getTv1().getCislo());
                    }
                    case (4): {
                        return (tObj1.getCisloObjednavky());
                    }
                    case (5): {
                        return (tObj1.getPocetObjednanychKusu() + " ks");
                    }
                    case (6): {
                        return (tObj1.getKusuNavic() + " ks");
                    }
                    case (7): {
                        if (tObj1.getDatumExpedice() != null) {
                            return (df.format(tObj1.getDatumExpedice()));
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

            if (e.getActionCommand().equals("NovaObjednavka")) {
                RamcovaObjednavkaFrame1 novaObjednavka = new RamcovaObjednavkaFrame1("Nová objednávka", "Uložit objednávku");
            }

            if (e.getActionCommand().equals("EditObjednavka")) {
                editObjednavka();
            }

            if (e.getActionCommand().equals("NovaObjednavkaStavajici")) {
                vytvorObjednavkuExistujici();
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
           

            if (tme.getSource() == tableModelHlavni1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmHlavni1) {
                if (e.getValueIsAdjusting() == false) {
                    if (arTRO1.size() > 0) {
                        nastavTabulkuDetail1();
                    }
                }
            }

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
        jCheckBoxFiltrZdrojObjednavka1 = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        NazevObjednavkyTextField1 = new javax.swing.JTextField();
        CisloObjednavkyTextField1 = new javax.swing.JTextField();
        jTFTerminObjednaniOd1 = new javax.swing.JTextField();
        jTFTerminObjednaniDo1 = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLHodnotaZakazek = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jBNovaObjednavka = new javax.swing.JButton();
        jBNovaObjednavkaStavajici = new javax.swing.JButton();
        jBEditObjednavka = new javax.swing.JButton();
        OdebratButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPHlavni = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jSPDetail = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

        jLabel3.setText("Rok objednání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        JPFiltrTop.add(jCBRokDodani, gridBagConstraints);

        jCheckBoxFiltrZdrojObjednavka1.setText("Filtr objednávky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jCheckBoxFiltrZdrojObjednavka1, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo výkresu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel8.setText("Název součásti");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel8, gridBagConstraints);

        jLabel13.setText("Číslo objednávky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel13, gridBagConstraints);

        jLabel15.setText("Objednáno od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel15, gridBagConstraints);

        jLabel19.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel19, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(CisloVykresuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(NazevObjednavkyTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(CisloObjednavkyTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFTerminObjednaniOd1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFTerminObjednaniDo1, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(VyhledatButton1, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBZakaznik.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        jLabel4.setText("Hodnota zakázek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(jLabel4, gridBagConstraints);

        jLHodnotaZakazek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(jLHodnotaZakazek, gridBagConstraints);

        jLabel1.setText("Počet objednávek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel1, gridBagConstraints);

        jLPocetPolozek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(jLPocetPolozek, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBNovaObjednavka.setText("Nová objednávka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaObjednavka, gridBagConstraints);

        jBNovaObjednavkaStavajici.setText("Nová objednávka na základě stávající");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaObjednavkaStavajici, gridBagConstraints);

        jBEditObjednavka.setText("Upravit objednávku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBEditObjednavka, gridBagConstraints);

        OdebratButton1.setText("Smazat objednávku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(OdebratButton1, gridBagConstraints);

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
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
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
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(jSPDetail, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloObjednavkyTextField1;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JTextField NazevObjednavkyTextField1;
    private javax.swing.JButton OdebratButton1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBEditObjednavka;
    private javax.swing.JButton jBNovaObjednavka;
    private javax.swing.JButton jBNovaObjednavkaStavajici;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JCheckBox jCheckBoxFiltrZdrojObjednavka1;
    private javax.swing.JLabel jLHodnotaZakazek;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPDetail;
    private javax.swing.JScrollPane jSPHlavni;
    private javax.swing.JTextField jTFTerminObjednaniDo1;
    private javax.swing.JTextField jTFTerminObjednaniOd1;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    // End of variables declaration//GEN-END:variables
}
