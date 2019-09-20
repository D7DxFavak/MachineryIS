/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRendererNab1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.ArrayList;
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

/**
 *
 * @author Favak
 */
public class NabidkyPanel extends javax.swing.JPanel {

    protected TableModel tableModelNabidka1;
    protected tabulkaModelNabidka1 tabulkaModelNabidka1;
    protected ListSelectionModel lsmNabidka1;
    protected TableModelListener tmlUdalosti;
    protected ListSelectionListener lslUdalosti;
    protected MouseListener mlUdalosti;
    protected ActionListener alUdalosti;
    protected FocusListener flUdalosti;
    protected TridaNabidka1 tNab1;
    protected ArrayList<TridaNabidka1> arNab1;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuNabidky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public NabidkyPanel() {
        initComponents();

        this.setSize(MikronIS2.gc.getBounds().getSize());
        this.setVisible(false);

        nastavParametry();

        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavParametryTabulek();
        nastavTabulkuNabidka1();

        nastavPosluchaceUdalostiOvladace();

        this.setVisible(true);


    }

    protected void nastavParametry() {
        arNab1 = new ArrayList<TridaNabidka1>();
        tNab1 = new TridaNabidka1();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

    }

    protected void nastavParametryTabulek() {
        tabulkaModelNabidka1 = new tabulkaModelNabidka1();

        tabulka.setModel(tabulkaModelNabidka1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmNabidka1 = tabulka.getSelectionModel();
        tableModelNabidka1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));
    }

    protected void zrusPosluchaceUdalostiTabulky() {
        lsmNabidka1.removeListSelectionListener(lslUdalosti);
        tableModelNabidka1.removeTableModelListener(tmlUdalosti);
    }

    protected void nastavPosluchaceUdalostiTabulky() {
        tableModelNabidka1.addTableModelListener(tmlUdalosti);
        lsmNabidka1.addListSelectionListener(lslUdalosti);
    }

    protected void nastavPosluchaceUdalostiOvladace() {

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jBVykres.addActionListener(alUdalosti);
        jCheckBoxFiltrZdrojNabidka1.addActionListener(alUdalosti);
        jBSmazatNabidka.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jBSeznamNabidek.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBNovaNabidka.addActionListener(alUdalosti);
        jBEditNabidka.addActionListener(alUdalosti);
        jBNovaNabidkaStavajici.addActionListener(alUdalosti);
        jBVykres.setActionCommand("ZobrazVykres");
        jBSmazatNabidka.setActionCommand("SmazatNabidka");
        VyhledatButton1.setActionCommand("Hledat");
        jBSeznamNabidek.setActionCommand("SeznamNabidek");
        jCBZakaznik.setActionCommand("Refresh");
        jCBRokDodani.setActionCommand("Hledat");
        jBNovaNabidka.setActionCommand("NovaNabidka");
        jBEditNabidka.setActionCommand("EditNabidka");
        jBNovaNabidkaStavajici.setActionCommand("NovaNabidkaStavajici");

    }

    protected void nastavTabulkuNabidka1() {
       /* TableColumn column = null;
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
        column.setPreferredWidth(80);*/

        refreshDataHlavni();

    }// konec nastavTabulkuBT1

    protected void refreshDataHlavni() {
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaNabidka();
        tabulkaModelNabidka1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();

        if (arNab1.size() > 0) {
            tabulka.setRowSelectionInterval(0, 0);
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
                "SELECT DISTINCT cast(date_part('year', nabidky_datum) AS integer), date_part('year', nabidky_datum) AS roky FROM spolecne.nabidky ORDER BY roky", "Neurčen", null,
                "V databázi nejsou žádné nabidky", 0); // bylo ...vs_id

        Vector vR = SQLFunkceObecne2.getVDvojicCisloRetez("SELECT * FROM (SELECT DISTINCT cast(date_part('year', pruvodky_termin_dokonceni) AS integer) as intpart, "
                + "date_part('year', pruvodky_termin_dokonceni) AS roky FROM spolecne.pruvodky ) AS x "
                + "WHERE x.intpart = cast(date_part('year', current_date) AS integer)");
        if (vR.size() == 1) {
            roletkaModelRoky.setSelectedItem(vR.get(0));
        }

        jCBRokDodani.setModel(roletkaModelRoky);
    }

    protected void getDataTabulkaNabidka() {

        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arNab1.clear();

        tabulkaModelNabidka1.oznamZmenu();

        try {
            pocetKusuNabidky = 0;
            String dotaz =
                    "SELECT nabidky_id, nabidky_zakaznik_id, "
                    + "nabidky_datum, nabidky_cislo_vykresu, vykresy_nazev, "
                    + "vykresy_cislo, "
                    + "vykresy_revize, "
                    + "nabidky_pocet_objednanych_kusu, "
                    + "nabidky_cena_za_kus, "
                    + "CASE WHEN nabidky_povrch_uprava = 0 THEN '' ELSE btrim(to_char(nabidky_povrch_uprava,'FM99990D00'),'.,') END AS uprava, "
                    + "meny_zkratka, "
                    + "CASE WHEN typ_materialu_nazev IS NULL THEN typ_materialu_norma "
                    + "WHEN typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                    + "ELSE typ_materialu_nazev || ' - ' || typ_materialu_norma END AS material, "
                    + "nabidky_polotovar_id, "
                    + "polotovary_nazev, "
                    + "btrim(to_char(nabidky_mnozstvi_materialu,'FM99990D9999'),'.,'), "
                    + "COALESCE(vykresy_bindata_nazev,''), nabidky_poznamka, t.subjekty_trhu_nazev "
                    + "FROM spolecne.nabidky "
                    + "LEFT JOIN(SELECT vykresy_bindata_id, vykresy_bindata_nazev "
                    + "FROM spolecne.vykresy_bindata) bd "
                    + "ON nabidky_cislo_vykresu = bd.vykresy_bindata_id "
                    + "LEFT JOIN (SELECT subjekty_trhu_id, subjekty_trhu_nazev "
                    + "FROM spolecne.subjekty_trhu) AS t "
                    + "ON t.subjekty_trhu_id = nabidky_zakaznik_id "
                    + "CROSS JOIN spolecne.typ_materialu "
                    + "CROSS JOIN spolecne.polotovary "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE polotovary_typ_materialu = typ_materialu_id "
                    + "AND meny_id = nabidky_mena_id "
                    + "AND meny.meny_id = nabidky.nabidky_mena_id "
                    + "AND polotovary_id = nabidky_polotovar_id "
                    + "AND vykresy.vykresy_id = nabidky_cislo_vykresu ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND nabidky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (jCheckBoxFiltrZdrojNabidka1.isSelected() == true) {
                dotaz += "AND nabidky_ukoncena IS FALSE ";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT(YEAR FROM nabidky_datum) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            if (CisloVykresuTextField1.getText().length() > 0) {
                dotaz += "AND vykresy.vykresy_cislo LIKE '%" + CisloVykresuTextField1.getText().trim() + "%' ";
            }
            if (NazevObjednavkyTextField1.getText().length() > 0) {
                dotaz += "AND vykresy_nazev LIKE '%" + NazevObjednavkyTextField1.getText().trim() + "%' ";
            }

            if (TextFunkce1.osetriDatum(jTextFieldDatumOd.getText().trim())) {
                dotaz += " AND nabidky_datum > " + TextFunkce1.osetriZapisTextDB1(jTextFieldDatumOd.getText().trim()) + " ";
            }
            if (TextFunkce1.osetriDatum(jTextFieldDatumDo.getText().trim())) {
                dotaz += " AND nabidky_datum < " + TextFunkce1.osetriZapisTextDB1(jTextFieldDatumDo.getText().trim()) + " ";
            }
            dotaz += "GROUP BY nabidky_id, vykresy.vykresy_nazev, vykresy.vykresy_cislo, vykresy.vykresy_revize, meny.meny_zkratka, typ_materialu.typ_materialu_nazev, "
                    + "typ_materialu.typ_materialu_norma, polotovary.polotovary_nazev, "
                    + "bd.vykresy_bindata_nazev, t.subjekty_trhu_nazev,nabidky.nabidky_zakaznik_id,"
                    + "nabidky.nabidky_datum, nabidky.nabidky_cislo_vykresu, nabidky.nabidky_pocet_objednanych_kusu, "
                    + "nabidky.nabidky_cena_za_kus, nabidky.nabidky_povrch_uprava, nabidky.nabidky_polotovar_id, "
                    + "nabidky.nabidky_mnozstvi_materialu, nabidky.nabidky_poznamka "
                    + "";
            dotaz += "ORDER BY t.subjekty_trhu_nazev,vykresy_cislo, vykresy_revize ASC, nabidky_pocet_objednanych_kusu  ";

            ResultSet q = PripojeniDB.dotazS(dotaz);
            //System.out.println(dotaz);

            while (q.next()) {
                tNab1 = new TridaNabidka1();
                tNab1.setIdNabidky(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tNab1.setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tNab1.setDatum(q.getDate(3));
                tNab1.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(q.getInt(4));
                tv1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tv1.setCislo(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                tv1.setRevize(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                tNab1.setPocetObjednanychKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
                pocetKusuNabidky += q.getInt(8);
                tNab1.setCenaKus(SQLFunkceObecne.osetriCteniString(q.getString(9)));
                tNab1.setPovrchUprava(SQLFunkceObecne.osetriCteniString(q.getString(10)));
                tNab1.setPopisMena(SQLFunkceObecne.osetriCteniString(q.getString(11)));
                TridaTypMaterial1 ttm1 = new TridaTypMaterial1();
                ttm1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(12)));
                TridaPolotovar1 tp1 = new TridaPolotovar1();
                tp1.settTM1(ttm1);
                tp1.setIdPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(13)));
                tp1.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(14)));
                tNab1.setTp1(tp1);
                tNab1.setMaterialMnozstvi(SQLFunkceObecne.osetriCteniString(q.getString(15)));
                tv1.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(16)));
                tNab1.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(17)));
                tNab1.setTv1(tv1);
                arNab1.add(tNab1);

            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        jLPocetPolozek.setText(arNab1.size() + "");

        obarvitTabulku();
        if (arNab1.size() > 0) {
            tNab1 = arNab1.get(0);
            getDataNabidka1(tNab1);
        }

    } //konec getDataTabulkaObjednavka1

    protected void getDataNabidka1(TridaNabidka1 tn1) {
        try {
            tn1.selectData(tn1.getIdNabidky());

            PraceTextField1.setText(tn1.getPrace());
            PraceTextField2.setText(tn1.getPrace2());
            PraceTextField3.setText(tn1.getPrace3());

            PripravaTextField1.setText(tn1.getPriprava());
            PGMTextField1.setText(tn1.getPgm());

            SazbaPraceTextField1.setText(tn1.getSazba1());
            SazbaPraceTextField2.setText(tn1.getSazba4());
            SazbaPraceTextField3.setText(tn1.getSazba5());
            SazbaPripravaTextField1.setText(tn1.getSazba2());
            SazbaPGMTextField1.setText(tn1.getSazba3());

            try {
                PovrchTextField1.setText(tn1.getPovrchUprava());
            } catch (Exception e) {
            }
            try {
                BrouseniTextField1.setText(tn1.getBrouseni());

            } catch (Exception e) {
            }
            try {
                // tn1.setTp1(new TridaPolotovar1(tn1.getIdPolotovar()));
                tn1.getTp1().selectData(tn1.getIdPolotovar());
                if (tn1.getTp1().getMernaHmotnost().isEmpty() == false && tn1.getMaterialMnozstvi().isEmpty() == false) {
                    MaterialTextField1.setText(nf2.format(Double.valueOf(tn1.getTp1().getMernaHmotnost()) * (Double.valueOf(tn1.getMaterialMnozstvi()))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            double celkCasMCV = 0;
            double celkCasDMU = 0;
            double celkCasTor = 0;
            if (tn1.getPrace().isEmpty() == false) {
                celkCasMCV = tn1.getPocetObjednanychKusu() * Double.valueOf(tn1.getPrace());
                CelkovyCasMCVTextField.setText(nf2.format(celkCasMCV / 60));
            }
            if (tn1.getPrace2().isEmpty() == false) {
                celkCasDMU = tn1.getPocetObjednanychKusu() * Double.valueOf(tn1.getPrace2());
                CelkovyCasDMUTextField.setText(nf2.format(celkCasDMU / 60));
            }
            if (tn1.getPrace3().isEmpty() == false) {
                celkCasTor = tn1.getPocetObjednanychKusu() * Double.valueOf(tn1.getPrace3());
                CelkovyCasTorTextField.setText(nf2.format(celkCasTor / 60));
            }
            CelkovyCasTextField.setText(nf2.format((celkCasMCV + celkCasDMU + celkCasTor + Double.valueOf(tn1.getPriprava())) / 60));

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void obarvitTabulku() {

        int index[][] = new int[arNab1.size()][tabulkaModelNabidka1.getColumnCount()];
        int index2[][] = new int[arNab1.size()][tabulkaModelNabidka1.getColumnCount()];
        int index3[][] = new int[arNab1.size()][tabulkaModelNabidka1.getColumnCount()];
        int index4[][] = new int[arNab1.size()][tabulkaModelNabidka1.getColumnCount()];
        int index5[][] = new int[arNab1.size()][tabulkaModelNabidka1.getColumnCount()];
        for (int row = 0; row < arNab1.size(); row++) {

            index[row][6] = 1;
            index3[row][5] = 1;
            index4[row][0] = 3;
            index4[row][1] = 3;
            index4[row][2] = 3;
            index4[row][3] = 3;
            index4[row][4] = 1;
            index4[row][5] = 1;
            index4[row][6] = 1;
            index4[row][7] = 2;
            index4[row][8] = 3;
            index4[row][9] = 3;
            index4[row][10] = 1;
            index4[row][11] = 2;

            //}

            //tabulka.se(Object.class, new ColorCellRenderer(index,index2,index3,index4, backColor,frontColor));
            // tabulka.getDefaultRenderer(tabulkaModelNabidka1.getColumnClass(5)).getTableCellRendererComponent(tabulka, ui, true, true, WIDTH, WIDTH);

        }
        Color backColor = new Color(255, 204, 204);
        Color frontColor = Color.BLACK;
        for (int i = 0; i
                < tabulkaModelNabidka1.getColumnCount(); i++) {
            TableColumn column = null;
            column = tabulka.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRendererNab1(index, index2, index3, index4, index5, backColor, frontColor));
        }
    }

    protected void vycistiFiltrNabidka1() {
        NazevObjednavkyTextField1.setText("");
        jTextFieldDatumOd.setText("");
        jTextFieldDatumDo.setText("");
        CisloVykresuTextField1.setText("");
    }

    public void vytvorNabidkuExistujici() {
        // System.out.println("Stavajici : " + arNab1.get(tabulka.getSelectedRow()).getIdNabidky());
        NabidkaFrame novaNabidka = new NabidkaFrame(arNab1.get(tabulka.getSelectedRow()).getIdNabidky(), false, "Nová nabídka na základě stávající", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }

    public void editNabidka() {
        NabidkaFrame novaNabidka = new NabidkaFrame(arNab1.get(tabulka.getSelectedRow()).getIdNabidky(), true, "Úprava nabídky", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }

    private void smazatNabidka() {
        int rc = 0;
        int ud = JednoducheDialogy1.warnAnoNe(this, "Ano", "Ne", "Prosím, potvrďte smazání nabídky",
                "Opravdu chcete smazat nabídku " + arNab1.get(tabulka.getSelectedRow()).getTv1().getNazev() + " ?", 1);


        if (ud == JOptionPane.YES_OPTION) {
            rc = 0;
            rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
            if (rc > 0) {
                String chyba = SQLFunkceObecne2.getCHybovouHlasku(rc);
                JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při mazání nabídky", chyba);

                return;
            }
            try {
                PripojeniDB.dotazIUD("DELETE FROM spolecne.nabidky "
                        + "WHERE nabidky_id = " + arNab1.get(tabulka.getSelectedRow()).getIdNabidky());
            } catch (Exception e) {
                e.printStackTrace();
            }
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        }
    }

    private void seznamNabidka() {
        int indexy[] = tabulka.getSelectedRows();
        long indexyNabidky[] = new long[indexy.length];
        for (int i = 0; i < indexy.length; i++) {
            indexyNabidky[i] = arNab1.get(indexy[i]).getIdNabidky();
        }
        SeznamNabidekFrame1 seznam = new SeznamNabidekFrame1(indexyNabidky, ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }

    protected class tabulkaModelNabidka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum",
            "Název součásti",
            "Číslo výkresu",
            "Revize",
            "Ks",
            "Cena/ks",
            "Povrch ",
            "Měna",
            "Materiál",
            "Polotovar",
            "Množství [m]",
            "Výkres",
            "Poznámka"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arNab1.size());
            //  updateZaznamyObjednavka1();
            if (arNab1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arNab1.remove(tabulka.getSelectedRow());
            fireTableRowsDeleted(tabulka.getSelectedRow(), tabulka.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrObjednavka1size() > 0)
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
            return arNab1.size();
        }//konec getRowCount

        @Override
        public String getColumnName(int col) {
            try {
                return columnNames[col];
            } catch (Exception ex) {
                return null;
            }
        }//konec getColumnName

        /*
         * @Override public Class getColumnClass(int col) {
         * System.out.println("trida sloupce col je " + getValueAt(0,
         * col).getClass()); if (col == 1) { return Boolean.class; } if (col ==
         * 2 || col == 3) { return Integer.class; } return getValueAt(0,
         * col).getClass();//String.class; }
         */
        public boolean nastavHodnotuNaVybrane(TridaNabidka1 bt) {
            System.out.println("nastavHodnotuNaVybraneNabidka1 " + tabulka.getSelectedRow());
            return nastavHodnotuNaPozici(bt, tabulka.getSelectedRow());
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaNabidka1 nastavPruv, int pozice) {
            //   System.out.println("nastav hodnotu na pozici");
            fireTableRowsUpdated(pozice, pozice);
            return true;
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            /*
             * if (col == 1 || col == 5 || col == 5 || col == 12) { return true;
             * } else {
             */
            return false;
            // }
        }

        @Override
        public Object getValueAt(int row, int col) {
            // System.out.println("getValueAt PruvFram");
            try {
                tNab1 = arNab1.get(row);
                switch (col) {
                    case (0): {
                        if (tNab1.getDatum() != null) {
                            return (df.format(tNab1.getDatum()));
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
                        return (tNab1.getPocetObjednanychKusu() + " ks");
                    }
                    case (5): {
                        return (tNab1.getCenaKus());
                    }
                    case (6): {
                        return (tNab1.getPovrchUprava());
                    }
                    case (7): {
                        return (tNab1.getPopisMena());
                    }
                    case (8): {
                        if (tNab1.getTp1() != null) {
                            if (tNab1.getTp1().gettTM1() != null) {
                                //  System.out.println("chyba : " + tNab1.getTv1().getCislo());
                                return (tNab1.getTp1().gettTM1().getNazev());
                            } else {
                                return "";
                            }
                        } else {
                            return "";
                        }
                    }
                    case (9): {
                        if (tNab1.getTp1() != null) {
                            return (tNab1.getTp1().getNazev());
                        } else {
                            return "";
                        }
                    }
                    case (10): {
                        return (tNab1.getMaterialMnozstvi() + " m");
                    }
                    case (11): {
                        return (tNab1.getTv1().getPoznamky());
                    }
                    case (12): {
                        return (tNab1.getPoznamka());
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
            if (e.getActionCommand().equals("Refresh") || e.getActionCommand().equals("FiltrujPruvodky1")
                    || e.getActionCommand().equals("VycistiFiltrObjednavka1") || e.getActionCommand().equals("Hledat")) {
                if (e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                    vycistiFiltrNabidka1();
                }
                refreshDataHlavni();
            }

            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }

            if (e.getActionCommand().equals("NovaNabidka")) {
                NabidkaFrame novaNabidka = new NabidkaFrame("Nová nabídka", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
            }

            if (e.getActionCommand().equals("EditNabidka")) {
                editNabidka();
            }

            if (e.getActionCommand().equals("NovaNabidkaStavajici")) {
                vytvorNabidkuExistujici();
            }
            if (e.getActionCommand().equals("SmazatNabidka")) {
                smazatNabidka();
            }
            if (e.getActionCommand().equals("SeznamNabidek")) {
                seznamNabidka();
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
          

            if (tme.getSource() == tableModelNabidka1) {
                //   updateZaznamyObjednavka1();
            }//konec if tme.getSource()           

        }// konec tableChanged
    } //konec TMLUdalosti

    class LSLUdalosti implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == lsmNabidka1) {
                if (e.getValueIsAdjusting() == false) {
                    getDataNabidka1(arNab1.get(tabulka.getSelectedRow()));
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
        JButtonVycistiFiltrObjednavka1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCBRokDodani = new javax.swing.JComboBox();
        jCheckBoxFiltrZdrojNabidka1 = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        NazevObjednavkyTextField1 = new javax.swing.JTextField();
        jTextFieldDatumOd = new javax.swing.JTextField();
        jTextFieldDatumDo = new javax.swing.JTextField();
        VyhledatButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jCBZakaznik = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLPocetPolozek = new javax.swing.JLabel();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jBNovaNabidka = new javax.swing.JButton();
        jBNovaNabidkaStavajici = new javax.swing.JButton();
        jBEditNabidka = new javax.swing.JButton();
        jBVykres = new javax.swing.JButton();
        jBSeznamNabidek = new javax.swing.JButton();
        jBSmazatNabidka = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        PraceTextField1 = new javax.swing.JTextField();
        PraceTextField2 = new javax.swing.JTextField();
        PraceTextField3 = new javax.swing.JTextField();
        SazbaPraceTextField1 = new javax.swing.JTextField();
        SazbaPraceTextField2 = new javax.swing.JTextField();
        SazbaPraceTextField3 = new javax.swing.JTextField();
        PripravaTextField1 = new javax.swing.JTextField();
        SazbaPripravaTextField1 = new javax.swing.JTextField();
        SazbaPGMTextField1 = new javax.swing.JTextField();
        PovrchTextField1 = new javax.swing.JTextField();
        BrouseniTextField1 = new javax.swing.JTextField();
        MaterialTextField1 = new javax.swing.JTextField();
        CelkovyCasMCVTextField = new javax.swing.JTextField();
        CelkovyCasDMUTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        CelkovyCasTorTextField = new javax.swing.JTextField();
        CelkovyCasTextField = new javax.swing.JTextField();
        PGMTextField1 = new javax.swing.JTextField();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

        jLabel3.setText("Rok nabídky :");
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

        jCheckBoxFiltrZdrojNabidka1.setText("Filtr aktivní nabídky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jCheckBoxFiltrZdrojNabidka1, gridBagConstraints);

        jPFiltry.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPFiltry.setDoubleBuffered(false);
        jPFiltry.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Číslo výkresu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPFiltry.add(jLabel6, gridBagConstraints);

        jLabel8.setText("Název součásti");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        jPFiltry.add(jLabel8, gridBagConstraints);

        jLabel16.setText("Datum od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPFiltry.add(jLabel16, gridBagConstraints);

        jLabel17.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(jLabel17, gridBagConstraints);
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
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldDatumOd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldDatumDo, gridBagConstraints);

        VyhledatButton1.setText("Vyhledat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
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

        jLabel1.setText("Zobrazených nabídek :");
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

        jBNovaNabidka.setText("Nová nabídka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaNabidka, gridBagConstraints);

        jBNovaNabidkaStavajici.setText("Nová nabídka na základě stávající");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaNabidkaStavajici, gridBagConstraints);

        jBEditNabidka.setText("Upravit nabídku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBEditNabidka, gridBagConstraints);

        jBVykres.setText("Zobrazit výkres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBVykres, gridBagConstraints);

        jBSeznamNabidek.setText("Vytvořit seznam nabídek - PDF");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSeznamNabidek, gridBagConstraints);

        jBSmazatNabidka.setText("Smazat nabídku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBSmazatNabidka, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Práce 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel4.setText("Práce 3:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Práce 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel7.setText("Příprava:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabel9.setText("PGM:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Povrchová úprava:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Sazba :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 35);
        jPanel2.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Broušení:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Čas[min]");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 35);
        jPanel2.add(jLabel13, gridBagConstraints);

        PraceTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PraceTextField1, gridBagConstraints);

        PraceTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PraceTextField2, gridBagConstraints);

        PraceTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PraceTextField3, gridBagConstraints);

        SazbaPraceTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SazbaPraceTextField1, gridBagConstraints);

        SazbaPraceTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SazbaPraceTextField2, gridBagConstraints);

        SazbaPraceTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SazbaPraceTextField3, gridBagConstraints);

        PripravaTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PripravaTextField1, gridBagConstraints);

        SazbaPripravaTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SazbaPripravaTextField1, gridBagConstraints);

        SazbaPGMTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SazbaPGMTextField1, gridBagConstraints);

        PovrchTextField1.setBackground(new java.awt.Color(254, 219, 150));
        PovrchTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PovrchTextField1, gridBagConstraints);

        BrouseniTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(BrouseniTextField1, gridBagConstraints);

        MaterialTextField1.setBackground(new java.awt.Color(184, 241, 241));
        MaterialTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(MaterialTextField1, gridBagConstraints);

        CelkovyCasMCVTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(CelkovyCasMCVTextField, gridBagConstraints);

        CelkovyCasDMUTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(CelkovyCasDMUTextField, gridBagConstraints);

        jLabel14.setText("Cena materiál:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel14, gridBagConstraints);

        jLabel15.setText("Celkový čas 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel15, gridBagConstraints);

        jLabel18.setText("Celkový čas 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel18, gridBagConstraints);

        jLabel19.setText("Celkový čas 3:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel19, gridBagConstraints);

        jLabel21.setText("Celkový čas:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel21, gridBagConstraints);

        CelkovyCasTorTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(CelkovyCasTorTextField, gridBagConstraints);

        CelkovyCasTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(CelkovyCasTextField, gridBagConstraints);

        PGMTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PGMTextField1, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPFiltry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1410, Short.MAX_VALUE)
            .addComponent(JPFiltrTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSPTabulka)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPFiltrTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPFiltry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSPTabulka, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BrouseniTextField1;
    private javax.swing.JTextField CelkovyCasDMUTextField;
    private javax.swing.JTextField CelkovyCasMCVTextField;
    private javax.swing.JTextField CelkovyCasTextField;
    private javax.swing.JTextField CelkovyCasTorTextField;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JTextField MaterialTextField1;
    private javax.swing.JTextField NazevObjednavkyTextField1;
    private javax.swing.JTextField PGMTextField1;
    private javax.swing.JTextField PovrchTextField1;
    private javax.swing.JTextField PraceTextField1;
    private javax.swing.JTextField PraceTextField2;
    private javax.swing.JTextField PraceTextField3;
    private javax.swing.JTextField PripravaTextField1;
    private javax.swing.JTextField SazbaPGMTextField1;
    private javax.swing.JTextField SazbaPraceTextField1;
    private javax.swing.JTextField SazbaPraceTextField2;
    private javax.swing.JTextField SazbaPraceTextField3;
    private javax.swing.JTextField SazbaPripravaTextField1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBEditNabidka;
    private javax.swing.JButton jBNovaNabidka;
    private javax.swing.JButton jBNovaNabidkaStavajici;
    private javax.swing.JButton jBSeznamNabidek;
    private javax.swing.JButton jBSmazatNabidka;
    private javax.swing.JButton jBVykres;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JCheckBox jCheckBoxFiltrZdrojNabidka1;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JTextField jTextFieldDatumDo;
    private javax.swing.JTextField jTextFieldDatumOd;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
