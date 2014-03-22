package lemas.feedback;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

public class LemasTask extends Thread {

	private int start;
	private int end;

	public LemasTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public void run() {
		CSVReader reader;

		try {
			String crawlStorageFolder = LemasConfig.crawlStorageFolder;
			int numberOfCrawlers = (end-start)+1;

			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);

			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller;

			controller = new CrawlController(config, pageFetcher, robotstxtServer);

			

			reader = new CSVReader(new FileReader(LemasTask.class.getResource("/seller.csv").getFile()));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Long numero = Long.parseLong(nextLine[0]);
				String seller = nextLine[1];
				if (numero >= start && numero <= end) {
					System.out.println(numero + "[" + seller + "]");
					controller.addSeed("http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200");
				}
			}
			
			controller.start(LemasWebCrawler.class, numberOfCrawlers);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void explorer(long numero, String seller) {

	}

}
