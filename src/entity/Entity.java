package entity;

import main.PanelGry;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    protected PanelGry pg;

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public boolean collisionOn = false;

    protected JLabel playerLabel;  //jako chronione pole

    public Entity (PanelGry pg){
        this.pg = pg;
    }
}
