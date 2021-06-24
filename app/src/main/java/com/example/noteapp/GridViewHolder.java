package com.example.noteapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Class View Holder
public class GridViewHolder extends RecyclerView.ViewHolder {

    TextView title, date, content;
    ImageView delete, update;
    View view;



    public GridViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.date);
        content = itemView.findViewById(R.id.content);
        delete = itemView.findViewById(R.id.delete);
        update = itemView.findViewById(R.id.favourite);

        view = itemView;
    }
}