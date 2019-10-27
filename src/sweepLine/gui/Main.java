package sweepLine.gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sceneBuilder.fxml"));
		
		Scene scene = new Scene(root, 300, 275, Color.BLACK);
		stage.setTitle("Hello, Sweep Line");
        stage.setScene(scene);
        stage.show();
	}
	
}
