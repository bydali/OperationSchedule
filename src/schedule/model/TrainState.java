package schedule.model;

import java.time.LocalTime;
import java.util.Date;

public class TrainState {

	public int getIdx() {
		return idx;
	}

	public String getTrainNum() {
		return trainNum;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getStationName() {
		return station.getStationName();
	}

	public String getTrack() {
		return track;
	}

	public TypeViaStation getType() {
		return type;
	}

	private int idx;
	private String trainNum;
	private String trainCode;
	private LocalTime time;
	private Station station;
	private String track;
	private TypeViaStation type;

	public TrainState(int idx, String trainNum, String trainCode, LocalTime time, Station station, String track,
			TypeViaStation type) {
		// TODO Auto-generated constructor stub
		this.idx = idx;
		this.trainNum = trainNum;
		this.trainCode = trainCode;
		this.time = time;
		this.station = station;
		this.track = track;
		this.type = type;
	}
}
