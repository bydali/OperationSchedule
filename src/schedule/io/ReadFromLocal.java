package schedule.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import schedule.model.Station;
import schedule.model.TimeTable;

public class ReadFromLocal {

	// 获取指定行的文件路径
	private static String getPath(int rowNum) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("cfg.txt"), "UTF-8"));
		String line = null;
		int num = 1;
		while ((line = br.readLine()) != null) {
			if (num == rowNum) {
				br.close();
				return line.split("：")[1];
			}
			num++;
		}
		br.close();
		return null;
	}

	public static TimeTable readTT() throws IOException {
		return new TimeTable(getPath(1), getPath(2));
	}
}
