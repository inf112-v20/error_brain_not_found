# Mandatory Assignment 2 
Team: error_no_brain_found

## Task 1
The roles work fine. Maguns is doing a great work as Customer Contact and Mathias is working fine as team lead.
### Roles
Our roles are working fine, and we will stick with the roles we have previously given.

#### Team lead
Has control of team and make sure that everybody do their assignments. 
#### Customer Contact
Has control of the project structure and the progress. 
#### Lead Tester
Make sure all functions work as they're supposed to, especially testing edge cases not thought of when they're written.
#### Lead Designer 
Responsible for all graphics, sounds and music. Also responsible for making all the maps.
#### Refactoring lead


### Communication and group dynamic
We communicate a lot better than last time. 
We discuss how we should do the project and the tests, and we're good at sharing knowledge.
We communicate good as a team, and between the different roles.


### Retrospective
Our project structure started out a bit unorganized, but we manged to clean it up early. And after that we have only
become better day by day. Until now we have gotten a working board and we are able to move the player around on it. We 
have used a lot of time to catch everyone up to date on how the board in `libGDX` work. People have struggled on 
getting their head around how parts of `libGDX` work and how to use them with our code. We have used some time on
sharing our knowledge, so everyone feel comfortable to write code and understanding what to get from `libGDX`.

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



#### Improvement points from retrospective





## Task 2

### User stories

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

Tasks:
- Make a check that says if a player is on the cell the player tries to move to. 
- A method that moves the player in the direction it is pushed. 
- Tests to fulfill acceptance-criteria.

### Prioritisation of tasks

 
### Main criteria for MVP


### Tasks done since last assignment
Made user stories and tests for everything, improved how the player moves, made flags and 

### Known bugs in 
Push player is wrong
Wrong start player

## Task 3
The project build, test and runs this way…

### Manual test and how to run them
