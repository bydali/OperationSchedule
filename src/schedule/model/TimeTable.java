package schedule.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TimeTable {

	public List<TrainState> allTrainState;

	public Map<String, List<TrainState>> allTrainPointTask;

	public List<FullTask> allTrainFullTask;

	public List<Station> allStation;

	// 测试构造函数
	public TimeTable(String stationPath, String timeTablePath) throws IOException {
		// TODO Auto-generated constructor stub
		readAllStations(stationPath);
		readAllTrainStates(timeTablePath);

		generateAllIOTask();
	}

	// 从配置文件读取车站
	public void readAllStations(String path) throws IOException {
		allStation = new ArrayList<Station>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
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

		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(new File(path));
			Element rootElement = document.getRootElement();
			Iterator iterator = rootElement.elementIterator();
			String trainNum = "";
			while (iterator.hasNext()) {
				Element stu = (Element) iterator.next();
				if (stu.getName().equals("LCCC")) {
					trainNum = stu.getText();
				}
				if (stu.getName().equals("YXZD")) {
					Iterator iterator1 = stu.elementIterator();
					while (iterator1.hasNext()) {
						Element stuChild = (Element) iterator1.next();
						List<Object> props = new ArrayList<>();
						props.add(stuChild.attributeValue("CZBH"));
						props.add(trainNum);
						if (stuChild.attributeValue("CZLB").equals("始发站")) {
							props.add(stuChild.attributeValue("CZSJ"));
							props.add(stuChild.attributeValue("CZMZ"));
							props.add(stuChild.attributeValue("TLDS"));
							props.add("发车");
							allTrainState.add(new TrainState(props));
						}
						if (stuChild.attributeValue("CZLB").equals("终到站")) {
							props.add(stuChild.attributeValue("JZSJ"));
							props.add(stuChild.attributeValue("CZMZ"));
							props.add(stuChild.attributeValue("TLDS"));
							props.add("接车");
							allTrainState.add(new TrainState(props));
						}
						if (stuChild.attributeValue("CZLB").equals("途径站")) {
							props.add(stuChild.attributeValue("JZSJ"));
							props.add(stuChild.attributeValue("CZMZ"));
							props.add(stuChild.attributeValue("TLDS"));
							props.add("接车");
							allTrainState.add(new TrainState(props));
							props.remove(props.size() - 1);
							props.add("发车");
							props.set(2, stuChild.attributeValue("CZSJ"));
							allTrainState.add(new TrainState(props));
						}
					}
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// excel读取
		// XSSFRow row;
		// FileInputStream fis = new FileInputStream(new File(path));
		// XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// XSSFSheet spreadsheet = workbook.getSheetAt(0);
		// Iterator<Row> rowIterator = spreadsheet.iterator();
		//
		// int rowNum = 1;
		//
		// while (rowIterator.hasNext()) {
		// // 跳过表头
		// row = (XSSFRow) rowIterator.next();
		// Iterator<Cell> cellIterator = row.cellIterator();
		// if (rowNum != 1) {
		// List<Object> props = new ArrayList<>();
		// while (cellIterator.hasNext()) {
		// Cell cell = cellIterator.next();
		// switch (cell.getCellType()) {
		// case NUMERIC:
		// props.add((int) cell.getNumericCellValue());
		// break;
		// case STRING:
		// props.add(cell.getStringCellValue());
		// break;
		// default:
		// break;
		// }
		// }
		// switch (props.get(props.size() - 1).toString()) {
		// case "通过":
		// props.remove(props.size() - 1);
		// props.add("接车");
		// allTrainState.add(new TrainState(props));
		// props.remove(props.size() - 1);
		// props.add("发车");
		// allTrainState.add(new TrainState(props));
		// break;
		// default:
		// allTrainState.add(new TrainState(props));
		// break;
		// }
		// }
		// rowNum++;
		// }
		// workbook.close();
		// fis.close();
	}

	public Station generateStation(String str) {
		return new Station(str);
	}

	public TimeTable(List<TrainState> allTrainState) {
		// TODO Auto-generated constructor stub
		this.allTrainState = allTrainState;
		generateAllIOTask();
	}

	public void generateAllIOTask() {
		// 以车次为唯一键，将每趟车次的路径放在字典中
		allTrainPointTask = new HashMap<String, List<TrainState>>();
		for (TrainState trainState : allTrainState) {
			if (!allTrainPointTask.containsKey(trainState.trainNum)) {
				List<TrainState> list = new ArrayList<TrainState>();
				list.add(trainState);

				allTrainPointTask.put(trainState.trainNum, list);
			} else {
				allTrainPointTask.get(trainState.trainNum).add(trainState);
			}
		}

		allTrainFullTask = new ArrayList<>();
		for (String key : allTrainPointTask.keySet()) {
			// 进一步整理车次的路径信息，将路径信息按时间排序
			Collections.sort(allTrainPointTask.get(key), new Comparator<TrainState>() {
				public int compare(TrainState o1, TrainState o2) {
					return o1.time.compareTo(o2.time);
				}
			});

			List<StationIO> allStationIO = new ArrayList<>();
			for (Station station : allStation) {
				allStationIO.add(new StationIO(station.getStationName()));
			}

			for (int i = 0; i < allTrainPointTask.get(key).size(); i++) {
				for (int j = 0; j < allStationIO.size(); j++) {
					if (allTrainPointTask.get(key).get(i).stationName.equals(allStationIO.get(j).stationName)) {
						if (i == 0) {
							allStationIO.get(j).outTime = allTrainPointTask.get(key).get(i).time;
							allStationIO.get(j).outDate = allTrainPointTask.get(key).get(i).date;
							break;
						}
						if (allStationIO.get(j).inTime == null) {
							allStationIO.get(j).inTime = allTrainPointTask.get(key).get(i).time;
							allStationIO.get(j).inDate = allTrainPointTask.get(key).get(i).date;
						} else {
							allStationIO.get(j).outTime = allTrainPointTask.get(key).get(i).time;
							allStationIO.get(j).outDate = allTrainPointTask.get(key).get(i).date;
						}
						break;
					}
				}
			}

			FullTask fullTask = new FullTask(key, allStationIO);
			allTrainFullTask.add(fullTask);
		}
	}

	public void updateTrainState(String trainNum, String oldTime, String newTime, String inOrOut) {
		// TODO Auto-generated method stub
		String[] oldTimeInfo = oldTime.split(":");
		LocalTime old = LocalTime.of(Integer.parseInt(oldTimeInfo[0]), Integer.parseInt(oldTimeInfo[1]),
				Integer.parseInt(oldTimeInfo[2]));
		boolean find = false;
		int gapTime = 0;
		for (TrainState trainState : allTrainPointTask.get(trainNum)) {
			if ((!find) && trainState.time.equals(old) && trainState.type.equals(inOrOut)) {
				String[] newTimeInfo = newTime.split(":");
				LocalTime newT = LocalTime.of(Integer.parseInt(newTimeInfo[0]), Integer.parseInt(newTimeInfo[1]),
						Integer.parseInt(newTimeInfo[2]));

				gapTime = newT.getHour() * 3600 + newT.getMinute() * 60 + newT.getSecond()
						- trainState.time.getHour() * 3600 - trainState.time.getMinute() * 60
						- trainState.time.getSecond();

				trainState.time = newT;
				find = true;
			} else if (trainState.time != null) {
				LocalTime new_ = trainState.time.plusSeconds(gapTime);
				trainState.time = new_;
			}
		}
	}
}
