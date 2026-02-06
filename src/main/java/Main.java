import org.jsoup.select.Elements;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DartsScraper scraper = new  DartsScraper();
        EloSystem eloSystem = new EloSystem();

        Elements events = scraper.scrapeEvents(2025);
        List<Match> matches = scraper.scrapeMatches(events);
        eloSystem.train(matches);

        double prob = eloSystem.predict("Luke Littler", "Michael van Gerwen");
        System.out.println("Probability of Luke Littler win : " + (prob));
    }
}
