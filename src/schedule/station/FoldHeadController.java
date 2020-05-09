package schedule.station;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FoldHeadController implements Initializable {

	@FXML
	private AnchorPane ap;
	@FXML
	private Label stationName;
	@FXML
	private Circle c0;
	@FXML
	private Circle c1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setData(String stationName, double upHeight, double bottomHeight) {
		String replace = "";
		for (int i = 0; i < stationName.length(); i++) {
			replace += stationName.charAt(i) + " ";
		}
		this.stationName.setText(replace);

		ap.setPrefHeight(upHeight + bottomHeight);

		c0.setCenterY(upHeight);
		c1.setCenterY(upHeight);
		this.stationName.setLayoutY(upHeight - 8);

		c0.setCenterX(15);
		c1.setCenterX(32);
		this.stationName.setLayoutX(65);
	}

}
