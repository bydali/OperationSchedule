package schedule.skeleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import c4j.ReportTimeCallCpp;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedule.io.ReadFromLocal;
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

	private String oldTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		inOrOut.getItems().add("接车");
		inOrOut.getItems().add("发车");

		for (StationVM stationVm : TimeTableVM.allStationVM) {
			stationName.getItems().add(stationVm.getStationName());
		}

		cancelReport.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				Stage stage = (Stage) cancelReport.getScene().getWindow();
				stage.close();
			}
		});
		reportTimeBtn.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				try {
					String server = ReadFromLocal.getPath(2);
					String port = ReadFromLocal.getPath(3);

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								System.out.println(ReportTimeCallCpp.myadd(1, 2));
								Platform.runLater(() -> {
									((MainController)(Main.loader.getController())).refresh(trainNum.getText().split(" ")[0], oldTime, reportTime.getText(), inOrOut.getValue());
							    });			
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		stationName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				if (inOrOut.getValue() != null) {
					String time = TimeTableVM.searchTrainState(trainNum.getText().split(" ")[0], stationName.getValue(),
							inOrOut.getValue());
					reportTime.setText(time);
					oldTime=time;
				}
			}
		});

		inOrOut.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				if (stationName .getValue() != null) {
					String time = TimeTableVM.searchTrainState(trainNum.getText().split(" ")[0], stationName.getValue(),
							inOrOut.getValue());
					reportTime.setText(time);
					oldTime=time;
				}
			}
		});
	}

	public void setData(List<SimpleStringProperty> list) {
		// TODO Auto-generated method stub
		trainNum.setText(list.get(0).getValue() + " 次列车于");

		stationName.setValue(stationName.getItems().get(0));
		inOrOut.setValue(inOrOut.getItems().get(1));
	}
}
