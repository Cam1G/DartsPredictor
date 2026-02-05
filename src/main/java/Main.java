import org.jsoup.select.Elements;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DartsScraper scraper = new  DartsScraper();
        Elements events = scraper.scrapeEvents(2025);
        List<Match> matches = scraper.scrapeMatches(events);

    }
}
