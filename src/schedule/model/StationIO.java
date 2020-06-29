package schedule.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StationIO {
	public LocalDate inDate;
	public LocalDate outDate;
	public LocalTime inTime;
	public LocalTime outTime;
	public String stationName;

	public StationIO(String stationName) {
		this.stationName = stationName;
	}
}
