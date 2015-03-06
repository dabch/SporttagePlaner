<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<body>
	<?php
	$servername = 'wp052.webpack.hosteurope.de';
	$username = 'db1093417-sport';
	$password = '%&SporTTage14@!';
	$dbname = 'db1093417-sporttage';
	
	$klasse = $_POST['klasse'];

	$conn = new mysqli($servername, $username, $password, $dbname);

	if($conn->connect_error){
		die('Connection failed:\n' . $conn->connect_error);
	}
	
	$abfrageSchueler = $conn->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
	
	$schuelerHinzufuegen = $conn->prepare('INSERT INTO Mannschaften_MS (Vorname, Name, Klasse) VALUES (?, ?, ?);');

	function getSchuelerID($name, $vorname, $klasse) {
	  //nun lokale variable damit man sie schließen und öffnen kann
    /*  $GLOBALS['abfrageSchueler'] = $conn->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
	    $GLOBALS['abfrageSchueler']->bind_param('sss', $vorname, $name, $klasse);
	    $GLOBALS['abfrageSchueler']->execute();
	    $GLOBALS['abfrageSchueler']->bind_result($id);
	    if($GLOBALS['abfrageSchueler']->fetch()) {
        $GLOBALS['abfrageSchueler']->close;
	    	return $id;
	    } else {
	    	$GLOBALS['schuelerHinzufuegen']->bind_param('sss', $vorname, $name, $klasse);
	    	$GLOBALS['schuelerHinzufuegen']->execute();
	    	$GLOBALS['abfrageSchueler']->execute();
	    	$GLOBALS['abfrageSchueler']->bind_result($id);
	    	if($GLOBALS['abfrageSchueler']->fetch()) {
          $GLOBALS['abfrageSchueler']->close();
		    	return $id;
	    	} 
	    }
      */
      $abfrageSchueler = $GLOBALS['conn']->prepare('SELECT ID FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?');
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
	$vornamen = $_POST['fb1_v'];
	$nachnamen = $_POST['fb1_n'];
	echo '<br>';
	echo $klasse . '<br>';
	 $id = 0;
   $mannschaftFB1 = $klasse .  '1';
  //für die SQL Anweisung muss die Mannschaft in Anführungszeichen sein:
   $mannschaftFB1= '"' . $mannschaftFB1 .  '"';
   
	for($i = 0; $i < count($vornamen); $i++) {
    if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
      $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
      echo $id . " ";
      echo $mannschaftFB1 . "  ";
      $addSportartToSchueler = "UPDATE Mannschaften_MS SET FB = " . $mannschaftFB1 . " WHERE ID = ". $id . " ";
     // echo $addSportartToSchueler;
      if ($conn->query($addSportartToSchueler) === TRUE) {
        echo "update succesfully";
      } else {
        echo "error: " . $conn->error;
      }
		  echo $vornamen[$i] . ' ';
		  echo $nachnamen[$i] . '<br>';
    
    }

		
	}
   //Fußball 2
  $vornamen = $_POST['fb2_v'];
	$nachnamen = $_POST['fb2_n'];
	echo '<br>';
	echo $klasse . '<br>';
	 $id = 0;
   $mannschaftFB2 = $klasse .  '2';
   $mannschaftFB2= '"' . $mannschaftFB2 .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftFB2;
            $addSportartToSchueler = "UPDATE Mannschaften_MS SET FB = " . $mannschaftFB2 . " WHERE ID = ". $id . " ";
         //  echo $mannschaftFB2;
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
     
  $vornamen = $_POST['bb_v'];
	$nachnamen = $_POST['bb_n'];
	echo '<br>';
	echo $klasse . '<br>';
	 $id = 0;
   $mannschaftBB = $klasse .  '1';
   $mannschaftBB= '"' . $mannschaftBB .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftBB;
            $addSportartToSchueler = "UPDATE Mannschaften_MS SET BB = " . $mannschaftBB . " WHERE ID = ". $id . " ";
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
  $vornamen = $_POST['vb_v'];
	$nachnamen = $_POST['vb_n'];
 // echo $nachnamen[1];
	echo '<br>';
	echo $klasse . '<br>';
	 $id = 0;
   $mannschaftVB = $klasse .  '1';
   $mannschaftVB= '"' . $mannschaftVB .  '"';
     	for($i = 0; $i < count($vornamen); $i++) {
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
           $id = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
   
            echo $id;
            echo $mannschaftVB;
            $addSportartToSchueler = "UPDATE Mannschaften_MS SET VB = " . $mannschaftVB . " WHERE ID = ". $id . " ";
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
     
     //tt
  $vornamen = $_POST['tt_v'];
	$nachnamen = $_POST['tt_n'];
	echo '<br>';
	echo $klasse . '<br>'; 
  // echo $vornamen[0];
   $id = 0;
   $mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
        $mannschaftsNummer++;
        echo "in der for schleife";
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
            echo "in der for schleife";
           $id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           $id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           $mannschaftTT=0;
           $mannschaftTT = $klasse .  $mannschaftsNummer;
           $mannschaftTT= '"' . $mannschaftTT .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftTT;
            $addSportartToSchueler1 = "UPDATE Mannschaften_MS SET TT = " . $mannschaftTT . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE Mannschaften_MS SET TT = " . $mannschaftTT . " WHERE ID = ". $id2 . " ";
         //  echo $mannschaftFB2;
             echo $addSportartToSchueler1;
            echo $addSportartToSchueler2;
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
     
 
 $conn->close();
 
	?>
</body>
</html>
