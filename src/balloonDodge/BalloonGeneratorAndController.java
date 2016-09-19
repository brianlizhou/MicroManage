package balloonDodge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class BalloonGeneratorAndController extends StackPane{
		private ArrayList<Balloon> balloonsThatWillBeSpawnedThisLevel = new ArrayList<Balloon>();
		private Queue<Balloon> visibleBalloons = new LinkedList<Balloon>();
		private ArrayList<Balloon> visiblePowerUpBalloons = new ArrayList<Balloon>();
		private HashMap<Balloon,Circle> balloonToHitboxMap = new HashMap<Balloon,Circle>(); 
		private HashMap<Balloon,Integer> balloonToRowNumberMap = new HashMap<Balloon,Integer>();
		private Balloon balloonAboutToBeRemoved = null;
		private int currentLevelOfGame;	
		private boolean generatedOnFirstHalf = true;
		
		public BalloonGeneratorAndController(int currentLevelOfGame,Group rootElement,int levelOneNumberOfBalloons){
			this.currentLevelOfGame = currentLevelOfGame;
			preLoadBalloons(rootElement,levelOneNumberOfBalloons);
		}
		
		/**
		 * @return how many balloons that will be spawned, or number of pregenerated balloons
		 */
		public ArrayList<Balloon> returnBalloonsThatWillBeSpawnedThisLevel(){
			return this.balloonsThatWillBeSpawnedThisLevel;
		}
		
		/**
		 * @return the queue of visible, NON-powerup balloons
		 */
		public Queue<Balloon> returnVisibleBalloons(){
			return this.visibleBalloons;
		}
		
		/**
		 * @return the Arraylist of all visible powerup balloons on the screen
		 */
		public ArrayList<Balloon> returnVisiblePowerUpBalloons(){
			return this.visiblePowerUpBalloons;
		}
		
		/**
		 * @return the map connecting each balloon to its hitbox
		 */
		public HashMap<Balloon,Circle> returnBalloonToHitboxMap(){
			return this.balloonToHitboxMap;
		}
		
		/**
		 * @return the map connecting the balloon to the row number that it will be spawned in
		 */
		public HashMap<Balloon,Integer> returnBalloonToRowNumberMap(){
			return this.balloonToRowNumberMap;
		}
		
		/**
		 * Starts moving the pre-generated balloon that is pre-generated off-screen and puts it 
		 * on the visible screen
		 */
		public void activatePreGeneratedBalloon(Group rootElement,int numberOfBalloonsUntilNextLevel){
			addBalloonToDataStructureAndAlign(0, balloonsThatWillBeSpawnedThisLevel.get(numberOfBalloonsUntilNextLevel - 1), false);
		}
		
		/**
		 * Generates a new T1 Powerup Balloon, SpeedUp, Slowdown, Shrink, or Enlarge
		 */
		public void generateCommonPowerupBalloon(Group rootElement){
			int whichPowerCommonPowerUp = Game.randomNumberGenerator.nextInt(4);
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,String.valueOf(whichPowerCommonPowerUp)),true);
		}
		
		/**	
		 * Generates a new T2 Powerup Balloon - Armor
		 */
		public void generateArmorBalloon(Group rootElement){
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,"Armor"),true);
		}
		
		/**	
		 * Generates a new T3 Powerup Balloon - Life
		 */
		public void generateLifeBalloon(Group rootElement){
			addBalloonToDataStructureAndAlign(generateRandomRowNumber(), new Balloon(currentLevelOfGame,rootElement,"Life"),true);
		}
		
		/** 
		 * Clears all balloons on the visible screen, both normal and powerup
		 */
		public void clearAllBalloons(Group rootElement){
	    	boolean areAllNormalBalloonsCleared = areAllNormalBalloonsCleared();
	    	int howManyPowerUpBalloonsToClear = visiblePowerUpBalloons.size();
	    	
	    	/*The data structure separation between normal and powerup is important (queue and separate ArrayList
	    	simply because when a balloon goes off screen, we can just remove it from the queue but for PowerUps, they're special
	    	because if we hit one mid-screen, we delete it (normal balloon you die) and if there are more than one, we 
	    	need to know WHICH one got hit, thus the arraylist*/
	    	while(!areAllNormalBalloonsCleared){
	    		clearNormalBalloon(rootElement);
	    		areAllNormalBalloonsCleared = areAllNormalBalloonsCleared();
	    	}
		    for(int balloonNum = 0; balloonNum<howManyPowerUpBalloonsToClear;balloonNum++){
				clearPowerUpBalloon(0,visiblePowerUpBalloons.get(0),rootElement);
		    }
		 }
		 
		/** 
		 * Returns if all normal balloons have been cleared / no visible balloons left
		 */	
		public boolean areAllNormalBalloonsCleared(){
			return (visibleBalloons.isEmpty());
		}
		
		/** 
		 * Returns if all powerup balloons have been cleared / no visible powerup balloons left
		 */	
		public boolean areAllPowerUpBalloonsCleared(){
			return (visiblePowerUpBalloons.isEmpty());
		}
		
		/**
		 * Method increases level of game, clears the data structures/maps, and pre-loads balloons for next level
		 * @param numberOfBalloonsUntilNextLevelForFixed - sets number of balloons to be spawned for next leve
		 */
		public void increaseLevelOfGame(int numberOfBalloonsUntilNextLevelForFixed,Group rootElement){
			currentLevelOfGame++;
			balloonsThatWillBeSpawnedThisLevel = new ArrayList<Balloon>();
			balloonToHitboxMap = new HashMap<Balloon,Circle>();
			balloonToRowNumberMap = new HashMap<Balloon,Integer>();
			preLoadBalloons(rootElement,numberOfBalloonsUntilNextLevelForFixed);
		}
		
		/**
		 * The heart of the game, this method moves all balloons and gives this class its name as it controls all balloons and checks
		 * if you've died by seeing if the monkey has intersected with any balloon -> also processes powerup balloons that you hit
		 * @param isHardMode - Tells us if we need to move laterally too
		 * @param monkeyImage - Gives us the monkey to move
		 * @param armorIsUp - Tells us if armor is up
		 * @return - Tells Game if you died or not
		 */
		public boolean moveAllBalloonsUpwards(boolean isHardMode,Monkey monkeyImage, boolean armorIsUp, Group rootElement) {
			for(Balloon balloon:visibleBalloons){
				if(isHardMode){
					balloon.moveBalloonUpwardsHARDMODE();
				}
				else{
					balloon.moveBalloonUpwardsNORMALMODE();
				}
				if(monkeyImage.returnMonkeyHitbox().getBoundsInParent().intersects(balloonToHitboxMap.get(balloon).getBoundsInParent()) && !armorIsUp) {
					return true;
				}
				balloon.setRotate();
			}
			
			for(int balloonIndex = 0; balloonIndex<visiblePowerUpBalloons.size();balloonIndex++){
				Balloon visiblePowerUpBalloon = visiblePowerUpBalloons.get(balloonIndex);
				visiblePowerUpBalloon.moveBalloonUpwardsNORMALMODE();
				if(monkeyImage.returnMonkeyHitbox().getBoundsInParent().intersects(balloonToHitboxMap.get(visiblePowerUpBalloon).getBoundsInParent())) {
					Game.implementPowerUpBalloonEffect(visiblePowerUpBalloon);
					clearPowerUpBalloon(balloonIndex,visiblePowerUpBalloon,rootElement);	
				}
			}
			return false;
		}
					
		private void preLoadBalloons(Group rootElement, int numberOfBalloonsToGenerate){
			for (int numBalloonsPreGenerated=0;numBalloonsPreGenerated<numberOfBalloonsToGenerate;numBalloonsPreGenerated++){
				Balloon newBalloon = new Balloon(currentLevelOfGame,rootElement,"Normal");
				balloonsThatWillBeSpawnedThisLevel.add(newBalloon);
				balloonToHitboxMap.put(newBalloon, newBalloon.returnBalloonHitbox());
				balloonToRowNumberMap.put(newBalloon, generateRandomRowNumber());
			}
		}
		
	    private int generateRandomRowNumber(){
	    	//Game alternates balloon generation, generates on first 4 rows then back 4 rows
	    	int rowToGenerateBalloon = Game.randomNumberGenerator.nextInt(4);
	    	//First Half
	    	if(generatedOnFirstHalf){
	    		generatedOnFirstHalf = false;
	    		return rowToGenerateBalloon;
	    	}
	    	//Second Half
	    	generatedOnFirstHalf = true;
	    	return ((Game.numBalloonSpawnPoints-1) - rowToGenerateBalloon); 
	    }	
		
		private void addBalloonToDataStructureAndAlign(int rowNumber, Balloon newBalloon, boolean isPowerUp){
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

		private void removePowerUpBalloon(Balloon visiblePowerUpBalloon,int balloonIndex){
			balloonAboutToBeRemoved = visiblePowerUpBalloon;
			visiblePowerUpBalloons.remove(balloonIndex);
		}
		
		private Balloon deleteNormalBalloonFromQueue(){
			balloonAboutToBeRemoved = visibleBalloons.peek();
			visibleBalloons.remove();	
			return balloonAboutToBeRemoved;
		}
		
		private Circle deleteBalloonHitbox(){
			return balloonToHitboxMap.get(balloonAboutToBeRemoved);
		}
		
		private void clearNormalBalloon(Group rootElement){
	    	Balloon balloonToBeRemoved = deleteNormalBalloonFromQueue();
	    	Circle balloonHitboxToBeRemoved = deleteBalloonHitbox();
	    	rootElement.getChildren().remove(balloonToBeRemoved.returnBalloonImage());
	  	  	rootElement.getChildren().remove(balloonHitboxToBeRemoved);
	  	  	balloonToBeRemoved.freeBalloonSpace();
	  	  	balloonToBeRemoved = null;
		}
		
		private void clearPowerUpBalloon(int balloonIndex,Balloon powerUpBalloonToBeCleared,Group rootElement){
	    	removePowerUpBalloon(powerUpBalloonToBeCleared,balloonIndex);
	    	Circle balloonHitboxToBeRemoved = deleteBalloonHitbox();
	    	rootElement.getChildren().remove(powerUpBalloonToBeCleared.returnBalloonImage());
	  	  	rootElement.getChildren().remove(balloonHitboxToBeRemoved);
	  	    powerUpBalloonToBeCleared.freeBalloonSpace();
	  	  	powerUpBalloonToBeCleared = null;
		 }
}
