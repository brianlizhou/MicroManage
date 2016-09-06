package balloonDodge;

import javafx.scene.paint.Color;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Balloon {
	//Fixed Balloon Size
	public static final int balloonHeight = 100;
	public static final int balloonWidth = 114;
	public static final int balloonHitboxRadius = 36;
	public static final int circleRadiusMargin = 2;
	//If you increase the FPS by 20%, need to decrease the speed by 20%
	public static final double initialSpeedLevelOne = 1.5;
	
	//These variables change on each step/frame iteration
	private static int currentLevelOfGame = 1;
	
	private double currentYLocation = Game.gameHEIGHTOFSCREEN - (balloonHeight/2 * 3);
	private double currentXLocation;
	private int currentRotationAmount;
	
	private static double defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	public static double speedOfBalloon; //Default speed of 2
	private boolean movingPositiveXDirection = false;
	
	//Defined within constructor
	private ImageView balloonImage = null;
	private Image balloon = null;
	private Circle balloonHitbox = null;
	public String typeOfBalloon = "Normal";
	

	private Group rootElement;
	
	public Balloon(int currentLevelOfGame,Group rootElement,String typeOfBalloon){
		int whichDirectionWillIBeMovingX = Game.randomNumberGenerator.nextInt(2);
		if(whichDirectionWillIBeMovingX == 1){
			movingPositiveXDirection = true;
		}
		this.currentLevelOfGame = currentLevelOfGame;
		this.rootElement = rootElement;
		
		this.typeOfBalloon = typeOfBalloon;
		String imageURLForBalloonColor;
		//Set up Balloon Image
		if(typeOfBalloon.equals("Normal")){
			this.currentRotationAmount = Game.randomNumberGenerator.nextInt(360);
			imageURLForBalloonColor = setBalloonColorBasedOnLevel(currentLevelOfGame);
		}
		else{
			imageURLForBalloonColor = setPowerUpBalloonImage();
			this.currentRotationAmount = 0;
		}
		this.balloon = new Image(getClass().getClassLoader().getResourceAsStream(imageURLForBalloonColor));
		this.balloonImage = new ImageView(balloon);
		
		//Set up Balloon Hitbox
        this.balloonHitbox = new Circle(balloonHitboxRadius);
        balloonHitbox.setFill(Color.TRANSPARENT);
        rootElement.getChildren().add(balloonHitbox);
        
        
        //Set up Balloon Location and Position
        balloonImage.setFitWidth(balloonWidth);
        balloonImage.setFitHeight(balloonHeight);
        balloonImage.setX(10000);//send image off screen
        rootElement.getChildren().add(balloonImage);
        
            
        //Set Speed of Balloon
        //setBalloonSpeedBasedOnLevel(currentLevelOfGame);      
	}
	
	public void freeBalloonSpace(){
		this.balloonImage = null;
		this.balloonHitbox = null;
		this.balloon = null;
		System.gc();
	}
	
	public static void setInitialSpeedOfBalloons(){
		speedOfBalloon = defaultUnchangedSpeedForThisLevel;
	}
	
	public static void reduceBalloonSpeed(){
		if(speedOfBalloon >= 1){
			speedOfBalloon--;
		}
	}
	public static void increaseBalloonSpeed(){
		speedOfBalloon++;
	}
	
	public Circle returnBalloonHitbox(){
		 return this.balloonHitbox;
	 }
	 
	public ImageView returnBalloonImage(){
		return this.balloonImage;
	}

	public void moveBalloonUpwardsNORMALMODE(){
		currentYLocation -= speedOfBalloon;
		balloonHitbox.setCenterY(currentYLocation);
		balloonImage.setY(currentYLocation-(balloonHeight/2) + circleRadiusMargin);
	}
	
	public void moveBalloonUpwardsHARDMODE(){
		moveBalloonUpwardsNORMALMODE();
		if(currentXLocation <= 0){
			movingPositiveXDirection = true;
		}
		else if(currentXLocation >= Game.gameWIDTHOFSCREEN){
			movingPositiveXDirection = false;
		}
		
		if(movingPositiveXDirection){
			currentXLocation += speedOfBalloon;
		}
		else{
			currentXLocation -= speedOfBalloon;
		}
		balloonHitbox.setCenterX(currentXLocation);
		balloonImage.setX(currentXLocation-(balloonHeight/2) + circleRadiusMargin);
	}
	
	public boolean haveIReachedTopOfScreen(){
		return (currentYLocation <= (0-(balloonHeight*2)));
	}
	
	public void setRotate(){
		if(currentRotationAmount == 360){
			currentRotationAmount = 0;
		}
		currentRotationAmount++;
		balloonImage.setRotate(currentRotationAmount);
	}
	
	public void setXLocation(int xLocation){
		this.currentXLocation = xLocation;
		balloonImage.setX(xLocation);
		balloonHitbox.setCenterX(xLocation+(balloonWidth/2) - circleRadiusMargin);
	}

	
	public static void setBalloonSpeedBasedOnLevel(int currentLevelOfGame){
		switch (currentLevelOfGame) {
	        case 1:  defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	                 break;
	        case 2:  defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	                 break;
	        case 3:  defaultUnchangedSpeedForThisLevel = 2;
	                 break;
	        case 4:  defaultUnchangedSpeedForThisLevel = 2;
	                 break;
	        case 5:  defaultUnchangedSpeedForThisLevel = 2.5;
	                 break;
	        case 6:  defaultUnchangedSpeedForThisLevel = 2.5;
	        		 break;
	        default: defaultUnchangedSpeedForThisLevel = 2.5;
	                 break;
		}
	}
	
	private static String setBalloonColorBasedOnLevel(int currentLevelOfGame){
		String balloonURL = "";
		switch (currentLevelOfGame) {
	        case 1:  balloonURL = "wb_1.png";
	                 break;
	        case 2:  balloonURL = "wb_2.png";
	                 break;
	        case 3:  balloonURL = "wb_3.png";
	                 break;
	        case 4:  balloonURL = "wb_4.png";
	                 break;
	        case 5:  balloonURL = "wb_5.png";
	                 break;
	        case 6:  balloonURL = "wb_6.png";
	        		 break;
	        default: balloonURL = "wb_6.png";
	                 break;
		}
		return balloonURL;
	}
	
	private String setPowerUpBalloonImage(){
		if(typeOfBalloon.equals("Big")){
			return "wb_big.png";
		}
		else if(typeOfBalloon.equals("Small")){
			return "wb_small.png";
		}
		else if(typeOfBalloon.equals("SpeedUp")){
			return "wb_speed.png";
		}
		else if(typeOfBalloon.equals("SlowDown")){
			return "wb_slow.png";
		}
		else if(typeOfBalloon.equals("Life")){
			return "wb_life.png";
		}
		else if(typeOfBalloon.equals("Armor")){
			return "wb_armor.png";
		}
		return "FAIL";
	}

}
