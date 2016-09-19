package balloonDodge;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameOverMenu {
	private Stage stage;
	private Scene gameOverScene;
	private Group root = new Group();
	private Monkey monkeyImage;
	public GameOverMenu(Stage primaryStage,int balloonsDodged,Monkey monkeyImage) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
		stage = primaryStage;
		gameOverScene = new Scene(root, Game.gameWIDTHOFSCREEN, Game.gameHEIGHTOFSCREEN);
		addBackground();
		addGameOverMessage();
		setUpMonkey();
		setUpSplashImage();
		addTotalBalloonsDodgedText(balloonsDodged);
	}
	
	/**
	 * Display GameOverMenu on stage.
	 */
	public void display() {
		stage.setScene(gameOverScene);		
	}
	
	private void addBackground(){
		Image is = new Image(getClass().getClassLoader().getResourceAsStream("BeachBackground.jpg")); 
		ImageView img = new ImageView(is);
		img.setFitWidth(Game.gameWIDTHOFSCREEN);
		img.setFitHeight(Game.gameHEIGHTOFSCREEN);
		root.getChildren().add(img);
	}
	
	private void addGameOverMessage(){
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
	}
	
	private void setUpMonkey(){
		monkeyImage= new Monkey(root);
		root.getChildren().remove(monkeyImage.returnMonkeyImage());
		monkeyImage.moveMonkeyTo(Game.gameWIDTHOFSCREEN/2,Game.gameHEIGHTOFSCREEN/2);
		root.getChildren().add(monkeyImage.returnMonkeyImage());
		
	}
	
	private void setUpSplashImage(){
		Image splash = new Image(getClass().getClassLoader().getResourceAsStream("wb_explode.png")); 
		ImageView splashImage = new ImageView(splash);
		splashImage.setX(Game.gameWIDTHOFSCREEN/2 - 70);
		splashImage.setY(Game.gameHEIGHTOFSCREEN/2 - 70);
		splashImage.setFitHeight(monkeyImage.returnMonkeyHeight());
		splashImage.setFitWidth(monkeyImage.returnMonkeyWidth());
		root.getChildren().add(splashImage);
	}
	
	private void addTotalBalloonsDodgedText(int balloonsDodged){
		Text TotalBalloonsDodgedText = new Text("You Dodged: " + String.valueOf(balloonsDodged) + " Balloons");
		TotalBalloonsDodgedText.setTranslateX(Game.gameWIDTHOFSCREEN/2 - 200);
		TotalBalloonsDodgedText.setTranslateY(Game.gameHEIGHTOFSCREEN/2 + 125);
		TotalBalloonsDodgedText.setFill(Color.BLACK);
		TotalBalloonsDodgedText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		root.getChildren().add(TotalBalloonsDodgedText);
	}
}

