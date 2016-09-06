package balloonDodge;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameOverMenu {
	private Stage stage;
	private Scene gameOverScene;
	
	public GameOverMenu(Stage primaryStage,int balloonsDodged) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		Group root = new Group();
		stage = primaryStage;
		gameOverScene = new Scene(root, Game.gameWIDTHOFSCREEN, Game.gameHEIGHTOFSCREEN);
		root.getChildren().add(new Button());
		
		Image is = new Image(getClass().getClassLoader().getResourceAsStream("BeachBackground.jpg")); 
		ImageView img = new ImageView(is);
		img.setFitWidth(Game.gameWIDTHOFSCREEN);
		img.setFitHeight(Game.gameHEIGHTOFSCREEN);
		root.getChildren().add(img);
		
		Text gameOverMessage = new Text("Game Over");
		gameOverMessage.setStyle(
			"-fx-font: 100px Tahoma;" +
		    "-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, red 0%, orange 15%, yellow 30%, green 45%, blue 60%, pink 75%, purple 90%);" +
		    "-fx-stroke: black;" +
		    " -fx-stroke-width: 1;"
		
		);
		gameOverMessage.setTranslateX(Game.gameWIDTHOFSCREEN/2 - 250);
		gameOverMessage.setTranslateY(Game.gameHEIGHTOFSCREEN/2 - 100);
		root.getChildren().add(gameOverMessage);
		
		
		Monkey splashedMonkey = new Monkey(root);
		root.getChildren().remove(splashedMonkey.returnMonkeyImage());
		splashedMonkey.moveMonkeyTo(Game.gameWIDTHOFSCREEN/2,Game.gameHEIGHTOFSCREEN/2);
		root.getChildren().add(splashedMonkey.returnMonkeyImage());
		
		Image splash = new Image(getClass().getClassLoader().getResourceAsStream("wb_explode.png")); 
		ImageView splashImage = new ImageView(splash);
		splashImage.setX(Game.gameWIDTHOFSCREEN/2 - 70);
		splashImage.setY(Game.gameHEIGHTOFSCREEN/2 - 70);
		splashImage.setFitHeight(Monkey.monkeyHeight);
		splashImage.setFitWidth(Monkey.monkeyWidth);
		root.getChildren().add(splashImage);
			
		Text TotalBalloonsDodgedText = new Text("You Dodged: " + String.valueOf(balloonsDodged) + " Balloons");
		TotalBalloonsDodgedText.setTranslateX(Game.gameWIDTHOFSCREEN/2 - 200);
		TotalBalloonsDodgedText.setTranslateY(Game.gameHEIGHTOFSCREEN/2 + 125);
		TotalBalloonsDodgedText.setFill(Color.BLACK);
		TotalBalloonsDodgedText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		root.getChildren().add(TotalBalloonsDodgedText);
	}
	
	/**
	 * Display GameOverMenu on stage.
	 */
	public void display() {
		stage.setScene(gameOverScene);
			
	}
}

