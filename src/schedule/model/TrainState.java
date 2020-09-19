package schedule.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

public class TrainState implements Serializable{

	public int idx;
	public String trainNum;
	public LocalDate date;
	public LocalTime time;
	public String stationName;
	public String track;
	public String type;
	
	public String rwdh;
	public String czsm;
	public String czlb;
	public String sslj;

	public TrainState(List<Object> props) {
		idx = Integer.parseInt(props.get(0).toString());
		trainNum = props.get(1).toString();
		String[] dateInfo = props.get(2).toString().split("  ")[0].split("-");
		date = LocalDate.of(Integer.parseInt(dateInfo[0]), Integer.parseInt(dateInfo[1]),
				Integer.parseInt(dateInfo[2]));
		String[] timeInfo = props.get(2).toString().split("  ")[1].split(":");
		time = LocalTime.of(Integer.parseInt(timeInfo[0]), Integer.parseInt(timeInfo[1]),
				Integer.parseInt(timeInfo[2]));
		stationName = props.get(3).toString();
		track = props.get(4).toString();
		type = props.get(5).toString();
		
		rwdh=props.get(6).toString();
		czsm=props.get(7).toString();
		czlb=props.get(8).toString();
		sslj=props.get(9).toString();
	}
}
