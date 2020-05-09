package schedule.skeleton;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import schedule.station.FoldHeadController;
import schedule.viewmodel.TimeTableVM;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			BorderPane root = (BorderPane) loader.load();

			setData(loader.getController());

			Scene scene = new Scene(root, 1680, 1050);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化时刻大表
	private void setData(MainController controller) throws IOException {
		// 生成时刻表viewmodel
		TimeTableVM timeTableVM = new TimeTableVM();
		// 主界面控制器设置viewmodel到主界面控件
		controller.setData(timeTableVM);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
