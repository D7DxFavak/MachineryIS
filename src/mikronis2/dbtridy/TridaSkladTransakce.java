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
public class TridaSkladTransakce {
    private long idTransakce;
    private String rozmer;
    private int idDruhTransakce;
    private String popisTransakce;
    private BigDecimal pocetKusu;
    private Date datum;
    private String popisPruvodkaVykres;

    public TridaSkladTransakce() {
        idDruhTransakce = 0;
        idTransakce = 0;
        rozmer = "";
        popisPruvodkaVykres = "";
        popisTransakce = "";
        datum = null;
        pocetKusu = new BigDecimal(0);        
    }    
      
    /**
     * @return the idTransakce
     */
    public long getIdTransakce() {
        return idTransakce;
    }

    /**
     * @param idTransakce the idTransakce to set
     */
    public void setIdTransakce(long idTransakce) {
        this.idTransakce = idTransakce;
    }

    /**
     * @return the rozmer
     */
    public String getRozmer() {
        return rozmer;
    }

    /**
     * @param rozmer the rozmer to set
     */
    public void setRozmer(String rozmer) {
        this.rozmer = rozmer;
    }

    /**
     * @return the idDruhTransakce
     */
    public int getIdDruhTransakce() {
        return idDruhTransakce;
    }

    /**
     * @param idDruhTransakce the idDruhTransakce to set
     */
    public void setIdDruhTransakce(int idDruhTransakce) {
        this.idDruhTransakce = idDruhTransakce;
    }

    /**
     * @return the popisTransakce
     */
    public String getPopisTransakce() {
        return popisTransakce;
    }

    /**
     * @param popisTransakce the popisTransakce to set
     */
    public void setPopisTransakce(String popisTransakce) {
        this.popisTransakce = popisTransakce;
    }

    /**
     * @return the pocetKusu
     */
    public BigDecimal getPocetKusu() {
        return pocetKusu;
    }

    /**
     * @param pocetKusu the pocetKusu to set
     */
    public void setPocetKusu(BigDecimal pocetKusu) {
        this.pocetKusu = pocetKusu;
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
     * @return the popisPruvodkaVykres
     */
    public String getPopisPruvodkaVykres() {
        return popisPruvodkaVykres;
    }

    /**
     * @param popisPruvodkaVykres the popisPruvodkaVykres to set
     */
    public void setPopisPruvodkaVykres(String popisPruvodkaVykres) {
        this.popisPruvodkaVykres = popisPruvodkaVykres;
    }
}
