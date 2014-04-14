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

public class MLCrawler {

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
		controller.start(Crawler.class, numberOfCrawlers);
		System.out.println("TERMINOU");
		WriteCSV.close();
	}
}


class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	private static int count = 0;
	private static Set<String> vendedores = new HashSet<String>();

	public Crawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches() && 
				(
						url.getURL().startsWith("http://informatica.mercadolivre.com.br") || 
						url.getURL().startsWith("http://produto.mercadolivre.com.br")
				) && 
				!url.getURL().contains("/denounce/") && 
				!url.getURL().contains("/question/");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		String contentDate = new String(page.getContentData());
		String vendedor = Find.findRegex(contentDate, "/jm/profile\\?id=(.+?)\"");
		if (!vendedores.contains(vendedor)){
			System.out.println(count++ + ":" + url);
			vendedores.add(vendedor);
			System.err.println(count+ "     " +vendedor);
			WriteCSV.add(vendedor);	
		}
	}
}