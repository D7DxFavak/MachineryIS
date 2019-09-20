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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class Termin1Panel extends javax.swing.JPanel {

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
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public Termin1Panel() {
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

        JButtonVycistiFiltrObjednavka1.addActionListener(alUdalosti);
        jCheckBoxFiltrZdrojObjednavka1.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");

        PridatDoTerminuButton1.addActionListener(alUdalosti);
        OdebratPolozkyButton1.addActionListener(alUdalosti);
        TiskTerminuButton1.addActionListener(alUdalosti);
        TiskVyberuButton1.addActionListener(alUdalosti);

        VyhledatButton1.setActionCommand("Hledat");
        jCBZakaznik.setActionCommand("Refresh");

        PridatDoTerminuButton1.setActionCommand("PridatDoTerminu");
        OdebratPolozkyButton1.setActionCommand("OdebratZTerminu");
        TiskTerminuButton1.setActionCommand("TiskTerminu");
        TiskVyberuButton1.setActionCommand("TiskVyberu");

    }

    protected void nastavTabulkuObjednavka1() {
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
        column.setPreferredWidth(80);

        column = tabulka.getColumnModel().getColumn(13);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(14);
        column.setPreferredWidth(110);*/

        /*
         * column = tabulka.getColumnModel().getColumn(18);
         * column.setPreferredWidth(110);
         */

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
        roletkaModelZakaznici = new RoletkaUniverzalRozsirenaModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_popis FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_popis", "Neurčen", null,
                "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBZakaznik.setModel(roletkaModelZakaznici);

    }

    protected void getDataTabulkaObjednavka1() {
        /*
         * if ((CisloVykresuTextField1.getText().trim().length() > 0) ||
         * (NazevObjednavkyTextField1.getText().trim().length() > 0) ||
         * (CisloObjednavkyTextField1.getText().trim().length() > 0) ||
         * (jTextFieldExpediceObjednavkyOd1.getText().trim().length() > 0) ||
         * (jTextFieldExpediceObjednavkyDo1.getText().trim().length() > 0) ||
         * (jTextFieldTerminDodaniOd1.getText().trim().length() > 0) ||
         * (jTextFieldTerminDodaniDo1.getText().trim().length() > 0) ||
         * (jTextFieldTerminObjednaniOd1.getText().trim().length() > 0) ||
         * (jTextFieldTerminObjednaniDo1.getText().trim().length() > 0)) {
         * roletkaModelRoky.setSelectedIndex(0); }
         */

        float celkovaDobaMcv = 0;
        float celkovaDobaTornado = 0;
        boolean dotazOk = false;
        boolean datumDodani = false;
        boolean datumExpedice = false;
        boolean datumObjednani = false;
        boolean filtr = false;

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
                    + "objednavky_poznamka, objednavky_cislo_vykresu, objednavky_reklamace , t.subjekty_trhu_nazev "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.druhy_povrchova_uprava "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.terminy "
                    + "LEFT JOIN (SELECT subjekty_trhu_id, subjekty_trhu_nazev "
                    + "FROM spolecne.subjekty_trhu) AS t "
                    + "ON t.subjekty_trhu_id = objednavky_zakaznik_id "
                    + "WHERE objednavky.objednavky_id IS NOT NULL "
                    + "AND druhy_povrchova_uprava.druhy_povrchova_uprava_id = objednavky.objednavky_povrchova_uprava_id "
                    + "AND meny.meny_id = objednavky.objednavky_mena_id "
                    + "AND terminy.terminy_cislo_terminu = 1 "
                    + "AND objednavky.objednavky_id = terminy.terminy_objednavky_id "
                    + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu ";
            if (CisloVykresuTextField1.getText().length() > 0) {
                dotaz += "AND vykresy.vykresy_cislo LIKE '%" + CisloVykresuTextField1.getText().trim() + "%' ";
                filtr = true;
            }
            if (NazevObjednavkyTextField1.getText().length() > 0) {
                dotaz += "AND objednavky_nazev_soucasti LIKE '%" + NazevObjednavkyTextField1.getText().trim() + "%' ";
                filtr = true;
            }
            if (CisloObjednavkyTextField1.getText().length() > 0) {
                dotaz += "AND objednavky_cislo_objednavky = '" + CisloObjednavkyTextField1.getText().trim() + "' ";
                filtr = true;
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            boolean osetritDatumOd = TextFunkce1.osetriDatum(jTextFieldTerminDodaniOd1.getText().trim());
            boolean osetritDatumDo = TextFunkce1.osetriDatum(jTextFieldTerminDodaniDo1.getText().trim());

            if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                datumDodani = true;
                dotaz += " AND objednavky_termin_dodani BETWEEN '" + jTextFieldTerminDodaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminDodaniDo1.getText() + "'";
                filtr = true;
            }

            osetritDatumOd = TextFunkce1.osetriDatum(jTextFieldExpediceObjednavkyOd1.getText().trim());
            osetritDatumDo = TextFunkce1.osetriDatum(jTextFieldExpediceObjednavkyDo1.getText().trim());
            if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                datumExpedice = true;
                dotaz += " AND objednavky_datum_expedice BETWEEN '" + jTextFieldExpediceObjednavkyOd1.getText().trim() + "' AND '"
                        + jTextFieldExpediceObjednavkyDo1.getText().trim() + "'";
                filtr = true;
            }
            osetritDatumOd = TextFunkce1.osetriDatum(jTextFieldTerminObjednaniOd1.getText().trim());
            osetritDatumDo = TextFunkce1.osetriDatum(jTextFieldTerminObjednaniDo1.getText().trim());
            if ((osetritDatumOd == true) && (osetritDatumDo == true)) {
                datumObjednani = true;
                dotaz += " AND objednavky_datum_objednani BETWEEN '" + jTextFieldTerminObjednaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminObjednaniDo1.getText().trim() + "' ";
                filtr = true;
            }
            if (jCheckBoxFiltrZdrojObjednavka1.isSelected() == true) {
                dotaz += "AND objednavky_datum_expedice IS NULL AND objednavky_cislo_faktury IS NULL ";
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "ORDER BY vykresy_cislo, objednavky_cislo_objednavky ASC ";
            } else {
                dotaz += "ORDER BY t.subjekty_trhu_nazev, vykresy_cislo ASC, objednavky_cislo_objednavky ASC ";
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
                //System.out.println("cena " + objednavka1.getString(14) + " x " + nf2.format(nf2.parse(objednavka1.getString(14).replace(".", ","))) + " x " + nf2.parse(objednavka1.getString(14).replace(".", ",")));
                tObj1.setCenaKus(nf2.format(nf2.parse(objednavka1.getString(15).replace(".", ",")))); // cena
                tObj1.setPopisMena((objednavka1.getString(16) == null) ? "" : objednavka1.getString(16));
                // tObj1.setCasCelkem((objednavka1.getString(22) == null) ? "" : objednavka1.getString(22));

                try {
                    tObj1.setPoznamka((objednavka1.getString(17) == null) ? "" : objednavka1.getString(17));
                } catch (Exception e) {
                    tObj1.setPoznamka(null); // poznamka
                }
                /* if ((objednavka1.getString(24)) == null) {
                 tObj1.setPopisRamcovaObjednavka((objednavka1.getString(26) == null) ? "" : objednavka1.getString(26));
                 } else {
                 tObj1.setPopisRamcovaObjednavka((objednavka1.getString(23) == null) ? "" : objednavka1.getString(23));
                 }*/
                tObj1.setReklamace(objednavka1.getBoolean(19));
                arTO1.add(tObj1);

            }// konec while
            dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        if ((KapacitaCheckBox1.isSelected() == true) && (dotazOk == true)) {
            try {
                String dotaz = "SELECT "
                        + "SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_mcv), "
                        + "SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_tornado) "
                        + ",SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_dmu) "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.vyrobni_kapacita "
                        + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND vyrobni_kapacita.vyrobni_kapacita_cislo_vykresu = vykresy_cislo "
                        + "AND objednavky_datum_expedice IS NULL ";
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                // System.out.println(dotaz);
                ResultSet kapacita1 = PripojeniDB.dotazS(dotaz);

                kapacita1.last();
                // System.out.println("kapacita :" + kapacita1.getRow());
                if (kapacita1.getRow() > 0) {
                    kapacita1.beforeFirst();
                    while (kapacita1.next()) {
                        McvKapacitaLabel1.setText(kapacita1.getString(1));
                        TornadoKapacitaLabel1.setText(kapacita1.getString(2));
                        DMUKapacitaLabel1.setText(kapacita1.getString(3));
                    }
                } else {
                    McvKapacitaLabel1.setText("0.00");
                    TornadoKapacitaLabel1.setText("0.00");
                    DMUKapacitaLabel1.setText("0.00");
                }

                dotaz = "SELECT "
                        + "SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_mcv), "
                        + "SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_tornado) "
                        + ",SUM ((objednavky_kusu_vyrobit + objednavky_kusu_navic) * vyrobni_kapacita_cas_dmu) "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.vyrobni_kapacita "
                        + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND vyrobni_kapacita.vyrobni_kapacita_cislo_vykresu = vykresy_cislo "
                        + "AND objednavky_datum_expedice IS NULL ";
                kapacita1 = PripojeniDB.dotazS(dotaz);
                while (kapacita1.next()) {
                    McvKapacitaCelkLabel1.setText(kapacita1.getString(1));
                    TornadoKapacitaCelkLabel1.setText(kapacita1.getString(2));
                    DMUKapacitaCelkLabel1.setText(kapacita1.getString(3));
                }

                if (McvKapacitaLabel1.getText() == null) {
                    McvKapacitaLabel1.setText("0.00");
                }
                if (TornadoKapacitaLabel1.getText() == null) {
                    TornadoKapacitaLabel1.setText("0.00");
                }
                if (DMUKapacitaLabel1.getText() == null) {
                    DMUKapacitaLabel1.setText("0.00");
                }
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
        if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
            try {
                String dotaz = "SELECT "
                        + "SUM (objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus) "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.terminy "
                        + "WHERE terminy.terminy_objednavky_id = objednavky.objednavky_id "
                        + " AND terminy.terminy_cislo_terminu = 1 ";
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                if (datumDodani == true) {
                    dotaz += " AND objednavky_termin_dodani BETWEEN " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniOd1.getText().trim()) + " AND "
                            + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminDodaniDo1.getText()) + " ";
                }
                if (datumExpedice == true) {
                    dotaz += " AND objednavky_datum_expedice BETWEEN " + TextFunkce1.osetriZapisTextDB1(jTextFieldExpediceObjednavkyOd1.getText().trim()) + " AND "
                            + TextFunkce1.osetriZapisTextDB1(jTextFieldExpediceObjednavkyDo1.getText()) + " ";
                }
                if (datumObjednani == true) {
                    dotaz += " AND objednavky_datum_objednani BETWEEN " + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminObjednaniOd1.getText().trim()) + " AND "
                            + TextFunkce1.osetriZapisTextDB1(jTextFieldTerminObjednaniDo1.getText()) + " ";
                }
                ResultSet celkovacena1 = PripojeniDB.dotazS(dotaz);
                celkovacena1.last();
                if (celkovacena1.getRow() > 0) {
                    // System.out.println("Celk. cena OK");
                    celkovacena1.beforeFirst();
                    while (celkovacena1.next()) {
                        SumaZakazekLabel1.setText((celkovacena1.getString(1) == null) ? "0" : celkovacena1.getString(1));
                    }
                }
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
        jLPocetPolozek.setText(arTO1.size() + "");
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

    private void pridatDoTerminu() {
        int indexy[] = tabulka.getSelectedRows();
        long indexObjednavky[] = new long[indexy.length];
        for (int i = 0; i
                < indexy.length; i++) {
            indexObjednavky[i] = arTO1.get(indexy[i]).getId();
        }
        long terminy_id = 0;
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(terminy_id) FROM spolecne.terminy");
            while (id.next()) {
                terminy_id = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i
                < indexObjednavky.length; i++) {
            String dotaz = "UPDATE spolecne.terminy "
                    + "SET terminy_cislo_terminu = 2 "
                    + "WHERE terminy_objednavky_id = " + indexObjednavky[i];
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            terminy_id++;
        }
        zrusPosluchaceUdalostiTabulky();
        getDataTabulkaObjednavka1();
        tabulkaModelObjednavka1.pridejSadu();
        nastavPosluchaceUdalostiTabulky();
    }

    private void odebratZTerminu() {
        int indexy[] = tabulka.getSelectedRows();
        long indexObjednavky;
        for (int i = 0; i
                < indexy.length; i++) {
            indexObjednavky = arTO1.get(i).getId();
            String dotaz1 = "DELETE FROM spolecne.terminy "
                    + "WHERE terminy_objednavky_id = " + indexObjednavky;
            try {
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

    private void tiskTerminu() {

        if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
            String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "termin1.jrxml";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("zakaznik", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).toString());
            params.put("datum", "datum");//posledniObjednavka);
            params.put("zakaznik_id", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());

            try {
                JasperReport jasperReport =
                        JasperCompileManager.compileReport(reportSource);

                JasperPrint jasperPrint1 =
                        JasperFillManager.fillReport(
                        jasperReport, params, PripojeniDB.con);

                //JasperViewer.viewReport(jasperPrint1);
                JasperPrintManager.printReport(jasperPrint1, true);
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        } else {
            String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "termin1celk.jrxml";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("zakaznik", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).toString());
            params.put("datum", "datum");//posledniObjednavka);            

            try {
                JasperReport jasperReport =
                        JasperCompileManager.compileReport(reportSource);

                JasperPrint jasperPrint1 =
                        JasperFillManager.fillReport(
                        jasperReport, params, PripojeniDB.con);

                //JasperViewer.viewReport(jasperPrint1);
                JasperPrintManager.printReport(jasperPrint1, true);
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }


    }

    private void tiskVyberu() {
        int radky[] = tabulka.getSelectedRows();
        try {
            int a = PripojeniDB.dotazIUD("DELETE FROM spolecne.terminy1");
            for (int i = 0; i < radky.length; i++) {
                a = PripojeniDB.dotazIUD("INSERT INTO spolecne.terminy1( "
                        + "terminy1_id, terminy1_objednavky_id, terminy1_poznamky) "
                        + "VALUES ("
                        + "(SELECT COALESCE((MAX(terminy1_id) +1),1) FROM spolecne.terminy1), "
                        + arTO1.get(radky[i]).getId() + ", null);");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac  + "termin1vyber.jrxml";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("zakaznik", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).toString());
        params.put("datum", "datum");//posledniObjednavka);
        params.put("zakaznik_id", ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());

        try {
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportSource);

            JasperPrint jasperPrint1 =
                    JasperFillManager.fillReport(
                    jasperReport, params, PripojeniDB.con);

            //JasperViewer.viewReport(jasperPrint1);
            JasperPrintManager.printReport(jasperPrint1, true);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    protected void vycistiFiltrObjednavka1() {
        CisloObjednavkyTextField1.setText("");
        NazevObjednavkyTextField1.setText("");
        jTextFieldExpediceObjednavkyDo1.setText("");
        jTextFieldExpediceObjednavkyOd1.setText("");
        jTextFieldTerminDodaniOd1.setText("");
        jTextFieldTerminDodaniDo1.setText("");
        jTextFieldTerminObjednaniOd1.setText("");
        jTextFieldTerminObjednaniDo1.setText("");
        CisloVykresuTextField1.setText("");
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
            //  System.out.println("pridej Sadu");

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
                if (e.getActionCommand().equals("VycistiFiltrObjednavka1")) {
                    vycistiFiltrObjednavka1();
                }
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaObjednavka1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }

            if (e.getActionCommand().equals("Hledat")) {
                zrusPosluchaceUdalostiTabulky();
                getDataTabulkaObjednavka1();
                //tabulkaModelObjednavka1.pridejSadu();
                nastavPosluchaceUdalostiTabulky();
            }


            if (e.getActionCommand().equals("VybratVseObjednavka1")) {
                //        zmenVyberObjednavka1(1);
            }
            if (e.getActionCommand().equals("VycistitVyberObjednavka1")) {
                //        zmenVyberObjednavka1(0);
            }
            if (e.getActionCommand().equals("PridatDoTerminu")) {
                pridatDoTerminu();
                //    zmenVyberObjednavka1(-1);
            }
            if (e.getActionCommand().equals("OdebratZTerminu")) {
                odebratZTerminu();
            }
            if (e.getActionCommand().equals("TiskTerminu")) {
                tiskTerminu();
            }

            if (e.getActionCommand().equals("TiskVyberu")) {
                tiskVyberu();
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
        JButtonVycistiFiltrObjednavka1 = new javax.swing.JButton();
        jCheckBoxFiltrZdrojObjednavka1 = new javax.swing.JCheckBox();
        jPFiltry = new javax.swing.JPanel();
        KapacitaCheckBox1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        McvKapacitaLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        McvKapacitaCelkLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TornadoKapacitaLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        TornadoKapacitaCelkLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        DMUKapacitaLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        DMUKapacitaCelkLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        NazevObjednavkyTextField1 = new javax.swing.JTextField();
        CisloObjednavkyTextField1 = new javax.swing.JTextField();
        jTextFieldTerminDodaniOd1 = new javax.swing.JTextField();
        jTextFieldExpediceObjednavkyOd1 = new javax.swing.JTextField();
        jTextFieldTerminObjednaniOd1 = new javax.swing.JTextField();
        jTextFieldTerminDodaniDo1 = new javax.swing.JTextField();
        jTextFieldExpediceObjednavkyDo1 = new javax.swing.JTextField();
        jTextFieldTerminObjednaniDo1 = new javax.swing.JTextField();
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
        TiskTerminuButton1 = new javax.swing.JButton();
        TiskVyberuButton1 = new javax.swing.JButton();
        PridatDoTerminuButton1 = new javax.swing.JButton();
        OdebratPolozkyButton1 = new javax.swing.JButton();

        JPFiltrTop.setLayout(new java.awt.GridBagLayout());

        JButtonVycistiFiltrObjednavka1.setText("Vyčistit filtr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        JPFiltrTop.add(JButtonVycistiFiltrObjednavka1, gridBagConstraints);

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

        KapacitaCheckBox1.setText("Zobrazit kapacitu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPFiltry.add(KapacitaCheckBox1, gridBagConstraints);

        jLabel5.setText("MCV");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel5, gridBagConstraints);

        McvKapacitaLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(McvKapacitaLabel1, gridBagConstraints);

        jLabel7.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPFiltry.add(jLabel7, gridBagConstraints);

        McvKapacitaCelkLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPFiltry.add(McvKapacitaCelkLabel1, gridBagConstraints);

        jLabel9.setText("Tornado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel9, gridBagConstraints);

        TornadoKapacitaLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(TornadoKapacitaLabel1, gridBagConstraints);

        jLabel11.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel11, gridBagConstraints);

        TornadoKapacitaCelkLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(TornadoKapacitaCelkLabel1, gridBagConstraints);

        jLabel10.setText("DMU");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(jLabel10, gridBagConstraints);

        DMUKapacitaLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(DMUKapacitaLabel1, gridBagConstraints);

        jLabel12.setText("/");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(jLabel12, gridBagConstraints);

        DMUKapacitaCelkLabel1.setText("0.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(DMUKapacitaCelkLabel1, gridBagConstraints);

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

        jLabel14.setText("Expedice od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel14, gridBagConstraints);

        jLabel15.setText("Objednáno od");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(jLabel15, gridBagConstraints);

        jLabel16.setText("Termín dodání od");
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

        jLabel18.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel18, gridBagConstraints);

        jLabel19.setText("do");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 2;
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
        jPFiltry.add(jTextFieldTerminDodaniOd1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldExpediceObjednavkyOd1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldTerminObjednaniOd1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldTerminDodaniDo1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldExpediceObjednavkyDo1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPFiltry.add(jTextFieldTerminObjednaniDo1, gridBagConstraints);

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

        SumaZakazekLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(SumaZakazekLabel1, gridBagConstraints);

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

        TiskTerminuButton1.setText("Tisk termínu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(TiskTerminuButton1, gridBagConstraints);

        TiskVyberuButton1.setText("Tisk výběru");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(TiskVyberuButton1, gridBagConstraints);

        PridatDoTerminuButton1.setText("Přidat do termínu 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(PridatDoTerminuButton1, gridBagConstraints);

        OdebratPolozkyButton1.setText("Odebrat položky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(OdebratPolozkyButton1, gridBagConstraints);

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
                .addComponent(jSPTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloObjednavkyTextField1;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JLabel DMUKapacitaCelkLabel1;
    private javax.swing.JLabel DMUKapacitaLabel1;
    private javax.swing.JButton JButtonVycistiFiltrObjednavka1;
    private javax.swing.JPanel JPFiltrTop;
    private javax.swing.JCheckBox KapacitaCheckBox1;
    private javax.swing.JLabel McvKapacitaCelkLabel1;
    private javax.swing.JLabel McvKapacitaLabel1;
    private javax.swing.JTextField NazevObjednavkyTextField1;
    private javax.swing.JButton OdebratPolozkyButton1;
    private javax.swing.JButton PridatDoTerminuButton1;
    private javax.swing.JLabel SumaZakazekLabel1;
    private javax.swing.JButton TiskTerminuButton1;
    private javax.swing.JButton TiskVyberuButton1;
    private javax.swing.JLabel TornadoKapacitaCelkLabel1;
    private javax.swing.JLabel TornadoKapacitaLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JCheckBox jCheckBoxFiltrZdrojObjednavka1;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPFiltry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JTextField jTextFieldExpediceObjednavkyDo1;
    private javax.swing.JTextField jTextFieldExpediceObjednavkyOd1;
    private javax.swing.JTextField jTextFieldTerminDodaniDo1;
    private javax.swing.JTextField jTextFieldTerminDodaniOd1;
    private javax.swing.JTextField jTextFieldTerminObjednaniDo1;
    private javax.swing.JTextField jTextFieldTerminObjednaniOd1;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
