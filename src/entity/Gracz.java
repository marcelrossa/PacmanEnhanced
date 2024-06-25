package entity;

import main.KeyHandler;
import main.PanelGry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Gracz extends Entity {
    KeyHandler keyH;

    JLabel playerLabel;
    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    public Gracz(PanelGry pg, KeyHandler keyH) {
        super(pg);
        this.pg = pg;
        this.keyH = keyH;

        solidArea = new Rectangle(8,8,32,32);//kolizja mniejsza niz obraz postaci dla latwosci steorwania

        setDefaultValues();
        getGraczImage();
        initPlayerLabel();
    }

    public void setDefaultValues() {
        x = 48;
        y = 48;
        speed = 4;
        direction = "down";
    }

    public void getGraczImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/up3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/down3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/left3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPlayerLabel() {
        playerLabel = new JLabel(new ImageIcon(down1));
        playerLabel.setBounds(x, y, pg.tileSize, pg.tileSize);
    }

    public JLabel getPlayerLabel() {
        return playerLabel;
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
        }
        if (keyH.downPressed) {
            direction = "down";
        }
        if (keyH.leftPressed) {
            direction = "left";
        }
        if (keyH.rightPressed) {
            direction = "right";
        }
        //sprawdzanie collision
        collisionOn = false;
        pg.cChecker.checkTile(this);
        //jesli collision false, nie mozemy sie ruszac
        if(collisionOn == false) {
            switch (direction){
                case "up":
                    y -= speed;
                    break;
                case "down":
                    y += speed;
                    break;
                case "left":
                    x -= speed;
                    break;
                case "right":
                    x += speed;
                    break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 16) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        updatePlayerLabel();
    }

    private void updatePlayerLabel() {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if(spriteNum == 1)
                    image = up1;
                if(spriteNum == 2)
                    image = up2;
                if(spriteNum == 3)
                    image = up3;
                break;
            case "down":
                if(spriteNum == 1)
                    image = down1;
                if(spriteNum == 2)
                    image = down2;
                if(spriteNum == 3)
                    image = down3;
                break;
            case "left":
                if(spriteNum == 1)
                    image = left1;
                if(spriteNum == 2)
                    image = left2;
                if(spriteNum == 3)
                    image = left3;
                break;
            case "right":
                if(spriteNum == 1)
                    image = right1;
                if(spriteNum == 2)
                    image = right2;
                if(spriteNum == 3)
                    image = right3;
                break;
        }
        playerLabel.setIcon(new ImageIcon(image));
        playerLabel.setLocation(x, y);
    }
}
