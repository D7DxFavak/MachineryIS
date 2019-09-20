/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
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
public class TridaPolotovar1 {

    private int idPolotovar;
    private int typMaterialu;
    private int typPolotovaru;
    private int pocetKusu;
    protected int pruvodkyKusu;
    private String nazev;
    private String mernaHmotnost;
    private String poznamky;
    private boolean aktivni;
    private TridaTypMaterial1 tTM1;
    private TridaTypPolotovar1 tTP1;

    public TridaPolotovar1() {
        idPolotovar = 0;
        typMaterialu = 0;
        typPolotovaru = 0;
        mernaHmotnost = "";
        poznamky = "";
        nazev = "";
        aktivni = true;
    }

    public TridaPolotovar1(long id) {
        this.selectData(id);
    }

    public boolean selectData(long id) {
        try {
            System.out.println("SELECT polotovary_id, polotovary_nazev, "
                    + "polotovary_typ_materialu, polotovary_typ_polotovaru, "
                    + "polotovary_poznamky, polotovary_merna_hmotnost, polotovary_aktivni "
                    + "FROM spolecne.polotovary "
                    + "WHERE polotovary_id = " + id);
            ResultSet q = PripojeniDB.dotazS("SELECT polotovary_id, polotovary_nazev, "
                    + "polotovary_typ_materialu, polotovary_typ_polotovaru, "
                    + "polotovary_poznamky, polotovary_merna_hmotnost, polotovary_aktivni, p.alokovano "
                    + "FROM spolecne.polotovary "
                    + "LEFT JOIN (SELECT SUM(pruvodky_material_prumerna_delka * pruvodky_polotovar_pocet_kusu) AS alokovano, pruvodky_polotovar_id "
                    + "FROM spolecne.pruvodky GROUP BY pruvodky_polotovar_id ) AS p "
                    + "ON p.pruvodky_polotovar_id = polotovary_id "
                    + "WHERE polotovary_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setTypMaterialu(SQLFunkceObecne.osetriCteniInt(q.getInt(3)));
                this.setTypPolotovaru(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                this.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                this.setMernaHmotnost(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                this.setAktivni(q.getBoolean(7));
                this.setPruvodkyKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
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
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(polotovary_id),1) FROM spolecne.polotovary");
            while (id.next()) {
                idPolotovar = id.getInt(1) + 1;
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            idPolotovar = -1;
        }
        // TODO kontrola datumu
        try {
            String dotazInsert = "INSERT INTO spolecne.polotovary("
                    + "polotovary_id, polotovary_nazev, polotovary_typ_materialu, "
                    + "polotovary_typ_polotovaru, polotovary_poznamky, polotovary_merna_hmotnost, "
                    + "polotovary_aktivni) "
                    + "VALUES( " + idPolotovar
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.nazev)
                    + ", " + this.typMaterialu
                    + ", " + this.typPolotovaru
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.poznamky)
                    + ", " + TextFunkce1.osetriZapisTextDB1(this.mernaHmotnost)
                    + ", " + this.isAktivni()
                    + ")";
            System.out.println(dotazInsert);
            int a = PripojeniDB.dotazIUD(dotazInsert);
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání nového rozměru", "Prosím zkontrolujte zadaná data "
                    + "a opravte případné chyby");
        }

        rc = SQLFunkceObecne2.spustPrikaz("COMMIT");

        return idPolotovar;
    }

    /**
     * @return the idPolotovar
     */
    public int getIdPolotovar() {
        return idPolotovar;
    }

    /**
     * @param idPolotovar the idPolotovar to set
     */
    public void setIdPolotovar(int idPolotovar) {
        this.idPolotovar = idPolotovar;
    }

    /**
     * @return the typMaterialu
     */
    public int getTypMaterialu() {
        return typMaterialu;
    }

    /**
     * @param typMaterialu the typMaterialu to set
     */
    public void setTypMaterialu(int typMaterialu) {
        this.typMaterialu = typMaterialu;
    }

    /**
     * @return the typPolotovaru
     */
    public int getTypPolotovaru() {
        return typPolotovaru;
    }

    /**
     * @param typPolotovaru the typPolotovaru to set
     */
    public void setTypPolotovaru(int typPolotovaru) {
        this.typPolotovaru = typPolotovaru;
    }

    /**
     * @return the mernaHmotnost
     */
    public String getMernaHmotnost() {
        return mernaHmotnost;
    }

    /**
     * @param mernaHmotnost the mernaHmotnost to set
     */
    public void setMernaHmotnost(String mernaHmotnost) {
        this.mernaHmotnost = mernaHmotnost;
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
     * @return the aktivni
     */
    public boolean isAktivni() {
        return aktivni;
    }

    /**
     * @param aktivni the aktivni to set
     */
    public void setAktivni(boolean aktivni) {
        this.aktivni = aktivni;
    }

    /**
     * @return the tTM1
     */
    public TridaTypMaterial1 gettTM1() {
        return tTM1;
    }

    /**
     * @param tTM1 the tTM1 to set
     */
    public void settTM1(TridaTypMaterial1 tTM1) {
        this.tTM1 = tTM1;
    }

    /**
     * @return the tTP1
     */
    public TridaTypPolotovar1 gettTP1() {
        return tTP1;
    }

    /**
     * @param tTP1 the tTP1 to set
     */
    public void settTP1(TridaTypPolotovar1 tTP1) {
        this.tTP1 = tTP1;
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
     * @return the pruvodkyKusu
     */
    public int getPruvodkyKusu() {
        return pruvodkyKusu;
    }

    /**
     * @param pruvodkyKusu the pruvodkyKusu to set
     */
    public void setPruvodkyKusu(int pruvodkyKusu) {
        this.pruvodkyKusu = pruvodkyKusu;
    }
}
