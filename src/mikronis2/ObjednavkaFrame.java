/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2;

import java.awt.Color;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JFrame;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.dbtridy.TridaObjednavka1;
import mikronis2.dbtridy.TridaVykres1;
import mikronis2.tridy.DvojiceCisloRetez;
import mikronis2.tridy.RoletkaUniverzalModel1;

/**
 *
 * @author Favak
 */
public class ObjednavkaFrame extends javax.swing.JFrame {

    private TridaObjednavka1 tObj;
    private TridaVykres1 tv1;
    private ArrayList<TridaVykres1> arTV1;
    private ArrayList<TridaVykres1> arVykresyZmeny;
    private Vector vrRamcova;
    private Vector vsRamcova;
    private Vector vsNabidka;
    private Vector vrNabidka;
    private TridaVykres1 vykresZmena;
    protected RoletkaUniverzalModel1 roletkaModelZakaznici, roletkaModelPovrchUprava, roletkaModelMeny;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    protected java.text.NumberFormat nf2, nf3;
    private long idObjednavky;
    private Vector vsKapacity;
    private boolean edit = false;
    private boolean editRamcova = false;
    

    /**
     * Creates new form ObjednavkaFrame
     */
    public ObjednavkaFrame(String title, String buttonText) {
        initComponents();
        this.setTitle(title);
        this.setVisible(true);
        initKomponenty();
        
    }

    public ObjednavkaFrame(long idObjednavky, boolean edit, String title, String buttonText) {
        initComponents();
        
     
                
                
        //novaPruvodka = false;
        this.setTitle(title);
        jBUlozit.setText(buttonText);

        this.setVisible(true);
        initKomponenty();

        tObj = new TridaObjednavka1();
        this.edit = edit;
        if (edit == true) {
            this.idObjednavky = idObjednavky;
        }
        //System.out.println("id Objednavky : " + idObjednavky);

        initHodnoty(idObjednavky);
    }

    private void initKomponenty() {
        
           vrRamcova = new Vector();
        vsRamcova = new Vector();

        arTV1 = new ArrayList<TridaVykres1>();
        tv1 = new TridaVykres1();

        vykresZmena = new TridaVykres1();
        arVykresyZmeny = new ArrayList<TridaVykres1>();
        vsKapacity = new Vector();
        initRoletky();

        
        vsNabidka = new Vector();
        vrNabidka = new Vector();
        //

        NabidkyComboBox1.removeAllItems();
        ZakaznikComboBox1.requestFocus();
        //initVykresy();
    }

    private void initRoletky() {
        roletkaModelZakaznici = new RoletkaUniverzalModel1(
                "SELECT subjekty_trhu_id, subjekty_trhu_nazev FROM spolecne.subjekty_trhu WHERE subjekty_trhu_druh_id <> 7 AND subjekty_trhu_aktivni = TRUE "
                + "ORDER BY subjekty_trhu_nazev",
                "V databázi nejsou zadáni zákazníci"); // bylo ...vs_id
        ZakaznikComboBox1.setModel(roletkaModelZakaznici);

        if (ZakaznikComboBox1.getItemCount() > 0) {
            roletkaModelZakaznici.setDataOId(MikronIS2.indexZakaznika);
        }
        if (MikronIS2.indexZakaznika != 12) {
            MaterialBBHLabel1.setVisible(false);
            MaterialBBHTextField1.setVisible(false);
        }

        roletkaModelPovrchUprava = new RoletkaUniverzalModel1(
                "SELECT druhy_povrchova_uprava_id, druhy_povrchova_uprava_nazev FROM spolecne.druhy_povrchova_uprava "
                + "ORDER BY druhy_povrchova_uprava_poradi_vyberu ASC",
                "V databázi nejsou zadány povrchové úpravy"); // bylo ...vs_id
        PovrchovaUpravaComboBox1.setModel(roletkaModelPovrchUprava);

        roletkaModelMeny = new RoletkaUniverzalModel1(
                "SELECT meny_id, meny_zkratka FROM spolecne.meny ORDER BY meny_poradi_vyberu ASC",
                "V databázi nejsou zadány měny"); // bylo ...vs_id
        MenaComboBox1.setModel(roletkaModelMeny);
    }

    private void cenaCelkem() {
        if ((PocetKusuTextField1.getText().length() > 0) && (CenaZaKusTextField1.getText().length() > 0)) {
            int PocetKusu = Integer.valueOf((String) PocetKusuTextField1.getText()).intValue();
            double CenaZaKus = Double.valueOf((String) CenaZaKusTextField1.getText()).doubleValue();
            CenaCelkemLabel1.setText((PocetKusu * CenaZaKus) + "");
        }
    }

