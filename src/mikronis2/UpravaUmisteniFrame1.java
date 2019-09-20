/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.sql.ResultSet;
import mikronis2.dbtridy.TridaVyrobek;

/**
 *
 * @author Favak
 */
public class UpravaUmisteniFrame1 extends javax.swing.JFrame {

     private String vykres_cislo;

    /** Creates new form UpravaUmisteniFrame1 */
    public UpravaUmisteniFrame1(String vykres) {
        initComponents();
        this.setVisible(true);
        this.vykres_cislo = vykres;
        VykresTextField1.setText(this.vykres_cislo);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void ulozZmeny() {
        int umisteni_id = -1;

        try {
            ResultSet umisteni = PripojeniDB.dotazS("SELECT sklady_vyrobky_umisteni_id "
                    + "FROM logistika.sklady_vyrobky_umisteni "
                    + "WHERE sklady_vyrobky_umisteni_nazev = '" + RegalTextField1.getText().trim() + "'");
            while (umisteni.next()) {
                umisteni_id = umisteni.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (umisteni_id == -1) {
            int poradi_id = -1;
            try {
                ResultSet umisteni = PripojeniDB.dotazS("SELECT MAX(sklady_vyrobky_umisteni_id), MAX(sklady_vyrobky_umisteni_poradi_vyber) "
                        + "FROM logistika.sklady_vyrobky_umisteni");
                while (umisteni.next()) {
                    umisteni_id = umisteni.getInt(1) + 1;
                    poradi_id = umisteni.getInt(1) + 100;
                }
                String dotaz = "INSERT INTO logistika.sklady_vyrobky_umisteni( "
                        + "sklady_vyrobky_umisteni_sklad_id, "
                        + "sklady_vyrobky_umisteni_id, "
                        + "sklady_vyrobky_umisteni_nazev, "
                        + "sklady_vyrobky_umisteni_popis, "
                        + "sklady_vyrobky_umisteni_poznamky, "
                        + "sklady_vyrobky_umisteni_poradi_vyber) "
                        + "VALUES( " + 1
                        + ", " + umisteni_id
                        + ", '" + RegalTextField1.getText().trim()
                        + "', ' ','Vloženo automaticky', " + poradi_id + ")";
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String dotaz = "UPDATE logistika.sklady_vyrobky_transakce "
                + "SET sklady_vyrobky_transakce_umisteni_id = " + umisteni_id + " "
                + "WHERE sklady_vyrobky_transakce_vykres_cislo = '" + VykresTextField1.getText().trim() + "' ";

        try {
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TransakceLabel1 = new javax.swing.JLabel();
        VykresTextField1 = new javax.swing.JTextField();
        RegalTextField1 = new javax.swing.JTextField();
        jBPotvrdit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sklad výrobků");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        TransakceLabel1.setText("Regál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(TransakceLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(VykresTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(RegalTextField1, gridBagConstraints);

        jBPotvrdit.setText("Potvrdit");
        jBPotvrdit.setPreferredSize(new java.awt.Dimension(71, 35));
        jBPotvrdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPotvrditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 50, 5, 5);
        getContentPane().add(jBPotvrdit, gridBagConstraints);

        jBZrusit.setText("Zrušit");
        jBZrusit.setPreferredSize(new java.awt.Dimension(59, 35));
        jBZrusit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBZrusitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jBZrusit, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBPotvrditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPotvrditActionPerformed
        ulozZmeny();
        this.dispose();
    }//GEN-LAST:event_jBPotvrditActionPerformed

    private void jBZrusitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBZrusitActionPerformed
        this.dispose();
    }//GEN-LAST:event_jBZrusitActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RegalTextField1;
    private javax.swing.JLabel TransakceLabel1;
    private javax.swing.JTextField VykresTextField1;
    private javax.swing.JButton jBPotvrdit;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}