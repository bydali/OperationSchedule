package test;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

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
	private AnchorPane operatePane;

	private double oldX;
	private double oldY;
	private double oldHValue;
	private double oldVValue;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		syncSliding();
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

					double relativeHValue = relativeX / (operatePane.getWidth() - operateSp.getWidth());
					double relativeVValue = relativeY / (operatePane.getHeight() - operateSp.getHeight());
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
