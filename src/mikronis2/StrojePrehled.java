/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaZaznam1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class StrojePrehled extends javax.swing.JPanel {

    private RoletkaUniverzalModel1 roletkaModelProgramovani, roletkaModelSoustruh, roletkaModelFrezka, roletkaModelVrtacka;
    protected Vector vrPrace1;
    protected Vector vsPrace1;
    protected Vector vrStroje1;
    protected Vector vsStroje1;
    protected Vector vrZaznamy1;
    protected Vector v;
    protected java.text.NumberFormat nf2, nf3;
    protected int druhZaznamu;
    protected TridaObjednavka1 objednavka;
    protected ActionListener alUdalosti;

    /**
     * Creates new form StrojePrehled
     */
    public StrojePrehled() {
        initComponents();
        nastavParametry();
        initStroje();
        initRoletky();
        nastavTridyObsluhyUdalosti();
        nastavPosluchaceUdalostiOvladace();

        getData();

    }

    protected void nastavTridyObsluhyUdalosti() {
        alUdalosti = new ALUdalosti();
    }

    private void nastavParametry() {
        vrPrace1 = new Vector();
        vsPrace1 = new Vector();

        vrStroje1 = new Vector();
        vsStroje1 = new Vector();

        vrZaznamy1 = new Vector();

        v = new Vector();

        druhZaznamu = -1;
        objednavka = new TridaObjednavka1();
        nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        nf3 = java.text.NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);
    }

    private void nastavPosluchaceUdalostiOvladace() {

        NovyStrojButton1.addActionListener(alUdalosti);
        UpravitUdajeButton1.addActionListener(alUdalosti);
        ObnovitUdajeButton1.addActionListener(alUdalosti);
        FrezkaComboBox1.addActionListener(alUdalosti);
        ProgramovaniComboBox1.addActionListener(alUdalosti);
        SoustruhComboBox1.addActionListener(alUdalosti);
        VrtackaComboBox1.addActionListener(alUdalosti);

        NovyStrojButton1.setActionCommand("NovyStroj");
        UpravitUdajeButton1.setActionCommand("UpravitStroj");
        ObnovitUdajeButton1.setActionCommand("Refresh");
        FrezkaComboBox1.setActionCommand("ZmenaFrezka");
        ProgramovaniComboBox1.setActionCommand("ZmenaProgramovani");
        SoustruhComboBox1.setActionCommand("ZmenaSoustruh");
        VrtackaComboBox1.setActionCommand("ZmenaVrtacka");

    }

    private void initStroje() {
        try {
            vsStroje1.removeAllElements();
            vrStroje1.removeAllElements();
            ResultSet zakaznik1 = PripojeniDB.dotazS(
                    "SELECT stroje_id, stroje_nazev, stroje_druh_stroje FROM spolecne.stroje ORDER BY stroje_id ASC");

            while (zakaznik1.next()) {
                vsStroje1 = new Vector();
                vsStroje1.add(new Integer(zakaznik1.getInt(1)));
                vsStroje1.add(new String(zakaznik1.getString(2)));
                vsStroje1.add(new Integer(zakaznik1.getInt(3)));
                vrStroje1.add(vsStroje1);
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch  

    }

    private void initRoletky() {
        roletkaModelFrezka = new RoletkaUniverzalModel1(
                "SELECT stroje_id, stroje_nazev FROM spolecne.stroje WHERE stroje_druh_stroje = 8 OR stroje_druh_stroje = 16 ORDER BY stroje_id ASC ",
                "V databázi nejsou zadány frézky"); // bylo ...vs_id
        FrezkaComboBox1.setModel(roletkaModelFrezka);

        roletkaModelProgramovani = new RoletkaUniverzalModel1(
                "SELECT stroje_id, stroje_nazev FROM spolecne.stroje WHERE stroje_druh_stroje = 10 ORDER BY stroje_id ASC ",
                "V databázi nejsou zadány programovací stanice"); // bylo ...vs_id
        ProgramovaniComboBox1.setModel(roletkaModelProgramovani);

        roletkaModelSoustruh = new RoletkaUniverzalModel1(
                "SELECT stroje_id, stroje_nazev FROM spolecne.stroje WHERE stroje_druh_stroje = 4 ORDER BY stroje_id ASC",
                "V databázi nejsou zadány soustruhy"); // bylo ...vs_id
        SoustruhComboBox1.setModel(roletkaModelSoustruh);

        roletkaModelVrtacka = new RoletkaUniverzalModel1(
                "SELECT stroje_id, stroje_nazev FROM spolecne.stroje WHERE stroje_druh_stroje = 6 ORDER BY stroje_id ASC",
                "V databázi nejsou zadány vrtačky"); // bylo ...vs_id
        VrtackaComboBox1.setModel(roletkaModelVrtacka);
    }

    private void getData() {
        int stroje_id = (Integer) ((Vector) vrStroje1.get(0)).get(0);
        RichmondLabel1.setText((String) ((Vector) vrStroje1.get(0)).get(1));
        RichmondTextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            RichmondLabel2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            RichmondLabel2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            RichmondLabel2.setText("Stroj vypnutý");
        } else {
            RichmondLabel2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(1)).get(0);
        Storm2Label1.setText((String) ((Vector) vrStroje1.get(1)).get(1));
        Storm2TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            Storm2Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            Storm2Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            Storm2Label2.setText("Stroj vypnutý");
        } else {
            Storm2Label2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(2)).get(0);
        MCV754Label1.setText((String) ((Vector) vrStroje1.get(2)).get(1));
        MCV754TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            MCV754Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            MCV754Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            MCV754Label2.setText("Stroj vypnutý");
        } else {
            MCV754Label2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(3)).get(0);
        Storm1Label1.setText((String) ((Vector) vrStroje1.get(3)).get(1));
        Storm1TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            Storm1Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            Storm1Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            Storm1Label2.setText("Stroj vypnutý");
        } else {
            Storm1Label2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(31)).get(0);
        DMU602Label1.setText((String) ((Vector) vrStroje1.get(31)).get(1));
        DMU602TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            DMU602Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            DMU602Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            DMU602Label2.setText("Stroj vypnutý");
        } else {
            DMU602Label2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(5)).get(0);
        MCV750Label1.setText((String) ((Vector) vrStroje1.get(5)).get(1));
        MCV750TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            MCV750Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            MCV750Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            MCV750Label2.setText("Stroj vypnutý");
        } else {
            MCV750Label2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(6)).get(0);
        Tornado2NLabel1.setText((String) ((Vector) vrStroje1.get(6)).get(1));
        Tornado2NTextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            Tornado2NLabel2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            Tornado2NLabel2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            Tornado2NLabel2.setText("Stroj vypnutý");
        } else {
            Tornado2NLabel2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(7)).get(0);
        Tornado2SLabel1.setText((String) ((Vector) vrStroje1.get(7)).get(1));
        Tornado2STextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            Tornado2SLabel2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            Tornado2SLabel2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            Tornado2SLabel2.setText("Stroj vypnutý");
        } else {
            Tornado2SLabel2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(8)).get(0);
        Tornado1Label1.setText((String) ((Vector) vrStroje1.get(8)).get(1));
        Tornado1TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            Tornado1Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            Tornado1Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            Tornado1Label2.setText("Stroj vypnutý");
        } else {
            Tornado1Label2.setText("");
        }
        stroje_id = (Integer) ((Vector) vrStroje1.get(9)).get(0);
        MasturnLabel1.setText((String) ((Vector) vrStroje1.get(9)).get(1));
        MasturnTextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            MasturnLabel2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            MasturnLabel2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            MasturnLabel2.setText("Stroj vypnutý");
        } else {
            MasturnLabel2.setText("");
        }

        stroje_id = (Integer) ((Vector) vrStroje1.get(28)).get(0);
        DMU60Label1.setText((String) ((Vector) vrStroje1.get(28)).get(1));
        DMU60TextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            DMU60Label2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            DMU60Label2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            DMU60Label2.setText("Stroj vypnutý");
        } else {
            DMU60Label2.setText("");
        }
        
        stroje_id = (Integer) ((Vector) vrStroje1.get(30)).get(0);
        DratakLabel1.setText((String) ((Vector) vrStroje1.get(30)).get(1));
        DratakTextArea1.setText(nactiPosledniTransakci(stroje_id));
        if (druhZaznamu == 4) {
            DratakLabel2.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            DratakLabel2.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            DratakLabel2.setText("Stroj vypnutý");
        } else {
            DratakLabel2.setText("");
        }

        zjistiOstatniStroje();

    }

    private void zjistiOstatniStroje() {        
        ProgramovaniTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelProgramovani.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            ProgramovaniLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            ProgramovaniLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            ProgramovaniLabel1.setText("Stroj vypnutý");
        } else {
            ProgramovaniLabel1.setText("");
        }
       
        VrtackaTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelVrtacka.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            VrtackaLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            VrtackaLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            VrtackaLabel1.setText("Stroj vypnutý");
        } else {
            VrtackaLabel1.setText("");
        }
      
        FrezkaTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelFrezka.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            FrezkaLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            FrezkaLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            FrezkaLabel1.setText("Stroj vypnutý");
        } else {
            FrezkaLabel1.setText("");
        }
 
        SoustruhTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelSoustruh.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            SoustruhLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            SoustruhLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            SoustruhLabel1.setText("Stroj vypnutý");
        } else {
            SoustruhLabel1.setText("");
        }
    }

    private void zmenaFrezka() {
        FrezkaTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelFrezka.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            FrezkaLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            FrezkaLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            FrezkaLabel1.setText("Stroj vypnutý");
        } else {
            FrezkaLabel1.setText("");
        }
    }

    private void zmenaProgramovani() {
        System.out.println("zmena programovani");
        ProgramovaniTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelProgramovani.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            ProgramovaniLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            ProgramovaniLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            ProgramovaniLabel1.setText("Stroj vypnutý");
        } else {
            ProgramovaniLabel1.setText("");
        }
    }

    private void zmenaSoustruh() {
        SoustruhTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelVrtacka.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            SoustruhLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            SoustruhLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            SoustruhLabel1.setText("Stroj vypnutý");
        } else {
            SoustruhLabel1.setText("");
        }
    }

    private void zmenaVrtacka() {
        VrtackaTextArea1.setText(nactiPosledniTransakci(((DvojiceCisloRetez) roletkaModelVrtacka.getSelectedItem()).cislo()));
        if (druhZaznamu == 4) {
            VrtackaLabel1.setText("Stroj neaktivní");
        } else if (druhZaznamu == 2) {
            VrtackaLabel1.setText("Přerušení práce");
        } else if (druhZaznamu == 6) {
            VrtackaLabel1.setText("Stroj vypnutý");
        } else {
            VrtackaLabel1.setText("");
        }
    }

    private String nactiPosledniTransakci(int strojeId) {
        TridaZaznam1 aktivniZaznamy = null;
        try {
            ResultSet transakce2 = PripojeniDB.dotazS(
                    "SELECT zamestnanci_stroje_transakce_id, "
                    + "zamestnanci_stroje_transakce_zamestnanci_id, "
                    + "zamestnanci_jmeno || ' ' || zamestnanci_prijmeni AS zamestnanec, "
                    + "zamestnanci_stroje_transakce_druh_id, "
                    + "zamestnanci_stroje_transakce_pruvodky_id, "
                    + "pruvodky_nazev, "
                    + "vykresy_cislo || ' ' || COALESCE(vykresy_revize, '') AS vykres, "
                    + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'HH24:MI DD.MM.YY') AS cas, "
                    + "to_char(age(current_timestamp,zamestnanci_stroje_transakce_log_timestamp), 'DD') AS doba_prace_dny, "
                    + "to_char(age(current_timestamp,zamestnanci_stroje_transakce_log_timestamp), 'HH24:MI') AS doba_prace, "
                    + "current_timestamp "
                    + "FROM spolecne.zamestnanci_stroje_transakce "
                    + "CROSS JOIN spolecne.pruvodky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "CROSS JOIN spolecne.zamestnanci "
                    + "WHERE zamestnanci_stroje_transakce_stroje_id = " + strojeId + " "
                    + "AND pruvodky_id = zamestnanci_stroje_transakce_pruvodky_id "
                    + "AND vykresy_id = pruvodky_cislo_vykresu "
                    + "AND zamestnanci_id = zamestnanci_stroje_transakce_zamestnanci_id "
                    + "ORDER BY zamestnanci_stroje_transakce_log_timestamp DESC "
                    + "LIMIT 1 ");
            while (transakce2.next()) {
                aktivniZaznamy = new TridaZaznam1();
                aktivniZaznamy.setTransakce_id((new Long(transakce2.getLong(1))).longValue());
                aktivniZaznamy.setZamestnanci_id((new Integer(transakce2.getInt(2))).intValue());
                aktivniZaznamy.setZamestnanec(new String(transakce2.getString(3)));
                aktivniZaznamy.setDruh_id((new Integer(transakce2.getInt(4))).intValue());
                aktivniZaznamy.setPruvodky_id((new Long(transakce2.getLong(5))).longValue());
                aktivniZaznamy.setPruvodka_nazev(new String(transakce2.getString(6)));
                aktivniZaznamy.setVykresy_cislo_revize(new String(transakce2.getString(7)));
                aktivniZaznamy.setTimestamp(new String(transakce2.getString(8)));
                aktivniZaznamy.setDoba_prace_dny(Integer.valueOf(transakce2.getString(9)).intValue());
                aktivniZaznamy.setDoba_prace(new String(transakce2.getString(10)));
                aktivniZaznamy.setCurrentTime(transakce2.getTimestamp(11));
                aktivniZaznamy.setStroje_id(strojeId);

            } // konec try
        } catch (Exception e) {
            e.printStackTrace();
            // PripojeniDB.vyjimkaS(e);
        } // konec catch
        Vector vrCasoveZnacky = new Vector();
        Vector vsCasoveZnacky = new Vector();
        int i = 0;
        String zacatekPrace = "";
        try {
            ResultSet transakce3 = PripojeniDB.dotazS(
                    "SELECT zamestnanci_stroje_transakce_id, "
                    + "zamestnanci_stroje_transakce_druh_id, "
                    + "zamestnanci_stroje_transakce_log_timestamp, "
                    + "to_char(zamestnanci_stroje_transakce_log_timestamp, 'HH24:MI DD.MM.YY') AS cas "
                    + "FROM spolecne.zamestnanci_stroje_transakce "
                    + "WHERE zamestnanci_stroje_transakce_stroje_id = " + aktivniZaznamy.getStroje_id() + " "
                    + "AND zamestnanci_stroje_transakce_pruvodky_id = " + aktivniZaznamy.getPruvodky_id() + " "
                    + "AND zamestnanci_stroje_transakce_zamestnanci_id = " + aktivniZaznamy.getZamestnanci_id() + " "
                    + "ORDER BY zamestnanci_stroje_transakce_id ASC");
            while (transakce3.next()) {
                vsCasoveZnacky = new Vector();
                vsCasoveZnacky.add(transakce3.getLong(1));
                vsCasoveZnacky.add(transakce3.getInt(2));
                vsCasoveZnacky.add(transakce3.getTimestamp(3));
                vrCasoveZnacky.add(vsCasoveZnacky);
                if (i == 0) {
                    zacatekPrace = transakce3.getString(4);
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long time135 = 0;
        long time246 = 0;
        for (int index = 0; index < vrCasoveZnacky.size(); index++) {
            if ((index % 2 == 0) && (index != vrCasoveZnacky.size() - 1)) {
                time135 = ((java.sql.Timestamp) ((Vector) vrCasoveZnacky.get(index)).get(2)).getTime();
            } else if (index % 2 == 1) {
                time246 += (((java.sql.Timestamp) ((Vector) vrCasoveZnacky.get(index)).get(2)).getTime() - time135);
                time135 = 0;
            } else if ((index == vrCasoveZnacky.size() - 1) && (index % 2 == 0)) {
                time246 += (aktivniZaznamy.getCurrentTime().getTime() - ((java.sql.Timestamp) ((Vector) vrCasoveZnacky.get(index)).get(2)).getTime());
            }
        }
        Timestamp celkovyCas = new Timestamp(time246);
        int dnu = Integer.valueOf(new SimpleDateFormat("dd").format(celkovyCas)).intValue() - 1;
        int hodin = Integer.valueOf(new SimpleDateFormat("HH").format(celkovyCas)).intValue() - 1;
        String minsec = new SimpleDateFormat("mm").format(celkovyCas);
        String zaznam = "";
        druhZaznamu = aktivniZaznamy.getDruh_id();
        if (aktivniZaznamy.getDruh_id() == 4) {
            zaznam = "";
        } else {
            zaznam =
                    "Zaměstnanec:   " + aktivniZaznamy.getZamestnanec() + "\n"
                    + "Průvodka: \t " + aktivniZaznamy.getPruvodky_id() + "\n"
                    + "Součást: \t " + aktivniZaznamy.getPruvodka_nazev() + "\n"
                    + "Výkres:  \t " + aktivniZaznamy.getVykresy_cislo_revize() + "\n"
                    + "Začátek: \t " + zacatekPrace + "\n";
            if (dnu > 0) {
                zaznam += "Doba: \t " + dnu + " dny " + hodin + ":" + minsec;
            } else {
                zaznam += "Doba: \t " + hodin + ":" + minsec;
            }
        }
        return zaznam;
    }

    class ALUdalosti implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Refresh")) {
                getData();
            }

            if (e.getActionCommand().equals("ZmenaFrezka")) {
                zmenaFrezka();
            }

            if (e.getActionCommand().equals("ZmenaProgramovani")) {
                zmenaProgramovani();
            }

            if (e.getActionCommand().equals("ZmenaSoustruh")) {
                zmenaSoustruh();
            }

            if (e.getActionCommand().equals("ZmenaVrtacka")) {
                zmenaVrtacka();
            }

        }
    } //konec ALUdalosti

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
        RichmondLabel1 = new javax.swing.JLabel();
        RichmondLabel2 = new javax.swing.JLabel();
        Storm2Label1 = new javax.swing.JLabel();
        Storm2Label2 = new javax.swing.JLabel();
        MCV754Label1 = new javax.swing.JLabel();
        MCV754Label2 = new javax.swing.JLabel();
        DMU60Label1 = new javax.swing.JLabel();
        DMU60Label2 = new javax.swing.JLabel();
        Storm1Label1 = new javax.swing.JLabel();
        Storm1Label2 = new javax.swing.JLabel();
        DMU602Label1 = new javax.swing.JLabel();
        DMU602Label2 = new javax.swing.JLabel();
        MCV750Label1 = new javax.swing.JLabel();
        MCV750Label2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        RichmondTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Storm2TextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        MCV754TextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        DMU60TextArea1 = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        Storm1TextArea1 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        DMU602TextArea1 = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        MCV750TextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        VrtackaComboBox1 = new javax.swing.JComboBox();
        FrezkaComboBox1 = new javax.swing.JComboBox();
        SoustruhComboBox1 = new javax.swing.JComboBox();
        ProgramovaniComboBox1 = new javax.swing.JComboBox();
        VrtackaLabel1 = new javax.swing.JLabel();
        FrezkaLabel1 = new javax.swing.JLabel();
        SoustruhLabel1 = new javax.swing.JLabel();
        ProgramovaniLabel1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        VrtackaTextArea1 = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        ProgramovaniTextArea1 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        SoustruhTextArea1 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        FrezkaTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        Tornado2NLabel1 = new javax.swing.JLabel();
        Tornado2NLabel2 = new javax.swing.JLabel();
        Tornado2SLabel1 = new javax.swing.JLabel();
        Tornado2SLabel2 = new javax.swing.JLabel();
        Tornado1Label1 = new javax.swing.JLabel();
        Tornado1Label2 = new javax.swing.JLabel();
        MasturnLabel1 = new javax.swing.JLabel();
        MasturnLabel2 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        Tornado2NTextArea1 = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        Tornado2STextArea1 = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        Tornado1TextArea1 = new javax.swing.JTextArea();
        jScrollPane15 = new javax.swing.JScrollPane();
        MasturnTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        DratakLabel1 = new javax.swing.JLabel();
        DratakLabel2 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        DratakTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        NovyStrojButton1 = new javax.swing.JButton();
        UpravitUdajeButton1 = new javax.swing.JButton();
        ObnovitUdajeButton1 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Frézovací centra"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        RichmondLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RichmondLabel1.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(RichmondLabel1, gridBagConstraints);

        RichmondLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RichmondLabel2.setText("jLabel2");
        jPanel1.add(RichmondLabel2, new java.awt.GridBagConstraints());

        Storm2Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm2Label1.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(Storm2Label1, gridBagConstraints);

        Storm2Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm2Label2.setText("jLabel4");
        jPanel1.add(Storm2Label2, new java.awt.GridBagConstraints());

        MCV754Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV754Label1.setText("jLabel5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(MCV754Label1, gridBagConstraints);

        MCV754Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV754Label2.setText("jLabel6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel1.add(MCV754Label2, gridBagConstraints);

        DMU60Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU60Label1.setText("jLabel7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(DMU60Label1, gridBagConstraints);

        DMU60Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU60Label2.setText("jLabel8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel1.add(DMU60Label2, gridBagConstraints);

        Storm1Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm1Label1.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(Storm1Label1, gridBagConstraints);

        Storm1Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm1Label2.setText("jLabel10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel1.add(Storm1Label2, gridBagConstraints);

        DMU602Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU602Label1.setText("jLabel11");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(DMU602Label1, gridBagConstraints);

        DMU602Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU602Label2.setText("jLabel12");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel1.add(DMU602Label2, gridBagConstraints);

        MCV750Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV750Label1.setText("jLabel13");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(MCV750Label1, gridBagConstraints);

        MCV750Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV750Label2.setText("jLabel14");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        jPanel1.add(MCV750Label2, gridBagConstraints);

        RichmondTextArea1.setEditable(false);
        RichmondTextArea1.setColumns(20);
        RichmondTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RichmondTextArea1.setRows(6);
        jScrollPane1.setViewportView(RichmondTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        Storm2TextArea1.setEditable(false);
        Storm2TextArea1.setColumns(20);
        Storm2TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm2TextArea1.setRows(6);
        jScrollPane2.setViewportView(Storm2TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane2, gridBagConstraints);

        MCV754TextArea1.setEditable(false);
        MCV754TextArea1.setColumns(20);
        MCV754TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV754TextArea1.setRows(6);
        jScrollPane3.setViewportView(MCV754TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        DMU60TextArea1.setEditable(false);
        DMU60TextArea1.setColumns(20);
        DMU60TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU60TextArea1.setRows(6);
        jScrollPane4.setViewportView(DMU60TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane4, gridBagConstraints);

        Storm1TextArea1.setEditable(false);
        Storm1TextArea1.setColumns(20);
        Storm1TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Storm1TextArea1.setRows(6);
        jScrollPane5.setViewportView(Storm1TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane5, gridBagConstraints);

        DMU602TextArea1.setEditable(false);
        DMU602TextArea1.setColumns(20);
        DMU602TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DMU602TextArea1.setRows(6);
        jScrollPane6.setViewportView(DMU602TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane6, gridBagConstraints);

        MCV750TextArea1.setEditable(false);
        MCV750TextArea1.setColumns(20);
        MCV750TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MCV750TextArea1.setRows(6);
        jScrollPane7.setViewportView(MCV750TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.4;
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Ostatni"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        VrtackaComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        VrtackaComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(VrtackaComboBox1, gridBagConstraints);

        FrezkaComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        FrezkaComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(FrezkaComboBox1, gridBagConstraints);

        SoustruhComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(SoustruhComboBox1, gridBagConstraints);

        ProgramovaniComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ProgramovaniComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(ProgramovaniComboBox1, gridBagConstraints);

        VrtackaLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        VrtackaLabel1.setText("jLabel15");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(VrtackaLabel1, gridBagConstraints);

        FrezkaLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        FrezkaLabel1.setText("jLabel16");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel2.add(FrezkaLabel1, gridBagConstraints);

        SoustruhLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SoustruhLabel1.setText("jLabel17");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(SoustruhLabel1, gridBagConstraints);

        ProgramovaniLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ProgramovaniLabel1.setText("jLabel18");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel2.add(ProgramovaniLabel1, gridBagConstraints);

        VrtackaTextArea1.setEditable(false);
        VrtackaTextArea1.setColumns(20);
        VrtackaTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        VrtackaTextArea1.setRows(6);
        jScrollPane8.setViewportView(VrtackaTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane8, gridBagConstraints);

        ProgramovaniTextArea1.setEditable(false);
        ProgramovaniTextArea1.setColumns(20);
        ProgramovaniTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ProgramovaniTextArea1.setRows(6);
        jScrollPane9.setViewportView(ProgramovaniTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane9, gridBagConstraints);

        SoustruhTextArea1.setEditable(false);
        SoustruhTextArea1.setColumns(20);
        SoustruhTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SoustruhTextArea1.setRows(6);
        jScrollPane10.setViewportView(SoustruhTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane10, gridBagConstraints);

        FrezkaTextArea1.setEditable(false);
        FrezkaTextArea1.setColumns(20);
        FrezkaTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        FrezkaTextArea1.setRows(6);
        jScrollPane11.setViewportView(FrezkaTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.4;
        add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "CNC soustruhy"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        Tornado2NLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2NLabel1.setText("jLabel19");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(Tornado2NLabel1, gridBagConstraints);

        Tornado2NLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2NLabel2.setText("jLabel20");
        jPanel3.add(Tornado2NLabel2, new java.awt.GridBagConstraints());

        Tornado2SLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2SLabel1.setText("jLabel21");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(Tornado2SLabel1, gridBagConstraints);

        Tornado2SLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2SLabel2.setText("jLabel22");
        jPanel3.add(Tornado2SLabel2, new java.awt.GridBagConstraints());

        Tornado1Label1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado1Label1.setText("jLabel23");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(Tornado1Label1, gridBagConstraints);

        Tornado1Label2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado1Label2.setText("jLabel24");
        jPanel3.add(Tornado1Label2, new java.awt.GridBagConstraints());

        MasturnLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MasturnLabel1.setText("jLabel25");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(MasturnLabel1, gridBagConstraints);

        MasturnLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MasturnLabel2.setText("jLabel26");
        jPanel3.add(MasturnLabel2, new java.awt.GridBagConstraints());

        Tornado2NTextArea1.setEditable(false);
        Tornado2NTextArea1.setColumns(20);
        Tornado2NTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2NTextArea1.setRows(6);
        jScrollPane12.setViewportView(Tornado2NTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane12, gridBagConstraints);

        Tornado2STextArea1.setEditable(false);
        Tornado2STextArea1.setColumns(20);
        Tornado2STextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado2STextArea1.setRows(6);
        jScrollPane13.setViewportView(Tornado2STextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane13, gridBagConstraints);

        Tornado1TextArea1.setEditable(false);
        Tornado1TextArea1.setColumns(20);
        Tornado1TextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Tornado1TextArea1.setRows(6);
        jScrollPane14.setViewportView(Tornado1TextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane14, gridBagConstraints);

        MasturnTextArea1.setEditable(false);
        MasturnTextArea1.setColumns(20);
        MasturnTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MasturnTextArea1.setRows(6);
        jScrollPane15.setViewportView(MasturnTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.4;
        add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Drátořez"));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        DratakLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DratakLabel1.setText("jLabel27");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel4.add(DratakLabel1, gridBagConstraints);

        DratakLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DratakLabel2.setText("jLabel28");
        jPanel4.add(DratakLabel2, new java.awt.GridBagConstraints());

        DratakTextArea1.setEditable(false);
        DratakTextArea1.setColumns(20);
        DratakTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        DratakTextArea1.setRows(6);
        jScrollPane16.setViewportView(DratakTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jScrollPane16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.4;
        add(jPanel4, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 40));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        NovyStrojButton1.setText("Nový stroj");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(NovyStrojButton1, gridBagConstraints);

        UpravitUdajeButton1.setText("Upravit údaje");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(UpravitUdajeButton1, gridBagConstraints);

        ObnovitUdajeButton1.setText("Obnovit data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(ObnovitUdajeButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.2;
        add(jPanel5, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DMU602Label1;
    private javax.swing.JLabel DMU602Label2;
    private javax.swing.JTextArea DMU602TextArea1;
    private javax.swing.JLabel DMU60Label1;
    private javax.swing.JLabel DMU60Label2;
    private javax.swing.JTextArea DMU60TextArea1;
    private javax.swing.JLabel DratakLabel1;
    private javax.swing.JLabel DratakLabel2;
    private javax.swing.JTextArea DratakTextArea1;
    private javax.swing.JComboBox FrezkaComboBox1;
    private javax.swing.JLabel FrezkaLabel1;
    private javax.swing.JTextArea FrezkaTextArea1;
    private javax.swing.JLabel MCV750Label1;
    private javax.swing.JLabel MCV750Label2;
    private javax.swing.JTextArea MCV750TextArea1;
    private javax.swing.JLabel MCV754Label1;
    private javax.swing.JLabel MCV754Label2;
    private javax.swing.JTextArea MCV754TextArea1;
    private javax.swing.JLabel MasturnLabel1;
    private javax.swing.JLabel MasturnLabel2;
    private javax.swing.JTextArea MasturnTextArea1;
    private javax.swing.JButton NovyStrojButton1;
    private javax.swing.JButton ObnovitUdajeButton1;
    private javax.swing.JComboBox ProgramovaniComboBox1;
    private javax.swing.JLabel ProgramovaniLabel1;
    private javax.swing.JTextArea ProgramovaniTextArea1;
    private javax.swing.JLabel RichmondLabel1;
    private javax.swing.JLabel RichmondLabel2;
    private javax.swing.JTextArea RichmondTextArea1;
    private javax.swing.JComboBox SoustruhComboBox1;
    private javax.swing.JLabel SoustruhLabel1;
    private javax.swing.JTextArea SoustruhTextArea1;
    private javax.swing.JLabel Storm1Label1;
    private javax.swing.JLabel Storm1Label2;
    private javax.swing.JTextArea Storm1TextArea1;
    private javax.swing.JLabel Storm2Label1;
    private javax.swing.JLabel Storm2Label2;
    private javax.swing.JTextArea Storm2TextArea1;
    private javax.swing.JLabel Tornado1Label1;
    private javax.swing.JLabel Tornado1Label2;
    private javax.swing.JTextArea Tornado1TextArea1;
    private javax.swing.JLabel Tornado2NLabel1;
    private javax.swing.JLabel Tornado2NLabel2;
    private javax.swing.JTextArea Tornado2NTextArea1;
    private javax.swing.JLabel Tornado2SLabel1;
    private javax.swing.JLabel Tornado2SLabel2;
    private javax.swing.JTextArea Tornado2STextArea1;
    private javax.swing.JButton UpravitUdajeButton1;
    private javax.swing.JComboBox VrtackaComboBox1;
    private javax.swing.JLabel VrtackaLabel1;
    private javax.swing.JTextArea VrtackaTextArea1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    // End of variables declaration//GEN-END:variables
}
