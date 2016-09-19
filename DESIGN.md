Design Implementations
===================
#### General mechanics
If you want to increase the number of balloons spawned and/or the rate, you change the steps until balloon spawn and then just call the
balloonGenerator.generateBalloon() whenever you want to generate a balloon
As for the speed, the speed for the balloons is within the Balloon class at the bottom.
The moveAllBalloonsUpwardsNORMAL and HARD should cover all of the moving incase you want the balloons to stop moving, move backwards, or anything
else. That class just iterates through all visible balloons and moves them up X amount of units depending on speed.

#### Adding Powerups
If you want to add a powerup, look for "type" within the Balloon class and the Game class, those two will turn a balloon into a special
type of powerup balloon, and when you touch it add to the class implementPowerUpBalloon in the Game class to change the current game state
with a certain parameter. Be sure to add the step count of how often is should spawn (if you look in the Game class, those steps already exist 
the powerups, just add a new one depending on how often you want it to spawn) and then generatePowerUpBalloon to make it

#### If you're adding a new menu at setup, look for the setUpWizardandTextController class to create a new menu, and gameover class if you want to create a new menu
at the end screen class.

#### New Features
If you want to add something completely new, if you just think about what it affects logically, I separated most of the various objects into their own classes
so you would need to go into the classes for everything it touches;
E.G. If you want to make the monkey dance or something, then you probably need to go into the Monkey class to change the hitboxes dynamically and then make sure the 
Game class also checks for intersections correctly within the check for intersections method (can find it under step, within the mouse move listener)
