package lemas.feedback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import lemas.beans.Feedback;
import lemas.beans.Seller;

import org.xml.sax.InputSource;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

public class Data {

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
	private static final String reputation = "reputation";
	private static final String price = "price";
	private static final String date = "date";

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
									out.writeCharacters(item.getType());
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
									out.writeCharacters(item.getFromIterations());
								}
								out.writeEndElement();

								out.writeStartElement(reputation);
								{
									out.writeCharacters(item.getReputation());
								}
								out.writeEndElement();

								out.writeStartElement(tagItem);
								{
									out.writeCharacters(item.getItem());
								}
								out.writeEndElement();

								out.writeStartElement(price);
								{
									out.writeCharacters(item.getPrice());
								}
								out.writeEndElement();

								out.writeStartElement(date);
								{
									out.writeCharacters(item.getDate());
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

	public static void fileToSeller(Seller seller, File file) {
		try {
			Feedback f = new Feedback();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = factory.createXMLStreamReader(new FileReader(file));
			while (streamReader.hasNext()) {
				streamReader.next();
				if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {

					if (streamReader.getLocalName().equals(name)) {
						seller.setName(streamReader.getElementText());
					} else if (streamReader.getLocalName().equals(id)) {
						seller.setId(Integer.parseInt(streamReader.getElementText()));
					} else if (streamReader.getLocalName().equals(iterations)) {
						seller.setIterations(Integer.parseInt(streamReader.getElementText()));
					} else

					if (streamReader.getLocalName().equals(status)) {
						seller.setStatus(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(feedback)) {
						f = new Feedback();
					} else

					if (streamReader.getLocalName().equals(type)) {
						f.setType(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(description)) {
						f.setDescription(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(from)) {
						f.setFrom(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(from_iterations)) {
						f.setFromIterations(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(reputation)) {
						f.setReputation(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(tagItem)) {
						f.setItem(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(price)) {
						f.setPrice(streamReader.getElementText());
					} else

					if (streamReader.getLocalName().equals(date)) {
						f.setDate(streamReader.getElementText());
						seller.getFeedbacks().add(f);
					}
				}
			}
			streamReader.close();
		} catch (Exception e) {
			e.printStackTrace();
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
}