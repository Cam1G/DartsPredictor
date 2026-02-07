import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        DartsScraper scraper = new  DartsScraper();
        List<Match> matches = new ArrayList<>();
        System.out.println("Would you like to rewrite matches.csv y/n");
        String rewriteCSV = input.nextLine();
        while (!rewriteCSV.equals("n") && !rewriteCSV.equals("y")) {
            rewriteCSV = input.nextLine();
        }

        if (rewriteCSV.equalsIgnoreCase("y")) {
            System.out.println("Generating CSV");
            Elements events = scraper.scrapeEvents(2026);
            matches = scraper.scrapeMatches(events);
            scraper.writeMatchesToCSV(matches);
        }
        else {
            matches = scraper.readMatchesFromCSV();
        }

        EloSystem eloSystem = new EloSystem();
        eloSystem.train(matches);

        System.out.println("Input player 1: ");
        String p1Name = input.nextLine();
        while (!eloSystem.getPlayers().containsKey(p1Name)) {
            System.out.println("Player " + p1Name + " does not exist");
            p1Name = input.nextLine();
        }

        System.out.println("Input player 2: ");
        String p2Name = input.nextLine();
        while (!eloSystem.getPlayers().containsKey(p2Name)) {
            System.out.println("Player " + p2Name + " does not exist");
            p2Name = input.nextLine();
        }

        double prob = eloSystem.predict(p1Name, p2Name);
        System.out.println("Probability of " + p1Name + " win : " + (prob));
    }
}
