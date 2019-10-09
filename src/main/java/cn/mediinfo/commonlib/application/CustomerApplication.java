package cn.mediinfo.commonlib.application;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;

import cn.mediinfo.commonlib.net.okhttp.HttpInterceptor;
import cn.mediinfo.commonlib.utils.ToastUtil;

/********************************
 * @author hujq
 * @date 2019/7/17
 * @cp 联众智慧科技股份有限公司
 * @TODO
 *********************************/

public class CustomerApplication extends Application{
    //true 显示 false 隐藏
    private boolean isDisplayLog = true;
    @Override
    public void onCreate() {
        super.onCreate();

        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/mns_okHttp_download/";
        String cacheDir = Environment.getExternalStorageDirectory().getPath()+"/mns_okHttp_cache";
        OkHttpUtil.init(this)
                //连接超时时间
                .setConnectTimeout(15)
                //写超时时间
                .setWriteTimeout(15)
                //读超时时间
                .setReadTimeout(15)
                //缓存空间大小
                .setMaxCacheSize(50 * 1024 * 1024)
                //缓存类型
                .setCacheType(CacheType.FORCE_NETWORK)
                //设置请求日志标识
                .setHttpLogTAG("HttpLog")
                //Gzip压缩，需要服务端支持
                .setIsGzip(false)
                //显示请求日志
                .setShowHttpLog(true)
                //显示Activity销毁日志
                .setShowLifecycleLog(false)
                //失败后不自动重连
                .setRetryOnConnectionFailure(false)
                //设置缓存目录
                .setCachedDir(new File(cacheDir))
                //文件下载保存目录
                .setDownloadFileDir(downloadFileDir)
                //设置全局的服务器响应编码
                .setResponseEncoding(Encoding.UTF_8)
                //设置全局的请求参数编码
                .setRequestEncoding(Encoding.UTF_8)
                //设置全局Https证书
//                .setHttpsCertificate("12306.cer")
                //请求结果拦截器
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)
                //请求链路异常拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)
                //持久化cookie
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))
                .build();
                ToastUtil.init(this);
        //初始化logger
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(3)
                .methodOffset(5)
                .tag("logTag")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return isDisplayLog;
            }
        });
    }
}
