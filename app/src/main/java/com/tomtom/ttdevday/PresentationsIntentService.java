package com.tomtom.ttdevday;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio.contentresolver.impl.DefaultStorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.queries.Query;
import com.tomtom.ttdevday.resolvers.PresenationPutResolver;
import com.tomtom.ttdevday.resolvers.PresentationGetResolver;
import com.tomtom.ttdevday.resolvers.PresentationsDeleteResolver;

import java.util.Arrays;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PresentationsIntentService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private DefaultStorIOContentResolver storIOSQLite;

    public class LocalBinder extends Binder {
        PresentationsIntentService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PresentationsIntentService.this;
        }
    }



    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Deployd.API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private final Deployd deployd = retrofit.create(Deployd.class);

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FETCH_PRESENTATIONS = "com.ttdevday.action.FETCH_PRESENTATIONS";
    private static final String ACTION_SEND_VOTE = "com.ttdevday.action.VOTE";

    // TODO: Rename parameters
    private static final String PARAM_PRESENTATION_ID = "com.ttdevday.extra.PRESENTATION_ID";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFetchPresentations(Context context) {
        Intent intent = new Intent(context, PresentationsIntentService.class);
        intent.setAction(ACTION_FETCH_PRESENTATIONS);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionVote(Context context, String vote, String param2) {
        Intent intent = new Intent(context, PresentationsIntentService.class);
        intent.setAction(ACTION_SEND_VOTE);
        intent.putExtra(PARAM_PRESENTATION_ID, vote);
        context.startService(intent);
    }

    public PresentationsIntentService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {

        ContentResolver contentResolver = getContentResolver();
        storIOSQLite = DefaultStorIOContentResolver.builder()
                .contentResolver(getContentResolver())
                .addTypeMapping(Presentation.class, ContentResolverTypeMapping.<Presentation>builder().putResolver(new PresenationPutResolver()).getResolver(new PresentationGetResolver()).deleteResolver(new PresentationsDeleteResolver()).build()) // required for object mapping
                .build();

        return mBinder;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    public Observable<List<Presentation>> handleActionFetchPresentations() {
        return deployd.presentations().subscribeOn(Schedulers.io()).map(new Func1<List<Presentation>, List<Presentation>>() {
            @Override
            public List<Presentation> call(final List<Presentation> presentations) {
                storIOSQLite.put().objects(presentations);
                return presentations;
            }
        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    public Observable<Vote> handleActionVote(String presentationId, String votedBy) {
        final Vote vote = new Vote();
        vote.deviceId = "10";
        vote.presentationId = presentationId;
        vote.votedBy = votedBy;
        return deployd.vote(vote).subscribeOn(Schedulers.io()).map(new Func1<Vote, Vote>() {
            @Override
            public Vote call(final Vote vote) {
                Query query = Query.builder().uri(PresentationsTable.CONTENT_URI).where(PresentationsTable.FIELD_ID + "=?").whereArgs(vote.presentationId).build();
                Presentation first = storIOSQLite.get().listOfObjects(Presentation.class).withQuery(query).prepare().createObservable().single().flatMap(new Func1<List<Presentation>, Observable<Presentation>>() {
                    @Override
                    public Observable<Presentation> call(final List<Presentation> presentations) {
                        return Observable.just(presentations.get(0));
                    }
                }).toBlocking().first();
                first.number = vote.noVotes;
                storIOSQLite.put().objects(Arrays.asList(first)).prepare().executeAsBlocking();
                return vote;
            }
        });
    }
}
