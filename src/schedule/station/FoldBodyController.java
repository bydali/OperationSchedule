package schedule.station;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class FoldBodyController implements Initializable {

	@FXML
	private AnchorPane ap;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setData(double upHeight, double bottomHeight, double leftOffset, double hourInterval,
			double decadeInterval, int startTime, int endTime) {
		for (int i = startTime; i <= endTime; i++) {
			double startX = leftOffset + (i - startTime) * hourInterval;
			Line hour = new Line(startX, 0, startX, upHeight + bottomHeight);
			hour.setStroke(Color.FORESTGREEN);
			ap.getChildren().add(hour);

			for (int j = 1; j <= 5; j++) {
				double start = startX + j * decadeInterval;
				Line decade = new Line(start, 0, start, upHeight + bottomHeight);
				decade.setStroke(Color.FORESTGREEN);
				ap.getChildren().add(decade);
			}
		}

		double startX = leftOffset;
		double startY = upHeight;
		double endX = startX + hourInterval * (endTime - startTime + 1);
		Line h = new Line(startX, startY, endX, startY);
		h.setStroke(Color.FORESTGREEN);
		h.setStrokeWidth(0.5);
		ap.getChildren().add(h);
	}

}