    private void initHodnoty(long idObjednavky) {
        tObj = new TridaObjednavka1(idObjednavky);
        roletkaModelZakaznici.setDataOId(tObj.getIdZakaznik());

        if (tObj.getIdZakaznik() != 12) {
            MaterialBBHLabel1.setVisible(false);
            MaterialBBHTextField1.setVisible(false);
        } else {
            MaterialBBHLabel1.setVisible(true);
            MaterialBBHTextField1.setVisible(true);
        }

        CisloObjednavkyTextField1.setText(tObj.getCisloObjednavky());
        DatumObjednaniTextField1.setText(df.format(tObj.getDatumObjednani()));
        //NazevSoucastiTextField1.setText((String) vsObjednavka1.get(3));

        TridaVykres1 tv1 = new TridaVykres1(tObj.getIdVykres());

        CisloVykresuTextField1.setText(tv1.getCislo());
        /*
         * for (int i = 0; i < vrVykresyZmeny.size(); i++) { if (((Integer)
         * ((Vector) vrVykresyZmeny.get(i)).get(0)).equals((Integer)
         * vsObjednavka1.get(4))) { VykresyZmenyComboBox1.setSelectedIndex(i); }
         * }
         */
        ZmenaVykresuTextField1.setText(tv1.getRevize());
        NazevSoucastiTextField1.setText(tv1.getNazev());
        PocetKusuTextField1.setText(tObj.getPocetObjednanychKusu() + "");
        CenaZaKusTextField1.setText(tObj.getCenaKus());
        cenaCelkem();
        roletkaModelMeny.setDataOId(tObj.getIdMena());

        TerminDodaniTextField1.setText(df.format(tObj.getDatumDodani()));
        if (tObj.getDatumPotvrzeni() != null) {
            TerminPotvrzeniTextField1.setText(df.format(tObj.getDatumPotvrzeni()));
        }
        roletkaModelPovrchUprava.setDataOId(tObj.getIdPovrchUprava());
        RozmerMaterialuTextField1.setText(tObj.getMaterialRozmer());

        try {
            ExpediceTextField1.setText(df.format(tObj.getDatumExpedice()));
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            CisloFakturyTextField1.setText(tObj.getCisloFaktury());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            PoznamkaTextField1.setText(tObj.getPoznamka());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            KusuNavicTextField1.setText(tObj.getKusuNavic() + "");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        try {
            KusuVyrobitTextField1.setText(tObj.getKusuVyrobit() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tObj.getMaterial() != null) {
            try {
                MaterialBBHTextField1.setText((tObj.getMaterial() == null) ? "" : tObj.getMaterial());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ReklamaceCheckBox1.setSelected(tObj.isReklamace());
        jCBVirtualni.setSelected(tObj.isVirtualni());

        try {
            ResultSet kapacity = PripojeniDB.dotazS(
                    "SELECT vyrobni_kapacita_id, vyrobni_kapacita_cas_mcv, "
                    + "vyrobni_kapacita_cas_tornado, vyrobni_kapacita_cas_dmu, vyrobni_kapacita_soustruh "
                    + "FROM spolecne.vyrobni_kapacita "
                    + "WHERE vyrobni_kapacita_cislo_vykresu = '" + CisloVykresuTextField1.getText().trim() + "' LIMIT 1");
            vsKapacity = new Vector();
            while (kapacity.next()) {
                vsKapacity.add(new Integer(kapacity.getInt(1)));
                vsKapacity.add(new Float(kapacity.getFloat(2)));
                vsKapacity.add(new Float(kapacity.getFloat(3)));
                vsKapacity.add(new Float(kapacity.getFloat(4)));
                try {
                    vsKapacity.add(new Integer(kapacity.getInt(5)));
                } catch (Exception e) {
                    vsKapacity.add(new Integer(0));
                }
            }
            if (vsKapacity.size() > 0) {
                McvTextField1.setText((Float) vsKapacity.get(1) + "");
                TornadoTextField1.setText((Float) vsKapacity.get(2) + "");
                DMUTextField1.setText((Float) vsKapacity.get(3) + "");
                if (((Integer) vsKapacity.get(4)).intValue() == 1) {
                    OstatniCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(1)).floatValue() > 0) {
                    McvCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(2)).floatValue() > 0) {
                    TornadoCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(3)).floatValue() > 0) {
                    DMUCheckBox1.setSelected(true);
                }

            } else {
                UpravaKapacitTextField1.setText("Novy vykres !!!");
                UpravaKapacitTextField1.setForeground(Color.red);
            }

            if (SQLFunkceObecne2.selectBooleanPole(
                    "SELECT EXISTS "
                    + "(SELECT vazba_objednavky_ramec_ramec_id "
                    + "FROM spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec_objednavky_id = "
                    + idObjednavky + ")") == true) {
                RamcovaCheckBox1.setSelected(true);
                editRamcova = true;
                String dotazKusy = "SELECT SUM(objednavky_pocet_objednanych_kusu ) "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_ramec_id = "
                        + "(SELECT vazba_objednavky_ramec_ramec_id "
                        + "FROM spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_objednavky_id = " + idObjednavky + ") "
                        + "AND vazba_objednavky_ramec_objednavky_id = objednavky_id ";
                //System.out.println(dotazKusy);
                ResultSet kusyRamcObj = PripojeniDB.dotazS(dotazKusy);
                while (kusyRamcObj.next()) {
                    PocetObjRamecLabel1.setText(kusyRamcObj.getInt(1) + "");
                }
            }
            if (SQLFunkceObecne2.selectBooleanPole(
                    "SELECT EXISTS "
                    + "(SELECT vazba_objednavky_kanban_kanban_id "
                    + "FROM spolecne.vazba_objednavky_kanban "
                    + "WHERE vazba_objednavky_kanban_objednavky_id = "
                    + idObjednavky + ")") == true) {
                KanbanCheckBox1.setSelected(true);

            }

            kontrolaVykresu();

            if (RamcovaCheckBox1.isSelected()
                    == true) {
                nastavRamcova(idObjednavky);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void nastavRamcova(long idObjednavky) {
        try {
            String ramcovaCislo = "";
            ResultSet ramcovaIdSet = PripojeniDB.dotazS("SELECT ramec_objednavky_cislo_objednavky "
                    + "FROM spolecne.vazba_objednavky_ramec "
                    + "CROSS JOIN spolecne.ramec_objednavky "
                    + "WHERE vazba_objednavky_ramec_objednavky_id = " + idObjednavky + " "
                    + "AND ramec_objednavky_id = vazba_objednavky_ramec_ramec_id ");
            while (ramcovaIdSet.next()) {
                ramcovaCislo = ramcovaIdSet.getString(1);
            }
            RamcoveObjednavkyComboBox1.setSelectedItem(ramcovaCislo);
            String dotazKusy = "SELECT SUM(objednavky_pocet_objednanych_kusu ) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec_ramec_id = "
                    + "(SELECT vazba_objednavky_ramec_ramec_id "
                    + "FROM spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec_objednavky_id = " + idObjednavky + ") "
                    + "AND vazba_objednavky_ramec_objednavky_id = objednavky_id ";

            ResultSet kusyRamcObj = PripojeniDB.dotazS(dotazKusy);
            while (kusyRamcObj.next()) {
                PocetObjRamecLabel1.setText(kusyRamcObj.getInt(1) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ulozObjednavku() {
        MikronIS2.indexZakaznika = ZakaznikComboBox1.getSelectedIndex();
        long objednavky_id = 0;
        if (edit == false) {
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(objednavky_id) FROM spolecne.objednavky");
                while (id.next()) {
                    objednavky_id = id.getLong(1) + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            objednavky_id = this.idObjednavky;
        }

        int idZakaznik;
        if (ZakaznikComboBox1.getSelectedIndex() > -1) {
            idZakaznik = ((DvojiceCisloRetez) roletkaModelZakaznici.getSelectedItem()).cislo();
        } else {
            idZakaznik = 0;
        }

        int idPovrchovaUprava;
        if (PovrchovaUpravaComboBox1.getSelectedIndex() > -1) {
            idPovrchovaUprava = ((DvojiceCisloRetez) roletkaModelPovrchUprava.getSelectedItem()).cislo();
        } else {
            idPovrchovaUprava = 0;
        }

        int idTepelneZpracovani;
        idTepelneZpracovani = 4;

        int idMena;
        if (MenaComboBox1.getSelectedIndex() > -1) {
            idMena = ((DvojiceCisloRetez) roletkaModelMeny.getSelectedItem()).cislo();
        } else {
            idMena = 0;
        }
        int idVykresu = -1;
        String dotazVykres = "SELECT vykresy_id "
                + "FROM spolecne.vykresy "
                + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "' ";
        if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
            dotazVykres += "AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
        }
        try {
            ResultSet vykresCisloDotaz = PripojeniDB.dotazS(dotazVykres);
            while (vykresCisloDotaz.next()) {
                vykresZmena = new TridaVykres1();
                idVykresu = (vykresCisloDotaz.getInt(1));
            }
        } catch (Exception e) {
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání objednávky", "Zadaný výkres neexistuje");
            e.printStackTrace();
        }

        /*
         * if (VykresyZmenyComboBox1.getSelectedIndex() > -1) { idVykresu =
         * (Integer) ((Vector)
         * vrVykresyZmeny.get(VykresyZmenyComboBox1.getSelectedIndex())).get(0);
         * } else { idVykresu = (Integer) ((Vector)
         * vrVykresyZmeny.get(0)).get(0);
         }
         */
        String dotaz = "";
        if (edit == false) {
            dotaz = "INSERT INTO spolecne.objednavky( "
                    + "objednavky_id, objednavky_zakaznik_id, objednavky_cislo_objednavky, "
                    + "objednavky_datum_objednani, objednavky_nazev_soucasti, objednavky_cislo_vykresu, "
                    + "objednavky_pocet_objednanych_kusu, objednavky_cena_za_kus, objednavky_mena_id, "
                    + "objednavky_termin_dodani, objednavky_material_rozmer, "
                    + "objednavky_povrchova_uprava_id, objednavky_tepelne_zpracovani_id";

            boolean expediceZadano = TextFunkce1.osetriDatum(ExpediceTextField1.getText());
            if (expediceZadano == true) {
                dotaz += ", objednavky_datum_expedice";
            }
            boolean cisloFakturyZadano = false;
            if (CisloFakturyTextField1.getText().length() > 0) {
                cisloFakturyZadano = true;
                dotaz += ", objednavky_cislo_faktury";
            }
            boolean kusuNavicZadano = false;
            if (KusuNavicTextField1.getText().length() > 0) {
                kusuNavicZadano = true;
                dotaz += ", objednavky_kusu_navic";
            }
            boolean kusuVyrobitZadano = false;
            if (KusuVyrobitTextField1.getText().length() > 0) {
                kusuVyrobitZadano = true;
                dotaz += ", objednavky_kusu_vyrobit";
            }
            boolean poznamkaZadano = false;
            if (PoznamkaTextField1.getText().length() > 0) {
                poznamkaZadano = true;
                dotaz += ", objednavky_poznamka";
            }
            boolean materialZadano = false;
            if (MaterialBBHTextField1.getText().length() > 0) {
                materialZadano = true;
                dotaz += ", objednavky_material";
            }
            boolean terminPotvrzeniZadano = false;
            if (TerminPotvrzeniTextField1.getText().length() > 0) {
                terminPotvrzeniZadano = true;
                dotaz += ", objednavky_datum_potvrzeni";
            }
            boolean reklamaceZadano = false;
            if (ReklamaceCheckBox1.isSelected() == true) {
                reklamaceZadano = true;

            }
            boolean virtualniZadano = false;
            if (jCBVirtualni.isSelected() == true) {
                virtualniZadano = true;

            }
            dotaz += ", objednavky_reklamace, objednavky_virtualni_polozka ";
            dotaz += ") VALUES (" + objednavky_id + ", " + idZakaznik + ", '" + CisloObjednavkyTextField1.getText().trim() + "', '"
                    + DatumObjednaniTextField1.getText().trim() + "', '" + NazevSoucastiTextField1.getText().trim() + "', "
                    + idVykresu + ", " + PocetKusuTextField1.getText().trim() + ", " + CenaZaKusTextField1.getText().trim() + ", "
                    + idMena + ", '" + TerminDodaniTextField1.getText().trim() + "', '"
                    + RozmerMaterialuTextField1.getText().trim() + "', " + idPovrchovaUprava + ", " + idTepelneZpracovani;
            if (expediceZadano == true) {
                dotaz += ", '" + ExpediceTextField1.getText().trim() + "'";
            }
            if (cisloFakturyZadano == true) {
                dotaz += ", '" + CisloFakturyTextField1.getText().trim() + "'";
            }
            if (kusuNavicZadano == true) {
                dotaz += ", " + KusuNavicTextField1.getText().trim() + " ";
            }
            if (kusuVyrobitZadano == true) {
                dotaz += ", " + KusuVyrobitTextField1.getText().trim() + " ";
            }
            if (poznamkaZadano == true) {
                dotaz += ", '" + PoznamkaTextField1.getText().trim() + "'";
            }
            if (materialZadano == true) {
                dotaz += ", " + TextFunkce1.osetriZapisTextDB1(MaterialBBHTextField1.getText().trim()) + " ";
            }
            if (terminPotvrzeniZadano) {
                dotaz += ", " + TextFunkce1.osetriZapisTextDB1(TerminPotvrzeniTextField1.getText().trim()) + " ";
            }
            if (reklamaceZadano == true) {
                dotaz += ", " + true + " ";
            } else if (reklamaceZadano == false) {
                dotaz += ", " + false + " ";
            }
            if (virtualniZadano == true) {
                dotaz += ", " + true + " ";
            } else if (virtualniZadano == false) {
                dotaz += ", " + false + " ";
            }
            dotaz += " )";
            
            
        } else {
            dotaz = "UPDATE spolecne.objednavky "
                    + "SET objednavky_zakaznik_id = " + idZakaznik
                    + ", objednavky_cislo_objednavky = '" + CisloObjednavkyTextField1.getText().trim()
                    + "', objednavky_datum_objednani = '" + DatumObjednaniTextField1.getText().trim()
                    + "', objednavky_nazev_soucasti = '" + NazevSoucastiTextField1.getText().trim()
                    + "', objednavky_cislo_vykresu = " + idVykresu
                    + ", objednavky_pocet_objednanych_kusu = " + PocetKusuTextField1.getText().trim()
                    + ", objednavky_cena_za_kus = " + CenaZaKusTextField1.getText().trim()
                    + ", objednavky_mena_id = " + idMena
                    + ", objednavky_termin_dodani = '" + TerminDodaniTextField1.getText().trim()
                    + "', objednavky_material_rozmer = '" + RozmerMaterialuTextField1.getText().trim()
                    + "', objednavky_povrchova_uprava_id = " + idPovrchovaUprava
                    + ", objednavky_tepelne_zpracovani_id = " + idTepelneZpracovani;

            boolean expediceZadano = TextFunkce1.osetriDatum(ExpediceTextField1.getText().trim());
            if (expediceZadano == true) {
                dotaz += ", objednavky_datum_expedice = '" + ExpediceTextField1.getText().trim() + "'";
            } else {
                dotaz += ", objednavky_datum_expedice = NULL";
            }

            if (TerminPotvrzeniTextField1.getText().length() > 0) {
                dotaz += ", objednavky_datum_potvrzeni = " + TextFunkce1.osetriZapisTextDB1(TerminPotvrzeniTextField1.getText().trim());
            } else {
                dotaz += ", objednavky_datum_potvrzeni = NULL ";
            }

            if (CisloFakturyTextField1.getText().length() > 0) {
                dotaz += ", objednavky_cislo_faktury = '" + CisloFakturyTextField1.getText().trim() + "'";
            } else {
                dotaz += ", objednavky_cislo_faktury = NULL";
            }

            if (KusuNavicTextField1.getText().length() > 0) {

                dotaz += ", objednavky_kusu_navic = " + KusuNavicTextField1.getText().trim();
            }
            if (KusuVyrobitTextField1.getText().length() > 0) {

                dotaz += ", objednavky_kusu_vyrobit = " + KusuVyrobitTextField1.getText().trim();
            }
            if (PoznamkaTextField1.getText().length() > 0) {

                dotaz += ", objednavky_poznamka = '" + PoznamkaTextField1.getText().trim() + "'";
            } else if (PoznamkaTextField1.getText().length() == 0) {
                dotaz += ", objednavky_poznamka = null";
            }
            if (MaterialBBHTextField1.getText().length() > 0) {

                dotaz += ", objednavky_material = " + TextFunkce1.osetriZapisTextDB1(MaterialBBHTextField1.getText().trim()) + " ";
            } else if (PoznamkaTextField1.getText().length() == 0) {
                dotaz += ", objednavky_material = null";
            }
            if (ReklamaceCheckBox1.isSelected() == true) {
                dotaz += ", objednavky_reklamace = true";
            } else {
                dotaz += ", objednavky_reklamace = false";
            }
            if (jCBVirtualni.isSelected() == true) {
                dotaz += ", objednavky_virtualni_polozka = true";
            } else {
                dotaz += ", objednavky_virtualni_polozka = false";
            }
            
            
            dotaz += " WHERE objednavky_id = " + objednavky_id;
        }
        if (CelkovyCasTextField1.getText().trim().length() > 0) {
            String dotaz1 = "UPDATE spolecne.pruvodky SET "
                    + "pruvodky_celkovy_cas = '" + CelkovyCasTextField1.getText().trim() + "' "
                    + "WHERE pruvodky_cislo_vykresu = " + idVykresu + " "
                    + "AND pruvodky_termin_dokonceni = '" + TerminDodaniTextField1.getText().trim() + "'";
            try {
                int a = PripojeniDB.dotazIUD(dotaz1);
            } catch (Exception e) {
                ErrorLabel.setText("Nepodařilo se uložit, chyba v zadaných údajích");
                e.printStackTrace();
            }
        }
        //System.out.println("Ulozeni objednavky " + dotaz);
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
            this.dispose();
        } catch (Exception e) {
            ErrorLabel.setText("Nepodařilo se uložit, chyba v zadaných údajích");
            e.printStackTrace();
        }
        String dotazRamcova = "";
        if (RamcovaCheckBox1.isSelected()) {

            int odvolani = 0;
            if (SQLFunkceObecne2.selectBooleanPole(
                    "SELECT EXISTS "
                    + "(SELECT vazba_objednavky_ramec_ramec_id "
                    + "FROM spolecne.vazba_objednavky_ramec "
                    + "WHERE vazba_objednavky_ramec_ramec_id = "
                    + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0) + ")") == true) {

                String dotazPom = "SELECT MAX(vazba_objednavky_ramec_abruf_cislo) "
                        + "FROM spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_ramec_id = "
                        + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0);
                try {
                    ResultSet odvolaniSet = PripojeniDB.dotazS(dotazPom);
                    while (odvolaniSet.next()) {
                        odvolani = odvolaniSet.getInt(1) + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                odvolani = 1;
            }

            if (edit == false) {

                dotazRamcova = "INSERT INTO spolecne.vazba_objednavky_ramec("
                        + "vazba_objednavky_ramec_ramec_id, "
                        + "vazba_objednavky_ramec_objednavky_id, "
                        + "vazba_objednavky_ramec_abruf_cislo) VALUES ( "
                        + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0) + ", "
                        + objednavky_id + ", " + odvolani + ")";
                try {
                    int a = PripojeniDB.dotazIUD(dotazRamcova);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (SQLFunkceObecne2.selectBooleanPole(
                        "SELECT EXISTS "
                        + "(SELECT vazba_objednavky_ramec_objednavky_id "
                        + "FROM spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_objednavky_id = " + objednavky_id + ")") == false) {
                    dotazRamcova = "INSERT INTO spolecne.vazba_objednavky_ramec("
                            + "vazba_objednavky_ramec_ramec_id, "
                            + "vazba_objednavky_ramec_objednavky_id, "
                            + "vazba_objednavky_ramec_abruf_cislo) VALUES ( "
                            + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0) + ", "
                            + objednavky_id + ", " + odvolani + ")";
                    try {
                        int a = PripojeniDB.dotazIUD(dotazRamcova);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int a = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_objednavky_ramec WHERE "
                                + "vazba_objednavky_ramec_objednavky_id = " + objednavky_id);
                        odvolani = 0;
                        if (SQLFunkceObecne2.selectBooleanPole(
                                "SELECT EXISTS "
                                + "(SELECT vazba_objednavky_ramec_ramec_id "
                                + "FROM spolecne.vazba_objednavky_ramec "
                                + "WHERE vazba_objednavky_ramec_ramec_id = "
                                + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0) + ")") == true) {

                            String dotazPom = "SELECT MAX(vazba_objednavky_ramec_abruf_cislo) "
                                    + "FROM spolecne.vazba_objednavky_ramec "
                                    + "WHERE vazba_objednavky_ramec_ramec_id = "
                                    + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0);
                            try {
                                ResultSet odvolaniSet = PripojeniDB.dotazS(dotazPom);
                                while (odvolaniSet.next()) {
                                    odvolani = odvolaniSet.getInt(1) + 1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            odvolani = 1;
                        }
                        dotazRamcova = "INSERT INTO spolecne.vazba_objednavky_ramec("
                                + "vazba_objednavky_ramec_ramec_id, "
                                + "vazba_objednavky_ramec_objednavky_id, "
                                + "vazba_objednavky_ramec_abruf_cislo) VALUES ( "
                                + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0) + ", "
                                + objednavky_id + ", " + odvolani + ")";
                        try {
                            int b = PripojeniDB.dotazIUD(dotazRamcova);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (RamcovaCheckBox1.isSelected() == false) {
            if (editRamcova == true) {
                String dotazDelete = "DELETE FROM spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_objednavky_id = " + objednavky_id;
                try {
                    int b = PripojeniDB.dotazIUD(dotazDelete);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (KanbanCheckBox1.isSelected()) {
            if (edit == false) {
                int kanbanId = 0;
                int odvolavkaId = 0;
                try {

                    ResultSet kanbanIdSet = PripojeniDB.dotazS("SELECT kanban_objednavky_id, COALESCE(maxID,1) "
                            + "FROM spolecne.kanban_objednavky "
                            + "LEFT JOIN ( "
                            + "SELECT COALESCE(MAX(vazba_objednavky_kanban_abruf_cislo) +1,1) AS maxID, vazba_objednavky_kanban_kanban_id "
                            + "FROM spolecne.vazba_objednavky_kanban GROUP BY vazba_objednavky_kanban_kanban_id) AS x "
                            + "ON kanban_objednavky_id = x.vazba_objednavky_kanban_kanban_id "
                            + "WHERE kanban_objednavky_cislo_vykresu =  " + TextFunkce1.osetriZapisTextDB1(CisloVykresuTextField1.getText().trim()));
                    while (kanbanIdSet.next()) {
                        kanbanId = kanbanIdSet.getInt(1);
                        odvolavkaId = kanbanIdSet.getInt(2);
                    }

                    dotazRamcova = "INSERT INTO spolecne.vazba_objednavky_kanban("
                            + "vazba_objednavky_kanban_kanban_id, "
                            + "vazba_objednavky_kanban_objednavky_id, "
                            + "vazba_objednavky_kanban_abruf_cislo) VALUES ( "
                            + kanbanId + ", "
                            + objednavky_id + ", " + odvolavkaId + ")";

                    int b = PripojeniDB.dotazIUD(dotazRamcova);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (edit == true) {
                int kanbanId = 0;
                int odvolavkaId = 0;
                try {
                    if (SQLFunkceObecne2.selectBooleanPole(
                            "SELECT EXISTS "
                            + "(SELECT vazba_objednavky_kanban_kanban_id "
                            + "FROM spolecne.vazba_objednavky_kanban "
                            + "WHERE vazba_objednavky_kanban_objednavky_id = "
                            + objednavky_id + ")") == false) {
                        ResultSet kanbanIdSet = PripojeniDB.dotazS("SELECT kanban_objednavky_id, COALESCE(maxID,1) "
                                + "FROM spolecne.kanban_objednavky "
                                + "LEFT JOIN ( "
                                + "SELECT COALESCE(MAX(vazba_objednavky_kanban_abruf_cislo) +1,1) AS maxID, vazba_objednavky_kanban_kanban_id "
                                + "FROM spolecne.vazba_objednavky_kanban GROUP BY vazba_objednavky_kanban_kanban_id) AS x "
                                + "ON kanban_objednavky_id = x.vazba_objednavky_kanban_kanban_id "
                                + "WHERE kanban_objednavky_cislo_vykresu =  " + TextFunkce1.osetriZapisTextDB1(CisloVykresuTextField1.getText().trim()));

                        while (kanbanIdSet.next()) {
                            kanbanId = kanbanIdSet.getInt(1);
                            odvolavkaId = kanbanIdSet.getInt(2);
                        }

                        dotazRamcova = "INSERT INTO spolecne.vazba_objednavky_kanban("
                                + "vazba_objednavky_kanban_kanban_id, "
                                + "vazba_objednavky_kanban_objednavky_id, "
                                + "vazba_objednavky_kanban_abruf_cislo) VALUES ( "
                                + kanbanId + ", "
                                + objednavky_id + ", " + odvolavkaId + ")";

                        int b = PripojeniDB.dotazIUD(dotazRamcova);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (KanbanCheckBox1.isSelected() == false) {
            try {
                int c = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_objednavky_kanban "
                        + "WHERE vazba_objednavky_kanban_objednavky_id = " + objednavky_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (KonecNabidkaCheckBox1.isSelected() == true) {
            dotaz = "UPDATE spolecne.nabidky "
                    + "SET nabidky_ukoncena = TRUE "
                    + "WHERE nabidky_id = " + (Integer) ((Vector) vrNabidka.get(NabidkyComboBox1.getSelectedIndex())).get(0);
            try {
                int a = PripojeniDB.dotazIUD(dotaz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void kontrolaVykresu() {
        try {

            arVykresyZmeny.clear();
            String dotaz = "SELECT vykresy_id, vykresy_nazev "
                    + "FROM spolecne.vykresy "
                    + "WHERE vykresy_cislo = '" + CisloVykresuTextField1.getText().trim() + "'";
            if (ZmenaVykresuTextField1.getText().trim().length() > 0) {
                dotaz += " AND vykresy_revize = '" + ZmenaVykresuTextField1.getText().trim() + "'";
            }

            ResultSet vykresyZmeny1 = PripojeniDB.dotazS(dotaz);

            while (vykresyZmeny1.next()) {
                vykresZmena = new TridaVykres1();
                vykresZmena.setIdVykres(new Integer(vykresyZmeny1.getInt(1)));
                vykresZmena.setNazev((vykresyZmeny1.getString(2) == null) ? "" : vykresyZmeny1.getString(2));
                arVykresyZmeny.add(vykresZmena);
                NazevSoucastiTextField1.setText(vykresZmena.getNazev());
            }

            String kusyNavicText = "";
            ResultSet kusyNavic1 = PripojeniDB.dotazS(
                    "SELECT SUM(objednavky_kusu_navic) + SUM(objednavky_kusu_vyrobit) - SUM(objednavky_pocet_objednanych_kusu) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE objednavky.objednavky_cislo_vykresu = vykresy.vykresy_id "
                    + "AND vykresy.vykresy_id = " + vykresZmena.getIdVykres());
            while (kusyNavic1.next()) {
                kusyNavicText += kusyNavic1.getString(1);

            }
            ResultSet kusyNavic2 = PripojeniDB.dotazS(
                    "SELECT SUM(objednavky_kusu_navic) + SUM(objednavky_kusu_vyrobit) - SUM(objednavky_pocet_objednanych_kusu) "
                    + "FROM spolecne.objednavky "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE objednavky.objednavky_cislo_vykresu = vykresy.vykresy_id "
                    + "AND vykresy.vykresy_cislo = " + TextFunkce1.osetriZapisTextDB1(CisloVykresuTextField1.getText().trim()));//(Integer) vsVykresyZmeny.get(0));
            while (kusyNavic2.next()) {
                kusyNavicText += " (celkove " + kusyNavic2.getString(1) + " ks)";
            }
            KusuNavicLabel1.setText(kusyNavicText);
            if (RamcovaCheckBox1.isSelected() == true) {
                vrRamcova.removeAllElements();
                vsRamcova.removeAllElements();
                RamcoveObjednavkyComboBox1.removeAllItems();
                ResultSet ramcova = PripojeniDB.dotazS(
                        "SELECT ramec_objednavky_id, ramec_objednavky_cislo_objednavky "
                        + "FROM spolecne.ramec_objednavky "
                        + "WHERE ramec_objednavky.ramec_objednavky_cislo_vykresu = " + vykresZmena.getIdVykres());
                while (ramcova.next()) {
                    vsRamcova = new Vector();
                    vsRamcova.add(ramcova.getLong(1));
                    vsRamcova.add(ramcova.getString(2));
                    vrRamcova.add(vsRamcova);
                    RamcoveObjednavkyComboBox1.addItem(ramcova.getString(2));
                }
                if (RamcoveObjednavkyComboBox1.getSelectedIndex() == -1) {
                    try {
                        RamcoveObjednavkyComboBox1.setSelectedIndex(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String dotazKusy = "SELECT SUM(objednavky_pocet_objednanych_kusu ) "
                        + "FROM spolecne.objednavky "
                        + "CROSS JOIN spolecne.vazba_objednavky_ramec "
                        + "WHERE vazba_objednavky_ramec_ramec_id = " + (Long) ((Vector) vrRamcova.get(RamcoveObjednavkyComboBox1.getSelectedIndex())).get(0)
                        + " AND vazba_objednavky_ramec_objednavky_id = objednavky_id ";
                System.out.println(dotazKusy);
                ResultSet kusyRamcObj = PripojeniDB.dotazS(dotazKusy);
                while (kusyRamcObj.next()) {
                    PocetObjRamecLabel1.setText(kusyRamcObj.getInt(1) + "");
                }
            }

            ResultSet kapacity = PripojeniDB.dotazS(
                    "SELECT vyrobni_kapacita_id, vyrobni_kapacita_cas_mcv, "
                    + "vyrobni_kapacita_cas_tornado, vyrobni_kapacita_cas_dmu, "
                    + "vyrobni_kapacita_soustruh "
                    + "FROM spolecne.vyrobni_kapacita "
                    + "WHERE vyrobni_kapacita_cislo_vykresu = '" + CisloVykresuTextField1.getText().trim() + "' LIMIT 1");
            vsKapacity = new Vector();
            while (kapacity.next()) {
                vsKapacity.add(new Integer(kapacity.getInt(1)));
                vsKapacity.add(new Float(kapacity.getFloat(2)));
                vsKapacity.add(new Float(kapacity.getFloat(3)));
                vsKapacity.add(new Float(kapacity.getFloat(4)));
                try {
                    vsKapacity.add(new Integer(kapacity.getInt(5)));
                } catch (Exception e) {
                    vsKapacity.add(new Integer(0));
                }
            }
            if (vsKapacity.size() > 0) {
                McvTextField1.setText((Float) vsKapacity.get(1) + "");
                TornadoTextField1.setText((Float) vsKapacity.get(2) + "");
                DMUTextField1.setText((Float) vsKapacity.get(3) + "");
                if (((Integer) vsKapacity.get(4)).intValue() == 1) {
                    OstatniCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(1)).floatValue() > 0) {
                    McvCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(2)).floatValue() > 0) {
                    TornadoCheckBox1.setSelected(true);
                }
                if (((Float) vsKapacity.get(3)).floatValue() > 0) {
                    DMUCheckBox1.setSelected(true);
                }

            } else {
                UpravaKapacitTextField1.setText("Novy vykres !!!");
                UpravaKapacitTextField1.setForeground(Color.red);
            }

        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);

        } // konec catch

        NabidkyComboBox1.removeAllItems();
        try {
            vsNabidka.removeAllElements();
            vrNabidka.removeAllElements();
            ResultSet nabidka1 = PripojeniDB.dotazS(
                    "SELECT nabidky_id, "
                    + "nabidky_pocet_objednanych_kusu || ' ks za ' || nabidky_cena_za_kus || ' ' || meny_zkratka || ' z ' || to_char(nabidky_datum,'DD.MM.YYYY') AS datum "
                    + "FROM spolecne.nabidky "
                    + "CROSS JOIN spolecne.meny "
                    + "CROSS JOIN spolecne.vykresy "
                    + "WHERE nabidky_cislo_vykresu= vykresy_id "
                    + "AND vykresy_cislo = " + TextFunkce1.osetriZapisTextDB1(CisloVykresuTextField1.getText().trim()) + " "
                    + "AND meny.meny_id = nabidky.nabidky_mena_id "
                    + "AND nabidky_ukoncena = FALSE "
                    + "ORDER BY nabidky_datum ASC");

            while (nabidka1.next()) {
                vsNabidka = new Vector();
                vsNabidka.add(new Integer(nabidka1.getInt(1)));
                try {
                    vsNabidka.add(new String(nabidka1.getString(2)));
                } catch (Exception e) {
                    vsNabidka.add(new String(""));
                }
                vrNabidka.add(vsNabidka);
                NabidkyComboBox1.addItem((String) vsNabidka.get(1));

            }
        } // konec try
        catch (Exception e) {
            e.printStackTrace();
            PripojeniDB.vyjimkaS(e);
        } // konec catch
        if (PovrchovaUpravaComboBox1.getItemCount() > 0) {

            if (PovrchovaUpravaComboBox1.getSelectedIndex() == -1) {
                PovrchovaUpravaComboBox1.setSelectedIndex(0);
            }
        }

        if (arVykresyZmeny.size() < 1) {
            // System.out.println("nenalezeno");
            NazevSoucastiTextField1.setText("");
            NovyVykresFrame novyVykres = new NovyVykresFrame(CisloVykresuTextField1.getText().trim(), ZmenaVykresuTextField1.getText().trim(), false);
        }
        /*
         * if (VykresyZmenyComboBox1.getItemCount() > 0) {
         * VykresyZmenyComboBox1.setSelectedIndex(0);
         * NazevSoucastiTextField1.setText((String)((Vector)
         * vrVykresyZmeny.get(0)).get(2)); } else {
         * NazevSoucastiTextField1.setText((String)((Vector)
         * vrVykresyZmeny.get(0)).get(2)); }
         */
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

        jPanel1 = new javax.swing.JPanel();
        RamcovaCheckBox1 = new javax.swing.JCheckBox();
        KanbanCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        MaterialBBHLabel1 = new javax.swing.JLabel();
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
        jLabel15 = new javax.swing.JLabel();
        CisloObjednavkyTextField1 = new javax.swing.JTextField();
        DatumObjednaniTextField1 = new javax.swing.JTextField();
        MaterialBBHTextField1 = new javax.swing.JTextField();
        CisloVykresuTextField1 = new javax.swing.JTextField();
        ZmenaVykresuTextField1 = new javax.swing.JTextField();
        NazevSoucastiTextField1 = new javax.swing.JTextField();
        RozmerMaterialuTextField1 = new javax.swing.JTextField();
        PocetKusuTextField1 = new javax.swing.JTextField();
        PocetKusuRamcovaTextField1 = new javax.swing.JTextField();
        KusuVyrobitTextField1 = new javax.swing.JTextField();
        CenaZaKusTextField1 = new javax.swing.JTextField();
        KusuNavicTextField1 = new javax.swing.JTextField();
        TerminDodaniTextField1 = new javax.swing.JTextField();
        McvTextField1 = new javax.swing.JTextField();
        TornadoTextField1 = new javax.swing.JTextField();
        DMUTextField1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        ZakaznikComboBox1 = new javax.swing.JComboBox();
        RamcoveObjednavkyComboBox1 = new javax.swing.JComboBox();
        NabidkyComboBox1 = new javax.swing.JComboBox();
        PovrchovaUpravaComboBox1 = new javax.swing.JComboBox();
        MenaComboBox1 = new javax.swing.JComboBox();
        NovyVykresButton1 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        McvCheckBox1 = new javax.swing.JCheckBox();
        TornadoCheckBox1 = new javax.swing.JCheckBox();
        DMUCheckBox1 = new javax.swing.JCheckBox();
        OstatniCheckBox1 = new javax.swing.JCheckBox();
        UpravaKapacitTextField1 = new javax.swing.JTextField();
        CenaCelkemLabel1 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        ExpediceTextField1 = new javax.swing.JTextField();
        CisloFakturyTextField1 = new javax.swing.JTextField();
        PoznamkaTextField1 = new javax.swing.JTextField();
        CelkovyCasTextField1 = new javax.swing.JTextField();
        ReklamaceCheckBox1 = new javax.swing.JCheckBox();
        PocetObjRamecLabel1 = new javax.swing.JLabel();
        KusuNavicLabel1 = new javax.swing.JLabel();
        ErrorLabel = new javax.swing.JLabel();
        KonecNabidkaCheckBox1 = new javax.swing.JCheckBox();
        jLabel25 = new javax.swing.JLabel();
        TerminPotvrzeniTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jBZavrit = new javax.swing.JButton();
        jBUlozit = new javax.swing.JButton();
        jCBVirtualni = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 750));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(950, 677));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        RamcovaCheckBox1.setText("Rámcová objednávka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        jPanel1.add(RamcovaCheckBox1, gridBagConstraints);

        KanbanCheckBox1.setText("Kanban");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 5, 100);
        jPanel1.add(KanbanCheckBox1, gridBagConstraints);

        jLabel1.setText("Zákazník :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Datum objednání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Číslo objednávky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        MaterialBBHLabel1.setText("Materiál :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(MaterialBBHLabel1, gridBagConstraints);

        jLabel5.setText("Číslo výkresu :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Změna :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Název součásti :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jLabel8.setText("Ks navíc ve výrobě :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Číslo rámcové obj. :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Objednáno kusů :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Nabídky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 5);
        jPanel1.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Materiál na kus :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Povrchová úprava :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel13, gridBagConstraints);

        jLabel14.setText("hodin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
        jPanel1.add(jLabel14, gridBagConstraints);

        jLabel15.setText("hodin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel15, gridBagConstraints);

        CisloObjednavkyTextField1.setNextFocusableComponent(MaterialBBHTextField1);
        CisloObjednavkyTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloObjednavkyTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CisloObjednavkyTextField1, gridBagConstraints);

        DatumObjednaniTextField1.setNextFocusableComponent(CisloObjednavkyTextField1);
        DatumObjednaniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                DatumObjednaniTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(DatumObjednaniTextField1, gridBagConstraints);

        MaterialBBHTextField1.setNextFocusableComponent(CisloVykresuTextField1);
        MaterialBBHTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MaterialBBHTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(MaterialBBHTextField1, gridBagConstraints);

        CisloVykresuTextField1.setNextFocusableComponent(ZmenaVykresuTextField1);
        CisloVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloVykresuTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CisloVykresuTextField1, gridBagConstraints);

        ZmenaVykresuTextField1.setNextFocusableComponent(NazevSoucastiTextField1);
        ZmenaVykresuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ZmenaVykresuTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(ZmenaVykresuTextField1, gridBagConstraints);

        NazevSoucastiTextField1.setNextFocusableComponent(RamcoveObjednavkyComboBox1);
        NazevSoucastiTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                NazevSoucastiTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(NazevSoucastiTextField1, gridBagConstraints);

        RozmerMaterialuTextField1.setNextFocusableComponent(PovrchovaUpravaComboBox1);
        RozmerMaterialuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                RozmerMaterialuTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(RozmerMaterialuTextField1, gridBagConstraints);

        PocetKusuTextField1.setNextFocusableComponent(KusuVyrobitTextField1);
        PocetKusuTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PocetKusuTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PocetKusuTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(PocetKusuTextField1, gridBagConstraints);

        PocetKusuRamcovaTextField1.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(PocetKusuRamcovaTextField1, gridBagConstraints);

        KusuVyrobitTextField1.setNextFocusableComponent(KusuNavicTextField1);
        KusuVyrobitTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                KusuVyrobitTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(KusuVyrobitTextField1, gridBagConstraints);

        CenaZaKusTextField1.setBackground(new java.awt.Color(255, 255, 204));
        CenaZaKusTextField1.setNextFocusableComponent(TerminDodaniTextField1);
        CenaZaKusTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CenaZaKusTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                CenaZaKusTextField1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CenaZaKusTextField1, gridBagConstraints);

        KusuNavicTextField1.setBackground(new java.awt.Color(255, 204, 204));
        KusuNavicTextField1.setNextFocusableComponent(CenaZaKusTextField1);
        KusuNavicTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                KusuNavicTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(KusuNavicTextField1, gridBagConstraints);

        TerminDodaniTextField1.setNextFocusableComponent(TerminPotvrzeniTextField1);
        TerminDodaniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TerminDodaniTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(TerminDodaniTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(McvTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(TornadoTextField1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(DMUTextField1, gridBagConstraints);

        jLabel16.setText("Odvolávka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel16, gridBagConstraints);

        jLabel17.setText("Počet objednaných ks :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel17, gridBagConstraints);

        jLabel18.setText("Počet kusů vyrobit:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel18, gridBagConstraints);

        jLabel19.setText("Vyrobit navíc:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel19, gridBagConstraints);

        jLabel20.setText("Cena za kus :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel20, gridBagConstraints);

        jLabel21.setText("Cena celkem :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 5);
        jPanel1.add(jLabel21, gridBagConstraints);

        jLabel22.setText("Potvrzení objednávky :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel22, gridBagConstraints);

        jLabel23.setText("z");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        jPanel1.add(jLabel23, gridBagConstraints);

        ZakaznikComboBox1.setNextFocusableComponent(DatumObjednaniTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(ZakaznikComboBox1, gridBagConstraints);

        RamcoveObjednavkyComboBox1.setNextFocusableComponent(NabidkyComboBox1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(RamcoveObjednavkyComboBox1, gridBagConstraints);

        NabidkyComboBox1.setNextFocusableComponent(RozmerMaterialuTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(NabidkyComboBox1, gridBagConstraints);

        PovrchovaUpravaComboBox1.setNextFocusableComponent(PocetKusuTextField1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(PovrchovaUpravaComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 12);
        jPanel1.add(MenaComboBox1, gridBagConstraints);

        NovyVykresButton1.setText("Nový");
        NovyVykresButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NovyVykresButton1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 20);
        jPanel1.add(NovyVykresButton1, gridBagConstraints);

        jLabel24.setText("hodin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel24, gridBagConstraints);

        McvCheckBox1.setText("MCV");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(McvCheckBox1, gridBagConstraints);

        TornadoCheckBox1.setText("Tornado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(TornadoCheckBox1, gridBagConstraints);

        DMUCheckBox1.setText("DMU");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(DMUCheckBox1, gridBagConstraints);

        OstatniCheckBox1.setText("Ostatní");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(OstatniCheckBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(UpravaKapacitTextField1, gridBagConstraints);

        CenaCelkemLabel1.setText("0,00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 71, 0, 71);
        jPanel1.add(CenaCelkemLabel1, gridBagConstraints);

        jLabel26.setText("Celkový čas :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 25, 0);
        jPanel1.add(jLabel26, gridBagConstraints);

        jLabel27.setText("Expedice :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 5, 10, 5);
        jPanel1.add(jLabel27, gridBagConstraints);

        jLabel28.setText("Číslo faktury :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        jPanel1.add(jLabel28, gridBagConstraints);

        jLabel29.setText("Poznámka :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 10, 0);
        jPanel1.add(jLabel29, gridBagConstraints);

        ExpediceTextField1.setNextFocusableComponent(CisloFakturyTextField1);
        ExpediceTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ExpediceTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 10, 0);
        jPanel1.add(ExpediceTextField1, gridBagConstraints);

        CisloFakturyTextField1.setNextFocusableComponent(PoznamkaTextField1);
        CisloFakturyTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CisloFakturyTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CisloFakturyTextField1, gridBagConstraints);

        PoznamkaTextField1.setNextFocusableComponent(CelkovyCasTextField1);
        PoznamkaTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PoznamkaTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 10, 0);
        jPanel1.add(PoznamkaTextField1, gridBagConstraints);

        CelkovyCasTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CelkovyCasTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(CelkovyCasTextField1, gridBagConstraints);

        ReklamaceCheckBox1.setText("Reklamace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(ReklamaceCheckBox1, gridBagConstraints);

        PocetObjRamecLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(PocetObjRamecLabel1, gridBagConstraints);

        KusuNavicLabel1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(KusuNavicLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        jPanel1.add(ErrorLabel, gridBagConstraints);

        KonecNabidkaCheckBox1.setText("Ukončit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel1.add(KonecNabidkaCheckBox1, gridBagConstraints);

        jLabel25.setText("Termín dodání :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel25, gridBagConstraints);

        TerminPotvrzeniTextField1.setNextFocusableComponent(ExpediceTextField1);
        TerminPotvrzeniTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TerminPotvrzeniTextField1FocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(TerminPotvrzeniTextField1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setPreferredSize(new java.awt.Dimension(1050, 71));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jBZavrit.setText("Zavřít");
        jBZavrit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBZavritMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 67;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jBZavrit, gridBagConstraints);

        jBUlozit.setText("Uložit objednávku");
        jBUlozit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBUlozitMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jBUlozit, gridBagConstraints);

        jCBVirtualni.setText("Virtuální položka");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jCBVirtualni, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBUlozitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBUlozitMouseClicked
        ulozObjednavku();
    }//GEN-LAST:event_jBUlozitMouseClicked

    private void NovyVykresButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NovyVykresButton1MouseClicked
        NovyVykresFrame novyVykres = new NovyVykresFrame(false);
    }//GEN-LAST:event_NovyVykresButton1MouseClicked

    private void jBZavritMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBZavritMouseClicked
        this.dispose();
    }//GEN-LAST:event_jBZavritMouseClicked

    private void DatumObjednaniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DatumObjednaniTextField1FocusGained
        DatumObjednaniTextField1.selectAll();
    }//GEN-LAST:event_DatumObjednaniTextField1FocusGained

    private void CisloObjednavkyTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloObjednavkyTextField1FocusGained
        CisloObjednavkyTextField1.selectAll();
    }//GEN-LAST:event_CisloObjednavkyTextField1FocusGained

    private void MaterialBBHTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MaterialBBHTextField1FocusGained
        MaterialBBHTextField1.selectAll();
    }//GEN-LAST:event_MaterialBBHTextField1FocusGained

    private void CisloVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloVykresuTextField1FocusGained
        CisloVykresuTextField1.selectAll();
    }//GEN-LAST:event_CisloVykresuTextField1FocusGained

    private void ZmenaVykresuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusGained
        ZmenaVykresuTextField1.selectAll();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusGained

    private void NazevSoucastiTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NazevSoucastiTextField1FocusGained
        NazevSoucastiTextField1.selectAll();
    }//GEN-LAST:event_NazevSoucastiTextField1FocusGained

    private void RozmerMaterialuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_RozmerMaterialuTextField1FocusGained
        RozmerMaterialuTextField1.selectAll();
    }//GEN-LAST:event_RozmerMaterialuTextField1FocusGained

    private void PocetKusuTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetKusuTextField1FocusGained
        PocetKusuTextField1.selectAll();
    }//GEN-LAST:event_PocetKusuTextField1FocusGained

    private void KusuVyrobitTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_KusuVyrobitTextField1FocusGained
        KusuVyrobitTextField1.selectAll();
    }//GEN-LAST:event_KusuVyrobitTextField1FocusGained

    private void KusuNavicTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_KusuNavicTextField1FocusGained
        KusuNavicTextField1.selectAll();
    }//GEN-LAST:event_KusuNavicTextField1FocusGained

    private void CenaZaKusTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CenaZaKusTextField1FocusGained
        CenaZaKusTextField1.selectAll();
    }//GEN-LAST:event_CenaZaKusTextField1FocusGained

    private void TerminDodaniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TerminDodaniTextField1FocusGained
        TerminDodaniTextField1.selectAll();
    }//GEN-LAST:event_TerminDodaniTextField1FocusGained

    private void ExpediceTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ExpediceTextField1FocusGained
        ExpediceTextField1.selectAll();
    }//GEN-LAST:event_ExpediceTextField1FocusGained

    private void CisloFakturyTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CisloFakturyTextField1FocusGained
        CisloFakturyTextField1.selectAll();
    }//GEN-LAST:event_CisloFakturyTextField1FocusGained

    private void PoznamkaTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PoznamkaTextField1FocusGained
        PoznamkaTextField1.selectAll();
    }//GEN-LAST:event_PoznamkaTextField1FocusGained

    private void CelkovyCasTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CelkovyCasTextField1FocusGained
        CelkovyCasTextField1.selectAll();
    }//GEN-LAST:event_CelkovyCasTextField1FocusGained

    private void ZmenaVykresuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ZmenaVykresuTextField1FocusLost
        kontrolaVykresu();
    }//GEN-LAST:event_ZmenaVykresuTextField1FocusLost

    private void PocetKusuTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PocetKusuTextField1FocusLost
        cenaCelkem();
    }//GEN-LAST:event_PocetKusuTextField1FocusLost

    private void CenaZaKusTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CenaZaKusTextField1FocusLost
        cenaCelkem();
    }//GEN-LAST:event_CenaZaKusTextField1FocusLost

    private void TerminPotvrzeniTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TerminPotvrzeniTextField1FocusGained
        TerminPotvrzeniTextField1.selectAll();
    }//GEN-LAST:event_TerminPotvrzeniTextField1FocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CelkovyCasTextField1;
    private javax.swing.JLabel CenaCelkemLabel1;
    private javax.swing.JTextField CenaZaKusTextField1;
    private javax.swing.JTextField CisloFakturyTextField1;
    private javax.swing.JTextField CisloObjednavkyTextField1;
    private javax.swing.JTextField CisloVykresuTextField1;
    private javax.swing.JCheckBox DMUCheckBox1;
    private javax.swing.JTextField DMUTextField1;
    private javax.swing.JTextField DatumObjednaniTextField1;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JTextField ExpediceTextField1;
    private javax.swing.JCheckBox KanbanCheckBox1;
    private javax.swing.JCheckBox KonecNabidkaCheckBox1;
    private javax.swing.JLabel KusuNavicLabel1;
    private javax.swing.JTextField KusuNavicTextField1;
    private javax.swing.JTextField KusuVyrobitTextField1;
    private javax.swing.JLabel MaterialBBHLabel1;
    private javax.swing.JTextField MaterialBBHTextField1;
    private javax.swing.JCheckBox McvCheckBox1;
    private javax.swing.JTextField McvTextField1;
    private javax.swing.JComboBox MenaComboBox1;
    private javax.swing.JComboBox NabidkyComboBox1;
    private javax.swing.JTextField NazevSoucastiTextField1;
    private javax.swing.JButton NovyVykresButton1;
    private javax.swing.JCheckBox OstatniCheckBox1;
    private javax.swing.JTextField PocetKusuRamcovaTextField1;
    private javax.swing.JTextField PocetKusuTextField1;
    private javax.swing.JLabel PocetObjRamecLabel1;
    private javax.swing.JComboBox PovrchovaUpravaComboBox1;
    private javax.swing.JTextField PoznamkaTextField1;
    private javax.swing.JCheckBox RamcovaCheckBox1;
    private javax.swing.JComboBox RamcoveObjednavkyComboBox1;
    private javax.swing.JCheckBox ReklamaceCheckBox1;
    private javax.swing.JTextField RozmerMaterialuTextField1;
    private javax.swing.JTextField TerminDodaniTextField1;
    private javax.swing.JTextField TerminPotvrzeniTextField1;
    private javax.swing.JCheckBox TornadoCheckBox1;
    private javax.swing.JTextField TornadoTextField1;
    private javax.swing.JTextField UpravaKapacitTextField1;
    private javax.swing.JComboBox ZakaznikComboBox1;
    private javax.swing.JTextField ZmenaVykresuTextField1;
    private javax.swing.JButton jBUlozit;
    private javax.swing.JButton jBZavrit;
    private javax.swing.JCheckBox jCBVirtualni;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
