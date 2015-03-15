<!DOCTYPE html>
<?php session_start(); ?>
<html>
<head>
<meta charset='UTF-8' name='viewport' content='width=device-width' />
<title>Eingabe Klassenlisten @ Sporttage-Kepler</title>
</head>

<body>
	<h1>Eingaben überprüfen</h1>
	<?php
	
	$klasse = $_POST['klasse'];
  //k1 in session speichern
  $k1 = $_POST['k1'];
  $_SESSION['k1'] = $k1;
  
	// Klasse ausgeben und in Session speichern
	echo 'Klasse: ';
  if($k1) {
  echo "K1-";
  }
  echo $klasse . '<br>';
	echo '<br>';

	$_SESSION['klasse'] = $klasse;
	
	//Fußball1
	$vornamen = $_POST['fb1_v'];
	$nachnamen = $_POST['fb1_n'];
	$_SESSION['fb1_v'] = $vornamen; // In session abspeichern
	$_SESSION['fb1_n'] = $nachnamen;

	
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
	$_SESSION['fb2_v'] = $vornamen;
	$_SESSION['fb2_n'] = $nachnamen;

	
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
	$_SESSION['bb_v'] = $vornamen; // In session abspeichern
	$_SESSION['bb_n'] = $nachnamen;
	
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
	$_SESSION['vb_v'] = $vornamen; // In session abspeichern
	$_SESSION['vb_n'] = $nachnamen;
	
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
	$_SESSION['st_v'] = $vornamen; // In session abspeichern
	$_SESSION['st_n'] = $nachnamen;
	
	echo '<b> Staffellauf </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';
	
	// Badminton
	$vornamen = $_POST['bm_v'];
	$nachnamen = $_POST['bm_n'];
	$_SESSION['bm_v'] = $vornamen; // In session abspeichern
	$_SESSION['bm_n'] = $nachnamen;
	
	echo '<b> Badminton </b><br>';
	$mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
		$mannschaftsNummer++;
		if ($vornamen[$i] != '' && $nachnamen[$i] != '' && $vornamen[$i - 1] != '' && $nachnamen[$i - 1] != '')  {
	        	echo 'Team ' . $mannschaftsNummer . ': ';
			// Spieler 1
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . ' &';
			// Spieler 2
			echo $vornamen[$i - 1] . ' ';
			echo $nachnamen[$i - 1] . '<br>';
		}
	}
	echo '<br>';

	// Tischtennis
	$vornamen = $_POST['tt_v'];
	$nachnamen = $_POST['tt_n'];
	$_SESSION['tt_v'] = $vornamen; // In session abspeichern
	$_SESSION['tt_n'] = $nachnamen;
	
	echo '<b> Tischtennis </b><br>';
	$mannschaftsNummer=0;
     	for($i = 1; $i < count($vornamen); $i+=2) {
		$mannschaftsNummer++;
		if ($vornamen[$i] != '' && $nachnamen[$i] != '' && $vornamen[$i - 1] != '' && $nachnamen[$i - 1] != '')  {
	        	echo 'Team ' . $mannschaftsNummer . ': ';
			// Spieler 1
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . ' & ';
			// Spieler 2
			echo $vornamen[$i - 1] . ' ';
			echo $nachnamen[$i - 1] . '<br>';
		}
	}
	echo '<br>';
	
	// Fahrradtour
	$vornamen = $_POST['ft_v'];
	$nachnamen = $_POST['ft_n'];
	$_SESSION['ft_v'] = $vornamen; // In session abspeichern
	$_SESSION['ft_n'] = $nachnamen;
	
	echo '<b> Fahrradtour </b><br>';
   
	for($i = 0; $i < count($vornamen); $i++) {
		if ($vornamen[$i] != '' && $nachnamen[$i] != '')  {
			echo $vornamen[$i] . ' ';
			echo $nachnamen[$i] . '<br>';
		}
	}
	echo '<br>';?>
	
	<form action='submit.php' method='post'>
		<input type='submit' value='Alles richtig!'>
	</form>
</body>
</html>
