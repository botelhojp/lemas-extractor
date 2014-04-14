package lemas.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lemas.beans.Feedback;
import lemas.beans.Seller;
import lemas.commons.Find;
import lemas.commons.Get;
import au.com.bytecode.opencsv.CSVReader;

public class EbayFeedback {

	private static String path = "C:\\Users\\vanderson\\Downloads\\vendedores.txt";
	private static File file = new File(path);
	private static FileWriter fileWriter;

	public static void main(String[] args) throws Exception {
		int iniciar = 500;
		int terminar = 999;

		int threads = 10;
		int step = terminar / threads;
		for (int i = 0; i < threads; i++) {
			int start = (i * step + 1) + iniciar - 1;
			int end = ((i * step) + step) + iniciar - 1;
			System.out.println(start + "," + end);
			new FeedbackTask(start, end).start();
		}
		System.out.println("fim");
	}

	public static void write(String value) {
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
			fileWriter.append(value + "\n");
			bufferFileWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FeedbackTask extends Thread {

	private int start;
	private int end;
	public static Hashtable<String, Integer> ids = new Hashtable<String, Integer>();

	public FeedbackTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public void run() {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(FeedbackTask.class.getResource("/seller.csv").getFile()));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				int numero = Integer.parseInt(nextLine[0]);
				String seller = nextLine[1];
				if (numero >= start && numero <= end) {
					System.out.println(numero + "[" + seller + "]");
					ids.put(seller, numero);
					Seller _seller = new Seller(numero, seller);
					try {
						if (!_seller.getFile().exists()) {
							if (!_seller.complete()) {
								String url = "http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200";
								System.out.println(url);
								String page = new Get(url).getPage();
								int maxPage = getMaxPage(page);
								if (maxPage < 1) {
									new ProcessPage(_seller).visit(page);
								} else
									for (int i = 1; i <= maxPage; i++) {
										String newUrl = "http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200&page=" + i;
										String newPage = new Get(newUrl).getPage();
										new ProcessPage(_seller).visit(newPage);
										System.out.println(_seller.getFeedbacks().size() + ":" + newUrl);
									}
								_seller.save();
							}
						}
					} catch (java.lang.NumberFormatException e) {
						System.err.println("SEGURANÇA EBAY");
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getMaxPage(String contentDate) {
		Pattern MY_PATTERN = Pattern.compile("page=(.+?)\"");
		Matcher m = MY_PATTERN.matcher(contentDate);
		int max = Integer.MIN_VALUE;
		while (m.find()) {
			int current = Integer.parseInt(m.group(1));
			max = (max <= current) ? current : max;
		}
		return max;
	}

	public void explorer(long numero, String seller) {

	}

}

class ProcessPage {

	private Seller seller;

	public ProcessPage(Seller seller) {
		this.seller = seller;
	}

	public void visit(String page) {
		String contentDate = page;

		// String _seller = find(contentDate, 2, 1, 5000,
		// "Member id(.+?)\\<span class=\"mbg-nw\"\\>(.+?)\\<\\/span\\>");
		String iterations = find(new String(page), 1, 1, 20, "\\<span class=\"mbg-l\"\\> \\((.+?)\\)", "(.+?)\\<img");
		seller.setIterations(Integer.parseInt(iterations));
		// Seller seller = null;
		// if (!sellers.containsKey(_seller)) {
		// seller = new Seller(FeedbackConfig.ids.get(_seller), _seller,
		// Integer.parseInt(iterations));
		// sellers.put(_seller, seller);
		// } else {
		// seller = sellers.get(_seller);
		// }

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

	// public void onBeforeExit() {
	// Enumeration<Seller> list = sellers.elements();
	// while (list.hasMoreElements()) {
	// Seller seller = (Seller) list.nextElement();
	// seller.save();
	// }
	// }

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
