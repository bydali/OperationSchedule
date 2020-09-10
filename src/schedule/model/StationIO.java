package schedule.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StationIO implements Serializable{
	public LocalDate inDate;
	public LocalDate outDate;
	public LocalTime inTime;
	public LocalTime outTime;
	public String stationName;

	public StationIO(String stationName) {
		this.stationName = stationName;
	}
}
