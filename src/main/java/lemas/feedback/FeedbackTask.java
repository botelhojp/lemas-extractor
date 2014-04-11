package lemas.feedback;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sleepycat.je.rep.elections.Proposer.MaxRetriesException;

import lemas.beans.Seller;
import au.com.bytecode.opencsv.CSVReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

public class FeedbackTask extends Thread {
	
	private int start;
	private int end;

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
					FeedbackConfig.ids.put(seller, numero);
					Seller _seller = new Seller(numero, seller);
					try{
					if (!_seller.getFile().exists()) {
						if (!_seller.complete()) {
							String url = "http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200";
							System.out.println(url);
							String page = new Get(url).getPage();
							int maxPage = getMaxPage(page);
							if (maxPage<1){
								new ProcessPage(_seller).visit(page);
							}else for (int i = 1; i <= maxPage; i++) {
								String newUrl = "http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback2&ftab=AllFeedback&userid=" + seller + "&items=200&page=" + i;
								String newPage = new Get(newUrl).getPage();
								new ProcessPage(_seller).visit(newPage);
								System.out.println(_seller.getFeedbacks().size() + ":" +newUrl);
							}
							_seller.save();
						}
					}
					}catch(java.lang.NumberFormatException e){
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
