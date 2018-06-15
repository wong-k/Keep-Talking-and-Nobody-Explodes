/*
 MainGame.java
 The goal of this 2-player game is to defuse a bomb by solving numerous puzzles within a time limit.
 Upon running, a main menu frame is displayed. From here, players can access an html manual or a select level page.
 From the select level page, the user can click play, which brings them to the game frame.
 A 3 x 2 grid which represents the bomb is shown on screen. The user must complete all puzzles in the square before time runs out.
 After the game ends, the user is brought to the game over frame, where they can play again or go play the rest of the levels.
 Once the player has completed all 10 levels, they unlock a customizable level where they create their own bomb.
 Keith Wong, Erik Yzeiri
 */
import javax.swing.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.MalformedURLException;
import java.util.*;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicArrowButton;

/*------------------------------------------------------------------------------------------------------------------
This class contains the main method and makes a main menu upon running the program.
From this frame, player can access an html bomb defusal manual, or a select level page.
Implements ActionListener to detect when buttons are clicked, MouseListener to detect when mouse hovers over a button.
--------------------------------------------------------------------------------------------------------------------*/
public class MainGame extends JFrame implements ActionListener,MouseListener {
    private JButton playBut,infoBut;			//buttons that bring user to level selection and instruction pages
    private Bomb[] allBombs;                   //contains 10 randomly generated bombs for the game
    private SelectLevelPage selectLevel;       //the select level page that's displayed once player clicks "Select level"
    private AudioClip sound;                   //enables sound effects for the buttons
    /*-------------------------------------------------------------------------------------------------------------------------
    Constructor which makes the main menu by creating the frame, adding images and adding buttons.
    Rather than having to create a new SelectLevelPage whenever user clicks play, a SelectLevelPage is passed in as an argument
     -------------------------------------------------------------------------------------------------------------------------*/
    public MainGame(SelectLevelPage selectPage) {
        super("Main Menu");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        allBombs=new Bomb[10];
        selectLevel=selectPage;

        ImageIcon background=new ImageIcon("images/main menu back.png");	//adding a background image to main menu
        JLabel menuBack=new JLabel(background);
        JLayeredPane mainPage=new JLayeredPane();                                  //layered pane to which the background image and buttons are added
        mainPage.setLayout(null);
        menuBack.setSize(800,600);
        menuBack.setLocation(0,0);

        playBut=new JButton("Bombs");                                         //making a play button that changes colour when hovering over it
        playBut.addActionListener(this);
        playBut.addMouseListener(this);
        playBut.setFont(new Font("Special Elite",Font.BOLD,50));
        playBut.setBackground(new Color(98,28,32));
        playBut.setForeground(Color.BLACK);
        playBut.setSize(220,60);
        playBut.setLocation(450,365);
        playBut.setFocusPainted(false);
        playBut.setBorderPainted(false);

        infoBut=new JButton("Manual");                                      //creating a button that opens html manual
        infoBut.addActionListener(this);
        infoBut.addMouseListener(this);
        infoBut.setFont(new Font("Special Elite",Font.BOLD,45));
        infoBut.setBackground(new Color(45,60,100));
        infoBut.setForeground(Color.BLACK);
        infoBut.setSize(220,60);
        infoBut.setLocation(125,365);
        infoBut.setFocusPainted(false);
        infoBut.setBorderPainted(false);

        mainPage.add(menuBack,JLayeredPane.DEFAULT_LAYER);                      //adding buttons and background image
        mainPage.add(playBut,JLayeredPane.DRAG_LAYER);
        mainPage.add(infoBut,JLayeredPane.DRAG_LAYER);
        add(mainPage);
        try{                                                                    //loading the audio file for button sound effects
            File soundFile=new File("button click.wav");
            sound=Applet.newAudioClip(soundFile.toURI().toURL());
        }
        catch(MalformedURLException e){
            System.out.println("Can't find audio file");
        }
        setVisible(true);
    }
    /*-----------------------------------------------------
     This method changes the frame when a button is clicked
     *-----------------------------------------------------*/
    public void actionPerformed(ActionEvent evt) {
        Object source=evt.getSource();
        sound.play();
        if(source==playBut){				//when play button is clicked, the main menu is no longer visible and the level selection frame is shown
            setVisible(false);
            selectLevel.start();            //this makes the select level page visible
        }
        else if(source==infoBut){			//when instructions button is clicked, html manual is opened
            try{                            //instructional code found at: https://stackoverflow.com/questions/20517434/how-to-open-html-file-using-java
                File htmlFile=new File("bombmanual.html");
                Desktop.getDesktop().browse(htmlFile.toURI());
            }
            catch(IOException e){
                System.out.println("Can't find html file");
            }
        }
    }
    /*-------------------------------------------------------------------
    This method makes button text white when mouse hovers over the button
    -------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e){
        Object source=e.getSource();
        if(source==playBut){
            playBut.setForeground(Color.WHITE);
        }
        if(source==infoBut){
            infoBut.setForeground(Color.WHITE);
        }
    }
    /*-------------------------------------------------------------------------------
    This method makes the button text black when mouse isn't hovering over the buttons
    ---------------------------------------------------------------------------------*/
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source == playBut) {
            playBut.setForeground(Color.BLACK);     //setForeground() controls text colour
        }
        if(source==infoBut){
            infoBut.setForeground(Color.BLACK);
        }
    }
    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}

    public static void main(String[]args){
        Bomb[] allBombs=new Bomb[10];                       //the bombs are only made once, so they are created in main method
        Random rand=new Random();
        for(int i=0;i<10;i++){                              //making 10 bombs with random modules
            int[] randomModules=new int[i+1];               //the number of modules in a bomb equals the level (1 to 10)
            for(int j=0;j<i+1;j++){                         //Adding random numbers from 1-4 to randomModules[]. These numbers are interpreted as constants in Modules class and are transformed into corresponding modules.
                int module=rand.nextInt(4)+1;
                randomModules[j]=module;
            }
            allBombs[i]=new Bomb(randomModules);
        }
        /*int[] moduleTypes=new int[1];                                                   //remove this code once all modules are implelemented
        Modules wireTest=new Modules(2,100,100);
        moduleTypes[0]=wireTest.getType();
        allBombs[0]=new Bomb(moduleTypes); */                          //remove up until here
        SelectLevelPage selectPage=new SelectLevelPage(0,allBombs);
        new MainGame(selectPage);
    }
}
/*----------------------------------------------------------------------------------------------------------------------
 This class creates the frame where players select a level to play.
 The frame uses cardLayout, where each panel is a BookPage Object that displays information about a level.
 Since the select level page has many fields and processes in its construction, it's only created once in the main method.
 *----------------------------------------------------------------------------------------------------------------------*/
class SelectLevelPage extends JFrame implements ActionListener,MouseListener{
    private JPanel completeBook;				    //JPanel that stores all the other panels in cardLayout
    private CardLayout cLayout;
    private BookPage[] pages;						//The Objects that represent the pages of the book. The Array is used to update the displayed panel when a button is clicked
    private BookPage currentPage;					//The current page being shown. This is used to update the panel's interface.
    private Timer myTimer;                         //fires every 10 milliseconds and causes the interface to update
    private JButton returnBut,playBut;			    //buttons that bring user to main menu and start the game
    private JButton[] levelBut;						//Array that stores the next/previous buttons to flip between pages
    private int level;                             //an index of pages[] that's used when constructing GameFrame. index+1 gives a number from 1 to 10.
    private Bomb[] allBombs;                       //the Array of 10 bombs. Each bomb is assigned to a BookPage Object so it belongs to that level.
    private AudioClip pageSound,buttonSound;       //sound effects for clicking next level, play, or return buttons
    private CustomPage custom;                     //the page where users customize their own bomb
    private boolean creatingCustom;                //indicates whether or not user is creating a custom bomb

