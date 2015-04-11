<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

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
<h1>Eingaben überprüfen</h1>   
<b> BITTE ALLES NOCHMALS KONTROLLIEREN!!! </b> <br>
KEINE LEERZEICHEN benutzen!
DU bist kurz vor dem FINALEN Eintragen in die DATENBANK!<br> <br>
<b><font color="#FF0000">HINWEISE: </font></b>

<br>
<?php 

$badmintonAnzahlTeams=$_POST['badmintonAnzahlTeams']; //anzahl badminton teams
$AnzahlSpielerMannschaft=$_POST['AnzahlSpielerMannschaft']; //spieler/mannschaftssportart team
$anzahlTeams = $_POST['anzahlTeams']; 
/*for ($i=0; $i < count($anzahlTeams); $i++) {
	echo  $anzahlTeams[$i];   
} */

//klasse speichern
$klasse = $_POST['klasse'];
//k1 speichern
$k1 = $_POST['k1'];

//$badmintonAnzahlTeams=10; //anzahl badminton teams
//$AnzahlSpielerMannschaft=9; //spieler/mannschaftssportart team

/* $anzahlTeams = array(
  0 => 2, //FB
  1 => 1, //BB
  2 => 1, //FT
  3 => 1, //VB
  4 => 1,  //ST 
  );*/
  
$anzahlTeamsGesamt=0;
  
for ($i=0; $i<count($anzahlTeams); $i++) {
	for ($a=0; $a<$anzahlTeams[$i]; $a++) {
		$anzahlTeamsGesamt++;
	}
}   

 $teamGroesse = array(          //teamgroesse fürs checken
  0 => '6',   //FB
  1 => '6',   //BB
  2 => '0',   //FT
  3 => '6',   //VB
  4 => '4',  //ST
     );
  
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
   
$neueArrayLaenge=0;
for ($i=0; $i<count($anzahlTeams); $i++) { //für alle Teams
    	for ($a=1; $a<$anzahlTeams[$i]+1; $a++) {
        	$teamNummer = $a;
        	//vornamen teil
        	$sportartenName[$neueArrayLaenge] = $nameDB[$i] . $teamNummer . '_v';          //array mit fb1_v etc
        	$sportartenVollerName[$neueArrayLaenge]= $nameVoll[$i] . ' ' . $teamNummer ;   //array fürs echo, ausgeschrieben
        	$sportartenTeamGroesse[$neueArrayLaenge] = $teamGroesse[$i];
      		/*  echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenVollerName[$neueArrayLaenge] . '<br>';   */
        
        	$neueArrayLaenge++;
        
        	//nachnamen teil
        	$sportartenName[$neueArrayLaenge]= $nameDB[$i] . $teamNummer . '_n';
        	$sportartenVollerName[$neueArrayLaenge]= $nameVoll[$i] . ' ' . $teamNummer;        
         	$sportartenTeamGroesse[$neueArrayLaenge] = $teamGroesse[$i];
      		/*  echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenName[$neueArrayLaenge] . '<br>';
        	echo 'Nr. ' . $neueArrayLaenge . ' Team: ' . $sportartenVollerName[$neueArrayLaenge] . '<br>';   */
        
        	$neueArrayLaenge++; 
    	}
}
  
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
      	$sportartenVollerName[$neueArrayLaenge]= $zweierTeamsName[$i+1];
      	$neueArrayLaenge++;
}
   
 //array für alle teamnamen anlegen    

$neueArrayLaenge=0;
for ($i=0; $i<count($anzahlTeams); $i++) { //für alle Teams
    	for ($a=1; $a<$anzahlTeams[$i]+1; $a++) {
        	$teamNummer = $a;
        	$sportartenVollerNameEinzeln[$neueArrayLaenge] = $nameVoll[$i] . ' ' . $teamNummer;          //array mit Fußball Team 1 etc.
        
        	$neueArrayLaenge++; 
    	}
} 
  
        

//läd die werte aus $_POST von sporttage.php in sportarten1 an der richtigen stelle
for ($i=0; $i < count($sportartenName); $i++ )  {   
      $sportarten1[$i] = $_POST[$sportartenName[$i]];
}

if ($k1=='k1') {
  if (strpos($klasse,'D') !== false) {
  $k1 = 'checked';     //wenn k1 gecheckt war soll es wieder gecheckt werden
  } else {
  echo '<b><font color="#FF0000"> Achtung: K1 falsch eingetragen</font> </b><br>';  //deutschkurs angabe fehlt
  }
} else {
  $k1='';
  }
  
//******************EINGABEN PRÜFEN******************************************************************************************************************  
  
  
 //prüfen, ob bei den mannschaftssportarten ein fehler/problem vorhanden ist 
