package balloonDodge;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application{
	Stage stage;
	
	public void start(Stage primaryStage){	
		stage = primaryStage;
		Scene scene = new Scene(new MainMenu(primaryStage).setUpWindow());
		primaryStage.setTitle("Balloon Dodge");
		primaryStage.setScene(scene);;
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}