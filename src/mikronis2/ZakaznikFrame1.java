/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mikronis2.HlavniRamec.ALUdalosti;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaZakaznik1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalRozsirenaModel1;

/**
 *
 * @author Favak
 */
public class ZakaznikFrame1 extends javax.swing.JFrame {

    private int idSubjekt = -1;
    private ActionListener alUdalosti;
    private RoletkaUniverzalRozsirenaModel1 RoletkaModelZakaznici, RoletkaModelDruhy, RoletkaModelStaty;
    private boolean novy = true;
    private TridaZakaznik1 tz1;

    /**
     * Creates new form ZakaznikFrame1
     */
    public ZakaznikFrame1(boolean novy) {
        initComponents();
        initStaty();
        initSubjektyTrhuDruhy();
        this.novy = novy;
        alUdalosti = new ALUdalosti();
        if (novy == false) {
            jCBVyber.setVisible(true);
            jLVyber.setVisible(true);
            initSubjektyTrhu();
            this.setTitle("Úprava zákaznických údajů");
        } else {
            jCBVyber.setVisible(false);
            jLVyber.setVisible(false);
            this.setTitle("Vytvoření nového zákazníka");
        }
        this.setVisible(true);
        jBPotvrdit.addActionListener(alUdalosti);
        jBZrusit.addActionListener(alUdalosti);
        jBDodAdresy.addActionListener(alUdalosti);
    }

