<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<?php
$badmintonAnzahlTeams=10; //anzahl badminton teams
$AnzahlSpielerMannschaft=9; //spieler/mannschaftssportart team
$anzahlTeams = array(	//anzahl mannschaften pro sportart
	0 => 2, //FB
  	1 => 1, //BB
  	2 => 1, //FT
  	3 => 1, //VB
  	4 => 1,  //ST 
  );
?>

<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 3px;
}
</style>

<body>
Bitte die K1-Kurse so eingeben: Box neben "K1" anklicken und dann den Kurs (D1,D2,D3 etc.) in das Feld eingeben <br>
KEINE LEERZEICHEN benutzen!<br>
Umlaute können problemlos verarbeitet werden, aber bitte darauf achten, dass Namen nicht falsch geschrieben werden (oder wenn falsch, dann einheitlich falsch)<br>
Zu einem Vornamen gehört IMMER ein Nachname, sonst geht die Welt unter!<br>
<br>
Klasse eingeben:
<form action="check.php" method="post">
<input type="checkbox" name="k1" value="k1" tabindex=1> K1  <br>
<input type="text" name="klasse" required="required" tabindex=1 />

<?php 


//klasse speichern
$klasse = $_POST['klasse'];
//k1 speichern
$k1 = $_POST['k1'];

//sportartenName Variabel erstellen


$nameDB = array(
  	0 => 'fb', //FB
  	1 => 'bb', //BB
  	2 => 'ft', //FT
  	3 => 'vb', //VB
	4 => 'st',  //ST 
  );
$nameVoll = array(
	0 => 'Fußball Team', //FB
  	1 => 'Basketball Team', //BB
  	2 => 'Fahrradtour', //FT
  	3 => 'Volleyball Team', //VB
  	4 => 'Staffellauf Team',  //ST 
  );
 
  
$anzahlTeamsGesamt=0;
  
for ($i=0; $i<count($anzahlTeams); $i++) {
	for ($a=0; $a<$anzahlTeams[$i]; $a++) {
		$anzahlTeamsGesamt++;
	}
} 
//echo $anzahlTeamsGesamt . '<br>'; 
  
  
$neueArrayLaenge=0;
for ($i=0; $i<count($anzahlTeams); $i++) { //für alle Teams
	for ($a=1; $a<$anzahlTeams[$i]+1; $a++) {
		$teamNummer = $a;
		//vornamen teil
		$sportartenName[$neueArrayLaenge] = $nameDB[$i] . $teamNummer . '_v';          //array mit fb1_v etc        
		// echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        
		$neueArrayLaenge++;
        
		//nachnamen teil
        	$sportartenName[$neueArrayLaenge]= $nameDB[$i] . $teamNummer . '_n';        
         
		//echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        
		$neueArrayLaenge++;  
	}
}
  
    //bm und tt am ende anlegen
    
$zweierTeamsName = array (
	0 => 'bm_v',
	1 => 'Badminton Teams',
	2 => 'bm_n',
	3 => 'Badminton Teams', 
	4 => 'tt_v',
	5 => 'Tischtennis Teams', 
	6 => 'tt_n',
	7 => 'Tischtennis Teams',  
  );
  
for ($i=0; $i < count($zweierTeamsName); $i+=2) {
	$sportartenName[$neueArrayLaenge]= $zweierTeamsName[$i];
      	//$sportartenVollerName[$neueArrayLaenge]= $zweierTeamsName[$i+1];
      	$neueArrayLaenge++;
}  

//array für alle teamnamen anlegen    

$neueArrayLaenge=0;
for ($i=0; $i<count($anzahlTeams); $i++) { //für alle Teams
    	for ($a=1; $a<$anzahlTeams[$i]+1; $a++) {
        	$teamNummer = $a;
        	//vornamen teil
        	$sportartenVollerName[$neueArrayLaenge] = $nameVoll[$i] . ' ' . $teamNummer;          //array mit fb1_v etc        
       		// echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        
        	$neueArrayLaenge++;
        
        	//nachnamen teil
       		// $sportartenName[$neueArrayLaenge]= $nameDB[$i] . $teamNummer . '_n';        
         
        	//echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
       		 
       		// $neueArrayLaenge++;  
    	}
}


