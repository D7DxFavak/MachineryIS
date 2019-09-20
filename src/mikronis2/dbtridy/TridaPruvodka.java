/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import mikronis2.PripojeniDB;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import cz.mikronplzen.dbfunkce.TextFunkce1;

/**
 *
 * @author Favak
 */
public class TridaPruvodka {

    private int id;
    private String nazev;
    private int idVykres;
    private Date terminDokonceni;
    private int pocetKusu;
    private String poznamky;
    private int pocetKusuSklad;
    private int pocetKusuPolotovar;
    private int idPolotovar;
    private int materialDelka;
    private int idObjednavky;
    private int vyrobenoKusu;
    private int narezanoZeSkladu;
    private boolean uzavrena;
    protected boolean odectena;    
    private String polotovarRozmer;
    private String celkovyCas;
    private TridaVykres1 tv1;
    private TridaObjednavka1 to1;
    private TridaPolotovar1 tp1;
    private ArrayList<TridaProgram1> arTProg1;
    private ArrayList<TridaPracovniPostupPruv1> arTPostup1;
    private TridaKooperace aktualniKooperace;
    private int poradi;
    protected java.text.DateFormat df = java.text.DateFormat.getDateInstance();

    public TridaPruvodka() {
        id = 0;
        idObjednavky = 0;
        idVykres = 0;
        idPolotovar = 0;
        pocetKusu = 0;
        pocetKusuPolotovar = 0;
        pocetKusuSklad = 10;
        materialDelka = 0;
        celkovyCas = "";
        polotovarRozmer = "";
        poznamky = "";
        terminDokonceni = null;
        vyrobenoKusu = 0;
        arTPostup1 = new ArrayList<TridaPracovniPostupPruv1>();
        arTProg1 = new ArrayList<TridaProgram1>();
        tv1 = null;
        to1 = null;
        tp1 = null;
        uzavrena = false;
        odectena = false;
        poradi = 0;
        aktualniKooperace = new TridaKooperace();
    }

    public TridaPruvodka(long id) {
        this.selectData(id);
    }

    public boolean selectData() {
        return selectData(this.id);
    }

