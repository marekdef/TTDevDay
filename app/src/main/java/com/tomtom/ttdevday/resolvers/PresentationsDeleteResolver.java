package com.tomtom.ttdevday.resolvers;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio.contentresolver.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.contentresolver.operations.delete.DeleteResult;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.PresentationsTable;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresentationsDeleteResolver extends DeleteResolver<Presentation> {


    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull final StorIOContentResolver storIOContentResolver, @NonNull final Presentation object) {
        DeleteResult deleteResult = storIOContentResolver.delete().object(object).prepare().executeAsBlocking();
        return DeleteResult.newInstance(deleteResult.numberOfRowsDeleted(), Uri.withAppendedPath(PresentationsTable.CONTENT_URI, object.id));
    }
}
