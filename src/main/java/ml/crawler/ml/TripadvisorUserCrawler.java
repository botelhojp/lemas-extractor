package ml.crawler.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import lemas.commons.DB;
import lemas.commons.Find;

public class TripadvisorUserCrawler {

	public static void main(String[] args) throws Exception {

		if (DB.connect()) {

			URL url;
			InputStream is = null;
			BufferedReader br;
			String line;

			String file = System.getProperty("user.home") + "/Dropbox/Doutorado/experimentos/tripadvisor/hoteis.csv";
			System.out.println(file);

			CSVReader reader = new CSVReader(new FileReader(file), ';');
			String[] nextLine;

			while ((nextLine = reader.readNext()) != null) {
				String user = nextLine[0];
				if (!DB.hasUser(user)) {
					String page = getPage("http://www.tripadvisor.com.br/members-reviews/" + user);
					if (page != null) {
						String local = Find.findRegex(page, "<div class=\"memberLoc\">(.+?)</div>");
						String desde = Find.findRegex(page, "<div class=\"memberSince\">Desde (.+?)</div>");
						String avaliacoes = Find.findRegex(page, "<span class=\"sprite-lhnselected\">Avaliações \\((.+?)\\)</span>");
						int count = 0;
						int pagina = 1;				
						List<String> list = new ArrayList<String>();
						do {
							list.addAll(Find.findLines(page, "<a href=\"/Hotel_Review-", "\">"));							
						}while (temAvaliacao(user, page, ++pagina));
						
						for (String string : list) {
							String p2 = getPage("http://www.tripadvisor.com.br/Hotel_Review-" + string);
							System.out.println(p2);
						}
						
						
						DB.addUser(user);						
					}
				}
			}
			reader.close();
		}

	}

	private static String getPage(String path) {
		try {
			URL url = new URL(path);
			System.out.println(url);
			InputStream is;
			is = url.openStream(); // throws an IOException
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			System.err.println(path);
			return null;
		}
	}

	private static boolean temAvaliacao(String user, String page, int pagina) {
		page = getPage("http://www.tripadvisor.com.br/members-reviews/" + user + "?offset=" + pagina);
		return (Find.countFindRegex(page, "Mais avaliações(.+?)a>") > 0 );
	}
}
