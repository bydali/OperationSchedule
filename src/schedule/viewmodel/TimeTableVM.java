package schedule.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import schedule.io.ReadFromLocal;
import schedule.model.FullTask;
import schedule.model.Station;
import schedule.model.StationIO;
import schedule.model.TimeTable;
import schedule.model.TrainState;

public class TimeTableVM {

	// 时刻表model
	public static TimeTable timeTable;

	// 全部车站vm
	public static List<StationVM> allStationVM;

	// 列车表格vm
	public static ObservableList<List<SimpleStringProperty>> fullTaskVMList;

	public TimeTableVM() throws IOException {
		// TODO Auto-generated constructor stub
		generateTimeTableVM("");
	}

	public TimeTableVM(TimeTable timeTable) throws IOException {
		// TODO Auto-generated constructor stub
		this.timeTable = timeTable;
		generatePart();
	}

	public TimeTableVM(String trainFile) throws IOException {
		// TODO Auto-generated constructor stub
		generateTimeTableVM(trainFile);
	}

	private void generateTimeTableVM(String trainFile) throws IOException {
		if (trainFile.equals("")) {
			timeTable = ReadFromLocal.readTT();
		} else {
			timeTable = ReadFromLocal.readTT(trainFile);
		}
		generatePart();
	}

	private void generatePart() {
		allStationVM = new ArrayList<StationVM>();
		for (Station station : timeTable.allStation) {
			allStationVM.add(new StationVM(station));
		}

		fullTaskVMList = FXCollections.observableArrayList();
		for (FullTask fullTask : timeTable.allTrainFullTask) {
			List<SimpleStringProperty> lst = new ArrayList<>();
			lst.add(new SimpleStringProperty(fullTask.trainNum));
			for (StationIO stationIO : fullTask.allStationIO) {
				lst.add(new SimpleStringProperty(
						stationIO.inTime == null ? "" : String.format("%tT", stationIO.inTime)));
				lst.add(new SimpleStringProperty(
						stationIO.outTime == null ? "" : String.format("%tT", stationIO.outTime)));
			}
			fullTaskVMList.add(lst);
		}
	}

	public void updateTrainState(String oldTrainNum, String oldTime, String newTime, String inOrOut) {
		// TODO Auto-generated method stub
		timeTable.updateTrainState(oldTrainNum, oldTime, newTime, inOrOut);
		timeTable.generateAllIOTask();
	}

	// 查找车次在某个车站的时间 eg."2012-02-12 19:21:20"
	public static String searchTrainState(String trainNum, String station, String inOrOut) {
		for (FullTask fullTask : timeTable.allTrainFullTask) {
			if (fullTask.trainNum.equals(trainNum)) {
				for (StationIO stationIO : fullTask.allStationIO) {
					if (stationIO.stationName.equals(station)) {
						switch (inOrOut) {
						case "接车":
							return stationIO.inDate.toString() + " " + String.format("%tT", stationIO.inTime);
						case "发车":
							return stationIO.outDate.toString() + " " + String.format("%tT", stationIO.outTime);
						default:
							return "";
						}
					}
				}
				break;
			}
		}

		return "";
	}
}
