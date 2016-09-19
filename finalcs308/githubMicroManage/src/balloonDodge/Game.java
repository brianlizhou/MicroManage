package balloonDodge;

import java.util.*;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

class Game extends StackPane{
    public static final String TITLE = "Balloon Dodge";
    public static final int numBalloonSpawnPoints = 8;
	public static final int gameWIDTHOFSCREEN = 900;
	public static final int gameHEIGHTOFSCREEN = 950;
	public static final int durationOfArmor = 2500;
	public static final int initialImmunityTime = 3000;
	public static final int scoreBoardArea = gameHEIGHTOFSCREEN - Scoreboard.scoreBoardHeight;
	public static final int howManyBalloonsUntilPossibleLIFESpawnFINAL = 44;
	public static final int howManyBalloonsUntilPossibleBigSmallUpDownSpawnFINAL = 17;
	public static final int howManyBalloonsUntilPossibleArmorSpawnFINAL = 25; 
	public static final Random randomNumberGenerator = new Random();
  
	/**
	 * Stinky code (global variables), before the lecture on Thursday that basically described how
	 * static variables were bad because they were globals (should have realized)
	 * ---I removed most of the static variables, but the variables below are weaved into
	 * the flow too deeply, changing them would require changing huge algorithms in the code
	 * specifically I would need to change like 6 different methods, won't happen in the future
	*/
    private static boolean armorIsUp = false;
    private static Monkey monkeyImage;
    private static Scoreboard scoreBoard;
    public static int numberOfBalloonsUntilNextLevelForFixed = 30;//30
    public static int livesLeft = 3;
    private static long beginningTimeForArmor = 0;
  
    //IF YOU CHANGE THE FRAME RATE, NEED TO CHANGE THESE PROPORTIONALLY, and Balloon Speed
    private int stepsUntilBalloonGeneration = 66;//33
    private int stepsToReduceOnLevelUp = 17;//7
    private int stepReduction = 4;//1

    //Setting up Water gun Image, Monkey, Balloon, Balloon Generator objects
    private BalloonGeneratorAndController balloonGeneratorAndController = null;
   

    //Parameters for game, EG. balloons per level and lives left
    public int numberOfBalloonsUntilNextLevel = 30;//30
    private int numberOfBalloonsToSpawnIncreasePerLevel = 10;//10
    
    //Flags and values for next level / level increase and death
    private boolean ifNeedToStartNextLevelDelay = true;
    private long beginningDelayTime = 0;
    private long deathTime = 0;
    private long currentTime = 0;
    private long timeToWaitUntilNextLevel = 1500;
    private long respawnTimeAfterDeath = 1500;
    
    private boolean needToDelayForNextLevel = false;
    private boolean recentlyDied = false;
    private boolean levelIsOver = false;
    private boolean isPaused = true;
    private boolean isGameOver = false;
    
    public int numBalloonsGeneratedTotal = 0;
    //How often Power Ups spawn
    private int howManyBalloonsUntilPossibleLIFESpawn = 88;//88
    private int howManyBalloonsUntilPossibleBigSmallUpDownSpawn = 6;//17
    private int howManyBalloonsUntilPossibleArmorSpawn = 25;//25
    private int currentLevel = 1;
    private int stepsSinceLastBalloonGeneration = 0;
    private boolean isHardMode = false;
  //Setting up Scene and Stage
    private Scene myScene;
    private Stage stage; 
    private Group rootElement = new Group();
    private SetupWizardAndTextController OnScreenTextController = new SetupWizardAndTextController(rootElement, stage);
    
    /**
     * Create the game's scene
     */
    public Scene init (Stage s, boolean isHardMode) {
    	stage = s;
        myScene = new Scene(rootElement, gameWIDTHOFSCREEN, gameHEIGHTOFSCREEN, Color.WHITE);  
        
        this.isHardMode = isHardMode;
      //Remove default Cursor
        myScene.getCursor();
        myScene.setCursor(Cursor.NONE);
		
        OnScreenTextController.setUpInitialScreenAndBaseObjects();
        setUpCheatCodes();
        setUpScoreboardAndMonkey();
        setUpEndButton();

        return myScene;
    }
    
