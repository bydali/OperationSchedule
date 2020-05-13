package schedule.skeleton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections4.map.HashedMap;
import org.w3c.dom.css.Rect;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import schedule.model.TrainState;
import schedule.station.FoldBodyController;
import schedule.station.FoldHeadController;
import schedule.viewmodel.StationVM;
import schedule.viewmodel.TimeTableVM;

/**
 * 测试运行图左上单元格长宽为，h=40，w=200
 * 
 * @author Administrator
 *
 */
public class OperateChartController implements Initializable {

	@FXML
	private ScrollPane operateSp;
	@FXML
	private ScrollPane stationSp;
	@FXML
	private ScrollPane timeSp;
	@FXML
	private ScrollPane sp;
	@FXML
	private AnchorPane ap;
	@FXML
	private VBox operateVb;
	@FXML
	private Group operateGroup;
	@FXML
	private AnchorPane timeAp;
	@FXML
	private VBox headsVb;

	private double oldX;
	private double oldY;
	private double oldHValue;
	private double oldVValue;

	private final int leftOffset = 20;
	private final int hourInterval = 240;
	private final int startTime = 6;
	private final int endTime = 24;
	private final int upHeight = 50;
	private final int bottomHeight = 150;

	private List<String> stationsOrder;
	private Map<Polyline, List<Rectangle>> polylineStatue;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setData(TimeTableVM timeTableVM) throws IOException {
		syncSliding();
		setPaneSize(endTime - startTime);
		drawTime(startTime, endTime);
		drawHeads(timeTableVM.getAllStationVM());
		drawOperateMap(timeTableVM.getAllStationVM().size());
		drawAllTrainEdgeTask(timeTableVM);
	}

	public void refreshMap(TimeTableVM timeTableVM) {
		List<Node> childList = new ArrayList<Node>();
		for (Node node : operateGroup.getChildren()) {
			if (!(node instanceof VBox)) {
				childList.add(node);
			}
		}
		for (Node node : childList) {
			operateGroup.getChildren().remove(node);
		}
		drawAllTrainEdgeTask(timeTableVM);
	}