    /*----------------------------------------------------------------------------------------------------------------------
     Constructor which makes the card layout, buttons, and BookPage Objects
     "displayedLevel" is an index of levelBut and pages. It controls which level page is initially shown (level 1 by default)
     *----------------------------------------------------------------------------------------------------------------------*/
    public SelectLevelPage(int displayedLevel,Bomb[] bombs){
        super("Select Level");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cLayout=new CardLayout();
        level=displayedLevel;
        completeBook=new JPanel(cLayout);
        pages=new BookPage[10];
        myTimer=new Timer(10,this);
        levelBut=new JButton[11];
        allBombs=bombs;
        creatingCustom=false;

        returnBut=new JButton("Main menu");         //The location of the return and play buttons is constant for all pages, so they're created here.
        returnBut.addActionListener(this);            //On the other hand, the buttons for the levels changes location depending on the displayed panel, so they're not designated a location here
        returnBut.addMouseListener(this);
        returnBut.setSize(150,50);
        returnBut.setLocation(0,510);
        returnBut.setFont(new Font("Special Elite",Font.BOLD,20));
        returnBut.setBackground(new Color(46,32,28));
        returnBut.setForeground(Color.WHITE);
        returnBut.setFocusPainted(false);
        returnBut.setBorderPainted(false);

        playBut=new JButton("Play");
        playBut.addActionListener(this);
        playBut.addMouseListener(this);
        playBut.setSize(200,50);
        playBut.setLocation(300,510);
        playBut.setFont(new Font("Special Elite",Font.BOLD,35));
        playBut.setBackground(new Color(255,247,152));
        playBut.setForeground(Color.BLACK);
        playBut.setFocusPainted(false);

        for(int i=0;i<11;i++){							                //creating 11 buttons that cause a different BookPage of pages[] to be shown
            JButton newBut=new JButton("Level "+(i+1));
            if(i==10){                                                  //the last button leads to a custom level where player constructs their own bomb
                newBut=new JButton("Custom");
            }
            newBut.addActionListener(this);                          //the size, text colour, and background colour of all buttons is constant
            newBut.addMouseListener(this);
            newBut.setFont(new Font("Special Elite",Font.BOLD,25));
            newBut.setBackground(new Color(255,247,152));
            newBut.setForeground(Color.BLACK);
            newBut.setSize(150,50);
            newBut.setFocusPainted(false);
            levelBut[i]=newBut;
        }

        for(int i=0;i<10;i++){                                          //Creating 10 BookPage Objects. These panels display information about a level, such as top score and how many modules there are.
            BookPage newPage=new BookPage(i+1,allBombs[i]);
            pages[i]=newPage;
            completeBook.add(newPage,Integer.toString(i+1));         //the String assigned to each level is a number from 1 to 10
        }
        for(BookPage page:pages){
            page.unlock();
        }
        custom=new CustomPage();                                        //creating the customizable page
        completeBook.add(custom,"11");

        try{                                                            //loading sound effects for next page, return, and play buttons
            File pageFile=new File("page flip.wav");
            File buttonFile=new File("button click.wav");
            pageSound=Applet.newAudioClip(pageFile.toURI().toURL());
            buttonSound=Applet.newAudioClip(buttonFile.toURI().toURL());
        }
        catch(MalformedURLException e){
            System.out.println("Can't find audio file");
        }
        unlockLevel(0);                                    //the first level is unlocked by default
        custom.unlock();
        showPage(displayedLevel);                                   //displaying the level indicated by the parameter
        getContentPane().add(completeBook);
    }
    /*-----------------------------------------------------------------------------------------
    This method unlocks a level to play, called whenever user wins a level by defusing the bomb
     ------------------------------------------------------------------------------------------*/
    public void unlockLevel(int pageIndex){
        pages[pageIndex].unlock();
    }
    /*-----------------------------------------------------------------------------------------------------------------------------------------------
    This method shows a specific page and adds buttons to the page.
    From testing, it was found that buttons occasionally disappeared from panels, so solution was to add buttons whenever a page is about to be shown
    Credit goes to Zulaikha, who suggested this solution to Keith.
     -----------------------------------------------------------------------------------------------------------------------------------------------*/
    public void showPage(int pageIndex){
        currentPage=pages[pageIndex];
        if(pageIndex==0){                                                       //the first page is special because it lacks a previous button
            currentPage.addButtons(null,levelBut[1],returnBut,playBut);
        }
        else{
            currentPage.addButtons(levelBut[pageIndex-1],levelBut[pageIndex+1],returnBut,playBut);
        }
        cLayout.show(completeBook,Integer.toString(pageIndex+1));
    }
    /*--------------------------------------------------------------------------------------
     This method starts the timer, causing the interface to be updated in actionPerformed().
     It also makes the frame visible.
     *-------------------------------------------------------------------------------------*/
    public void start(){
        setVisible(true);
        myTimer.start();
    }
    /*-----------------------------------------------------------------------------
     *This method changes the page that's shown whenever the player clicks a button
     *----------------------------------------------------------------------------*/
    public void actionPerformed(ActionEvent e){
        Object source=e.getSource();
        if(source==returnBut){				            //return button was clicked, so show the main menu
            buttonSound.play();                         //playing a sound effect
            setVisible(false);
            new MainGame(this);
        }
        if(source==playBut && creatingCustom && custom.getTotalNumMod()>0 ){         //player has clicked play on the custom page and they have a valid bomb
            buttonSound.play();
            setVisible(false);
            GameFrame actualGame=new GameFrame(custom.getBomb(),9,this);
            actualGame.start();
        }
        if(source==playBut && currentPage.getLockedStatus().equals("Unlocked")){       //player has clicked play on a normal level page that's unlocked, so start the game
            buttonSound.play();
            setVisible(false);
            GameFrame actualGame = new GameFrame(currentPage.getBomb(), level, this);
            actualGame.start();
        }
        if(source==myTimer){                        //updating the level page's graphics
            if(creatingCustom){
                custom.repaint();
            }
            else {
                currentPage.repaint();
            }
        }
        if(source==levelBut[10]){                   //player wants to go to customizable level page
            creatingCustom=true;
            custom.addButtons(levelBut[9],returnBut,playBut);
            cLayout.show(completeBook,"11");
        }
        else{								        //detecting which level button was clicked and showing the corresponding level page
            for(int i=0;i<10;i++){                  //the last button in levelBut[] is excluded because this loop only works for BookPage Objects, not CustomPage Objects
                if(source==levelBut[i]){
                    creatingCustom=false;
                    pageSound.play();
                    showPage(i);                    //adding necessary buttons and displaying the page
                }
            }
        }
    }
    /*-------------------------------------------------------------------
    This method makes button text white when mouse hovers over the button
    -------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e){
        Object source=e.getSource();
        if(source==playBut){
            playBut.setForeground(Color.RED);
        }
        if(source==returnBut){
            returnBut.setForeground(Color.RED);
        }
        for(JButton but:levelBut){
            if(source==but){
                but.setForeground(Color.RED);
            }
        }
    }
    /*-------------------------------------------------------------------------------
    This method makes the button text black when mouse isn't hovering over the buttons
    ---------------------------------------------------------------------------------*/
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source == playBut) {
            playBut.setForeground(Color.BLACK);     //setForeground() controls text colour
        }
        if(source==returnBut){
            returnBut.setForeground(Color.WHITE);
        }
        for(JButton but:levelBut){
            if(source==but){
                but.setForeground(Color.BLACK);
            }
        }
    }
    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
}
/*-------------------------------------------------------------------------------------------------------------------------------------------------
 This class makes a panel designated to the custom level. Players construct their own bomb by using arrow keys to specify which modules they want.
 This is separate from the BookPage Objects because those Objects have Bombs designated to them in their constructors.
 Here, a bomb is only created once the player has chosen all their modules and clicks play.
 -----------------------------------------------------------------------------------------------------------------------------------------------*/
class CustomPage extends JPanel implements ActionListener{
    private String locked;                          //tells player if level can be played. String because paintComponent writes either "Locked" or "Unlocked" on the panel
    private Image back;                             //background image
    private int[] numMod;                           //an Array of length 4 where each element indicates how many modules of a specific type the bomb will have
    private BasicArrowButton[] allButtons;          //the buttons which player uses to increase or decrease number of modules
    private int totalNumMod;                        //Tracks the total number of modules on the bomb. It must be greater than 0 and less than 11.

