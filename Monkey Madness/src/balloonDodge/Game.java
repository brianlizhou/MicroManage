package balloonDodge;

import java.util.*;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

class Game extends StackPane{
	//Title, size of window, armor duration, and initial immunity for user screen readjustment purposes
    public static final String TITLE = "Balloon Dodge";
    public static final int numBalloonSpawnPoints = 8;
	public static final int gameWIDTHOFSCREEN = 900;
	public static final int gameHEIGHTOFSCREEN = 950;
	public static final int durationOfArmor = 2500;
	public static final int initialImmunityTime = 3000;
	public static final int scoreBoardArea = gameHEIGHTOFSCREEN - Scoreboard.scoreBoardHeight;
    
    private static final int howManyBalloonsUntilPossibleLIFESpawnFINAL = 44;
    private static final int howManyBalloonsUntilPossibleBigSmallUpDownSpawnFINAL = 17;
    private static final int howManyBalloonsUntilPossibleArmorSpawnFINAL = 25;
    
    //Text for Good Job! and Starting Next Level...
    private static final Text goodJobText = new Text(gameWIDTHOFSCREEN/2 - 35, gameHEIGHTOFSCREEN/2 - 30,"GOOD JOB!");;
    private static final Text startingNextLevelText = new Text(gameWIDTHOFSCREEN/2 - 80, gameHEIGHTOFSCREEN/2 + 20,"STARTING NEXT LEVEL");
    private static final Text pressPToStartOrPlay = new Text(gameWIDTHOFSCREEN/2 - 160, gameHEIGHTOFSCREEN/2 - 30,"Press P To Play Or Resume");
 
    public static boolean amIHitByABalloon = false;
    
    //How often Power Ups spawn
    private static int howManyBalloonsUntilPossibleLIFESpawn = 88;//88
    private static int howManyBalloonsUntilPossibleBigSmallUpDownSpawn = 6;//17
    private static int howManyBalloonsUntilPossibleArmorSpawn = 25;//25
    public static int numBalloonsGeneratedTotal = 0;
    
    private int currentLevel = 1;//Game starts at level 1
    private int stepsSinceLastBalloonGeneration = 0;
    private static boolean armorIsUp = false;
    
    private boolean isHardMode = false;
    
    //Setting up Scene and Stage
    private Scene myScene;
    static Stage stage; 
    private Group rootElement = new Group();
    

    //CODE STYLING: Switch statements polymorphism

    /*
    Declaring most of the stuff here so we don't have to declare a thousand times per frame 
    within step function
    */
    
    public static final Random randomNumberGenerator = new Random();
    
    //Stuff related to balloon spawn places
    private int firstHalfPreviousRowSpawned = -1;
    private int secondHalfPreviousRowSpawned = -1;
    //IF YOU CHANGE THE FRAME RATE, NEED TO CHANGE THESE PROPORTIONALLY, and Balloon Speed
    private int stepsUntilBalloonGeneration = 70;//35
    private int stepsToReduceOnLevelUp = 12;//7
    private int stepReduction = 2;//1

    //Setting up Water gun Image, Monkey, Balloon, Balloon Generator objects
    private ImageView waterGunBalloonSpawn;
    private BalloonGenerator balloonGeneratorAndController = null;
    private static Monkey monkeyImage = null;
    private static Scoreboard scoreBoard = null;

    //Parameters for game, EG. balloons per level and lives left
    public static int numberOfBalloonsUntilNextLevel = 30;//30
    public static int numberOfBalloonsUntilNextLevelForFixed = 30;//30
    public static int livesLeft = 2;
    private int numberOfBalloonsToSpawnIncreasePerLevel = 10;//10
    
    //Flags and values for next level / level increase and death
    private boolean ifNeedToStartNextLevelDelay = true;
    private long beginningDelayTime = 0;
    private static long beginningTimeForArmor = 0;
    private long deathTime = 0;
    private long currentTime = 0;
    private long timeToWaitUntilNextLevel = 1500;
    private long respawnTimeAfterDeath = 1500;
    
    private static boolean needToDelayForNextLevel = false;
    private static boolean recentlyDied = false;
    private boolean isPaused = true;
    private boolean isGameOver = false;
    
    public String getTitle () {
        return TITLE;
    }

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
        
        /* Set up Scoreboard @ bottom of screen, background image, water guns (balloon spawns), 
        End Early button, and Good Job!/Starting Next Level text */
        
        //Order matters in which you set it up, later ones have higher Z-index
        setUpBackground();
		setUpWaterGuns(); 
        setUpScoreboardAndMonkey();
        setUpOnScreenText();
        setUpEndButton();
        
        setUpCheatCodes();

