import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
public class Button implements ActionListener,MouseListener {//We use actionlistener for timers and we use mouselistener because we need to know when the button is released
    private Random ran;
    private Ellipse2D circle,defu; //This is the button and the indicator in the top right of the module showing whether it's defused or not
    private Rectangle button,strip; //This is module's rectangle and the strip colour indicator on the side
    private Timer myTimer;
    private int[]colour; //Array that holds the r g b values of the buttons colour
    private boolean defused,buttonheld; //Defused boolean and buttonheld boolean used to know when the button is being pressed and not
    private int time,tickcount,pressed,tdefuse,mouseX,mouseY;
    /*Time needed to defuse the module
    tickcount to know how long the button is held
    pressed to know that the button has been pressed
    Tdefuse to know how long the button needs to be held to be defused
    The mouses coordinates
    */
    private Color stripped; //The colour of the strip indicator on the side
    private String word;//The string displayed on the button
    private Color[]strips={Color.RED,Color.WHITE,Color.YELLOW,Color.BLUE}; //Choice of colours to be displayed
    private String[]words={"Press","Hold","Click","Button"}; //Choice of strings to be displayed
    /*Constructor*/
    public Button(int x,int y){
        ran=new Random();
        time=20;
        colour=new int[3];
        stripped=Color.BLACK; //Auto setting the strip to be black
        button=new Rectangle(x,y,200,200); //Module rectangle
        int select=ran.nextInt(4); //Select is used to determine which of the strings is being chosen and adding the time
        tdefuse+=select;
        word=words[select];
        strip=new Rectangle((int)(button.getX())+150,(int)(button.getY()),20,100);
        circle=new Ellipse2D.Double(button.getX()+50.0,button.getY()+50.0,100.0,100.0);
        myTimer=new Timer(1000,this);
        defu=new Ellipse2D.Double(button.getX()+180.0,button.getY()+10.0,10.0,10.0);
        select=ran.nextInt(4); //Select being used to choose the oclour of the button
        tdefuse+=select;
        Color selected=strips[select];
        colour[0]=selected.getRed(); //Getting the RGB of the selected colour
        colour[1]=selected.getBlue();
        colour[2]=selected.getGreen();
    }
    /*-Defused method that tells the bomb when the module has been defused-*/
    public boolean checkDefused(){ return defused; }
    /*---------------------------------
    *This is the interact function that will be used for each of the modules
    *Uses the mouse coordinates to know if the button is being pressed
    *Returns a boolean so that if they make a mistake, we can return false in the main and display the mistake.
    -----------------------------------*/
    public boolean interact(int x,int y){
        if(circle.contains(new Point2D.Double((double)(x),(double)(y)))){
            if(pressed==0){//If this is the first time the button has been pressed, we mark that and we determine the strip colour
                if(tdefuse>=4) {
                    int select = ran.nextInt(4);
                    tdefuse -= select;
                    stripped = strips[ran.nextInt(4)];
                }
                pressed+=1;
            }
            if(!myTimer.isRunning()) {
                myTimer.start();
            }
            return true;
        }
        if(!buttonheld && tickcount==tdefuse && pressed==1){ //We check if the button is not being held and that we have held the button down long enough and that its been pressed
            defused=true;
            return true;
        }
        return false;
    }
    /*-Allotted time method used to help determine the total amount of time needed for the bomb-*/
    public int getAllottedTime(){return time;}
    /*-Reset method to allow the bomb to be replayed-*/
    public void reset(){
        pressed=0;
        tickcount=0;
        defused=false;
    }
    /*-Draw method to draw the module and each of its components-*/
    public void draw(Graphics g){
        g.setColor(new Color(colour[0],colour[1],colour[2]));
        g.fillOval((int)(circle.getX()),(int)(circle.getY()),100,100);
        g.setColor(Color.WHITE);
        g.drawString(word,(int)(circle.getX())+50,(int)(circle.getY())+50);
        g.setColor(Color.BLACK);
        g.fillRect((int)(strip.getX()),(int)(strip.getY()),20,50);
        if(buttonheld){ //If the button is clicked we show it and we show the strip
            g.setColor(Color.YELLOW);
            g.drawOval((int)(circle.getX()),(int)(circle.getY()),100,100);
            g.setColor(stripped);
            g.fillRect((int)(strip.getX()),(int)(strip.getY()),20,50);
        }
        g.setColor(Color.RED);
        if(defused) {
            g.setColor(Color.GREEN);
        }
        g.fillOval((int)(defu.getX()),(int)(defu.getY()),10,10);
    }
    public void actionPerformed(ActionEvent e){
        tickcount+=1;
    }
    public void mouseReleased(MouseEvent e) { //This is used to help determine when the button is let go to check if it was held for the right amount of time
        buttonheld=false;
        interact(mouseX,mouseY);
    }
    public void mousePressed(MouseEvent e) {
        buttonheld=true;
        mouseX=e.getX();
        mouseY=e.getY();
    }
    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
}
