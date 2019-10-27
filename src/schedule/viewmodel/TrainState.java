package schedule.viewmodel;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	private Date date;
	private Station station;
	private String track;
	private TypeViaStation type;
}
