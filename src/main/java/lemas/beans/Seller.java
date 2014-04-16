package lemas.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lemas.commons.Data;
import lemas.commons.LemasConfig;

public class Seller {

	private String name;

	private List<Feedback> feedbacks;

	private int id;

	private int iterations;

	private String status;

	private String date;

	public Seller(int id, String name, int iterations) {
		this();
		this.id = id;
		this.name = name;
		this.iterations = iterations;
	}

	public Seller() {
		feedbacks = new ArrayList<Feedback>();
	}

	public Seller(int numero, String seller) {
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

	public boolean complete() {
		File file = getFile();
		if (!file.exists()) {
			return false;
		} else {
			load(file);
			if (this.getIterations() == this.getFeedbacks().size()) {
				return true;
			}
			return false;
		}
	}

	private void load(File file) {
		Data.sellerToFile(this, file);
	}

	public File getFile() {
		int folderId = this.id / 1000;
		String folder = LemasConfig.path + folderId;
		File _folder = new File(folder);
		if (!_folder.exists()) {
			_folder.mkdirs();
		}
		return new File(folder + File.separatorChar + getId() + "_"
				+ getName().replace("*", "_") + ".xml");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

}
