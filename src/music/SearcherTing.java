package music;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * A Searcher which takes a keyword and returns a json formated result
 * */

public class SearcherTing {
	private static final String base = "tingapi.ting.baidu.com";

	public static ArrayList<Song> searchMusic(String keyword) throws IOException {
		ArrayList<Song> result = new ArrayList<Song>();
		
		System.out.println("Searching for: " + keyword);

		// TODO: Determine whether to use utf-8 or not
//		String utfKeyword = URLEncoder.encode(keyword, "utf-8");
//		String request = "/v1/restserver/ting?format=xml&calback=&from=webapp_music&"
//				+ "method=baidu.ting.search.catalogSug&query=" + utfKeyword;
		String request = "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
				+ "method=baidu.ting.search.catalogSug&query=" + keyword;
		
		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			System.out.println("Executing on " + target);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine());

			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}

			// TODO: currently printing out the content
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity));
			}
			

		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		return result;

	}

	public static void main(String[] args) {
		try {
			searchMusic("hello");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
