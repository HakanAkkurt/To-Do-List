package hakan_akkurt_matthias_will.de.to_do_list.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hakan_akkurt_matthias_will.de.to_do_list.model.ToDo;

/**
 * Created by Hakan Akkurt on 13.01.2017.
 */

public class TodoDatabase extends SQLiteOpenHelper {
    public static TodoDatabase INSTANCE = null;

    private static final String DB_NAME = "TODOS";
    private static final int VERSION = 9;
    private static final String TABLE_NAME = "todos";

    public static final String ID_COLUMN = "ID";
    public static final String NAME_COLUMN = "name";
    public static final String DUEDATE_COLUMN = "dueDate";
    public static final String FAVORITE_COLUMN = "favorite";
    public static final String DESCRIPTION_COLUMN ="description";
    public static final String LATITUDE_COLUMN    = "latitude";
    public static final String LONGITUDE_COLUMN   = "longitude";

    private TodoDatabase(final Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static TodoDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TodoDatabase(context);
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        String createQuery = "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY, " + NAME_COLUMN + " TEXT NOT NULL, " + DUEDATE_COLUMN + " INTEGER DEFAULT NULL, " + FAVORITE_COLUMN + " INTEGER DEFAULT 0, " + DESCRIPTION_COLUMN + " TEXT DEFAULT NULL, " + LATITUDE_COLUMN + " REAL DEFAULT NULL, " + LONGITUDE_COLUMN + " REAL DEFAULT NULL)";

        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);

        onCreate(sqLiteDatabase);
    }

    public ToDo createToDo(final ToDo todo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, todo.getName());
        values.put(DUEDATE_COLUMN, todo.getDueDate() == null ? null : todo.getDueDate().getTimeInMillis() / 1000);
        values.put(FAVORITE_COLUMN, todo.isFavorite() ? 1 : 0);
        values.put(DESCRIPTION_COLUMN, todo.getDescription());

        values.put(LATITUDE_COLUMN, todo.getLocation() == null ? null : todo.getLocation().latitude);
        values.put(LONGITUDE_COLUMN, todo.getLocation() == null ? null : todo.getLocation().longitude);

        long newID = database.insert(TABLE_NAME, null, values);

        database.close();

        return readToDo(newID);

    }

    public ToDo readToDo(final long id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{ID_COLUMN, NAME_COLUMN, DUEDATE_COLUMN, FAVORITE_COLUMN, DESCRIPTION_COLUMN, LATITUDE_COLUMN, LONGITUDE_COLUMN}, ID_COLUMN + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        ToDo todo = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            todo = new ToDo(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
            todo.setId(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));

            Calendar calendar = null;

            if (!cursor.isNull(cursor.getColumnIndex(DUEDATE_COLUMN))) {
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DUEDATE_COLUMN)) * 1000);
            }

            todo.setFavorite(cursor.getInt(cursor.getColumnIndex(FAVORITE_COLUMN)) == 1);

            todo.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN)));

            todo.setDueDate(calendar);

            if (!cursor.isNull(cursor.getColumnIndex(LATITUDE_COLUMN)) && !cursor.isNull(cursor.getColumnIndex(LONGITUDE_COLUMN))) {
                todo.setLocation(new LatLng(cursor.getFloat(cursor.getColumnIndex(LATITUDE_COLUMN)), cursor.getFloat(cursor.getColumnIndex(LONGITUDE_COLUMN))));
            }

        }

        database.close();

        return todo;
    }

    public List<ToDo> readAllToDos() {
        List<ToDo> todos = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo todo = readToDo(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));
                if (todo != null) {
                    todos.add(todo);
                }
            } while (cursor.moveToNext());
        }

        database.close();

        return todos;
    }

    public ToDo updateToDo(final ToDo todo) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COLUMN, todo.getName());
        values.put(DUEDATE_COLUMN, todo.getDueDate() == null ? null : todo.getDueDate().getTimeInMillis() / 1000);
        values.put(FAVORITE_COLUMN, todo.isFavorite() ? 1 : 0);
        values.put(LATITUDE_COLUMN, todo.getLocation() == null ? null : todo.getLocation().latitude);
        values.put(LONGITUDE_COLUMN, todo.getLocation() == null ? null : todo.getLocation().longitude);

        database.update(TABLE_NAME, values, ID_COLUMN + " = ?", new String[]{String.valueOf(todo.getId())});


        database.close();

        return this.readToDo(todo.getId());
    }

    public void deleteToDo(final ToDo todo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, ID_COLUMN + " = ?", new String[]{String.valueOf(todo.getId())});
        database.close();
    }

    public void deleteAllToDos() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);
        database.close();
    }

    public Cursor getAllTodosAsCursor() {
        return this.getReadableDatabase().rawQuery("SELECT " + ID_COLUMN + " as _id, " + NAME_COLUMN + "," + DUEDATE_COLUMN + " FROM " + TABLE_NAME, null);
    }

    public ToDo getFirstTodo() {
        List<ToDo> todos = this.readAllToDos();

        if (todos.size() > 0) {
            return todos.get(0);
        }

        return null;
    }
}