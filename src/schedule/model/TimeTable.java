package schedule.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TimeTable {

	public List<TrainState> allTrainState;

	public Map<String, List<TrainState>> trainPointTask;

	public Map<String, List<EdgeTask>> trainEdgeTask;

	private List<Station> allStation;

	public List<Station> getAllStation() {
		return allStation;
	}

	// 测试构造函数
	public TimeTable(String stationPath, String timeTablePath) throws IOException {
		// TODO Auto-generated constructor stub
		readAllStations(stationPath);
		readAllTrainStates(timeTablePath);

		generateAllEdgeTask();
	}

	// 从配置文件读取车站
	public void readAllStations(String path) throws IOException {
		allStation = new ArrayList<Station>();

		File file = new File(path);
		BufferedReader reader = null;

		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			Station station = generateStation(tempString);
			allStation.add(station);
		}
		reader.close();
	}

	// 读取时刻表
	public void readAllTrainStates(String path) throws IOException {
		allTrainState = new ArrayList<>();

		XSSFRow row;
		FileInputStream fis = new FileInputStream(new File(path));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet spreadsheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = spreadsheet.iterator();

		int rowNum = 1;

		while (rowIterator.hasNext()) {
			// 跳过表头
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			if (rowNum != 1) {
				List<Object> props = new ArrayList<>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case NUMERIC:
						props.add((int) cell.getNumericCellValue());
						break;
					case STRING:
						props.add(cell.getStringCellValue());
						break;
					default:
						break;
					}
				}
				allTrainState.add(new TrainState(props));
			}
			rowNum++;
		}
		workbook.close();
		fis.close();
	}

	public Station generateStation(String str) {
		return new Station(str);
	}

	public TimeTable(List<TrainState> allTrainState) {
		// TODO Auto-generated constructor stub
		this.allTrainState = allTrainState;
		generateAllEdgeTask();
	}

	public void generateAllEdgeTask() {
		// 按车次整理任务
		trainPointTask = new HashMap<String, List<TrainState>>();
		for (TrainState trainState : allTrainState) {
			if (!trainPointTask.containsKey(trainState.trainNum)) {
				List<TrainState> list = new ArrayList<TrainState>();
				list.add(trainState);

				trainPointTask.put(trainState.trainNum, list);
			} else {
				trainPointTask.get(trainState.trainNum).add(trainState);
			}
		}

		// 对单个车次任务在时间上排序
		trainEdgeTask = new HashMap<String, List<EdgeTask>>();
		for (String key : trainPointTask.keySet()) {
			Collections.sort(trainPointTask.get(key), new Comparator<TrainState>() {
				public int compare(TrainState o1, TrainState o2) {
					return o1.time.compareTo(o2.time);
				}
			});

			List<EdgeTask> edgeTask = new ArrayList<EdgeTask>();
			for (int i = 0; i < trainPointTask.get(key).size() - 2; i++) {
				TrainState firstTrain = trainPointTask.get(key).get(i);
				TrainState secondTrain = trainPointTask.get(key).get(i + 1);

				edgeTask.add(new EdgeTask(firstTrain, secondTrain));
			}
			trainEdgeTask.put(key, edgeTask);
		}
	}
}
