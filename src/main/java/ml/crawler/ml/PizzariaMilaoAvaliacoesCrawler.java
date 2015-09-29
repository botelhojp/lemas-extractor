package ml.crawler.ml;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.url.WebURL;
import lemas.commons.Find;
import lemas.commons.LemasConfig;
import lemas.commons.LemasRobotstxtServer;
import lemas.commons.WriteCSV;

public class PizzariaMilaoAvaliacoesCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(LemasConfig.filter);
	private static int avaliacoesTotal = 0;

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
			String url = page.getWebURL().getURL();
			System.out.println(url);

			String contentDate = new String(page.getContentData());
			List<String> avaliacoes = Find.findLines(contentDate, "<div class=\"review", "<span class=\"isHelpful\">");
			System.out.println("\t" + avaliacoes.size() + " avaliações.");
			avaliacoesTotal += avaliacoes.size();
			for (String avaliacao : avaliacoes) {
				String comments = Find.findRegex(avaliacao, "<span class='noQuotes'>(.+?)</span>");
				String valor = Find.findRegex(avaliacao, "<img class=\"sprite-rating_s_fill rating_s_fill s(.+?)0\"");
				String user = Find.findRegex(avaliacao, "user_name_name_click'\\)\">(.+?)</span>").replace(" ", "-");
				String server = Find.findRegex(url, "_Review-(.+?)-Reviews-");
				String data = Find.findRegex(avaliacao, "<span class=\"ratingDate\">Avaliou em (.+?)\n").replace("</span></div> </div>", "");
//				String confianca_revidor = Find.findRegex(avaliacao, "<span class=\"badgeText\">\n(.+?) votos");
				String revisor_local = Find.findRegex(avaliacao, "<div class=\"location\">\n(.+?)\n</div>").replace("</div>", "null").replace(",", ":");
				String save = "\"" + user + "\",\"" + server + "\",\"" + convertDate(data) + "\",\"" + "general" + "\",\"" + getCost(server) + "\",\"" + revisor_local + "\",\"" + comments + "\",\"" + getValue(valor) + "\"";
				WriteCSV.add(save);
				System.out.println(save);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private String getCost(String server) {
		if (values.containsKey(server)) {
			return values.get(server);
		}
		return "ERROR";
	}

	private String getValue(String valor) {
		int v = Integer.parseInt(valor);
		switch (v) {
		case 1:
			return "Horrível";
		case 2:
			return "Ruim";
		case 3:
			return "Razoável";
		case 4:
			return "Muito_bom";
		case 5:
			return "Excelente";
		default:
			return "ERROR";
		}
	}

	private String convertDate(String data) {
		data = data.replace(" de Janeiro de ", "/01/");
		data = data.replace(" de Fevereiro de ", "/02/");
		data = data.replace(" de Março de ", "/03/");
		data = data.replace(" de Abril de ", "/04/");
		data = data.replace(" de Maio de ", "/05/");
		data = data.replace(" de Junho de ", "/06/");
		data = data.replace(" de Julho de ", "/07/");
		data = data.replace(" de Agosto de ", "/08/");
		data = data.replace(" de Setembro de ", "/09/");
		data = data.replace(" de Outubro de ", "/10/");
		data = data.replace(" de Novembro de ", "/11/");
		data = data.replace(" de Dezembro de ", "/12/");
		data = data.replace("de", "ERROR");
		return data;
	}

	public static List<String> listRest = load();
	public static Hashtable<String, String> values;

	public static void main(String[] args) throws Exception {
		String filepath =  System.getProperty("user.home") + "/Dropbox/Doutorado/experimentos/tripadvisor/avaliacoes_resta_sao_paulo.csv";
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		WriteCSV.open(filepath);
		WriteCSV.add("% Feedbacks");
		WriteCSV.add("");
		WriteCSV.add("@RELATION feedbacks");
		WriteCSV.add("@ATTRIBUTE client string");
		WriteCSV.add("@ATTRIBUTE server string");
		WriteCSV.add("@ATTRIBUTE date date dd/MM/yyyy");
		WriteCSV.add("@ATTRIBUTE context string");
		WriteCSV.add("@ATTRIBUTE cost real");
		WriteCSV.add("@ATTRIBUTE local string");
		WriteCSV.add("@ATTRIBUTE comments string");
		WriteCSV.add("@ATTRIBUTE feedback { Excelente, Muito_bom, Razoável, Ruim , Horrível }");
		WriteCSV.add("");
		WriteCSV.add("@DATA");

		String crawlStorageFolder = LemasConfig.crawlStorageFolder;
		int numberOfCrawlers = 10;

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
		System.out.println("TERMINOU com " + avaliacoesTotal + " avaliações.");
		WriteCSV.close();
	}

	private static List<String> load() {
		values = new Hashtable<String, String>();
		try {
			List<String> r = new ArrayList<String>();
			File file = new File(System.getProperty("user.home") + "/Dropbox/Doutorado/experimentos/tripadvisor/restaurantes_sao_paulo.csv");
			CSVReader reader = new CSVReader(new FileReader(file), ';');
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				String server = nextLine[0];
				String value = nextLine[2];
				if (!values.containsKey(server)) {
					values.put(server, value.replace(",", "."));
				}
				r.add(server);
			}
			reader.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
