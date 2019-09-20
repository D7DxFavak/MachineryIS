/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.math.BigDecimal;
import java.sql.ResultSet;
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
public class TridaRamcovaObjednavka {
    
    private int id;
    private int idZakaznik;
    private int idVykres;
    private int idMena;
    private Date datumObjednani;
    private String cisloObjednavky;    
    private String nazevSoucasti;
    private String poznamka;
    private int celkemKusu;
    private int odvolavkaKusu;
    private int objednanoKusu;
    private BigDecimal cenaKus;
    private TridaVykres1 tv1;
    private String menaZkratka;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();
    
    public TridaRamcovaObjednavka() {
        this.id = 0;
        this.idZakaznik = 0;
        this.idVykres = 0;
        this.idMena = 0;
        this.celkemKusu = 0;
        this.datumObjednani = null;
        this.cisloObjednavky = "";
        this.nazevSoucasti = "";
        this.poznamka = "";
        this.odvolavkaKusu = 0;
        this.cenaKus = new BigDecimal(0);        
        this.tv1 = null;
    }
    
    public TridaRamcovaObjednavka(int id) {
        selectData(id);
    }
    
    public boolean selectData() {
        return selectData(this.id);
    }
    
    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT ramec_objednavky_zakaznik_id, ramec_objednavky_cislo_objednavky, "
                    + "ramec_objednavky_datum_objednani, ramec_objednavky_nazev_soucasti, "
                    + "ramec_objednavky_cislo_vykresu, "
                    + "ramec_objednavky_pocet_objednanych_kusu, "
                    + "ramec_objednavky_cena_za_kus, ramec_objednavky_mena_id, "
                    + "ramec_objednavky_odvolavka_kusu, "
                    + "ramec_objednavky_poznamka, "
                    + "ramec_objednavky_id "
                    + "FROM spolecne.ramec_objednavky "
                    + "WHERE ramec_objednavky_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.idZakaznik = q.getInt(1);
                this.cisloObjednavky = TextFunkce1.osetriCteniTextDB1(q.getString(2));
                this.datumObjednani = q.getDate(3);
                this.nazevSoucasti = TextFunkce1.osetriCteniTextDB1(q.getString(4));
                this.idVykres = q.getInt(5);
                this.celkemKusu = q.getInt(6);
                this.cenaKus = q.getBigDecimal(7);
                this.idMena = q.getInt(8);
                this.odvolavkaKusu = q.getInt(9);
                this.poznamka = TextFunkce1.osetriCteniTextDB1(q.getString(10));                
                this.id = q.getInt(11);
                
                
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
            String dotaz = "UPDATE spolecne.ramec_objednavky "
                    + "SET ramec_objednavky_id = " + this.id + ", "
                    + "ramec_objednavky_zakaznik_id = " + this.idZakaznik + ", ";
            if (this.datumObjednani != null) {
                dotaz += "ramec_objednavky_datum_objednani = " + TextFunkce1.osetriZapisTextDB1(df.format(this.datumObjednani)) + ", ";
            }
            dotaz += "ramec_objednavky_cislo_objednavky = " + TextFunkce1.osetriZapisTextDB1(this.cisloObjednavky) + ", "
                    + "ramec_objednavky_nazev_soucasti = " + TextFunkce1.osetriZapisTextDB1(this.nazevSoucasti) + ", "
                    + "ramec_objednavky_cislo_vykresu = " + this.idVykres + ", "
                    + "ramec_objednavky_pocet_objednanych_kusu = " + this.celkemKusu + ", "
                    + "ramec_objednavky_cena_za_kus = " + this.cenaKus + ", "
                    + "ramec_objednavky_mena_id = " + this.idMena + ", "
                    + "ramec_objednavky_odvolavka_kusu = " + this.odvolavkaKusu + ", "
                    + "ramec_objednavky_poznamka = " + TextFunkce1.osetriZapisTextDB1(this.poznamka) + " "
                    + "WHERE ramec_objednavky_id = " + this.id;
            //System.out.println("Update data : " + dotaz);
            r = SQLFunkceObecne2.update(dotaz);
            
        } catch (Exception e) {
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání rámcové objednávky", "Prosím zkontrolujte zadané informace");
            e.printStackTrace();
        } finally {
            return r;
        }
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
        
    }
    
    public long insertData() {
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(ramec_objednavky_id)+1,1) FROM spolecne.ramec_objednavky");
            while (id.next()) {
                this.id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.ramec_objednavky("
                    + "ramec_objednavky_id, ramec_objednavky_zakaznik_id, "
                    + "ramec_objednavky_datum_objednani, ramec_objednavky_nazev_soucasti, "
                    + "ramec_objednavky_cislo_vykresu, ramec_objednavky_pocet_objednanych_kusu, "
                    + "ramec_objednavky_cena_za_kus, ramec_objednavky_mena_id, "
                    + "ramec_objednavky_cislo_objednavky, ramec_objednavky_poznamka, ramec_objednavky_odvolavka_kusu) "
                    + "VALUES (" + this.id + ", "
                    + this.idZakaznik + ", "
                    + TextFunkce1.osetriZapisDatumDB1(this.datumObjednani) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.nazevSoucasti) + ", "
                    + this.idVykres + ", "
                    + this.celkemKusu + ", "
                    + this.cenaKus + ", "
                    + this.idMena + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.cisloObjednavky) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.poznamka) + ", "
                    + this.odvolavkaKusu + ") ";
            System.out.println("RO INSERT : " + dotaz);
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání rámcové objednávky", "Prosím zkontrolujte zadané informace");
            e.printStackTrace();
        } finally {
            return this.id;
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the datumObjednani
     */
    public Date getDatumObjednani() {
        return datumObjednani;
    }

    /**
     * @param datumObjednani the datumObjednani to set
     */
    public void setDatumObjednani(Date datumObjednani) {
        this.datumObjednani = datumObjednani;
    }

    /**
     * @return the cisloObjednavky
     */
    public String getCisloObjednavky() {
        return cisloObjednavky;
    }

    /**
     * @param cisloObjednavky the cisloObjednavky to set
     */
    public void setCisloObjednavky(String cisloObjednavky) {
        this.cisloObjednavky = cisloObjednavky;
    }

    /**
     * @return the nazevSoucasti
     */
    public String getNazevSoucasti() {
        return nazevSoucasti;
    }

    /**
     * @param nazevSoucasti the nazevSoucasti to set
     */
    public void setNazevSoucasti(String nazevSoucasti) {
        this.nazevSoucasti = nazevSoucasti;
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
     * @return the odvolavkaKusu
     */
    public int getOdvolavkaKusu() {
        return odvolavkaKusu;
    }

    /**
     * @param odvolavkaKusu the odvolavkaKusu to set
     */
    public void setOdvolavkaKusu(int odvolavkaKusu) {
        this.odvolavkaKusu = odvolavkaKusu;
    }

    /**
     * @return the cenaKus
     */
    public BigDecimal getCenaKus() {
        return cenaKus;
    }

    /**
     * @param cenaKus the cenaKus to set
     */
    public void setCenaKus(BigDecimal cenaKus) {
        this.cenaKus = cenaKus;
    }

    /**
     * @return the celkemKusu
     */
    public int getCelkemKusu() {
        return celkemKusu;
    }

    /**
     * @param celkemKusu the celkemKusu to set
     */
    public void setCelkemKusu(int celkemKusu) {
        this.celkemKusu = celkemKusu;
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
     * @return the objednanoKusu
     */
    public int getObjednanoKusu() {
        return objednanoKusu;
    }

    /**
     * @param objednanoKusu the objednanoKusu to set
     */
    public void setObjednanoKusu(int objednanoKusu) {
        this.objednanoKusu = objednanoKusu;
    }

    /**
     * @return the menaZkratka
     */
    public String getMenaZkratka() {
        return menaZkratka;
    }

    /**
     * @param menaZkratka the menaZkratka to set
     */
    public void setMenaZkratka(String menaZkratka) {
        this.menaZkratka = menaZkratka;
    }
}
