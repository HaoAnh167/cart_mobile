package hanu.a2_2001040067.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String dbName = "2001040067.db";
    private static final int version = 1;


    public DBHelper(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        sqLiteDatabase.execSQL("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "thumbnail TEXT, " +
                "name TEXT , " +
                "category TEXT, " +
                "unitPrice INTEGER," +
                "quantity INTEGER," +
                "sumPrice INTEGER," +
                "totalPrice INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        onCreate(sqLiteDatabase);
    }
}
