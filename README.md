# INF112 Maven template 

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2681b622f0f64c85b70112115c4eae89)](https://app.codacy.com/gh/inf112-v20/error_brain_not_found?utm_source=github.com&utm_medium=referral&utm_content=inf112-v20/error_brain_not_found&utm_campaign=Badge_Grade_Settings)
[![Build Status](https://travis-ci.com/inf112-v20/error_brain_not_found.svg?branch=develop)](https://travis-ci.com/inf112-v20/error_brain_not_found)  
Simple skeleton with libgdx. 

## How to run
The program runs `java` and `maven`, these need to be installed.  
Every text-editor that can run version control system, can be used. We recommend IntelliJ IDEA.  
Clone the repo from github to you computer: `git clone https://github.com/inf112-v20/error_brain_not_found.git`  
Then open `pom.xml` in IntelliJ and the IntelliJ will build the project for you.  
Then open the repo and go to `src/main/java/inf112/skeleton/app/Main.java` and run `main` function.

## How to set up the game  
Multiplayer:  
The game runs over LAN and IPv4. If you want to start a game, you press "Create". Your IP address will be displayed 
 to you. If you do not want to host the game you can go back and press "Join" instead. There needs to be one person 
 hosting the game before anyone can join.  
 If you are hosting the game your IP address (it is displayed to you on the screen) to you friend(s) and tell them to 
 join the game with this IP address. When everyone has joined the game you can press start.


## Known bugs
Currently throws "WARNING: An illegal reflective access operation has occurred", 
when the java version used is >8. This has no effect on function or performance, and is just a warning.