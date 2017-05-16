package music;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Fetcher {
	
	private static final String base = "box.zhangmen.baidu.com";

	public static void fetchMusic(String song, String author) throws IOException {
		System.out.println("Searching for: " + song + ", " + author);

		String utfSong = URLEncoder.encode(song, "utf-8");
		String utfAuthor = URLEncoder.encode(author, "utf-8");
		String request = "/x?op=12&count=1&title=" + utfSong + "$$" + utfAuthor + "$$$$";
		// String request = "/x?op=12&count=1&title=" + song + "$$" + author + "$$$$";
		
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

//			if (entity != null) {
//				System.out.println(EntityUtils.toString(entity));
//			}
//			
			

			byte[] buffer = new byte[1024];
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);
					while ((bytesRead = bis.read(buffer)) != -1) {
						String chunk = new String(buffer, 0, bytesRead);
						System.out.println(chunk);
					}
				} catch (IOException ioException) {
					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					ioException.printStackTrace();
				} catch (RuntimeException runtimeException) {
					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection immediately.
					httpGet.abort();
					runtimeException.printStackTrace();
				} finally {
					// Closing the input stream will trigger connection release
					try {
						inputStream.close();
					} catch (Exception ignore) {
					}
				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			client.close();
		}

	}

	public static void main(String[] args) {
		try {
			fetchMusic("hello", "");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

}
