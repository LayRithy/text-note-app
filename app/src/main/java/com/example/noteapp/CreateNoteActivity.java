package com.example.noteapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextInputEditText text_title, text_content;
    TextView text_date;
    String mtitle, mcontent, mdate, randomId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_note);

        //Setup back toolbar
        Toolbar toolbar = findViewById(R.id.create_toolbar);
        toolbar.setTitle("Create Note");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_nav_createnote);
        bottomNavigationView.setBackground(null);

        //bottomNavigationView.setOnNavigationItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("note");

        text_title = findViewById(R.id.create_title);
        text_content = findViewById(R.id.create_content);

        //Set up Current Date on TextDate
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        Date date = new Date();
        text_date = findViewById(R.id.create_date);
        text_date.setText(formatter.format(date));

        //Save Item
        saveItem();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }



    //Handle Back Button
    @Override
    public boolean onSupportNavigateUp() {
        //Go to HomeFragmentRecyclerview
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    //Setup menu on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_note_toolbar, menu);
        return true;
    }


    //Handle Save
    public void saveItem(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()== R.id.save){
                    //Get Data
                    if(text_title.getText().toString().isEmpty() && text_content.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Title and Content are empty.", Toast.LENGTH_SHORT).show();
                    }else if(text_title.getText().toString().isEmpty() ){
                        Toast.makeText(getApplicationContext(),"Title is empty.", Toast.LENGTH_SHORT).show();
                    }else if(text_content.getText().toString().isEmpty() ){
                        Toast.makeText(getApplicationContext(),"Content is empty.", Toast.LENGTH_SHORT).show();
                    }else{
                        randomId = databaseReference.push().getKey(); //Random Data ID
                        mtitle = text_title.getText().toString(); //Get data after user input
                        mcontent = text_content.getText().toString();
                        mdate = text_date.getText().toString();

                        //Add data into Firebase Database
                        databaseReference.child(randomId).setValue(new NoteModel(randomId, mtitle, mcontent, mdate));

                        text_title.setText("");
                        text_content.setText("");

                        //Back to HomeActivity
                        Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
    }







}


