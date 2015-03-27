<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width" />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<?php
$badmintonAnzahlTeams=10; //anzahl badminton teams
$AnzahlSpielerVB=9;
$AnzahlSpielerFB=9;
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
<input type="checkbox" name="k1" value="k1"> K1  <br>
<input type="text" name="klasse" required="required" />

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

   
?>



<table>
	<!-- Fußball 1 + 2 & Fahrradtour -->
	<tr>
		<td colspan="2"> Fußball 1
		<td colspan="2"> Fußball 2		
		<td colspan="2"> Basketball
	</tr>
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
  
  <?php 
  //tabindex: zum vereinfachten 'tabben' durch die website, zwei aufeinanderfolgende sind vorname nachname, von _n zu _v zum nächsten +17
  $tabindex = array(1, 2, 19, 20, 37, 38, 55, 56, 73, 74, 91, 92, 109, 110);
 //die länge hängt mit der länge von fb1_v zusammen
    
    
  for($zeile = 0; $zeile<$AnzahlSpielerFB; $zeile++) {   //zeilenweise durch die tabelle
    echo "<tr> \n";                                   //neue reihe erstellen
    for($spalte = 0 ; $spalte<6; $spalte++)  {      //in jeder spalte die richtige mannschaft einfügen
      //echo "in der for schleife";
      //richtigen namen gemäß spalte wählen und richtige daten nach spalte und zeile
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" tabindex=" . $tabindex[$spalte] . "/> \n";   
      //tabindex der spalte wird um zwei erhöht, da nach 22 (v) 23 (n) und danach dann wieder 24 (v) folgt
      $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
    }
    echo "</tr> \n";  //row beenden
  }
 
  
?> 
  
  
 	<tr>
		<td colspan="8">  	
	</tr>
		<td colspan="2"> Fahrradtour
		<td colspan="2"> Volleyball
		<td colspan="2"> Staffellauf

	</tr>
	
	<tr>
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
		<td> <b>Vorname
		<td> <b>Nachname
	</tr>
      
   <?php 
 //die länge hängt mit der länge von vb1_v zusammen   
  for($zeile = 0; $zeile<$AnzahlSpielerVB; $zeile++) {
    echo "<tr> \n";
    for($spalte = 6 ; $spalte<12; $spalte++)  {
      //echo "in der for schleife";
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" tabindex=" . $tabindex[$spalte] . "/> \n"; 
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
  for($zeile = 0; $zeile < ($badmintonAnzahlTeams*3); $zeile++) {
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
      echo "<td> <input type=\"text\" name=\"" . $sportartenName[$spalte] . "[]\" tabindex=" . $tabindex[$spalte] . "/> \n"; 
      $tabindex[$spalte]+=2; //tabindexwert um 2 erhöhen für schönes eintippen  
       }
    }
    
    echo "</tr> \n"; 
  }
       
  
?> 
    
  

	

</table>
  <input type="submit" name="submit">
</form>
<br>
</body>
</html>

