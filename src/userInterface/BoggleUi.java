/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import core.Board;
import core.Die;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.border.TitledBorder;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author acrux
 */
public class BoggleUi implements ActionListener {
    ArrayList<Integer> lettersUsed=new ArrayList<Integer>();
    int scoreTracker;
    ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
    long startTime;
    Board boardUi;
    ArrayList dictionaryUi;
    JFrame top = new JFrame("Boggle");
    BorderLayout  layout = new BorderLayout();
    GridBagLayout bagLayout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints(); 
    JMenuBar bar = new JMenuBar();
    JMenu boggle = new JMenu("Boggle");
    JMenuItem newGame = new JMenuItem("New Game");
    JMenuItem exit = new JMenuItem("Exit");
    JPanel diceHolder  = new JPanel();
    JPanel wordHolder  = new JPanel();
    JPanel scoreHolder = new JPanel();
    JPanel bottomLabelHolder = new JPanel();
    JButton[] bDie;
    JButton shakeDice = new JButton("Shake Dice");
    JButton submitWord = new JButton("Submit"); 
    JTextArea userWords = new JTextArea(17,20);
    JScrollPane userWordsScroll;
    JLabel timer2Label = new JLabel("       3:00 ");
    JLabel score = new JLabel("                0 ");
    JLabel currentWord = new JLabel("");
    JPanel shakeDicePanel = new JPanel();
    JPanel bottomHolder = new JPanel();

