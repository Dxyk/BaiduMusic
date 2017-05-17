package music;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.sql.Timestamp;
import song.Song;

public class Downloader {

	private static final String base = "tingapi.ting.baidu.com";

	/**
	 * Download a given song
	 * 
	 * @param song
	 * @throws IOException
	 */
	public void download(Song song) throws IOException {

		System.out.println("Donwloading song: " + song.toString());

		// TODO: Determine whether to use utf-8 or not
		// String utfKeyword = URLEncoder.encode(keyword, "utf-8");
		// String request =
		// "/v1/restserver/ting?format=xml&calback=&from=webapp_music&"
		// + "method=baidu.ting.search.catalogSug&query=" + utfKeyword;
		String request = "method=baidu.ting.song.downWeb&songid=" + song.getSongId() + "&bit=128&_t=" + dateToStamp();
		System.out.println("from url " + base + request);

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);

			// TODO: check status for 200 only? or is there any other code worth
			// checking?
			StatusLine status = response.getStatusLine();
			System.out.println("Status: " + status);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
	}

	/**
	 * Return the current time stamp
	 * 
	 * @return
	 */
	private static long dateToStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.getTime();
	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();

		Downloader player = new Downloader();
		try {
			player.download(searcher.searchMusic("Hello").get(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
