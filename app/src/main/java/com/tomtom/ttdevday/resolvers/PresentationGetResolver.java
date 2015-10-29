package com.tomtom.ttdevday.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.TTDevDaySQLite;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresentationGetResolver extends DefaultGetResolver<Presentation> {
    @NonNull
    @Override
    public Presentation mapFromCursor(final Cursor cursor) {
        Presentation presentation = new Presentation();
        presentation.id = cursor.getString(cursor.getColumnIndex(TTDevDaySQLite.ID));
        presentation.author = cursor.getString(cursor.getColumnIndex(TTDevDaySQLite.AUTHOR));
        presentation.description = cursor.getString(cursor.getColumnIndex(TTDevDaySQLite.DESCRIPTION));
        presentation.number = cursor.getInt(cursor.getColumnIndex(TTDevDaySQLite.NO_VOTES));
        presentation.title = cursor.getString(cursor.getColumnIndex(TTDevDaySQLite.TITLE));
        return presentation;
    }

}
