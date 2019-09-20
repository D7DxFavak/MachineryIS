/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.Timestamp;

/**
 *
 * @author 7Data Gotzy
 */
public class TridaZaznam1 {

    private long transakce_id;
    private int zamestnanci_id;
    private int stroje_id;
    private int druh_id;
    private long pruvodky_id;
    private String timestamp;
    private String stroje_nazev;
    private String druh_nazev;
    private String zamestnanec;
    private String pruvodka_nazev;
    private String vykresy_cislo_revize;
    private int doba_prace_dny;
    private String doba_prace;
    private Timestamp currentTime;

    /**
     * @return the transakce_id
     */
    public long getTransakce_id() {
        return transakce_id;
    }

    /**
     * @param transakce_id the transakce_id to set
     */
    public void setTransakce_id(long transakce_id) {
        this.transakce_id = transakce_id;
    }

    /**
     * @return the zamestnanci_id
     */
    public int getZamestnanci_id() {
        return zamestnanci_id;
    }

    /**
     * @param zamestnanci_id the zamestnanci_id to set
     */
    public void setZamestnanci_id(int zamestnanci_id) {
        this.zamestnanci_id = zamestnanci_id;
    }

    /**
     * @return the stroje_id
     */
    public int getStroje_id() {
        return stroje_id;
    }

    /**
     * @param stroje_id the stroje_id to set
     */
    public void setStroje_id(int stroje_id) {
        this.stroje_id = stroje_id;
    }

    /**
     * @return the druh_id
     */
    public int getDruh_id() {
        return druh_id;
    }

    /**
     * @param druh_id the druh_id to set
     */
    public void setDruh_id(int druh_id) {
        this.druh_id = druh_id;
    }

    /**
     * @return the pruvodky_id
     */
    public long getPruvodky_id() {
        return pruvodky_id;
    }

    /**
     * @param pruvodky_id the pruvodky_id to set
     */
    public void setPruvodky_id(long pruvodky_id) {
        this.pruvodky_id = pruvodky_id;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the stroje_nazev
     */
    public String getStroje_nazev() {
        return stroje_nazev;
    }

    /**
     * @param stroje_nazev the stroje_nazev to set
     */
    public void setStroje_nazev(String stroje_nazev) {
        this.stroje_nazev = stroje_nazev;
    }

    /**
     * @return the druh_nazev
     */
    public String getDruh_nazev() {
        return druh_nazev;
    }

    /**
     * @param druh_nazev the druh_nazev to set
     */
    public void setDruh_nazev(String druh_nazev) {
        this.druh_nazev = druh_nazev;
    }

    /**
     * @return the zamestnanec
     */
    public String getZamestnanec() {
        return zamestnanec;
    }

    /**
     * @param zamestnanec the zamestnanec to set
     */
    public void setZamestnanec(String zamestnanec) {
        this.zamestnanec = zamestnanec;
    }

    /**
     * @return the pruvodka_nazev
     */
    public String getPruvodka_nazev() {
        return pruvodka_nazev;
    }

    /**
     * @param pruvodka_nazev the pruvodka_nazev to set
     */
    public void setPruvodka_nazev(String pruvodka_nazev) {
        this.pruvodka_nazev = pruvodka_nazev;
    }

    /**
     * @return the vykresy_cislo_revize
     */
    public String getVykresy_cislo_revize() {
        return vykresy_cislo_revize;
    }

    /**
     * @param vykresy_cislo_revize the vykresy_cislo_revize to set
     */
    public void setVykresy_cislo_revize(String vykresy_cislo_revize) {
        this.vykresy_cislo_revize = vykresy_cislo_revize;
    }

    /**
     * @return the doba_prace
     */
    public String getDoba_prace() {
        return doba_prace;
    }

    /**
     * @param doba_prace the doba_prace to set
     */
    public void setDoba_prace(String doba_prace) {
        this.doba_prace = doba_prace;
    }

    /**
     * @return the doba_prace_dny
     */
    public int getDoba_prace_dny() {
        return doba_prace_dny;
    }

    /**
     * @param doba_prace_dny the doba_prace_dny to set
     */
    public void setDoba_prace_dny(int doba_prace_dny) {
        this.doba_prace_dny = doba_prace_dny;
    }

    /**
     * @return the currentTime
     */
    public Timestamp getCurrentTime() {
        return currentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(Timestamp current) {
        this.currentTime = current;
    }
}
