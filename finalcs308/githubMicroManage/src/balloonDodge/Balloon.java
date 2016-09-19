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
	public static final double initialSpeedLevelOne = 2;
	
	private double currentYLocation = Game.gameHEIGHTOFSCREEN - (balloonHeight/2 * 3);
	private double currentXLocation;
	private int currentRotationAmount;
	private static double defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	public static double speedOfBalloon; //Default speed of 2
	private boolean movingPositiveXDirection = false;
	
	//Defined within constructor, defaulted to value
	private ImageView balloonImage = null;
	private Image balloon = null;
	private Circle balloonHitbox = null;
	public String typeOfBalloon = "Normal";
	private Group rootElement;

	public Balloon(int currentLevelOfGame,Group rootElement,String typeOfBalloon){
		int whichDirectionWillIBeMovingX = Game.randomNumberGenerator.nextInt(2);
		this.rootElement = rootElement;
		if(whichDirectionWillIBeMovingX == 1){
			movingPositiveXDirection = true;
		}
		
		//Set type of balloon
		setTypeOfBalloon(typeOfBalloon);
		
		//Set up Balloon Hitbox
        setUpBalloonHitbox();
        
      //Set up Balloon Location and Position
        setUpBalloonImage(currentLevelOfGame);   
	}
	
	/**
	 * Method nullifies objects to free space so we don't get java heap space error
	 */
	public void freeBalloonSpace(){
		this.balloonImage = null;
		this.balloonHitbox = null;
		this.balloon = null;
		System.gc();
	}
	
	/**
	 * Resets balloon speed at beginning of each level
	 */
	public static void setInitialSpeedOfBalloons(){
		speedOfBalloon = defaultUnchangedSpeedForThisLevel;
	}
	
	/**
	 * PowerUp Slow
	 */
	public static void reduceBalloonSpeed(){
		if(speedOfBalloon > 1){
			speedOfBalloon--;
		}
	}
	
	/**
	 * PowerUp Speed Up
	 */
	public static void increaseBalloonSpeed(){
		speedOfBalloon += 0.5;
	}
	
	/**
	 * PowerUp Shrink
	 */
	public Circle returnBalloonHitbox(){
		 return this.balloonHitbox;
	 }
	 
	/**
	 * Returns balloon Image
	 */
	public ImageView returnBalloonImage(){
		return this.balloonImage;
	}

	/**
	 * Moves all balloons upwards at speedOfBalloon
	 */
	public void moveBalloonUpwardsNORMALMODE(){
		currentYLocation -= speedOfBalloon;
		balloonHitbox.setCenterY(currentYLocation);
		balloonImage.setY(currentYLocation-(balloonHeight/2) + circleRadiusMargin);
	}
	
	/**
	 * Moves all balloons upwards and horizontally at speedOfBalloon
	 */
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
	
	/**
	 * Checks if balloon has gone off-screen at top
	 */
	public boolean haveIReachedTopOfScreen(){
		return (currentYLocation <= (0-(balloonHeight)));
	}
	
	/**
	 * Rotates Balloon
	 */
	public void setRotate(){
		if(currentRotationAmount == 360){
			currentRotationAmount = 0;
		}
		currentRotationAmount++;
		balloonImage.setRotate(currentRotationAmount);
	}
	
	/**
	 * Sets X-Location
	 */
	public void setXLocation(int xLocation){
		this.currentXLocation = xLocation;
		balloonImage.setX(xLocation);
		balloonHitbox.setCenterX(xLocation+(balloonWidth/2) - circleRadiusMargin);
	}

	/**
	 * Initializes default speed for level
	 */
	public static void setBalloonSpeedBasedOnLevel(int currentLevelOfGame){
		switch (currentLevelOfGame) {
	        case 1:  defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	                 break;
	        case 2:  defaultUnchangedSpeedForThisLevel = initialSpeedLevelOne;
	                 break;
	        case 3:  defaultUnchangedSpeedForThisLevel = 2.5;
	                 break;
	        case 4:  defaultUnchangedSpeedForThisLevel = 2.5;
	                 break;
	        case 5:  defaultUnchangedSpeedForThisLevel = 2.75;
	                 break;
	        case 6:  defaultUnchangedSpeedForThisLevel = 3;
	        		 break;
	        default: defaultUnchangedSpeedForThisLevel = 3;
	                 break;
		}
	}
	
	/**
	 * Initializes Type of Balloon (normal or power-up)
	 */
	private void setTypeOfBalloon(String typeOfBalloon){
		String type = typeOfBalloon;
		if(typeOfBalloon.equals("0")){
			type = "SpeedUp";
		}
		else if(typeOfBalloon.equals("1")){
			type = "SlowDown";
		}
		else if(typeOfBalloon.equals("2")){
			type = "Big";
		}
		else if(typeOfBalloon.equals("3")){
			type = "Small";
		}
		this.typeOfBalloon = type;
	}
	
	/**
	 * Changes balloon image depending on level
	 */
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
	
	/**
	 * Set up balloon hitbox
	 */
	private void setUpBalloonHitbox(){
		this.balloonHitbox = new Circle(balloonHitboxRadius);
        balloonHitbox.setFill(Color.TRANSPARENT);
        rootElement.getChildren().add(balloonHitbox);
	}
	
	/**
	 * Set up balloon image
	 */
	private void setUpBalloonImage(int currentLevelOfGame){
		String imageURLForBalloonColor;
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
        balloonImage.setFitWidth(balloonWidth);
        balloonImage.setFitHeight(balloonHeight);
        balloonImage.setX(10000);//send image off screen
        rootElement.getChildren().add(balloonImage);  
	}
	
	/**
	 * Sets Power Up image depending on type
	 */
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