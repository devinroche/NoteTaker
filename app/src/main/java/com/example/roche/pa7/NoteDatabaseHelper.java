package com.example.roche.pa7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class NoteDatabaseHelper extends SQLiteOpenHelper {

    String TAG = "NoteDatabaseHelper";

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "notesDatabase";
    static final String TABLE_NOTES = "tableNotes";

    static final String ID ="_id";
    static final String TITLE = "title";
    static final String TYPE = "type";
    static final String CONTENT = "content";
    static final String IMAGE_RESOURCE = "imageResource";

    public NoteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // SQL statement to create new table
        String sqlCreateTable = "CREATE TABLE " + TABLE_NOTES +
                "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                TYPE + " TEXT, " +
                CONTENT + " TEXT, " +
                IMAGE_RESOURCE + " INTEGER)";

//        Log.d(TAG, "onCreate: " + sqlCreateTable);
        sqLiteDatabase.execSQL(sqlCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Adds a new row to TABLE_NOTES with the values
     * from the note parameter
     *
     * @param note is an instance of a NOTE
     */
    public void insertNote(Note note){
        // SQL Statement to insert a new row with
        // values from a Note Object
        String insertString = "INSERT INTO " + TABLE_NOTES + " VALUES(null, " +
                "'" + note.getTitle() + "', " +
                "'" + note.getCategory() + "', " +
                "'" + note.getContent() + "', " +
                note.getImageResource() + ")";

//        Log.d(TAG, "insertNote: " + insertString);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(insertString);
        db.close();
    }

    /**
     * Gets all rows in TABLE_NOTES
     *
     * @return
     */
    public Cursor getSelectAllNotesCursor(){
        // SQL Statement to get all rows in the database
        String selectSQLString = "SELECT * from " + TABLE_NOTES;
//        Log.d(TAG, "getSelectAllNotesCursor: " + selectSQLString);

        return getReadableDatabase().rawQuery(selectSQLString, null);
    }

    /**
     * Gets values from a row in TABLE_NOTES where the ID value
     * corresponds with the id attribute and returns them in a
     * new Note instance
     *
     * @param id is a long
     * @return Returns an instance of a Note
     */
    public Note getNoteById(long id){
        // SQL Statement to get a row by an id
        String selectByIdSQL = "SELECT * from " + TABLE_NOTES +
                " WHERE " + ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectByIdSQL, null);
        return toNote(cursor);
    }

    /**
     * Updates the values in a row in TABLE_NOTES corresponding
     * to the given id with attributes from a Note Object
     *
     * @param id is an integer that represents a row id
     * @param note is an instance of a Note
     */
    public void upDateNoteById(int id, Note note){

        // SQL Statement to update a row by id with
        // values from a Note Object
        String updateString = "UPDATE " + TABLE_NOTES + " SET " +
                TITLE + " = '" + note.getTitle() + "', " +
                TYPE + " = '" + note.getCategory() + "', " +
                CONTENT + " = '" + note.getContent() + "', " +
                IMAGE_RESOURCE + " = " + note.getImageResource() +
                " WHERE " + ID + " = " + id;

//        Log.d(TAG, "upDateNoteById: " + updateString);

        getWritableDatabase().execSQL(updateString);
    }

    /**
     * Deletes all notes from TABLE_NOTES
     */
    public void deleteAllNotes(){
        // SQL Statement to delete all notes from TABLE_NOTES
        String deleteAllString = "DELETE from " + TABLE_NOTES;
        getWritableDatabase().execSQL(deleteAllString);
    }

    /**
     * Deletes the row in TABLE_NOTES that corresponds
     * to the id attribute
     *
     * @param id is an integer that represents a row id
     */
    public void deleteNoteById(int id){
        // SQL Statement to delete row by id from TABLE_NOTES
        String deleteString = "DELETE from " + TABLE_NOTES + " WHERE " + ID + " = " + id;
        getWritableDatabase().execSQL(deleteString);
    }

    /**
     * Deletes all rows in TABLE_NOTES that correspond
     * to the ids in the ids List
     *
     * @param ids is a List of Longs that represent ids
     */
    public void deleteNotesByIds(List<Long> ids){
        // Iterates through each id in ids and
        // deletes the row corresponding to the id
        for(long id : ids)
            deleteNoteById((int) id);
    }

    /**
     * Gets an ArrayList of Titles from TABLE_NOTES
     * exclude the title corresponding with the given id
     *
     * @param id is an integer that represents a row id
     * @return Returns an ArrayList of Strings
     */
    public ArrayList<String> getTitles(int id){
        // SQL Statement to get all ids and titles from TABLE_NOTES
        String sqlSelectTitles = "SELECT " + ID + ", " + TITLE + " from " + TABLE_NOTES;
        Cursor cursor = getReadableDatabase().rawQuery(sqlSelectTitles, null);
        ArrayList<String> titles = new ArrayList<>();
        while(cursor.moveToNext())
            if(cursor.getInt(0) != id)
                titles.add(cursor.getString(1));
        cursor.close();
        return titles;
    }

    /**
     * Gets an ArrayList of Titles from TABLE_NOTES
     *
     * @return Returns an ArrayList of Strings
     */
    public ArrayList<String> getTitles(){
        return getTitles(-1);
    }

    /**
     * Converts the first item in a Cursor to a note Object
     *
     * @param cursor
     * @return Returns a Note Object
     */
    private static Note toNote(Cursor cursor){
        Note note = null;
        if(cursor.moveToNext())
            note = new Note();

        note.setId(cursor.getInt(0));
        note.setTitle(cursor.getString(1));
        note.setCategory(cursor.getString(2));
        note.setContent(cursor.getString(3));
        note.setImageResource(cursor.getInt(4));

        return note;
    }
}
