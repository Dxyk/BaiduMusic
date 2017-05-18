package music;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import player.PlayerGUI;
import song.Song;

public class Main {

	private static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();
		URLFetcher fetcher = new URLFetcher();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Song selectedSong = null;
		String input = "";

		// get input
		System.out.println("Please type in search keyword");
		System.out.print(">>>");
		try {
			input = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Quitting... ");
			System.exit(-1);
		}

		try {
			// Search for music according to keyword
			ArrayList<Song> result = searcher.searchMusic(input);
			System.out.println("--------------------");
			System.out.println("Please select the song (index) you want to listen to:");
			for (int i = 0; i < result.size(); i++) {
				Song currentSong = result.get(i);
				currentSong = fetcher.fetch(currentSong);
				if (currentSong.hasUrl()) {
					System.out.println((i + 1) + ". " + currentSong);
				} else {
					System.out.println((i + 1) + ". Not Available");
				}
			}
			System.out.print(">>>");
			
			// Select song
			try {
				input = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Quitting... ");
				System.exit(-1);
			}

			if (isNumeric(input) && (Integer.valueOf(input) < 4 || Integer.valueOf(input) > 0)) {
				selectedSong = result.get(Integer.valueOf(input) - 1);
				System.out.println("You selected: ");
				System.out.println(selectedSong);
				PlayerGUI playerGUI = new PlayerGUI(selectedSong);
				playerGUI.createAndShowGUI();
			} else {
				System.err.println("Invalid index");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		
		System.out.println("Quitting ...");
	}

}
