* Walls 
Skal ikke kunne gå gjennom veggen

Brukerhistorie:
Som spiller vil jeg at veggen skal stoppe meg slik at jeg ikke kan gå gjennom veggen, slik at jeg kan følge spillereglene.

Akseptansekriterier: 
Gitt at jeg møter en vegg, så skal jeg ikke kunne gå gjennom den.

Arbeidsoppgaver:
Lager en sjekk canGo() som forteller om vi er på en vegg i den retningen. 

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