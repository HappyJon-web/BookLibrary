package com.example.booklibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteBooks extends AppCompatActivity {

    RecyclerView recyclerViewFav; //Display list for all favorite books.
    ImageView empty_fav; //This will display if there are no favorites.
    TextView no_fav; //This will display if there are no favorites.

    DatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages, book_fav;
    FavAdapter favAdapter; //This class fetches any book data that are marked as favorites.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_books);

        recyclerViewFav = findViewById(R.id.rvFav);
        empty_fav = findViewById(R.id.empty_fav);
        no_fav = findViewById(R.id.no_fav);

        myDB = new DatabaseHelper(FavoriteBooks.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_fav = new ArrayList<>();

        storeFavInArrays(); //Store the favorite books in the ArrayList for users to view
        favAdapter = new FavAdapter(FavoriteBooks.this, this, book_id,
                book_title, book_author, book_pages, book_fav);
        recyclerViewFav.setAdapter(favAdapter);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(FavoriteBooks.this));
//This FavAdapter class handles the layout and functions of the RecyclerView favorites list.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    } //On Activity Results allows users to start another activity in

    void storeFavInArrays(){ //This function stores the favorite books into the ArrayList.
        Cursor cursor = myDB.readAllFav(); //Retrieves all book data that are marked as favorites.
        if(cursor.getCount() == 0){ //If there are no favorites, the page will only display empty message.
            empty_fav.setVisibility(View.VISIBLE);
            no_fav.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
                book_fav.add(cursor.getString(4));
            }
            empty_fav.setVisibility(View.GONE);
            no_fav.setVisibility(View.GONE);
        }
    }
}