# Mandatory Assignment 3 
Team: error_no_brain_found

## Task 1 - Team and project
### Meeting minutes

[Meeting 8](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-8)


## Task 2 - Requirements
### User-stories 

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

#### Make a program  
User-story:  
As a user I want to be able to arrange the cards, so that I can make a program for my robot.  

Acceptance-criteria:  
*   If I rearrange the cards, the cards are played in this order.  

Tasks:  
*   Create a method that takes input from screen and update how the cards are arranged in player.  
*   Create a function that "lock" the cards so the user can tell the game the program to be made is done.  
*   Create a method that lets the user rearrange the cards given.  

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


#### Preferences  
User-story:  
As a user I want to be able to control the game volume, so I can adjust the volum to a comfortable level.  

Acceptance-criteria:
*   When I turn down volume, the sound goes down.  
*   When I turn up volume, the sound goes up. 

Tasks:  
*   Create method for turning up/down volume. 
*   Manuel tests.  


#### AI 
User-story:  
As a user I want to be able to play against the machine, so that I can play the game alone.  

Acceptance-criteria:  
*   When I play the game the computer plays the other robot(s).  

Tasks:  
*   Create an AI that represent a player or more players. Need to be able to powerdown and make a program.  


#### LAN  
User-story:  
As a user I want to be able to connect to other players, so we can play together.  

Acceptance-criteria:  
*   If I am the only one playing on the LAN, I will be the server in the game-session.  
*   If there already are players in the game I will be a client in the game-session.   
*   If I connect to another, then we are in the same game.  

Tasks:  
*   Create a method that tells if you should be host or client in the game.  
*   If server: Create a class that creates a new game and wait for other players using sockets  
*   If client: Create a new player and send this to server using sockets.
*   Send input/output to/from server and the clients.

#### Power down 
User-story:  
As a player I want to power down, so I can take away my damage.

Acceptance-criteria:  
*   If I am in power down I will clear my damage points.  
*   If I am in power down I can not move.   
*   If I am in power down I do not fire lasers.  

Tasks:  
*   Create a boolean that tells if a player is in power down mode.  
*   Create a button so that a player can announce their power down.  


#### Robot fire lasers  
User-story:  
As a player I want to be able to shoot lasers, so that other players can take damage.  

Acceptance-criteria:  
*   If I shoot another player the player will take damage.  

Tasks:  
*   Create a function in player that activates shooting.  


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
*   Start a turn and wait for all players to move. Then the playerLasers should fire. Then the lasers from wall should fire.
*   Stear your player in front of a laser. The laser should be stopped by a player.   

Menu-screen:  
*   Aa screen with a roll-down menu and start and exit buttons should show.   
*   Choose your board by choosing from the roll-down menu then click start. The board should show with players on it.

Showing cards:  
*   Nine cards should be displayed to you when you start the game.

Making a program:  
*   When you see your nine cards, click on the cards you want to select. Your program is in the order you click the cards. When you have made a program (5 cards), the checkbutton will turn green.

Mute-button:
* Press the "m" button. The sound should be muted. 