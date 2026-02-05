import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DartsScraper {

    public Elements scrapeEvents(int year) throws IOException {
        Elements events;
        try {
            //Connect to dartsdatabase
            Document doc = Jsoup.connect("https://www.dartsdatabase.co.uk/events-all.php")
                    .userAgent("Mozilla/5.0")
                    .data("yearselect", String.valueOf(year))
                    .post();
            //Go to the table and extract links that contain display-event
            events = doc.select(
                    "table.w3-table.w3-striped a[href*='display-event']"
            );
        } catch (IOException e) {
            throw new RuntimeException("Error scraping events for year " +  year, e);
        }
        return events;
    }

    public List<Match> scrapeMatches(Elements events) throws IOException {
        List<Match> matches = new ArrayList<>();
        for (Element link : events) {
            String eventLink = "https://www.dartsdatabase.co.uk/" + link.attr("href");
            Document eventPage = Jsoup.connect(eventLink)
                    .userAgent("Mozilla/5.0")
                    .get();
            Elements outcomes = eventPage.select("tr.match-row");
            for (Element outcome : outcomes) {
                //All the match results
                System.out.println(outcome.text());
                Elements matchData = outcome.select("td");
                String player1 = matchData.get(0).text();
                String result = matchData.get(1).text();
                int player1sets = Integer.parseInt(result.substring(0, result.indexOf('V') - 1));
                int player2sets = Integer.parseInt(result.substring(result.indexOf('V') + 2));
                String player2 = matchData.get(2).text();

                matches.add(new Match(player1, player2, player1sets, player2sets));
            }
        }
        return matches;
    }
}
