package schedule.model;

import java.io.Serializable;

public class Station implements Serializable{

	private String stationName;

	public String getStationName() {
		return stationName;
	}

	public Station(String stationName) {
		// TODO Auto-generated constructor stub
		this.stationName = stationName;
	}

}
