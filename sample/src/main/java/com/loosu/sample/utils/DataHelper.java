package com.loosu.sample.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;


import com.loosu.sample.domain.VideoEntry;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    @NonNull
    public static List<VideoEntry> getVideos(Context context) {
        String[] columns = {
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

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        List<VideoEntry> videoEntries = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                videoEntries.add(new VideoEntry(cursor));
            }
        }
        return videoEntries;
    }
}
