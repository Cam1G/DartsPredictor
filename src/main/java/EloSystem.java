import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EloSystem {

    Map<String, Player> players = new HashMap<>();

    public void addPlayer(String playerName) {
        if (!players.containsKey(playerName)) {
            players.put(playerName, new Player(playerName));
        }
    }

    public double expectedScore(Player p1, Player p2) {
        return 1 / (1 + Math.pow(10, (double) (p2.getRating() - p1.getRating()) / 400));
    }

    public void updateScore(Player p1, Player p2, boolean player1Win) {
        int s1 = player1Win ? 1 : 0;
        int s2 = player1Win ? 0 : 1;
        double p1Rating = p1.getRating() + p1.getK() * (s1 - expectedScore(p1, p2));
        double p2Rating = p2.getRating() + p2.getK() * (s2 - expectedScore(p2, p1));
        p1.setRating((int)p1Rating);
        p2.setRating((int)p2Rating);
    }

    public void train(List<Match> matches) {
        for (Match match : matches) {
            String p1Name = match.getPlayer1();
            String p2Name = match.getPlayer2();
            addPlayer(p1Name);
            addPlayer(p2Name);
            Player p1 = players.get(p1Name);
            Player p2 = players.get(p2Name);
            updateScore(p1, p2, match.getPlayer1Win());
        }
    }

    public double predict(String p1Name, String p2Name) {
        Player p1  = players.get(p1Name);
        Player p2  = players.get(p2Name);
        return expectedScore(p1, p2);
    }

}
