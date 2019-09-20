/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import eu.data7.tableTools.TableTextEditor1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaPruvodka1;
import mikronis2.tridy.DvojiceCisloRetez;
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
public class PruvodkaFrame extends javax.swing.JFrame {

    private TridaPruvodka1 pruvodka;
    private boolean edit = false;
    private long idEditPruvodka;
    long pruvodky_id_uloz = -1;
    private Vector vsVykresy;
    private Vector vrVykresy;
    private Vector vsVykresyZmeny;
    private Vector vrVykresyZmeny;
    private Vector vsMaterialy;
    private Vector vrMaterialy;
    private Vector vsPolotovary;
    private Vector vrPolotovary;
    private Vector vsProgramy;
    private Vector vrProgramy;
    private Vector vrPostupy;
    private Vector vrObjednavka1;
    private Vector vsObjednavka1;
    private Vector vrVybraneProgramy;
    private int typPolotovaruId = 0;
    private int typMaterialuId = 0;
    private ComboBoxMaterialUdalosti MaterialListener;
    private ComboBoxPolotovarUdalosti PolotovarListener;
    private ComboBoxVyberPolotovaruUdalosti VyberPolotovaruListener;
    private ActionListener akceUdalosti;
    private PridatButtonListener PridatListener;
    private OdebratButtonListener OdebratListener;
    private PridatRadekButtonListener PridatRadekListener;
    private OdebratRadekButtonListener OdebratRadekListener;
    private UlozButtonListener UlozPruvodkuListener;
    tabulkaModelProgramy programyModel = new tabulkaModelProgramy();
    tabulkaPracovniPostup pracovniPostup = new tabulkaPracovniPostup();
    private ListSelectionListener lslUdalosti;
    private ListSelectionModel lsmr1, lsms1;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3, nf0;
   // private boolean zobrazError;

    public PruvodkaFrame(TridaPruvodka1 pruvodkaVstup, String title) {
        initComponents();
        //zobrazError = false;
        this.setVisible(true);
        inicializaceKomponent();
        this.pruvodka = (TridaPruvodka1) pruvodkaVstup;
        this.setTitle(title);
    }

    public PruvodkaFrame(String title) {
        initComponents();
       // zobrazError = false;
        // novaPruvodka = true;
        this.setVisible(true);
        inicializaceKomponent();
        this.setTitle(title);
    }

    public PruvodkaFrame(long pruvodky_id, boolean edit, String title) {
        initComponents();
        //zobrazError = true;
        //novaPruvodka = false;
        this.setVisible(true);
        this.edit = edit;
        this.idEditPruvodka = pruvodky_id;
        inicializaceKomponent();
        naplnFormular(pruvodky_id);
        this.setTitle(title);
        if (edit == false) {
            this.jCBOdecteno.setSelected(false);
        }
    }

    private void inicializaceKomponent() {

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

        nf0 = java.text.NumberFormat.getInstance();
        nf0.setMinimumFractionDigits(0);
        nf0.setMaximumFractionDigits(0);

        UlozPruvodkuListener = new UlozButtonListener();
        PridatListener = new PridatButtonListener();
        OdebratListener = new OdebratButtonListener();
        PridatRadekListener = new PridatRadekButtonListener();
        OdebratRadekListener = new OdebratRadekButtonListener();
        MaterialListener = new ComboBoxMaterialUdalosti();
        PolotovarListener = new ComboBoxPolotovarUdalosti();

        VyberPolotovaruListener = new ComboBoxVyberPolotovaruUdalosti();
        //TypPolotovaruComboBox1.addItemListener(PolotovarListener);
        VyberMaterialuComboBox1.addItemListener(MaterialListener);
        VyberPolotovaruComboBox1.addItemListener(VyberPolotovaruListener);

        UlozPruvodkuButton1.addActionListener(UlozPruvodkuListener);
        PridatButton.addActionListener(PridatListener);
        OdebratButton.addActionListener(OdebratListener);
        PridatRadekButton1.addActionListener(PridatRadekListener);
        OdebratRadekButton1.addActionListener(OdebratRadekListener);
        lslUdalosti = new LSLUdalosti();

        ProgramyTable.setModel(programyModel);
        postupPraceTable.setModel(pracovniPostup);

        lsmr1 = postupPraceTable.getSelectionModel();
        lsms1 = postupPraceTable.getColumnModel().getSelectionModel();

        nastavTableListenersPostupPraceTabulka();
        jCBPridatProgram.removeAllItems();
        initProgramyTable();

        vsProgramy = new Vector();
        vrProgramy = new Vector();

        vsVykresy = new Vector();
        vrVykresy = new Vector();
        // initVykresy();

        vsVykresyZmeny = new Vector();
        vrVykresyZmeny = new Vector();

        vsObjednavka1 = new Vector();
        vrObjednavka1 = new Vector();

        vsMaterialy = new Vector();
        vrMaterialy = new Vector();
        initMaterialy();

        /* vsTypPolotovary = new Vector();
         vrTypPolotovary = new Vector();
         initTypPolotovary();*/
        vsPolotovary = new Vector();
        vrPolotovary = new Vector();
        initPolotovary();

        vrPostupy = new Vector();
        initPostupPraceTable();

//        initProgramy();
        //vsVybraneProgramy = new Vector();
        vrVybraneProgramy = new Vector();
//        initProgramy();
        getPrazdnaDataTabulkaPracovniPostupy();

        akceUdalosti = new AkceUdalosti();

        ObjednavkaComboBox1.removeAllItems();

    }

    private void nastavUdalostiCommandsRoletky() {
        VyberPolotovaruComboBox1.setActionCommand("vyberPolotovaruPrikaz");

    }

    private void nastavPosluchaceUdalostiRoletky() {
        VyberPolotovaruComboBox1.addActionListener(akceUdalosti);

    }

    private void zrusPosluchaceUdalostiRoletky() {
        VyberPolotovaruComboBox1.removeActionListener(akceUdalosti);

    }

    private void initProgramyTable() {
        TableColumn column = null;
        column = ProgramyTable.getColumnModel().getColumn(0);//ID
        column.setPreferredWidth(100);
    }

    private void initPostupPraceTable() {
        TableColumn column = null;

        column = postupPraceTable.getColumnModel().getColumn(0);//ID
        column.setPreferredWidth(60);
        column.setCellRenderer(new TableVyberRenderer1("SELECT druhy_stroju_id, druhy_stroju_nazev FROM spolecne.druhy_stroju ORDER BY druhy_stroju_priorita",
                "Chyba při načítání druhů strojů do tabulky seznamu činností průvodky"));
        column.setCellEditor(new TableVyberEditor1("SELECT druhy_stroju_id, druhy_stroju_nazev FROM spolecne.druhy_stroju ORDER BY druhy_stroju_priorita",
                "Chyba při načítání druhů strojů do tabulky seznamu činností průvodky"));

        column = postupPraceTable.getColumnModel().getColumn(1);//ID
        column.setPreferredWidth(550);
        column.setCellEditor(new TableTextEditor1(600));

        postupPraceTable.setRowHeight(30);
    }

