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
import music.Searcher;
import music.URLFetcher;
import song.Song;

public class PlayerGUI {

	private PausablePlayer player;
	
	private final JFrame window;
	private final JPanel buttonContainer;
	private final JButton startButton;
	private final JButton pauseButton;
	private final JButton stopButton;
	private final JButton resumeButton;
	
	public PlayerGUI(Song song) throws MalformedURLException, JavaLayerException, IOException {
		String urlAsString = song.getUrl().toString();

		player = new PausablePlayer(new URL(urlAsString).openStream(),
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
		
		window = new JFrame();
		buttonContainer = new JPanel();
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
		pauseButton = new JButton("pause");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});
		stopButton = new JButton("stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
				window.dispose();
			}
		});
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
	
	public void createAndShowGUI() {
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
	public static void main(String[] args) {
		Searcher searcher = new Searcher();
		URLFetcher fetcher = new URLFetcher();
		Song song;
		try {
			song = searcher.searchMusic("Hello").get(0);
			fetcher.fetch(song);
			PlayerGUI playerGUI = new PlayerGUI(song);
			playerGUI.createAndShowGUI();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		

	}

}
