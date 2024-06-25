package main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    private static RankingManager rankingManager = new RankingManager();
    private static JFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowMenu());
    }

    static void createAndShowMenu() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }

        mainFrame = new JFrame("Game Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(60, 63, 65)); //ciemne tlo
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Start Button
        JButton startButton = createMenuButton("Start");
        startButton.addActionListener(e -> {
            mainFrame.dispose();
            showMapSizeMenu();
        });

        // High Score Button
        JButton highScoreButton = createMenuButton("High Score");
        highScoreButton.addActionListener(e -> {
            showHighScoreWindow();
        });

        // Exit Button
        JButton exitButton = createMenuButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(highScoreButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exitButton);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));

        // Button colors and styling
        button.setBackground(new Color(30, 144, 255)); //niebieskie tlo
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); //biala ramka

        // Mouse hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225)); //hover flex
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); //niebieskie jak wczeniej
            }
        });

        return button;
    }

    static void showMapSizeMenu() {
        JFrame frame = new JFrame("Select Map Size");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(60, 63, 65)); //ciemne tlo
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Przyciski do wyboru map
        JButton mapButton1 = createMenuButton("13x13 v1");
        mapButton1.addActionListener(e -> {
            frame.dispose();
            startGame(13, 1);
        });

        JButton mapButton2 = createMenuButton("13x13 v2");
        mapButton2.addActionListener(e -> {
            frame.dispose();
            startGame(13, 2);
        });

        JButton mapButton3 = createMenuButton("13x13 v3");
        mapButton3.addActionListener(e -> {
            frame.dispose();
            startGame(13, 3);
        });

        JButton mapButton4 = createMenuButton("15x15");
        mapButton4.addActionListener(e -> {
            frame.dispose();
            startGame(15, 4);
        });

        JButton mapButton5 = createMenuButton("17x17");
        mapButton5.addActionListener(e -> {
            frame.dispose();
            startGame(17, 5);
        });

        panel.add(mapButton1);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(mapButton2);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(mapButton3);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(mapButton4);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(mapButton5);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void startGame(int mapSize, int mapVersion) {
        JFrame okno = new JFrame("Pacman");
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setResizable(false);

        PanelGry panelGry = new PanelGry(mapSize, mapVersion);
        okno.add(panelGry);

        okno.pack();
        okno.setLocationRelativeTo(null);
        okno.setVisible(true);

        panelGry.startGraThread();
    }

    static void showHighScoreWindow() {
        JFrame highScoreFrame = new JFrame("High Scores");
        highScoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        highScoreFrame.setSize(400, 400);
        highScoreFrame.setLocationRelativeTo(null);

        List<PlayerScore> ranking = rankingManager.getRanking();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (PlayerScore score : ranking) {
            listModel.addElement(score.getName() + ": " + score.getScore());
        }

        JList<String> highScoreList = new JList<>(listModel);
        highScoreList.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(highScoreList);

        highScoreFrame.add(scrollPane);
        highScoreFrame.setVisible(true);
    }

    public static void refreshRanking() {
        rankingManager = new RankingManager();
    }
}
