package com.example.ryosuke.useomiaiapisample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by ryosuke on 2018/02/19.
 */

public class EventListActivity extends AppCompatActivity implements EventAdapter.OnEventItemClickListener{

    private EventAdapter eventAdapter;
    private ProgressBar progressBar;
    private Spinner prefectureSpinner;
    private CoordinatorLayout coordinatorLayout;

    private static List<String> PREF_LIST;
    static {
        List<String> list = new ArrayList<String>();

        String[] prefs ={"北海道", "青森県", "岩手県", "宮城県", "秋田県",
                "山形県", "福島県", "茨城県", "栃木県", "群馬県",
                "埼玉県", "千葉県", "東京都", "神奈川県", "新潟県",
                "富山県", "石川県", "福井県", "山梨県", "長野県",
                "岐阜県", "静岡県", "愛知県", "三重県", "滋賀県",
                "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県",
                "鳥取県", "島根県", "岡山県", "広島県", "山口県",
                "徳島県", "香川県", "愛媛県", "高知県", "福岡県",
                "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県",
                "鹿児島県", "沖縄県"};

        for(int i = 0; i<prefs.length; i++){
            list.add(prefs[i]);
        }

        PREF_LIST= Collections.unmodifiableList(list);

    }

   

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_list);

        setUpViews();
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter((Context) this, (EventAdapter.OnEventItemClickListener) this);
        recyclerView.setAdapter(eventAdapter);
        
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        // Spinner
        prefectureSpinner = (Spinner) findViewById(R.id.prefecture_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(PREF_LIST);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefectureSpinner.setAdapter(adapter);
        prefectureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // 選択時だけでなく、最初にも呼ばれる
                    int prefNumver = position + 1;
                    loadEvents(prefNumver);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
    
                }
            
            });
    
    }
    

    private void loadEvents(int prefNumver) {
        // 読込中なのでプログレスバーを表示する
        progressBar.setVisibility(View.VISIBLE);

        // 一週間前の日付の文字列 今が2016-10-27ならば2016-10-20 などの文字列を取得する
        //final Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, -7);
        //String text = DateFormat.format("yyyy-MM-dd", calendar).toString();

        // Retrofitを利用してサーバーにアクセスする
        final MyApplication application = (MyApplication) getApplication();
        // 過去一週間で作られて、言語がlanguageのものをクエリとして渡す
        Single<OmiaiService.ListResult> single = application.getOmiaiService().getEvents("json", prefNumver, 2, 0 );
        // 入出力(IO)用のスレッドで通信を行い、メインスレッドで結果を受け取るようにする
        single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listResult -> {
                            progressBar.setVisibility(View.GONE);
                            // 取得したアイテムを表示するために、RecyclerViewにアイテムをセットして更新する
                            eventAdapter.setItemAndRefresh(listResult.items);
                            },
                        throwable -> {
                            Snackbar.make(coordinatorLayout, "読み込めませんでした。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        }

                );
    }


    @Override
    public void onEventItemClick(OmiaiService.Result item) {
            Log.d("TAG",item.title + "clicked");
    }
}
