package cn.mediinfo.commonlib.net.okhttp;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.mediinfo.commonlib.net.ReqApi;
import cn.mediinfo.commonlib.net.getTokenCallBack;
import cn.mediinfo.commonlib.utils.SharedPreferenceUtil;

/**
 */

public class MyOkhttpUtils {

    public  final String TAG = "MyOkhttpUtils";
    public String BaseUrl = ReqApi.BASE_URL;
    public static MyOkhttpUtils instance;

    public static MyOkhttpUtils getInstance() {
        if (instance == null) {
            synchronized (MyOkhttpUtils.class) {
                if (instance == null) {
                    instance = new MyOkhttpUtils();
                }
            }
        }
        return instance;
    }

    public void doPostAsync(final Activity activity, String url, HashMap<String, Object> mapParams, final MyOkhttpCallBack myOkhttpCallBack, final getTokenCallBack tokenCallBack ){

//        String head = "Bearer "+ MySharedPreference.get("access_token", "", activity);
        String head = ReqApi.tk;
        String json = new Gson().toJson(mapParams);
        OkHttpUtil.Builder().setCacheType(CacheType.FORCE_NETWORK).build(activity)
                .doPostAsync(HttpInfo.Builder().setUrl(BaseUrl + url)
                        .addParamJson(json).addHead("Token", head).build(), new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        myOkhttpCallBack.onResponse(info);
                        myOkhttpCallBack.onFinish();
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        if(info.getNetCode() == 401){
                            getToken(activity,tokenCallBack);
                            return;
                        }
                        myOkhttpCallBack.onFailure(info);
                        myOkhttpCallBack.onFinish();
                    }
                });

    }

    public void doPostAsync(final Activity activity, String url, HashMap<String, Object> mapParams, final MyOkhttpCallBack myOkhttpCallBack){

//        String head = "Bearer "+ MySharedPreference.get("access_token", "", activity);
        String head = ReqApi.tk;
        String json = new Gson().toJson(mapParams);
        OkHttpUtil.Builder().setCacheType(CacheType.FORCE_NETWORK).build(activity)
                .doPostAsync(HttpInfo.Builder().setUrl(BaseUrl + url)
                        .addParamJson(json).addHead("Token", head).build(), new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        myOkhttpCallBack.onResponse(info);
                        myOkhttpCallBack.onFinish();
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        myOkhttpCallBack.onFailure(info);
                        myOkhttpCallBack.onFinish();
                    }
                });

    }
    public void doGetAsync(final Activity activity, String url, HashMap<String, String> mapParams, final MyOkhttpCallBack myOkhttpCallBack, final getTokenCallBack tokenCallBack ){

        String head = "Bearer "+ SharedPreferenceUtil.get("access_token", "", activity);
        String json = new Gson().toJson(mapParams);
        OkHttpUtil.Builder().setCacheType(CacheType.FORCE_NETWORK).build(activity)
                .doGetAsync(HttpInfo.Builder().setUrl(BaseUrl + url)
                        .addParams(mapParams).addHead("Authorization", head).build(), new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        myOkhttpCallBack.onResponse(info);
                        myOkhttpCallBack.onFinish();
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        Log.d(TAG,info.getRetDetail());
                        if(info.getNetCode() == 401){
                            getToken(activity,tokenCallBack);
                            return;
                        }
                        myOkhttpCallBack.onFailure(info);
                        myOkhttpCallBack.onFinish();
                    }
                });

    }


    /**
     * 获取token
     * @param activity
     * @param tokenCallBack
     */
    void getToken(final Activity activity, final getTokenCallBack tokenCallBack){
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("grant_type","client_credentials");
        mapParams.put("client_id","JobClient");
        mapParams.put("client_secret", ReqApi.AppSecret);
        OkHttpUtil.Builder().setCacheType(CacheType.FORCE_NETWORK).build(this)
                .doPostAsync(HttpInfo.Builder().setUrl(ReqApi.tokenUrl)
                        .addParams(mapParams).build(), new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result = new String(info.getRetDetail());
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            String access_token = jsonObject.getString("access_token");
                            SharedPreferenceUtil.save("access_token",access_token,activity);
                            tokenCallBack.onTokenResponse();

                        } catch (JSONException e) {
                            e.printStackTrace();

                            tokenCallBack.onTokenError();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        Log.d(">>>",info.toString());
                        tokenCallBack.onTokenError();

                    }
                });

    }

}
