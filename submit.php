<?p
$servername = 'wp052.webpack.hosteurope.de';
$username = 'db1093417-sport';
$password = '%&SporTTage14@!';
$dbname = 'db1093417-sporttage';

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

function getSchuelerID($name, $vorname, $klasse) {
    $abfrageSchueler = $conn->prepare("SELECT ID, Vorname, Name FROM Mannschaften_MS WHERE Vorname = ? AND Name = ? AND Klasse = ?");
    $abfrageSchueler->bind_param("sss", $vorname, $name, $klasse);
    $result = $conn->query($abfrageSchueler);
    if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $id = $row["id"];
    }
} else {
    //neu erstellen
}
}

// prepare and bind
$insertSchueler = $conn->prepare("INSERT INTO MyGuests (ID, Name, Vorname, Klasse, BM, FB, BB, VB, TT, ST, FT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
$insertSchueler->bind_param("sss", $id, $name, $vorname, $klasse, $bm, $fb, $bb, $vb, $tt, $st, $ft);

// set parameters and execute
$name = $_POST['']
$vorname
$klasse
$bm
$fb
$bb
$vb
$tt
$st
$ft

$firstname = "John";
$lastname = "Doe";
$email = "john@example.com";
$insertSchueler->execute();

echo "New records created successfully";

$stmt->close();
$conn->close();

?>
