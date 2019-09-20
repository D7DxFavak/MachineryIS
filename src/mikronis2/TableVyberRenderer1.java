package mikronis2;

//zobrazovac pro vyber v tabulkach
import java.awt.Component;
//import java.util.EventObject;
//import java.util.Enumeration;
import java.util.Vector;
//import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;
//import javax.swing.event.*;
import javax.swing.table.*;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.tridy.DvojiceCisloRetez;

public class TableVyberRenderer1 extends JComboBox implements TableCellRenderer
{

//  protected DvojiceCisloRetez dv;
  private int id;
  private Vector v;
  
  public TableVyberRenderer1(String dotaz, String chybaPrazdne)
  {
    super();
    this.setFont(new Font("helvetica", Font.BOLD, 10));
    this.setMaximumRowCount(15);
  //  this.setEditable(true);
    getData(dotaz, chybaPrazdne);
  }//konec konstruktoru

  public Component getTableCellRendererComponent(JTable table, 
          Object value, boolean isSelected, boolean hasFocus, 
          int row, int column)
  {
    if (isSelected)
    {
     this.setBackground(Color.CYAN);
    }
    else
    {
     this.setBackground(Color.GREEN);
    }//kone else pro isSelected
  
    id = ((Integer)value).intValue();
    for(int i = 0; i < this.getItemCount(); i++)
    {
    if(((DvojiceCisloRetez)this.getItemAt(i)).cislo() == id)
    {
    this.setSelectedIndex(i);
    return this;
    }
    }//konec for
    return this;
  }//konec getTableCellRendererComponent

  //nastavit data do zobrazovace vyberu
    public void getData(String dotaz, String chybaPrazdne)
    {
      this.removeAllItems();
      
      v = SQLFunkceObecne2.getVDvojicCisloRetez(dotaz); 
      if((v.isEmpty() == true) && !chybaPrazdne.trim().equalsIgnoreCase(""))
       {
       JOptionPane.showMessageDialog(this, chybaPrazdne,
       "Chyba", JOptionPane.ERROR_MESSAGE);
       return;
       }//konec if
      
      if((v.size() == 2) && (v.get(0) == null))
       {
       String chyba = SQLFunkceObecne2.getCHybovouHlasku(((Integer)v.get(1)).intValue());
       JOptionPane.showMessageDialog(this, chyba,
       "Chyba při načítání dat pro výběrový zobrazovač buněk tabulky", JOptionPane.ERROR_MESSAGE);
       return;
       }//konec if
      
    for (int i = 0; i < v.size(); i++)
    {
    this.addItem(v.get(i));
    }// konec for
   }//konec getData

    
}//konec tridy TableVyberRenderer1
