package schedule.skeleton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FakeWinController implements Initializable{

	@FXML
	Button btn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage stage = (Stage) btn.getScene().getWindow();
				stage.close();
				
				Stage alert = new Stage();
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("AlertScene.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
					Scene scene = new Scene(root);
					scene.getStylesheets().add(
							getClass().getResource("application.css").toExternalForm());
					alert.setScene(scene);
					alert.setResizable(false);
					alert.getIcons().add(
							new Image(getClass().getResourceAsStream("app_icon.PNG")));
					AlertSceneController controller = loader.getController();
					controller.setContent("已接受列车运行任务！");
					alert.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
