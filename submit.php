<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<body>
<?php
session_start();
$servername = 'wp052.webpack.hosteurope.de';
$username = 'db1093417-sport';
$password = '%&SporTTage14@!';
$dbname = 'db1093417-sporttage';

$klasse = $_SESSION['klasse'];
$k1 = $_SESSION['k1'];

   
if (strpos($klasse,'10') !== false || $k1 == "k1" || strpos($klasse,'K1') !== false ||  strpos($klasse,'K2') !== false) {
	$stufenListe = "Mannschaften_OS";
} else if (strpos($klasse,'7') !== false || strpos($klasse,'8') !== false || strpos($klasse,'9') !== false || strpos($klasse,'A1') !== false) {
	$stufenListe = "Mannschaften_MS";
} else if (strpos($klasse,'5') !== false || strpos($klasse,'6') !== false) {
	$stufenListe = "Mannschaften_US";	
}

echo $stufenListe . '<br>' ;

$conn = new mysqli($servername, $username, $password, $dbname);

if($conn->connect_error){
	die('Connection failed:\n' . $conn->connect_error);
}

  // Umlaute fixen
  $conn->query('SET NAMES "utf8"');
  
  $abfrageSchuelerStatement = 'SELECT ID FROM ' . $stufenListe . ' WHERE Vorname = ? AND Name = ? AND Klasse = ?';
  	
	$abfrageSchueler = $conn->prepare($abfrageSchuelerStatement);
	
  $schuelerHinzufuegenStatement =  'INSERT INTO ' . $stufenListe . ' (Vorname, Name, Klasse) VALUES (?, ?, ?);';
	$schuelerHinzufuegen = $conn->prepare($schuelerHinzufuegenStatement);

	function getSchuelerID($name, $vorname, $klasse) {
	  //nun lokale variable damit man sie schließen und öffnen kann
    
		$abfrageSchueler = $GLOBALS['conn']->prepare($GLOBALS['abfrageSchuelerStatement']);	
        $abfrageSchueler->bind_param('sss', $vorname, $name, $klasse);
	    	$abfrageSchueler->execute();
	    	$abfrageSchueler->bind_result($id);
		if($abfrageSchueler->fetch()) {
        		$abfrageSchueler->close;
	    		return $id;
		 } else {
	    		$GLOBALS['schuelerHinzufuegen']->bind_param('sss', $vorname, $name, $klasse);
	    		$GLOBALS['schuelerHinzufuegen']->execute();
	    		$abfrageSchueler->execute();
	    		$abfrageSchueler->bind_result($id);
	    		if($abfrageSchueler->fetch()) {
        			 $abfrageSchueler->close();
			 	return $id;
	    		} 
	    	}	
	}

	//echo getSchuelerID('Herbert', 'Idris', '9a');


	//Fußball1
//variablen wird der wert zugewiesen	
$vornamen = $_SESSION['fb1_v'];
$nachnamen = $_SESSION['fb1_n'];
$id = 0;
//manschaftname besteht aus klasse + zahl, z.B. 9a1 9a2, es geht hier um mannschaft 1
$mannschaftFB1 = $klasse .  '1';
//für die SQL Anweisung muss die Mannschaft in Anführungszeichen sein:
$mannschaftFB1= '"' . $mannschaftFB1 .  '"';
   //für alle eingetragenen namen
for($i = 0; $i < count($vornamen); $i++) {
	if ($vornamen[$i] != '' && $nachnamen[$i] != '')  { //wenn etwas eingetragen ist
      		$id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);   //id holen
		$addSportartToSchueler = "UPDATE " . $stufenListe . " SET FB = " . $mannschaftFB1 . " WHERE ID = ". $id . " "; // string erstellenn
		if ($conn->query($addSportartToSchueler) === TRUE) {	//string ausführen und mit rückgabewert weitermachen
			echo "Fussball update erfolgreich bei: ";		//erfolg ausgeben
		} else {
			echo "error: " . $conn->error . " bei: ";		//fehler ausgeben
		}
		echo $vornamen[$i] . ' ';		//jeweils den Namen des Schülers ausgeben
		echo $nachnamen[$i] . '<br>';
	}
}	
	
   //Fußball 2
