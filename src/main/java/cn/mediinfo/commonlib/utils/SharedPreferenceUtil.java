package cn.mediinfo.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class SharedPreferenceUtil {

    public static void save(String name, String value, Context context) {
        if(context == null){
            return;
        }
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void saveBitmapToSharedPreferences(String key, Bitmap bitmap,
                                                     Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray,
                Base64.DEFAULT));


        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, imageString);
        editor.commit();
    }


    public static Bitmap getBitmapFromSharedPreferences(String key, String def,
                                                        Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);

        String imageString = mySharedPreferences.getString(key, def);

        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                byteArray);

        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }

    public static String get(String name, String defvalue, Context context) {
        if (context == null) {
            return "";
        }
        if (name == null) {
            return "";
        }
        if (defvalue == null) {
            return "";

        }
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        if (mySharedPreferences == null) {
            return "";
        } else
            return mySharedPreferences.getString(name, defvalue);
    }

    public static void clear(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "text", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove("userId");
        editor.clear();
        editor.commit();
    }
}
