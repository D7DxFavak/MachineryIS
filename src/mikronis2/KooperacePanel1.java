/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Dimension;
import java.awt.event.*;
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
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import eu.data7.tableTools.ColorCellRendererKoop;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import mikronis2.dbtridy.TridaKooperace;
import mikronis2.dbtridy.TridaPruvodka;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class KooperacePanel1 extends javax.swing.JPanel {

    protected TableModel tableModelKooperace1;
    protected TabulkaModelKooperace1 tabulkaModelKooperace1;
    protected ListSelectionModel lsmKooperace1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    private WinUdalosti winUdalosti;
    protected TridaPruvodka tPr1;
    protected ArrayList<TridaPruvodka> arTP1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public KooperacePanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuHlavni();

        nastavPosluchaceUdalostiOvladace();

        this.setVisible(true);

    }

    protected void nastavParametry() {
        arTP1 = new ArrayList<>();
        tPr1 = new TridaPruvodka();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelKooperace1 = new TabulkaModelKooperace1();

        tabulka.setModel(tabulkaModelKooperace1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmKooperace1 = tabulka.getSelectionModel();
        tableModelKooperace1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmKooperace1.removeListSelectionListener(lslUdalosti);
        tableModelKooperace1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelKooperace1.addTableModelListener(tmlUdalosti);
        lsmKooperace1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jCBFiltrKooperace.addActionListener(alUdalosti);
        jBVyhledat.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltr");
        jBNovaKooperace.addActionListener(alUdalosti);
        jBEditKooperace.addActionListener(alUdalosti);
        jBSmazatKooperace.addActionListener(alUdalosti);
        jBVyhledat.setActionCommand("Hledat");
        jCBZakaznik.setActionCommand("Refresh");
        jCBRokDodani.setActionCommand("Hledat");
        jBNovaKooperace.setActionCommand("NovaKooperace");
        jBEditKooperace.setActionCommand("EditKooperace");
        jBSmazatKooperace.setActionCommand("SmazatKooperace");

    }

    protected void nastavTabulkuHlavni() {
        /*TableColumn column = null;
        column = tabulka.getColumnModel().getColumn(0);
        column.setPreferredWidth(80);

        column = tabulka.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(30);

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(115);

        column = tabulka.getColumnModel().getColumn(5);
        column.setPreferredWidth(40);

        column = tabulka.getColumnModel().getColumn(6);
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(7);  // termin dodani
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(8);  // termin potvrzeni
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(9);  // skluz
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(10);
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(11);
        column.setPreferredWidth(100);

        column = tabulka.getColumnModel().getColumn(12);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(13);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(14);
        column.setPreferredWidth(80);

        column = tabulka.getColumnModel().getColumn(15);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(16);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(17);
        column.setPreferredWidth(110);

        column = tabulka.getColumnModel().getColumn(18);
        column.setPreferredWidth(110);

        column = tabulka.getColumnModel().getColumn(19);
        column.setPreferredWidth(110);

        column = tabulka.getColumnModel().getColumn(20);
        column.setPreferredWidth(110);*/
 /*
         * column = tabulka.getColumnModel().getColumn(18);
         * column.setPreferredWidth(110);
         */
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaKooperace1();
        tabulkaModelKooperace1.pridejSadu();
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
        for (int i = 0; i < tabulka.getColumnCount(); i++) {
            out.add(tabulka.getColumnModel().getColumn(i).getPreferredWidth());
        }
        return out;
    }

    protected void setSirkaSloupcu(ArrayList in) {
        for (int i = 0; i < tabulka.getColumnCount(); i++) {
            tabulka.getColumnModel().getColumn(i).setPreferredWidth((Integer) in.get(i));
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

        // roletkaModelRoky.setSelectedIndex(roletkaModelRoky.getSize() - 1);
        jCBRokDodani.setModel(roletkaModelRoky);
    }

    protected void getDataTabulkaKooperace1() {

        if (tabulka.getCellEditor() != null) {
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTP1.clear();

        tabulkaModelKooperace1.oznamZmenu();

        try {
            String dotaz = "";

            dotaz = "SELECT pruvodka_kooperace_pruvodka_id, vykres.vykresy_id, vykres.vykresy_cislo, vykres.vykresy_revize, postup.pracovni_postup_pruvodka_popis, "
                    + "pruvodka_kooperace_datum_odeslani, pruvodka_kooperace_pocet_odeslani, pruvodka_kooperace_datum_prijeti, pruvodka_kooperace_pocet_prijeti, "
                    + "pruvodky_nazev, vykres.vykresy_zakaznik_id, pruvodka_kooperace_poradi "
                    + "FROM spolecne.pruvodka_kooperace "
                    + "CROSS JOIN spolecne.pruvodky "
                    + "LEFT JOIN (SELECT vykresy_id, vykresy_cislo, vykresy_revize, vykresy_zakaznik_id "
                    + "FROM spolecne.vykresy) AS vykres "
                    + "ON vykres.vykresy_id = pruvodky_cislo_vykresu "
                    + "LEFT JOIN (SELECT pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_poradi, pracovni_postup_pruvodka_popis "
                    + "FROM spolecne.pracovni_postup_pruvodka) AS postup "
                    + "ON (postup.pracovni_postup_pruvodka_pruvodka_id = pruvodka_kooperace_pruvodka_id  "
                    + "AND postup.pracovni_postup_pruvodka_poradi = pruvodka_kooperace_poradi) "
                    + "WHERE pruvodky_id = pruvodka_kooperace_pruvodka_id ";

            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND ((EXTRACT(YEAR FROM pruvodka_kooperace_datum_odeslani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + ") OR  "
                        + "(EXTRACT(YEAR FROM pruvodka_kooperace_datum_prijeti) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + "))";
            }
            if (jTFCisloVykresu.getText().length() > 0) {
                dotaz += "AND vykres.vykresy_cislo LIKE '%" + jTFCisloVykresu.getText().trim() + "%' ";
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND vykres.vykresy_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";

            }
            if (jTFNazevPruvodky.getText().length() > 0) {
                dotaz += "AND pruvodky_nazev ILIKE '%" + jTFNazevPruvodky.getText().trim() + "%' ";
            }

            boolean osetritDatumOd = TextFunkce1.osetriDatum(jTFTerminOd.getText().trim());
            boolean osetritDatumDo = TextFunkce1.osetriDatum(jTFTerminDo.getText().trim());

            if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                /*
                dotaz += " AND objednavky_termin_dodani BETWEEN '" + jTFTerminOd.getText().trim() + "' AND '"
                        + jTFTerminDo.getText() + "'";
                 */
            }

            if (jCBFiltrKooperace.isSelected() == true) {
                // dotaz += "AND objednavky_datum_expedice IS NULL AND objednavky_cislo_faktury IS NULL ";
                dotaz += "AND opruvodka_kooperace_datum_prijeti IS NULL ";
            }
            dotaz += "ORDER BY pruvodka_kooperace_datum_prijeti DESC, pruvodka_kooperace_datum_odeslani DESC ";

            //System.out.println("Dotaz KOOP : " + dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            int poradi = 1;
            while (q.next()) {
                tPr1 = new TridaPruvodka();
                tPr1.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tPr1.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tPr1.setTv1(new TridaVykres1());
                tPr1.getTv1().setCislo(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                tPr1.getTv1().setRevize(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                tPr1.getTv1().setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(11)));
                TridaKooperace tk1 = new TridaKooperace();
                tk1.setIdPruvodka(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tk1.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tk1.setDatumOdeslani(q.getDate(6));
                tk1.setPocetOdeslano(SQLFunkceObecne.osetriCteniInt(q.getInt(7)));
                tk1.setDatumPrijeti(q.getDate(8));
                tk1.setPocetPrijato(SQLFunkceObecne.osetriCteniInt(q.getInt(9)));
                tk1.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(12)));
                tPr1.setAktualniKooperace(tk1);
                tPr1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(10)));
                arTP1.add(tPr1);
                poradi++;
            }// konec while                                   

        } // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        jLPocetPolozek.setText(arTP1.size() + "");

        obarvitTabulku();
        tabulkaModelKooperace1.oznamZmenu();

    } //konec getDataTabulkaObjednavka1

    private void obarvitTabulku() {               
        
        int index[][] = new int[arTP1.size()][tabulkaModelKooperace1.getColumnCount()];
        int index2[][] = new int[arTP1.size()][tabulkaModelKooperace1.getColumnCount()];       
        for (int row = 0; row < arTP1.size(); row++) {          
            TridaKooperace tk = arTP1.get(row).getAktualniKooperace();
            if(tk.getPocetPrijato() > 0 && tk.getPocetPrijato() != tk.getPocetOdeslano()) {
                index[row][8] = 1;
            }
            
            index2[row][5] = 2;
            index2[row][7] = 2;
            index2[row][6] = 1;
            index2[row][8] = 1;
            
            // System.out.println("class : " + tabulkaModelObjednavka1.getColumnClass(19).getName());
            //}
            //tabulka.se(Object.class, new ColorCellRenderer(index,index2,index3,index4, backColor,frontColor));
            // tabulka.getDefaultRenderer(tabulkaModelObjednavka1.getColumnClass(5)).getTableCellRendererComponent(tabulka, ui, true, true, WIDTH, WIDTH);
        }
        Color backColor = new Color(255, 204, 204);
        Color frontColor = Color.BLACK;

        for (int i = 0; i < tabulkaModelKooperace1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulka.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRendererKoop(index, index2, backColor, frontColor));
        }
    }

    protected void vycistiFiltr() {

        jTFNazevPruvodky.setText("");
        jTFTerminOd.setText("");
        jTFTerminDo.setText("");
        jTFCisloVykresu.setText("");
    }

    public void editKooperace() {
        KooperaceFrame1 kooperace = new KooperaceFrame1(arTP1.get(tabulka.getSelectedRow()), true, "Upravit kooperaci");
        kooperace.addWindowListener(winUdalosti);
    }

    public void novaKooperace() {
        KooperaceFrame1 kooperace = new KooperaceFrame1(false, "Nová kooperace");
        kooperace.addWindowListener(winUdalosti);
    }

    public void smazatKooperace() {
        TridaKooperace tk = arTP1.get(tabulka.getSelectedRow()).getAktualniKooperace();
        int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání kooperace",
                "Opravdu chcete smazat kooperaci ? ", 1);
        if (ud == JOptionPane.YES_OPTION) {
            try {
                int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");

                String dotaz = "DELETE FROM spolecne.pruvodka_kooperace "
                        + "WHERE pruvodka_kooperace_pruvodka_id = " + tk.getIdPruvodka() + " "
                        + "AND pruvodka_kooperace_poradi = " + tk.getPoradi();
                int a = PripojeniDB.dotazIUD(dotaz);
                System.out.println("DELETE koop " + dotaz);
                rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            } catch (Exception e) {
                int rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
            }
        }
        tabulkaModelKooperace1.uberJednu();
    }

    protected class TabulkaModelKooperace1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Průvodka",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Zpracování",
            "Datum odeslání",
            "Počet odesláno",
            "Datum přijetí",
            "Počet přijato",
            "Poznámka",};

        public void pridejSadu() {
            System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTP1.size());
            //  updateZaznamyObjednavka1();
            if (arTP1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaPruvodka tPR) {
            arTP1.add(tPR);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulka.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arTP1.remove(tabulka.getSelectedRow());
            fireTableRowsDeleted(tabulka.getSelectedRow(), tabulka.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (arTO1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(tabulka.getSelectedRow(), tabulka.getSelectedRow());
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
            return arTP1.size();
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
            if (col == 19 || col == 20) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaPruvodka tPR) {
            //System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulka.getSelectedRow());
            return nastavHodnotuNaPozici(tPR, tabulka.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaPruvodka tPR, int pozice) {
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
                tPr1 = arTP1.get(row);
                switch (col) {
                    case (0): {
                        return (tPr1.getId());
                    }
                    case (1): {
                        return (tPr1.getNazev());
                    }
                    case (2): {
                        return (tPr1.getTv1().getCislo());
                    }
                    case (3): {
                        return (tPr1.getTv1().getRevize());
                    }
                    case (4): {
                        return (tPr1.getAktualniKooperace().getPopis());
                    }
                    case (5): {
                        if (tPr1.getAktualniKooperace().getDatumOdeslani() != null) {
                            return (df.format(tPr1.getAktualniKooperace().getDatumOdeslani()));
                        } else {
                            return "";
                        }
                    }
                    case (6): {
                        if (tPr1.getAktualniKooperace().getPocetOdeslano() > 0) {
                            return (tPr1.getAktualniKooperace().getPocetOdeslano() + " ks");
                        } else {
                            return "";
                        }
                    }
                    case (7): {
                        if (tPr1.getAktualniKooperace().getDatumPrijeti() != null) {
                            return (df.format(tPr1.getAktualniKooperace().getDatumPrijeti()));
                        } else {
                            return "";
                        }
                    }
                    case (8): {
                        if (tPr1.getAktualniKooperace().getPocetPrijato() > 0) {
                            return (tPr1.getAktualniKooperace().getPocetPrijato() + " ks");
                        } else {
                            return "";
                        }
                    }
                    case (9): {
                        tPr1.getAktualniKooperace().getPoznamka();
                    }
                    default: {
                        return null;
                    }
                }//konec switch
            }//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try//konec try
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
        }//konec setValueAt
    }//konec modelu tabulky

    class ALUdalosti implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //  System.out.println("action : " + e.getActionCommand());
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("Hledat") || e.getActionCommand().equals("VycistiFiltr")) {
                if (e.getActionCommand().equals("VycistiFiltr")) {
                    vycistiFiltr();
                }
                MikronIS2.indexZakaznika = ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo();
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaKooperace1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }

            if (e.getActionCommand().equals("Hledat")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaKooperace1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }

            if (e.getActionCommand().equals("NovaKooperace")) {
                novaKooperace();
            }

            if (e.getActionCommand().equals("SmazatKooperace")) {
                smazatKooperace();
            }

            if (e.getActionCommand().equals("EditKooperace")) {
                if (tabulka.getSelectedRow() > -1) {
                    editKooperace();
                } else {
                    JednoducheDialogy1.warnAno(MikronIS2.ramecAplikace, "Úprava kooperace", "Nejdříve vyberte kooperaci pro úpravu");
                }
            }

        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {

            if (tme.getSource() == tableModelKooperace1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmKooperace1) {
                if (e.getValueIsAdjusting() == false) {
                    //nastavVyberTabulkyObjednavka1();
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
            //Timer timer = new Timer(500, task); //fire every half second
            //timer.setInitialDelay(2000);        //first delay 2 seconds
            //timer.start();
        }

        public void windowClosed(WindowEvent e) {
            if (e.getSource().getClass().getSimpleName().equals("KooperaceFrame1")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaKooperace1();
                tabulkaModelKooperace1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();               
            }

            //displayMessage("WindowListener method called: windowClosed.");
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
        JButtonVycistiFiltrObjednavka1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCBRokDodani = new javax.swing.JComboBox();
        jCBFiltrKooperace = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFCisloVykresu = new javax.swing.JTextField();
        jTFNazevPruvodky = new javax.swing.JTextField();
        jTFTerminOd = new javax.swing.JTextField();
        jTFTerminDo = new javax.swing.JTextField();
        jBVyhledat = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jBNovaKooperace = new javax.swing.JButton();
        jBEditKooperace = new javax.swing.JButton();
        jBSmazatKooperace = new javax.swing.JButton();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

        jLabel3.setText("Rok zpracování :");
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

        jCBFiltrKooperace.setText("Filtr aktuální kooperace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        JPFiltrTop.add(jCBFiltrKooperace, gridBagConstraints);

        jLabel2.setText("Počet kooperací :  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        JPFiltrTop.add(jLabel2, gridBagConstraints);

        jLPocetPolozek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jLPocetPolozek, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo výkresu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel8.setText("Název součásti");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel8, gridBagConstraints);

        jLabel16.setText("Termín od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);

        jLabel17.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFCisloVykresu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFNazevPruvodky, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFTerminOd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFTerminDo, gridBagConstraints);

        jBVyhledat.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jBVyhledat, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBZakaznik.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        tabulka.setModel(new javax.swing.table.DefaultTableModel(
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
        jSPTabulka.setViewportView(tabulka);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBNovaKooperace.setText("Nová kooperace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaKooperace, gridBagConstraints);

        jBEditKooperace.setText("Upravit kooperaci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBEditKooperace, gridBagConstraints);

        jBSmazatKooperace.setText("Smazat kooperaci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazatKooperace, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
            .addComponent(jSPTabulka)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSPTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JButton jBEditKooperace;
    private javax.swing.JButton jBNovaKooperace;
    private javax.swing.JButton jBSmazatKooperace;
    private javax.swing.JButton jBVyhledat;
    private javax.swing.JCheckBox jCBFiltrKooperace;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JTextField jTFCisloVykresu;
    private javax.swing.JTextField jTFNazevPruvodky;
    private javax.swing.JTextField jTFTerminDo;
    private javax.swing.JTextField jTFTerminOd;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
