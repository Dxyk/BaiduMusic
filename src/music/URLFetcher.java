package music;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import song.Song;

public class URLFetcher {
	
	private static final String base = "tingapi.ting.baidu.com";

	public Song fetch(Song song) throws IOException {
		// TODO: Determine whether to use utf-8 or not
		// String utfKeyword = URLEncoder.encode(keyword, "utf-8");
		// String request =
		// "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
		// + "method=baidu.ting.search.catalogSug&query=" + utfKeyword;
		String request = "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
				+ "method=baidu.ting.song.play&songid=" + song.getSongId();

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				URI url = new URI(getDownloadUrl(entity));
				// System.out.println(url);
				// open the url to listen
				song.setUrl(url);
				return song;
			} else {
				System.err.println("Status error");
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		System.err.println("Failed to extract url");
		return song;
	}
	
	
	private static String getDownloadUrl(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {		
		// process the entity to json string format
		String json = IOUtils.toString(entity.getContent());
//		System.out.println(json);
		String jsonSubStr = json.substring(json.indexOf("bitrate", json.indexOf("bitrate") + 1) + 9, json.length() - 1);
		jsonSubStr = "[" + jsonSubStr + "]";
//		System.out.println(jsonSubStr);
		JSONArray arr = new JSONArray(jsonSubStr);
		
		// get the download link of the song
		JSONObject obj = arr.getJSONObject(0);
		String url = obj.getString("show_link");

		return url;

	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();

		URLFetcher player = new URLFetcher();
		try {
			player.fetch(searcher.searchMusic("Hello").get(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
