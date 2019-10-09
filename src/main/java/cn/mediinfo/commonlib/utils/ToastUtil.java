package cn.mediinfo.commonlib.utils;

import android.content.Context;
import android.widget.Toast;

/********************************
 * @author hujq
 * @date 2019/4/9
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class ToastUtil {
    private static  ToastUtil util;
    private Context context;
    private ToastUtil(Context context){
        this.context = context;
    }


    public static ToastUtil init(Context context){
        if (util == null){
            util = new ToastUtil(context);
        }
        return util;
    }


    public static void showS(String string){
        if (util!=null){
            Toast.makeText(util.context, string, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showL(String string){
        if (util!=null){
            Toast.makeText(util.context, string,Toast.LENGTH_LONG).show();
        }
    }

}
