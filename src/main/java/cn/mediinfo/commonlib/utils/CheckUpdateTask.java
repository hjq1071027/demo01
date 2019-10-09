package cn.mediinfo.commonlib.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.mediinfo.commonlib.R;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;
    private boolean mShowProgressDialog;
    private static final String url = Constants.UPDATE_URL;

    CheckUpdateTask(Context context, int type, boolean showProgressDialog) {
        this.mContext = context;
        this.mType = type;
        this.mShowProgressDialog = showProgressDialog;

    }


    @Override
    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.android_auto_update_dialog_checking));
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONArray versionArray = new JSONArray(result);
            JSONObject newVersion = versionArray.getJSONObject(0);
            String updateMessage = newVersion.getString(Constants.APK_UPDATE_CONTENT);
            //String apkUrl = newVersion.getString(Constants.APK_DOWNLOAD_URL);
            // String apkUrl = "http://192.168.1.108:8080/ydhl.apk";
            String apkUrl = "http://192.168.18.216/ydhl.apk";
            //  String apkUrl = "http://zhibao.zjtpyun.com/customer.apk";
            int apkCode = newVersion.getInt(Constants.APK_VERSION_CODE);

            int versionCode = AppUtils.getVersionCode(mContext);

            if (apkCode > versionCode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // skipGooglePlayUpdate();
                    showDialog(mContext, updateMessage, apkUrl);
                } else {
                    if (mType == Constants.TYPE_NOTIFICATION) {
                        showNotification(mContext, updateMessage, apkUrl);
                    } else if (mType == Constants.TYPE_DIALOG) {
                        showDialog(mContext, updateMessage, apkUrl);
                    }
                }
            } else if (mShowProgressDialog) {
                Toast.makeText(mContext, mContext.getString(R.string.android_auto_update_toast_no_new_update), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }

    private void skipGooglePlayUpdate() {

        try {
            Uri uri = Uri.parse("market://details?id="+ "packName");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "你的手机没有安装应用商城", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Show dialog
     */
    private void showDialog(Context context, String content, String apkUrl) {
        UpdateDialog.show(context, content, apkUrl);
    }

    /**
     * Show Notification
     */
    private void showNotification(Context context, String content, String apkUrl) {
        Intent myIntent = new Intent(context, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = context.getApplicationInfo().icon;
        Notification notify = new NotificationCompat.Builder(context)
                .setTicker(context.getString(R.string.android_auto_update_notify_ticker))
                .setContentTitle(context.getString(R.string.android_auto_update_notify_content))
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    protected String doInBackground(Void... args) {
        return HttpUpdateUtils.get(url);
    }
}

