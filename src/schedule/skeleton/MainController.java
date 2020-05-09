package schedule.skeleton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.Line;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import schedule.station.FoldBodyController;
import schedule.viewmodel.TimeTableVM;

public class MainController implements Initializable {
	@FXML
	private Tab operateTab;

	@FXML
	private TableView<String> operateLog;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setData(TimeTableVM timeTableVM) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OperateChart.fxml"));
		GridPane gp = (GridPane) loader.load();
		
		OperateChartController controller = loader.getController();
		controller.setData();
		operateTab.setContent(gp);
		
	}
}
