/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRenderer;
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
import javax.swing.table.TableModel;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class Termin2Panel extends javax.swing.JPanel {

    protected TableModel tableModelObjednavka1;
    protected TabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected ListSelectionModel lsmObjednavka1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaObjednavka1 tObj1;
    protected ArrayList<TridaObjednavka1> arTO1;
    protected RoletkaUniverzalModel1 roletkaModelZakaznici;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public Termin2Panel() {
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
        arTO1 = new ArrayList<TridaObjednavka1>();
        tObj1 = new TridaObjednavka1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelObjednavka1 = new TabulkaModelObjednavka1();

        tabulka.setModel(tabulkaModelObjednavka1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulka.getSelectionModel();
        tableModelObjednavka1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmObjednavka1.removeListSelectionListener(lslUdalosti);
        tableModelObjednavka1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelObjednavka1.addTableModelListener(tmlUdalosti);
        lsmObjednavka1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {
        VyhledatButton1.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);

        jBFaktura.addActionListener(alUdalosti);
        jBSmazatPolozky.addActionListener(alUdalosti);
        jBLieferschein.addActionListener(alUdalosti);

        VyhledatButton1.setActionCommand("Hledat");
        jCBZakaznik.setActionCommand("Refresh");

        jBFaktura.setActionCommand("VytvoritFakturu");
        jBSmazatPolozky.setActionCommand("OdebratZTerminu");
        jBLieferschein.setActionCommand("VytvoritLieferschein");

    }

    protected void nastavTabulkuObjednavka1() {
      /*  TableColumn column = null;
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

        column = tabulka.getColumnModel().getColumn(7);
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(8);
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(9);
        column.setPreferredWidth(100);

        column = tabulka.getColumnModel().getColumn(10);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(11);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(12);
        column.setPreferredWidth(80);

        column = tabulka.getColumnModel().getColumn(13);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(14);
        column.setPreferredWidth(110);*/

        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaObjednavka1();
        tabulkaModelObjednavka1.pridejSadu();
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
        roletkaModelZakaznici = new RoletkaUniverzalModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_popis FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_popis", "V databázi nejsou zadáni zákazníci"); // bylo ...vs_id
        jCBZakaznik.setModel(roletkaModelZakaznici);

    }

    protected void getDataTabulkaObjednavka1() {

        boolean dotazOk = false;
        boolean datumDodani = false;
        boolean datumExpedice = false;
        boolean datumObjednani = false;


        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();

        tabulkaModelObjednavka1.oznamZmenu();

        try {
            pocetKusuObjednavky = 0;
            String dotaz =
                    "SELECT objednavky_id, "
                    + "objednavky_datum_objednani, objednavky_nazev_soucasti, "
                    + "vykresy_id, "
                    + "vykresy_cislo, "
                    + "vykresy_revize, "
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_kusu_navic, "
                    + "objednavky_termin_dodani, "
                    + "objednavky_datum_expedice, "
                    + "objednavky_material_rozmer, "
                    + "druhy_povrchova_uprava_nazev, "
                    + "objednavky_cislo_faktury, "
                    + "objednavky_cena_za_kus, "
                    + "meny_zkratka, "
                    + "objednavky_poznamka, objednavky_cislo_vykresu, objednavky_reklamace, terminy_id  "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.druhy_povrchova_uprava "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.terminy "
                    + "WHERE objednavky.objednavky_id IS NOT NULL "
                    + "AND druhy_povrchova_uprava.druhy_povrchova_uprava_id = objednavky.objednavky_povrchova_uprava_id "
                    + "AND meny.meny_id = objednavky.objednavky_mena_id "
                    + "AND terminy.terminy_cislo_terminu = 2 "
                    + "AND objednavky.objednavky_id = terminy.terminy_objednavky_id "
                    + "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " "
                    + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu ";

            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "ORDER BY vykresy_cislo, objednavky_cislo_objednavky ASC ";
            } else {
                dotaz += "ORDER BY objednavky_termin_dodani,vykresy_cislo ASC ";
            }

            //System.out.println(dotaz);
            ResultSet objednavka1 = PripojeniDB.dotazS(dotaz);
            while (objednavka1.next()) {
                tObj1 = new TridaObjednavka1();
                tObj1.setId(new Long(objednavka1.getLong(1)));
                tObj1.setDatumObjednani(objednavka1.getDate(2));
                tObj1.setNazevSoucasti((objednavka1.getString(3) == null) ? "" : objednavka1.getString(3));
                tObj1.setIdVykres(objednavka1.getInt(4));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(objednavka1.getInt(4));
                tv1.setCislo((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                tv1.setRevize((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                tObj1.setTv1(tv1);
                tObj1.setCisloObjednavky((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                tObj1.setPocetObjednanychKusu(objednavka1.getInt(8));
                pocetKusuObjednavky += objednavka1.getInt(8);

                try {
                    tObj1.setKusuNavic(new Integer(objednavka1.getInt(9))); // kusy navic
                } catch (Exception e) {
                    tObj1.setKusuNavic(0); // kusy navic
                }
                tObj1.setDatumDodani(objednavka1.getDate(10)); // datum dodani
                try {
                    tObj1.setDatumExpedice(objednavka1.getDate(11)); // datum expedice
                } catch (Exception e) {
                    tObj1.setDatumExpedice(objednavka1.getDate(11));
                }
                tObj1.setMaterialRozmer((objednavka1.getString(12) == null) ? "" : objednavka1.getString(12));

                String povrchUprava = ((objednavka1.getString(13) == null) ? "" : objednavka1.getString(13));
                if (povrchUprava.equals("žádná")) {
                    tObj1.setPopisPovrchUprava(new String("---"));
                } else {
                    tObj1.setPopisPovrchUprava(povrchUprava);
                }// povrchova uprava               

                try {
                    tObj1.setCisloFaktury((objednavka1.getString(14) == null) ? "" : objednavka1.getString(14));
                } catch (Exception e) {
                    tObj1.setCisloFaktury(null);
                }
                tObj1.setCenaKus(nf2.format(nf2.parse(objednavka1.getString(15).replace(".", ",")))); // cena
                tObj1.setPopisMena((objednavka1.getString(16) == null) ? "" : objednavka1.getString(16));

                try {
                    tObj1.setPoznamka((objednavka1.getString(17) == null) ? "" : objednavka1.getString(17));
                } catch (Exception e) {
                    tObj1.setPoznamka(null); // poznamka
                }

                tObj1.setReklamace(objednavka1.getBoolean(19));
                arTO1.add(tObj1);

            }// konec while
            dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        jLPocetPolozek.setText(arTO1.size() + "");
        
        try {
            String dotaz = "SELECT "
                    + "SUM (objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.terminy "
                    + "WHERE objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo()
                    + " AND terminy.terminy_objednavky_id = objednavky.objednavky_id"
                    + " AND terminy.terminy_cislo_terminu = 2 ";
            ResultSet celkovacena1 = PripojeniDB.dotazS(dotaz);
            while (celkovacena1.next()) {
                SumaZakazekLabel1.setText(celkovacena1.getString(1));

            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        //jTextFieldPocetKusuObjednavka1.setText(pocetKusuObjednavky + "");
        obarvitTabulku();

    } //konec getDataTabulkaObjednavka1

    private void obarvitTabulku() {

        int index[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index2[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index3[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index4[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];

        for (int row = 0; row < arTO1.size(); row++) {
            index2[row][6] = 1;
            index3[row][12] = 1;
            index4[row][0] = 3;
            index4[row][1] = 3;
            index4[row][2] = 3;
            index4[row][3] = 3;
            index4[row][4] = 3;
            index4[row][7] = 3;
            index4[row][8] = 3;
            index4[row][9] = 3;
            index4[row][10] = 3;

            index4[row][5] = 1;
            index4[row][6] = 1;
            index4[row][12] = 1;
            index4[row][13] = 2;
            index4[row][14] = 1;
        }
        Color backColor = new Color(255, 204, 204);
        Color frontColor = Color.BLACK;
        tabulka.setDefaultRenderer(Object.class, new ColorCellRenderer(index, index2, index3, index4, backColor, frontColor));

    }

    private void odebratZTerminu() {
        int indexy[] = tabulka.getSelectedRows();
        long indexObjednavky;
        for (int i = 0; i
                < indexy.length; i++) {         
            indexObjednavky = arTO1.get(indexy[i]).getId();
            String dotaz1 = "UPDATE spolecne.terminy "
                    + "SET terminy_cislo_terminu= 1 "                   
                    + "WHERE terminy_objednavky_id = " + indexObjednavky;
            try {
                System.out.println("dotaz1 : " + dotaz1);
                int a = PripojeniDB.dotazIUD(dotaz1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaObjednavka1();
        tabulkaModelObjednavka1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
    }

    private void vytvorLieferschein() {
        int indexy[] = tabulka.getSelectedRows();
        long indexyOjb[] = new long[indexy.length];        
        for (int i = 0; i
                < indexy.length; i++) {
            indexyOjb[i] = arTO1.get(indexy[i]).getId();
        }
        LieferscheinFrame lf = new LieferscheinFrame(indexyOjb, ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }
    
    private void vytvorFakturu() {
        int indexy[] = tabulka.getSelectedRows();
        long indexyOjb[] = new long[indexy.length];        
        for (int i = 0; i
                < indexy.length; i++) {
            indexyOjb[i] = arTO1.get(indexy[i]).getId();
        }
        FakturaFrame1 lf = new FakturaFrame1(indexyOjb, ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }
    

    protected class TabulkaModelObjednavka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",
            "Ks",
            "Ks navíc",
            "Termín dodání",
            "Expedice",
            "Rozměr",
            "Povrch úprava",
            "Faktury",
            "Cena/ks",
            "Měna",
            "Poznamka"
        };

        public void pridejSadu() {
           // System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTO1.size());
            //  updateZaznamyObjednavka1();
            if (arTO1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void pridejJeden(TridaObjednavka1 tObj) {
            arTO1.add(tObj);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            //updateZaznamy();
            tabulka.changeSelection(getRowCount() - 1, 0, false, false);
        }//konec pridejJeden

        public void uberJednu() {
            arTO1.remove(tabulka.getSelectedRow());
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
            //  System.out.println("col" + col);
            if (col == 17) {
                // System.out.println("boolean");
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaObjednavka1 bt) {
            System.out.println("nastavHodnotuNaVybraneObjednavka1 " + tabulka.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulka.getSelectedRow());
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
                        if (tObj1.getDatumObjednani() != null) {
                            return (df.format(tObj1.getDatumObjednani()));
                        }
                    }
                    case (1): {
                        return (tObj1.getNazevSoucasti());
                    }
                    case (2): {
                        return (tObj1.getTv1().getCislo());
                    }
                    case (3): {
                        return (tObj1.getTv1().getRevize());
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
                        if (tObj1.getDatumDodani() != null) {
                            return (df.format(tObj1.getDatumDodani()));
                        } else {
                            return "";
                        }
                    }
                    case (8): {
                        if (tObj1.getDatumExpedice() != null) {
                            return (df.format(tObj1.getDatumExpedice()));
                        } else {
                            return "";
                        }
                    }
                    case (9): {
                        return (tObj1.getMaterialRozmer());
                    }
                    case (10): {
                        return (tObj1.getPopisPovrchUprava());
                    }
                    case (11): {
                        return (tObj1.getCisloFaktury());
                    }
                    case (12): {
                        return (tObj1.getCenaKus());
                    }
                    case (13): {
                        return (tObj1.getPopisMena());
                    }
                    case (14): {
                        return (tObj1.getPoznamka());
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

                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaObjednavka1();
                nastavPosluchaceUdalostiTabulky();
            }

            if (e.getActionCommand().equals("Hledat")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaObjednavka1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }
            if (e.getActionCommand().equals("VytvoritLieferschein")) {
                vytvorLieferschein();
            }
            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }

            if (e.getActionCommand().equals("OdebratZTerminu")) {
                odebratZTerminu();
            }
            if (e.getActionCommand().equals("VytvoritFakturu")) {
                vytvorFakturu();
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
        jPFiltry = new javax.swing.JPanel();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        SumaZakazekLabel1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jBLieferschein = new javax.swing.JButton();
        jBFaktura = new javax.swing.JButton();
        jBSmazatPolozky = new javax.swing.JButton();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jLabel20, gridBagConstraints);

        jCBZakaznik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBZakaznik.setMaximumRowCount(14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jCBZakaznik, gridBagConstraints);

        jLabel4.setText("Hodnota zakázek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jLabel4, gridBagConstraints);

        SumaZakazekLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(SumaZakazekLabel1, gridBagConstraints);

        jLabel1.setText("Zobrazených objednávek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jLabel1, gridBagConstraints);

        jLPocetPolozek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jLPocetPolozek, gridBagConstraints);

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

        jBLieferschein.setText("Vytvořit dodací list");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBLieferschein, gridBagConstraints);

        jBFaktura.setText("Vytvořit fakturu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBFaktura, gridBagConstraints);

        jBSmazatPolozky.setText("Odebrat položky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazatPolozky, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSPTabulka)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                .addGap(474, 474, 474))
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSPTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JLabel SumaZakazekLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBFaktura;
    private javax.swing.JButton jBLieferschein;
    private javax.swing.JButton jBSmazatPolozky;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