    public boolean selectData(long id) {
         arTPostup1 = new ArrayList<TridaPracovniPostupPruv1>();
        arTProg1 = new ArrayList<TridaProgram1>();
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT pruvodky_id, pruvodky_nazev, pruvodky_cislo_vykresu, "
                    + "pruvodky_termin_dokonceni, pruvodky_pocet_kusu, pruvodky_poznamky, pruvodky_pocet_kusu_sklad, "
                    + "pruvodky_polotovar_pocet_kusu, pruvodky_polotovar_id, pruvodky_material_prumerna_delka, "
                    + "pruvodky_objednavky_id, pruvodky_vyrobeno_kusu, "
                    + "pruvodky_polotovar_rozmer, pruvodky_celkovy_cas, pruvodky_narezano_sklad, pruvodky_odectena "
                    + "FROM spolecne.pruvodky "
                    + "WHERE pruvodky_id = " + id);
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setNazev(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setIdVykres(SQLFunkceObecne.osetriCteniInt(q.getInt(3)));
                this.tv1 = new TridaVykres1(this.getIdVykres());
                this.setTerminDokonceni(q.getDate(4));
                this.setPocetKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(5)));
                this.setPoznamky(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                this.setPocetKusuSklad(SQLFunkceObecne.osetriCteniInt(q.getInt(7)));
                this.setPocetKusuPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(8)));
                this.setIdPolotovar(SQLFunkceObecne.osetriCteniInt(q.getInt(9)));
                this.setMaterialDelka(SQLFunkceObecne.osetriCteniInt(q.getInt(10)));
                this.setIdObjednavky(SQLFunkceObecne.osetriCteniInt(q.getInt(11)));
                this.to1 = new TridaObjednavka1(this.getIdObjednavky());
                this.setVyrobenoKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(12)));
                this.setPolotovarRozmer(SQLFunkceObecne.osetriCteniString(q.getString(13)));
                this.setCelkovyCas(SQLFunkceObecne.osetriCteniString(q.getString(14)));
                this.setNarezanoZeSkladu(SQLFunkceObecne.osetriCteniInt(q.getInt(15)));
                this.setOdectena(q.getBoolean(16));
            }

            q = PripojeniDB.dotazS("SELECT pracovni_postup_pruvodka_id, pracovni_postup_pruvodka_poradi, "
                    + "pracovni_postup_pruvodka_pruvodka_id, pracovni_postup_pruvodka_druh_stroje_id, "
                    + "pracovni_postup_pruvodka_popis, pracovni_postup_poznamky "
                    + "FROM spolecne.pracovni_postup_pruvodka "
                    + "WHERE pracovni_postup_pruvodka_pruvodka_id = " + id + " "
                    + "ORDER BY pracovni_postup_pruvodka_poradi");
            while (q.next()) {
                TridaPracovniPostupPruv1 tppp = new TridaPracovniPostupPruv1();
                tppp.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tppp.setPoradi(SQLFunkceObecne.osetriCteniInt(q.getInt(2)));
                tppp.setIdPruvodka(SQLFunkceObecne.osetriCteniInt(q.getInt(3)));
                tppp.setIdDruhStroje(SQLFunkceObecne.osetriCteniInt(q.getInt(4)));
                tppp.setPopis(SQLFunkceObecne.osetriCteniString(q.getString(5)));
                tppp.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                arTPostup1.add(tppp);
            }

            q = PripojeniDB.dotazS("SELECT programy_id, programy_cislo, programy_revize, programy_material_rozmer, "
                    + "programy_sestaveno_zamestnancem, programy_poznamky "
                    + "FROM spolecne.programy "
                    + "CROSS JOIN spolecne.vazba_pruvodky_programy "
                    + "WHERE vazba_pruvodky_programy_pruvodky_id = " + id + " "
                    + "AND vazba_pruvodky_programy_programy_id = programy_id "
                    + "ORDER BY programy_cislo");
            while (q.next()) {
                TridaProgram1 tp1 = new TridaProgram1();
                tp1.setId(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                tp1.setCislo(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                tp1.setRevize(SQLFunkceObecne.osetriCteniString(q.getString(3)));
                tp1.setMaterialRozmer(SQLFunkceObecne.osetriCteniString(q.getString(4)));
                tp1.setIdZamestnanec(SQLFunkceObecne.osetriCteniInt(q.getInt(5)));
                tp1.setPoznamka(SQLFunkceObecne.osetriCteniString(q.getString(6)));
                arTProg1.add(tp1);
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
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {

            String dotaz = " UPDATE spolecne.pruvodky "
                    + "SET pruvodky_id= " + this.id + ", "
                    + "pruvodky_nazev=" + TextFunkce1.osetriZapisTextDB1(this.nazev) + ", "
                    + "pruvodky_cislo_vykresu=" + this.idVykres + ", ";
            if (this.terminDokonceni != null) {
                dotaz += "pruvodky_termin_dokonceni=" + TextFunkce1.osetriZapisTextDB1(df.format(this.terminDokonceni)) + ", ";
            }
            dotaz += "pruvodky_pocet_kusu= " + this.pocetKusu + ", "
                    + "pruvodky_poznamky= " + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                    + "pruvodky_pocet_kusu_sklad= " + this.pocetKusuSklad + ", "
                    + "pruvodky_polotovar_pocet_kusu= " + this.pocetKusuPolotovar + ", "
                    + "pruvodky_polotovar_id= " + this.idPolotovar + ", "
                    + "pruvodky_material_prumerna_delka= " + this.materialDelka + ", "
                    + "pruvodky_objednavky_id= " + this.idObjednavky + ", "
                    + "pruvodky_vyrobeno_kusu= " + this.vyrobenoKusu + ", "
                    + "pruvodky_polotovar_rozmer= " + TextFunkce1.osetriZapisTextDB1(this.polotovarRozmer) + ", "
                    + "pruvodky_celkovy_cas= " + TextFunkce1.osetriZapisTextDB1(this.celkovyCas) + ", "
                    + "pruvodky_narezano_sklad = " + this.narezanoZeSkladu + ", "
                    + "pruvodky_odectena = " + this.odectena + " "
                    + "WHERE pruvodky_id = " + this.id;

            r = SQLFunkceObecne2.update(dotaz);

            for (int i = 0; i < arTPostup1.size(); i++) {              
                r = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_pracovni_postup_pruvodka_zamestnanci WHERE "
                        + "vazba_pracovni_postup_pruvodka_zamestnanci_postup_id = " + arTPostup1.get(i).getId());
            }

            r = PripojeniDB.dotazIUD("DELETE FROM spolecne.pracovni_postup_pruvodka WHERE "
                    + "pracovni_postup_pruvodka_pruvodka_id = " + this.id);

            r = PripojeniDB.dotazIUD("DELETE FROM spolecne.vazba_pruvodky_programy WHERE "
                    + "vazba_pruvodky_programy_pruvodky_id = " + this.id);
            
            for (int i = 0; i < arTPostup1.size(); i++) {
                arTPostup1.get(i).insertData();
            }

            for (int i = 0; i < arTProg1.size(); i++) {
                dotaz = "INSERT INTO spolecne.vazba_pruvodky_programy("
                        + "vazba_pruvodky_programy_pruvodky_id, vazba_pruvodky_programy_programy_id, "
                        + "vazba_pruvodky_programy_poznamky) "
                        + "VALUES(" + this.id + ", " + arTProg1.get(i).getId() + ", null)";
                r = PripojeniDB.dotazIUD(dotaz);
            }
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        } finally {
            return r;
        }
    }

    public long insertData() {
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {
            ResultSet id = PripojeniDB.dotazS("SELECT COALESCE(MAX(pruvodky_id)+1,1) FROM spolecne.pruvodky");
            while (id.next()) {
                this.id = id.getInt(1);
            }
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        }
        try {
            String dotaz = "INSERT INTO spolecne.pruvodky("
                    + "pruvodky_id, pruvodky_nazev, pruvodky_cislo_vykresu, pruvodky_termin_dokonceni, "
                    + "pruvodky_pocet_kusu, pruvodky_poznamky, pruvodky_pocet_kusu_sklad, "
                    + "pruvodky_polotovar_pocet_kusu, pruvodky_polotovar_id, pruvodky_material_prumerna_delka, "
                    + "pruvodky_objednavky_id, pruvodky_vyrobeno_kusu, "
                    + "pruvodky_polotovar_rozmer, pruvodky_celkovy_cas, pruvodky_odectena) "
                    + "VALUES (" + this.id + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.nazev) + ", "
                    + this.idVykres + ", "
                    + TextFunkce1.osetriZapisDatumDB1(this.terminDokonceni) + ", "
                    + this.pocetKusu + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.poznamky) + ", "
                    + this.pocetKusuSklad + ", "
                    + this.pocetKusuPolotovar + ", "
                    + this.idPolotovar + ", "
                    + this.materialDelka + ", "
                    + this.idObjednavky + ", "
                    + this.vyrobenoKusu + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.polotovarRozmer) + ", "
                    + TextFunkce1.osetriZapisTextDB1(this.celkovyCas) +", "
                    + this.odectena + ") ";
            rc = PripojeniDB.dotazIUD(dotaz);

            for (int i = 0; i < arTPostup1.size(); i++) {
                arTPostup1.get(i).insertData();
            }

            for (int i = 0; i < arTProg1.size(); i++) {
                dotaz = "INSERT INTO spolecne.vazba_pruvodky_programy("
                        + "vazba_pruvodky_programy_pruvodky_id, vazba_pruvodky_programy_programy_id, "
                        + "vazba_pruvodky_programy_poznamky) "
                        + "VALUES(" + this.id + ", " + arTProg1.get(i).getId() + ", null)";
                rc = PripojeniDB.dotazIUD(dotaz);
            }

            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            e.printStackTrace();
        } finally {
            return this.idObjednavky;
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
     * @return the idVykres
     */
    public int getIdVykres() {
        return idVykres;
    }

    /**
     * @param idVykres the idVykres to set
     */
    public void setIdVykres(int idVykres) {
        this.idVykres = idVykres;
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
     * @return the pocetKusuSklad
     */
    public int getPocetKusuSklad() {
        return pocetKusuSklad;
    }

    /**
     * @param pocetKusuSklad the pocetKusuSklad to set
     */
    public void setPocetKusuSklad(int pocetKusuSklad) {
        this.pocetKusuSklad = pocetKusuSklad;
    }

    /**
     * @return the pocetKusuPolotovar
     */
    public int getPocetKusuPolotovar() {
        return pocetKusuPolotovar;
    }

    /**
     * @param pocetKusuPolotovar the pocetKusuPolotovar to set
     */
    public void setPocetKusuPolotovar(int pocetKusuPolotovar) {
        this.pocetKusuPolotovar = pocetKusuPolotovar;
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
     * @return the materialDelka
     */
    public int getMaterialDelka() {
        return materialDelka;
    }

    /**
     * @param materialDelka the materialDelka to set
     */
    public void setMaterialDelka(int materialDelka) {
        this.materialDelka = materialDelka;
    }

    /**
     * @return the idObjednavky
     */
    public int getIdObjednavky() {
        return idObjednavky;
    }

    /**
     * @param idObjednavky the idObjednavky to set
     */
    public void setIdObjednavky(int idObjednavky) {
        this.idObjednavky = idObjednavky;
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
     * @return the to1
     */
    public TridaObjednavka1 getTo1() {
        return to1;
    }

    /**
     * @param to1 the to1 to set
     */
    public void setTo1(TridaObjednavka1 to1) {
        this.to1 = to1;
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
     * @return the arTProg1
     */
    public ArrayList<TridaProgram1> getArTProg1() {
        return arTProg1;
    }

    /**
     * @param arTProg1 the arTProg1 to set
     */
    public void setArTProg1(ArrayList<TridaProgram1> arTProg1) {
        this.arTProg1 = arTProg1;
    }

    /**
     * @return the arTPostup1
     */
    public ArrayList<TridaPracovniPostupPruv1> getArTPostup1() {
        return arTPostup1;
    }

    /**
     * @param arTPostup1 the arTPostup1 to set
     */
    public void setArTPostup1(ArrayList<TridaPracovniPostupPruv1> arTPostup1) {
        this.arTPostup1 = arTPostup1;
    }

    /**
     * @return the uzavrena
     */
    public boolean isUzavrena() {
        return uzavrena;
    }

    /**
     * @param uzavrena the uzavrena to set
     */
    public void setUzavrena(boolean uzavrena) {
        this.uzavrena = uzavrena;
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

    /**
     * @return the aktualniKooperace
     */
    public TridaKooperace getAktualniKooperace() {
        return aktualniKooperace;
    }

    /**
     * @param aktualniKooperace the aktualniKooperace to set
     */
    public void setAktualniKooperace(TridaKooperace aktualniKooperace) {
        this.aktualniKooperace = aktualniKooperace;
    }
}
