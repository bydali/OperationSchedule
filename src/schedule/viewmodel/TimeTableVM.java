package schedule.viewmodel;

import java.util.ArrayList;
import java.util.List;

import schedule.io.ReadFromLocal;
import schedule.model.Station;
import schedule.model.TimeTable;

public class TimeTableVM {
	private TimeTable timeTable;
	private List<StationVM> allStationVM;

	public TimeTableVM() {
		// TODO Auto-generated constructor stub
		generateTimeTableVM();
	}

	private void generateTimeTableVM() {
		// 生成原始时刻表数据
		timeTable = ReadFromLocal.readTT();
		allStationVM = new ArrayList<StationVM>();
		for (Station station : timeTable.getAllStation()) {
			allStationVM.add(new StationVM(station));
		}
	}
}
