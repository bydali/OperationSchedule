package schedule.model;

import java.time.LocalTime;
import java.util.List;

public class FullTask {
	public String trainNum;
	public List<StationIO> allStationIO;

	public FullTask(String trainNum, List<StationIO> allStationIO) {
		this.trainNum = trainNum;
		this.allStationIO = allStationIO;
	}

}
