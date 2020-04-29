# Mandatory Assignment 3 
Team: error_no_brain_found

## Task 1 - Team and project
### Meeting minutes

[Meeting 8](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-8)  
[Meeting 9](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-9)
[Meeting 10](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-10)  
[Meeting 11](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-11)  
[Meeting 12](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-12)

### Roles
The roles in the team is fine. The team leader have the over all control over the team. He is making sure everyone
contributes, and does what they are suppose to in the given time frame. For the rest of the team, people still have their
responsibility area. Since we are in the last sprint of the project the lines between the roles are a bit wiped out and 
people are helping where it is needed to get finished. 
 
For more depth information on the roles see [ObligatoriskOppgave2](https://github.com/inf112-v20/error_brain_not_found/blob/develop/Deliverables/Obligatorisk%20Oppgave2/ObligatoriskOppgave2.md),
under `Roles`.

### Experiences with the team or project-methods
We are getting better at using issues, tags in issues and the projectboard. When a task needs to be done, 
we make user stories, acceptance criteria's and tasks. This is copied into the issue, and the tasks are made as 
check boxes so that we can see progress in the issue. Also an own issue for tests for this task is made and is tagging the issue 
it should be testing, with acceptance criteria as tests to be made. This gives us a great overview of who is doing what, 
and a great workflow because we can always see what to do next.

### Retrospective from the project

### Project board
![Project board form github]()

### Group dynamic and communication

## Task 2 - Requirements
### User-stories 

#### Belts
User-story:
As player standing on a belt, I want to be moved as many steps as the belt is moving, so that I can get closer to my goal.

Acceptance-criteria:
*   If a player is standing on a belt, and the belt is moving, then the player will move.
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
*   When I have drawn the cards, cards are displayed in front of me on the screen.

Tasks:
*   Make graphics for showing cards 
*   Manual test

#### Make a program  
User-story:  
As a user I want to be able to arrange the cards, so that I can make a program for my robot.  

Acceptance-criteria:  
*   If I rearrange the cards, the cards are played in this order.  

Tasks:  
*   Create a method that takes input from screen and update how the cards are arranged in the player.  
*   Create a function that "lock" the cards, so the user can tell the game the program to be made is done.  
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
- [x] The player is able to pick programcards and place them so that the robot can move.  
- [x] The game should be able to do a round, and do the phases in the correct order in that round.  
- [x] The robot should be able to win by going to the flags in a specific order.   
- [x] The robot is stopped by walls, pushed by other players and respawned when outside board/on hole.   
- [ ] The robot should be able to do a powerdown.  
- [x] The laser can shoot the robots and the robots take damage.   
- [ ] The belts move so the player can be moved by belts. 
- [ ] Create AI/LAN so player can play with other robots 


## Task 3 - Productdelivery and codequality
### Manual tests
#### Game loop
*   When the game has started choose you cards and press the confirm button when it has turned green.

*   If you are in LAN you need to wait for other players to confirm their cards. When all have confirmed, the game should 
start the turn.

*   Then all robots will use one programcard in turn, 5 times. (5 rounds) 

*   Choose cards again and press comfirm, the game will start the second turn.  

#### Laser (fire)  
*   Start a turn and wait for all players to move. Then the playerLasers should fire. Then the lasers from wall should fire.

#### Laser (blocking)
*   Stear your player in front of a laser. The laser should be stopped by a player.   

#### Menu-screen  
*   Aa screen with a roll-down menu and start and exit buttons should show.   
*   Choose your board by choosing from the roll-down menu then click start. The board should show with players on it.

#### Showing cards
*   Nine cards should be displayed to you when you start the game.

#### Making a program  
*   When you see your nine cards, click on the cards you want to select. Your program is in the order you click the cards. When you have made a program (5 cards), the checkbutton will turn green.

#### Mute-button
* Press the "m" button. The sound should be muted. 