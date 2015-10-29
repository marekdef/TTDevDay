package com.tomtom.ttdevday.resolvers;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.TTDevDaySQLite;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresentationsDeleteResolver extends DeleteResolver<Presentation> {
    @NonNull
    @Override
    public DeleteResult performDelete(final StorIOSQLite storIOSQLite, final Presentation object) {
        DeleteResult deleteResult = storIOSQLite.delete().object(object).prepare().executeAsBlocking();
        return DeleteResult.newInstance(deleteResult.numberOfRowsDeleted(), TTDevDaySQLite.TABLE_PRESENTATIONS);
    }
}
