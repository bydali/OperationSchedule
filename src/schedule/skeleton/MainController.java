package schedule.skeleton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.sampled.Line;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import schedule.model.TrainState;
import schedule.station.FoldBodyController;
import schedule.viewmodel.TimeTableVM;
import schedule.viewmodel.TrainStateVM;

public class MainController implements Initializable {
	@FXML
	private Tab operateTab;

	@FXML
	private TableView<String> operateLog;

	@FXML
	private TableView<TrainStateVM> trainStateView;

	private TimeTableVM timeTableVM;
	private OperateChartController mapController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setData(TimeTableVM timeTableVM) throws IOException {
		this.timeTableVM = timeTableVM;

		// 加载作业大表控件
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OperateChart.fxml"));
		GridPane gp = (GridPane) loader.load();
		OperateChartController controller = loader.getController();
		mapController = controller;
		controller.setData(timeTableVM);
		operateTab.setContent(gp);

		TableColumn<TrainStateVM, String> col1 = new TableColumn<>("编号");
		TableColumn<TrainStateVM, String> col2 = new TableColumn<>("车次");
		TableColumn<TrainStateVM, String> col3 = new TableColumn<>("时间");
		TableColumn<TrainStateVM, String> col4 = new TableColumn<>("车站");
		TableColumn<TrainStateVM, String> col5 = new TableColumn<>("相关股道");
		TableColumn<TrainStateVM, String> col6 = new TableColumn<>("动作类别");

		col1.setMinWidth(200);
		col2.setMinWidth(200);
		col3.setMinWidth(200);
		col4.setMinWidth(200);
		col5.setMinWidth(200);
		col6.setMinWidth(200);

		ArrayList<TableColumn<TrainStateVM, String>> list = new ArrayList<>();
		list.add(col1);
		list.add(col2);
		list.add(col3);
		list.add(col4);
		list.add(col5);
		list.add(col6);

		col1.setCellValueFactory(new PropertyValueFactory<>("id_v"));
		col2.setCellValueFactory(new PropertyValueFactory<>("trainNum_v"));
		col3.setCellValueFactory(new PropertyValueFactory<>("time_v"));
		col4.setCellValueFactory(new PropertyValueFactory<>("station_v"));
		col5.setCellValueFactory(new PropertyValueFactory<>("track_v"));
		col6.setCellValueFactory(new PropertyValueFactory<>("type_v"));

		col1.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col1.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setId_v(t.getNewValue());
		});
		col2.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col2.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setTrainNum_v(t.getNewValue());
			refresh();
		});
		col3.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col3.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTime_v(t.getNewValue());
			refresh();
		});
		col4.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col4.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setStation_v(t.getNewValue());
			refresh();
		});
		col5.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col5.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTrack_v(t.getNewValue());
			refresh();
		});
		col6.setCellFactory(TextFieldTableCell.<TrainStateVM>forTableColumn());
		col6.setOnEditCommit((CellEditEvent<TrainStateVM, String> t) -> {
			((TrainStateVM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setType_v(t.getNewValue());
		});

		trainStateView.getColumns().addAll(list);
		trainStateView.setItems(timeTableVM.trainStateVMList);
	}

	private void refresh() {
		timeTableVM.timeTable.generateAllEdgeTask();
		mapController.refreshMap(timeTableVM);
	}
}
