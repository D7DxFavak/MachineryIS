/*
 * RoletkaUniverzalModel1.java
 *
 * Created on 14. duben 2007, 22:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mikronis2.tridy;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import mikronis2.JednoducheDialogy1;
import mikronis2.MikronIS2;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;

/**
 *
 * @author dave
 */
public class RoletkaUniverzalModel1 extends AbstractListModel implements MutableComboBoxModel {
    
    private Vector v;
    private List list;
    private Object selected;
    
    /** Creates a new instance of RoletkaUniverzalModel1 */
    public RoletkaUniverzalModel1() {
          v = new Vector();
          this.list = new ArrayList(v);
          this.selected = getElementAt(0);
    }

    // Dotaz musi vracet data typu DvojiceRetezCislo
    public RoletkaUniverzalModel1(String dotaz) {
          v = new Vector();
          this.list = new ArrayList(v);
          setData(dotaz, "");
          this.selected = getElementAt(0);
    }

    // Dotaz musi vracet data typu DvojiceCisloRetez
    public RoletkaUniverzalModel1(String dotaz, String chybaPrazdne) {
          v = new Vector();
          this.list = new ArrayList(v);
          setData(dotaz, chybaPrazdne);
          this.selected = getElementAt(0);
    }
    
     public RoletkaUniverzalModel1(Vector in) {
          v = in;
          this.list = new ArrayList(v);
          this.selected = getElementAt(0);
    }

    
    
    @Override
    public void addElement(Object obj) {
          int index = list.size();
          this.list.add(obj);
          if(index == 0 && this.selected == null && obj != null)
             this.selected = obj;
          //System.out.println("Pridana polozka do seznamu");
          fireIntervalAdded(this, index, index);
    }
    
//    public void addElementAt(Object obj, int index) {
//          insertElementAt(obj, index);
//    }

    @Override
    public void insertElementAt(Object obj, int index) {
          this.list.add(index, obj);
          fireIntervalAdded(this, index, index);
    }

    @Override
    public void removeElement(Object obj) {
          int index = getIndexOf(obj);
          if(index != -1)
             removeElementAt(index);
    }

    @Override
    public void removeElementAt(int index) {
          if(getElementAt(index) == this.selected)
             this.selected = getElementAt(index == 0 ? 1 : index - 1);
          this.list.remove(index);
          fireIntervalRemoved(this, index, index);
    }
    
    
    @Override
    public Object getElementAt(int index) {
        return index >= 0 && index < this.list.size() ? this.list.get(index) : null;
    }

    
    
    public void removeAll() {
          int size = this.list.size();
          if (size == 0)
             return;
          this.list.clear();
          this.selected = null;
          fireIntervalRemoved (this, 0, size - 1);
    }

    public void replaceAll(List list) {
          this.list.clear();
          this.list.addAll(list);
          if (this.selected != null && this.list.indexOf(this.selected) == -1)
             this.selected = getElementAt(0);
          fireContentsChanged(this, 0, Integer.MAX_VALUE);
    }
    
    

    public int getSelectedIndex() {
          return getIndexOf(getSelectedItem());
    }

    public void setSelectedIndex(int index) {
          setSelectedItem(getElementAt(index));
    }
    
    
    @Override
    public Object getSelectedItem() {
        return this.selected;
    }

    @Override
    public void setSelectedItem(Object obj) {
          int index = this.list.indexOf(obj);
          if (index != -1)
             obj = this.list.get(index);
          if (obj == this.selected)
             return;
          this.selected = obj;
          fireContentsChanged(this, -1, -1);
    }


    @Override
    public int getSize() {
        return this.list.size();
    }

    public int getIndexOf(Object obj) {
          return this.list.indexOf(obj);
    }
    
    
    //public void addListDataListener(ListDataListener list) {
    //}

    //public void removeListDataListener(ListDataListener list) {
    //}

    
    //nastavit data do roletky normalne, bez vyberu polozky
    public boolean setData(String dotaz, String chybaPrazdne)
    {
      v = nactiData(dotaz, chybaPrazdne);
      if(v.isEmpty() == true)
       return false;
      else //else hlavni pro test isEmpty
       {
        for(int i = 0; i < v.size(); i++)
        {
        this.addElement(v.get(i));
        }// konec for
       }//konec else
      return true;
   }//konec setData
    
    
  //nastavit data a nastavit vyber na cislo id, pokud je id neexistuje, vynechej nastaveni vyberu
    public boolean setDataId(String dotaz, String chybaPrazdne, int id)
    {
      v = nactiData(dotaz, chybaPrazdne);
      if(v.isEmpty() == true)
       return false;
      else //else hlavni pro test isEmpty
       {
    int i = 0;
    for(i = 0; i < v.size(); i++)
    {
    this.addElement(v.get(i));
    }// konec for
    //vyber polozky
    for(i = 0; i < this.getSize(); i++)
    {
    if(((DvojiceCisloRetez)this.getElementAt(i)).cislo() == id)
    {
    this.setSelectedIndex(i);
    break;
    }//konec if
    }//konec for pro vyber polozky
       }//konec else
      return true;
   }//konec setDataId

  //nastavit data a nastavit vyber na index index, pokud je index mimo rozsah, ponechej vyber polozky
    public boolean setDataIndex(String dotaz, String chybaPrazdne, int index)
    {
      v = nactiData(dotaz, chybaPrazdne);
      if(v.isEmpty() == true)
       return false;
      else //else hlavni pro test isEmpty
       {
    int i = 0;
    for(i = 0; i < v.size(); i++)
    {
    this.addElement(v.get(i));
    }// konec for
    //vyber indexu
       if (this.getSize() > index & index >= 0)
        this.setSelectedIndex(index);
//       else
  //      this.setSelectedIndex(0);
       }//konec else
      return true;
   }//konec setDataIndex

    
  //nastavit vyber na cislo id bez nacteni dat, pokud id neexistuje, vynechej nastaveni vyberu
    public void setDataOId(int id)
    {
    int i = 0;
    //vyber polozky
    for(i = 0; i < this.getSize(); i++)
    {
    if(((DvojiceCisloRetez)this.getElementAt(i)).cislo() == id)
    {
    this.setSelectedIndex(i);
    break;
    }//konec if
    }//konec for pro vyber polozky
    }//konec setDataOId

    
  //nastavit vyber na index index bez nacitani dat, pokud je index mimo rozsah, ponechej vyber polozky
    public void setDataOIndex(int index)
    {
    int i = 0;
    //vyber indexu
       if (this.getSize() > index & index >= 0)
        this.setSelectedIndex(index);
//       else
  //      this.setSelectedIndex(0);
    }//konec setDataOIndex

    
    //nacte data do pripravneho vektoru s osetrenim chyb
   private Vector nactiData(String dotaz, String chybaPrazdne)
   {
   v.removeAllElements();
   v = SQLFunkceObecne2.getVDvojicCisloRetez(dotaz);
      if((v.isEmpty() == true) && !chybaPrazdne.trim().equalsIgnoreCase(""))
         {//zobrazit chybu pro prazdne jen tehdy, pokud je zadana
         JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba - prázdný výběr", chybaPrazdne);
         return v;
         }//konec if

      if((v.size() == 2) && (v.get(0) == null))
       {
       String chyba = SQLFunkceObecne2.getCHybovouHlasku(((Integer)v.get(1)).intValue());
       JednoducheDialogy1.errAno(MikronIS2.ramecAplikace, "Chyba při načítání dat do výběrového seznamu", chyba);
       this.removeAll();
       return v;
       }//konec if
   
    return v;
   }//konec nactiData
    
    
    
} 


