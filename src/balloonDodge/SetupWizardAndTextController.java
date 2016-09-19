package balloonDodge;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SetupWizardAndTextController {
	public static final Text goodJobText = new Text(Game.gameWIDTHOFSCREEN/2 - 35, Game.gameHEIGHTOFSCREEN/2 - 30,"GOOD JOB!");;
	public static final Text startingNextLevelText = new Text(Game.gameWIDTHOFSCREEN/2 - 80, Game.gameHEIGHTOFSCREEN/2 + 20,"STARTING NEXT LEVEL");
	public static final Text pressPToStartOrPlay = new Text(Game.gameWIDTHOFSCREEN/2 - 160, Game.gameHEIGHTOFSCREEN/2 - 30,"Press P To Play Or Resume");
	public static final Text youWinText = new Text(Game.gameWIDTHOFSCREEN/2 - 35, Game.gameHEIGHTOFSCREEN/2 - 30,"YOU WIN!");;
	public static final Text survivalTimeText = new Text(Game.gameWIDTHOFSCREEN/2 - 80, Game.gameHEIGHTOFSCREEN/2 + 20,"IT'S SURVIVAL TIME");	
	private Group rootElement;
	private Stage stage;
	
	public SetupWizardAndTextController(Group rootElement, Stage s){
		this.rootElement = rootElement;
		this.stage = s;
	}
	
	/**
	 *	Set up Scoreboard @ bottom of screen, background image, water guns (balloon spawns), 
     *	End Early button, and Good Job!/Starting Next Level text
     *  -Order matters in which you set it up, later ones have higher Z-index
	 */
	public void setUpInitialScreenAndBaseObjects(){
		setUpBackground();
		setUpWaterGuns(); 
        setUpOnScreenText();
	}
	
	/**
	 * Shows Press To Play... text
	 */
	public void showPausedText() {
		pressPToStartOrPlay.setVisible(true);
	}
	
	/**
	 * Hides Press To Play... text
	 */
	public void hidePausedText() {
		pressPToStartOrPlay.setVisible(false);
	}
	
	/**
	 * Shows starting next level text
	 */
	public void showNextLevelText(){
		goodJobText.setFill(Color.BLACK);
	    startingNextLevelText.setFill(Color.BLACK);
	}
	
	/**
	 * Shows You Win! text
	 */
	public void showYouWinText(){
		youWinText.setFill(Color.BLACK);
		survivalTimeText.setFill(Color.BLACK);
	}
	
	/**
	 * Hides all of the text in the middle of the screen not including pause text
	 */
	public void hideBothYouWinNextLeveText(){
		goodJobText.setFill(Color.TRANSPARENT);
	    startingNextLevelText.setFill(Color.TRANSPARENT);
	    youWinText.setFill(Color.TRANSPARENT);
		survivalTimeText.setFill(Color.TRANSPARENT);
	}
    
	
    
	private void setUpOnScreenText(){
		goodJobText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		startingNextLevelText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		pressPToStartOrPlay.setFont(Font.font("Verdana",FontWeight.BLACK,20));
		youWinText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		survivalTimeText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		goodJobText.setFill(Color.TRANSPARENT);
		startingNextLevelText.setFill(Color.TRANSPARENT);
		youWinText.setFill(Color.TRANSPARENT);
		survivalTimeText.setFill(Color.TRANSPARENT);
        rootElement.getChildren().add(goodJobText);
        rootElement.getChildren().add(startingNextLevelText);
        rootElement.getChildren().add(pressPToStartOrPlay);
        rootElement.getChildren().add(youWinText);
        rootElement.getChildren().add(survivalTimeText);
	}
	
	
	private void setUpBackground(){
    	//Background Stuff
        Image backgroundData = new Image(getClass().getClassLoader().getResourceAsStream("BeachBackground.jpg")); 
		ImageView backgroundImage = new ImageView(backgroundData);
		backgroundImage.setFitWidth(Game.gameWIDTHOFSCREEN);
		backgroundImage.setFitHeight(Game.gameHEIGHTOFSCREEN);
		rootElement.getChildren().add(backgroundImage);
    }
    
    private void setUpWaterGuns(){
    	//Initialize Water Guns At Bottom Of Screen
		int widthOfBalloon = Balloon.balloonWidth;
		int heightOfBalloon = Balloon.balloonHeight;
        Image waterGunSpawn = new Image(getClass().getClassLoader().getResourceAsStream("watergun.png"));
        for(int numWaterGunsSpawned=0;numWaterGunsSpawned<Game.numBalloonSpawnPoints;numWaterGunsSpawned++){	        
        	ImageView waterGunBalloonSpawn = new ImageView(waterGunSpawn);
        	waterGunBalloonSpawn.setX((widthOfBalloon)*(numWaterGunsSpawned) + 10);
        	waterGunBalloonSpawn.setY(Game.gameHEIGHTOFSCREEN - heightOfBalloon - Scoreboard.scoreBoardHeight);
        	waterGunBalloonSpawn.setFitWidth(widthOfBalloon/4 * 3);
        	waterGunBalloonSpawn.setFitHeight(heightOfBalloon);	        
	        rootElement.getChildren().add(waterGunBalloonSpawn);
        };
    }
}
