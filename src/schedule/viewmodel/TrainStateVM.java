package schedule.viewmodel;

import java.time.LocalTime;

import javafx.beans.property.SimpleStringProperty;
import schedule.model.TrainState;

public class TrainStateVM {
	private final SimpleStringProperty id_v;
	private final SimpleStringProperty trainNum_v;
	private final SimpleStringProperty time_v;
	private final SimpleStringProperty station_v;
	private final SimpleStringProperty track_v;
	private final SimpleStringProperty type_v;

	private TrainState trainState;

	public TrainStateVM(TrainState trainState, String id, String trainNum, String time, String station, String track,
			String type) {
		this.trainState = trainState;
		this.id_v = new SimpleStringProperty(id);
		this.trainNum_v = new SimpleStringProperty(trainNum);
		this.time_v = new SimpleStringProperty(time);
		this.station_v = new SimpleStringProperty(station);
		this.track_v = new SimpleStringProperty(track);
		this.type_v = new SimpleStringProperty(type);
	}

	public String getId_v() {
		return id_v.get();
	}

	public void setId_v(String id_v) {
		this.id_v.set(id_v);
		trainState.idx = Integer.valueOf(id_v);
	}

	public String getTrainNum_v() {
		return trainNum_v.get();
	}

	public void setTrainNum_v(String trainNum_v) {
		this.trainNum_v.set(trainNum_v);
		trainState.trainNum = trainNum_v;
	}

	public String getTime_v() {
		return time_v.get();
	}

	public void setTime_v(String time_v) {
		this.time_v.set(time_v);
		trainState.time = LocalTime.of(Integer.valueOf(time_v.split(":")[0]), Integer.valueOf(time_v.split(":")[1]),
				Integer.valueOf(time_v.split(":")[2]));
	}

	public String getStation_v() {
		return station_v.get();
	}

	public void setStation_v(String station_v) {
		this.station_v.set(station_v);
		trainState.stationName = station_v;
	}

	public String getTrack_v() {
		return track_v.get();
	}

	public void setTrack_v(String track_v) {
		this.track_v.set(track_v);
		trainState.track = track_v;
	}

	public String getType_v() {
		return type_v.get();
	}

	public void setType_v(String type_v) {
		this.type_v.set(type_v);
		trainState.type = type_v;
	}

}
