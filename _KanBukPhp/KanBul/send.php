<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> <!--Türkçe karakter sorunu yaşamamak için-->

 <title>GCM Send</title>
</head>
<body>
 <?php
  if(isset($_POST['submit'])){//kontrol
   require_once("config.php");//database bağlantısı gercekleştirdik
   $registatoin_ids = array();//registration idlerimizi tutacak array ı oluşturuyoruz
   
   $sql = "SELECT  registration_id FROM tbl_gcm_kullanicilar where id=12";//Tüm kullanıcı gcm registration idlerini alıcak sql sorgumuz
   $result = mysqli_query($con, $sql);//sorguyu çalıştırıyoruz
   while($row = mysqli_fetch_assoc($result)){
    array_push($registatoin_ids, $row['registration_id']);//databaseden dönen registration idleri $registatoin_ids arrayine atıyoruz
   }
 
   // GCM servicelerine gidecek veri
   //Arkadaşlar aşşağıdaki PHP kodlarıyla oynamıyoruz. Bu Google 'n bizden kullanmamızı istediği kodlar
   //Sadece registration_ids,mesaj ve Authorization: key değerlerini değiştiriyoruz
    $url = 'https://android.googleapis.com/gcm/send';
   // $url = 'https://gcm-http.googleapis.com/gcm/send';
   
    $mesaj = array("notification_message" => $_POST['mesaj']); //gönderdiğimiz mesaj POST 'tan alıyoruz.Androidde okurken notification_message değerini kullanacağız
         $fields = array(
             'registration_ids' => $registatoin_ids,
             'data' => $mesaj,
         );
		
		//Alttaki Authorization: key= kısmına Google Apis kısmında oluşturduğumuz key'i yazacağız
         $headers = array(
             'Authorization: key=GCM Key', 
             'Content-Type: application/json'
         );
         // Open connection
         $ch = curl_init();
   
         // Set the url, number of POST vars, POST data
         curl_setopt($ch, CURLOPT_URL, $url);
   
         curl_setopt($ch, CURLOPT_POST, true);
         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
   
         // Disabling SSL Certificate support temporarly
         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
   
         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
   
         // Execute post
         $result = curl_exec($ch);
         if ($result === FALSE) {
             die('Curl failed: ' . curl_error($ch));
         }
   
         // Close connection
         curl_close($ch);
         echo $result;
  }
 ?>
 
 <form method="post" action="send.php">
  <label>Mesajı giriniz: </label><input type="text" name="mesaj" />
 
  <input type="submit" name="submit" value="Send" />
 </form>
</body>
</html>
