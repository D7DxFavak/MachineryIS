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
public class TridaZakaznik1 {

    private int idZakaznik;
    private int idStat;
    private int idDruhZakaznik;
    private int poradiVyber;
    private String nazev;
    private String popis;
    private String ustNr;
    private String vatNr;
    private String adresa;
    private String mesto;
    private String psc;
    private String telefony;
    private String faxy;
    private String eMaily;
    private String url;
    private String bankovniSpojeni;
    private String poznamky;
    private String mikronBank;
    private String mikronIban;
    private String prvniRadek;
    private String druhyRadek;
    private boolean aktivni;

    public TridaZakaznik1() {
        idZakaznik = 0;
        idStat = 0;
        idDruhZakaznik = 0;
        poradiVyber = 0;
        nazev = "";
        popis = "";
        ustNr = "";
        vatNr = "";
        adresa = "";
        mesto = "";
        psc = "";
        telefony = "";
        faxy = "";
        eMaily = "";
        url = "";
        bankovniSpojeni = "";
        poznamky = "";
        mikronBank = "";
        mikronIban = "";
        prvniRadek = "";
        druhyRadek = "";
        aktivni = true;
    }

    public TridaZakaznik1(int id) {
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
     * @return the idDruhZakaznik
     */
    public int getIdDruhZakaznik() {
        return idDruhZakaznik;
    }

    /**
     * @param idDruhZakaznik the idDruhZakaznik to set
     */
    public void setIdDruhZakaznik(int idDruhZakaznik) {
        this.idDruhZakaznik = idDruhZakaznik;
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
    public String getUstNr() {
        return ustNr;
    }

    /**
     * @param ustNr the ustNr to set
     */
    public void setUstNr(String ustNr) {
        this.ustNr = ustNr;
    }

    /**
     * @return the vatNr
     */
    public String getVatNr() {
        return vatNr;
    }

    /**
     * @param vatNr the vatNr to set
     */
    public void setVatNr(String vatNr) {
        this.vatNr = vatNr;
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
     * @return the telefony
     */
    public String getTelefony() {
        return telefony;
    }

    /**
     * @param telefony the telefony to set
     */
    public void setTelefony(String telefony) {
        this.telefony = telefony;
    }

    /**
     * @return the faxy
     */
    public String getFaxy() {
        return faxy;
    }

    /**
     * @param faxy the faxy to set
     */
    public void setFaxy(String faxy) {
        this.faxy = faxy;
    }

    /**
     * @return the eMaily
     */
    public String geteMaily() {
        return eMaily;
    }

    /**
     * @param eMaily the eMaily to set
     */
    public void seteMaily(String eMaily) {
        this.eMaily = eMaily;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the bankovniSpojeni
     */
    public String getBankovniSpojeni() {
        return bankovniSpojeni;
    }

    /**
     * @param bankovniSpojeni the bankovniSpojeni to set
     */
    public void setBankovniSpojeni(String bankovniSpojeni) {
        this.bankovniSpojeni = bankovniSpojeni;
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
     * @return the mikronBank
     */
    public String getMikronBank() {
        return mikronBank;
    }

    /**
     * @param mikronBank the mikronBank to set
     */
    public void setMikronBank(String mikronBank) {
        this.mikronBank = mikronBank;
    }

    /**
     * @return the mikronIban
     */
    public String getMikronIban() {
        return mikronIban;
    }

    /**
     * @param mikronIban the mikronIban to set
     */
    public void setMikronIban(String mikronIban) {
        this.mikronIban = mikronIban;
    }

    public boolean selectData() {
        return this.selectData(this.idZakaznik);
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT subjekty_trhu_id, subjekty_trhu_nazev, subjekty_trhu_popis, subjekty_trhu_druh_id, "
                    + "subjekty_trhu_ust_id_nr, subjekty_trhu_vat_nr, subjekty_trhu_adresa, "
                    + "subjekty_trhu_mesto, subjekty_trhu_psc, subjekty_trhu_stat_id, "
                    + "subjekty_trhu_telefony, subjekty_trhu_faxy, subjekty_trhu_e_maily, "
                    + "subjekty_trhu_url, subjekty_trhu_bankovni_spojeni, subjekty_trhu_poznamky, "
                    + "subjekty_trhu_poradi, subjekty_trhu_mikron_bank, subjekty_trhu_mikron_iban, "
                    + "subjekty_trhu_prvni_radek, subjekty_trhu_druhy_radek, subjekty_trhu_aktivni  "
                    + "FROM spolecne.subjekty_trhu "
                    + "WHERE subjekty_trhu_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.idZakaznik = SQLFunkceObecne.osetriCteniInt(q.getInt(1));
                this.nazev = SQLFunkceObecne.osetriCteniString(q.getString(2));
                this.popis = SQLFunkceObecne.osetriCteniString(q.getString(3));
                this.idDruhZakaznik = SQLFunkceObecne.osetriCteniInt(q.getInt(4));
                this.ustNr = SQLFunkceObecne.osetriCteniString(q.getString(5));
                this.vatNr = SQLFunkceObecne.osetriCteniString(q.getString(6));
                this.adresa = SQLFunkceObecne.osetriCteniString(q.getString(7));
                this.mesto = SQLFunkceObecne.osetriCteniString(q.getString(8));
                this.psc = SQLFunkceObecne.osetriCteniString(q.getString(9));
                this.idStat = SQLFunkceObecne.osetriCteniInt(q.getInt(10));
                this.telefony = SQLFunkceObecne.osetriCteniString(q.getString(11));
                this.faxy = SQLFunkceObecne.osetriCteniString(q.getString(12));
                this.eMaily = SQLFunkceObecne.osetriCteniString(q.getString(13));
                this.url = SQLFunkceObecne.osetriCteniString(q.getString(14));
                this.bankovniSpojeni = SQLFunkceObecne.osetriCteniString(q.getString(15));
                this.poznamky = SQLFunkceObecne.osetriCteniString(q.getString(16));
                this.poradiVyber = SQLFunkceObecne.osetriCteniInt(q.getInt(17));
                this.mikronBank = SQLFunkceObecne.osetriCteniString(q.getString(18));
                this.mikronIban = SQLFunkceObecne.osetriCteniString(q.getString(19));
                this.prvniRadek = SQLFunkceObecne.osetriCteniString(q.getString(20));
                this.druhyRadek = SQLFunkceObecne.osetriCteniString(q.getString(21));
                this.setAktivni(q.getBoolean(22));
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
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(subjekty_trhu_id) FROM spolecne.subjekty_trhu");
            while (id.next()) {
                this.idZakaznik = id.getInt(1) + 1;
            }
            //int pocet_mj = Integer.valueOf(PocetKusuTextField1.getText().trim()).intValue();
            int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.subjekty_trhu("
                    + "subjekty_trhu_id, "
                    + "subjekty_trhu_nazev, "
                    + "subjekty_trhu_popis, "
                    + "subjekty_trhu_druh_id, "
                    + "subjekty_trhu_ust_id_nr, "
                    + "subjekty_trhu_vat_nr, "
                    + "subjekty_trhu_adresa, "
                    + "subjekty_trhu_mesto, "
                    + "subjekty_trhu_psc, "
                    + "subjekty_trhu_stat_id, "
                    + "subjekty_trhu_telefony, "
                    + "subjekty_trhu_e_maily, "
                    + "subjekty_trhu_poradi, "
                    + "subjekty_trhu_poznamky, "
                    + "subjekty_trhu_mikron_bank, "
                    + "subjekty_trhu_mikron_iban, "
                    + "subjekty_trhu_prvni_radek, "
                    + "subjekty_trhu_druhy_radek, "
                    + "subjekty_trhu_aktivni) "
                    + "VALUES( " + this.idZakaznik
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.nazev)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.popis)
                    + ", " + this.idDruhZakaznik
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.ustNr)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.vatNr)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.adresa)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mesto)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.psc)
                    + ", " + idStat
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.telefony)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.eMaily)
                    + ", 1100 "
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.poznamky)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mikronBank)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mikronIban)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.prvniRadek)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.druhyRadek) 
                    + ", " + this.isAktivni() + ")");
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            //e.printStackTrace();
            JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při ukládání dat oboru", e.toString());
        }
        return this.idZakaznik;
    }

    public int updateData() {
        String dotaz = "UPDATE spolecne.subjekty_trhu "
                + "SET subjekty_trhu_nazev= " + TextFunkce1.osetriZapisTextDB1(this.nazev) + ", "
                + "subjekty_trhu_popis= " + TextFunkce1.osetriZapisTextDB1(this.popis) + ", "
                + "subjekty_trhu_druh_id= " + this.idDruhZakaznik + ", "
                + "subjekty_trhu_ust_id_nr= " + TextFunkce1.osetriZapisTextDB1(this.ustNr) + ", "
                + "subjekty_trhu_vat_nr= " + TextFunkce1.osetriZapisTextDB1(this.vatNr) + ", "
                + "subjekty_trhu_adresa= " + TextFunkce1.osetriZapisTextDB1(this.adresa) + ", "
                + "subjekty_trhu_mesto= " + TextFunkce1.osetriZapisTextDB1(this.mesto) + ", "
                + "subjekty_trhu_psc= " + TextFunkce1.osetriZapisTextDB1(this.psc) + ", "
                + "subjekty_trhu_stat_id= " + idStat + ", "
                + "subjekty_trhu_telefony= " + TextFunkce1.osetriZapisTextDB1(this.telefony) + ", "
                + "subjekty_trhu_faxy= " + TextFunkce1.osetriZapisTextDB1(null) + ", "
                + "subjekty_trhu_e_maily= " + TextFunkce1.osetriZapisTextDB1(this.eMaily) + ", "
                + "subjekty_trhu_url= " + TextFunkce1.osetriZapisTextDB1(null) + ", "
                + "subjekty_trhu_bankovni_spojeni= " + TextFunkce1.osetriZapisTextDB1(null) + ", "
                + "subjekty_trhu_poznamky= " + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                + "subjekty_trhu_poradi= " + 10 + ", "
                + "subjekty_trhu_mikron_bank= " + TextFunkce1.osetriZapisTextDB1(this.mikronBank) + ", "
                + "subjekty_trhu_mikron_iban= " + TextFunkce1.osetriZapisTextDB1(this.mikronIban) + ", "
                + "subjekty_trhu_prvni_radek= " + TextFunkce1.osetriZapisTextDB1(this.prvniRadek) + ", "
                + "subjekty_trhu_druhy_radek= " + TextFunkce1.osetriZapisTextDB1(this.druhyRadek) + ", "
                + "subjekty_trhu_aktivni = " + this.isAktivni() + " "
                + "WHERE subjekty_trhu_id = " + this.idZakaznik;

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
     * @return the prvniRadek
     */
    public String getPrvniRadek() {
        return prvniRadek;
    }

    /**
     * @param prvniRadek the prvniRadek to set
     */
    public void setPrvniRadek(String prvniRadek) {
        this.prvniRadek = prvniRadek;
    }

    /**
     * @return the druhyRadek
     */
    public String getDruhyRadek() {
        return druhyRadek;
    }

    /**
     * @param druhyRadek the druhyRadek to set
     */
    public void setDruhyRadek(String druhyRadek) {
        this.druhyRadek = druhyRadek;
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
}
