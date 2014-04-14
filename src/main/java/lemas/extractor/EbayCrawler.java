package lemas.extractor;

import java.util.ArrayList;
import java.util.regex.Pattern;

import lemas.commons.LemasConfig;
import lemas.commons.LemasRobotstxtServer;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.url.WebURL;

public class EbayCrawler {

	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = LemasConfig.numberOfCrawlers;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		
		controller.addSeed("http://www.ebay.com/usr/case.world");
		controller.start(LemasWebCrawler.class, numberOfCrawlers);
	}
}


class LemasWebCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	
	private static ArrayList<String> urlsVisitadas = new ArrayList<String>();
	
	public LemasWebCrawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		
		if (urlsVisitadas.contains(href))
			return false;
		urlsVisitadas.add(href);
		
		return !FILTERS.matcher(href).matches() && url.getURL().startsWith("http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2");
	}
	

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println(url);
		String contentDate = new String(page.getContentData());
		System.out.println(contentDate);
	}
}
