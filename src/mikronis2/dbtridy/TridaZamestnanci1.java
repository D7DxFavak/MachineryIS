/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import mikronis2.JednoducheDialogy1;
import mikronis2.MikronIS2;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;

/**
 *
 * @author Favak
 */
public class TridaZamestnanci1 {

    private int idZamestnanec;
    private String jmeno;
    private String prijmeni;
    private String poznamky;
    private boolean aktivni;
    private boolean vedeni;

    public TridaZamestnanci1() {
        this.aktivni = false;
        this.vedeni = false;
    }
    
    public boolean selectData() {
        return this.selectData(this.idZamestnanec);
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT zamestnanci_id, zamestnanci_jmeno, "
                    + "zamestnanci_prijmeni, zamestnanci_poznamky, "
                    + "zamestnanci_vedeni, zamestnanci_aktivni "
                    + "FROM spolecne.zamestnanci "
                    + "WHERE zamestnanci_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.idZamestnanec = SQLFunkceObecne.osetriCteniInt(q.getInt(1));
                this.jmeno = SQLFunkceObecne.osetriCteniString(q.getString(2));
                this.prijmeni = SQLFunkceObecne.osetriCteniString(q.getString(3));
                this.poznamky = SQLFunkceObecne.osetriCteniString(q.getString(4));
                this.vedeni = q.getBoolean(5);
                this.aktivni = q.getBoolean(6);
            }
            return true;
        } catch (Exception e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }
    }

    public int insertData() {
        try {
            int rc = 0;
            rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(zamestnanci_id) FROM spolecne.zamestnanci");
            while (id.next()) {
                this.idZamestnanec = id.getInt(1) + 1;
            }
            //int pocet_mj = Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.zamestnanci("
                    + "zamestnanci_id, zamestnanci_jmeno, zamestnanci_prijmeni, "
                    + "zamestnanci_poznamky, zamestnanci_vedeni, zamestnanci_aktivni) "
                    + "VALUES( " + this.idZamestnanec
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.jmeno)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.prijmeni)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.poznamky)
                    + ", " + this.vedeni
                    + ", " + this.aktivni + ")");
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            //e.printStackTrace();
            JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při ukládání dat oboru", e.toString());
        }
        return this.idZamestnanec;
    }

    public int updateData() {
        String dotaz = "UPDATE spolecne.zamestnanci "
                + "SET zamestnanci_id= " + this.idZamestnanec + ", "
                + "zamestnanci_jmeno= " + TextFunkce1.osetriZapisTextDB1(this.jmeno) + ", "
                + "zamestnanci_prijmeni= " + TextFunkce1.osetriZapisTextDB1(this.prijmeni) + ", "
                + "zamestnanci_poznamky= " + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                + "zamestnanci_vedeni= " + this.vedeni + ", "
                + "zamestnanci_aktivni= " + this.aktivni + " "
                + "WHERE zamestnanci_id = " + this.idZamestnanec;
        //System.out.println("Update data : " + dotaz);
        int r = SQLFunkceObecne2.update(dotaz);
        /*
         * int vu = -10000; try { int r = SQLFunkceObecne2.update(dotaz);
         * System.out.println("r : " + r); if (r == 1) { vu = 0; } // konec if
         * radky } // konec try catch (Exception e) { System.out.println("Update
         * osoby - doslo k vyjimce"); e.printStackTrace(); // vu =
         * -Pripojeni.vyjimkaUD(e); //System.out.println("vu je ve vyjimce " +
         * vu); //Pripojeni.zavriPrikaz(); if (vu == 0) { vu = -10000; } } //
         * konec catch finally { Pripojeni.zavriPrikaz();
         * System.out.println("return vu : " + vu); return vu; } // konec
         * finally
         */
        return r;
    }

    public void deleteData() {
        String dotaz = "DELETE FROM spolecne.zamestnanci WHERE zamestnanci_id = " + this.idZamestnanec;
        try {
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the idZamestnanec
     */
    public int getIdZamestnanec() {
        return idZamestnanec;
    }

    /**
     * @param idZamestnanec the idZamestnanec to set
     */
    public void setIdZamestnanec(int idZamestnanec) {
        this.idZamestnanec = idZamestnanec;
    }

    /**
     * @return the jmeno
     */
    public String getJmeno() {
        return jmeno;
    }

    /**
     * @param jmeno the jmeno to set
     */
    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    /**
     * @return the prijmeni
     */
    public String getPrijmeni() {
        return prijmeni;
    }

    /**
     * @param prijmeni the prijmeni to set
     */
    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    /**
     * @return the poznamky
     */
    public String getPoznamky() {
        return poznamky;
    }

    /**
     * @param poznamky the poznamky to set
     */
    public void setPoznamky(String poznamky) {
        this.poznamky = poznamky;
    }

    /**
     * @return the aktivni
     */
    public boolean isAktivni() {
        return aktivni;
    }

    /**
     * @param aktivni the aktivni to set
     */
    public void setAktivni(boolean aktivni) {
        this.aktivni = aktivni;
    }

    /**
     * @return the vedeni
     */
    public boolean isVedeni() {
        return vedeni;
    }

    /**
     * @param vedeni the vedeni to set
     */
    public void setVedeni(boolean vedeni) {
        this.vedeni = vedeni;
    }
}
