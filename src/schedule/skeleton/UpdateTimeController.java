package schedule.skeleton;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedule.io.BigLittleConverter;
import schedule.io.ReadFromLocal;

public class UpdateTimeController implements Initializable {

	@FXML
	private Button updateBtn;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField armyNum;
	@FXML
	private TextField routeNum;
	@FXML
	private TextField oName;
	@FXML
	private TextField oTime;
	@FXML
	private TextField cName;
	@FXML
	private TextField inTime;
	@FXML
	private TextField outTime;
	@FXML
	private TextField nName;
	@FXML
	private TextField nTime;
	@FXML
	private TextField dName;
	@FXML
	private TextField dTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		updateBtn.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				try {
					String server0 = ReadFromLocal.getPath(3);
					String port0 = ReadFromLocal.getPath(4);
					String server1 = ReadFromLocal.getPath(5);
					String port1 = ReadFromLocal.getPath(6);
					String server2 = ReadFromLocal.getPath(7);
					String port2 = ReadFromLocal.getPath(8);

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								// System.out.println(ReportTimeCallCpp.myadd(1, 2));

								DatagramSocket datagramSocket = new DatagramSocket();

								// 头
								byte[] data0 = BigLittleConverter.toMinByte((char) 0xBE);
								byte[] data1 = BigLittleConverter.toMinByte((short) 0);
								byte[] data2 = BigLittleConverter.toMinByte((char) 1);
								byte[] data3 = BigLittleConverter.toMinByte((char) 3);
								byte[] data4 = BigLittleConverter.toMinByte((short) 1);
								byte[] data5 = BigLittleConverter.toMinByte((char) 81);
								byte[] data6 = BigLittleConverter.toMinByte((char) 3);
								byte[] data7 = BigLittleConverter.toMinByte((short) 1);
								byte[] data8 = BigLittleConverter.toMinByte((char) 93);
								byte[] data9 = BigLittleConverter.toMinByte((short) LocalDate.now().getYear());
								byte[] data10 = BigLittleConverter.toMinByte((char) LocalDate.now().getMonthValue());
								byte[] data11 = BigLittleConverter.toMinByte((char) LocalDate.now().getDayOfMonth());
								byte[] data12 = BigLittleConverter.toMinByte(
										(int) ((LocalTime.now().getHour() * 60 + LocalTime.now().getMinute()) * 60
												+ LocalTime.now().getSecond()) * 1000);
								byte[] data13 = BigLittleConverter.toMinByte((int) ReportTimeController.msgCount++);
								byte[] data14 = BigLittleConverter.toMinByte((char) 0);
								byte[] data15 = BigLittleConverter.toMinByte((char) 0);
								byte[] data16 = BigLittleConverter.toMinByte((char) 1);
								byte[] data17 = BigLittleConverter.toMinByte((short) 1);
								byte[] data18 = BigLittleConverter.toMinByte((int) 0);
								byte[] data19 = BigLittleConverter.toMinByte((char) 0);

								// 信息字2
								byte[] bh = BigLittleConverter.toMinByte((short) 4258);
								byte[] cllx = BigLittleConverter.toMinByte((char) 2);
								byte[] glcbh = BigLittleConverter.toMinByte((char) 1);
								byte[] by = BigLittleConverter.toMinByte((short) 0);

								// 包1 站点信息
								byte[] msg0 = BigLittleConverter.toMinByte((char) armyNum.getText().getBytes().length);
								byte[] msg1 = armyNum.getText().getBytes();
								byte[] msg2 = BigLittleConverter.toMinByte((char) routeNum.getText().getBytes().length);
								byte[] msg3 = routeNum.getText().getBytes();
								byte[] msg4 = BigLittleConverter.toMinByte((char) oName.getText().getBytes().length);
								byte[] msg5 = oName.getText().getBytes();

								String date0 = oTime.getText().split(" ")[0];
								String time0 = oTime.getText().split(" ")[1];
								byte[] msg6 = BigLittleConverter
										.toMinByte((short) Integer.parseInt(date0.split("-")[0]));
								byte[] msg7 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date0.split("-")[1]));
								byte[] msg8 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date0.split("-")[2]));
								int h0 = Integer.parseInt(time0.split(":")[0]);
								int m0 = Integer.parseInt(time0.split(":")[1]);
								int s0 = Integer.parseInt(time0.split(":")[2]);
								byte[] msg9 = BigLittleConverter.toMinByte(((h0 * 60 + m0) * 60 + s0) * 1000);

								byte[] msg10 = BigLittleConverter.toMinByte((char) cName.getText().getBytes().length);
								byte[] msg11 = cName.getText().getBytes();

								String date1 = inTime.getText().split(" ")[0];
								String time1 = inTime.getText().split(" ")[1];
								byte[] msg12 = BigLittleConverter
										.toMinByte((short) Integer.parseInt(date1.split("-")[0]));
								byte[] msg13 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date1.split("-")[1]));
								byte[] msg14 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date1.split("-")[2]));
								int h1 = Integer.parseInt(time1.split(":")[0]);
								int m1 = Integer.parseInt(time1.split(":")[1]);
								int s1 = Integer.parseInt(time1.split(":")[2]);
								byte[] msg15 = BigLittleConverter.toMinByte(((h1 * 60 + m1) * 60 + s1) * 1000);

								String date2 = outTime.getText().split(" ")[0];
								String time2 = outTime.getText().split(" ")[1];
								byte[] msg16 = BigLittleConverter
										.toMinByte((short) Integer.parseInt(date2.split("-")[0]));
								byte[] msg17 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date2.split("-")[1]));
								byte[] msg18 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date2.split("-")[2]));
								int h2 = Integer.parseInt(time2.split(":")[0]);
								int m2 = Integer.parseInt(time2.split(":")[1]);
								int s2 = Integer.parseInt(time2.split(":")[2]);
								byte[] msg19 = BigLittleConverter.toMinByte(((h2 * 60 + m2) * 60 + s2) * 1000);

								byte[] msg20 = BigLittleConverter.toMinByte((char) nName.getText().getBytes().length);
								byte[] msg21 = nName.getText().getBytes();

								String date3 = nTime.getText().split(" ")[0];
								String time3 = nTime.getText().split(" ")[1];
								byte[] msg22 = BigLittleConverter
										.toMinByte((short) Integer.parseInt(date3.split("-")[0]));
								byte[] msg23 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date3.split("-")[1]));
								byte[] msg24 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date3.split("-")[2]));
								int h3 = Integer.parseInt(time3.split(":")[0]);
								int m3 = Integer.parseInt(time3.split(":")[1]);
								int s3 = Integer.parseInt(time3.split(":")[2]);
								byte[] msg25 = BigLittleConverter.toMinByte(((h3 * 60 + m3) * 60 + s3) * 1000);

								byte[] msg26 = BigLittleConverter.toMinByte((char) dName.getText().getBytes().length);
								byte[] msg27 = dName.getText().getBytes();

								String date4 = dTime.getText().split(" ")[0];
								String time4 = dTime.getText().split(" ")[1];
								byte[] msg28 = BigLittleConverter
										.toMinByte((short) Integer.parseInt(date4.split("-")[0]));
								byte[] msg29 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date4.split("-")[1]));
								byte[] msg30 = BigLittleConverter
										.toMinByte((char) Integer.parseInt(date4.split("-")[2]));
								int h4 = Integer.parseInt(time4.split(":")[0]);
								int m4 = Integer.parseInt(time4.split(":")[1]);
								int s4 = Integer.parseInt(time4.split(":")[2]);
								byte[] msg31 = BigLittleConverter.toMinByte(((h4 * 60 + m4) * 60 + s4) * 1000);

								byte[] head = BigLittleConverter.concatBytes(data0, data1, data2, data3, data4, data5,
										data6, data7, data8, data9, data10, data11, data12, data13, data14, data15,
										data16, data17, data18, data19, bh, cllx, glcbh, by, msg0, msg1, msg2, msg3,
										msg4, msg5, msg6, msg7, msg8, msg9, msg10, msg11, msg12, msg13, msg14, msg15,
										msg16, msg17, msg18, msg19, msg20, msg21, msg22, msg23, msg24, msg25, msg26,
										msg27, msg28, msg29, msg30, msg31);

								// 修改长度
								byte[] totalLen = BigLittleConverter.toMinByte((short) (head.length));
								if (totalLen.length == 2) {
									head[1] = totalLen[0];
									head[2] = totalLen[1];
								}
								// 添加校验
								byte jyh = 0;
								for (int i = 0; i < head.length; i++) {
									if (i != 33) {
										jyh += head[i];
									}
								}
								head[33] = jyh;

								InetAddress address0 = InetAddress.getByName(server0);
								DatagramPacket datagramPacket0 = new DatagramPacket(head, head.length, address0,
										Integer.parseInt(port0));

								InetAddress address1 = InetAddress.getByName(server1);
								DatagramPacket datagramPacket1 = new DatagramPacket(head, head.length, address1,
										Integer.parseInt(port1));

								InetAddress address2 = InetAddress.getByName(server2);
								DatagramPacket datagramPacket2 = new DatagramPacket(head, head.length, address2,
										Integer.parseInt(port2));

								datagramSocket.send(datagramPacket0);
								datagramSocket.send(datagramPacket1);
								datagramSocket.send(datagramPacket2);

								datagramSocket.close();

								Platform.runLater(() -> {
									Stage stage = (Stage) cancelBtn.getScene().getWindow();
									stage.close();
								});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		cancelBtn.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				Stage stage = (Stage) cancelBtn.getScene().getWindow();
				stage.close();
			}
		});
	}

}
