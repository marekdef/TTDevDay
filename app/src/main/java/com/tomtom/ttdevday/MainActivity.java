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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    PresentationsIntentService mPresentationService;

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bindService(new Intent(this, PresentationsIntentService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textAuthor;
        private final TextView textTitle;
        private final TextView textDescription;
        private final TextView textNumber;

        private final RelativeLayout relativeLayout;
        private final View parentView;

        public MyViewHolder(View card) {
            super(card);

            parentView = card;

            textAuthor = (TextView) card.findViewById(R.id.author);
            textTitle = (TextView) card.findViewById(R.id.title);
            textDescription = (TextView) card.findViewById(R.id.description);
            textNumber = (TextView) card.findViewById(R.id.votes);

            relativeLayout = (RelativeLayout) card.findViewById(R.id.relative);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Presentation> presentations = new ArrayList<>();

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_card, parent, false);

            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Presentation presentation = this.presentations.get(position);
            holder.textAuthor.setText(presentation.author);
            holder.textTitle.setText(presentation.title);
            holder.textDescription.setText(presentation.description);
            holder.textNumber.setText(String.valueOf(presentation.number));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mService.handleActionVoteForPresentation(presentation.id).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Vote>() {
                        @Override
                        public void call(final Vote vote) {
                            Toast.makeText(MainActivity.this, String.valueOf(vote.noVotes), Toast.LENGTH_SHORT);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(final Throwable throwable) {
                            Log.e("TAG", "", throwable);
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return presentations.size();
        }

        public void setList(List<Presentation> presentations) {
            this.presentations = presentations;
            notifyDataSetChanged();
        }
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
