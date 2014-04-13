package ml.lemas.extractor;

import java.util.GregorianCalendar;

import util.WriteCSV;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

public class LemasMain {

	public static void main2(String[] args) throws Exception {
		WriteCSV.open("/tmp/vendedores.txt");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "-1");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "");
		WriteCSV.add(GregorianCalendar.getInstance().getTimeInMillis() + "-10");
		WriteCSV.close();
	}
	public static void main(String[] args) throws Exception {
		
		WriteCSV.open("/tmp/vendedores.txt");

		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = LemasConfig.numberOfCrawlers;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		
		controller.addSeed("http://informatica.mercadolivre.com.br/");
		controller.start(LemasWebCrawler.class, numberOfCrawlers);
		System.out.println("TERMINOU");
		WriteCSV.close();
	}
}

