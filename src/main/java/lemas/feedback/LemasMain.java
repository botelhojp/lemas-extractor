package lemas.feedback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LemasMain {

	private static String path = "C:\\Users\\vanderson\\Downloads\\vendedores.txt";
	private static File file = new File(path);
	private static FileWriter fileWriter;

	public static void main(String[] args) throws Exception {
		
		
		new LemasTask(1, 5).run();
		
//		for (int i = 1 ; i <= 1080; i+=1){
//			int start = ((i-1)*50)+1;
//			int end = (i*50);
//			System.out.println(start + "-" + end );
//			//new LemasTask(start, end).start();
//		}
		
//		new LemasTask(1, 1000).start();
//		new LemasTask(1001, 2000).start();

//		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
//		int numberOfCrawlers = LemasConfig.numberOfCrawlers;
//
//		CrawlConfig config = new CrawlConfig();
//		config.setCrawlStorageFolder(crawlStorageFolder);
//
//		PageFetcher pageFetcher = new PageFetcher(config);
//		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
//		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(
//				robotstxtConfig, pageFetcher);
//		CrawlController controller = new CrawlController(config, pageFetcher,
//				robotstxtServer);
//
//		controller.addSeed("http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + LemasConfig.seller +"&items=200");
//		controller.start(LemasWebCrawler.class, numberOfCrawlers);
	}

	public static void write(String value) {
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
			fileWriter.append(value+ "\n");
			bufferFileWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
