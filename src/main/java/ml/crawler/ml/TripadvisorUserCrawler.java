package ml.crawler.ml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import lemas.commons.Find;
import lemas.commons.WriteCSV;
import au.com.bytecode.opencsv.CSVReader;

public class TripadvisorUserCrawler {

	public static void main(String[] args) throws Exception {

		WriteCSV.open("/tmp/detail_users.csv");
		
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
			String page = getPage("http://www.tripadvisor.com.br/members-reviews/" + user); 
			String local = Find.findRegex(page, "<div class=\"memberLoc\">(.+?)</div>");
			String desde = Find.findRegex(page, "<div class=\"memberSince\">Desde (.+?)</div>");
			String avaliacoes = Find.findRegex(page, "<span class=\"sprite-lhnselected\">Avaliações \\((.+?)\\)</span>");		
			
			int count  = 0;
			int pagina = 1;
			
			while (temAvaliacao(user, pagina++)){
			
			List<String> lista_ava = Find.findLines(page, "<a href=\"/Hotel_Review-", "\">");
			for (String string : lista_ava) {
				System.out.println(string);
				count++;
			}

			WriteCSV.add(user+";"+local+";"+desde+";"+avaliacoes);
			
			
			
//			<a href="/members-reviews/Vilane_GS?offset=2">Mais avaliações</a>
			
			
			
			System.out.println(local);
			
		}
		reader.close();
		WriteCSV.close();

	}

}

	private static String getPage(String path) {
		try{
			URL url = new URL(path);		
			InputStream is;
			is = url.openStream(); // throws an IOException
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		}catch(Exception e){
			System.err.println(path);
			return null;
		}		
	}

	private static boolean temAvaliacao(String user, int pagina) {
		String page = getPage("http://www.tripadvisor.com.br/members-reviews/" +  user + "?offset=" + pagina);
		int count = Find.countFindRegex(page, "<a href=\"/(.+?)_Review");
		return false;
	}
}
	
