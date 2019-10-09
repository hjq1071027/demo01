package cn.mediinfo.commonlib.net;

/**
 * Created by yansy on 2018/10/19.
 */

public interface getTokenCallBack {

    public void onTokenError();//获取token失败
    public void onTokenResponse();//获取token成功

}
