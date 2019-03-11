package qx.app.freight.qxappfreight.utils.httpUtils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

class RetrofitFactory {
    private Retrofit mRetrofit;

    RetrofitFactory(String url, Converter.Factory factory) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(10, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);//读操作超时时间
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            Log.i("tagRetrofit", "msg====" + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(chain -> {
//            Log.e("tagToken", "token=====" + Tools.getToken());
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
//                    .header("token", Tools.getToken())
                    .header("Content-Type", "application/json")
                    .method(originalRequest.method(), originalRequest.body())
                    .build();
            return chain.proceed(authorised);
        });
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .baseUrl(url)
                .build();
    }

    <T> T getApiService(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}

