/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import eu.data7.tableTools.ColorCellRendererObj1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
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
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class ObjednavkyPanel extends javax.swing.JPanel {

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
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelZakaznici, roletkaModelRoky;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    protected int pocetKusuObjednavky;

    /**
     * Creates new form ObjednavkyPanel
     */
    public ObjednavkyPanel() {
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
        PridatDoTerminuButton1.addActionListener(alUdalosti);
        jCBFiltrObjednavka.addActionListener(alUdalosti);
        OdebratButton1.addActionListener(alUdalosti);
        VyhledatButton1.addActionListener(alUdalosti);
        PotvrditObjednavkuButton1.addActionListener(alUdalosti);
        jCBZakaznik.addActionListener(alUdalosti);
        jCBRokDodani.addActionListener(alUdalosti);
        JButtonVycistiFiltrObjednavka1.setActionCommand("VycistiFiltrObjednavka1");
        jBNovaObjednavka.addActionListener(alUdalosti);
        jBEditObjednavka.addActionListener(alUdalosti);
        jBNovaObjednavkaStavajici.addActionListener(alUdalosti);
        PridatDoTerminuButton1.setActionCommand("PridatDoTerminu");
        OdebratButton1.setActionCommand("OdebratObjednavka");
        VyhledatButton1.setActionCommand("Hledat");
        PotvrditObjednavkuButton1.setActionCommand("PotvrditObjednavky");
        jCBZakaznik.setActionCommand("Refresh");
        jCBRokDodani.setActionCommand("Hledat");
        jBNovaObjednavka.setActionCommand("NovaObjednavka");
        jBEditObjednavka.setActionCommand("EditObjednavka");
        jBNovaObjednavkaStavajici.setActionCommand("NovaObjednavkaStavajici");

    }

    protected void nastavTabulkuObjednavka1() {
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

     
        float skluzCelkem = 0;
        int pocetSkluzObjednavek = 0;
        int pocetVirtualObjednavek = 0;
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
            String dotaz
                    = "SELECT * FROM (SELECT objednavky_id, "
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
                    + "objednavky_poznamka, "
                    + "objednavky_cislo_vykresu, "
                    + "objednavky_reklamace, "
                    + "objednavky_datum_potvrzeni, "
                    + "CASE WHEN (objednavky_datum_potvrzeni IS NOT NULL AND objednavky_datum_expedice IS NOT NULL) THEN objednavky_datum_expedice - objednavky_datum_potvrzeni "
                    + "WHEN (objednavky_datum_potvrzeni IS NOT NULL AND objednavky_datum_expedice IS NULL) THEN current_date - objednavky_datum_potvrzeni "
                    + "WHEN (objednavky_datum_potvrzeni IS NULL AND objednavky_termin_dodani IS NOT NULL AND objednavky_datum_expedice IS NOT NULL) THEN objednavky_datum_expedice - objednavky_termin_dodani "
                    + "WHEN (objednavky_datum_potvrzeni IS NULL AND objednavky_termin_dodani IS NOT NULL AND objednavky_datum_expedice IS NULL) THEN current_date - objednavky_termin_dodani "
                    + "ELSE NULL "
                    + "END AS skluz, "
                    + "objednavky_virtualni_polozka "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.druhy_povrchova_uprava "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE druhy_povrchova_uprava.druhy_povrchova_uprava_id = objednavky.objednavky_povrchova_uprava_id "
                    + "AND meny.meny_id = objednavky.objednavky_mena_id "
                    + "AND vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu ";
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT(YEAR FROM objednavky_datum_objednani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
            }
            if (CisloVykresuTextField1.getText().length() > 0) {
                dotaz += "AND vykresy.vykresy_cislo LIKE '%" + CisloVykresuTextField1.getText().trim() + "%' ";
                filtr = true;
            }
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
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
            if (jCBFiltrObjednavka.isSelected() == true) {
               // dotaz += "AND objednavky_datum_expedice IS NULL AND objednavky_cislo_faktury IS NULL ";
                dotaz += "AND objednavky_cislo_faktury IS NULL ";
            }
            dotaz += "ORDER BY objednavky_termin_dodani,vykresy_cislo ASC ) AS t "
                    + "LEFT JOIN (SELECT pruvodky_objednavky_id, pruvodky_cislo_vykresu, COALESCE(pruvodky_celkovy_cas, '') AS celkovy_cas "
                    + "FROM spolecne.pruvodky ) p "
                    + "ON (t.objednavky_id = p.pruvodky_objednavky_id AND t.objednavky_cislo_vykresu = p.pruvodky_cislo_vykresu)";
            dotaz += "LEFT JOIN (SELECT ramec_objednavky_cislo_objednavky, "
                    + "vazba_objednavky_ramec_objednavky_id "
                    + "FROM spolecne.ramec_objednavky "
                    + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec.vazba_objednavky_ramec_ramec_id = ramec_objednavky.ramec_objednavky_id ) AS r "
                    + "ON (t.objednavky_id = r.vazba_objednavky_ramec_objednavky_id) ";
            dotaz += "LEFT JOIN (SELECT vazba_objednavky_kanban_objednavky_id, CASE WHEN vazba_objednavky_kanban_kanban_id IS NOT NULL THEN 'Kanban' "
                    + "ELSE NULL "
                    + "END AS kanbanTrue "
                    + "FROM  spolecne.vazba_objednavky_kanban "
                    + ") AS kanban "
                    + "ON (t.objednavky_id = kanban.vazba_objednavky_kanban_objednavky_id) ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "ORDER BY t.objednavky_termin_dodani,t. vykresy_cislo ASC";
                filtr = true;
            } else {
                dotaz += "ORDER BY t.objednavky_termin_dodani,t. vykresy_cislo ASC";
                filtr = true;
            }
            //System.out.println(" DO " + dotaz);
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
                tObj1.setCasCelkem((objednavka1.getString(25) == null) ? "" : objednavka1.getString(25));

                try {
                    tObj1.setPoznamka((objednavka1.getString(17) == null) ? "" : objednavka1.getString(17));
                } catch (Exception e) {
                    tObj1.setPoznamka(null); // poznamka
                }
                if ((objednavka1.getString(26)) == null) {
                    tObj1.setPopisRamcovaObjednavka((objednavka1.getString(29) == null) ? "" : objednavka1.getString(29));
                } else {
                    tObj1.setPopisRamcovaObjednavka((objednavka1.getString(26) == null) ? "" : objednavka1.getString(26));
                }
                tObj1.setReklamace(objednavka1.getBoolean(19));
                try {
                    tObj1.setDatumPotvrzeni(objednavka1.getDate(20)); // datum expedice
                } catch (Exception e) {
                    tObj1.setDatumPotvrzeni(objednavka1.getDate(20));
                }

                if (SQLFunkceObecne.osetriCteniInt(objednavka1.getInt(21)) > 0 && objednavka1.getBoolean(22) == false) {
                    tObj1.setSkluz(SQLFunkceObecne.osetriCteniInt(objednavka1.getInt(21)));
                }
                tObj1.setVirtualni(objednavka1.getBoolean(22));
                if(objednavka1.getBoolean(22) == true) {
                    pocetVirtualObjednavek++;
                }

                if (tObj1.getSkluz() > 0 && tObj1.isVirtualni() == false) {// && tObj1.getDatumExpedice() != null) {
                    //if(tObj1.getSkluz() > 0) {

                    skluzCelkem += SQLFunkceObecne.osetriCteniInt(objednavka1.getInt(21));
                    pocetSkluzObjednavek++;
                }
                arTO1.add(tObj1);

            }// konec while
            dotazOk = true;
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        if ((KapacitaCheckBox1.isSelected() == true) && (dotazOk == true) && (datumDodani == true)) {
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
                        + "AND objednavky_datum_expedice IS NULL "
                        + "AND objednavky_termin_dodani BETWEEN '" + jTextFieldTerminDodaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminDodaniDo1.getText() + "' ";
                if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND EXTRACT(YEAR FROM objednavky_datum_objednani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
                }
                if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
                }
                //System.out.println(dotaz);
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
                        + "AND objednavky_datum_expedice IS NULL "
                        + "AND objednavky_termin_dodani BETWEEN '" + jTextFieldTerminDodaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminDodaniDo1.getText() + "' ";
                if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                    dotaz += "AND EXTRACT(YEAR FROM objednavky_datum_objednani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
                }
                kapacita1 = PripojeniDB.dotazS(dotaz);
                while (kapacita1.next()) {
                    McvKapacitaCelkLabel1.setText(kapacita1.getString(1));
                    TornadoKapacitaCelkLabel1.setText(kapacita1.getString(2));
                    DMUKapacitaCelkLabel1.setText(kapacita1.getString(3));
                }

                if (McvKapacitaLabel1.getText() == null) {
                    McvKapacitaLabel1.setText("0.00");
                    TornadoKapacitaLabel1.setText("0.00");
                    DMUKapacitaLabel1.setText("0.00");
                }
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
        try {
            String dotaz = "SELECT "
                    + "SUM (objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus) "
                    + "FROM spolecne.objednavky "
                    + "WHERE objednavky_zakaznik_id IS NOT NULL ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            if (datumDodani == true) {
                dotaz += " AND objednavky_termin_dodani BETWEEN '" + jTextFieldTerminDodaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminDodaniDo1.getText() + "'";
            }
            if (datumExpedice == true) {
                dotaz += " AND objednavky_datum_expedice BETWEEN '" + jTextFieldExpediceObjednavkyOd1.getText().trim() + "' AND '"
                        + jTextFieldExpediceObjednavkyDo1.getText() + "'";
            }
            if (datumObjednani == true) {
                dotaz += " AND objednavky_datum_objednani BETWEEN '" + jTextFieldTerminObjednaniOd1.getText().trim() + "' AND '"
                        + jTextFieldTerminObjednaniDo1.getText() + "'";
            }
            if (((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).cislo() > 0) {
                dotaz += "AND EXTRACT(YEAR FROM objednavky_datum_objednani) = " + ((DvojiceCisloRetez) roletkaModelRoky.getSelectedItem()).toString() + " ";
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
        jLPocetPolozek.setText(arTO1.size() + "");
        if (pocetSkluzObjednavek != 0) {
            jLSkluz.setText(nf2.format(skluzCelkem / arTO1.size()) + " d");
            // jLSkluz.setText(nf2.format(skluzCelkem/pocetSkluzObjednavek) + " d");
            jLSkluzProcenta.setText(nf2.format(100 * Double.valueOf(pocetSkluzObjednavek) / (arTO1.size()-pocetVirtualObjednavek)) + "%");
        } else {
            jLSkluz.setText("0 d");
            jLSkluzProcenta.setText("0 %");
        }
        //jTextFieldPocetKusuObjednavka1.setText(pocetKusuObjednavky + "");
        obarvitTabulku();
        tabulkaModelObjednavka1.oznamZmenu();

    } //konec getDataTabulkaObjednavka1

    private void obarvitTabulku() {
        Vector vrTerminy = new Vector();
        try {
            String dotaz = "SELECT terminy_objednavky_id "
                    + "FROM spolecne.terminy "
                    + "CROSS JOIN spolecne.objednavky "
                    + "WHERE terminy.terminy_objednavky_id = objednavky.objednavky_id "
                    + "AND (terminy.terminy_cislo_terminu = 1 OR terminy.terminy_cislo_terminu = 2 OR terminy.terminy_cislo_terminu = 3) "
                    + "AND objednavky_cislo_faktury IS NULL ";
            if (((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                dotaz += "AND objednavky_zakaznik_id = " + ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo() + " ";
            }
            dotaz += " ORDER BY terminy_objednavky_id ASC";
            ResultSet terminy = PripojeniDB.dotazS(dotaz);
            while (terminy.next()) {
                vrTerminy.add(new Long(terminy.getLong(1)));
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        long idObjednavka = 0;
        int index[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index2[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index3[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index4[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        int index5[][] = new int[arTO1.size()][tabulkaModelObjednavka1.getColumnCount()];
        for (int row = 0; row < arTO1.size(); row++) {
            idObjednavka = arTO1.get(row).getId();
            if (Collections.binarySearch(vrTerminy, idObjednavka) > -1) {
                index[row][10] = 1;
            }
            if (arTO1.get(row).getSkluz() > 0) {
                index[row][9] = 2;
            }
            index2[row][6] = 1;
            index3[row][14] = 1;
            index4[row][0] = 3;
            index4[row][1] = 3;
            index4[row][2] = 3;
            index4[row][3] = 3;
            index4[row][4] = 3;
            index4[row][7] = 3;
            index4[row][10] = 3;
            index4[row][11] = 3;
            index4[row][12] = 3;

            index4[row][16] = 3;

            index4[row][5] = 1;
            index4[row][6] = 1;
            index4[row][14] = 1;
            index4[row][15] = 2;

            index4[row][16] = 2;

            index5[row][16] = 1;

            // System.out.println("class : " + tabulkaModelObjednavka1.getColumnClass(19).getName());
            //}
            //tabulka.se(Object.class, new ColorCellRenderer(index,index2,index3,index4, backColor,frontColor));
            // tabulka.getDefaultRenderer(tabulkaModelObjednavka1.getColumnClass(5)).getTableCellRendererComponent(tabulka, ui, true, true, WIDTH, WIDTH);
        }
        Color backColor = new Color(255, 204, 204);
        Color frontColor = Color.BLACK;

        for (int i = 0; i
                < tabulkaModelObjednavka1.getColumnCount() - 3; i++) {
            TableColumn column = null;
            column = tabulka.getColumnModel().getColumn(i);
            column.setCellRenderer(new ColorCellRendererObj1(index, index2, index3, index4, index5, backColor, frontColor));
        }
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
            String dotaz = "INSERT INTO spolecne.terminy"
                    + "(terminy_id,terminy_objednavky_id,terminy_cislo_terminu) "
                    + "VALUES(" + terminy_id + ", " + indexObjednavky[i] + ", 1" + ")";
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            terminy_id++;
        }
        obarvitTabulku();
    }

    private void potvrditObjednavky() {
        int indexy[] = tabulka.getSelectedRows();
        long indexObjednavky[] = new long[indexy.length];
        for (int i = 0; i
                < indexy.length; i++) {
            indexObjednavky[i] = arTO1.get(indexy[i]).getId();
        }
        PotvrzeniFrame1 potvrzeni = new PotvrzeniFrame1(indexObjednavky, ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
    }

    private void odebratZTerminu() {
        int indexy[] = tabulka.getSelectedRows();
        long indexObjednavky;
        for (int i = 0; i
                < indexy.length; i++) {
            indexObjednavky = arTO1.get(i).getId();
            String dotaz1 = "DELETE FROM spolecne.terminy "
                    + "WHERE terminy_objednavky_id = " + indexObjednavky;
            String dotaz2 = "DELETE FROM spolecne.vazba_faktury_objednavky "
                    + "WHERE vazba_faktury_objednavky_objednavky_id = " + indexObjednavky;

            String dotaz3 = "DELETE FROM spolecne.vazba_lieferscheiny_objednavky "
                    + "WHERE vazba_lieferscheiny_objednavky_objednavky_id = " + indexObjednavky;
            try {
                int a = PripojeniDB.dotazIUD(dotaz1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        obarvitTabulku();
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

    public void vytvorObjednavkuExistujici() {
        ObjednavkaFrame novaObjednavka = new ObjednavkaFrame(arTO1.get(tabulka.getSelectedRow()).getId(), false, "Nová objednávka na základě stávající", "Uložit objednávku na základě stávající");
    }

    public void editObjednavka() {
        ObjednavkaFrame novaObjednavka = new ObjednavkaFrame(arTO1.get(tabulka.getSelectedRow()).getId(), true, "Upravit objednávku", "Uložit úpravu");
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
            "Potvrzení",
            "Skluz",
            "Expedice",
            "Rozměr",
            "Povrch úprava",
            "Faktury",
            "Cena/ks",
            "Měna",
            "Čas celkem",
            "Poznamka",
            "Rámcová",
            "Reklamace",
            "Virtuální"
        };

        public void pridejSadu() {
            System.out.println("pridej Sadu");

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
            if (col == 19 || col == 20) {
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
                        if (tObj1.getDatumPotvrzeni() != null) {
                            return (df.format(tObj1.getDatumPotvrzeni()));
                        } else {
                            return "";
                        }
                    }
                    case (9): {
                        if (tObj1.getSkluz() != 0 /*&& tObj1.getDatumExpedice() != null*/) {
                            String dny = " d";
                            if (tObj1.getSkluz() == 1) {
                                dny = " den";
                            } else if (tObj1.getSkluz() > 1 && tObj1.getSkluz() < 5) {
                                dny = " dny";
                            } else {
                                dny = " dni";
                            }
                            return (tObj1.getSkluz() + dny);
                        } else {
                            return "";
                        }
                    }
                    case (10): {
                        if (tObj1.getDatumExpedice() != null) {
                            return (df.format(tObj1.getDatumExpedice()));
                        } else {
                            return "";
                        }
                    }
                    case (11): {
                        return (tObj1.getMaterialRozmer());
                    }
                    case (12): {
                        return (tObj1.getPopisPovrchUprava());
                    }
                    case (13): {
                        return (tObj1.getCisloFaktury());
                    }
                    case (14): {
                        return (tObj1.getCenaKus());
                    }
                    case (15): {
                        return (tObj1.getPopisMena());
                    }
                    case (16): {
                        return (tObj1.getCasCelkem());
                    }
                    case (17): {
                        return (tObj1.getPoznamka());
                    }
                    case (18): {
                        return (tObj1.getPopisRamcovaObjednavka());
                    }
                    case (19): {
                        return (tObj1.isReklamace());
                    }
                    case (20): {
                        return (tObj1.isVirtualni());
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
                MikronIS2.indexZakaznika = ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo();
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
            }

            if (e.getActionCommand().equals("OdebratObjednavka")) {
                odebratZTerminu();
            }

            if (e.getActionCommand().equals("PotvrditObjednavky")) {
                potvrditObjednavky();
            }

            if (e.getActionCommand().equals("NovaObjednavka")) {
                ObjednavkaFrame novaObjednavka = new ObjednavkaFrame("Nová objednávka", "Uložit objednávku");
            }

            if (e.getActionCommand().equals("EditObjednavka")) {
                if (tabulka.getSelectedRow() > -1) {
                    editObjednavka();
                } else {
                    JednoducheDialogy1.warnAno(MikronIS2.ramecAplikace, "Úprava objednávky", "Nejdříve vyberte objednávku pro úpravu");
                }
            }

            if (e.getActionCommand().equals("NovaObjednavkaStavajici")) {
                if (tabulka.getSelectedRow() > -1) {
                    vytvorObjednavkuExistujici();
                } else {
                    JednoducheDialogy1.warnAno(MikronIS2.ramecAplikace, "Vytvoření objednávky", "Nejdříve vyberte vzorovou objednávku");
                }
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
        jLabel3 = new javax.swing.JLabel();
        jCBRokDodani = new javax.swing.JComboBox();
        jCBFiltrObjednavka = new javax.swing.JCheckBox();
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
        jLabel2 = new javax.swing.JLabel();
        jLSkluz = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLSkluzProcenta = new javax.swing.JLabel();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jBNovaObjednavka = new javax.swing.JButton();
        jBNovaObjednavkaStavajici = new javax.swing.JButton();
        jBEditObjednavka = new javax.swing.JButton();
        PridatDoTerminuButton1 = new javax.swing.JButton();
        OdebratButton1 = new javax.swing.JButton();
        PotvrditObjednavkuButton1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

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

        jCBFiltrObjednavka.setText("Filtr objednávky");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        JPFiltrTop.add(jCBFiltrObjednavka, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
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

        jLabel1.setText("Průměrný skluz :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel1, gridBagConstraints);

        jLPocetPolozek.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(jLPocetPolozek, gridBagConstraints);

        jLabel2.setText("Počet objednávek :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPFiltry.add(jLabel2, gridBagConstraints);

        jLSkluz.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(jLSkluz, gridBagConstraints);

        jLabel21.setText("Skluz/poč. obj.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPFiltry.add(jLabel21, gridBagConstraints);

        jLSkluzProcenta.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPFiltry.add(jLSkluzProcenta, gridBagConstraints);

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

        jBNovaObjednavka.setText("Nová objednávka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaObjednavka, gridBagConstraints);

        jBNovaObjednavkaStavajici.setText("Nová objednávka na základě stávající");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBNovaObjednavkaStavajici, gridBagConstraints);

        jBEditObjednavka.setText("Upravit objednávku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jBEditObjednavka, gridBagConstraints);

        PridatDoTerminuButton1.setText("Přidat do termínu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(PridatDoTerminuButton1, gridBagConstraints);

        OdebratButton1.setText("Odebrat z termínu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(OdebratButton1, gridBagConstraints);

        PotvrditObjednavkuButton1.setText("Potvrdit objednávku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(PotvrditObjednavkuButton1, gridBagConstraints);

        jButton1.setText("Smazat objednávku");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jButton1, gridBagConstraints);

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
    private javax.swing.JButton OdebratButton1;
    private javax.swing.JButton PotvrditObjednavkuButton1;
    private javax.swing.JButton PridatDoTerminuButton1;
    private javax.swing.JLabel SumaZakazekLabel1;
    private javax.swing.JLabel TornadoKapacitaCelkLabel1;
    private javax.swing.JLabel TornadoKapacitaLabel1;
    private javax.swing.JButton VyhledatButton1;
    private javax.swing.JButton jBEditObjednavka;
    private javax.swing.JButton jBNovaObjednavka;
    private javax.swing.JButton jBNovaObjednavkaStavajici;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCBFiltrObjednavka;
    private javax.swing.JComboBox jCBRokDodani;
    private javax.swing.JComboBox jCBZakaznik;
    private javax.swing.JLabel jLPocetPolozek;
    private javax.swing.JLabel jLSkluz;
    private javax.swing.JLabel jLSkluzProcenta;
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
