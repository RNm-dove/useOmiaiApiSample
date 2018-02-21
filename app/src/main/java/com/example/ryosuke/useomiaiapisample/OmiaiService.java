package com.example.ryosuke.useomiaiapisample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ryosuke on 2018/02/19.
 */

public interface OmiaiService {

    @GET("top/api/get")
    Single<ListResult> getEvents(@Query("format") String format, @Query("pref") int pref, @Query("results") int max_number, @Query("start") int start_number );

    public static class ListResult{
        @SerializedName("Result")
        @Expose
        public final List<Result>  items;
        public ListResult(List<Result> items){this.items = items;}

    }

    public static class Result{

        @SerializedName("Title")
        @Expose
        public final String title;
        @SerializedName("Place")
        @Expose
        public final String place;
        @SerializedName("Date")
        @Expose
        public final String date;
        @SerializedName("ReserveMan")
        @Expose
        public final String reserveMan;
        @SerializedName("ReserveWoman")
        @Expose
        public final String reserveWoman;
        @SerializedName("AgeMan")
        @Expose
        public final String ageMan;
        @SerializedName("AgeWoman")
        @Expose
        public final String ageWoman;
        @SerializedName("Url")
        @Expose
        public final String url;
        @SerializedName("AreaName")
        @Expose
        public final String areaName;
        @SerializedName("Article")
        @Expose
        public final String article;
        @SerializedName("Image")
        @Expose
        public final String image;

        public Result(String title, String place, String date, String reserveMan, String reserveWoman, String ageMan, String ageWoman, String url, String areaName, String article, String image) {
            this.title = title;
            this.place = place;
            this.date = date;
            this.reserveMan = reserveMan;
            this.reserveWoman = reserveWoman;
            this.ageMan = ageMan;
            this.ageWoman = ageWoman;
            this.url = url;
            this.areaName = areaName;
            this.article = article;
            this.image = image;
        }
    }




}
