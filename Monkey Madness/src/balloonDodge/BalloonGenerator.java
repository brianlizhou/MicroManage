package balloonDodge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class BalloonGenerator extends StackPane{
		public static ArrayList<Balloon> balloonsThatWillBeSpawnedThisLevel = new ArrayList<Balloon>();
		//Need this array to prevent Concurrent Modification Exception
		public static Queue<Balloon> visibleBalloons = new LinkedList<Balloon>();
		public static ArrayList<Balloon> visiblePowerUpBalloons = new ArrayList<Balloon>();
		public static HashMap<Balloon,Circle> balloonToHitboxMap = new HashMap<Balloon,Circle>(); 
		public static HashMap<Balloon,Integer> balloonToRowNumberMap = new HashMap<Balloon,Integer>();
		private Balloon balloonAboutToBeRemoved = null;
		private int currentLevelOfGame;	
		private boolean needToClearABalloon = false;
		private int balloonIndexHolder = -1;
		private Balloon balloonToClear = null;
		private boolean generatedOnFirstHalf = true;
				
		public BalloonGenerator(int currentLevelOfGame,Group rootElement,int levelOneNumberOfBalloons){
			this.currentLevelOfGame = currentLevelOfGame;
			createAndAddBalloonsToArrayList(rootElement,levelOneNumberOfBalloons);
		}
		
		private void createAndAddBalloonsToArrayList(Group rootElement, int numberOfBalloonsToGenerate){
			for (int numBalloonsPreGenerated=0;numBalloonsPreGenerated<numberOfBalloonsToGenerate;numBalloonsPreGenerated++){
				Balloon newBalloon = new Balloon(currentLevelOfGame,rootElement,"Normal");
				balloonsThatWillBeSpawnedThisLevel.add(newBalloon);
				balloonToHitboxMap.put(newBalloon, newBalloon.returnBalloonHitbox());
				balloonToRowNumberMap.put(newBalloon, generateRandomRowNumber());
			}
		}
		
	    private int generateRandomRowNumber(){
	    	//CANNOT generate 8, only 7 rows but need an equal chance of , so need to subtract super small number to remove that possibility	
	    	int rowToGenerateBalloon = Game.randomNumberGenerator.nextInt(4);
	    	//First Half
	    	if(generatedOnFirstHalf){
	    		generatedOnFirstHalf = false;
	    		return rowToGenerateBalloon;
	    	}
	    	//Second Half
	    	generatedOnFirstHalf = true;
	    	return (7 - rowToGenerateBalloon); 
	    }
		
		public void activatePreGeneratedBalloon(Group rootElement,int numberOfBalloonsUntilNextLevel){
			addBalloonToDataStructureAndAlign(0, balloonsThatWillBeSpawnedThisLevel.get(numberOfBalloonsUntilNextLevel - 1), false);
		}
		
		public void generateCommonPowerupBalloon(Group rootElement){
			int whichPowerCommonPowerUp = Game.randomNumberGenerator.nextInt(4);
			String type = null;
			if(whichPowerCommonPowerUp == 0){
				type = "SpeedUp";
			}
			else if(whichPowerCommonPowerUp == 1){
				type = "SlowDown";
			}
			else if(whichPowerCommonPowerUp == 2){
				type = "Big";
			}
			else if(whichPowerCommonPowerUp == 3){
				type = "Small";
			}
			
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,type),true);
		}
		
		
		public void generateArmorBalloon(Group rootElement){
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,"Armor"),true);
		}
		
		public void generateLifeBalloon(Group rootElement){
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,"Life"),true);
		}
		
		public void addBalloonToDataStructureAndAlign(int rowNumber, Balloon newBalloon, boolean isPowerUp){
			if(!newBalloon.typeOfBalloon.equals("Normal")){
				newBalloon.setXLocation((Balloon.balloonWidth)*(rowNumber)-10);
			}
			else{
				newBalloon.setXLocation((Balloon.balloonWidth)*(balloonToRowNumberMap.get(newBalloon))-10);	
			}
			
			if(isPowerUp){
				visiblePowerUpBalloons.add(newBalloon);
				balloonToHitboxMap.put(newBalloon, newBalloon.returnBalloonHitbox());
			}
			else{
				visibleBalloons.add(newBalloon);
			}
		}

		
		public void removePowerUpBalloon(Balloon visiblePowerUpBalloon,int balloonIndex){
			balloonAboutToBeRemoved = visiblePowerUpBalloon;
			visiblePowerUpBalloons.remove(balloonIndex);
		}
		
		public void increaseLevelOfGame(int numberOfBalloonsUntilNextLevelForFixed,Group rootElement){
			currentLevelOfGame++;
			balloonsThatWillBeSpawnedThisLevel = new ArrayList<Balloon>();
			balloonToHitboxMap = new HashMap<Balloon,Circle>();
			balloonToRowNumberMap = new HashMap<Balloon,Integer>();
			createAndAddBalloonsToArrayList(rootElement,numberOfBalloonsUntilNextLevelForFixed);
		}
		
		public Balloon deleteNormalBalloonFromQueue(){
			balloonAboutToBeRemoved = visibleBalloons.peek();
			visibleBalloons.poll();	
			return balloonAboutToBeRemoved;
		}
		
		public Circle deleteBalloonHitbox(){
			return balloonToHitboxMap.get(balloonAboutToBeRemoved);
		}
		
		public boolean areAllNormalBalloonsCleared(){
			return (visibleBalloons.isEmpty());
		}
		public boolean areAllPowerUpBalloonsCleared(){
			return (visiblePowerUpBalloons.isEmpty());
		}
		
		public void clearNormalBalloon(Group rootElement){
	    	Balloon balloonToBeRemoved = deleteNormalBalloonFromQueue();
	    	Circle balloonHitboxToBeRemoved = deleteBalloonHitbox();
	    	rootElement.getChildren().remove(balloonToBeRemoved.returnBalloonImage());
	  	  	rootElement.getChildren().remove(balloonHitboxToBeRemoved);
	  	  	balloonToBeRemoved.freeBalloonSpace();
	  	  	balloonToBeRemoved = null;
	  	  	System.gc();
		}
		
		public void clearPowerUpBalloon(int balloonIndex,Balloon powerUpBalloonToBeCleared,Group rootElement){
	    	removePowerUpBalloon(powerUpBalloonToBeCleared,balloonIndex);
	    	Circle balloonHitboxToBeRemoved = deleteBalloonHitbox();
	    	rootElement.getChildren().remove(powerUpBalloonToBeCleared.returnBalloonImage());
	  	  	rootElement.getChildren().remove(balloonHitboxToBeRemoved);
	  	    powerUpBalloonToBeCleared.freeBalloonSpace();
	  	  	powerUpBalloonToBeCleared = null;
	  	  	balloonToClear = null;
			balloonIndexHolder  = -1;
	  	  	System.gc();
		 }
		
		 public void clearAllBalloons(Group rootElement){
	    	boolean areAllNormalBalloonsCleared = areAllNormalBalloonsCleared();
	    	while(!areAllNormalBalloonsCleared){
	    		clearNormalBalloon(rootElement);
	    		areAllNormalBalloonsCleared = areAllNormalBalloonsCleared();
	    	}
	    	//boolean areAllPowerUpBalloonsCleared = balloonGeneratorAndController.areAllPowerUpBalloonsCleared();
	    	int howManyPowerUpBalloonsToClear = BalloonGenerator.visiblePowerUpBalloons.size();
		    for(int balloonNum = 0; balloonNum<howManyPowerUpBalloonsToClear;balloonNum++){
				Balloon visiblePowerUpBalloon = BalloonGenerator.visiblePowerUpBalloons.get(0);
				clearPowerUpBalloon(0,visiblePowerUpBalloon,rootElement);
		    }
		 }


		public void moveAllBalloonsUpwards(boolean isHardMode,Monkey monkeyImage, boolean armorIsUp, Group rootElement) {
			for(Balloon balloon:visibleBalloons){
				if(isHardMode){
					balloon.moveBalloonUpwardsHARDMODE();
				}
				else{
					balloon.moveBalloonUpwardsNORMALMODE();
				}
				if(monkeyImage.returnMonkeyHitbox().getBoundsInParent().intersects(BalloonGenerator.balloonToHitboxMap.get(balloon).getBoundsInParent()) && !armorIsUp) {
					Game.amIHitByABalloon = true;
					return;
				}
				balloon.setRotate();
				if(balloon.haveIReachedTopOfScreen()){
					balloonAboutToBeRemoved = visibleBalloons.peek();
					needToClearABalloon = true;
					
				}
			}
			
			for(int balloonIndex = 0; balloonIndex<visiblePowerUpBalloons.size();balloonIndex++){
				Balloon visiblePowerUpBalloon = visiblePowerUpBalloons.get(balloonIndex);
				if(monkeyImage.returnMonkeyHitbox().getBoundsInParent().intersects(balloonToHitboxMap.get(visiblePowerUpBalloon).getBoundsInParent())) {
					Game.implementPowerUpBalloonEffect(visiblePowerUpBalloon);
					balloonIndexHolder = balloonIndex;
					balloonToClear = visiblePowerUpBalloon;
				}
				else if(visiblePowerUpBalloon.haveIReachedTopOfScreen()){
					balloonIndexHolder = 0;
					balloonToClear = visiblePowerUpBalloon;
				}
				visiblePowerUpBalloon.moveBalloonUpwardsNORMALMODE();
			}
			
			clearBalloonIfReachedEndOfScreen(balloonIndexHolder, rootElement, balloonToClear);
			
		}
		
		private void clearBalloonIfReachedEndOfScreen(int balloonIndexHolder, Group rootElement, Balloon balloonToClear){
			if(needToClearABalloon){
				clearNormalBalloon(rootElement);
				needToClearABalloon = false;
			}
			if((balloonIndexHolder != -1) && (balloonToClear != null)){
				clearPowerUpBalloon(balloonIndexHolder,balloonToClear,rootElement);	
			}
		}
}

	
