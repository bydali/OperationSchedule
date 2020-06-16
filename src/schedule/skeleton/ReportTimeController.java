package schedule.skeleton;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import schedule.model.TimeTable;
import schedule.viewmodel.StationVM;
import schedule.viewmodel.TimeTableVM;

public class ReportTimeController implements Initializable {

	@FXML
	private ComboBox<String> inOrOut;

	@FXML
	private Button cancelReport;

	@FXML
	private Button reportTimeBtn;

	@FXML
	private Label trainNum;

	@FXML
	private ComboBox<String> stationName;

	@FXML
	private TextField reportTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		inOrOut.getItems().add("到达");
		inOrOut.getItems().add("出发");
		
		for (StationVM stationVm : TimeTableVM.allStationVM) {
			stationName.getItems().add(stationVm.getStationName());
		}
	}

	public void setData(List<SimpleStringProperty> list) {
		// TODO Auto-generated method stub

	}

}