    JPanel timerHolder = new JPanel();
    ArrayList wordsFound = new ArrayList();
    public Runnable counter;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final AtomicBoolean isExecuting = new AtomicBoolean(false);
    private final int N_SECONDS = 60*3; // 3 minutes
    private final AtomicInteger seconds = new AtomicInteger(N_SECONDS);
    //creating an object to change the font type and size.
    Font font = new Font("Courier", Font.CENTER_BASELINE,36);
               
           
    public BoggleUi(Board boardData,ArrayList dictionary){
       boardUi = boardData;
       dictionaryUi = dictionary; 
       counter = new Runnable() {
            @Override
            public void run() {
                while(!isExecuting.get()){
                    lock.lock();
                    try {
                        condition.await();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BoggleUi.class.getName()).log(Level.SEVERE, null, ex);
                    } finally{
                        lock.unlock();
                    }
                }
                int currentTime = seconds.getAndDecrement();
                int minutes = currentTime / 60;
                int seconds = currentTime % 60;

                timer2Label.setText(String.format("       %d:%02d", minutes, seconds));
                if(currentTime == 0){
                    isExecuting.set(false);
                }
            }
        };
       initComponents();
       timer.scheduleAtFixedRate(counter,0,1,TimeUnit.SECONDS);
    }
    
    private void initComponents(){
        
        scoreTracker = 0;
        bDie = new JButton[16];
        top.setLayout(layout);
        //changing size of JFrame
        top.setSize(600,600 );
        top.setJMenuBar(bar);
        bar.add(boggle);
        //adding menu items to the menu bar 
        newGame.addActionListener(this);//adding action listener.
        boggle.add(newGame);
        exit.addActionListener(this);
        boggle.add(exit);
         
        
        //setting locations
        
        top.add(wordHolder, BorderLayout.EAST);
        top.add(diceHolder, BorderLayout.WEST);
        top.add(bottomHolder, BorderLayout.SOUTH);
        
        //changing size of shake size button.
        
        int shakeButtonWidth = 100;
        int submitWordWidth= 100;
        shakeDice.setSize(shakeButtonWidth,30);
        shakeDice.setAlignmentX(Component.CENTER_ALIGNMENT);
        shakeDice.setPreferredSize(new Dimension(shakeButtonWidth, 30));
        shakeDice.addActionListener(this);
        //setting Submit button
        submitWord.setEnabled(false);
        submitWord.setSize(submitWordWidth ,30);
        submitWord.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitWord.setPreferredSize(new Dimension(submitWordWidth, 30));
        
        userWords.setEditable(false);
        //Setting the Scroll
        userWordsScroll = new JScrollPane(userWords);//JScroll (desired location in this case user words)
        userWordsScroll.setVisible(true);
        wordHolder.setLayout(new BorderLayout());
        //Scroll just appered when the user has typed on all vissible area.
        userWordsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        
        //wrapping lines. text strats at a new line when the set widht has been utilized.
        userWords.setLineWrap(true);
        wordHolder.setLayout(new BorderLayout(5,5));//setting gaps between components.        
        wordHolder.setBorder(new TitledBorder("Enter Words Found"));//setting a title for the the boarder
        wordHolder.add(userWordsScroll, BorderLayout.NORTH); //addin the scroll to the container NO THE JText. since the scroll already contains the JText.
        wordHolder.add(timerHolder, BorderLayout.CENTER);
        
        //Setting font for timer and Score.
        score.setFont(font);
        timer2Label.setFont(font);
        //setting Jpane for the timer and shake dice button.
        submitWord.addActionListener(this);
        shakeDicePanel.add(submitWord);
        timerHolder.setLayout(new BorderLayout(5,5));/// this is what makes the buttons have dimension of the size of the container.
        timerHolder.setBorder(new TitledBorder("Time left"));
        timerHolder.add(timer2Label, BorderLayout.CENTER);
        shakeDicePanel.add(shakeDice);
        shakeDicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        wordHolder.add(shakeDicePanel, BorderLayout.SOUTH);
        wordHolder.doLayout();
        timerHolder.doLayout();
        //adding scrore label and creating title
        currentWord.setBorder(new TitledBorder ("Current Word"));
        bottomLabelHolder.setBorder(new TitledBorder("Current Word"));
        score.setPreferredSize(new Dimension(350,70));
        bottomHolder.setLayout(new BorderLayout());
        bottomHolder.add(scoreHolder,BorderLayout.WEST);
        bottomLabelHolder.setLayout(new BorderLayout());
        bottomLabelHolder.add(currentWord, BorderLayout.CENTER);
        bottomHolder.add(bottomLabelHolder,BorderLayout.CENTER);///work on spacing
        //adding current word Label
        
        
        //TitledBorder scoreTitle; 
        //scoreTitle= BorderFactory.createTitledBorder(" Score  ");
        //scoreTitle.setTitlePosition(TitledBorder.CENTER);
       // scoreHolder.setBorder(scoreTitle);
        scoreHolder.setBorder(new TitledBorder("score"));
        scoreHolder.setLayout(new BorderLayout());
        scoreHolder.add(score, BorderLayout.CENTER);
        //setting title of current word
        
    
        //creating dice.
        
        diceHolder.setBorder(new TitledBorder("Boggle Board"));//assining title.
        diceHolder.setLayout( bagLayout);//creating a Baglayout for the panel that holds the dice.
        constraints.gridx= 0;//setting i tial position of x
        constraints.gridy= 0;//setting intial possition of  y
        constraints.gridheight = 1;//number of cells
        constraints.gridwidth = 1;
        //creating 16 Dice for the game.
        for(int i=0; i<16; i++){
            
            bDie[i]= new JButton();
            bDie[i].setPreferredSize(new Dimension(85, 85));
            //setting the predefined constraits for each die.
            bagLayout.setConstraints(bDie[i], constraints); //position , and passing the constraints
            bDie[i].addActionListener(this);
            diceHolder.add(bDie[i]); //adding a die to the dice holder
            
            constraints.gridx++;
            //makes a new row of dice
            if((i+1)%4==0){
                constraints.gridx = 0;// restarts from x position 0;
                constraints.gridy++; //new row.
        
            }
          }
        wordHolder.setVisible(true);
        diceHolder.setVisible(true);
        
        top.setVisible(true);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// function to close the application when the exit button is clicked.. 
    
    }
    
    public void addingLetters(Board boardData){
        
        for(int i=0; i<16; i++){
           
            bDie[i].setText(boardData.dice.get(i).getLetter());
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(action.getActionCommand().equals("Exit")){
            top.dispose();
            timer.shutdownNow();
        }
        else if(action.getActionCommand().equals("New Game")){
            
            lettersUsed.clear();
            submitWord.setEnabled(false);
            userWords.setText("");
            shakeDice.setEnabled(true);
            wordsFound.clear();
            isExecuting.set(false);
            seconds.set(N_SECONDS);
        }
        else if (action.getActionCommand().equals("Submit")){
            lettersUsed.clear();
            //wordsFound.add("0");
            for(int i=0; i<16;i++)
                bDie[i].setEnabled(true);
            //i am going to check the dictionary
            //
            String query = currentWord.getText();
            int sizeDictionary = dictionaryUi.size();
            int sizeWordsFound = wordsFound.size();
            boolean foundWord = false; 
            int j;
            
            for( j= 0;j<sizeWordsFound;j++){
                if (((String)wordsFound.get(j)).equalsIgnoreCase(query))
                    foundWord = true;
                break;
            }
            for(int i=0; i<sizeDictionary; i++ ){
            
                if((((String)dictionaryUi.get(i)).equalsIgnoreCase(query))&& foundWord== false){
                    //adding word to JText Area.
                    userWords.setText(query+"\n"+userWords.getText());
                    wordsFound.add(j    , query);
                    //Getting score.
                    int wordSize =query.length();
                    
                    
                    //1point
                    if((wordSize == 3)||(wordSize == 4)){
                        scoreTracker = scoreTracker+1; 
                        score.setText("               "+Integer.toHexString(scoreTracker));
                    }
                    //2 points
                    if(wordSize == 5){
                        scoreTracker = scoreTracker+2; 
                        score.setText("               "+Integer.toHexString(scoreTracker));
                    }
                    //three points
                    if(wordSize == 6){
                        scoreTracker = scoreTracker+3; 
                        score.setText("               "+Integer.toHexString(scoreTracker));
                    }
                    if(wordSize == 7){
                        scoreTracker = scoreTracker+5; 
                       score.setText("               "+Integer.toHexString(scoreTracker));
                    }
                    // 11 points
                    if(wordSize >= 8){
                        scoreTracker = scoreTracker+11; 
                        score.setText("               "+Integer.toHexString(scoreTracker));
                    }
                    break;
                }
                
            }
            
           
            //
            currentWord.setText("");
        }
        else if(action.getActionCommand().equals("Shake Dice")){
            //setting enabled and disabled buttons.
            
            shakeDice.setEnabled(false);
            lettersUsed.clear();
            submitWord.setEnabled(true);
            for(int i=0; i<16;i++)
                bDie[i].setEnabled(true);
            ArrayList<String> letters = new ArrayList();
            int[] positions = new int[16];
             
            ArrayList<Die> dice = boardUi.shakeDice();
            for(int i=0; i<16; i++){
                positions[i]=0;
                letters.add(dice.get(i).getLetter());
            }
            
            
            Random rand;
            int temp;
            rand = new Random();
            rand.setSeed(System.currentTimeMillis());
            
            for(int j=0; j<16; j++){
                  
                temp = rand.nextInt(16);
                while(positions[temp]!=0){
                    
                    temp++;
                    if(temp==16)
                        temp=0;
                }
                bDie[j].setText(letters.get(temp));
                positions[temp]= 1;
            }
            
            score.setText("                0 ");//work on spacing
            timer2Label.setText("");//work on spacing...
            currentWord.setText("");
            lock.lock();
            isExecuting.set(true);
            condition.signal();
            lock.unlock();
        }
        else {
            int choice=0;
            boolean firstCol=false,lastCol=false,lastRow=false,firstRow=false;
            currentWord.setText(currentWord.getText()+action.getActionCommand());
            ((JButton)action.getSource()).setEnabled(false);
            for(int i=0;i<16;i++){
                bDie[i].setEnabled(false);
                if(bDie[i]== (JButton)action.getSource()){
                    choice=i;
                    lettersUsed.add(choice);
                }
                    
            }
            if(choice<4)
                firstRow=true;
            if(choice>=12)
                lastRow=true;
            if(choice%4==0)
                firstCol=true;         
            if(choice%4==3)
                lastCol=true;
            System.out.print(firstRow+""+lastRow+""+firstCol+""+lastCol);
            if(firstCol==true){
                bDie[choice+1].setEnabled(true);
                if(firstRow==false){
                    bDie[choice-4].setEnabled(true);
                    bDie[(choice-4)+1].setEnabled(true);
                }
                if(lastRow==false){
                    bDie[choice+4].setEnabled(true);
                    bDie[choice+4+1].setEnabled(true);
                }
                    
            }
            else if(lastCol==true){
                bDie[choice-1].setEnabled(true);
                if(firstRow==false){
                    bDie[choice-4].setEnabled(true);
                    bDie[choice-4-1].setEnabled(true);
                }
                if(lastRow== false){
                    bDie[choice+4].setEnabled(true);
                    bDie[choice+4-1].setEnabled(true);
                }
                    
            }
            else if(firstRow==true){
                bDie[choice+4].setEnabled(true);
                if(firstCol==false){
                    bDie[choice-1].setEnabled(true);
                    bDie[choice-1+4].setEnabled(true);
                }
                if(lastCol==false){
                    bDie[choice+1].setEnabled(true);
                    bDie[choice+1+4].setEnabled(true);
                }
            }
            else if(lastRow==true){
                bDie[choice-4].setEnabled(true);
                if(firstCol==false){
                    bDie[choice-1].setEnabled(true);
                    bDie[choice-1-4].setEnabled(true);
                }
                if(lastCol==false){
                    bDie[choice+1].setEnabled(true);
                    bDie[choice+1-4].setEnabled(true);
                }
            }
            else{
                bDie[choice+4].setEnabled(true);
                bDie[choice-4].setEnabled(true);
                bDie[choice+1].setEnabled(true);
                bDie[choice+1+4].setEnabled(true);
                bDie[choice+1-4].setEnabled(true);
                bDie[choice-1].setEnabled(true);
                bDie[choice-4-1].setEnabled(true);
                bDie[choice+4-1].setEnabled(true);
            }
            for(int i=lettersUsed.size();i>0;i--){   
                bDie[lettersUsed.get(i-1)].setEnabled(false);
            }
        }
    }
}
    
