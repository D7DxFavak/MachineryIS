/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Favak
 */
public class NovyProgramFrame1 extends javax.swing.JFrame {

    private Vector vsVykresy;
    private Vector vrVykresy;
    private Vector vsVykresyZmeny;
    private Vector vrVykresyZmeny;
    private ComboBoxVykresyUdalosti VykresyListener;

      /** Creates new form NovyVykresFrame */
    public NovyProgramFrame1() {
        initComponents();
        this.setVisible(true);
        VykresyListener = new ComboBoxVykresyUdalosti();

        vsVykresy = new Vector();
        vrVykresy = new Vector();

        vsVykresyZmeny = new Vector();
        vrVykresyZmeny = new Vector();

        initVykresy();

    }

    public NovyProgramFrame1(String cisloVykresu, String zmenaVykresu) {
        initComponents();
        this.setVisible(true);
        VykresyListener = new ComboBoxVykresyUdalosti();

        vsVykresy = new Vector();
        vrVykresy = new Vector();

        vsVykresyZmeny = new Vector();
        vrVykresyZmeny = new Vector();

        initVykresy();
        vyhledatVykres(cisloVykresu, zmenaVykresu);

    }

    private void initVykresy() {
        VykresyComboBox1.removeAllItems();
        try {
            vsVykresy.removeAllElements();
            vrVykresy.removeAllElements();
            ResultSet vykresy1 = PripojeniDB.dotazS(
                    "SELECT DISTINCT vykresy_cislo FROM spolecne.vykresy ORDER BY vykresy_cislo ASC");

            while (vykresy1.next()) {
                vsVykresy = new Vector();

                try {
                    vsVykresy.add(new String(vykresy1.getString(1)));
                } catch (Exception e) {
                    vsVykresy.add(new String(""));
                }
                vrVykresy.add(vsVykresy);
                VykresyComboBox1.addItem((String) vsVykresy.get(0));

            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        VykresyComboBox1.setSelectedIndex(0);
        initVykresyZmeny();
        VykresyComboBox1.addItemListener(VykresyListener);
    }

    private void initVykresyZmeny() {
        System.out.println("initVykresyZmeny()");
        VykresyZmenyComboBox1.removeAllItems();
        try {
            vsVykresyZmeny.removeAllElements();
            vrVykresyZmeny.removeAllElements();
            ResultSet vykresyZmeny1 = PripojeniDB.dotazS(
                    "SELECT vykresy_id, vykresy_revize FROM spolecne.vykresy WHERE vykresy_cislo = '"
                    + ((String) ((Vector) vrVykresy.get(VykresyComboBox1.getSelectedIndex())).get(0)) + "'");

            while (vykresyZmeny1.next()) {
                vsVykresyZmeny = new Vector();
                vsVykresyZmeny.add(new Integer(vykresyZmeny1.getInt(1)));
                try {
                    vsVykresyZmeny.add(new String(vykresyZmeny1.getString(2)));
                } catch (Exception e) {
                    vsVykresyZmeny.add(new String(""));
                }
                vrVykresyZmeny.add(vsVykresyZmeny);
                VykresyZmenyComboBox1.addItem((String) vsVykresyZmeny.get(1));

            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch*/
        if (VykresyZmenyComboBox1.getItemCount() > 0) {
            VykresyZmenyComboBox1.setSelectedIndex(0);
        }
        //System.out.println("vykresy zmeny : " + (Integer) vsVykresyZmeny.get(0));
    }

    private void vyhledatVykres(String cisloVykresu, String zmenaVykresu) {
        for (int i = 0; i < vrVykresy.size(); i++) {
            if (cisloVykresu.trim().equals((String) ((Vector) vrVykresy.get(i)).get(0))) {
                VykresyComboBox1.setSelectedIndex(i);
            }
        }

    }

    private void ulozProgram() {
        long idProgramy = 0;
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(programy_id) FROM spolecne.programy");
            while (id.next()) {
                idProgramy = id.getLong(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dotaz;
        /* if(ZmenaProgramuTextField1.getText().trim().length() > 0) {
        dotaz = "INSERT INTO spolecne.programy " +
        "(programy_id, programy_cislo, programy_revize, " +
        "programy_material_rozmer, programy_poznamky) " +
        "VALUES (" +idProgramy + ", '" +
        CisloProgramuTextField1.getText() +"', '" +
        ZmenaProgramuTextField1.getText() +"', '" +
        MaterialRozmerTextField1.getText() +"', '" +
        PoznamkyTextField1.getText() + "')" ;
        }*/

        dotaz = "INSERT INTO spolecne.programy "
                + "(programy_id, programy_cislo, programy_revize, "
                + "programy_poznamky) "
                + "VALUES (" + idProgramy + ", '"
                + CisloProgramuTextField1.getText() + "', '"
                + RevizeProgramuTextField1.getText()  + "', '"
                + PoznamkyTextField1.getText() + "')";


        try {
            int a = PripojeniDB.dotazIUD(dotaz);
            //int programyId = 0;


        } catch (Exception e) {
            e.printStackTrace();
        }

        int idVykresu;
        if (VykresyZmenyComboBox1.getSelectedIndex() > -1) {
            idVykresu = (Integer) ((Vector) vrVykresyZmeny.get(VykresyZmenyComboBox1.getSelectedIndex())).get(0);
        } else {
            idVykresu = (Integer) ((Vector) vrVykresyZmeny.get(0)).get(0);
        }

        dotaz = "INSERT INTO spolecne.vazba_programy_vykresy"
                + "(vazba_programy_vykresy_vykresy_id, "
                + "vazba_programy_vykresy_programy_id) "
                + "VALUES(" + idVykresu + ", " + idProgramy + ")";
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
            //int programyId = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }



        this.dispose();

    }
    
      class ComboBoxVykresyUdalosti implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            initVykresyZmeny();
        }
    }//konec FLUdalosti
    
    /* This method is called from within the constructor to initialize the form.
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
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CisloProgramuTextField1 = new javax.swing.JTextField();
        RevizeProgramuTextField1 = new javax.swing.JTextField();
        PoznamkyTextField1 = new javax.swing.JTextField();
        VykresyZmenyComboBox1 = new javax.swing.JComboBox();
        UlozButton1 = new javax.swing.JButton();
        ZrusButton1 = new javax.swing.JButton();
        VykresyComboBox1 = new javax.swing.JComboBox();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Program");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Revize programu :");
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

        jLabel6.setText("Změna výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel4.setText("Číslo programu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel3.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(CisloProgramuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(RevizeProgramuTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(PoznamkyTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(VykresyZmenyComboBox1, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(VykresyComboBox1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UlozButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UlozButton1MouseClicked
        ulozProgram();
        this.dispose();
    }//GEN-LAST:event_UlozButton1MouseClicked

    private void ZrusButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ZrusButton1MouseClicked
        this.dispose();
    }//GEN-LAST:event_ZrusButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CisloProgramuTextField1;
    private javax.swing.JTextField PoznamkyTextField1;
    private javax.swing.JTextField RevizeProgramuTextField1;
    private javax.swing.JButton UlozButton1;
    private javax.swing.JComboBox VykresyComboBox1;
    private javax.swing.JComboBox VykresyZmenyComboBox1;
    private javax.swing.JButton ZrusButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
