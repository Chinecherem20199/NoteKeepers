package nigeriandailies.com.ng.notekeepers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteKeepersOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NoteKeepers.db";
    public static final int DATABASE_VERSION = 1;

    public NoteKeepersOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteKeepersDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(NoteKeepersDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE);

        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.insertCourses();
        worker.insertSampleNotes();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
