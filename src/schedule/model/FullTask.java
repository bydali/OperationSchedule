package schedule.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class FullTask implements Serializable{
	public String trainNum;
	public List<StationIO> allStationIO;

	public FullTask(String trainNum, List<StationIO> allStationIO) {
		this.trainNum = trainNum;
		this.allStationIO = allStationIO;
	}

}
