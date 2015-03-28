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

//klasse speichern
$klasse = $_POST['klasse'];
//k1 speichern
$k1 = $_POST['k1'];

$sportartenName = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => 'fb1_v',
  1 => 'fb1_n',
  2 => 'fb2_v', 
  3 => 'fb2_n',
  4 => 'bb_v',
  5 => 'bb_n', 
  6 => 'ft_v', 
  7 => 'ft_n',
  //2. teil
  8 => 'vb_v',
  9 => 'vb_n',
  10 => 'st_v', 
  11 => 'st_n',
  12 => 'bm_v',
  13 => 'bm_n', 
  14 => 'tt_v', 
  15 => 'tt_n',
     );
     
 $sportartenVollerName = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => 'Fußball Team 1',
  1 => 'Fußball Team 1',
  2 => 'Fußball Team 2', 
  3 => 'Fußball Team 2',
  4 => 'Basketball Team',
  5 => 'Basketball Team', 
  6 => 'Fahrradtour', 
  7 => 'Fahrradtour',
  //2. teil
  8 => 'Volleyball Team',
  9 => 'Volleyball Team',
  10 => 'Staffellauf Team', 
  11 => 'Staffellauf Team',
  12 => 'Badminton Teams',
  13 => 'Badminton Teams', 
  14 => 'Tischtennis Teams', 
  15 => 'Tischtennis Teams',
     );    
     
  $teamGroesse = array(          //reihenfolge der Sportarten bestimmen für erstellen der tabelle
  0 => '6',   //FB
  2 => '6',   //FB
  4 => '6',   //BB
  8 => '6',   //VB
  10 => '4',  //ST
     );       

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
for ($sportart=0; $sportart<12; $sportart+=2) {  
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
  if ($sportart!=6 && (($namenZaehler>0 && $namenZaehler < $teamGroesse[$sportart]) || ($nameFehlt==true))) {
     	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler in ' . $sportartenVollerName[$sportart] . ' (' . $sportartenVollerName[$sportart] . ' braucht mind ' . $teamGroesse[$sportart] . ') oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  }
  
 /* 
  if ($sportart==0 && (($namenZaehler>0 && $namenZaehler < 6) || ($nameFehlt==true))) {
      	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler in Fußball Team 1 (FB braucht mind 6) oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  }
  if ($sportart==2 && (($namenZaehler>0 && $namenZaehler < 6) || ($nameFehlt==true))) {
      	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler in Fußball Team 2 (FB braucht mind 6) oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  }
  if ($sportart==4 && (($namenZaehler>0 && $namenZaehler < 5) || ($nameFehlt==true))) {
      	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler im Basketball Team (BB braucht mind 5) oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  }
  if ($sportart==8 && (($namenZaehler>0 && $namenZaehler < 6) || ($nameFehlt==true))) {
      	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler im Volleyball Team (VB braucht mind 6) oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  }
  if ($sportart==10 && (($namenZaehler>0 && $namenZaehler < 6) || ($nameFehlt==true))) {
      	echo '<b><font color="#FF0000"> Achtung: zu wenig Schüler im Staffellauf Team (ST braucht mind ) oder ein Vor- oder Nachname fehlt!</font> </b><br>';
  } */
  $nameFehlt=false; 
  
}

for ($sportart=12; $sportart<16; $sportart+=2) { 
	$mannschaftsNummer=0;
  	for($i = 1; $i < count($sportarten1[$sportart]); $i+=2) {
		  $mannschaftsNummer++; 
		  if (($sportarten1[$sportart][$i] != '' xor $sportarten1[$sportart+1][$i] != '') or ($sportarten1[$sportart][$i-1] != '' xor $sportarten1[$sportart+1][$i-1] != ''))  {
         	if ($sportart==12) {
            echo '<b><font color="#FF0000"> Achtung:  ein Vor- oder Nachname fehlt in Badminton Team ' . $mannschaftsNummer . '!</font> </b><br>';
		      }
          if ($sportart==14) {
            echo '<b><font color="#FF0000"> Achtung:  ein Vor- oder Nachname fehlt in Tischtennis Team ' . $mannschaftsNummer . '!</font> </b><br>';
		      }
      }
	 }
} 

  
//******************************TABELLE BAUEN UND AUSFÜLLEN***************************************************************************************************************  
   
