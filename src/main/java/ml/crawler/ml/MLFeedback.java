package ml.crawler.ml;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import lemas.commons.Data;
import lemas.commons.Find;
import lemas.commons.Get;
import lemas.commons.LemasConfig;
import lemas.db.SellerDAO;

import org.apache.xml.utils.XMLChar;

public class MLFeedback {

	public static String folder = System.getProperty("user.home");
	public static int count1 = 0;
	public static int count2 = 0;
	public static int count3 = 0;

	private static SellerDAO dao = new SellerDAO();

	public static void main(String[] args) throws Exception {

		System.out.println("java -jar lemas-extractor-1.0.o-jar-with-dependencies.jar 1 999 15 2000");

		if (args[0].equals("import")) {
			int start = Integer.parseInt(args[1]);
			save(LemasConfig.path + File.separatorChar, start);
			System.out.println("analisados: " + count1);
			System.out.println("deletados: " + count2);
			System.out.println("erro: " + count3);
		}

		if (args[0].equals("verify")) {
			int start = Integer.parseInt(args[1]);
			verify(LemasConfig.path + File.separatorChar, start);
			System.out.println("analisados: " + count1);
			System.out.println("deletados: " + count2);
			System.out.println("erro: " + count3);
		} else {
			int iniciar = Integer.parseInt(args[0]);
			int terminar = Integer.parseInt(args[1]);
			int threads = Integer.parseInt(args[2]);

			boolean save = (args.length == 5) ? Boolean.parseBoolean(args[4]) : false;

			int step = (terminar - iniciar) / threads;

			for (int i = 0; i < threads; i++) {
				int start = (i * step + 1) + iniciar - 1;
				int end = ((i * step) + step) + iniciar - 1;
				System.out.println(start + "," + end);
				new FeedbackTask(start, end, Integer.parseInt(args[3]), save).start();
			}
			System.out.println("fim");
		}
	}

	@SuppressWarnings("unused")
	private static void verify(String path, int start) {
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;
		for (File f : list) {
			if (f.isDirectory()) {
				verify(f.getAbsolutePath(), start);
			} else {
				count1++;
				System.out.print("verify:" + f.getAbsoluteFile());
				MLSeller seller = null;
				try {
					seller = findSeller(f);
					if (seller.getId() >= start) {
						if (seller == null) {
							System.out.println("   DELETED");
							f.delete();
							count2++;
						} else {
							seller.load();

							if (!dao.contains(seller)) {
								dao.insert(seller);
								System.out.println("   INSERIDO");
							}else{
								System.out.println("   JA EXISTE");
							}

							System.out.println("   OK");
						}
					} else {
						System.out.println("   PULOU");
					}
				} catch (RuntimeException e) {
					count3++;
					System.out.print("...PROBLEMA COM " + seller.getName());
//					corrige(seller);
				}
				seller.clear();

			}
		}
	}

	@SuppressWarnings("unused")
	private static void save(String path, int start) {
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;
		for (File f : list) {
			if (f.isDirectory()) {
				verify(f.getAbsolutePath(), start);
			} else {
				count1++;
				System.out.print("verify:" + f.getAbsoluteFile());
				MLSeller seller = null;
				try {
					seller = findSeller(f);
					if (seller.getId() >= start) {
						if (seller == null) {
							System.out.println("   DELETED");
							f.delete();
							count2++;
						} else {
							seller.load();
							System.out.println("   OK");
						}
					} else {
						System.out.println("   PULOU");
					}
				} catch (RuntimeException e) {
					count3++;
					System.out.print("...PROBLEMA COM " + seller.getName());
					corrige(seller);
				}
				seller.clear();

			}
		}
	}

	private static void corrige(MLSeller seller) {
		try {
			StringBuilder sb = new StringBuilder();
			FileInputStream fis = new FileInputStream(seller.getFile());
			int content;
			while ((content = fis.read()) != -1) {
				char c = (char) content;
				if (XMLChar.isValid(c)) {
					sb.append(c);
				}
			}
			fis.close();
			PrintWriter writer = new PrintWriter(seller.getFile(), "UTF-8");
			writer.print(sb.toString());
			writer.close();

			seller.load();
			System.out.println("...RESOLVIDO");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static MLSeller findSeller(File f) {
		if (Data.find.isEmpty()) {
			Data.getSellers();
		}
		return Data.find.get(f.getAbsolutePath());
	}
}

class FeedbackTask extends Thread {
	private static int threadcount = 0;
	private int threadnumber = 0;
	private int start;
	private int end;
	private long sleep;
	private boolean save;

	// public static Hashtable<String, Integer> ids = new Hashtable<String,
	// Integer>();

	public FeedbackTask(int start, int end, long sleep, boolean save) {
		this.start = start;
		this.end = end;
		this.sleep = sleep;
		this.save = save;
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
						if (save) {
							_seller.load();
						}
						if (!_seller.getFile().exists() || save) {
							String url = "";
							if (save) {
								int _page = (((_seller.getFeedbacks().size() / 25) + 1) * 25) + 1;
								url = "http://www.mercadolivre.com.br/jm/profile?act=ver&id=" + _seller.getName() + "&baseLista=" + _page + "&tipo=0&oper=B&orden=1#head_califs";
							} else {
								url = "http://www.mercadolivre.com.br/jm/profile?id=" + _seller.getName();
							}
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
								if (save) {
									if ((_seller.getFeedbacks().size() / 25) % 50 == 0) {
										System.out.println("...SAVE...");
										_seller.save();
									}
								}
							}
							_seller.save();
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