//TABINDEX ERSTELLEN

$anzahlSpaltenOhneBM = 0;
$currentTabIndex=0;
// echo '18= ' . ($AnzahlSpielerMannschaft*2) . '<br>';

for (; $anzahlSpaltenOhneBM <= ($anzahlTeamsGesamt*2); $anzahlSpaltenOhneBM+=2) {
	//echo "spalten " . $anzahlSpaltenOhneBM . '<br>';
  	$tabindex[$anzahlSpaltenOhneBM] = $currentTabIndex +1 ;
  	$tabindex[$anzahlSpaltenOhneBM+1] = $currentTabIndex +2;
  	$currentTabIndex += ($AnzahlSpielerMannschaft*2);  	 
}

$currentTabIndex-=($AnzahlSpielerMannschaft*2) -2;	

//bm übernimmt die werte von staffellauf und zählt weiter, für die tt zeile müssen wir wissen, bis wieviel die bm zeilen zählen
//das wird hier gespeichert und später abgerufen
$tabindex[$anzahlSpaltenOhneBM] = $currentTabIndex + ($badmintonAnzahlTeams*4);	//TT v
$tabindex[$anzahlSpaltenOhneBM+1] = $currentTabIndex + ($badmintonAnzahlTeams*4) + 1;	//TT n
  
//tabindex ehrhöhen, sodass tabelle erst bei 3 beginnt (für k1 feld=1 und klasse feld=2)
for ($i=0; $i < count($tabindex); $i++) {
   	//echo 'tabindex von ' . $i . ' ist: ' . $tabindex[$i] . '<br> ';
    	$tabindex[$i]+=2;
}
   
?>


<table>
	<!-- MANNSCHAFTSSPORTARTEN -->


<?php 


$anzahlMannschaftZeilen = ($anzahlTeamsGesamt)/3; //3 mannschaften pro zeile, bm und tt zählen nicht
//echo "anzahl mannschaft zeilen: " . $anzahlMannschaftZeilen;
$mannschaftZeile=0;	//zeile, in der mannschaften stehen
$mannschaftSpalte=0; 	//spalten, in denen mannschaften stehen != spalte mit vor und nachnamen, max größe = mannschaftenProZeile
$mannschaftenProZeile=3; //s.o.
$spalte=0; 	//spalte für die textfelder
$multiMannschaftSportart=0; //zähler für die mannschaften mit mehr als einem team pro zeile