?>

<br> 

Klasse eingeben:
<form action="submit.php" method="post">
<input type="checkbox" name="k1" value="k1" <?= $k1 ?> > K1  <br>
<input type="text" name="klasse" required="required" value="<?= $klasse ?> " />

<table>
	<!-- Fußball 1 + 2 & Fahrradtour  & BB -->
	<tr>
		<td colspan="2"> Fußball 1
		<td colspan="2"> Fußball 2		
		<td colspan="2"> Basketball
<!--		<td colspan="2"> Fahrradtour       -->
	</tr>
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
<!--
		<td> <b>Vorname
		<td> <b>Nachname -->
	</tr>



 <?php 
  //tabindex: zum vereinfachten 'tabben' durch die website, zwei aufeinanderfolgende sind vorname nachname, von _n zu _v zum nächsten +17
  $tabindex = array(1, 2, 19, 20, 37, 38, 55, 56, 73, 74, 91, 92, 109, 110, 110+($badmintonAnzahlTeams*4) , 110+($badmintonAnzahlTeams*4)+1);
 //die länge hängt mit der länge von fb1_v zusammen
    
    
  for($zeile = 0; $zeile<count($sportarten1[0]); $zeile++) {   //zeilenweise durch die tabelle
    echo "<tr> \n";                                   //neue reihe erstellen
    for($spalte = 0 ; $spalte<6; $spalte++)  {      //in jeder spalte die richtige mannschaft einfügen
      //echo "in der for schleife";
      //richtigen namen gemäß spalte wählen und richtige daten nach spalte und zeile
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\"  value=\"" .  $sportarten1[$spalte][$zeile] .  "\" tabindex=" . $tabindex[$spalte] . "/> \n";   
      //tabindex der spalte wird um zwei erhöht, da nach 22 (v) 23 (n) und danach dann wieder 24 (v) folgt
      $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
    }
    echo "</tr> \n";  //row beenden
  }
 
  
?> 
    
	  
  

	<!-- Badminton & Tischtennis & Volleyball -->
	
	<tr>
		<td colspan="8">  	
	</tr>
		<td colspan="2"> Fahrradtour
		<td colspan="2"> Volleyball
		<td colspan="2"> Staffellauf
<!--		<td colspan="2"> Staffellauf -->
	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
<!--
		<td> <b>Vorname
		<td> <b>Nachname    -->
	</tr>
  
   <?php 
 //die länge hängt mit der länge von vb1_v zusammen   
  for($zeile = 0; $zeile<count($sportarten1[9]); $zeile++) {
    echo "<tr> \n";
    for($spalte = 6 ; $spalte<12; $spalte++)  {
      //echo "in der for schleife";
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\"  value=\"" .  $sportarten1[$spalte][$zeile] .  "\" tabindex=" . $tabindex[$spalte] . "/> \n"; 
      $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
    }
    echo "</tr> \n"; 
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
  for($zeile = 0; $zeile < (((count($sportarten1[12])/2))*3); $zeile++) {
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
    for($spalte = 12; $spalte<16; $spalte++)  {
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
<br> du bist sicher, dass alles stimmt? <br>
<input type="checkbox" name="blabla" value="1" required="required"> ich, 
<input type="text" name="schuldiger" required="required" value="Idiot" /> (anderen Namen bei Bedarf eintragen) 
bin verantwortlich dafür, wenn etwas nicht stimmt <br>
  <input type="submit" name="submit" value="Alles Richtig?">
</form>
<br>
</body>
</html>

