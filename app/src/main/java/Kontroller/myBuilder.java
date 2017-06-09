package Kontroller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class myBuilder  implements DialogInterface.OnClickListener{

    AlertDialog.Builder builder;
    static myBuilder instance;
    static String _title,_message;
    private  myBuilder() {}

    public AlertDialog.Builder getBuilder(Context context)
    {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(_title);
        builder.setMessage(_message);
        builder.setCancelable(false);
        builder .setPositiveButton("Tamam", this);
        builder.create();
        return  builder;
    }

    public static myBuilder getInstance(String title,String message)
    {
        _message=message;
        _title=title;
        if(instance ==null)
        {
            instance =new myBuilder();
        }
        return instance;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(builder == dialog)
        {
            Log.d("myBuilder", _message);
        }
    }
}
