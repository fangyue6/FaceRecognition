package com.ai.face;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * <b>Project:</b> FaceRecognition <br>
 * <b>Create Date:</b> 2018/12/23 <br>
 * <b>@author:</b> qy <br>
 * <b>Address:</b> qingyongai@gmail.com <br>
 * <b>Description:</b>  <br>
 */
public class App extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
    }

    /**
     * 网络框架
     */
    private void initHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("[Http]");
        HttpLoggingInterceptor.Level level;
        level = HttpLoggingInterceptor.Level.BODY;
        loggingInterceptor.setPrintLevel(level);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(15000L, TimeUnit.MILLISECONDS);
        builder.writeTimeout(15000L, TimeUnit.MILLISECONDS);
        builder.connectTimeout(15000L, TimeUnit.MILLISECONDS);
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        // 其他统一的配置
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(0);
    }

}
