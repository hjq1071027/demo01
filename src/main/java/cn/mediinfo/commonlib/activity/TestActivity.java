package cn.mediinfo.commonlib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.mediinfo.commonlib.utils.UpdateChecker;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class TestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        checkUpdateVersion();
    }

    /**
     * 检查版本升级
     */
    private void checkUpdateVersion() {
        UpdateChecker.checkForNotification(TestActivity.this);
    }
}
