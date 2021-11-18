package moon_lander;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {
	
    /**
     * The space rocket with which player will have to land.
     */
    private PlayerRocket[] PlayerRocket = new PlayerRocket[2];
    /**
     * Landing area on which rocket will have to land.
     */
    private LandingArea landingArea;
    
   
    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;
    /**
     * Red border of the frame. It is used when player crash the rocket.
     */
    private BufferedImage redBorderImg;
    
    public static int RocketN = 0;
    public static int RocketN1 = 1;
    ArrayList meteors = new ArrayList();
    ArrayList stars = new ArrayList();
    
//    public static enum StageLevel{STAGE1, STAGE2, STAGE3, STAGE4, STAGE5}
//    public static StageLevel stagelevel;
    
    public int speed = 0;
    
    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
            
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
    	
    	switch(Framework.player) {
    	case 1:
    		PlayerRocket[0] = new PlayerRocket();
    		meteors.clear();
    	break;
    	case 2:
    		PlayerRocket[0] = new PlayerRocket();
    		PlayerRocket[1] = new PlayerRocket();
    		meteors.clear();
    	break;
    	}
        landingArea  = new LandingArea();
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2){
    	boolean check;
 	   if ( Math.abs( ( x1 + w1 / 2 )  - ( x2 + w2 / 2 ))  <  ( w2 / 2 + w1 / 2 ) && Math.abs( ( y1 + h1 / 2 )  - ( y2 + h2 / 2 ))  <  ( h2 / 2 + h1/ 2 ) ){
 	        check = true;//위 값이 true면 check에 true를 전달합니다.
 	    } else {
 	        check = false;
 	    }
 	    return check; //check의 값을 메소드에 리턴 시킵니다.
    }
    
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/resources/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL redBorderImgUrl = this.getClass().getResource("/resources/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void player2() {
    	for(int i=0; i<2; i++) {
			PlayerRocket[i].Update(i);
            for (int j = 0; j < meteors.size(); j++) {
                Meteor me = (Meteor) meteors.get(j);
                if (Crash(PlayerRocket[i].x,PlayerRocket[i].y,me.x,me.y,
                    PlayerRocket[i].rocketImgWidth, PlayerRocket[i].rocketImgHeight, me.meteorImgWidth,me.meteorImgHeight)){
                    PlayerRocket[i].crashed = true;
//                    Framework.gameState = Framework.gameState.GAMEOVER;
                }                
            }
            for (int j = 0; j < stars.size(); j++) {
                Star st = (Star) stars.get(j);
                if (Crash(PlayerRocket[0].x,PlayerRocket[0].y,st.x,st.y,
                    PlayerRocket[0].rocketImgWidth, PlayerRocket[0].rocketImgHeight, st.starImgWidth,st.starImgHeight)){
                    Framework.starcount++;
                    stars.remove(j);
                }
            }
			if(PlayerRocket[i].y + PlayerRocket[i].rocketImgHeight - 10 > landingArea.y)
	        {
	            // Here we check if the rocket is over landing area.
	            if((PlayerRocket[i].x > landingArea.x) && (PlayerRocket[i].x < landingArea.x + landingArea.landingAreaImgWidth - PlayerRocket[i].rocketImgWidth))
	            {
	                // Here we check if the rocket speed isn't too high.
	                if(PlayerRocket[i].speedY <= PlayerRocket[i].topLandingSpeed)
	                	if (!PlayerRocket[i].crashed && PlayerRocket[i].landed == false) {
                            PlayerRocket[i].landed = true;
                        } 
	                	else
	                		PlayerRocket[i].crashed=true;
	      
	                
	            }
	            else
	                PlayerRocket[i].crashed = true;
	        }
			if(PlayerRocket[0].crashed) {
                PlayerRocket[0].speedY=0;
                PlayerRocket[0].speedX=0;
                PlayerRocket[0].speedAccelerating=0;
                PlayerRocket[0].speedStopping=0;
            }
            if(PlayerRocket[1].crashed) {
                PlayerRocket[1].speedY=0;
                PlayerRocket[1].speedX=0;
                PlayerRocket[1].speedAccelerating=0;
                PlayerRocket[1].speedStopping=0;
            }
			if(PlayerRocket[0].crashed && PlayerRocket[1].crashed) {
            	Framework.gameState = Framework.GameState.GAMEOVER;
            }
        }
    }
    public void RestartGame()
    {
    	switch(Framework.player) {
    	case 1:
    		PlayerRocket[0] = new PlayerRocket();
    		stars.clear();
    		meteors.clear();    		
    	break;
    	case 2:
    		
    		PlayerRocket[0] = new PlayerRocket();
    		PlayerRocket[1] = new PlayerRocket();
    		stars.clear();
    		meteors.clear();  		
    	break;
    	
    	}
    	

       
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition, int count)
    {   	 // Used Count Value
    	if (count%30 == 0) {
    		switch (Framework.stagelevel) {        	
        	case STAGE1:
        		speed = -5;
        		break;
        	case STAGE2:
        		speed = -8;
        		break;
        	case STAGE3:
        		speed = -11;
        		break;
        	case STAGE4:
        		speed = -14;
        		break;
        	case STAGE5:
        		speed = -17;
        		break;
        	}
            Meteor me = new Meteor();
            me.Initialize(speed);
            me.LoadContent();
            meteors.add(me);
            Star st = new Star();
            st.LoadContent();
            stars.add(st);
        }
    	for (int i = 0 ; i < meteors.size(); i++) {
        	Meteor me = (Meteor) meteors.get(i);
        	me.Update();
    	}
    	for (int i = 0 ; i < stars.size(); i++) {
        	Star st = (Star) stars.get(i);
    	}
    	switch(Framework.player){        // Move the rocket
    	case 1:   		
    		PlayerRocket[0].Update(0);  		
        // Checks where the player rocket is. Is it still in the space or is it landed or crashed?
        // First we check bottom y coordinate of the rocket if is it near the landing area.
        for (int j = 0; j < meteors.size(); j++) {
            Meteor me = (Meteor) meteors.get(j);
            if (Crash(PlayerRocket[0].x,PlayerRocket[0].y,me.x,me.y,
                PlayerRocket[0].rocketImgWidth, PlayerRocket[0].rocketImgHeight, me.meteorImgWidth,me.meteorImgHeight)){
                PlayerRocket[0].crashed = true;
                Framework.gameState = Framework.gameState.GAMEOVER;
            }
        }
        for (int j = 0; j < stars.size(); j++) {
            Star st = (Star) stars.get(j);
            if (Crash(PlayerRocket[0].x,PlayerRocket[0].y,st.x,st.y,
                PlayerRocket[0].rocketImgWidth, PlayerRocket[0].rocketImgHeight, st.starImgWidth,st.starImgHeight)){
                Framework.starcount++;
                stars.remove(j);
            }
        }
        if(PlayerRocket[0].y + PlayerRocket[0].rocketImgHeight - 10 > landingArea.y) {
            // Here we check if the rocket is over landing area.
            if((PlayerRocket[0].x > landingArea.x) && (PlayerRocket[0].x < landingArea.x + landingArea.landingAreaImgWidth - PlayerRocket[0].rocketImgWidth))
            {
                // Here we check if the rocket speed isn't too high.
                if(PlayerRocket[0].speedY <= PlayerRocket[0].topLandingSpeed)
                    PlayerRocket[0].landed = true;
                else
                    PlayerRocket[0].crashed = true;
            }
            else
                PlayerRocket[0].crashed = true;
                
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
        break;
    	   			  
    	case 2:
    		player2();   		 
    	    }
        }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        landingArea.Draw(g2d);
        
        
        switch (Framework.player) {
		case 1:
			
			PlayerRocket[0].Draw(g2d);
			for (int l = 0; l < meteors.size(); l++) {
	        	Meteor me = (Meteor) meteors.get(l);
	        	me.Draw(g2d);
	        }
			for (int l = 0; l < stars.size(); l++) {
	        	Star st = (Star) stars.get(l);
	        	st.Draw(g2d);
	        }
			break;
		case 2:
			
			PlayerRocket[0].Draw(g2d);
			
			PlayerRocket[1].Draw(g2d);
			
			for (int l = 0; l < meteors.size(); l++) {
	        	Meteor me = (Meteor) meteors.get(l);
	        	me.Draw(g2d);
	        }
			for (int l = 0; l < stars.size(); l++) {
	        	Star st = (Star) stars.get(l);
	        	st.Draw(g2d);
	        }
			break;
		}
        
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     * @param gameTime Game time in nanoseconds.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        Draw(g2d, mousePosition);
        
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
        
        
        switch (Framework.player) {
		case 1:
			 if(PlayerRocket[0].landed)
		        {
		            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
		            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
		        }
		        else
		        {
		            g2d.setColor(Color.red);
		            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
		            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		        }
			break;
			
		case 2:
			 if(!(PlayerRocket[0].crashed & PlayerRocket[1].crashed) | (PlayerRocket[0].landed & PlayerRocket[1].landed))
		        {
		            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3);
		            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 20);
		        }
		        else
		        {
		            g2d.setColor(Color.red);
		            g2d.drawString("You have crashed the rocket!", Framework.frameWidth / 2 - 95, Framework.frameHeight / 3);
		            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		        }
			break;
		}
       
    }
}