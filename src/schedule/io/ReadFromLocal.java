package schedule.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import schedule.model.Station;
import schedule.model.TimeTable;

public class ReadFromLocal {

	// 获取指定行的文件路径
	private static String getPath(int rowNum) throws IOException {
		File file = new File("./src/cfg");
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		int num = 1;
		while ((tempString = reader.readLine()) != null) {
			if (num == rowNum) {
				tempString = tempString.split("：")[1];
				reader.close();
				return tempString;
			}
			num++;
		}
		reader.close();

		return null;
	}

	public static TimeTable readTT() throws IOException {
		return new TimeTable(getPath(1), getPath(2));
	}
}
