package schedule.viewmodel;

import schedule.model.Station;

public class StationVM {
	
	private String stationName;

	public String getStationName() {
		return stationName;
	}

	public StationVM(Station station) {
		stationName=station.getStationName();
	}
}
