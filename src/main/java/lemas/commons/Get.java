package lemas.commons;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Get {
	
	private String url;
	
	public Get(String url){
		this.url = url;
	}

	public String getPage() {
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse responseGet = HttpClientBuilder.create().build().execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null) {
				return EntityUtils.toString(resEntityGet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
