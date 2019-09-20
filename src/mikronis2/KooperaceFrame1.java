/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaKooperace;
import mikronis2.dbtridy.TridaPruvodka;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class KooperaceFrame1 extends javax.swing.JFrame {

    private boolean uprava = false;
    private TridaPruvodka tPr;
    private java.text.DateFormat df = java.text.DateFormat.getDateInstance();

    public KooperaceFrame1(boolean uprava, String nazev) {
        this.uprava = uprava;
        tPr = new TridaPruvodka();
        initComponents();
        this.setTitle(nazev);
        this.setVisible(true);
    }

    public KooperaceFrame1(TridaPruvodka tPr, boolean uprava, String nazev) {
        this.uprava = uprava;
        this.tPr = tPr;
        initComponents();
        this.setTitle(nazev);
        initKooperace();
        this.setVisible(true);
    }

    private void initKooperace() {
        try {
            jTFCisloPruvodky.setText(String.valueOf(tPr.getId()));
            jTFCisloVykresu.setText(tPr.getTv1().getCislo());
            jTFRevizeVykresu.setText(tPr.getTv1().getRevize());
            jTFNazevVykresu.setText(tPr.getNazev());
            jTFPoznamky.setText(tPr.getAktualniKooperace().getPoznamka());
            jTFZpracovani.setText(tPr.getAktualniKooperace().getPopis());
            if (tPr.getAktualniKooperace().getDatumOdeslani() != null) {
                jTFDatumOdeslano.setText(df.format(tPr.getAktualniKooperace().getDatumOdeslani()));
            }
            if (tPr.getAktualniKooperace().getDatumPrijeti() != null) {
                jTFDatumPrijato.setText(df.format(tPr.getAktualniKooperace().getDatumPrijeti()));
            }
            if (tPr.getAktualniKooperace().getPocetOdeslano() > 0) {
                jTFKusuOdeslano.setText(String.valueOf(tPr.getAktualniKooperace().getPocetOdeslano()));
            }
            if (tPr.getAktualniKooperace().getPocetPrijato() > 0) {
                jTFKusuPrijato.setText(String.valueOf(tPr.getAktualniKooperace().getPocetPrijato()));
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void ulozKooperace() throws Exception {
        TridaKooperace tk1 = tPr.getAktualniKooperace();
        if (!jTFDatumOdeslano.getText().trim().isEmpty()) {
            tk1.setDatumOdeslani(df.parse(jTFDatumOdeslano.getText().trim()));
        } else {
            tk1.setDatumOdeslani(null);
        }
        if (!jTFDatumPrijato.getText().trim().isEmpty()) {
            tk1.setDatumPrijeti(df.parse(jTFDatumPrijato.getText().trim()));
        } else {
            tk1.setDatumPrijeti(null);
        }
        if (!jTFKusuOdeslano.getText().trim().isEmpty()) {
            tk1.setPocetOdeslano(Integer.valueOf(jTFKusuOdeslano.getText().trim()));
        } else {
            tk1.setPocetOdeslano(0);
        }
        if (!jTFKusuPrijato.getText().trim().isEmpty()) {
            tk1.setPocetPrijato(Integer.valueOf(jTFKusuPrijato.getText().trim()));
        } else {
            tk1.setPocetPrijato(0);
        }

        String dotaz = "";
        try {
            if (this.uprava) {
                dotaz = "UPDATE spolecne.pruvodka_kooperace "
                        + "SET pruvodka_kooperace_datum_prijeti = " + TextFunkce1.osetriZapisDatumDB1(tk1.getDatumPrijeti()) + ", "
                        + "pruvodka_kooperace_pocet_prijeti = " + ((tk1.getPocetPrijato() == 0) ? null : tk1.getPocetPrijato()) + ", "
                        + "pruvodka_kooperace_datum_odeslani = " + TextFunkce1.osetriZapisDatumDB1(tk1.getDatumOdeslani()) + ", "
                        + "pruvodka_kooperace_pocet_odeslani = " + ((tk1.getPocetOdeslano() == 0) ? null : tk1.getPocetOdeslano()) + " "
                        + "WHERE pruvodka_kooperace_pruvodka_id = " + tk1.getIdPruvodka() + " "
                        + "AND pruvodka_kooperace_poradi = " + tk1.getPoradi();
            } else {
                dotaz = "INSERT INTO spolecne.pruvodka_kooperace( "
                        + "pruvodka_kooperace_pruvodka_id, pruvodka_kooperace_poradi, pruvodka_kooperace_datum_odeslani, "
                        + "pruvodka_kooperace_pocet_odeslani,pruvodka_kooperace_datum_prijeti, pruvodka_kooperace_pocet_prijeti, pruvodka_kooperace_poznamky) "
                        + "VALUES (" + tk1.getIdPruvodka() + ", " + tk1.getPoradi() + ", " + TextFunkce1.osetriZapisDatumDB1(tk1.getDatumOdeslani()) + ", "
                        + ((tk1.getPocetOdeslano() == 0) ? null : tk1.getPocetOdeslano()) + ", " + TextFunkce1.osetriZapisDatumDB1(tk1.getDatumPrijeti()) + ", "
                        + ((tk1.getPocetPrijato() == 0) ? null : tk1.getPocetPrijato()) + ", null)";

            }
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dispose();
    }

    protected static TridaPruvodka nactiDataPruvodky(int idPruvodky) {
        TridaPruvodka tp1 = new TridaPruvodka(idPruvodky);
        try {

            if (SQLFunkceObecne2.selectBooleanPole(
                    "SELECT EXISTS (SELECT pruvodka_kooperace_pruvodka_id FROM spolecne.pruvodka_kooperace WHERE "
                    + "pruvodka_kooperace_datum_prijeti IS NULL "
                    + "AND pruvodka_kooperace_pruvodka_id = " + idPruvodky + ")") == true) {              
                ResultSet q = PripojeniDB.dotazS("SELECT pracovni_postup_pruvodka_popis, pracovni_postup_pruvodka_poradi, current_date "
                        + "FROM spolecne.pracovni_postup_pruvodka "
                        + "WHERE pracovni_postup_pruvodka_pruvodka_id = " + idPruvodky + " "
                        + "AND pracovni_postup_pruvodka_druh_stroje_id = 20 "
                        + "AND pracovni_postup_pruvodka_poradi = (SELECT pruvodka_kooperace_poradi FROM spolecne.pruvodka_kooperace "
                        + "WHERE pruvodka_kooperace_pruvodka_id = pracovni_postup_pruvodka_pruvodka_id AND pruvodka_kooperace_datum_prijeti IS NULL) "
                        + "ORDER BY pracovni_postup_pruvodka_poradi "
                        + "LIMIT 1");
                while (q.next()) {
                    TridaKooperace tk1 = new TridaKooperace();
                    tk1.setIdPruvodka(idPruvodky);
                    tk1.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                    tk1.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(1)));
                    tk1.setDatumPrijeti(q.getDate(3));
                    tk1.setRozpracovana(true);
                    tp1.setAktualniKooperace(tk1);
                }

            } else {                
                ResultSet q = PripojeniDB.dotazS(
                        "SELECT pracovni_postup_pruvodka_popis, pracovni_postup_pruvodka_poradi, current_date "
                        + "FROM spolecne.pracovni_postup_pruvodka "
                        + "WHERE pracovni_postup_pruvodka_pruvodka_id = " + idPruvodky + " "
                        + "AND pracovni_postup_pruvodka_druh_stroje_id = 20 "
                        + "AND pracovni_postup_pruvodka_poradi NOT IN (SELECT pruvodka_kooperace_poradi FROM spolecne.pruvodka_kooperace "
                        + "WHERE pruvodka_kooperace_pruvodka_id = pracovni_postup_pruvodka_pruvodka_id) "
                        + "ORDER BY pracovni_postup_pruvodka_poradi "
                        + "LIMIT 1");
                while (q.next()) {
                    TridaKooperace tk1 = new TridaKooperace();
                    tk1.setIdPruvodka(idPruvodky);
                    tk1.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                    tk1.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(1)));
                    tk1.setDatumOdeslani(q.getDate(3));
                    tk1.setRozpracovana(false);
                    tp1.setAktualniKooperace(tk1);

                }// konec while
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tp1;
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
        jTextField4 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFCisloPruvodky = new javax.swing.JTextField();
        jTFRevizeVykresu = new javax.swing.JTextField();
        jTFNazevVykresu = new javax.swing.JTextField();
        jTFPoznamky = new javax.swing.JTextField();
        UlozButton1 = new javax.swing.JButton();
        ZrusButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTFCisloVykresu = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTFDatumOdeslano = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTFKusuOdeslano = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTFDatumPrijato = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFKusuPrijato = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTFZpracovani = new javax.swing.JTextField();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField4.setText("jTextField4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nový výkres");
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel4.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel3.setText("Zpracování :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel2.setText("Poznámky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jTFCisloPruvodky.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCisloPruvodkyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFCisloPruvodky, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFRevizeVykresu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFNazevVykresu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFPoznamky, gridBagConstraints);

        UlozButton1.setText("Uložit");
        UlozButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UlozButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        getContentPane().add(ZrusButton1, gridBagConstraints);

        jLabel7.setText("Číslo průvodky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFCisloVykresu, gridBagConstraints);

        jLabel5.setText("Odesláno :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 10, 5);
        getContentPane().add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 10, 5);
        getContentPane().add(jTFDatumOdeslano, gridBagConstraints);

        jLabel6.setText("Kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 10, 5);
        getContentPane().add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 10, 5);
        getContentPane().add(jTFKusuOdeslano, gridBagConstraints);

        jLabel8.setText("Přijato :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jTFDatumPrijato, gridBagConstraints);

        jLabel9.setText("Kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        getContentPane().add(jTFKusuPrijato, gridBagConstraints);

        jButton1.setText("Načíst");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jLabel10.setText("Název výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFZpracovani, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UlozButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UlozButton1MouseClicked
        try {
            ulozKooperace();
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_UlozButton1MouseClicked

    private void ZrusButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZrusButton1MouseClicked
        this.dispose();
    }//GEN-LAST:event_ZrusButton1MouseClicked

    private void jTFCisloPruvodkyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCisloPruvodkyActionPerformed
        tPr = nactiDataPruvodky(Integer.valueOf(jTFCisloPruvodky.getText().trim()));
        initKooperace();
    }//GEN-LAST:event_jTFCisloPruvodkyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tPr = nactiDataPruvodky(Integer.valueOf(jTFCisloPruvodky.getText().trim()));
        initKooperace();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UlozButton1;
    private javax.swing.JButton ZrusButton1;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFCisloPruvodky;
    private javax.swing.JTextField jTFCisloVykresu;
    private javax.swing.JTextField jTFDatumOdeslano;
    private javax.swing.JTextField jTFDatumPrijato;
    private javax.swing.JTextField jTFKusuOdeslano;
    private javax.swing.JTextField jTFKusuPrijato;
    private javax.swing.JTextField jTFNazevVykresu;
    private javax.swing.JTextField jTFPoznamky;
    private javax.swing.JTextField jTFRevizeVykresu;
    private javax.swing.JTextField jTFZpracovani;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
