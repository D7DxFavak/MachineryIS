/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

/**
 *
 * @author Favak
 */
public class TridaTypPolotovar1 {

    private int idMaterial;
    private String nazev;
    private String zkratka;
    private String poznamky;
    private int priorita;

    public TridaTypPolotovar1() {
        idMaterial = 0;
        nazev = "";
        zkratka = "";
        poznamky = "";
        priorita = 0;
    }

    /**
     * @return the idMaterial
     */
    public int getIdMaterial() {
        return idMaterial;
    }

    /**
     * @param idMaterial the idMaterial to set
     */
    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
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
     * @return the norma
     */
    public String getZkratka() {
        return zkratka;
    }

    /**
     * @param norma the norma to set
     */
    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
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
     * @return the priorita
     */
    public int getPriorita() {
        return priorita;
    }

    /**
     * @param priorita the priorita to set
     */
    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    
}
