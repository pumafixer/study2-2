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

public class Meteor   {

        private Random random;

        public int x;

        public int y;

        private BufferedImage meteorImg;

        public int meteorImgWidth;

        public int meteorImgHeight;

        public int speedX;

        private int speedY;

        private boolean check;

        public int gamestagelevel;



public Meteor(){
    // Meteor Create
    LoadContent();
    Random rand = new Random();

    x = (Framework.frameWidth);
    y = rand.nextInt(Framework.frameHeight - meteorImgHeight);

}


void Initialize(int speed)
{
    random = new Random();
    speedX = speed;
    speedY = 0;

}
void LoadContent()
{
    try {
    URL meteorImgUrl = this.getClass().getResource("/resources/images/meteor.jpg");
    meteorImg = ImageIO.read(meteorImgUrl);
    meteorImgWidth = meteorImg.getWidth();
    meteorImgHeight = meteorImg.getHeight();
    }
     catch (IOException ex) {
         Logger.getLogger(Meteor.class.getName()).log(Level.SEVERE, null, ex);
     }
}

public void Draw(Graphics2D g2d)
{
    g2d.drawImage(meteorImg, x, y, null);
}


public void Update() {
    // TODO Auto-generated method stub

     x += speedX;
     y += speedY;

    }

}