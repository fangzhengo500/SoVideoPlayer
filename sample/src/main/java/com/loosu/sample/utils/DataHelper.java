package com.loosu.sample.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;


import com.loosu.sample.domain.VideoEntry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

public class DataHelper {
    @NonNull
    public static List<VideoEntry> getVideos(Context context) {
        try {
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            List<VideoEntry> videoEntries = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(uri, COLUMNS, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    videoEntries.add(new VideoEntry(cursor));
                }
                cursor.close();
            }
            return videoEntries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static List<VideoEntry> getVideos(Context context, int offset, int pageSize) {
        try {
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            List<VideoEntry> videoEntries = new ArrayList<>();
            String sortOrder = MediaStore.Video.VideoColumns._ID + " LIMIT " + pageSize + " OFFSET " + offset;
            Cursor cursor = context.getContentResolver().query(uri, COLUMNS, null, null, sortOrder);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    videoEntries.add(new VideoEntry(cursor));
                }
                cursor.close();
            }
            return videoEntries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static Observable<List<VideoEntry>> voidrxGetVideos(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = context.getContentResolver().query(uri, COLUMNS, null, null, null);
                emitter.onNext(cursor);
                emitter.onComplete();
            }
        }).map(new Function<Cursor, List<VideoEntry>>() {
            @Override
            public List<VideoEntry> apply(Cursor cursor) throws Exception {
                List<VideoEntry> videoEntries = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        videoEntries.add(new VideoEntry(cursor));
                    }
                    cursor.close();
                }
                return videoEntries;
            }
        });
    }

    private static final String[] COLUMNS = {
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DATE_ADDED,
            MediaStore.Video.VideoColumns.DATE_MODIFIED,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.HEIGHT,
            MediaStore.Video.VideoColumns.MIME_TYPE,
            MediaStore.Video.VideoColumns.SIZE,
            MediaStore.Video.VideoColumns.TITLE,
            MediaStore.Video.VideoColumns.WIDTH,
            MediaStore.Video.VideoColumns.DURATION,
    };
}
