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
public class TridaDodAdresa1 {

    private int idZakaznik;
    private int idStat;
    private int idAdresa;
    private int poradiVyber;
    private String nazev;
    private String popis;
    private String celyNazev;
    private String adresa;
    private String mesto;
    private String psc;

    public TridaDodAdresa1() {
        idZakaznik = 0;
        idStat = 0;
        idAdresa = 0;
        poradiVyber = 0;
        nazev = "";
        popis = "";
        celyNazev = "";
        adresa = "";
        mesto = "";
        psc = "";
    }
    
    public TridaDodAdresa1(int id) {
        selectData(id);
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

    /**
     * @return the poradiVyber
     */
    public int getPoradiVyber() {
        return poradiVyber;
    }

    /**
     * @param poradiVyber the poradiVyber to set
     */
    public void setPoradiVyber(int poradiVyber) {
        this.poradiVyber = poradiVyber;
    }

    /**
     * @return the nazev
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * @param nazev the nazev to set
     */
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    /**
     * @return the popis
     */
    public String getPopis() {
        return popis;
    }

    /**
     * @param popis the popis to set
     */
    public void setPopis(String popis) {
        this.popis = popis;
    }

    /**
     * @return the ustNr
     */
    public String getCelyNazev() {
        return celyNazev;
    }

    /**
     * @param ustNr the ustNr to set
     */
    public void setCelyNazev(String celyNazev) {
        this.celyNazev = celyNazev;
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

    public boolean selectData() {
        return this.selectData(this.idAdresa);
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT dodaci_adresy_id, dodaci_adresy_subjekt_trhu, "
                    + "dodaci_adresy_cely_nazev, dodaci_adresy_nazev, dodaci_adresy_popis, "
                    + "dodaci_adresy_adresa, dodaci_adresy_mesto, dodaci_adresy_psc, "
                    + "dodaci_adresy_stat_id, dodaci_adresy_poradi "
                    + "FROM spolecne.dodaci_adresy "
                    + "WHERE dodaci_adresy_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.idAdresa = SQLFunkceObecne.osetriCteniInt(q.getInt(1));
                this.idZakaznik = SQLFunkceObecne.osetriCteniInt(q.getInt(2));
                this.celyNazev = SQLFunkceObecne.osetriCteniString(q.getString(3));
                this.nazev = SQLFunkceObecne.osetriCteniString(q.getString(4));
                this.popis = SQLFunkceObecne.osetriCteniString(q.getString(5));
                this.adresa = SQLFunkceObecne.osetriCteniString(q.getString(6));
                this.mesto = SQLFunkceObecne.osetriCteniString(q.getString(7));
                this.psc = SQLFunkceObecne.osetriCteniString(q.getString(8));
                this.idStat = SQLFunkceObecne.osetriCteniInt(q.getInt(9));
                this.poradiVyber = SQLFunkceObecne.osetriCteniInt(q.getInt(10));
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
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(dodaci_adresy_id) FROM spolecne.dodaci_adresy");
            while (id.next()) {
                this.idAdresa = id.getInt(1) + 1;
            }
            //int pocet_mj = Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.dodaci_adresy("
                    + "dodaci_adresy_id, dodaci_adresy_subjekt_trhu, dodaci_adresy_cely_nazev, "
                    + "dodaci_adresy_nazev, dodaci_adresy_popis, dodaci_adresy_adresa, "
                    + "dodaci_adresy_mesto, dodaci_adresy_psc, dodaci_adresy_stat_id, "
                    + "dodaci_adresy_poradi) "
                    + "VALUES( " + this.idAdresa 
                    + ", "+ this.idZakaznik
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.celyNazev)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.nazev)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.popis)                 
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.adresa)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mesto)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.psc)
                    + ", " + idStat                   
                    + ", 1100 " + ")");
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            //e.printStackTrace();
            JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při ukládání dat dodací adresy", e.toString());
        }
        return this.idZakaznik;
    }

    public int updateData() {
        String dotaz = "UPDATE spolecne.dodaci_adresy "
                + "SET dodaci_adresy_subjekt_trhu= " + this.idZakaznik + ", "
                + "dodaci_adresy_cely_nazev= " + TextFunkce1.osetriZapisTextDB1(this.celyNazev) + ", "
                + "dodaci_adresy_nazev= " + TextFunkce1.osetriZapisTextDB1(this.nazev) + ", "
                + "dodaci_adresy_popis= " + TextFunkce1.osetriZapisTextDB1(this.popis) + ", "
                + "dodaci_adresy_adresa= " + TextFunkce1.osetriZapisTextDB1(this.adresa) + ", "
                + "dodaci_adresy_mesto= " + TextFunkce1.osetriZapisTextDB1(this.mesto) + ", "
                + "dodaci_adresy_psc= " + TextFunkce1.osetriZapisTextDB1(this.psc) + ", "
                + "dodaci_adresy_stat_id= " + idStat + ", "
                + "dodaci_adresy_poradi= " + 1100 + " "
                 + "WHERE dodaci_adresy_id = " + this.idAdresa;                     
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
         * System.out.println("return vu : " + vu); return vu; } // konec finally
         */
        return r;
    }

    /**
     * @return the idAdresa
     */
    public int getIdAdresa() {
        return idAdresa;
    }

    /**
     * @param idAdresa the idAdresa to set
     */
    public void setIdAdresa(int idAdresa) {
        this.idAdresa = idAdresa;
    }
}
