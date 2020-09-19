package schedule.skeleton;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.sound.sampled.Line;

import org.apache.commons.math3.random.ISAACRandom;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import schedule.io.BigLittleConverter;
import schedule.io.ReadFromLocal;
import schedule.io.Write2Local;
import schedule.io.Write2Port;
import schedule.model.TimeTable;
import schedule.model.TrainState;
import schedule.station.FoldBodyController;
import schedule.viewmodel.StationVM;
import schedule.viewmodel.TimeTableVM;
import schedule.viewmodel.TrainStateVM;

public class MainController implements Initializable {
	@FXML
	private SplitPane sP;

	@FXML
	private Tab operateTab;

	@FXML
	private TableView<String> operateLog;

	@FXML
	private TableView<List<SimpleStringProperty>> trainStateView;

	@FXML
	private MenuItem reportTime;

	@FXML
	private MenuItem trainStateUpdate;

	@FXML
	private MenuItem openTrainStates;

	@FXML
	private MenuItem syncTimeTable;

	@FXML
	private MenuItem sendTimeTable;

	@FXML
	private MenuItem saveTimeTable;

	private TimeTableVM timeTableVM;
	private OperateChartController mapController;
	private FXMLLoader operateChartLoader;
	private FXMLLoader operateCmdLoader;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// 加载作业大表控件
		operateChartLoader = new FXMLLoader(getClass().getResource("OperateChart.fxml"));
		GridPane gp;
		try {
			gp = (GridPane) operateChartLoader.load();
			operateTab.setContent(gp);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		openTrainStates.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("打开时刻表");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
				File file = fileChooser.showOpenDialog(sP.getScene().getWindow());
				if (file != null) {
					try {
						Thread initialData = new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								// 生成时刻表VM
								try {
									timeTableVM = new TimeTableVM(file.getAbsolutePath());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						initialData.start();
						try {
							initialData.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setData(timeTableVM);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		reportTime.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// 打开报点的stage
				Stage reportTime = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportTime.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
					Scene reportScene = new Scene(root, 530, 200);
					reportScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					reportTime.setScene(reportScene);
					reportTime.setResizable(false);
					reportTime.setTitle("列车报点");
					reportTime.getIcons().add(new Image(getClass().getResourceAsStream("app_icon.PNG")));
					// reportTime.initOwner(stage);
					// reportTime.initModality(Modality.WINDOW_MODAL);
					ReportTimeController reportTimeController = loader.getController();
					reportTimeController.setData(trainStateView.getSelectionModel().getSelectedItem());
					reportTime.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		trainStateUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage updateTime = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateTime.fxml"));
				BorderPane root;
				try {
					root = (BorderPane) loader.load();
					Scene updateScene = new Scene(root);
					updateScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					updateTime.setScene(updateScene);
					updateTime.setResizable(false);
					updateTime.setTitle("列车运行状态更新");
					updateTime.getIcons().add(new Image(getClass().getResourceAsStream("app_icon.PNG")));
					UpdateTimeController updateTimeController = loader.getController();
					updateTime.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		syncTimeTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				ArrayList<String> targets = new ArrayList<>();
				int i = 6;
				while (true) {
					try {
						String device = ReadFromLocal.getPath(i);
						String port = ReadFromLocal.getPath(i + 1);
						i += 2;
						if (device == null || port == null) {
							break;
						}
						targets.add(device + '：' + port);
					} catch (Exception e) {
						// TODO: handle exception
						break;
					}
				}

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (String string : targets) {
							Write2Port.sendTimeTable(string.split("：")[0], string.split("：")[1], 1);
						}
					}
				}).start();
			}
		});
		sendTimeTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage sendTable = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SendTimeTable.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
					Scene sendScene = new Scene(root);
					sendScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
					sendTable.setScene(sendScene);
					sendTable.setResizable(false);
					sendTable.setTitle("发送时刻表");
					sendTable.getIcons().add(new Image(getClass().getResourceAsStream("app_icon.PNG")));
					sendTable.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		saveTimeTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("保存时刻表");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
				File file = fileChooser.showSaveDialog(sP.getScene().getWindow());
				if (file != null) {
					try {
						if (file.exists()) {
							file.delete();
						}
						Write2Local.save2Disk(file.getAbsolutePath(), timeTableVM.timeTable);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setData(TimeTableVM timeTableVM) throws IOException {
		this.timeTableVM = timeTableVM;

		OperateChartController controller = operateChartLoader.getController();
		mapController = controller;
		// 画作业大表基本底图
		controller.setData(timeTableVM);

		// MenuItem mI = new MenuItem("实时报点");
		// mI.setOnAction(new EventHandler<ActionEvent>() {
		// public void handle(ActionEvent event) {
		// List<SimpleStringProperty> selectItemList =
		// trainStateView.getSelectionModel().getSelectedItem();
		//
		// // 打开报点的stage
		// Stage reportTime = new Stage();
		// FXMLLoader loader = new
		// FXMLLoader(getClass().getResource("ReportTime.fxml"));
		// AnchorPane root;
		// try {
		// root = (AnchorPane) loader.load();
		// Scene reportScene = new Scene(root, 500, 200);
		// reportScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// reportTime.setScene(reportScene);
		// reportTime.setResizable(false);
		// reportTime.setTitle("列车报点");
		// reportTime.getIcons().add(new
		// Image(getClass().getResourceAsStream("app_icon.PNG")));
		// // reportTime.initOwner(stage);
		// // reportTime.initModality(Modality.WINDOW_MODAL);
		// ReportTimeController reportTimeController = loader.getController();
		// reportTimeController.setData(trainStateView.getSelectionModel().getSelectedItem());
		// reportTime.show();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
		//
		// ContextMenu cM = new ContextMenu();
		// cM.getItems().add(mI);
		// trainStateView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>()
		// {
		// @Override
		// public void handle(ContextMenuEvent event) {
		// cM.show(trainStateView, event.getScreenX(), event.getScreenY());
		// }
		// });

		trainStateView.getColumns().clear();

		TableColumn<List<SimpleStringProperty>, String> col0 = new TableColumn<>("车次");
		col0.setMinWidth(100);
		col0.setCellValueFactory(data -> data.getValue().get(0));
		col0.setCellFactory(TextFieldTableCell.<List<SimpleStringProperty>>forTableColumn());
		col0.setOnEditCommit((CellEditEvent<List<SimpleStringProperty>, String> t) -> {
			List<SimpleStringProperty> rowData = ((List<SimpleStringProperty>) t.getTableView().getItems()
					.get(t.getTablePosition().getRow()));
			String oldTrainNum = rowData.get(0).getValue();
			// rowData.set(0, new SimpleStringProperty(t.getNewValue()));
		});

		trainStateView.getColumns().add(col0);

		for (int i = 0; i < timeTableVM.allStationVM.size(); i++) {
			TableColumn<List<SimpleStringProperty>, String> col = new TableColumn<>(
					timeTableVM.allStationVM.get(i).getStationName());
			col.setMinWidth(200);

			int col_in_idx = 1 + i * 2;
			int col_out_idx = 1 + i * 2 + 1;

			TableColumn<List<SimpleStringProperty>, String> col_in = new TableColumn<>("接车");
			col_in.setMinWidth(100);
			col_in.setCellValueFactory(data -> data.getValue().get(col_in_idx));
			col_in.setCellFactory(TextFieldTableCell.<List<SimpleStringProperty>>forTableColumn());
			col_in.setOnEditCommit((CellEditEvent<List<SimpleStringProperty>, String> t) -> {
				List<SimpleStringProperty> rowData = ((List<SimpleStringProperty>) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				String oldTrainNum = rowData.get(0).getValue();
				String oldInTime = rowData.get(col_in_idx).getValue();
				String newInTime = t.getNewValue();
				// rowData.set(col_in_idx, new SimpleStringProperty(newInTime));

				refresh(oldTrainNum, oldInTime, newInTime, "接车");
			});

			TableColumn<List<SimpleStringProperty>, String> col_out = new TableColumn<>("发车");
			col_out.setMinWidth(100);
			col_out.setCellValueFactory(data -> data.getValue().get(col_out_idx));
			col_out.setCellFactory(TextFieldTableCell.<List<SimpleStringProperty>>forTableColumn());
			col_out.setOnEditCommit((CellEditEvent<List<SimpleStringProperty>, String> t) -> {
				List<SimpleStringProperty> rowData = ((List<SimpleStringProperty>) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				String oldTrainNum = rowData.get(0).getValue();
				String oldOutTime = rowData.get(col_out_idx).getValue();
				String newOutTime = t.getNewValue();
				// rowData.set(col_out_idx, new SimpleStringProperty(newOutTime));

				refresh(oldTrainNum, oldOutTime, newOutTime, "发车");
			});

			col.getColumns().add(col_in);
			col.getColumns().add(col_out);

			trainStateView.getColumns().add(col);
		}

		trainStateView.setItems(timeTableVM.fullTaskVMList);
	}

	public void refresh(String oldTrainNum, String oldTime, String newTime, String inOrOut) {
		if (!oldTime.equals(newTime)) {
			for (List<SimpleStringProperty> props : timeTableVM.fullTaskVMList) {
				// 找到该车次
				if (props.get(0).getValue().equals(oldTrainNum)) {
					boolean find = false;
					int gapTime = 0;
					for (SimpleStringProperty ssp : props) {
						if (ssp.getValue().equals(oldTrainNum)) {
							continue;
						}
						// 找到修改的时间点
						else if ((!find) && ssp.getValue().equals(oldTime)) {
							switch (inOrOut) {
							case "接车":
								if (props.indexOf(ssp) % 2 == 1) {
									ssp.set(newTime);

									LocalTime old = LocalTime.of(Integer.valueOf(oldTime.split(":")[0]),
											Integer.valueOf(oldTime.split(":")[1]),
											Integer.valueOf(oldTime.split(":")[2]));
									LocalTime new_ = LocalTime.of(Integer.valueOf(newTime.split(":")[0]),
											Integer.valueOf(newTime.split(":")[1]),
											Integer.valueOf(newTime.split(":")[2]));

									gapTime = new_.getHour() * 3600 + new_.getMinute() * 60 + new_.getSecond()
											- old.getHour() * 3600 - old.getMinute() * 60 - old.getSecond();
									find = true;
								}
								break;
							case "发车":
								if (props.indexOf(ssp) % 2 == 0) {
									ssp.set(newTime);

									LocalTime old = LocalTime.of(Integer.valueOf(oldTime.split(":")[0]),
											Integer.valueOf(oldTime.split(":")[1]),
											Integer.valueOf(oldTime.split(":")[2]));
									LocalTime new_ = LocalTime.of(Integer.valueOf(newTime.split(":")[0]),
											Integer.valueOf(newTime.split(":")[1]),
											Integer.valueOf(newTime.split(":")[2]));

									gapTime = new_.getHour() * 3600 + new_.getMinute() * 60 + new_.getSecond()
											- old.getHour() * 3600 - old.getMinute() * 60 - old.getSecond();
									find = true;
								}
								break;
							default:
								break;
							}
						}
						// 在修改的时间点之后依次后移时间
						else if (!ssp.getValue().equals("")) {
							LocalTime old = LocalTime.of(Integer.valueOf(ssp.getValue().split(":")[0]),
									Integer.valueOf(ssp.getValue().split(":")[1]),
									Integer.valueOf(ssp.getValue().split(":")[2]));
							LocalTime new_ = old.plusSeconds(gapTime);
							ssp.set(String.format("%tT", new_));
						}
					}
					break;
				}
			}
			// 刷新列车表格
			trainStateView.setItems(timeTableVM.fullTaskVMList);

			// 刷新时刻表
			timeTableVM.updateTrainState(oldTrainNum, oldTime, newTime, inOrOut);
			// 刷新作业大表
			mapController.refreshMap(timeTableVM);

		}
	}

	public void refreshMap(TimeTableVM timeTableVM) {
		this.timeTableVM = timeTableVM;
		trainStateView.setItems(timeTableVM.fullTaskVMList);
		mapController.refreshMap(timeTableVM);
	}

	public void notifyMsg(String msg) {
		if (operateCmdLoader != null) {
			((NotifyController) operateCmdLoader.getController()).notifyMsg(msg);
			Stage stage = (Stage) ((BorderPane) operateCmdLoader.getRoot()).getScene().getWindow();
			stage.show();
		} else {
			Stage updateTimeTable = new Stage();
			operateCmdLoader = new FXMLLoader(getClass().getResource("Notify.fxml"));
			BorderPane root;
			try {
				root = (BorderPane) operateCmdLoader.load();
				Scene newMsgScene = new Scene(root, 1200, 700);
				newMsgScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				updateTimeTable.setScene(newMsgScene);
				// updateTimeTable.setResizable(false);
				updateTimeTable.setTitle("收到调度命令！");
				updateTimeTable.getIcons().add(new Image(getClass().getResourceAsStream("app_icon.PNG")));
				NotifyController notifyController = operateCmdLoader.getController();
				notifyController.notifyMsg(msg);
				updateTimeTable.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
