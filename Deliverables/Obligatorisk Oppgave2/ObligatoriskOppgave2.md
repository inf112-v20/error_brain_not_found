* Walls: 
A player can not go through a wall

User-story:
As a player I want that the wall stop me, so that I can not go in that direction, such that I have a challange in
make the route.

Acceptance-criteria: 
Given that I am facing a wall, I will not be able to go through it.

Tasks:
Make a check canGo() that tells if we are facing a wall.

* Game board 
Hvis du går utenfor så dør du 

Brukerhistorie:
Som spiller vil jeg at når jeg går utenfor brettet så respawner jeg et sted slik at jeg kan fortsette spillet.

Akseptansekriterie:
Hvis jeg går utenfor brettet, så skal jeg respawne på spawnlocation.

Arbeidsoppgaver:
Lage en sjekk som sier om man er utenfor brettet. Lagre spillerinformasjon i spilleren. En metode som plasserer spiler på respawnsted.

* Playing piece 
Playere kan ikke gå på hverandre

Brukerhistorie:
Som spiller vil jeg at når jeg prøver å gå på en annen spiller så går ikke det, siden jeg vil kunne dytte den.

Akseptansekriterier: 
Hvis jeg prøver å gå på en spiller, så flytter den på seg når jeg går på den. 

Arbeidsoppgaver:
Lage en sjekk som sier hvis det er en spiller på cellen du prøver å gå på. En metode som flytter spillerene til der de skal.