/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mikronis2.dbtridy;

import java.math.BigDecimal;

/**
 *
 * @author 7Data Gotzy
 */
public class TridaPotvrzeniObjednavka1 {

    private String datumObjednani;
    private String datumPotvrzeni;
    private String nazev;
    private String cisloVykresu;
    private String revizeVykresu;
    private String cisloObjednavky;
    private int pocetKusu;
    private String datumDodani;
    private long idObjednavky;
    private int poradi;
    private BigDecimal cenaKus;

    public TridaPotvrzeniObjednavka1() {
        idObjednavky = 0;
        pocetKusu = 0;
        poradi = 0;
        cenaKus = new BigDecimal(0);
        datumObjednani = "";
        datumDodani = "";
        nazev = "";
        cisloVykresu = "";
        revizeVykresu = "";
        cisloObjednavky = "";
    }

    /**
     * @return the datumObjednani
     */
    public String getDatumObjednani() {
        return datumObjednani;
    }

    /**
     * @param datumObjednani the datumObjednani to set
     */
    public void setDatumObjednani(String datumObjednani) {
        this.datumObjednani = datumObjednani;
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
     * @return the cisloVykresu
     */
    public String getCisloVykresu() {
        return cisloVykresu;
    }

    /**
     * @param cisloVykresu the cisloVykresu to set
     */
    public void setCisloVykresu(String cisloVykresu) {
        this.cisloVykresu = cisloVykresu;
    }

    /**
     * @return the revizeVykresu
     */
    public String getRevizeVykresu() {
        return revizeVykresu;
    }

    /**
     * @param revizeVykresu the revizeVykresu to set
     */
    public void setRevizeVykresu(String revizeVykresu) {
        this.revizeVykresu = revizeVykresu;
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
     * @return the pocetKusu
     */
    public int getPocetKusu() {
        return pocetKusu;
    }

    /**
     * @param pocetKusu the pocetKusu to set
     */
    public void setPocetKusu(int pocetKusu) {
        this.pocetKusu = pocetKusu;
    }

    /**
     * @return the datumDodani
     */
    public String getDatumDodani() {
        return datumDodani;
    }

    /**
     * @param datumDodani the datumDodani to set
     */
    public void setDatumDodani(String datumDodani) {
        this.datumDodani = datumDodani;
    }

    /**
     * @return the idObjednavky
     */
    public long getIdObjednavky() {
        return idObjednavky;
    }

    /**
     * @param idObjednavky the idObjednavky to set
     */
    public void setIdObjednavky(long idObjednavky) {
        this.idObjednavky = idObjednavky;
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
     * @return the datumPotvrzeni
     */
    public String getDatumPotvrzeni() {
        return datumPotvrzeni;
    }

    /**
     * @param datumPotvrzeni the datumPotvrzeni to set
     */
    public void setDatumPotvrzeni(String datumPotvrzeni) {
        this.datumPotvrzeni = datumPotvrzeni;
    }

}