$vornamen = $_SESSION['fb2_v'];
$nachnamen = $_SESSION['fb2_n'];
$id = 0;
$mannschaftFB2 = $klasse .  '2';
$mannschaftFB2= '"' . $mannschaftFB2 .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
            echo $id;
            echo $mannschaftFB2;
            $addSportartToSchueler = "UPDATE " . $stufenListe . " SET FB = " . $mannschaftFB2 . " WHERE ID = ". $id . " ";
             echo $addSportartToSchueler;
              if ($conn->query($addSportartToSchueler) === TRUE) {
                  echo "update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
    
       }	
	   }
     
     
     //basketball
     
  $vornamen = $_SESSION['bb_v'];
	$nachnamen = $_SESSION['bb_n'];
	echo '<br>';
	 $id = 0;
   $mannschaftBB = $klasse .  '1';
   $mannschaftBB= '"' . $mannschaftBB .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftBB;
            $addSportartToSchueler = "UPDATE " . $stufenListe . " SET BB = " . $mannschaftBB . " WHERE ID = ". $id . " ";
         //  echo $mannschaftFB2;
             echo $addSportartToSchueler;
              if ($conn->query($addSportartToSchueler) === TRUE) {
                  echo "Basketball update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
    
       }	
	   }
     
     //volleyball
  $vornamen = $_SESSION['vb_v'];
	$nachnamen = $_SESSION['vb_n'];
 // echo $nachnamen[1];
	echo '<br>';
	 $id = 0;
   $mannschaftVB = $klasse .  '1';
   $mannschaftVB= '"' . $mannschaftVB .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftVB;
            $addSportartToSchueler = "UPDATE " . $stufenListe . " SET VB = " . $mannschaftVB . " WHERE ID = ". $id . " ";
         //  echo $mannschaftFB2;
             echo $addSportartToSchueler;
              if ($conn->query($addSportartToSchueler) === TRUE) {
                  echo "Volleyball update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
    
       }	
	   }
     
          //staffellauf
  $vornamen = $_SESSION['st_v'];
	$nachnamen = $_SESSION['st_n'];
 // echo $nachnamen[1];
	echo '<br>';
	 $id = 0;
   $mannschaftST = $klasse .  '1';
   $mannschaftST= '"' . $mannschaftST .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftST;
            $addSportartToSchueler = "UPDATE " . $stufenListe . " SET ST = " . $mannschaftST . " WHERE ID = ". $id . " ";
         //  echo $mannschaftFB2;
             echo $addSportartToSchueler;
              if ($conn->query($addSportartToSchueler) === TRUE) {
                  echo "Staffellauf update succesfully für Schüler: ";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
    
       }	
	   }
     
               //fahrradtour
  $vornamen = $_SESSION['ft_v'];
	$nachnamen = $_SESSION['ft_n'];
 // echo $nachnamen[1];
	echo '<br>';
	 $id = 0;
   $mannschaftFT = 'FT';
   $mannschaftFT= '"' . $mannschaftFT .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftFT;
            $addSportartToSchueler = "UPDATE " . $stufenListe . " SET FT = " . $mannschaftFT . " WHERE ID = ". $id . " ";
         //  echo $mannschaftFB2;
             echo $addSportartToSchueler;
              if ($conn->query($addSportartToSchueler) === TRUE) {
                  echo "Fahrradtour update succesfully für Schüler: ";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
    
       }	
	   }
     
     //tt
  $vornamen = $_SESSION['tt_v'];
	$nachnamen = $_SESSION['tt_n'];
	echo '<br>';
   $id = 0;
   $mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
        $mannschaftsNummer++;
     //   echo "in der for schleife";
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
          //  echo "in der for schleife";
           $id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           $id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           $mannschaftTT=0;
           $mannschaftTT = $klasse .  $mannschaftsNummer;
           $mannschaftTT= '"' . $mannschaftTT .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftTT;
            $addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET TT = " . $mannschaftTT . " WHERE ID = ". $id2 . " ";
         //  echo $mannschaftFB2;
         //    echo $addSportartToSchueler1;
          //  echo $addSportartToSchueler2;
              if ($conn->query($addSportartToSchueler1) === TRUE) {
                  echo "TT update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
              echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
              if ($conn->query($addSportartToSchueler2) === TRUE) {
                  echo "TT update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i-1] . ' ';
		          echo $nachnamen[$i-1] . '<br>';
    
       }	
	   }        
     
       //bm
  $vornamen = $_SESSION['bm_v'];
	$nachnamen = $_SESSION['bm_n'];
	echo '<br>';
   $id = 0;
   $mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
        $mannschaftsNummer++;
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           $id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           $mannschaftBM=0;
           $mannschaftBM = $klasse .  $mannschaftsNummer;
           $mannschaftBM= '"' . $mannschaftBM .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftBM . "<br>";
            $addSportartToSchueler1 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE " . $stufenListe . " SET BM = " . $mannschaftBM . " WHERE ID = ". $id2 . " ";
              if ($conn->query($addSportartToSchueler1) === TRUE) {
                  echo "BM update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
              echo $vornamen[$i] . ' ';
		          echo $nachnamen[$i] . '<br>';
              if ($conn->query($addSportartToSchueler2) === TRUE) {
                  echo "BM update succesfully";
              } else {
                  echo "error: " . $conn->error;
              }
		          echo $vornamen[$i-1] . ' ';
		          echo $nachnamen[$i-1] . '<br>';
    
       }	
	   }        
 
 $conn->close();
 
	?>
  
 Wenn oben ein Fehler steht, bitte bei Daniel/Sandesh melden und Fehlermeldung kopieren (Screenshot)! <br>
 Wenn alles erfolgreich war geht es <a href="http://www.danielbuecheler.hol.es/sporttage.php">hier</a> wieder zum Anfang:

</body>
</html>
