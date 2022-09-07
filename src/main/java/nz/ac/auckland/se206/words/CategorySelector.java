package nz.ac.auckland.se206.words;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CategorySelector {

	public enum Difficulty {
		E, M, H
	}

	private Map<Difficulty, List<String>> difficultyToCategories;

	public CategorySelector() throws IOException, CsvException, URISyntaxException {
		difficultyToCategories = new HashMap<>();
		for (Difficulty difficulty : Difficulty.values()) {
			// creates an empty ArrayList for each difficulty
			difficultyToCategories.put(difficulty, new ArrayList<>());
		}

		for (String[] line : getLines()) {
			// adds each word on each line to its corresponding difficulty ArrayList
			difficultyToCategories.get(Difficulty.valueOf(line[1])).add(line[0]);
		}
	}

	/**
	 * This method gets a random word of a specified difficulty
	 *
	 * @param difficulty difficulty of words to select from
	 * @return
	 */
	public String getRandomCategory(Difficulty difficulty) {
		// returns a random category of a specified difficulty
		return difficultyToCategories.get(difficulty)
				.get(new Random().nextInt(difficultyToCategories.get(difficulty).size()));
	}

	protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
		File fileName = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

		try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8); CSVReader reader = new CSVReader(fr)) {
			// returns a list of string array where each string array is each line in the
			// csv file
			// each word in each string array is separated by commas in each line in the csv
			// file
			return reader.readAll();
		}
	}

	public List<String> getWordList(Difficulty difficulty) {
		return difficultyToCategories.get(difficulty);
	}
}
