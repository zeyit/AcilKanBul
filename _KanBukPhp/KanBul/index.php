<?php

	require_once("config.php");

	$sayfa =temizle(@$_GET["sayfa"]);

	if($sayfa == "kisi_kaydet")
	{
		kisi_kaydet();
	}else if($sayfa == "kan_ara")
	{
		kan_ara();
	}else if($sayfa == "mesaj_gonder")
	{
		$gID =temizle($_POST["g_id"]);
		$aID =temizle($_POST["a_id"]);
		$msg =temizle($_POST["mesaj"]);
		global $con;
		$query ="insert into tbl_mesaj(g_id,a_id,mesaj,durum) values($gID,$aID,'$msg',0)";
		if(mysqli_query($con,$query))
		{
			$query ="select ad,soyad from tbl_gcm_kullanicilar where id="$gID;
			$sonuc =mysql_query($con,$query);
			if($sonuc)
			{
				$row =mysql_fetch_array($sonuc);
				$adi =$row["ad"];
				$soyadi =$row["soyad"];
				
				$mesajim="{'adi':'$adi','soyadi':'$soyadi','aid':$gID,'aid':aID,'mesaj':'$msg'}'";
				mesajGonder($aID,$mesajim);
			}else
			{
				echo "{'durum':false}";	
			}
			
		}
		else{
			echo "{'durum':false}";
		}
	}

	function temizle($txt)
	{
		return htmlspecialchars($txt);
	}

	function kisi_kaydet()
	{
		global $con;
		$ad =temizle($_POST["ad"]);
		$soyad =temizle($_POST["soyad"]);
		$tel =temizle($_POST["tel"]);
			
		$kan =temizle($_POST["kan"]);
		$il =temizle($_POST["il"]);
		$ilce =temizle($_POST["ilce"]);
		$query="";
		if(isset($_POST["tel_id"]))
		{
			$tel_id =temizle($_POST["tel_id"]);
			$query="insert into tbl_gcm_kullanicilar(ad,soyad,tel,kan,il,ilce,registration_id) value('$ad','$soyad','$tel',$kan,$il,$ilce,'$tel_id')";
		}else
		{
			$id =temizle($_POST["user_id"]);
			$query="update tbl_gcm_kullanicilar set ad='$ad',soyad='$soyad' , tel='$tel', kan=$kan,il=$il,ilce=$ilce where id=".$id;
		}
			
		$sonuc =mysqli_query($con,$query);
		if(isset($_POST["tel_id"]))
		{
			if($sonuc)
			{
				echo json_encode(array('isSave'=>true,'id'=>mysqli_insert_id($con)));
			}else
			{
				echo json_encode(array('isSave'=>false,'id'=>-1));
			}
		}else
		{
			if($sonuc)
			{
				echo json_encode(array('isSave'=>true));
			}else
			{
				echo json_encode(array('isSave'=>false));
			}
		}
	}

	function kan_ara()
	{
		$il =temizle($_POST["il"]);
		$ilce =temizle($_POST["ilce"]);
		$kan =temizle($_POST["kan"]);
		
		global $con;
		$query="Select K.id,K.ad,K.soyad,K.tel,G.kan_grubu from tbl_gcm_kullanicilar as K inner join tbl_kangrubu as G on K.kan=G.id where il=$il and ilce =$ilce ";
		if($kan == "1")
		{
			$query .="and (K.kan = 1 or K.kan=2)";
		}
		else if($kan == "2")
		{
			$query .="and K.kan = 1";
		}
		else if($kan == "3")
		{
			$query .="and (K.kan = 1 or K.kan = 2 or K.kan= 3 or K.kan = 4)";
		}
		else if($kan == "4")
		{
			$query .="and (K.kan = 2 or K.kan = 4)";
		}
		else if($kan == "5")
		{
			$query .="and (K.kan = 1 or K.kan=2 or K.kan = 5 or K.kan=6)";
		}
		else if($kan == "6")
		{
			$query .="and (K.kan = 2 or K.kan = 6)";
		}
		else if($kan == "7")
		{
			$query .="and (K.kan = 1 or K.kan = 3 or K.kan = 5 or K.kan = 5)";
		}

		$sonuc =mysqli_query($con,$query);
		if($sonuc)
		{
			$kayit_sayisi =mysqli_num_rows($sonuc);
			echo "{'kayit':$kayit_sayisi,'kisi':[";
				while($row =mysqli_fetch_array($sonuc))
				{
					echo json_encode($row).",";
				}
			echo "]}";
		}

	}

	function mesajGonder($id,$msg)
	{
		global $con;
		$registatoin_ids = array();//registration idlerimizi tutacak array ı oluşturuyoruz
	   
	   $sql = "SELECT  registration_id FROM gcm_kullanicilar where id=$id";//Tüm kullanıcı gcm registration idlerini alıcak sql sorgumuz
	   $result = mysqli_query($con, $sql);//sorguyu çalıştırıyoruz
	   while($row = mysqli_fetch_assoc($result)){
	    array_push($registatoin_ids, $row['registration_id']);//databaseden dönen registration idleri $registatoin_ids arrayine atıyoruz
	   }
	 
	     $url = 'https://android.googleapis.com/gcm/send';
	   
	    $mesaj = array("notification_message" => $msg); //gönderdiğimiz mesaj POST 'tan alıyoruz.Androidde okurken notification_message değerini kullanacağız
	         $fields = array(
	             'registration_ids' => $registatoin_ids,
	             'data' => $mesaj,
	         );
			
			//Alttaki Authorization: key= kısmına Google Apis kısmında oluşturduğumuz key'i yazacağız
	         $headers = array(
	             'Authorization: key=AIzaSyB-o1gXJzJ5zSpjd4DbjzwJsQByjpZp3bs', 
	             'Content-Type: application/json'
	         );

	         $ch = curl_init();
	   
	         curl_setopt($ch, CURLOPT_URL, $url);
	   
	         curl_setopt($ch, CURLOPT_POST, true);
	         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	   
	         // Disabling SSL Certificate support temporarly
	         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	   
	         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

	         $result = curl_exec($ch);
	         if ($result === FALSE) {
	             die('Curl failed: ' . curl_error($ch));
	         }

	         curl_close($ch);
         echo "{'durum':true}";
	  }
?>