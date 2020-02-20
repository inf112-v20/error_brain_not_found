### User stories

#### Walls: 
A player can not go through a wall

User-story:
As a player I want that the wall stop me, so that I can not go in that direction, such that I have a challange in
make the route.

Acceptance-criteria: 
Given that I am facing a wall, I will not be able to go through it.

Tasks:
Make a check canGo() that tells if we are facing a wall.


#### Game board:
If you get outside the board you get respawned.

User-story:
As a player I want to be respawned when I go outside the board, such that I can continue the game.

Acceptance-criteria: 
If I go outside the board, then I will respawn in a spawn location on the board.

Tasks:
Make a check that says if you are outside the board. Store the playerinformation. A method that places player on respawn-place.

#### Playing piece:
Player can not be on same cell

User-story:
As a player I want to not be able to be on same cell as another player, so that I can push it.

Acceptance-criteria:  
If I try to walk on another player, then it moves away from me.

Tasks:
Make a check that says if a player is on the cell the player tries to move to. A method that moves the player til 
where it should be.