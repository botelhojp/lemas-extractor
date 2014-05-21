package lemas.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lemas.commons.Data;

import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "tb_feedback")
public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "feedback", length = 3)
	@NotNull
	private String feedback;

	@Column(name = "type", length = 1)
	@NotNull
	private String type;

	@Column(name = "description", length = 256)
	private String description;

	@Column(name = "from_", length = 25)
	private String from;

	@Column(name = "fromIterations")
	private Integer fromIterations;

	@Column(name = "item", length = 256)
	private String item;

	@Column(name = "price")
	private Double price;

	@Column(name = "date", length = 10)
	@Type(type = "date")
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller", nullable = false)
	private MLSeller seller;

	public Feedback(String feedback, String type, String description, String from, String fromIterations, String reputation, String item, String price, String date) {
		super();
		setFeedback(feedback.trim());
		setType("S");
		setDescription(description.trim().replaceAll("\"", ""));
		this.from = from.trim();
		this.fromIterations = Integer.parseInt(fromIterations.trim());
		// this.reputation = reputation.trim();
		this.item = item.trim();
		
		try{
			this.price = Data.strToDouble(price.trim());
		}catch(NumberFormatException e){
			this.price = 0.0;
		}
		
		this.date = Data.strToDate(date);
	}

	public Feedback() {
		// TODO Auto-generated constructor stub
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback.toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		int tam = (description.length() > 256) ? 256 : description.length();
		if (tam > 2) {
			this.description = description.substring(1, tam - 1);
		} else {
			this.description = description;
		}
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getFromIterations() {
		return fromIterations;
	}

	public void setFromIterations(Integer fromIterations) {
		this.fromIterations = fromIterations;
	}

	// public String getReputation() {
	// return reputation;
	// }
	//
	// public void setReputation(String reputation) {
	// this.reputation = reputation;
	// }

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
