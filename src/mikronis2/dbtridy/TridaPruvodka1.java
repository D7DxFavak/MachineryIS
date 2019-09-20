/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.util.Date;

/**
 *
 * @author Favak
 */
public class TridaPruvodka1 {

    private int idPruvodky;
    private int nazev;
    private int vyrobenoKusu;
    private int pocetKusu;
    private int pocetPolotovaru;
    private int materialPrumernaDelka;
    private int narezanoZeSkladu;
    private TridaVykres1 tv1;
    private Date terminDokonceni;
    private String poznamky;
    private TridaPolotovar1 tp1;
    private TridaObjednavka1 tObj1;
    private String polotovarRozmer;
    private String celkovyCas;
    protected boolean odectena;

    /**
     * @return the idPruvodky
     */
    public int getIdPruvodky() {
        return idPruvodky;
    }

    /**
     * @param idPruvodky the idPruvodky to set
     */
    public void setIdPruvodky(int idPruvodky) {
        this.idPruvodky = idPruvodky;
    }

    /**
     * @return the nazev
     */
    public int getNazev() {
        return nazev;
    }

    /**
     * @param nazev the nazev to set
     */
    public void setNazev(int nazev) {
        this.nazev = nazev;
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
     * @return the terminDokonceni
     */
    public Date getTerminDokonceni() {
        return terminDokonceni;
    }

    /**
     * @param terminDokonceni the terminDokonceni to set
     */
    public void setTerminDokonceni(Date terminDokonceni) {
        this.terminDokonceni = terminDokonceni;
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
     * @return the pocetPolotovaru
     */
    public int getPocetPolotovaru() {
        return pocetPolotovaru;
    }

    /**
     * @param pocetPolotovaru the pocetPolotovaru to set
     */
    public void setPocetPolotovaru(int pocetPolotovaru) {
        this.pocetPolotovaru = pocetPolotovaru;
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
     * @return the materialPrumernaDelka
     */
    public int getMaterialPrumernaDelka() {
        return materialPrumernaDelka;
    }

    /**
     * @param materialPrumernaDelka the materialPrumernaDelka to set
     */
    public void setMaterialPrumernaDelka(int materialPrumernaDelka) {
        this.materialPrumernaDelka = materialPrumernaDelka;
    }

    /**
     * @return the tObj1
     */
    public TridaObjednavka1 gettObj1() {
        return tObj1;
    }

    /**
     * @param tObj1 the tObj1 to set
     */
    public void settObj1(TridaObjednavka1 tObj1) {
        this.tObj1 = tObj1;
    }

    /**
     * @return the vyrobenoKusu
     */
    public int getVyrobenoKusu() {
        return vyrobenoKusu;
    }

    /**
     * @param vyrobenoKusu the vyrobenoKusu to set
     */
    public void setVyrobenoKusu(int vyrobenoKusu) {
        this.vyrobenoKusu = vyrobenoKusu;
    }

    /**
     * @return the polotovarRozmer
     */
    public String getPolotovarRozmer() {
        return polotovarRozmer;
    }

    /**
     * @param polotovarRozmer the polotovarRozmer to set
     */
    public void setPolotovarRozmer(String polotovarRozmer) {
        this.polotovarRozmer = polotovarRozmer;
    }

    /**
     * @return the celkovyCas
     */
    public String getCelkovyCas() {
        return celkovyCas;
    }

    /**
     * @param celkovyCas the celkovyCas to set
     */
    public void setCelkovyCas(String celkovyCas) {
        this.celkovyCas = celkovyCas;
    }

    /**
     * @return the narezanoZeSkladu
     */
    public int getNarezanoZeSkladu() {
        return narezanoZeSkladu;
    }

    /**
     * @param narezanoZeSkladu the narezanoZeSkladu to set
     */
    public void setNarezanoZeSkladu(int narezanoZeSkladu) {
        this.narezanoZeSkladu = narezanoZeSkladu;
    }

    /**
     * @return the odectena
     */
    public boolean isOdectena() {
        return odectena;
    }

    /**
     * @param odectena the odectena to set
     */
    public void setOdectena(boolean odectena) {
        this.odectena = odectena;
    }
}
