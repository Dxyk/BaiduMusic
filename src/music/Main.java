package music;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import song.Song;

public class Main {

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Searcher searcher = new Searcher();
		Downloader downloader = new Downloader();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "quit";

		System.out.println("Please type in search keyword or quit");
		System.out.print(">>>");
		try {
			input = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (!input.equals("quit")) {
			try {
				ArrayList<Song> result = searcher.searchMusic(input);

				System.out.println("--------------------");
				System.out.println("Please select the song (index) you want to listen to:");
				for (int i = 0; i < result.size(); i++) {
					System.out.println((i + 1) + ". " + result.get(i));
				}
				System.out.print(">>>");
				try {
					input = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (isNumeric(input) && (Integer.valueOf(input) < 4 || Integer.valueOf(input) > 0)) {
					downloader.download(result.get(Integer.valueOf(input)));
				} else {
					System.out.println("Invalid index");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Please type in search keyword or quit");
			System.out.print(">>>");

			try {
				input = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Quitting");

	}

}
