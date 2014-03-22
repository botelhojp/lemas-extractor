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

			System.out.println(url);

			String contentDate = new String(page.getContentData());

			InputSource inputSource = new InputSource(new StringReader(contentDate));
			XPath xPath = XPathFactory.newInstance().newXPath();
			String _seller = "";

			try {
				_seller = (String) xPath.evaluate("//span[contains(@role, 'presentation')]//a[1]", inputSource, XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
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
}
