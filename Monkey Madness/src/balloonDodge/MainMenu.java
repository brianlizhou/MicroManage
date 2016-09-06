package balloonDodge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenu {
	public static final int mainMenuWIDTHOFSCREEN = 1300;
	public static final int mainMenuHEIGHTOFSCREEN = 750;
	
	public static final int numberOfPowerups = 8;
	public static final int marginHorizontalForWindow = 200;
	public static final int marginVerticalForWindow = 200;
		
	public static final int FRAMES_PER_SECOND = 30;
	private static final int MILLISECOND_DELAY = 250 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private static final double BackButtonWidth = 90.0;
	private static ArrayList<InfoBlock> PowerUpInfoWindowStuff = new ArrayList<InfoBlock>();
	static Stage stage;
	
	private static HashMap<String,String> IconToDescriptionMap = new HashMap<String,String>();
	
	private static Rectangle infoFullWindow;
	private static Button clearInfoWindowButton;


	public MainMenu(Stage s){
		 stage= s;		
	}
	

	public static class MenuItem extends StackPane{
		private Scene scene = null;
		private KeyFrame frame = null;
		public MenuItem(String Name) {	
			LinearGradient gradient = new LinearGradient(0d,1d,1d,0d, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
						new Stop(0, Color.RED),
			            new Stop(0.15, Color.ORANGE),
			            new Stop(0.3, Color.YELLOW),
			            new Stop(0.45, Color.GREEN),
			            new Stop(0.6, Color.BLUE),
			            new Stop(0.75, Color.DARKBLUE),
			            new Stop(0.9, Color.PURPLE),
			            new Stop(1, Color.BLACK)
			});
			
			Rectangle bg = new Rectangle(300,50);
			bg.setOpacity(0.4);
			
			Text optionText = new Text(Name);
			optionText.setFill(Color.web("rgba(204,204,204,1.0)"));
			optionText.setFont(Font.font("Tw Cen MT Condensed", FontWeight.SEMI_BOLD,22));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(bg,optionText);
			
			//On Mouse Over	For Option/Mode Hovering
			setOnMouseEntered(event ->{
				bg.setFill(gradient);
				optionText.setFill(Color.WHITE);
			});
			
			//On Mouse Exit (Return buttons to default state)	
			setOnMouseExited(event ->{
				bg.setFill(Color.BLACK);
				optionText.setFill(Color.web("rgba(204,204,204,1.0)"));
			});
			
			//Change button appearance when they are pressed
			setOnMousePressed(event ->{
				bg.setFill(Color.DARKVIOLET);
			});
			
			setOnMouseReleased(event ->{
				bg.setFill(gradient);
				
				if(Name.equals("INSTRUCTIONS")){
					showPowerUpInfoWindow(); 
				}
				else{
					Game myGame = new Game();
					if(Name.equals("PLAY NORMAL")){
						//POWER UP MODE
						scene = myGame.init(stage,false);
					}
					else if(Name.equals("PLAY HARD")){
						//POWER UP MODE
						scene = myGame.init(stage,true);
					}
					frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
			                e -> myGame.step(SECOND_DELAY));
					stage.setScene(scene);
			        stage.show();
					Timeline animation = new Timeline();
					animation.setCycleCount(Timeline.INDEFINITE);
					animation.getKeyFrames().add(frame);
					animation.play(); 
				}
				
			});		
		}//Closes MenuItem Object
	}//Closes MenuItem Declaration
	

	public Parent setUpWindow() {
		Pane gameWindow = new Pane();
		setUpMap();
		gameWindow.setPrefSize(mainMenuWIDTHOFSCREEN,mainMenuHEIGHTOFSCREEN);
		Image background = new Image(getClass().getClassLoader().getResourceAsStream("MainScreenBackground.jpg")); 
		ImageView backgrondImageMainScreen = new ImageView(background);
		backgrondImageMainScreen.setFitWidth(mainMenuWIDTHOFSCREEN);
		backgrondImageMainScreen.setFitHeight(mainMenuHEIGHTOFSCREEN);;
		gameWindow.getChildren().add(backgrondImageMainScreen); 
		
		BigGameNameText titleText = new BigGameNameText("MONKEY MADNESS");
		titleText.setTranslateX(125);
		titleText.setTranslateY(200);
		gameWindow.getChildren().add(titleText);
		
		OptionContainer optionList = new OptionContainer(
				new MenuItem("PLAY NORMAL"),
				new MenuItem("PLAY HARD"),
				new MenuItem("INSTRUCTIONS"));
		optionList.setTranslateX(200);
		optionList.setTranslateY(350);
		gameWindow.getChildren().add(optionList);
		
		this.infoFullWindow = new Rectangle(MainMenu.mainMenuWIDTHOFSCREEN - marginHorizontalForWindow,MainMenu.mainMenuHEIGHTOFSCREEN - marginVerticalForWindow/2);
		infoFullWindow.setFill(Color.web("#ffffff"));
		infoFullWindow.setX(marginHorizontalForWindow/2);
		infoFullWindow.setY(marginVerticalForWindow/4);
		gameWindow.getChildren().add(infoFullWindow);
		
		setUpBackButton(gameWindow);
		addInDataInfoBlocks(gameWindow);
		clearPowerUpInfoWindow();
		

		return gameWindow;
	}
		
	private void setUpMap(){
		IconToDescriptionMap.put("wb_life.png", "LIFE&Gives you an additional life! Most rare Power Up");
		IconToDescriptionMap.put("wb_speed.png", "SPEED UP&Slows the balloons down --- Can be good or bad, doesn't accelerate\n" + "balloon generation so it prevents accumulation of balloons");
		IconToDescriptionMap.put("wb_slow.png", "SLOW DOWN&Speeds up balloons --- Can be good or bad, doesn't accelerate\n" + "balloon generation so they're easier to dodge but accumulate easily");
		IconToDescriptionMap.put("wb_armor.png", "ARMOR&Temporarily shields your monkey, making it immune to all\n" + "balloons for 2.5 seconds, turns red when about to expire");
		IconToDescriptionMap.put("wb_big.png", "BIG&Makes your monkey bigger, generally bad but you can get \nit to challenge yourself");
		IconToDescriptionMap.put("wb_small.png", "SMALL&Makes your monkey smaller, good! Can only stack up to 3x");
	}
	
	private void addInDataInfoBlocks(Pane gameWindow){
		int iconNumber = 0;
		InfoBlock header = new InfoBlock(gameWindow,0,0,"","","");
		PowerUpInfoWindowStuff.add(header);
		for (String imageURL : IconToDescriptionMap.keySet()) {
			iconNumber++;
			
			String infoTitle = IconToDescriptionMap.get(imageURL).split("&")[0];
			String infoText = IconToDescriptionMap.get(imageURL).split("&")[1];
			InfoBlock currentInfoBlockGenerating;
			if(iconNumber<=3){
				currentInfoBlockGenerating = new InfoBlock(gameWindow,1,iconNumber,imageURL,infoText,infoTitle);
			}
			else{
				currentInfoBlockGenerating = new InfoBlock(gameWindow,2,iconNumber,imageURL,infoText,infoTitle);
			}
		currentInfoBlockGenerating.moveInfoBlock();
		currentInfoBlockGenerating.addInfoBlockToScreen();
		PowerUpInfoWindowStuff.add(currentInfoBlockGenerating);
		}
	}
	
	private void setUpBackButton(Pane gameWindow){
		this.clearInfoWindowButton = new Button("BACK");
		clearInfoWindowButton.setStyle(
				"-fx-background-color: linear-gradient(#f0ff35, #a9ff00),radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);" +
			    "-fx-background-radius: 8,7,6;" + 
			    "-fx-background-insets: 0,1,2;" +
			    "-fx-text-fill: black;" +
			    "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
		);
		clearInfoWindowButton.setPrefSize(BackButtonWidth, BackButtonWidth/2);
		clearInfoWindowButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearPowerUpInfoWindow();
			}
		});
		clearInfoWindowButton.setTranslateX(MainMenu.mainMenuWIDTHOFSCREEN/3 - 305);
		clearInfoWindowButton.setTranslateY(MainMenu.mainMenuHEIGHTOFSCREEN/7 + 5);
		gameWindow.getChildren().add(clearInfoWindowButton);
	}
	
	private static class BigGameNameText extends StackPane{
		public BigGameNameText(String Name){
			Text titleText = new Text(Name);
			titleText.setFont(Font.font("Rockwell", FontWeight.BOLD,60));
			LinearGradient gradient = new LinearGradient(0d,1d,1d,0d, true, CycleMethod.NO_CYCLE, 
					new Stop[]{
							new Stop(0, Color.YELLOW),
				            new Stop(0.15, Color.ORANGE),
				            new Stop(0.3, Color.RED),
				            new Stop(0.45, Color.GREEN),
				            new Stop(0.6, Color.BLUE),
				            new Stop(0.75, Color.DARKBLUE),
				            new Stop(0.9, Color.PURPLE),
				            new Stop(1, Color.BLACK)
			});
			titleText.setFill(gradient);
			getChildren().add(titleText);
		}
	}

	private static class OptionContainer extends VBox{
		public OptionContainer(MenuItem...items) {
			getChildren().add(createline());
			
			for(MenuItem item: items) {
				getChildren().addAll(item,createline());
			}
		}
	
		private Line createline() {
			Line sep = new Line();
			sep.setEndX(300);
			sep.setStroke(Color.DARKGREY);
			return sep;
		}
	}
	
	private static void clearPowerUpInfoWindow(){
		for(InfoBlock infoBox: PowerUpInfoWindowStuff){
			infoBox.hideInfoBlock();
		}
		infoFullWindow.setVisible(false);
		clearInfoWindowButton.setVisible(false);
	}

	private static void showPowerUpInfoWindow(){
		for(InfoBlock infoBox: PowerUpInfoWindowStuff){
			infoBox.showInfoBlock();
		}
		infoFullWindow.setVisible(true);
		clearInfoWindowButton.setVisible(true);
	}
	
	public void display() {
		stage.setTitle("Balloon Dodge");
		stage.setScene(new Scene(setUpWindow()));
	}
	
}//Closes entire class
