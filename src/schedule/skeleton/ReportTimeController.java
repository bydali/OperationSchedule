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
					String server = ReadFromLocal.getPath(3);
					String port = ReadFromLocal.getPath(4);

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								// System.out.println(ReportTimeCallCpp.myadd(1, 2));
								
								DatagramSocket datagramSocket = new DatagramSocket();

								// 以下为数据接口
								byte[] data0 = toMinByte((char) 0xBE);
								byte[] data1 = toMinByte((short) 0);
								byte[] data2 = toMinByte((char) 1);
								byte[] data3 = toMinByte((char) 3);
								byte[] data4 = toMinByte((short) 1);
								byte[] data5 = toMinByte((char) 81);
								byte[] data6 = toMinByte((char) 3);
								byte[] data7 = toMinByte((short) 1);
								byte[] data8 = toMinByte((char) 93);
								byte[] data9 = toMinByte((short) 2020);
								byte[] data10 = toMinByte((char) 6);
								byte[] data11 = toMinByte((char) 28);
								byte[] data12 = toMinByte((int) 0);
								byte[] data13 = toMinByte((int) 0);
								byte[] data14 = toMinByte((char) 0);
								byte[] data15 = toMinByte((char) 0);
								byte[] data16 = toMinByte((char) 1);
								byte[] data17 = toMinByte((short) 1);
								byte[] data18 = toMinByte((int) 0);
								byte[] data19 = toMinByte((char) 0);
								byte[] head = new byte[data0.length + data1.length + data2.length + data3.length
										+ data4.length + data5.length + data6.length + data7.length + data8.length
										+ data9.length + data10.length + data11.length + data12.length + data13.length
										+ data14.length + data15.length + data16.length + data17.length + data18.length
										+ data19.length];
								System.arraycopy(data0, 0, head, 0, data0.length);
								System.arraycopy(data1, 0, head, data0.length, data1.length);
								System.arraycopy(data2, 0, head, data1.length, data2.length);
								System.arraycopy(data3, 0, head, data2.length, data3.length);
								System.arraycopy(data4, 0, head, data3.length, data4.length);
								System.arraycopy(data5, 0, head, data4.length, data5.length);
								System.arraycopy(data6, 0, head, data5.length, data6.length);
								System.arraycopy(data7, 0, head, data6.length, data7.length);
								System.arraycopy(data8, 0, head, data7.length, data8.length);
								System.arraycopy(data9, 0, head, data8.length, data9.length);
								System.arraycopy(data10, 0, head, data9.length, data10.length);
								System.arraycopy(data11, 0, head, data10.length, data11.length);
								System.arraycopy(data12, 0, head, data11.length, data12.length);
								System.arraycopy(data13, 0, head, data12.length, data13.length);
								System.arraycopy(data14, 0, head, data13.length, data14.length);
								System.arraycopy(data15, 0, head, data14.length, data15.length);
								System.arraycopy(data16, 0, head, data15.length, data16.length);
								System.arraycopy(data17, 0, head, data16.length, data17.length);
								System.arraycopy(data18, 0, head, data17.length, data18.length);
								System.arraycopy(data19, 0, head, data18.length, data19.length);

								InetAddress address = InetAddress.getByName(server);
								DatagramPacket datagramPacket = new DatagramPacket(head, head.length, address,
										Integer.parseInt(port));
								datagramSocket.send(datagramPacket);

//								byte[] receBuf = new byte[1024];
//								DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
//								datagramSocket.receive(recePacket);
//
//								String receStr = new String(recePacket.getData(), 0, recePacket.getLength());

								datagramSocket.close();
								
								Platform.runLater(() -> {
									((MainController) (Main.loader.getController())).refresh(
											trainNum.getText().split(" ")[0], oldTime, reportTime.getText(),
											inOrOut.getValue());
									oldTime = reportTime.getText();
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
					oldTime = time;
				}
			}
		});

		inOrOut.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				if (stationName.getValue() != null) {
					String time = TimeTableVM.searchTrainState(trainNum.getText().split(" ")[0], stationName.getValue(),
							inOrOut.getValue());
					reportTime.setText(time);
					oldTime = time;
				}
			}
		});
	}

	private byte[] toMinByte(char c) {
		byte[] result = new byte[1];
		// 由高位到低位
		result[0] = (byte) (c & 0xFF);
		return result;
	}

	private byte[] toMinByte(short c) {
		byte[] result = new byte[2];
		// 由高位到低位
		result[1] = (byte) ((c >> 8) & 0xFF);
		result[0] = (byte) (c & 0xFF);
		return result;
	}

	private byte[] toMinByte(int c) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[3] = (byte) ((c >> 24) & 0xFF);
		result[2] = (byte) ((c >> 16) & 0xFF);
		result[1] = (byte) ((c >> 8) & 0xFF);
		result[0] = (byte) (c & 0xFF);
		return result;
	}

	public void setData(List<SimpleStringProperty> list) {
		// TODO Auto-generated method stub
		trainNum.setText(list.get(0).getValue() + " 次列车于");

		stationName.setValue(stationName.getItems().get(0));
		inOrOut.setValue(inOrOut.getItems().get(1));
	}
}
