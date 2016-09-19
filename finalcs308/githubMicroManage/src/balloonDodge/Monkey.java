package balloonDodge;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class Monkey {
	private int monkeyHeight = 150;
	private int monkeyWidth = 150;
	private int monkeyHitBoxWidth = 26;
	private int monkeyHitBoxHeight = 28;
	private double armorBubbleRadius = 55;

	private Image monkeyImage = null;
	private ImageView movableMonkeyImage = null;
	private Ellipse hitBoxMonkey = null;
	private Circle armorBubble = null;
	private ImageView splashImageMonkeyDeath = null;
	private boolean monkeyIsSplashed = false;
	private boolean monkeyIsArmored = false;
	private Group rootElement;
	private int amountToShiftMonkeyForArmor = 0;
	private double currentMonkeyXCoord = 0;
	private double currentMonkeyYCoord = 0;
	
	public Monkey(Group rootElement){
		this.rootElement = rootElement;	
		setUpMonkeyHitbox();
		setUpMonkeyImage();
		setUpMonkeyImageDeath();
		addArmorBubble();
    		
    	if(currentMonkeyXCoord != 0){
    		this.armorBubble.setCenterX(currentMonkeyXCoord);
        	this.armorBubble.setCenterY(currentMonkeyYCoord);
    	}
    	else{
    		this.armorBubble.setCenterX(10000);
        	this.armorBubble.setCenterY(10000);
    	}  	
	}
	
	/**
	 * @return monkeyImage
	 */
	public ImageView returnMonkeyImage(){
		return this.movableMonkeyImage;
	}
	
	/**
	 * Hides monkey image
	 */
	public void hideMonkey(){
		movableMonkeyImage.setVisible(false);
		armorBubble.setVisible(false);
		splashImageMonkeyDeath.setVisible(false);
	}
	
	/**
	 * Shows monkey image
	 */
	public void showMonkey(){
		movableMonkeyImage.setVisible(true);
		armorBubble.setVisible(true);
		splashImageMonkeyDeath.setVisible(true);
	}
	
	/**
	 * @return monkey hitbox
	 */
	public Ellipse returnMonkeyHitbox(){
		return this.hitBoxMonkey;
	}
	
	/**
	 * Sets monkey as invulnerable and adds balloon around him
	 */
	public void armorUpMonkey(){
		monkeyIsArmored = true;	
		armorBubble.setFill(Color.rgb(64,64,64,0.7));
	}
	
	/**
	 * Changes the balloon color to red to indicate that it's about to fall off
	 */
	public void decayShieldMonkey(){
		armorBubble.setFill(Color.rgb(255,77,77,0.7));
	}
	
	/**
	 * Removes shield on monkey
	 */
	public void removeShieldMonkey(){
		monkeyIsArmored = false;
		armorBubble.setFill(Color.TRANSPARENT);
	}
	
	/**
	 * Makes monkey bigger
	 */
	public void enlargeMonkey(){
		if(amountToShiftMonkeyForArmor >= 5){
			amountToShiftMonkeyForArmor -= 5;
		}
		monkeyWidth += 15;
		monkeyHeight += 15;
		monkeyHitBoxWidth += 2;
		monkeyHitBoxHeight += 2;
		armorBubbleRadius += 1;
		armorBubble.setRadius(armorBubbleRadius);
		movableMonkeyImage.setFitWidth(monkeyWidth);
    	movableMonkeyImage.setFitHeight(monkeyHeight);
    	hitBoxMonkey.setRadiusX(monkeyHitBoxWidth);
    	hitBoxMonkey.setRadiusY(monkeyHitBoxHeight);
	}
	
	/**
	 * Makes monkey smaller
	 */
	public void shrinkMonkey(){
		if(monkeyWidth > 105){//Don't make him have like 0 size
			monkeyWidth -= 15;
			monkeyHeight -= 15;
			monkeyHitBoxWidth -= 3;
			monkeyHitBoxHeight -= 3;
			armorBubbleRadius -= 2;
			amountToShiftMonkeyForArmor += 5;
		}
		armorBubble.setRadius(armorBubbleRadius);
		hitBoxMonkey.setRadiusX(monkeyHitBoxWidth);
    	hitBoxMonkey.setRadiusY(monkeyHitBoxHeight);
		movableMonkeyImage.setFitWidth(monkeyWidth);
    	movableMonkeyImage.setFitHeight(monkeyHeight);
	}
	
	/**
	 * Adds splash image in front of monkey
	 */
	public void changeToSplashedMonkey(){
		monkeyIsSplashed = true;
		if(!rootElement.getChildren().contains(splashImageMonkeyDeath)){
			this.rootElement.getChildren().add(splashImageMonkeyDeath);
		}
	}
	
	/**
	 * Removes splash image in front of monkey
	 */
	public void changeToNormalMonkey(){
		monkeyIsSplashed = false;
		if(rootElement.getChildren().contains(splashImageMonkeyDeath)){
			this.rootElement.getChildren().remove(splashImageMonkeyDeath);
		}
	}
	
	/**
	 * @return if monkey has splash icon on
	 */
	public boolean isMonkeySplashed(){
		return monkeyIsSplashed;
	}
	
	/**
	 * Moves monkey to specific coordinates
	 * @param xCoord - specific x-coordinate
	 * @param yCoord - specific y-coordinate
	 */
	public void moveMonkeyTo(double xCoord, double yCoord){
		hitBoxMonkey.setCenterX(xCoord);
    	hitBoxMonkey.setCenterY(yCoord);	
    	
    	currentMonkeyXCoord = xCoord - 68;
    	currentMonkeyYCoord = yCoord - 68;
    	movableMonkeyImage.setX(currentMonkeyXCoord);
    	movableMonkeyImage.setY(currentMonkeyYCoord);   	
    	if(monkeyIsSplashed){
    		splashImageMonkeyDeath.setX(currentMonkeyXCoord);
    		splashImageMonkeyDeath.setY(currentMonkeyYCoord);
    	}
    	if(monkeyIsArmored){
    		armorBubble.setCenterX(xCoord - amountToShiftMonkeyForArmor);
    		armorBubble.setCenterY(yCoord - amountToShiftMonkeyForArmor);
    	}
	}
	
	/**
	 * @return monkey width
	 */
	public double returnMonkeyWidth() {
		return monkeyWidth;
	}

	/**
	 * @return monkey height
	 */
	public double returnMonkeyHeight() {
		return monkeyHeight;
	}
	
	private void setUpMonkeyHitbox(){
		this.hitBoxMonkey = new Ellipse(monkeyHitBoxWidth,monkeyHitBoxHeight);
    	hitBoxMonkey.setFill(Color.TRANSPARENT);
    	this.rootElement.getChildren().add(hitBoxMonkey);
	}
	
	private void setUpMonkeyImage(){
		this.monkeyImage = new Image(getClass().getClassLoader().getResourceAsStream("Monkey.png"));
    	this.movableMonkeyImage = new ImageView(monkeyImage);
    	movableMonkeyImage.setFitWidth(monkeyWidth);
    	movableMonkeyImage.setFitHeight(monkeyHeight);
    	movableMonkeyImage.setX(0);
    	movableMonkeyImage.setY(0);
    	this.rootElement.getChildren().add(movableMonkeyImage);
	}

	
	private void setUpMonkeyImageDeath(){
		Image splashImage = new Image(getClass().getClassLoader().getResourceAsStream("wb_explode.png"));
    	this.splashImageMonkeyDeath = new ImageView(splashImage);
    	splashImageMonkeyDeath.setFitWidth(monkeyWidth);
    	splashImageMonkeyDeath.setFitHeight(monkeyHeight);
	}
	
	private void addArmorBubble(){
		this.armorBubble = new Circle(armorBubbleRadius);
		armorBubble.setFill(Color.TRANSPARENT);
		this.rootElement.getChildren().add(armorBubble);
	}
}
