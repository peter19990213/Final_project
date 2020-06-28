package com.example.final_project.data;

import android.net.Uri;
import android.provider.BaseColumns;



public class FinalContract {

    public static final String AUTHORITY = "com.example.final_project.data.MyContentProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String Context_PATH = "locations";
    public static final String Context_PATH2 = "nearby";

    public static final class FinalEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Context_PATH).build();
        public static final Uri CONTENT_URI2 = BASE_CONTENT_URI.buildUpon().appendPath(Context_PATH2).build();

        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_longitude= "longitude";
        public static final String COLUMN_latitude = "latitude";
        public static final String COLUMN_name = "name";
    }
}
