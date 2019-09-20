package mikronis2;

//editor pro vybery v tabulkach
import java.awt.Component;
import java.util.EventObject;
import java.util.Vector;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import cz.mikronplzen.dbfunkce.SQLFunkceObecne2;
import mikronis2.tridy.DvojiceCisloRetez;

public class TableVyberEditor1 extends JComboBox implements TableCellEditor
{

//  protected DvojiceCisloRetez dv;
  protected EventListenerList listenerList;
  protected ChangeEvent changeEvent;
  protected CellEditorListener ceListener;
  protected Object[] listeners;
  private int id;
  private Vector v;
  
  public TableVyberEditor1()
  {
    super();
    changeEvent = new ChangeEvent(this);
    listenerList = new EventListenerList();

    this.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
      fireEditingStopped();
      }
    });
  }//konec konstruktoru1

  
  public TableVyberEditor1(String dotaz, String chybaPrazdne)
  {
    super();
    changeEvent = new ChangeEvent(this);
    listenerList = new EventListenerList();
    this.setFont(new Font("helvetica", Font.BOLD, 10));
    this.setMaximumRowCount(15);
    getData(dotaz, chybaPrazdne);
    
    this.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
      fireEditingStopped();
      }
    });
  }//konec konstruktoru2

  public void addCellEditorListener(CellEditorListener listener)
  {
    listenerList.add(CellEditorListener.class, listener);
  } 

  public void removeCellEditorListener(CellEditorListener listener)
  {
    listenerList.remove(CellEditorListener.class, listener);
  } 

  protected void fireEditingStopped()
  {
 //   CellEditorListener listener;
    listeners = listenerList.getListenerList();
    for (int i = 0; i < listeners.length; i++)
    {
      if (listeners[i] == CellEditorListener.class)
      {
        ceListener = (CellEditorListener)listeners[i + 1];
        ceListener.editingStopped(changeEvent);
      }
    }
  }

  protected void fireEditingCanceled()
  {
 //   CellEditorListener listener;
    listeners = listenerList.getListenerList();
    for (int i = 0; i < listeners.length; i++)
    {
      if (listeners[i] == CellEditorListener.class)
      {
        ceListener = (CellEditorListener)listeners[i + 1];
        ceListener.editingCanceled(changeEvent);
      }
    }
  }//konec fireEditingCanceled

  public void cancelCellEditing()
  {
    fireEditingCanceled();
  }

  public boolean stopCellEditing()
  {
    fireEditingStopped();
    return true;
  }

  public boolean isCellEditable(EventObject event)
  {
    return true;
  } 

  public boolean shouldSelectCell(EventObject event)
  {
    return true;
  } 

  public Object getCellEditorValue()
  {
    return new Integer(((DvojiceCisloRetez)this.getSelectedItem()).cislo());
  } 

  public Component getTableCellEditorComponent(JTable table, 
          Object value, boolean isSelected, int row, int column)
  {
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
  } //konec getTableCellEditorComponent


  //nastavit data do editoru (roletky)
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
       "Chyba při načítání dat pro výběrový editor buněk tabulky", JOptionPane.ERROR_MESSAGE);
       return;
       }//konec if
      
    for(int i = 0; i < v.size(); i++)
    {
    this.addItem(v.get(i));
    }// konec for
   }//konec getData

    public void setVyberId(int id)
    {
    for(int i = 0; i < this.getItemCount(); i++)
    {
    if(((DvojiceCisloRetez)this.getItemAt(i)).cislo() == id)
     this.setSelectedIndex(i);
    }//konec for
    }//konec setVyberID
        
    
}//konec tridy TableVyberEditor1
