package lemas.extractor;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class LemasRobotstxtServer extends RobotstxtServer{

	public LemasRobotstxtServer(RobotstxtConfig config, PageFetcher pageFetcher) {
		super(config, pageFetcher);		
	}
	
	@Override
	public boolean allows(WebURL webURL) {
		return true;
	}

}
