 package music;

import java.io.IOException;
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
 * */
public class Searcher {
	private static final String base = "tingapi.ting.baidu.com";

	/**
	 * Search and return an ArrayList of the top 3 music given the search keyword
	 *
	 * @param keyword
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Song> searchMusic(String keyword) throws IOException {
		ArrayList<Song> result = new ArrayList<Song>();
		System.out.println("Searching for keyword: " + keyword);

//		// TODO: Determine whether to use utf-8 or not
//		String utfKeyword = URLEncoder.encode(keyword, "utf-8");
//		String request = "/v1/restserver/ting?format=xml&calback=&from=webapp_music&"
//				+ "method=baidu.ting.search.catalogSug&query=" + utfKeyword;
		String request = "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
				+ "method=baidu.ting.search.catalogSug&query=" + keyword;
		
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
	 * Helper function that takes an entity as input and return a result ArrayList of songs
	 * @param entity
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws JSONException
	 */
	private static ArrayList<Song> parseJson(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {
		ArrayList<Song> result = new ArrayList<Song>();
		
		// process the entity to json string format
		String json = IOUtils.toString(entity.getContent());
//		System.out.println(json);
		String jsonSubStr = json.substring(8, json.length() - 1);
		JSONArray arr = new JSONArray(jsonSubStr);
		
		// Getting the top 3 search result
		for (int i = 0; i < Math.min(arr.length(), 3); i ++) {
			JSONObject obj = arr.getJSONObject(i);
//			System.out.println(obj);
			Song currentSong = new Song(obj.getString("songname"),
					obj.getInt("songid"),
					obj.getString("artistname"));
			result.add(currentSong);
		}

		return result;
	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();
		try {
			System.out.println("Result:");
			for (Song song: searcher.searchMusic("Hello")) {
				System.out.println(song);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
