package ml.crawler.ml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.url.WebURL;
import lemas.commons.Data;
import lemas.commons.Find;
import lemas.commons.LemasConfig;
import lemas.commons.LemasRobotstxtServer;
import lemas.commons.WriteCSV;

public class PizzariaMilaoAvaliacoesCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	private static int count = 0;
	private static Set<String> vendedores = new HashSet<String>();

	public PizzariaMilaoAvaliacoesCrawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches() && (verifyllist(url));
	}

	private boolean verifyllist(WebURL url) {
		for (String string : listRest) {
			if (url.getURL().startsWith("http://www.tripadvisor.com.br/Restaurant_Review-" + string + "-Reviews-")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void visit(Page page) {
		try {
			WriteCSV.open("/tmp/tmp.html");

			String url = page.getWebURL().getURL();
			System.out.println(url);

			String urlContent = Data.getUrl(url);

			String contentDate = new String(page.getContentData());
			WriteCSV.add(urlContent);
			WriteCSV.close();
			System.out.println("\ttamnho:" + contentDate.length());
			System.out.println("\tprimeiro:" + Find.findRegex(contentDate, "<span class='noQuotes'>(.+?)</span>"));
			List<String> avaliacoes = Find.findLines(contentDate, "<div class=\"review", "<span class=\"isHelpful\">");
			System.out.println("\t" + avaliacoes.size() + " avaliações.");
			if (avaliacoes.size() == 6) {
				System.out.println("vai");
				System.out.println(contentDate);
			}
			for (String avaliacao : avaliacoes) {
				String frase = Find.findRegex(avaliacao, "<span class='noQuotes'>(.+?)</span>");
				System.out.println("\t" + frase);
			}

			// String restaurante = Find.findRegex(contentDate,
			// "Restaurant_Review-(.+?).html");

			// if (restaurante.length() > 0 &&
			// !vendedores.contains(restaurante)) {
			// WriteCSV.add(restaurante);

			// String estrelas = Find.findRegex(contentDate,
			// "content=\"(.+?)\"");
			// String local = Find.findRegex(url, "_State_of(.+?).html");
			//
			// String avaliacoes = Find.findRegex(contentDate, "<span
			// property=\"v:count\">(.+?)<\\/span>");
			//
			//
			// if (estrelas.length() > 0 && local.length() > 0 &&
			// avaliacoes.length() > 0) {
			// System.out.println(count++ + ":" + url);
			// vendedores.add(hotel);
			// System.err.println(count + " " + hotel);
			// WriteCSV.add(hotel+ ";" + estrelas + ";" + local + ";" +
			// avaliacoes);
			// }
			// }
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	

	public static String[] listRest = { "g187849-d833443"
			// "g187849-d784583"
	};

	public static void main(String[] args) throws Exception {
		WriteCSV.open("/tmp/hoteis.csv");
		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		LemasRobotstxtServer robotstxtServer = new LemasRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		for (String r : listRest) {
			controller.addSeed("http://www.tripadvisor.com.br/Restaurant_Review-" + r + ".html");
		}

		controller.start(PizzariaMilaoAvaliacoesCrawler.class, numberOfCrawlers);
		System.out.println("TERMINOU");
		WriteCSV.close();
	}
}
