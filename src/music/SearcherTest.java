package music;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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
 * A Searcher which takes a keyword and returns the search result as HTML form.
 * */
public class SearcherTest {

	private static final String base = "box.zhangmen.baidu.com";
	
	public ArrayList<Song> searchMusic(String keyword) throws IOException {
		ArrayList<Song> result = new ArrayList<Song>();
		
		System.out.println("Searching for: " + keyword);

		// TODO: Determine whether to use utf-8 or not
		String utfKeyword = URLEncoder.encode(keyword, "utf-8");
		String request = "/x?format=xml&op=12&count=1&title=TITLE" + utfKeyword + "$$$$$$";
//		String request = "/x?op=12&count=1&title=TITLE" + keyword + "$$$$$$";
		
		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			System.out.println("Executing on " + target);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();

			// TODO: check status for 200 only? or is there any other code worth checking?
			StatusLine status = response.getStatusLine();
			System.out.println("Status: " + status);

			// get the json content from entity and add each Song obj to result
			if (entity != null) {
				result = parseJson(entity);
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		return result;

	}
	
	/**
	 * Helper function that takes an entity as input and return a result list of songs
	 * */
	private static ArrayList<Song> parseJson(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {
		ArrayList<Song> result = new ArrayList<Song>();
		String json = IOUtils.toString(entity.getContent());
		String jsonSubStr = json.substring(8, json.length() - 1);
		JSONArray arr = new JSONArray(jsonSubStr);
		for (int i = 0; i < arr.length(); i ++) {
			JSONObject obj = arr.getJSONObject(i);
			System.out.println(obj);
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
			for (Song song: searcher.searchMusic("Hello")) {
				System.out.println(song);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}