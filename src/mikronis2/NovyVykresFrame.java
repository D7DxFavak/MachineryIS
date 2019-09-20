/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class NovyVykresFrame extends javax.swing.JFrame {

    private int vykres_id;
    private TridaVykres1 tv1;
    private boolean uprava = false;
    private boolean smazat = false;
    protected RoletkaUniverzalModel1 roletkaModelZakaznici;
    private File soubor;

     
    public NovyVykresFrame(boolean uprava) {
        this.uprava = uprava;
        initComponents();
        inicializaceKomponent();
        this.setVisible(true);
    }

    public NovyVykresFrame(String cislo, String revize, boolean uprava) {
        this.uprava = uprava;
        initComponents();
        inicializaceKomponent();
        this.setVisible(true);
        CisloVykresuTextField1.setText(cislo);
        ZmenaVykresuTextField1.setText(revize);
        try {
            ResultSet vykresy = PripojeniDB.dotazS(
                    "SELECT vykresy_nazev "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = " + TextFunkce1.osetriZapisTextDB1(cislo));

            while (vykresy.next()) {
                NazevVykresuTextField1.setText(vykresy.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NovyVykresFrame(int vykres_id, boolean uprava) {
        this.vykres_id = vykres_id;
        this.uprava = uprava;
        initComponents();
        inicializaceKomponent();
        initVykres();

        this.setVisible(true);
    }

    public NovyVykresFrame(int vykres_id, boolean uprava, boolean smazat) {
        this.vykres_id = vykres_id;
        this.smazat = smazat;
        this.uprava = uprava;
        initComponents();
        inicializaceKomponent();
        initVykres();

        NazevVykresuTextField1.setEditable(false);
        CisloVykresuTextField1.setEditable(false);
        ZmenaVykresuTextField1.setEditable(false);
        PoznamkyTextField1.setEditable(false);
        ZakaznikComboBox1.setEditable(false);

        this.setVisible(true);
        this.setTitle("Smazat výkres");
        UlozButton1.setText("Smazat výkres");
    }
    
    public void setZakaznik(int idZakaznik) {
        roletkaModelZakaznici.setDataOId(idZakaznik);
    }

    private void zpracovatVykres() {
        if (smazat == false) {
            ulozVykres();
        } else if (smazat == true) {
            smazatVykres();
        }
    }

    private void inicializaceKomponent() {
        roletkaModelZakaznici = new RoletkaUniverzalModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_nazev FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_nazev",
                "V databázi nejsou zadáni zákazníci"); // bylo ...vs_id
        ZakaznikComboBox1.setModel(roletkaModelZakaznici);
        roletkaModelZakaznici.setDataOId(MikronIS2.indexZakaznika);
    }

    private void initVykres() {
        try {
            tv1 = new TridaVykres1(this.vykres_id);
            CisloVykresuTextField1.setText(tv1.getCislo());
            try {
                ZmenaVykresuTextField1.setText(tv1.getRevize());
            } catch (Exception e) {
                ZmenaVykresuTextField1.setText(new String(""));
            }
            try {
                PoznamkyTextField1.setText(tv1.getPoznamky());
            } catch (Exception e) {
                PoznamkyTextField1.setText(new String(""));
            }
            try {
                NazevVykresuTextField1.setText(tv1.getNazev());
            } catch (Exception e) {
                NazevVykresuTextField1.setText(new String(""));
            }
            roletkaModelZakaznici.setDataOId(tv1.getIdZakaznik());

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void ulozVykres() {
        tv1 = new TridaVykres1();
        tv1.setCislo(CisloVykresuTextField1.getText());
        tv1.setRevize(ZmenaVykresuTextField1.getText());
        tv1.setNazev(NazevVykresuTextField1.getText());
        tv1.setPoznamky(PoznamkyTextField1.getText());
        tv1.setIdZakaznik(((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo());
        if (uprava == false) {
            try {
                this.vykres_id = tv1.insertData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {           
            tv1.setIdVykres(this.vykres_id);
            tv1.updateData();
        }
        if (souborTextField1.getText().trim().length() > 0) {
            try {
                FileInputStream fis = new FileInputStream(soubor);
                int r = PripojeniDB.dotazIUD(
                        "INSERT INTO spolecne.vykresy_bindata(vykresy_bindata_id, vykresy_bindata_nazev) "
                        + "VALUES(" + this.vykres_id + ", "
                        + TextFunkce1.osetriZapisTextDB1(soubor.getName()) + ")");

                PreparedStatement ps = PripojeniDB.con.prepareStatement(
                        "UPDATE spolecne.vykresy_bindata SET vykresy_bindata_data = ? WHERE vykresy_bindata_id = ? ");
                ps.setBinaryStream(1, fis, (int) soubor.length());
                ps.setInt(2, this.vykres_id);
                ps.executeUpdate();
                ps.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MikronIS2.indexVykres = tv1.getIdVykres();
        this.dispose();
    }
    
     private void smazatVykres() {
       tv1.deleteData();
    }
     
     
    private void pridatSoubor() {
        String filename = File.separator + "tmp";
        JFileChooser fc = new JFileChooser(new File(filename));
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        soubor = fc.getSelectedFile();
        souborTextField1.setText(soubor.getName());
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

        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        ZmenaVykresuTextField1 = new javax.swing.JTextField();
        NazevVykresuTextField1 = new javax.swing.JTextField();
        PoznamkyTextField1 = new javax.swing.JTextField();
        souborTextField1 = new javax.swing.JTextField();
        ZakaznikComboBox1 = new javax.swing.JComboBox();
        vybratSouborButton1 = new javax.swing.JButton();
        UlozButton1 = new javax.swing.JButton();
        ZrusButton1 = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nový výkres");
        setPreferredSize(new java.awt.Dimension(450, 320));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Změna výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel5.setText("Soubor :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel4.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel3.setText("Název výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel2.setText("Poznámky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(CisloVykresuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(ZmenaVykresuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(NazevVykresuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(PoznamkyTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(souborTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(ZakaznikComboBox1, gridBagConstraints);

        vybratSouborButton1.setText("Vybrat");
        vybratSouborButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vybratSouborButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        getContentPane().add(vybratSouborButton1, gridBagConstraints);

        UlozButton1.setText("Uložit");
        UlozButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UlozButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(10, 56, 10, 10);
        getContentPane().add(UlozButton1, gridBagConstraints);

        ZrusButton1.setText("Zrušit");
        ZrusButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ZrusButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        getContentPane().add(ZrusButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UlozButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UlozButton1MouseClicked
        zpracovatVykres();
        this.dispose();
    }//GEN-LAST:event_UlozButton1MouseClicked

    private void ZrusButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZrusButton1MouseClicked
        this.dispose();
    }//GEN-LAST:event_ZrusButton1MouseClicked

    private void vybratSouborButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vybratSouborButton1MouseClicked
        pridatSoubor();
    }//GEN-LAST:event_vybratSouborButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JTextField NazevVykresuTextField1;
    private javax.swing.JTextField PoznamkyTextField1;
    private javax.swing.JButton UlozButton1;
    private javax.swing.JComboBox ZakaznikComboBox1;
    private javax.swing.JTextField ZmenaVykresuTextField1;
    private javax.swing.JButton ZrusButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField souborTextField1;
    private javax.swing.JButton vybratSouborButton1;
    // End of variables declaration//GEN-END:variables
}
