/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Favak
 */
public class TridaPracovniTransakce {
    
    private TridaPruvodka tPruvodka1;
    private int druhTransakce;
    private TridaZamestnanci1 tZamestnanec1;
    private Timestamp cas;
    private Timestamp aktualniCas;
    private Date datum;        

    public TridaPracovniTransakce() {
        tPruvodka1 = new TridaPruvodka();
        druhTransakce = 0;
        tZamestnanec1 = new TridaZamestnanci1();
        cas = null;
        aktualniCas = null;
        datum = null;
    }

    /**
     * @return the tPruvodka1
     */
    public TridaPruvodka gettPruvodka1() {
        return tPruvodka1;
    }

    /**
     * @param tPruvodka1 the tPruvodka1 to set
     */
    public void settPruvodka1(TridaPruvodka tPruvodka1) {
        this.tPruvodka1 = tPruvodka1;
    }

    /**
     * @return the druhTransakce
     */
    public int getDruhTransakce() {
        return druhTransakce;
    }

    /**
     * @param druhTransakce the druhTransakce to set
     */
    public void setDruhTransakce(int druhTransakce) {
        this.druhTransakce = druhTransakce;
    }

    /**
     * @return the tZamestnanec1
     */
    public TridaZamestnanci1 gettZamestnanec1() {
        return tZamestnanec1;
    }

    /**
     * @param tZamestnanec1 the tZamestnanec1 to set
     */
    public void settZamestnanec1(TridaZamestnanci1 tZamestnanec1) {
        this.tZamestnanec1 = tZamestnanec1;
    }

    /**
     * @return the cas
     */
    public Timestamp getCas() {
        return cas;
    }

    /**
     * @param cas the cas to set
     */
    public void setCas(Timestamp cas) {
        this.cas = cas;
    }

    /**
     * @return the aktualniCas
     */
    public Timestamp getAktualniCas() {
        return aktualniCas;
    }

    /**
     * @param aktualniCas the aktualniCas to set
     */
    public void setAktualniCas(Timestamp aktualniCas) {
        this.aktualniCas = aktualniCas;
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
    
    
    
}
