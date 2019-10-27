package test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.text.Position;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import schedule.station.FoldHeadController;

/**
 * 测试运行图左上单元格长宽为，h=40，w=200
 * 
 * @author Administrator
 *
 */
public class TestPaneController implements Initializable {

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
	private AnchorPane timeAp;
	@FXML
	private VBox headsVb;

	private double oldX;
	private double oldY;
	private double oldHValue;
	private double oldVValue;

	private final int leftOffset = 20;
	private final int hourInterval = 240;
	private final int decadeInterval = hourInterval / 6;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			syncSliding();
			setPaneSize();
			drawTime();
			drawHeads();
			drawOperateMap();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}	
	}

	private void setPaneSize() {
		timeAp.setPrefWidth(hourInterval * 19);
		operateVb.setPrefWidth(hourInterval * 19);
		ap.setPrefWidth(hourInterval * 19);
	}

	private void drawTime() {
		int topOffsetHour = 18;
		int topOffsetDecade = 23;
		int lenOfHourLine = 15;
		int lenOfDecadeLine = 7;

		for (int i = 6; i <= 24; i++) {
			double startX = leftOffset + (i - 6) * hourInterval;
			double endY = topOffsetHour + lenOfHourLine;
			Line hour = new Line(startX, topOffsetHour, startX, endY);
			hour.setStroke(Color.RED);
			timeAp.getChildren().add(hour);

			Label hourL = new Label(String.valueOf(i));
			hourL.setTextFill(Color.RED);
			hourL.setPadding(new Insets(3, 0, 0, startX - 5));
			timeAp.getChildren().add(hourL);

			for (int j = 1; j <= 5; j++) {
				double start = startX + j * decadeInterval;
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
		double endX = startX + hourInterval * (24 - 6 + 1);
		Line h = new Line(startX, startY, endX, startY);
		h.setStroke(Color.RED);
		h.setStrokeWidth(0.5);
		timeAp.getChildren().add(h);
	}

	private void drawHeads() throws IOException {
		List<String> stations = new ArrayList<String>();
		stations.add("赤壁北");
		stations.add("岳阳东");
		stations.add("汨罗东");
		stations.add("长沙南");
		stations.add("株洲北线路所");
		stations.add("株洲南线路所");
		stations.add("株洲西");
		stations.add("衡山西");
		stations.add("衡阳东");

		for (String station : stations) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../schedule/station/FoldHead.fxml"));
			AnchorPane ap = (AnchorPane)loader.load();
			FoldHeadController controller = loader.getController();
			controller.setData(station);
			headsVb.getChildren().add(ap);
		}
	}

	private void drawOperateMap() {

	}

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
