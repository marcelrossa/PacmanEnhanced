package main;

import entity.Duch;
import entity.Entity;
import entity.Gracz;
import entity.Ulepszenie;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PanelGry extends JLayeredPane implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public int maxScreenCol;
    public int maxScreenRow;
    int screenWidth;
    int screenHeight;

    int FPS = 60;
    TileManager tileM;
    KeyHandler keyH = new KeyHandler();
    Thread graThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    Gracz gracz;
    Duch duch1, duch2, duch3;
    JLabel[][] tileLabels;
    ArrayList<Ulepszenie> ulepszenia = new ArrayList<>();

    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JButton menuButton;
    private int elapsedTime = 0;//w sek
    private int score = 0;
    private int lives = 3;
    private boolean running = true;
    private RankingManager rankingManager = new RankingManager();

    public PanelGry(int mapSize, int mapVersion) {
        this.maxScreenCol = mapSize;
        this.maxScreenRow = mapSize;
        this.screenWidth = tileSize * maxScreenCol;
        this.screenHeight = tileSize * maxScreenRow;

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(null);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        tileM = new TileManager(this, mapVersion);
        tileLabels = new JLabel[maxScreenCol][maxScreenRow];

        gracz = new Gracz(this, keyH);
        duch1 = new Duch(this, "/npc/1.png");
        duch2 = new Duch(this, "/npc/2.png");
        duch3 = new Duch(this, "/npc/3.png");

        this.add(gracz.getPlayerLabel(), Integer.valueOf(1));//warstwa 1 zeby bylo widac nad plansza
        this.add(duch1.getPlayerLabel(), Integer.valueOf(1));
        this.add(duch2.getPlayerLabel(), Integer.valueOf(1));
        this.add(duch3.getPlayerLabel(), Integer.valueOf(1));

         // Dodanie licznika czasu
        timeLabel = new JLabel("Time: 0s");
        timeLabel.setBounds(10, 10, 200, 30);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(timeLabel, Integer.valueOf(2));//warstwa 2 zeby bylo widac

        // Dodanie licznika punktów
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(220, 10, 200, 30);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(scoreLabel, Integer.valueOf(2)); //warstwa 2 zeby bylo widac

        //Dodanie licznika żyć
        livesLabel = new JLabel("Lives: 3");
        livesLabel.setBounds(430, 10, 200, 30);
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(livesLabel, Integer.valueOf(2));//warstwa 2 zeby bylo widac

        //Dodanie przycisku "Menu"
        menuButton = new JButton("Menu");
        menuButton.setBounds(screenWidth - 90, 10, 75, 30);
        menuButton.addActionListener(e -> returnToMenu());
        this.add(menuButton, Integer.valueOf(2));//warstwa 2 zeby bylo widac
    }

    public void returnToMenu() {
        running = false;
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
        Main.createAndShowMenu();
    }

    public void endGame() {
        running = false;
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        String playerName = JOptionPane.showInputDialog(frame, "Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            rankingManager.addScore(new PlayerScore(playerName, score));
        }
        Main.refreshRanking();
        frame.dispose();
        Main.createAndShowMenu();
    }


    public void startGraThread() {
        graThread = new Thread(this);
        graThread.start();
        startTimerThread(); //watek czas
        startScoreThread(); //watek punkty
        startPowerUpSpawnThread(); //watek duchy ulepszeia
    }

    public void startTimerThread() {
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(1000); //1sek
                        elapsedTime++;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                timeLabel.setText("Time: " + elapsedTime + "s");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timerThread.start();
    }

    public void startScoreThread() {
        Thread scoreThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(500);//co 0.5s 1pkt
                        score += 1;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                scoreLabel.setText("Score: " + score);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        scoreThread.start();
    }

    public void startPowerUpSpawnThread() {
        Thread powerUpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (running) {
                    try {
                        Thread.sleep(5000); // Co 5 sekund
                        if (random.nextInt(100) < 35) { // 35% szans na spawn
                            Duch[] duchy = {duch1, duch2, duch3};
                            Duch duch = duchy[random.nextInt(duchy.length)];
                            spawnUlepszenie(duch.x, duch.y, random.nextInt(5));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        powerUpThread.start();
    }

    public void spawnUlepszenie(int x, int y, int type) {
        Ulepszenie ulepszenie = new Ulepszenie(this, type);
        ulepszenie.dropUlepszenie(x, y, type);
        ulepszenia.add(ulepszenie);
        this.add(ulepszenie.getUlepszenieLabel(), Integer.valueOf(1));
    }

    public void resetGame() {
        gracz.setDefaultValues();//reset
        duch1.setDefaultValues(); //resety
        duch2.setDefaultValues(432); //
        duch3.setDefaultValues(384); //
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (running) {
            update();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        gracz.update();
        duch1.update();
        duch2.update();
        duch3.update();
        checkGhostCollision(); //collision duchy
        //collision upelszenia
        Iterator<Ulepszenie> iterator = ulepszenia.iterator();
        while (iterator.hasNext()) {
            Ulepszenie ulepszenie = iterator.next();
            if (checkCollision(gracz, ulepszenie)) {
                iterator.remove();
                this.remove(ulepszenie.getUlepszenieLabel());
                switch (ulepszenie.getType()) {
                    case 0:
                        gracz.speed += 1;
                        break;
                    case 1:
                        score += 100;
                        break;
                    case 2:
                        score += 300;
                        break;
                    case 3:
                        score += 500;
                        break;
                    case 4:
                        lives += 1;
                        break;
                }
                this.revalidate();
                this.repaint();
            }
        }
    }

    public void checkGhostCollision() {
        if (checkCollision(gracz, duch1) || checkCollision(gracz, duch2) || checkCollision(gracz, duch3)) {
            lives -= 1;
            resetGame();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    livesLabel.setText("Lives: " + lives);
                }
            });

            if (lives <= 0) {
                endGame();
            }
        }
    }

    public boolean checkCollision(Entity entity1, Entity entity2) {
        Rectangle entity1Bounds = new Rectangle(entity1.x, entity1.y, tileSize, tileSize);
        Rectangle entity2Bounds = new Rectangle(entity2.x, entity2.y, tileSize, tileSize);
        return entity1Bounds.intersects(entity2Bounds);
    }
}
