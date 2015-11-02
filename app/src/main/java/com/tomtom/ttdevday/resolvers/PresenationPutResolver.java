package com.tomtom.ttdevday.resolvers;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.operations.put.PutResolver;
import com.pushtorefresh.storio.contentresolver.operations.put.PutResult;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.PresentationsTable;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresenationPutResolver extends PutResolver<Presentation> {


    @NonNull
    @Override
    public PutResult performPut(@NonNull final StorIOContentResolver storIOContentResolver, @NonNull final Presentation presentation) {
        com.pushtorefresh.storio.contentresolver.operations.put.PutResult putResult = storIOContentResolver.put().object(presentation).prepare().executeAsBlocking();

        return PutResult.newUpdateResult(putResult.numberOfRowsUpdated(), PresentationsTable.CONTENT_URI);
    }
}
