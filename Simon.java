import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
public class Simon implements ActionListener { //ActionListener needed for using the timer correctly for how long to display the pattern
    private Ellipse2D defu; //This is the indicator in the top right of the module showing whether it's defused or not
    private Timer myTimer; //Timer used for displaying the pattern
    private Rectangle simon; //The modules rectangle
    private Polygon[]colours; //The 4 diamond shapes stored as an array
    private Color[]painting; //Array of the 4 colours for each diamond
    private boolean defused; //Standard defuse boolean depicting whether or not the module has been defused
    private Random ran=new Random();
    private ArrayList<Integer>pattern=new ArrayList<Integer>();//This is the pattern of 0,1,2,3's that determines which of the colours needs to be clicked
    private int complexity,clicks,time,tickcount,display,displaycount;
    /*Complexity is the total amount of times the colour will flash,
     *Clicks keeps up to date with how many times the user has clicked colours to keep up with the pattern,
     *Time is the amount of time allocatted to defuse the module
     *Tickcount is used to help display the pattern
     *Display is used to display the right colour when pressing on the symbols
     *Displaycount is used to time how long the display is show colour wise*/
    private int[][]invert={{3,1,0,2},{2,3,0,1},{0,2,3,1}}; //This makes the inverted effect that the reader of the manual has to tell the person to click different colours depending on the # of strikes
    /*Constructor*/
    public Simon(int x,int y){
        Timer myTimer=new Timer(50,this);
        complexity=8-(ran.nextInt(4)+1);//Complexity of the module is randomly determined
        time=15*complexity;
        simon=new Rectangle(x,y,200,200);
        colours=new Polygon[4];
        painting=new Color[]{new Color(1f,0f,0f),new Color(0f,0f,1f),new Color(0f,1f,0f),new Color(1f,1f,0f)};
        for(int i=0;i<4;i++){//Creating the diamonds
            int[]xcoords={simon.x+30+35*(i<2 ? i:i-1),simon.x+65+35*(i<2 ? i:i-1),simon.x+65+35*(i<2 ? i:i-1),simon.x+100+35*(i<2 ? i:i-1)};
            int[]ycoords={simon.y+100-35*(i<2 ? i:i-3),simon.y+65-35*(i<2 ? i:i-3),simon.y+135-35*(i<2 ? i:i-3),simon.y+100-35*(i<2 ? i:i-3)};//NEED 0,+1,-1,0
            colours[i]=new Polygon(xcoords,ycoords,4);
        }
        defu=new Ellipse2D.Double(simon.getX()+180.0,simon.getY()+10.0,10.0,10.0);
    }
    /*---------------------------------
    *This is the interact function that will be used for each of the modules
    *Uses the mouse coordinates for knowing which colour it's clicked and strikes to know the proper inverting
    *Returns a boolean so that if they make a mistake, we can return false in the main and display the mistake.
    -----------------------------------*/
    public boolean interact(int mousex,int mousey,int strikes){
        if(colours[invert[strikes][pattern.get(clicks)]].contains(mousex,mousey)) { //We check the part of the pattern they're at, followed by the "correct" inverted pattern which then tells us which polygon to check for collision
            display=invert[strikes][pattern.get(clicks)];//This helps display the proper colour when clicked
            displaycount=50;
            myTimer.start();
            clicks += 1;//The user has gone one further into their pattern
            if (pattern.size() == clicks) { //We check if they've finished the pattern
                if (clicks == complexity) { //And check if they've gotten to the largest complexity
                    defused = true;
                    return true;
                }
                clicks = 0;
                pattern.add(ran.nextInt(4));//We add one more to the pattern
            }
            return true;
        }
        else{
            return false;
        }
    }
    /*-Defused method that tells the bomb when the module has been defused-*/
    public boolean checkDefused(){
        return defused;
    }
    /*-Allotted time method used to help determine the total amount of time needed for the bomb-*/
    public int getAllottedTime(){return time;}
    /*-Reset method to allow the bomb to be replayed-*/
    public void reset(){
        pattern.clear();
    }
    /*-Draw method to draw the module and each of its components-*/
    public void draw(Graphics g){
        g.setColor(new Color(1f,0f,0f,0.4f));
        g.fillPolygon(colours[0]);
        g.setColor(new Color(0f,0f,1f,0.4f));
        g.fillPolygon(colours[1]);
        g.setColor(new Color(0f,1f,0f,0.4f));
        g.fillPolygon(colours[2]);
        g.setColor(new Color(1f,1f,0f,0.4f));
        g.fillPolygon(colours[3]);
        g.setColor(Color.RED);
        if(defused) {
            g.setColor(Color.GREEN);
        }
        g.fillOval((int)(defu.getX()),(int)(defu.getY()),10,10);
        g.setColor(painting[display]);
        while(displaycount>0){
            g.fillPolygon(colours[display]);
        }
        pattern(g);
    }
    /*Pattern method to be used when displaying the pattern of the module*/
    private void pattern(Graphics g){
        myTimer.start();
        for(int i:pattern){
            tickcount=100;
            while(true){
                if(tickcount==0){
                    tickcount=100;
                    break;
                }
            }
            while(true){
                g.setColor(painting[pattern.get(i)]);
                g.fillPolygon(colours[pattern.get(i)]);
                if(tickcount==0){
                    myTimer.stop();
                    break;
                }
            }
        }
    }
    public void actionPerformed(ActionEvent e){
        if(tickcount>0){
            tickcount-=10;
        }
        if(displaycount>0){
            displaycount-=10;
            if(displaycount==0){
                myTimer.stop();
            }
        }
    }
}