    /**
     * Plays animation to darken screen and changes to gameover screen
     */
    public void gameOver(){
    	Rectangle rect = new Rectangle(gameWIDTHOFSCREEN, gameHEIGHTOFSCREEN);
		rect.setFill(Color.BLACK);
		rootElement.getChildren().add(rect);
		rect.setOpacity(0);
		FadeTransition fade = new FadeTransition(Duration.millis(1000), rect);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setAutoReverse(false);
		isGameOver = true;
		rootElement.getChildren().remove(monkeyImage);
		fade.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				rootElement.getChildren().clear();
				new GameOverMenu(stage,numBalloonsGeneratedTotal,monkeyImage).display();				
			}
		});
		fade.play();
	}
      
    /**
     * What happens every frame, moves balloons, checks if you intersected with anything, moves monkey,
     * and displays text properly
     */
    public void step (double elapsedTime) {
    	//Move Monkey wherever cursor moves
    	if(isGameOver){
    		return;
    	}
    	myScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) { 	      		
            	if(event.getY() >= scoreBoardArea){
            		monkeyImage.hideMonkey();
            		myScene.getCursor();
                    myScene.setCursor(Cursor.DEFAULT);
            	}
            	else{
            		monkeyImage.showMonkey();
            		myScene.getCursor();
                    myScene.setCursor(Cursor.NONE);
            	}
            	monkeyImage.moveMonkeyTo(event.getX(),event.getY());
            }           
        });
    	
    	if(isPaused){
    		OnScreenTextController.showPausedText();
    		return;
    	}
    	OnScreenTextController.hidePausedText();
	
		stepsSinceLastBalloonGeneration++;
		if(stepsSinceLastBalloonGeneration >= stepsUntilBalloonGeneration){
			stepsSinceLastBalloonGeneration = 0; 
			if(!needToDelayForNextLevel && !recentlyDied){
				if(monkeyImage.isMonkeySplashed()){
					monkeyImage.changeToNormalMonkey();
				}
				//Generate New Balloon
				if(!levelIsOver){
					numBalloonsGeneratedTotal++;
				    generateBalloon();  
			    	scoreBoard.updateBalloonsLeftText(numberOfBalloonsUntilNextLevelForFixed - numberOfBalloonsUntilNextLevel);
				}
		    	if(numberOfBalloonsUntilNextLevel <= 0){
		    		levelIsOver = true;
		    		if(haveAllBalloonsMovedOffScreen()){
		    			if(currentLevel < 6){
		    				OnScreenTextController.showNextLevelText();
		    			 }
		    			 else{
		    				 OnScreenTextController.showYouWinText();
		    			 }
			    		balloonGeneratorAndController.clearAllBalloons(rootElement);
			    		needToDelayForNextLevel = true;
		    		return;
		    		}
		    	}
			}
		}
		moveBalloonsUpAndDeleteIfOffScreen();
		//Check if there are any delays
	    checkIfNeedToDelayForNextLevePOSSIBLEDELAY();
	    checkIfArmorIsUpPOSSIBLEDELAY();
	    checkIfRecentlyDiedPOSSIBLEDELAY();
    }
    
    /**
	 * Implements the power up balloon you contacted
	 */
	public static void implementPowerUpBalloonEffect(Balloon visiblePowerUpBalloon) {
		String powerUpType = visiblePowerUpBalloon.typeOfBalloon;
		if(powerUpType.equals("Small")){
			monkeyImage.shrinkMonkey();
		}
		else if(powerUpType.equals("Big")){
			monkeyImage.enlargeMonkey();
		}
		else if(powerUpType.equals("SlowDown")){
			Balloon.reduceBalloonSpeed();
		}
		else if(powerUpType.equals("SpeedUp")){
			Balloon.increaseBalloonSpeed();
		}
		else if(powerUpType.equals("Life")){
			increaseLives();
		}
		else if(powerUpType.equals("Armor")){
			armorUp();	
		}	
	}
	/**
	 * Increases Lives
	 */
	public static void increaseLives(){
		livesLeft++;
		scoreBoard.updateLifeCountText();
	}
	
	/**
	 * Activates Armor
	 */
	public static void armorUp(){
		armorIsUp = true;
		monkeyImage.armorUpMonkey();
		beginningTimeForArmor = System.currentTimeMillis();	
	}
    
    private boolean haveAllBalloonsMovedOffScreen(){
    	return balloonGeneratorAndController.returnBalloonsThatWillBeSpawnedThisLevel().get(0).haveIReachedTopOfScreen();
    }
     
    private void moveBalloonsUpAndDeleteIfOffScreen(){
    	//Move all balloons up and Delete Balloon if it hits the end of the map
	    boolean ifIHitBalloon = balloonGeneratorAndController.moveAllBalloonsUpwards(isHardMode, monkeyImage, armorIsUp,rootElement);  
	    if(ifIHitBalloon){
	    	livesLeft -= 1;
		    scoreBoard.updateLifeCountText();
		    deathAnimation();
		    return;	 
	    }
    }
    
    private void checkIfArmorIsUpPOSSIBLEDELAY(){
    	if(armorIsUp){
        	currentTime = System.currentTimeMillis();
        	if((currentTime - beginningTimeForArmor) >= durationOfArmor){
        		armorIsUp = false;
        		monkeyImage.removeShieldMonkey();
        	}
        	else if((currentTime - beginningTimeForArmor) >= durationOfArmor/2){
        		monkeyImage.decayShieldMonkey();
        	}
        }
    }
    
    private void checkIfNeedToDelayForNextLevePOSSIBLEDELAY(){
    	if(needToDelayForNextLevel){
	    	 if(balloonGeneratorAndController.returnVisibleBalloons().isEmpty() && balloonGeneratorAndController.returnVisiblePowerUpBalloons().isEmpty()){ 
	    		 if(ifNeedToStartNextLevelDelay){
	    			 ifNeedToStartNextLevelDelay = false;
	    			 levelIsOver = false;
	    			 setUpNextLevel();
	    			 beginningDelayTime = System.currentTimeMillis();
 
	    		 }
	    		 currentTime = System.currentTimeMillis();
	    		 if((currentTime - beginningDelayTime) >= timeToWaitUntilNextLevel){
		    		 needToDelayForNextLevel = false;
		    		 OnScreenTextController.hideBothYouWinNextLeveText();
		    	     ifNeedToStartNextLevelDelay = true;
		    		 returnBalloonsToDefaultSpeed();
	    		 }
	    	 }
		}
    }
    
    private void checkIfRecentlyDiedPOSSIBLEDELAY(){
    	if(recentlyDied){
 		   currentTime = System.currentTimeMillis();
 		   if((currentTime - deathTime) >= respawnTimeAfterDeath){
 			   recentlyDied = false;
 		   }
 	    }	
    } 
    
	private void generateBalloon() {
		int shouldISpawn;
    	if(howManyBalloonsUntilPossibleLIFESpawn == 0){
    		shouldISpawn = randomNumberGenerator.nextInt(2);
	    	howManyBalloonsUntilPossibleLIFESpawn = howManyBalloonsUntilPossibleLIFESpawnFINAL;
	    	if(shouldISpawn == 0){
	    		balloonGeneratorAndController.generateLifeBalloon(rootElement);
	    	}
	    }
	    else if(howManyBalloonsUntilPossibleArmorSpawn == 0){
	    	howManyBalloonsUntilPossibleArmorSpawn = howManyBalloonsUntilPossibleArmorSpawnFINAL;
	    	shouldISpawn = randomNumberGenerator.nextInt(2);
	    	if(shouldISpawn == 0){
	    		balloonGeneratorAndController.generateArmorBalloon(rootElement);
	    	}
		}
	    else if(howManyBalloonsUntilPossibleBigSmallUpDownSpawn == 0){
	    	howManyBalloonsUntilPossibleBigSmallUpDownSpawn = howManyBalloonsUntilPossibleBigSmallUpDownSpawnFINAL;	
	    	balloonGeneratorAndController.generateCommonPowerupBalloon(rootElement);
		}
	    else{
	    	balloonGeneratorAndController.activatePreGeneratedBalloon(rootElement,numberOfBalloonsUntilNextLevel);
	    	howManyBalloonsUntilPossibleLIFESpawn--;
	    	howManyBalloonsUntilPossibleArmorSpawn--;
	    	howManyBalloonsUntilPossibleBigSmallUpDownSpawn--;
	    	numberOfBalloonsUntilNextLevel -= 1;
	    }	    
	}
	
   
    private void deathAnimation(){
    	monkeyImage.changeToSplashedMonkey();
    	recentlyDied = true;
    	balloonGeneratorAndController.clearAllBalloons(rootElement);
    	deathTime = System.currentTimeMillis();
    	if(livesLeft <= 0){
    		gameOver();
    	}
    }
 
    private void setUpNextLevel(){
    	currentLevel += 1;
    	numberOfBalloonsUntilNextLevelForFixed += numberOfBalloonsToSpawnIncreasePerLevel;
    	numberOfBalloonsUntilNextLevel = numberOfBalloonsUntilNextLevelForFixed;
    	balloonGeneratorAndController.increaseLevelOfGame(numberOfBalloonsUntilNextLevelForFixed,rootElement);   
	    if(stepsToReduceOnLevelUp > stepReduction){
	    	stepsUntilBalloonGeneration -= stepsToReduceOnLevelUp; 
	    	stepsToReduceOnLevelUp -= stepReduction;
    	} 	
    }
    
    private void returnBalloonsToDefaultSpeed(){
    	Balloon.setBalloonSpeedBasedOnLevel(currentLevel);
    	Balloon.setInitialSpeedOfBalloons();
    }

    private void setUpEndButton(){
    	Button endGameButton = new Button("End Game Early");
    	endGameButton.setStyle(
    			"-fx-background-color: linear-gradient(#ff5400, #be1d00);" + 
    			"-fx-background-radius: 20;" + 
    			"-fx-text-fill: white;"		
		);
    	endGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameOver();
			}
		});
    	endGameButton.setTranslateX(Game.gameWIDTHOFSCREEN - 200);
    	endGameButton.setTranslateY(Game.gameHEIGHTOFSCREEN - 40);
		rootElement.getChildren().add(endGameButton);
    }
    
    private void setUpCheatCodes(){
    	myScene.setOnKeyReleased(event -> {
      	  if (event.getCode() == KeyCode.L){
      		  increaseLives();
      	  }
      	  else if (event.getCode() == KeyCode.F){
      		  Balloon.increaseBalloonSpeed();
       	  }
      	  else if (event.getCode() == KeyCode.S){
      		  Balloon.reduceBalloonSpeed();
          }
      	  else if (event.getCode() == KeyCode.U){
      		  monkeyImage.enlargeMonkey();
          }
      	  else if (event.getCode() == KeyCode.D){
      		  monkeyImage.shrinkMonkey();
          }
      	  else if (event.getCode() == KeyCode.A){
	  		  armorUp();	
      	  }
      	  else if (event.getCode() == KeyCode.P){
	  		  isPaused = !isPaused;
    	  }
       });
     }
    
	private void setUpScoreboardAndMonkey(){
		//Initialize scoreboard at bottom of screen
        Scoreboard scoreboard = new Scoreboard(rootElement);
        this.balloonGeneratorAndController = new BalloonGeneratorAndController(1,rootElement,Game.numberOfBalloonsUntilNextLevelForFixed);
        this.monkeyImage = new Monkey(rootElement);
        this.scoreBoard = scoreboard;
        Balloon.setInitialSpeedOfBalloons();
	} 
}