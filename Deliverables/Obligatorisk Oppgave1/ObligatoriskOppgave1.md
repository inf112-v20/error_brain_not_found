# Mandatory Assignment 1
TEAM: error_no_brain_found

## Task 1
### Mathias Skallerud Jacobsen
Role: Team leader  
Skills: Good to have control over people, and dare to say if someone is not doing what they are supposed to. Relatively
 good to code. Trying to get an overview of situations.  
Expertise: INF100, INF101, INF142, INF143, INF102.  
Main tasks: Have overall control of the team. Make sure people do what they are supposed to, in the time-space they are 
suppose to. Responsibility to create and get the Project Board up and running, and keep it up to date.

### Magnus Tønnessen
Role: Customer Contact  
Skills: Good at programming. Structured and organized. See the whole of the product.  
Expertise: INF100, INF101, INF102, INF122.  
Main tasks: The teams's main domain expert. Make sure the end product meets the requirements and expectations (High
 level requirements) we have.

### Jenny Strømmen
Role: Lead Tester  
Skills: Good at making tests and code in general.  
Expertise: INF100, INF101, INF102, INF122, INF142, INF234  
Main tasks: The team's main tester. She will be responsible for 'testing code', and that the test tests what it is 
suppose to. 

### Duy Kenny Vinh Nguyen
Role: Lead Designer  
Skills: 'Okey' at coding. Does what he is asked to do.   
Expertise: INF100, INF101  
Main tasks: The teams main lead on design / graphics. Responsible for knowing the most of the GUI (libgdx), 
so he can be the one to talk to if anyone need help to understand something that is libGDX-special.

### Abdul Hamed Yadgari
Role: Refactoring lead  
Skills: Fast learner, and get things done. Know how to code.  
Expertise: Inf100, inf115, Inf101  
Main tasks: Checks the code for errors, and try to clean-up code.

## Task 2
Overall goal: To be able to play a digital version of RoboRally against another (AI or player), with set rules and a 
working board and responds to input.  

### High level requirements 
|                       |                              |                  |
| --------------------- |:----------------------------:| ----------------:|
| Game board            | Move robots                  | Game over        |
| Playing piece         | Delegate cards               | Multiplayer      |
| Program card          | Visit flag                   | Singleplayer     |
| Player                | Drop the backup              | Difficulty levels|
| Moving fields / belts | Repair damage                | Default level    |
| Special field         | Get destroyed / with damage  |                  |
| Laser                 | Shoot / Activate laser       |                  |
| Flag                  | Powerdown                    |                  |
| Walls                 | Just one robot at a time     | 
| Rounds                | Priority moves               |
| Phase                 | Damaged robots               |

#### Requirements for first iteration
1.  We have a board.
    *   Able to see the the board.
    
2.  We have a playing piece.
    *   Able to se the piece.
    
3.  Able to set the playing piece on the board.
    *   Able to move the piece around.
    
4.  Work on the design.

## Task 3
Various elements from project methodologies:  

*   XP
    *   Test before every commit.
    
        *   Reason: We want to make sure we deliver something that is functional,
         and if the tests are based on the rules of the game then we can program with that
         considering what the game is all about. By testing before each commit, we will also ensure that the code meets 
         the requirements we give it.  
         
*   Kanban
    *   Limit in the progress board.
    
        *   Reason: In order not to be overloaded with tasks, we intend to limit the number of tasks each person can have 
        in order to focus on a task and ensure that this task is done properly before further building on it or work on 
        another task.  
        
*   Scrum
    *   Cross-functional team.
    
    *   Meetings / team updates after each iteration.
        *   Reason: With a cross-functional team, we have defined roles where each person has their area of 
        responsibility. What we want to achieve with this is a more orderly progress, as it hopefully limits more work 
        on the same task. In order for everything to be coordinated, we must try to have meetings after each iteration.
        
Using gitFlow; one ``master``-branch only for iterations. ``develop`` is used for day to day developing, and this 
branch is the one pushed to ``master``-branch mandatory assignments. Every feature have their own ``feature``-branch.
 
Meetings: We decided to have one extra meeting every week to check the status of the project.  
Communication between meetings: Slack.  
Workflow: As describe in ``Task 1``.
Common docs: Google drive.

### User stories
Story: As a player I want to see a board, to be able to know what's happening in the game.  

Acceptance criteria: 
*   Given that the board shows a cell, the board has only one in height and one in width (one cell). 
*   Given that a board appears, when the board has no robots / items on it, all cells are empty.
*   Given that a board is displayed, when I should have the height and length of the board, it matches what I expect.  

How to get there: Make a ``Board``-class, with cells. We have to have methods that can get dimensions from 
the board.  

Story: As a player I want to be able to place the player where I want it to start. 
Acceptance criteria: 
*   Given that I have a robot and a board, when I place the robot on the board, then the board has a 
robot on it.  

How to get there: Need to have a ``Player/Robot``-class that is able to store a position. ``Board``-class need to know 
whats on the cells, and be able to put something on the board.

Story: As a player I want to be able to press the mouse so that the robot moves.  
Acceptance criteria: 
*   Given that I have a player on the board, when I click on a tile the player moves there.
How to get there: Implement ``mouseListener``, that are able to get positions and return it to the board, and update the
 ``player / robot`` positions.  

Story: As a player I want to be able to move the pointer over the board in such a way that the cell under the pointer is 
highlighted. Then I can be sure that the piece is placed where I want.  
Acceptance criteria: 
*   Given that I want to place a robot, when I place the pointer over a given cell, that cell should be highlighted.   
How to get there: Board must receive an input from the pointer so that it can select a current cell.  

## Summary

*   What worked?

We have done what the task tells us to do, we managed to show a board and place a player on it. We also made user
stories, and tasks according to these. 

*   What did not work?

We had some communication problems in the beginning, and we should have
defined who should do what more clearly by using issues more.
When trying to test the board it did not work out, because you need to 
run the application in order to get information from it. 

*   Is there something we should de better to next mandatory assignment?

Make the tasks for each member of the team more clearer, and create more user stories
for the tasks. 

*   Are you satisfied with your work, is there something that you could not do?

It was a bit hard to learn LibGDX right away, so it was a bit back and 
fourth. The tests written seems ok, but we could not test the board. The 
tester wants to learn a bit more about mocking before trying to write tests for 
the board. 
