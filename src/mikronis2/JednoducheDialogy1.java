package mikronis2;
/**
 * Ruzne potvrzovaci dialogy
 */

import javax.swing.*; //imported for buttons, labels, and images


public class JednoducheDialogy1
{ //Zacatek tridy
 
    public static JOptionPane dialogPanel;
    public static JDialog dialog;
    public static String text;
    
    //public JednoducheDialogy1 () {
    //}
    
    //Vytvori ANO/NE dialog s titulkem, hlaskou a vychozim stavem vb
    // false = vychozi odpoved NE, true = vychozi odpoved ANO
   public static int warnAnoNe(JComponent kontejner, String a, String n, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 1)
    {
    Object[] options = {a, n};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    } //konec if
    return 1;// pri zadani spatneho vyberu vrati NE
   } // konec warnAnoNe

    //Vytvori ANO/NE dialog s titulkem, hlaskou a vychozim stavem vb
    // false = vychozi odpoved NE, true = vychozi odpoved ANO
   public static int warnAnoNe(JFrame kontejner, String a, String n, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 1)
    {
    Object[] options = {a, n};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    } //konec if
    return 1;// pri zadani spatneho vyberu vrati NE
   } // konec warnAnoNe


   public static int warnAnoNeZrusit(JComponent kontejner, String a, String n, String z, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 2)
    {
    Object[] options = {a, n, z};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.WARNING_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    }
    return 2;// pri zadani spatneho vyberu vrati ZRUSIT
   } // konec warnAnoNeZrusit
   
   

   public static int warnAnoNeZrusit(JFrame kontejner, String a, String n, String z, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 2)
    {
    Object[] options = {a, n, z};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.WARNING_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    }
    return 2;// pri zadani spatneho vyberu vrati ZRUSIT
   } // konec warnAnoNeZrusit


   //Vytvori ANO/NE dialog s titulkem, hlaskou a vychozim stavem vb
    // false = vychozi odpoved NE, true = vychozi odpoved ANO
   public static int otazkaAnoNe(JComponent kontejner, String a, String n, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 1)
    {
    Object[] options = {a, n};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    } //konec if
    return 1;// pri zadani spatneho vyberu vrati NE
   } // konec otazkaAnoNe
   
   
   public static int otazkaAnoNeZrusit(JComponent kontejner, String a, String n, String z, String titulek, String hlaska, int vb)
   {
    if (vb >= 0 & vb <= 2)
    {
    Object[] options = {a, n, z};
    return JOptionPane.showOptionDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,     //don't use a custom Icon
    options,  //the titles of buttons
    options[vb]); //default button title
    }
    return 2;// pri zadani spatneho vyberu vrati ZRUSIT
   } // konec otazkaAnoNeZrusit

   
   public static void errAno(JComponent kontejner, String titulek, String hlaska)
   {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.ERROR_MESSAGE);
   } // konec errAno

   public static void errAno(JFrame kontejner, String titulek, String hlaska)
   {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.ERROR_MESSAGE);
   } // konec errAno


   public static void infoOK(JComponent kontejner, String titulek, String hlaska) {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.INFORMATION_MESSAGE);
   } // konec infoOK

   public static void infoOK(JFrame kontejner, String titulek, String hlaska) {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.INFORMATION_MESSAGE);
   } // konec infoOK


   public static void warnAno(JComponent kontejner, String titulek, String hlaska)
   {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.WARNING_MESSAGE);
   } // konec errAno

   public static void warnAno(JFrame kontejner, String titulek, String hlaska)
   {
    JOptionPane.showMessageDialog(kontejner,
    hlaska,
    titulek,
    JOptionPane.WARNING_MESSAGE);
   } // konec errAno

 
    
}