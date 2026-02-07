import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
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
            Element eventInfo = eventPage.selectFirst("strong.w3-text-black:contains(Prize fund)").parent();
            int poundIndex = eventInfo.text().indexOf("£");
            int nextSpaceIndex = eventInfo.text().indexOf(' ', poundIndex);
            if (nextSpaceIndex == -1) {
                nextSpaceIndex = eventInfo.text().length();
            }
            double prizeMoney = 0;
            if (poundIndex != -1 ) {
                String prizeMoneyText = eventInfo.text().substring(poundIndex, nextSpaceIndex);
                //Gives prize money with no decimal point
                prizeMoney = Double.parseDouble(prizeMoneyText.replaceAll("[^0-9.]", ""));
            }
            for (Element outcome : outcomes) {
                //All the match results
                Elements matchData = outcome.select("td");
                String player1 = matchData.get(0).text();
                if (player1.contains(" (")) {
                    player1 = player1.substring(0, player1.indexOf(" ("));
                }
                String result = matchData.get(1).text();
                int player1sets = Integer.parseInt(result.substring(0, result.indexOf('V') - 1));
                int player2sets = Integer.parseInt(result.substring(result.indexOf('V') + 2));
                String player2 = matchData.get(2).text();
                if (player2.contains(" (")) {
                    player2 = player2.substring(0, player2.indexOf(" ("));
                }
                matches.add(new Match(player1, player2, player1sets, player2sets, prizeMoney));
            }
        }
        return matches;
    }

    public void writeMatchesToCSV(List<Match> matches) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("matches.csv"));
        for (Match match : matches) {
            writer.write(match.getPlayer1() + "," +
                    match.getPlayer2() + "," +
                    match.getPlayer1Win() + "," +
                    match.getPrizeMoney()
            );
            writer.newLine();
        }
        writer.close();
    }

    public List<Match> readMatchesFromCSV() throws IOException {
        List<Match> matches = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("matches.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String p1Name = values[0];
                String p2Name = values[1];
                boolean player1Win = Boolean.parseBoolean(values[2]);
                double prizeMoney = Double.parseDouble(values[3]);
                matches.add(new Match(p1Name, p2Name, player1Win, prizeMoney));
            }
        }

        return matches;
    }

}
