/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaNabidka1;
import mikronis2.dbtridy.TridaSeznamNabidek1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.dbtridy.TridaZakaznik1;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author Favak
 */
public class SeznamNabidekFrame1 extends javax.swing.JFrame {

    private long[] idNabidky;
    private int idZakaznik;
    private long idSeznam;
    private boolean uprava = false;
    private TridaNabidka1 tNab1;
    private TridaSeznamNabidek1 tSeznam1;
    private ArrayList<TridaNabidka1> arNab1;
    private TridaZakaznik1 zakaznik1;
    protected TableModel tableModelNabidka1;
    protected tabulkaModelObjednavka1 tabulkaModelNabidka1;
    protected ListSelectionModel lsmObjednavka1;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2;

    public SeznamNabidekFrame1(long[] idNabidky, int idZakaznik) {
        this.setVisible(true);
        this.idNabidky = idNabidky;
        this.idZakaznik = idZakaznik;

        arNab1 = new ArrayList<TridaNabidka1>();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        initComponents();
        inicializaceKomponent(idZakaznik);
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        getDataTabulkaSeznam();
    }

    public SeznamNabidekFrame1(TridaSeznamNabidek1 tSeznam) {
        this.setVisible(true);
        this.tSeznam1 = tSeznam;
        this.idSeznam = tSeznam.getIdSeznam();
        arNab1 = new ArrayList<TridaNabidka1>();

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        initComponents();
        nastavParametryTabulek();
        nastavTabulkuObjednavka1();
        // init faktura
        VystavilComboBox1.removeAllItems();

        try {
            ResultSet seznamInfo = PripojeniDB.dotazS(
                    "SELECT seznam_nabidek_zakaznik_id, "
                    + "seznam_nabidek_datum, "
                    + "seznam_nabidek_komu, "
                    + "seznam_nabidek_predmet, "
                    + "seznam_nabidek_veta, "
                    + "seznam_nabidek_vystavil "
                    + "FROM spolecne.seznam_nabidek "
                    + "WHERE seznam_nabidek_id = " + tSeznam.getIdSeznam() + " "
                    + "ORDER BY seznam_nabidek_datum ASC");

            while (seznamInfo.next()) {
                this.idZakaznik = ((Integer) seznamInfo.getInt(1)).intValue();
                inicializaceKomponent(idZakaznik);
                DatumVystaveniTextField1.setText((seznamInfo.getDate(2) == null) ? "" : df.format(seznamInfo.getDate(2))); // datum vystaveni
                PrijemceTextField1.setText((String) seznamInfo.getString(3));
                PredmetTextField1.setText((String) seznamInfo.getString(4));
                jTPoznamka.setText((String) seznamInfo.getString(5));

                VystavilComboBox1.removeAllItems();
                VystavilComboBox1.addItem((String) seznamInfo.getString(6));
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

        getDataTabulkaTerminUprava1();

        uprava = true;
        TiskButton.setEnabled(true);
        this.setVisible(true);
    }

    protected void nastavParametryTabulek() {

        tabulkaModelNabidka1 = new tabulkaModelObjednavka1();

        tabulka.setModel(tabulkaModelNabidka1);
        tabulka.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabulka.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        lsmObjednavka1 = tabulka.getSelectionModel();
        tableModelNabidka1 = tabulka.getModel();

        tabulka.setPreferredScrollableViewportSize(new Dimension(800, 300));

    }

    protected void nastavTabulkuObjednavka1() {

        TableColumn column = null;
        column = tabulka.getColumnModel().getColumn(0);
        column.setPreferredWidth(79);

        column = tabulka.getColumnModel().getColumn(1);
        column.setPreferredWidth(160);

        column = tabulka.getColumnModel().getColumn(2);
        column.setPreferredWidth(160);

        column = tabulka.getColumnModel().getColumn(3);
        column.setPreferredWidth(150);

        column = tabulka.getColumnModel().getColumn(4);
        column.setPreferredWidth(149);

    }// konec nastavTabulkuBT1

    private void inicializaceKomponent(int idZakaznik) {
        this.zakaznik1 = new TridaZakaznik1(idZakaznik);

        try {

            FirmaTextField1.setText(zakaznik1.getNazev());
            AdresaTextField1.setText(zakaznik1.getAdresa());
            MestoTextField1.setText(zakaznik1.getMesto());
            PSCTextField1.setText(zakaznik1.getPsc());

            VystavilComboBox1.removeAllItems();
            VystavilComboBox1.addItem("Stanislav Kaprál");
            VystavilComboBox1.addItem("Ing. Jiří Kaprál");

            jCBJazyk.removeAllItems();
            jCBJazyk.addItem("česky");
            jCBJazyk.addItem("německy");
            jCBJazyk.addItem("anglicky");

            if (zakaznik1.getIdStat() == 1) {
                jCBJazyk.setSelectedItem("česky");
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                //  InfoSoupisTextField1.setText("Ceny jsou včetně materiálu a bez DPH:");
                PredmetTextField1.setText("Cenová nabídka");
            } else if (zakaznik1.getIdStat() == 4 || zakaznik1.getIdStat() == 7) {
                jCBJazyk.setSelectedItem("anglicky");
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                //  InfoSoupisTextField1.setText("Prices include finish:");
                PredmetTextField1.setText("Price quotation ");
            } else if (zakaznik1.getIdStat() == 2 || zakaznik1.getIdStat() == 3 || zakaznik1.getIdStat() == 6) {
                jCBJazyk.setSelectedItem("německy");
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                //   InfoSoupisTextField1.setText("Preise ohne Oberflächenveredelung");
                PredmetTextField1.setText("Preisangebot");
            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void getDataTabulkaSeznam() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arNab1.clear();
        tabulkaModelNabidka1.oznamZmenu();

        for (int k = 0; k < idNabidky.length; k++) {
            //nacist data
            try {
                ResultSet q = PripojeniDB.dotazS(
                        "SELECT nabidky_id, "
                        + "vykresy_nazev, "
                        + "vykresy_cislo, "
                        + "vykresy_revize, "
                        + "vykresy_id, "
                        + "nabidky_pocet_objednanych_kusu, "
                        + "nabidky_cena_za_kus, "
                        + "meny_zkratka "
                        + "FROM spolecne.nabidky "
                        + "CROSS JOIN spolecne.vykresy "
                        + "CROSS JOIN spolecne.meny "
                        + "WHERE vykresy.vykresy_id = nabidky.nabidky_cislo_vykresu "
                        + "AND meny_id = nabidky_mena_id "
                        + "AND nabidky.nabidky_id = " + idNabidky[k] + " "
                        + "ORDER BY vykresy_cislo ASC");

                while (q.next()) {
                    tNab1 = new TridaNabidka1();
                    tNab1.setIdNabidky(q.getLong(1));
                    TridaVykres1 tv = new TridaVykres1();
                    tv.setIdVykres(q.getInt(5));
                    tv.setNazev((q.getString(2) == null) ? "" : q.getString(2));
                    tv.setCislo((q.getString(3) == null) ? "" : q.getString(3));
                    tv.setRevize((q.getString(4) == null) ? "" : q.getString(4));
                    tNab1.setTv1(tv);
                    tNab1.setPocetObjednanychKusu(q.getInt(6));
                    tNab1.setCenaKus(nf2.format(q.getBigDecimal(7)));
                    tNab1.setPopisMena((q.getString(8) == null) ? "" : q.getString(8));
                    tNab1.setPoradi(k + 1);

                    arNab1.add(tNab1);
                }// konec while
            } // konec try
            catch (Exception e) {
                e.printStackTrace();
                PripojeniDB.vyjimkaS(e);
            } // konec catch
        }
    } //konec getDataTabulkaObjednavka1

    private void getDataTabulkaTerminUprava1() {
        if (tabulka.getCellEditor() != null) {
            // System.out.println("Zastavena editace");
            tabulka.getCellEditor().cancelCellEditing();//zastavit editaci bunky pred nactenim novych dat
        }//konec if
        arNab1.clear();
        tabulkaModelNabidka1.oznamZmenu();
        int k = 0;
        try {
            ResultSet q = PripojeniDB.dotazS(
                    "SELECT nabidky_id, "
                    + "vykresy_nazev, "
                    + "vykresy_cislo, "
                    + "vykresy_revize, "
                    + "vykresy_id, "
                    + "nabidky_pocet_objednanych_kusu, "
                    + "nabidky_cena_za_kus, "
                    + "meny_zkratka "
                    + "FROM spolecne.nabidky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.vazba_seznam_nabidky "
                    + "CROSS JOIN spolecne.meny "
                    + "WHERE vykresy.vykresy_id = nabidky.nabidky_cislo_vykresu "
                    + "AND nabidky.nabidky_id = vazba_seznam_nabidky_nabidky_id "
                    + "AND meny_id = nabidky_mena_id "
                    + "AND vazba_seznam_nabidky_seznam_id = " + idSeznam + " "
                    + "ORDER BY vazba_seznam_nabidky_poradi, vykresy_cislo ASC");
            while (q.next()) {
                tNab1 = new TridaNabidka1();
                tNab1.setIdNabidky(q.getLong(1));
                TridaVykres1 tv = new TridaVykres1();
                tv.setIdVykres(q.getInt(5));
                tv.setNazev((q.getString(2) == null) ? "" : q.getString(2));
                tv.setCislo((q.getString(3) == null) ? "" : q.getString(3));
                tv.setRevize((q.getString(4) == null) ? "" : q.getString(4));
                tNab1.setTv1(tv);
                tNab1.setPocetObjednanychKusu(q.getInt(6));
                tNab1.setCenaKus(nf2.format(q.getBigDecimal(7)));
                tNab1.setPopisMena((q.getString(8) == null) ? "" : q.getString(8));
                tNab1.setPoradi(k + 1);
                k++;
                arNab1.add(tNab1);
            }// konec while

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch

    } //konec getDataTabulkaObjednavka1

    private void ulozit() {
        if (uprava == false) {
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(seznam_nabidek_id) FROM spolecne.seznam_nabidek");
                while (id.next()) {
                    idSeznam = id.getLong(1) + 1;
                }
            } catch (Exception e) {
                idSeznam = 1;
            }
            // TODO kontrola datumu
            try {
                int a = PripojeniDB.dotazIUD("BEGIN");
                String dotazFakt = "INSERT INTO spolecne.seznam_nabidek("
                        + "seznam_nabidek_id, "
                        + "seznam_nabidek_zakaznik_id, "
                        + "seznam_nabidek_datum, "
                        + "seznam_nabidek_komu, "
                        + "seznam_nabidek_predmet, "
                        + "seznam_nabidek_veta, seznam_nabidek_vystavil) "
                        + "VALUES( " + idSeznam + ", " + idZakaznik
                        + ", " + TextFunkce1.osetriZapisTextDB1(DatumVystaveniTextField1.getText())
                        + ", " + TextFunkce1.osetriZapisTextDB1(PrijemceTextField1.getText())
                        + ", " + TextFunkce1.osetriZapisTextDB1(PredmetTextField1.getText())
                        + ", " + TextFunkce1.osetriZapisTextDB1(jTPoznamka.getText())
                        + ", " + TextFunkce1.osetriZapisTextDB1((String) VystavilComboBox1.getSelectedItem())
                        + ")";

                a = PripojeniDB.dotazIUD(dotazFakt);

                for (int nabIndex = 0; nabIndex < arNab1.size(); nabIndex++) {
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_seznam_nabidky("
                            + "vazba_seznam_nabidky_seznam_id, "
                            + "vazba_seznam_nabidky_nabidky_id, "
                            + "vazba_seznam_nabidky_poradi "
                            + ") "
                            + "VALUES( " + idSeznam + ", " + arNab1.get(nabIndex).getIdNabidky()
                            + ", " + arNab1.get(nabIndex).getPoradi() + ")");

                }
                a = PripojeniDB.dotazIUD("COMMIT");
            } catch (Exception e) {
                int a = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
            }

            TiskButton.setEnabled(true);

        } else if (uprava == true) {
            int rc = 0;
            rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
            // TODO kontrola datumu
            try {
                String dotazFakt = "UPDATE spolecne.seznam_nabidek "
                        + "SET seznam_nabidek_zakaznik_id = " + this.idZakaznik + ", "
                        + "seznam_nabidek_datum = " + TextFunkce1.osetriZapisTextDB1(DatumVystaveniTextField1.getText()) + ", "
                        + "seznam_nabidek_komu = " + TextFunkce1.osetriZapisTextDB1(PrijemceTextField1.getText()) + ", "
                        + "seznam_nabidek_predmet = " + TextFunkce1.osetriZapisTextDB1(PredmetTextField1.getText()) + ", "
                        + "seznam_nabidek_veta = " + TextFunkce1.osetriZapisTextDB1(jTPoznamka.getText()) + ", "
                        + "seznam_nabidek_vystavil = " + TextFunkce1.osetriZapisTextDB1((String) VystavilComboBox1.getSelectedItem()) + " "
                        + "WHERE seznam_nabidek_id = " + this.idSeznam;

                int a = PripojeniDB.dotazIUD(dotazFakt);
                String dotazFaktDelete = "DELETE FROM spolecne.vazba_seznam_nabidky "
                        + "WHERE vazba_seznam_nabidky_seznam_id = " + this.idSeznam;

                a = PripojeniDB.dotazIUD(dotazFaktDelete);

                for (int nabIndex = 0; nabIndex < arNab1.size(); nabIndex++) {
                    a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_seznam_nabidky("
                            + "vazba_seznam_nabidky_seznam_id, "
                            + "vazba_seznam_nabidky_nabidky_id, "
                            + "vazba_seznam_nabidky_poradi "
                            + ") "
                            + "VALUES( " + idSeznam + ", " + arNab1.get(nabIndex).getIdNabidky()
                            + ", " + arNab1.get(nabIndex).getPoradi() + ")");
                }
                SQLFunkceObecne2.spustPrikaz("COMMIT");
            } catch (Exception e) {
                int a = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
            }

            TiskButton.setEnabled(true);
        }

        tisk(false);

        seznamUploadDB(this.idSeznam);
        UlozFakturuButton.setEnabled(false);
        UlozFakturuButton.setBackground(
                new Color(255, 51, 51));
    }

    private void seznamUploadDB(long idSeznam) {
        // System.out.println("Updatuji doklad " + dokladCislo);
        try {
            File soubor = new File(HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "preisangebot.pdf");
            FileInputStream fis = new FileInputStream(soubor);
            PreparedStatement ps = PripojeniDB.con.prepareStatement("UPDATE spolecne.seznam_nabidek SET seznam_nabidek_bindata = ? WHERE seznam_nabidek_id = ?");
            ps.setBinaryStream(1, fis, (int) soubor.length());
            ps.setLong(2, idSeznam);
            ps.executeUpdate();
            ps.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tisk(boolean tisk) {
        String reportSource = "";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("prijemce", PrijemceTextField1.getText().trim());
        params.put("predmet", PredmetTextField1.getText().trim());
        params.put("info", jTPoznamka.getText().trim());

        if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekCesky.jrxml";
            params.put("datum", ("V Plzni, dne " + DatumVystaveniTextField1.getText().trim()));
        } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekBBHUSA.jrxml";
            params.put("datum", ("Pilsen, " + DatumVystaveniTextField1.getText().trim()));
        } else if (((String) jCBJazyk.getSelectedItem()).equals("německy")) {
            reportSource = HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "seznamNabidekLeica1.jrxml";
            params.put("datum", ("Pilsen, den " + DatumVystaveniTextField1.getText().trim()));
        }

        params.put("seznam_vystavil", VystavilComboBox1.getSelectedItem().toString());
        params.put("firma", FirmaTextField1.getText().trim());
        params.put("adresa_ulice", AdresaTextField1.getText().trim());
        params.put("adresa_psc_mesto", PSCTextField1.getText().trim() + " " + MestoTextField1.getText().trim());
        params.put("mena", arNab1.get(0).getPopisMena());

        params.put("seznam_id", idSeznam);
        params.put("zakaznik_id", idZakaznik);
        params.put("logo", HlavniRamec.mikronAdresar + HlavniRamec.oddelovac + HlavniRamec.tiskAdresar + HlavniRamec.oddelovac + "mikron.jpg");

        try {
            JasperReport jasperReport
                    = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint1
                    = JasperFillManager.fillReport(
                            jasperReport, params, PripojeniDB.con);
            if (tisk == true) {
                JasperPrintManager.printReport(jasperPrint1, true);
            } else {
                JasperExportManager.exportReportToPdfFile(jasperPrint1, HlavniRamec.loaderAdresar + HlavniRamec.oddelovac + "preisangebot.pdf");
            }
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    private void posunNahoru() {
        int indexPuv = tabulka.getSelectedRow();
        TridaNabidka1 objPuv = arNab1.get(indexPuv);
        //System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv > 0) {
            TridaNabidka1 objNova = arNab1.get(indexPuv - 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv);
            arNab1.set(indexPuv, objNova);
            arNab1.set(indexPuv - 1, objPuv);
            tabulkaModelNabidka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv - 1, indexPuv - 1);
        }
    }

    private void posunDolu() {
        int indexPuv = tabulka.getSelectedRow();
        TridaNabidka1 objPuv = arNab1.get(indexPuv);
        // System.out.println("indexPuvodni : " + indexPuv + " poradi " + (Integer) objPuv.get(0));
        if (indexPuv < arNab1.size() - 1) {
            TridaNabidka1 objNova = arNab1.get(indexPuv + 1);
            objNova.setPoradi(indexPuv + 1);
            objPuv.setPoradi(indexPuv + 2);
            arNab1.set(indexPuv, objNova);
            arNab1.set(indexPuv + 1, objPuv);
            tabulkaModelNabidka1.oznamZmenu();
            ListSelectionModel selectionModel
                    = tabulka.getSelectionModel();
            selectionModel.setSelectionInterval(indexPuv + 1, indexPuv + 1);
        }
    }

    protected class tabulkaModelObjednavka1 extends AbstractTableModel {

        protected final String[] columnNames = {
            "Pořadí",
            "Výkres",
            "Název",
            "Množství",
            "Cena/ks"
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

        @Override
        public Class getColumnClass(int col) {
            return String.class;

        }

        public boolean nastavHodnotuNaVybrane(TridaNabidka1 po) {
            try {
                arNab1.set(tabulka.getSelectedRow(), po);
                oznamUpdateRadky();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }//konec getNastavHodnotuNaVybrane

        public boolean nastavHodnotuNaPozici(TridaNabidka1 po, int pozice) {
            try {
                arNab1.set(pozice, po);
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
                TridaNabidka1 tNab1 = arNab1.get(row);
                switch (col) {
                    case (0): {
                        return (tNab1.getPoradi());
                    }
                    case (1): {
                        return (tNab1.getTv1().getCislo());
                    }
                    case (2): {
                        return (tNab1.getTv1().getNazev());
                    }
                    case (3): {
                        return (tNab1.getPocetObjednanychKusu());
                    }
                    case (4): {
                        return (tNab1.getCenaKus());
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        FirmaTextField1 = new javax.swing.JTextField();
        AdresaTextField1 = new javax.swing.JTextField();
        MestoTextField1 = new javax.swing.JTextField();
        PSCTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        DatumVystaveniTextField1 = new javax.swing.JTextField();
        PredmetTextField1 = new javax.swing.JTextField();
        PrijemceTextField1 = new javax.swing.JTextField();
        VystavilComboBox1 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jCBJazyk = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTPoznamka = new javax.swing.JTextArea();
        jSPTabulka = new javax.swing.JScrollPane();
        tabulka = new javax.swing.JTable();
        TiskButton = new javax.swing.JButton();
        UlozFakturuButton = new javax.swing.JButton();
        jBVzhuru = new javax.swing.JButton();
        jBDolu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seznam nabídek");

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Firma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(FirmaTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(AdresaTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(MestoTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(PSCTextField1, gridBagConstraints);

        jLabel9.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel10.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel10, gridBagConstraints);

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
        jPanel2.add(DatumVystaveniTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PredmetTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(PrijemceTextField1, gridBagConstraints);

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

        jLabel11.setText("Jazyk :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel11, gridBagConstraints);

        jCBJazyk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCBJazyk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBJazykActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jCBJazyk, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel7.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel7, gridBagConstraints);

        jTPoznamka.setColumns(20);
        jTPoznamka.setRows(5);
        jScrollPane1.setViewportView(jTPoznamka);

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

        TiskButton.setText("Tisknout");
        TiskButton.setEnabled(false);
        TiskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TiskButtonActionPerformed(evt);
            }
        });

        UlozFakturuButton.setText("Uložit");
        UlozFakturuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UlozFakturuButtonActionPerformed(evt);
            }
        });

        jBVzhuru.setText("Posunout nahoru");
        jBVzhuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBVzhuruMouseClicked(evt);
            }
        });

        jBDolu.setText("Posunout dolů");
        jBDolu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBDoluMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(TiskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UlozFakturuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSPTabulka, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBVzhuru)
                            .addComponent(jBDolu, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBVzhuru)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBDolu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSPTabulka, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TiskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UlozFakturuButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TiskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TiskButtonActionPerformed
        tisk(true);
    }//GEN-LAST:event_TiskButtonActionPerformed

    private void UlozFakturuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UlozFakturuButtonActionPerformed
        ulozit();
    }//GEN-LAST:event_UlozFakturuButtonActionPerformed

    private void jBVzhuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVzhuruMouseClicked
        posunNahoru();
    }//GEN-LAST:event_jBVzhuruMouseClicked

    private void jBDoluMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBDoluMouseClicked
        posunDolu();
    }//GEN-LAST:event_jBDoluMouseClicked

    private void jCBJazykActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBJazykActionPerformed
        if (jCBJazyk.getItemCount() > 0) {

            if (((String) jCBJazyk.getSelectedItem()).equals("česky")) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                // InfoSoupisTextField1.setText("Ceny jsou včetně materiálu a bez DPH:");
                PredmetTextField1.setText("Cenová nabídka");
            } else if (((String) jCBJazyk.getSelectedItem()).equals("anglicky")) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                //  InfoSoupisTextField1.setText("Prices include finish:");
                PredmetTextField1.setText("Price quotation ");
            } else if (((String) jCBJazyk.getSelectedItem()).equals("německy")) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date date = new java.util.Date();
                DatumVystaveniTextField1.setText(dateFormat.format(date));
                //   InfoSoupisTextField1.setText("Preise ohne Oberflächenveredelung");
                PredmetTextField1.setText("Preisangebot");
            }
        }
    }//GEN-LAST:event_jCBJazykActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AdresaTextField1;
    private javax.swing.JTextField DatumVystaveniTextField1;
    private javax.swing.JTextField FirmaTextField1;
    private javax.swing.JTextField MestoTextField1;
    private javax.swing.JTextField PSCTextField1;
    private javax.swing.JTextField PredmetTextField1;
    private javax.swing.JTextField PrijemceTextField1;
    private javax.swing.JButton TiskButton;
    private javax.swing.JButton UlozFakturuButton;
    private javax.swing.JComboBox VystavilComboBox1;
    private javax.swing.JButton jBDolu;
    private javax.swing.JButton jBVzhuru;
    private javax.swing.JComboBox jCBJazyk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jSPTabulka;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTPoznamka;
    private javax.swing.JTable tabulka;
    // End of variables declaration//GEN-END:variables
}