    /*---------------------------------------------------------------------------
     *Constructor which creates all the buttons that alter the number of modules.
     *--------------------------------------------------------------------------*/
    public CustomPage(){
        setLayout(null);
        locked="Locked";
        totalNumMod=0;
        numMod=new int[4];
        allButtons=new BasicArrowButton[8];                                         //buttons alternate between increasing and decreasing functions. For example, allButtons[0] increases number of wire modules, allButtons[1] decreases number of wires.
        back=new ImageIcon("images/game back.png").getImage();

        for(int i=0;i<8;i++){                                                       //creating the 8 buttons
            BasicArrowButton newBut=new BasicArrowButton(BasicArrowButton.NORTH);
            if(i%2==1){                                                             //odd indices contain buttons that decrease number of modules
                newBut=new BasicArrowButton(BasicArrowButton.SOUTH);
            }
            newBut.setBackground(new Color(255, 247, 152));
            newBut.setForeground(Color.black);
            newBut.setSize(50,35);
            newBut.setLocation(600,90+35*i);
            newBut.addActionListener(this);
            allButtons[i]=newBut;
            add(newBut);
        }
    }
    /*------------------------------------------------------------------------------------------------------------------
    This method is used by SelectLevelPage once player has clicked "Play" to verify that the Bomb has at least 1 module.
    This covers the case of the player reaching the custom page and immediately clicking play.
     -------------------------------------------------------------------------------------------------------------------*/
    public int getTotalNumMod(){
        return totalNumMod;
    }
    /*------------------------------------------------------------------------------------
    This method is used by paintComponent() to tell player if level is locked or unlocked
    ------------------------------------------------------------------------------------*/
    public String getLockedStatus(){
        return locked;
    }
    /*--------------------------------------------------------------------------
    Once a level is unlocked, this method updates the interface to reflect that
     -------------------------------------------------------------------------*/
    public void unlock(){
        locked="Unlocked";
    }
    /*------------------------------------------------------------------------------------------------------------------------
    This method is used by SelectLevelPage to get the Bomb the player has made and start the actual game.
    At this moment, the field numMod looks something like this: {3,2,3,1}. That means they want 3 wires, 2 buttons, and so on.
    The Bomb constructor needs an int[] Array which contains numbers 1 to 4 because those will be interpreted as constants that
    prompt the creation of specific modules. The input in the Bomb parameter must look like this: {1,1,1,2,2,3,3,3,4}.
    This method creates the correct Array and uses it to create the Bomb the player designed.
     ------------------------------------------------------------------------------------------------------------------------*/
    public Bomb getBomb(){
        int[] inputArray=new int[totalNumMod];          //the Array that will be passed into Bomb constructor must be large enough to house all the modules
        int index=0;
        for(int modType=0;modType<4;modType++){         //looking at the sample Array in the header comment, this loop will go through each element in numMod[] and add however many ones or twos are specified to inputArray[]
            for(int j=0;j<numMod[modType];j++){
                inputArray[index]=modType+1;            //must add 1 because modType values range from 0 to 3, but we want 1 to 4.
                index++;                                //this advances through inputArray
            }
        }
        return new Bomb(inputArray);
    }
    /*-----------------------------------------------------------------------------------------------------------------------
    This method detects when player has clicked on an increase/decrease button and changes the number of modules accordingly
     ----------------------------------------------------------------------------------------------------------------------*/
    public void actionPerformed(ActionEvent e){
        Object source=e.getSource();
        for(int i=0;i<8;i++){                                               //going through all the buttons until the source is found
            if(source==allButtons[i]){
                if(i%2==0){                                                 //user wants to increase number of modules
                    numMod[i/2]=determineNum(true,i/2);
                }
                else{                                                       //user wants to decrease number of modules
                    numMod[i/2]=determineNum(false,i/2);
                }
            }
        }
    }
    /*------------------------------------------------------------------------------------------------------------------------
    This method determines how many modules there are after user clicks an increase/decrease button.
    It makes sure that the total number of modules is greater than one and doesn't exceed ten.
    "increase" indicates whether the player clicked an increase or decrease button.
    "numModIndex" is an index of numMod[] which is used to update an element in the Array to reflect the new number of modules.
     -------------------------------------------------------------------------------------------------------------------------*/
    public int determineNum(boolean increase,int numModIndex){
        if(increase){                               //player wants to increase the number of modules
            if(totalNumMod+1<=10){                  //the module number only increases if it won't exceed ten
                totalNumMod++;
                return numMod[numModIndex]+1;
            }
        }
        if(!increase){                              //player wants to decrease the number of modules
            if(totalNumMod-1>=1){                   //there must be at least one module
                totalNumMod--;
                return numMod[numModIndex]-1;
            }
        }
        return numMod[numModIndex];                 //the number of modules can't be changed, so the corresponding value in numMod doesn't change
    }
    /*-------------------------------------------------------------------------------------------------------
     This method adds specific buttons to the page when it is displayed to make sure buttons don't disappear.
     "prev" goes back a level, "returnBut" returns player to main menu, "playBut" starts the game.
     Called in SelectLevelPage actionPerformed() whenever the custom page is displayed.
     The detection of when these buttons are clicked and the consequent processes are handled by SelectLevelPage.
     *---------------------------------------------------------------------------------------------------------*/
    public void addButtons(JButton prev,JButton returnBut,JButton playBut){
        if(!isAncestorOf(returnBut)){			//The buttons are added only if the JPanel doesn't already have the button. Credit to Zulaikha who told Keith he must check this.
            add(returnBut);
        }
        if(!isAncestorOf(playBut)){
            add(playBut);
        }
        if(!isAncestorOf(prev)){
            prev.setLocation(150,510);
            add(prev);
        }
    }
    /*----------------------------------------------------------------------------
     This method is used to display how many modules the player has chosen so far
     *--------------------------------------------------------------------------*/
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(back, 0, 0, this);              //adding background image and a yellow rectangle that resembles paper
        g.setColor(new Color(255, 247, 152));
        g.fillRect(150, 0, 500, getHeight());

        g.setColor(new Color(0, 0, 0));                  //drawing lines so the yellow rectangle looks like lined paper
        for (int y = 90; y < 550; y += 70) {
            g.drawLine(150, y, 650, y);
        }
        g.setFont(new Font("Special Elite", Font.BOLD, 40));
        g.drawString("Custom", 320, 50);
        g.setFont(new Font("Special Elite", Font.PLAIN, 30));
        g.drawString("Max total of 10 modules",220,80);

        g.setFont(new Font("Special Elite", Font.PLAIN, 35));   //displaying number of modules
        g.drawString("Buttons: "+numMod[0], 310, 140);
        g.drawString("Wires: "+numMod[1], 320, 210);
        g.drawString("Symbols: "+numMod[2], 300, 280);
        g.drawString("Simon says: "+numMod[3], 280, 350);
        g.drawString("Status: " + locked, 260, 420);
    }
}
/*---------------------------------------------------------------------------------------------------------------------------------
 This class makes a panel designated to a specific level. The ten level pages are created in SelectLevelPage constructor.
 Custom Objects are required because every page has different buttons. These Objects facilitate the process of adding unique buttons.
 *---------------------------------------------------------------------------------------------------------------------------------*/
class BookPage extends JPanel{
    private int pageNum;				//the level that the page represents (1 to 10)
    private Bomb bomb;                  //the bomb that's played for a specific level
    private String locked;              //String because paintComponent writes either "Locked" or "Unlocked" on the panel
    private Image back;                 //background image
    private int[] modFrequency;         //contains number of wires, simon says, button, and symbols modules

