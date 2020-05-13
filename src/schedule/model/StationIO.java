package schedule.model;

import java.time.LocalTime;

public class StationIO {
	public LocalTime inTime;
	public LocalTime outTime;
	public String stationName;

	public StationIO(String stationName) {
		this.stationName = stationName;
	}
}
