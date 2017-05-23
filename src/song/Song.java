package song;

import java.net.URI;

/**
 * A Song Object
 * 
 * @author Xiangyu Kong
 */
public class Song {
	private String name;
	private int songId;
	private String author;
	private URI url;

	/**
	 * Construct a Song object
	 * 
	 * @param name 		The name of the song
	 * @param songId	The ID of the song
	 * @param author	The author of the song
	 */
	public Song(String name, int songId, String author) {
		super();
		this.name = name;
		this.songId = songId;
		this.author = author;
		this.url = null;
	}

	
	/**
	 * Determine whether the song has a url yet
	 * 
	 * @return		the song's url is not null
	 */
	public boolean hasUrl() {
		return url != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " - " + author + ";\n\tID: " + songId + "; url: " + url;
	}

	/**
	 * Get the ID of the song
	 * 
	 * @return	the ID of the song
	 */
	public int getSongId() {
		return songId;
	}

	/**
	 * Set the URL of the song
	 * 
	 * @param url	The new URL of the song
	 */
	public void setUrl(URI url) {
		this.url = url;
	}

	/**
	 * Get the URL of the song
	 * 
	 * @return	the url of the song
	 */
	public URI getUrl() {
		return url;
	}


	/**
	 * Get the name of the song
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	

}
