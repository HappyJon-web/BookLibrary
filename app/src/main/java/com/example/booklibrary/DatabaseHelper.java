package com.example.booklibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db"; //Create a database
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "my_library"; //Create the database table and its entries
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title"; //Book title
    private static final String COLUMN_AUTHOR = "book_author"; //Book author
    private static final String COLUMN_PAGES = "book_pages"; //Number of pages in the book
    private static final String COLUMN_FAV = "book_fav"; //The user's favorite book

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_PAGES + " INTEGER, " +
                    COLUMN_FAV + " INTEGER);";
        db.execSQL(query);
    } //This function inserts the SQL command to create the database table for the app.

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    } //If any similar tables exist, it will be dropped.

    void addBook(String title, String author, int pages){ //Add book into the database
        SQLiteDatabase db = this.getWritableDatabase(); //Get writable database for inserting new data
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_FAV, 0); //Set default favorite value to 0
        long result = db.insert(TABLE_NAME, null, cv); //Insert the book information into the database.
        if(result == -1){ //Prompt toast message based on the function result
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){ //Lets user view all book data in the app
        String query = "SELECT * FROM " + TABLE_NAME; // Select from SQL command
        SQLiteDatabase db = this.getReadableDatabase(); //Get readable database for reading new data

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String author, String pages){ //Updates book data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title); //Any entries put in by user will be saved
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        //Update the book information in the database.
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){ //Prompt toast message based on the function result
            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Update Successful!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id){ //Delete one book data
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id}); //Delete book from database.
        if(result == -1){ //Prompt toast message based on the function result
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "The row has been deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){ //Deletes all the data from the database
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME); //SQL command to delete everything in the database table
    }

    //Mark or unmark some books as favorites.
    //This function uses the same database, only that it's updating the favorites column.
    void updateFav(String row_id, int fav){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_FAV, fav); //Update the favorites column based on its current status.
            long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

            if(result == -1){ //If the process fails, an error message will occur.
                Toast.makeText(context, "Adding Failed.", Toast.LENGTH_SHORT).show();
            }
    }

    Cursor readAllFav(){ //Displays all the book data which are marked as favorites.
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE book_fav = 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readSearch(String search_term){ //Displays all the book data according to the search query.
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE book_title LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{"%"+search_term+"%"});
            //Because this function needs a search string to filter the database query,
            // the search string query value must be included.
        }
        return cursor;
    }

}
