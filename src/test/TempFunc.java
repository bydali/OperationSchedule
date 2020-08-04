package test;

import java.time.LocalTime;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import schedule.io.BigLittleConverter;

public class TempFunc extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Temp.fxml"));
			Scene scene = new Scene(root, 1680, 1050);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}