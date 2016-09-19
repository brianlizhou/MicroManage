package balloonDodge;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Scoreboard {
    public static final int scoreBoardHeight = 50;
    public static final int scoreBoardTextFont = 12;
    public static final String scoreBoardFont = "Verdana";
    public static final String scoreBoardColor = "#dfbf9f";
    
    private double textAlignmentY =  Game.gameHEIGHTOFSCREEN - (scoreBoardHeight * 0.4);
    private double textLivesAlignmentX = Game.gameWIDTHOFSCREEN/5;
    private double textScoopAlignmentX = Game.gameWIDTHOFSCREEN/2;
    
    private Text scoopsLeftNumber;
    private Text liveCountNumber;
    private Group rootElement;
    
	public Scoreboard(Group rootElement){
		this.rootElement = rootElement;
		
		setUpBackgroundForScoreboard();	
		setUpText();
	}
	
	/**
	 * Increases visual indicator of how many lives are left
	 */
	public void updateLifeCountText(){
		this.liveCountNumber.setText(String.valueOf(Game.livesLeft));
	}
	
	/**
	 * Updates amount of balloons left for the level
	 * @param balloonsGoneBy - how many balloons have gone by so far
	 */
	public void updateBalloonsLeftText(int balloonsGoneBy){
		this.scoopsLeftNumber.setText(String.valueOf(balloonsGoneBy) + "/" + Game.numberOfBalloonsUntilNextLevelForFixed);
		
	}
	
	private void setUpBackgroundForScoreboard(){
		Rectangle scoreboard = new Rectangle(Game.gameWIDTHOFSCREEN,scoreBoardHeight);
		scoreboard.setY(Game.gameHEIGHTOFSCREEN - scoreBoardHeight);
		scoreboard.setX(0);
		scoreboard.setFill(Color.web(scoreBoardColor));
		rootElement.getChildren().add(scoreboard);
	}
	
	private void setUpText(){
		Text liveCount = new Text(textLivesAlignmentX,textAlignmentY,"LIVES:");
		liveCount.setFont(Font.font(scoreBoardFont, FontWeight.BOLD, scoreBoardTextFont));
		rootElement.getChildren().add(liveCount);
		
		Text scoopsLeft = new Text(textScoopAlignmentX, textAlignmentY, "BALLOONS:");
		scoopsLeft.setFont(Font.font(scoreBoardFont, FontWeight.BOLD, scoreBoardTextFont));
		rootElement.getChildren().add(scoopsLeft);
		
		liveCountNumber = new Text(textLivesAlignmentX + 70,textAlignmentY,String.valueOf(Game.livesLeft));
		liveCount.setFont(Font.font(scoreBoardFont, FontWeight.BOLD, scoreBoardTextFont + 5));
		rootElement.getChildren().add(liveCountNumber);
		
		scoopsLeftNumber = new Text(textScoopAlignmentX + 120, textAlignmentY, ("0/" + Game.numberOfBalloonsUntilNextLevelForFixed));
		scoopsLeft.setFont(Font.font(scoreBoardFont, FontWeight.BOLD, scoreBoardTextFont + 5));
		rootElement.getChildren().add(scoopsLeftNumber);
	}
}
