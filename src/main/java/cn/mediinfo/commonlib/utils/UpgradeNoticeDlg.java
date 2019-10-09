package cn.mediinfo.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;

import cn.mediinfo.commonlib.R;
import cn.mediinfo.commonlib.bean.Update;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class UpgradeNoticeDlg extends LibBaseDialog implements View.OnClickListener{

    private Button o_okBtn;
    private Button o_cancelBtn;
    private Update update;
    private TextView o_versionTv;
    private TextView o_sizeTv;
    private TextView o_infoTv;
    private TextView o_progressTv;
    private LinearLayout o_progressLayout;
    private ProgressBar o_progress;
    private Context mContext;

    public UpgradeNoticeDlg(Context context, int width, int height, Update update) {
        super(context, width, height, R.layout.dialog_upgrade_notice, 3);
        // TODO Auto-generated constructor stub
        UpgradeNoticeDlg.this.update = update;
        this.mContext = context;
    }

    public UpgradeNoticeDlg(Context context, int width, int height, int theme, Update update) {
        super(context, width, height, R.layout.dialog_upgrade_notice, theme);
        // TODO Auto-generated constructor stub
        UpgradeNoticeDlg.this.update = update;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        o_cancelBtn = (Button) UpgradeNoticeDlg.this.findViewById(R.id.upgrade_cancel);
        o_okBtn = (Button) UpgradeNoticeDlg.this.findViewById(R.id.upgrade_ok);

        o_versionTv = (TextView) UpgradeNoticeDlg.this.findViewById(R.id.upgrade_version);
        o_sizeTv = (TextView) UpgradeNoticeDlg.this.findViewById(R.id.upgrade_size);
        o_infoTv = (TextView) UpgradeNoticeDlg.this.findViewById(R.id.upgrade_info);

        o_versionTv.setText("最新版本：" + UpgradeNoticeDlg.this.update.getVersionname());
        String sizeString = String.format("%.2f", UpgradeNoticeDlg.this.update.getFilesize() / 1024f / 1024f);

        o_sizeTv.setText("新版本大小: " + sizeString + "M");
        o_infoTv.setText(UpgradeNoticeDlg.this.update.getVersion_info());

        o_cancelBtn.setOnClickListener(UpgradeNoticeDlg.this);
        o_okBtn.setOnClickListener(UpgradeNoticeDlg.this);

        o_progress = (ProgressBar) findViewById(R.id.update_progress);
        o_progressLayout = (LinearLayout) findViewById(R.id.update_progress_ll);
        o_progressTv = (TextView) findViewById(R.id.upgrade_tv);

        final String pathString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ydhl" + update.getVersioncode() + ".apk";
        File file = new File(pathString);
        if (file.exists()) {
            if (file.length() == update.getFilesize()) {
                o_okBtn.setText("安装");
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == o_cancelBtn) {

            this.dismiss();


        } else if (v == o_okBtn) {
            this.dismiss();
            goToDownload(mContext, update.getUpgradefile());
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }


    public static void showNoticeDialog(Context mContext,Update update) {
        int gw = DensityUtils.getScreenW(mContext);
        int gh = DensityUtils.getScreenH(mContext);
        Logger.i("-----gw:" + gw + "gh:" + gh);
        int w = 560;
        if (gw < 560) {
            w = gw;
        } else {
            w = 560 + (int) (0.3 * (gw - 560));
        }

        UpgradeNoticeDlg dlg = new UpgradeNoticeDlg(mContext, w, LinearLayout.LayoutParams.WRAP_CONTENT, R.style.CustomDialogStyle, update);
        dlg.setCancelable(false);
        dlg.show();

    }

}