    /*---------------------------------------------------------------------------------
     Constructor where "level" is the level the page represents (a number from 1 to 10.
     "inputBomb" is the bomb assigned to the level.
     *-------------------------------------------------------------------------------*/
    public BookPage(int level,Bomb inputBomb){
        pageNum=level;
        setLayout(null);
        bomb=inputBomb;
        locked="Locked";
        back=new ImageIcon("images/game back.png").getImage();
        modFrequency=new int[4];                                                //{num buttons, num wires, num simon says, num symbols}

        int[] allModules=bomb.getModules();                                     //contains a series of numbers ranging from 1 to 4 that represent the modules
                                                                                //the frequency of these numbers must be found in order for BookPage to display how many modules there are
        HashMap<Integer,Integer> numFrequency=new HashMap<Integer,Integer>();   //the first value is the integer, the second value is its frequency
        for(int i:allModules){
            if(!numFrequency.containsKey(i)){                                   //if this value is unique, put it in the map with a frequency of 1
                numFrequency.put(i,1);
            }
            else{                                                               //if the value isn't unique, put it in the map and increase the current frequency of that number
                int currentFrequency=numFrequency.get(i);
                numFrequency.put(i,currentFrequency+1);
            }
        }
        for(Map.Entry<Integer,Integer> numPair:numFrequency.entrySet()){        //putting the values into an Array so paintComponent() can display how many of each module there are
            int moduleType=numPair.getKey();                                    //A number from 1 to 4. Since modFrequency[] has a size of 4, subtract 1 to use as an index
            int frequency=numPair.getValue();                                   //how many of that module is in the bomb
            modFrequency[moduleType-1]=frequency;
        }
    }
    /*------------------------------------------------------------------------------------
    This method is used by paintComponent() to tell player if level is locked or unlocked
    ------------------------------------------------------------------------------------*/
    public String getLockedStatus(){
        return locked;
    }
    /*--------------------------------------------------------------------------
    Once a level is unlocked, this method updates the interface to reflect that.
    Called whenever user defuses a bomb.
     -------------------------------------------------------------------------*/
    public void unlock(){
        locked="Unlocked";
    }
    /*----------------------------------------------------------------------
    This method returns the bomb that belongs to a certain level.
    Used to create GameFrame in SelectLevelPage once play button is clicked
     ----------------------------------------------------------------------*/
    public Bomb getBomb(){
        return bomb;
    }
    /*---------------------------------------------------------------------------------------------------------------------
     This method adds specific buttons to the page
     "prev" goes back a level, "next" advances a level,"returnBut" returns player to main menu, "playBut" starts the game
     Called in SelectLevelPage actionPerformed() whenever the displayed panel changes
     --------------------------------------------------------------------------------------------------------------------*/
    public void addButtons(JButton prev,JButton next,JButton returnBut,JButton playBut){
        if(!isAncestorOf(returnBut)){			//the buttons are added only if the JPanel doesn't already have the button
            add(returnBut);
        }
        if(!isAncestorOf(playBut)){
            add(playBut);
        }
        if(prev!=null){							//the first level page lacks a previous button, so this avoids a null pointer exception
            if(!isAncestorOf(prev)){
                prev.setLocation(150,510);
                add(prev);
            }
        }
        if(!isAncestorOf(next)){
            next.setLocation(500,510);
            add(next);
        }
    }
    /*-------------------------------------------------------------------------------------------------------------------
     This method displays information about the bomb for a level: number of modules and current best time
     Since bombs are randomly created every time the program is run, we need a general method of displaying information,
     rather than blitting a picture that contains information about the bomb on each page of the book.
     *-------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(back, 0, 0, this);
        g.setColor(new Color(255, 247, 152));                       //drawing a yellow "sticky note" on the panel
        g.fillRect(150, 0, 500, getHeight());

        g.setColor(new Color(0, 0, 0));
        for (int y = 90; y < 550; y += 70) {
            g.drawLine(150, y, 650, y);
        }
        g.drawLine(400, 510, 400, 510);
        g.setFont(new Font("Special Elite", Font.BOLD, 50));
        g.drawString("Level " + pageNum, 300, 70);                   //displaying level number

        g.setFont(new Font("Special Elite", Font.PLAIN, 35));
        g.drawString("Buttons: "+modFrequency[0], 310, 140);              //replace "10" with +numMod[0] and so on
        g.drawString("Wires: "+modFrequency[1], 320, 210);                //modFrequency[1]
        g.drawString("Symbols: "+modFrequency[2], 300, 280);              //modFrequency[2]
        g.drawString("Simon says: "+modFrequency[3], 280, 350);           //modFrequency[3]
        g.drawString("Status: " + locked, 260, 420);
        if (bomb != null) {
            g.drawString("Best score: " + bomb.getHighScore(), 250, 490);
        }
    }
}

/*-------------------------------------------------------------------------------------
This class controls the gameplay by displaying and updating a Bomb.
It also prompts the creation of a game over frame once the bomb explodes or is defused.
 -------------------------------------------------------------------------------------*/
class GameFrame extends JFrame implements ActionListener,MouseListener{
    private Timer myTimer;                      //controls when the bomb is updated
    private int levelIndex;                     //an index from 0 to 9 which controls which level is unlocked once a bomb is defused
    private Bomb bomb;                          //the bomb being played
    private SelectLevelPage selectLevel;        //once a bomb is defused, this Object is used to unlock the next level
    private JButton flipBut;                    //controls which side of the Bomb is displayed
    private Clip bgMusic;                       //background music
    private AudioClip timerBeep;                //when there are less than 10 seconds left, a beeping nonise plays every second
    /*---------------------------------------------------------------------------------------------------
    Constructor which makes the frame
    "thisBomb" is the Bomb that belongs to the level being played
    "index" is an index of BookPages (0 to 9).
    "levelPage" is a SelectLevelPage Object. Necessary because from here, GameOverFrame is made.
    From GameOverMade, main menu can be accessed, and main menu constructor needs a SelectLevelPage Object.
     -----------------------------------------------------------------------------------------------------*/
    public GameFrame(Bomb thisBomb, int index,SelectLevelPage levelPage){
        super("Game Screen");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        levelIndex=index;
        selectLevel=levelPage;
        myTimer=new Timer(10,this);
        bomb=thisBomb;
        JLayeredPane thisFrame=new JLayeredPane();
        thisFrame.setLayout(null);
        bomb.setBounds(0,0,800,600);          //necessary because the Bomb (which is a JPanel) and flipBut are added at specific locations

        flipBut=new JButton("Flip");                        //making a button that changes the displayed side of the Bomb
        flipBut.setSize(100,100);
        flipBut.setLocation(700,0);
        flipBut.addActionListener(this);
        flipBut.addMouseListener(this);
        flipBut.setFont(new Font("Special Elite",Font.PLAIN,25));
        flipBut.setBackground(new Color(42,22,13));
        flipBut.setForeground(Color.WHITE);
        flipBut.setFocusPainted(false);
        flipBut.setBorderPainted(false);

        thisFrame.add(bomb,JLayeredPane.DEFAULT_LAYER);
        thisFrame.add(flipBut,JLayeredPane.DRAG_LAYER);
        add(thisFrame);
        try{													            //instructions for implementing music were found at: https://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
            File musicFile=new File("background music.aiff");		//reading the music file
            AudioInputStream audioInput=AudioSystem.getAudioInputStream(musicFile);
            bgMusic=AudioSystem.getClip();							        //using Clip's methods, music can be started and stopped
            bgMusic.open(audioInput);
        }
        catch(IOException ex){
            System.out.println("Can't find music file.");
        }
        catch(UnsupportedAudioFileException ex){
            System.out.println("Change the file type.");
        }
        catch(LineUnavailableException ex){
            System.out.println("Line unavailable.");
        }
        try{                                                                    //loading the audio file for timer beeping sound effects
            File soundFile=new File("timer beep.wav");
            timerBeep=Applet.newAudioClip(soundFile.toURI().toURL());
        }
        catch(MalformedURLException e){
            System.out.println("Can't find audio file");
        }
        setVisible(true);
    }
    /*-----------------------------------------------------------------
    This method starts the Timer, which enables the game to be updated.
    It also starts the background music and loops it infinitely.
     ----------------------------------------------------------------*/
    public void start(){
        bgMusic.start();
        bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        myTimer.start();
    }
    /*------------------------------------------------------------------------------------------------------------------------------------------------------------
    This method updates the game whenever myTimer fires, creates a GameOverFrame when the game is done, and changes displayed side of Bomb when flipBut is clicked.
     -------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public void actionPerformed(ActionEvent evt){
        Object source=evt.getSource();
        if(source==myTimer){
            bomb.updateState();
            bomb.repaint();
        }
        if(bomb.getTime()<10000 && bomb.getTime()%1000==0){                         //if there's less than 10 seconds left, a beeping noise is played every second
            timerBeep.play();
        }
        if(source==flipBut){                                                        //this makes the bomb show either the front or back side
            bomb.changeFace();
        }
        if(bomb.getTime()==0 || bomb.getStrikes()==3 || bomb.isDefused()){          //game ends if time runs out, player makes 3 mistakes, or player completes the level
            myTimer.stop();
            bgMusic.stop();
            setVisible(false);
            if(bomb.isDefused()){                                                   //the player won the level
                                                                                    //Since that's the last level, Math.min is used to avoid an invalid index for pages[] in SelectLevelPage.
                new GameOverFrame(bomb,levelIndex,selectLevel,bomb.getScore());
            }
            else{                                                                   //the player failed the level
                new GameOverFrame(bomb,levelIndex,selectLevel,"You died");
            }
            bomb.reset();                                                           //this enables the bomb to be played again
        }
    }
    /*-----------------------------------------------------------------------
    This method makes button text red when mouse hovers over the flip button.
    ------------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e){
        Object source=e.getSource();
        if(source==flipBut){
            flipBut.setForeground(Color.RED);
        }
    }
    /*---------------------------------------------------------------------------
    This method makes button text white when mouse isn't hovering over the button.
    ------------------------------------------------------------------------------*/
    public void mouseExited(MouseEvent e){
        Object source=e.getSource();
        if(source==flipBut){
            flipBut.setForeground(Color.WHITE);
        }
    }
    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
}
/*---------------------------------------------------------------------------------------------------
This class makes a Bomb Object which has an Array of Modules as an attribute.
The Bomb detects when the player clicks on a module and then tells the module to handle interactions.
The Bomb also tells all its modules to draw themselves using methods in Modules class.
When the game is over, the Bomb tells all its modules to reset themselves.
 ----------------------------------------------------------------------------------------------------*/
class Bomb extends JPanel implements MouseListener{
    public static final int SIMON=4;        //a constant for clarity when dealing with simon says modules
    private int numMod;                     //the number of modules on this bomb
    private Modules[][] minigames;          //2D Array that contains the modules located on front and back of the Bomb
    private int face;                       //Controls which side of the Bomb is shown. 0 for the front, 1 for the back.
    private int mouseX,mouseY,strikes;      //mouseX and mouseY are the mouse coordinates. strikes is how many mistakes the player has made.
    private Modules currentInteract;        //the module that is being interacted with right now
    private Image back,strikePic;           //images for background, and an X for strikes
    private TimeModule timer;               //displays the time left to defuse the bomb
    private boolean defused;                //tells GameFrame whether or not the Bomb is defused
    private int bestTime;                   //the lowest time it took the player to complete the level
    private int[] modConstants;             //used by BookPage to see how many of each module type there are
    /*-----------------------------------------------------------------------------------------------------
    Constructor which makes all the Modules for the Bomb.
    "modTypes" contains numbers from 1 to 4 that're interpreted as constants when a Modules Object is created.
    ------------------------------------------------------------------------------------------------------*/
    public Bomb(int[] modTypes){
        addMouseListener(this);
        defused=false;
        bestTime=0;
        numMod=modTypes.length;
        back=new ImageIcon("images/game back.png").getImage();
        strikePic=new ImageIcon("images/strike.png").getImage();
        face=strikes=0;                                                                   //the front of the bomb is shown by default
        int[][] cornerCoord={{100,100},{100,300},{300,100},{500,100},{500,300}};          //each module box is drawn as a rectangle, so these are the rectangles' coordinates
        modConstants=modTypes;

        if(numMod>5){                                           //A maximum of 5 modules can fit on one side of the Bomb. In this case, modules need to be assigned to the back of the Bomb
            minigames=new Modules[5][2];                        //Each element has two spots: the first one contains a module on the front of the bomb, the second one is for the back
        }
        else {
            minigames = new Modules[modTypes.length][1];
        }
        for(int i=0;i<numMod;i++) {                             //assigning modules to the new 2D Array
            if (i<5) {
                minigames[i][0] = new Modules(modTypes[i],cornerCoord[i][0],cornerCoord[i][1]);
            }
            else {                                              //the front of the bomb only has room for 5 modules, so we go back and start adding modules to the back
                minigames[i - 5][1] = new Modules(modTypes[i],cornerCoord[i-5][0],cornerCoord[i-5][1]);
            }
        }
        int totalTime=1000;                                         //creating a timer
        for(Modules[] mod:minigames){                               //Each module has a designated time to solve it. This method goes through all the modules and gets those time values
            totalTime+=mod[0].getAllottedTime();
            if(mod.length==2 && mod[1]!=null){
                totalTime+=mod[1].getAllottedTime();
            }
        }
        timer=new TimeModule(300,300,totalTime);
    }
    /*------------------------------------------------------------------------------------
    This method is used by BookPage to see how many of each module type there are.
    Returns an int Array containing numbers 1 to 4, such as: {1,1,2,2,2,4}
    BookPage will take these constants and display how many of each module type a Bomb has.
     ------------------------------------------------------------------------------------*/
    public int[] getModules(){
        return modConstants;
    }
    /*------------------------------------------------------------------------------
    This method is used by GameFrame to see how much time is left to defuse the Bomb
     ------------------------------------------------------------------------------*/
    public int getTime(){
        return timer.getTime();
    }
    /*-------------------------------------------------------------------------------------------------
    This method takes in a time value in milliseconds and turns it into a String in the format, "00:30"
    Used by getScore() and getHighScore() to convert the player's completion times into Strings
     -------------------------------------------------------------------------------------------------*/
    public String convertTime(int timeVal){
        int min=timeVal/60000;                                                                        //determining number of minutes in the millisecond value
        int seconds=(timeVal-(min*60000))/1000;
        String output=String.format("%2d:%2d",(int)min,seconds).replace(" ","0");   //formatting the output, help from Aaron Li
        return output;
    }
    /*------------------------------------------------------------------------------------------------------------------------------
    This method returns how much time it took the player to finish the level and makes this the player's best score if it's a record.
    Returns a String in the format of, 00:30
    Called by GameFrame once it has been verified that player won the level.
    -------------------------------------------------------------------------------------------------------------------------------*/
    public String getScore(){
        int completionTime=timer.getCompletionTime();       //returns a time value in milliseconds
        if(bestTime==0){
            bestTime=completionTime;
        }
        else {
            bestTime = Math.min(bestTime, completionTime);  //the lowest time value is wanted
        }
        return convertTime(completionTime);
    }
    /*--------------------------------------------------------------------
    This method returns the best score for this level in a String format.
    It's called by BookPage when displaying information about a Bomb.
     ---------------------------------------------------------------------*/
    public String getHighScore(){
        return convertTime(bestTime);
    }
    /*---------------------------------------------------
    Used by GameFrame to see if the bomb has been defused
    ---------------------------------------------------*/
    public boolean isDefused() {
        return defused;
    }
    /*----------------------------------------------------------------------------
    This method is used by GameFrame to see how many mistakes the player has made
    -----------------------------------------------------------------------------*/
    public int getStrikes(){
        return strikes;
    }
    /*-----------------------------------------------------------------------------------------
    This method changes the displayed side of the bomb when flip button is clicked in GameFrame.
    Changing "face" will change the modules that're drawn
    ------------------------------------------------------------------------------------------*/
    public void changeFace() {
        if (minigames[0].length == 2) {         //if the bomb has enough modules for 2 sides
            face = 1 - face;                    //if face is currently 0, it must change to 1 and vice versa
        }
        else{
            face=0;
        }
    }
    /*-----------------------------------------------------------------------------------------------------
    This method updates the Bomb whenever the Timer fires in GameFrame class.
    It also checks how many modules have been defused, so GameFrame knows when the level has been completed
     -----------------------------------------------------------------------------------------------------*/
    public void updateState(){
        timer.subtractTime();                       //this updates the countdown
        int defusedCount=0;                         //a counter used to track how many modules have been defused
        for(Modules[] mod:minigames){               //going through each module and seeing if it's defused
            if(mod[0].checkDefused()){              //a defused module has been found
                defusedCount++;
            }
            if(mod.length==2 && mod[1]!=null){      //checking modules on the back side of the bomb
                if(mod[1].checkDefused()){          //a defused module on the back of the bomb has been found
                    defusedCount++;
                }
            }
            for(Modules simon:mod){                 //Since simon says needs to know the amount of strikes on the bomb
                if(simon.getType()==SIMON){         //We update the amount of strikes everytime we update state
                    simon.updateStrikes(strikes);
                }
            }
        }
        defused=(defusedCount==numMod);             //seeing if the player has defused all the modules
    }
    /*----------------------------------------------------------------------------------------
    This method updates the panel interface.
    It draws a 3 x 2 grid that represents the bomb and tells all the modules to draw themselves.
    This is called whenever the Timer fires in GameFrame
    ------------------------------------------------------------------------------------------*/
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(211,211,211));       //drawing a grey rectangle on top of which lines will be drawn to form a grid
        g.drawImage(back,0,0,this);
        g.fillRect(100,100,600,400);
        g.setColor(new Color(0,0,0));

