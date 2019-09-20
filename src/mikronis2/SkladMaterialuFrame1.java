/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.Vector;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaPolotovar1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class SkladMaterialuFrame1 extends javax.swing.JFrame {

    private int typAkce;
    private boolean edit;
    private TridaPolotovar1 polotovar;
    private ComboBoxMaterialUdalosti MaterialListener;
    private ComboBoxVyberPolotovaruUdalosti VyberPolotovaruListener;
    protected RoletkaUniverzalRozsirenaModel1 roletkaModelMaterial, roletkaModelSkupinaMaterial;

    public SkladMaterialuFrame1(int akce, boolean edit) {
        this.typAkce = akce;
        this.edit = edit;
        this.polotovar = new TridaPolotovar1();
        this.polotovar.setTypMaterialu(MikronIS2.indexMaterial);
        initComponents();
        inicializaceKomponent();
        HmotnostLabel1.setVisible(false);
        jTFHmotnost.setVisible(false);
        this.setVisible(true);
    }
    
    public SkladMaterialuFrame1(int akce, int idTypMaterialu, int idSkupina, boolean edit) {
        this.typAkce = akce;
        this.edit = edit;
         this.polotovar = new TridaPolotovar1();
        this.polotovar.setTypMaterialu(idTypMaterialu);
        initComponents();
        try {
            if (typAkce == 300) {
                jTFPocetKusu.setText(polotovar.getPoznamky());
                jTFHmotnost.setText(polotovar.getMernaHmotnost());
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch-

        inicializaceKomponent();
        //roletkaModelSkupinaMaterial.setDataOId(idSkupina);
        //roletkaModelMaterial.setDataOId(idTypMaterialu);
        this.setVisible(true);
    }

    public SkladMaterialuFrame1(int akce, TridaPolotovar1 polotovar, boolean edit) {
        this.typAkce = akce;
        this.polotovar = polotovar;
        this.edit = edit;
        initComponents();
        try {
            if (typAkce == 300) {
                jTFPocetKusu.setText(polotovar.getPoznamky());
                jTFHmotnost.setText(polotovar.getMernaHmotnost());
            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch-

        inicializaceKomponent();
        this.setVisible(
                true);
    }

    private void inicializaceKomponent() {
        if (this.typAkce == 100) {
            nazevOperaceLabel1.setText("Vyskladnit");
            HmotnostLabel1.setText("Poznámka");
            HmotnostLabel1.setVisible(true);
            jTFHmotnost.setVisible(true);
            jTFRozmer.setEditable(false);
            jCBAktivni.setVisible(false);
        } else if (this.typAkce == 200) {
            nazevOperaceLabel1.setText("Naskladnit");
            HmotnostLabel1.setText("Poznámka");
            HmotnostLabel1.setVisible(true);
            jTFHmotnost.setVisible(true);
            jTFRozmer.setEditable(false);
            jCBAktivni.setVisible(false);
        } else if (this.typAkce == 300) {
            nazevOperaceLabel1.setText("Poznámka");
            HmotnostLabel1.setVisible(true);
            jTFHmotnost.setVisible(true);
        } else if (this.typAkce == 400) {
            nazevOperaceLabel1.setText("Poznámka");
            HmotnostLabel1.setVisible(true);
            jTFHmotnost.setVisible(true);
        } else {
            nazevOperaceLabel1.setText("Nedefinovano");
            jTFPocetKusu.setEditable(false);
        }

        MaterialListener = new ComboBoxMaterialUdalosti();
        VyberPolotovaruListener = new ComboBoxVyberPolotovaruUdalosti();
        jCBVyberMaterialu.addItemListener(MaterialListener);

        initRoletkaSkupina();
        initRoletkaMaterial();
        initPolotovary();
    }

    protected void initRoletkaSkupina() {
        roletkaModelSkupinaMaterial = new RoletkaUniverzalRozsirenaModel1(
                "SELECT skupina_materialu_id, skupina_materialu_nazev "
                + "FROM spolecne.skupina_materialu "
                + "ORDER BY skupina_materialu_priorita ASC", "Všechny", null, "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBSkupina.setModel(roletkaModelSkupinaMaterial);
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT typ_materialu_skupina FROM spolecne.typ_materialu WHERE typ_materialu_id = " + polotovar.getTypMaterialu());
            while (q.next()) {
                roletkaModelSkupinaMaterial.setDataOId(q.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRoletkaMaterial() {
        String dotaz = "SELECT typ_materialu_id, "
                + "CASE "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_nazev || ' - ' || typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NULL AND typ_materialu_norma IS NOT NULL THEN typ_materialu_norma "
                + "WHEN typ_materialu_nazev IS NOT NULL AND typ_materialu_norma IS NULL THEN typ_materialu_nazev "
                + "ELSE null "
                + "END AS nazev_norma "
                + "FROM spolecne.typ_materialu ";
        if (((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() != 0) {
            dotaz += "WHERE typ_materialu_skupina = " + ((DvojiceCisloRetez) roletkaModelSkupinaMaterial.getSelectedItem()).cislo() + " ";
        }
        dotaz += "ORDER BY typ_materialu_priorita ASC";
        roletkaModelMaterial = new RoletkaUniverzalRozsirenaModel1(
                dotaz, "Neurčen", null, "V databázi nejsou zadáni zákazníci", 0); // bylo ...vs_id
        jCBVyberMaterialu.setModel(roletkaModelMaterial);
        roletkaModelMaterial.setDataOId(polotovar.getTypMaterialu());
    }

    private void initPolotovary() {
        MaterialListener.itemStateChanged(null);
        jTFRozmer.setText(polotovar.getNazev());
        jCBAktivni.setSelected(polotovar.isAktivni());
    }

    public void zjistiPolotovarySklad() {
        /* int idPolotovary;
         if (VyberPolotovaruComboBox1.getSelectedIndex() > -1) {
         idPolotovary = (Integer) ((Vector) vrPolotovary.get(VyberPolotovaruComboBox1.getSelectedIndex())).get(0);
         } else {
         idPolotovary = 0;
         }*/
        jLNaskladneno.setText(SQLFunkceObecne2.selectStringPole("SELECT CAST( "
                + "(SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_polotovary_transakce "
                + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS TRUE "
                + "AND sklady_polotovary_transakce_polotovar_id = " + polotovar.getIdPolotovar() + ") "
                + " - (SELECT COALESCE(SUM(sklady_polotovary_transakce_pocet_mj),0) "
                + "FROM logistika.sklady_polotovary_transakce "
                + "INNER JOIN logistika.sklady_polotovary_transakce_druhy ON sklady_polotovary_transakce_druh_id = "
                + "sklady_polotovary_transakce_druhy_id AND sklady_polotovary_transakce_druhy_je_prijem IS FALSE "
                + "AND sklady_polotovary_transakce_polotovar_id = " + polotovar.getIdPolotovar() + ") AS TEXT)"));
    }

    private void ulozTransakci() {
        long transakce_id = -1;

        if ((typAkce == 100) || (typAkce == 200)) {
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(sklady_polotovary_transakce_id) FROM logistika.sklady_polotovary_transakce");
                while (id.next()) {
                    transakce_id = id.getLong(1) + 1;
                }
                int pocet_mj = Integer.valueOf(jTFPocetKusu.getText().trim()).intValue();
                int a = PripojeniDB.dotazIUD("INSERT INTO logistika.sklady_polotovary_transakce("
                        + " sklady_polotovary_transakce_id, "
                        + "sklady_polotovary_transakce_sklad_id, "
                        + "sklady_polotovary_transakce_umisteni_id, "
                        + "sklady_polotovary_transakce_druh_id, "
                        + "sklady_polotovary_transakce_polotovar_id, "
                        + "sklady_polotovary_transakce_pocet_mj, "
                        + "sklady_polotovary_transakce_log_uzivatel, "
                        + "sklady_polotovary_transakce_popis) "
                        + "VALUES( " + transakce_id + ", " + 1 + ", " + 1 + ", " + typAkce + ", " + polotovar.getIdPolotovar() + ", "
                        + pocet_mj + ", 'test', " + TextFunkce1.osetriZapisTextDB1(jTFHmotnost.getText().trim()) + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (typAkce == 200) {
                try {
                    String dotaz = "UPDATE spolecne.polotovary "
                            + "SET polotovary_poznamky = '' "
                            + "WHERE polotovary_id = " + polotovar.getIdPolotovar();
                    int a = PripojeniDB.dotazIUD(dotaz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (typAkce == 300) {
            try {
                String dotaz = "UPDATE spolecne.polotovary "
                        + "SET polotovary_poznamky = " + TextFunkce1.osetriZapisTextDB1(jTFPocetKusu.getText().trim()) + ", "
                        + "polotovary_nazev = " + TextFunkce1.osetriZapisTextDB1(jTFRozmer.getText().trim()) + ", "
                        + "polotovary_aktivni = " + jCBAktivni.isSelected() + ", ";
                if (jTFHmotnost.getText().isEmpty() == true) {
                    dotaz += "polotovary_merna_hmotnost = NULL ";
                } else {
                    dotaz += "polotovary_merna_hmotnost = " + TextFunkce1.osetriZapisNumericDB1(jTFHmotnost.getText().trim()) + " ";
                }
                dotaz += "WHERE polotovary_id = " + polotovar.getIdPolotovar();
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();


            }
        } else if (typAkce == 400) {
            polotovar = new TridaPolotovar1();
            polotovar.setNazev(jTFRozmer.getText().trim());
            polotovar.setAktivni(jCBAktivni.isSelected());
            polotovar.setMernaHmotnost(jTFHmotnost.getText().trim());
            polotovar.setPoznamky(jTFPocetKusu.getText().trim());
            polotovar.setTypMaterialu(((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo()); 
            polotovar.setTypPolotovaru(9);
            polotovar.insertData();  
            
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(sklady_polotovary_transakce_id) FROM logistika.sklady_polotovary_transakce");
                while (id.next()) {
                    transakce_id = id.getLong(1) + 1;
                }
                //int pocet_mj = Integer.valueOf(jTFPocetKusu.getText().trim()).intValue();
                int a = PripojeniDB.dotazIUD("INSERT INTO logistika.sklady_polotovary_transakce("
                        + " sklady_polotovary_transakce_id, "
                        + "sklady_polotovary_transakce_sklad_id, "
                        + "sklady_polotovary_transakce_umisteni_id, "
                        + "sklady_polotovary_transakce_druh_id, "
                        + "sklady_polotovary_transakce_polotovar_id, "
                        + "sklady_polotovary_transakce_pocet_mj, "
                        + "sklady_polotovary_transakce_log_uzivatel, "
                        + "sklady_polotovary_transakce_popis) "
                        + "VALUES( " + transakce_id + ", " + 1 + ", " + 1 + ", " + 200 + ", " + polotovar.getIdPolotovar() + ", "
                        + 0 + ", 'test', " + TextFunkce1.osetriZapisTextDB1(jTFHmotnost.getText().trim()) + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }

    class ComboBoxMaterialUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            polotovar.setTypMaterialu(((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo());
            MikronIS2.indexMaterial = jCBVyberMaterialu.getSelectedIndex();
            
        }
    }//konec FLUdalosti

    class ComboBoxPolotovarUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            // System.out.println("Polotovar vybrana radkasss focus " + e.getClass().toString() + " " + e.getItem().toString() + " x  " + e.getStateChange());
            // System.out.println("vybaa " + TypPolotovaruComboBox1.getSelectedIndex());
            // typPolotovaruId = (Integer) ((Vector) vrTypPolotovary.get(TypPolotovaruComboBox1.getSelectedIndex())).get(0);
          polotovar.setTypMaterialu(((DvojiceCisloRetez) roletkaModelMaterial.getSelectedItem()).cislo());
            MikronIS2.indexMaterial = jCBVyberMaterialu.getSelectedIndex();
            //e.getItem()
        }
    }//konec FLUdalosti

    class ComboBoxVyberPolotovaruUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            zjistiPolotovarySklad();
        }
    }//konec FLUdalosti

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
        nazevOperaceLabel1 = new javax.swing.JLabel();
        HmotnostLabel1 = new javax.swing.JLabel();
        jLNaskladneno = new javax.swing.JLabel();
        jTFRozmer = new javax.swing.JTextField();
        jTFPocetKusu = new javax.swing.JTextField();
        jTFHmotnost = new javax.swing.JTextField();
        jCBVyberMaterialu = new javax.swing.JComboBox();
        jCBAktivni = new javax.swing.JCheckBox();
        jBPotvrdit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jCBSkupina = new javax.swing.JComboBox();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Materiál");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Materiál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Rozměr :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Naskladněno :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        nazevOperaceLabel1.setText("Délka / počet kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(nazevOperaceLabel1, gridBagConstraints);

        HmotnostLabel1.setText("Hmotnost 1m [kg] :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(HmotnostLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLNaskladneno, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFRozmer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFPocetKusu, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTFHmotnost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jCBVyberMaterialu, gridBagConstraints);

        jCBAktivni.setText("Zobrazovat ve skladu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jCBAktivni, gridBagConstraints);

        jBPotvrdit.setText("Potvrdit");
        jBPotvrdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPotvrditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 5, 20);
        getContentPane().add(jBPotvrdit, gridBagConstraints);

        jBZrusit.setText("Zrušit");
        jBZrusit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBZrusitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 50);
        getContentPane().add(jBZrusit, gridBagConstraints);

        jLabel8.setText("Skupina :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel8, gridBagConstraints);

        jCBSkupina.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBSkupinaItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jCBSkupina, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBZrusitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBZrusitActionPerformed
        this.dispose();
    }//GEN-LAST:event_jBZrusitActionPerformed

    private void jBPotvrditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPotvrditActionPerformed
        ulozTransakci();
        this.dispose();
    }//GEN-LAST:event_jBPotvrditActionPerformed

    private void jCBSkupinaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBSkupinaItemStateChanged
        initRoletkaMaterial();
    }//GEN-LAST:event_jCBSkupinaItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HmotnostLabel1;
    private javax.swing.JButton jBPotvrdit;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JCheckBox jCBAktivni;
    private javax.swing.JComboBox jCBSkupina;
    private javax.swing.JComboBox jCBVyberMaterialu;
    private javax.swing.JLabel jLNaskladneno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTFHmotnost;
    private javax.swing.JTextField jTFPocetKusu;
    private javax.swing.JTextField jTFRozmer;
    private javax.swing.JLabel nazevOperaceLabel1;
    // End of variables declaration//GEN-END:variables
}
