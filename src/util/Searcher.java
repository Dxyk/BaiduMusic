package util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import song.Song;

/**
 * A Searcher which takes a keyword and returns a json formated result
 * 
 * Known bugs: 
 * 		json encoding results in resulting Chinese character's last letter error
 *
 * */
public class Searcher {
	// base of the searching url
	private static final String base = "tingapi.ting.baidu.com";
	// the maximum number of results
	private static final int numResult = 3;

	/**
	 * Search and return an ArrayList of the top 3 music given the search keyword
	 *
	 * @param keyword		The searching keyword
	 * @return				The resulting list of the search keyword
	 * @throws IOException
	 */
	public static ArrayList<Song> searchMusic(String keyword) throws IOException {
		ArrayList<Song> result = new ArrayList<Song>();
		System.out.println("Searching for keyword: " + keyword);

		String utfKeyword = URLEncoder.encode(keyword, "UTF8");
		String request = "/v1/restserver/ting?format=json&encoding=utf-8&calback=&from=webapp_music&"
				+ "method=baidu.ting.search.catalogSug&query=" + utfKeyword;

		System.out.println("From url: " + base + request);

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();

			StatusLine status = response.getStatusLine();
			System.out.println("Status: " + status);
			// Checking if status is success
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				result = parseJson(entity);
			} else {
				System.err.println("Status error");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}

	/**
	 * Helper function that takes an entity as input and return a resulting ArrayList of songs
	 * 
	 * @param entity
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws JSONException
	 */
	private static ArrayList<Song> parseJson(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {
		ArrayList<Song> result = new ArrayList<Song>();

		// process the entity to json string format
		String json = IOUtils.toString(entity.getContent(), "GBK");
//		String json = IOUtils.toString(entity.getContent());
		// Error checking if searching failed
		if (json.contains("\"error_message\":\"params error\"") || json.contains("\"error_message\":\"failed\"")) {
			return null;
		}

		String jsonSubStr = json.substring(8, json.length() - 1);
		JSONArray arr = new JSONArray(jsonSubStr);

		// Getting the top 3 search result
		for (int i = 0; i < Math.min(arr.length(), numResult); i ++) {
			JSONObject obj = arr.getJSONObject(i);
			System.out.println("==================");
			System.out.println(obj);

			String encodedName = URLEncoder.encode(obj.getString("songname"), "GBK");
			String encodedAuthor = URLEncoder.encode(obj.getString("artistname"), "GBK");
			String decodedName = URLDecoder.decode(encodedName, "UTF8");
			String decodedAuthor = URLDecoder.decode(encodedAuthor, "UTF8");
			System.out.println("Passed in = " + obj.getString("artistname"));
			System.out.println("Encoded with GBK = " + encodedAuthor);
			System.out.println("Decoded with UTF8 = " + decodedAuthor);
			Song currentSong = new Song(decodedName, obj.getInt("songid"), decodedAuthor);
			result.add(currentSong);
		}

		return result;
	}

	// Demo
	public static void main(String[] args) {
		try {
			System.out.println("Result:");
			for (Song song: Searcher.searchMusic("周杰伦")) {
				System.out.println(song);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
