/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import mikronis2.dbtridy.TridaTypMaterial1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class NovyMaterialFrame1 extends javax.swing.JFrame {

    protected RoletkaUniverzalRozsirenaModel1 roletkaModelSkupinaMaterial;
    private TridaTypMaterial1 ttm1;

    public NovyMaterialFrame1() {
        initComponents();
        initRoletkaSkupina();
        
        ttm1 = new TridaTypMaterial1();
        this.setVisible(true);
    }

    protected void initRoletkaSkupina() {
        roletkaModelSkupinaMaterial = new RoletkaUniverzalRozsirenaModel1(
                "SELECT skupina_materialu_id, skupina_materialu_nazev "
                + "FROM spolecne.skupina_materialu "
                + "ORDER BY skupina_materialu_priorita ASC", "Všechny", null, "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBSkupina.setModel(roletkaModelSkupinaMaterial);
    }
    
     private void ulozZmeny() {
           ttm1.setIdSkupina(((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo());
           ttm1.setNazev(jTFNazev.getText().trim());
           ttm1.setNorma(jTFNorma.getText().trim());
           ttm1.setPoznamky(jTFPoznamka.getText().trim());
           ttm1.insertData();
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
        jCBSkupina = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTFNazev = new javax.swing.JTextField();
        jBPotvrdit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTFNorma = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTFPoznamka = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nový materiál");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Skupina :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jCBSkupina, gridBagConstraints);

        jLabel2.setText("Název :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFNazev, gridBagConstraints);

        jBPotvrdit.setText("Potvrdit");
        jBPotvrdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPotvrditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 50, 5, 10);
        getContentPane().add(jBPotvrdit, gridBagConstraints);

        jBZrusit.setText("Zrušit");
        jBZrusit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBZrusitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jBZrusit, gridBagConstraints);

        jLabel3.setText("Norma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFNorma, gridBagConstraints);

        jLabel4.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFPoznamka, gridBagConstraints);

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
    private javax.swing.JButton jBPotvrdit;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JComboBox jCBSkupina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTFNazev;
    private javax.swing.JTextField jTFNorma;
    private javax.swing.JTextField jTFPoznamka;
    // End of variables declaration//GEN-END:variables
}
