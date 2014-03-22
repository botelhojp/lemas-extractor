package lemas.beans;

public class Feedback {

	private String type;
	private String description;
	private String from;
	private String fromIterations;
	private String reputation;
	private String item;
	private String price;
	private String date;
	
	

	public Feedback(String type, String description, String from, String fromIterations, String reputation, String item, String price, String date) {
		super();
		this.type = type;
		this.description = description;
		this.from = from;
		this.fromIterations = fromIterations;
		this.reputation = reputation;
		this.item = item;
		this.price = price;
		this.date = date;
	}

	public Feedback() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromIterations() {
		return fromIterations;
	}

	public void setFromIterations(String fromIterations) {
		this.fromIterations = fromIterations;
	}

	public String getReputation() {
		return reputation;
	}

	public void setReputation(String reputation) {
		this.reputation = reputation;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
