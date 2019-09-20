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
public class TridaStat1 {

    private int idStat;
    private int poradiVyber;
    private String nazev;
    private String popis;
    private boolean tuzemsko;
    private boolean eu;
    private String poznamky;

    public TridaStat1() {
        idStat = 0;
        poradiVyber = 0;
        nazev = "";
        popis = "";
        poznamky = "";
        tuzemsko = false;
        eu = false;
    }

    public TridaStat1(int id) {
        selectData(id);
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

    public boolean selectData() {
        return this.selectData(this.getIdStat());
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT staty_id, staty_nazev, staty_popis, "
                    + "staty_tuzemsko, staty_eu, staty_poznamky, staty_poradi_vyber "
                    + "FROM spolecne.staty "
                    + "WHERE staty_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.idStat = SQLFunkceObecne.osetriCteniInt(q.getInt(1));
                this.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                this.setTuzemsko(q.getBoolean(4));
                this.setEu(q.getBoolean(5));
                this.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                this.setPoradiVyber(SQLFunkceObecne.osetriCteniInt(q.getInt(7)));
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
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(staty_id) FROM spolecne.staty");
            while (id.next()) {
                this.idStat = id.getInt(1) + 1;
            }
            //int pocet_mj = Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.staty("
                    + "staty_id, staty_nazev, staty_popis, staty_tuzemsko, staty_eu, "
                    + "staty_bindata, staty_poznamky, staty_poradi_vyber) "
                    + "VALUES( " + this.idStat
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.getNazev())
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.getPopis())
                    + ", " + this.isTuzemsko()
                    + ", " + this.isEu()
                    + ", " + null
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.getPoznamky())
                    + ", " + this.getPoradiVyber()  + ")");
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            int rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            //e.printStackTrace();
            JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při ukládání dat státu", e.toString());
        }
        return this.idStat;
    }

    public int updateData() {
        String dotaz = "UPDATE spolecne.staty "
                + "SET staty_nazev= " + TextFunkce1.osetriZapisTextDB1(this.getNazev()) + ", "
                + "staty_popis= " + TextFunkce1.osetriZapisTextDB1(this.getPopis()) + ", "
                + "staty_tuzemsko= " + this.isTuzemsko() + ", "
                + "staty_eu= " +this.isEu() + ", "
                + "staty_poznamky= " + TextFunkce1.osetriZapisTextDB1(this.getPoznamky()) + ", "
                + "staty_poradi_vyber = " + this.getPoradiVyber() + " "               
                + "WHERE staty_id = " + this.idStat;
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

    /**
     * @return the tuzemsko
     */
    public boolean isTuzemsko() {
        return tuzemsko;
    }

    /**
     * @param tuzemsko the tuzemsko to set
     */
    public void setTuzemsko(boolean tuzemsko) {
        this.tuzemsko = tuzemsko;
    }

    /**
     * @return the eu
     */
    public boolean isEu() {
        return eu;
    }

    /**
     * @param eu the eu to set
     */
    public void setEu(boolean eu) {
        this.eu = eu;
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
}
