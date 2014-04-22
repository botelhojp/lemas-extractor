package ml.crawler.ml;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import lemas.beans.Seller;
import lemas.commons.Data;
import lemas.commons.Find;
import lemas.commons.Get;
import lemas.commons.LemasConfig;

public class MLFeedback {

	public static String folder = System.getProperty("user.home");

	public static void main(String[] args) throws Exception {
		System.out.println("java -jar lemas-extractor-1.0.o-jar-with-dependencies.jar 1 999 15 2000");

		if (args[0].equals("verify")) {
			verify(LemasConfig.path + File.separatorChar);
		} else {
			int iniciar = Integer.parseInt(args[0]);
			int terminar = Integer.parseInt(args[1]);
			int threads = Integer.parseInt(args[2]);

			int step = (terminar - iniciar) / threads;

			for (int i = 0; i < threads; i++) {
				int start = (i * step + 1) + iniciar - 1;
				int end = ((i * step) + step) + iniciar - 1;
				System.out.println(start + "," + end);
				new FeedbackTask(start, end, Integer.parseInt(args[3])).start();
			}
			System.out.println("fim");
		}
	}

	private static void verify(String path) {
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;
		for (File f : list) {
			if (f.isDirectory()) {
				verify(f.getAbsolutePath());
			} else {
				System.out.print("verify:" + f.getAbsoluteFile());
				if (!findSeller(f)){
					System.out.println("   DELETED");
					f.delete();
				}else{
					System.out.println("   OK");
				}
			}
		}
	}

	private static boolean findSeller(File f) {
		for(MLSeller seller : Data.getSellers()){
			if (seller.getFile().getAbsolutePath().equals(f.getAbsolutePath())){
				return true;
			}
		}
		return false;
	}
}

class FeedbackTask extends Thread {
	private static int threadcount = 0;
	private int threadnumber = 0;
	private int start;
	private int end;
	private long sleep;

	// public static Hashtable<String, Integer> ids = new Hashtable<String,
	// Integer>();

	public FeedbackTask(int start, int end, long sleep) {
		this.start = start;
		this.end = end;
		this.sleep = sleep;
		threadnumber = ++threadcount;
	}

	public void run() {

		try {
			List<MLSeller> sellers = Data.getSellers();
			int numero = 0;
			for (MLSeller seller : sellers) {
				numero++;
				if (numero >= start && numero <= end) {
					System.out.println(numero + "[" + seller.getName() + "]");
					MLSeller _seller = new MLSeller(seller.getId(), seller.getName());
					try {
						if (!_seller.getFile().exists()) {
							if (!_seller.complete()) {
								String url = "http://www.mercadolivre.com.br/jm/profile?id=" + _seller.getName();
								boolean done = false;
								while (!done) {
									double percent = 0.0;
									if (_seller.getIterations() > 0) {
										percent = arredondar(((_seller.getFeedbacks().size() * 1.00) / (_seller.getIterations() * 1.00)) * 100.00, 1, 1);
									}
									Thread.sleep(sleep);
									System.out.println(threadnumber + ": " + percent + "% [" + numero + "]" + _seller.getName() + ":" + url + "\n");
									String page = new Get(url).getPage();
									new ProcessPage(_seller).visit(page);

									List<String> index = Find.findLines(page, "<b class=\"nropag_curr\">", "</b>");
									if (index.size() == 1) {
										int _page = (Integer.parseInt(index.get(0)) * 25) + 1;
										url = "http://www.mercadolivre.com.br/jm/profile?act=ver&id=" + _seller.getName() + "&baseLista=" + _page + "&tipo=0&oper=B&orden=1#head_califs";
									} else {
										done = true;
									}
								}
								_seller.save();
							}
						}
					} catch (java.lang.NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	double arredondar(double valor, int casas, int ceilOrFloor) {
		double arredondado = valor;
		arredondado *= (Math.pow(10, casas));
		if (ceilOrFloor == 0) {
			arredondado = Math.ceil(arredondado);
		} else {
			arredondado = Math.floor(arredondado);
		}
		arredondado /= (Math.pow(10, casas));
		return arredondado;
	}
}

class ProcessPage {

	private MLSeller seller;

	public ProcessPage(MLSeller seller) {
		this.seller = seller;
	}

	public void visit(String page) {
		String contentDate = page;

		String iterations = find(contentDate, 1, 1, 20, "<div id=\"points\"><b>(.+?)<\\/b>");
		String date = find(contentDate, 1, 1, 20, "Membro desde:(.+?)<br>");

		seller.setIterations(Integer.parseInt(iterations));
		seller.setDate(date);

		List<String> lines = Find.findLines(contentDate, "<div id=\"content_box\">", "<div id=\"abajo\" class=\"linea_row\"></div>");

		for (String feedback : lines) {
			String classe = find(feedback, 1, 1, 5000, "rp2\\/(.+?)_fb.gif");
			String description = find(feedback, 1, 1, 5000, "<div id=\"box_texto\">(.+?)<\\/div>");
			String from = find(feedback, 1, 1, 5000, "profile\\?id=(.+?)\\&");

			String fromIteration = find(feedback, 1, 1, 5000, "\\.\\.\\.\\((.+?)\\)<");
			String Star = "";

			String item = find(feedback, 2, 1, 5000, "comprou(.+?)>(.+?)\\.\\.\\.");
			String value = find(feedback, 1, 1, 5000, "R\\$(.+?)<");
			String time = find(feedback, 1, 1, 5000, "fecha\">(.+?) -");
			seller.getFeedbacks().add(new Feedback(classe, description, from, fromIteration, Star, item, value, time));

		}
		seller.setStatus(seller.getFeedbacks().size() + "/" + seller.getIterations());
	}

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
