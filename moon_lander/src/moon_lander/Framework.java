package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;

//import moon_lander.Game.StageLevel;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L; //10의 -9승 밀리세컨드 
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 16; //자연스러운 화면 -> 프레임이 많을 수록 자연스러움  
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS; //초당 몇개의 프레임으로 나눌 것인가 
    
    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, STAGE}  //gameState -> enum 씀
    /**
     * Current state of the game
     */
    public static GameState gameState; //하나의 state 존재 
    
    /**
     * Elapsed game time in nanoseconds.
     */
    
    public static enum StageLevel{STAGE1, STAGE2, STAGE3, STAGE4, STAGE5}
    public static StageLevel stagelevel;
    
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    int HIGH_SCORE = 0;
    Music bit = new Music("bit.mp3",true);
    /**
     * Image for menu.
     */
    private BufferedImage moonLanderMenuImg;
    
    ///////////GUI
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {//Thread 생성 
            @Override
            public void run(){
                GameLoop(); //게임루프 
                
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
        
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
        try
        {
            URL moonLanderMenuImgUrl = this.getClass().getResource("/resources/images/menu.jpg"); //this.getclass().getResourceAsStream() -> 리턴값이 inputStream이 넘어온다 // this.getClassgetClassLouder()  R클래스 -> 리소스를 갖고있는 메인 클레스 
            moonLanderMenuImg = ImageIO.read(moonLanderMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    static public int starcount;
    
    private void GameLoop() 
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        int count=0;
        while(true)
        {
            beginTime = System.nanoTime();
           
            switch (gameState)
            { //if문 안에 if -> 임베디드 시스템 
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;  //게임 사용 시작으로 나노타임으로 가져와서 마지막에 시간을 뺀다
                    count++;
                    game.UpdateGame(gameTime, mousePosition() ,count);                    
                    lastTime = System.nanoTime();                    
                break;
                case GAMEOVER:
                	count+=(starcount*100);
                	// ex)                	
                	if (count > HIGH_SCORE) {
                    	HIGH_SCORE = count;
                    	count=0;
                    } else if (HIGH_SCORE <= 0 ) {
                    	HIGH_SCORE = count;
                    	count=0;
                    }
                	
                	// Use Switch (CHECK GAME.STAGELEVEL) 
                	
                	// CASE STAGE1:
                	// ...
                	// break;
                	
                	// CASE STAGE2:
                	// ...
                	// break;
                	
                	// ...
                break;
                case MAIN_MENU:
                	 
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();
                    bit.start();
                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    private JButton btn1, btn2 ;
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)  //화면에 출력되는 부분
    {
    	JButton btn1 = new JButton("1인용");
    	btn1.setBounds(frameWidth/2 -115, frameHeight /2 +50, 90,20);  	
    	btn1.addMouseListener(this);
        JButton btn2 = new JButton("2인용");
        btn2.setBounds(frameWidth /2-15, frameHeight/2+50,90,20);
        btn2.addMouseListener(this);
        JButton btn3 = new JButton("level1");
        btn3.setBounds(frameWidth /2-200, frameHeight/2+50,90,20);
        btn3.addMouseListener(this);
        JButton btn4 = new JButton("level2");
        btn4.setBounds(frameWidth /2-100, frameHeight/2+50,90,20);
        btn4.addMouseListener(this);
        JButton btn5 = new JButton("level3");
        btn5.setBounds(frameWidth /2, frameHeight/2+50,90,20);
        btn5.addMouseListener(this);
        JButton btn6 = new JButton("level4");
        btn6.setBounds(frameWidth /2+100, frameHeight/2+50,90,20);
        btn6.addMouseListener(this);
        JButton btn7 = new JButton("level5");
        btn7.setBounds(frameWidth /2+200, frameHeight/2+50,90,20);
        btn7.addMouseListener(this);
        JButton btn8 = new JButton("MAIN_MENU");
    	btn8.setBounds(frameWidth/2 +85, frameHeight /2 +50, 90,20);  	
    	btn8.addMouseListener(this);
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime);
                this.add(btn1);
                this.add(btn2);
                this.add(btn8);
            break;
            case MAIN_MENU:
                g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Use w a d keys to controle the rocket.", frameWidth / 2 - 107, frameHeight / 2);
                g2d.drawString("Press the button to start the game.", frameWidth / 2 - 107, frameHeight / 2 + 30);
                g2d.drawString("made by : sehyun park,yuseung baik ", frameWidth / 2 - 80, frameHeight -23);
                g2d.drawString("Designed by : ", frameWidth / 2 - 80, frameHeight -10);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                this.add(btn1);
                this.add(btn2);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
            case STAGE:
            	g2d.drawImage(moonLanderMenuImg, 0, 0, frameWidth, frameHeight, null);
            	this.add(btn3);
            	this.add(btn4);
            	this.add(btn5);
            	this.add(btn6);
            	this.add(btn7);
            	g2d.drawString("HIGH_SCORE"+HIGH_SCORE,frameWidth /2,frameHeight/2+100);
            break;
        }
    }
    
    /**
     * Starts new game.
     */
  
    private void gotoStage() {
    	gameState = GameState.STAGE;
    }
    
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    
  
    
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    public static int player = 1;
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
      
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e){
    	
    	System.out.println("버튼이 클릭됨");
    	
    	if((JButton)e.getSource() instanceof JButton) {
    	JButton b = (JButton)e.getSource(); 
    	if(b.getText() == "level1") {
    		System.out.println("level1"); 
    		stagelevel = StageLevel.STAGE1;
    		if (b.getText() == "1인용"){
        		System.out.println("1인용");
        		player = 1;
        		stagelevel = StageLevel.STAGE1;
        	} 
    	}
    	if(b.getText() == "level2") {
        	System.out.println("level2");
        	stagelevel = StageLevel.STAGE2;
        	if (b.getText() == "1인용"){
        		System.out.println("1인용");
        		player = 1;
        		stagelevel = StageLevel.STAGE2;
        	} 
    	}
        if(b.getText() == "level3") {
        	System.out.println("level3");
        	stagelevel = StageLevel.STAGE3;
        	if (b.getText() == "1인용"){
        		System.out.println("1인용");
        		player = 1;
        		stagelevel = StageLevel.STAGE3;
        	} 
        }
        if(b.getText() == "level4") {
          	System.out.println("level4");
          	stagelevel = StageLevel.STAGE4;
          	if (b.getText() == "1인용"){
        		System.out.println("1인용");
        		player = 1;
        		stagelevel = StageLevel.STAGE4;
        	} 
        }
        if(b.getText() == "level5") {
    		System.out.println("level5");
    		stagelevel = StageLevel.STAGE5;
    		if (b.getText() == "1인용"){
        		System.out.println("1인용");
        		player = 1;
        		stagelevel = StageLevel.STAGE5;
        	}   	
    	}
        if (b.getText() == "1인용") {
        	System.out.println("2인용");
        	player = 1;
        }
        else if (b.getText() == "2인용"){
    		System.out.println("2인용");
    		player = 2;
    	}
        else if (b.getText() == "MAIN_MENU"){
    		System.out.println("MAIN_MENU");
    		gameState=gameState.MAIN_MENU;
    	}
    	
    	switch(gameState) {
    		case MAIN_MENU:
    			if(player == 1) {
    				gotoStage();
    			}
    			if(player == 2) {
    				newGame();
    			}
    			break;
    		case STAGE:
    			newGame();
    			break;
    		case GAMEOVER:
    			restartGame();
    			break;
    	}
    	this.removeAll();
    	}
    }
    	
} 