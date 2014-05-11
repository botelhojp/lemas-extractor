package lemas.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lemas.commons.Data;
import lemas.commons.LemasConfig;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "tb_agent")
public class MLSeller implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", length = 25, unique=true)
	@NotNull
	private String name;

	@Column(name = "date")
	@Type(type = "date")
	private Date date;

	@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Feedback> feedbacks;

	@Column(name = "iterations")
	private int iterations;

	@Transient
	private String status;

	public MLSeller(int id, String name, int iterations) {
		this();
		this.id = id;
		this.name = name;
		this.iterations = iterations;
	}

	public MLSeller() {
		feedbacks = new ArrayList<Feedback>();
	}

	public MLSeller(int numero, String seller) {
		this(numero, seller, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public void save() {
		Data.sellerToFile(this, getFile());
	}

	public File getFile() {
		int folderId = this.id / 1000;
		String folder = LemasConfig.path + File.separatorChar + folderId;
		File _folder = new File(folder);
		if (!_folder.exists()) {
			_folder.mkdirs();
		}
		return new File(folder + File.separatorChar + add("0", 5, getId()) + "_" + getName().replace("*", "_") + ".xml");
	}

	private String add(String token, int count, int value) {
		String sValue = value + "";
		while (sValue.length() <= count) {
			sValue = "0" + sValue;
		}
		return sValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void load() {
		Data.fileToMLSeller(this, getFile());
	}

	public void clear() {
		this.feedbacks.clear();
	}

	public void load(File f) {
		Data.fileToMLSeller(this, f);
	}

}
