package player;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import javazoom.jl.decoder.JavaLayerException;
import song.Song;
import util.Searcher;
import util.URLFetcher;

/**
 * A GUI for the song player with four buttons: start, stop, pause and resume
 * The GUI quits when the stop button is pressed
 * 
 * @author Xiangyu Kong
 */
public class PlayerGUI {

	private PausablePlayer player;

	private final JFrame window;
	private final JPanel buttonContainer;
	private final JButton startButton;
	private final JButton pauseButton;
	private final JButton stopButton;
	private final JButton resumeButton;

	/**
	 * Construct a player GUI
	 * 
	 * @param song
	 *            The song that will be played
	 * @throws MalformedURLException
	 *             If the URL is malformed
	 * @throws JavaLayerException
	 *             If Exception occurs when using Swing components
	 * @throws IOException
	 *             If Exception occurs when the song is played using the player
	 */
	public PlayerGUI(Song song) throws MalformedURLException, JavaLayerException, IOException {
		String urlAsString = song.getUrl().toString();

		player = new PausablePlayer(new URL(urlAsString).openStream(),
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		window = new JFrame(song.toString());
		buttonContainer = new JPanel();
		// Start button: starts playing the song on click
		startButton = new JButton("start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					player.play();
				} catch (JavaLayerException e1) {
					e1.printStackTrace();
				}
			}
		});
		// Stop button: stop the song and quit the GUI on click
		stopButton = new JButton("stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
				window.dispose();
			}
		});
		// Pause button: pauses the song on click
		pauseButton = new JButton("pause");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});
		// Resume button: resume the song from where it was paused on click
		resumeButton = new JButton("resume");
		resumeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.resume();
			}
		});
		buttonContainer.add(startButton, BorderLayout.LINE_START);
		buttonContainer.add(stopButton, BorderLayout.CENTER);
		buttonContainer.add(pauseButton, BorderLayout.LINE_END);
		buttonContainer.add(resumeButton, BorderLayout.LINE_END);

		Container content = window.getContentPane();
		content.add(buttonContainer, BorderLayout.CENTER);
	}

	/**
	 * Creates and show the GUI and bring it to the center front of the window
	 */
	public void createAndShowGUI() {
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		window.toFront();
		window.setLocation(800, 450);
	}

	// Demo of a GUI player when playing "Hello"
	public static void main(String[] args) {
		Song song;
		try {
			song = Searcher.searchMusic("Hello").get(0);
			URLFetcher.fetch(song);
			PlayerGUI playerGUI = new PlayerGUI(song);
			playerGUI.createAndShowGUI();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

}
