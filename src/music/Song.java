package music;

public class Song {
	private String name;
	private int songId;
	private String url;
	
	public Song(String name, int songId) {
		super();
		this.name = name;
		this.songId = songId;
	}

	public Song(String name, int songId, String url) {
		super();
		this.name = name;
		this.songId = songId;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
	
}