        for(int i=100;i<800;i+=200){						//drawing a 3 x 2 grid to represent the bomb
            g.drawLine(i,100,i,500);
        }
        for(int i=100;i<600;i+=200){
            g.drawLine(100,i,700,i);
        }
        timer.draw(g);                                      //displaying the countdown
        for(Modules[] mod:minigames){                       //drawing the modules on the current face
            Modules facingMod=mod[face];
            if(facingMod!=null) {
                facingMod.draw(g);
            }
        }
        for(int i=0;i<strikes;i++){                         //drawing x's to represent number of strikes
            g.drawImage(strikePic,310+60*i,310,this);
        }
    }
    /*----------------------------------------------------------
    This method resets the bomb, enabling it to be played again.
     -----------------------------------------------------------*/
    public void reset(){
        strikes=face=0;                                     //front face shown by default
        defused=false;
        timer.reset();
        for(Modules[] modList:minigames){                   //telling all the modules to reset themselves
            modList[0].reset();
            if(modList.length==2 && modList[1]!=null){
                modList[1].reset();
            }
        }
    }
    /*------------------------------------------------------------------------------------------------------
    This method verifies if the user wants to interact with a module or if they just clicked on empty space.
    It also checks if the user is defusing a module correctly.
    -------------------------------------------------------------------------------------------------------*/
    public void mousePressed(MouseEvent e){
        mouseX=e.getX();
        mouseY=e.getY();
        for(Modules[] mod:minigames){
            Modules facingMod=mod[face];
            if(facingMod!=null) {                                       //when the bomb has 2 sides, some elements in the Array will be null
                if (!facingMod.alreadyDefused(mouseX, mouseY)) {        //user has clicked on a module that has not been defused
                    currentInteract=facingMod;
                    facingMod.startInteraction();                       //this enables the actual gameplay with the module, such as cutting wires
                }
                else {                                                  //only one module is focused on at a time
                    facingMod.setUnfocused();

                }
            }
        }
        if(currentInteract!=null) {                                     //checking if the player is defusing a module correctly
            if (!currentInteract.correctPlayerAction()) {               //this can't be called in updateState() because "strikes" would increase every 10 milliseconds, ending the game instantly
                strikes++;
            }
        }
    }
    /*--------------------------------------------------------------------
    The following methods must be implemented in order to use MouseListener
     --------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}
/*-------------------------------------------------------------------------------------------------------
This class creates a generic module which is then assigned a specific module, such as wires or simon says.
It tells modules to draw themselves and determines when player has clicked on a module
 -------------------------------------------------------------------------------------------------------*/
