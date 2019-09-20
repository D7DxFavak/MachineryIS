/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaRamcovaObjednavka;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class RamcovaObjednavkaFrame1 extends javax.swing.JFrame {

    private TridaRamcovaObjednavka tObj;
    private TridaVykres1 tv1;
    //  private ArrayList<TridaVykres1> arTV1;
    //  private ArrayList<TridaVykres1> arVykresyZmeny;
    private TridaVykres1 vykresZmena;
    protected RoletkaUniverzalModel1 roletkaModelZakaznici, roletkaModelMeny;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    private long idObjednavky;
    private Vector vsKapacity;
    private boolean edit = false;

    /**
     * Creates new form ObjednavkaFrame
     */
    public RamcovaObjednavkaFrame1(String title, String buttonText) {
        initComponents();
        this.setTitle(title);
        this.setVisible(true);
        initKomponenty();
    }

    public RamcovaObjednavkaFrame1(int idObjednavky, boolean edit, String title, String buttonText) {
        initComponents();
        //novaPruvodka = false;
        this.setTitle(title);
        jBUlozit.setText(buttonText);

        this.setVisible(true);
        initKomponenty();

        tObj = new TridaRamcovaObjednavka();
        this.edit = edit;
        if (edit == true) {
            tObj.setId(idObjednavky);
        }
        System.out.println("id Objednavky : " + idObjednavky);

        initHodnoty(idObjednavky);
    }

    private void initKomponenty() {

        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);


        tv1 = new TridaVykres1();
        tObj = new TridaRamcovaObjednavka();

        vykresZmena = new TridaVykres1();

        vsKapacity = new Vector();
        initRoletky();

        //initVykresy();
    }

    private void initRoletky() {
        roletkaModelZakaznici = new RoletkaUniverzalModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_nazev FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_nazev",
                "V databázi nejsou zadáni zákazníci"); // bylo ...vs_id
        ZakaznikComboBox1.setModel(roletkaModelZakaznici);

        if (ZakaznikComboBox1.getItemCount() > 0) {
            roletkaModelZakaznici.setDataOId(MikronIS2.indexZakaznika);
        }

        roletkaModelMeny = new RoletkaUniverzalModel1(
                "SELECT meny_id, meny_zkratka FROM spolecne.meny ORDER BY meny_poradi_vyberu ASC",
                "V databázi nejsou zadány měny"); // bylo ...vs_id
        MenaComboBox1.setModel(roletkaModelMeny);
    }

    private void cenaCelkem() {
        if ((PocetKusuTextField1.getText().length() > 0) && (CenaZaKusTextField1.getText().length() > 0)) {
            int PocetKusu = Integer.valueOf((String) PocetKusuTextField1.getText()).intValue();
            BigDecimal cenaZaKus = new BigDecimal((String) CenaZaKusTextField1.getText().replaceAll(" ", "").replaceAll(",", "."));
            CenaCelkemLabel1.setText(nf2.format(cenaZaKus.multiply(new BigDecimal(PocetKusu))));
        }
    }

    private void initHodnoty(int idObjednavky) {
        tObj = new TridaRamcovaObjednavka(idObjednavky);
        roletkaModelZakaznici.setDataOId(tObj.getIdZakaznik());

        CisloObjednavkyTextField1.setText(tObj.getCisloObjednavky());
        DatumObjednaniTextField1.setText(df.format(tObj.getDatumObjednani()));
        //NazevSoucastiTextField1.setText((String) vsObjednavka1.get(3));

        tv1 = new TridaVykres1(tObj.getIdVykres());

        CisloVykresuTextField1.setText(tv1.getCislo());

        ZmenaVykresuTextField1.setText(tv1.getRevize());
        NazevSoucastiTextField1.setText(tv1.getNazev());
        PocetKusuTextField1.setText(tObj.getCelkemKusu() + "");
        try {
            CenaZaKusTextField1.setText(nf2.format(tObj.getCenaKus()));
        } catch (Exception e) {          
            CenaZaKusTextField1.setText((tObj.getCenaKus()) + "");
            e.printStackTrace();
        }
        cenaCelkem();
        roletkaModelMeny.setDataOId(tObj.getIdMena());
        jTFOdvolavka1.setText(tObj.getOdvolavkaKusu() + "");

        try {
            PoznamkaTextField1.setText(tObj.getPoznamka());
        } catch (Exception e) {
            e.printStackTrace();
        }
        kontrolaVykresu();

    }

    private void ulozObjednavku() {
        try {
            MikronIS2.indexZakaznika = ZakaznikComboBox1.getSelectedIndex();
            if (ZakaznikComboBox1.getSelectedIndex() > -1) {
                tObj.setIdZakaznik(((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
            } else {
                tObj.setIdZakaznik(0);
            }
            if (MenaComboBox1.getSelectedIndex() > -1) {
                tObj.setIdMena(((DvojiceCisloRetez) roletkaModelMeny.getSelectedItem()).cislo());
            } else {
                tObj.setIdMena(0);
            }
            tObj.setCisloObjednavky(CisloObjednavkyTextField1.getText().trim());
            try {
                tObj.setDatumObjednani(df.parse(DatumObjednaniTextField1.getText().trim()));
            } catch (Exception e) {
                e.printStackTrace();
                JednoducheDialogy1.errAno(new JFrame(), "Chyba při zpracování data objednání", "Prosím zkontrolujte datum objednání");
            }
            tObj.setNazevSoucasti(NazevSoucastiTextField1.getText().trim());
            tObj.setCelkemKusu(new Integer(PocetKusuTextField1.getText().trim()).intValue());
            tObj.setOdvolavkaKusu(new Integer(jTFOdvolavka1.getText().trim()).intValue());
            tObj.setCenaKus(new BigDecimal(CenaZaKusTextField1.getText().trim().replaceAll(",", ".")));
            tObj.setPoznamka(PoznamkaTextField1.getText().trim());

            String dotazVykres = "SELECT vykresy_id "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "' ";
            if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
                dotazVykres += "AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }
            try {
                ResultSet vykresCisloDotaz = PripojeniDB.dotazS(dotazVykres);
                while (vykresCisloDotaz.next()) {
                    tObj.setIdVykres(vykresCisloDotaz.getInt(1));
                }
            } catch (Exception e) {
                JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání rámcové objednávky", "Zadaný výkres neexistuje");
                e.printStackTrace();
            }

            if (edit == false) {
                tObj.insertData();
            } else {
                tObj.updateData();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání rámcové objednávky", "Prosím zkontrolujte zadané informace");

        }
    }

    private void kontrolaVykresu() {
        int pocetVykresu = 0;
        try {


            String dotaz = "SELECT vykresy_id, vykresy_nazev "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }

            ResultSet vykresyZmeny1 = PripojeniDB.dotazS(dotaz);

            while (vykresyZmeny1.next()) {
                vykresZmena = new TridaVykres1();
                vykresZmena.setIdVykres(new Integer(vykresyZmeny1.getInt(1)));
                vykresZmena.setNazev((vykresyZmeny1.getString(2) == null) ? "" : vykresyZmeny1.getString(2));
                pocetVykresu++;
                NazevSoucastiTextField1.setText(vykresZmena.getNazev());
            }

            String kusyNavicText = "";
            ResultSet kusyNavic1 = PripojeniDB.dotazS(
                    "SELECT SUM(objednavky_kusu_navic) + SUM(objednavky_kusu_vyrobit) - SUM(objednavky_pocet_objednanych_kusu) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE objednavky.objednavky_cislo_vykresu = vykresy.vykresy_id "
                    + "AND vykresy.vykresy_id = " + vykresZmena.getIdVykres());
            while (kusyNavic1.next()) {
                kusyNavicText += kusyNavic1.getString(1);

            }
            ResultSet kusyNavic2 = PripojeniDB.dotazS(
                    "SELECT SUM(objednavky_kusu_navic) + SUM(objednavky_kusu_vyrobit) - SUM(objednavky_pocet_objednanych_kusu) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE objednavky.objednavky_cislo_vykresu = vykresy.vykresy_id "
                    + "AND vykresy.vykresy_cislo = " + TextFunkce1.osetriZapisTextDB1(CisloVykresuTextField1.getText().trim()));//(Integer) vsVykresyZmeny.get(0));
            while (kusyNavic2.next()) {
                kusyNavicText += " (celkove " + kusyNavic2.getString(1) + " ks)";
            }
            KusuNavicLabel1.setText(kusyNavicText);

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);

        } // konec catch

        if (pocetVykresu < 1) {
            // System.out.println("nenalezeno");
            NazevSoucastiTextField1.setText("");
            NovyVykresFrame novyVykres = new NovyVykresFrame(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim(), false);
        }
        /*
         * if (VykresyZmenyComboBox1.getItemCount() > 0) {
         * VykresyZmenyComboBox1.setSelectedIndex(0);
         * NazevSoucastiTextField1.setText((String)((Vector)
         * vrVykresyZmeny.get(0)).get(2)); } else {
         * NazevSoucastiTextField1.setText((String)((Vector)
         * vrVykresyZmeny.get(0)).get(2)); }
         */
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        CisloObjednavkyTextField1 = new javax.swing.JTextField();
        DatumObjednaniTextField1 = new javax.swing.JTextField();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        ZmenaVykresuTextField1 = new javax.swing.JTextField();
        NazevSoucastiTextField1 = new javax.swing.JTextField();
        PocetKusuTextField1 = new javax.swing.JTextField();
        CenaZaKusTextField1 = new javax.swing.JTextField();
        jTFOdvolavka1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        ZakaznikComboBox1 = new javax.swing.JComboBox();
        MenaComboBox1 = new javax.swing.JComboBox();
        NovyVykresButton1 = new javax.swing.JButton();
        CenaCelkemLabel1 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        PoznamkaTextField1 = new javax.swing.JTextField();
        jBUlozit = new javax.swing.JButton();
        jBZavrit = new javax.swing.JButton();
        KusuNavicLabel1 = new javax.swing.JLabel();
        ErrorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Rámcová objednávka");

        jPanel1.setToolTipText("");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Datum objednání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Číslo objednávky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel5.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Změna :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Název součásti :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jLabel8.setText("Ks navíc ve výrobě :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        CisloObjednavkyTextField1.setNextFocusableComponent(CisloVykresuTextField1);
        CisloObjednavkyTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloObjednavkyTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CisloObjednavkyTextField1, gridBagConstraints);

        DatumObjednaniTextField1.setNextFocusableComponent(CisloObjednavkyTextField1);
        DatumObjednaniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                DatumObjednaniTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(DatumObjednaniTextField1, gridBagConstraints);

        CisloVykresuTextField1.setNextFocusableComponent(ZmenaVykresuTextField1);
        CisloVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloVykresuTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CisloVykresuTextField1, gridBagConstraints);

        ZmenaVykresuTextField1.setNextFocusableComponent(NazevSoucastiTextField1);
        ZmenaVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(ZmenaVykresuTextField1, gridBagConstraints);

        NazevSoucastiTextField1.setNextFocusableComponent(PocetKusuTextField1);
        NazevSoucastiTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                NazevSoucastiTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(NazevSoucastiTextField1, gridBagConstraints);

        PocetKusuTextField1.setNextFocusableComponent(CenaZaKusTextField1);
        PocetKusuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PocetKusuTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PocetKusuTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        jPanel1.add(PocetKusuTextField1, gridBagConstraints);

        CenaZaKusTextField1.setBackground(new java.awt.Color(255, 255, 204));
        CenaZaKusTextField1.setNextFocusableComponent(jTFOdvolavka1);
        CenaZaKusTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CenaZaKusTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                CenaZaKusTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CenaZaKusTextField1, gridBagConstraints);

        jTFOdvolavka1.setNextFocusableComponent(PoznamkaTextField1);
        jTFOdvolavka1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFOdvolavka1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTFOdvolavka1, gridBagConstraints);

        jLabel17.setText("Počet objednaných ks :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel17, gridBagConstraints);

        jLabel20.setText("Cena za kus :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel20, gridBagConstraints);

        jLabel21.setText("Cena celkem :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel21, gridBagConstraints);

        jLabel22.setText("Odvolávka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel22, gridBagConstraints);

        ZakaznikComboBox1.setMaximumRowCount(15);
        ZakaznikComboBox1.setNextFocusableComponent(DatumObjednaniTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        jPanel1.add(ZakaznikComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 12);
        jPanel1.add(MenaComboBox1, gridBagConstraints);

        NovyVykresButton1.setText("Nový");
        NovyVykresButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NovyVykresButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 20);
        jPanel1.add(NovyVykresButton1, gridBagConstraints);

        CenaCelkemLabel1.setText("0,00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPanel1.add(CenaCelkemLabel1, gridBagConstraints);

        jLabel29.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 10, 10, 0);
        jPanel1.add(jLabel29, gridBagConstraints);

        PoznamkaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PoznamkaTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 10, 0);
        jPanel1.add(PoznamkaTextField1, gridBagConstraints);

        jBUlozit.setText("Uložit objednávku");
        jBUlozit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBUlozitMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 6, 10);
        jPanel1.add(jBUlozit, gridBagConstraints);

        jBZavrit.setText("Zavřít");
        jBZavrit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBZavritMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 6, 25);
        jPanel1.add(jBZavrit, gridBagConstraints);

        KusuNavicLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(KusuNavicLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        jPanel1.add(ErrorLabel, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBUlozitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBUlozitMouseClicked
        ulozObjednavku();
    }//GEN-LAST:event_jBUlozitMouseClicked

    private void NovyVykresButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NovyVykresButton1MouseClicked
        NovyVykresFrame novyVykres = new NovyVykresFrame(false);
    }//GEN-LAST:event_NovyVykresButton1MouseClicked

    private void jBZavritMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBZavritMouseClicked
        this.dispose();
    }//GEN-LAST:event_jBZavritMouseClicked

    private void DatumObjednaniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DatumObjednaniTextField1FocusGained
        DatumObjednaniTextField1.selectAll();
    }//GEN-LAST:event_DatumObjednaniTextField1FocusGained

    private void CisloObjednavkyTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloObjednavkyTextField1FocusGained
        CisloObjednavkyTextField1.selectAll();
    }//GEN-LAST:event_CisloObjednavkyTextField1FocusGained

    private void CisloVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloVykresuTextField1FocusGained
        CisloVykresuTextField1.selectAll();
    }//GEN-LAST:event_CisloVykresuTextField1FocusGained

    private void ZmenaVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusGained
        ZmenaVykresuTextField1.selectAll();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusGained

    private void ZmenaVykresuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusLost
        kontrolaVykresu();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusLost

    private void NazevSoucastiTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NazevSoucastiTextField1FocusGained
        NazevSoucastiTextField1.selectAll();
    }//GEN-LAST:event_NazevSoucastiTextField1FocusGained

    private void PocetKusuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetKusuTextField1FocusGained
        PocetKusuTextField1.selectAll();
    }//GEN-LAST:event_PocetKusuTextField1FocusGained

    private void PocetKusuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetKusuTextField1FocusLost
        cenaCelkem();
    }//GEN-LAST:event_PocetKusuTextField1FocusLost

    private void CenaZaKusTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CenaZaKusTextField1FocusGained
        CenaZaKusTextField1.selectAll();
    }//GEN-LAST:event_CenaZaKusTextField1FocusGained

    private void CenaZaKusTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CenaZaKusTextField1FocusLost
        cenaCelkem();
    }//GEN-LAST:event_CenaZaKusTextField1FocusLost

    private void jTFOdvolavka1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFOdvolavka1FocusGained
        jTFOdvolavka1.selectAll();
    }//GEN-LAST:event_jTFOdvolavka1FocusGained

    private void PoznamkaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PoznamkaTextField1FocusGained
        PoznamkaTextField1.selectAll();
    }//GEN-LAST:event_PoznamkaTextField1FocusGained
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CenaCelkemLabel1;
    private javax.swing.JTextField CenaZaKusTextField1;
    private javax.swing.JTextField CisloObjednavkyTextField1;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JTextField DatumObjednaniTextField1;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JLabel KusuNavicLabel1;
    private javax.swing.JComboBox MenaComboBox1;
    private javax.swing.JTextField NazevSoucastiTextField1;
    private javax.swing.JButton NovyVykresButton1;
    private javax.swing.JTextField PocetKusuTextField1;
    private javax.swing.JTextField PoznamkaTextField1;
    private javax.swing.JComboBox ZakaznikComboBox1;
    private javax.swing.JTextField ZmenaVykresuTextField1;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JButton jBZavrit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFOdvolavka1;
    // End of variables declaration//GEN-END:variables
}
