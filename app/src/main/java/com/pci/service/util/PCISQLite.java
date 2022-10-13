package com.pci.service.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class PCISQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "history.db";
    public static final int DATABASE_VERSION = 1;

    public PCISQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@Nullable SQLiteDatabase db) {
        if (db != null) Record.Table.create(db);
    }

    @Override
    public void onUpgrade(@Nullable SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void destroy(@Nullable Context context) {
        if (context != null) context.deleteDatabase(DATABASE_NAME);
    }

    public static class Record {

        public Long id;
        public Long time;
        public Integer priority;
        public String tag;
        public String message;

        public Record(Integer priority, String tag, String message) {
            this(null, System.currentTimeMillis(), priority, tag, message);
        }

        private Record(Long id, Long time, Integer priority, String tag, String message) {
            this.id = id;
            this.time = time;
            this.priority = priority;
            this.tag = tag;
            this.message = message;
        }

        public ContentValues toContentValues() {
            final ContentValues values = new ContentValues();
            if (id != null) values.put(Table.Columns.ID, id);
            if (time != null) values.put(Table.Columns.TIME, time);
            if (priority != null) values.put(Table.Columns.PRIORITY, priority);
            if (tag != null) values.put(Table.Columns.TAG, tag);
            if (tag != null) values.put(Table.Columns.MESSAGE, message);
            return values;
        }

        public static Record fromCursor(@NonNull Cursor cursor) {
            List<String> columns = Arrays.asList(Table.Columns.ALL);
            return new Record(
                cursor.getLong(columns.indexOf(Table.Columns.ID)),
                cursor.getLong(columns.indexOf(Table.Columns.TIME)),
                cursor.getInt(columns.indexOf(Table.Columns.PRIORITY)),
                cursor.getString(columns.indexOf(Table.Columns.TAG)),
                cursor.getString(columns.indexOf(Table.Columns.MESSAGE))
            );
        }

        @Override
        public String toString() {
            String priorityString;
            switch (priority) {
                case Log.VERBOSE:
                    priorityString = "VERBOSE";
                    break;
                case Log.DEBUG:
                    priorityString = "DEBUG";
                    break;
                case Log.INFO:
                    priorityString = "INFO";
                    break;
                case Log.WARN:
                    priorityString = "WARN";
                    break;
                case Log.ERROR:
                    priorityString = "ERROR";
                    break;
                case Log.ASSERT:
                    priorityString = "ASSERT";
                    break;
                default:
                    priorityString = "NULL";
                    break;
            }
            return String.format(Locale.getDefault(), "[#%d] %s %s/%s: %s", id, PCIFormatter.time(time), priorityString, tag, message);
        }

        public static class Table {

            public static final String NAME = "pci";
//            public static final long MAX_ROW_COUNT = BuildConfig.PUBLISH ? 1000L : 20L;

            public static void create(SQLiteDatabase db) {
                db.execSQL(String.format(
                    "CREATE TABLE IF NOT EXISTS %s (\n" +
                    "    %s INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    %s INTEGER DEFAULT 0,\n" +
                    "    %s INTEGER DEFAULT 0,\n" +
                    "    %s TEXT NOT NULL,\n" +
                    "    %s TEXT NOT NULL\n" +
                    ")",
                    Table.NAME,
                    Columns.ID,
                    Columns.TIME,
                    Columns.PRIORITY,
                    Columns.TAG,
                    Columns.MESSAGE
                ));
            }

            public static void drop(SQLiteDatabase db) {
                db.execSQL(String.format(
                    "DROP TABLE IF EXISTS %s",
                    Table.NAME
                ));
            }

            public static class Columns {
                public static final String ID = BaseColumns._ID;
                public static final String TIME = "time";
                public static final String PRIORITY = "priority";
                public static final String TAG = "tag";
                public static final String MESSAGE = "message";
                public static final String[] ALL = {ID, TIME, PRIORITY, TAG, MESSAGE};
            }
        }
    }

}

