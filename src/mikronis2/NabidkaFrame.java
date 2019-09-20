/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.dbtridy.TridaNabidka1;
import mikronis2.dbtridy.TridaPolotovar1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class NabidkaFrame extends javax.swing.JFrame {

    private TridaNabidka1 tNab1;
    private TridaVykres1 tv1;
    private ArrayList<TridaVykres1> arTV1;
    private ArrayList<TridaVykres1> arVykresyZmeny;
    private TridaVykres1 vykresZmena;
    protected RoletkaUniverzalModel1 roletkaModelZakaznici, roletkaModelSkupina, roletkaModelTvar, roletkaModelMaterial, roletkaModelMeny;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    private long idNabidka;
    private int idZakaznik;
    private boolean edit = false;
    protected WinUdalosti winUdalosti;

    /**
     * Creates new form ObjednavkaFrame
     */
    public NabidkaFrame(String title, int idZakaznik) {
        initComponents();
        this.setTitle(title);
        this.setVisible(true);
        this.idZakaznik = idZakaznik;
        tNab1 = new TridaNabidka1();

        initKomponenty();
        initRoletkaMaterial();
        initRoletkaTvar();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public NabidkaFrame(long idNabidky, boolean edit, String title, int idZakaznik) {
        initComponents();
        //novaPruvodka = false;
        this.setTitle(title);
        this.setVisible(true);
        this.idZakaznik = idZakaznik;
        initKomponenty();
        initRoletkaMaterial();
        initRoletkaTvar();
        tNab1 = new TridaNabidka1();
        this.edit = edit;
        // if (edit == true) {
        this.idNabidka = idNabidky;
        //}
        System.out.println("id Nabidky : " + idNabidky);

        initHodnoty(idNabidky);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initKomponenty() {

        arTV1 = new ArrayList<TridaVykres1>();
        tv1 = new TridaVykres1();

        vykresZmena = new TridaVykres1();
        arVykresyZmeny = new ArrayList<TridaVykres1>();
        winUdalosti = new WinUdalosti();
        initRoletky();

        //  MaterialComboBox1.removeAllItems();
        //initVykresy();
    }

    private void initRoletky() {
        roletkaModelZakaznici = new RoletkaUniverzalModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_nazev FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_nazev",
                "V databázi nejsou zadáni zákazníci"); // bylo ...vs_id
        ZakaznikComboBox1.setModel(roletkaModelZakaznici);
        if (this.idZakaznik > 0) {
            roletkaModelZakaznici.setDataOId(this.idZakaznik);
        }

        if (this.idZakaznik != 12) {
            MaterialBBHLabel1.setVisible(false);
            KusuRocneTextField1.setVisible(false);
        }

        roletkaModelSkupina = new RoletkaUniverzalModel1(
                "SELECT skupina_materialu_id, skupina_materialu_nazev FROM spolecne.skupina_materialu "
                + "ORDER BY skupina_materialu_nazev ASC",
                "V databázi nejsou zadány skupiny materialu"); // bylo ...vs_id
        jCBSkupina.setModel(roletkaModelSkupina);

        roletkaModelMeny = new RoletkaUniverzalModel1(
                "SELECT meny_id, meny_zkratka FROM spolecne.meny ORDER BY meny_poradi_vyberu ASC",
                "V databázi nejsou zadány měny"); // bylo ...vs_id
        MenaComboBox1.setModel(roletkaModelMeny);
        MenaComboBox2.setModel(roletkaModelMeny);
    }

    private void initRoletkaMaterial() {
        if (roletkaModelSkupina.getSelectedItem() == null) {
            roletkaModelSkupina.setSelectedIndex(0);
        }

        roletkaModelMaterial = new RoletkaUniverzalModel1(
                "SELECT typ_materialu_id, "
                + "CASE "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_nazev || ' - ' || typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                + "ELSE null "
                + "END AS nazev_norma "
                + "FROM spolecne.typ_materialu "
                + "WHERE typ_materialu_skupina = " + ((DvojiceCisloRetez) roletkaModelSkupina.getSelectedItem()).cislo() + " "
                + "ORDER BY typ_materialu_id ASC",
                "V databázi nejsou zadány typy materialu"); // bylo ...vs_id
        MaterialComboBox1.setModel(roletkaModelMaterial);

    }

    private void initRoletkaTvar() {
        if (roletkaModelMaterial.getSelectedItem() == null) {
            roletkaModelMaterial.setSelectedIndex(0);
        }
        roletkaModelTvar = new RoletkaUniverzalModel1(
                "SELECT polotovary_id, polotovary_nazev "
                + "FROM spolecne.polotovary "
                + "WHERE polotovary_typ_materialu = " + ((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo() + " "
                + "ORDER BY polotovary_nazev",
                "V databázi nejsou zadány typy materialu"); // bylo ...vs_id
        TvarUpravaComboBox1.setModel(roletkaModelTvar);
    }

    private void initHodnoty(long idNabidky) {
        //  System.out.println("inti : " + idNabidky);
        tNab1 = new TridaNabidka1(idNabidky);
        roletkaModelZakaznici.setDataOId(tNab1.getIdZakaznik());
        TridaPolotovar1 tpInit = new TridaPolotovar1(tNab1.getIdPolotovar());
        tNab1.setTp1(tpInit);

        // roletkaModelSkupina.setDataOId(tpInit.getTypMaterialu());
        roletkaModelSkupina.setDataOId(SQLFunkceObecne2.selectINTPole("SELECT typ_materialu_skupina "
                + "FROM spolecne.typ_materialu WHERE typ_materialu_id = " + tpInit.getTypMaterialu()));
        initRoletkaMaterial();
        roletkaModelMaterial.setDataOId(tpInit.getTypMaterialu());
        initRoletkaTvar();
        roletkaModelTvar.setDataOId(tNab1.getIdPolotovar());
        roletkaModelMeny.setDataOId(tNab1.getIdMena());
        CenaMaterialTextField1.setText(tNab1.getCenaKg());

        PocetKusuTextField1.setText(tNab1.getPocetObjednanychKusu() + "");
        System.out.println("datum " + tNab1.getDatum());
        DatumTextField1.setText(df.format(tNab1.getDatum()));
        //NazevSoucastiTextField1.setText((String) vsObjednavka1.get(3));

        TridaVykres1 tv1 = new TridaVykres1(tNab1.getIdVykres());
        tNab1.setTv1(tv1);

        CisloVykresuTextField1.setText(tv1.getCislo());
        ZmenaVykresuTextField1.setText(tv1.getRevize());
        NazevSoucastiTextField1.setText(tv1.getNazev());
        PocetKusuTextField1.setText(tNab1.getPocetObjednanychKusu() + "");
        cenaKusTextField1.setText(tNab1.getCenaKus());
        upravenaCenaKusTextField1.setText(tNab1.getCenaKus());

        MnozstviTextField1.setText(tNab1.getMaterialMnozstvi());
        PraceTextField1.setText(tNab1.getPrace());
        PraceTextField2.setText(tNab1.getPrace2());
        PraceTextField3.setText(tNab1.getPrace3());
        PripravaTextField1.setText(tNab1.getPriprava());
        PGMTextField1.setText(tNab1.getPgm());

        SazbaPraceTextField1.setText(tNab1.getSazba1());
        SazbaPraceTextField2.setText(tNab1.getSazba4());
        SazbaPraceTextField3.setText(tNab1.getSazba5());
        SazbaPripravaTextField1.setText(tNab1.getSazba2());
        SazbaPGMTextField1.setText(tNab1.getSazba3());

        try {
            KusuRocneTextField1.setText(tNab1.getPocetKusuRocne() + "");
        } catch (Exception e) {
            // e.printStackTrace();
        }

        try {
            PovrchTextField1.setText(tNab1.getPovrchUprava());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            BrouseniTextField1.setText(tNab1.getBrouseni());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            PoznamkaTextField1.setText(tNab1.getPoznamka());
        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

    private void ulozNabidku() {
        if (!upravenaCenaKusTextField1.getText().trim().isEmpty()) {
            tNab1.setCenaKus(Float.valueOf(upravenaCenaKusTextField1.getText().trim().replaceAll(",", ".")).floatValue() + "");
        }
        MikronIS2.indexZakaznika = ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo();

        if (edit == false) {
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(nabidky_id)+1,1) FROM spolecne.nabidky");
                while (id.next()) {
                    tNab1.setIdNabidky(id.getLong(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tNab1.setIdNabidky(this.idNabidka);
        }

        if (ZakaznikComboBox1.getSelectedIndex() > -1) {
            tNab1.setIdZakaznik(((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
        } else {
            tNab1.setIdZakaznik(0);
        }
        int idMena;
        if (MenaComboBox1.getSelectedIndex() > -1) {
            tNab1.setIdMena(((DvojiceCisloRetez) roletkaModelMeny.getSelectedItem()).cislo());
        } else {
            tNab1.setIdMena(0);
        }

        String dotazVykres = "SELECT vykresy_id "
                + "FROM spolecne.vykresy "
                + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "' ";
        if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
            dotazVykres += "AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
        }
        try {
            ResultSet vykresCisloDotaz = PripojeniDB.dotazS(dotazVykres);
            while (vykresCisloDotaz.next()) {
                // vsVykresyZmeny = new Vector();
                tNab1.setIdVykres((vykresCisloDotaz.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BrouseniTextField1.getText().trim().isEmpty()) {
            BrouseniTextField1.setText("0");
        }
        if (PovrchTextField1.getText().trim().isEmpty()) {
            PovrchTextField1.setText("0");
        }
        if (KusuRocneTextField1.getText().trim().isEmpty()) {
            KusuRocneTextField1.setText("0");
        }
        try {
            tNab1.setDatum(df.parse(DatumTextField1.getText().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tNab1.setPocetObjednanychKusu(Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue());
        tNab1.setIdPolotovar(((DvojiceCisloRetez) roletkaModelTvar.getSelectedItem()).cislo());
        tNab1.setCenaKg(CenaMaterialTextField1.getText().trim());
        tNab1.setMaterialMnozstvi(MnozstviTextField1.getText().trim());
        tNab1.setPrace(PraceTextField1.getText().trim());
        tNab1.setPrace2(PraceTextField2.getText().trim());
        tNab1.setPrace3(PraceTextField3.getText().trim());
        tNab1.setPriprava(PripravaTextField1.getText().trim());
        tNab1.setPgm(PGMTextField1.getText().trim());
        tNab1.setSazba1(SazbaPraceTextField1.getText().trim());
        tNab1.setSazba2(SazbaPripravaTextField1.getText().trim());
        tNab1.setSazba3(SazbaPGMTextField1.getText().trim());
        tNab1.setSazba4(SazbaPraceTextField2.getText().trim());
        tNab1.setSazba5(SazbaPraceTextField3.getText().trim());
        tNab1.setPocetKusuRocne(Integer.valueOf(KusuRocneTextField1.getText().trim()).intValue());
        tNab1.setBrouseni(BrouseniTextField1.getText().trim());
        tNab1.setPoznamka(PoznamkaTextField1.getText().trim());
        tNab1.setPovrchUprava(PovrchTextField1.getText().trim());

        if (edit == false) {
            tNab1.insertData();
        } else {
            tNab1.updateData();
        }

    }

    private void kontrolaVykresu() {
        try {

            arVykresyZmeny.clear();
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
                arVykresyZmeny.add(vykresZmena);
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
                kusyNavicText += (kusyNavic1.getString(1) == null) ? "0" : kusyNavic1.getString(1);

            }
            KusuNavicLabel1.setText(kusyNavicText);

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);

        } // konec catch      

        if (arVykresyZmeny.size() < 1) {
            // System.out.println("nenalezeno");
            NazevSoucastiTextField1.setText("");
            NovyVykresFrame novyVykres = new NovyVykresFrame(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim(), false);
        }
    }

    private void vypocetNabidka() {
        float pocetKusu = Float.valueOf(PocetKusuTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        float pocetKusuRocne;
        if (KusuRocneTextField1.getText().trim().isEmpty() || KusuRocneTextField1.getText().trim().equals("0")) {
            pocetKusuRocne = pocetKusu;
        } else {
            pocetKusuRocne = Float.valueOf(KusuRocneTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        }
        TridaPolotovar1 tp1 = new TridaPolotovar1(((DvojiceCisloRetez) roletkaModelTvar.getSelectedItem()).cislo());

        System.out.println("NN : " + tp1.getMernaHmotnost());
        if (tp1.getMernaHmotnost().isEmpty()) {
            tp1.setMernaHmotnost("0");
        }
        float cenaCelkem = Float.valueOf(MnozstviTextField1.getText().trim().replaceAll(",", ".")).floatValue()
                * Float.valueOf(tp1.getMernaHmotnost()).floatValue()
                * Float.valueOf(CenaMaterialTextField1.getText().trim().replaceAll(",", ".")).floatValue();

        float cinnostSazba = 0;
        if (PraceTextField1.getText().trim().isEmpty() == false && SazbaPraceTextField1.getText().trim().isEmpty() == false) {
            cinnostSazba += (Float.valueOf(PraceTextField1.getText().trim().replaceAll(",", ".")).floatValue()
                    * Float.valueOf(SazbaPraceTextField1.getText().trim().replaceAll(",", ".")).floatValue());
        }
        if (PraceTextField2.getText().trim().isEmpty() == false && SazbaPraceTextField2.getText().trim().isEmpty() == false) {
            cinnostSazba += (Float.valueOf(PraceTextField2.getText().trim().replaceAll(",", ".")).floatValue()
                    * Float.valueOf(SazbaPraceTextField2.getText().trim().replaceAll(",", ".")).floatValue());
        }
        if (PraceTextField3.getText().trim().isEmpty() == false && SazbaPraceTextField3.getText().trim().isEmpty() == false) {
            cinnostSazba += (Float.valueOf(PraceTextField3.getText().trim().replaceAll(",", ".")).floatValue()
                    * Float.valueOf(SazbaPraceTextField3.getText().trim().replaceAll(",", ".")).floatValue());
        }
        if (PripravaTextField1.getText().trim().isEmpty() == false && SazbaPripravaTextField1.getText().trim().isEmpty() == false) {
            cinnostSazba += ((Float.valueOf(PripravaTextField1.getText().trim().replaceAll(",", ".")).floatValue()
                    * Float.valueOf(SazbaPripravaTextField1.getText().trim().replaceAll(",", ".")).floatValue())
                    / pocetKusu);
        }
        if (PGMTextField1.getText().trim().isEmpty() == false && SazbaPGMTextField1.getText().trim().isEmpty() == false) {
            cinnostSazba += ((Float.valueOf(PGMTextField1.getText().trim().replaceAll(",", ".")).floatValue()
                    * Float.valueOf(SazbaPGMTextField1.getText().trim().replaceAll(",", ".")).floatValue())
                    / pocetKusuRocne);
        }
        float brouseni = 0;
        if (!BrouseniTextField1.getText().trim().isEmpty()) {
            brouseni = Float.valueOf(BrouseniTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        }
        float povrch = 0;

        if (!PovrchTextField1.getText().trim().isEmpty()) {
            povrch = Float.valueOf(PovrchTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        }
        float cenaKus = cenaCelkem + cinnostSazba
                + +povrch + brouseni;
        float celkemMinut = (Float.valueOf(PraceTextField1.getText().trim().replaceAll(",", ".")).floatValue()
                + Float.valueOf(PraceTextField2.getText().trim().replaceAll(",", ".")).floatValue())
                * pocetKusu
                + Float.valueOf(PripravaTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        //+ Float.valueOf(PGMTextField1.getText().trim().replaceAll(",", ".")).floatValue();
        celkovyCasTextField1.setText((Math.round((celkemMinut / 60.0) * 100)) / 100.0 + "");
        if (PraceTextField1.getText().isEmpty() == false) {
            CelkovyCas1TextField.setText((Math.round((((Float.valueOf(PraceTextField1.getText().trim().replaceAll(",", ".")).floatValue())
                    * pocetKusu) / 60.0) * 100)) / 100.0 + "");
        }
        if (PraceTextField2.getText().isEmpty() == false) {
            CelkovyCas2TextField.setText((Math.round((((Float.valueOf(PraceTextField2.getText().trim().replaceAll(",", ".")).floatValue())
                    * pocetKusu) / 60.0) * 100)) / 100.0 + "");
        }
        if (PraceTextField3.getText().isEmpty() == false) {
            CelkovyCas3TextField.setText((Math.round((((Float.valueOf(PraceTextField3.getText().trim().replaceAll(",", ".")).floatValue())
                    * pocetKusu) / 60.0) * 100)) / 100.0 + "");
        }
        cenaKusTextField1.setText((Math.round((cenaKus) * 100)) / 100.0 + "");
        System.out.println(" Cena celkem : " + cenaCelkem);
        cenaMaterialKusTextField1.setText(cenaCelkem + "");

    }

    class WinUdalosti implements WindowListener {

        public void windowClosing(WindowEvent e) {
            //displayMessage("WindowListener method called: windowClosing.");

            //A pause so user can see the message before
            //the window actually closes.
            ActionListener task = new ActionListener() {
                boolean alreadyDisposed = false;

                public void actionPerformed(ActionEvent e) {
                    if (!alreadyDisposed) {
                        alreadyDisposed = true;
                        // frame.dispose();
                    } else { //make sure the program exits
                        System.exit(0);
                    }
                }
            };
            //Timer timer = new Timer(500, task); //fire every half second
            //timer.setInitialDelay(2000);        //first delay 2 seconds
            //timer.start();
        }

        public void windowClosed(WindowEvent e) {
            if (e.getSource().getClass().getSimpleName().equals("NovyVykresFrame")) {
                TridaVykres1 tv1 = new TridaVykres1(MikronIS2.indexVykres);
                tNab1.setTv1(tv1);

                CisloVykresuTextField1.setText(tv1.getCislo());
                ZmenaVykresuTextField1.setText(tv1.getRevize());
                NazevSoucastiTextField1.setText(tv1.getNazev());
            }

            //displayMessage("WindowListener method called: windowClosed.");
        }

        public void windowOpened(WindowEvent e) {
            //displayMessage("WindowListener method called: windowOpened.");
        }

        public void windowIconified(WindowEvent e) {
            // displayMessage("WindowListener method called: windowIconified.");
        }

        public void windowDeiconified(WindowEvent e) {
            // displayMessage("WindowListener method called: windowDeiconified.");
        }

        public void windowActivated(WindowEvent e) {
            // displayMessage("WindowListener method called: windowActivated.");
        }

        public void windowDeactivated(WindowEvent e) {
            // displayMessage("WindowListener method called: windowDeactivated.");
        }

        public void windowGainedFocus(WindowEvent e) {
            // displayMessage("WindowFocusListener method called: windowGainedFocus.");
        }

        public void windowLostFocus(WindowEvent e) {
            // displayMessage("WindowFocusListener method called: windowLostFocus.");
        }

        public void windowStateChanged(WindowEvent e) {
            // displayMessage("WindowStateListener method called: windowStateChanged.");
        }

        void displayMessage(String msg) {
            System.out.println(msg);
        }
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
        jBUlozit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();
        ErrorLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ZakaznikComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        DatumTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        PocetKusuTextField1 = new javax.swing.JTextField();
        MaterialBBHLabel1 = new javax.swing.JLabel();
        KusuRocneTextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        jBNovyVykres = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        ZmenaVykresuTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        NazevSoucastiTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        KusuNavicLabel1 = new javax.swing.JLabel();
        jBObnovit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        MaterialComboBox1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        MnozstviTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        TvarUpravaComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        CenaMaterialTextField1 = new javax.swing.JTextField();
        MenaComboBox1 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jCBSkupina = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        PoznamkaTextField1 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
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
        PGMTextField1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        cenaMaterialKusTextField1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        CelkovyCas1TextField = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        CelkovyCas2TextField = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        CelkovyCas3TextField = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        celkovyCasTextField1 = new javax.swing.JTextField();
        MenaComboBox2 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        upravenaCenaKusTextField1 = new javax.swing.JTextField();
        cenaKusTextField1 = new javax.swing.JTextField();
        jBVypocet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setMinimumSize(new java.awt.Dimension(1090, 528));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 508));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jBUlozit.setText("Uložit nabídku");
        jBUlozit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBUlozitMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 28, 0);
        jPanel1.add(jBUlozit, gridBagConstraints);

        jBZrusit.setText("Zavřít");
        jBZrusit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBZrusitMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 28, 0);
        jPanel1.add(jBZrusit, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(ErrorLabel, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Základní údaje"));
        jPanel2.setMinimumSize(new java.awt.Dimension(490, 95));
        jPanel2.setPreferredSize(new java.awt.Dimension(490, 95));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        ZakaznikComboBox1.setNextFocusableComponent(DatumTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(ZakaznikComboBox1, gridBagConstraints);

        jLabel2.setText("Datum :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        DatumTextField1.setNextFocusableComponent(PocetKusuTextField1);
        DatumTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                DatumTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(DatumTextField1, gridBagConstraints);

        jLabel3.setText("Kusy :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        PocetKusuTextField1.setNextFocusableComponent(KusuRocneTextField1);
        PocetKusuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PocetKusuTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel2.add(PocetKusuTextField1, gridBagConstraints);

        MaterialBBHLabel1.setText("Ročně :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(MaterialBBHLabel1, gridBagConstraints);

        KusuRocneTextField1.setNextFocusableComponent(CisloVykresuTextField1);
        KusuRocneTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                KusuRocneTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel2.add(KusuRocneTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = -27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Výkres"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel5, gridBagConstraints);

        CisloVykresuTextField1.setNextFocusableComponent(ZmenaVykresuTextField1);
        CisloVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloVykresuTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(CisloVykresuTextField1, gridBagConstraints);

        jBNovyVykres.setText("Nový");
        jBNovyVykres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBNovyVykresMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel3.add(jBNovyVykres, gridBagConstraints);

        jLabel6.setText("Změna :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel6, gridBagConstraints);

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(ZmenaVykresuTextField1, gridBagConstraints);

        jLabel7.setText("Název součásti :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel7, gridBagConstraints);

        NazevSoucastiTextField1.setNextFocusableComponent(MaterialComboBox1);
        NazevSoucastiTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                NazevSoucastiTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(NazevSoucastiTextField1, gridBagConstraints);

        jLabel8.setText("Ks navíc ve výrobě :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel8, gridBagConstraints);

        KusuNavicLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(KusuNavicLabel1, gridBagConstraints);

        jBObnovit.setText("Obnovit");
        jBObnovit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBObnovitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel3.add(jBObnovit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 255;
        gridBagConstraints.ipady = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Materiál"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel11.setText("Druh :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel11, gridBagConstraints);

        MaterialComboBox1.setMaximumRowCount(12);
        MaterialComboBox1.setNextFocusableComponent(TvarUpravaComboBox1);
        MaterialComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MaterialComboBox1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(MaterialComboBox1, gridBagConstraints);

        jLabel12.setText("Délka[m] :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel12, gridBagConstraints);

        MnozstviTextField1.setNextFocusableComponent(CenaMaterialTextField1);
        MnozstviTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MnozstviTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(MnozstviTextField1, gridBagConstraints);

        jLabel13.setText("Rozměr :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel13, gridBagConstraints);

        TvarUpravaComboBox1.setMaximumRowCount(12);
        TvarUpravaComboBox1.setNextFocusableComponent(MnozstviTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(TvarUpravaComboBox1, gridBagConstraints);

        jLabel4.setText("cena za kg :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel4, gridBagConstraints);

        CenaMaterialTextField1.setNextFocusableComponent(MenaComboBox1);
        CenaMaterialTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CenaMaterialTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.9;
        jPanel4.add(CenaMaterialTextField1, gridBagConstraints);

        MenaComboBox1.setNextFocusableComponent(PraceTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel4.add(MenaComboBox1, gridBagConstraints);

        jLabel14.setText("Skupina :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel14, gridBagConstraints);

        jCBSkupina.setMaximumRowCount(12);
        jCBSkupina.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBSkupinaItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jCBSkupina, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 322;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel4, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Další"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel29.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel29, gridBagConstraints);

        PoznamkaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PoznamkaTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(PoznamkaTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 370;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel5, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Práce a sazby"));
        jPanel7.setMinimumSize(new java.awt.Dimension(360, 247));
        jPanel7.setPreferredSize(new java.awt.Dimension(360, 247));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel25.setText("Práce 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel25, gridBagConstraints);

        jLabel30.setText("Práce 3:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel30, gridBagConstraints);

        jLabel31.setText("Práce 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel31, gridBagConstraints);

        jLabel32.setText("Příprava:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel32, gridBagConstraints);

        jLabel33.setText("PGM:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel33, gridBagConstraints);

        jLabel34.setText("Povrchová úprava:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel34, gridBagConstraints);

        jLabel35.setText("Sazba :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 35);
        jPanel7.add(jLabel35, gridBagConstraints);

        jLabel36.setText("Broušení:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel7.add(jLabel36, gridBagConstraints);

        jLabel37.setText("Čas[min]");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 35);
        jPanel7.add(jLabel37, gridBagConstraints);

        PraceTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PraceTextField1.setNextFocusableComponent(SazbaPraceTextField1);
        PraceTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PraceTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PraceTextField1, gridBagConstraints);

        PraceTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PraceTextField2.setNextFocusableComponent(SazbaPraceTextField2);
        PraceTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PraceTextField2FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PraceTextField2, gridBagConstraints);

        PraceTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PraceTextField3.setNextFocusableComponent(SazbaPraceTextField3);
        PraceTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PraceTextField3FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PraceTextField3, gridBagConstraints);

        SazbaPraceTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SazbaPraceTextField1.setNextFocusableComponent(PraceTextField2);
        SazbaPraceTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SazbaPraceTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(SazbaPraceTextField1, gridBagConstraints);

        SazbaPraceTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SazbaPraceTextField2.setNextFocusableComponent(PraceTextField3);
        SazbaPraceTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SazbaPraceTextField2FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(SazbaPraceTextField2, gridBagConstraints);

        SazbaPraceTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SazbaPraceTextField3.setNextFocusableComponent(PripravaTextField1);
        SazbaPraceTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SazbaPraceTextField3FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(SazbaPraceTextField3, gridBagConstraints);

        PripravaTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PripravaTextField1.setNextFocusableComponent(SazbaPripravaTextField1);
        PripravaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PripravaTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PripravaTextField1, gridBagConstraints);

        SazbaPripravaTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SazbaPripravaTextField1.setNextFocusableComponent(PGMTextField1);
        SazbaPripravaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SazbaPripravaTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(SazbaPripravaTextField1, gridBagConstraints);

        SazbaPGMTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        SazbaPGMTextField1.setNextFocusableComponent(PovrchTextField1);
        SazbaPGMTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SazbaPGMTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(SazbaPGMTextField1, gridBagConstraints);

        PovrchTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PovrchTextField1.setNextFocusableComponent(BrouseniTextField1);
        PovrchTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PovrchTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PovrchTextField1, gridBagConstraints);

        BrouseniTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        BrouseniTextField1.setNextFocusableComponent(upravenaCenaKusTextField1);
        BrouseniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                BrouseniTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                BrouseniTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(BrouseniTextField1, gridBagConstraints);

        PGMTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PGMTextField1.setNextFocusableComponent(SazbaPGMTextField1);
        PGMTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PGMTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(PGMTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = -33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        jPanel1.add(jPanel7, gridBagConstraints);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Výsledky"));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel38.setText("Cena materiál za kus:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel38, gridBagConstraints);

        cenaMaterialKusTextField1.setBackground(new java.awt.Color(184, 241, 241));
        cenaMaterialKusTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cenaMaterialKusTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cenaMaterialKusTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(cenaMaterialKusTextField1, gridBagConstraints);

        jLabel39.setText("Celkový čas 1:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel39, gridBagConstraints);

        CelkovyCas1TextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(CelkovyCas1TextField, gridBagConstraints);

        jLabel40.setText("Celkový čas 2:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel40, gridBagConstraints);

        CelkovyCas2TextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(CelkovyCas2TextField, gridBagConstraints);

        jLabel41.setText("Celkový čas 3:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel41, gridBagConstraints);

        CelkovyCas3TextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(CelkovyCas3TextField, gridBagConstraints);

        jLabel42.setText("Celkový čas:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel42, gridBagConstraints);

        celkovyCasTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(celkovyCasTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(MenaComboBox2, gridBagConstraints);

        jLabel9.setText("Cena za kus:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Upravená cena:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jLabel10, gridBagConstraints);

        upravenaCenaKusTextField1.setBackground(new java.awt.Color(255, 255, 204));
        upravenaCenaKusTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        upravenaCenaKusTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                upravenaCenaKusTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        jPanel6.add(upravenaCenaKusTextField1, gridBagConstraints);

        cenaKusTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(cenaKusTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 158;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel6, gridBagConstraints);

        jBVypocet.setText("Výpočet");
        jBVypocet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBVypocetMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 28, 0);
        jPanel1.add(jBVypocet, gridBagConstraints);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBVypocetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVypocetMouseClicked
        vypocetNabidka();
    }//GEN-LAST:event_jBVypocetMouseClicked

    private void jBUlozitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBUlozitMouseClicked
        ulozNabidku();
        this.dispose();
    }//GEN-LAST:event_jBUlozitMouseClicked

    private void jBZrusitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBZrusitMouseClicked
        this.dispose();
    }//GEN-LAST:event_jBZrusitMouseClicked

    private void ZmenaVykresuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusLost
        kontrolaVykresu();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusLost

    private void jBNovyVykresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBNovyVykresMouseClicked
        NovyVykresFrame novyVykres = new NovyVykresFrame(false);
        novyVykres.setZakaznik(((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
        novyVykres.addWindowListener(winUdalosti);
    }//GEN-LAST:event_jBNovyVykresMouseClicked

    private void MaterialComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MaterialComboBox1ItemStateChanged
        initRoletkaTvar();
    }//GEN-LAST:event_MaterialComboBox1ItemStateChanged

    private void DatumTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DatumTextField1FocusGained
        DatumTextField1.selectAll();
    }//GEN-LAST:event_DatumTextField1FocusGained

    private void PocetKusuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetKusuTextField1FocusGained
        PocetKusuTextField1.selectAll();
    }//GEN-LAST:event_PocetKusuTextField1FocusGained

    private void KusuRocneTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_KusuRocneTextField1FocusGained
        KusuRocneTextField1.selectAll();
    }//GEN-LAST:event_KusuRocneTextField1FocusGained

    private void CisloVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloVykresuTextField1FocusGained
        CisloVykresuTextField1.selectAll();
    }//GEN-LAST:event_CisloVykresuTextField1FocusGained

    private void ZmenaVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusGained
        ZmenaVykresuTextField1.selectAll();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusGained

    private void NazevSoucastiTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NazevSoucastiTextField1FocusGained
        NazevSoucastiTextField1.selectAll();
    }//GEN-LAST:event_NazevSoucastiTextField1FocusGained

    private void MnozstviTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MnozstviTextField1FocusGained
        MnozstviTextField1.selectAll();
    }//GEN-LAST:event_MnozstviTextField1FocusGained

    private void CenaMaterialTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CenaMaterialTextField1FocusGained
        CenaMaterialTextField1.selectAll();
    }//GEN-LAST:event_CenaMaterialTextField1FocusGained

    private void PoznamkaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PoznamkaTextField1FocusGained
        PoznamkaTextField1.selectAll();
    }//GEN-LAST:event_PoznamkaTextField1FocusGained

    private void PraceTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PraceTextField1FocusGained
        PraceTextField1.selectAll();
    }//GEN-LAST:event_PraceTextField1FocusGained

    private void SazbaPraceTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SazbaPraceTextField1FocusGained
        SazbaPraceTextField1.selectAll();
    }//GEN-LAST:event_SazbaPraceTextField1FocusGained

    private void PraceTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PraceTextField2FocusGained
        PraceTextField2.selectAll();
    }//GEN-LAST:event_PraceTextField2FocusGained

    private void SazbaPraceTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SazbaPraceTextField2FocusGained
        SazbaPraceTextField2.selectAll();
    }//GEN-LAST:event_SazbaPraceTextField2FocusGained

    private void PraceTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PraceTextField3FocusGained
        PraceTextField3.selectAll();
    }//GEN-LAST:event_PraceTextField3FocusGained

    private void SazbaPraceTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SazbaPraceTextField3FocusGained
        SazbaPraceTextField3.selectAll();
    }//GEN-LAST:event_SazbaPraceTextField3FocusGained

    private void PripravaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PripravaTextField1FocusGained
        PripravaTextField1.selectAll();
    }//GEN-LAST:event_PripravaTextField1FocusGained

    private void SazbaPripravaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SazbaPripravaTextField1FocusGained
        SazbaPripravaTextField1.selectAll();
    }//GEN-LAST:event_SazbaPripravaTextField1FocusGained

    private void PGMTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PGMTextField1FocusGained
        PGMTextField1.selectAll();
    }//GEN-LAST:event_PGMTextField1FocusGained

    private void SazbaPGMTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SazbaPGMTextField1FocusGained
        SazbaPGMTextField1.selectAll();
    }//GEN-LAST:event_SazbaPGMTextField1FocusGained

    private void PovrchTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PovrchTextField1FocusGained
        PovrchTextField1.selectAll();
    }//GEN-LAST:event_PovrchTextField1FocusGained

    private void BrouseniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BrouseniTextField1FocusGained
        BrouseniTextField1.selectAll();
    }//GEN-LAST:event_BrouseniTextField1FocusGained

    private void cenaMaterialKusTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cenaMaterialKusTextField1FocusGained
        CenaMaterialTextField1.selectAll();
    }//GEN-LAST:event_cenaMaterialKusTextField1FocusGained

    private void upravenaCenaKusTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_upravenaCenaKusTextField1FocusGained
        upravenaCenaKusTextField1.selectAll();
    }//GEN-LAST:event_upravenaCenaKusTextField1FocusGained

    private void jCBSkupinaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBSkupinaItemStateChanged
        initRoletkaMaterial();
    }//GEN-LAST:event_jCBSkupinaItemStateChanged

    private void jBObnovitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBObnovitActionPerformed
        kontrolaVykresu();
    }//GEN-LAST:event_jBObnovitActionPerformed

    private void BrouseniTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BrouseniTextField1FocusLost
        vypocetNabidka();
    }//GEN-LAST:event_BrouseniTextField1FocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BrouseniTextField1;
    private javax.swing.JTextField CelkovyCas1TextField;
    private javax.swing.JTextField CelkovyCas2TextField;
    private javax.swing.JTextField CelkovyCas3TextField;
    private javax.swing.JTextField CenaMaterialTextField1;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JTextField DatumTextField1;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JLabel KusuNavicLabel1;
    private javax.swing.JTextField KusuRocneTextField1;
    private javax.swing.JLabel MaterialBBHLabel1;
    private javax.swing.JComboBox MaterialComboBox1;
    private javax.swing.JComboBox MenaComboBox1;
    private javax.swing.JComboBox MenaComboBox2;
    private javax.swing.JTextField MnozstviTextField1;
    private javax.swing.JTextField NazevSoucastiTextField1;
    private javax.swing.JTextField PGMTextField1;
    private javax.swing.JTextField PocetKusuTextField1;
    private javax.swing.JTextField PovrchTextField1;
    private javax.swing.JTextField PoznamkaTextField1;
    private javax.swing.JTextField PraceTextField1;
    private javax.swing.JTextField PraceTextField2;
    private javax.swing.JTextField PraceTextField3;
    private javax.swing.JTextField PripravaTextField1;
    private javax.swing.JTextField SazbaPGMTextField1;
    private javax.swing.JTextField SazbaPraceTextField1;
    private javax.swing.JTextField SazbaPraceTextField2;
    private javax.swing.JTextField SazbaPraceTextField3;
    private javax.swing.JTextField SazbaPripravaTextField1;
    private javax.swing.JComboBox TvarUpravaComboBox1;
    private javax.swing.JComboBox ZakaznikComboBox1;
    private javax.swing.JTextField ZmenaVykresuTextField1;
    private javax.swing.JTextField celkovyCasTextField1;
    private javax.swing.JTextField cenaKusTextField1;
    private javax.swing.JTextField cenaMaterialKusTextField1;
    private javax.swing.JButton jBNovyVykres;
    private javax.swing.JButton jBObnovit;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JButton jBVypocet;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JComboBox jCBSkupina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField upravenaCenaKusTextField1;
    // End of variables declaration//GEN-END:variables
}
