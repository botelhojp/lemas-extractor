package ml.crawler.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lemas.commons.Find;
import lemas.commons.WriteCSV;

public class PizzariaMilao {

	public static void main(String[] args) throws IOException {
		WriteCSV.open(System.getProperty("user.home") + "/Dropbox/Doutorado/experimentos/tripadvisor/restaurantes_sao_paulo.csv");
		String file = System.getProperty("user.home") + "/Dropbox/Doutorado/experimentos/tripadvisor/restaurantes_sao_paulo.txt";
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String aux = "";
			int count = 0;
			while (line != null) {
				String restaurante = Find.findRegex(line, "Restaurant_Review-(.+?).html");
				if (restaurante != null) {
					if (!aux.equals(restaurante)) {
						System.out.println(++count + " - " + restaurante);
						sb.append(restaurante).append(";");
					}
					aux = restaurante;
				}
				if (line.contains("R$")) {
					List<String> precos = getPrecos(line);
					for (String preco : precos) {
						if (preco.length() > 0) {
							System.out.println(preco);
							sb.append(preco).append(";");
						}
					}
					WriteCSV.add(sb.toString());
					sb = new StringBuilder();
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}

		WriteCSV.close();

	}

	private static List<String> getPrecos(String line) {
		List<String> precos = new ArrayList<String>();
		int index = line.indexOf("$") + 1;
		StringBuffer s = new StringBuffer();
		while (index < line.length()) {
			String ch = line.substring(index, index + 1);
			if (isNumeric(ch)) {
				s.append(ch);
			} else {
				precos.add(s.toString().trim());
				s = new StringBuffer();
				index = line.indexOf("$", index);
				if (index < 0) {
					return precos;
				}
			}
			index++;
		}
		precos.add(s.toString().trim());
		return precos;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
