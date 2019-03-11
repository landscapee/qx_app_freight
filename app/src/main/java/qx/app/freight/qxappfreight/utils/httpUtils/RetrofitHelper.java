package qx.app.freight.qxappfreight.utils.httpUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitHelper {
    static RetrofitFactory getRetrofit(String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new RetrofitFactory(url, GsonConverterFactory.create(gson));
    }
}
