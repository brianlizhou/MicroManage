package balloonDodge;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class InfoBlock {
	
	public final int balloonSize = 100;
	public final int TitleFontSize = 50;
	public final int InfoFontSize = 15;
	public final String InfoFont = "Verdana";
	
	private Pane gameWindow;
	private double xTranslation;
	private double yTranslation;
	private ImageView powerUpVisibleImage;
	private Text powerUpType;
	private Text powerUpInfoText;
	private Text ImportantNoteText;
	private Text cheatCodeText;
	private boolean isNoteAtTop = false;
	
	public InfoBlock(Pane gameWindow,int column, int powerUpNumber,String powerUpURL, String infoText, String powerUpName){
		 this.gameWindow = gameWindow;
		 if(column == 1){
			 this.xTranslation = MainMenu.mainMenuWIDTHOFSCREEN/6;
		 }
		 else if(column == 2){
			 this.xTranslation = 4 * MainMenu.mainMenuWIDTHOFSCREEN/7;
		 }
		 
		 int relativeHeightOfPowerUpLocation = powerUpNumber;
		 if(relativeHeightOfPowerUpLocation > 3){
			 relativeHeightOfPowerUpLocation -= 3;
		 }
		 this.yTranslation = relativeHeightOfPowerUpLocation * MainMenu.mainMenuHEIGHTOFSCREEN/5 + 50;
		 
		 if(column == 0){
			 setUpNoteText();
			 isNoteAtTop = true;
		 }
		 else{
			 setUpImagesForInfoblock(powerUpName, powerUpURL, infoText);
		 }
	}
	
	/**
	 * Moves description box for powerup and text to a certain location
	 */
	public void moveInfoBlock(){
		 //PowerUp Icon
		 powerUpVisibleImage.setX(xTranslation-100);
		 powerUpVisibleImage.setY(yTranslation);
		 
		 //Title Text
		 powerUpType.setX(xTranslation + 150);
		 powerUpType.setY(yTranslation + 25);
		 
		 //Info for PowerUp
		 powerUpInfoText.setX(xTranslation);
		 powerUpInfoText.setY(yTranslation + 50);
	}
	
	/**
	 * Adds small info description block for a powerUp including image and description
	 */
	public void addInfoBlockToScreen(){
		gameWindow.getChildren().add(powerUpVisibleImage);
		gameWindow.getChildren().add(powerUpType);
		gameWindow.getChildren().add(powerUpInfoText);
	}
	
	/**
	 * Hides a specific description info block
	 */
	public void hideInfoBlock(){
		if(isNoteAtTop){
			ImportantNoteText.setVisible(false);
			cheatCodeText.setVisible(false);
		}
		else{
			powerUpVisibleImage.setVisible(false);
			powerUpType.setVisible(false);
			powerUpInfoText.setVisible(false);
		}
	}
	
	/**
	 * Shows a specific description info block
	 */
	public void showInfoBlock(){
		if(isNoteAtTop){
			ImportantNoteText.setVisible(true);
			cheatCodeText.setVisible(true);
		}
		else{
			powerUpVisibleImage.setVisible(true);
			powerUpType.setVisible(true);
			powerUpInfoText.setVisible(true);
		}
	}
	
	private void setUpImagesForInfoblock(String powerUpName, String powerUpURL,String infoText){
		 //Power Up Icon
		 Image powerUpBaseImage = new Image(getClass().getClassLoader().getResourceAsStream(powerUpURL)); 
		 this.powerUpVisibleImage = new ImageView(powerUpBaseImage);
		 powerUpVisibleImage.setFitHeight(balloonSize);
		 powerUpVisibleImage.setFitWidth(balloonSize);
		
		 //Power Up Title
		 this.powerUpType = new Text(powerUpName);
		 powerUpType.setFont(Font.font(InfoFont, FontWeight.BLACK, TitleFontSize));
		 
		 //Power Up Information
		 this.powerUpInfoText = new Text(infoText);
		 powerUpType.setFont(Font.font(InfoFont, FontWeight.NORMAL, InfoFontSize));
	}
	
	private void setUpNoteText(){
		this.ImportantNoteText = new Text("NOTE: The game doesn't start until you press P to Play! Readjust the screen if you need to, then try to keep\n" + "your mouse in the game for the duration of the play!\n" 
	+ "\nINSTRUCTIONS: You're the monkey fixed on the cursor, and on a mission to dodge as many balloons as posible.\n See how long you can last! ");
		ImportantNoteText.setFont(Font.font(InfoFont, FontWeight.BOLD, InfoFontSize)); 
		ImportantNoteText.setX(MainMenu.mainMenuWIDTHOFSCREEN/3 - 200);
		ImportantNoteText.setY(MainMenu.mainMenuHEIGHTOFSCREEN/7);
		ImportantNoteText.setFill(Color.BLUE);
		gameWindow.getChildren().add(ImportantNoteText);
		this.cheatCodeText = new Text("Press P To Pause/Resume at Any Time --- CHEATS: L = Extra life, F = (Fast) Speed Up, \nS = Slow Down, U = Up Monkey Size, D = Down Monkey Size, A = Armor Up");
		cheatCodeText.setFont(Font.font(InfoFont, FontWeight.BOLD, InfoFontSize));
		cheatCodeText.setX(MainMenu.mainMenuWIDTHOFSCREEN/3 - 150);
		cheatCodeText.setY(MainMenu.mainMenuHEIGHTOFSCREEN/7 * 6);
		gameWindow.getChildren().add(cheatCodeText);
	}
}
