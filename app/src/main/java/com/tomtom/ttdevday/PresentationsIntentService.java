package com.tomtom.ttdevday;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.tomtom.ttdevday.resolvers.PresenationPutResolver;
import com.tomtom.ttdevday.resolvers.PresentationGetResolver;
import com.tomtom.ttdevday.resolvers.PresentationsDeleteResolver;

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

    public class LocalBinder extends Binder {
        PresentationsIntentService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PresentationsIntentService.this;
        }
    }



    private final DefaultStorIOSQLite storIOSQLite;
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

        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new TTDevDaySQLite(this))
                .addTypeMapping(Presentation.class, SQLiteTypeMapping.<Presentation>builder().putResolver(new PresenationPutResolver()).getResolver(new PresentationGetResolver()).deleteResolver(new PresentationsDeleteResolver()).build()) // required for object mapping
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
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
    public Observable<Vote> handleActionBaz(String presentationId) {
        final Vote vote = new Vote();
        vote.deviceId = "10";
        vote.presentationId = presentationId;
        vote.votedBy = "marek.defecinski";
        return deployd.vote(vote).subscribeOn(Schedulers.io()).map(new Func1<Vote, Vote>() {
            @Override
            public Vote call(final Vote vote) {
                final Query query = Query.builder().table(TTDevDaySQLite.TABLE_PRESENTATIONS).where(TTDevDaySQLite.ID + " = ?" ).whereArgs(vote.presentationId).build();
                storIOSQLite.get().cursor().withQuery(query).prepare().executeAsBlocking();
                return vote;
            }
        });
    }
}
