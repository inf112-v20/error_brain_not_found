# Mandatory Assignment 2 
Team: error_no_brain_found

## Task 1
The roles work fine. Maguns is doing a great work as Customer Contact and Mathias is working fine as team lead.
### Roles
Our roles are working fine, and we will stick with the roles we have previously given.

#### Team lead

#### Customer Contact

#### Lead Tester

#### Lead Designer 

#### Refactoring lead


### Communication and group dynamic




### Retrospective


#### Improvement points from retrospective






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