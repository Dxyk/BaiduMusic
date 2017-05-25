package util;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import song.Song;

/**
 * A URL fetcher that when given a song, fetch the playable url from ting.baidu.com
 * and store it to the song's url
 * 
 * @author Xiangyu Kong
 */
public class URLFetcher {
	// The base of the fetching url
	private static final String base = "tingapi.ting.baidu.com";

	/**
	 * Fetch the url of a song and add it to the song's attributes.
	 * 
	 * @param song			The song that need to be fetched
	 * @return				The song with the url added to its attribute
	 * @throws IOException	If the song is not accessible
	 */
	public static Song fetch(Song song) throws IOException {
		String request = "/v1/restserver/ting?format=json&callback=&from=webapp_music&"
				+ "method=baidu.ting.song.play&songid=" + song.getSongId();

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();
			
			// Set the url of the song
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				URI url = new URI(getPlayableUrl(entity));
//				System.out.println(url);
				song.setUrl(url);
				return song;
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		System.err.println("Failed to extract url");
		return song;
	}
	
	/**
	 * Helper function that parse the json from the entity and get the playable URL
	 * 
	 * @param entity		The http entity that contains the playable URL
	 * @return				The string formated url
	 * @throws UnsupportedOperationException
	 * @throws IOException		If the song cannot be accessed
	 * @throws JSONException	If the Json file in the entity is not well formated
	 */
	private static String getPlayableUrl(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {		
		// process the entity to json string format
		String json = IOUtils.toString(entity.getContent(), "GBK");
//		System.out.println(json);
//		String jsonSubStr = json;
		JSONObject object = new JSONObject(json.substring(1, json.length() - 1));
		System.out.println(object);
		
		// get the playable link of the song
		String url = object.getJSONObject("bitrate").getString("show_link");

		return url;

	}

	// Demo
	public static void main(String[] args) {
		try {
			URLFetcher.fetch(Searcher.searchMusic("Hello").get(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
