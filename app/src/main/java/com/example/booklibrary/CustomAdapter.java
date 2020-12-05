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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList book_id, book_title, book_author, book_pages, book_fav;
    Animation translate_anim; //Animate the RecyclerView List to slide up

    //This is the CustomAdapter constructor. It initializes the getter and setter of all variables in the adapter.
    CustomAdapter(Activity activity, Context context, ArrayList book_id, ArrayList book_title,
                  ArrayList book_author, ArrayList book_pages, ArrayList book_fav ){
        this.activity = activity;
        this.context = context;
        //Getter
        this.book_id = new ArrayList<>();
        this.book_title = new ArrayList<>();
        this.book_author = new ArrayList<>();
        this.book_pages = new ArrayList<>();
        this.book_fav = new ArrayList<>();
        //Setter
        this.book_id.addAll(book_id);
        this.book_title.addAll(book_title);
        this.book_author.addAll(book_author);
        this.book_pages.addAll(book_pages);
        this.book_fav.addAll(book_fav);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        //Retrieve the layout design from the my_row xml class
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.book_id_txt.setText(String.valueOf(book_id.get(position)));
        holder.book_title_txt.setText(String.valueOf(book_title.get(position)));
        holder.book_author_txt.setText(String.valueOf(book_author.get(position)));
        holder.book_pages_txt.setText(String.valueOf(book_pages.get(position)));

        //The user can click any book data row layout, and the app will perform actions on the selected book data.
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(book_id.get(position)));
                intent.putExtra("title", String.valueOf(book_title.get(position)));
                intent.putExtra("author", String.valueOf(book_author.get(position)));
                intent.putExtra("pages", String.valueOf(book_pages.get(position)));
                activity.startActivityForResult(intent, 1);
                //UpdateActivity activity on the selected book data will start.
            }
        });

        //Clicking the share button on any book will share the book information to any messaging field
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Book Title: " + book_title.get(position) + "\n" +
                                "Book Author: " + book_author.get(position) + "\n" +
                                "Book Pages: " + book_pages.get(position) );
                sendIntent.setType("text/plain"); //The app will send the text description of the book data.
                activity.startActivityForResult(sendIntent, 1);

            }
        });


        holder.fav_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Integer.parseInt(String.valueOf(book_fav.get(position)))==0) {
                    holder.fav_btn.setImageResource(R.drawable.ic_favyes); //The button image will change to a filled heart.
                    book_fav.set(position, 1); //The book's favorite value will be set to 1.
                    Toast.makeText(context, "Added to Favorites.", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(String.valueOf(book_fav.get(position)))==1) {
                    holder.fav_btn.setImageResource(R.drawable.ic_favno); //The button image will change to an outline heart.
                    book_fav.set(position, 0); //The book's favorite value will be set to 0.
                    Toast.makeText(context, "Removed from Favorites.", Toast.LENGTH_SHORT).show();
                }

                DatabaseHelper myDB = new DatabaseHelper(context);
                myDB.updateFav(String.valueOf(book_id.get(position)), (Integer) book_fav.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
        private ImageButton share_btn, fav_btn;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id_txt = itemView.findViewById(R.id.book_id_txt);
            book_title_txt = itemView.findViewById(R.id.book_title_txt);
            book_author_txt = itemView.findViewById(R.id.book_author_txt);
            book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            share_btn = (ImageButton) itemView.findViewById(R.id.btnShare);
            fav_btn = (ImageButton) itemView.findViewById(R.id.btnFav);

            //Animate Recyclerview
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);

        }
    }

    //Allows users to filter the book list through search entry.
    public void searchBook(ArrayList book_id, ArrayList book_title, ArrayList book_author,
                        ArrayList book_pages, ArrayList book_fav){
        this.book_id.clear();
        this.book_title.clear();
        this.book_author.clear();
        this.book_pages.clear();
        this.book_fav.clear();
        this.book_id.addAll(book_id);
        this.book_title.addAll(book_title);
        this.book_author.addAll(book_author);
        this.book_pages.addAll(book_pages);
        this.book_fav.addAll(book_fav);
        notifyDataSetChanged();
        //This function tells adapter that data has been updated and should refresh RecyclerView with filtered books.
    }

}
