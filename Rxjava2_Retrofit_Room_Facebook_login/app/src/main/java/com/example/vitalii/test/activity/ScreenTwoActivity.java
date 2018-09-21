package com.example.vitalii.test.activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.vitalii.test.DataAdapter;
import com.example.vitalii.test.R;
import com.example.vitalii.test.database.AppDatabase;
import com.example.vitalii.test.model.Person;
import com.example.vitalii.test.service.RecyclerViewClickListener;
import com.example.vitalii.test.service.RetrofitService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ScreenTwoActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private static final String DB_NAME = "person";
    public static final String PERSON_EXTRA = "person_extra";
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private RecyclerView recyclerView;
    private AppDatabase db;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_two);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.detail));
        }
        db = null;
        if (!doesDatabaseExist(DB_NAME)) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        RetrofitService request = retrofit.create(RetrofitService.class);

        Observable<List<Person>> obs = request.getPersons();

        obs.doOnNext(persons -> {
                if (db != null) {
                    db.personDao().insertAll(persons);
                }
             })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(persons -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        createAdapter(persons, this);
                    },
                    throwable -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (throwable != null) {
                                Log.e("throwable", throwable.getMessage());
                        }
                    });

        initViews();
    }

    private boolean doesDatabaseExist(String dbName) {
        File dbFile = getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }


    private void createAdapter(List<Person> people, RecyclerViewClickListener mListener) {
        DataAdapter adapter = new DataAdapter(people, mListener);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(View v, Person person) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(PERSON_EXTRA, person);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.onDestroy();
    }
}
