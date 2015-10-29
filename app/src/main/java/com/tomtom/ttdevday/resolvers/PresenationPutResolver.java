package com.tomtom.ttdevday.resolvers;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.TTDevDaySQLite;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresenationPutResolver extends PutResolver<Presentation> {
    @NonNull
    @Override
    public PutResult performPut(@NonNull final StorIOSQLite storIOSQLite, @NonNull final Presentation presentation) {
        PutResult putResult = storIOSQLite.put().object(presentation).prepare().executeAsBlocking();

        return PutResult.newUpdateResult(putResult.numberOfRowsUpdated(), TTDevDaySQLite.TABLE_PRESENTATIONS);
    }
}
