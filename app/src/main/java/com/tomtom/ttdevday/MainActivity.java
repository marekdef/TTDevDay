/*
 *
 *  Copyright (C) TomTom International B.V., 2015.
 *  All rights reserved.
 * /
 */

package com.tomtom.ttdevday;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    PresentationsIntentService mPresentationService;

    private RecyclerView recyclerView;
    private PresentationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PresentationAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bindService(new Intent(this, PresentationsIntentService.class), mConnection, Context.BIND_AUTO_CREATE);
    }



    private PresentationsIntentService mService;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PresentationsIntentService.LocalBinder binder = (PresentationsIntentService.LocalBinder) service;
            mService = binder.getService();
            mService.handleActionFetchPresentations().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Presentation>>() {
                @Override
                public void call(final List<Presentation> presentations) {
                    adapter.setList(presentations);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
}
