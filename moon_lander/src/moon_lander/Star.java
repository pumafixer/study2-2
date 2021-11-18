package moon_lander;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Star {
	        private Random random;

	        public int x;

	        public int y;

	        private BufferedImage starImg;

	        public int starImgWidth;

	        public int starImgHeight;
	        
	        private boolean check;

	public Star(){
	    // star Create
	    LoadContent();
	    Random rand = new Random();

	    x = rand.nextInt(Framework.frameWidth- starImgWidth);
	    y = rand.nextInt(Framework.frameHeight - starImgHeight);

	}

	void LoadContent()
	{
	    try {
	    URL starImgUrl = this.getClass().getResource("/resources/images/star.jpg");
	    starImg = ImageIO.read(starImgUrl);
	    starImgWidth = starImg.getWidth();
	    starImgHeight = starImg.getHeight();
	    }
	     catch (IOException ex) {
	         Logger.getLogger(Star.class.getName()).log(Level.SEVERE, null, ex);
	     }
	}

	public void Draw(Graphics2D g2d)
	{
	    g2d.drawImage(starImg, x, y, null);
	}

}