	private void drawAllTrainEdgeTask(TimeTableVM timeTableVM) {
		// TODO Auto-generated method stub
		polylineStatue = new HashedMap<>();
		for (String key : timeTableVM.timeTable.allTrainPointTask.keySet()) {
			// 画车次任务
			Polyline polyline = new Polyline();
			List<Double> allPoints = new ArrayList<>();
			for (TrainState trainState : timeTableVM.timeTable.allTrainPointTask.get(key)) {
				int seconds = trainState.time.toSecondOfDay() - startTime * 3600;
				double x = leftOffset + seconds * ((double) hourInterval / 3600);
				int yIndex = stationsOrder.indexOf(trainState.stationName);
				// 1是补偿值
				double y = upHeight + yIndex * (upHeight + bottomHeight + 1);
				allPoints.add(x);
				allPoints.add(y);
			}
			polyline.getPoints().addAll(allPoints);

			polyline.setStroke(Color.LIGHTPINK);
			polyline.setStrokeWidth(3);
			operateGroup.getChildren().add(polyline);

			polyline.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
					// System.out.println("sfsaffdasdf");
					List<Rectangle> tmp = polylineStatue.get((Polyline) event.getSource());
					for (Rectangle rect : polylineStatue.get((Polyline) event.getSource())) {
						if (rect.isVisible()) {
							rect.setVisible(false);
						} else {
							rect.setVisible(true);
						}
					}
				}
			});

			// 画车站标记
			int rectSize = 6;
			List<Rectangle> rectList = new ArrayList<>();
			for (int i = 0; i < allPoints.size(); i += 2) {
				Rectangle r = new Rectangle();
				r.setX(allPoints.get(i) - rectSize / 2);
				r.setY(allPoints.get(i + 1) - rectSize / 2);
				r.setWidth(rectSize);
				r.setHeight(rectSize);
				r.setFill(new Color(0, 0, 1, 1.0));
				r.setVisible(false);
				rectList.add(r);
				operateGroup.getChildren().add(r);
			}
			polylineStatue.put(polyline, rectList);

			// 画时间
			for (int i = 0; i < timeTableVM.timeTable.allTrainPointTask.get(key).size(); i++) {
				Text minute = new Text();
				minute.setText(
						String.valueOf(timeTableVM.timeTable.allTrainPointTask.get(key).get(i).time.getMinute() % 10));
				minute.setX(allPoints.get(i * 2) - 5);
				minute.setY(allPoints.get(i * 2 + 1) - 7);
				operateGroup.getChildren().add(minute);
			}

			// 画股道
			for (int i = 0; i < timeTableVM.timeTable.allTrainPointTask.get(key).size(); i++) {
				Text minute = new Text();
				minute.setText(String.valueOf(timeTableVM.timeTable.allTrainPointTask.get(key).get(i).track));
				minute.setX(allPoints.get(i * 2) - 7);
				minute.setY(allPoints.get(i * 2 + 1) + 18);
				operateGroup.getChildren().add(minute);
			}
		}
	}

	// 测试：画19个小时的画布，这个值应该大于下面时间的差值
	private void setPaneSize(int count) {
		ap.setPrefWidth(leftOffset + hourInterval * count);
	}

	// 测试：画出从6点到24点的时间
	private void drawTime(int startTime, int endTime) {
		int topOffsetHour = 18;
		int topOffsetDecade = 23;
		int lenOfHourLine = 15;
		int lenOfDecadeLine = 7;

		for (int i = startTime; i <= endTime; i++) {
			double startX = leftOffset + (i - startTime) * hourInterval;
			double endY = topOffsetHour + lenOfHourLine;
			Line hour = new Line(startX, topOffsetHour, startX, endY);
			hour.setStroke(Color.RED);
			timeAp.getChildren().add(hour);

			Label hourL = new Label(String.valueOf(i));
			hourL.setTextFill(Color.RED);
			hourL.setPadding(new Insets(3, 0, 0, startX - 5));
			timeAp.getChildren().add(hourL);

			for (int j = 1; j <= 5; j++) {
				double start = startX + j * (hourInterval / 6);
				double end = topOffsetDecade + lenOfDecadeLine;
				Line decade = new Line(start, topOffsetDecade, start, end);
				decade.setStroke(Color.RED);
				timeAp.getChildren().add(decade);

				Label decadeL = new Label(String.valueOf(j));
				decadeL.setFont(new Font(8));
				decadeL.setTextFill(Color.RED);
				decadeL.setPadding(new Insets(10, 0, 0, start - 3));
				timeAp.getChildren().add(decadeL);
			}
		}

		double startX = leftOffset;
		double startY = topOffsetDecade + lenOfDecadeLine / 2;
		double endX = startX + hourInterval * (endTime - startTime + 1);
		Line h = new Line(startX, startY, endX, startY);
		h.setStroke(Color.RED);
		h.setStrokeWidth(0.5);
		timeAp.getChildren().add(h);
	}

	// 画出车站表头
	private void drawHeads(List<StationVM> allStationsVM) throws IOException {
		stationsOrder = new ArrayList<>();
		for (StationVM stationVM : allStationsVM) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FoldHead.fxml"));
			AnchorPane ap = (AnchorPane) loader.load();
			FoldHeadController controller = loader.getController();
			controller.setData(stationVM.getStationName(), upHeight, bottomHeight);
			headsVb.getChildren().add(ap);

			stationsOrder.add(stationVM.getStationName());
		}
	}

	private void drawOperateMap(int stationSize) throws IOException {
		for (int i = 0; i < stationSize; i++) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FoldBody.fxml"));
			AnchorPane ap = (AnchorPane) loader.load();
			FoldBodyController controller = loader.getController();
			controller.setData(upHeight, bottomHeight, leftOffset, hourInterval, hourInterval / 6, startTime, endTime);
			operateVb.getChildren().add(ap);
		}
	}

	// 同步作业大表滑动条
	private void syncSliding() {
		sp.hvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number o, Number n) {
				// TODO Auto-generated method stub
				timeSp.setHvalue(n.doubleValue());
				operateSp.setHvalue(n.doubleValue());
			}
		});
		operateSp.vvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number o, Number n) {
				// TODO Auto-generated method stub
				stationSp.setVvalue(n.doubleValue());
			}
		});
		stationSp.vvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number o, Number n) {
				// TODO Auto-generated method stub
				operateSp.setVvalue(n.doubleValue());
			}
		});
		operateSp.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (event.isMiddleButtonDown()) {
					double relativeX = event.getX() - oldX;
					double relativeY = event.getY() - oldY;

					double relativeHValue = relativeX / (operateVb.getWidth() - operateSp.getWidth());
					double relativeVValue = relativeY / (operateVb.getHeight() - operateSp.getHeight());
					sp.setHvalue(oldHValue + relativeHValue);
					operateSp.setVvalue(oldVValue + relativeVValue);
				}
			}
		});
		operateSp.setOnMousePressed((new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (event.isMiddleButtonDown()) {
					oldX = event.getX();
					oldY = event.getY();
					oldHValue = sp.getHvalue();
					oldVValue = operateSp.getVvalue();
				}
			}
		}));
	}

}
