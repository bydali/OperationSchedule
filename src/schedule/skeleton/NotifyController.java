package schedule.skeleton;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NotifyController implements Initializable {

	@FXML
	private TextArea msg;

	@FXML
	private ListView<String> lst;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.msg.setWrapText(true);
		this.msg.setStyle("-fx-font-size:24");
		msg.textProperty().bind(lst.getSelectionModel().selectedItemProperty());
		lst.setCellFactory((ListView<String> l) -> new SimpleCell());
	}

	public void notifyMsg(String msg) {
		lst.getItems().add(0, msg);
		lst.getSelectionModel().select(0);
	}
}

class SimpleCell extends ListCell<String> {
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null) {
			setGraphic(null);
		} else {
			setGraphic(new Label(item.split("\n")[0]));
		}
	}
}
