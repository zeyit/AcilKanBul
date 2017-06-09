package Kontroller;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class Network{

    static Network network =null;
    private Network() {}

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public AlertDialog.Builder getBuilder(Context context)
    {
        return  myBuilder.getInstance("Network","İnternet bağlantınız yok").getBuilder(context);
    }

    public static Network getInstance()
    {
        if(network ==null)
        {
            network =new Network();
        }
        return network;
    }

}
