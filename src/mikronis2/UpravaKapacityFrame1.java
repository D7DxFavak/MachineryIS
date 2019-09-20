/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Favak
 */
public class UpravaKapacityFrame1 extends javax.swing.JFrame {

    private String vykresy_cislo;
    private boolean uprava;
    private int kapacita_id = -1;

    /**
     * Creates new form UpravaKapacityFrame1
     */
    public UpravaKapacityFrame1(String vykresy_cislo) {
        this.vykresy_cislo = vykresy_cislo;
        initComponents();
        inicializaceKomponent();
        initKapacity();
        this.setVisible(true);
    }
    
     private void inicializaceKomponent() {
        CisloVykresuTextField1.setText(this.vykresy_cislo);
        uprava = false;
        kapacita_id = -1;
    }

    private void initKapacity() {
        Vector kapacita1 = new Vector();
        try {

            ResultSet kapacita = PripojeniDB.dotazS(
                    "SELECT vyrobni_kapacita_id, "
                    + "vyrobni_kapacita_cas_mcv, "
                    + "vyrobni_kapacita_cas_tornado, "
                    + "vyrobni_kapacita_cas_dmu, "
                    + "vyrobni_kapacita_soustruh "
                    + "FROM spolecne.vyrobni_kapacita "
                    + "WHERE vyrobni_kapacita_cislo_vykresu = '" + this.vykresy_cislo + "'");

            while (kapacita.next()) {
                kapacita1 = new Vector();
                kapacita1.add(new Integer(kapacita.getInt(1)));
                try {
                    kapacita1.add(new String(kapacita.getString(2)));
                } catch (Exception e) {
                    kapacita1.add(new String(""));
                }
                try {
                    kapacita1.add(new String(kapacita.getString(3)));
                } catch (Exception e) {
                    kapacita1.add(new String(""));
                }
                try {
                    kapacita1.add(new String(kapacita.getString(4)));
                } catch (Exception e) {
                    kapacita1.add(new String(""));
                }
                try {
                    kapacita1.add(new Integer(kapacita.getInt(5)).intValue());
                } catch (Exception e) {
                    kapacita1.add(0);
                }
            }

            MCVcasTextField1.setText((String) kapacita1.get(1));
            TornadoCasTextField1.setText((String) kapacita1.get(2));
            DMUCasTextField1.setText((String) kapacita1.get(3));
            if (((Integer) kapacita1.get(4)).intValue() == 1) {
                SoustruhCheckBox1.setSelected(true);
            } else {
                SoustruhCheckBox1.setSelected(false);
            }
            kapacita_id = ((Integer) kapacita1.get(0)).intValue();
            uprava = true;
        } catch (Exception e) {
            uprava = false;
        }
        System.out.println("uprava " + uprava);


    }

      private void ulozKapacity() {
        System.out.println("uloz kapacitu");
        int soustruh = 0;
        if (SoustruhCheckBox1.isSelected() == true) {
            soustruh = 1;
        }

        if (uprava == true) {
            String dotaz = "UPDATE spolecne.vyrobni_kapacita "
                    + "SET vyrobni_kapacita_cas_mcv = '" + MCVcasTextField1.getText().trim() + "', "
                    + "vyrobni_kapacita_cas_tornado = '" + TornadoCasTextField1.getText().trim() + "', "
                     + "vyrobni_kapacita_cas_dmu = '" + DMUCasTextField1.getText().trim() + "', "
                    + "vyrobni_kapacita_soustruh = " + soustruh + " "
                    + "WHERE vyrobni_kapacita_id = " + kapacita_id;

            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(vyrobni_kapacita_id) "
                        + "FROM spolecne.vyrobni_kapacita ");
                while (id.next()) {
                    kapacita_id = id.getInt(1) + 1;
                }
                int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vyrobni_kapacita("
                        + "vyrobni_kapacita_id, "
                        + "vyrobni_kapacita_cislo_vykresu, "
                        + "vyrobni_kapacita_cas_mcv, "
                        + "vyrobni_kapacita_cas_tornado, "
                        + "vyrobni_kapacita_cas_dmu, "
                        + "vyrobni_kapacita_soustruh) "
                        + "VALUES( " + kapacita_id
                        + ", '" + CisloVykresuTextField1.getText().trim()
                        + "', '" + MCVcasTextField1.getText().trim()
                        + "', '" + TornadoCasTextField1.getText().trim()
                         + "', '" + DMUCasTextField1.getText().trim()
                        + "', " + soustruh+ ")");

            } catch (Exception e) {
                e.printStackTrace();
            }



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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        MCVcasTextField1 = new javax.swing.JTextField();
        TornadoCasTextField1 = new javax.swing.JTextField();
        DMUCasTextField1 = new javax.swing.JTextField();
        SoustruhCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Čas MCV :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Čas Tornado :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Čas DMU :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(CisloVykresuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(MCVcasTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(TornadoCasTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(DMUCasTextField1, gridBagConstraints);

        SoustruhCheckBox1.setText("použít soustruh");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(SoustruhCheckBox1, gridBagConstraints);

        jButton1.setText("Potvrdit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jButton2.setText("Zrušit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ulozKapacity();
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JTextField DMUCasTextField1;
    private javax.swing.JTextField MCVcasTextField1;
    private javax.swing.JCheckBox SoustruhCheckBox1;
    private javax.swing.JTextField TornadoCasTextField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