    private void initSubjektyTrhu() {
        RoletkaModelZakaznici = new RoletkaUniverzalRozsirenaModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_nazev FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_nazev ASC", "Vše", null,
                "V databázi nejsou zadány zákazníci", 0); // bylo ...vs_id
        jCBVyber.setModel(RoletkaModelZakaznici);
        jCBVyber.addActionListener(alUdalosti);
        jCBVyber.setActionCommand("zmenaZakaznik");
    }

    private void initSubjektyTrhuDruhy() {
        RoletkaModelDruhy = new RoletkaUniverzalRozsirenaModel1(
                "SELECT subjekty_trhu_druhy_id, subjekty_trhu_druhy_nazev FROM spolecne.subjekty_trhu_druhy "
                + "ORDER BY subjekty_trhu_druhy_poradi_vyber ASC", "Vše", null,
                "V databázi nejsou zadány druh subjektu trhu", 0); // bylo ...vs_id
        jCBDruhZakaznik.setModel(RoletkaModelDruhy);
    }

    private void initStaty() {
        RoletkaModelStaty = new RoletkaUniverzalRozsirenaModel1(
                "SELECT staty_id, staty_nazev FROM spolecne.staty "
                + "ORDER BY staty_id ASC", "Vše", null,
                "V databázi nejsou zadány státy", 0); // bylo ...vs_id
        jCBStat.setModel(RoletkaModelStaty);
    }

    private void getDataZakaznik() {
        idSubjekt = ((DvojiceCisloRetez) RoletkaModelZakaznici.getSelectedItem()).cislo();
        tz1 = new TridaZakaznik1();
        tz1.selectData(idSubjekt);
        jTFFirma.setText(tz1.getNazev());
        jTFZkratka.setText(tz1.getPopis());
        RoletkaModelDruhy.setDataOId(tz1.getIdDruhZakaznik());
        jTFUst.setText(tz1.getUstNr());
        jTFVat.setText(tz1.getVatNr());
        jTFAdresa.setText(tz1.getAdresa());
        jTFMesto.setText(tz1.getMesto());
        jTFPSC.setText(tz1.getPsc());
        RoletkaModelStaty.setDataOId(tz1.getIdStat());
        jTFTelefon.setText(tz1.getTelefony());
        jTFEmail.setText(tz1.geteMaily());
        jTFMikronUcet.setText(tz1.getMikronBank());
        jTFMikronIban.setText(tz1.getMikronIban());
        jTFPrvniRadek.setText(tz1.getPrvniRadek());
        jTFDruhyRadek.setText(tz1.getDruhyRadek());
        jCBAktivni.setSelected(tz1.isAktivni());
        
    }

    private void zrusit() {
        this.dispose();
    }

    private void ulozit() {
        tz1.setIdDruhZakaznik(((DvojiceCisloRetez) RoletkaModelDruhy.getSelectedItem()).cislo());
        tz1.setIdStat(((DvojiceCisloRetez) RoletkaModelStaty.getSelectedItem()).cislo());
        tz1.setNazev((jTFFirma.getText().trim()));
        tz1.setPopis((jTFZkratka.getText().trim()));
        tz1.setUstNr((jTFUst.getText().trim()));
        tz1.setVatNr((jTFVat.getText().trim()));
        tz1.setAdresa((jTFAdresa.getText().trim()));
        tz1.setMesto((jTFMesto.getText().trim()));
        tz1.setPsc((jTFPSC.getText().trim()));
        tz1.setTelefony((jTFTelefon.getText().trim()));
        tz1.seteMaily((jTFEmail.getText().trim()));
        tz1.setMikronBank((jTFMikronUcet.getText().trim()));
        tz1.setMikronIban((jTFMikronIban.getText().trim()));
        tz1.setPrvniRadek((jTFPrvniRadek.getText().trim()));
        tz1.setDruhyRadek((jTFDruhyRadek.getText().trim()));
        tz1.setAktivni(jCBAktivni.isSelected());
        
        if (novy == true) {
            tz1.insertData();
        } else {
            tz1.updateData();
        }
        this.dispose();
    }

    class ALUdalosti implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("zmenaZakaznik")) {
                getDataZakaznik();
            }
            if (e.getSource() == jBZrusit) {
                zrusit();
            }
            if (e.getSource() == jBPotvrdit) {
                ulozit();
            }
            if (e.getSource() == jBDodAdresy) {
                if(((DvojiceCisloRetez) RoletkaModelZakaznici.getSelectedItem()).cislo() > 0) {
                    DodAdresyFrame1 dodAdr = new DodAdresyFrame1(((DvojiceCisloRetez) RoletkaModelZakaznici.getSelectedItem()).cislo(),
                            ((DvojiceCisloRetez) RoletkaModelZakaznici.getSelectedItem()).toString());
                }                
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

        jTextField2 = new javax.swing.JTextField();
        jLVyber = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jCBVyber = new javax.swing.JComboBox();
        jTFFirma = new javax.swing.JTextField();
        jTFZkratka = new javax.swing.JTextField();
        jTFAdresa = new javax.swing.JTextField();
        jTFMesto = new javax.swing.JTextField();
        jTFPSC = new javax.swing.JTextField();
        jCBStat = new javax.swing.JComboBox();
        jCBDruhZakaznik = new javax.swing.JComboBox();
        jTFUst = new javax.swing.JTextField();
        jTFVat = new javax.swing.JTextField();
        jTFTelefon = new javax.swing.JTextField();
        jTFEmail = new javax.swing.JTextField();
        jTFMikronUcet = new javax.swing.JTextField();
        jTFMikronIban = new javax.swing.JTextField();
        jBPotvrdit = new javax.swing.JButton();
        jBZrusit = new javax.swing.JButton();
        jBDodAdresy = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTFPrvniRadek = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTFDruhyRadek = new javax.swing.JTextField();
        jCBAktivni = new javax.swing.JCheckBox();

        jTextField2.setText("jTextField2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editace zákazníka");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLVyber.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLVyber, gridBagConstraints);

        jLabel2.setText("Firma :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("Zkratka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Adresa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText("Město :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText("PSČ :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setText("Druh zákazníka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel7, gridBagConstraints);

        jLabel8.setText("Stát :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel9.setText("IČ / Ust. Id. Nr. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel9, gridBagConstraints);

        jLabel10.setText("DIČ / Vat. Id. Nr. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel10, gridBagConstraints);

        jLabel11.setText("Telefon :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel11, gridBagConstraints);

        jLabel12.setText("E-mail :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel12, gridBagConstraints);

        jLabel13.setText("Mikron číslo účtu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel13, gridBagConstraints);

        jLabel14.setText("Mikron IBAN :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel14, gridBagConstraints);

        jCBVyber.setMaximumRowCount(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBVyber, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFFirma, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFZkratka, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFAdresa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFMesto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFPSC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBStat, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jCBDruhZakaznik, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFUst, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFVat, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFTelefon, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFEmail, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFMikronUcet, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFMikronIban, gridBagConstraints);

        jBPotvrdit.setText("Potvrdit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 28);
        getContentPane().add(jBPotvrdit, gridBagConstraints);

        jBZrusit.setText("Zrušit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.ipadx = 24;
        getContentPane().add(jBZrusit, gridBagConstraints);

        jBDodAdresy.setText("Dodací adresy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        getContentPane().add(jBDodAdresy, gridBagConstraints);

        jLabel1.setText("Faktura 1.řádek");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFPrvniRadek, gridBagConstraints);

        jLabel15.setText("Faktura 2. řádek");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jLabel15, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jTFDruhyRadek, gridBagConstraints);

        jCBAktivni.setSelected(true);
        jCBAktivni.setText("aktivní zákazník");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 0);
        getContentPane().add(jCBAktivni, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBDodAdresy;
    private javax.swing.JButton jBPotvrdit;
    private javax.swing.JButton jBZrusit;
    private javax.swing.JCheckBox jCBAktivni;
    private javax.swing.JComboBox jCBDruhZakaznik;
    private javax.swing.JComboBox jCBStat;
    private javax.swing.JComboBox jCBVyber;
    private javax.swing.JLabel jLVyber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTFAdresa;
    private javax.swing.JTextField jTFDruhyRadek;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFFirma;
    private javax.swing.JTextField jTFMesto;
    private javax.swing.JTextField jTFMikronIban;
    private javax.swing.JTextField jTFMikronUcet;
    private javax.swing.JTextField jTFPSC;
    private javax.swing.JTextField jTFPrvniRadek;
    private javax.swing.JTextField jTFTelefon;
    private javax.swing.JTextField jTFUst;
    private javax.swing.JTextField jTFVat;
    private javax.swing.JTextField jTFZkratka;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
