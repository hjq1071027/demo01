package cn.mediinfo.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;

import cn.mediinfo.commonlib.bean.Update;
import cn.mediinfo.commonlib.net.ReqApi;
import cn.mediinfo.commonlib.net.okhttp.MyOkhttpCallBack;
import cn.mediinfo.commonlib.net.okhttp.MyOkhttpUtils;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO 版本更新
 *********************************/
public class UpdateChecker {
    public static List<Update> mUpdateList;
    public static void checkForDialog(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }
    public static void checkForNotification(final Context context) {
        if (context != null) {
            /**
             * 请求网络接口获取版本号
             */
            HashMap<String, Object> paramMap = new HashMap<>();
            List<String> dataList = new ArrayList<>();
            dataList.add("移动护理升级");
            paramMap.put("shuJuZDIdList", dataList);
            MyOkhttpUtils.getInstance().doPostAsync((Activity) context, ReqApi.getCommonData, paramMap, new MyOkhttpCallBack() {
                public List<Update> mUpdateList;
                @Override
                public void onFailure(HttpInfo info) {
                    if (TextUtils.isEmpty(info.getRetDetail())) {
                        ToastUtil.showS("服务器异常");
                    } else {
                        ToastUtil.showS(info.getRetDetail());
                    }
                }
                @Override
                public void onResponse(HttpInfo info) {
                    String str = info.getRetDetail();
                    mUpdateList = new ArrayList<>();
                    if (!TextUtils.isEmpty(str)) {
                        try {
                            JSONObject jsObj = new JSONObject(str);
                            String objStr = jsObj.getString("Return");
                            JSONObject obj = new JSONObject(objStr);
                            String upgradeStr =  obj.getString("移动护理升级");
                            mUpdateList = new Gson().fromJson(upgradeStr, new TypeToken<List<Update>>() {
                            }.getType());

                            if (mUpdateList != null && mUpdateList.size() > 0) {
                                if (AppUtils.getVersionCode(context) < mUpdateList.get(0).getVersioncode()) {
                                   // showDialog(context, mUpdateList.get(0).getVersion_info(), mUpdateList.get(0).getUpgradefile());
                                    UpgradeNoticeDlg.showNoticeDialog(context,mUpdateList.get(0));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFinish() {}
            });

        } else {
            Log.e(Constants.TAG, "The arg context is null");
        }
    }
    private static void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }
}
