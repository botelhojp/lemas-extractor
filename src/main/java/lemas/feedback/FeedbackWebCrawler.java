package lemas.feedback;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lemas.beans.Feedback;
import lemas.beans.Seller;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class FeedbackWebCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(FeedbackConfig.filter);

	private ArrayList<String> urlsVisitadas = new ArrayList<String>();

	// private static ArrayList<String> users = new ArrayList<String>();

	private static Hashtable<String, Seller> sellers = new Hashtable<String, Seller>();

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
		if (urlsVisitadas.contains(url) || !url.contains("page="))
			return;
		urlsVisitadas.add(url);

		System.out.println(url);
		String contentDate = new String(page.getContentData());
		// System.out.println(contentDate);
		String _seller = find(contentDate, 2, 1, 5000, "Member id(.+?)\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>");

		String iterations = find(new String(page.getContentData()), 1, 1, 20, "\\<span class=\"mbg-l\"\\> \\((.+?)\\)", "(.+?)\\<img");

		Seller seller = null;
		if (!sellers.containsKey(_seller)) {
			seller = new Seller(FeedbackConfig.ids.get(_seller), _seller, Integer.parseInt(iterations));
			sellers.put(_seller, seller);
		} else {
			seller = sellers.get(_seller);
		}

		// String table = find(contentDate,
		// "FbOuterYukon\"\\>(.+?)\\<\\/table\\>", 1, 1);
		String table = find(contentDate, 1, 1, Integer.MAX_VALUE, "FbOuterYukon(.+?)Page");
		//String table = contentDate;

		// System.out.println(table);

		List<String> feedbacks = findall(table, "width=\"16\" alt=\"(.+?) feedback rating");
		for (int i = 0; i < feedbacks.size(); i++) {
			String classe = feedbacks.get(i).toLowerCase();
			String description = find(table, 1, i + 1, 5000, "rating\"\\>\\<\\/td\\>\\<td\\>(.+?)\\<\\/td\\>\\<td");
			String from = find(table, 1, i + 1, 5000, "\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>");

			String fromIteration = find(table, 1, i + 1, 10, "\\<span class=\"mbg-l\"\\> \\( (.+?)\\) \\<\\/span\\>", "(.+?)\\<img");
			String Star = find(table, 1, 1, 20, "\\<span class=\"mbg-l\"\\> \\( (.+?)\\) \\<\\/span\\>", "icon\\/icon(.+?)_25x25").toLowerCase();

			String item = find(table, 1, i + 1, 5000, "\\<tr class=\"bot\"\\>\\<td\\>&nbsp;\\<\\/td\\>\\<td\\>(.+?)\\(#");
			String value = find(table, 1, i + 1, 5000, "US \\$(.+?)<");
			String time = find(table, 1, i + 1, 5000, "\\<td nowrap=\"nowrap\"\\>(.+?)<");
			seller.getFeedbacks().add(new Feedback(classe, description, from, fromIteration, Star, item, value, time));

		}
		seller.setStatus(seller.getFeedbacks().size() + "/" + seller.getIterations());
		seller.save();

		System.out.println("(" + seller.getName() + ") end");
	}

	private List<String> findall(String contentDate, String pattern) {
		List<String> l = new ArrayList<String>();
		Pattern MY_PATTERN = Pattern.compile(pattern);
		Matcher m = MY_PATTERN.matcher(contentDate);
		while (m.find()) {
			l.add(m.group(1));
		}
		return l;
	}

	private String find(String contentDate, int group, int _line, int size, String... pattern) {
		boolean recursive = false;
		String _contentDate = contentDate.trim();
		for (String p : pattern) {
			if (recursive){
				_line = 1;
			}
			_contentDate = findPrivate(_contentDate, group, _line, p).trim();
			if (_contentDate.length() <= size) {
				return _contentDate;
			}else{
				recursive = true;
			}
		}
		return "";
	}

	private String findPrivate(String contentDate, int group, int _line, String pattern) {
		Pattern MY_PATTERN = Pattern.compile(pattern);
		Matcher m = MY_PATTERN.matcher(contentDate);
		int line = 0;
		while (m.find()) {
			line++;
			if (line == _line) {
				return m.group(group);
			}
		}
		return "";
	}

}
