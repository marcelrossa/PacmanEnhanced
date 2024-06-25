package entity;

import main.PanelGry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Ulepszenie extends Entity {
    private JLabel ulepszenieLabel;
    private int type;

    public Ulepszenie(PanelGry pg, int type) {
        super(pg);
        this.type = type;
        setDefaultValues();
        getUlepszenieImage(type);
        initUlepszenieLabel();
    }

    public void setDefaultValues() {
        x = -100;//poza plansza na default
        y = -100;
    }

    public void getUlepszenieImage(int type) {
        try {
            switch (type) {
                case 0:
                    up1 = ImageIO.read(getClass().getResourceAsStream("/ulepszenia/1.png"));
                    break;
                case 1:
                    up1 = ImageIO.read(getClass().getResourceAsStream("/ulepszenia/2.png"));
                    break;
                case 2:
                    up1 = ImageIO.read(getClass().getResourceAsStream("/ulepszenia/3.png"));
                    break;
                case 3:
                    up1 = ImageIO.read(getClass().getResourceAsStream("/ulepszenia/4.png"));
                    break;
                case 4:
                    up1 = ImageIO.read(getClass().getResourceAsStream("/ulepszenia/5.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUlepszenieLabel() {
        ulepszenieLabel = new JLabel(new ImageIcon(up1));
        ulepszenieLabel.setBounds(x, y, pg.tileSize, pg.tileSize);
    }

    public void dropUlepszenie(int x, int y, int type) {
        this.x = x;
        this.y = y;
        getUlepszenieImage(type);
        ulepszenieLabel.setIcon(new ImageIcon(up1));
        ulepszenieLabel.setLocation(x, y);
    }

    public JLabel getUlepszenieLabel() {
        return ulepszenieLabel;
    }

    public int getType() {
        return type;
    }
}
