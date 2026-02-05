public class Match {
    private final String player1;
    private final String player2;
    private final int player1Sets;
    private final int player2Sets;
    private final boolean winner; //0 for player1 win 1 for player2 win

    public Match(String player1, String player2, int player1Sets, int player2Sets) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Sets = player1Sets;
        this.player2Sets = player2Sets;
        this.winner = player1Sets < player2Sets;
    }
}
