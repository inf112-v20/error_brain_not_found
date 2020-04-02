#### User-stories 

#### Belts
User-story:
As player standing on a belt, I want to be moved as many steps as the belt is moving, so that I can get closer to my goal.

Acceptance-criteria:
*   If a player is standing on a belt and the belt is moving, then the player will move.
*   If a player is on a belt, it moves as many steps as the belt does (express or regular)

Tasks:
*   Make a method to tell if you are on a belt. 
*   Let the belt know if it should move or not and how fast it should go
*   Make a method to move any player on the belt

User-story:
As a player standing on a belt, I want to be moved in the same direction the belt is moving, so that I can stay on the belt.

Acceptance-criteria:
*   If a player is standing on the corner and the belt is moving, then the player changes direction with the belt.

Task:
*   Make a method to tell if player is on the corner of the belt and what direction it 
should have

#### See the cards
User-story:
As a player I want to be able to see the cards, so that I can plan my program. 

Acceptance-citeria:
*   When I have drawn the cards, the cards are displayed in front of me on the screen.

Tasks:
*   Make graphics for showing cards 
*   Manual test

#### Game loop
User-story:
As a player I want the game to go in a loop, so that events happen in a specific order. 

Acceptance-criteria:
When the game starts, it goes through the fases and the rounds. 

Tasks:
*   Make loop for the game. 
*   Test that events happen in right order
*   Manual tests  

#### Mute button
User-story:
As a user I want to be able to mute the game, so I can choose to have sound or not.

Acceptance-criteria:
When I hit "m", there is no sound from the game.

Tasks:
*   Make method for muting the game. 

#### Main criteria for MVP
- [x] The game should be able to run, show a board with starting robot and programcards to this robot.  
- [ ] The player is able to pick programcards and place them so that the robot can move.  
- [ ] The game should be able to do a round, and do the phases in the correct order in that round.  
- [x] The robot should be able to win by going to the flags in a specific order.   
- [x] The robot is stopped by walls, pushed by other players and respawned when outside board/on hole.   
- [ ] The robot should be able to do a powerdown.  
- [x] The laser can shoot the robots and the robots take damage.   
- [ ] The belts move so the player can be moved by belts. 
- [ ] Create AI so player can play with other robots 


## Task 3 - Productdelivery and codequality
### Manual tests
Game loop:  
*   When the game has started press space once.
*   Then all robots will use one programcard in turn, 5 times. (5 rounds) 
*   Press space again after all robots have moved and the robots will do five more rounds.   

Laser:  
*   Press on of the keyboard-arrows and the lasers will be activated. 
*   Stear your player in front of a laser. The laser should be stopped by a player.   

Menu-screen:  
*   Aa screen with a roll-down menu and start and exit buttons should show.   
*   Choose your board by choosing from the roll-down menu then click start. The board should show with players on it.

Mute-button:
* Press the "m" button. The sound should be muted. 