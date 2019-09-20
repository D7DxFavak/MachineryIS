/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.util.Date;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;

/**
 *
 * @author Favak
 */
public class TridaNabidka1 {

    private long idNabidky;
    private int cisloNabidky;
    private int poradi;
    private int idZakaznik;
    private Date datum;
    private int idVykres;
    private int pocetObjednanychKusu;
    private int idMena;
    private int idPolotovar;
    private String cenaKus;
    private String cenaKg;
    private String materialMnozstvi;
    private String prace;
    private String prace2;
    private String prace3;
    private String priprava;
    private String pgm;
    private String sazba1;
    private String sazba2;
    private String sazba3;
    private String sazba4;
    private String sazba5;
    private int pocetKusuRocne;
    private String povrchUprava;
    private String brouseni;
    private boolean ukoncena;
    private String poznamka;
    private String popisMena;
    private String popisMaterial;
    private String popisPolotovar;
    private String popisAktivni;
    private TridaVykres1 tv1;
    private TridaPolotovar1 tp1;
    private java.text.DateFormat df = java.text.DateFormat.getDateInstance();

    public TridaNabidka1() {


        tv1 = null;
    }

    public TridaNabidka1(long id) {
        this.selectData(id);
    }

    public boolean selectData(long id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT nabidky_id, nabidky_zakaznik_id, nabidky_datum, nabidky_cislo_vykresu, "
                    + "nabidky_pocet_objednanych_kusu, nabidky_polotovar_id, nabidky_cena_za_kus, "
                    + "nabidky_cena_za_kg, nabidky_mena_id, nabidky_mnozstvi_materialu, "
                    + "nabidky_prace, nabidky_priprava, nabidky_pgm, nabidky_sazba1, "
                    + "nabidky_sazba2, nabidky_sazba3, nabidky_pocet_kusu_rocne, "
                    + "CASE WHEN nabidky_povrch_uprava = 0 THEN '' ELSE btrim(to_char(nabidky_povrch_uprava,'FM99990D00'),'.,') END AS uprava, "
                    + "nabidky_brouseni, nabidky_poznamka, "
                    + "nabidky_prace2, nabidky_sazba4, nabidky_prace3, nabidky_sazba5 "
                    + "FROM spolecne.nabidky "
                    + "WHERE nabidky_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdNabidky(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                this.setDatum(q.getDate(3));
                this.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                this.tv1 = new TridaVykres1(this.getIdVykres());
                this.setPocetObjednanychKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(5)));
                this.setIdPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                this.setCenaKus(SQLFunkceObecne.osetriCteniString(q.getString(7)));
                this.setCenaKg(SQLFunkceObecne.osetriCteniString(q.getString(8)));
                this.setIdMena(SQLFunkceObecne.osetriCteniInt(q.getInt(9)));
                this.setMaterialMnozstvi(SQLFunkceObecne.osetriCteniString(q.getString(10)));
                this.setPrace(SQLFunkceObecne.osetriCteniString(q.getString(11)));
                this.setPriprava(SQLFunkceObecne.osetriCteniString(q.getString(12)));
                this.setPgm(SQLFunkceObecne.osetriCteniString(q.getString(13)));
                this.setSazba1(SQLFunkceObecne.osetriCteniString(q.getString(14)));
                this.setSazba2(SQLFunkceObecne.osetriCteniString(q.getString(15)));
                this.setSazba3(SQLFunkceObecne.osetriCteniString(q.getString(16)));
                this.setPocetKusuRocne(SQLFunkceObecne.osetriCteniInt(q.getInt(17)));
                this.setPovrchUprava(SQLFunkceObecne.osetriCteniString(q.getString(18)));
                this.setBrouseni(SQLFunkceObecne.osetriCteniString(q.getString(19)));
                this.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(20)));
                this.setPrace2(SQLFunkceObecne.osetriCteniString(q.getString(21)));
                this.setSazba4(SQLFunkceObecne.osetriCteniString(q.getString(22)));
                this.setPrace3(SQLFunkceObecne.osetriCteniString(q.getString(23)));
                this.setSazba5(SQLFunkceObecne.osetriCteniString(q.getString(24)));

            }
            return true;
        } catch (Exception e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }
    }

    public int updateData() {
        int r = -10000;
        try {
            String dotaz = "UPDATE spolecne.nabidky "
                    + "SET nabidky_zakaznik_id= " + this.idZakaznik + ", ";
            if (this.datum != null) {
                dotaz += "nabidky_datum= " + TextFunkce1.osetriZapisTextDB1(df.format(datum)) + ", ";
            }
            dotaz += "nabidky_cislo_vykresu= " + this.idVykres + ", "
                    + "nabidky_pocet_objednanych_kusu= " + this.pocetObjednanychKusu + ", "
                    + "nabidky_polotovar_id= " + this.idPolotovar + ", "
                    + "nabidky_cena_za_kus= " + TextFunkce1.osetriZapisNumericDB1(this.cenaKus) + ", "
                    + "nabidky_cena_za_kg= " + TextFunkce1.osetriZapisNumericDB1(this.cenaKg) + ", "
                    + "nabidky_mena_id= " + idMena + ", "
                    + "nabidky_mnozstvi_materialu= " + TextFunkce1.osetriZapisNumericDB1(this.materialMnozstvi) + ", "
                    + "nabidky_prace= " + TextFunkce1.osetriZapisNumericDB1(this.prace) + ", "
                    + "nabidky_prace2= " + TextFunkce1.osetriZapisNumericDB1(this.prace2) + ", "
                    + "nabidky_prace3= " + TextFunkce1.osetriZapisNumericDB1(this.prace3) + ", "
                    + "nabidky_priprava= " + TextFunkce1.osetriZapisNumericDB1(this.priprava) + ", "
                    + "nabidky_pgm= " + TextFunkce1.osetriZapisNumericDB1(this.pgm) + ", "
                    + "nabidky_sazba1= " + TextFunkce1.osetriZapisNumericDB1(this.sazba1) + ", "
                    + "nabidky_sazba2= " + TextFunkce1.osetriZapisNumericDB1(this.sazba2) + ", "
                    + "nabidky_sazba3= " + TextFunkce1.osetriZapisNumericDB1(this.sazba3) + ", "
                    + "nabidky_sazba4= " + TextFunkce1.osetriZapisNumericDB1(this.sazba4) + ", "
                    + "nabidky_sazba5= " + TextFunkce1.osetriZapisNumericDB1(this.sazba5) + ", "
                    + "nabidky_pocet_kusu_rocne= " + this.pocetKusuRocne + ", "
                    + "nabidky_povrch_uprava= " + TextFunkce1.osetriZapisNumericDB1(this.povrchUprava) + ", "
                    + "nabidky_brouseni= " + TextFunkce1.osetriZapisNumericDB1(this.brouseni) + ", "
                    + "nabidky_ukoncena = FALSE, "
                    + "nabidky_poznamka = " + TextFunkce1.osetriZapisTextDB1(this.poznamka) + " "
                    + "WHERE nabidky_id = " + idNabidky;
          //  System.out.println("Update data : " + dotaz);
            r = SQLFunkceObecne2.update(dotaz);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return r;
        }
    }

    public long insertData() {
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT MAX(nabidky_id) FROM spolecne.nabidky");
            while (id.next()) {
                this.setIdNabidky(id.getLong(1) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.nabidky( "
                    + "nabidky_id, nabidky_zakaznik_id, nabidky_datum, "
                    + "nabidky_cislo_vykresu, "
                    + "nabidky_pocet_objednanych_kusu, nabidky_polotovar_id, nabidky_cena_za_kus, "
                    + "nabidky_cena_za_kg, nabidky_mena_id, nabidky_mnozstvi_materialu, nabidky_prace, "
                    + "nabidky_priprava, nabidky_pgm, nabidky_sazba1, nabidky_sazba2, nabidky_sazba3, "
                    + "nabidky_pocet_kusu_rocne, nabidky_povrch_uprava, nabidky_brouseni, nabidky_ukoncena, nabidky_poznamka, "
                    + "nabidky_prace2, nabidky_sazba4,  nabidky_prace3, nabidky_sazba5 "
                    + ") VALUES (" + idNabidky + ", " + idZakaznik + ", " + TextFunkce1.osetriZapisTextDB1(df.format(datum)) + ", "
                    + idVykres + ", " + this.pocetObjednanychKusu + ", "
                    + this.idPolotovar + ", " + TextFunkce1.osetriZapisNumericDB1(this.cenaKus) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.cenaKg) + ", " + idMena + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.materialMnozstvi) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.prace) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.priprava) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.pgm) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.sazba1) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.sazba2) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.sazba3) + ", "
                    + this.pocetKusuRocne + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.povrchUprava) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.brouseni) + ", FALSE, "
                    + TextFunkce1.osetriZapisTextDB1(this.poznamka) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.prace2) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.sazba4) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.prace3) + ", "
                    + TextFunkce1.osetriZapisNumericDB1(this.sazba5) + " )";
            System.out.println(dotaz);
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return this.getIdNabidky();
        }
    }

    /**
     * @return the tv1
     */
    public TridaVykres1 getTv1() {
        return tv1;
    }

    /**
     * @param tv1 the tv1 to set
     */
    public void setTv1(TridaVykres1 tv1) {
        this.tv1 = tv1;
    }

    /**
     * @return the idObjednavky
     */
    public long getIdNabidky() {
        return idNabidky;
    }

    /**
     * @param idObjednavky the idObjednavky to set
     */
    public void setIdNabidky(long idNabidky) {
        this.idNabidky = idNabidky;
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
     * @return the idVykres
     */
    public int getIdVykres() {
        return idVykres;
    }

    /**
     * @param idVykres the idVykres to set
     */
    public void setIdVykres(int idVykres) {
        this.idVykres = idVykres;
    }

    /**
     * @return the pocetObjednanychKusu
     */
    public int getPocetObjednanychKusu() {
        return pocetObjednanychKusu;
    }

    /**
     * @param pocetObjednanychKusu the pocetObjednanychKusu to set
     */
    public void setPocetObjednanychKusu(int pocetObjednanychKusu) {
        this.pocetObjednanychKusu = pocetObjednanychKusu;
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
     * @return the idPolotovar
     */
    public int getIdPolotovar() {
        return idPolotovar;
    }

    /**
     * @param idPolotovar the idPolotovar to set
     */
    public void setIdPolotovar(int idPolotovar) {
        this.idPolotovar = idPolotovar;
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
     * @return the cenaKg
     */
    public String getCenaKg() {
        return cenaKg;
    }

    /**
     * @param cenaKg the cenaKg to set
     */
    public void setCenaKg(String cenaKg) {
        this.cenaKg = cenaKg;
    }

    /**
     * @return the materialMnozstvi
     */
    public String getMaterialMnozstvi() {
        return materialMnozstvi;
    }

    /**
     * @param materialMnozstvi the materialMnozstvi to set
     */
    public void setMaterialMnozstvi(String materialMnozstvi) {
        this.materialMnozstvi = materialMnozstvi;
    }

    /**
     * @return the prace
     */
    public String getPrace() {
        return prace;
    }

    /**
     * @param prace the prace to set
     */
    public void setPrace(String prace) {
        this.prace = prace;
    }

    /**
     * @return the prace2
     */
    public String getPrace2() {
        return prace2;
    }

    /**
     * @param prace2 the prace2 to set
     */
    public void setPrace2(String prace2) {
        this.prace2 = prace2;
    }

    /**
     * @return the prace3
     */
    public String getPrace3() {
        return prace3;
    }

    /**
     * @param prace3 the prace3 to set
     */
    public void setPrace3(String prace3) {
        this.prace3 = prace3;
    }

    /**
     * @return the priprava
     */
    public String getPriprava() {
        return priprava;
    }

    /**
     * @param priprava the priprava to set
     */
    public void setPriprava(String priprava) {
        this.priprava = priprava;
    }

    /**
     * @return the pgm
     */
    public String getPgm() {
        return pgm;
    }

    /**
     * @param pgm the pgm to set
     */
    public void setPgm(String pgm) {
        this.pgm = pgm;
    }

    /**
     * @return the sazba1
     */
    public String getSazba1() {
        return sazba1;
    }

    /**
     * @param sazba1 the sazba1 to set
     */
    public void setSazba1(String sazba1) {
        this.sazba1 = sazba1;
    }

    /**
     * @return the sazba2
     */
    public String getSazba2() {
        return sazba2;
    }

    /**
     * @param sazba2 the sazba2 to set
     */
    public void setSazba2(String sazba2) {
        this.sazba2 = sazba2;
    }

    /**
     * @return the sazba3
     */
    public String getSazba3() {
        return sazba3;
    }

    /**
     * @param sazba3 the sazba3 to set
     */
    public void setSazba3(String sazba3) {
        this.sazba3 = sazba3;
    }

    /**
     * @return the sazba4
     */
    public String getSazba4() {
        return sazba4;
    }

    /**
     * @param sazba4 the sazba4 to set
     */
    public void setSazba4(String sazba4) {
        this.sazba4 = sazba4;
    }

    /**
     * @return the sazba5
     */
    public String getSazba5() {
        return sazba5;
    }

    /**
     * @param sazba5 the sazba5 to set
     */
    public void setSazba5(String sazba5) {
        this.sazba5 = sazba5;
    }

    /**
     * @return the pocetKusuRocne
     */
    public int getPocetKusuRocne() {
        return pocetKusuRocne;
    }

    /**
     * @param pocetKusuRocne the pocetKusuRocne to set
     */
    public void setPocetKusuRocne(int pocetKusuRocne) {
        this.pocetKusuRocne = pocetKusuRocne;
    }

    /**
     * @return the povrchUprava
     */
    public String getPovrchUprava() {
        return povrchUprava;
    }

    /**
     * @param povrchUprava the povrchUprava to set
     */
    public void setPovrchUprava(String povrchUprava) {
        this.povrchUprava = povrchUprava;
    }

    /**
     * @return the brouseni
     */
    public String getBrouseni() {
        return brouseni;
    }

    /**
     * @param brouseni the brouseni to set
     */
    public void setBrouseni(String brouseni) {
        this.brouseni = brouseni;
    }

    /**
     * @return the ukoncena
     */
    public boolean isUkoncena() {
        return ukoncena;
    }

    /**
     * @param ukoncena the ukoncena to set
     */
    public void setUkoncena(boolean ukoncena) {
        this.ukoncena = ukoncena;
    }

    /**
     * @return the poznamka
     */
    public String getPoznamka() {
        return poznamka;
    }

    /**
     * @param poznamka the poznamka to set
     */
    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    /**
     * @return the df
     */
    public java.text.DateFormat getDf() {
        return df;
    }

    /**
     * @param df the df to set
     */
    public void setDf(java.text.DateFormat df) {
        this.df = df;
    }

    /**
     * @return the datum
     */
    public Date getDatum() {
        return datum;
    }

    /**
     * @param datum the datum to set
     */
    public void setDatum(Date datum) {
        this.datum = datum;
    }

    /**
     * @return the tp1
     */
    public TridaPolotovar1 getTp1() {
        return tp1;
    }

    /**
     * @param tp1 the tp1 to set
     */
    public void setTp1(TridaPolotovar1 tp1) {
        this.tp1 = tp1;
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

    /**
     * @return the cisloNabidky
     */
    public int getCisloNabidky() {
        return cisloNabidky;
    }

    /**
     * @param cisloNabidky the cisloNabidky to set
     */
    public void setCisloNabidky(int cisloNabidky) {
        this.cisloNabidky = cisloNabidky;
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
     * @return the popisMaterial
     */
    public String getPopisMaterial() {
        return popisMaterial;
    }

    /**
     * @param popisMaterial the popisMaterial to set
     */
    public void setPopisMaterial(String popisMaterial) {
        this.popisMaterial = popisMaterial;
    }

    /**
     * @return the popisPolotovar
     */
    public String getPopisPolotovar() {
        return popisPolotovar;
    }

    /**
     * @param popisPolotovar the popisPolotovar to set
     */
    public void setPopisPolotovar(String popisPolotovar) {
        this.popisPolotovar = popisPolotovar;
    }

    /**
     * @return the popisAktivni
     */
    public String getPopisAktivni() {
        return popisAktivni;
    }

    /**
     * @param popisAktivni the popisAktivni to set
     */
    public void setPopisAktivni(String popisAktivni) {
        this.popisAktivni = popisAktivni;
    }
}
