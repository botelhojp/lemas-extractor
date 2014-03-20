package lemas.extractor;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

public class LemasMain {

	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = LemasConfig.numberOfCrawlers;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		
		controller.addSeed(LemasConfig.seed);
		controller.start(LemasWebCrawler.class, numberOfCrawlers);
	}
}

