package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.opencsv.exceptions.CsvException;

import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class User {

	private String username; // first column

	private String password; // second column

	private int wins; // third column

	private int losses; // fourth column

	private int fastestWin; // fifth column

	private ArrayList<String> wordsGiven;
	private ArrayList<String> wordList;

	public User(String username, String password) {
		// assign the fields for each new user created
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public int getWins() {
		return this.wins;
	}

	public int getLosses() {
		return this.losses;
	}

	public int getFastestWin() {
		return this.fastestWin;
	}

	public void setWins(int numWins) {
		this.wins = numWins;
	}

	public void setLosses(int numLosses) {
		this.losses = numLosses;
	}

	public void setFastestWin(int fastestWin) {
		this.fastestWin = fastestWin;
	}

	/**
	 * setWordList method fills the HashSet wordList with words the user has not
	 * been given
	 */
	public void setWordList() {
		CategorySelector selector;
		try {
			selector = new CategorySelector();
			wordList = (ArrayList<String>) selector.getWordList(Difficulty.E);
			// Checks if there are words given to the user
			if (!wordsGiven.isEmpty()) {
				wordList.removeAll(wordsGiven);
			}

		} catch (IOException | CsvException | URISyntaxException e) {
			return;
		}

	}

	/**
	 * updateWordList saves the new word given to the user and updates what words
	 * can be given to the user for a new game
	 * 
	 * @param word word given to user for the round
	 */
	public void updateWordList(String word) {
		wordList.remove(word);
		wordsGiven.add(word);
	}
}
