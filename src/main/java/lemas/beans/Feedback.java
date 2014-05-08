package lemas.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "tb_feedback")
public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "type", length=3)
	private String type;
	
	@Column(name = "description", length=256)
	private String description;
	
	@Column(name = "from_", length=25)
	private String from;
	
	@Column(name = "fromIterations", length=15)
	private String fromIterations;
	
	@Column(name = "reputation", length=10)
	private String reputation;
	
	@Column(name = "item", length=256)
	private String item;
	
	@Column(name = "price", length=256)
	private String price;
	
	@Column(name = "date", length=10)
	private String date;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller", nullable = false)
	private MLSeller seller;

	public Feedback(String type, String description, String from, String fromIterations, String reputation, String item, String price, String date) {
		super();
		this.type = type.trim();
		this.description = description.trim();
		this.from = from.trim();
		this.fromIterations = fromIterations.trim();
		this.reputation = reputation.trim();
		this.item = item.trim();
		this.price = price.trim();
		this.date = date.trim();
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MLSeller getSeller() {
		return seller;
	}

	public void setSeller(MLSeller seller) {
		this.seller = seller;
	}

}
