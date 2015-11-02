/*
 *
 *  Copyright (C) TomTom International B.V., 2015.
 *  All rights reserved.
 * /
 */

package com.tomtom.ttdevday;/*
* Copyright (C) TomTom International B.V., 2015
* All rights reserved.
*/

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

public class PresentationAdapter extends RecyclerView.Adapter<PresentationAdapter.MyViewHolder> {
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


    public static class MyViewHolder extends RecyclerView.ViewHolder {
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
}
