package player;

import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;
import song.Song;
import util.Searcher;
import util.URLFetcher;

/**
 * A Pausable Player implemented using JLayer.
 * Supported functions: play, stop, pause and resume.
 * 
 * @author idl-ext2
 * 			Credit to Durandal and Zahlii on stackoverflow.com
 */
public class PausablePlayer {

	private final static int NOTSTARTED = 0;
	private final static int PLAYING = 1;
	private final static int PAUSED = 2;
	private final static int FINISHED = 3;

	// the player actually doing all the work
	private final Player player;

	// locking object used to communicate with player thread
	private final Object playerLock = new Object();

	// status variable what player thread is doing/supposed to do
	private int playerStatus = NOTSTARTED;

	/**
	 * Construct the player using a given input stream
	 * @see javazoom.jl.player.PausablePlayer
	 */
	public PausablePlayer(final InputStream inputStream) throws JavaLayerException {
		this.player = new Player(inputStream);
	}

	/**
	 * Construct the player using a given input stream and an audio device
	 * @see javazoom.jl.player.PausablePlayer
	 */
	public PausablePlayer(final InputStream inputStream, final AudioDevice audioDevice)
			throws JavaLayerException {
		this.player = new Player(inputStream, audioDevice);
	}

	/**
	 * Starts playback (resumes if paused).
	 * 
	 * @throws JavaLayerException
	 */
	public void play() throws JavaLayerException {
		synchronized (playerLock) {
			switch (playerStatus) {
			case NOTSTARTED:
				final Runnable r = new Runnable() {
					public void run() {
						playInternal();
					}
				};
				final Thread t = new Thread(r);
				t.setPriority(Thread.MAX_PRIORITY);
				playerStatus = PLAYING;
				t.start();
				break;
			case PAUSED:
				resume();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Pauses playback. Returns true if new state is PAUSED.
	 */
	public boolean pause() {
		synchronized (playerLock) {
			if (playerStatus == PLAYING) {
				playerStatus = PAUSED;
			}
			return playerStatus == PAUSED;
		}
	}

	/**
	 * Resumes playback. Returns true if the new state is PLAYING.
	 */
	public boolean resume() {
		synchronized (playerLock) {
			if (playerStatus == PAUSED) {
				playerStatus = PLAYING;
				playerLock.notifyAll();
			}
			return playerStatus == PLAYING;
		}
	}

	/**
	 * Stops playback. If not playing, does nothing
	 */
	public void stop() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
			playerLock.notifyAll();
		}
	}

	/**
	 * The internal run method for the player to be runnable
	 */
	private void playInternal() {
		while (playerStatus != FINISHED) {
			try {
				if (!player.play(1)) {
					break;
				}
			} catch (final JavaLayerException e) {
				break;
			}
			// check if paused or terminated
			synchronized (playerLock) {
				while (playerStatus == PAUSED) {
					try {
						playerLock.wait();
					} catch (final InterruptedException e) {
						// terminate player
						break;
					}
				}
			}
		}
		close();
	}

	/**
	 * Closes the player, regardless of current state.
	 */
	public void close() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
		}
		try {
			player.close();
		} catch (final Exception e) {
			// ignore, we are terminating anyway
		}
	}
	
	/**
	 * Determines if the player has finished playing
	 * 
	 * @return	the current state is finished
	 */
	public boolean isFinished() {
		return playerStatus == FINISHED;
	}

	// demo how to use
	public static void main(String[] argv) {
		try {
			Song song = Searcher.searchMusic("Hello").get(0);
			URLFetcher.fetch(song);
			String urlAsString = song.getUrl().toString();

			PausablePlayer player = new PausablePlayer(new URL(urlAsString).openStream(),
					javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

			// start playing
			player.play();

			// after 5 secs, pause
			Thread.sleep(5000);
			player.pause();     

			// after 5 secs, resume
			Thread.sleep(5000);
			player.resume();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

}