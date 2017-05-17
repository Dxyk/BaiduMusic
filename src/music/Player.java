package music;

import java.io.IOException;
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

public class Player {
	
	private static final String base = "tingapi.ting.baidu.com";

	public void play(Song song) throws IOException {

		System.out.println("Donwloading song: " + song.toString());

		// TODO: Determine whether to use utf-8 or not
		// String utfKeyword = URLEncoder.encode(keyword, "utf-8");
		// String request =
		// "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
		// + "method=baidu.ting.search.catalogSug&query=" + utfKeyword;
		String request = "/v1/restserver/ting?format=json&calback=&from=webapp_music&"
				+ "method=baidu.ting.song.play&songid=" + song.getSongId();
		System.out.println("from url " + base + request);

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();
			
			StatusLine status = response.getStatusLine();
			System.out.println("Status: " + status);
			
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				parseJson(entity);
			} else {
				System.err.println("Status error");
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
	}
	
	
	private static String parseJson(HttpEntity entity) throws UnsupportedOperationException, IOException, JSONException {		
		// process the entity to json string format
		String json = IOUtils.toString(entity.getContent());
//		System.out.println(json);
		String jsonSubStr = json.substring(json.indexOf("bitrate", json.indexOf("bitrate") + 1) + 9, json.length() - 1);
		jsonSubStr = "[" + jsonSubStr + "]";
		System.out.println(jsonSubStr);
		JSONArray arr = new JSONArray(jsonSubStr);
		
		// Getting the top 3 search result
		
		JSONObject obj = arr.getJSONObject(0);
		String url = obj.getString("show_link");

		return url;

	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();

		Player player = new Player();
		try {
			player.play(searcher.searchMusic("Hello").get(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
