package schedule.skeleton;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.Line;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

public class MainController implements Initializable {
	@FXML
	private AnchorPane operateTab;

	@FXML
	private TableView<String> operateLog;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		System.out.println(arg0);
		System.out.println("-------");
		System.out.println(arg1);

		javafx.scene.control.Label l = new javafx.scene.control.Label();
		l.setText("jhhhafsdsaf");

	}
}
