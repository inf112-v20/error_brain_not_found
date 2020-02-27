# Mandatory Assignment 2 
Team: error_no_brain_found

## Task 1
The roles work fine. Maguns is doing a great work as Customer Contact and Mathias is working fine as team lead. 
### Roles
Our roles are working fine, and we will stick with the roles we have previously given.

#### Team lead
Have overall control of the team. Make sure people do what they are supposed to, in the time-space they are 
suppose to. Responsibility to create and get the Project Board up and running, and keep it up to date. Make sure that 
everybody do their assignments.  

#### Customer Contact
The teams's main domain expert. Make sure the end product meets the requirements and expectations (High
 level requirements) we have. Also have a depth control of the project structure and the progress.
  
#### Lead Tester
Make sure all functions work as they're supposed to, especially testing edge cases not thought of when they're written.
Also responsible for good user stories to every requirement.

#### Lead Designer 
Responsible for all graphics, sounds and music. Also responsible for making all the maps.

#### Refactoring lead
Checks the code for errors, and try to clean-up code. Making code easy to read for others.

### Communication and group dynamic
We communicate a lot better than last time, something we go more in depth in `Retrospective`. 
We discuss how we should do the project and the tests, and we're good at sharing knowledge.
We communicate good as a team, and between the different roles.


### Retrospective
Our project structure started out a bit unorganized, people started with requirements on their own and the team felt 
left out. Since they didnt want to do something that was already working on, but we manged to clean it up early. And 
after that we have become better day by day. Until now we have gotten a working board and we are able to move the player 
around on it. We have used a lot of time to catch everyone up to date on how the board in `libGDX` work. People have 
struggled on getting their head around how parts of `libGDX` work and how to use them with our code. We have used some 
time on sharing our knowledge, so everyone feel comfortable to write code and understanding what to get from `libGDX`.

In meetings we have tried to do some pair-programming. When we are working on the project outside of meetings, and get 
stuck on a problem we try to ask someone in the team to see the problem and if their able to help. If they have time we 
that often end-up with some pair-programming, until the task is complete. In meetings there is have become every easy to
ask if there is anything someone is wondering about, but outside of meetings people use a bit to long before they ask 
someone. So we should be better at using Slack or finding someone faster.
 
We have improved our ability to delegate work tasks and define them. Your workflow is going better, and we are planning 
our code in advance. So the tester are able to make up a overview of what she want to test in that specific domain. We 
give each other better feedback, on what we have done, but also what we should improve in our code and also what we want 
each other to complete in a given time.   

We can still be a better at communication, get a better communication flow through the week. 

Git has been a bottleneck in this project. There are some in the group that understand the basics on how to use git, but
most of the had never used git before and didnt in the start ask for explaining. So that made it hard for them to start, 
but after they asked for an explaining they are starting to get confident with it. But its still a way to go. Small tasks
takes longer then it needs to, just because people are afraid to start and this is since they don't 100% have they git 
experience. But this have gotten much better after they asked for help.  

#### Improvement points from retrospective
* Still getting better at communication in between meetings, on Slack ang go and ask in person
* Using git and get to know it well, and not be afraid for mess it up

### Meeting minutes
[Møte 1](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte)  
[Møte 2](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-2.)  
[Møte 3](https://github.com/inf112-v20/error_brain_not_found/wiki/Møte-3.)  

We forgot to make meeting minutes for our first couple of meeting. That is why there are missing some. And we only have 
one big meeting every week, and if there are need for it we have a stand-up meeting where we answer everything people 
need answering.

## Task 2

### User stories for requirements

#### Walls: 
A player can not go through a wall

User-story:
As a player I do not want to be able to go through walls, so that I can have a challenge in reaching the flags.

Acceptance-criteria: 
- Given that I am facing a wall, I will not be able to go through it.
- Given that I am not facing a wall but are on a tile with a wall, I will be able 
to go around the wall. 


Tasks:
- Make a check canGo() that tells if the player is facing a wall.
- Make tests to check acceptance-criteria.

#### Game board:
If you get outside the board you get respawned.

User-story:
As a player I want to be able to go outside the board without error, so that I can respawn.

Acceptance-criteria: 
- If I go outside the board, then I will not get an error.
- If I go outside the board, I will be respawned on the board.

Tasks:
- Make a check that says if you are outside the board. Store the playerinformation. 
- A method that places player on respawn-place.
- Tests to check acceptance-criteria.

#### Playing piece:
Player can not be on same cell

User-story:
As a player I do not want to be able to be on the same cell as another player, so that I can push the other player.

Acceptance-criteria:  
- If I try to go on the same cell as another player, both players will not be on this
cell.
- If I try to go on the same cell as another player, the player will move away from me 
in the same direction it was pushed. 
- If I try to go on the same cell as another player, the player will move in the 
same direction it was pushed. 

#### Programcards:
Show programcards and let the player draw cards, and making program.

User-story:
As a player I want to be able to draw cards, so that I can plan the program for the robot.

Acceptance-criteria:
- If I draw cards, I will have the correct amount of cards given to me.
- Given that I have drawn cards, I will have them displayed on the screen.

Tasks:
- Make a function that let the player draw cards from the deck.
- Test that the function draws the correct amount of cards and delegate them to correct player.
- Make a function that displays the cards on the screen.


User-story:     
As a player I want to be able to rearrange the cards, so that I can make a program for the robot.

Acceptance-criteria:
- If I pick up one card, I can decide where to put this card down again.
- When I put one card on top of the other, the cards will swap position. 

Tasks:
- Make a function that takes input from user and tells if a user picks up a card. 
- Make a function that swaps the position to two cards. 
- Make tests for when cards are swapped.

#### Several Boards:

User-story:  
As a player I want to be able to choose what board I want to play on, so that I can get a new challenge in the game. 

Acceptance-criteria:  
- If I pick a board, this is the board that will show on the screen.
- If I pick a different board, the same tiles will have the same functions as the previous one. 

Tasks:   
- Make several boards.
- Make tests for when a board is picked, this is the board that is actually used by the game. 
   


### Prioritisation of tasks
For the next iteration we are focusing on getting some game-logic so we can start to have a turn in our game.
 
### Main criteria for MVP
* The game should be able to run, show a board with starting robot and programcards to this robot.
* The player is able to pick programcards and place them so that the robot can move.
* The game should be able to do a round, and do the phases in the correct order in that round.
* The robot should be able to win by going to the flags in a specific order.
* The robot is stopped by walls, and respawned when outside board. 

### Tasks done since last assignment
Made user stories and tests for everything, improved how the player moves, made flags and gotten a understanding of how 
board works. And made many changes to it.

### Known bugs in 
Push player is wrong, when other players are in a corner  
Wrong start player

## Task 3
See `README.md -> How to run` 

### Manual test and how to run them
