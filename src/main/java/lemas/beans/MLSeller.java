package lemas.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lemas.commons.Data;
import lemas.commons.LemasConfig;

public class MLSeller {

	private String name;
	private String date;

	private List<Feedback> feedbacks;

	private int id;

	private int iterations;

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
		return new File(folder + File.separatorChar + add("0", 5, getId()) + "_"
				+ getName().replace("*", "_") + ".xml");
	}

	private String add(String token, int count, int value) {
		String sValue = value + "";
		while(sValue.length() <= count){
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void load() {
		Data.fileToMLSeller(this, getFile());
	}

}
