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
public class TridaProgram1 {

    private int id;
    private int idZamestnanec;
    private String cislo;
    private String revize;
    private String materialRozmer;
    private String poznamka;

    public TridaProgram1() {
        id = 0;
        idZamestnanec = 0;
        materialRozmer = "";
        poznamka = "";
        cislo = "";
        revize = "";
    }

    public TridaProgram1(int id) {
        this.selectData(id);

    }

    public boolean selectData() {
        return selectData(this.id);
    }

    public boolean selectData(int id) {
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT programy_id, programy_cislo, programy_revize, programy_material_rozmer, "
                    + "programy_sestaveno_zamestnancem, programy_poznamky "
                    + "FROM spolecne.programy "
                    + "WHERE programy_id = " + id + " ");
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setCislo(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setRevize(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                this.setMaterialRozmer(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                this.setIdZamestnanec(SQLFunkceObecne.osetriCteniInt(q.getInt(5)));
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
            String dotaz = " UPDATE spolecne.programy "
                    + "SET programy_id = " + this.id + ", "
                    + "programy_cislo = " + TextFunkce1.osetriZapisTextDB1(this.cislo) + ", "
                    + "programy_revize = " + TextFunkce1.osetriZapisTextDB1(this.revize) + ", "
                    + "programy_material_rozmer = " + TextFunkce1.osetriZapisTextDB1(this.materialRozmer) + ", "
                    + "programy_sestaveno_zamestnancem = " + 50000001 + ", "
                    + "programy_poznamky = " + TextFunkce1.osetriZapisTextDB1(this.poznamka) + " "
                    + "WHERE programy_id = " + this.id + " "
                    + "AND pracovni_postup_pruvodka_pruvodka_id = " + this.id;

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
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(programy_id)+1,1) FROM spolecne.programy");
            while (id.next()) {
                this.id = id.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.programy("
                    + "programy_id, programy_cislo, programy_revize, programy_material_rozmer, "
                    + "programy_sestaveno_zamestnancem, programy_cas_sestaveni, programy_poznamky) "
                    + "VALUES (" + this.id + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.cislo) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.revize) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.materialRozmer) + ", "
                    + 50000001 + ", "
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
     * @return the idZamestnanec
     */
    public int getIdZamestnanec() {
        return idZamestnanec;
    }

    /**
     * @param idZamestnanec the idZamestnanec to set
     */
    public void setIdZamestnanec(int idZamestnanec) {
        this.idZamestnanec = idZamestnanec;
    }

    /**
     * @return the cislo
     */
    public String getCislo() {
        return cislo;
    }

    /**
     * @param cislo the cislo to set
     */
    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    /**
     * @return the revize
     */
    public String getRevize() {
        return revize;
    }

    /**
     * @param revize the revize to set
     */
    public void setRevize(String revize) {
        this.revize = revize;
    }

    /**
     * @return the materialRozmer
     */
    public String getMaterialRozmer() {
        return materialRozmer;
    }

    /**
     * @param materialRozmer the materialRozmer to set
     */
    public void setMaterialRozmer(String materialRozmer) {
        this.materialRozmer = materialRozmer;
    }
}
