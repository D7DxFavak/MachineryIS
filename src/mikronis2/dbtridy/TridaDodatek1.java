/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.tridy.DvojiceCisloCislo;

/**
 *
 * @author Favak
 */
public class TridaDodatek1 {

    private int idDodatek;
    private int idFaktura;
    private String text;
    private int pocet;
    private String cenaKus;
    private int idMena;
    private String poznamky;
    private String popisMena;
    private int poradi;

    public TridaDodatek1() {
        this.idDodatek = 0;
        this.idFaktura = 0;
        this.text = "";
        this.pocet = 1;
        this.cenaKus = "";
        this.idMena = 0;
        this.poznamky = "";
        this.popisMena = "";
        this.poradi = 0;
    }

    public TridaDodatek1(int idFaktura, int idDodatek) {
        this.selectData(idFaktura, idDodatek);
    }

    public boolean selectData() {
        return selectData(this.getIdDodatek(), this.getIdFaktura());
    }

    public boolean selectData(int idD, int idF) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT vazba_faktury_dodatky_faktury_id, vazba_faktury_dodatky_dodatky_id,"
                    + "vazba_faktury_dodatky_text, vazba_faktury_dodatky_pocet, vazba_faktury_dodatky_cena_za_kus, "
                    + "vazba_faktury_dodatky_mena_id, vazba_faktury_dodatky_poznamky, "
                    + "vazba_faktury_dodatky_poradi "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "WHERE vazba_faktury_dodatky_faktury_id = " + idF + " "
                    + "AND  vazba_faktury_dodatky_dodatky_id = " + idD);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdFaktura(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setIdDodatek(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                this.setText(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                this.setPocet(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                this.setCenaKus(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                this.setIdMena(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                this.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                this.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
            }
            return true;
        } catch (Exception e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }
    }

    public DvojiceCisloCislo insertData() {
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(vazba_faktury_dodatky_dodatky_id)+1,1) FROM spolecne.vazba_faktury_dodatky "
                    + "WHERE vazba_faktury_dodatky_faktury_id = " + this.idFaktura);
            while (id.next()) {
                this.idDodatek = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.vazba_faktury_dodatky("
                    + "vazba_faktury_dodatky_faktury_id, vazba_faktury_dodatky_dodatky_id, "
                    + "vazba_faktury_dodatky_text, vazba_faktury_dodatky_pocet, vazba_faktury_dodatky_cena_za_kus, "
                    + "vazba_faktury_dodatky_mena_id, vazba_faktury_dodatky_poznamky, "
                    + "vazba_faktury_dodatky_poradi) "
                    + "VALUES (" + this.idFaktura + ", "
                    + this.idDodatek + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.text) + ", "
                    + this.pocet + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.cenaKus) + ", "
                    + this.idMena + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                    + this.poradi + ") ";
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return new DvojiceCisloCislo(this.idFaktura, this.idDodatek);
        }
    }

    public int updateData() {
        int r = -10000;
        try {
            String dotaz = "UPDATE spolecne.vazba_faktury_dodatky "
                    + "SET vazba_faktury_dodatky_faktury_id= " + this.idFaktura + ", "
                    + "vazba_faktury_dodatky_dodatky_id= " + this.idDodatek + ", "
                    + "vazba_faktury_dodatky_text= " + TextFunkce1.osetriZapisTextDB1(this.text) + ", "
                    + "vazba_faktury_dodatky_pocet= " + this.pocet + ", "
                    + "vazba_faktury_dodatky_cena_za_kus= " + TextFunkce1.osetriZapisTextDB1(this.cenaKus) + ", "
                    + "vazba_faktury_dodatky_mena_id= " + this.idMena + ", "
                    + "vazba_faktury_dodatky_poznamky= " + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                    + "vazba_faktury_dodatky_poradi= " + this.poradi + " "
                    + "WHERE vazba_faktury_dodatky_faktury_id= " + this.idFaktura + " "
                    + "AND vazba_faktury_dodatky_dodatky_id= " + this.idDodatek;
            //System.out.println("Update data : " + dotaz);
            r = SQLFunkceObecne2.update(dotaz);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return r;
        }

    }

    /**
     * @return the idDodatek
     */
    public int getIdDodatek() {
        return idDodatek;
    }

    /**
     * @param idDodatek the idDodatek to set
     */
    public void setIdDodatek(int idDodatek) {
        this.idDodatek = idDodatek;
    }

    /**
     * @return the idFaktura
     */
    public int getIdFaktura() {
        return idFaktura;
    }

    /**
     * @param idFaktura the idFaktura to set
     */
    public void setIdFaktura(int idFaktura) {
        this.idFaktura = idFaktura;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the pocet
     */
    public int getPocet() {
        return pocet;
    }

    /**
     * @param pocet the pocet to set
     */
    public void setPocet(int pocet) {
        this.pocet = pocet;
    }

    /**
     * @return the cenaKus
     */
    public String getCenaKus() {
        return cenaKus;
    }

    /**
     * @param cenaKus the cenaKus to set
     */
    public void setCenaKus(String cenaKus) {
        this.cenaKus = cenaKus;
    }

    /**
     * @return the idMena
     */
    public int getIdMena() {
        return idMena;
    }

    /**
     * @param idMena the idMena to set
     */
    public void setIdMena(int idMena) {
        this.idMena = idMena;
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
     * @return the poradi
     */
    public int getPoradi() {
        return poradi;
    }

    /**
     * @param poradi the poradi to set
     */
    public void setPoradi(int poradi) {
        this.poradi = poradi;
    }

    /**
     * @return the popisMena
     */
    public String getPopisMena() {
        return popisMena;
    }

    /**
     * @param popisMena the popisMena to set
     */
    public void setPopisMena(String popisMena) {
        this.popisMena = popisMena;
    }

}
