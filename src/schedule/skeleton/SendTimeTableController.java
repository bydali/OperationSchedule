package schedule.skeleton;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import schedule.io.BigLittleConverter;
import schedule.io.ReadFromLocal;
import schedule.io.Write2Port;
import schedule.model.TimeTable;
import schedule.viewmodel.TimeTableVM;

public class SendTimeTableController implements Initializable {

	@FXML
	private ComboBox<String> cb;

	@FXML
	private Button btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		cb.setCellFactory((ListView<String> l) -> new SimpleCell1());
		int i = 6;
		while (true) {
			try {
				String device = ReadFromLocal.getPath(i);
				String port = ReadFromLocal.getPath(i + 1);
				i += 2;
				if (device == null || port == null) {
					break;
				}
				cb.getItems().add(device + '：' + port);
			} catch (Exception e) {
				// TODO: handle exception
				break;
			}
		}
		cb.getSelectionModel().select(0);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				String server = cb.getSelectionModel().getSelectedItem().split("：")[0];
				String port = cb.getSelectionModel().getSelectedItem().split("：")[1];

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("选择时刻表计划");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
				File file = fileChooser.showOpenDialog(btn.getScene().getWindow());

				if (file != null) {
					try {
						Thread initialData = new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								// 生成时刻表VM
								try {
									TimeTable timeTable = ReadFromLocal.readTT(file.getAbsolutePath());
									Write2Port.sendTimeTable(server, port, timeTable);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						initialData.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
}

class SimpleCell1 extends ListCell<String> {
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null) {
			setGraphic(null);
		} else {
			setGraphic(new Label(item.split("：")[0]));
		}
	}
}
