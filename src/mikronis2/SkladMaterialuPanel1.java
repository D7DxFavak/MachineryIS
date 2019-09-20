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
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import mikronis2.dbtridy.*;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class SkladMaterialuPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelDetail1, tableModelHlavni1, tableModelPruvodky1;
    protected TabulkaModelDetail tabulkaModelDetail1;
    protected TabulkaModelHlavni tabulkaModelHlavni1;
    protected TabulkaModelPruvodky tabulkaModelPruvodky1;
    protected ListSelectionModel lsmDetail1, lsmHlavni1, lsmPruvodky1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected WinUdalosti winUdalosti;
    protected TridaSkladTransakce tMatTrans1;
    protected TridaPolotovar1 tPol1;
    protected TridaPruvodka tPr1;
    protected ArrayList<TridaSkladTransakce> arPT1;
    protected ArrayList<TridaPolotovar1> arPol1;
    protected ArrayList<TridaPruvodka> arPr1;
    protected ArrayList vsSkladVyskladnitPolozka1;
    protected ArrayList vrSkladVyskladnitPolozka1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelMaterial, roletkaModelSkupinaMaterial;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;
    protected int indexOznaceno;

    /**
     * Creates new form ObjednavkyPanel
     */
    public SkladMaterialuPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletkaSkupina();
        initRoletkaMaterial();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuHlavni1();
        nastavTabulkuDetail1();
        nastavTabulkuPruvodky1();

        nastavPosluchaceUdalostiOvladace();
        this.setVisible(true);
    }

    protected void nastavParametry() {
        arPT1 = new ArrayList<TridaSkladTransakce>();
        vrSkladVyskladnitPolozka1 = new ArrayList();
        vsSkladVyskladnitPolozka1 = new ArrayList();
        tMatTrans1 = new TridaSkladTransakce();

        arPol1 = new ArrayList<TridaPolotovar1>();
        tPol1 = new TridaPolotovar1();
                
        arPr1 = new ArrayList<>();
        tPr1 = new TridaPruvodka();
                

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
        
        tabulkaModelPruvodky1 = new TabulkaModelPruvodky();

        tabulkaPruvodky.setModel(tabulkaModelPruvodky1);
        tabulkaPruvodky.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulkaPruvodky.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmPruvodky1 = tabulkaPruvodky.getSelectionModel();
        tableModelPruvodky1 = tabulkaPruvodky.getModel();

        tabulkaPruvodky.setPreferredScrollableViewportSize(new Dimension(800, 300));
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
        jCBMaterial.addActionListener(alUdalosti);
        jCBSkupina.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravit.addActionListener(alUdalosti);
        jBNovyMaterial.addActionListener(alUdalosti);
        jBNovyRozmer.addActionListener(alUdalosti);
        jBVycistiVyber.addActionListener(alUdalosti);
        jBNaskladnit.addActionListener(alUdalosti);
        jBVyskladnit.addActionListener(alUdalosti);

        jBVycistitFiltr.setActionCommand("VycistiFiltrObjednavka1");
        VyhledatButton1.setActionCommand("Hledat");
        jBVycistiVyber.setActionCommand("Vycisti");
        jBUpravit.setActionCommand("Upravit");
        jBNovyMaterial.setActionCommand("NovyMaterial");
        jBNovyRozmer.setActionCommand("NovyRozmer");
        jBNaskladnit.setActionCommand("Naskladnit");
        jBVyskladnit.setActionCommand("Vyskladnit");
        jCBSkupina.setActionCommand("VyberSkupina");
        jCBMaterial.setActionCommand("Hledat");
    }

    protected void nastavTabulkuDetail1() {
       /*  TableColumn column = null;
         column = tabulkaDetail.getColumnModel().getColumn(0);
         column.setPreferredWidth(60);

         column = tabulkaDetail.getColumnModel().getColumn(1);
         column.setPreferredWidth(120);

         column = tabulkaDetail.getColumnModel().getColumn(2);
         column.setPreferredWidth(100);

         column = tabulkaDetail.getColumnModel().getColumn(3);
         column.setPreferredWidth(50);

         column = tabulkaDetail.getColumnModel().getColumn(4);
         column.setPreferredWidth(80);

         column = tabulkaDetail.getColumnModel().getColumn(5);
         column.setPreferredWidth(120);*/

        //  zrusPosluchaceUdalostiTabulky();
        if (arPol1.size() > 0) {
            getDataTabulkaDetail1();
            tabulkaModelDetail1.pridejSadu();
        }
        // nastavPosluchaceUdalostiTabulky();

    }// konec nastavTabulkuBT1
            
    protected void nastavTabulkuPruvodky1() {
       
         TableColumn column = null;
        /* column = tabulkaPruvodky.getColumnModel().getColumn(0);
         column.setPreferredWidth(60);

         column = tabulkaPruvodky.getColumnModel().getColumn(1);
         column.setPreferredWidth(120);

         column = tabulkaPruvodky.getColumnModel().getColumn(2);
         column.setPreferredWidth(100);

         column = tabulkaPruvodky.getColumnModel().getColumn(3);
         column.setPreferredWidth(50);

         column = tabulkaPruvodky.getColumnModel().getColumn(4);
         column.setPreferredWidth(80);

         column = tabulkaPruvodky.getColumnModel().getColumn(5);
         column.setPreferredWidth(120);*/

        //  zrusPosluchaceUdalostiTabulky();
        if (arPol1.size() > 0) {
            getDataTabulkaPruvodky1();
            tabulkaModelPruvodky1.pridejSadu();
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
        column.setPreferredWidth(90);

        column = tabulkaHlavni.getColumnModel().getColumn(4);
        column.setPreferredWidth(90);

        column = tabulkaHlavni.getColumnModel().getColumn(5);
        column.setPreferredWidth(90);*/

        refreshDataHlavni();

    }// konec nastavTabulkuBT1

    protected void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaHlavni1();
        tabulkaModelHlavni1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
        if (arPol1.size() > 0) {
            tabulkaHlavni.setRowSelectionInterval(0, 0);
            nastavTabulkuDetail1();
            nastavTabulkuPruvodky1();
        }
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
        for (int i = 0; i < tabulkaHlavni.getColumnCount(); i++) {
            out.add(tabulkaHlavni.getColumnModel().getColumn(i).getPreferredWidth());
        }
        for (int i = 0; i < tabulkaDetail.getColumnCount(); i++) {
            out.add(tabulkaDetail.getColumnModel().getColumn(i).getPreferredWidth());
        }
        for (int i = 0; i < tabulkaPruvodky.getColumnCount(); i++) {
            out.add(tabulkaPruvodky.getColumnModel().getColumn(i).getPreferredWidth());
        }       
        return out;
    }

    protected void setSirkaSloupcu(ArrayList in) {     
                for (int i = 0; i < tabulkaHlavni.getColumnCount(); i++) {
            tabulkaHlavni.getColumnModel().getColumn(i).setPreferredWidth((Integer) in.get(i));
        }
        for (int i = tabulkaHlavni.getColumnCount(); i < tabulkaDetail.getColumnCount() + tabulkaHlavni.getColumnCount(); i++) {
//            System.out.println((i - tabulkaHlavni.getColumnCount()) + ". " + in.get(i));
            tabulkaDetail.getColumnModel().getColumn(i - tabulkaHlavni.getColumnCount()).setPreferredWidth((Integer) in.get(i));
        }
         for (int i = tabulkaDetail.getColumnCount() + tabulkaHlavni.getColumnCount(); i < tabulkaDetail.getColumnCount() + tabulkaHlavni.getColumnCount() + tabulkaPruvodky.getColumnCount(); i++) {
//            System.out.println((i - tabulkaHlavni.getColumnCount()) + ". " + in.get(i));
            tabulkaPruvodky.getColumnModel().getColumn(i - (tabulkaDetail.getColumnCount() + tabulkaHlavni.getColumnCount())).setPreferredWidth((Integer) in.get(i));
        }
    }

    protected void initRoletkaSkupina() {
        roletkaModelSkupinaMaterial = new RoletkaUniverzalRozsirenaModel1(
                "SELECT skupina_materialu_id, skupina_materialu_nazev "
                + "FROM spolecne.skupina_materialu "
                + "ORDER BY skupina_materialu_priorita ASC", "Všechny", null, "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBSkupina.setModel(roletkaModelSkupinaMaterial);
        roletkaModelSkupinaMaterial.setSelectedIndex(0);

    }

    private void initRoletkaMaterial() {

        String dotaz = "SELECT typ_materialu_id, "
                + "CASE "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_nazev || ' - ' || typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                + "ELSE null "
                + "END AS nazev_norma "
                + "FROM spolecne.typ_materialu ";
        if (((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() != 0) {
            dotaz += "WHERE typ_materialu_skupina = " + ((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() + " ";
        }
        dotaz += "ORDER BY typ_materialu_priorita ASC";
        roletkaModelMaterial = new RoletkaUniverzalRozsirenaModel1(
                dotaz, "Neurčen", null, "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBMaterial.setModel(roletkaModelMaterial);
    }

    protected void getDataTabulkaHlavni1() {
        if (tabulkaHlavni.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaHlavni.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arPol1.clear();
        vrSkladVyskladnitPolozka1.clear();
        tabulkaModelHlavni1.oznamZmenu();

        try {
            String dotazNaskl = "SELECT sklady_polotovary_transakce_polotovar_id, polotovary_nazev, "
                    + "COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0), "
                    + "btrim(to_char(polotovary_merna_hmotnost,'FM99990D00'),'.,'), "
                    + "COALESCE(polotovary_poznamky,' '), polotovary_typ_materialu, polotovary_aktivni, "
                    + "typ_materialu_id, "
                    + "CASE "
                    + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_nazev || ' - ' || typ_materialu_norma "
                    + "WHEN typ_materialu_nazev IS NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_norma "
                    + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                    + "ELSE null "
                    + "END AS nazev_norma, p.alokovano "
                    + "FROM logistika.sklady_polotovary_transakce "
                    + "CROSS JOIN logistika.sklady_polotovary_transakce_druhy "
                    + "CROSS JOIN spolecne.polotovary "
                    + "CROSS JOIN spolecne.typ_materialu "
                    + "CROSS JOIN spolecne.skupina_materialu "
                    + "LEFT JOIN (SELECT SUM(pruvodky_material_prumerna_delka * pruvodky_polotovar_pocet_kusu) AS alokovano, pruvodky_polotovar_id "
                    + "FROM spolecne.pruvodky "
                    + "WHERE pruvodky_odectena IS FALSE "
                    + "GROUP BY pruvodky_polotovar_id ) AS p "
                    + "ON p.pruvodky_polotovar_id = polotovary_id "
                    + "WHERE polotovary_id = sklady_polotovary_transakce_polotovar_id "
                    + "AND sklady_polotovary_transakce_druh_id = sklady_polotovary_transakce_druhy_id "
                    + "AND typ_materialu_id = polotovary_typ_materialu "
                    + "AND skupina_materialu_id = typ_materialu_skupina "
                    + "AND sklady_polotovary_transakce_druhy_je_prijem IS TRUE ";
            if (((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() > 0) {
                dotazNaskl += "AND typ_materialu_id = " + ((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() > 0) {
                dotazNaskl += "AND skupina_materialu_id = " + ((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() + " ";
            }
            if (jCBAktivni.isSelected()) {
                dotazNaskl += "AND polotovary_aktivni IS TRUE ";
            }
            if (!jTFRozmer.getText().trim().isEmpty()) {
                dotazNaskl += "AND polotovary_nazev ILIKE '%" + jTFRozmer.getText().trim() + "%' ";
            }
            dotazNaskl += "GROUP BY sklady_polotovary_transakce_polotovar_id, polotovary_nazev, polotovary_merna_hmotnost, "
                    + "polotovary_poznamky, polotovary_typ_materialu, polotovary_aktivni, typ_materialu.typ_materialu_id, nazev_norma, p.alokovano "
                    + "ORDER BY polotovary_nazev";
            System.out.println("dotaz Nask : " + dotazNaskl);
            ResultSet q = PripojeniDB.dotazS(dotazNaskl);
            while (q.next()) {
                tPol1 = new TridaPolotovar1();
                tPol1.setIdPolotovar(q.getInt(1));
                tPol1.setNazev((q.getString(2) == null) ? "" : q.getString(2));
                tPol1.setPocetKusu(q.getInt(3));
                tPol1.setMernaHmotnost((q.getString(4) == null) ? "" : q.getString(4));
                tPol1.setPoznamky((q.getString(5) == null) ? "" : q.getString(5));
                tPol1.setTypMaterialu(q.getInt(6));
                tPol1.setAktivni(q.getBoolean(7));
                TridaTypMaterial1 ttm1 = new TridaTypMaterial1();
                ttm1.setIdMaterial(q.getInt(8));
                ttm1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(9)));
                tPol1.settTM1(ttm1);
                tPol1.setPruvodkyKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(10)));
                arPol1.add(tPol1);
            }// konec while

            String dotazVyskl = "SELECT sklady_polotovary_transakce_polotovar_id, "
                    + "COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                    + "FROM logistika.sklady_polotovary_transakce "
                    + "CROSS JOIN logistika.sklady_polotovary_transakce_druhy "
                    + "CROSS JOIN spolecne.polotovary "
                    + "CROSS JOIN spolecne.typ_materialu "
                    + "WHERE polotovary_id = sklady_polotovary_transakce_polotovar_id "
                    + "AND sklady_polotovary_transakce_druh_id = sklady_polotovary_transakce_druhy_id "
                    + "AND typ_materialu_id = polotovary_typ_materialu "
                    //+ "AND typ_materialu_id = " + ((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() + " "
                    + "AND sklady_polotovary_transakce_druhy_je_prijem IS FALSE ";
            if (((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() > 0) {
                dotazNaskl += "AND typ_materialu_id = " + ((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() + " ";
            }
            if (((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() > 0) {
                dotazNaskl += "AND skupina_materialu_id = " + ((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() + " ";
            }
            if (jCBAktivni.isSelected()) {
                dotazVyskl += "AND polotovary_aktivni IS TRUE ";
            }
            if (!jTFRozmer.getText().trim().isEmpty()) {
                dotazNaskl += "AND polotovary_nazev ILIKE '%" + jTFRozmer.getText().trim() + "%' ";
            }

            dotazVyskl += "GROUP BY sklady_polotovary_transakce_polotovar_id, polotovary_nazev "
                    + "ORDER BY polotovary_nazev";
            ResultSet vyskladnit1 = PripojeniDB.dotazS(dotazVyskl);
            while (vyskladnit1.next()) {
                vsSkladVyskladnitPolozka1 = new ArrayList();
                vsSkladVyskladnitPolozka1.add(new Integer(vyskladnit1.getInt(1))); // polotovar_id
                vsSkladVyskladnitPolozka1.add(new Integer(vyskladnit1.getInt(2))); // pocet kusu               
                vrSkladVyskladnitPolozka1.add(vsSkladVyskladnitPolozka1);
            }// konec while
            int plus = 0;
            int minus = 0;
            for (int iPlus = 0; iPlus < arPol1.size(); iPlus++) {
                for (int iMinus = 0; iMinus < vrSkladVyskladnitPolozka1.size(); iMinus++) {
                    if (((Integer) ((ArrayList) vrSkladVyskladnitPolozka1.get(iMinus)).get(0)).intValue() == arPol1.get(iPlus).getIdPolotovar()) {
                        //System.out.println("prvek " + ((Integer) ((Vector) vrSkladVyskladnitPolozka1.get(iMinus)).get(0)));
                        plus = arPol1.get(iPlus).getPocetKusu();
                        minus = ((Integer) ((ArrayList) vrSkladVyskladnitPolozka1.get(iMinus)).get(1));
                        arPol1.get(iPlus).setPocetKusu(plus - minus);
                    }
                }
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        PocetZaznamuLabel1.setText(arPol1.size() + "");
        int index[][] = new int[arPol1.size()][tabulkaModelHlavni1.getColumnCount()];
        int index2[][] = new int[arPol1.size()][tabulkaModelHlavni1.getColumnCount()];

        for (int row = 0; row < arPol1.size(); row++) {
           // index2[row][2] = 1;
            index2[row][3] = 1;
        }

        Color backColor = new Color(216, 232, 249);
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
            String dotaz = "SELECT sklady_polotovary_transakce_id, "
                    + "polotovary_nazev, "
                    + "sklady_polotovary_transakce_druhy_nazev, "
                    + "sklady_polotovary_transakce_pocet_mj, "
                    + "to_char(sklady_polotovary_transakce_log_timestamp, 'DD.MM.YYYY'), "
                    + "COALESCE(sklady_polotovary_transakce_popis,'')"
                    + "FROM logistika.sklady_polotovary_transakce "
                    + "CROSS JOIN spolecne.polotovary "
                    + "CROSS JOIN logistika.sklady_polotovary_transakce_druhy "
                    + "WHERE sklady_polotovary_transakce_polotovar_id = " + arPol1.get(tabulkaHlavni.getSelectedRow()).getIdPolotovar() + " "
                    + "AND sklady_polotovary_transakce_druhy_id = sklady_polotovary_transakce_druh_id "
                    + "AND sklady_polotovary_transakce_polotovar_id = polotovary_id "
                    + "ORDER BY sklady_polotovary_transakce_log_timestamp DESC";

            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tMatTrans1 = new TridaSkladTransakce();
                tMatTrans1.setIdTransakce(new Long(q.getLong(1))); // transakce id
                tMatTrans1.setRozmer((q.getString(2) == null) ? "" : q.getString(2)); // nazev
                tMatTrans1.setPopisTransakce((q.getString(3) == null) ? "" : q.getString(3)); // transakce
                tMatTrans1.setPocetKusu(q.getBigDecimal(4)); // pocet kusu
                tMatTrans1.setDatum(df.parse(q.getString(5))); // timestamp
                tMatTrans1.setPopisPruvodkaVykres((q.getString(6) == null) ? "" : q.getString(6)); // popis
                arPT1.add(tMatTrans1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        int index[][] = new int[arPT1.size()][tabulkaModelDetail1.getColumnCount()];
        int index2[][] = new int[arPT1.size()][tabulkaModelDetail1.getColumnCount()];

       /* for (int row = 0; row < arPT1.size(); row++) {
            index2[row][1] = 1;
        }*/
        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelDetail1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaDetail.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }

    } //konec getDataTabulkaObjednavka1

    protected void getDataTabulkaPruvodky1() {

        // boolean datumDodani = false;
        // boolean datumExpedice = false;
        // boolean datumObjednani = false;
        if (tabulkaPruvodky.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulkaPruvodky.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arPr1.clear();

        tabulkaModelPruvodky1.oznamZmenu();

        try {
            String dotaz = "SELECT pruvodky_id, pruvodky_nazev, pruvodky_termin_dokonceni, pruvodky_polotovar_pocet_kusu, "
                    + "vykresy_cislo, vykresy_revize, "
                    + "(pruvodky_material_prumerna_delka * pruvodky_polotovar_pocet_kusu) AS alokovano, pruvodky_polotovar_id,"
                    + "pruvodky_cislo_vykresu "
                    + "FROM spolecne.pruvodky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE pruvodky_odectena IS FALSE "                    
                    + "AND pruvodky_polotovar_id = " + arPol1.get(tabulkaHlavni.getSelectedRow()).getIdPolotovar() + " "
                    + "AND pruvodky_cislo_vykresu = vykresy_id "                    
                    + "ORDER BY pruvodky_id DESC";                  
                    
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                                tPr1 = new TridaPruvodka();
                tPr1.setTv1(new TridaVykres1());
                tPr1.getTv1().setCislo(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                /* if (idOznaceno > -1 && SQLFunkceObecne.osetriCteniInt(q.getInt(2)) == idOznaceno) {
                 indexOznaceno = arPr1.size();
                 // System.out.println("indO1 " + indexOznaceno);
                 }*/
                tPr1.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tPr1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                tPr1.getTv1().setRevize(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                tPr1.setTerminDokonceni(q.getDate(3));            
                tPr1.setPocetKusuPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));          
               
                tPr1.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(9)));
                tPr1.getTv1().setIdVykres(tPr1.getIdVykres());               
                tPr1.setIdPolotovar(q.getInt(8));
                tPr1.setNarezanoZeSkladu(SQLFunkceObecne.osetriCteniInt(q.getInt(7)));                
                arPr1.add(tPr1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

       /* int index[][] = new int[arPT1.size()][tabulkaModelPruvodky1.getColumnCount()];
        int index2[][] = new int[arPT1.size()][tabulkaModelPruvodky1.getColumnCount()];

      
        Color backColor = new Color(255, 255, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelPruvodky1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulkaPruvodky.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }*/

    } //konec getDataTabulkaObjednavka1

    
    protected void vycistiFiltrObjednavka1() {
        jTFRozmer.setText("");
    }

    private void naskladnit() {

        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladMaterialuFrame1 naskladnit = new SkladMaterialuFrame1(200, arPol1.get(tabulkaHlavni.getSelectedRow()), false);

            naskladnit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 naskladnit = new SkladMaterialuFrame1(200, false);
            naskladnit.addWindowListener(winUdalosti);

        }
    }

    private void vyskladnit() {
        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladMaterialuFrame1 vyskladnit = new SkladMaterialuFrame1(100, arPol1.get(tabulkaHlavni.getSelectedRow()), false);
            vyskladnit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 vyskladnit = new SkladMaterialuFrame1(100, false);
            vyskladnit.addWindowListener(winUdalosti);
        }
    }

    private void upravit() {
        if (tabulkaHlavni.getSelectedRow() > -1) {
            indexOznaceno = tabulkaHlavni.getSelectedRow();
            SkladMaterialuFrame1 upravit = new SkladMaterialuFrame1(300, arPol1.get(tabulkaHlavni.getSelectedRow()), false);
            upravit.addWindowListener(winUdalosti);
        } else {
            SkladMaterialuFrame1 upravit = new SkladMaterialuFrame1(300, false);
            upravit.addWindowListener(winUdalosti);
        }
    }

    private void novyRozmer() {
        SkladMaterialuFrame1 novy = new SkladMaterialuFrame1(400,  ((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo(), 
                ((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo(), true);
        novy.addWindowListener(winUdalosti);
    }

    protected class TabulkaModelDetail extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo transakce",
            "Rozměr",
            "Druh transakce",
            "Délka / ks",
            "Datum",
            "Průvodka/výkres"
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
                    case (5): {
                        return (tMatTrans1.getPopisPruvodkaVykres());
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
            "Číslo",
            "Název",
            "Rozměr",
            "Množství [mm]",
            "V průvodkách [mm]",
            "Hmotnost 1m [kg]",
            "Poznámka"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arPol1.size());
            //  updateZaznamyObjednavka1();
            if (arPol1.size() > 0) {
                tabulkaHlavni.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arPol1.remove(tabulkaHlavni.getSelectedRow());
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
            return arPol1.size();
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
                tPol1 = arPol1.get(row);
                switch (col) {
                    case (0): {
                        return tPol1.getIdPolotovar();
                    }
                    case (1): {
                        return tPol1.gettTM1().getNazev();
                    }
                    case (2): {
                        return tPol1.getNazev();
                    }
                    case (3): {
                        return tPol1.getPocetKusu();
                    }
                    case (4): {
                        return tPol1.getPruvodkyKusu();
                    }
                    case (5): {
                        return tPol1.getMernaHmotnost();
                    }
                    case (6): {
                        return tPol1.getPoznamky();
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

    protected class TabulkaModelPruvodky extends AbstractTableModel {

        protected final String[] columnNames = {
            "Průvodka",
            "Výkres",
            "Název výrobku",
            "Datum dokončení",
            "Nařezat ks",
            "V průvodce[mm]"
        };

        public void pridejSadu() {
            // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arPr1.size());
            //  updateZaznamyObjednavka1();
            if (arPr1.size() > 0) {
                tabulkaPruvodky.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaPruvodka tObj) {
            arPr1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulkaPruvodky.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arPr1.remove(tabulkaPruvodky.getSelectedRow());
            fireTableRowsDeleted(tabulkaPruvodky.getSelectedRow(), tabulkaPruvodky.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (arTO1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        public void oznamUpdateRadkyVybrane() {
            fireTableRowsUpdated(tabulkaPruvodky.getSelectedRow(), tabulkaPruvodky.getSelectedRow());
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
            return String.class;

        }

        public boolean nastavHodnotuNaVybrane(TridaPruvodka bt) {
            // System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulkaDetail.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulkaPruvodky.getSelectedRow());
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
                tPr1 = arPr1.get(row);
                switch (col) {
                    case (0): {
                        return (tPr1.getId());
                    }
                    case (1): {
                        return (tPr1.getTv1().getCislo() + "/" + tPr1.getTv1().getRevize());

                    }
                    case (2): {
                        return (tPr1.getNazev());

                    }
                    case (3): {
                        if (tPr1.getTerminDokonceni() != null) {
                            return (df.format(tPr1.getTerminDokonceni()));
                        } else {
                            return "";
                        }

                    }
                    case (4): {
                        try {
                            return (tPr1.getPocetKusuPolotovar() + " ks");
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    case (5): {
                        return (tPr1.getNarezanoZeSkladu());
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

            if (e.getActionCommand().equals("VyberSkupina")) {
                initRoletkaMaterial();
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaHlavni1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }
            if (e.getActionCommand().equals("Upravit")) {
                upravit();
            }
            if (e.getActionCommand().equals("NovyMaterial")) {
                NovyMaterialFrame1 material = new NovyMaterialFrame1();
            }
            if (e.getActionCommand().equals("NovyRozmer")) {
                novyRozmer();
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
                    if (arPol1.size() > 0) {
                        nastavTabulkuPruvodky1();
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
        jCBAktivni = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTFRozmer = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBMaterial = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jCBSkupina = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jBVycistiVyber = new javax.swing.JButton();
        jBNaskladnit = new javax.swing.JButton();
        jBUpravit = new javax.swing.JButton();
        jBNovyMaterial = new javax.swing.JButton();
        jBVyskladnit = new javax.swing.JButton();
        jBNovyRozmer = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSPHlavni = new javax.swing.JScrollPane();
        tabulkaHlavni = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jSPDetail = new javax.swing.JScrollPane();
        tabulkaDetail = new javax.swing.JTable();
        jSPPruvodky = new javax.swing.JScrollPane();
        tabulkaPruvodky = new javax.swing.JTable();

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

        jLabel16.setText("Rozměr :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFRozmer, gridBagConstraints);

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

        jLabel20.setText("Materiál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBMaterial.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBMaterial, gridBagConstraints);

        jLabel1.setText("Skupina materiálu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 37);
        jPFiltry.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jCBSkupina, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBVycistiVyber.setText("Vyčistit výběr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBVycistiVyber, gridBagConstraints);

        jBNaskladnit.setText("Přidat do skladu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNaskladnit, gridBagConstraints);

        jBUpravit.setText("Upravit položku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravit, gridBagConstraints);

        jBNovyMaterial.setText("Nový materiál");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovyMaterial, gridBagConstraints);

        jBVyskladnit.setText("Vyskladnit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBVyskladnit, gridBagConstraints);

        jBNovyRozmer.setText("Nový rozměr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovyRozmer, gridBagConstraints);

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

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jSPDetail.setPreferredSize(new java.awt.Dimension(452, 700));

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        jPanel3.add(jSPDetail, gridBagConstraints);

        jSPPruvodky.setPreferredSize(new java.awt.Dimension(452, 200));

        tabulkaPruvodky.setModel(new javax.swing.table.DefaultTableModel(
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
        tabulkaPruvodky.setMinimumSize(new java.awt.Dimension(64, 64));
        jSPPruvodky.setViewportView(tabulkaPruvodky);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel3.add(jSPPruvodky, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 20);
        jPanel2.add(jPanel3, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JLabel PocetZaznamuLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBNaskladnit;
    private javax.swing.JButton jBNovyMaterial;
    private javax.swing.JButton jBNovyRozmer;
    private javax.swing.JButton jBUpravit;
    private javax.swing.JButton jBVycistiVyber;
    private javax.swing.JButton jBVycistitFiltr;
    private javax.swing.JButton jBVyskladnit;
    private javax.swing.JCheckBox jCBAktivni;
    private javax.swing.JComboBox jCBMaterial;
    private javax.swing.JComboBox jCBSkupina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jSPDetail;
    private javax.swing.JScrollPane jSPHlavni;
    private javax.swing.JScrollPane jSPPruvodky;
    private javax.swing.JTextField jTFRozmer;
    private javax.swing.JTable tabulkaDetail;
    private javax.swing.JTable tabulkaHlavni;
    private javax.swing.JTable tabulkaPruvodky;
    // End of variables declaration//GEN-END:variables
}
