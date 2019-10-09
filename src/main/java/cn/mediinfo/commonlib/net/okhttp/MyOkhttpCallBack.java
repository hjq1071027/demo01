package cn.mediinfo.commonlib.net.okhttp;

import com.okhttplib.HttpInfo;

/**
 * Created by yansy on 2018/12/19.
 */

public interface MyOkhttpCallBack {
    public void onFailure(HttpInfo info);//接口返回错误信息
    public void onResponse(HttpInfo info);
    public void onFinish();

}
