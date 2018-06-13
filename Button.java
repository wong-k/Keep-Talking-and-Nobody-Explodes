import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Button implements ActionListener {
    private Ellipse2D circle;
    private Rectangle buttonrec,strip;
    private Timer myTimer;
    private boolean defused,buttonheld;
    private int bat,time,tickcount,pressed,tdefuse;
    private int[]colour;
    private Color stripped;
    private String word;
    private Color[]strips={Color.RED,Color.WHITE,Color.YELLOW,Color.BLUE};
    public Button(String word,Rectangle mod,int[]colour,int bat,int defuse){
        tdefuse=defuse;
        time=20;
        buttonrec=mod;
        this.word=word;
        this.colour=colour;
        this.bat=bat;
        strip=new Rectangle((int)(buttonrec.getX())+150,(int)(buttonrec.getY()),20,100);
        circle=new Ellipse2D.Double(buttonrec.getX()+50.0,buttonrec.getY()+50.0,100.0,100.0);
        myTimer=new Timer(1000,this);
    }
    public boolean isdefused(){
        return defused;
    }
    public boolean interact(int x,int y){
        if(circle.contains(new Point2D.Double((double)(x),(double)(y)))){
            buttonheld=true;
            if(pressed<=0){
                pressed+=1;
            }
            if(!myTimer.isRunning()) {
                myTimer.start();
            }
        }
        if(!buttonheld && tickcount==tdefuse && pressed==1){

        }
        buttonheld=false;
        return false;
    }
    public int getAllottedTime(){return time;}
    public void reset(){}
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
    }
    public void actionPerformed(ActionEvent e){
        tickcount+=1;
    }
}