//***************************TABELLE BAUEN********************************************************************
for (; $mannschaftZeile < $anzahlMannschaftZeilen; $mannschaftZeile++) {
	//anzahl mannschaften in dieser zeile ausrechnen, für später
	$mannschaftenDieseZeile = (count($sportartenVollerName))%($mannschaftZeile+1);
	if ($mannschaftenDieseZeile == 0) {
		$mannschaftenDieseZeile=3;
	}
	//**********überschrift bauen******************+
	
	echo '<tr>' . " \n";	//es müssen " sein, damit er eine neue seite für den seitenquelltext einfügt

	$oldMannschaftSpalte=$mannschaftSpalte;	//spaltenanzahl von vorher speichern, für die länge der for schleife wichtig
	$multiMannschaftSportart=0; //für jede zeile von vorn, gibt an, ob eine sportarten mehrere mannschaften in dieser zeile hat
	
	for (; $mannschaftSpalte<($oldMannschaftSpalte+($mannschaftenProZeile-$multiMannschaftSportart)); $mannschaftSpalte++) { //for schleife soll 3 spalten ausgeben, ob es mehrere teams/sportart gibt beeinflusst die berechnung	
		echo '<td colspan="2"> ' . $sportartenVollerName[$mannschaftSpalte] . " \n";	//human readable mannschaftsnamen eintragen, 2 spalten da es vor und nachname gibt
	}
 	
 	echo "</tr> \n"; //zeilenende einfügen
 	echo '<tr>' . " \n";		//es müssen " sein, damit er eine neue seite für den seitenquelltext einfügt

	for ($i=0; ($i<$mannschaftenProZeile) && $i<$mannschaftenDieseZeile; $i++) { //"vorname" und "nachname" einfügen
		echo '<td> <b>Vorname' . " \n";	
		echo '<td> <b>Nachname' . " \n";	
	}
	echo "</tr> \n";

	//*********textfelder bauen***********
 
    	$oldSpalte=$spalte;	//spaltenanzahl von vorher speichern, für die länge der for schleife wichtig
   	//die länge hängt mit der länge von anzahlspieler/mannschaft zusammen 
  	for($zeile = 0; $zeile<$AnzahlSpielerMannschaft; $zeile++) {   //zeilenweise durch die tabelle
    		echo "<tr> \n";                                   //neue reihe erstellen
    		for($spalte=$oldSpalte ; ($spalte<$oldSpalte+($mannschaftenProZeile*2)) && ($spalte < (count($sportartenName)-4)); $spalte++)  {      //in jeder spalte die richtige mannschaft einfügen
      			//richtigen namen gemäß spalte wählen und richtige daten nach spalte und zeile
      			echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" tabindex=" . $tabindex[$spalte] . "/> \n";   
      			//tabindex der spalte wird um zwei erhöht, da nach 22 (v) 23 (n) und danach dann wieder 24 (v) folgt
     			 $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
   		}
    		echo "</tr> \n";  //row beenden
  	}	
 }
  
 
  
  

?>  
    
 <!-- BADMINTON UND TISCHTENNIS -->   
    
 	<tr>
		<td colspan="8">  	
	</tr>
		<td colspan="2"> Badminton
		<td colspan="2"> Tischtennis
	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
  
  
<?php 

$teamzaehler = 0; //zaehler für das einzutragende team alle 3 zeilen
$eintragZeile = 0; //zeile, in der ein textfeld ist   (anzahl beginnend bei 0 und spieler1 von team1)
for($zeile = 0; $zeile < ($badmintonAnzahlTeams*3); $zeile++) {
    	$teamRow=0;       //zeile in der ein teamname steht
	$eintragZeile++;    
    	echo "<tr> \n";
    	if ($zeile%3 == 0) {     //team zeile (jede 3.)
        	$teamzaehler++;
          	echo "<td colspan=\"2\" style=\"text-align:center\"> Team " . $teamzaehler . "\n";		//teamnamen einfügen, simultan für bm und tt
          	echo "<td colspan=\"2\" style=\"text-align:center\"> Team " . $teamzaehler . "\n";
          	$eintragZeile--;
          	$teamRow=1;
      	} 
    	for($spalte = $anzahlTeamsGesamt*2; $spalte<($anzahlTeamsGesamt*2)+4; $spalte++)  {
       		if ($teamRow) { //wenn es eine "teamrow" gibt, keine textfelder einfügen
          		break;   
        	} else {   //sonst textfelder passend einfügen
      			echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" tabindex=" . $tabindex[$spalte] . "/> \n"; 
	      		$tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
	       	}	
	}
    
    echo "</tr> \n"; 
}
       
  
?> 
    
  

	

</table>
<input type="checkbox" name="blabla" value="danielundsandeshsindKINGS" required="required"> Ich akzeptiere die AGBs und die Datenschutzbestimmungen, außerdem akzeptiere ich, dass Daniel und Sandesh echte Kings sind und skill haben <br>
  <input type="submit" name="submit">
</form>
<br>
</body>
</html>

