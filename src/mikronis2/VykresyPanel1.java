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
import javax.swing.JFileChooser;
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
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class VykresyPanel1 extends javax.swing.JPanel {

    protected TableModel tableModelVykres1;
    protected TabulkaModelVykres1 tabulkaModelVykres;
    protected ListSelectionModel lsmObjednavka1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaVykres1 tV1;
    protected ArrayList<TridaVykres1> arTV1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public VykresyPanel1() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();

        nastavPosluchaceUdalostiOvladace();

        this.setVisible(true);


    }

    protected void nastavParametry() {
        arTV1 = new ArrayList<TridaVykres1>();
        tV1 = new TridaVykres1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelVykres = new TabulkaModelVykres1();

        tabulka.setModel(tabulkaModelVykres);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulka.getSelectionModel();
        tableModelVykres1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmObjednavka1.removeListSelectionListener(lslUdalosti);
        tableModelVykres1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelVykres1.addTableModelListener(tmlUdalosti);
        lsmObjednavka1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        jBVycistiFiltr.addActionListener(alUdalosti);
        jBNahratPDF.addActionListener(alUdalosti);
        jBZobrazitPDF.addActionListener(alUdalosti);
        jCBFiltrZdroj.addActionListener(alUdalosti);
        jBSmazatPDF.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBUpravitKapacita.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jBVycistiFiltr.setActionCommand("VycistiFiltrObjednavka1");
        jBNovy.addActionListener(alUdalosti);
        jBEdit.addActionListener(alUdalosti);
        jBSmazat.addActionListener(alUdalosti);
        jBNahratPDF.setActionCommand("NahratPDF");
        jBZobrazitPDF.setActionCommand("ZobrazitPDF");
        jBSmazatPDF.setActionCommand("SmazatPDF");
        VyhledatButton1.setActionCommand("Hledat");
        jBUpravitKapacita.setActionCommand("UpravitKapacitu");
        jCBZakaznik.setActionCommand("Refresh");
        jBNovy.setActionCommand("NovyVykres");
        jBEdit.setActionCommand("EditVykres");
        jBSmazat.setActionCommand("SmazatVykres");

    }

    protected void nastavTabulkuObjednavka1() {
        /*TableColumn column = null;
        column = tabulka.getColumnModel().getColumn(0);
        column.setPreferredWidth(141);

        column = tabulka.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(128);

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(115);

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(115);

        column = tabulka.getColumnModel().getColumn(5);
        column.setPreferredWidth(135);

        column = tabulka.getColumnModel().getColumn(6);
        column.setPreferredWidth(125);

        column = tabulka.getColumnModel().getColumn(7);
        column.setPreferredWidth(125);

        column = tabulka.getColumnModel().getColumn(8);
        column.setPreferredWidth(105);

        column = tabulka.getColumnModel().getColumn(9);
        column.setPreferredWidth(100);

        column = tabulka.getColumnModel().getColumn(10);
        column.setPreferredWidth(170);

        column = tabulka.getColumnModel().getColumn(11);
        column.setPreferredWidth(180);*/

        /*column = tabulka.getColumnModel().getColumn(12);
         column.setPreferredWidth(80);

         column = tabulka.getColumnModel().getColumn(13);
         column.setPreferredWidth(70);

         column = tabulka.getColumnModel().getColumn(14);
         column.setPreferredWidth(70);

         column = tabulka.getColumnModel().getColumn(17);
         column.setPreferredWidth(110);*/

        /*
         * column = tabulka.getColumnModel().getColumn(18);
         * column.setPreferredWidth(110);
         */

        refreshTabulka();

    }// konec nastavTabulkuBT1

    protected void refreshTabulka() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaObjednavka1();
        tabulkaModelVykres.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
    }

    protected void nastavTridyObsluhyUdalosti() {
        tmlUdalosti = new TMLUdalosti();
        lslUdalosti = new LSLUdalosti();
        alUdalosti = new ALUdalosti();
        flUdalosti = new FLUdalosti();
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
    }

    protected void getDataTabulkaObjednavka1() {


        boolean dotazOk = false;
        boolean datumDodani = false;
        boolean datumExpedice = false;
        boolean datumObjednani = false;
        boolean filtr = false;

        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTV1.clear();

        tabulkaModelVykres.oznamZmenu();

        try {
            pocetKusuObjednavky = 0;
            String dotaz =
                    "SELECT vykresy_id, vykresy_cislo, "
                    + "vykresy_revize, vykresy_nazev, "
                    + "vykresy_poznamky, subjekty_trhu_nazev, "
                    + "COALESCE(vyrobni_kapacita_cas_mcv,0) AS mcv, "
                    + "CAST(CASE WHEN vyrobni_kapacita_mcv_kusy > 0 THEN vyrobni_kapacita_cas_mcv_vyroba/vyrobni_kapacita_mcv_kusy/3600000 ELSE 0 END AS numeric(12,2)), "
                    + "COALESCE (vyrobni_kapacita_cas_tornado,0) AS tornado, "
                    + "CAST(CASE WHEN vyrobni_kapacita_tornado_kusy > 0 THEN vyrobni_kapacita_cas_tornado_vyroba/vyrobni_kapacita_tornado_kusy/3600000 ELSE 0 END AS numeric(12,2)), "
                    + "COALESCE(vyrobni_kapacita_cas_dmu,0) AS dmu, "
                    + "CAST(CASE WHEN vyrobni_kapacita_dmu_kusy > 0 THEN vyrobni_kapacita_cas_dmu_vyroba/vyrobni_kapacita_dmu_kusy/3600000 ELSE 0 END AS numeric(12,2)), "
                    + "COALESCE(vykresy_bindata_nazev,'') "
                    + "FROM spolecne.vykresy s "
                    + "LEFT JOIN (SELECT vyrobni_kapacita_cislo_vykresu, "
                    + "vyrobni_kapacita_cas_mcv, "
                    + "vyrobni_kapacita_cas_mcv_vyroba, vyrobni_kapacita_mcv_kusy, "
                    + "vyrobni_kapacita_cas_tornado, "
                    + "vyrobni_kapacita_cas_tornado_vyroba, vyrobni_kapacita_tornado_kusy, "
                    + "vyrobni_kapacita_cas_dmu, "
                    + "vyrobni_kapacita_cas_dmu_vyroba, vyrobni_kapacita_dmu_kusy, "
                    + "vyrobni_kapacita_soustruh "
                    + "FROM spolecne.vyrobni_kapacita) z "
                    + "ON s.vykresy_cislo = z.vyrobni_kapacita_cislo_vykresu "
                    + "LEFT JOIN(SELECT vykresy_bindata_id, vykresy_bindata_nazev "
                    + "FROM spolecne.vykresy_bindata) bd "
                    + "ON s.vykresy_id = bd.vykresy_bindata_id "
                    + "CROSS JOIN spolecne.subjekty_trhu "
                    + "WHERE vykresy_zakaznik_id = subjekty_trhu_id ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND vykresy_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                filtr = true;
            }
            if (jTFNazev.getText().length() > 0) {
                dotaz += "AND vykresy_nazev ILIKE '%" + jTFNazev.getText().trim() + "%' ";
                filtr = true;
            }
            if (jTFCisloVykresu.getText().length() > 0) {
                dotaz += "AND vykresy_cislo ILIKE '%" + jTFCisloVykresu.getText().trim() + "%' ";
                filtr = true;
            }
            dotaz += "ORDER BY vykresy_cislo, vykresy_revize ";
            //System.out.println(dotaz);
            ResultSet q = PripojeniDB.dotazS(dotaz);
            while (q.next()) {
                tV1 = new TridaVykres1();
                tV1.setIdVykres(q.getInt(1));
                tV1.setCislo((q.getString(2) == null) ? "" : q.getString(2));
                tV1.setRevize((q.getString(3) == null) ? "" : q.getString(3));
                tV1.setNazev((q.getString(4) == null) ? "" : q.getString(4));
                tV1.setPoznamky((q.getString(5) == null) ? "" : q.getString(5));
                tV1.setNazevZakaznik((q.getString(6) == null) ? "" : q.getString(6));
                tV1.setMcv((q.getString(7) == null) ? "" : q.getString(7));
                tV1.setMcvOdhad((q.getString(8) == null) ? "" : q.getString(8));
                tV1.setTornado((q.getString(9) == null) ? "" : q.getString(9));
                tV1.setTornadoOdhad((q.getString(10) == null) ? "" : q.getString(10));
                tV1.setDmu((q.getString(11) == null) ? "" : q.getString(11));
                tV1.setDmuOdhad((q.getString(12) == null) ? "" : q.getString(12));
                tV1.setNazevSoubor((q.getString(13) == null) ? "" : q.getString(13));

                arTV1.add(tV1);

            }// konec while
            dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        jLPocetPolozek.setText(arTV1.size() + "");

        //jTextFieldPocetKusuObjednavka1.setText(pocetKusuObjednavky + "");
        obarvitTabulku();

    } //konec getDataTabulkaObjednavka1

    private void obarvitTabulku() {

        int index[][] = new int[arTV1.size()][tabulkaModelVykres.getColumnCount()];
        int index2[][] = new int[arTV1.size()][tabulkaModelVykres.getColumnCount()];

        for (int row = 0; row < arTV1.size(); row++) {

            index[row][4] = 1;
            index[row][6] = 1;
            index[row][8] = 1;
            index2[row][11] = 2;
            index2[row][10] = 2;

        }
        Color backColor = new Color(188, 247, 188);
        Color frontColor = Color.BLACK;
        for (int i = 0; i < tabulkaModelVykres.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulka.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRenderer1(index, index2, backColor, frontColor));
        }
    }

    protected void vycistiFiltrObjednavka1() {
        jTFNazev.setText("");
        jTFCisloVykresu.setText("");
    }

    private void novyVykres() {
        NovyVykresFrame novyVykres = new NovyVykresFrame(false);
    }

    private void upravitVykres() {
        NovyVykresFrame upravaVykresu = new NovyVykresFrame(arTV1.get(tabulka.getSelectedRow()).getIdVykres(), true);
    }

    private void smazatVykres() {

        NovyVykresFrame smazatVykres = new NovyVykresFrame(arTV1.get(tabulka.getSelectedRow()).getIdVykres(), true, true);
    }

    private void upravitKapacitu() {
        UpravaKapacityFrame1 kapacita = new UpravaKapacityFrame1(arTV1.get(tabulka.getSelectedRow()).getNazev());
    }

    private void nahratVykres() {
        String filename = File.separator + "tmp";
        JFileChooser fc = new JFileChooser(new File(filename));
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        File soubor = fc.getSelectedFile();
        System.out.println("Soubor " + soubor.getName());


        try {
            FileInputStream fis = new FileInputStream(soubor);
            int r = PripojeniDB.dotazIUD(
                    "INSERT INTO spolecne.vykresy_bindata(vykresy_bindata_id, vykresy_bindata_nazev) "
                    + "VALUES(" + arTV1.get(tabulka.getSelectedRow()).getIdVykres() + ", "
                    + TextFunkce1.osetriZapisTextDB1(soubor.getName()) + ")");

            PreparedStatement ps = PripojeniDB.con.prepareStatement(
                    "UPDATE spolecne.vykresy_bindata SET vykresy_bindata_data = ? WHERE vykresy_bindata_id = ? ");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setInt(2, arTV1.get(tabulka.getSelectedRow()).getIdVykres());
            ps.executeUpdate();
            ps.close();
            fis.close();

            refreshTabulka();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void smazatSoubor() {
        if (tabulka.getSelectedRow() > -1) {
            int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání souboru",
                    "Opravdu chcete smazat soubor " + arTV1.get(tabulka.getSelectedRow()).getNazevSoubor() + " ?", 1);
            if (ud == JOptionPane.YES_OPTION) {
                try {
                    int rc = PripojeniDB.dotazIUD("DELETE FROM spolecne.vykresy_bindata WHERE vykresy_bindata_id = "
                            + arTV1.get(tabulka.getSelectedRow()).getIdVykres());
                    refreshTabulka();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void zobrazitVykres() {
        String nazevVykresu = "";
        try {
            ResultSet vykres = PripojeniDB.dotazS("SELECT vykresy_bindata_nazev, vykresy_bindata_data "
                    + "FROM spolecne.vykresy_bindata "
                    + "WHERE vykresy_bindata_id = " + arTV1.get(tabulka.getSelectedRow()).getIdVykres());
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
                /*if (nazevVykresu.toLowerCase().contains(".pdf")) {
                    
                 Runtime.getRuntime().exec(HlavniRamec.pdfReaderPath + " " + HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevVykresu);
                 } else if ((nazevVykresu.toLowerCase().contains(".dwg")) || (nazevVykresu.toLowerCase().contains(".dxf"))) {
                 System.out.println("Autocad " + HlavniRamec.autoCadPath + " " + HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevVykresu);
                 Runtime.getRuntime().exec(HlavniRamec.autoCadPath + " \"" + HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevVykresu + "\"");
                 } else {
                 Runtime.getRuntime().exec(HlavniRamec.pictureViewerPath + " " + HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + nazevVykresu);
                 }*/
            } // konec if
        }//konec try
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected class TabulkaModelVykres1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Číslo výkresu",
            "Změna",
            "Název",
            "MCV - kapacita(h/ks)",
            "MCV - stroj(h/ks)",
            "Tornado - kapacita(h/ks)",
            "Tornado - stroj(h/ks)",
            "DMU - kapacita(h/ks)",
            "DMU - stroj(h/ks)",
            "Poznámky",
            "Zákazník",
            "Název výkresu"
        };

        public void pridejSadu() {
          //  System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTV1.size());
            //  updateZaznamyObjednavka1();
            if (arTV1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaVykres1 tv) {
            arTV1.add(tv);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulka.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arTV1.remove(tabulka.getSelectedRow());
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
            return arTV1.size();
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
            //  System.out.println("col" + col);
            if (col == 17) {
                // System.out.println("boolean");
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaVykres1 bt) {
            System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulka.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulka.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaVykres1 nastavPruv, int pozice) {
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
                tV1 = arTV1.get(row);
                switch (col) {
                    case (0): {
                        return (tV1.getCislo());
                    }
                    case (1): {
                        return (tV1.getRevize());
                    }
                    case (2): {
                        return (tV1.getNazev());
                    }
                    case (3): {
                        return (tV1.getMcv());
                    }
                    case (4): {
                        return (tV1.getMcvOdhad());
                    }
                    case (5): {
                        return (tV1.getTornado());
                    }
                    case (6): {
                        return (tV1.getTornadoOdhad());
                    }
                    case (7): {
                        return (tV1.getDmu());
                    }
                    case (8): {
                        return (tV1.getDmuOdhad());
                    }
                    case (9): {
                        return ((tV1.getPoznamky()));
                    }
                    case (10): {
                        return ((tV1.getNazevZakaznik()));
                    }
                    case (11): {
                        return (tV1.getNazevSoubor());
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
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("Hledat") || e.getActionCommand().equals("FiltrVykresy") || e.getActionCommand().equals("VycistiFiltrSklad1")) {
                if (e.getActionCommand().equals("VycistiFiltrSklad1")) {
                    vycistiFiltrObjednavka1();
                }
                refreshTabulka();
            }

            if (e.getActionCommand().equals("NovyVykres")) {
                novyVykres();
               
            }
            if (e.getActionCommand().equals("UpravitKapacitu")) {
                upravitKapacitu();
                //        zmenVyberObjednavka1(0);
            }
            if (e.getActionCommand().equals("EditVykres")) {
                upravitVykres();
                //    zmenVyberObjednavka1(-1);
            }
            if (e.getActionCommand().equals("SmazatVykres")) {
                smazatVykres();
                //    zmenVyberObjednavka1(-1);
            }

            if (e.getActionCommand().equals("ZobrazitPDF")) {
                zobrazitVykres();
            }

            if (e.getActionCommand().equals("NahratPDF")) {
                nahratVykres();
            }

            if (e.getActionCommand().equals("SmazatPDF")) {
                smazatSoubor();
            }

        }
    } //konec ALUdalosti

    class TMLUdalosti implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent tme) {
            

            if (tme.getSource() == tableModelVykres1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmObjednavka1) {
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
        jBVycistiFiltr = new javax.swing.JButton();
        jCBFiltrZdroj = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTFCisloVykresu = new javax.swing.JTextField();
        jTFNazev = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jBNovy = new javax.swing.JButton();
        jBSmazat = new javax.swing.JButton();
        jBEdit = new javax.swing.JButton();
        jBZobrazitPDF = new javax.swing.JButton();
        jBSmazatPDF = new javax.swing.JButton();
        jBUpravitKapacita = new javax.swing.JButton();
        jBNahratPDF = new javax.swing.JButton();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        jBVycistiFiltr.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(jBVycistiFiltr, gridBagConstraints);

        jCBFiltrZdroj.setText("Filtr výkresy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jCBFiltrZdroj, gridBagConstraints);

        jLabel1.setText("Počet položek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JPFiltrTop.add(jLabel1, gridBagConstraints);

        jLPocetPolozek.setText("0");
        JPFiltrTop.add(jLPocetPolozek, new java.awt.GridBagConstraints());

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo výkresu : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel16.setText("Název výkresu (součásti) : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFCisloVykresu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTFNazev, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
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

        jBNovy.setText("Nový výkres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovy, gridBagConstraints);

        jBSmazat.setText("Smazat výkres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazat, gridBagConstraints);

        jBEdit.setText("Upravit výkres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBEdit, gridBagConstraints);

        jBZobrazitPDF.setText("Zobrazit soubor PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBZobrazitPDF, gridBagConstraints);

        jBSmazatPDF.setText("Smazat soubor PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazatPDF, gridBagConstraints);

        jBUpravitKapacita.setText("Upravit kapacitu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBUpravitKapacita, gridBagConstraints);

        jBNahratPDF.setText("Nahrát soubor PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNahratPDF, gridBagConstraints);

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
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBEdit;
    private javax.swing.JButton jBNahratPDF;
    private javax.swing.JButton jBNovy;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBSmazatPDF;
    private javax.swing.JButton jBUpravitKapacita;
    private javax.swing.JButton jBVycistiFiltr;
    private javax.swing.JButton jBZobrazitPDF;
    private javax.swing.JCheckBox jCBFiltrZdroj;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JTextField jTFCisloVykresu;
    private javax.swing.JTextField jTFNazev;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
