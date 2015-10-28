/*
 *
 *  Copyright (C) TomTom International B.V., 2015.
 *  All rights reserved.
 * /
 */

package devday.tomtom.com.ttdevday;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Deployd.API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private final Deployd deployd = retrofit.create(Deployd.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyAdapter();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        deployd.presentations().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<List<Presentation>>() {
            @Override
            public void call(List<Presentation> presentations) {
                adapter.setList(presentations);
            }
        });
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

    private static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Presentation> presentations = new ArrayList<>();

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_card, parent, false);

            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Presentation presentation = this.presentations.get(position);
            holder.textAuthor.setText(presentation.author);
            holder.textTitle.setText(presentation.title);
            holder.textDescription.setText(presentation.description);
            holder.textNumber.setText(String.valueOf(presentation.number));

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator animator = ViewPropertyObjectAnimator
                            .animate(holder.parentView)
                            .height(600)
                            .setDuration(300)
                            .setInterpolator(new AnticipateInterpolator())
                            .get();

                    animator.start();
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
}
