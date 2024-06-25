package entity;

import main.PanelGry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Duch extends Entity {
    PanelGry pg;
    JLabel playerLabel;
    BufferedImage down1;
    Random random;
    String imagePath;
    private int lastX, lastY;

    public Duch(PanelGry pg, String imagePath) {
        super(pg);
        this.pg = pg;
        this.imagePath = imagePath;
        random = new Random();

        solidArea = new Rectangle(8, 16, 32, 32);//jak dla gracza dla latwosci

        setDefaultValues();
        getDuchImage();
        initPlayerLabel();
    }

    public void setDefaultValues() {
        x = 48;
        y = 528;
        speed = 3;
        direction = "right";
        lastX = x;
        lastY = y;
    }

    public void setDefaultValues(int x) {
        this.x = x;
        y = 528;
        direction = "right";
        speed = 3;
        lastX = x;
        lastY = y;
    }

    public void getDuchImage() {
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream(imagePath));
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
        if (random.nextInt(60) == 0) { //60 szans na zmiane kierunku
            changeDirection();
        }

        collisionOn = false;
        pg.cChecker.checkTile(this);

        if (!collisionOn) {
            move();
        } else {
            changeDirection();
        }

        if (x == lastX && y == lastY) {
            changeDirection();
        }

        lastX = x;
        lastY = y;

        updatePlayerLabel();
    }

    private void move() {
        switch (direction) {
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

    private void changeDirection() {
        switch (random.nextInt(4)) {
            case 0:
                direction = "up";
                break;
            case 1:
                direction = "down";
                break;
            case 2:
                direction = "left";
                break;
            case 3:
                direction = "right";
                break;
        }
    }

    private void updatePlayerLabel() {
        playerLabel.setLocation(x, y);
    }

    public void dropUlepszenie() {
        if (random.nextInt(100) < 35) { //35% szans na wyrzucenie ulepszenia
            int imageIndex = random.nextInt(5); //1z5
            pg.spawnUlepszenie(x, y, imageIndex);
        }
    }
}