class Modules {
    public final int BUTTON=1;               //Constants for clarity when dealing with modules
    public final int WIRES=2;
    public final int SYMBOLS=3;
    public final int SIMON=4;

    private Rectangle mod;                  //the module's hitbox
    private int type,x,y,strikes;           //type is one of the constants which indicate the type of module. x and y are coordinates of the hitbox. strikes is used by simon says to update its strikes.
    private boolean defused,isFocused;      //defused indicates if this module has been solved or not. isFocused indicates if user has clicked a particular module
    private boolean correctAction;          //this verifies if the user is playing the module correctly
    private int mouseX,mouseY;              //mouse coordinates

    private Button click;                   //only one of these is assigned a value, the rest are null
    private WireModule cut;
    private Symbols press;
    private Simon pattern;
    /*-------------------------------------------------------------------------------------------------
    Constructor that makes the module
    "modType" is a number from 1 to 4 that indicates what type of module is made based on the constants
    "x" and "y" are the top left corner coordinates of the module's hitbox
    --------------------------------------------------------------------------------------------------*/
    public Modules(int modType,int x,int y){
        type=modType;
        if(type==BUTTON){                               //creating the specific modules indicated by modType
            click=new Button(x,y);
        }
        if(type==WIRES){
            cut=new WireModule(x,y);
        }
        if(type==SYMBOLS){
            press=new Symbols(x,y);
        }
        if(type==SIMON){
            pattern=new Simon(x,y);
        }
        this.x=x;
        this.y=y;
        mod=new Rectangle(x,y,200,200);
        isFocused=false;                                //by default, the module has not been defused yet and it hasn't been clicked on
        defused=false;
        correctAction=true;
        strikes=0;
    }
    /*---------------------------------------------------------------------------------------
    This method is used by Bomb class to see if user is interacting correctly with the module.
    If it's false, a strike will be added in Bomb class.
     --------------------------------------------------------------------------------------*/
    public boolean correctPlayerAction(){
        return correctAction;
    }
    /*----------------------------------------------------------------------------------------
    This method is used by updateState() in Bomb class to update the strikes on simon modules.
    -----------------------------------------------------------------------------------------*/
    public int getType(){
        return type;
    }
    /*------------------------------------------------------------------------------------
    This method changes the isFocused value, meaning user isn't interacting with this box
     ------------------------------------------------------------------------------------*/
    public void setUnfocused(){
        isFocused=false;
    }
    /*---------------------------------------------------------------------------------
    This method checks if the user clicked on a module that hasn't been defused yet
    Called in mousePressed() in Bomb class
    Returns a boolean that tells the game whether or not the module is already defused
    "mouseX" and "mouseY" are the coordinates of where the user clicked
    ----------------------------------------------------------------------------------*/
    public boolean alreadyDefused(int mouseX,int mouseY){
        if(mod.contains(mouseX,mouseY)) {
            this.mouseX=mouseX;
            this.mouseY=mouseY;
            isFocused = true;                       //this enables interaction with the module
            return defused;                         //this will only be returned if the user actually clicked on the module
        }
        return true;                                //this must be the opposite of what "defused" is supposed to be if the module can be played
    }
    /*-----------------------------------------------------------------------------------------------------------------
    This method calls the interaction methods of each module.
    This enables gameplay, such as checking if correct wires were cut, or if Simon says pattern was repeated correctly
    This is called in mousePressed() in Bomb class once it has been verified that the player clicked on a valid module
     ------------------------------------------------------------------------------------------------------------------*/
    public void startInteraction(){
        if(type==WIRES){
            correctAction=cut.interact(mouseX,mouseY);
        }
        if(type==BUTTON){
            correctAction=click.interact(mouseX,mouseY);
        }
        if(type==SYMBOLS){
            correctAction=press.interact(mouseX,mouseY);
        }
        if(type==SIMON){
            correctAction=pattern.interact(mouseX,mouseY,strikes);
        }
    }
    /*------------------------------------------------------------------------
    This method outlines the module box if it's currently focused.
    It also calls the draw methods of the module, which makes it draw itself.
     -------------------------------------------------------------------------*/
    public void draw(Graphics g){
        if(isFocused){                      //if this module is currently being focused on, it's outlined in green
            g.setColor(Color.WHITE);
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.drawRect((int)mod.getX(),(int)mod.getY(),200,200);       //drawing the box for the module
        if(type==WIRES){
            cut.draw(g);
        }
        if(type==BUTTON){
            click.draw(g);
        }
        if(type==SYMBOLS){
            press.draw(g);
        }
        if(type==SIMON){
            pattern.draw(g);
        }
    }
    /*---------------------------------------------------------------------------------
    This method tells the module to reset itself, allowing the bomb to be played again
     --------------------------------------------------------------------------------*/
    public void reset(){
        isFocused=false;
        if(type==WIRES){
            cut.reset();
        }
        if(type==BUTTON){
            click.reset();
        }
        if(type==SYMBOLS){
            press.reset();
        }
        if(type==SIMON){
            pattern.reset();
        }
    }
    /*-------------------------------------------------------------------------------------------
    This method is used by Timer module in Bomb class to see how much time is allotted to a level.
    Each specific module has a designated time, and the Timer module sums these values.
    Returns the designated time for this module in milliseconds
    --------------------------------------------------------------------------------------------*/
    public int getAllottedTime(){
        if(type==WIRES) {
            return cut.getAllottedTime();
        }
        if(type==BUTTON){
            return click.getAllottedTime();
        }
        if(type==SYMBOLS){
            return press.getAllottedTime();
        }
        else{
            return pattern.getAllottedTime();
        }
    }
    /*---------------------------------------------------------------------
    This method is used by Bomb class to see if a module has been defused.
    True means defused, false means not defused.
     ---------------------------------------------------------------------*/
    public boolean checkDefused(){
        if(type==WIRES){
            defused=cut.checkDefused();
        }
        if(type==BUTTON){
            defused=click.checkDefused();
        }
        if(type==SYMBOLS){
            defused=press.checkDefused();
        }
        if(type==SIMON){
            defused=pattern.checkDefused();
        }
        return defused;
    }
    /*----------------------------------------------------------------------------------------------
    This method is used by the Bomb class to update how many strikes are on the bomb.
    Strikes is then passed in through simon's interact function to use the proper pattern inverter.
     ---------------------------------------------------------------------------------------------*/
    public void updateStrikes(int strikes){
        this.strikes=strikes;
    }
}
/*-----------------------------------------------------------------------------------------------------------------
This class makes a wire module, draws it, and verifies if user is cutting wires in correct order.
Each wire is assigned a code based on its rgb colour, and wires must be cut in ascending order of these codes.
The program checks the cutting order of wires based on these codes, but players cut wires according to their colours.
For example, the manual says to cut blue wires first when there are 3 wires.
------------------------------------------------------------------------------------------------------------------*/
class WireModule{
    private SingleWire[] wires;				 //custom Objects that contain each wire's hitbox and colour
    private int[] correctOrder;             //contains wire codes in the order they need to be cut
    private int numWires,allottedTime;      //numWires is the number of wires, allottedTime is how much time the player has to solve this module
    private int startIndex,totalCut;       //totalCut tracks how many wires have been cut in total. startIndex is used to go through elements of correctOrder and see if user is cutting wires correctly
    private int cornerX,cornerY;           //the top left corner of the module box
    /*----------------------------------------------------------------------------
    Constructor which creates random SingleWire Objects to represent the wires
    "startX" and "startY" are the top left corner coordinates of the module's box
     ----------------------------------------------------------------------------*/
    public WireModule(int startX,int startY){
        Random rand=new Random();
        numWires=3+rand.nextInt(3);                                   //3 to 5 possible number of wires
        allottedTime=10000*numWires;                                         //20 to 50 seconds to solve the module
        int[][]allColours={{255,0,0},{0,255,0},{0,0,255},{255,0,255}};       //possible wire colours: red, blue, green, magenta
        correctOrder=new int[numWires];
        wires=new SingleWire[numWires];
        int spaceBetween=(200-numWires*10)/(numWires+1);                    //space between wires in order for them to be evenly spaced
        startIndex=totalCut=0;
        cornerX=startX;
        cornerY=startY;

        for(int i=0;i<numWires;i++){                                        //creating 10 randomly coloured SingleWire Objects
            int index=rand.nextInt(4);	    			            //choosing a random colour out of all the possible colours and assigning it to a wire
            int[] rgbSet=allColours[index];
            correctOrder[i]=rgbSet[0]*numWires+rgbSet[1]*2+rgbSet[2]*3*(int)(Math.pow(-1,numWires));     //each rgb value has a certain weighting it contributes to the final code

            int yCoord=startY+spaceBetween*(i+1)+10*i;
            wires[i]=new SingleWire(startX,yCoord,rgbSet,correctOrder[i]);
        }
        Arrays.sort(correctOrder);					                    //wires must be cut from least to greatest code value
    }
    /*--------------------------------------------------------------------------------------------------------------
    This method draws all wires on the screen, called by paintComponent() in Bomb whenever Timer fires in GameFrame
     -------------------------------------------------------------------------------------------------------------*/
    public void draw(Graphics g){
        for(SingleWire wire:wires) {
            int[] rgb = wire.getColour();                               //the colour of the wire
            Rectangle hitbox = wire.getRect();                          //the Rectangle that represents the wire's hitbox
            if (!wire.alreadyCut()) {                                   //if the wire has already been cut, it turns white
                g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
            }
            else{
                g.setColor(Color.WHITE);
            }
            g.fillRect((int) hitbox.getX(), (int) hitbox.getY(), 200, 10);
        }
        g.setColor(Color.RED);                                          //drawing a red circle in the corner of the box that turns green once the module is defused
        if(totalCut==numWires){
            g.setColor(Color.GREEN);
        }
        g.fillOval(cornerX+180,cornerY+10,10,10);
    }
    /*----------------------------------------------------
    Used by the Bomb class to check if modules are defused
     ----------------------------------------------------*/
    public boolean checkDefused(){
        return totalCut==numWires;
    }
    /*--------------------------------------------------------------------
    This method determines if user is cutting wires in the correct order.
    The value returned determines if the player gets a strike or not.
    "x" and "y" are the mouse coordinates
     -------------------------------------------------------------------*/
    public boolean interact(int x,int y){
        SingleWire selected=null;                   //if this is still null at the end of the process, that means user clicked empty space
        for(SingleWire wire:wires){                 //first, we have to determine if user clicked a wire or empty space
            if(wire.contains(x,y)){
                selected=wire;
            }
        }
        if(selected!=null) {
            if (selected.getCode() == correctOrder[startIndex] && !selected.alreadyCut()) {         //check if the correct wire was cut by comparing the correct wire's code to the clicked wire's code
                startIndex++;                                                                       //the correct wire has been clicked, which means the next wire must be compared to the next element in correctOrder[]
                totalCut++;
                selected.setCut();                                                                  //this will turn the cut wire white
                return true;
            }
        }
        if(selected==null || selected.alreadyCut()){        //player isn't penalized for clicking empty space or a wire that was already cut
            return true;
        }
        return false;                                       //this means user clicked the incorrect wire
    }
    /*---------------------------------------------------------------
    This method resets the wire module so a level can be played again
    ----------------------------------------------------------------*/
    public void reset(){
        startIndex=totalCut=0;
        for(SingleWire wire:wires){                         //every wire that was cut is now whole again
            if(wire.alreadyCut())
                wire.setCut();
        }
    }
    /*---------------------------------------------------------------------------------------------------
    This method is used by Timer module in Bomb to see how much time the player gets to solve this module
     ----------------------------------------------------------------------------------------------------*/
    public int getAllottedTime(){
        return allottedTime;
    }
}
/*----------------------------------------------------------------------------------------------------------------
Each wire in the wire module is a SingleWire Object.
The point of the Object is to facilitate the process of drawing wires and verifying if a wire has already been cut.
------------------------------------------------------------------------------------------------------------------*/
class SingleWire{
    private Rectangle hitbox;           //the wire's hitbox
    private int code;                   //the code assigned to the wire based on its rgb colour
    private int colour[];               //the wire's colour: {r,g,b}
    private boolean cut;                //draw() in WireModule shows different graphics for cut and uncut wires
    /*-------------------------------------------------------------------------------------------------------
    Constructor where "x" and "y" are starting coordinates for the hitbox Rectangle.
    "rgb" is the rgb value for this wire's colour, colourCode is the wire's code that controls cutting order
     -------------------------------------------------------------------------------------------------------*/
    public SingleWire(int x,int y, int[] rgb,int colourCode){
        hitbox=new Rectangle(x,y,200,10);
        code=colourCode;
        colour=rgb;
        cut=false;
    }
    /*------------------------------------------------------------------------------------------------------------
    This method is used by interact() in WireModule to see if user is clicking on a wire that has already been cut
     ------------------------------------------------------------------------------------------------------------*/
    public boolean alreadyCut(){
        return cut;
    }
    /*-------------------------------------------------------------------
    By changing this variable, different graphics for this wire are shown.
    USed by reset() in WireModule to make all cut wires whole again
    -------------------------------------------------------------------*/
    public void setCut(){
        cut=!cut;
    }
    /*-------------------------------------------
    Used by draw() in WireModule to draw the wire
     -------------------------------------------*/
    public int[] getColour(){
        return colour;
    }
    /*--------------------------------------------------------------
    Returns the wire's hitbox to check if user is clicking on a wire
     --------------------------------------------------------------*/
    public Rectangle getRect(){
        return hitbox;
    }
    /*--------------------------------------------------------------
    Returns a wire's code to see if user clicked on the correct wire
    --------------------------------------------------------------*/
    public int getCode(){
        return code;
    }
    /*-----------------------------------------------------------------------------------
    Used by interact() in WireModule to see if user is clicking on a wire or empty space
     ----------------------------------------------------------------------------------*/
    public boolean contains(int x,int y){
        return hitbox.contains(x,y);
    }
}
/*---------------------------------------------
 This class makes a countdown for a Bomb Object.
 The Object can draw and reset itself.
 --------------------------------------------*/
class TimeModule{
    private int x,y,timeLeft,originalTime;		//time is in milliseconds, x and y are where the module is displayed, originalTime is how much time is assigned to the level
    /*------------------------------------------------------------------
    Constructor. The module is made in Bomb class.
    "xCoord" and "yCoord" are top left corner coordinates of the box.
    "startingTime" is how much time is originally assigned to the level.
     ------------------------------------------------------------------*/
    public TimeModule(int xCoord, int yCoord, int startingTime){
        x=xCoord;
        y=yCoord;
        timeLeft=originalTime=startingTime;
    }
    /*----------------------------------------------------------------------
    This method gets the amount of time the player took to defuse the bomb.
    Called in Bomb once GameFrame has verified that user completed the level.
    -------------------------------------------------------------------------*/
    public int getCompletionTime(){
        return originalTime-timeLeft;
    }
    /*-----------------------------------------------------------------------------------------------
    This method is used by Bomb and GameFrame to see how much time is remaining to complete the level
     ----------------------------------------------------------------------------------------------*/
    public int getTime(){
        return timeLeft;
    }
    /*-------------------------------------------------------------------------
    This method allows players to play a level again by resetting the countdown
     -------------------------------------------------------------------------*/
    public void reset(){
        timeLeft=originalTime;
    }
    /*----------------------------------------------------------------------------------------------
     Responsible for the countdown because it updates the time that's displayed every 10 milliseconds
     -----------------------------------------------------------------------------------------------*/
    public void subtractTime(){
        timeLeft-=10;
    }
    /*--------------------------------------------------------------------------
    This method draws the time remaining to defuse the bomb in the style, 00:30
     -------------------------------------------------------------------------*/
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(310,370,180,80);
        g.setFont(new Font("Special Elite",Font.BOLD,50));
        if(timeLeft<10000){                                                         //time becomes bigger and turns red once there are less than 10 seconds left
            g.setColor(Color.RED);
            g.setFont(new Font("Special Elite",Font.BOLD,55));
        }
        int min=timeLeft/60000;                                                                            //time is currently in milliseconds but the countdown displays in minutes:seconds
        int seconds=(timeLeft-(min*60000))/1000;
        String displayed=String.format("%2d:%2d",(int)min,seconds).replace(" ","0");		//replacing blank spaces with 0's so it looks like a timer
        g.drawString(displayed,320,430);
    }
}
/*--------------------------------------------------------------------
This class makes a frame after user completes a level
From this frame, user can play again or return to select level screen
 --------------------------------------------------------------------*/
