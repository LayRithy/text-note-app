package com.example.noteapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Class View Holder
public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView text_title, text_date;
    ImageView default_image, btn_delete, btn_favourite;
    RecyclerView recyclerView;
    View view;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title = itemView.findViewById(R.id.text_title);
        text_date = itemView.findViewById(R.id.text_date);
        default_image = itemView.findViewById(R.id.id_default_image);
        btn_delete = itemView.findViewById(R.id.btn_delete);
        btn_favourite = itemView.findViewById(R.id.btn_favourite);

        view = itemView;
    }

}
