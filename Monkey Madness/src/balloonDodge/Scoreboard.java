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
    
    private Text scoopsLeftNumber = null;
    private Text liveCountNumber = null;
    
	public Scoreboard(Group rootElement){
		Rectangle scoreboard = new Rectangle(Game.gameWIDTHOFSCREEN,scoreBoardHeight);
		scoreboard.setY(Game.gameHEIGHTOFSCREEN - scoreBoardHeight);
		scoreboard.setX(0);
		scoreboard.setFill(Color.web(scoreBoardColor));
		rootElement.getChildren().add(scoreboard);
			
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
	
	public void updateLifeCountText(){
		this.liveCountNumber.setText(String.valueOf(Game.livesLeft));
	}
	
	public void updateBalloonsLeftText(int balloonsGoneBy){
		this.scoopsLeftNumber.setText(String.valueOf(balloonsGoneBy) + "/" + Game.numberOfBalloonsUntilNextLevelForFixed);
		
	}
}
