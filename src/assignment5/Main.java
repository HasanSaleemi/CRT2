package assignment5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
		primaryStage.setTitle("Critters Simulator");
		primaryStage.setScene(new Scene(root, 866, 443));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
