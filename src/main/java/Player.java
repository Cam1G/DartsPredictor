public class Player {
    private String name;
    private int rating;
    private int k;

    public Player(String name) {
        this.name = name;
        this.rating = 1500;
        this.k = 20;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public int getK() {
        return k;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
