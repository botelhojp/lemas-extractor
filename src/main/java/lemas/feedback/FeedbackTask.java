package lemas.feedback;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import lemas.beans.Seller;

import au.com.bytecode.opencsv.CSVReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

public class FeedbackTask extends Thread {

	private int start;
	private int end;

	public FeedbackTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public void run() {
		CSVReader reader;

		try {
			String crawlStorageFolder = FeedbackConfig.crawlStorageFolder;
			//int numberOfCrawlers = (end-start)+1;
			int numberOfCrawlers = 1;

			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);

			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			FeedbackRobotstxtServer robotstxtServer = new FeedbackRobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller;

			controller = new CrawlController(config, pageFetcher, robotstxtServer);

			reader = new CSVReader(new FileReader(FeedbackTask.class.getResource("/seller.csv").getFile()));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				int numero = Integer.parseInt(nextLine[0]);
				String seller = nextLine[1];
				if (numero >= start && numero <= end) {
					System.out.println(numero + "[" + seller + "]");
					FeedbackConfig.ids.put(seller, numero);
					Seller _seller = new Seller(numero, seller);
					if (!_seller.complete()){					
						controller.addSeed("http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200");
					}					
				}
			}
			
			controller.start(FeedbackWebCrawler.class, numberOfCrawlers);
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
