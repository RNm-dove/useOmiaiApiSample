package com.example.ryosuke.useomiaiapisample;

import android.app.Application;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ryosuke on 2018/02/19.
 */

public class MyApplication extends Application {

    private OmiaiService omiaiService;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        // どのActivityからでもAPIを利用できるように、このクラスでAPIを利用する
        setupAPIClient();
    }

    private void setupAPIClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("API LOG", message);
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://goucon.jp/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        omiaiService = retrofit.create(OmiaiService.class);
    }

    public OmiaiService getOmiaiService() {
        return omiaiService;
    }
}
