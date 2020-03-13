### User stories:

#### Holes 

User story:  
As a player I want to respawn if I end up on a hole, so that I can continue the game from backup.

Acceptance-criteria:  
-   If I go on a tile where there is a hole I want to be respawned at my backup.  

Tasks:  
-   Make a method that tells if you are on a hole     
-   Test that you are respawned when on a hole   

#### Laser

User-story:
As a player I want the laser to be able to hit me, so that I take damage.

Acceptance-criteria: 
- If a laser is activated and player is standing in its shooting line, it gives player damage.
- If a laser is activated and player is not standing in its shooting line, it does not give player damage.

Tasks:
- Make a method that tells if a laser is hitting a player 
- Make a method that makes the player take damage 
- Make tests for when laser is hitting player


User-story:
As a player I want the laser not to hit me when it is blocked by objects on the board, so that I do not take damage.

Acceptance-criteria: 
If a laser is activated and player is standing in its shooting line but is blocked, then player does not get damage.

Tasks:
- Make a method to tell if a laser is blocked, or make laser object know how far it can go by checking board.
- Test if the range of the specific laser is correct.
- Test that a player is not damaged when out of range of laser. 


#### Belts

User-story:
As player standing on a belt, I want to be moved as many steps as the belt is moving, so that I can get closer to my goal.

Acceptance-criteria:
- If a player is standing on a belt and the belt is moving, then the player will move.
- If a player is on a belt, it moves as many steps as the belt does (express or regular)

Tasks:
- Make a method to tell if you are on a belt. 
- Let the belt know if it should move or not and how fast it should go
- Make a method to move any player on the belt


User-story:
As a player standing on a belt, I want to be moved in the same direction the belt is moving, so that I can stay on the belt.

Acceptance-criteria:
- If a player is standing on the corner and the belt is moving, then the player changes direction with the belt.

Task:
- Make a method to tell if player is on the corner of the belt and what direction it 
should have


#### RotatePads

User-story:
As a user I want to get turned if I walk on a rotate pad

Acceptance-criteria:
when I move to a tile with a rotate-wheel I want to be turned in the direction of the rotate pad

Tasks:
- make a method that tells if player i on rotate pad and then rotate player 
- test that player is rotated in correct direction

#### MenuScreen - Skal kunne velge brett

User-Story:
As a user of the game I want to be able to choose different board layouts, so that I can have a new challenge in the game.

Acceptance-criteria:
If I choose a board-layout, this is the layout that shows when I start the game

Tasks:
- Make methods for choosing board
- Make graphics for choosing board

#### Liv på roboter

User-Story:
As a player I want my robot to be able to take damage, so that my robot can die

Acceptance-criteria:
If a laser hits me, I want to recieve damage

Tasks:
Make methods for robots to take damage

#### Damagetoken

User-Story:
As a player I want my robot to have a Token-bar, to see when or if my robot gets hurt

Acceptance-criteria:
- When i get 10 damage tokens, I want my robot to die and respawn
- When i get 5 or more damage tokens, then the program cards will 'lock up' starting from register 5
- When i get x damage tokens, I will get x less program cards

Tasks:
- Make a GUI for visible damage tokens for the robots
- Make methods to recieve damage tokens
- Make methods to recieve less programs cards based on damage tokens

#### Lifetoken

User-Story: 
As a player I want to have Life tokens, to see how many lives I have left

Acceptance-criteria:
- If my robot gets 10 damage tokens, I lose a life token
- If my life tokens are depleted, then im permanently out of the game

Tasks:
- Make a GUI to visualize life tokens
- Make a method that removes life tokens when robots die.    
- Make a method that robots do not respawn after losing all life tokens


#### See the cards - se hånden til kortene

User-story:
As a player I want to be able to see the cards, so that I can plan my program. 

Acceptance-citeria:
- When I have drawn the cards, the cards are displayed in front of me on the screen.

Tasks:
- Make graphics for showing cards 
- Manual test