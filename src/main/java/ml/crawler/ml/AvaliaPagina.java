package ml.crawler.ml;

import java.io.File;

import lemas.commons.Data;
import lemas.commons.WriteCSV;

public class AvaliaPagina {

	public static void main(String[] args) {
		File file = new File("/tmp/test.html");
		file.delete();
		String urlContent = Data.getUrl("http://www.tripadvisor.com.br/Restaurant_Review-g303506-d7729792-Reviews-or20-Restaurante_Visual-Rio_de_Janeiro_State_of_Rio_de_Janeiro.html#REVIEWS");
		WriteCSV.open(file.getAbsolutePath());
		WriteCSV.add(urlContent);
		WriteCSV.close();
	}
}
