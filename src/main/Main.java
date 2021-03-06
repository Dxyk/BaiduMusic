package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import player.PlayerGUI;
import song.Song;
import util.Searcher;
import util.URLFetcher;

public class Main {

	/**
	 * Helper function that determines whether a string is numeric
	 * 
	 * @param str	The string to be determined
	 * @return		Whether the string is numeric
	 */
	private static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
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
			ArrayList<Song> result = Searcher.searchMusic(input);
			if (result == null) {
				System.out.println("Result not found. Quitting ...");
				return;
			}
			System.out.println("--------------------");
			System.out.println("Please select the song (index) you want to listen to:");
			for (int i = 0; i < result.size(); i++) {
				Song currentSong = result.get(i);
				currentSong = URLFetcher.fetch(currentSong);
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
			}

			if (isNumeric(input) && Integer.valueOf(input) < 4 && Integer.valueOf(input) > 0) {
				selectedSong = result.get(Integer.valueOf(input) - 1);
				System.out.println("You selected: ");
				System.out.println(selectedSong);
				PlayerGUI playerGUI = new PlayerGUI(selectedSong);
				playerGUI.createAndShowGUI();
				System.out.println("Success");
			} else {
				System.out.println("Invalid index, Quitting ...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		
		
	}

}