    private void initVykresy() {
        // CisloVykresuComboBox1.removeAllItems();
        try {
            vsVykresy.removeAllElements();
            vrVykresy.removeAllElements();
            ObjednavkaComboBox1.removeAllItems();
            ResultSet vykresy1 = PripojeniDB.dotazS(
                    "SELECT vykresy_id, vykresy_cislo, vykresy_revize, vykresy_nazev FROM spolecne.vykresy ORDER BY vykresy_cislo ASC");

            while (vykresy1.next()) {
                vsVykresy = new Vector();
                vsVykresy.add(new Integer(vykresy1.getInt(1)));
                vsVykresy.add(new String(vykresy1.getString(2)));
                try {
                    vsVykresy.add(new String(vykresy1.getString(3)));
                } catch (Exception e) {
                    vsVykresy.add(new String(""));
                }
                vrVykresy.add(vsVykresy);
                //  CisloVykresuComboBox1.addItem(new String(vsVykresy.get(1) + " - " + vsVykresy.get(2)));
                //tabulkaModelProgramy1.oznamUpdateRadkyPozice(vrProgram1.size() - 1);
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        //CisloVykresuComboBox1.addItemListener(VykresyListener);
        //zjistiVyrobkySklad();
        //CisloVykresuComboBox1.setSelectedIndex(0);
        //initProgramy();
    }

    private void initProgramy(String cisloVykresu) {
        // System.out.println("PruvodkaFrame.initProgramy " + idVykresu);
        for (int i = 0; i < vrVybraneProgramy.size(); i++) {
            System.out.println("vybrane " + 1);
        }
        jCBPridatProgram.removeAllItems();
        try {
            vsProgramy.removeAllElements();
            vrProgramy.removeAllElements();
            ResultSet programy1 = PripojeniDB.dotazS(
                    "SELECT programy_id, programy_cislo, programy_revize, "
                    + "programy_material_rozmer "
                    + "FROM spolecne.programy "
                    + "CROSS JOIN spolecne.vazba_programy_vykresy "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE spolecne.programy.programy_id = spolecne.vazba_programy_vykresy.vazba_programy_vykresy_programy_id "
                    + "AND spolecne.vazba_programy_vykresy.vazba_programy_vykresy_vykresy_id = vykresy.vykresy_id "
                    + "AND vykresy.vykresy_cislo = " + TextFunkce1.osetriZapisTextDB1(cisloVykresu));

            while (programy1.next()) {
                vsProgramy = new Vector();
                vsProgramy.add(new Integer(programy1.getInt(1)));
                vsProgramy.add(new String(programy1.getString(2)));
                try {
                    vsProgramy.add(new String(programy1.getString(3)));
                } catch (Exception e) {
                    vsProgramy.add(new String(""));
                }
                try {
                    vsProgramy.add(new String(programy1.getString(4)));
                } catch (Exception e) {
                    vsProgramy.add(new String(""));
                }
                try {
                    vsProgramy.add(new String(programy1.getString(5)));
                } catch (Exception e) {
                    vsProgramy.add(new String(""));
                }
                try {
                    vsProgramy.add((Timestamp) (programy1.getTimestamp(6)));
                } catch (Exception e) {
                    vsProgramy.add(null);
                }
                vrProgramy.add(vsProgramy);
                jCBPridatProgram.addItem(new String(vsProgramy.get(1) + " - " + vsProgramy.get(2)));
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        /* for (int i = 0; i < vrVybraneProgramy.size(); i++) {
         // System.out.println("2vybrane " + 1);
         }*/
    }

    private void initMaterialy() {
        VyberMaterialuComboBox1.removeItemListener(MaterialListener);
        VyberMaterialuComboBox1.removeAllItems();
        try {
            vsMaterialy.removeAllElements();
            vrMaterialy.removeAllElements();
            ResultSet materialy1 = PripojeniDB.dotazS(
                    "SELECT typ_materialu_id, typ_materialu_nazev, typ_materialu_norma, typ_materialu_priorita FROM spolecne.typ_materialu ORDER BY typ_materialu_priorita ASC");

            while (materialy1.next()) {
                vsMaterialy = new Vector();
                vsMaterialy.add(new Integer(materialy1.getInt(1)));
                try {
                    vsMaterialy.add(new String(materialy1.getString(2)));
                } catch (Exception e) {
                    vsMaterialy.add(new String(""));
                }
                try {
                    vsMaterialy.add(new String(materialy1.getString(3)));
                } catch (Exception e) {
                    vsMaterialy.add(new String(""));
                }
                vrMaterialy.add(vsMaterialy);
                VyberMaterialuComboBox1.addItem(new String(vsMaterialy.get(1) + " - " + vsMaterialy.get(2)));
                //tabulkaModelProgramy1.oznamUpdateRadkyPozice(vrProgram1.size() - 1);
            }
            VyberMaterialuComboBox1.setSelectedIndex(0);
            VyberMaterialuComboBox1.addItemListener(MaterialListener);
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void initPolotovary() {
        VyberPolotovaruComboBox1.setEnabled(false);
        VyberPolotovaruComboBox1.removeAllItems();
        VyberMaterialuComboBox1.removeActionListener(akceUdalosti);
        MaterialListener.itemStateChanged(null);

        try {
            vsPolotovary.removeAllElements();
            vrPolotovary.removeAllElements();
            ResultSet typPolotovary1 = PripojeniDB.dotazS(
                    "SELECT polotovary_id, polotovary_nazev FROM spolecne.polotovary WHERE polotovary_typ_materialu = " + typMaterialuId + " ORDER BY polotovary_nazev ASC");

            while (typPolotovary1.next()) {
                vsPolotovary = new Vector();
                vsPolotovary.add(new Integer(typPolotovary1.getInt(1)));
                try {
                    vsPolotovary.add(new String(typPolotovary1.getString(2)));
                } catch (Exception e) {
                    vsPolotovary.add(new String(""));
                }
                try {
                    vsPolotovary.add(new String(typPolotovary1.getString(3)));
                } catch (Exception e) {
                    vsPolotovary.add(new String(""));
                }
                vrPolotovary.add(vsPolotovary);
                VyberPolotovaruComboBox1.addItem((String) vsPolotovary.get(1));
            }
            // VyberPolotovaruComboBox1.addItemListener(PolotovarListener);
            VyberPolotovaruComboBox1.setEnabled(true);
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        nastavPosluchaceUdalostiRoletky();
        // VyberPolotovaruComboBox1.addItemListener(PolotovarListener);
    }

    public void zjistiPolotovarySeznam() {
        // VyberPolotovaruComboBox1.removeItemListener(VyberPolotovaruListener);
        VyberPolotovaruComboBox1.setEnabled(false);
        VyberPolotovaruComboBox1.removeAllItems();

        try {
            vsPolotovary.removeAllElements();
            vrPolotovary.removeAllElements();
            ResultSet typPolotovary1 = PripojeniDB.dotazS(
                    "SELECT polotovary_id, polotovary_nazev FROM spolecne.polotovary WHERE polotovary_typ_materialu = " + typMaterialuId + " ORDER BY polotovary_nazev ASC ");

            while (typPolotovary1.next()) {
                vsPolotovary = new Vector();
                vsPolotovary.add(new Integer(typPolotovary1.getInt(1)));
                try {
                    vsPolotovary.add(new String(typPolotovary1.getString(2)));
                } catch (Exception e) {
                    vsPolotovary.add(new String(""));
                }
                try {
                    vsPolotovary.add(new String(typPolotovary1.getString(3)));
                } catch (Exception e) {
                    vsPolotovary.add(new String(""));
                }
                vrPolotovary.add(vsPolotovary);
                //  System.out.println("polotovary " + vsTypPolotovary.get(1) + " - " + vsTypPolotovary.get(2));
                VyberPolotovaruComboBox1.addItem((String) vsPolotovary.get(1));
                //tabulkaModelProgramy1.oznamUpdateRadkyPozice(vrProgram1.size() - 1);
            }
            // VyberPolotovaruComboBox1.addItemListener(PolotovarListener);
            VyberPolotovaruComboBox1.setEnabled(true);
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

    }

    public void zjistiVyrobkySklad() {
        //int idVykresu = (Integer) ((Vector) vrVykresy.get(CisloVykresuComboBox1.getSelectedIndex())).get(0);
        String cisloVykresu = CisloVykresuTextField1.getText().trim();
        System.out.println("SELECT btrim(to_char( "
                + "(SELECT COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_vyrobky_transakce "
                + "INNER JOIN logistika.sklady_vyrobky_transakce_druhy ON sklady_vyrobky_transakce_druh_id = "
                + "sklady_vyrobky_transakce_druhy_id AND sklady_vyrobky_transakce_druhy_je_prijem IS TRUE "
                + "AND sklady_vyrobky_transakce_vykres_cislo = '" + cisloVykresu + "') "
                + " - (SELECT COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_vyrobky_transakce "
                + "INNER JOIN logistika.sklady_vyrobky_transakce_druhy ON sklady_vyrobky_transakce_druh_id = "
                + "sklady_vyrobky_transakce_druhy_id AND sklady_vyrobky_transakce_druhy_je_prijem IS FALSE "
                + "AND sklady_vyrobky_transakce_vykres_cislo = '" + cisloVykresu + "') , 'FM99990D99'),'.,')");
        VyrobkySkladLabel1.setText(SQLFunkceObecne2.selectStringPole("SELECT btrim(to_char( "
                + "(SELECT COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_vyrobky_transakce "
                + "INNER JOIN logistika.sklady_vyrobky_transakce_druhy ON sklady_vyrobky_transakce_druh_id = "
                + "sklady_vyrobky_transakce_druhy_id AND sklady_vyrobky_transakce_druhy_je_prijem IS TRUE "
                + "AND sklady_vyrobky_transakce_vykres_cislo = '" + cisloVykresu + "') "
                + " - (SELECT COALESCE(SUM(sklady_vyrobky_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_vyrobky_transakce "
                + "INNER JOIN logistika.sklady_vyrobky_transakce_druhy ON sklady_vyrobky_transakce_druh_id = "
                + "sklady_vyrobky_transakce_druhy_id AND sklady_vyrobky_transakce_druhy_je_prijem IS FALSE "
                + "AND sklady_vyrobky_transakce_vykres_cislo = '" + cisloVykresu + "') , 'FM99990D99'),'.,')"));
        String dotaz = "SELECT sklady_vyrobky_umisteni_nazev "
                + "FROM logistika.sklady_vyrobky_umisteni "
                + "CROSS JOIN logistika.sklady_vyrobky_transakce "
                + "WHERE sklady_vyrobky_transakce.sklady_vyrobky_transakce_umisteni_id = sklady_vyrobky_umisteni.sklady_vyrobky_umisteni_id "
                + "AND sklady_vyrobky_transakce.sklady_vyrobky_transakce_vykres_cislo = '" + cisloVykresu + "'";
        try {
            ResultSet nazevRegalu = PripojeniDB.dotazS(dotaz);
            while (nazevRegalu.next()) {
                UmisteniSkladLabel1.setText(new String(nazevRegalu.getString(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void zjistiPolotovarySklad() {

        int idPolotovary;
        if (VyberPolotovaruComboBox1.getSelectedIndex() > -1) {
            idPolotovary = (Integer) ((Vector) vrPolotovary.get(VyberPolotovaruComboBox1.getSelectedIndex())).get(0);
        } else {
            idPolotovary = 0;
        }
        //System.out.println("Polotovary sklad " + idPolotovary);
        String pocetKusu = SQLFunkceObecne2.selectStringPole("SELECT btrim(to_char( "
                + "(SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_polotovary_transakce "
                + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS TRUE "
                + "AND sklady_polotovary_transakce_polotovar_id = " + idPolotovary + ") "
                + " - (SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_polotovary_transakce "
                + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS FALSE "
                + "AND sklady_polotovary_transakce_polotovar_id = " + idPolotovary + "), 'FM99990D99'),'.,')");
        jLabelPolotovarySklad.setText(pocetKusu);

        String alokovanoPruvodky = SQLFunkceObecne2.selectStringPole("SELECT SUM(pruvodky_material_prumerna_delka * pruvodky_polotovar_pocet_kusu) AS alokovano "
                + "FROM spolecne.pruvodky "
                + "WHERE pruvodky_odectena IS FALSE "
                + "AND pruvodky_polotovar_id = " + idPolotovary);
        jLabelPruvodkyAlokovano.setText(alokovanoPruvodky);
        try {
            if (!PrumernaDelkaTextField1.getText().trim().isEmpty() && !PocetPolotovaruTextField1.getText().trim().isEmpty()
                    && !pocetKusu.isEmpty()) {
                double delka = Double.parseDouble(PrumernaDelkaTextField1.getText().trim());
                double kusu = Double.parseDouble(PocetPolotovaruTextField1.getText().trim());
                double naSklade = Double.parseDouble(pocetKusu);
                double alokovano = 0;
                if (!alokovanoPruvodky.isEmpty()) {
                    alokovano = Double.parseDouble(alokovanoPruvodky);
                }
                jLPolotovarySkladZbyle.setText(nf0.format(naSklade - (delka * kusu) - alokovano) + "");
               /* if (zobrazError == false && (naSklade - (delka * kusu) - alokovano) < 0) {
                    JednoducheDialogy1.warnAno(this, "Nedostatek materiálu na skladě", "Na skladě není dostatek materiálu pro nařezání dostatečného počtu kusů");
                    zobrazError = true;

                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void ulozPruvodku() {
        UlozPruvodkuButton1.removeActionListener(UlozPruvodkuListener);
        UlozPruvodkuButton1.setEnabled(false);

        if (this.edit == false) {
            pruvodky_id_uloz = ulozPruvodkuNova();
        } else {
            pruvodky_id_uloz = ulozPruvodkuEdit();
        }
    }

    protected long ulozPruvodkuNova() {
        long pruvodky_id = 0;
        long pracovni_postup_id = 0;
        long vazba_pos_zam_id = -1;
        int vykresy_id = -1;
        long transakce_id = -1;

        try {
            String dotaz = "SELECT vykresy_id "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }
            ResultSet id = PripojeniDB.dotazS(dotaz);

            while (id.next()) {
                vykresy_id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vykresy_id = 1;
        }

        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(pruvodky_id) FROM spolecne.pruvodky");
            while (id.next()) {
                pruvodky_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pruvodky_id = 1;
        }

        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(pracovni_postup_pruvodka_id) FROM spolecne.pracovni_postup_pruvodka");
            while (id.next()) {
                pracovni_postup_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pruvodky_id = 1;
        }

        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(vazba_pracovni_postup_pruvodka_zamestnanci_id) FROM spolecne.vazba_pracovni_postup_pruvodka_zamestnanci");
            while (id.next()) {
                vazba_pos_zam_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pruvodky_id = 1;
        }

        int polotovary_id = -1;
        if (VyberPolotovaruComboBox1.getSelectedIndex() > -1) {
            polotovary_id = (Integer) ((Vector) vrPolotovary.get(VyberPolotovaruComboBox1.getSelectedIndex())).get(0);
        } else {
            polotovary_id = 1;
        }
        String dotaz = "INSERT INTO spolecne.pruvodky( "
                + "pruvodky_id, pruvodky_nazev, pruvodky_cislo_vykresu, pruvodky_termin_dokonceni, "
                + "pruvodky_pocet_kusu, pruvodky_pocet_kusu_sklad, "
                + "pruvodky_polotovar_pocet_kusu, pruvodky_polotovar_id, pruvodky_material_prumerna_delka, pruvodky_objednavky_id, "
                + "pruvodky_polotovar_rozmer, pruvodky_poznamky, pruvodky_odectena) "
                + "VALUES (" + pruvodky_id + ", '" + NazevPruvodkyTextField1.getText().trim() + "', "
                + vykresy_id + ", '"
                + TerminDodaniTextField1.getText().trim() + "', " + PocetKusuTextField1.getText().trim() + ", "
                + PocetKusuSkladTextField1.getText().trim() + ", "
                + PocetPolotovaruTextField1.getText().trim() + ", "
                + polotovary_id + ", "
                + PrumernaDelkaTextField1.getText().trim() + ", "
                + (Long) ((Vector) vrObjednavka1.get(ObjednavkaComboBox1.getSelectedIndex())).get(0) + ", '"
                + RozmerPolotovarutextField1.getText().trim() + "', '"
                + PoznamkaTextField1.getText().trim() + " ', "
                + jCBOdecteno.isSelected() + ")";
        // System.out.println("Ulozeni pruvodky " +dotaz);
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
            //int programyId = 0;
            if (vrVybraneProgramy.size() > 0) {
                for (int i = 0; i < vrVybraneProgramy.size(); i++) {
                    //programyId = (Integer) programyModel.getValueAt(i, 0);
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_pruvodky_programy( "
                            + "vazba_pruvodky_programy_pruvodky_id, vazba_pruvodky_programy_programy_id) "
                            + "VALUES (" + pruvodky_id + ", " + (Integer) ((Vector) vrVybraneProgramy.get(i)).get(0) + ")");
                }
            }
            if (pracovniPostup.getPocetKroku() > 0) {
                // System.out.println("pocet kroku prac. postup" + pracovniPostup.getPocetKroku());
                for (int i = 0; i < pracovniPostup.getPocetKroku(); i++) {
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.pracovni_postup_pruvodka( "
                            + "pracovni_postup_pruvodka_id, pracovni_postup_pruvodka_poradi, "
                            + "pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_druh_stroje_id, "
                            + "pracovni_postup_pruvodka_popis) "
                            + "VALUES (" + pracovni_postup_id + ", " + (i + 1) + ", " + pruvodky_id + ", "
                            + ((Integer) pracovniPostup.getValueAt(i, 0)).intValue() + ", '"
                            + (String) pracovniPostup.getValueAt(i, 1) + "')");
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_pracovni_postup_pruvodka_zamestnanci("
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_postup_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_zamestnanci_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_stav_id) "
                            + "VALUES (" + vazba_pos_zam_id + ", " + pracovni_postup_id + ", "
                            + "50000001, 1)");
                    pracovni_postup_id++;
                    vazba_pos_zam_id++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //BYLO : odecteni materialu pri ulozeni pruvodky
        /* try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(sklady_polotovary_transakce_id) FROM logistika.sklady_polotovary_transakce");
            while (id.next()) {
                transakce_id = id.getLong(1) + 1;
            }
            int pocet_mj = Integer.valueOf(PocetPolotovaruTextField1.getText().trim()).intValue()
                    * Integer.valueOf(PrumernaDelkaTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO logistika.sklady_polotovary_transakce("
                    + " sklady_polotovary_transakce_id, "
                    + "sklady_polotovary_transakce_sklad_id, "
                    + "sklady_polotovary_transakce_umisteni_id, "
                    + "sklady_polotovary_transakce_druh_id, "
                    + "sklady_polotovary_transakce_polotovar_id, "
                    + "sklady_polotovary_transakce_popis, "
                    + "sklady_polotovary_transakce_pocet_mj, "
                    + "sklady_polotovary_transakce_log_uzivatel) "
                    + "VALUES( " + transakce_id + ", " + 1 + ", " + 1 + ", " + 100 + ", " + polotovary_id + ", '"
                    + pruvodky_id + ", " + CisloVykresuTextField1.getText().trim() + "', " + pocet_mj + ", 'test')");

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        UlozPruvodkuButton1.setEnabled(false);
        return pruvodky_id;
    }

    protected long ulozPruvodkuEdit() {
        long pruvodky_id = idEditPruvodka;
        long pracovni_postup_id = 0;
        long vazba_pos_zam_id = -1;
        int vykresy_id = -1;
        long transakce_id = -1;

        try {
            String dotaz = "SELECT vykresy_id "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }
            ResultSet id = PripojeniDB.dotazS(dotaz);

            while (id.next()) {
                vykresy_id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vykresy_id = 1;
        }

        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(pracovni_postup_pruvodka_id) FROM spolecne.pracovni_postup_pruvodka");
            while (id.next()) {
                pracovni_postup_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pruvodky_id = 1;
        }

        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(vazba_pracovni_postup_pruvodka_zamestnanci_id) FROM spolecne.vazba_pracovni_postup_pruvodka_zamestnanci");
            while (id.next()) {
                vazba_pos_zam_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pruvodky_id = 1;
        }

        int polotovary_id = -1;
        if (VyberPolotovaruComboBox1.getSelectedIndex() > -1) {
            polotovary_id = (Integer) ((Vector) vrPolotovary.get(VyberPolotovaruComboBox1.getSelectedIndex())).get(0);
        } else {
            polotovary_id = 1;
        } ///pruvodky_id,
        String dotaz = "UPDATE spolecne.pruvodky "
                + "SET pruvodky_nazev = '" + NazevPruvodkyTextField1.getText().trim() + "', "
                + "pruvodky_cislo_vykresu = " + vykresy_id + ", "
                + "pruvodky_termin_dokonceni = '" + TerminDodaniTextField1.getText().trim() + "', "
                + "pruvodky_pocet_kusu = " + PocetKusuTextField1.getText().trim() + ", "
                + "pruvodky_pocet_kusu_sklad = " + PocetKusuSkladTextField1.getText().trim() + ", "
                + "pruvodky_polotovar_pocet_kusu = " + PocetPolotovaruTextField1.getText().trim() + ", "
                + "pruvodky_polotovar_id = " + polotovary_id + ", "
                + "pruvodky_material_prumerna_delka = " + PrumernaDelkaTextField1.getText().trim() + ", "
                + "pruvodky_objednavky_id = " + (Long) ((Vector) vrObjednavka1.get(ObjednavkaComboBox1.getSelectedIndex())).get(0) + ", "
                + "pruvodky_polotovar_rozmer = '" + RozmerPolotovarutextField1.getText().trim() + "', "
                + "pruvodky_poznamky = '" + PoznamkaTextField1.getText().trim() + " ', "
                + "pruvodky_narezano_sklad = " + jTFNarezanoZeSkladu.getText().trim() + ", "
                + "pruvodky_odectena = " + jCBOdecteno.isSelected() + " "
                + "WHERE pruvodky_id = " + pruvodky_id;

        // System.out.println("Ulozeni pruvodky " +dotaz);
        try {
            int a = PripojeniDB.dotazIUD(dotaz);

            a = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_pruvodky_programy WHERE "
                    + "vazba_pruvodky_programy_pruvodky_id = " + pruvodky_id);

            dotaz = "SELECT pracovni_postup_pruvodka_id "
                    + "FROM spolecne.pracovni_postup_pruvodka "
                    + "WHERE pracovni_postup_pruvodka_pruvodka_id = " + pruvodky_id
                    + " ORDER BY pracovni_postup_pruvodka_id ASC";
            ResultSet popisyDel1 = PripojeniDB.dotazS(dotaz);
            Vector deletePopisy = new Vector();
            while (popisyDel1.next()) {
                //deletePopisy = new Vector();
                deletePopisy.add(new Long(popisyDel1.getLong(1)));
            }

            for (int i = 0; i < deletePopisy.size(); i++) {
                // System.out.println("Delete popisy : " + (Long) deletePopisy.get(i));
                a = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_pracovni_postup_pruvodka_zamestnanci WHERE "
                        + "vazba_pracovni_postup_pruvodka_zamestnanci_postup_id = " + (Long) deletePopisy.get(i));
            }

            a = PripojeniDB.dotazIUD("DELETE FROM spolecne.pracovni_postup_pruvodka WHERE "
                    + "pracovni_postup_pruvodka_pruvodka_id = " + pruvodky_id);
            //int programyId = 0;
            if (vrVybraneProgramy.size() > 0) {
                for (int i = 0; i < vrVybraneProgramy.size(); i++) {
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_pruvodky_programy( "
                            + "vazba_pruvodky_programy_pruvodky_id, vazba_pruvodky_programy_programy_id) "
                            + "VALUES (" + pruvodky_id + ", " + (Integer) ((Vector) vrVybraneProgramy.get(i)).get(0) + ")");
                }
            }

            if (pracovniPostup.getPocetKroku() > 0) {
                //System.out.println("pocet kroku prac. postup" + pracovniPostup.getPocetKroku());
                for (int i = 0; i < pracovniPostup.getPocetKroku(); i++) {
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.pracovni_postup_pruvodka( "
                            + "pracovni_postup_pruvodka_id, pracovni_postup_pruvodka_poradi, "
                            + "pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_druh_stroje_id, "
                            + "pracovni_postup_pruvodka_popis) "
                            + "VALUES (" + pracovni_postup_id + ", " + (i + 1) + ", " + pruvodky_id + ", "
                            + ((Integer) pracovniPostup.getValueAt(i, 0)).intValue() + ", '"
                            + (String) pracovniPostup.getValueAt(i, 1) + "')");
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_pracovni_postup_pruvodka_zamestnanci("
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_postup_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_zamestnanci_id, "
                            + "vazba_pracovni_postup_pruvodka_zamestnanci_stav_id) "
                            + "VALUES (" + vazba_pos_zam_id + ", " + pracovni_postup_id + ", "
                            + "50000001, 1)");
                    pracovni_postup_id++;
                    vazba_pos_zam_id++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        UlozPruvodkuButton1.setEnabled(false);
        return pruvodky_id;
    }

    protected void naplnFormular(long pruvodky_id) {
        odeberTableListenersPostupPraceTabulka();
        Vector vsPruvodka1 = null;
        Vector vsPolotovar1 = null;

        try {
            ResultSet pruvodka1 = PripojeniDB.dotazS(
                    "SELECT vykresy_cislo, vykresy_revize, vykresy_nazev, pruvodky_termin_dokonceni, "
                    + "pruvodky_pocet_kusu, pruvodky_pocet_kusu_sklad, pruvodky_polotovar_pocet_kusu, pruvodky_polotovar_id, "
                    + "pruvodky_material_prumerna_delka, pruvodky_polotovar_rozmer, pruvodky_poznamky, pruvodky_narezano_sklad, pruvodky_odectena "
                    + "FROM spolecne.pruvodky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE pruvodky_id = " + pruvodky_id
                    + " AND pruvodky_cislo_vykresu = vykresy_id");
            while (pruvodka1.next()) {
                vsPruvodka1 = new Vector();
                vsPruvodka1.add(pruvodka1.getString(1));            //cislo vykresu
                vsPruvodka1.add(pruvodka1.getString(2));            //revize vykresu
                vsPruvodka1.add(pruvodka1.getString(3));            //nazev vykresu - pruvodky
                vsPruvodka1.add(df.format(pruvodka1.getDate(4)));   //datum dodani
                vsPruvodka1.add(new Integer(pruvodka1.getInt(5)));  //pocet kusu
                vsPruvodka1.add(new Integer(pruvodka1.getInt(6)));  //pocet kusu sklad
                vsPruvodka1.add(new Integer(pruvodka1.getInt(7)));  //pocet kusu polotovar
                vsPruvodka1.add(new Integer(pruvodka1.getInt(8)));  //polotovar id
                vsPruvodka1.add(new Integer(pruvodka1.getInt(9)));  //pruvodky material prumerna delka

                PoznamkaTextField1.setText(pruvodka1.getString(11));

                try {
                    vsPruvodka1.add(pruvodka1.getString(10));
                } catch (Exception e) {
                    vsPruvodka1.add("");
                }
                //pruvodka rozmer polotovaru na pilu
                vsPruvodka1.add(SQLFunkceObecne.osetriCteniInt(pruvodka1.getInt(12)));
                vsPruvodka1.add(pruvodka1.getBoolean(13));
            }// konec while

            ResultSet polotovar1 = PripojeniDB.dotazS(
                    "SELECT polotovary_nazev, polotovary_typ_materialu "
                    + "FROM spolecne.polotovary WHERE polotovary_id  = " + ((Integer) vsPruvodka1.get(7)).intValue());
            while (polotovar1.next()) {
                vsPolotovar1 = new Vector();
                vsPolotovar1.add(new String(polotovar1.getString(1)));
                vsPolotovar1.add(new Integer(polotovar1.getInt(2)));
                //vsPolotovar1.add(new Integer(polotovar1.getInt(3)));
            }

            ResultSet programy1 = PripojeniDB.dotazS(
                    /* "SELECT programy_id, programy_cislo, programy_revize, "
                     + "programy_material_rozmer, zamestnanci_prijmeni, programy_cas_sestaveni "
                     + "FROM spolecne.programy "
                     + "CROSS JOIN spolecne.vazba_pruvodky_programy "
                     + "CROSS JOIN spolecne.zamestnanci "
                     + "WHERE spolecne.programy.programy_id = spolecne.vazba_pruvodky_programy.vazba_pruvodky_programy_programy_id "
                     + "AND spolecne.zamestnanci.zamestnanci_id = spolecne.programy.programy_sestaveno_zamestnancem "
                     + "AND spolecne.vazba_pruvodky_programy.vazba_pruvodky_programy_pruvodky_id = " + pruvodky_id);*/
                    "SELECT programy_id, programy_cislo, programy_revize, "
                    + "programy_material_rozmer "
                    + "FROM spolecne.programy "
                    + "CROSS JOIN spolecne.vazba_pruvodky_programy "
                    + "WHERE spolecne.programy.programy_id = spolecne.vazba_pruvodky_programy.vazba_pruvodky_programy_programy_id "
                    + "AND spolecne.vazba_pruvodky_programy.vazba_pruvodky_programy_pruvodky_id = " + pruvodky_id);
            Vector vsVybraneProgramy;
            while (programy1.next()) {
                vsVybraneProgramy = new Vector();
                vsVybraneProgramy.add(new Integer(programy1.getInt(1)));
                vsVybraneProgramy.add(new String(programy1.getString(2)));
                try {
                    vsVybraneProgramy.add(new String(programy1.getString(3)));
                } catch (Exception e) {
                    vsVybraneProgramy.add("");
                }
                try {
                    vsVybraneProgramy.add(new String(programy1.getString(4)));
                } catch (Exception e) {
                    vsVybraneProgramy.add("");
                }
                try {
                    vsVybraneProgramy.add(new String(programy1.getString(5)));
                } catch (Exception e) {
                    vsVybraneProgramy.add("");
                }
                try {
                    vsVybraneProgramy.add(df.format(programy1.getDate(6)));
                } catch (Exception e) {
                    vsVybraneProgramy.add("");
                }

                vrVybraneProgramy.add(vsVybraneProgramy);
            }

            ResultSet popisy1 = PripojeniDB.dotazS(
                    "SELECT pracovni_postup_pruvodka_poradi, pracovni_postup_pruvodka_druh_stroje_id, "
                    + "pracovni_postup_pruvodka_popis "
                    + "FROM spolecne.pracovni_postup_pruvodka "
                    + "WHERE pracovni_postup_pruvodka_pruvodka_id = " + pruvodky_id
                    + "ORDER BY pracovni_postup_pruvodka_poradi");

            vrPostupy = new Vector();
            while (popisy1.next()) {
                vrPostupy.add(new DvojiceCisloRetez(new Integer(popisy1.getInt(2)), new String(popisy1.getString(3))));

            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        NazevPruvodkyTextField1.setText((String) vsPruvodka1.get(2));
        TerminDodaniTextField1.setText(((String) vsPruvodka1.get(3)));
        PocetKusuTextField1.setText(((Integer) vsPruvodka1.get(4)).toString());
        PocetKusuSkladTextField1.setText(((Integer) vsPruvodka1.get(5)).toString());
        PocetPolotovaruTextField1.setText(((Integer) vsPruvodka1.get(6)).toString());
        PrumernaDelkaTextField1.setText(((Integer) vsPruvodka1.get(8)).toString());
        RozmerPolotovarutextField1.setText((String) vsPruvodka1.get(9));
        jTFNarezanoZeSkladu.setText(((Integer) vsPruvodka1.get(10)).toString());
        jCBOdecteno.setSelected((Boolean) vsPruvodka1.get(11));
        for (int i = 0; i < vrMaterialy.size(); i++) {
            if (((Integer) vsPolotovar1.get(1)).intValue() == ((Integer) ((Vector) vrMaterialy.get(i)).get(0))) {
                VyberMaterialuComboBox1.setSelectedIndex(i);
            }
        }

        /* for (int i = 0; i < vrTypPolotovary.size(); i++) {
         if (((Integer) vsPolotovar1.get(2)).intValue() == ((Integer) ((Vector) vrTypPolotovary.get(i)).get(0))) {
         TypPolotovaruComboBox1.setSelectedIndex(i);
         }
         }*/
        VyberPolotovaruComboBox1.setSelectedItem((String) vsPolotovar1.get(0));
        /* for (int i = 0; i < vrPolotovary.size(); i++) {
         if (((Integer) vsPruvodka1.get(6)).intValue() == ((Integer) ((Vector) vrPolotovary.get(i)).get(0))) {
         VyberPolotovaruComboBox1.setSelectedItem(jLabel1);
         }
         }*/

        // for (int i = 0; i < vrVykresy.size(); i++) {
        //  if (((Integer) vsPruvodka1.get(1)).intValue() == ((Integer) ((Vector) vrVykresy.get(i)).get(0))) {
        CisloVykresuTextField1.setText((String) vsPruvodka1.get(0));
        ZmenaVykresuTextField1.setText((String) vsPruvodka1.get(1));
        // }
        // }

        zjistiVyrobkySklad();
        // initProgramy(zjistiIdVykresu());
        initProgramy((String) vsPruvodka1.get(0));
        programyModel.pridejSadu();
        pracovniPostup.pridejSadu();
        nastavTableListenersPostupPraceTabulka();

        try {
            String dotaz
                    = "SELECT objednavky_id, "
                    + "objednavky_nazev_soucasti, "
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_kusu_navic, "
                    + "objednavky_kusu_vyrobit "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.pruvodky "
                    + "WHERE pruvodky.pruvodky_objednavky_id = objednavky.objednavky_id "
                    + "AND pruvodky.pruvodky_id = " + pruvodky_id;

            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            while (objednavka1.next()) {
                vsObjednavka1 = new Vector();
                vsObjednavka1.add(new Long(objednavka1.getLong(1))); // id
                vsObjednavka1.add(objednavka1.getString(2)); // nazev soucasti
                vsObjednavka1.add(objednavka1.getString(3));   // cislo objednavky
                vsObjednavka1.add(new Integer(objednavka1.getInt(4))); // pocet kusu objednany
                try {
                    vsObjednavka1.add(new Integer(objednavka1.getInt(5))); // kusy navic
                } catch (Exception e) {
                    vsObjednavka1.add(0);
                }
                try {
                    vsObjednavka1.add(new Integer(objednavka1.getInt(6))); // kusy vyrobit
                } catch (Exception e) {
                    vsObjednavka1.add(0);
                }

                vrObjednavka1.add(vsObjednavka1);
                ObjednavkaComboBox1.addItem(objednavka1.getString(2) + " " + objednavka1.getString(3));

            }
            // dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    protected void nastavTableListenersPostupPraceTabulka() {
        lsmr1.addListSelectionListener(lslUdalosti);
        lsms1.addListSelectionListener(lslUdalosti);
    }

    protected void odeberTableListenersPostupPraceTabulka() {
        lsmr1.removeListSelectionListener(lslUdalosti);
        lsms1.removeListSelectionListener(lslUdalosti);
    }

    protected void nastavVyberPolotovaruComboBoxListener() {
    }

    protected void odeberVyberPolotovaruComboBoxListener() {
        lsmr1.removeListSelectionListener(lslUdalosti);
        lsms1.removeListSelectionListener(lslUdalosti);
    }

    protected void getPrazdnaDataTabulkaPracovniPostupy() {
        vrPostupy.removeAllElements();

        int druhStroje = SQLFunkceObecne2.selectINTPole("SELECT min (druhy_stroju_id) FROM spolecne.druhy_stroju");
        if (druhStroje < 1) {
            JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při načítání z DB", "Nepodařilo se načíst seznam druhů strojů z databáze!");
            return;
        } else {
            for (int i = 0; i < 15; i++) {
                vrPostupy.add(new DvojiceCisloRetez(druhStroje, ""));
            }

        } // konec try

        pracovniPostup.oznamZmenu();

    } //konec getDataTabulkaPruvodka1

    private void tiskPruvodky() {
        if (pruvodky_id_uloz == -1) {
            ulozPruvodku();
        }
        int prebytky = Double.valueOf(VyrobkySkladLabel1.getText().trim()).intValue();
        int vyrobitKusu = Integer.valueOf(PocetKusuSkladTextField1.getText().trim()).intValue()
                + Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue();
        String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "pruvodkaReport2.jrxml";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nazev_pruvodky", NazevPruvodkyTextField1.getText().trim());
        params.put("cislo_vykresu", CisloVykresuTextField1.getText().trim());
        params.put("termin_dodani", TerminDodaniTextField1.getText().trim());
        params.put("objednano_kusu", ObjednanoKusuLabel1.getText().trim()); // upravit
        params.put("prebytky_kusu", prebytky + "");
        params.put("vyrobit_kusu", vyrobitKusu + "");
        params.put("druh_materialu", VyberMaterialuComboBox1.getSelectedItem().toString());
        params.put("polotovar_rozmer", RozmerPolotovarutextField1.getText().trim());
        params.put("narezat_kusu", PocetPolotovaruTextField1.getText().trim());
        params.put("prumerna_delka", PrumernaDelkaTextField1.getText().trim());
        params.put("cislo_regalu", UmisteniSkladLabel1.getText().trim());
        for (int i = 0; i < vrVybraneProgramy.size(); i++) {
            params.put(("program" + (i + 1)), (String) ((Vector) vrVybraneProgramy.get(i)).get(1));
        }
        for (int i = vrVybraneProgramy.size(); i < 4; i++) {
            params.put(("program" + (i + 1)), "");
        }
        // params.put("pruvodky_id", pruvodky_id + "");
        params.put("pruvodky_id", pruvodky_id_uloz + "");
        params.put("pruvodky_int_id", pruvodky_id_uloz);

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

    private void kontrolaVykresu() {
        try {
            vsVykresyZmeny.removeAllElements();
            vrVykresyZmeny.removeAllElements();
            String dotaz = "SELECT vykresy_id, vykresy_nazev "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            } else {
                dotaz += " AND (vykresy_revize = '' OR vykresy_revize IS NULL)";
            }

            System.out.println(dotaz);
            ResultSet vykresyZmeny1 = PripojeniDB.dotazS(dotaz);

            while (vykresyZmeny1.next()) {
                vsVykresyZmeny = new Vector();
                vsVykresyZmeny.add(new Integer(vykresyZmeny1.getInt(1)));
                vsVykresyZmeny.add(new String(vykresyZmeny1.getString(2)));
                vrVykresyZmeny.add(vsVykresyZmeny);
                NazevPruvodkyTextField1.setText((String) vsVykresyZmeny.get(1));
            }

        } // konec try
        catch (Exception e) {
            NovyVykresFrame novyVykres = new NovyVykresFrame(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim(), false);
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);

        } // konec catch
        /* if (vrVykresyZmeny.size() < 1) {
         System.out.println("nenalezeno");
         NazevPruvodkyTextField1.setText("");
         NovyVykresFrame novyVykres = new NovyVykresFrame(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim());
         }*/

        zjistiVyrobkySklad();
        // initProgramy((Integer) vsVykresyZmeny.get(0));
        initProgramy(CisloVykresuTextField1.getText().trim());
    }

    private int zjistiIdVykresu() {
        int vykresy_id = -1;
        try {
            String dotaz = "SELECT vykresy_id "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }
            ResultSet id = PripojeniDB.dotazS(dotaz);
            while (id.next()) {
                vykresy_id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vykresy_id;
    }

    private void nacistObjednavky() {
        vrObjednavka1.removeAllElements();
        vsObjednavka1.removeAllElements();
        ObjednavkaComboBox1.removeAllItems();
        ObjednanoKusuLabel1.setText("");

        float celkovaDobaMcv = 0;
        float celkovaDobaTornado = 0;
        boolean dotazOk = false;
        boolean datumDodani = false;

        //System.out.println("dotaz filtr ");  + "objednavky_termin_dodani, "
        try {
            String dotaz
                    = "SELECT objednavky_id, "
                    + "objednavky_nazev_soucasti, "
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_kusu_navic, "
                    + "objednavky_kusu_vyrobit "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "AND vykresy.vykresy_cislo LIKE '%" + CisloVykresuTextField1.getText().trim() + "%' ";

            boolean osetritDatumDodani = TextFunkce1.osetriDatum(TerminDodaniTextField1.getText().trim());

            if (osetritDatumDodani == true) {
                datumDodani = true;
                dotaz += "AND objednavky_termin_dodani = '" + TerminDodaniTextField1.getText().trim() + "' ";
            }
            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            while (objednavka1.next()) {
                vsObjednavka1 = new Vector();
                vsObjednavka1.add(new Long(objednavka1.getLong(1))); // id
                vsObjednavka1.add(objednavka1.getString(2)); // nazev soucasti
                vsObjednavka1.add(objednavka1.getString(3));   // cislo objednavky
                vsObjednavka1.add(new Integer(objednavka1.getInt(4))); // pocet kusu objednany
                try {
                    vsObjednavka1.add(new Integer(objednavka1.getInt(5))); // kusy navic
                } catch (Exception e) {
                    vsObjednavka1.add(0);
                }
                try {
                    vsObjednavka1.add(new Integer(objednavka1.getInt(6))); // kusy vyrobit
                } catch (Exception e) {
                    vsObjednavka1.add(0);
                }

                vrObjednavka1.add(vsObjednavka1);
                ObjednavkaComboBox1.addItem(objednavka1.getString(2) + " " + objednavka1.getString(3));

            }// konec while
            dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch     

        //ObjednavkaComboBox1.setSelectedIndex(0);
    }

    private void zmenaKusuVyroba() {
        try {
            int index = ObjednavkaComboBox1.getSelectedIndex();
            PocetKusuTextField1.setText((Integer) ((Vector) vrObjednavka1.get(index)).get(5) + "");
            PocetKusuSkladTextField1.setText((Integer) ((Vector) vrObjednavka1.get(index)).get(4) + "");
            ObjednanoKusuLabel1.setText((Integer) ((Vector) vrObjednavka1.get(index)).get(3) + "");
        } catch (Exception e) {
            try {
                PocetKusuTextField1.setText((Integer) ((Vector) vrObjednavka1.get(0)).get(5) + "");

                PocetKusuSkladTextField1.setText((Integer) ((Vector) vrObjednavka1.get(0)).get(4) + "");
                ObjednanoKusuLabel1.setText((Integer) ((Vector) vrObjednavka1.get(0)).get(3) + "");
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }

    }

    private void odebratRadek() {
        System.out.println("PruvodkaFrame odebrat radek : " + postupPraceTable.getSelectedRow());
        vrPostupy.remove(postupPraceTable.getSelectedRow());
        pracovniPostup.oznamZmenu();
    }

    class ComboBoxMaterialUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
//            System.out.println("Material vybrana radkasss focus " + e.getClass().toString() + " " + e.getItem().toString() + " x  " + e.getStateChange());
            //     System.out.println("vybaa " + TypPolotovaruComboBox1.getSelectedIndex());
            //typPolotovaruId = (Integer) ((Vector) vrTypPolotovary.get(TypPolotovaruComboBox1.getSelectedIndex())).get(0);
            typMaterialuId = (Integer) ((Vector) vrMaterialy.get(VyberMaterialuComboBox1.getSelectedIndex())).get(0);
            zjistiPolotovarySeznam();
            //e.getItem()
        }
    }//konec FLUdalosti

    class ComboBoxPolotovarUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            // System.out.println("Polotovar vybrana radkasss focus " + e.getClass().toString() + " " + e.getItem().toString() + " x  " + e.getStateChange());
            // System.out.println("vybaa " + TypPolotovaruComboBox1.getSelectedIndex());
            // typPolotovaruId = (Integer) ((Vector) vrTypPolotovary.get(TypPolotovaruComboBox1.getSelectedIndex())).get(0);
            typMaterialuId = (Integer) ((Vector) vrMaterialy.get(VyberMaterialuComboBox1.getSelectedIndex())).get(0);
            zjistiPolotovarySeznam();
            //e.getItem()
        }
    }//konec FLUdalosti

    class ComboBoxVyberPolotovaruUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            zjistiPolotovarySklad();
        }
    }//konec FLUdalosti

    class PridatButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            odeberTableListenersPostupPraceTabulka();
            vrVybraneProgramy.add((Vector) vrProgramy.get(jCBPridatProgram.getSelectedIndex()));
            programyModel.pridejSadu();
            nastavTableListenersPostupPraceTabulka();
        }
    }

    class UlozButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ulozPruvodku();

        }
    }

    class PridatRadekButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            pracovniPostup.pridejNovyRadek();
        }
    }

    class OdebratRadekButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            odebratRadek();
        }
    }

    class OdebratButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = ProgramyTable.getSelectedRow();
            vrVybraneProgramy.removeElementAt(row);
            programyModel.odeberSadu(row);
        }
    }

    protected class tabulkaModelProgramy extends AbstractTableModel {

        protected final String[] columnNames = {"Název"/*,
         "Revize",
         "Rozměr materiálu",
         "Zhotovil",
         "Čas zhotovení"*/

        };

        public void pridejSadu() {
            fireTableRowsInserted(0, vrVybraneProgramy.size());
            //fireTableRowsDeleted(WIDTH, WIDTH);
            //updateZaznamyProgram1();
            if (vrVybraneProgramy.size() > 0) {
                ProgramyTable.changeSelection(0, 0, false, false);
            }
        }

        public void odeberSadu(int row) {
            fireTableRowsDeleted(row, row);
            //updateZaznamyProgram1();
            if (vrVybraneProgramy.size() > 0) {
                ProgramyTable.changeSelection(0, 0, false, false);
            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            //  System.out.println(row + " getValueAt PoloFram " + col);
            try {
                return (((Vector) vrVybraneProgramy.elementAt(row)).elementAt(col + 1));
            } catch (Exception ex) {
                //ex.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            //System.out.println("setValueAt PruvFram");
            if (col == 1) {
                ((Vector) vrVybraneProgramy.get(row)).setElementAt(value, col);
                fireTableCellUpdated(row, col);
                //nastavVyberTabulkyPruvodka1();
                return;
            } //konec if
            try {
                vrVybraneProgramy.get(row);
                // pruvodka = updatePruvodka1DB(pruvodka);
                //akce po update s osetrenim chyb
                if (pruvodka.getIdPruvodky() > 0) {
                    fireTableCellUpdated(row, col);
                    //nastavHodnotuNaVybrane(pruvodka);
                }
                if (pruvodka.getIdPruvodky() == 0) {
                    //   deletePruvodka1TAB();
                }

            }//konec try
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }//konec setValueAt           

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(ProgramyTable.getSelectedRow(), ProgramyTable.getSelectedRow());
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
            return vrVybraneProgramy.size();
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

    protected class tabulkaPracovniPostup extends AbstractTableModel {

        protected final String[] columnNames = {"Druh stroje",
            "Popis práce"
        };

        public void pridejSadu() {
            fireTableRowsInserted(0, vrPostupy.size());
            //updateZaznamyProgram1();
            if (vrPostupy.size() > 0) {
                postupPraceTable.changeSelection(0, 0, false, false);
            }
        }

        public void pridejNovyRadek() {
            int druhStroje = SQLFunkceObecne2.selectINTPole("SELECT min (druhy_stroju_id) FROM spolecne.druhy_stroju");
            if (druhStroje < 1) {
                JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při načítání z DB", "Nepodařilo se načíst seznam druhů strojů z databáze!");
                return;
            } else {
                if (postupPraceTable.getRowCount() < 1) {
                    vrPostupy.removeAllElements();
                    vrPostupy.add(new DvojiceCisloRetez(druhStroje, ""));
                    pridejSadu();
                }
                if ((postupPraceTable.getRowCount() > 0) && (postupPraceTable.getSelectedRow() > -1)) {
                    int row = postupPraceTable.getSelectedRow();
                    vrPostupy.add(row + 1, new DvojiceCisloRetez(druhStroje, ""));
                    oznamZmenu();
                    postupPraceTable.changeSelection(row + 1, 1, true, false);
                }
                if ((postupPraceTable.getRowCount() > 0) && (postupPraceTable.getSelectedRow() < 0)) {
                    vrPostupy.add(new DvojiceCisloRetez(druhStroje, ""));
                    oznamZmenu();
                    postupPraceTable.changeSelection(postupPraceTable.getRowCount() - 1, 1, false, false);
                }
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void odeberSadu(int row) {
            fireTableRowsDeleted(row, row);
            //updateZaznamyProgram1();
            if (vrPostupy.size() > 0) {
                postupPraceTable.changeSelection(0, 0, false, false);

            }
        }

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrPruvodka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        @Override
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                try {
                    return ((Integer) ((DvojiceCisloRetez) vrPostupy.elementAt(row)).cislo());
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    return null;
                }
            }//konec getValueAt
            else {
                try {
                    return ((DvojiceCisloRetez) vrPostupy.elementAt(row)).toString();
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    return null;
                }
            }//konec getValueAt
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            //         System.out.println(value.toString() + " x " + value.getClass());
            if (col == 0) {
                ((DvojiceCisloRetez) vrPostupy.get(row)).setCislo(((Integer) value).intValue());

            } //konec if
            if (col == 1) {
                ((DvojiceCisloRetez) vrPostupy.get(row)).setRetez((String) value.toString());
            } //konec if
            fireTableCellUpdated(row, col);
            return;

        }//konec setValueAt

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(postupPraceTable.getSelectedRow(), postupPraceTable.getSelectedRow());
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
            return vrPostupy.size();
        }//konec getRowCount

        public int getPocetKroku() {
            int posledniIndex = vrPostupy.size() - 1;
            for (int i = vrPostupy.size() - 1; i >= 0; i--) {
                String krok = (String) getValueAt(i, 1);
                // System.out.println("krok : " + krok);
                if (!(krok.equals(null) || krok.equals(" ") || krok.equals(""))) {
                    posledniIndex = i;
                    break;
                }
            }
            return posledniIndex + 1;
        }

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        public Class getColumnClass(int col) {
            if (col == 0) {
                return Integer.class;
            }
            return String.class;
        }
    }//konec modelu tabulky

    protected void nastavVyberPoliTabulka1() {
        if (postupPraceTable.getSelectedColumn() == 1) {
//            System.out.println("Nastavuji editaci castek ");
            postupPraceTable.editCellAt(postupPraceTable.getSelectedRow(), postupPraceTable.getSelectedColumn());
            ((TableTextEditor1) postupPraceTable.getCellEditor(postupPraceTable.getSelectedRow(), postupPraceTable.getSelectedColumn())).requestFocus();
            //((TableCastkaEditor1)postupPraceTable.getCellEditor(postupPraceTable.getSelectedRow(), JTablePredpisZauctovani.getSelectedColumn())).requestFocus();
//            ((TableCastkaEditor1)JTablePredpisZauctovani.getCellEditor(JTablePredpisZauctovani.getSelectedRow(), JTablePredpisZauctovani.getSelectedColumn())).fireEditingStarted();
        }
    }

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmr1) {
                if (e.getValueIsAdjusting() == false) {
                    // System.out.println("vybrana radka ");
                    nastavVyberPoliTabulka1();
                }
            }
            if (e.getSource() == lsms1) {
                nastavVyberPoliTabulka1();
                // System.out.println("vybrany sloupec");
            }
        }
    } //konec LSLUdalosti

    class AkceUdalosti implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("VyberPolotovaruPrikaz")) {
                System.out.println("Akce combo");
                zjistiPolotovarySklad();
            }

            if (e.getSource() == OdebratRadekButton1) {
                odebratRadek();
            }

        }
    } //konec AkceUdalosti

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        ZmenaVykresuTextField1 = new javax.swing.JTextField();
        NazevPruvodkyTextField1 = new javax.swing.JTextField();
        UmisteniSkladLabel1 = new javax.swing.JTextField();
        VyrobkySkladLabel1 = new javax.swing.JTextField();
        TerminDodaniTextField1 = new javax.swing.JTextField();
        ObjednavkaComboBox1 = new javax.swing.JComboBox();
        ObjednanoKusuLabel1 = new javax.swing.JTextField();
        PocetKusuTextField1 = new javax.swing.JTextField();
        PocetKusuSkladTextField1 = new javax.swing.JTextField();
        FiltrCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        PocetPolotovaruTextField1 = new javax.swing.JTextField();
        PrumernaDelkaTextField1 = new javax.swing.JTextField();
        RozmerPolotovarutextField1 = new javax.swing.JTextField();
        VyberMaterialuComboBox1 = new javax.swing.JComboBox();
        jLabelPolotovarySklad = new javax.swing.JTextField();
        jLPolotovarySkladZbyle = new javax.swing.JTextField();
        VyberPolotovaruComboBox1 = new javax.swing.JComboBox();
        jCBPridatProgram = new javax.swing.JComboBox();
        jTFNarezanoZeSkladu = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jCBOdecteno = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jLabelPruvodkyAlokovano = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        postupPraceTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        PoznamkaTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        ProgramyTable = new javax.swing.JTable();
        PridatButton = new javax.swing.JButton();
        OdebratButton = new javax.swing.JButton();
        NovyProgramButton1 = new javax.swing.JButton();
        RefreshButton1 = new javax.swing.JButton();
        UlozPruvodkuButton1 = new javax.swing.JButton();
        TiskPruvodkyButton1 = new javax.swing.JButton();
        PridatRadekButton1 = new javax.swing.JButton();
        OdebratRadekButton1 = new javax.swing.JButton();

        jLabel13.setText("jLabel13");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Průvodka");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Změna výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel5.setText("Aktuální naskladněné množství :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Název průvodky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Umístění ve skladu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jLabel8.setText("Termín dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Číslo objednávky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Objednáno kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Kusů vyrobit :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Vyrobit navíc :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel12, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel1.add(CisloVykresuTextField1, gridBagConstraints);

        ZmenaVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(ZmenaVykresuTextField1, gridBagConstraints);

        NazevPruvodkyTextField1.setNextFocusableComponent(TerminDodaniTextField1);
        NazevPruvodkyTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                NazevPruvodkyTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(NazevPruvodkyTextField1, gridBagConstraints);

        UmisteniSkladLabel1.setEditable(false);
        UmisteniSkladLabel1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(UmisteniSkladLabel1, gridBagConstraints);

        VyrobkySkladLabel1.setEditable(false);
        VyrobkySkladLabel1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(VyrobkySkladLabel1, gridBagConstraints);

        TerminDodaniTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        TerminDodaniTextField1.setMinimumSize(new java.awt.Dimension(6, 20));
        TerminDodaniTextField1.setNextFocusableComponent(PocetPolotovaruTextField1);
        TerminDodaniTextField1.setPreferredSize(new java.awt.Dimension(6, 20));
        TerminDodaniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TerminDodaniTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                TerminDodaniTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(TerminDodaniTextField1, gridBagConstraints);

        ObjednavkaComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ObjednavkaComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ObjednavkaComboBox1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(ObjednavkaComboBox1, gridBagConstraints);

        ObjednanoKusuLabel1.setEditable(false);
        ObjednanoKusuLabel1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(ObjednanoKusuLabel1, gridBagConstraints);

        PocetKusuTextField1.setEditable(false);
        PocetKusuTextField1.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(PocetKusuTextField1, gridBagConstraints);

        PocetKusuSkladTextField1.setEditable(false);
        PocetKusuSkladTextField1.setBackground(new java.awt.Color(255, 204, 204));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(PocetKusuSkladTextField1, gridBagConstraints);

        FiltrCheckBox1.setSelected(true);
        FiltrCheckBox1.setText("Filtrovat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel1.add(FiltrCheckBox1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Nařezat kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Rozměr materiálu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel16.setText("Číslo programu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 5, 5);
        jPanel2.add(jLabel16, gridBagConstraints);

        jLabel17.setText("Zbylé množství :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel17, gridBagConstraints);

        jLabel18.setText("Alokováno v průvodkách :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel18, gridBagConstraints);

        jLabel19.setText("Rozměr polotovaru :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel19, gridBagConstraints);

        jLabel20.setText("Průměrná délka [mm] :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel20, gridBagConstraints);

        jLabel21.setText("Materiál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel21, gridBagConstraints);

        PocetPolotovaruTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PocetPolotovaruTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PocetPolotovaruTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel2.add(PocetPolotovaruTextField1, gridBagConstraints);

        PrumernaDelkaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PrumernaDelkaTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PrumernaDelkaTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PrumernaDelkaTextField1, gridBagConstraints);

        RozmerPolotovarutextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                RozmerPolotovarutextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(RozmerPolotovarutextField1, gridBagConstraints);

        VyberMaterialuComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(VyberMaterialuComboBox1, gridBagConstraints);

        jLabelPolotovarySklad.setEditable(false);
        jLabelPolotovarySklad.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabelPolotovarySklad, gridBagConstraints);

        jLPolotovarySkladZbyle.setEditable(false);
        jLPolotovarySkladZbyle.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLPolotovarySkladZbyle, gridBagConstraints);

        VyberPolotovaruComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(VyberPolotovaruComboBox1, gridBagConstraints);

        jCBPridatProgram.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jCBPridatProgram, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTFNarezanoZeSkladu, gridBagConstraints);

        jLabel22.setText("Nařezáno ze skladu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel22, gridBagConstraints);

        jCBOdecteno.setText("Odečteno ze skladu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        jPanel2.add(jCBOdecteno, gridBagConstraints);

        jLabel23.setText("Naskladněné množství :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel23, gridBagConstraints);

        jLabelPruvodkyAlokovano.setEditable(false);
        jLabelPruvodkyAlokovano.setBorder(null);
        jLabelPruvodkyAlokovano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLabelPruvodkyAlokovanoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabelPruvodkyAlokovano, gridBagConstraints);

        postupPraceTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(postupPraceTable);

        jScrollPane1.setViewportView(jScrollPane2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel14.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 10, 10);
        jPanel3.add(jLabel14, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel3.add(PoznamkaTextField1, gridBagConstraints);

        ProgramyTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(ProgramyTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel3.add(jScrollPane3, gridBagConstraints);

        PridatButton.setText("Přidat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(PridatButton, gridBagConstraints);

        OdebratButton.setText("Odebrat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(OdebratButton, gridBagConstraints);

        NovyProgramButton1.setText("Nový");
        NovyProgramButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NovyProgramButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(NovyProgramButton1, gridBagConstraints);

        RefreshButton1.setText("Obnovit");
        RefreshButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(RefreshButton1, gridBagConstraints);

        UlozPruvodkuButton1.setText("Uložit průvodku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(UlozPruvodkuButton1, gridBagConstraints);

        TiskPruvodkyButton1.setText("Tisk průvodky");
        TiskPruvodkyButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TiskPruvodkyButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(TiskPruvodkyButton1, gridBagConstraints);

        PridatRadekButton1.setText("Přidat řádek");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(PridatRadekButton1, gridBagConstraints);

        OdebratRadekButton1.setText("Odebrat řádek");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(OdebratRadekButton1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ZmenaVykresuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusLost
        kontrolaVykresu();

    }//GEN-LAST:event_ZmenaVykresuTextField1FocusLost

    private void TerminDodaniTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TerminDodaniTextField1FocusLost
        nacistObjednavky();
    }//GEN-LAST:event_TerminDodaniTextField1FocusLost

    private void ObjednavkaComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ObjednavkaComboBox1ItemStateChanged
        zmenaKusuVyroba();
    }//GEN-LAST:event_ObjednavkaComboBox1ItemStateChanged

    private void RefreshButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshButton1ActionPerformed
        initProgramy(CisloVykresuTextField1.getText().trim());
    }//GEN-LAST:event_RefreshButton1ActionPerformed

    private void NovyProgramButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NovyProgramButton1ActionPerformed
        NovyProgramFrame1 program = new NovyProgramFrame1(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim());
    }//GEN-LAST:event_NovyProgramButton1ActionPerformed

    private void TiskPruvodkyButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TiskPruvodkyButton1ActionPerformed
        tiskPruvodky();
    }//GEN-LAST:event_TiskPruvodkyButton1ActionPerformed

    private void ZmenaVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusGained
        ZmenaVykresuTextField1.selectAll();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusGained

    private void NazevPruvodkyTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NazevPruvodkyTextField1FocusGained
        NazevPruvodkyTextField1.selectAll();
    }//GEN-LAST:event_NazevPruvodkyTextField1FocusGained

    private void TerminDodaniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TerminDodaniTextField1FocusGained
        TerminDodaniTextField1.selectAll();
    }//GEN-LAST:event_TerminDodaniTextField1FocusGained

    private void PocetPolotovaruTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetPolotovaruTextField1FocusGained
        PocetPolotovaruTextField1.selectAll();
    }//GEN-LAST:event_PocetPolotovaruTextField1FocusGained

    private void PrumernaDelkaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrumernaDelkaTextField1FocusGained
        PrumernaDelkaTextField1.selectAll();
    }//GEN-LAST:event_PrumernaDelkaTextField1FocusGained

    private void RozmerPolotovarutextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RozmerPolotovarutextField1FocusGained
        RozmerPolotovarutextField1.selectAll();
    }//GEN-LAST:event_RozmerPolotovarutextField1FocusGained

    private void PocetPolotovaruTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetPolotovaruTextField1FocusLost
        if (PrumernaDelkaTextField1.getText().trim().length() > 0) {
            zjistiPolotovarySklad();
        }
    }//GEN-LAST:event_PocetPolotovaruTextField1FocusLost

    private void PrumernaDelkaTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrumernaDelkaTextField1FocusLost
        if (PocetPolotovaruTextField1.getText().trim().length() > 0) {
            zjistiPolotovarySklad();
        }
    }//GEN-LAST:event_PrumernaDelkaTextField1FocusLost

    private void jLabelPruvodkyAlokovanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLabelPruvodkyAlokovanoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelPruvodkyAlokovanoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JCheckBox FiltrCheckBox1;
    private javax.swing.JTextField NazevPruvodkyTextField1;
    private javax.swing.JButton NovyProgramButton1;
    private javax.swing.JTextField ObjednanoKusuLabel1;
    private javax.swing.JComboBox ObjednavkaComboBox1;
    private javax.swing.JButton OdebratButton;
    private javax.swing.JButton OdebratRadekButton1;
    private javax.swing.JTextField PocetKusuSkladTextField1;
    private javax.swing.JTextField PocetKusuTextField1;
    private javax.swing.JTextField PocetPolotovaruTextField1;
    private javax.swing.JTextField PoznamkaTextField1;
    private javax.swing.JButton PridatButton;
    private javax.swing.JButton PridatRadekButton1;
    private javax.swing.JTable ProgramyTable;
    private javax.swing.JTextField PrumernaDelkaTextField1;
    private javax.swing.JButton RefreshButton1;
    private javax.swing.JTextField RozmerPolotovarutextField1;
    private javax.swing.JTextField TerminDodaniTextField1;
    private javax.swing.JButton TiskPruvodkyButton1;
    private javax.swing.JButton UlozPruvodkuButton1;
    private javax.swing.JTextField UmisteniSkladLabel1;
    private javax.swing.JComboBox VyberMaterialuComboBox1;
    private javax.swing.JComboBox VyberPolotovaruComboBox1;
    private javax.swing.JTextField VyrobkySkladLabel1;
    private javax.swing.JTextField ZmenaVykresuTextField1;
    private javax.swing.JCheckBox jCBOdecteno;
    private javax.swing.JComboBox jCBPridatProgram;
    private javax.swing.JTextField jLPolotovarySkladZbyle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jLabelPolotovarySklad;
    private javax.swing.JTextField jLabelPruvodkyAlokovano;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTFNarezanoZeSkladu;
    private javax.swing.JTable postupPraceTable;
    // End of variables declaration//GEN-END:variables
}
