/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class TridaSeznamNabidek1 {

    private int idSeznam;
    private int idZakaznik;
    private Date datumVystaveni;
    private String prijemce;
    private String predmet;
    private String veta;
    private String vystavitel;
    private String dataPDF;
    private int pocetNabidek;
    private ArrayList<TridaNabidka1> arNab1;

    public TridaSeznamNabidek1() {
        idSeznam = 0;
        idZakaznik = 0;
        datumVystaveni = null;
        prijemce = "";
        predmet = "";
        veta = "";
        vystavitel = "";
        arNab1 = new ArrayList<TridaNabidka1>();
    }

    public boolean selectData() {
        return selectData(this.idSeznam);
    }

    public boolean selectData(long id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT seznam_nabidek_id, "
                    + "seznam_nabidek_zakaznik_id, "
                    + "seznam_nabidek_datum, "
                    + "seznam_nabidek_komu, "
                    + "seznam_nabidek_predmet, "
                    + "seznam_nabidek_veta, "
                    + "seznam_nabidek_vystavil "
                    + "FROM spolecne.seznam_nabidek "
                    + "WHERE seznam_nabidek_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdSeznam(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                this.setDatumVystaveni(q.getDate(3));
                this.setPrijemce(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                this.setPredmet(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                this.setVeta(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                this.setVystavitel(SQLFunkceObecne.osetriCteniString(q.getString(7)));
            }
            return true;
        } catch (Exception e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }

    }

    public long insertData() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(seznam_nabidek_id)+1,1) FROM spolecne.seznam_nabidek");
            while (id.next()) {
                idSeznam = id.getInt(1);
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            idSeznam = -1;
        }
        // TODO kontrola datumu
        try {
            String dotazFakt = "INSERT INTO spolecne.seznam_nabidek("
                    + "seznam_nabidek_id, seznam_nabidek_zakaznik_id, seznam_nabidek_datum, "
                    + "seznam_nabidek_komu, seznam_nabidek_predmet, seznam_nabidek_veta, "
                    + "seznam_nabidek_vystavil) "
                    + "VALUES( " + idSeznam + ", " + idZakaznik
                    + ", " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(datumVystaveni))
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.prijemce)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.predmet)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.veta)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.vystavitel)
                    + ")";

            int a = PripojeniDB.dotazIUD(dotazFakt);
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání seznamu nabídek", "Prosím zkontrolujte zadaná data "
                    + "a opravte případné chyby");
        }

        for (int objIndex = 0; objIndex < arNab1.size(); objIndex++) {
            try {
                TridaNabidka1 tNab1 = (TridaNabidka1) arNab1.get(objIndex);

                int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_seznam_nabidky("
                        + "vazba_seznam_nabidky_seznam_id, vazba_seznam_nabidky_nabidky_id, "
                        + "vazba_seznam_nabidky_poznamky, vazba_seznam_nabidky_datum, vazba_seznam_nabidky_poradi)) "
                        + "VALUES( " + idSeznam + ", " + tNab1.getIdNabidky() + ", " + ", null, "
                        + TextFunkce1.osetriZapisTextDB1(dateFormat.format(tNab1.getDatum())) + ", " + tNab1.getPoradi() + ")");

            } catch (Exception e) {
                rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
                JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání seznamu nabídek", "Prosím zkontrolujte zadaná data "
                        + "a opravte případné chyby");
            }
        }
        rc = SQLFunkceObecne2.spustPrikaz("COMMIT");

        return idSeznam;
    }

    public int updateData() {
        int r = -10000;
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date date = new java.util.Date();
        try {
            String dotazFakt = "UPDATE spolecne.seznam_nabidek "
                    + "SET seznam_nabidek_zakaznik_id = " + this.idZakaznik + ", "
                    + "seznam_nabidek_datum = " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(datumVystaveni)) + ", "
                    + "seznam_nabidek_komu = " + TextFunkce1.osetriZapisTextDB1(this.prijemce) + ", "
                    + "seznam_nabidek_predmett = " + TextFunkce1.osetriZapisTextDB1(this.predmet) + ", "
                    + "seznam_nabidek_veta = " + TextFunkce1.osetriZapisTextDB1(this.veta) + ", "
                    + "seznam_nabidek_vystavil = " + TextFunkce1.osetriZapisTextDB1(this.vystavitel) + " "         
                    + "WHERE seznam_nabidek_id = " + this.idSeznam;           
           
            r = PripojeniDB.dotazIUD(dotazFakt);

            r = PripojeniDB.dotazIUD(
                    "DELETE FROM spolecne.vazba_seznam_nabidky "
                    + "WHERE vazba_seznam_nabidky_seznam_id = " + this.idSeznam);
            for (int objIndex = 0; objIndex < arNab1.size(); objIndex++) {
                try {
                   TridaNabidka1 tNab1 = (TridaNabidka1) arNab1.get(objIndex);

                int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_seznam_nabidky("
                        + "vazba_seznam_nabidky_seznam_id, vazba_seznam_nabidky_nabidky_id, "
                        + "vazba_seznam_nabidky_poznamky, vazba_seznam_nabidky_datum, vazba_seznam_nabidky_poradi)) "
                        + "VALUES( " + idSeznam + ", " + tNab1.getIdNabidky() + ", " + ", null, "
                        + TextFunkce1.osetriZapisTextDB1(dateFormat.format(tNab1.getDatum())) + ", " + tNab1.getPoradi() + ")");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        } finally {
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            return r;
        }
    }

    /**
     * @return the idSeznam
     */
    public int getIdSeznam() {
        return idSeznam;
    }

    /**
     * @param idSeznam the idSeznam to set
     */
    public void setIdSeznam(int idSeznam) {
        this.idSeznam = idSeznam;
    }

    /**
     * @return the datumVystaveni
     */
    public Date getDatumVystaveni() {
        return datumVystaveni;
    }

    /**
     * @param datumVystaveni the datumVystaveni to set
     */
    public void setDatumVystaveni(Date datumVystaveni) {
        this.datumVystaveni = datumVystaveni;
    }

    /**
     * @return the prijemce
     */
    public String getPrijemce() {
        return prijemce;
    }

    /**
     * @param prijemce the prijemce to set
     */
    public void setPrijemce(String prijemce) {
        this.prijemce = prijemce;
    }

    /**
     * @return the predmet
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     * @param predmet the predmet to set
     */
    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    /**
     * @return the veta
     */
    public String getVeta() {
        return veta;
    }

    /**
     * @param veta the veta to set
     */
    public void setVeta(String veta) {
        this.veta = veta;
    }

    /**
     * @return the dataPDF
     */
    public String getDataPDF() {
        return dataPDF;
    }

    /**
     * @param dataPDF the dataPDF to set
     */
    public void setDataPDF(String dataPDF) {
        this.dataPDF = dataPDF;
    }

    /**
     * @return the pocetNabidek
     */
    public int getPocetNabidek() {
        return pocetNabidek;
    }

    /**
     * @param pocetNabidek the pocetNabidek to set
     */
    public void setPocetNabidek(int pocetNabidek) {
        this.pocetNabidek = pocetNabidek;
    }

    /**
     * @return the arNab1
     */
    public ArrayList<TridaNabidka1> getArNab1() {
        return arNab1;
    }

    /**
     * @param arNab1 the arNab1 to set
     */
    public void setArNab1(ArrayList<TridaNabidka1> arNab1) {
        this.arNab1 = arNab1;
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
     * @return the vystavitel
     */
    public String getVystavitel() {
        return vystavitel;
    }

    /**
     * @param vystavitel the vystavitel to set
     */
    public void setVystavitel(String vystavitel) {
        this.vystavitel = vystavitel;
    }
}
