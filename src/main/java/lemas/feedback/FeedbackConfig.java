package lemas.feedback;

import java.io.File;
import java.util.Hashtable;

public class FeedbackConfig {
	
	public static final String seller = "mono9raph";
	public static final String crawlStorageFolder = "/tmp/t6";
	public static String filter = ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|war|rar|gz))$";
	public static String path = System.getProperty("user.home") + File.separatorChar + "feedbacks" + File.separatorChar;
	public static Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
	

}
