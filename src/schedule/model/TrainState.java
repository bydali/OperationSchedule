package schedule.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class TrainState {

	public int idx;
	public String trainNum;
	public LocalTime time;
	public String stationName;
	public String track;
	public String type;

	public TrainState(int idx, String trainNum, LocalTime time, String stationName, String track, String type) {
		// TODO Auto-generated constructor stub
		this.idx = idx;
		this.trainNum = trainNum;
		this.time = time;
		this.stationName = stationName;
		this.track = track;
		this.type = type;
	}

	public TrainState(List<Object> props) {
		idx = Integer.parseInt(props.get(0).toString());
		trainNum = props.get(1).toString();
		String[] timeInfo = props.get(2).toString().split("  ")[1].split(":");
		time = LocalTime.of(Integer.parseInt(timeInfo[0]), Integer.parseInt(timeInfo[1]),
				Integer.parseInt(timeInfo[2]));
		stationName = props.get(3).toString();
		track = props.get(4).toString();
		type = props.get(5).toString();
	}
}
