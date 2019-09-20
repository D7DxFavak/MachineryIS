/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.dbtridy.TridaPotvrzeni1;
import mikronis2.dbtridy.TridaPotvrzeniObjednavka1;
import mikronis2.dbtridy.TridaZakaznik1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class PotvrzeniFrame1 extends javax.swing.JFrame {

    private long[] idsObjednavky;
    private int idZakaznik;
    private long idPotvrzeni;
    private boolean uprava = false;
    private TridaZakaznik1 zakaznik1;
    private TridaPotvrzeni1 tPot1;
    private TridaPotvrzeniObjednavka1 potObj1;
    private ArrayList<TridaPotvrzeniObjednavka1> arObj1;
    protected TableModel tableModelObjednavka1;
    protected tabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected ListSelectionModel lsmObjednavka1;
    private RoletkaUniverzalRozsirenaModel1 RoletkaModelDodaciAdresa, RoletkaModelStaty;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2;

    public PotvrzeniFrame1(long[] idsObjednavky, int idZakaznik) {
        this.setVisible(true);
        this.idsObjednavky = idsObjednavky;
        this.idZakaznik = idZakaznik;

        tPot1 = new TridaPotvrzeni1();
        tPot1.setIdZakaznik(idZakaznik);

        arObj1 = new ArrayList<TridaPotvrzeniObjednavka1>();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        initComponents();
        inicializaceKomponent(idZakaznik);
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaPotvrzeni();
    }

    public PotvrzeniFrame1(TridaPotvrzeni1 tIn) {
        this.setVisible(true);

        tPot1 = tIn;
        tPot1.selectData();
        this.idPotvrzeni = tPot1.getIdPotvrzeni();
        arObj1 = new ArrayList<TridaPotvrzeniObjednavka1>();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        initComponents();
        VystavilComboBox1.removeAllItems();
        inicializaceKomponent();
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        // init faktura

        getDataTabulkaTerminUprava1();

        uprava = true;
        jBTisknout.setEnabled(true);
        this.setVisible(true);
    }

    private void inicializaceKomponent() {

        RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStat.setModel(RoletkaModelStaty);

        zakaznik1 = new TridaZakaznik1();
        zakaznik1.selectData(tPot1.getIdZakaznik());
        this.idZakaznik = tPot1.getIdZakaznik();
        inicializaceKomponent(idZakaznik);
        jTFDatumVystaveni.setText(df.format(tPot1.getDatumPotvrzeni()));
        jTFPrijemce.setText(tPot1.getPrijemce());
        jTFPredmet.setText(tPot1.getPredmet());
        jTFInfoSoupis1.setText(tPot1.getUvodVeta());
        // jTFDodatek.setText(tPot1.getDodatek());
        //  System.out.println(tPot1.getVystavitel().isEmpty() + " Vystavil : " + tPot1.getVystavitel());
        if (tPot1.getVystavitel().isEmpty() || tPot1.getVystavitel() == null) {
            VystavilComboBox1.removeAllItems();
            VystavilComboBox1.addItem("Stanislav Kaprál");
            VystavilComboBox1.addItem("Ing. Jiří Kaprál");
        } else {
            VystavilComboBox1.addItem(tPot1.getVystavitel());
        }

        if (tPot1.getAdresa().isEmpty() == false) {
            jTFAdresa.setText(tPot1.getAdresa());
        }
        if (tPot1.getMesto().isEmpty() == false) {
            jTFMesto.setText(zakaznik1.getMesto());
        }
        if (tPot1.getPsc().isEmpty() == false) {
            jTFPSC.setText(zakaznik1.getPsc());
        }
        if (tPot1.getIdStat() > 0) {
            RoletkaModelStaty.setDataOId(tPot1.getIdStat());
        }
    }

    protected void nastavParametryTabulek() {

        tabulkaModelObjednavka1 = new tabulkaModelObjednavka1();

        tabulka.setModel(tabulkaModelObjednavka1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulka.getSelectionModel();
        tableModelObjednavka1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));

    }

    protected void nastavTabulkuObjednavka1() {

        TableColumn column = null;
        column = tabulka.getColumnModel().getColumn(0);
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(1);
        column.setPreferredWidth(170);

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(125);

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(75);

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(125);

        column = tabulka.getColumnModel().getColumn(5);
        column.setPreferredWidth(60);

        column = tabulka.getColumnModel().getColumn(6);
        column.setPreferredWidth(110);

    }// konec nastavTabulkuBT1

    private void nastavDetailyAdresa() {
        try {
            if (((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo() > 0) {
                //idDodaciAdresa = ((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo();
                ResultSet q = PripojeniDB.dotazS("SELECT "
                        + "dodaci_adresy_popis, dodaci_adresy_adresa, "
                        + "dodaci_adresy_mesto, dodaci_adresy_psc, "
                        + "staty_id "
                        + "FROM spolecne.dodaci_adresy "
                        + "CROSS JOIN spolecne.staty "
                        + "WHERE staty_id = dodaci_adresy_stat_id "
                        + "AND dodaci_adresy_id = " + ((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo());
                while (q.next()) {
                    //  jTFKontakt.setText((q.getString(1) == null) ? "" : q.getString(1));
                    jTFAdresa.setText((q.getString(2) == null) ? "" : q.getString(2));
                    jTFMesto.setText((q.getString(3) == null) ? "" : q.getString(3));
                    jTFPSC.setText((q.getString(4) == null) ? "" : q.getString(4));
                    RoletkaModelStaty.setDataOId(q.getInt(5));
                }
            } else {
                jTFFirma.setText(zakaznik1.getNazev());
                jTFAdresa.setText(zakaznik1.getAdresa());
                jTFMesto.setText(zakaznik1.getMesto());
                jTFPSC.setText(zakaznik1.getPsc());
                RoletkaModelStaty.setDataOId(zakaznik1.getIdStat());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializaceKomponent(int idZakaznik) {
        this.zakaznik1 = new TridaZakaznik1(idZakaznik);

        jCBJazyk.removeAllItems();
        jCBJazyk.addItem("česky");
        jCBJazyk.addItem("německy");
        jCBJazyk.addItem("anglicky");

        if (zakaznik1.getIdStat() == 1) {
            jCBJazyk.setSelectedItem("česky");
        } else if (zakaznik1.getIdStat() == 4 || zakaznik1.getIdStat() == 7 || zakaznik1.getIdStat() == 8) {
            jCBJazyk.setSelectedItem("anglicky");
        } else if (zakaznik1.getIdStat() == 2 || zakaznik1.getIdStat() == 3 || zakaznik1.getIdStat() == 6) {
            jCBJazyk.setSelectedItem("německy");
        }

        try {

            jTFFirma.setText(zakaznik1.getNazev());
            jTFAdresa.setText(zakaznik1.getAdresa());
            jTFMesto.setText(zakaznik1.getMesto());
            jTFPSC.setText(zakaznik1.getPsc());

            if (zakaznik1.getIdStat() == 4) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();

                jTFDatumVystaveni.setText(dateFormat.format(date));
                jTFInfoSoupis1.setText("we confirm following articles:");
                jTFPredmet.setText("Purchase order confirmation ");
                jTFPrijemce.setText("Marc Franklin");
            } else if ((zakaznik1.getIdStat() == 2) || (zakaznik1.getIdStat() == 3) || (zakaznik1.getIdStat() == 6)) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();

                jTFDatumVystaveni.setText(dateFormat.format(date));
                jTFInfoSoupis1.setText("Wir bestätigen der Bestellungen, die in den letzte Woche kamen - sieht Tabelle :");
                jTFPredmet.setText("Auftragsbestätigung");
                jTFPrijemce.setText("");
                // jTFDodatek.setText("");
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();

                jTFDatumVystaveni.setText(dateFormat.format(date));
                jTFInfoSoupis1.setText("Potvrzujeme následující položky:");
                jTFPredmet.setText("Potvrzení objednávek ");
                jTFPrijemce.setText("");
            }

            VystavilComboBox1.removeAllItems();
            VystavilComboBox1.addItem("Stanislav Kaprál");
            VystavilComboBox1.addItem("Ing. Jiří Kaprál");

            RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                    "SELECT staty_id, staty_nazev FROM spolecne.staty "
                    + "ORDER BY staty_id ASC", "Vše", null,
                    "V databázi nejsou zadány státy", 0); // bylo ...vs_id
            jCBStat.setModel(RoletkaModelStaty);

            RoletkaModelStaty.setDataOId(zakaznik1.getIdStat());

            if (SQLFunkceObecne2.selectBooleanPole(
                    "SELECT EXISTS (SELECT dodaci_adresy_id FROM spolecne.dodaci_adresy WHERE "
                    + "dodaci_adresy_subjekt_trhu = " + idZakaznik + ")") == true) {
                RoletkaModelDodaciAdresa = new RoletkaUniverzalRozsirenaModel1(
                        "SELECT dodaci_adresy_id, dodaci_adresy_nazev FROM spolecne.dodaci_adresy "
                        + "WHERE dodaci_adresy_subjekt_trhu = " + idZakaznik
                        + " ORDER BY dodaci_adresy_poradi", "Výchozí adresa", null,
                        "V databázi nejsou zadány dodací adresy", 0); // bylo ...vs_id          

                RoletkaModelDodaciAdresa.setDataOIndex(0);
                jCBAdresa.setModel(RoletkaModelDodaciAdresa);

                // nastavDetailyDodani();
            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void getDataTabulkaPotvrzeni() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arObj1.clear();
        tabulkaModelObjednavka1.oznamZmenu();

        for (int k = 0; k < idsObjednavky.length; k++) {
            //nacist data
            try {
                ResultSet objednavka1 = PripojeniDB.dotazS(
                        "SELECT objednavky_datum_objednani, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky, "
                        + "objednavky_pocet_objednanych_kusu, "
                        + "objednavky_id, objednavky_cena_za_kus, "
                        + "objednavky_datum_potvrzeni "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND objednavky.objednavky_id = " + idsObjednavky[k] + " "
                        + "ORDER BY objednavky_cislo_objednavky ASC, vykresy_cislo ");
                // + "ORDER BY vykresy_cislo ASC");

                while (objednavka1.next()) {
                    potObj1 = new TridaPotvrzeniObjednavka1();
                    potObj1.setDatumObjednani(df.format(objednavka1.getDate(1)));
                    potObj1.setNazev((objednavka1.getString(2) == null) ? "" : objednavka1.getString(2));
                    potObj1.setCisloVykresu((objednavka1.getString(3) == null) ? "" : objednavka1.getString(3));
                    potObj1.setRevizeVykresu((objednavka1.getString(4) == null) ? "" : objednavka1.getString(4));
                    potObj1.setCisloObjednavky((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                    potObj1.setPocetKusu(objednavka1.getInt(6));
                    potObj1.setIdObjednavky(objednavka1.getLong(7));
                    potObj1.setCenaKus(objednavka1.getBigDecimal(8));
                    potObj1.setDatumPotvrzeni((objednavka1.getDate(9) == null) ? "" : df.format(objednavka1.getDate(9)));
                    potObj1.setPoradi(k + 1);

                    arObj1.add(potObj1);
                }// konec while
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
    } //konec getDataTabulkaObjednavka1

    private void getDataTabulkaTerminUprava1() {
        System.out.println("Potvrzeni get data uprava");
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arObj1.clear();
        tabulkaModelObjednavka1.oznamZmenu();

        try {           
            ResultSet objednavka1 = PripojeniDB.dotazS(
                    "SELECT objednavky_datum_objednani, "
                    + "objednavky_nazev_soucasti, "
                    + "vykresy_cislo, "
                    + "COALESCE(vykresy_revize, '') as revize, "
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_id, objednavky_cena_za_kus, "
                    + "objednavky_datum_potvrzeni "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_potvrzeni_objednavky "
                    + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "AND objednavky.objednavky_id = vazba_potvrzeni_objednavky_objednavky_id "
                    + "AND vazba_potvrzeni_objednavky_potvrzeni_id = " + tPot1.getIdPotvrzeni() + " "
                    + "ORDER BY objednavky_cislo_objednavky");
            //   + "ORDER BY vazba_potvrzeni_datum, vykresy_cislo, revize ASC");
            while (objednavka1.next()) {
                potObj1 = new TridaPotvrzeniObjednavka1();
                potObj1.setDatumObjednani(df.format(objednavka1.getDate(1)));
                potObj1.setNazev((objednavka1.getString(2) == null) ? "" : objednavka1.getString(2));
                potObj1.setCisloVykresu((objednavka1.getString(3) == null) ? "" : objednavka1.getString(3));
                potObj1.setRevizeVykresu((objednavka1.getString(4) == null) ? "" : objednavka1.getString(4));
                potObj1.setCisloObjednavky((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                potObj1.setPocetKusu(objednavka1.getInt(6));
                potObj1.setIdObjednavky(objednavka1.getLong(7));
                potObj1.setCenaKus(objednavka1.getBigDecimal(8));
                potObj1.setDatumPotvrzeni((objednavka1.getDate(9) == null) ? "" : df.format(objednavka1.getDate(9)));
                arObj1.add(potObj1);

            }// konec while

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

    } //konec getDataTabulkaObjednavka1

    private void nactiData() {
        try {
            tPot1.setDatumPotvrzeni(df.parse(jTFDatumVystaveni.getText()));
            tPot1.setPrijemce(jTFPrijemce.getText().trim());
            tPot1.setPredmet(jTFPredmet.getText().trim());
            tPot1.setUvodVeta(jTFInfoSoupis1.getText().trim());
            tPot1.setVystavitel((String) VystavilComboBox1.getSelectedItem());
            //tPot1.setDodatek(jTFDodatek.getText().trim());
            tPot1.setAdresa(jTFAdresa.getText().trim());
            tPot1.setMesto(jTFMesto.getText().trim());
            tPot1.setPsc(jTFPSC.getText().trim());
            tPot1.setIdStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo());
            tPot1.setArTO1(arObj1);
        } catch (Exception e) {
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání potvrzení objednávek", "Prosím zkontrolujte zadaná data "
                    + "a opravte případné chyby");
        }
    }

    private void ulozPotvrzeni() {
        nactiData();
        if (uprava == false) {
            idPotvrzeni = tPot1.insertData();
            jBTisknout.setEnabled(true);
            jBUlozit.setEnabled(false);
        } else if (uprava == true) {
            tPot1.updateData();
            jBTisknout.setEnabled(true);
            jBUlozit.setEnabled(false);
        }

        tiskPotvrzeni(false);
        potvrzeniUploadDB(this.idPotvrzeni);
        jBUlozit.setForeground(
                new Color(255, 51, 51));
        jBUlozit.setText("Uloženo");

    }

    private void potvrzeniUploadDB(long potvrzeniId) {
        // System.out.println("Updatuji doklad " + dokladCislo);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "potvrzeni.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.potvrzeni SET potvrzeni_bindata = ? WHERE potvrzeni_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, potvrzeniId);
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tiskPotvrzeni(boolean tisk) {

        String reportSource = "";
        String stat = "";
        Map<String, Object> params = new HashMap<String, Object>();
        if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
            df = java.text.DateFormat.getDateInstance();
            params.put("datum", ((jTFDatumVystaveni.getText().trim())));
             params.put("zakaznik_id_number", zakaznik1.getVatNr());
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniCZ.jrxml";
        } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
            df = java.text.DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            params.put("datum", (jTFDatumVystaveni.getText().trim()));
             params.put("zakaznik_id_number", zakaznik1.getVatNr());
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniBBHUSA.jrxml";
        } else {
            params.put("datum", ("Pilsen, den " + jTFDatumVystaveni.getText().trim()));
             params.put("zakaznik_id_number", zakaznik1.getUstNr());
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "potvrzeniDE.jrxml";
        }

        try {
            ResultSet q = PripojeniDB.dotazS("SELECT staty_nazev FROM spolecne.staty WHERE staty_id = " + zakaznik1.getIdStat());
            while (q.next()) {
                stat = q.getString(1);
            }

            params.put("prijemce", jTFPrijemce.getText().trim());
            params.put("predmet", jTFPredmet.getText().trim());
            params.put("info", jTFInfoSoupis1.getText().trim());
            //  params.put("dodatek", jTFDodatek.getText().trim()); // upravit       
            params.put("fakturu_vystavil", VystavilComboBox1.getSelectedItem().toString());
            params.put("firma", jTFFirma.getText().trim());
            params.put("adresa_ulice", jTFAdresa.getText().trim());
            params.put("adresa_psc_mesto", jTFPSC.getText().trim() + " " + jTFMesto.getText().trim());
            
             
            
           
            params.put("adresa_stat", stat);

            ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                    + "subjekty_trhu_adresa, "
                    + "'CZ - ' || subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                    + "'Czech Republic', "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_faxy, "
                    + "subjekty_trhu_e_maily "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = 19");
            while (mikronF.next()) {
                params.put("firma_mikron", mikronF.getString(1));
                params.put("adresa_ulice_mikron", mikronF.getString(2));
                params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                params.put("stat_mikron", mikronF.getString(4));
                params.put("telefon", "Tel.: " + mikronF.getString(5));
                params.put("fax", "Fax: " + mikronF.getString(6));
                params.put("email", "e-mail: " + mikronF.getString(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

         params.put("logo" , HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "Logo_mikron.png");
        params.put("potvrzeni_id", idPotvrzeni);
        params.put("zakaznik_id", idZakaznik);

        try {
            System.out.println("POT source " + reportSource);
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            //  JasperViewer.viewReport(jasperPrint1);
            if (tisk == true) {
                JasperPrintManager.printReport(jasperPrint1, true);
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "potvrzeni.pdf");
            }
            //   JasperExportManager.exportReportToPdfFile(jasperPrint1, "pruvokda.pdf");
        } catch (JRException ex) {
            ex.printStackTrace();
        }

        df = java.text.DateFormat.getDateInstance();
    }

    private void posunNahoru() {
        int indexPuv = tabulka.getSelectedRow();
        TridaPotvrzeniObjednavka1 objPuv = arObj1.get(indexPuv);
        //System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv > 0) {
            TridaPotvrzeniObjednavka1 objNova = arObj1.get(indexPuv - 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv);
            arObj1.set(indexPuv, objNova);
            arObj1.set(indexPuv - 1, objPuv);
            tabulkaModelObjednavka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv - 1, indexPuv - 1);
        }
    }

    private void posunDolu() {
        int indexPuv = tabulka.getSelectedRow();
        TridaPotvrzeniObjednavka1 objPuv = arObj1.get(indexPuv);
        // System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv < arObj1.size() - 1) {
            TridaPotvrzeniObjednavka1 objNova = arObj1.get(indexPuv + 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv + 2);
            arObj1.set(indexPuv, objNova);
            arObj1.set(indexPuv + 1, objPuv);
            tabulkaModelObjednavka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv + 1, indexPuv + 1);
        }
    }

    private void smazat() {
        int indexy[] = tabulka.getSelectedRows();
        for (int i = 0; i
                < indexy.length; i++) {
            arObj1.remove(indexy[i]);
        }
        tabulkaModelObjednavka1.fireTableDataChanged();
    }

    protected class tabulkaModelObjednavka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Datum:",
            "Název součásti:",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",
            "Ks",
            "Datum dodání",
            "Cena/ks",
            "Celkem"
        };

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arObj1.size());
            //  updateZaznamyObjednavka1();
            if (arObj1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arObj1.remove(tabulka.getSelectedRow());
            fireTableRowsDeleted(tabulka.getSelectedRow(), tabulka.getSelectedRow());
            // updateZaznamyObjednavka1();
        }//konec uberJednu

        public void oznamZmenu() {
            fireTableDataChanged();
//        if (vrObjednavka1size() > 0)
//        jTablePruvodkyPruvodky.changeSelection(0, 0, false, false);
        }//konec oznamZmenu

        public void oznamUpdateRadky() {
            fireTableRowsUpdated(tabulka.getSelectedRow(), tabulka.getSelectedRow());
        }//konec pridej

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
            return arObj1.size();
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
            if (col == 5) {
                return Integer.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaPotvrzeniObjednavka1 po) {
            try {
                arObj1.set(tabulka.getSelectedRow(), po);
                oznamUpdateRadky();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaPotvrzeniObjednavka1 po, int pozice) {
            try {
                arObj1.set(pozice, po);
                oznamUpdateRadky();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//konec nastavHodnotuNaPozici

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 6) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Object getValueAt(int row, int col) {
            try {
                potObj1 = (TridaPotvrzeniObjednavka1) arObj1.get(row);
                switch (col) {
                    case (0): {
                        return (potObj1.getDatumObjednani());
                    }
                    case (1): {
                        return (potObj1.getNazev());
                    }
                    case (2): {
                        return (potObj1.getCisloVykresu());
                    }
                    case (3): {
                        return (potObj1.getRevizeVykresu());
                    }
                    case (4): {
                        return (potObj1.getCisloObjednavky());
                    }
                    case (5): {
                        return (potObj1.getPocetKusu());
                    }
                    case (6): {
                        return (potObj1.getDatumPotvrzeni());
                    }
                    case (7): {
                        return nf2.format(potObj1.getCenaKus());
                    }
                    case (8): {
                        return nf2.format(potObj1.getCenaKus().multiply(new BigDecimal(potObj1.getPocetKusu())));
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
            try {
                potObj1 = (TridaPotvrzeniObjednavka1) arObj1.get(row);
                switch (col) {
                    case (0): {
                        potObj1.setDatumObjednani((value == null) ? "" : value.toString());
                        break;
                    }
                    case (1): {
                        potObj1.setNazev((value == null) ? "" : value.toString());
                        break;
                    }
                    case (2): {
                        potObj1.setCisloVykresu((value == null) ? "" : value.toString());
                        break;
                    }
                    case (3): {
                        potObj1.setRevizeVykresu((value == null) ? "" : value.toString());
                        break;
                    }
                    case (4): {
                        potObj1.setCisloObjednavky((value == null) ? "" : value.toString());
                        break;
                    }
                    case (5): {
                        potObj1.setPocetKusu((Integer) value);
                        break;
                    }
                    case (6): {
                        potObj1.setDatumPotvrzeni((value == null) ? "" : value.toString());
                        break;
                    }
                    default: {
                    }
                }//konec switch

                //akce po update s osetrenim chyb
                if (potObj1.getIdObjednavky() > 0) {
                    arObj1.set(row, potObj1);
                    fireTableCellUpdated(row, col);
                    //setFormData(tad1);
                }

            }//konec try
            catch (Exception e) {
                e.printStackTrace();
            }
        }//konec setValueAt
    }//konec modelu tabulky

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTFDatumVystaveni = new javax.swing.JTextField();
        jTFPredmet = new javax.swing.JTextField();
        jTFPrijemce = new javax.swing.JTextField();
        VystavilComboBox1 = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jCBJazyk = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTFInfoSoupis1 = new javax.swing.JTextArea();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jBTisknout = new javax.swing.JButton();
        jBUlozit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTFFirma = new javax.swing.JTextField();
        jTFAdresa = new javax.swing.JTextField();
        jTFMesto = new javax.swing.JTextField();
        jTFPSC = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCBAdresa = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jCBStat = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jBNahoru = new javax.swing.JButton();
        jBDolu = new javax.swing.JButton();
        jBSmazat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Potvrzení objednávek");

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Datum vystavení :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Vystavil :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Příjemce :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Předmět :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTFDatumVystaveni, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTFPredmet, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTFPrijemce, gridBagConstraints);

        VystavilComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(VystavilComboBox1, gridBagConstraints);

        jLabel29.setText("Jazyk :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel2.add(jLabel29, gridBagConstraints);

        jCBJazyk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        jPanel2.add(jCBJazyk, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel7.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel7, gridBagConstraints);

        jTFInfoSoupis1.setColumns(20);
        jTFInfoSoupis1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTFInfoSoupis1.setRows(5);
        jScrollPane1.setViewportView(jTFInfoSoupis1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane1, gridBagConstraints);

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

        jBTisknout.setText("Tisknout");
        jBTisknout.setEnabled(false);
        jBTisknout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTisknoutActionPerformed(evt);
            }
        });

        jBUlozit.setText("Uložit");
        jBUlozit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBUlozitActionPerformed(evt);
            }
        });

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel9.setText("Firma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jTFFirma, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jTFAdresa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jTFMesto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jTFPSC, gridBagConstraints);

        jLabel11.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel11, gridBagConstraints);

        jLabel12.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel12, gridBagConstraints);

        jLabel5.setText("Výběr adresy :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel4.add(jLabel5, gridBagConstraints);

        jCBAdresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBAdresaItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jCBAdresa, gridBagConstraints);

        jLabel6.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel6, gridBagConstraints);

        jCBStat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        jPanel4.add(jCBStat, gridBagConstraints);

        jBNahoru.setText("Nahorů");
        jBNahoru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNahoruActionPerformed(evt);
            }
        });

        jBDolu.setText("Dolů");
        jBDolu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDoluActionPerformed(evt);
            }
        });

        jBSmazat.setText("Smazat");
        jBSmazat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSmazatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBNahoru, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
            .addComponent(jBDolu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBSmazat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jBNahoru)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDolu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSmazat)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jBTisknout, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBUlozit, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSPTabulka, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSPTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBTisknout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBUlozit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBTisknoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTisknoutActionPerformed
        tiskPotvrzeni(true);
    }//GEN-LAST:event_jBTisknoutActionPerformed

    private void jBUlozitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBUlozitActionPerformed
        ulozPotvrzeni();
    }//GEN-LAST:event_jBUlozitActionPerformed

    private void jBNahoruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNahoruActionPerformed
        posunNahoru();
    }//GEN-LAST:event_jBNahoruActionPerformed

    private void jBDoluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDoluActionPerformed
        posunDolu();
    }//GEN-LAST:event_jBDoluActionPerformed

    private void jBSmazatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSmazatActionPerformed
        smazat();
    }//GEN-LAST:event_jBSmazatActionPerformed

    private void jCBAdresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBAdresaItemStateChanged
        nastavDetailyAdresa();
    }//GEN-LAST:event_jCBAdresaItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox VystavilComboBox1;
    private javax.swing.JButton jBDolu;
    private javax.swing.JButton jBNahoru;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisknout;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JComboBox jCBAdresa;
    private javax.swing.JComboBox jCBJazyk;
    private javax.swing.JComboBox jCBStat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFAdresa;
    private javax.swing.JTextField jTFDatumVystaveni;
    private javax.swing.JTextField jTFFirma;
    private javax.swing.JTextArea jTFInfoSoupis1;
    private javax.swing.JTextField jTFMesto;
    private javax.swing.JTextField jTFPSC;
    private javax.swing.JTextField jTFPredmet;
    private javax.swing.JTextField jTFPrijemce;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
