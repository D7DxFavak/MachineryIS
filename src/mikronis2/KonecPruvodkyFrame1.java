/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.sql.ResultSet;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;

/**
 *
 * @author Favak
 */
public class KonecPruvodkyFrame1 extends javax.swing.JFrame {

    private long pruvodky_id;
    private long casMCV;
    private long casTornado;
    private long casDMU;
    private long casDrat;
    private String celkovyCas;
    private String vykresy_cislo;
    private java.text.DateFormat df = java.text.DateFormat.getDateInstance();

    /**
     * Creates new form NovyVykresFrame
     */
    public KonecPruvodkyFrame1() {
        initComponents();
        this.setVisible(true);

    }

    public KonecPruvodkyFrame1(long pruvodky_id, long casMCV, long casTornado, long casDMU, long casDrat, String celkovyCas) {
        initComponents();
        this.setVisible(true);
        this.pruvodky_id = pruvodky_id;
        this.casMCV = casMCV;
        this.casDMU = casDMU;
        this.casTornado = casTornado;
        this.casDrat = casDrat;
        this.celkovyCas = celkovyCas;
        initKomponenty();
    }

    private void initKomponenty() {

        try {
            ResultSet pruvodka1 = PripojeniDB.dotazS(
                    "SELECT pruvodky_id, vykresy_cislo, COALESCE(vykresy_revize, '') AS revize, pruvodky_nazev, pruvodky_termin_dokonceni "
                    + "FROM spolecne.pruvodky CROSS JOIN spolecne.vykresy "
                    + "WHERE pruvodky_cislo_vykresu = vykresy_id "
                    + "AND pruvodky_id = " + pruvodky_id);
            while (pruvodka1.next()) {
                cisloPruvodkyLabel1.setText(pruvodka1.getString(1));
                cisloVykresuLabel1.setText(pruvodka1.getString(2) + " " + pruvodka1.getString(3));
                nazevSoucastiLabel1.setText(pruvodka1.getString(4));
                terminDodaniLabel1.setText(df.format(pruvodka1.getDate(5)));
                vykresy_cislo = pruvodka1.getString(2);

            }// konec while
            ResultSet umisteni1 = PripojeniDB.dotazS(
                    "SELECT sklady_vyrobky_umisteni_nazev "
                    + "FROM logistika.sklady_vyrobky_umisteni "
                    + "CROSS JOIN logistika.sklady_vyrobky_transakce "
                    + "WHERE sklady_vyrobky_transakce_umisteni_id = sklady_vyrobky_umisteni_id "
                    + "AND sklady_vyrobky_transakce_vykres_cislo  = '" + vykresy_cislo + "' LIMIT 1");
            while (umisteni1.next()) {
                RegalTextField1.setText(umisteni1.getString(1));
            }// konec while
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
    }

    private void ulozPruvodku() {
        System.out.println("Uloz pruvodku");
        String dotaz = "UPDATE spolecne.pruvodky "
                + "SET pruvodky_vyrobeno_kusu = " + VyrobenoKusuTextField1.getText().trim() + ", "
                + "pruvodky_celkovy_cas = '" + celkovyCas + "' "
                + "WHERE pruvodky_id = " + pruvodky_id;

        if (Integer.valueOf(VyrobenoKusuTextField1.getText().trim()) > 0) {
            naskladnitVyrobky();
            upravitKapacitu();
        } else {
            UlozButton1.setEnabled(false);
        }

        try {
            System.out.println("DUP " + dotaz);
            int a = PripojeniDB.dotazIUD(dotaz);
            //int programyId = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dispose();
    }

    private void upravitKapacitu() {
        if (SQLFunkceObecne2.selectBooleanPole(
                "SELECT EXISTS (SELECT vyrobni_kapacita_id FROM spolecne.vyrobni_kapacita WHERE vyrobni_kapacita_cislo_vykresu = '" + vykresy_cislo + "')") == true) {
            String dotaz = "";
            if (casDMU != 0) {
                dotaz = "UPDATE spolecne.vyrobni_kapacita "
                        + "SET vyrobni_kapacita_cas_dmu_vyroba = vyrobni_kapacita_cas_dmu_vyroba + " + casDMU + ", "
                        + "vyrobni_kapacita_dmu_kusy = vyrobni_kapacita_dmu_kusy + " + VyrobenoKusuTextField1.getText().trim() + " "
                        + "WHERE vyrobni_kapacita_cislo_vykresu = '" + vykresy_cislo + "' ";
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                    //int programyId = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (casMCV != 0) {
                dotaz = "UPDATE spolecne.vyrobni_kapacita "
                        + "SET vyrobni_kapacita_cas_mcv_vyroba = vyrobni_kapacita_cas_mcv_vyroba + " + casMCV + ", "
                        + "vyrobni_kapacita_mcv_kusy = vyrobni_kapacita_mcv_kusy + " + VyrobenoKusuTextField1.getText().trim() + " "
                        + "WHERE vyrobni_kapacita_cislo_vykresu = '" + vykresy_cislo + "' ";
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                    //int programyId = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (casTornado != 0) {
                dotaz = "UPDATE spolecne.vyrobni_kapacita "
                        + "SET vyrobni_kapacita_cas_tornado_vyroba = vyrobni_kapacita_cas_tornado_vyroba + " + casTornado + ", "
                        + "vyrobni_kapacita_tornado_kusy = vyrobni_kapacita_tornado_kusy + " + VyrobenoKusuTextField1.getText().trim() + " "
                        + "WHERE vyrobni_kapacita_cislo_vykresu = '" + vykresy_cislo + "' ";
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                    //int programyId = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (casDrat != 0) {
                dotaz = "UPDATE spolecne.vyrobni_kapacita "
                        + "SET vyrobni_kapacita_cas_edm_vyroba = vyrobni_kapacita_cas_edm_vyroba + " + casDrat + ", "
                        + "vyrobni_kapacita_edm_kusy = vyrobni_kapacita_edm_kusy + " + VyrobenoKusuTextField1.getText().trim() + " "
                        + "WHERE vyrobni_kapacita_cislo_vykresu = '" + vykresy_cislo + "' ";
                try {
                    int a = PripojeniDB.dotazIUD(dotaz);
                    //int programyId = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            long kapacita_id = -1;
            try {
                ResultSet transakce = PripojeniDB.dotazS("SELECT MAX(vyrobni_kapacita_id) FROM spolecne.vyrobni_kapacita");
                while (transakce.next()) {
                    kapacita_id = transakce.getLong(1) + 1;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            String dotaz = "";
            if ((casMCV != 0) || (casTornado != 0) || (casDMU != 0) || (casDrat != 0)) {
                dotaz = "INSERT INTO spolecne.vyrobni_kapacita( "
                        + "vyrobni_kapacita_id, "
                        + "vyrobni_kapacita_cislo_vykresu ";
                if (casMCV != 0) {
                    dotaz += ", vyrobni_kapacita_cas_mcv "
                            + ", vyrobni_kapacita_cas_mcv_vyroba "
                            + ", vyrobni_kapacita_mcv_kusy ";
                }
                if (casTornado != 0) {
                    dotaz += ", vyrobni_kapacita_cas_tornado "
                            + ", vyrobni_kapacita_cas_tornado_vyroba "
                            + ", vyrobni_kapacita_tornado_kusy ";
                }
                if (casDMU != 0) {
                    dotaz += ", vyrobni_kapacita_cas_dmu "
                            + ", vyrobni_kapacita_cas_dmu_vyroba "
                            + ", vyrobni_kapacita_dmu_kusy ";
                }
                if (casDrat != 0) {
                    dotaz += ", vyrobni_kapacita_cas_edm "
                            + ", vyrobni_kapacita_cas_edm_vyroba "
                            + ", vyrobni_kapacita_edm_kusy ";
                }
                dotaz += " ) VALUES ( " + kapacita_id + ", '" + vykresy_cislo + "' ";
                if (casMCV != 0) {
                    dotaz += ", 0 "
                            + ", " + casMCV + " "
                            + ", " + VyrobenoKusuTextField1.getText().trim() + " ";
                }
                if (casTornado != 0) {
                    dotaz += ", 0 "
                            + ", " + casTornado + " "
                            + ", " + VyrobenoKusuTextField1.getText().trim() + " ";
                }
                if (casDMU != 0) {
                    dotaz += ", 0 "
                            + ", " + casDMU + " "
                            + ", " + VyrobenoKusuTextField1.getText().trim() + " ";
                }
                if (casDrat != 0) {
                    dotaz += ", 0 "
                            + ", " + casDrat + " "
                            + ", " + VyrobenoKusuTextField1.getText().trim() + " ";
                }
                dotaz += ")";
            }

            
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
                //int programyId = 0;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void naskladnitVyrobky() {       
        long transakce_id = -1;
        int umisteni_id = -1;
        try {
            ResultSet transakce = PripojeniDB.dotazS("SELECT MAX(sklady_vyrobky_transakce_id) FROM logistika.sklady_vyrobky_transakce");
            while (transakce.next()) {
                transakce_id = transakce.getLong(1) + 1;
            }
            if (RegalTextField1.getText().trim().length() > 0) {
                try {
                    ResultSet umisteni = PripojeniDB.dotazS("SELECT sklady_vyrobky_umisteni_id "
                            + "FROM logistika.sklady_vyrobky_umisteni "
                            + "WHERE sklady_vyrobky_umisteni_nazev = " + TextFunkce1.osetriZapisTextDB1(RegalTextField1.getText().trim()));
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
                                + ", " + TextFunkce1.osetriZapisTextDB1(RegalTextField1.getText().trim())
                                + ", ' ','Vloženo automaticky', " + poradi_id + ")";
                      
                        int a = PripojeniDB.dotazIUD(dotaz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                try {
                    ResultSet umisteni = PripojeniDB.dotazS("SELECT sklady_vyrobky_umisteni_id "
                            + "FROM logistika.sklady_vyrobky_umisteni "
                            + "WHERE sklady_vyrobky_umisteni_nazev = " + TextFunkce1.osetriZapisTextDB1("Nezařazeno"));
                    while (umisteni.next()) {
                        umisteni_id = umisteni.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int pocet_mj = Integer.valueOf(VyrobenoKusuTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO logistika.sklady_vyrobky_transakce("
                    + "sklady_vyrobky_transakce_id, "
                    + "sklady_vyrobky_transakce_sklad_id, "
                    + "sklady_vyrobky_transakce_umisteni_id, "
                    + "sklady_vyrobky_transakce_druh_id, "
                    + "sklady_vyrobky_transakce_pocet_mj, "
                    + "sklady_vyrobky_transakce_vykres_cislo) "
                    + "VALUES( " + transakce_id + ", " + 1 + ", " + umisteni_id + ", " + 200 + ", " + pocet_mj + ", "
                    + TextFunkce1.osetriZapisTextDB1(vykresy_cislo) + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
        UlozButton1.setEnabled(false);
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cisloPruvodkyLabel1 = new javax.swing.JLabel();
        cisloVykresuLabel1 = new javax.swing.JLabel();
        nazevSoucastiLabel1 = new javax.swing.JLabel();
        terminDodaniLabel1 = new javax.swing.JLabel();
        RegalTextField1 = new javax.swing.JTextField();
        VyrobenoKusuTextField1 = new javax.swing.JTextField();
        UlozButton1 = new javax.swing.JButton();
        ZrusButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dokončení průvodky");
        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().add(jLabel1, new java.awt.GridBagConstraints());

        jLabel2.setText("Číslo průvodky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Název součásti :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText("Termín dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText("Regál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setText("Vyrobeno kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel7, gridBagConstraints);

        cisloPruvodkyLabel1.setText("jLabel8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(cisloPruvodkyLabel1, gridBagConstraints);

        cisloVykresuLabel1.setText("jLabel8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(cisloVykresuLabel1, gridBagConstraints);

        nazevSoucastiLabel1.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(nazevSoucastiLabel1, gridBagConstraints);

        terminDodaniLabel1.setText("jLabel10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(terminDodaniLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(RegalTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(VyrobenoKusuTextField1, gridBagConstraints);

        UlozButton1.setText("Uložit");
        UlozButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UlozButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 49, 5, 1);
        getContentPane().add(UlozButton1, gridBagConstraints);

        ZrusButton1.setText("Zrušit");
        ZrusButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZrusButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(ZrusButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UlozButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UlozButton1ActionPerformed
        ulozPruvodku();
        this.dispose();
    }//GEN-LAST:event_UlozButton1ActionPerformed

    private void ZrusButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZrusButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_ZrusButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RegalTextField1;
    private javax.swing.JButton UlozButton1;
    private javax.swing.JTextField VyrobenoKusuTextField1;
    private javax.swing.JButton ZrusButton1;
    private javax.swing.JLabel cisloPruvodkyLabel1;
    private javax.swing.JLabel cisloVykresuLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel nazevSoucastiLabel1;
    private javax.swing.JLabel terminDodaniLabel1;
    // End of variables declaration//GEN-END:variables
}
