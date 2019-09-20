/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import mikronis2.JednoducheDialogy1;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;

/**
 *
 * @author Favak
 */
public class TridaPotvrzeni1 {

    private int idPotvrzeni;
    private int idZakaznik;
    private Date datumPotvrzeni;
    private String prijemce;
    private String predmet;
    private String uvodVeta;
    private String vystavitel;
    private String dodatek;
    private String adresa;
    private String mesto;
    private String psc;
    private int idStat;
    private int pocetObjednavek;
    private String dataPDF;
    private ArrayList<TridaPotvrzeniObjednavka1> arTO1;

    public TridaPotvrzeni1() {
        idPotvrzeni = 0;
        idZakaznik = 0;
        datumPotvrzeni = null;
        prijemce = "";
        predmet = "";
        uvodVeta = "";
        vystavitel = "";
        dodatek = "";
        adresa = "";
        mesto = "";
        psc = "";
        idStat = 0;
        arTO1 = new ArrayList<TridaPotvrzeniObjednavka1>();

    }

    public boolean selectData() {
        return selectData(this.idPotvrzeni);
    }

    public boolean selectData(long id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT potvrzeni_id, "
                    + "potvrzeni_zakaznik_id, "
                    + "potvrzeni_datum, "
                    + "potvrzeni_komu, "
                    + "potvrzeni_predmet, "
                    + "potvrzeni_veta, "
                    + "potvrzeni_dodatek, "
                    + "potvrzeni_vystavil, "
                    + "potvrzeni_adresa, "
                    + "potvrzeni_mesto, "
                    + "potvrzeni_psc, "
                    + "potvrzeni_stat "
                    + "FROM spolecne.potvrzeni "
                    + "WHERE potvrzeni_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdPotvrzeni(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                this.setDatumPotvrzeni(q.getDate(3));
                this.setPrijemce(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                this.setPredmet(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                this.setUvodVeta(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                this.setDodatek(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                this.setVystavitel(SQLFunkceObecne.osetriCteniString(q.getString(8)));
                this.setAdresa(SQLFunkceObecne.osetriCteniString(q.getString(9)));
                this.setMesto(SQLFunkceObecne.osetriCteniString(q.getString(10)));
                this.setPsc(SQLFunkceObecne.osetriCteniString(q.getString(11)));
                this.setIdStat(q.getInt(12));
            }
            return true;
        } catch (Exception e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }

    }

    public long insertData() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(potvrzeni_id) FROM spolecne.potvrzeni");
            while (id.next()) {
                idPotvrzeni = id.getInt(1) + 1;
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            idPotvrzeni = -1;
        }
        // TODO kontrola datumu
        try {
            String dotazFakt = "INSERT INTO spolecne.potvrzeni("
                    + "potvrzeni_id, "
                    + "potvrzeni_zakaznik_id, "
                    + "potvrzeni_datum, "
                    + "potvrzeni_komu, "
                    + "potvrzeni_predmet, "
                    + "potvrzeni_veta, potvrzeni_vystavil, potvrzeni_dodatek, "
                    + "potvrzeni_adresa, potvrzeni_mesto, potvrzeni_psc, potvrzeni_stat) "
                    + "VALUES( " + idPotvrzeni + ", " + idZakaznik
                    + ", " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(datumPotvrzeni))
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.prijemce)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.predmet)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.uvodVeta)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.vystavitel)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.dodatek)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.adresa)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mesto)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.psc)
                    + ", " + this.idStat
                    + ")";

            int a = PripojeniDB.dotazIUD(dotazFakt);
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání potvrzení objednávek", "Prosím zkontrolujte zadaná data "
                    + "a opravte případné chyby");
        }

        for (int objIndex = 0; objIndex < arTO1.size(); objIndex++) {
            try {
                TridaPotvrzeniObjednavka1 potObj1 = (TridaPotvrzeniObjednavka1) arTO1.get(objIndex);

                int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_potvrzeni_objednavky("
                        + "vazba_potvrzeni_objednavky_potvrzeni_id, "
                        + "vazba_potvrzeni_objednavky_objednavky_id, "
                        + "vazba_potvrzeni_datum, "
                        + "vazba_potvrzeni_objednavky_poradi ) "
                        + "VALUES( " + idPotvrzeni + ", " + potObj1.getIdObjednavky() + ", "
                        + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + ", "
                        + potObj1.getPoradi() + ")");

                a = PripojeniDB.dotazIUD("UPDATE spolecne.objednavky "
                        + "SET objednavky_poznamka= COALESCE(objednavky_poznamka, '') || ' ' || "
                        + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + ", "
                        + "objednavky_datum_potvrzeni = " + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + " "
                        + "WHERE objednavky_id= " + potObj1.getIdObjednavky());

            } catch (Exception e) {
                rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
                JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání potvrzení objednávek", "Prosím zkontrolujte zadaná data "
                        + "a opravte případné chyby");
            }
        }
        rc = SQLFunkceObecne2.spustPrikaz("COMMIT");

        return idPotvrzeni;
    }

    public int updateData() {
        int r = -10000;
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();
        try {
            String dotazFakt = "UPDATE spolecne.potvrzeni "
                    + "SET potvrzeni_zakaznik_id = " + this.idZakaznik + ", "
                    + "potvrzeni_datum = " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(datumPotvrzeni)) + ", "
                    + "potvrzeni_komu = " + TextFunkce1.osetriZapisTextDB1(this.prijemce) + ", "
                    + "potvrzeni_predmet = " + TextFunkce1.osetriZapisTextDB1(this.predmet) + ", "
                    + "potvrzeni_veta = " + TextFunkce1.osetriZapisTextDB1(this.uvodVeta) + ", "
                    + "potvrzeni_vystavil = " + TextFunkce1.osetriZapisTextDB1(this.vystavitel) + ", "
                    + "potvrzeni_dodatek = " + TextFunkce1.osetriZapisTextDB1(this.dodatek) + ", "
                    + "potvrzeni_adresa = " + TextFunkce1.osetriZapisTextDB1(this.adresa) + ", "
                    + "potvrzeni_mesto = " + TextFunkce1.osetriZapisTextDB1(this.mesto) + ", "
                    + "potvrzeni_psc = " + TextFunkce1.osetriZapisTextDB1(this.psc) + ", "
                    + "potvrzeni_stat = " + this.idStat + " "
                    + "WHERE potvrzeni_id = " + this.idPotvrzeni;

            r = PripojeniDB.dotazIUD(dotazFakt);

            r = PripojeniDB.dotazIUD(
                    "DELETE FROM spolecne.vazba_potvrzeni_objednavky "
                    + "WHERE vazba_potvrzeni_objednavky_potvrzeni_id = " + this.idPotvrzeni);
            for (int objIndex = 0; objIndex < arTO1.size(); objIndex++) {
                try {
                    TridaPotvrzeniObjednavka1 potObj1 = (TridaPotvrzeniObjednavka1) arTO1.get(objIndex);

                    r = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_potvrzeni_objednavky("
                            + "vazba_potvrzeni_objednavky_potvrzeni_id, "
                            + "vazba_potvrzeni_objednavky_objednavky_id, "
                            + "vazba_potvrzeni_datum, "
                            + "vazba_potvrzeni_objednavky_poradi ) "
                            + "VALUES( " + idPotvrzeni + ", " + potObj1.getIdObjednavky() + ", "
                            + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + ", "
                            + potObj1.getPoradi() + ")");

                    r = PripojeniDB.dotazIUD("UPDATE spolecne.objednavky "
                            + "SET objednavky_poznamka= COALESCE(objednavky_poznamka, '') || ' ' || "
                            + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + ", "
                            + "objednavky_datum_potvrzeni = " + TextFunkce1.osetriZapisTextDB1(potObj1.getDatumPotvrzeni()) + " "
                            + "WHERE objednavky_id= " + potObj1.getIdObjednavky());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        } finally {
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            return r;
        }
    }

    /**
     * @return the idPotvrzeni
     */
    public int getIdPotvrzeni() {
        return idPotvrzeni;
    }

    /**
     * @param idPotvrzeni the idPotvrzeni to set
     */
    public void setIdPotvrzeni(int idPotvrzeni) {
        this.idPotvrzeni = idPotvrzeni;
    }

    /**
     * @return the idZakaznik
     */
    public int getIdZakaznik() {
        return idZakaznik;
    }

    /**
     * @param idZakaznik the idZakaznik to set
     */
    public void setIdZakaznik(int idZakaznik) {
        this.idZakaznik = idZakaznik;
    }

    /**
     * @return the datumPotvrzeni
     */
    public Date getDatumPotvrzeni() {
        return datumPotvrzeni;
    }

    /**
     * @param datumPotvrzeni the datumPotvrzeni to set
     */
    public void setDatumPotvrzeni(Date datumPotvrzeni) {
        this.datumPotvrzeni = datumPotvrzeni;
    }

    /**
     * @return the prijemce
     */
    public String getPrijemce() {
        return prijemce;
    }

    /**
     * @param prijemce the prijemce to set
     */
    public void setPrijemce(String prijemce) {
        this.prijemce = prijemce;
    }

    /**
     * @return the predmet
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     * @param predmet the predmet to set
     */
    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    /**
     * @return the uvodVeta
     */
    public String getUvodVeta() {
        return uvodVeta;
    }

    /**
     * @param uvodVeta the uvodVeta to set
     */
    public void setUvodVeta(String uvodVeta) {
        this.uvodVeta = uvodVeta;
    }

    /**
     * @return the vystavitel
     */
    public String getVystavitel() {
        return vystavitel;
    }

    /**
     * @param vystavitel the vystavitel to set
     */
    public void setVystavitel(String vystavitel) {
        this.vystavitel = vystavitel;
    }

    /**
     * @return the dodatek
     */
    public String getDodatek() {
        return dodatek;
    }

    /**
     * @param dodatek the dodatek to set
     */
    public void setDodatek(String dodatek) {
        this.dodatek = dodatek;
    }

    /**
     * @return the arTO1
     */
    public ArrayList<TridaPotvrzeniObjednavka1> getArTO1() {
        return arTO1;
    }

    /**
     * @param arTO1 the arTO1 to set
     */
    public void setArTO1(ArrayList<TridaPotvrzeniObjednavka1> arTO1) {
        this.arTO1 = arTO1;
    }

    /**
     * @return the pocetObjednavek
     */
    public int getPocetObjednavek() {
        return pocetObjednavek;
    }

    /**
     * @param pocetObjednavek the pocetObjednavek to set
     */
    public void setPocetObjednavek(int pocetObjednavek) {
        this.pocetObjednavek = pocetObjednavek;
    }

    /**
     * @return the dataPDF
     */
    public String getDataPDF() {
        return dataPDF;
    }

    /**
     * @param dataPDF the dataPDF to set
     */
    public void setDataPDF(String dataPDF) {
        this.dataPDF = dataPDF;
    }

    /**
     * @return the adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * @param adresa the adresa to set
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * @return the mesto
     */
    public String getMesto() {
        return mesto;
    }

    /**
     * @param mesto the mesto to set
     */
    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    /**
     * @return the psc
     */
    public String getPsc() {
        return psc;
    }

    /**
     * @param psc the psc to set
     */
    public void setPsc(String psc) {
        this.psc = psc;
    }

    /**
     * @return the idStat
     */
    public int getIdStat() {
        return idStat;
    }

    /**
     * @param idStat the idStat to set
     */
    public void setIdStat(int idStat) {
        this.idStat = idStat;
    }
}
