package com.example.noteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class  NoteAdapter extends FirebaseRecyclerAdapter <NoteModel, NoteViewHolder> {

    Context context ;
    public NoteAdapter(@NonNull FirebaseRecyclerOptions<NoteModel> options) {
        super(options);
    }

    public NoteAdapter(@NonNull FirebaseRecyclerOptions<NoteModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteModel model) {


        //Handle Delete Button
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("d", String.valueOf(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.btn_delete.getContext());
                builder.setTitle("Delete Note");
                builder.setMessage("Are you sure delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("note").child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


        String tittlee = model.getTitle();
        String contentt = model.getContent();
        String datee = model.getDate();

        holder.default_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to Detail Activity
                Context context = holder.itemView.getContext();
                //String pos = position;
                Intent intent = new Intent (context, DetailActivity.class);
                //intent.putExtra ("key", key);
                intent.putExtra("title", tittlee);
                intent.putExtra("content", contentt);
                intent.putExtra("date", datee);

                context.startActivity(intent);

            }
        });


        holder.btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.btn_favourite.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dialog))
                        .setExpanded(true,1500)
                        .create();

                View view = dialogPlus.getHolderView();

                TextInputEditText title = view.findViewById(R.id.input_title);
                TextInputEditText content = view.findViewById(R.id.input_content);
                TextView date = view.findViewById(R.id.update_date);

                //Set Current Date
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
                Date date1 = new Date();

                title.setText(tittlee);
                content.setText(contentt);
                date.setText(formatter.format(date1));

                dialogPlus.show();

                Button btnUpdate = view.findViewById(R.id.update_button);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map <String , Object> map = new HashMap<>();
                        map.put("title",title.getText().toString());
                        map.put("content",content.getText().toString());
                        map.put("date",date.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("note")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(holder.text_title.getContext(),"Update Successfully",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                });
            }
        });

        //Show on Recycler View
        holder.text_title.setText(model.getTitle());
        holder.text_date.setText(model.getDate());


    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_note_view_holder,parent,false);
        return new NoteViewHolder(view);
    }

}
