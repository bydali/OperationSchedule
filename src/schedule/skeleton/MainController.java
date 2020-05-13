package schedule.skeleton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.sound.sampled.Line;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import schedule.model.TrainState;
import schedule.station.FoldBodyController;
import schedule.viewmodel.StationVM;
import schedule.viewmodel.TimeTableVM;
import schedule.viewmodel.TrainStateVM;

public class MainController implements Initializable {
	@FXML
	private Tab operateTab;

	@FXML
	private TableView<String> operateLog;

	@FXML
	private TableView<List<SimpleStringProperty>> trainStateView;

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

		TableColumn<List<SimpleStringProperty>, String> col0 = new TableColumn<>("车次");
		col0.setMinWidth(100);
		col0.setCellValueFactory(data -> data.getValue().get(0));
		col0.setCellFactory(TextFieldTableCell.<List<SimpleStringProperty>>forTableColumn());
		col0.setOnEditCommit((CellEditEvent<List<SimpleStringProperty>, String> t) -> {
			List<SimpleStringProperty> rowData = ((List<SimpleStringProperty>) t.getTableView().getItems()
					.get(t.getTablePosition().getRow()));
			String oldTrainNum = rowData.get(0).getValue();
			rowData.set(0, new SimpleStringProperty(t.getNewValue()));
		});

		trainStateView.getColumns().add(col0);

		for (int i = 0; i < timeTableVM.getAllStationVM().size(); i++) {
			TableColumn<List<SimpleStringProperty>, String> col = new TableColumn<>(
					timeTableVM.getAllStationVM().get(i).getStationName());
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
				rowData.set(col_in_idx, new SimpleStringProperty(newInTime));

				refresh(oldTrainNum, oldInTime, newInTime);
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
				rowData.set(col_out_idx, new SimpleStringProperty(newOutTime));

				refresh(oldTrainNum, oldOutTime, newOutTime);
			});

			col.getColumns().add(col_in);
			col.getColumns().add(col_out);

			trainStateView.getColumns().add(col);
		}

		trainStateView.setItems(timeTableVM.fullTaskVMList);

	}

	private void refresh(String oldTrainNum, String oldTime, String newTime) {
		timeTableVM.updateTrainState(oldTrainNum, oldTime, newTime);
		timeTableVM.timeTable.generateAllIOTask();
		mapController.refreshMap(timeTableVM);
	}
}
