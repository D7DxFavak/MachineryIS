/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

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
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.dbtridy.TridaDodatek1;
import mikronis2.dbtridy.TridaFaktura1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.dbtridy.TridaZakaznik1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class FakturaFrame1 extends javax.swing.JFrame {

    private long[] idsObjednavky;
    private int idZakaznik;
    private int idDodaciAdresa = -1;
    private boolean uprava = false;
    private TridaFaktura1 tFak1;
    private TridaObjednavka1 tObj1;
    private TridaDodatek1 tDod1;
    private ArrayList<TridaObjednavka1> arTO1;
    private ArrayList<TridaDodatek1> arDO1;
    private TridaZakaznik1 zakaznik1;
    private RoletkaUniverzalRozsirenaModel1 RoletkaModelStaty, RoletkaModelDodaciAdresa, RoletkaModelStatyFakturace;
    private RoletkaUniverzalModel1 RoletkaModelLieferSchein;
    protected TableModel tableModelObjednavka1;
    protected tabulkaModelObjednavka1 tabulkaModelObjednavka1;
    protected ListSelectionModel lsmObjednavka1;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;

    /**
     * Creates new form FakturaFrame
     */
    public FakturaFrame1(long[] idsObjednavky, int idZakaznik) {
        this.setVisible(true);

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

        initComponents();
        initStaty();
        this.idsObjednavky = idsObjednavky;
        this.idZakaznik = idZakaznik;

        tFak1 = new TridaFaktura1();
        tFak1.setIdZakaznik(idZakaznik);
        // tLief1.setArTObj1(arTO1);
        tObj1 = new TridaObjednavka1();
        tDod1 = new TridaDodatek1();
        arTO1 = new ArrayList<TridaObjednavka1>();
        arDO1 = new ArrayList<>();
        uprava = false;

//        vsPridatObjednavka1 = new Vector();
        //  vrPridatObjednavka1 = new Vector();
        inicializaceKomponent(idZakaznik);
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaTermin1();
        jTFCisloFaktura.requestFocus();
        
        
    }

    public FakturaFrame1(TridaFaktura1 tfIn) {
        this.setVisible(true);

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);

        initComponents();
        initStaty();

        tFak1 = tfIn;
        tFak1.selectData();
        // tLief1.setArTObj1(arTO1);
        tObj1 = new TridaObjednavka1();
        tDod1 = new TridaDodatek1();
        arTO1 = new ArrayList<>();
        arDO1 = new ArrayList<>();
        uprava = true;

//        vsPridatObjednavka1 = new Vector();
        //  vrPridatObjednavka1 = new Vector();
        inicializaceKomponent();
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaFaktura1();
        jTFCisloFaktura.requestFocus();
    }

    private void initStaty() {
        RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStat.setModel(RoletkaModelStaty);

        RoletkaModelStatyFakturace = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStatDod.setModel(RoletkaModelStatyFakturace);
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
        zakaznik1.selectData(tFak1.getIdZakaznik());
        // System.out.println("Prazdne jmeno firmy : " +tFak1.getZakaznikJmenoFirmy().isEmpty());
        if (tFak1.getZakaznikJmenoFirmy().isEmpty()) {
            jTFFirma.setText(zakaznik1.getNazev());
        } else {
            jTFFirma.setText(tFak1.getZakaznikJmenoFirmy());
        }
        jTFCisloFaktura.setText(tFak1.getCisloFaktury());
        jTFBank.setText(zakaznik1.getMikronBank());
        jTFIban.setText(zakaznik1.getMikronIban());
        if (tFak1.getNazev1Radek().isEmpty() && zakaznik1.getPrvniRadek().isEmpty() == false) {
            jTFPrvniRadek.setText(zakaznik1.getPrvniRadek());
        } else {
            jTFPrvniRadek.setText(tFak1.getNazev1Radek());
        }
        if (tFak1.getNazev2Radek().isEmpty() && zakaznik1.getDruhyRadek().isEmpty() == false) {
            jTFDruhyRadek.setText(zakaznik1.getDruhyRadek());
        } else {
            jTFDruhyRadek.setText(tFak1.getNazev2Radek());
        }
        jTFKontakt.setText(tFak1.getKontakt());

        if (tFak1.getAdresa().isEmpty()) {
            jTFAdresa.setText(zakaznik1.getAdresa());

        } else {
            jTFAdresa.setText(tFak1.getAdresa());
        }
        if (tFak1.getMesto().isEmpty()) {
            jTFMesto.setText(zakaznik1.getMesto());
        } else {
            jTFMesto.setText(tFak1.getMesto());
        }
        if (tFak1.getPsc().isEmpty()) {
            jTFPSC.setText(zakaznik1.getPsc());
        } else {
            jTFPSC.setText(tFak1.getPsc());
        }
        if (tFak1.getKontaktTelefon().isEmpty()) {
            jTFTel.setText(zakaznik1.getTelefony());
        } else {
            jTFTel.setText(tFak1.getKontaktTelefon());
        }
        if (tFak1.getVatNr().isEmpty()) {
            jTFVAT.setText(zakaznik1.getVatNr());
        } else {
            jTFVAT.setText(tFak1.getVatNr());
        }

        jTFAdresaDod.setText(tFak1.getAdresaFakturace());
        jTFMestoDod.setText(tFak1.getMestoFakturace());
        jTFPSCDod.setText(tFak1.getPscFakturace());
        
        
        if(tFak1.getStatFakturace().isEmpty()) {
             RoletkaModelStatyFakturace.setDataOId(zakaznik1.getIdStat()); 
        } else {
            RoletkaModelStatyFakturace.setDataOItem(tFak1.getStatFakturace());
        }
        
        if(tFak1.getStat().isEmpty()) {
             RoletkaModelStaty.setDataOId(zakaznik1.getIdStat());
        } else {
            RoletkaModelStaty.setDataOItem(tFak1.getStat());
        }    
                    

        jTFHrubaVaha.setText(tFak1.getHrubaVaha());
        jTFCistaVaha.setText(tFak1.getCistaVaha());
        jTFCelniKod.setText(tFak1.getComCode());
        jTFNakladovyList.setText(tFak1.getCisloAwb());
        jTFPocetKusu.setText(tFak1.getPocetKusu() + "");
        jTFDopravce.setText(tFak1.getDopravce());

        if (!(tFak1.getHrubaVaha().isEmpty()) || !tFak1.getCistaVaha().isEmpty()
                || !tFak1.getComCode().isEmpty() || !tFak1.getDopravce().isEmpty()
                || !tFak1.getCisloAwb().isEmpty() || !tFak1.getComCode().isEmpty()) {
            jCBUlozitDalsiInfo.setSelected(true);
        }

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();

        RoletkaModelLieferSchein = new RoletkaUniverzalModel1(
                "SELECT lieferscheiny_id, "
                + "(lieferscheiny_cislo_lieferscheinu || ' vom ' || to_char(lieferscheiny_datum_vystaveni,'DD.MM.YYYY')) as lc "
                + "FROM spolecne.lieferscheiny "
                + "WHERE lieferscheiny_zakaznik_id = " + tFak1.getIdZakaznik() + " "
                + "ORDER BY lieferscheiny_id DESC", "V databázi nejsou zadány lieferscheiny"); // bylo ...vs_id*/
        jCBDodaciList.setModel(RoletkaModelLieferSchein);

        RoletkaModelLieferSchein.setDataOId(tFak1.getIdLieferschein());

        jTFDatumVystaveni.setText(dateFormat.format(tFak1.getDatumVystaveni()));
        jTFDatumSplatnost.setText(dateFormat.format(tFak1.getDatumSplatnosti()));
        jTFDatumZdPlneni.setText(dateFormat.format(tFak1.getDatumPlneni()));

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

        if (tFak1.getJazyk().isEmpty()) {
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
        } else {
            if (tFak1.getJazyk().equals("CZ")) {
                jCBJazyk.setSelectedItem("česky");
            } else if (tFak1.getJazyk().equals("ENBBH")) {
                jCBJazyk.setSelectedItem("anglicky BBH");
            } else if (tFak1.getJazyk().equals("EN")) {
                jCBJazyk.setSelectedItem("anglicky");
            } else {
                jCBJazyk.setSelectedItem("německy");
            }
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

        if (idZakaznik == 12) {
            jCBUlozitDalsiInfo.setSelected(true);
        }

        jTFFirma.setText(zakaznik1.getNazev());
        jTFPrvniRadek.setText(zakaznik1.getPrvniRadek());
        jTFDruhyRadek.setText(zakaznik1.getDruhyRadek());
        jTFKontakt.setText("");
        jTFAdresa.setText(zakaznik1.getAdresa());
        jTFMesto.setText(zakaznik1.getMesto());
        jTFPSC.setText(zakaznik1.getPsc());
        RoletkaModelStaty.setDataOId(zakaznik1.getIdStat());
        jTFTel.setText(zakaznik1.getTelefony());

        jTFBank.setText(zakaznik1.getMikronBank());
        jTFIban.setText(zakaznik1.getMikronIban());
        jTFVAT.setText(zakaznik1.getVatNr());

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();

        jTFDatumVystaveni.setText(dateFormat.format(date));
        jTFDatumZdPlneni.setText(dateFormat.format(date));
        jTFDatumSplatnost.setText(SQLFunkceObecne2.selectDatumPole("SELECT current_date + 30 days"));

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

        RoletkaModelLieferSchein = new RoletkaUniverzalModel1(
                "SELECT lieferscheiny_id, "
                + "(lieferscheiny_cislo_lieferscheinu || ' vom ' || to_char(lieferscheiny_datum_vystaveni,'DD.MM.YYYY')) as lc "
                + "FROM spolecne.lieferscheiny "
                + "WHERE lieferscheiny_zakaznik_id = " + idZakaznik + " "
                + "ORDER BY lieferscheiny_id DESC", "V databázi nejsou zadány lieferscheiny"); // bylo ...vs_id*/
        jCBDodaciList.setModel(RoletkaModelLieferSchein);

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
                jTFAdresaDod.setText((q.getString(2) == null) ? "" : q.getString(2));
                jTFMestoDod.setText((q.getString(3) == null) ? "" : q.getString(3));
                jTFPSCDod.setText((q.getString(4) == null) ? "" : q.getString(4));
                RoletkaModelStatyFakturace.setDataOId(q.getInt(5));
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

    private void novyDodatek() {
        TridaDodatek1 td1 = new TridaDodatek1();
        if (arTO1.size() > 0) {
            td1.setIdMena(arTO1.get(0).getIdMena());
            td1.setPopisMena(arTO1.get(0).getPopisMena());
        }
        arDO1.add(td1);
        tabulkaModelObjednavka1.oznamZmenu();

    }

    private void nactiData() {
        try {
            // tFak1.setCisloLieferschein(jTFCisloFaktura.getText().trim());
            tFak1.setDatumVystaveni(df.parse(jTFDatumVystaveni.getText().trim()));
            tFak1.setDatumPlneni(df.parse(jTFDatumZdPlneni.getText().trim()));
            tFak1.setDatumSplatnosti(df.parse(jTFDatumSplatnost.getText().trim()));
            tFak1.setCisloFaktury(jTFCisloFaktura.getText().trim());
            tFak1.setVystavitel((String) jCBVystavil.getSelectedItem());
            tFak1.setHrubaVaha(jTFHrubaVaha.getText().trim());
            tFak1.setCistaVaha(jTFCistaVaha.getText().trim());
            tFak1.setComCode(jTFCelniKod.getText().trim());
            tFak1.setCisloAwb(jTFNakladovyList.getText().trim());
            tFak1.setNazev1Radek(jTFPrvniRadek.getText().trim());
            tFak1.setNazev2Radek(jTFDruhyRadek.getText().trim());
            try {
                tFak1.setIdAdresa(((DvojiceCisloRetez) RoletkaModelDodaciAdresa.getSelectedItem()).cislo());
            } catch (Exception e) {
                tFak1.setIdAdresa(0);
            }
            tFak1.setDopravce(jTFDopravce.getText().trim());
            tFak1.setIdLieferschein(((DvojiceCisloRetez) RoletkaModelLieferSchein.getSelectedItem()).cislo());
            if (jTFPocetKusu.getText().isEmpty() == false) {
                tFak1.setPocetKusu(Integer.valueOf(jTFPocetKusu.getText().trim()));
            }
            tFak1.setStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
            tFak1.setKontakt(jTFKontakt.getText().trim());
            tFak1.setKontaktTelefon(jTFTel.getText().trim());
            tFak1.setAdresaFakturace(jTFAdresaDod.getText().trim());
            tFak1.setMestoFakturace(jTFMestoDod.getText().trim());
            tFak1.setPscFakturace(jTFPSCDod.getText().trim());
            tFak1.setStatFakturace(((DvojiceCisloRetez) RoletkaModelStatyFakturace.getSelectedItem()).toString());
            tFak1.setAdresa(jTFAdresa.getText().trim());
            tFak1.setMesto(jTFMesto.getText().trim());
            tFak1.setPsc(jTFPSC.getText().trim());
            tFak1.setStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
            tFak1.setZakaznikJmenoFirmy(jTFFirma.getText().trim());
            tFak1.setVatNr(jTFVAT.getText().trim());
            tFak1.setArTObj1(arTO1);
            tFak1.setArDodatek1(arDO1);

            if (((String) jCBJazyk.getSelectedItem()).equals("německy")) {
                tFak1.setJazyk("DE");
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky BBH")) {
                tFak1.setJazyk("ENBBH");
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
                tFak1.setJazyk("EN");
            } else if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
                tFak1.setJazyk("CZ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při zpracování faktury", "Prosím zkontrolujte hlavičku faktury");
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
            tFak1.insertData();
            tiskFakturu(false);
        } else {
            tFak1.updateData();
            tiskFakturu(false);
        }
    }

    private void fakturaUploadDB(long idFaktury) {
        // System.out.println("Updatuji doklad " + dokladCislo);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "faktura.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.faktury SET faktury_bindata = ? WHERE faktury_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, idFaktury);
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void tiskFakturu(boolean tisk) {
        if (tFak1.getIdFaktury() != -1) {
            java.text.NumberFormat nf2;
            nf2 = java.text.NumberFormat.getInstance();
            nf2.setMinimumFractionDigits(2);
            nf2.setMaximumFractionDigits(2);
            int pocetPolozek = 0;

            String reportSource = "";

            if (((String) jCBJazyk.getSelectedItem()).equals("německy")) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaDE.jrxml";
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky BBH")) {
                if (jCBCelniDeklarace.isSelected() == true) {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENDeklarace.jrxml";
                } else {
                    reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaEN.jrxml";
                }
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "fakturaENUni.jrxml";

            } else if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
                reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac+ "fakturaCZ.jrxml";
            }

            try {
                String dotaz = "";
                if (tFak1.isFixovana()) {
                    dotaz = "SELECT COUNT(faktury_polozky_fix_polozka) "
                            + "FROM spolecne.faktury_polozky_fix WHERE faktury_polozky_fix_id = " + tFak1.getIdFaktury();
                } else {
                    dotaz = "SELECT COUNT(vazba_faktury_objednavky_objednavky_id) "
                            + "FROM spolecne.vazba_faktury_objednavky WHERE vazba_faktury_objednavky_faktury_id = " + tFak1.getIdFaktury();
                }
                ResultSet q = PripojeniDB.dotazS(dotaz);
                while (q.next()) {
                    pocetPolozek = q.getInt(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("Firma : " + ((tFak1.getNazev1Radek() == null || tFak1.getNazev1Radek().isEmpty()) ? tFak1.getZakaznikJmenoFirmy() : tFak1.getNazev1Radek()));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("cislo_faktury", tFak1.getCisloFaktury());
            params.put("pocetPolozek", pocetPolozek);
            params.put("datum_vystaveni", df.format(tFak1.getDatumVystaveni()));
            params.put("datum_plneni", df.format(tFak1.getDatumPlneni())); // upravit
            params.put("datum_splatnost", df.format(tFak1.getDatumSplatnosti()));
            params.put("bankovni_ucet", jTFBank.getText().trim());
            params.put("iban", jTFIban.getText().trim());
            // params.put("cislo_lieferschein", NrLieferscheinTextField1.getText().trim());
            if (zakaznik1.getIdStat() == 2 || zakaznik1.getIdStat() == 3 || zakaznik1.getIdStat() == 6) {
                params.put("lieferschein", "Lieferschein Nr.: " + ((DvojiceCisloRetez) RoletkaModelLieferSchein.getSelectedItem()).toString());
            } else {
                params.put("lieferschein", tFak1.getCisloLieferschein());
            }
            // params.put("lieferschein", "Lieferschein Nr.: " + ((DvojiceCisloRetez) RoletkaModelLieferSchein.getSelectedItem()).toString());
            params.put("fakturu_vystavil", tFak1.getVystavitel());
            params.put("firma", ((tFak1.getNazev1Radek() == null || tFak1.getNazev1Radek().isEmpty()) ? tFak1.getZakaznikJmenoFirmy() : tFak1.getNazev1Radek()));
            params.put("firmaPokr", tFak1.getNazev2Radek());
            params.put("adresa_ulice", tFak1.getAdresa());
            if (((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo() == 4) {
                params.put("adresa_psc_mesto", tFak1.getPsc() + ", " + tFak1.getMesto());
            } else {
                params.put("adresa_psc_mesto", tFak1.getPsc() + " " + tFak1.getMesto());
            }
            params.put("adresa_stat", tFak1.getStat());

            if (tFak1.getIdZakaznik() == 6 || tFak1.getIdZakaznik() == 5) {
                params.put("telVat", "VAT-No.:");
                params.put("telefonni_cislo", zakaznik1.getVatNr());
            } else {
                params.put("telVat", "Tel.:");
                params.put("telefonni_cislo", tFak1.getKontaktTelefon());
            }
            if (tFak1.getIdZakaznik() != 12) {
                params.put("ust_id_nr", zakaznik1.getUstNr());
            }

            BigDecimal cena = new BigDecimal(0);
            for (int i = 0; i < arTO1.size(); i++) {
                cena = cena.add(new BigDecimal(arTO1.get(i).getCenaKus().replaceAll(",", ".")).multiply(new BigDecimal(arTO1.get(i).getPocetObjednanychKusu())));
            }
            for (int i = 0; i < arDO1.size(); i++) {
                cena = cena.add(new BigDecimal(arDO1.get(i).getCenaKus().replaceAll(",", ".")).multiply(new BigDecimal(arDO1.get(i).getPocet())));
            }
            params.put("celkova_cena", nf2.format(cena));
            params.put("kontakt", tFak1.getKontakt());
            
            if (jCBUlozitDalsiInfo.isSelected() == true) {
                params.put("dodAdresa1", tFak1.getAdresaFakturace());
                params.put("dodAdresa2", tFak1.getPscFakturace() + ", " + tFak1.getMestoFakturace());
                params.put("dodAdresa3", tFak1.getStatFakturace());
                
                params.put("nakl_list", tFak1.getCisloAwb());
                params.put("pocet_kusu", tFak1.getPocetKusu());
                params.put("hruba_vaha", ((tFak1.getHrubaVaha() == null) ? "---" : tFak1.getHrubaVaha()) + " kg");
                params.put("cista_vaha", ((tFak1.getCistaVaha() == null || tFak1.getCistaVaha().isEmpty()) ? "---" : tFak1.getCistaVaha()) + " kg");
                params.put("dopravce", tFak1.getDopravce());
                params.put("com_code", ((tFak1.getComCode() == null || tFak1.getCistaVaha().isEmpty()) ? "---" : tFak1.getComCode()));
            }
            try {
                ResultSet mikronF = PripojeniDB.dotazS("SELECT subjekty_trhu_nazev, "
                        + "subjekty_trhu_adresa, "
                        + "subjekty_trhu_psc || ' ' || subjekty_trhu_mesto AS adresaMesto, "
                        + "'Czech Republic', "
                        + "subjekty_trhu_telefony, "
                        + "subjekty_trhu_faxy, "
                        + "subjekty_trhu_vat_nr "
                        + "FROM spolecne.subjekty_trhu "
                        + "WHERE subjekty_trhu_id = 19");
                while (mikronF.next()) {
                    params.put("firma_mikron", mikronF.getString(1));
                    params.put("adresa_ulice_mikron", mikronF.getString(2));
                    params.put("adresa_psc_mesto_mikron", mikronF.getString(3));
                    params.put("stat_mikron", mikronF.getString(4));
                    params.put("telefon", mikronF.getString(5));
                    params.put("fax", mikronF.getString(6));
                    params.put("dic", mikronF.getString(7));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("id_faktura", tFak1.getIdFaktury());
            params.put("zakaznik_id", tFak1.getIdZakaznik());
            params.put("mena", arTO1.get(0).getPopisMena());
            if (tFak1.getVatNr().isEmpty()) {
                params.put("vatId", zakaznik1.getVatNr());
            } else {
                params.put("vatId", tFak1.getVatNr());
            }

            params.put("paymentMethod", "net 30");
            try {
                JasperReport jasperReport
                        = JasperCompileManager.compileReport(reportSource);

                JasperPrint jasperPrint1
                        = JasperFillManager.fillReport(
                                jasperReport, params, PripojeniDB.con);
                if (tisk == true) {
                    JasperPrintManager.printReport(jasperPrint1, true);
                } else {
                    JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "faktura.pdf");
                    fakturaUploadDB(tFak1.getIdFaktury());
                }

            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getDataTabulkaFaktura1() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arTO1.clear();
        arDO1.clear();
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
                    + "objednavky_cislo_objednavky, "
                    + "objednavky_cena_za_kus, "
                    + "meny_zkratka, "
                    + "objednavky_mena_id "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_faktury_objednavky "
                    + "CROSS JOIN spolecne.meny "
                    + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                    + "AND objednavky.objednavky_id = vazba_faktury_objednavky_objednavky_id "
                    + "AND meny_id = objednavky_mena_id "
                    + "AND vazba_faktury_objednavky_faktury_id = " + tFak1.getIdFaktury() + " "
                    + "ORDER BY vazba_faktury_objednavky_poradi ASC, vykresy_cislo ASC");

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
                tObj1.setCenaKus(objednavka1.getString(8).replace(".", ","));
                tObj1.setPopisMena((objednavka1.getString(9) == null) ? "" : objednavka1.getString(9));
                tObj1.setIdMena(objednavka1.getInt(10));
                tObj1.setPoradi(i);
                arTO1.add(tObj1);

                tObj1 = new TridaObjednavka1();
            }// konec while

            ResultSet q = PripojeniDB.dotazS("SELECT vazba_faktury_dodatky_faktury_id, vazba_faktury_dodatky_dodatky_id,"
                    + "vazba_faktury_dodatky_text, vazba_faktury_dodatky_pocet, vazba_faktury_dodatky_cena_za_kus, "
                    + "vazba_faktury_dodatky_mena_id, vazba_faktury_dodatky_poznamky, "
                    + "vazba_faktury_dodatky_poradi "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "WHERE vazba_faktury_dodatky_faktury_id = " + tFak1.getIdFaktury() + " "
                    + "ORDER BY vazba_faktury_dodatky_dodatky_id");
            while (q.next()) {
                tDod1 = new TridaDodatek1();
                tDod1.setIdFaktura(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tDod1.setIdDodatek(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tDod1.setText(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                tDod1.setPocet(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                tDod1.setCenaKus(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tDod1.setIdMena(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                tDod1.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                tDod1.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
                arDO1.add(tDod1);
            }
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
                        + "objednavky_cislo_objednavky, "
                        + "objednavky_cena_za_kus, "
                        + "meny_zkratka, "
                        + "objednavky_mena_id "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.terminy "
                        + "CROSS JOIN spolecne.meny "
                        + "WHERE vykresy.vykresy_id = objednavky.objednavky_cislo_vykresu "
                        + "AND objednavky.objednavky_id = terminy.terminy_objednavky_id "
                        + "AND meny_id = objednavky_mena_id "
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
                    tObj1.setCenaKus((objednavka1.getString(8) == null) ? "" : objednavka1.getString(8));
                    tObj1.setPopisMena((objednavka1.getString(9) == null) ? "" : objednavka1.getString(9));
                    tObj1.setIdMena(objednavka1.getInt(10));
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
            "Číslo objednávky",
            "Cena za kus",
            "Měna"};

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
            return arTO1.size() + arDO1.size();
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
            // if (col == 6) {
            return true;
            /* } else {
             return false;
             }*/
        }

        @Override
        public Object getValueAt(int row, int col) {
            try {
                if (row < arTO1.size()) {
                    tObj1 = arTO1.get(row);
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
                        case (6): {
                            return (tObj1.getCenaKus());
                        }
                        case (7): {
                            return (tObj1.getPopisMena());
                        }
                        default: {
                            return null;
                        }
                    }
                } else {
                    tDod1 = arDO1.get(row - arTO1.size());
                    switch (col) {
                        case (0): {
                            return (tDod1.getPoradi());
                        }
                        case (1): {
                            return (tDod1.getPocet());
                        }
                        case (2): {
                            return (tDod1.getText());
                        }
                        case (3): {
                            return ("");
                        }
                        case (4): {
                            return ("");
                        }
                        case (5): {
                            return (null);
                        }
                        case (6): {
                            return (tDod1.getCenaKus());
                        }
                        case (7): {
                            return (tObj1.getPopisMena());
                        }
                        default: {
                            return null;
                        }
                    }
                }
            }//konec try
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//konec getValueAt

        @Override
        public void setValueAt(Object value, int row, int col) {
            try {
                if (row < arTO1.size()) {
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
                } else {
                    tDod1 = arDO1.get(row - arTO1.size());
                    switch (col) {
                        case (0): {
                            tDod1.setPoradi((Integer) value);
                            break;
                        }
                        case (1): {
                            tDod1.setPocet((Integer) value);
                            break;
                        }
                        case (2): {
                            tDod1.setText((String) value);
                            break;
                        }
                        case (6): {
                            tDod1.setCenaKus((String) value);
                            break;
                        }
                        default: {

                        }
                    }

                    arDO1.set(row - arTO1.size(), tDod1);
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
        jTFCisloFaktura = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFDatumVystaveni = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jCBVystavil = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jTFFirma = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTFAdresaDod = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBDodani = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTFKontakt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTFMestoDod = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFPSCDod = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFTel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTFBank = new javax.swing.JTextField();
        jTFIban = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTFPoznamka = new javax.swing.JTextField();
        jCBStatDod = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jTFPrvniRadek = new javax.swing.JTextField();
        jTFDruhyRadek = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTFDatumZdPlneni = new javax.swing.JTextField();
        jCBDodaciList = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jCBUlozitDalsiInfo = new javax.swing.JCheckBox();
        jTFNakladovyList = new javax.swing.JTextField();
        jTFPocetKusu = new javax.swing.JTextField();
        jTFHrubaVaha = new javax.swing.JTextField();
        jTFCistaVaha = new javax.swing.JTextField();
        jTFDopravce = new javax.swing.JTextField();
        jTFCelniKod = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTFAdresa = new javax.swing.JTextField();
        jTFMesto = new javax.swing.JTextField();
        jTFPSC = new javax.swing.JTextField();
        jCBStat = new javax.swing.JComboBox();
        jCBCelniDeklarace = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        jCBJazyk = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jTFDatumSplatnost = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jTFVAT = new javax.swing.JTextField();
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
        jBPridat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Faktura");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Číslo faktury :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jTFCisloFaktura.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTFCisloFaktura.setNextFocusableComponent(jTFDatumVystaveni);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        jPanel1.add(jTFCisloFaktura, gridBagConstraints);

        jLabel2.setText("Datum splatnosti :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jTFDatumVystaveni.setNextFocusableComponent(jTFDatumZdPlneni);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTFDatumVystaveni, gridBagConstraints);

        jLabel3.setText("Vystavil :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jCBVystavil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stanislav Kaprál", "Ing. Jiří Kaprál" }));
        jCBVystavil.setName(""); // NOI18N
        jCBVystavil.setNextFocusableComponent(jTFBank);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jCBVystavil, gridBagConstraints);

        jLabel4.setText("Firma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jTFFirma.setNextFocusableComponent(jTFPrvniRadek);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFFirma, gridBagConstraints);

        jLabel5.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTFAdresaDod.setNextFocusableComponent(jTFMestoDod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFAdresaDod, gridBagConstraints);

        jLabel6.setText("Dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jCBDodani.setNextFocusableComponent(jTFAdresaDod);
        jCBDodani.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBDodaniItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jCBDodani, gridBagConstraints);

        jLabel7.setText("Kontakt :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jTFKontakt.setNextFocusableComponent(jTFTel);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFKontakt, gridBagConstraints);

        jLabel8.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        jTFMestoDod.setNextFocusableComponent(jTFPSCDod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFMestoDod, gridBagConstraints);

        jLabel9.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        jTFPSCDod.setNextFocusableComponent(jCBStatDod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFPSCDod, gridBagConstraints);

        jLabel10.setText("Tel.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel10, gridBagConstraints);

        jTFTel.setNextFocusableComponent(jTFAdresa);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFTel, gridBagConstraints);

        jLabel11.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel11, gridBagConstraints);

        jLabel12.setText("BANK :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel12, gridBagConstraints);

        jLabel13.setText("IBAN :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Dodací list :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel14, gridBagConstraints);

        jTFBank.setNextFocusableComponent(jTFIban);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFBank, gridBagConstraints);

        jTFIban.setNextFocusableComponent(jCBDodaciList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFIban, gridBagConstraints);

        jLabel15.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel15, gridBagConstraints);

        jTFPoznamka.setNextFocusableComponent(jTFNakladovyList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFPoznamka, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jCBStatDod, gridBagConstraints);

        jLabel16.setText("2. řádek:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel16, gridBagConstraints);

        jTFPrvniRadek.setNextFocusableComponent(jTFDruhyRadek);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFPrvniRadek, gridBagConstraints);

        jTFDruhyRadek.setNextFocusableComponent(jTFKontakt);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFDruhyRadek, gridBagConstraints);

        jLabel17.setText("1. řádek:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel17, gridBagConstraints);

        jLabel18.setText("Datum vystavení :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel18, gridBagConstraints);

        jTFDatumZdPlneni.setNextFocusableComponent(jTFDatumSplatnost);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFDatumZdPlneni, gridBagConstraints);

        jCBDodaciList.setNextFocusableComponent(jTFFirma);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jCBDodaciList, gridBagConstraints);

        jLabel20.setText("Hrubá váha :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel20, gridBagConstraints);

        jLabel21.setText("Nákladový list :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel21, gridBagConstraints);

        jLabel22.setText("Počet kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel22, gridBagConstraints);

        jLabel23.setText("Celní kód :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel23, gridBagConstraints);

        jLabel24.setText("Dopravce :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel24, gridBagConstraints);

        jLabel25.setText("Čistá váha :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel25, gridBagConstraints);

        jCBUlozitDalsiInfo.setSelected(true);
        jCBUlozitDalsiInfo.setText("Uložit doplňkové informace");
        jCBUlozitDalsiInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBUlozitDalsiInfoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 150);
        jPanel1.add(jCBUlozitDalsiInfo, gridBagConstraints);

        jTFNakladovyList.setNextFocusableComponent(jTFPocetKusu);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFNakladovyList, gridBagConstraints);

        jTFPocetKusu.setNextFocusableComponent(jTFHrubaVaha);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFPocetKusu, gridBagConstraints);

        jTFHrubaVaha.setNextFocusableComponent(jTFCistaVaha);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFHrubaVaha, gridBagConstraints);

        jTFCistaVaha.setNextFocusableComponent(jTFDopravce);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFCistaVaha, gridBagConstraints);

        jTFDopravce.setNextFocusableComponent(jTFCelniKod);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFDopravce, gridBagConstraints);

        jTFCelniKod.setNextFocusableComponent(jCBDodani);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel1.add(jTFCelniKod, gridBagConstraints);

        jLabel19.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel19, gridBagConstraints);

        jLabel26.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel26, gridBagConstraints);

        jLabel27.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel27, gridBagConstraints);

        jLabel28.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel28, gridBagConstraints);

        jTFAdresa.setNextFocusableComponent(jTFMesto);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFAdresa, gridBagConstraints);

        jTFMesto.setNextFocusableComponent(jTFPSC);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFMesto, gridBagConstraints);

        jTFPSC.setNextFocusableComponent(jCBStat);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFPSC, gridBagConstraints);

        jCBStat.setNextFocusableComponent(jTFPoznamka);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jCBStat, gridBagConstraints);

        jCBCelniDeklarace.setText("Přidat celní deklaraci");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jCBCelniDeklarace, gridBagConstraints);

        jLabel29.setText("Jazyk :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel29, gridBagConstraints);

        jCBJazyk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jCBJazyk, gridBagConstraints);

        jLabel30.setText("Datum zd. plnění :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel30, gridBagConstraints);

        jTFDatumSplatnost.setNextFocusableComponent(jCBVystavil);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jTFDatumSplatnost, gridBagConstraints);

        jLabel31.setText("VAT :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel31, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 30);
        jPanel1.add(jTFVAT, gridBagConstraints);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBNahoru, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
            .addComponent(jBDolu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBSmazat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBTisk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBZavrit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1))
            .addComponent(jBUlozit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(18, 18, 18)
                .addComponent(jBUlozit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBTisk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBZavrit)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jBPridat.setText("Nový řádek");
        jBPridat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPridatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jspTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBPridat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTabulka, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBPridat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
        tiskFakturu(true);
    }//GEN-LAST:event_jBTiskActionPerformed

    private void jBZavritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBZavritActionPerformed
        this.dispose();
    }//GEN-LAST:event_jBZavritActionPerformed

    private void jCBDodaniItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBDodaniItemStateChanged
        nastavDetailyDodani();
    }//GEN-LAST:event_jCBDodaniItemStateChanged

    private void jCBUlozitDalsiInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBUlozitDalsiInfoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBUlozitDalsiInfoActionPerformed

    private void jBPridatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPridatActionPerformed
        novyDodatek();
    }//GEN-LAST:event_jBPridatActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBDolu;
    private javax.swing.JButton jBNahoru;
    private javax.swing.JButton jBPridat;
    private javax.swing.JButton jBSmazat;
    private javax.swing.JButton jBTisk;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JButton jBZavrit;
    private javax.swing.JCheckBox jCBCelniDeklarace;
    private javax.swing.JComboBox jCBDodaciList;
    private javax.swing.JComboBox jCBDodani;
    private javax.swing.JComboBox jCBJazyk;
    private javax.swing.JComboBox jCBStat;
    private javax.swing.JComboBox jCBStatDod;
    private javax.swing.JCheckBox jCBUlozitDalsiInfo;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
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
    private javax.swing.JTextField jTFAdresaDod;
    private javax.swing.JTextField jTFBank;
    private javax.swing.JTextField jTFCelniKod;
    private javax.swing.JTextField jTFCisloFaktura;
    private javax.swing.JTextField jTFCistaVaha;
    private javax.swing.JTextField jTFDatumSplatnost;
    private javax.swing.JTextField jTFDatumVystaveni;
    private javax.swing.JTextField jTFDatumZdPlneni;
    private javax.swing.JTextField jTFDopravce;
    private javax.swing.JTextField jTFDruhyRadek;
    private javax.swing.JTextField jTFFirma;
    private javax.swing.JTextField jTFHrubaVaha;
    private javax.swing.JTextField jTFIban;
    private javax.swing.JTextField jTFKontakt;
    private javax.swing.JTextField jTFMesto;
    private javax.swing.JTextField jTFMestoDod;
    private javax.swing.JTextField jTFNakladovyList;
    private javax.swing.JTextField jTFPSC;
    private javax.swing.JTextField jTFPSCDod;
    private javax.swing.JTextField jTFPocetKusu;
    private javax.swing.JTextField jTFPoznamka;
    private javax.swing.JTextField jTFPrvniRadek;
    private javax.swing.JTextField jTFTel;
    private javax.swing.JTextField jTFVAT;
    private javax.swing.JScrollPane jspTabulka;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
