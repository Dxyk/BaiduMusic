package downloader;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import song.Song;
import util.Searcher;
import util.URLFetcher;

public class Downloader {
	// The base of the fetching url
	private static final String base = "tingapi.ting.baidu.com";

	public static boolean download(Song song) throws IOException {
		String request = "/v1/restserver/ting?format=json&callback=&from=webapp_music&"
				+ "method=baidu.ting.song.downWeb&songid=" + song.getSongId() + 
				"&bit=24&_t=" + getTimeStamp();
		
		System.out.println("==============================");
		System.out.println("Downloading from url: http://" + base + request);

		CloseableHttpClient client = HttpClients.custom().build();
		try {
			HttpHost target = new HttpHost(base);
			HttpGet httpGet = new HttpGet(request);
			HttpResponse response = client.execute(target, httpGet);
			HttpEntity entity = response.getEntity();

			// Download the song

			System.out.println("Status: " + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
				Desktop desktop = java.awt.Desktop.getDesktop();
				URI url = new URI("http://" + base + request);
				desktop.browse(url);
				
//				File f = new File("C:\\Users\\idl-ext2\\Downloads\\" + song.getName());
//				f.createNewFile();
//				FileUtils.copyURLToFile(url.toURL(), f);
				return true;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}
		System.err.println("Failed to download");
		return false;
	}

	private static long getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.getTime();

	}

	public static void main(String[] args) {
		Song song;
		try {
			song = Searcher.searchMusic("Hello").get(0);
			URLFetcher.fetch(song);
			Downloader.download(song);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
