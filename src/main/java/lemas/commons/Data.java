package lemas.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import lemas.beans.Feedback;
import lemas.beans.MLSeller;
import lemas.beans.Seller;
import ml.crawler.ml.MLFeedback;

import org.apache.xml.utils.XMLChar;
import org.xml.sax.InputSource;

import au.com.bytecode.opencsv.CSVReader;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

public class Data {

	private static SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
	private static final String tag_seller = "seller";
	private static final String name = "name";
	private static final String id = "id";
	private static final String iterations = "iterations";
	private static final String status = "status";
	private static final String feedbacks = "feedbacks";
	private static final String type = "type";
	private static final String description = "description";
	private static final String from = "from";
	private static final String feedback = "feedback";
	private static final String from_iterations = "from-iterations";
	private static final String tagItem = "item";
	// private static final String reputation = "reputation";
	private static final String price = "price";
	private static final String date = "date";
	private static List<MLSeller> sellers = null;
	public static Hashtable<String, MLSeller> find = new Hashtable<String, MLSeller>();

	public static String loadFileToStr(File file) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				sb.append(br.readLine()).append("\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public synchronized static void sellerToFile(MLSeller seller, File file) {
		try {
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			out = new IndentingXMLStreamWriter(out);
			out.writeStartDocument();
			{
				out.writeStartElement(tag_seller);
				{
					out.writeStartElement(name);
					{
						out.writeCharacters(seller.getName());
					}
					out.writeEndElement();

					out.writeStartElement(id);
					{
						out.writeCharacters(seller.getId() + "");
					}
					out.writeEndElement();

					out.writeStartElement("date");
					{
						out.writeCharacters(Data.dateToStr(seller.getDate()));
					}
					out.writeEndElement();

					out.writeStartElement(iterations);
					{
						out.writeCharacters(seller.getIterations() + "");
					}
					out.writeEndElement();

					out.writeStartElement(status);
					{
						out.writeCharacters(seller.getStatus() + "");
					}
					out.writeEndElement();

					out.writeStartElement(feedbacks);
					{
						for (Feedback item : seller.getFeedbacks()) {
							out.writeStartElement(feedback);
							{
								out.writeStartElement(type);
								{
									out.writeCharacters(item.getFeedback());
								}
								out.writeEndElement();

								out.writeStartElement(description);
								{
									out.writeCharacters(item.getDescription());
								}
								out.writeEndElement();

								out.writeStartElement(from);
								{
									out.writeCharacters(item.getFrom());
								}
								out.writeEndElement();

								out.writeStartElement(from_iterations);
								{
									out.writeCharacters(item.getFromIterations().toString());
								}
								out.writeEndElement();

								// out.writeStartElement(reputation);
								// {
								// out.writeCharacters(item.getReputation());
								// }
								// out.writeEndElement();

								out.writeStartElement(tagItem);
								{
									out.writeCharacters(item.getItem());
								}
								out.writeEndElement();

								out.writeStartElement(price);
								{
									out.writeCharacters(item.getPrice().toString());
								}
								out.writeEndElement();

								out.writeStartElement(date);
								{
									out.writeCharacters(Data.dateToStr(item.getDate()));
								}
								out.writeEndElement();
							}
							out.writeEndElement();
						}
					}
					out.writeEndElement();
				}
				out.writeEndElement();
			}
			out.writeEndDocument();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized static void sellerToFile(Seller seller, File file) {
		try {
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			out = new IndentingXMLStreamWriter(out);
			out.writeStartDocument();
			{
				out.writeStartElement(tag_seller);
				{
					out.writeStartElement(name);
					{
						out.writeCharacters(seller.getName());
					}
					out.writeEndElement();

					out.writeStartElement(id);
					{
						out.writeCharacters(seller.getId() + "");
					}
					out.writeEndElement();

					out.writeStartElement(iterations);
					{
						out.writeCharacters(seller.getIterations() + "");
					}
					out.writeEndElement();

					out.writeStartElement(status);
					{
						out.writeCharacters(seller.getStatus() + "");
					}
					out.writeEndElement();

					out.writeStartElement(feedbacks);
					{
						for (Feedback item : seller.getFeedbacks()) {
							out.writeStartElement(feedback);
							{
								out.writeStartElement(type);
								{
									out.writeCharacters(item.getFeedback());
								}
								out.writeEndElement();

								out.writeStartElement(description);
								{
									out.writeCharacters(item.getDescription());
								}
								out.writeEndElement();

								out.writeStartElement(from);
								{
									out.writeCharacters(item.getFrom());
								}
								out.writeEndElement();

								out.writeStartElement(from_iterations);
								{
									out.writeCharacters(item.getFromIterations().toString());
								}
								out.writeEndElement();

								// out.writeStartElement(reputation);
								// {
								// out.writeCharacters(item.getReputation());
								// }
								// out.writeEndElement();

								out.writeStartElement(tagItem);
								{
									out.writeCharacters(item.getItem());
								}
								out.writeEndElement();

								out.writeStartElement(price);
								{
									out.writeCharacters(item.getPrice().toString());
								}
								out.writeEndElement();

								out.writeStartElement(date);
								{
									out.writeCharacters(Data.dateToStr(item.getDate()));
								}
								out.writeEndElement();
							}
							out.writeEndElement();
						}
					}
					out.writeEndElement();
				}
				out.writeEndElement();
			}
			out.writeEndDocument();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static MLSeller fileToMLSeller(MLSeller seller, File file) {
		try {

			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = factory.createXMLStreamReader(new FileReader(file));
			Feedback f = null;
			while (streamReader.hasNext()) {
				streamReader.next();
				if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
					if (streamReader.getLocalName().equals(name)) {
						seller.setName(streamReader.getElementText());
					}
					if (streamReader.getLocalName().equals(id)) {
						seller.setId(Integer.parseInt(streamReader.getElementText()));
					}
					if (streamReader.getLocalName().equals("date") && f == null) {
						seller.setDate(Data.strToDate(streamReader.getElementText()));
					}
					if (streamReader.getLocalName().equals(iterations)) {
						seller.setIterations(Integer.parseInt(streamReader.getElementText()));
					}
					if (streamReader.getLocalName().equals(status)) {
						seller.setStatus(streamReader.getElementText());
					}
					if (streamReader.getLocalName().equals(feedback)) {
						f = new Feedback();
						f.setType("S");
					}
					if (streamReader.getLocalName().equals(type)) {
						f.setFeedback(streamReader.getElementText());
					}
					if (streamReader.getLocalName().equals(description)) {
						try {
							f.setDescription(streamReader.getElementText());
						} catch (javax.xml.stream.XMLStreamException e) {
							throw new RuntimeException("erro ao ler xml", e);
						}
					}
					if (streamReader.getLocalName().equals(from)) {
						f.setFrom(streamReader.getElementText());
					}
					if (streamReader.getLocalName().equals(from_iterations)) {
						try {
							f.setFromIterations(Integer.parseInt(streamReader.getElementText()));
						} catch (NumberFormatException e) {
							f.setFromIterations(0);
						}
					}
					// if (streamReader.getLocalName().equals(reputation)) {
					// f.setReputation(streamReader.getElementText());
					// }
					if (streamReader.getLocalName().equals(tagItem)) {
						f.setItem(streamReader.getElementText());
					}
					if (streamReader.getLocalName().equals(price)) {
						f.setPrice(Data.strToDouble(streamReader.getElementText()));
					}
					if (streamReader.getLocalName().equals(date) && f != null) {
						f.setDate(Data.strToDate(streamReader.getElementText()));
						seller.getFeedbacks().add(f);
						f.setSeller(seller);
					}
				}
			}
			streamReader.close();
			return seller;
		} catch (Throwable e) {
			throw new RuntimeException("", e);
		}
	}

	public static void main(String args[]) {

		String xml = "<resp><status>good</status><msg>hi</msg></resp>";

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		InputSource source = new InputSource(new StringReader(xml));

		String status = "";
		String msg = "";
		try {
			status = (String) xpath.evaluate("/resp/status", source, XPathConstants.STRING);
			msg = (String) xpath.evaluate("/resp/msg", source, XPathConstants.STRING);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("status=" + status);
		System.out.println("Message=" + msg);

	}

	public static synchronized List<MLSeller> getSellers() {
		try {
			if (sellers == null) {
				sellers = new ArrayList<MLSeller>();
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Enumeration<URL> urls = loader.getResources("vendedores-ml.csv");
				URL _url = (URL) urls.nextElement();
				System.out.println(_url);
				InputStream is = _url.openStream();
				OutputStream os = new FileOutputStream(MLFeedback.folder + File.separatorChar + "vendedores-ml.csv");
				byte[] buffer = new byte[1024];
				while (is.read(buffer) > -1) {
					os.write(buffer);
				}
				is.close();
				os.close();

				CSVReader reader = new CSVReader(new FileReader(MLFeedback.folder + File.separatorChar + "vendedores-ml.csv"));
				String[] nextLine;
				int numero = 0;
				while ((nextLine = reader.readNext()) != null) {
					MLSeller s = new MLSeller(++numero, nextLine[0]);
					sellers.add(s);
					find.put(s.getFile().getAbsolutePath(), s);
				}
				reader.close();
			}
			return sellers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String stripInvalidXmlCharacters(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (XMLChar.isValid(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Date strToDate(String date) {
		try {
			return dt.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String dateToStr(Date date) {
		return dt.format(date);
	}

	public static Double strToDouble(String value) {
		try {
			return Double.parseDouble(value.replaceAll(",", "."));
		} catch (NumberFormatException e) {
			return 0.0;
		}

	}
	
	
	public static String getUrl(String _url) {
		StringBuffer sb = new StringBuffer();
		URL url;
		try {
			url = new URL(_url);
			InputStream is = url.openConnection().getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
