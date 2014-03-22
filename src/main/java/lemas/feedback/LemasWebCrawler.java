package lemas.feedback;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class LemasWebCrawler extends WebCrawler {
	
	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	
	private ArrayList<String> urlsVisitadas = new ArrayList<String>();
	
	private static ArrayList<String> users = new ArrayList<String>();
	
	
	public LemasWebCrawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public synchronized boolean shouldVisit(WebURL url) {
		String href = url.getURL();		
		return (!FILTERS.matcher(href).matches() && href.contains("page=")	&& href.contains("AllFeedback"));
	}
	

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (urlsVisitadas.contains(url))
			return;
		urlsVisitadas.add(url);
		
		System.out.println(url);
		//String contentDate2 = new String(page.getContentData());
		//System.out.println(contentDate2);
		
		if (url.startsWith("http://www.ebay.com/itm")){
			String contentDate = new String(page.getContentData());
			Pattern MY_PATTERN = Pattern.compile("<span class=\"mbg-nw\">(.+?)</span>");
			Matcher m = MY_PATTERN.matcher(contentDate);
			while (m.find()) {
			    String s = m.group(1);
			    if (!users.contains(s)){
			    	users.add(s);
			    	System.out.println(users.size() + "," + s);
			    	LemasMain.write(users.size() + "," + s);
			    }
			 }
		}
	}
}
