/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mikronis2.dbtridy;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class TridaFaktura1 {

    private int idFaktury;
    private String cisloFaktury;
    private int idZakaznik;
    private Date datumVystaveni;
    private Date datumPlneni;
    private Date datumSplatnosti;
    private int idLieferschein;
    private boolean fixovana;
    private String cena;
    private int pocetPolozek;
    private int pocetKusu;
    private String cistaVaha;
    private String hrubaVaha;
    private String cisloAwb;
    private String comCode;
    private String dopravce;
    private boolean deklarovana;
    private String stavFaktury;
    private int idAdresa;
    private String adresa;
    private String mesto;
    private String psc;
    private String stat;
    private String kontakt;
    private String kontaktTelefon;
    private String adresaFakturace;
    private String mestoFakturace;
    private String pscFakturace;
    private String statFakturace;
    private ArrayList<TridaObjednavka1> arTObj1;
    private ArrayList<TridaDodatek1> arDodatek1;
    private String cisloLieferschein;
    private String pdf;
    private String vystavitel;
    private String nazev1Radek;
    private String nazev2Radek;
    private String zakaznikJmenoFirmy;
    private String vatNr;
    private String jazyk;

    public boolean selectData() {
        return selectData(this.idFaktury);
    }

    public boolean selectData(long id) {
        java.text.NumberFormat nf2 = java.text.NumberFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
        try {
            ResultSet q = PripojeniDB.dotazS("SELECT faktury_id, faktura_cislo_faktury, faktury_zakaznik_id, faktury_datum_vystaveni, " //1 - 4
                    + "faktury_datum_plneni, faktury_lieferschein_nr, faktury_fixovana, " // 5 - 7
                    + "faktury_cena, faktury_polozky, faktury_pocet_kusu, " // 8 - 10
                    + "faktury_cista_vaha, faktury_hruba_vaha, faktury_cislo_awb, faktury_com_code, " //11 - 14
                    + "faktury_dopravce, faktury_deklarovana, faktury_dodaci_adresa, "
                    + "faktury_kontakt, faktury_kontakt_telefon, faktury_adresa, faktury_mesto, "
                    + "faktury_psc, faktury_stat, faktury_adresa_fakturace, faktury_mesto_fakturace, "
                    + "faktury_psc_fakturace, faktury_stat_fakturace, "
                    + "COALESCE(faktury_vystavil, 'Stanislav Kaprál') as vystavitel, "
                    + "faktury_1radek, faktury_2radek, faktury_zakaznik_jmeno, l.lieferscheiny_cislo_lieferscheinu, "
                    + "CASE WHEN faktury_fixovana IS FALSE THEN (obo.cena_celkem + COALESCE(dod.dodatek_celkem,0))"
                    + "ELSE (obf.cena_celkem + COALESCE(dod.dodatek_celkem,0)) END AS cena_celkem,"
                    + "COALESCE(faktury_datum_splatnosti, faktury_datum_vystaveni), faktury_vat, faktury_jazyk "
                    + "FROM spolecne.faktury "
                    + "LEFT JOIN "
                    + "(SELECT lieferscheiny_id, lieferscheiny_cislo_lieferscheinu "
                    + "FROM spolecne.lieferscheiny) AS l "
                    + "ON l.lieferscheiny_id = faktury_lieferschein_nr "
                    + "LEFT JOIN (SELECT vazba_faktury_objednavky_faktury_id, "
                    + "SUM(objednavky_pocet_objednanych_kusu * objednavky_cena_za_kus) AS cena_celkem "
                    + "FROM spolecne.vazba_faktury_objednavky "
                    + "CROSS JOIN spolecne.objednavky "
                    + "WHERE objednavky_id = vazba_faktury_objednavky_objednavky_id "
                    + "GROUP BY vazba_faktury_objednavky_faktury_id) obo "
                    + "ON obo.vazba_faktury_objednavky_faktury_id = faktury_id "
                    + "LEFT JOIN (SELECT COALESCE(SUM(vazba_faktury_dodatky_cena_za_kus * vazba_faktury_dodatky_pocet),0) as dodatek_celkem, vazba_faktury_dodatky_faktury_id "
                    + "FROM spolecne.vazba_faktury_dodatky "
                    + "GROUP BY vazba_faktury_dodatky_faktury_id) dod "
                    + "ON dod.vazba_faktury_dodatky_faktury_id = faktury_id "
                    + "LEFT JOIN (SELECT faktury_polozky_fix_id, "
                    + "SUM(faktury_polozky_fix_kusu * faktury_polozky_fix_cena_za_kus) AS cena_celkem "
                    + "FROM spolecne.faktury_polozky_fix "
                    + "GROUP BY faktury_polozky_fix_id) obf "
                    + "ON obf.faktury_polozky_fix_id = faktury_id "
                    + "WHERE faktury_id = " + id);          
            q.last();
            if (q.getRow() == 1) {
                q.first();
                this.setIdFaktury(SQLFunkceObecne.osetriCteniInt(q.getInt(1)));
                this.setCisloFaktury(SQLFunkceObecne.osetriCteniString(q.getString(2)));
                this.setIdZakaznik(SQLFunkceObecne.osetriCteniInt(q.getInt(3)));
                this.setDatumVystaveni(q.getDate(4));
                this.setDatumPlneni(q.getDate(5));
                this.setIdLieferschein(SQLFunkceObecne.osetriCteniInt(q.getInt(6)));
                this.setFixovana(q.getBoolean(7));
                this.setCena(SQLFunkceObecne.osetriCteniString(q.getString(8)));
                this.setPocetPolozek(SQLFunkceObecne.osetriCteniInt(q.getInt(9)));
                this.setPocetKusu(SQLFunkceObecne.osetriCteniInt(q.getInt(10)));
                this.setCistaVaha(SQLFunkceObecne.osetriCteniString(q.getString(11)));
                this.setHrubaVaha(SQLFunkceObecne.osetriCteniString(q.getString(12)));
                this.setCisloAwb(SQLFunkceObecne.osetriCteniString(q.getString(13)));
                this.setComCode(SQLFunkceObecne.osetriCteniString(q.getString(14)));
                this.setDopravce(SQLFunkceObecne.osetriCteniString(q.getString(15)));
                this.setDeklarovana(q.getBoolean(16));
                this.setIdAdresa(SQLFunkceObecne.osetriCteniInt(q.getInt(17)));
                this.setKontakt(SQLFunkceObecne.osetriCteniString(q.getString(18)));
                this.setKontaktTelefon(SQLFunkceObecne.osetriCteniString(q.getString(19)));
                this.setAdresa(SQLFunkceObecne.osetriCteniString(q.getString(20)));
                this.setMesto(SQLFunkceObecne.osetriCteniString(q.getString(21)));
                this.setPsc(SQLFunkceObecne.osetriCteniString(q.getString(22)));
                this.setStat(SQLFunkceObecne.osetriCteniString(q.getString(23)));
                this.setAdresaFakturace(SQLFunkceObecne.osetriCteniString(q.getString(24)));
                this.setMestoFakturace(SQLFunkceObecne.osetriCteniString(q.getString(25)));
                this.setPscFakturace(SQLFunkceObecne.osetriCteniString(q.getString(26)));
                this.setStatFakturace(SQLFunkceObecne.osetriCteniString(q.getString(27)));
                this.setVystavitel(SQLFunkceObecne.osetriCteniString(q.getString(28)));
                this.setNazev1Radek(SQLFunkceObecne.osetriCteniString(q.getString(29)));
                this.setNazev2Radek(SQLFunkceObecne.osetriCteniString(q.getString(30)));
                this.setZakaznikJmenoFirmy(SQLFunkceObecne.osetriCteniString(q.getString(31)));
                this.setCisloLieferschein(SQLFunkceObecne.osetriCteniString(q.getString(32)));
                try {
                    this.setCena(nf2.format(nf2.parse(((q.getString(33) == null) ? "" : q.getString(33)).replace(".", ",")))); // celkova suma                                 
                } catch (Exception e) {
                    this.setCena(((q.getString(33) == null) ? "" : q.getString(33)));
                }
                this.setDatumSplatnosti(q.getDate(34));
                this.setVatNr(SQLFunkceObecne.osetriCteniString(q.getString(35)));
                this.setJazyk(SQLFunkceObecne.osetriCteniString(q.getString(36)));
            }           
           
            return true;
        } catch (SQLException e) {
            PripojeniDB.vyjimkaS(e);
            return false;
        } finally {
            PripojeniDB.zavriPrikaz();
        }
    }

    public long insertData() {
        boolean existuje = false;

        try {
            ResultSet exist = PripojeniDB.dotazS("SELECT EXISTS "
                    + "(SELECT faktury_id FROM spolecne.faktury WHERE faktura_cislo_faktury = "
                    + TextFunkce1.osetriZapisTextDB1(this.cisloFaktury) + ")");
            while (exist.next()) {
                existuje = exist.getBoolean(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            existuje = false;
        }
        // System.out.println("existuje : " + existuje);
        if (existuje == false) {
            int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date date = new java.util.Date();

            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(faktury_id) FROM spolecne.faktury");
                while (id.next()) {
                    this.idFaktury = id.getInt(1) + 1;
                }
            } catch (Exception e) {
                rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                this.idFaktury = -1;
            }
            String idsAdresa = "null";
            if(idAdresa != 0 ) {
                idsAdresa = idAdresa +"";
            }
            // TODO kontrola datumu
            try {
                String dotazFakt = "INSERT INTO spolecne.faktury("
                        + "faktury_id, faktura_cislo_faktury, faktury_zakaznik_id, faktury_datum_vystaveni, "
                        + "faktury_datum_plneni, faktury_lieferschein_nr, faktury_pocet_kusu, "
                        + "faktury_cista_vaha, faktury_hruba_vaha, faktury_cislo_awb, faktury_com_code, "
                        + "faktury_dopravce, faktury_deklarovana, faktury_dodaci_adresa, "
                        + "faktury_kontakt, faktury_kontakt_telefon, faktury_adresa, faktury_mesto, "
                        + "faktury_psc, faktury_stat, faktury_adresa_fakturace, faktury_mesto_fakturace, "
                        + "faktury_psc_fakturace, faktury_stat_fakturace, faktury_vystavil, "
                        + "faktury_1radek, faktury_2radek, faktury_zakaznik_jmeno, faktury_datum_splatnosti, faktury_vat, faktury_jazyk) "
                        + "VALUES( " + this.idFaktury + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.cisloFaktury) + ", "
                        + this.idZakaznik + ", "
                        + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumVystaveni)) + ", "
                        + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumPlneni)) + ", "
                        + this.idLieferschein + ", "
                        + this.pocetKusu + ", "
                        + TextFunkce1.osetriZapisNumericDB1(this.cistaVaha) + ", "
                        + TextFunkce1.osetriZapisNumericDB1(this.hrubaVaha) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.cisloAwb) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.comCode) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.dopravce) + ", "
                        + this.deklarovana + ", "
                        + idsAdresa + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.kontakt) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.kontaktTelefon) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.adresa) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.mesto) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.psc) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.stat) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.adresaFakturace) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.mestoFakturace) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.pscFakturace) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.statFakturace) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.vystavitel) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.nazev1Radek) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.nazev2Radek) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.zakaznikJmenoFirmy) + ", "
                        + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumSplatnosti)) + ", "
                        + TextFunkce1.osetriZapisTextDB1(this.getVatNr())+ ", "
                        + TextFunkce1.osetriZapisTextDB1(this.getJazyk())+ ")";
              //  System.out.println("Dotaz fakt : " + dotazFakt);
                int a = PripojeniDB.dotazIUD(dotazFakt);
            } catch (SQLException e) {
                rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                e.printStackTrace();
            }

            for (int objIndex = 0; objIndex < arTObj1.size(); objIndex++) {
                try {

                    int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_faktury_objednavky("
                            + "vazba_faktury_objednavky_faktury_id, "
                            + "vazba_faktury_objednavky_objednavky_id, "
                            + "vazba_faktury_objednavky_poradi) "
                            + "VALUES( " + this.idFaktury + ", " + arTObj1.get(objIndex).getId() + ", "
                            + arTObj1.get(objIndex).getPoradi() + ")");
                } catch (SQLException e) {
                    rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                    e.printStackTrace();
                }
            }
            for(int dodIndex = 0; dodIndex < arDodatek1.size(); dodIndex++) {
                try {
                    TridaDodatek1 tDod1 = arDodatek1.get(dodIndex);
                    tDod1.setIdFaktura(this.idFaktury);
                    tDod1.setIdDodatek(dodIndex);
                    tDod1.insertData();
                    
                } catch (Exception e) {
                    rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                    e.printStackTrace();
                }
            }
            long idTransakce = 0;
            try {
                ResultSet id = PripojeniDB.dotazS("SELECT MAX(sklady_vyrobky_transakce_id) FROM logistika.sklady_vyrobky_transakce");
                while (id.next()) {
                    idTransakce = id.getLong(1) + 1;
                }

                int umisteni_id;
                for (int objIndex = 0; objIndex < arTObj1.size(); objIndex++) {
                    umisteni_id = 1;
                    String dotaz = "SELECT sklady_vyrobky_transakce_umisteni_id "
                            + "FROM logistika.sklady_vyrobky_transakce "
                            + "WHERE sklady_vyrobky_transakce_vykres_cislo = "
                            + TextFunkce1.osetriZapisTextDB1(arTObj1.get(objIndex).getTv1().getCislo()) + " ";
                    try {
                        ResultSet cisloRegalu = PripojeniDB.dotazS(dotaz);
                        while (cisloRegalu.next()) {
                            umisteni_id = cisloRegalu.getInt(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        int a = PripojeniDB.dotazIUD("INSERT INTO logistika.sklady_vyrobky_transakce("
                                + "sklady_vyrobky_transakce_id, "
                                + "sklady_vyrobky_transakce_sklad_id, "
                                + "sklady_vyrobky_transakce_umisteni_id, "
                                + "sklady_vyrobky_transakce_druh_id, "
                                + "sklady_vyrobky_transakce_pocet_mj, "
                                + "sklady_vyrobky_transakce_vykres_cislo) "
                                + "VALUES( " + idTransakce + ", 1, " + umisteni_id + ", "
                                + 100 + ", " + arTObj1.get(objIndex).getPocetObjednanychKusu()
                                + ", " + TextFunkce1.osetriZapisTextDB1(arTObj1.get(objIndex).getTv1().getCislo()) + ")");
                        idTransakce++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < arTObj1.size(); i++) {
                try {
                    String dotaz = "UPDATE spolecne.terminy "
                            + "SET terminy_cislo_terminu = 3 "
                            + "WHERE terminy_objednavky_id = " + arTObj1.get(i).getId();
                    int a = PripojeniDB.dotazIUD(dotaz);
                    dotaz = "UPDATE spolecne.objednavky "
                            + "SET objednavky_cislo_faktury = " + TextFunkce1.osetriZapisTextDB1(this.cisloFaktury) + " "
                            + "WHERE objednavky_id = " + arTObj1.get(i).getId();
                    a = PripojeniDB.dotazIUD(dotaz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
        } else {
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání faktury", "Faktura s daným číslem již existuje,"
                    + "změnte prosím číslo faktury");
        }
        return idFaktury;
    }

    public int updateData() {
        int r = -10000;
        int rc = SQLFunkceObecne2.spustPrikaz("BEGIN");
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date date = new java.util.Date();
            
            String idsAdresa = "null";
            if(idAdresa != 0 ) {
                idsAdresa = idAdresa +"";
            }
            
            String dotaz = "UPDATE spolecne.faktury "
                    + "SET faktura_cislo_faktury = " + TextFunkce1.osetriZapisTextDB1(this.cisloFaktury) + ", "
                    + "faktury_zakaznik_id = " + this.idZakaznik + ", "
                    + "faktury_datum_vystaveni = " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumVystaveni)) + ", "
                    + "faktury_datum_plneni = " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumPlneni)) + ", "
                    + "faktury_lieferschein_nr = " + this.idLieferschein + ", "
                    + "faktury_pocet_kusu = " + this.pocetKusu + ", "
                    + "faktury_cista_vaha = " + TextFunkce1.osetriZapisNumericDB1(this.cistaVaha) + ", "
                    + "faktury_hruba_vaha = " + TextFunkce1.osetriZapisNumericDB1(this.hrubaVaha) + ", "
                    + "faktury_cislo_awb = " + TextFunkce1.osetriZapisTextDB1(this.cisloAwb) + ", "
                    + "faktury_com_code = " + TextFunkce1.osetriZapisTextDB1(this.comCode) + ", "
                    + "faktury_dopravce = " + TextFunkce1.osetriZapisTextDB1(this.dopravce) + ", "
                    + "faktury_deklarovana = " + this.deklarovana + ", "
                    + "faktury_dodaci_adresa= " + idsAdresa + ", "
                    + "faktury_kontakt= " + TextFunkce1.osetriZapisTextDB1(this.kontakt) + ", "
                    + "faktury_kontakt_telefon= " + TextFunkce1.osetriZapisTextDB1(this.kontaktTelefon) + ", "
                    + "faktury_adresa= " + TextFunkce1.osetriZapisTextDB1(this.adresa) + ", "
                    + "faktury_mesto= " + TextFunkce1.osetriZapisTextDB1(this.mesto) + ", "
                    + "faktury_psc= " + TextFunkce1.osetriZapisTextDB1(this.psc) + ", "
                    + "faktury_stat= " + TextFunkce1.osetriZapisTextDB1(this.stat) + ", "
                    + "faktury_adresa_fakturace= " + TextFunkce1.osetriZapisTextDB1(this.adresaFakturace) + ", "
                    + "faktury_mesto_fakturace= " + TextFunkce1.osetriZapisTextDB1(this.mestoFakturace) + ", "
                    + "faktury_psc_fakturace= " + TextFunkce1.osetriZapisTextDB1(this.pscFakturace) + ", "
                    + "faktury_stat_fakturace= " + TextFunkce1.osetriZapisTextDB1(this.statFakturace) + ", "
                    + "faktury_vystavil= " + TextFunkce1.osetriZapisTextDB1(this.vystavitel) + ", "
                    + "faktury_1radek= " + TextFunkce1.osetriZapisTextDB1(this.nazev1Radek) + ", "
                    + "faktury_2radek= " + TextFunkce1.osetriZapisTextDB1(this.nazev2Radek) + ", "
                    + "faktury_zakaznik_jmeno= " + TextFunkce1.osetriZapisTextDB1(this.zakaznikJmenoFirmy) + ", "
                    + "faktury_datum_splatnosti = " + TextFunkce1.osetriZapisTextDB1(dateFormat.format(this.datumSplatnosti)) + ", "
                    + "faktury_vat = " + TextFunkce1.osetriZapisTextDB1(this.getVatNr()) + ", "
                    + "faktury_jazyk = " + TextFunkce1.osetriZapisTextDB1(this.getJazyk()) + " "
                    + "WHERE faktury_id = " + this.idFaktury;

            r = PripojeniDB.dotazIUD(dotaz);

            String dotazFaktDelete = "DELETE FROM spolecne.vazba_faktury_objednavky "
                        + "WHERE vazba_faktury_objednavky_faktury_id = " + this.idFaktury;

            r = PripojeniDB.dotazIUD(dotazFaktDelete);
            
            dotazFaktDelete = "DELETE FROM spolecne.vazba_faktury_dodatky "
                        + "WHERE vazba_faktury_dodatky_faktury_id = " + this.idFaktury;

            r = PripojeniDB.dotazIUD(dotazFaktDelete);

            for (int objIndex = 0; objIndex < arTObj1.size(); objIndex++) {
                try {

                    int a = PripojeniDB.dotazIUD("INSERT INTO spolecne.vazba_faktury_objednavky("
                            + "vazba_faktury_objednavky_faktury_id, "
                            + "vazba_faktury_objednavky_objednavky_id, "
                            + "vazba_faktury_objednavky_poradi) "
                            + "VALUES( " + this.idFaktury + ", " + arTObj1.get(objIndex).getId() + ", "
                            + arTObj1.get(objIndex).getPoradi() + ")");
                } catch (Exception e) {
                    rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                    e.printStackTrace();
                }
            }
            
            for(int dodIndex = 0; dodIndex < arDodatek1.size(); dodIndex++) {
                try {
                    TridaDodatek1 tDod1 = arDodatek1.get(dodIndex);
                    tDod1.setIdFaktura(this.idFaktury);
                    tDod1.setIdDodatek(dodIndex);
                    tDod1.insertData();
                    
                } catch (Exception e) {
                    rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            rc = SQLFunkceObecne2.spustPrikaz("ROLLBACK");
            JednoducheDialogy1.errAno(new JFrame(), "Chyba při ukládání dodacího listu", "Nepodařilo se uložit novou verzi dodacího listu, zůstane zachovaná stará verze!");
            e.printStackTrace();
        } finally {
            rc = SQLFunkceObecne2.spustPrikaz("COMMIT");
            return r;
        }
    }

    /**
     * @return the idfaktury
     */
    public int getIdFaktury() {
        return idFaktury;
    }

    /**
     * @param idfaktury the idfaktury to set
     */
    public void setIdFaktury(int idfaktury) {
        this.idFaktury = idfaktury;
    }

    /**
     * @return the cisloFaktury
     */
    public String getCisloFaktury() {
        return cisloFaktury;
    }

    /**
     * @param cisloFaktury the cisloFaktury to set
     */
    public void setCisloFaktury(String cisloFaktury) {
        this.cisloFaktury = cisloFaktury;
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
     * @return the datumPlneni
     */
    public Date getDatumPlneni() {
        return datumPlneni;
    }

    /**
     * @param datumPlneni the datumPlneni to set
     */
    public void setDatumPlneni(Date datumPlneni) {
        this.datumPlneni = datumPlneni;
    }

    /**
     * @return the idLieferschein
     */
    public int getIdLieferschein() {
        return idLieferschein;
    }

    /**
     * @param idLieferschein the idLieferschein to set
     */
    public void setIdLieferschein(int idLieferschein) {
        this.idLieferschein = idLieferschein;
    }

    /**
     * @return the fixovana
     */
    public boolean isFixovana() {
        return fixovana;
    }

    /**
     * @param fixovana the fixovana to set
     */
    public void setFixovana(boolean fixovana) {
        this.fixovana = fixovana;
    }

    /**
     * @return the cena
     */
    public String getCena() {
        return cena;
    }

    /**
     * @param cena the cena to set
     */
    public void setCena(String cena) {
        this.cena = cena;
    }

    /**
     * @return the pocetPolozek
     */
    public int getPocetPolozek() {
        return pocetPolozek;
    }

    /**
     * @param pocetPolozek the pocetPolozek to set
     */
    public void setPocetPolozek(int pocetPolozek) {
        this.pocetPolozek = pocetPolozek;
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
     * @return the cistaVaha
     */
    public String getCistaVaha() {
        return cistaVaha;
    }

    /**
     * @param cistaVaha the cistaVaha to set
     */
    public void setCistaVaha(String cistaVaha) {
        this.cistaVaha = cistaVaha;
    }

    /**
     * @return the hrubaVaha
     */
    public String getHrubaVaha() {
        return hrubaVaha;
    }

    /**
     * @param hrubaVaha the hrubaVaha to set
     */
    public void setHrubaVaha(String hrubaVaha) {
        this.hrubaVaha = hrubaVaha;
    }

    /**
     * @return the cisloAwb
     */
    public String getCisloAwb() {
        return cisloAwb;
    }

    /**
     * @param cisloAwb the cisloAwb to set
     */
    public void setCisloAwb(String cisloAwb) {
        this.cisloAwb = cisloAwb;
    }

    /**
     * @return the comCode
     */
    public String getComCode() {
        return comCode;
    }

    /**
     * @param comCode the comCode to set
     */
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    /**
     * @return the dopravce
     */
    public String getDopravce() {
        return dopravce;
    }

    /**
     * @param dopravce the dopravce to set
     */
    public void setDopravce(String dopravce) {
        this.dopravce = dopravce;
    }

    /**
     * @return the deklarovana
     */
    public boolean isDeklarovana() {
        return deklarovana;
    }

    /**
     * @param deklarovana the deklarovana to set
     */
    public void setDeklarovana(boolean deklarovana) {
        this.deklarovana = deklarovana;
    }

    /**
     * @return the idAdresa
     */
    public int getIdAdresa() {
        return idAdresa;
    }

    /**
     * @param idAdresa the idAdresa to set
     */
    public void setIdAdresa(int idAdresa) {
        this.idAdresa = idAdresa;
    }

    /**
     * @return the arTObj1
     */
    public ArrayList<TridaObjednavka1> getArTObj1() {
        return arTObj1;
    }

    /**
     * @param arTObj1 the arTObj1 to set
     */
    public void setArTObj1(ArrayList<TridaObjednavka1> arTObj1) {
        this.arTObj1 = arTObj1;
    }

    /**
     * @return the cisloLieferschein
     */
    public String getCisloLieferschein() {
        return cisloLieferschein;
    }

    /**
     * @param cisloLieferschein the cisloLieferschein to set
     */
    public void setCisloLieferschein(String cisloLieferschein) {
        this.cisloLieferschein = cisloLieferschein;
    }

    /**
     * @return the pdf
     */
    public String getPdf() {
        return pdf;
    }

    /**
     * @param pdf the pdf to set
     */
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    /**
     * @return the stavFaktury
     */
    public String getStavFaktury() {
        return stavFaktury;
    }

    /**
     * @param stavFaktury the stavFaktury to set
     */
    public void setStavFaktury(String stavFaktury) {
        this.stavFaktury = stavFaktury;
    }

    /**
     * @return the adresa
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * @param adresa the adresa to set
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * @return the mesto
     */
    public String getMesto() {
        return mesto;
    }

    /**
     * @param mesto the mesto to set
     */
    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    /**
     * @return the psc
     */
    public String getPsc() {
        return psc;
    }

    /**
     * @param psc the psc to set
     */
    public void setPsc(String psc) {
        this.psc = psc;
    }

    /**
     * @return the stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * @param stat the stat to set
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

    /**
     * @return the kontakt
     */
    public String getKontakt() {
        return kontakt;
    }

    /**
     * @param kontakt the kontakt to set
     */
    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    /**
     * @return the kontaktTelefon
     */
    public String getKontaktTelefon() {
        return kontaktTelefon;
    }

    /**
     * @param kontaktTelefon the kontaktTelefon to set
     */
    public void setKontaktTelefon(String kontaktTelefon) {
        this.kontaktTelefon = kontaktTelefon;
    }

    /**
     * @return the adresaFakturace
     */
    public String getAdresaFakturace() {
        return adresaFakturace;
    }

    /**
     * @param adresaFakturace the adresaFakturace to set
     */
    public void setAdresaFakturace(String adresaFakturace) {
        this.adresaFakturace = adresaFakturace;
    }

    /**
     * @return the mestoFakturace
     */
    public String getMestoFakturace() {
        return mestoFakturace;
    }

    /**
     * @param mestoFakturace the mestoFakturace to set
     */
    public void setMestoFakturace(String mestoFakturace) {
        this.mestoFakturace = mestoFakturace;
    }

    /**
     * @return the pscFakturace
     */
    public String getPscFakturace() {
        return pscFakturace;
    }

    /**
     * @param pscFakturace the pscFakturace to set
     */
    public void setPscFakturace(String pscFakturace) {
        this.pscFakturace = pscFakturace;
    }

    /**
     * @return the statFakturace
     */
    public String getStatFakturace() {
        return statFakturace;
    }

    /**
     * @param statFakturace the statFakturace to set
     */
    public void setStatFakturace(String statFakturace) {
        this.statFakturace = statFakturace;
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

    /**
     * @return the nazev1Radek
     */
    public String getNazev1Radek() {
        return nazev1Radek;
    }

    /**
     * @param nazev1Radek the nazev1Radek to set
     */
    public void setNazev1Radek(String nazev1Radek) {
        this.nazev1Radek = nazev1Radek;
    }

    /**
     * @return the nazev2Radek
     */
    public String getNazev2Radek() {
        return nazev2Radek;
    }

    /**
     * @param nazev2Radek the nazev2Radek to set
     */
    public void setNazev2Radek(String nazev2Radek) {
        this.nazev2Radek = nazev2Radek;
    }

    /**
     * @return the zakaznikJmenoFirmy
     */
    public String getZakaznikJmenoFirmy() {
        return zakaznikJmenoFirmy;
    }

    /**
     * @param zakaznikJmenoFirmy the zakaznikJmenoFirmy to set
     */
    public void setZakaznikJmenoFirmy(String zakaznikJmenoFirmy) {
        this.zakaznikJmenoFirmy = zakaznikJmenoFirmy;
    }

    /**
     * @return the datumSplatnosti
     */
    public Date getDatumSplatnosti() {
        return datumSplatnosti;
    }

    /**
     * @param datumSplatnosti the datumSplatnosti to set
     */
    public void setDatumSplatnosti(Date datumSplatnosti) {
        this.datumSplatnosti = datumSplatnosti;
    }

    /**
     * @return the arDodatek1
     */
    public ArrayList<TridaDodatek1> getArDodatek1() {
        return arDodatek1;
    }

    /**
     * @param arDodatek1 the arDodatek1 to set
     */
    public void setArDodatek1(ArrayList<TridaDodatek1> arDodatek1) {
        this.arDodatek1 = arDodatek1;
    }

    /**
     * @return the vatNr
     */
    public String getVatNr() {
        return vatNr;
    }

    /**
     * @param vatNr the vatNr to set
     */
    public void setVatNr(String vatNr) {
        this.vatNr = vatNr;
    }

    /**
     * @return the jazyk
     */
    public String getJazyk() {
        return jazyk;
    }

    /**
     * @param jazyk the jazyk to set
     */
    public void setJazyk(String jazyk) {
        this.jazyk = jazyk;
    }
}
