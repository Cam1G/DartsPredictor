public class Match {
    private final String player1;
    private final String player2;
    private final boolean player1Win;

    public Match(String player1, String player2, int player1Sets, int player2Sets) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Win = player1Sets > player2Sets;
    }

    public Match(String player1, String player2, boolean player1Win) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Win = player1Win;
    }

    public  String getPlayer1() {
        return player1;
    }
    public String getPlayer2() {
        return player2;
    }

    public boolean getPlayer1Win() {
        return player1Win;
    }
}
