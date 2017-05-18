package song;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

public class Song {
	private String name;
	private int songId;
	private String author;
	private URI url;
	
	public Song(String name, int songId, String author) {
		super();
		this.name = name;
		this.songId = songId;
		this.author = author;
		this.url = null;
	}
	
	public boolean hasUrl() {
		return url != null;
	}

	@Override
	public String toString() {
		try {
			String decodedName = URLDecoder.decode(name, "UTF8");
			String decodedAuthor = URLDecoder.decode(author, "UTF8");
			return decodedName + " - " + decodedAuthor + ";\n\tID: " + songId + "; url: " + url;
		} catch (UnsupportedEncodingException e) {
			return name + " - " + author + ";\n\tID: " + songId + "; url: " + url;
		}
		
	}

	public int getSongId() {
		return songId;
	}
	
	public void setUrl(URI url) {
		this.url = url;
	}

	public URI getUrl() {
		return url;
	}

	
}
