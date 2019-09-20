/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.dbtridy.TridaLieferschein1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.dbtridy.TridaZakaznik1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class LieferscheinFrame extends javax.swing.JFrame {

    private long[] idsObjednavky;
    private int idZakaznik;
    private int idDodaciAdresa = -1;
    private boolean uprava = false;
    private TridaLieferschein1 tLief1;
    private TridaObjednavka1 tObj1;
    private ArrayList<TridaObjednavka1> arTO1;
    private TridaZakaznik1 zakaznik1;
    private RoletkaUniverzalRozsirenaModel1 RoletkaModelStaty, RoletkaModelDodaciAdresa;
    protected TableModel tableModelObjednavka1;
    protected tabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected ListSelectionModel lsmObjednavka1;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();

    /**
     * Creates new form LieferscheinFrame
     */
    public LieferscheinFrame(long[] idsObjednavky, int idZakaznik) {
        this.setVisible(true);
        initComponents();
        initStaty();
        this.idsObjednavky = idsObjednavky;
        this.idZakaznik = idZakaznik;

        tLief1 = new TridaLieferschein1();
        tLief1.setIdZakaznik(idZakaznik);
        // tLief1.setArTObj1(arTO1);
        tObj1 = new TridaObjednavka1();
        arTO1 = new ArrayList<TridaObjednavka1>();
        uprava = false;

//        vsPridatObjednavka1 = new Vector();
        //  vrPridatObjednavka1 = new Vector();
        inicializaceKomponent(idZakaznik);
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaTermin1();
    }

    public LieferscheinFrame(TridaLieferschein1 tlIn) {
        this.setVisible(true);
        initComponents();
        initStaty();

        tLief1 = tlIn;
        tLief1.selectData();
        // tLief1.setArTObj1(arTO1);
        tObj1 = new TridaObjednavka1();
        arTO1 = new ArrayList<TridaObjednavka1>();
        uprava = true;

//        vsPridatObjednavka1 = new Vector();
        //  vrPridatObjednavka1 = new Vector();
        inicializaceKomponent();
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaLieferschein1();
    }

    private void initStaty() {
        RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStat.setModel(RoletkaModelStaty);
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
        column.setPreferredWidth(70);

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(145);

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(145);

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(145);

        column = tabulka.getColumnModel().getColumn(5);
        column.setPreferredWidth(110);

    }// konec nastavTabulkuBT1

    private void inicializaceKomponent() {

        zakaznik1 = new TridaZakaznik1();
        zakaznik1.selectData(tLief1.getIdZakaznik());
        //System.out.println("Prazdne jmeno firmy : " + tLief1.getZakaznikJmenoFirmy().isEmpty());
        if (tLief1.getZakaznikJmenoFirmy().isEmpty()) {
            jTFFirma.setText(zakaznik1.getNazev());
        } else {
            jTFFirma.setText(tLief1.getZakaznikJmenoFirmy());
        }
        jTFCisloLiefer.setText(tLief1.getCisloLieferschein());
        if (tLief1.getNazev1Radek().isEmpty() && zakaznik1.getPrvniRadek().isEmpty() == false) {
            jTFPrvniRadek.setText(zakaznik1.getPrvniRadek());
        } else {
            jTFPrvniRadek.setText(tLief1.getNazev1Radek());
        }
        if (tLief1.getNazev2Radek().isEmpty() && zakaznik1.getDruhyRadek().isEmpty() == false) {
            jTFDruhyRadek.setText(zakaznik1.getDruhyRadek());
        } else {
            jTFDruhyRadek.setText(tLief1.getNazev2Radek());
        }

        jTFKontakt.setText(tLief1.getKontaktOsoba());

        if (tLief1.getAdresa().isEmpty()) {
            jTFAdresa.setText(zakaznik1.getAdresa());

        } else {
            jTFAdresa.setText(tLief1.getAdresa());
        }
        if (tLief1.getMesto().isEmpty()) {
            jTFMesto.setText(zakaznik1.getMesto());
        } else {
            jTFMesto.setText(tLief1.getMesto());
        }
        if (tLief1.getPsc().isEmpty()) {
            jTFPSC.setText(zakaznik1.getPsc());
        } else {
            jTFPSC.setText(tLief1.getPsc());
        }
        if (tLief1.getKontaktTelefon().isEmpty()) {
            jTFTel.setText(zakaznik1.getTelefony());
        } else {
            jTFTel.setText(tLief1.getKontaktTelefon());
        }

        /*  jTFAdresa.setText(tLief1.getAdresa());
        jTFMesto.setText(tLief1.getMesto());
        jTFPSC.setText(tLief1.getPsc());*/
        RoletkaModelStaty.setDataOId(zakaznik1.getIdStat()); // TODO: domyslet co se staty...
        // jTFTel.setText(tLief1.getKontaktTelefon());
        jTFHrubaVaha.setText(tLief1.getHrubaVaha());
        jTFCistaVaha.setText(tLief1.getCistaVaha());
        jTFPoznamka.setText(tLief1.getPoznamky());
        jTFCelniKod.setText(tLief1.getComCode());

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        jTFDatumVystaveni.setText(dateFormat.format(tLief1.getDatumVystaveni()));

        if (SQLFunkceObecne2.selectBooleanPole(
                "SELECT EXISTS (SELECT dodaci_adresy_id FROM spolecne.dodaci_adresy WHERE "
                + "dodaci_adresy_subjekt_trhu = " + idZakaznik + ")") == true) {
            RoletkaModelDodaciAdresa = new RoletkaUniverzalRozsirenaModel1(
                    "SELECT dodaci_adresy_id, dodaci_adresy_nazev FROM spolecne.dodaci_adresy "
                    + "WHERE dodaci_adresy_subjekt_trhu = " + idZakaznik
                    + " ORDER BY dodaci_adresy_poradi", "Fakturační adresa", null,
                    "V databázi nejsou zadány dodací adresy", 0); // bylo ...vs_id          

            RoletkaModelDodaciAdresa.setDataOIndex(0);
            jCBDodani.setModel(RoletkaModelDodaciAdresa);

            // nastavDetailyDodani();
        }

        jCBJazyk.removeAllItems();
        jCBJazyk.addItem("česky");
        jCBJazyk.addItem("německy");
        jCBJazyk.addItem("anglicky");
        jCBJazyk.addItem("anglicky BBH");

        if (zakaznik1.getIdStat() == 1) {
            jCBJazyk.setSelectedItem("česky");
        } else if (zakaznik1.getIdStat() == 4 || zakaznik1.getIdStat() == 7) {
            if (zakaznik1.getIdZakaznik() == 12) {
                jCBJazyk.setSelectedItem("anglicky BBH");
            } else {
                jCBJazyk.setSelectedItem("anglicky");
            }
        } else if (zakaznik1.getIdStat() == 2 || zakaznik1.getIdStat() == 3 || zakaznik1.getIdStat() == 6) {
            jCBJazyk.setSelectedItem("německy");
        }

        /*
         * else { if (RoletkaModelDodaciAdresa != null) { if
         * (RoletkaModelDodaciAdresa.getSize() > 0) {
         * RoletkaModelDodaciAdresa.removeAll(); } } }
         */
    }

    private void inicializaceKomponent(int idZakaznik) {
        zakaznik1 = new TridaZakaznik1();
        zakaznik1.selectData(idZakaznik);

        jTFFirma.setText(zakaznik1.getNazev());
        jTFPrvniRadek.setText(zakaznik1.getPrvniRadek());
        jTFDruhyRadek.setText(zakaznik1.getDruhyRadek());
        jTFKontakt.setText("");
        jTFAdresa.setText(zakaznik1.getAdresa());
        jTFMesto.setText(zakaznik1.getMesto());
        jTFPSC.setText(zakaznik1.getPsc());
        RoletkaModelStaty.setDataOId(zakaznik1.getIdStat());
        jTFTel.setText(zakaznik1.getTelefony());

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();

        jTFDatumVystaveni.setText(dateFormat.format(date));

        if (SQLFunkceObecne2.selectBooleanPole(
                "SELECT EXISTS (SELECT dodaci_adresy_id FROM spolecne.dodaci_adresy WHERE "
                + "dodaci_adresy_subjekt_trhu = " + idZakaznik + ")") == true) {
            RoletkaModelDodaciAdresa = new RoletkaUniverzalRozsirenaModel1(
                    "SELECT dodaci_adresy_id, dodaci_adresy_nazev FROM spolecne.dodaci_adresy "
                    + "WHERE dodaci_adresy_subjekt_trhu = " + idZakaznik
                    + " ORDER BY dodaci_adresy_poradi", "Fakturační adresa", null,
                    "V databázi nejsou zadány dodací adresy", 0); // bylo ...vs_id          

            RoletkaModelDodaciAdresa.setDataOIndex(0);
            jCBDodani.setModel(RoletkaModelDodaciAdresa);

            nastavDetailyDodani();

        }

        jCBJazyk.removeAllItems();
        jCBJazyk.addItem("česky");
        jCBJazyk.addItem("německy");
        jCBJazyk.addItem("anglicky");
        jCBJazyk.addItem("anglicky BBH");

        if (zakaznik1.getIdStat() == 1) {
            jCBJazyk.setSelectedItem("česky");
            jTFPoznamka.setText("Datum dodání: ");
        } else if (zakaznik1.getIdStat() == 4 || zakaznik1.getIdStat() == 7) {
            if (zakaznik1.getIdZakaznik() == 12) {
                jCBJazyk.setSelectedItem("anglicky BBH");
            } else {
                jCBJazyk.setSelectedItem("anglicky");
            }
        } else if (zakaznik1.getIdStat() == 2 || zakaznik1.getIdStat() == 3 || zakaznik1.getIdStat() == 6) {
            jCBJazyk.setSelectedItem("německy");
        }

        /*
         * else { if (RoletkaModelDodaciAdresa != null) { if
         * (RoletkaModelDodaciAdresa.getSize() > 0) {
         * RoletkaModelDodaciAdresa.removeAll(); } } }
         */
    }

    private void nastavDetailyDodani() {
        try {
            idDodaciAdresa = ((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo();
            ResultSet q = PripojeniDB.dotazS("SELECT "
                    + "dodaci_adresy_popis, dodaci_adresy_adresa, "
                    + "dodaci_adresy_mesto, dodaci_adresy_psc, "
                    + "staty_id "
                    + "FROM spolecne.dodaci_adresy "
                    + "CROSS JOIN spolecne.staty "
                    + "WHERE staty_id = dodaci_adresy_stat_id "
                    + "AND dodaci_adresy_id = " + ((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo());
            while (q.next()) {
                jTFKontakt.setText((q.getString(1) == null) ? "" : q.getString(1));
                jTFAdresa.setText((q.getString(2) == null) ? "" : q.getString(2));
                jTFMesto.setText((q.getString(3) == null) ? "" : q.getString(3));
                jTFPSC.setText((q.getString(4) == null) ? "" : q.getString(4));
                RoletkaModelStaty.setDataOId(q.getInt(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void posunNahoru() {
        int indexPuv = tabulka.getSelectedRow();
        TridaObjednavka1 objPuv = arTO1.get(indexPuv);
        //System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv > 0) {
            TridaObjednavka1 objNova = arTO1.get(indexPuv - 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv);
            arTO1.set(indexPuv, objNova);
            arTO1.set(indexPuv - 1, objPuv);
            tabulkaModelObjednavka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv - 1, indexPuv - 1);
        }
    }

    private void posunDolu() {
        int indexPuv = tabulka.getSelectedRow();
        TridaObjednavka1 objPuv = arTO1.get(indexPuv);
        // System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv < arTO1.size() - 1) {
            TridaObjednavka1 objNova = arTO1.get(indexPuv + 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv + 2);
            arTO1.set(indexPuv, objNova);
            arTO1.set(indexPuv + 1, objPuv);
            tabulkaModelObjednavka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv + 1, indexPuv + 1);
        }
    }

    private void nactiData() {
        try {
            tLief1.setCisloLieferschein(jTFCisloLiefer.getText().trim());
            tLief1.setDatumVystaveni(df.parse(jTFDatumVystaveni.getText().trim()));
            tLief1.setVystavitel((String) jCBVystavil.getSelectedItem());
            tLief1.setHrubaVaha(jTFHrubaVaha.getText().trim());
            tLief1.setCistaVaha(jTFCistaVaha.getText().trim());
            tLief1.setComCode(jTFCelniKod.getText().trim());
            tLief1.setNazev1Radek(jTFPrvniRadek.getText().trim());
            tLief1.setNazev2Radek(jTFDruhyRadek.getText().trim());
            try {
                tLief1.setIdAdresa(((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo());
            } catch (Exception e) {
                tLief1.setIdAdresa(0);
            }

            tLief1.setStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
            tLief1.setKontaktOsoba(jTFKontakt.getText().trim());
            tLief1.setKontaktTelefon(jTFTel.getText().trim());
            tLief1.setAdresa(jTFAdresa.getText().trim());
            tLief1.setMesto(jTFMesto.getText().trim());
            tLief1.setPsc(jTFPSC.getText().trim());
            tLief1.setZakaznikJmenoFirmy(jTFFirma.getText().trim());
            tLief1.setPoznamky(jTFPoznamka.getText().trim());
            tLief1.setArTObj1(arTO1);

        } catch (ParseException e) {
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při zpracování dodacího listu", "Prosím zkontrolujte hlavičku dodacího listu");
        }
    }

    private void smazat() {
        int indexy[] = tabulka.getSelectedRows();
        for (int i = 0; i
                < indexy.length; i++) {
            arTO1.remove(indexy[i]);
        }
        tabulkaModelObjednavka1.fireTableDataChanged();
    }

    private void ulozit() {

        if (uprava == false) {
            tLief1.insertData();
            tiskLieferscheinu(false);
        } else {
            tLief1.updateData();
            tiskLieferscheinu(false);
        }
    }

    private void tiskLieferscheinu(boolean tisk) {
        if (tLief1.getIdLieferschein() != -1) {
            String reportSource = "";
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Map<String, Object> params = new HashMap<String, Object>();

            if (((String) jCBJazyk.getSelectedItem()).equals("německy")) {
                params.put("lieferschein", "L I E F E R S C H E I N - Nr.: " + tLief1.getCisloLieferschein());
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinDE.jrxml";
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky BBH")) {
                params.put("lieferschein", tLief1.getCisloLieferschein());
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinEN.jrxml";
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
                params.put("lieferschein", tLief1.getCisloLieferschein());
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinENUni.jrxml";

            } else if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
                params.put("lieferschein", tLief1.getCisloLieferschein());
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinCZ.jrxml";
            }

            if (tLief1.getNazev1Radek().isEmpty()) {
                params.put("firma", SQLFunkceObecne2.selectStringPole("SELECT subjekty_trhu_nazev "
                        + "FROM spolecne.subjekty_trhu "
                        + "WHERE subjekty_trhu_id = " + tLief1.getIdZakaznik()));
            } else {
                params.put("firma", tLief1.getNazev1Radek());
                params.put("firmaPokr", tLief1.getNazev2Radek());
            }

            params.put("adresa_ulice", tLief1.getAdresa());
            params.put("adresa_psc_mesto", tLief1.getPsc() + " " + tLief1.getMesto());

            // if (((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo() == 4) {
            params.put("adresa_stat", ((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
            params.put("telefonni_cislo", tLief1.getKontaktTelefon());
            params.put("kontakt", tLief1.getKontaktOsoba());
            //}

            params.put("datum_vystaveni", dateFormat.format(tLief1.getDatumVystaveni()) + ""); // upravit
            params.put("vystavil", (String) jCBVystavil.getSelectedItem());
            params.put("poznamky", tLief1.getPoznamky());

            if (tLief1.getIdZakaznik() == 12) {
                try {
                    params.put("hruba_vaha", ((jTFHrubaVaha.getText().trim().isEmpty()) ? "---" : jTFHrubaVaha.getText().trim()) + " kg");
                    params.put("cista_vaha", ((jTFCistaVaha.getText().trim().isEmpty()) ? "---" : jTFCistaVaha.getText().trim()) + " kg");
                    params.put("com_code", ((jTFCelniKod.getText().trim().isEmpty()) ? "---" : jTFCelniKod.getText().trim()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }

            try {
                ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                        + "subjekty_trhu_adresa, "
                        + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                        + "'Czech Republic', "
                        + "subjekty_trhu_telefony, "
                        + "subjekty_trhu_faxy "
                        + "FROM spolecne.subjekty_trhu "
                        + "WHERE subjekty_trhu_id = 19");
                while (mikronF.next()) {
                    params.put("firma_mikron", mikronF.getString(1));
                    params.put("adresa_ulice_mikron", mikronF.getString(2));
                    params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                    params.put("stat_mikron", mikronF.getString(4));

                    params.put("telefon", mikronF.getString(5));
                    params.put("fax", mikronF.getString(6));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("lieferschein_id", tLief1.getIdLieferschein());
            params.put("pocetPolozek", arTO1.size());

            try {
                JasperReport jasperReport
                        = JasperCompileManager.compileReport(reportSource);
                JasperPrint jasperPrint1
                        = JasperFillManager.fillReport(
                                jasperReport, params, PripojeniDB.con);
                //  JasperViewer.viewReport(jasperPrint1);
                if (tisk == true) {
                    JasperPrintManager.printReport(jasperPrint1, true);
                } else {
                    JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "lieferschein.pdf");
                    lieferscheinUploadDB(tLief1.getIdLieferschein());
                }

            } catch (JRException ex) {
                ex.printStackTrace();
            }

        } else {
            JednoducheDialogy1.errAno(this, "Chyba při ukládání lieferscheinu", "Lieferschein nelze uložit, zkontrolujte prosím data pro lieferschein.");
        }
    }

    private void tiskLieferscheinuExpedice() {
        if (tLief1.getIdLieferschein() != -1) {
            String reportSource = "";
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Map<String, Object> params = new HashMap<String, Object>();

            params.put("lieferschein", tLief1.getCisloLieferschein());
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "lieferscheinExpedice.jrxml";
            
            if (tLief1.getNazev1Radek().isEmpty()) {
                params.put("firma", SQLFunkceObecne2.selectStringPole("SELECT subjekty_trhu_nazev "
                        + "FROM spolecne.subjekty_trhu "
                        + "WHERE subjekty_trhu_id = " + tLief1.getIdZakaznik()));
            } else {
                params.put("firma", tLief1.getNazev1Radek());
                params.put("firmaPokr", tLief1.getNazev2Radek());
            }

            params.put("adresa_ulice", tLief1.getAdresa());
            params.put("adresa_psc_mesto", tLief1.getPsc() + " " + tLief1.getMesto());

            // if (((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo() == 4) {
            params.put("adresa_stat", ((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
            params.put("telefonni_cislo", tLief1.getKontaktTelefon());
            params.put("kontakt", tLief1.getKontaktOsoba());
            //}

            params.put("datum_vystaveni", dateFormat.format(tLief1.getDatumVystaveni()) + ""); // upravit
            params.put("vystavil", (String) jCBVystavil.getSelectedItem());
            params.put("poznamky", tLief1.getPoznamky());

            /*if (tLief1.getIdZakaznik() == 12) {
                try {
                    params.put("hruba_vaha", ((jTFHrubaVaha.getText().trim().isEmpty()) ? "---" : jTFHrubaVaha.getText().trim()) + " kg");
                    params.put("cista_vaha", ((jTFCistaVaha.getText().trim().isEmpty()) ? "---" : jTFCistaVaha.getText().trim()) + " kg");
                    params.put("com_code", ((jTFCelniKod.getText().trim().isEmpty()) ? "---" : jTFCelniKod.getText().trim()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }*/

            try {
                ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                        + "subjekty_trhu_adresa, "
                        + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                        + "'Czech Republic', "
                        + "subjekty_trhu_telefony, "
                        + "subjekty_trhu_faxy "
                        + "FROM spolecne.subjekty_trhu "
                        + "WHERE subjekty_trhu_id = 19");
                while (mikronF.next()) {
                    params.put("firma_mikron", mikronF.getString(1));
                    params.put("adresa_ulice_mikron", mikronF.getString(2));
                    params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                    params.put("stat_mikron", mikronF.getString(4));

                    params.put("telefon", mikronF.getString(5));
                    params.put("fax", mikronF.getString(6));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("lieferschein_id", tLief1.getIdLieferschein());
            params.put("pocetPolozek", arTO1.size());

            try {
                JasperReport jasperReport
                        = JasperCompileManager.compileReport(reportSource);
                JasperPrint jasperPrint1
                        = JasperFillManager.fillReport(
                                jasperReport, params, PripojeniDB.con);
                //  JasperViewer.viewReport(jasperPrint1);
                
                    JasperPrintManager.printReport(jasperPrint1, true);                

            } catch (JRException ex) {
                ex.printStackTrace();
            }

        } else {
            JednoducheDialogy1.errAno(this, "Chyba při ukládání lieferscheinu", "Lieferschein nelze uložit, zkontrolujte prosím data pro lieferschein.");
        }
    }

    private void lieferscheinUploadDB(long lieferId) {
        //System.out.println("Updatuji liefer " + lieferId);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "lieferschein.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.lieferscheiny SET lieferscheiny_bindata = ? WHERE lieferscheiny_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, lieferId);
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getDataTabulkaLieferschein1() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();
        tabulkaModelObjednavka1.oznamZmenu();
        int i = 0;
        //nacist data
        try {
            ResultSet objednavka1 = PripojeniDB.dotazS(
                    "SELECT objednavky_id , "
                    + "objednavky_pocet_objednanych_kusu, "
                    + "objednavky_nazev_soucasti, "
                    + "vykresy_id, "
                    + "vykresy_cislo, "
                    + "vykresy_revize, "
                    + "objednavky_cislo_objednavky "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_lieferscheiny_objednavky "
                    + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "AND objednavky.objednavky_id = vazba_lieferscheiny_objednavky_objednavky_id "
                    + "AND vazba_lieferscheiny_objednavky_lieferscheiny_id = " + tLief1.getIdLieferschein() + " "
                    + "ORDER BY vazba_lieferscheiny_objednavky_poradi ASC, vykresy_cislo ASC");

            while (objednavka1.next()) {
                tObj1 = new TridaObjednavka1();
                i++;
                tObj1.setId(new Long(objednavka1.getLong(1)));
                tObj1.setPocetObjednanychKusu(objednavka1.getInt(2));
                tObj1.setNazevSoucasti((objednavka1.getString(3) == null) ? "" : objednavka1.getString(3));
                tObj1.setIdVykres(objednavka1.getInt(4));
                TridaVykres1 tv1 = new TridaVykres1();
                tv1.setIdVykres(objednavka1.getInt(4));
                tv1.setCislo((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                tv1.setRevize((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                tObj1.setTv1(tv1);
                tObj1.setCisloObjednavky((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                tObj1.setPoradi(i);
                arTO1.add(tObj1);

                tObj1 = new TridaObjednavka1();
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

    } //konec getDataTabulkaObjednavka1

    private void getDataTabulkaTermin1() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();
        tabulkaModelObjednavka1.oznamZmenu();
        int i = 0;
        for (int k = 0; k < idsObjednavky.length; k++) {
            //nacist data
            try {
                ResultSet objednavka1 = PripojeniDB.dotazS(
                        "SELECT objednavky_id , "
                        + "objednavky_pocet_objednanych_kusu, "
                        + "objednavky_nazev_soucasti, "
                        + "vykresy_id, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "objednavky_cislo_objednavky "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.terminy "
                        + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND objednavky.objednavky_id = terminy.terminy_objednavky_id "
                        + "AND (terminy.terminy_cislo_terminu = 2 OR terminy.terminy_cislo_terminu = 3) "
                        + "AND objednavky.objednavky_id = " + idsObjednavky[k] + " "
                        + "ORDER BY vykresy_cislo ASC");

                while (objednavka1.next()) {
                    tObj1 = new TridaObjednavka1();
                    i++;
                    tObj1.setId(new Long(objednavka1.getLong(1)));
                    tObj1.setPocetObjednanychKusu(objednavka1.getInt(2));
                    tObj1.setNazevSoucasti((objednavka1.getString(3) == null) ? "" : objednavka1.getString(3));
                    tObj1.setIdVykres(objednavka1.getInt(4));
                    TridaVykres1 tv1 = new TridaVykres1();
                    tv1.setIdVykres(objednavka1.getInt(4));
                    tv1.setCislo((objednavka1.getString(5) == null) ? "" : objednavka1.getString(5));
                    tv1.setRevize((objednavka1.getString(6) == null) ? "" : objednavka1.getString(6));
                    tObj1.setTv1(tv1);
                    tObj1.setCisloObjednavky((objednavka1.getString(7) == null) ? "" : objednavka1.getString(7));
                    tObj1.setPoradi(i);
                    arTO1.add(tObj1);

                    tObj1 = new TridaObjednavka1();
                }// konec while
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
    } //konec getDataTabulkaObjednavka1

    protected class tabulkaModelObjednavka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Poz.",
            "Množství:",
            "Název:",
            "Číslo výkresu",
            "Revize",
            "Číslo objednávky",};

        public void pridejSadu() {
            //System.out.println("pridej Sadu");

            fireTableRowsInserted(0, arTO1.size());
            //  updateZaznamyObjednavka1();
            if (arTO1.size() > 0) {
                tabulka.changeSelection(0, 0, false, false);
            }
        }//konec pridej

        public void uberJednu() {
            arTO1.remove(tabulka.getSelectedRow());
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
            if (col == 5) {
                return Integer.class;
            } else {
                return String.class;
            }
        }

        public boolean nastavHodnotuNaVybrane(TridaObjednavka1 po) {
            try {
                arTO1.set(tabulka.getSelectedRow(), po);
                oznamUpdateRadky();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaObjednavka1 po, int pozice) {
            try {
                arTO1.set(pozice, po);
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
                tObj1 = (TridaObjednavka1) arTO1.get(row);
                switch (col) {
                    case (0): {
                        return (tObj1.getPoradi());
                    }
                    case (1): {
                        return (tObj1.getPocetObjednanychKusu());
                    }
                    case (2): {
                        return (tObj1.getNazevSoucasti());
                    }
                    case (3): {
                        return (tObj1.getTv1().getCislo());
                    }
                    case (4): {
                        return (tObj1.getTv1().getRevize());
                    }
                    case (5): {
                        return (tObj1.getCisloObjednavky());
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
                tObj1 = (TridaObjednavka1) arTO1.get(row);
                switch (col) {
                    case (0): {
                        tObj1.setPoradi((Integer) value);
                        break;
                    }
                    default: {
                    }
                }//konec switch

                //akce po update s osetrenim chyb
                if (tObj1.getId() > 0) {
                    arTO1.set(row, tObj1);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFCisloLiefer = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFDatumVystaveni = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jCBVystavil = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTFFirma = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTFAdresa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBDodani = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTFKontakt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTFMesto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFPSC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFTel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTFHrubaVaha = new javax.swing.JTextField();
        jTFCistaVaha = new javax.swing.JTextField();
        jTFCelniKod = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTFPoznamka = new javax.swing.JTextField();
        jCBStat = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jTFPrvniRadek = new javax.swing.JTextField();
        jTFDruhyRadek = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jCBJazyk = new javax.swing.JComboBox();
        jspTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jBNahoru = new javax.swing.JButton();
        jBDolu = new javax.swing.JButton();
        jBSmazat = new javax.swing.JButton();
        jBUlozit = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jBTisk = new javax.swing.JButton();
        jBZavrit = new javax.swing.JButton();
        jBTiskExpedice = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dodací list");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Číslo dodacího listu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jTFCisloLiefer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTFCisloLiefer.setNextFocusableComponent(jTFFirma);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
        jPanel1.add(jTFCisloLiefer, gridBagConstraints);

        jLabel2.setText("Datum vystavení :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jTFDatumVystaveni.setNextFocusableComponent(jCBVystavil);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFDatumVystaveni, gridBagConstraints);

        jLabel3.setText("Vystavil :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jCBVystavil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stanislav Kaprál", "Ing. Jiří Kaprál" }));
        jCBVystavil.setNextFocusableComponent(jTFHrubaVaha);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jCBVystavil, gridBagConstraints);

        jLabel4.setText("Firma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jTFFirma.setNextFocusableComponent(jTFDatumVystaveni);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        jPanel1.add(jTFFirma, gridBagConstraints);

        jLabel5.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTFAdresa.setNextFocusableComponent(jTFMesto);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFAdresa, gridBagConstraints);

        jLabel6.setText("Dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jCBDodani.setNextFocusableComponent(jTFKontakt);
        jCBDodani.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBDodaniItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jCBDodani, gridBagConstraints);

        jLabel7.setText("Kontakt :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jTFKontakt.setNextFocusableComponent(jTFAdresa);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFKontakt, gridBagConstraints);

        jLabel8.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        jTFMesto.setNextFocusableComponent(jTFPSC);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFMesto, gridBagConstraints);

        jLabel9.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        jTFPSC.setNextFocusableComponent(jCBStat);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFPSC, gridBagConstraints);

        jLabel10.setText("Tel.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel10, gridBagConstraints);

        jTFTel.setNextFocusableComponent(jTFPoznamka);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFTel, gridBagConstraints);

        jLabel11.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Hrubá váha :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Čistá váha :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Celní kód :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel14, gridBagConstraints);

        jTFHrubaVaha.setNextFocusableComponent(jTFCistaVaha);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFHrubaVaha, gridBagConstraints);

        jTFCistaVaha.setNextFocusableComponent(jTFCelniKod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFCistaVaha, gridBagConstraints);

        jTFCelniKod.setNextFocusableComponent(jTFPrvniRadek);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFCelniKod, gridBagConstraints);

        jLabel15.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel15, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFPoznamka, gridBagConstraints);

        jCBStat.setNextFocusableComponent(jTFTel);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jCBStat, gridBagConstraints);

        jLabel16.setText("2. řádek:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jLabel16, gridBagConstraints);

        jTFPrvniRadek.setNextFocusableComponent(jTFDruhyRadek);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFPrvniRadek, gridBagConstraints);

        jTFDruhyRadek.setNextFocusableComponent(jCBDodani);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 180);
        jPanel1.add(jTFDruhyRadek, gridBagConstraints);

        jLabel17.setText("1. řádek:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jLabel17, gridBagConstraints);

        jLabel18.setText("Jazyk :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel18, gridBagConstraints);

        jCBJazyk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jCBJazyk, gridBagConstraints);

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
        jspTabulka.setViewportView(tabulka);

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

        jBUlozit.setText("Uložit");
        jBUlozit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBUlozitActionPerformed(evt);
            }
        });

        jBTisk.setText("Tisk");
        jBTisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTiskActionPerformed(evt);
            }
        });

        jBZavrit.setText("Zavřít");
        jBZavrit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBZavritActionPerformed(evt);
            }
        });

        jBTiskExpedice.setText("Tisk expedice");
        jBTiskExpedice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTiskExpediceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBNahoru, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
            .addComponent(jBDolu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBSmazat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBUlozit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(jBTisk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBZavrit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBTiskExpedice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jBNahoru)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDolu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSmazat)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBUlozit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBTisk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBTiskExpedice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBZavrit)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTabulka)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBNahoruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNahoruActionPerformed
        posunNahoru();
    }//GEN-LAST:event_jBNahoruActionPerformed

    private void jBDoluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDoluActionPerformed
        posunDolu();
    }//GEN-LAST:event_jBDoluActionPerformed

    private void jBSmazatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSmazatActionPerformed
        smazat();
    }//GEN-LAST:event_jBSmazatActionPerformed

    private void jBUlozitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBUlozitActionPerformed
        nactiData();
        ulozit();

    }//GEN-LAST:event_jBUlozitActionPerformed

    private void jBTiskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTiskActionPerformed
        tiskLieferscheinu(true);
    }//GEN-LAST:event_jBTiskActionPerformed

    private void jBZavritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBZavritActionPerformed
        this.dispose();
    }//GEN-LAST:event_jBZavritActionPerformed

    private void jCBDodaniItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBDodaniItemStateChanged
        nastavDetailyDodani();
    }//GEN-LAST:event_jCBDodaniItemStateChanged

    private void jBTiskExpediceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTiskExpediceActionPerformed
        tiskLieferscheinuExpedice();
    }//GEN-LAST:event_jBTiskExpediceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBDolu;
    private javax.swing.JButton jBNahoru;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisk;
    private javax.swing.JButton jBTiskExpedice;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JButton jBZavrit;
    private javax.swing.JComboBox jCBDodani;
    private javax.swing.JComboBox jCBJazyk;
    private javax.swing.JComboBox jCBStat;
    private javax.swing.JComboBox jCBVystavil;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTFAdresa;
    private javax.swing.JTextField jTFCelniKod;
    private javax.swing.JTextField jTFCisloLiefer;
    private javax.swing.JTextField jTFCistaVaha;
    private javax.swing.JTextField jTFDatumVystaveni;
    private javax.swing.JTextField jTFDruhyRadek;
    private javax.swing.JTextField jTFFirma;
    private javax.swing.JTextField jTFHrubaVaha;
    private javax.swing.JTextField jTFKontakt;
    private javax.swing.JTextField jTFMesto;
    private javax.swing.JTextField jTFPSC;
    private javax.swing.JTextField jTFPoznamka;
    private javax.swing.JTextField jTFPrvniRadek;
    private javax.swing.JTextField jTFTel;
    private javax.swing.JScrollPane jspTabulka;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
