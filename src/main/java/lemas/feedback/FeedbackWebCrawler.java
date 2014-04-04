package lemas.feedback;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lemas.beans.Feedback;
import lemas.beans.Seller;

import org.xml.sax.InputSource;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class FeedbackWebCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(FeedbackConfig.filter);

	private ArrayList<String> urlsVisitadas = new ArrayList<String>();

	private static ArrayList<String> users = new ArrayList<String>();

	public FeedbackWebCrawler() {
		System.out.println("Iniciando o Crawller");
	}

	@Override
	public synchronized boolean shouldVisit(WebURL url) {
		String href = url.getURL();
		return (!FILTERS.matcher(href).matches() && href.contains("page=") && href.contains("AllFeedback"));
	}

	@Override
	public void visit(Page page) {
			String url = page.getWebURL().getURL();
			if (urlsVisitadas.contains(url))
				return;
			urlsVisitadas.add(url);

		String _seller = find(new String(page.getContentData()), "Member id(.+?)\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>", 2, 1);
		
		String table = find(new String(page.getContentData()), "FbOuterYukon\"\\>(.+?)\\<\\/table\\>", 1, 1);
		System.out.println(table);
		
		Seller seller = new Seller(FeedbackConfig.ids.get(_seller), _seller, Integer.parseInt("8"));
		
		List<String> feedbacks = findall(table, "width=\"16\" alt=\"(.+?) feedback rating");
		for(int i = 0; i < feedbacks.size(); i ++){
			String classe = feedbacks.get(i).toLowerCase();
			String description = find(table, "rating\"\\>\\<\\/td\\>\\<td\\>(.+?)\\<\\/td\\>\\<td", 1, i+1);
			String from = find(table, "\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>", 1, i+1);
			//String fromIteration =  find(table, "\\( (.+?) \\)", 1, i+1);
			String item =  find(table, "\\<tr class=\"bot\"\\>\\<td\\>&nbsp;\\<\\/td\\>\\<td\\>(.+?)\\(#", 1, i+1);
			String value = find(table, "US \\$(.+?)<", 1, i+1);
			String time = find(table, "\\<td nowrap=\"nowrap\"\\>(.+?)<", 1, i+1);
					
			
			String iterations = "";
			try {
				iterations = xPath.evaluate("//span[contains(@role, 'presentation')]//a[2]", inputSource);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			Seller seller = new Seller(FeedbackConfig.ids.get(_seller), _seller, Integer.parseInt(iterations));

			seller.getFeedbacks().add(new Feedback("positive", "very gooe", "mira", "300", "start4", "computer", "3,8", "10/30/2014"));
			seller.getFeedbacks().add(new Feedback("negative", "very gooe", "mira", "300", "start4", "computer", "3,8", "10/30/2014"));

			seller.save();


		// String contentDate2 = new String(page.getContentData());
		// System.out.println(contentDate2);

		// if (url.startsWith("http://www.ebay.com/itm")) {
		// String contentDate = new String(page.getContentData());
		// Pattern MY_PATTERN =
		// Pattern.compile("<span class=\"mbg-nw\">(.+?)</span>");
		// Matcher m = MY_PATTERN.matcher(contentDate);
		// while (m.find()) {
		// String s = m.group(1);
		// if (!users.contains(s)) {
		// users.add(s);
		// System.out.println(users.size() + "," + s);
		// FeedbackMain.write(users.size() + "," + s);
		// }
		// }
		// }
	}

	private List<String> findall(String contentDate, String pattern) {
		List<String> l = new ArrayList<String>();
		Pattern MY_PATTERN = Pattern.compile(pattern);
		Matcher m = MY_PATTERN.matcher(contentDate);
		while (m.find()) {
			System.out.println(m.group(1));
			l.add(m.group(1));
		}
		return l;
	}

	private String find(String contentDate, String pattern, int group, int _line) {
		Pattern MY_PATTERN = Pattern.compile(pattern);
		Matcher m = MY_PATTERN.matcher(contentDate);
		int line = 0;
		while (m.find()) {
			line++;
			if (line == _line){
				return m.group(group);	
			}
		}
		throw new RuntimeException("String não encontrada [" + pattern + "]");
	}
}
