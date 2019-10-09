package cn.mediinfo.commonlib.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class AppUtils {
    //com.YiDongCF 安装apk的包名
    public static String APK_URL_PACKAGE = "package:com.YiDongCF";

    public static int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    public static String getVersionName(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        return "";
    }
}
