package assignment5;

import com.sun.javafx.tools.packager.Param;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	private static GridPane grid = new GridPane();

	private static final double gridSquareSize = 25;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		ScrollPane scroll = new ScrollPane(grid);
		scroll.setContent(grid);
		scroll.setPrefSize(200, 200);

		Scene scene = new Scene(scroll, scroll.getWidth(), scroll.getHeight());

		grid.setHgap(-1);
		grid.setVgap(-1);

		for(int x = 0; x < Params.world_width; x++){
			for(int y = 0; y < Params.world_height; y++){
				Shape s = new Rectangle(gridSquareSize, gridSquareSize);
				s.setFill(null);
				s.setStroke(Color.RED);
				grid.add(s, x, y);
			}
		}


		primaryStage.setScene(scene);
		primaryStage.setHeight(419);
		primaryStage.setWidth(419);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
