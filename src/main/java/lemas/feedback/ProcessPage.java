package lemas.feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Find;

import lemas.beans.Feedback;
import lemas.beans.Seller;

public class ProcessPage {

	private Seller seller;
	

	public ProcessPage(Seller seller) {
		this.seller = seller;
	}

	public void visit(String page) {
		String contentDate = page;

//		String _seller = find(contentDate, 2, 1, 5000, "Member id(.+?)\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>");
		String iterations = find(new String(page), 1, 1, 20, "\\<span class=\"mbg-l\"\\> \\((.+?)\\)", "(.+?)\\<img");
		seller.setIterations(Integer.parseInt(iterations));
//		Seller seller = null;
//		if (!sellers.containsKey(_seller)) {
//			seller = new Seller(FeedbackConfig.ids.get(_seller), _seller, Integer.parseInt(iterations));
//			sellers.put(_seller, seller);
//		} else {
//			seller = sellers.get(_seller);
//		}

		List<String> table = Find.findLines(contentDate, "FbOuterYukon", "newPagination");
		List<String> lines = Find.findLines(table.get(0), "height=\"16\" width=\"16\" alt=\"", "<td id=\"viewItemId\">");

		for (String feedback : lines) {
			String classe = find(feedback, 1, 1, 5000, "alt=\"(.+?) feedback rating");
			String description = find(feedback, 1, 1, 5000, "rating\"><\\/td><td>(.+?)<\\/td><td");
			String from = find(feedback, 1, 1, 5000, "<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>");

			String fromIteration = find(feedback, 1, 1, 10, "\\<span class=\"mbg-l\"\\> \\( (.+?)\\) \\<\\/span\\>", "(.+?)\\<img");
			String Star = find(feedback, 1, 1, 20, "\\<span class=\"mbg-l\"\\> \\( (.+?)\\) \\<\\/span\\>", "icon\\/icon(.+?)_25x25").toLowerCase();

			String item = find(feedback, 1, 1, 5000, "<tr class=\"bot\"><td>&nbsp;<\\/td><td>(.+?)\\(#");
			String value = find(feedback, 1, 1, 5000, "US \\$(.+?)<");
			String time = find(feedback, 1, 1, 5000, "\\<td nowrap=\"nowrap\"\\>(.+?)<");
			seller.getFeedbacks().add(new Feedback(classe, description, from, fromIteration, Star, item, value, time));

		}
		seller.setStatus(seller.getFeedbacks().size() + "/" + seller.getIterations());
	}

	

	

//	public void onBeforeExit() {
//		Enumeration<Seller> list = sellers.elements();
//		while (list.hasMoreElements()) {
//			Seller seller = (Seller) list.nextElement();
//			seller.save();
//		}
//	}

	private String find(String contentDate, int group, int _line, int size, String... pattern) {
		boolean recursive = false;
		String _contentDate = contentDate.trim();
		for (String p : pattern) {
			if (recursive) {
				_line = 1;
			}
			_contentDate = findPrivate(_contentDate, group, _line, p).trim();
			if (_contentDate.length() <= size) {
				return _contentDate;
			} else {
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
