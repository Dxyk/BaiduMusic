package music;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import song.Song;

public class MP3Player extends PlaybackListener implements Runnable {
	public AdvancedPlayer player;
	private int pausedOnFrame = 0;
	private Thread playerThread;

	public MP3Player(Song song) {
		super();
		String urlAsString = song.getUrl().toString();
		
		try {
			this.player = new AdvancedPlayer(new URL(urlAsString).openStream(),
					javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
		} catch (JavaLayerException | IOException e) {
			e.printStackTrace();
		}
		
		this.player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackFinished(PlaybackEvent event) {
				System.out.println("Pausing");
				pausedOnFrame = event.getFrame();
				System.out.println(PlaybackEvent.STARTED);
			}
			@Override
			public void playbackStarted(PlaybackEvent event) {
				System.out.println("Starting");
			}
		});
	}

	public void play() throws FileNotFoundException, JavaLayerException {
		try {
			this.player.play(pausedOnFrame, Integer.MAX_VALUE);
			this.playerThread = new Thread(this, "AudioPlayerThread");

	        this.playerThread.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void stop() {
		this.player.stop();
	}

	@Override
	public void run() {
	}

	@Override
	public void playbackStarted(PlaybackEvent playbackEvent) {
		System.out.println("Start playing");
	}

	@Override
	public void playbackFinished(PlaybackEvent playbackEvent) {
		System.out.println("Playing ended");
	}

	public static void main(String args[]) {
		Searcher searcher = new Searcher();
		URLFetcher fetcher = new URLFetcher();

		try {
			Song song = searcher.searchMusic("Hello").get(0);
			fetcher.fetch(song);
			MP3Player player = new MP3Player(song);
			player.play();
			TimeUnit.SECONDS.sleep(5);
			player.stop();
			TimeUnit.SECONDS.sleep(5);
			player.play();
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
