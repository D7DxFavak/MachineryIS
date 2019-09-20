/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import eu.data7.tableTools.ColorCellRenderer1;
import eu.data7.tableTools.ColorCellRendererPruvCasy1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import mikronis2.dbtridy.TridaPracovniPostupPruv1;
import mikronis2.dbtridy.TridaProgram1;
import mikronis2.dbtridy.TridaPruvodka;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.DvojiceRetezRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class PruvodkyPanel extends javax.swing.JPanel {

    protected TableModel tableModelPruvodka1;
    protected TableModel tableModelProgramy1;
    protected TableModel tableModelPolotovary1;
    protected TableModel tableModelPopisy1;
    protected TableModel tableModelZamestnanci1;
    protected TabulkaModelPruvodka1 tabulkaModelPruvodka1;
    protected TabulkaModelProgramy1 tabulkaModelProgramy1;
    protected TabulkaModelPopisy1 tabulkaModelPopisy1;
    protected TabulkaModelPolotovary1 tabulkaModelPolotovary1;
    protected TabulkaModelZamestnanci1 tabulkaModelZamestnanci1;
    protected TabulkaModelCasy tabulkaModelCasy1;
    protected Vector vrZamestnanci1;
    protected Vector vsZamestnanci1;
    protected Vector vrZamestnanciFiltr1;
    protected Vector vsZamestnanciFiltr1;
    protected Vector vsCasVector1;
    protected Vector vrCasVector1;
    protected Vector vrPolotovar1;
    protected Vector vsPolotovar1;
    protected ListSelectionModel lsmPruvodka1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    private WinUdalosti winUdalosti;
    protected TridaPruvodka tPr1;
    protected ArrayList<TridaPruvodka> arPr1;
    protected ArrayList<DvojiceRetezRetez> arCasy;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    private long celkovyCasTornado = 0;
    private long celkovyCasMCV = 0;
    private long celkovyCasDMU = 0;
    private long celkovyCasDrat = 0;
    private long celkovyCasZamestnanci = 0;
    protected int indexOznaceno;
    // private int idOznaceno = -1;

    /**
     * Creates new form ObjednavkyPanel
     */
    public PruvodkyPanel() {
        initComponents();

        //idOznaceno = -1;
        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuPruvodka1();
        nastavTabulkuPopis1();
        nastavTabulkuZamestnanci1();
        nastavTabulkuCasy1();

        nastavPosluchaceUdalostiOvladace();

        this.setVisible(true);

    }

    protected void nastavParametry() {
        arPr1 = new ArrayList<TridaPruvodka>();
        arCasy = new ArrayList<DvojiceRetezRetez>();
        tPr1 = new TridaPruvodka();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

        vrPolotovar1 = new Vector();
        vrZamestnanci1 = new Vector();
        vrZamestnanciFiltr1 = new Vector();

        vsZamestnanci1 = new Vector();

        vsZamestnanciFiltr1 = new Vector();
        vsCasVector1 = new Vector();
        vrCasVector1 = new Vector();

    }

    protected void nastavParametryTabulek() {
        tabulkaModelPruvodka1 = new TabulkaModelPruvodka1();
        tabulkaModelProgramy1 = new TabulkaModelProgramy1();
        tabulkaModelPolotovary1 = new TabulkaModelPolotovary1();
        tabulkaModelPopisy1 = new TabulkaModelPopisy1();
        tabulkaModelZamestnanci1 = new TabulkaModelZamestnanci1();
        tabulkaModelCasy1 = new TabulkaModelCasy();

        jTablePruvodkyProgramy.setModel(tabulkaModelProgramy1);
        jTablePruvodkyProgramy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePruvodkyProgramy.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jTablePruvodkyPopisy1.setModel(tabulkaModelPopisy1);
        jTablePruvodkyPopisy1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePruvodkyPopisy1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jTablePruvodkyPolotovary.setModel(tabulkaModelPolotovary1);
        jTablePruvodkyPolotovary.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePruvodkyPolotovary.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jTablePruvodkyZamestnanci.setModel(tabulkaModelZamestnanci1);
        jTablePruvodkyZamestnanci.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePruvodkyZamestnanci.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        jTablePruvodkyCasy.setModel(tabulkaModelCasy1);
        jTablePruvodkyCasy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePruvodkyCasy.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabulkaModelPruvodka1 = new TabulkaModelPruvodka1();

        tabulka.setModel(tabulkaModelPruvodka1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmPruvodka1 = tabulka.getSelectionModel();
        tableModelPruvodka1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmPruvodka1.removeListSelectionListener(lslUdalosti);
        tableModelPruvodka1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelPruvodka1.addTableModelListener(tmlUdalosti);
        lsmPruvodka1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jCBFiltrZdroj.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUzavrit.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBNova.addActionListener(alUdalosti);
        jBUpravit.addActionListener(alUdalosti);
        jBNovaStavajici.addActionListener(alUdalosti);
        VyhledatButton1.setActionCommand("Refresh");
        jBUzavrit.setActionCommand("UzavritPruvodka");
        jCBZakaznik.setActionCommand("Refresh");
        jCBRokDodani.setActionCommand("Refresh");
        jBNova.setActionCommand("NovaPruvodka");
        jBUpravit.setActionCommand("EditPruvodka");
        jBNovaStavajici.setActionCommand("NovaPruvodkaStavajici");

    }

    protected void nastavTabulkuPruvodka1() {
        /*double wKoef = 1060 / 765.0;
        TableColumn column = null;
        column = tabulka.getColumnModel().getColumn(0);
        column.setPreferredWidth(new Double(80 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(1);
        column.setPreferredWidth(new Double(120 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(new Double(100 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(new Double(30 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(new Double(115 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(5);
        column.setPreferredWidth(new Double(40 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(6);
        column.setPreferredWidth(new Double(60 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(7);
        column.setPreferredWidth(new Double(60 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(8);
        column.setPreferredWidth(new Double(60 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(9);
        column.setPreferredWidth(new Double(60 * wKoef).intValue());

        column = tabulka.getColumnModel().getColumn(10);
        column.setPreferredWidth(new Double(100 * wKoef).intValue());*/

        //   System.out.println("Pokus :  " + new Double(100 * wKoef).intValue());
        /*
         * column = tabulka.getColumnModel().getColumn(18);
         * column.setPreferredWidth(110);
         */
        refreshData();

    }// konec nastavTabulkuBT1

    protected void refreshData() {
        indexOznaceno = tabulka.getSelectedRow();
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaPruvodka1();
        tabulkaModelPruvodka1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arPr1.size() > 0) {
            if (indexOznaceno != 0 && indexOznaceno < arPr1.size()) {
                ListSelectionModel selectionModel
                        = tabulka.getSelectionModel();
                selectionModel.setSelectionInterval(indexOznaceno, indexOznaceno);
                tabulka.scrollRectToVisible(tabulka.getCellRect(indexOznaceno, 0, false));
            }
            getDetailPruvodka1();
        }
    }

    protected void nastavTabulkuZamestnanci1() {
        //System.out.println("nastavTabulkuZamestnanci1()");
        TableColumn column = null;
        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(0);//Zamestnanec
        column.setPreferredWidth(95);

        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(1);//Stroj
        column.setPreferredWidth(85);

        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(2);//Cas
        column.setPreferredWidth(70);

        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(3);//Cas
        column.setPreferredWidth(85);

        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(4);//Cas
        column.setPreferredWidth(85);

        column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(5);//Cas
        column.setPreferredWidth(85);
    }

    protected void nastavTabulkuPopis1() {
        TableColumn column = null;
        column = jTablePruvodkyPopisy1.getColumnModel().getColumn(0);//ID
        column.setPreferredWidth(100);

        column = jTablePruvodkyPopisy1.getColumnModel().getColumn(1);//Nazev
        column.setCellRenderer(new TableVyberRenderer1("SELECT druhy_stroju_id, druhy_stroju_nazev FROM spolecne.druhy_stroju ORDER BY druhy_stroju_priorita",
                "Chyba při načítání druhů strojů - v databázi pravděpodobně nejsou zadány žádné druhy strojů"));
        column.setCellEditor(new TableVyberEditor1("SELECT druhy_stroju_id, druhy_stroju_nazev FROM spolecne.druhy_stroju ORDER BY druhy_stroju_priorita",
                "Chyba při načítání druhů strojů - v databázi pravděpodobně nejsou zadány žádné druhy strojů"));
        column.setPreferredWidth(140);

        column = jTablePruvodkyPopisy1.getColumnModel().getColumn(2);//Nazev
        column.setPreferredWidth(731);

    }

    protected void nastavTabulkuCasy1() {
        TableColumn column = null;
        column = jTablePruvodkyCasy.getColumnModel().getColumn(0);//ID
        column.setPreferredWidth(200);

        column = jTablePruvodkyCasy.getColumnModel().getColumn(1);//Nazev       
        column.setPreferredWidth(140);
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
                "SELECT DISTINCT cast(date_part('year', pruvodky_termin_dokonceni) AS integer), date_part('year', pruvodky_termin_dokonceni) AS roky FROM spolecne.pruvodky ORDER BY roky", "Neurčen", null,
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

    protected void getDataTabulkaPruvodka1() {

        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arPr1.clear();
        tabulkaModelPruvodka1.oznamZmenu();

        try {
            String dotaz;
            if (jCBFiltrZdroj.isSelected() == true) {
                dotaz = "SELECT * FROM (SELECT vykresy_cislo, "
                        + "pruvodky_id, pruvodky_nazev, vykresy_revize, "
                        + "pruvodky_termin_dokonceni, pruvodky_pocet_kusu, "
                        + "pruvodky_pocet_kusu_sklad, pruvodky_polotovar_pocet_kusu, "
                        + "pruvodky_vyrobeno_kusu, pruvodky_poznamky, vykresy_id, "
                        + "pruvodky_objednavky_id, pruvodky_polotovar_id, pruvodky_narezano_sklad "
                        + "FROM spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE pruvodky_cislo_vykresu = vykresy_id "
                        + "AND pruvodky_vyrobeno_kusu IS NULL "
                        + "AND pruvodky.pruvodky_nazev LIKE '%" + jTFNazev.getText().trim() + "%' "
                        + "AND vykresy.vykresy_cislo LIKE '%" + jTFCisloVykresu.getText().trim() + "%' ";
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += " AND vykresy_zakaznik_id  =  " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND EXTRACT(YEAR FROM pruvodky_termin_dokonceni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
                }
                if (jTextFieldTerminDodaniOd1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni >= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniOd1.getText().trim()) + " ";
                }
                if (jTextFieldTerminDodaniDo1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni <= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniDo1.getText().trim()) + " ";
                }
                if (jTFCisloPruvodky.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky.pruvodky_id = " + jTFCisloPruvodky.getText().trim() + " ";
                }
                dotaz += "ORDER BY vykresy_cislo, vykresy_revize DESC, pruvodky_termin_dokonceni DESC) AS pruvodky1 "
                        + "UNION SELECT * FROM "
                        + "(SELECT DISTINCT ON (vykresy_cislo) vykresy_cislo, "
                        + "pruvodky_id, pruvodky_nazev, vykresy_revize, "
                        + "pruvodky_termin_dokonceni, pruvodky_pocet_kusu, "
                        + "pruvodky_pocet_kusu_sklad, pruvodky_polotovar_pocet_kusu, "
                        + "pruvodky_vyrobeno_kusu, pruvodky_poznamky, vykresy_id, "
                        + "pruvodky_objednavky_id, pruvodky_polotovar_id, pruvodky_narezano_sklad  "
                        + "FROM spolecne.pruvodky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE pruvodky_cislo_vykresu = vykresy_id "
                        + "AND pruvodky_vyrobeno_kusu IS NOT NULL "
                        + "AND vykresy_cislo NOT IN (SELECT vykresy_cislo FROM spolecne.pruvodky CROSS JOIN spolecne.vykresy "
                        + "WHERE pruvodky_vyrobeno_kusu IS NULL AND pruvodky_cislo_vykresu = vykresy_id) "
                        + "AND pruvodky.pruvodky_nazev LIKE '%" + jTFNazev.getText().trim() + "%' "
                        + "AND vykresy.vykresy_cislo LIKE '%" + jTFCisloVykresu.getText().trim() + "%' ";
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += " AND vykresy_zakaznik_id  =  " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND EXTRACT(YEAR FROM pruvodky_termin_dokonceni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
                }
                if (jTextFieldTerminDodaniOd1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni >= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniOd1.getText().trim()) + " ";
                }
                if (jTextFieldTerminDodaniDo1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni <= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniDo1.getText().trim()) + " ";
                }
                if (jTFCisloPruvodky.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky.pruvodky_id = " + jTFCisloPruvodky.getText().trim() + " ";
                }
                dotaz += "ORDER BY vykresy_cislo, vykresy_revize DESC , pruvodky_termin_dokonceni DESC) "
                        + "AS pruvodky2 "
                        + "ORDER BY vykresy_cislo, vykresy_revize DESC, pruvodky_termin_dokonceni ASC, pruvodky_vyrobeno_kusu DESC";
            } else {
                dotaz = "SELECT vykresy_cislo, pruvodky_id, pruvodky_nazev,  vykresy_revize, pruvodky_termin_dokonceni, "
                        + "pruvodky_pocet_kusu, pruvodky_pocet_kusu_sklad, pruvodky_polotovar_pocet_kusu, pruvodky_vyrobeno_kusu, pruvodky_poznamky, "
                        + "vykresy_id, pruvodky_objednavky_id, pruvodky_polotovar_id, pruvodky_narezano_sklad  "
                        + "FROM spolecne.pruvodky JOIN spolecne.vykresy ON pruvodky_cislo_vykresu = vykresy_id "
                        + "WHERE vykresy_zakaznik_id IS NOT NULL "
                        + "AND pruvodky.pruvodky_nazev LIKE '%" + jTFNazev.getText().trim() + "%' "
                        + "AND vykresy.vykresy_cislo LIKE '%" + jTFCisloVykresu.getText().trim() + "%' ";
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += " AND vykresy_zakaznik_id  =  " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                if (jTFCisloPruvodky.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky.pruvodky_id = " + jTFCisloPruvodky.getText().trim() + " ";
                }
                if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND EXTRACT(YEAR FROM pruvodky_termin_dokonceni) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
                }
                if (jTextFieldTerminDodaniOd1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni >= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniOd1.getText().trim()) + " ";
                }
                if (jTextFieldTerminDodaniDo1.getText().trim().length() > 0) {
                    dotaz += "AND pruvodky_termin_dokonceni <= " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniDo1.getText().trim()) + " ";
                }
                dotaz += "ORDER BY vykresy_cislo, vykresy_revize, pruvodky_termin_dokonceni ASC";
            }
            // System.out.println("dotaz: " + dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);

            while (q.next()) {
                tPr1 = new TridaPruvodka();
                tPr1.setTv1(new TridaVykres1());
                tPr1.getTv1().setCislo(SQLFunkceObecne.osetriCteniString(q.getString(1)));
                /* if (idOznaceno > -1 && SQLFunkceObecne.osetriCteniInt(q.getInt(2)) == idOznaceno) {
                 indexOznaceno = arPr1.size();
                 // System.out.println("indO1 " + indexOznaceno);
                 }*/
                tPr1.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tPr1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                tPr1.getTv1().setRevize(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                tPr1.setTerminDokonceni(q.getDate(5));
                tPr1.setPocetKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                tPr1.setPocetKusuSklad(SQLFunkceObecne.osetriCteniInt(q.getInt(7)));
                tPr1.setPocetKusuPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
                tPr1.setVyrobenoKusu((q.getInt(9)));
                tPr1.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(10)));
                tPr1.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(11)));
                tPr1.getTv1().setIdVykres(tPr1.getIdVykres());
                tPr1.setIdObjednavky(q.getInt(12));
                tPr1.setIdPolotovar(q.getInt(13));
                tPr1.setNarezanoZeSkladu(SQLFunkceObecne.osetriCteniInt(q.getInt(14)));
                if (q.getObject(9) == null) {
                    tPr1.setUzavrena(false);
                } else {
                    tPr1.setUzavrena(true);
                }
                arPr1.add(tPr1);
            }// konec while

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch       

        //jTextFieldPocetKusuObjednavka1.setText(pocetKusuObjednavky + "");
        jLPocetPolozek.setText(arPr1.size() + "");
        obarvitTabulku();

    } //konec getDataTabulkaPruvodka1

    protected void getDetailPruvodka1() {
        if (tabulka.getSelectedRow() >= 0) {
            /*  if (idOznaceno > -1) {
             if (indexOznaceno < arPr1.size()) {
             ListSelectionModel selectionModel
             = tabulka.getSelectionModel();
             selectionModel.setSelectionInterval(indexOznaceno, indexOznaceno);
             tabulka.scrollRectToVisible(tabulka.getCellRect(indexOznaceno, 0, false));
             }
             } else {*/
            indexOznaceno = tabulka.getSelectedRow();
            //}           
            tPr1 = new TridaPruvodka(arPr1.get(tabulka.getSelectedRow()).getId());
            getDataTabulkaPolotovary1(tPr1.getId());
            getDataTabulkaZamestnanci1(tPr1.getId());
            tabulkaModelPolotovary1.oznamZmenu();
            tabulkaModelPopisy1.oznamZmenu();
            tabulkaModelProgramy1.oznamZmenu();
        }
    }

    protected void getDataTabulkaPolotovary1(long id) {
        int polotovarId = -1;
        if (jTablePruvodkyPolotovary.getCellEditor() != null) {
            jTablePruvodkyPolotovary.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        vrPolotovar1 = new Vector();
        vsPolotovar1 = new Vector();
        tabulkaModelPolotovary1.oznamZmenu();
        //nacist data
        try {

            ResultSet pruvodka1 = PripojeniDB.dotazS(
                    "SELECT polotovary_id, polotovary_nazev, "
                    + "COALESCE(typ_materialu_nazev, ''), COALESCE(typ_materialu_norma, ''), "
                    + "pruvodky_polotovar_rozmer, pruvodky_material_prumerna_delka "
                    + "FROM spolecne.polotovary "
                    + "CROSS JOIN spolecne.typ_materialu "
                    + "CROSS JOIN spolecne.pruvodky "
                    + "WHERE pruvodky_polotovar_id = polotovary_id "
                    + "AND pruvodky_id= " + id + " "
                    + "AND typ_materialu.typ_materialu_id = polotovary.polotovary_typ_materialu ");

            while (pruvodka1.next()) {
                vsPolotovar1 = new Vector();
                vsPolotovar1.add("ID");
                vsPolotovar1.add(new Integer(pruvodka1.getInt(1)));
                vrPolotovar1.add(vsPolotovar1);
                polotovarId = pruvodka1.getInt(1);

                vsPolotovar1 = new Vector();
                vsPolotovar1.add("Rozměr");
                vsPolotovar1.add(pruvodka1.getString(2));
                vrPolotovar1.add(vsPolotovar1);

                vsPolotovar1 = new Vector();
                vsPolotovar1.add("Materiál");
                vsPolotovar1.add(pruvodka1.getString(3) + " " + pruvodka1.getString(4));
                vrPolotovar1.add(vsPolotovar1);

                vsPolotovar1 = new Vector();
                vsPolotovar1.add("Polotovar");
                vsPolotovar1.add(pruvodka1.getString(5));
                vrPolotovar1.add(vsPolotovar1);

                vsPolotovar1 = new Vector();
                vsPolotovar1.add("Délka [mm]");
                vsPolotovar1.add(pruvodka1.getInt(6));
                vrPolotovar1.add(vsPolotovar1);

            }

            String alokovanoPruvodky = SQLFunkceObecne2.selectStringPole("SELECT SUM(pruvodky_material_prumerna_delka * pruvodky_polotovar_pocet_kusu) AS alokovano "
                    + "FROM spolecne.pruvodky "
                    + "WHERE pruvodky_odectena IS FALSE "
                    + "AND pruvodky_polotovar_id = " + polotovarId);

            vsPolotovar1 = new Vector();
            vsPolotovar1.add("V průvodkách [mm]");
            if (!alokovanoPruvodky.isEmpty()) {
                vsPolotovar1.add(Math.round(Float.parseFloat(alokovanoPruvodky)));
            } else {
                vsPolotovar1.add(0);
            }
            vrPolotovar1.add(vsPolotovar1);

            String naSklade = (SQLFunkceObecne2.selectStringPole("SELECT CAST( "
                    + "(SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                    + "FROM logistika.sklady_polotovary_transakce "
                    + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                    + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS TRUE "
                    + "AND sklady_polotovary_transakce_polotovar_id = " + polotovarId + ") "
                    + " - (SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                    + "FROM logistika.sklady_polotovary_transakce "
                    + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                    + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS FALSE "
                    + "AND sklady_polotovary_transakce_polotovar_id = " + polotovarId + ") AS TEXT)"));

            vsPolotovar1 = new Vector();
            vsPolotovar1.add("Na skladě [mm]");
            vsPolotovar1.add(Math.round(Float.valueOf(naSklade).floatValue()));
            vrPolotovar1.add(vsPolotovar1);

            //  System.out.println("VRPolotovar : " + vrPolotovar1.size());
            tabulkaModelPolotovary1.oznamZmenu();

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

    } //konec getDataTabulkaPruvodka1

    protected void getDataTabulkaZamestnanci1(long id) {
        vrZamestnanci1.removeAllElements();
        vsZamestnanci1.removeAllElements();
        vrZamestnanciFiltr1.removeAllElements();
        vsZamestnanciFiltr1.removeAllElements();
        vsCasVector1.removeAllElements();
        vrCasVector1.removeAllElements();
        tabulkaModelZamestnanci1.oznamZmenu();
        celkovyCasZamestnanci = 0;
        celkovyCasTornado = 0;
        celkovyCasMCV = 0;
        celkovyCasDMU = 0;
        celkovyCasDrat = 0;
        int strojeDruh = 0;
        int zamestnanecId = 0;
        String strojNazev = "";
        String zamestnanecJmeno = "";
        Vector zamestnanciIdPomocne = new Vector();
        Vector razeniPomocne = new Vector();
        try {
            ResultSet zamestnanciTransakce = PripojeniDB.dotazS(
                    "select * from ( "
                    + "select distinct on (zamestnanci_stroje_transakce_zamestnanci_id) zamestnanci_stroje_transakce_zamestnanci_id, "
                    + "zamestnanci_stroje_transakce_log_timestamp "
                    + "from spolecne.zamestnanci_stroje_transakce "
                    + "WHERE zamestnanci_stroje_transakce_pruvodky_id =  " + id + "  )  "
                    + "foo order by zamestnanci_stroje_transakce_log_timestamp");

            while (zamestnanciTransakce.next()) {
                zamestnanciIdPomocne.add(zamestnanciTransakce.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int z = 0; z < zamestnanciIdPomocne.size(); z++) {
            Vector strojeIdPomocne = new Vector();
            try {
                ResultSet strojeTransakce = PripojeniDB.dotazS(
                        "SELECT DISTINCT(zamestnanci_stroje_transakce_stroje_id) zamestnanci_stroje_transakce_stroje_id  "
                        + "FROM spolecne.zamestnanci_stroje_transakce "
                        + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + id + " "
                        + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + (Integer) zamestnanciIdPomocne.get(z) + " "
                        + "ORDER BY zamestnanci_stroje_transakce_stroje_id ASC");
                while (strojeTransakce.next()) {
                    strojeIdPomocne.add(strojeTransakce.getInt(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < strojeIdPomocne.size(); i++) {
                vrZamestnanci1.removeAllElements();
                vsZamestnanci1.removeAllElements();

                try {
                    ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                            "SELECT zamestnanci_stroje_transakce_id, "
                            + "zamestnanci_stroje_transakce_druh_id, "
                            + "stroje_id, "
                            + "stroje_nazev, "
                            + "stroje_druh_stroje, "
                            + "zamestnanci_id, "
                            + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                            + "zamestnanci_stroje_transakce_log_timestamp, "
                            + "current_timestamp "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "CROSS JOIN spolecne.zamestnanci "
                            + "CROSS JOIN spolecne.stroje "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + id + " "
                            + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                            + "AND stroje_id = zamestnanci_stroje_transakce_stroje_id "
                            + "AND stroje_id = " + (Integer) strojeIdPomocne.get(i) + " "
                            + "AND zamestnanci_id = " + (Integer) zamestnanciIdPomocne.get(z) + " "
                            + "ORDER BY zamestnanci_stroje_transakce_log_timestamp ASC,zamestnanci_stroje_transakce_zamestnanci_id, zamestnanci_stroje_transakce_stroje_id");
                    while (transakceZamestnanci.next()) {
                        vsZamestnanci1 = new Vector();
                        vsZamestnanci1.add(transakceZamestnanci.getLong(1));    // transakce
                        vsZamestnanci1.add(transakceZamestnanci.getInt(2));     // druh
                        vsZamestnanci1.add(transakceZamestnanci.getInt(3));     // stroj id
                        vsZamestnanci1.add(transakceZamestnanci.getString(4));  // stroj nazev
                        vsZamestnanci1.add(transakceZamestnanci.getInt(5));     // stroje druh
                        vsZamestnanci1.add(transakceZamestnanci.getInt(6));     // zamestnanec id
                        vsZamestnanci1.add(transakceZamestnanci.getString(7));  // zamestnanec jmeno
                        vsZamestnanci1.add(transakceZamestnanci.getTimestamp(8)); // cas
                        vsZamestnanci1.add(transakceZamestnanci.getTimestamp(9)); // nyni cas
                        vrZamestnanci1.add(vsZamestnanci1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    ResultSet transakceZamestnanci = PripojeniDB.dotazS(
                            "SELECT to_char(MIN(zamestnanci_stroje_transakce_log_timestamp), 'DD.MM.YY HH24:MI') AS casZ, "
                            + "to_char(MAX(zamestnanci_stroje_transakce_log_timestamp), 'DD.MM.YY HH24:MI') AS casK "
                            + "FROM spolecne.zamestnanci_stroje_transakce "
                            + "WHERE zamestnanci_stroje_transakce_pruvodky_id = " + id + " "
                            + "AND zamestnanci_stroje_transakce_stroje_id = " + (Integer) strojeIdPomocne.get(i) + " "
                            + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + (Integer) zamestnanciIdPomocne.get(z) + " "
                            + "GROUP BY zamestnanci_stroje_transakce_zamestnanci_id, zamestnanci_stroje_transakce_stroje_id "
                            + "ORDER BY casZ,zamestnanci_stroje_transakce_zamestnanci_id, zamestnanci_stroje_transakce_stroje_id ASC");
                    while (transakceZamestnanci.next()) {
                        vsCasVector1 = new Vector();
                        vsCasVector1.add(transakceZamestnanci.getString(1));    // zac
                        vsCasVector1.add(transakceZamestnanci.getString(2));     // kon
                        vrCasVector1.add(vsCasVector1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long time135 = 0;
                long time246 = 0;
                String aktivni = "NE";

                for (int indexTransakce = 0; indexTransakce < vrZamestnanci1.size(); indexTransakce++) {
                    // System.out.println("transakceIndex  " + indexTransakce);
                    if (indexTransakce == 0) {
                        // System.out.println("transakce 0  " + (String) ((Vector) vrZamestnanci1.get(indexTransakce)).get(3));
                        zamestnanecJmeno = (String) ((Vector) vrZamestnanci1.get(indexTransakce)).get(6);
                        strojNazev = (String) ((Vector) vrZamestnanci1.get(indexTransakce)).get(3);
                        strojeDruh = ((Integer) ((Vector) vrZamestnanci1.get(indexTransakce)).get(4)).intValue();
                    }
                    if ((indexTransakce % 2 == 0) && (indexTransakce != vrZamestnanci1.size() - 1)) {
                        time135 = ((java.sql.Timestamp) ((Vector) vrZamestnanci1.get(indexTransakce)).get(7)).getTime();
                    } else if (indexTransakce % 2 == 1) {
                        time246 += (((java.sql.Timestamp) ((Vector) vrZamestnanci1.get(indexTransakce)).get(7)).getTime() - time135);
                        time135 = 0;
                    } else if ((indexTransakce == vrZamestnanci1.size() - 1) && (indexTransakce % 2 == 0)) {
                        time246 += ((((java.sql.Timestamp) ((Vector) vrZamestnanci1.get(indexTransakce)).get(8)).getTime() - ((java.sql.Timestamp) ((Vector) vrZamestnanci1.get(indexTransakce)).get(7)).getTime()));
                        aktivni = "ANO";
                    }
                }

                Timestamp celkovyCas = new Timestamp(time246);

                int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
                int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
                String minsec = new SimpleDateFormat("mm").format(celkovyCas);
                celkovyCasZamestnanci += time246;
                if (strojeDruh == 1) {
                    celkovyCasMCV += time246;
                } else if ((strojeDruh == 2) || (strojeDruh == 3) || (strojeDruh == 7)) {
                    celkovyCasTornado += time246;
                } else if (strojeDruh == 17) {
                    celkovyCasDMU += time246;
                } else if (strojeDruh == 19) {
                    celkovyCasDrat += time246;
                }
                vsZamestnanciFiltr1 = new Vector();
                vsZamestnanciFiltr1.add(zamestnanecJmeno);
                vsZamestnanciFiltr1.add(strojNazev);
                if (dnu > 0) {
                    vsZamestnanciFiltr1.add((24 * dnu + hodin) + ":" + minsec);
                } else {
                    vsZamestnanciFiltr1.add(hodin + ":" + minsec);
                }
                vsZamestnanciFiltr1.add((String) vsCasVector1.get(0));
                vsZamestnanciFiltr1.add((String) vsCasVector1.get(1));
                vsZamestnanciFiltr1.add(aktivni);
                vrZamestnanciFiltr1.add(vsZamestnanciFiltr1);

            }
        }
        arCasy = new ArrayList<>();
        Timestamp CasTimestamp = new Timestamp(celkovyCasZamestnanci);
        int dnu = Integer.parseInt(new SimpleDateFormat("dd").format(CasTimestamp)) - 1;
        int hodin = Integer.parseInt(new SimpleDateFormat("HH").format(CasTimestamp)) - 1;
        String minsec = new SimpleDateFormat("mm").format(CasTimestamp);
        if (dnu > 0) {
            arCasy.add(new DvojiceRetezRetez("Celkový čas", ((24 * dnu + hodin) + ":" + minsec)));
        } else {
            arCasy.add(new DvojiceRetezRetez("Celkový čas", (hodin + ":" + minsec)));
        }

        Timestamp CasMCVTimestamp = new Timestamp(celkovyCasMCV);
        int dnuMCV = Integer.parseInt(new SimpleDateFormat("dd").format(CasMCVTimestamp)) - 1;
        int hodinMCV = Integer.parseInt(new SimpleDateFormat("HH").format(CasMCVTimestamp)) - 1;
        String minsecMCV = new SimpleDateFormat("mm").format(CasMCVTimestamp);
        if (dnu > 0) {
            arCasy.add(new DvojiceRetezRetez("Čas MCV", ((24 * dnuMCV + hodinMCV) + ":" + minsecMCV)));
        } else {
            arCasy.add(new DvojiceRetezRetez("Čas MCV", (hodinMCV + ":" + minsecMCV)));
        }

        Timestamp CasTornadoTimestamp = new Timestamp(celkovyCasTornado);
        int dnuTornado = Integer.parseInt(new SimpleDateFormat("dd").format(CasTornadoTimestamp)) - 1;
        int hodinTornado = Integer.parseInt(new SimpleDateFormat("HH").format(CasTornadoTimestamp)) - 1;
        String minsecTornado = new SimpleDateFormat("mm").format(CasTornadoTimestamp);
        if (dnu > 0) {
            arCasy.add(new DvojiceRetezRetez("Čas Tornado", ((24 * dnuTornado + hodinTornado) + ":" + minsecTornado)));
        } else {
            arCasy.add(new DvojiceRetezRetez("Čas Tornado", (hodinTornado + ":" + minsecTornado)));
        }

        Timestamp CasDMUTimestamp = new Timestamp(celkovyCasDMU);
        int dnuDMU = Integer.parseInt(new SimpleDateFormat("dd").format(CasDMUTimestamp)) - 1;
        int hodinDMU = Integer.parseInt(new SimpleDateFormat("HH").format(CasDMUTimestamp)) - 1;
        String minsecDMU = new SimpleDateFormat("mm").format(CasDMUTimestamp);
        if (dnu > 0) {
            arCasy.add(new DvojiceRetezRetez("Čas DMU", ((24 * dnuDMU + hodinDMU) + ":" + minsecDMU)));
        } else {
            arCasy.add(new DvojiceRetezRetez("Čas DMU", (hodinDMU + ":" + minsecDMU)));
        }

        Timestamp CasDratTimestamp = new Timestamp(celkovyCasDrat);
        int dnuDrat = Integer.parseInt(new SimpleDateFormat("dd").format(CasDratTimestamp)) - 1;
        int hodinDrat = Integer.parseInt(new SimpleDateFormat("HH").format(CasDratTimestamp)) - 1;
        String minsecDrat = new SimpleDateFormat("mm").format(CasDratTimestamp);
        if (dnu > 0) {
            arCasy.add(new DvojiceRetezRetez("Čas EDM", ((24 * dnuDrat + hodinDrat) + ":" + minsecDrat)));
        } else {
            arCasy.add(new DvojiceRetezRetez("Čas EDM", (hodinDrat + ":" + minsecDrat)));
        }

        nastavTabulkuZamestnanci1();
        tabulkaModelCasy1.fireTableDataChanged();
        tabulkaModelZamestnanci1.oznamUpdateRadkyPozice(vrZamestnanciFiltr1.size() - 1);
        Color backColor = new Color(188, 247, 188);
        Color frontColor = Color.BLACK;

        jTablePruvodkyCasy.setDefaultRenderer(Object.class, new ColorCellRendererPruvCasy1(backColor, frontColor));

        int index[][] = new int[vrZamestnanciFiltr1.size()][tabulkaModelZamestnanci1.getColumnCount()];
        int index2[][] = new int[vrZamestnanciFiltr1.size()][tabulkaModelZamestnanci1.getColumnCount()];

        for (int row = 0; row < vrZamestnanciFiltr1.size(); row++) {
            index[row][2] = 1;
        }

        for (int i = 0; i < tabulkaModelZamestnanci1.getColumnCount(); i++) {
            TableColumn column = null;
            column = jTablePruvodkyZamestnanci.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
    }

    protected boolean selectPruvodka1(long id) {
        //System.out.println("selectpruvodka");

        if (SQLFunkceObecne.selectBooleanPole(
                "SELECT EXISTS (SELECT pruvodky_id FROM spolecne.pruvodky WHERE pruvodky_id = " + id + ")") == false) {
            JednoducheDialogy1.errAno(this, "Chyba při načítání dat průvodky", "Průvodka s daným ID neexistuje,"
                    + "pravděpodobně byla v průběhu Vaší práce vymazána z databáze");
            return false;
        } else {
            return true;
        }

        //return pruvodka.selectData(id);
    }

    private void obarvitTabulku() {

        int index[][] = new int[arPr1.size()][tabulkaModelPruvodka1.getColumnCount()];
        int index2[][] = new int[arPr1.size()][tabulkaModelPruvodka1.getColumnCount()];

        for (int row = 0; row < arPr1.size(); row++) {

            index[row][6] = 1;
            index2[row][8] = 1;
            index2[row][5] = 1;
            index2[row][6] = 1;
            index2[row][7] = 1;

        }
        Color backColor = new Color(255, 204, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelPruvodka1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulka.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
    }

    protected void vycistiFiltrObjednavka1() {
        jTFNazev.setText("");
        jTextFieldTerminDodaniOd1.setText("");
        jTextFieldTerminDodaniDo1.setText("");
        jTFCisloVykresu.setText("");
    }

    private void vytvorPruvodkuExistujici() {
        if (tabulka.getSelectedRow() > -1) {
            indexOznaceno = tabulka.getSelectedRow();
            PruvodkaFrame novaPruvodka = new PruvodkaFrame(arPr1.get(tabulka.getSelectedRow()).getId(), false, "Nová průvodka na základě existující");
            novaPruvodka.addWindowListener(winUdalosti);
        }
    }

    private void editPruvodkuExistujici() {
        if (tabulka.getSelectedRow() > -1) {
            indexOznaceno = tabulka.getSelectedRow();
            PruvodkaFrame novaPruvodka = new PruvodkaFrame(arPr1.get(tabulka.getSelectedRow()).getId(), true, "Upravit průvodku");
            novaPruvodka.addWindowListener(winUdalosti);
        }
    }

    private void uzavritPruvodku() {
        if (tabulka.getSelectedRow() > -1) {
            indexOznaceno = tabulka.getSelectedRow();
            KonecPruvodkyFrame1 konecPruvodky = new KonecPruvodkyFrame1(arPr1.get(tabulka.getSelectedRow()).getId(), celkovyCasMCV, celkovyCasTornado, celkovyCasDMU, celkovyCasDrat, arCasy.get(0).getRetez2().trim());
            konecPruvodky.addWindowListener(winUdalosti);
        } else {
            KonecPruvodkyFrame1 konecPruvodky = new KonecPruvodkyFrame1(arPr1.get(tabulka.getSelectedRow()).getId(), celkovyCasMCV, celkovyCasTornado, celkovyCasDMU, celkovyCasDrat, arCasy.get(0).getRetez2().trim());
            konecPruvodky.addWindowListener(winUdalosti);
        }
    }

    protected class TabulkaModelPruvodka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo",
            "Název",
            "Číslo výkresu",
            "Změna",
            "Termín dokončení",
            "Počet kusů",
            "Počet ks sklad",
            "Nařezat ks",
            "Ze skladu ks",
            "Vyrobeno ks",
            "Poznámky"
        };

        public void pridejSadu() {
            // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arPr1.size());
            //  updateZaznamyObjednavka1();
            if (arPr1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaPruvodka tObj) {
            arPr1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulka.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arPr1.remove(tabulka.getSelectedRow());
            fireTableRowsDeleted(tabulka.getSelectedRow(), tabulka.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (arPr1size() > 0)
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
            return arPr1.size();
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
            System.out.println("col" + col);
            if (col == 17) {
                // System.out.println("boolean");
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaPruvodka bt) {
            System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulka.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulka.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaPruvodka nastavPruv, int pozice) {
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
                TridaPruvodka tPrX = arPr1.get(row);
                switch (col) {
                    case (0): {
                        return tPrX.getId();
                    }
                    case (1): {
                        return (tPrX.getNazev());
                    }
                    case (2): {
                        return (tPrX.getTv1().getCislo());
                    }
                    case (3): {
                        return (tPrX.getTv1().getRevize());
                    }
                    case (4): {
                        if (tPrX.getTerminDokonceni() != null) {
                            return (df.format(tPrX.getTerminDokonceni()));
                        } else {
                            return "";
                        }
                    }
                    case (5): {
                        return (tPrX.getPocetKusu() + " ks");
                    }
                    case (6): {
                        return (tPrX.getPocetKusuSklad() + " ks");
                    }
                    case (7): {
                        return (tPrX.getPocetKusuPolotovar() + " ks");
                    }
                    case (8): {
                        return (tPrX.getNarezanoZeSkladu() + " ks");
                    }
                    case (9): {
                        if (tPrX.getVyrobenoKusu() >= 0 && tPrX.isUzavrena()) {
                            return (tPrX.getVyrobenoKusu() + " ks");
                        } else {
                            return "--";
                        }
                    }
                    case (10): {
                        return (tPrX.getPoznamky());
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

    protected class TabulkaModelProgramy1 extends AbstractTableModel {

        protected final String[] columnNames = {"ID",
            "Název",
            "Revize"
        };

        public void pridejSadu() {
            fireTableRowsInserted(0, tPr1.getArTProg1().size());
            //updateZaznamyProgram1();
            if (tPr1.getArTProg1().size() > 0) {
                jTablePruvodkyProgramy.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            // System.out.println(row + " getValueAt PoloFram " + col);
            try {
                switch (col) {
                    case (0): {
                        return (tPr1.getArTProg1().get(row).getId());
                    }
                    case (1): {
                        return (tPr1.getArTProg1().get(row).getCislo());
                    }
                    case (2): {
                        return (tPr1.getArTProg1().get(row).getRevize());
                    }
                    default: {
                        return null;
                    }
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            //System.out.println("setValueAt PruvFram");
        }//konec setValueAt

        public boolean nastavHodnotuNaVybrane(TridaProgram1 bt) {
            //System.out.println("nastavHodnotuNaVybranePruvodka1 " + jTablePruvodkyPruvodky.getSelectedRow());
            return nastavHodnotuNaPozici(bt, jTablePruvodkyProgramy.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaProgram1 nastavPruv, int pozice) {
            System.out.println("nastav hodnotu na pozici");
            try {
                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(jTablePruvodkyProgramy.getSelectedRow(), jTablePruvodkyProgramy.getSelectedRow());
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
            return tPr1.getArTProg1().size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName
    }//konec modelu tabulky

    protected class TabulkaModelPolotovary1 extends AbstractTableModel {

        protected final String[] columnNames = {"Název", "Hodnota"};

        public void pridejSadu() {
            fireTableRowsInserted(0, vrPolotovar1.size());
            //updateZaznamyProgram1();
            if (vrPolotovar1.size() > 0) {
                jTablePruvodkyPolotovary.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            //System.out.println("getValueAt ProgFram");
            try {
                return (((Vector) vrPolotovar1.elementAt(row)).elementAt(col));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            //System.out.println("setValueAt PruvFram");           
            try {
            }//konec try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }//konec setValueAt

        public boolean nastavHodnotuNaVybrane(Vector bt) {
            // System.out.println("nastavHodnotuNaVybranePruvodka1 " + jTablePruvodkyPruvodky.getSelectedRow());
            return nastavHodnotuNaPozici(bt, jTablePruvodkyPolotovary.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(Vector nastavPruv, int pozice) {
            // System.out.println("nastav hodnotu na pozici");
            try {
                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(jTablePruvodkyPolotovary.getSelectedRow(), jTablePruvodkyPolotovary.getSelectedRow());
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
            return vrPolotovar1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName
    }//konec modelu tabulky

    protected class TabulkaModelZamestnanci1 extends AbstractTableModel {

        protected final String[] columnNames = {"Zaměstnanec", "Stroj", "Čas", "Začátek", "Konec", "Akt."};

        public void pridejSadu() {
            fireTableRowsInserted(0, vrZamestnanciFiltr1.size());
            //updateZaznamyProgram1();
            if (vrZamestnanciFiltr1.size() > 0) {
                jTablePruvodkyZamestnanci.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            //System.out.println("getValueAt ProgFram");
            try {
                return (((Vector) vrZamestnanciFiltr1.elementAt(row)).elementAt(col));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            // System.out.println("setValueAt PruvFram");
        }//konec setValueAt

        public boolean nastavHodnotuNaVybrane(Vector bt) {
            //  System.out.println("nastavHodnotuNaVybranePruvodka1 " + jTablePruvodkyPruvodky.getSelectedRow());
            return nastavHodnotuNaPozici(bt, jTablePruvodkyZamestnanci.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(Vector nastavPruv, int pozice) {
            // System.out.println("nastav hodnotu na pozici");
            try {
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(jTablePruvodkyPopisy1.getSelectedRow(), jTablePruvodkyPopisy1.getSelectedRow());
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
            return vrZamestnanciFiltr1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName
    }//konec modelu tabulky

    protected class TabulkaModelPopisy1 extends AbstractTableModel {

        protected final String[] columnNames = {"Poradi", "Druh stroje", "Popis operace"};

        public void pridejSadu() {
            fireTableRowsInserted(0, tPr1.getArTPostup1().size());
            //updateZaznamyProgram1();
            if (tPr1.getArTPostup1().size() > 0) {
                jTablePruvodkyPopisy1.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            try {
                TridaPracovniPostupPruv1 tppp = tPr1.getArTPostup1().get(row);
                switch (col) {
                    case (0): {
                        return (tppp.getPoradi());
                    }
                    case (1): {
                        return (tppp.getIdDruhStroje());
                    }
                    case (2): {
                        return (tppp.getPopis());
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
            try {
            }//konec try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }//konec setValueAt

        public boolean nastavHodnotuNaVybrane(TridaPracovniPostupPruv1 bt) {
            // System.out.println("nastavHodnotuNaVybranePruvodka1 " + jTablePruvodkyPruvodky.getSelectedRow());
            return nastavHodnotuNaPozici(bt, jTablePruvodkyPopisy1.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaPracovniPostupPruv1 nastavPruv, int pozice) {
            // System.out.println("nastav hodnotu na pozici");
            try {
                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(jTablePruvodkyPopisy1.getSelectedRow(), jTablePruvodkyPopisy1.getSelectedRow());
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
            return tPr1.getArTPostup1().size();
        }//konec getRowCount

        @Override
        public Class getColumnClass(int col) {
            if (col == 1) {
                return Integer.class;
            } else {
                return String.class;
            }
        }

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName
    }

    protected class TabulkaModelCasy extends AbstractTableModel {

        protected final String[] columnNames = {"Kategorie", "Čas"};

        public void pridejSadu() {
            fireTableRowsInserted(0, arCasy.size());
            //updateZaznamyProgram1();
            if (arCasy.size() > 0) {
                jTablePruvodkyCasy.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            try {
                DvojiceRetezRetez drr = arCasy.get(row);
                switch (col) {
                    case (0): {
                        return (drr.getRetez1());
                    }
                    case (1): {
                        return (drr.getRetez2());
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
            try {
            }//konec try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }//konec setValueAt

        public boolean nastavHodnotuNaVybrane(DvojiceRetezRetez drr) {
            // System.out.println("nastavHodnotuNaVybranePruvodka1 " + jTablePruvodkyPruvodky.getSelectedRow());
            return nastavHodnotuNaPozici(drr, jTablePruvodkyCasy.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(DvojiceRetezRetez drr, int pozice) {
            // System.out.println("nastav hodnotu na pozici");
            try {
                oznamUpdateRadkyPozice(pozice);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(jTablePruvodkyCasy.getSelectedRow(), jTablePruvodkyCasy.getSelectedRow());
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
            return arCasy.size();
        }//konec getRowCount

        @Override
        public Class getColumnClass(int col) {
            if (col == 1) {
                return String.class;
            } else {
                return String.class;
            }
        }

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName
    }

    class ALUdalosti implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //  System.out.println("action : " + e.getActionCommand());
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("FiltrujPruvodky1") || e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                if (e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                    vycistiFiltrObjednavka1();
                }
                /* if (tabulka.getSelectedRow() > -1) {
                 idOznaceno = arPr1.get(tabulka.getSelectedRow()).getId();
                 }*/
                refreshData();
            }

            if (e.getActionCommand().equals("Hledat")) {

                /* if (tabulka.getSelectedRow() > -1) {
                 idOznaceno = arPr1.get(tabulka.getSelectedRow()).getId();
                 }*/
                refreshData();

            }

            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }

            if (e.getActionCommand().equals("UzavritPruvodka")) {
                uzavritPruvodku();
            }

            if (e.getActionCommand().equals("NovaPruvodka")) {
                PruvodkaFrame novaPruvodka = new PruvodkaFrame("Nová průvodka");

            }

            if (e.getActionCommand().equals("EditPruvodka")) {
                editPruvodkuExistujici();

            }

            if (e.getActionCommand().equals("NovaPruvodkaStavajici")) {
                vytvorPruvodkuExistujici();
            }

        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {

            if (tme.getSource() == tableModelPruvodka1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmPruvodka1) {
                if (e.getValueIsAdjusting() == false) {
                    getDetailPruvodka1();
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
            if (e.getSource().getClass().getSimpleName().equals("KonecPruvodkyFrame1")
                    || e.getSource().getClass().getSimpleName().equals("PruvodkaFrame")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaPruvodka1();
                tabulkaModelPruvodka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
                ListSelectionModel selectionModel
                        = tabulka.getSelectionModel();
                selectionModel.setSelectionInterval(indexOznaceno, indexOznaceno);
                tabulka.scrollRectToVisible(tabulka.getCellRect(indexOznaceno, 0, false));
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
        jCBFiltrZdroj = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFCisloVykresu = new javax.swing.JTextField();
        jTFNazev = new javax.swing.JTextField();
        jTextFieldTerminDodaniOd1 = new javax.swing.JTextField();
        jTextFieldTerminDodaniDo1 = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTFCisloPruvodky = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jBNova = new javax.swing.JButton();
        jBNovaStavajici = new javax.swing.JButton();
        jBUpravit = new javax.swing.JButton();
        jBUzavrit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTablePruvodkyCasy = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePruvodkyZamestnanci = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePruvodkyPolotovary = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePruvodkyProgramy = new javax.swing.JTable();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTablePruvodkyPopisy1 = new javax.swing.JTable();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

        jLabel3.setText("Rok dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        JPFiltrTop.add(jCBRokDodani, gridBagConstraints);

        jCBFiltrZdroj.setText("Filtr průvodky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jCBFiltrZdroj, gridBagConstraints);

        jLabel4.setText("Počet položek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jLabel4, gridBagConstraints);

        jLPocetPolozek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        JPFiltrTop.add(jLPocetPolozek, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo výkresu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel8.setText("Název součásti");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel8, gridBagConstraints);

        jLabel16.setText("Termín dodání od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);

        jLabel17.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFCisloVykresu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFNazev, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldTerminDodaniOd1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldTerminDodaniDo1, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 70;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBZakaznik.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        jLabel7.setText("Číslo průvodky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFCisloPruvodky, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBNova.setText("Nová průvodka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNova, gridBagConstraints);

        jBNovaStavajici.setText("Nová průvodka na základě stávající");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaStavajici, gridBagConstraints);

        jBUpravit.setText("Upravit průvodku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravit, gridBagConstraints);

        jBUzavrit.setText("Uzavřít průvodku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUzavrit, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder("Shrnutí práce na průvodce"));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(100, 100));

        jTablePruvodkyCasy.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablePruvodkyCasy.setModel(new javax.swing.table.DefaultTableModel(
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
        jTablePruvodkyCasy.setRowHeight(20);
        jScrollPane5.setViewportView(jTablePruvodkyCasy);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane5, gridBagConstraints);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Zaměstnanci na průvodce"));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 64));

        jTablePruvodkyZamestnanci.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTablePruvodkyZamestnanci);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane3, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informace o materiálu"));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 64));

        jTablePruvodkyPolotovary.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTablePruvodkyPolotovary);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Použité programy"));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 64));

        jTablePruvodkyProgramy.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTablePruvodkyProgramy);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jSPTabulka.setBorder(javax.swing.BorderFactory.createTitledBorder("Přehled průvodek"));
        jSPTabulka.setPreferredSize(new java.awt.Dimension(300, 64));

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jSPTabulka, gridBagConstraints);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Pracovní postup"));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(300, 64));

        jTablePruvodkyPopisy1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTablePruvodkyPopisy1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane4, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBNova;
    private javax.swing.JButton jBNovaStavajici;
    private javax.swing.JButton jBUpravit;
    private javax.swing.JButton jBUzavrit;
    private javax.swing.JCheckBox jCBFiltrZdroj;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jTFCisloPruvodky;
    private javax.swing.JTextField jTFCisloVykresu;
    private javax.swing.JTextField jTFNazev;
    private javax.swing.JTable jTablePruvodkyCasy;
    private javax.swing.JTable jTablePruvodkyPolotovary;
    private javax.swing.JTable jTablePruvodkyPopisy1;
    private javax.swing.JTable jTablePruvodkyProgramy;
    private javax.swing.JTable jTablePruvodkyZamestnanci;
    private javax.swing.JTextField jTextFieldTerminDodaniDo1;
    private javax.swing.JTextField jTextFieldTerminDodaniOd1;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
