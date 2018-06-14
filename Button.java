import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;
public class Button implements ActionListener,MouseListener {
    private Random ran;
    private Ellipse2D circle,defu;
    private Rectangle button,strip;
    private Timer myTimer;
    private int[]colour; //Need to add that this randomly gets assigned a colour
    private boolean defused,buttonheld;
    private int time,tickcount,pressed,tdefuse,mouseX,mouseY;
    private Color stripped;
    private String word;
    private Color[]strips={Color.RED,Color.WHITE,Color.YELLOW,Color.BLUE};
    private String[]words={"Press","Hold","Click","Button"};
    public Button(int x,int y){
        ran=new Random();
        time=20;
        colour=new int[4];
        button=new Rectangle(x,y,200,200);
        int select=ran.nextInt(4);
        tdefuse+=select;
        word=words[select];
        strip=new Rectangle((int)(button.getX())+150,(int)(button.getY()),20,100);
        circle=new Ellipse2D.Double(button.getX()+50.0,button.getY()+50.0,100.0,100.0);
        myTimer=new Timer(1000,this);
        defu=new Ellipse2D.Double(button.getX()+180.0,button.getY()+10.0,10.0,10.0);
        select=ran.nextInt(4);
        tdefuse+=select;
        Color selected=strips[select];
        colour[0]=selected.getRed();
        colour[1]=selected.getBlue();
        colour[2]=selected.getGreen();
    }
    public boolean checkDefused(){
        return defused;
    }
    public boolean interact(int x,int y){
        if(circle.contains(new Point2D.Double((double)(x),(double)(y)))){
            if(pressed==0){
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
        if(!buttonheld && tickcount==tdefuse && pressed==1){
            defused=true;
            return true;
        }
        return false;
    }
    public int getAllottedTime(){return time;}
    public void reset(){
        pressed=0;
        tickcount=0;
        defused=false;
    }
    public void draw(Graphics g){
        g.setColor(new Color(colour[0],colour[1],colour[2]));
        g.fillOval((int)(circle.getX()),(int)(circle.getY()),100,100);
        g.setColor(Color.WHITE);
        g.drawString(word,(int)(circle.getX())+50,(int)(circle.getY())+50);
        g.setColor(Color.BLACK);
        g.fillRect((int)(strip.getX()),(int)(strip.getY()),20,50);
        if(buttonheld){
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
    public void mouseExited(MouseEvent e) {}
    /*-------------------------------------------------------------------
    This method makes button text red when mouse hovers over the button
     -------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e) {}

    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
        buttonheld=false;
        interact(mouseX,mouseY);
    }
    public void mousePressed(MouseEvent e) {
        buttonheld=true;
        mouseX=e.getX();
        mouseY=e.getY();
    }
}
