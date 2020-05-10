package schedule.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import schedule.io.ReadFromLocal;
import schedule.model.Station;
import schedule.model.TimeTable;
import schedule.model.TrainState;

public class TimeTableVM {

	public TimeTable timeTable;

	private List<StationVM> allStationVM;

	public List<StationVM> getAllStationVM() {
		return allStationVM;
	}

	public ObservableList<TrainStateVM> trainStateVMList;

	public TimeTableVM() throws IOException {
		// TODO Auto-generated constructor stub
		generateTimeTableVM();
	}

	private void generateTimeTableVM() throws IOException {
		// 生成原始时刻表数据
		timeTable = ReadFromLocal.readTT();
		allStationVM = new ArrayList<StationVM>();
		for (Station station : timeTable.getAllStation()) {
			allStationVM.add(new StationVM(station));
		}

		trainStateVMList = FXCollections.observableArrayList();
		for (TrainState trainState : timeTable.allTrainState) {
			trainStateVMList.add(new TrainStateVM(trainState, String.valueOf(trainState.idx), trainState.trainNum,
					trainState.time.toString(), trainState.stationName, trainState.track, trainState.type));
		}
	}
}
