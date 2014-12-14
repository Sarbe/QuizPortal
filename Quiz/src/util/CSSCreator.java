package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSSCreator {
	static String refrenceColor = "1876e8";
	static String refrenceFile = "C:\\Users\\Sarbe\\Desktop\\trns\\main1.css";
	static String cssValues[][] = { { "2", "f66" }, { "3", "639" },
			{ "4", "fc0" }, { "5", "44595a" }, { "6", "24b260" },
			{ "7", "F692F1" }, { "8", "4857B3" }, { "9", "800000" },
			{ "10", "C0C0C0" } };
	static List<String> dataLine = new ArrayList<String>();

	public static void main(String[] args) {
		readFile();
		String code = "";
		String cssVal = "";
		for (int i = 0; i < cssValues.length; i++) {
			code = cssValues[i][0];
			cssVal = cssValues[i][1];
			System.out.println(code + "::" + cssVal);
			writeFile(code, cssVal);
		}
		// writeFile(code,refrenceColor);

	}

	private static void readFile() {

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(refrenceFile));

			while ((sCurrentLine = br.readLine()) != null) {
				dataLine.add(sCurrentLine);
			}
			System.out.println("Fle reading over");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static void writeFile(String code, String cssVal) {
		try {

			File file = new File("C:\\Users\\Sarbe\\Desktop\\trns\\main" + code
					+ ".css");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < dataLine.size(); i++) {
				bw.write(dataLine.get(i).replace(refrenceColor, cssVal));
				bw.newLine();
			}

			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
