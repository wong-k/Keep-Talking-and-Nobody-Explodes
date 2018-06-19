import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
/*---------------------------------------------------------------------------
This class makes a symbols module, which can draw and reset itself.
The way to defuse the symbols is to click them in the correct sequence.
 --------------------------------------------------------------------------*/
public class Symbols{
    //All the images used for symbols, cannot be loaded in a loop because there's a specific order wanted
    private static Image symbol1=new ImageIcon("images/KTANESymbol1.png").getImage();
    private static Image symbol2=new ImageIcon("images/KTANESymbol2.png").getImage();
    private static Image symbol3=new ImageIcon("images/KTANESymbol3.png").getImage();
    private static Image symbol4=new ImageIcon("images/KTANESymbol4.png").getImage();
    private static Image symbol5=new ImageIcon("images/KTANESymbol5.png").getImage();
    private static Image symbol6=new ImageIcon("images/KTANESymbol6.png").getImage();
    private static Image symbol7=new ImageIcon("images/KTANESymbol7.png").getImage();
    private static Image symbol8=new ImageIcon("images/KTANESymbol8.png").getImage();
    private static Image symbol9=new ImageIcon("images/KTANESymbol9.png").getImage();
    private static Image symbol10=new ImageIcon("images/KTANESymbol10.png").getImage();
    private static Image symbol11=new ImageIcon("images/KTANESymbol11.png").getImage();
    private static Image symbol12=new ImageIcon("images/KTANESymbol12.png").getImage();
    private static Image symbol13=new ImageIcon("images/KTANESymbol13.png").getImage();
    private static Image symbol14=new ImageIcon("images/KTANESymbol14.png").getImage();
    private static Image symbol15=new ImageIcon("images/KTANESymbol15.png").getImage();
    private static Image symbol16=new ImageIcon("images/KTANESymbol16.png").getImage();
    private static Image symbol17=new ImageIcon("images/KTANESymbol17.png").getImage();
    private static Image symbol18=new ImageIcon("images/KTANESymbol18.png").getImage();
    private static Image symbol19=new ImageIcon("images/KTANESymbol19.png").getImage();
    private static Image symbol20=new ImageIcon("images/KTANESymbol20.png").getImage();
    private static Image symbol21=new ImageIcon("images/KTANESymbol21.png").getImage();
    private static Image symbol22=new ImageIcon("images/KTANESymbol22.png").getImage();
    private static Image symbol23=new ImageIcon("images/KTANESymbol23.png").getImage();
    private static Image symbol24=new ImageIcon("images/KTANESymbol24.png").getImage();
    private static Image symbol25=new ImageIcon("images/KTANESymbol25.png").getImage();
    private static Image symbol26=new ImageIcon("images/KTANESymbol26.png").getImage();
    private static Image symbol27=new ImageIcon("images/KTANESymbol27.png").getImage();
    private Ellipse2D defu;                                                                     //This is the indicator in the top right of the module showing whether it's defused or not
    private ArrayList<Rectangle>symbols;                                                        //4 symbol rectangles in the middle of the module.
    private Image[][]images={{symbol1,symbol2,symbol3,symbol4,symbol5,symbol6,symbol7},         //2D ArrayList for the options of symbols to be used
                             {symbol8,symbol1,symbol7,symbol9,symbol10,symbol6,symbol11},
                             {symbol12,symbol13,symbol9,symbol14,symbol15,symbol3,symbol10},
                             {symbol16,symbol17,symbol18,symbol5,symbol14,symbol11,symbol19},
                             {symbol20,symbol19,symbol18,symbol21,symbol17,symbol22,symbol23},
                             {symbol16,symbol8,symbol24,symbol25,symbol20,symbol26,symbol27}};
    private Image[]options;                                                                     //This is the line of symbols used for the specific module to defuse
    private TreeSet<Integer>right,undo;                                                         //Treesets holding the correct order of symbols to be pressed and undo for when we reset
    private Rectangle symbol;                                                                   //The rectangle of the module itself
    private int time;                                                                           //The amount of time allocated to defuse the module
    private boolean defused;                                                                    //Standard defuse boolean depicting whether or not the module has been defused
    private ArrayList<Integer>selected;                                                         //Randomly chosen integers for which symbols will be displayed and used
    /*---------------------------------------------------------------------------
    Constructor where "x" and "y" are the top left corner coordinates of the box
    ----------------------------------------------------------------------------*/
    public Symbols(int x,int y){
        int[]order={0,1,2,3,4,5,6};                          //Order is used when deciding which symbols to select
        time=50000;
        symbol=new Rectangle(x,y,200,200);      //Constructing the modules rectangle
        symbols=new ArrayList<Rectangle>();                 //The 4 rectangles for each of the symbols
        Random ran=new Random();                            //Random generator for randomly choosing the line of symbols and which symbols
        options=images[ran.nextInt(6)];              //Picking our line of symbols
        right=new TreeSet<Integer>();                       //Initialized right order of selecting symbols
        undo=new TreeSet<Integer>();                        //The undo treeset for resetting
        selected=new ArrayList<Integer>();                  //ArrayList for choosing our symbols
        while(selected.size()<4){                           //Randomly selects symbols from order to be the ones we're using
            int select=ran.nextInt(order.length);
            if(selected.indexOf(select)<0){
                selected.add(select);
            }
        }
        for(int j=0;j<order.length;j++){
            if(selected.indexOf(order[j])>=0){              //We add the symbol to the treeset if it's included in our selected symbols
                right.add(order[j]);
            }
        }
        for(int i=0;i<4;i++){                               //Making all our rectangles for each symbol
            symbols.add(new Rectangle(symbol.x+25+76*(i-1<=0 ? 0:1),symbol.y+25+76*(i%2),75,75));
        }
        defu=new Ellipse2D.Double(symbol.getX()+180.0,symbol.getY()+10.0,10.0,10.0);
    }
    /*------------------------------------------------------------------------------------------------------------
    This is the interact method that will be used for each of the modules
    Uses the mouse coordinates to be sure of the right symbol being pressed
    Returns a boolean so that if they make a mistake, we can return false in the main and display the mistake.
    -------------------------------------------------------------------------------------------------------------*/
    public boolean interact(int mousex,int mousey){
        Rectangle select=null;
        for (Rectangle sym:symbols) {                       //We check for each symbol having been pressed
            if (sym.contains(mousex, mousey)) {
                select=sym;
            }
        }                                                   //If the symbol being pressed is not the correct one, we return false
        if(select!=null){
            if (right.size() > 0){
                if(right.first().equals(selected.get(symbols.indexOf(select)))) {   //We check to see that the symbol pressed is the right one
                    undo.add(right.pollFirst());                                    //We add that symbol to our undo list
                    if (right.size()==0) {                                          //Once they've clicked on all the symbols, the module is defused
                        defused = true;
                        return true;
                    }
                    return true;
                }
            }
        }
        if(select==null || !right.contains(symbols.indexOf(select))){
            return true;
        }
        return false;
    }
    /*--------------------------------------------------------------------------------------
    Allotted time method used to help determine the total amount of time needed for the bomb
    ---------------------------------------------------------------------------------------*/
    public int getAllottedTime(){return time;}
    /*------------------------------------------------------------------
    Defused method that tells the bomb when the module has been defused
    -------------------------------------------------------------------*/
    public boolean checkDefused(){
        return defused;
    }
    /*-------------------------------------------
    Reset method to allow the bomb to be replayed
    ---------------------------------------------*/
    public void reset(){
        defused=false;
        right.addAll(undo);
        undo.clear();
    }
    /*----------------------------------------------------------
    Draw method to draw the module and each of its components
    ------------------------------------------------------------*/
    public void draw(Graphics g){
                                            //This is an Array of indicators put above the symbols to show whether they've been pressed correctly or not
        Rectangle[]indicator={new Rectangle((int)(symbols.get(0).getX())+10,(int)(symbols.get(0).getY())+3,55,5),new Rectangle((int)(symbols.get(0).getX())+10,(int)(symbols.get(0).getY())+80,55,10),new Rectangle((int)(symbols.get(0).getX())+85,(int)(symbols.get(0).getY())+5,55,10),new Rectangle((int)(symbols.get(0).getX())+85,(int)(symbols.get(0).getY())+80,55,10)};
        int coloured=4-right.size();        //This is used to determine which indicators will be coloured green
        g.setColor(new Color(246,231,206));

        for(int i=0;i<4;i++) {              //Drawing the rectangles and symbols
            g.fillRect((int)(symbols.get(i).getX()),(int)(symbols.get(i).getY()),(int)(symbols.get(i).getWidth()),(int)(symbols.get(i).getHeight()));
            g.drawImage(options[selected.get(i)],(int)(symbols.get(i).getX())+5,(int)(symbols.get(i).getY())+5,null);
        }

        for(int i=0;i<2;i++){               //This is all done to colour the indicators the proper colour
            for(int j=0;j<2;j++){
                g.setColor(Color.black);
                g.fillRect((int)(indicator[i*2+j].getX()),(int)(indicator[i*2+j].getY()),(int)(indicator[i*2+j].getWidth()),(int)(indicator[i*2+j].getHeight()));
                for(int k=0;k<coloured;k++){
                    ArrayList<Integer>current=new ArrayList<Integer>();
                    current.addAll(selected);
                    current.removeAll(right);
                    g.setColor(Color.green);
                    g.fillRect((int)(indicator[selected.indexOf(current.get(k))].getX()),(int)(indicator[selected.indexOf(current.get(k))].getY()),(int)(indicator[selected.indexOf(current.get(k))].getWidth()),(int)(indicator[selected.indexOf(current.get(k))].getHeight()));
                }
            }
        }
        g.setColor(Color.RED);              //Colouring and drawing the defused circle in the top right
        if(defused) {
            g.setColor(Color.GREEN);
        }
        g.fillOval((int)(defu.getX()),(int)(defu.getY()),10,10);
    }
}
