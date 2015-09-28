package ml.crawler.ml;

import java.io.File;

import lemas.commons.Data;
import lemas.commons.WriteCSV;

public class AvaliaPagina {

	public static void main(String[] args) {
		File file = new File("/tmp/test.html");
		file.delete();
		String urlContent = Data.getUrl("http://www.tripadvisor.com.br/Restaurant_Review-g303632-d4025776-Reviews-or20-Pio_Montes-Sorocaba_State_of_Sao_Paulo.html#REVIEWS");
		WriteCSV.open(file.getAbsolutePath());
		WriteCSV.add(urlContent);
		WriteCSV.close();
	}
}
