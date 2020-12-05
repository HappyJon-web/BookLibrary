package com.example.booklibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {
    //FavAdapter class handle the RecyclerView favorite books entries, its functions, and its designs.
    private Context context;
    Activity activity;
    private ArrayList book_id, book_title, book_author, book_pages, book_fav;
    Animation translate_anim; //The Favorites Adapter List is also animated.

//FavoriteAdapter's constructor with getter and setter inside.
    FavAdapter(Activity activity, Context context, ArrayList book_id, ArrayList book_title,
                  ArrayList book_author, ArrayList book_pages, ArrayList book_fav ){
        this.activity = activity;
        this.context = context;
        this.book_id= book_id;
        this.book_title= book_title;
        this.book_author= book_author;
        this.book_pages= book_pages;
        this.book_fav= book_fav;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fav_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.book_id_fav.setText(String.valueOf(book_id.get(position)));
        holder.book_title_fav.setText(String.valueOf(book_title.get(position)));
        holder.book_author_fav.setText(String.valueOf(book_author.get(position)));
        holder.book_pages_fav.setText(String.valueOf(book_pages.get(position)));

        //Clicking the book data's list row allows users to update or delete the book.
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(book_id.get(position)));
                intent.putExtra("title", String.valueOf(book_title.get(position)));
                intent.putExtra("author", String.valueOf(book_author.get(position)));
                intent.putExtra("pages", String.valueOf(book_pages.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });

//Clicking removeFav button will disable the book's favorite value and remove it from the Favorites list.
        holder.removeFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                book_fav.set(position, 0); //Sets book's favorite value to 0
                Toast.makeText(context, "Removed from Favorites.", Toast.LENGTH_SHORT).show();

                DatabaseHelper myDB = new DatabaseHelper(context);
                myDB.updateFav(String.valueOf(book_id.get(position)), (Integer) book_fav.get(position));
//Database will update the book's favorite value.
            }
        });

    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //Initialize the elements in the favAdapter
        TextView book_id_fav, book_title_fav, book_author_fav, book_pages_fav;
        private ImageButton removeFav;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_fav = itemView.findViewById(R.id.book_id_fav);
            book_title_fav = itemView.findViewById(R.id.book_title_fav);
            book_author_fav = itemView.findViewById(R.id.book_author_fav);
            book_pages_fav = itemView.findViewById(R.id.book_pages_fav);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            removeFav = (ImageButton) itemView.findViewById(R.id.btnDelete);

            //Animate Recyclerview
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);

        }
    }
}
