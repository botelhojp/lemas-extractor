package ml.lemas.extractor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import util.Find;
import util.WriteCSV;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class LemasWebCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);

	private static int count = 0;

	private static Set<String> vendedores = new HashSet<String>();

	public LemasWebCrawler() {
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
