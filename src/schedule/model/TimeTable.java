package schedule.model;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TimeTable {

	private List<TrainState> allTrainState;
	private List<EdgeTask> allTrainTask;
	private List<Station> allStation;

	// 测试构造函数
	public TimeTable() {
		// TODO Auto-generated constructor stub
		allStation = Arrays.asList(new Station("赤壁北"), new Station("岳阳东"), new Station("汨罗东"), new Station("长沙南"),
				new Station("株洲北线路所"), new Station("株洲南线路所"), new Station("株洲西"), new Station("衡山西"),
				new Station("衡阳东"));
		allTrainState = Arrays.asList(
				new TrainState(1, "101001", "101", LocalTime.of(8, 0, 0), new Station("赤壁北"), "1", TypeViaStation.接车),
				new TrainState(2, "101001", "101", LocalTime.of(8, 5, 0), new Station("赤壁北"), "1", TypeViaStation.发车),
				new TrainState(3, "101001", "101", LocalTime.of(8, 10, 0), new Station("岳阳东"), "1", TypeViaStation.接车),
				new TrainState(4, "101001", "101", LocalTime.of(8, 15, 0), new Station("岳阳东"), "1", TypeViaStation.发车),
				new TrainState(5, "101001", "101", LocalTime.of(8, 20, 0), new Station("汨罗东"), "1", TypeViaStation.接车),
				new TrainState(6, "101001", "101", LocalTime.of(8, 25, 0), new Station("汨罗东"), "1", TypeViaStation.发车));
		generateAllEdgeTask(allTrainState);
	}

	public TimeTable(List<TrainState> allTrainState) {
		// TODO Auto-generated constructor stub
		this.allTrainState = allTrainState;
		generateAllEdgeTask(allTrainState);
	}

	private void generateAllEdgeTask(List<TrainState> allTrainState) {
		Collections.sort(allTrainState, new Comparator<TrainState>() {
			public int compare(TrainState o1, TrainState o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		allTrainTask = new ArrayList<EdgeTask>();
		for (int i = 0; i < allTrainState.size() - 1; i++) {
			TrainState firstTrain = allTrainState.get(i);
			TrainState secondTrain = allTrainState.get(i + 1);

			allTrainTask.add(new EdgeTask(firstTrain, secondTrain));
		}
	}

	public List<Station> getAllStation() {
		return allStation;
	}
}
