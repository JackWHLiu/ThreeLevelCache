package com.example.threelevelcache.http;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * 简单封装一下，知道怎么回事就好，主要是代码语法上不报错。
 */
public class RetrofitFactory {

    private static RetrofitFactory sInstance;
    private Retrofit mRetrofit;

    /**
     * 服务器的地址。
     */
    private static final String BASE_URL = "";

    /**
     * Retrofit API缓存池。
     */
    private Map<Class<?>, Object> mServiceMap = new ConcurrentHashMap<>();

    private RetrofitFactory() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(initOkHttp())
                .build();
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("referer", BASE_URL)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                });
        return builder.build();
    }

    /**
     * double-check单例模式，线程安全。
     */
    private static RetrofitFactory getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitFactory.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitFactory();
                }
            }
        }
        return sInstance;
    }

    /**
     * @see #_getService(Class)
     */
    public static <T> T getService(Class<T> clazz) {
        return getInstance()._getService(clazz);
    }

    /**
     * @return Retrofit用于动态代理的接口
     */
    private <T> T _getService(Class<T> clazz) {
        if (mServiceMap.containsKey(clazz)) {
            return (T) mServiceMap.get(clazz);
        } else {
            Object service = mRetrofit.create(clazz);
            mServiceMap.put(clazz, service);
            return (T) service;
        }
    }
}