for ($sportart=0; $sportart<count($sportartenName)-4; $sportart+=2) {  
  	$namenZaehler=0;  //zaehler erstellen
  	$nameFehlt=0;
	for($i = 0; $i < count($sportarten1[$sportart]); $i++) {
		if ($sportarten1[$sportart][$i] != '' && $sportarten1[$sportart+1][$i] != '')  {
      			$namenZaehler++;  //zaehler+1 je mannschaftsteam
		}
    		if ($sportarten1[$sportart][$i] != '' xor $sportarten1[$sportart+1][$i] != '')  {
      			$nameFehlt=true;  //true, wennn es einen unvollständigen namen gibt
		}
    
	}
	//echo 'sportart: ' . $sportart . ' teamgroesse: ' . $sportartenTeamGroesse[$sportart];
  	if (strpos($sportartenVollerName[$sportart],'Fahrradtour') == false && (($namenZaehler>0 && $namenZaehler < $sportartenTeamGroesse[$sportart]) || ($nameFehlt==true))) {
     		echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler in ' . $sportartenVollerName[$sportart] . ' (' . $sportartenVollerName[$sportart] . ' braucht mind ' . $sportartenTeamGroesse[$sportart] . ') oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  	}
  	$nameFehlt=false;   
}

for ($sportart=(count($sportartenName)-4); $sportart<count($sportartenName); $sportart+=2) { 
	//echo $sportart;
	$mannschaftsNummer=0;
  	for($i = 1; $i < count($sportarten1[$sportart]); $i+=2) {
		$mannschaftsNummer++; 
		if (($sportarten1[$sportart][$i] != '' xor $sportarten1[$sportart+1][$i] != '') or ($sportarten1[$sportart][$i-1] != '' xor $sportarten1[$sportart+1][$i-1] != ''))  {
        	 	if ($sportart==(count($sportartenName)-4)) {
            			echo '<b><font color="#FF0000"> Achtung:  ein Vor- oder Nachname fehlt in Badminton Team ' . $mannschaftsNummer . '!</font> </b><br>';
		      	}
          		if ($sportart==(count($sportartenName)-2)) {
            			echo '<b><font color="#FF0000"> Achtung:  ein Vor- oder Nachname fehlt in Tischtennis Team ' . $mannschaftsNummer . '!</font> </b><br>';
		      	}
      		}	
	}
} 

//*************************TABINDEX*********************************************************************
$anzahlSpaltenOhneBM = 0;
$currentTabIndex=0;
 //echo '18= ' . (count($sportarten1[0])*2) . '<br>';

for (; $anzahlSpaltenOhneBM <= ($anzahlTeamsGesamt*2); $anzahlSpaltenOhneBM+=2) {
	//echo "spalten " . $anzahlSpaltenOhneBM . '<br>';
  	$tabindex[$anzahlSpaltenOhneBM] = $currentTabIndex +1 ;
  	$tabindex[$anzahlSpaltenOhneBM+1] = $currentTabIndex +2;
  	$currentTabIndex += (count($sportarten1[0])*2);  	 
}

$currentTabIndex-=(count($sportarten1[0])) -2;	

//bm übernimmt die werte von staffellauf und zählt weiter, für die tt zeile müssen wir wissen, bis wieviel die bm zeilen zählen
//das wird hier gespeichert und später abgerufen
$tabindex[$anzahlSpaltenOhneBM] = $currentTabIndex + ($badmintonAnzahlTeams*4);	//TT v
$tabindex[$anzahlSpaltenOhneBM+1] = $currentTabIndex + ($badmintonAnzahlTeams*4) + 1;	//TT n
  
//tabindex ehrhöhen, sodass tabelle erst bei 3 beginnt (für k1 feld=1 und klasse feld=2)
for ($i=0; $i < count($tabindex); $i++) {
   	//echo 'tabindex von ' . $i . ' ist: ' . $tabindex[$i] . '<br> ';
    	$tabindex[$i]+=2;
}


  
//******************************TABELLE BAUEN UND AUSFÜLLEN***************************************************************************************************************  
   
?>

<br> 

Klasse eingeben:
<form action="submit.php" method="post">
<input type="checkbox" name="k1" value="k1" <?= $k1 ?> tabindex=1> K1  <br>
<input type="text" name="klasse" required="required" value="<?= $klasse ?> " tabindex=2/>
<table>
	<!-- Fußball 1 + 2 & Fahrradtour  & BB -->



 <?php 


$anzahlMannschaftZeilen = ceil($anzahlTeamsGesamt)/3; //3 mannschaften pro zeile, bm und tt zählen nicht
//echo "anzahl mannschaft zeilen: " . $anzahlMannschaftZeilen;
$mannschaftZeile=0;	//zeile, in der mannschaften stehen
$mannschaftSpalte=0; 	//spalten, in denen mannschaften stehen != spalte mit vor und nachnamen, max größe = mannschaftenProZeile
$mannschaftenProZeile=3; //s.o.
$spalte=0; 	//spalte für die textfelder
$multiMannschaftSportart=0; //zähler für die mannschaften mit mehr als einem team pro zeile

