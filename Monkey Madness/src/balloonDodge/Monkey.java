package balloonDodge;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class Monkey {
	public static int monkeyHeight = 150;
	public static int monkeyWidth = 150;
	public static int monkeyHitBoxWidth = 26;
	public static int monkeyHitBoxHeight = 28;
	public static double armorBubbleRadius = 55;

	private Image monkeyImage = null;
	private ImageView movableMonkeyImage = null;
	private Ellipse hitBoxMonkey = null;
	private Circle armorBubble = null;
	private ImageView splashImageMonkeyDeath = null;
	private boolean monkeyIsSplashed = false;
	private boolean monkeyIsArmored = false;
	private Group rootElement;
	private int amountToShiftMonkeyForArmor = 0;
	
	public Monkey(Group rootElement){
		this.rootElement = rootElement;
		this.hitBoxMonkey = new Ellipse(monkeyHitBoxWidth,monkeyHitBoxHeight);
    	//hitBoxMonkey.setCenterX(0);
    	//hitBoxMonkey.setCenterY(0);
    	hitBoxMonkey.setFill(Color.TRANSPARENT);
    	this.rootElement.getChildren().add(hitBoxMonkey);
    	
		this.monkeyImage = new Image(getClass().getClassLoader().getResourceAsStream("Monkey.png"));
    	this.movableMonkeyImage = new ImageView(monkeyImage);
    	movableMonkeyImage.setFitWidth(monkeyWidth);
    	movableMonkeyImage.setFitHeight(monkeyHeight);
    	movableMonkeyImage.setX(0);
    	movableMonkeyImage.setY(0);
    	this.rootElement.getChildren().add(movableMonkeyImage);

    	Image splashImage = new Image(getClass().getClassLoader().getResourceAsStream("wb_explode.png"));
    	this.splashImageMonkeyDeath = new ImageView(splashImage);
    	splashImageMonkeyDeath.setFitWidth(monkeyWidth);
    	splashImageMonkeyDeath.setFitHeight(monkeyHeight);
    	
    	this.armorBubble = new Circle(armorBubbleRadius);
    	armorBubble.setFill(Color.TRANSPARENT);
    	this.rootElement.getChildren().add(armorBubble);
    	
	}
	
	public ImageView returnMonkeyImage(){
		return this.movableMonkeyImage;
	}
	
	
	public void hideMonkey(){
		movableMonkeyImage.setVisible(false);
	}
	
	public void showMonkey(){
		movableMonkeyImage.setVisible(true);
	}
	
	public Ellipse returnMonkeyHitbox(){
		return this.hitBoxMonkey;
	}
	
	public void armorUpMonkey(){
		monkeyIsArmored = true;	
		armorBubble.setFill(Color.rgb(64,64,64,0.7));
	}
	
	public void decayShieldMonkey(){
		armorBubble.setFill(Color.rgb(255,77,77,0.7));
	}
	
	public void removeShieldMonkey(){
		monkeyIsArmored = false;
		armorBubble.setFill(Color.TRANSPARENT);
	}
	
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
	
	public void changeToSplashedMonkey(){
		monkeyIsSplashed = true;
		if(!rootElement.getChildren().contains(splashImageMonkeyDeath)){
			this.rootElement.getChildren().add(splashImageMonkeyDeath);
		}
	}
	
	public void changeToNormalMonkey(){
		monkeyIsSplashed = false;
		if(rootElement.getChildren().contains(splashImageMonkeyDeath)){
			this.rootElement.getChildren().remove(splashImageMonkeyDeath);
		}
	}
	
	public boolean isMonkeySplashed(){
		return monkeyIsSplashed;
	}
	
	public void moveMonkeyTo(double xCoord, double yCoord){
		hitBoxMonkey.setCenterX(xCoord);
    	hitBoxMonkey.setCenterY(yCoord);	
    	movableMonkeyImage.setX(xCoord - 68);
    	movableMonkeyImage.setY(yCoord - 68);   	
    	if(monkeyIsSplashed){
    		splashImageMonkeyDeath.setX(xCoord - 68);
    		splashImageMonkeyDeath.setY(yCoord - 68);
    	}
    	if(monkeyIsArmored){
    		armorBubble.setCenterX(xCoord - amountToShiftMonkeyForArmor);
    		armorBubble.setCenterY(yCoord - amountToShiftMonkeyForArmor);
    	}
	}
}
