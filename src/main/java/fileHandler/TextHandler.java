package fileHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextHandler {
	private BufferedReader reader;
	private BufferedWriter writer;

	
	public void read(String textFilePath) {
		try {
			reader = new BufferedReader(new FileReader(textFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write(String textFilePath) {
		try {
			writer = new BufferedWriter(new FileWriter(textFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> readLines() {
		List<String> lines = new ArrayList<String>();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public TextHandler writeLine(String stringValue) {
		try {
			writer.write(stringValue);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public TextHandler writeLine(int intValue) {
		try {
			writer.write(intValue);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	

}