//***************************TABELLE BAUEN********************************************************************
for (; $mannschaftZeile < $anzahlMannschaftZeilen; $mannschaftZeile++) {
	//anzahl mannschaften in dieser zeile ausrechnen, für später
	$mannschaftenDieseZeile = (count($sportartenVollerNameEinzeln))%($mannschaftenProZeile);
	//echo ' count voller name: ' .  (count($sportartenVollerName)/2) . ' mannschaft/zeile ' . ($mannschaftenProZeile) . '<br>';
	//echo " diese zeile " . $mannschaftenDieseZeile . '<br>';
	if ($mannschaftZeile <  ($anzahlMannschaftZeilen-1) || $mannschaftenDieseZeile==0) {
		$mannschaftenDieseZeile=3;
	}
	//**********überschrift bauen******************+
	
	echo '<tr>' . " \n";	//es müssen " sein, damit er eine neue seite für den seitenquelltext einfügt

	$oldMannschaftSpalte=$mannschaftSpalte;	//spaltenanzahl von vorher speichern, für die länge der for schleife wichtig
	$multiMannschaftSportart=0; //für jede zeile von vorn, gibt an, ob eine sportarten mehrere mannschaften in dieser zeile hat
	
	for (; $mannschaftSpalte<($oldMannschaftSpalte+($mannschaftenProZeile-$multiMannschaftSportart)); $mannschaftSpalte++) { //for schleife soll 3 spalten ausgeben, ob es mehrere teams/sportart gibt beeinflusst die berechnung	
		echo '<td colspan="2"> ' . $sportartenVollerNameEinzeln[$mannschaftSpalte] . " \n";	//human readable mannschaftsnamen eintragen, 2 spalten da es vor und nachname gibt
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
  	for($zeile = 0; $zeile<count($sportarten1[0]); $zeile++) {   //zeilenweise durch die tabelle
    		echo "<tr> \n";                                   //neue reihe erstellen
    		for($spalte=$oldSpalte ; ($spalte<$oldSpalte+($mannschaftenProZeile*2)) && ($spalte < (count($sportartenName)-4)); $spalte++)  {      //in jeder spalte die richtige mannschaft einfügen
      			//richtigen namen gemäß spalte wählen und richtige daten nach spalte und zeile
      			echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" value=\"" .  $sportarten1[$spalte][$zeile] .  "\" tabindex=" . $tabindex[$spalte] . "/> \n";   
      			//tabindex der spalte wird um zwei erhöht, da nach 22 (v) 23 (n) und danach dann wieder 24 (v) folgt
     			 $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
   		}
    		echo "</tr> \n";  //row beenden
  	}	
 }
  
 
  
  
  
?> 
   
   	<tr>
		<td colspan="8">  	
	</tr>
		<td colspan="2"> Badminton
		<td colspan="2"> Tischtennis
<!--		<td colspan="2"> Staffellauf -->
	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname

<!--
		<td> <b>Vorname
		<td> <b>Nachname    -->
	</tr>
  
  
    <?php 
    
 // $tabindex = array(1, 2, 19, 20, 37, 38, 55, 56);
  
  //echo $tabindex[1];
    $teamzaehler = 0; //zaehler für das einzutragende team alle 3 zeilen
    $eintragZeile = 0; //zeile, in der ein textfeld ist   (anzahl beginnend bei 0 und spieler1 von team1)
  for($zeile = 0; $zeile < (((count($sportarten1[(count($sportarten1)-4)])/2))*3); $zeile++) {
    $teamRow=0;       //zeile in der ein teamname steht
    $eintragZeile++;
    
    echo "<tr> \n";
    if ($zeile%3 == 0) {     //team zeile (jede 3.)
           $teamzaehler++;
          echo "<td colspan=\"2\" style=\"text-align:center\"> Team " . $teamzaehler . "\n";
          echo "<td colspan=\"2\" style=\"text-align:center\"> Team " . $teamzaehler . "\n";
          $eintragZeile--;
          $teamRow=1;
      } 
    	for($spalte = $anzahlTeamsGesamt*2; $spalte<($anzahlTeamsGesamt*2)+4; $spalte++)  {
      //echo "in der for schleife";
    //  echo $zeile%3;
       if ($teamRow) { 
          break;   
          //$teamRow=0;
        } else {   
       //echo $eintragZeile-1;     
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\"  value=\"" .  $sportarten1[$spalte][$eintragZeile-1] .  "\" tabindex=" . $tabindex[$spalte] . "/> \n"; 
      $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
       }
    }
    
    echo "</tr> \n"; 
  }
       
  
?> 
  
</table>
<!--- mannschaftsgrößen etc übergeben: -->
    <input type="hidden" name="badmintonAnzahlTeams" value=" <?= $badmintonAnzahlTeams?>">
   <input type="hidden" name="AnzahlSpielerMannschaft" value=" <?= $AnzahlSpielerMannschaft?>">
<?php 
for ($i=0; $i < count($anzahlTeams); $i++) {
	echo   '<input type="hidden" name="anzahlTeams[]" value="' . $anzahlTeams[$i] . '">';   
} 
?>
<br> Du bist sicher, dass alles stimmt? <br>
<input type="checkbox" name="blabla" value="1" required="required"> Ich, 
<input type="text" name="schuldiger" required="required" value="" /> (bitte den Namen eintragen) 
bin verantwortlich dafür, wenn etwas nicht stimmt <br>
  <input type="submit" name="submit" value="Alles Richtig?">
</form>
<br>
</body>
</html>