        return myScene;
    }
      
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
    		pressPToStartOrPlay.setVisible(true);
    		return;
    	}
    	pressPToStartOrPlay.setVisible(false);
	
		stepsSinceLastBalloonGeneration++;
		if(stepsSinceLastBalloonGeneration >= stepsUntilBalloonGeneration){
			stepsSinceLastBalloonGeneration = 0; 
			if(!needToDelayForNextLevel && !recentlyDied){
				if(monkeyImage.isMonkeySplashed()){
					monkeyImage.changeToNormalMonkey();
				}
				
				//Generate New Balloon
				numBalloonsGeneratedTotal++;
			    generateBalloon();  
		    	scoreBoard.updateBalloonsLeftText(numberOfBalloonsUntilNextLevelForFixed - numberOfBalloonsUntilNextLevel);
		    	if(numberOfBalloonsUntilNextLevel <= 0){
		    		needToDelayForNextLevel = true;
		    		return;
		    	}
			}
		}
		moveBalloonsUpAndDeleteIfOffScreen();
		
 
		//Check if there are any delays
	    checkIfNeedToDelayForNextLevePOSSIBLEDELAY();
	    checkIfArmorIsUpPOSSIBLEDELAY();
	    checkIfRecentlyDiedPOSSIBLEDELAY();
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
     
    private void moveBalloonsUpAndDeleteIfOffScreen(){
    	//Move all balloons up and Delete Balloon if it hits the end of the map
	    balloonGeneratorAndController.moveAllBalloonsUpwards(isHardMode, monkeyImage, armorIsUp,rootElement); 
	    if(amIHitByABalloon){
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
	    	 if(BalloonGenerator.visibleBalloons.isEmpty() && BalloonGenerator.visiblePowerUpBalloons.isEmpty()){
	    		 
	    		 if(ifNeedToStartNextLevelDelay){
	    			 goodJobText.setFill(Color.BLACK);
	    		     startingNextLevelText.setFill(Color.BLACK);
	    			 setUpNextLevel();
	    			 ifNeedToStartNextLevelDelay = false;
	    			 beginningDelayTime = System.currentTimeMillis();
	    			 
	    			 
	    		 }
	    		 currentTime = System.currentTimeMillis();
	    		 if((currentTime - beginningDelayTime) >= timeToWaitUntilNextLevel){
		    		 needToDelayForNextLevel = false;
		    		 goodJobText.setFill(Color.TRANSPARENT);
		    	     startingNextLevelText.setFill(Color.TRANSPARENT);
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
	
	public static void increaseLives(){
		livesLeft++;
		scoreBoard.updateLifeCountText();
	}
	
	public static void armorUp(){
		armorIsUp = true;
		monkeyImage.armorUpMonkey();
		beginningTimeForArmor = System.currentTimeMillis();	
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
	
	
	private void setUpOnScreenText(){
		goodJobText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		startingNextLevelText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		pressPToStartOrPlay.setFont(Font.font("Verdana",FontWeight.BLACK,20));
        goodJobText.setFill(Color.TRANSPARENT);
        startingNextLevelText.setFill(Color.TRANSPARENT);
        rootElement.getChildren().add(goodJobText);
        rootElement.getChildren().add(startingNextLevelText);
        rootElement.getChildren().add(pressPToStartOrPlay);
	}
	
	private void setUpScoreboardAndMonkey(){
		//Initialize scoreboard at bottom of screen
        Scoreboard scoreboard = new Scoreboard(rootElement);
        this.balloonGeneratorAndController = new BalloonGenerator(currentLevel,rootElement,numberOfBalloonsUntilNextLevelForFixed);
        this.monkeyImage = new Monkey(rootElement);
        this.scoreBoard = scoreboard;
        Balloon.setInitialSpeedOfBalloons();
        //beginningInvincibilityTime = System.currentTimeMillis();
		
	}

	private void setUpBackground(){
    	//Background Stuff
        Image is = new Image(getClass().getClassLoader().getResourceAsStream("BeachBackground.jpg")); 
		ImageView img = new ImageView(is);
		img.setFitWidth(gameWIDTHOFSCREEN);
		img.setFitHeight(gameHEIGHTOFSCREEN);
		rootElement.getChildren().add(img);
    }
    
    private void setUpWaterGuns(){
    	//Initialize Water Guns At Bottom Of Screen
		int widthOfBalloon = Balloon.balloonWidth;
		int heightOfBalloon = Balloon.balloonHeight;
        Image waterGunSpawn = new Image(getClass().getClassLoader().getResourceAsStream("watergun.png"));
        for(int numWaterGunsSpawned=0;numWaterGunsSpawned<numBalloonSpawnPoints;numWaterGunsSpawned++){	        
        	waterGunBalloonSpawn = new ImageView(waterGunSpawn);
        	waterGunBalloonSpawn.setX((widthOfBalloon)*(numWaterGunsSpawned) + 10);
        	waterGunBalloonSpawn.setY(gameHEIGHTOFSCREEN - heightOfBalloon - Scoreboard.scoreBoardHeight);
        	waterGunBalloonSpawn.setFitWidth(widthOfBalloon/4 * 3);
        	waterGunBalloonSpawn.setFitHeight(heightOfBalloon);	        
	        rootElement.getChildren().add(waterGunBalloonSpawn);
        };
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
				Stage newWindow = new Stage(); 
				newWindow.setTitle(TITLE);
				newWindow.show();
				new MainMenu(newWindow).display();
				stage.close();		
			}
		});
    	endGameButton.setTranslateX(gameWIDTHOFSCREEN - 200);
    	endGameButton.setTranslateY(gameHEIGHTOFSCREEN - 40);
		rootElement.getChildren().add(endGameButton);
    }
    
    private void deathAnimation(){
    	monkeyImage.changeToSplashedMonkey();
    	recentlyDied = true;
    	amIHitByABalloon = false;
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
				new GameOverMenu(stage,numBalloonsGeneratedTotal).display();				
			}
		});
		fade.play();
	}
    
}