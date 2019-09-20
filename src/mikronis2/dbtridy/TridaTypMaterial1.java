/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class TridaTypMaterial1 {

    private int idMaterial;
    private int idSkupina;
    private String nazev;
    private String norma;
    private String poznamky;
    private int priorita;

    public TridaTypMaterial1() {
        idMaterial = 0;
        nazev = "";
        norma = "";
        poznamky = "";
        priorita = 0;
        idSkupina = 0;
    }
    
     public boolean selectData() {
        return selectData(this.idMaterial);
    }

    public boolean selectData(long id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT typ_materialu_id, typ_materialu_nazev, "
                    + "typ_materialu_norma, typ_materialu_poznamky, "
                    + "typ_materialu_priorita, typ_materialu_skupina "
                    + "FROM spolecne.typ_materialu; "
                    + "WHERE typ_materialu_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdMaterial(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setNorma(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                this.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                this.setPriorita(SQLFunkceObecne.osetriCteniInt(q.getInt(5)));
                this.setIdSkupina(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));             
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
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(typ_materialu_id),1) FROM spolecne.typ_materialu");
            while (id.next()) {
                idMaterial = id.getInt(1) + 1;
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            idMaterial = -1;
        }
        // TODO kontrola datumu
        try {
            String dotazInsert = "INSERT INTO spolecne.typ_materialu("
                    + "typ_materialu_id, typ_materialu_nazev, "
                    + "typ_materialu_norma, typ_materialu_poznamky, "
                    + "typ_materialu_priorita, typ_materialu_skupina) "
                    + "VALUES( " + idMaterial
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.nazev)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.norma)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.poznamky)
                    + ", " + this.priorita
                    + ", " + this.idSkupina
                    + ")";

            int a = PripojeniDB.dotazIUD(dotazInsert);
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání nového materiálu", "Prosím zkontrolujte zadaná data "
                    + "a opravte případné chyby");
        }
       
        rc = SQLFunkceObecne2.spustPrikaz("COMMIT");

        return idMaterial;
    }

    public int updateData() {
        int r = -10000;
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
       
        try {
            String dotazUpdate = "UPDATE spolecne.typ_materialu "
                    + "SET typ_materialu_nazev = " + TextFunkce1.osetriZapisTextDB1(this.nazev) + ", "
                    + "typ_materialu_norma = " + TextFunkce1.osetriZapisTextDB1(this.norma) + ", "
                    + "typ_materialu_poznamky = " + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                    + "typ_materialu_priorita = " + this.priorita + ", "
                    + "typ_materialu_skupina = " + this.idSkupina + " "                  
                    + "WHERE typ_materialu_id = " + this.idMaterial;           
           
            r = PripojeniDB.dotazIUD(dotazUpdate);           
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        } finally {
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            return r;
        }
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
    public String getNorma() {
        return norma;
    }

    /**
     * @param norma the norma to set
     */
    public void setNorma(String norma) {
        this.norma = norma;
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

    /**
     * @return the idSkupina
     */
    public int getIdSkupina() {
        return idSkupina;
    }

    /**
     * @param idSkupina the idSkupina to set
     */
    public void setIdSkupina(int idSkupina) {
        this.idSkupina = idSkupina;
    }

    
}
