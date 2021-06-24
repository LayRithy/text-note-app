package com.example.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Grid_Note_Adapter extends FirebaseRecyclerAdapter<NoteModel, GridViewHolder> {


    public Grid_Note_Adapter(@NonNull FirebaseRecyclerOptions<NoteModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GridViewHolder holder, int position, @NonNull NoteModel model) {
        holder.title.setText(model.getTitle());
        holder.content.setText(model.getContent());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_note_view_holder,parent,false);
        return new GridViewHolder(view);
    }
}
