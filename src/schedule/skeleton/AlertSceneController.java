package schedule.skeleton;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class AlertSceneController implements Initializable {

	@FXML
	private Text content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setContent(String string) {
		content.setText(string);
	}

}
