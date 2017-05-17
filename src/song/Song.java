package song;

public class Song {
	private String name;
	private int songId;
	private String url;
	private String author;
	
	public Song(String name, int songId, String author) {
		super();
		this.name = name;
		this.songId = songId;
		this.author = author;
	}

	public Song(String name, int songId, String url, String author) {
		super();
		this.name = name;
		this.songId = songId;
		this.url = url;
		this.author = author;
	}
	

	@Override
	public String toString() {
		return "Song name: " + name + "; Author: " + author + "; ID: " + songId;
	}

	public int getSongId() {
		return songId;
	}
	
	
	
}
