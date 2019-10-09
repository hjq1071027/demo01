package cn.mediinfo.commonlib.utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.mediinfo.commonlib.BuildConfig;
import cn.mediinfo.commonlib.R;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class DownloadService extends IntentService {
    // 8k ~ 32K
    private static final int BUFFER_SIZE = 10 * 1024;
    private static final String TAG = "DownloadService";

    private static final int NOTIFICATION_ID = 100;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;

        mBuilder.setContentTitle(appName).setSmallIcon(icon);
        String urlStr = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);
        Log.i(TAG, "urlStr----------: "+urlStr);
        InputStream in = null;
        FileOutputStream out = null;
        try {
//            urlStr = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            urlConnection.connect();
            int reCode = urlConnection.getResponseCode();
            if (reCode == 200){
                long bytetotal = urlConnection.getContentLength();
                long bytesum = 0;
                int byteread = 0;
                in = urlConnection.getInputStream();
                File dir = StorageUtils.getCacheDirectory(this);
                String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
                File apkFile = new File(dir, apkName);
                out = new FileOutputStream(apkFile);
                byte[] buffer = new byte[BUFFER_SIZE];

                int oldProgress = 0;

                while ((byteread = in.read(buffer)) != -1) {
                    bytesum += byteread;
                    out.write(buffer, 0, byteread);
                    //Log.i(TAG, "onHandleIntent: --------");
                    int progress = (int) (bytesum * 100L / bytetotal);
                    // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
                    if (progress != oldProgress) {
                        updateProgress(progress);
                    }
                    oldProgress = progress;
                }
                // 下载完成

                installAPk(apkFile);

                mNotifyManager.cancel(NOTIFICATION_ID);
            }

        } catch (Exception e) {
            //Log.e(TAG, "download apk file err"+e.getMessage());
            if (urlStr.contains("http://")) {
                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(urlStr);
                intent2.setData(content_url);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                Uri packageURI = Uri.parse(AppUtils.APK_URL_PACKAGE);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {

                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }
    }

    private void updateProgress(int progress) {
        //"正在下载:" + progress + "%"
        mBuilder.setContentText(this.getString(R.string.android_auto_update_download_progress, progress))
                .setProgress(100, progress, false);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private void installAPk(File apkFile) {
        Toast.makeText(getApplicationContext(),""+apkFile,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);

        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
            Toast.makeText(getApplicationContext(),ignored.getMessage(),Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Log.e(TAG, "11111");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Log.e(TAG, "2222222");
        }
        startActivity(intent);
        // Log.e(TAG, "download apk file success");

    }

}
