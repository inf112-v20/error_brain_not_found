### User stories:

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
If I choose a board-layout, this is the layout that shows when I start the game.

Tasks: 
- Make methods for choosing board
- Make graphics for choosing board

#### Liv på roboter - Kenny lager userstory

#### Damagetoken - Kenny lager userstory


#### See the cards - se hånden til kortene

User-story:
As a player I want to be able to see the cards, so that I can plan my program. 

Acceptance-citeria:
- When I have drawn the cards, the cards are displayed in front of me on the screen.

Tasks:
- Make graphics for showing cards 
- Manual test