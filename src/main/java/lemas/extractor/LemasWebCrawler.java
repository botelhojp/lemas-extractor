package lemas.extractor;

import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class LemasWebCrawler extends WebCrawler {
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
