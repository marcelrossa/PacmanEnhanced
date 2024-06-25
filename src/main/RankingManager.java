package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingManager {
    private static final String FILE_NAME = "ranking.txt";
    private List<PlayerScore> ranking;

    public RankingManager() {
        ranking = new ArrayList<>();
        loadRanking();
    }

    public void addScore(PlayerScore score) {
        ranking.add(score);
        Collections.sort(ranking);
        saveRanking();
    }

    public List<PlayerScore> getRanking() {
        return new ArrayList<>(ranking);
    }

    private void saveRanking() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (PlayerScore score : ranking) {
                writer.write(score.getName() + "," + score.getScore());

                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRanking() {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    ranking.add(new PlayerScore(name, score));
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        Collections.sort(ranking);
    }
}
