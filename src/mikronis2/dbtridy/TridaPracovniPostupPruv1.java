/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;
import mikronis2.tridy.DvojiceCisloCislo;

/**
 *
 * @author Favak
 */
public class TridaPracovniPostupPruv1 {
    
    private int id;
    private int poradi;
    private int idPruvodka;
    private int idDruhStroje;
    private String popis;
    private String poznamka;

    public TridaPracovniPostupPruv1() {
        id = 0;
        poradi = 0;
        idPruvodka = 0;
        idDruhStroje = 0;
        popis = "";
        poznamka = "";
    }
    
     public TridaPracovniPostupPruv1(int id) {
         this.selectData(id);
        
    }
     
     public boolean selectData() {
        return selectData(this.id);
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT pracovni_postup_pruvodka_id, pracovni_postup_pruvodka_poradi, "
                    + "pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_druh_stroje_id, "
                    + "pracovni_postup_pruvodka_popis, pracovni_postup_poznamky "
                    + "FROM spolecne.pracovni_postup_pruvodka "
                    + "WHERE pracovni_postup_pruvodka_id = " + id + " ");
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                this.setIdPruvodka(SQLFunkceObecne.osetriCteniInt(q.getInt(3)));
                this.setIdDruhStroje(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                this.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(5)));                
                this.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(6)));            
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
            String dotaz = " UPDATE spolecne.pracovni_postup_pruvodka "
                    + "SET pracovni_postup_pruvodka_id = " + this.id + ", "
                    + "pracovni_postup_pruvodka_poradi = " + this.poradi + ", "
                    + "pracovni_postup_pruvodka_pruvodka_id = "+ this.idPruvodka + ", "           
                    + "pracovni_postup_pruvodka_druh_stroje_id = " + this.idDruhStroje + ", "
                    + "pracovni_postup_pruvodka_popis = " + TextFunkce1.osetriZapisTextDB1(this.popis) + ", "
                    + "pracovni_postup_poznamky = "+ TextFunkce1.osetriZapisTextDB1(this.poznamka) + " "
                    + "WHERE pracovni_postup_pruvodka_id = " + this.id + " "
                    + "AND pracovni_postup_pruvodka_pruvodka_id = " + this.poradi;

            //System.out.println("Update data : " + dotaz);
            r = SQLFunkceObecne2.update(dotaz);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return r;
        }
    }
    
    public long insertData() {
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(pruvodky_id)+1,1) FROM spolecne.pruvodky");
            while (id.next()) {
                this.id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.pracovni_postup_pruvodka("
                    + "pracovni_postup_pruvodka_id, pracovni_postup_pruvodka_poradi, "
                    + "pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_druh_stroje_id, "
                    + "pracovni_postup_pruvodka_popis, pracovni_postup_poznamky) "
                    + "VALUES (" + this.id + ", "
                    + this.poradi + ", "
                    + this.idPruvodka + ", "
                    + this.idDruhStroje + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.popis) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.poznamka) + ") ";
            int a = PripojeniDB.dotazIUD(dotaz);
        } catch (Exception e) {
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
     * @return the idPruvodka
     */
    public int getIdPruvodka() {
        return idPruvodka;
    }

    /**
     * @param idPruvodka the idPruvodka to set
     */
    public void setIdPruvodka(int idPruvodka) {
        this.idPruvodka = idPruvodka;
    }

    /**
     * @return the idDruhStroje
     */
    public int getIdDruhStroje() {
        return idDruhStroje;
    }

    /**
     * @param idDruhStroje the idDruhStroje to set
     */
    public void setIdDruhStroje(int idDruhStroje) {
        this.idDruhStroje = idDruhStroje;
    }

    /**
     * @return the popis
     */
    public String getPopis() {
        return popis;
    }

    /**
     * @param popis the popis to set
     */
    public void setPopis(String popis) {
        this.popis = popis;
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
    
    
}
