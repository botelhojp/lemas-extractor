package ml.crawler.ml;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import lemas.commons.Find;
import lemas.commons.LemasConfig;
import lemas.commons.LemasRobotstxtServer;
import lemas.commons.WriteCSV;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.url.WebURL;

public class TripadvisorCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	private static int count = 0;
	private static Set<String> vendedores = new HashSet<String>();

	public TripadvisorCrawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches() && (url.getURL().startsWith("http://www.tripadvisor.com.br/Hotels-") || url.getURL().startsWith("http://www.tripadvisor.com.br/Hotel_Review"));
	}

	@Override
	public void visit(Page page) {
		try {
			String url = page.getWebURL().getURL();
			System.out.println(url);
			String contentDate = new String(page.getContentData());

			String hotel = Find.findRegex(contentDate, "Hotel_Review-(.+?).html");
			if (hotel.length() > 0 && !vendedores.contains(hotel)) {
				
				String estrelas = Find.findRegex(contentDate, "content=\"(.+?)\"");
				String local = Find.findRegex(url, "_State_of(.+?).html");
				
				String avaliacoes = Find.findRegex(contentDate, "<span property=\"v:count\">(.+?)<\\/span>");
				
				
				if (estrelas.length() > 0 && local.length() > 0 && avaliacoes.length() > 0) {
					System.out.println(count++ + ":" + url);
					vendedores.add(hotel);
					System.err.println(count + "     " + hotel);
					WriteCSV.add(hotel+ ";" + estrelas + ";" + local + ";" + avaliacoes);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		WriteCSV.open("/tmp/hoteis.csv");
		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = 3;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		controller.addSeed("http://www.tripadvisor.com.br/Hotels-g303638-Aracaju_State_of_Sergipe-Hotels.html");
		controller.start(TripadvisorCrawler.class, numberOfCrawlers);
		System.out.println("TERMINOU");
		WriteCSV.close();
	}
}
