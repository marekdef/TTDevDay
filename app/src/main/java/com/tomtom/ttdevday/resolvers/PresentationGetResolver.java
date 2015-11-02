package com.tomtom.ttdevday.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.contentresolver.operations.get.DefaultGetResolver;
import com.tomtom.ttdevday.Presentation;
import com.tomtom.ttdevday.PresentationsTable;

/**
 * Created by marekdef on 28.10.15.
 */
public class PresentationGetResolver extends DefaultGetResolver<Presentation> {
    @NonNull
    @Override
    public Presentation mapFromCursor(final Cursor cursor) {
        Presentation presentation = new Presentation();
        presentation.id = cursor.getString(cursor.getColumnIndex(PresentationsTable.FIELD_ID));
        presentation.author = cursor.getString(cursor.getColumnIndex(PresentationsTable.FIELD_AUTHOR));
        presentation.description = cursor.getString(cursor.getColumnIndex(PresentationsTable.FIELD_DESCRIPTION));
        presentation.number = cursor.getInt(cursor.getColumnIndex(PresentationsTable.FIELD_NOVOTES));
        presentation.title = cursor.getString(cursor.getColumnIndex(PresentationsTable.FIELD_TITLE));
        return presentation;
    }

}