class GameOverFrame extends JFrame implements ActionListener,MouseListener {
    private Bomb bomb;                           //necessary field because if user clicks play again, GameFrame constructor needs a Bomb Object
    private int levelIndex;                      //an index of pages[] in SelectLevelPage. Necessary to recreate GameFrame if playAgain button is clicked
    private JButton returnBut, playAgainBut;     //buttons that allow user to return to select level screen or play again
    private SelectLevelPage selectLevel;         //necessary field because player can return to main menu from this frame, and main menu's constructor needs a SelectLevelPage
    private AudioClip buttonSound;              //enables button sound effects
    /*---------------------------------------------------------------------------------------------------------------
    Constructor that makes the frame.
    "justPlayed" is the bomb that was completed, necessary because it's used to recreate GameFrame if user plays again
    "level" is the level that was just played, necessary to play again
    "score" can be a time value such as 00:30, which is how long it took the player to complete a level, or "you died"
     ----------------------------------------------------------------------------------------------------------------*/
    public GameOverFrame(Bomb justPlayed, int level,SelectLevelPage levelPage,String score) {
        super("Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        bomb = justPlayed;
        levelIndex = level;
        selectLevel=levelPage;

        ImageIcon background = new ImageIcon("images/game over back.png");
        JLayeredPane thisPage = new JLayeredPane();
        thisPage.setLayout(null);

        JLabel scoreLabel = new JLabel(score);            //the JLabel either displays the player's score, or "You died" if the player failed the level
        scoreLabel.setLocation(325,300);
        if(!score.equals("You died")){                    //player defused the bomb, so different background image and score is shown
            background=new ImageIcon("images/bomb defused back.png");
            scoreLabel=new JLabel("Completion Time: "+score);
            scoreLabel.setLocation(250,300);
        }
        JLabel back = new JLabel(background);
        back.setSize(800, 600);
        back.setLocation(0, 0);
        scoreLabel.setSize(500, 60);
        scoreLabel.setBackground(Color.WHITE);
        scoreLabel.setFont(new Font("Special Elite", Font.BOLD, 30));
        scoreLabel.setForeground(Color.WHITE);

        playAgainBut = new JButton("Play again");
        playAgainBut.addActionListener(this);
        playAgainBut.addMouseListener(this);
        playAgainBut.setFont(new Font("Special Elite", Font.BOLD, 30));
        playAgainBut.setBackground(new Color(51, 31, 23));
        playAgainBut.setForeground(Color.WHITE);
        playAgainBut.setSize(250, 100);
        playAgainBut.setLocation(0, 460);
        playAgainBut.setFocusPainted(false);
        playAgainBut.setBorderPainted(false);

        returnBut = new JButton("Main menu");
        returnBut.addActionListener(this);
        returnBut.addMouseListener(this);
        returnBut.setFont(new Font("Special Elite", Font.BOLD, 30));
        returnBut.setBackground(new Color(51, 31, 23));
        returnBut.setForeground(Color.WHITE);
        returnBut.setSize(250, 100);
        returnBut.setLocation(550, 460);
        returnBut.setFocusPainted(false);
        returnBut.setBorderPainted(false);

        thisPage.add(back, JLayeredPane.DEFAULT_LAYER);
        thisPage.add(returnBut, JLayeredPane.DRAG_LAYER);
        thisPage.add(playAgainBut,JLayeredPane.DRAG_LAYER);
        thisPage.add(scoreLabel,JLayeredPane.DRAG_LAYER);
        add(thisPage);
        try{                                                                //loading sound effects for the buttons
            File buttonFile=new File("button click.wav");
            buttonSound=Applet.newAudioClip(buttonFile.toURI().toURL());
        }
        catch(MalformedURLException e){
            System.out.println("Can't find audio file");
        }
        setVisible(true);
    }

    /*-------------------------------------------------------------------
     This method changes frames when a button is pressed
     When the game is over, you can either go to main menu or play again.
     *-------------------------------------------------------------------*/
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        buttonSound.play();
        if (source == returnBut) {
            setVisible(false);
            new MainGame(selectLevel);
        }
        if (source == playAgainBut) {               //a new game is started when player clicks play again
            setVisible(false);
            GameFrame actualGame = new GameFrame(bomb, levelIndex,selectLevel);
            actualGame.start();
        }
    }

    /*-------------------------------------------------------------------------------
    This method makes the button text white when mouse isn't hovering over the buttons
    ---------------------------------------------------------------------------------*/
    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source == playAgainBut) {
            playAgainBut.setForeground(Color.WHITE);
        }
        if (source == returnBut) {
            returnBut.setForeground(Color.WHITE);
        }
    }

    /*-------------------------------------------------------------------
    This method makes button text red when mouse hovers over the button
     -------------------------------------------------------------------*/
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source == playAgainBut) {
            playAgainBut.setForeground(Color.RED);
        }
        if (source == returnBut) {
            returnBut.setForeground(Color.RED);
        }
    }

    /*-------------------------------------------------------------------------
    The following methods must be included in order to implement MouseListener
     -------------------------------------------------------------------------*/
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
}