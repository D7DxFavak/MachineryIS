/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mikronis2.HlavniRamec.ALUdalosti;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaDodAdresa1;
import mikronis2.dbtridy.TridaZakaznik1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class DodAdresyFrame1 extends javax.swing.JFrame {

    private int idAdresa = -1;
    private ActionListener alUdalosti;
    private RoletkaUniverzalRozsirenaModel1 RoletkaModelDodAdresy, RoletkaModelStaty;
    private TridaDodAdresa1 tda1;
    private int idZakaznik;

    /**
     * Creates new form ZakaznikFrame1
     */
    public DodAdresyFrame1(int idZakaznik, String zakaznik) {
        this.idZakaznik = idZakaznik;
        this.setTitle(zakaznik);
        alUdalosti = new ALUdalosti();
        tda1 = new TridaDodAdresa1();
        initComponents();
        initStaty();

        initDodaciAdresy();


        /*
         * if (novy == false) { jCBVyber.setVisible(true);
         * jLVyber.setVisible(true);
         *
         * } else { jCBVyber.setVisible(false); jLVyber.setVisible(false); }
         */
        this.setVisible(true);
        jBPotvrdit.addActionListener(alUdalosti);
        jBZrusit.addActionListener(alUdalosti);
    }

    private void initDodaciAdresy() {
        RoletkaModelDodAdresy = new RoletkaUniverzalRozsirenaModel1(
                "SELECT dodaci_adresy_id, dodaci_adresy_cely_nazev FROM spolecne.dodaci_adresy "
                + "WHERE dodaci_adresy_subjekt_trhu = " + idZakaznik
                + "ORDER BY dodaci_adresy_poradi ASC", "Nová", null,
                "V databázi nejsou zadány zákazníci", 0); // bylo ...vs_id
        jCBVyberAdresa.setModel(RoletkaModelDodAdresy);
        jCBVyberAdresa.addActionListener(alUdalosti);
        jCBVyberAdresa.setActionCommand("zmenaAdresa");
    }

    private void initStaty() {
        RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStat.setModel(RoletkaModelStaty);
    }

    private void getDataAdresa() {

        idAdresa = ((DvojiceCisloRetez) RoletkaModelDodAdresy.getSelectedItem()).cislo();
        tda1 = new TridaDodAdresa1();
        tda1.selectData(idAdresa);
        // RoletkaModelDodAdresy.setDataOId(tda1.getIdZakaznik());
        RoletkaModelDodAdresy.setDataOId(tda1.getIdAdresa());

        jTFNazev.setText(tda1.getNazev());
        jTFCelyNazev.setText(tda1.getCelyNazev());
        jTFPopis.setText(tda1.getPopis());
        jTFAdresa.setText(tda1.getAdresa());
        jTFMesto.setText(tda1.getMesto());
        jTFPSC.setText(tda1.getPsc());
        RoletkaModelStaty.setDataOId(tda1.getIdStat());
    }

    private void zrusit() {
        this.dispose();
    }

    private void ulozit() {
        if (((DvojiceCisloRetez) RoletkaModelDodAdresy.getSelectedItem()) != null) {
            tda1.setIdAdresa(((DvojiceCisloRetez) RoletkaModelDodAdresy.getSelectedItem()).cislo());
        } else {
            tda1.setIdAdresa(0);
        }

        tda1.setIdStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo());
        tda1.setIdZakaznik(idZakaznik);
        if ((jTFCelyNazev.getText().trim()).length() > 0) {
            tda1.setCelyNazev((jTFCelyNazev.getText().trim()));
        } else {
            tda1.setCelyNazev((jTFAdresa.getText().trim())
                    + ", " + (jTFMesto.getText().trim())
                    + ", " + (jTFPSC.getText().trim())
                    + ", " + ((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).toString());
        }
        tda1.setNazev((jTFNazev.getText().trim()));
        tda1.setPopis((jTFPopis.getText().trim()));
        tda1.setAdresa((jTFAdresa.getText().trim()));
        tda1.setMesto((jTFMesto.getText().trim()));
        tda1.setPsc((jTFPSC.getText().trim()));

        if (tda1.getIdAdresa() == 0) {
            tda1.insertData();
        } else {
            tda1.updateData();
        }
        this.dispose();
    }

    class ALUdalosti implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("zmenaZakaznik")) {
                getDataAdresa();
            }
            if (e.getActionCommand().equals("zmenaAdresa")) {              
                getDataAdresa();
            }
            if (e.getSource() == jBZrusit) {
                zrusit();
            }
            if (e.getSource() == jBPotvrdit) {
                ulozit();
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

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFAdresa = new javax.swing.JTextField();
        jTFMesto = new javax.swing.JTextField();
        jTFPSC = new javax.swing.JTextField();
        jCBStat = new javax.swing.JComboBox();
        jCBVyberAdresa = new javax.swing.JComboBox();
        jTFPopis = new javax.swing.JTextField();
        jTFNazev = new javax.swing.JTextField();
        jTFCelyNazev = new javax.swing.JTextField();
        jBPotvrdit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();
        jCBStat1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dodací adresy");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setText("Dodací adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 10, 6);
        getContentPane().add(jLabel7, gridBagConstraints);

        jLabel8.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel10.setText("Popis :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel10, gridBagConstraints);

        jLabel11.setText("Název :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel11, gridBagConstraints);

        jLabel12.setText("Celý název :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel12, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFAdresa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFMesto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFPSC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBStat, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBVyberAdresa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFPopis, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFNazev, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFCelyNazev, gridBagConstraints);

        jBPotvrdit.setText("Potvrdit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 28);
        getContentPane().add(jBPotvrdit, gridBagConstraints);

        jBZrusit.setText("Zrušit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        getContentPane().add(jBZrusit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBStat1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBPotvrdit;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JComboBox jCBStat;
    private javax.swing.JComboBox jCBStat1;
    private javax.swing.JComboBox jCBVyberAdresa;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTFAdresa;
    private javax.swing.JTextField jTFCelyNazev;
    private javax.swing.JTextField jTFMesto;
    private javax.swing.JTextField jTFNazev;
    private javax.swing.JTextField jTFPSC;
    private javax.swing.JTextField jTFPopis;
    // End of variables declaration//GEN-END:variables
}
