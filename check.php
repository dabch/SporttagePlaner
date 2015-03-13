<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingaben Überprüfen @ Sporttage-Kepler</title>
</head>

<body>
	<h1>Eingaben überprüfen</h1>
	<?php
	
	$klasse = $_POST['klasse'];
	
	// Klasse ausgeben
	echo 'Klasse: ' . $klasse . '<br>';
	echo '<br>';
	
	//Fußball1
	$vornamen = $_POST['fb1_v'];
	$nachnamen = $_POST['fb1_n'];
	
	echo '<b> Fußball 1 </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
	
	//Fußball2
	$vornamen = $_POST['fb2_v'];
	$nachnamen = $_POST['fb2_n'];
	
	echo '<b> Fußball 2 </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
	
	// Basketball
	$vornamen = $_POST['bb_v'];
	$nachnamen = $_POST['bb_n'];
	
	echo '<b> Basketball </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
		
	// Volleyball
	$vornamen = $_POST['vb_v'];
	$nachnamen = $_POST['vb_n'];
	
	echo '<b> Volleyball </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
	
	// Staffellauf
	$vornamen = $_POST['st_v'];
	$nachnamen = $_POST['st_n'];
	
	echo '<b> Staffellauf </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';

	// Tischtennis
	$vornamen = $_POST['tt_v'];
	$nachnamen = $_POST['tt_n'];
	
	echo '<b> Tischtennis </b><br>';
	$mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
        $mannschaftsNummer++;
        echo '<b>Team 1:</b> ';
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
		// Spieler 1
        	echo $vornamen[$i] . ' ';
        	echo $nachnamen[$i] . ' &';
        	// Spieler 2
        	echo $vornamen[$i - 1] . ' ';
		echo $nachnamen[$i - 1] . '<br>';
	}
     
       //bm
  $vornamen = $_POST['bm_v'];
	$nachnamen = $_POST['bm_n'];
	echo '<br>';
	echo $klasse . '<br>'; 
  // echo $vornamen[0];
   $id = 0;
   $mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
        $mannschaftsNummer++;
       // echo "in der for schleife";
        if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
       //     echo "in der for schleife";
           $id1 = getSchuelerID($nachnamen[$i], $vornamen[$i], $klasse);
           $id2 = getSchuelerID($nachnamen[$i-1], $vornamen[$i-1], $klasse);
           $mannschaftBM=0;
           $mannschaftBM = $klasse .  $mannschaftsNummer;
           $mannschaftBM= '"' . $mannschaftBM .  '"';
   
            echo $id1;
            echo $id2;
            echo $mannschaftBM . "<br>";
            $addSportartToSchueler1 = "UPDATE Mannschaften_MS SET BM = " . $mannschaftBM . " WHERE ID = ". $id1 . " ";
            $addSportartToSchueler2 = "UPDATE Mannschaften_MS SET BM = " . $mannschaftBM . " WHERE ID = ". $id2 . " ";
         //  echo $mannschaftFB2;
         //    echo $addSportartToSchueler1;
         //   echo $addSportartToSchueler2;
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
	
	// Fahrradtour
	$vornamen = $_POST['ft_v'];
	$nachnamen = $_POST['ft_n'];
	
	echo '<b> Fahrradtour </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
	?>
</body>
</html>
