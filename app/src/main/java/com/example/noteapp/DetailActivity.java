package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextInputEditText title , content;
    TextView date;
    MenuItem edit, favourite, remainder;
    List <NoteModel> noteModelList = new ArrayList<>();
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        title = findViewById(R.id.detail_title);
        content = findViewById(R.id.detail_content);
        date = findViewById((R.id.detail_date));

        //Pass Data From RecyclerView
        String tittlee = getIntent().getStringExtra("title");
        String contentt = getIntent().getStringExtra("content");
        String datee = getIntent().getStringExtra("date");
            title.setText(tittlee);
            content.setText(contentt);
            date.setText(datee);

        //Setup toolbar
        setupToolbar();

        //Bottom navigation
        bottomNavigationView = findViewById(R.id.detail_bottom_nav);

        //Handle BottomNavigation
        handleBottomNavigation();


        //Text Only Read
        title.setEnabled(false);
        content.setEnabled(false);

        //Get Data from RecyclerView
        getDataFromRecycler();

        //Hide BottomNavigationView
        hideBottomNavigation();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("note");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    //Setup Toolbar
    public void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);

        //Setup Back Toolbar
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back1);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Get Data from RecyclerView
    public void getDataFromRecycler(){
        //Get Data from RecyclerVIew
        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra("content"));
        date.setText(getIntent().getStringExtra("date"));
    }


    //Handle Back Button
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }


    //Setup Menu Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_toolbar, menu);

        favourite = menu.findItem(R.id.id_detail_toolbar_favourite);
        remainder = menu.findItem(R.id.id_detail_toolbar_remainder);
        edit = menu.findItem(R.id.id_detail_toolbar_edit);

        //Hide item toolbar
        favourite.setVisible(false);
        remainder.setVisible(false);
        return true;
    }


    //Handle Click BottomNavigationView
    public void handleBottomNavigation(){
        bottomNavigationView = findViewById(R.id.detail_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()== R.id.delete){

                }else if(item.getItemId()== R.id.backward){
                    Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else if(item.getItemId()== R.id.update){

                }
                return true;
            }
        });
    }

    //Hide BottomNavigation
    public void hideBottomNavigation(){
        bottomNavigationView.getMenu().findItem(R.id.update).setVisible(false);
        bottomNavigationView.getMenu().findItem(R.id.delete).setVisible(false);
        bottomNavigationView.getMenu().findItem(R.id.backward).setVisible(true);
        //bottomNavigationView.getMenu().findItem(R.id.forward).setVisible(false);
        bottomNavigationView.invalidate();
    }


    //Handle Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.id_detail_toolbar_edit ){

            //Show BottomNavigation View
            bottomNavigationView.getMenu().findItem(R.id.update).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.delete).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.backward).setVisible(true);
            //bottomNavigationView.getMenu().findItem(R.id.forward).setVisible(true);
            bottomNavigationView.invalidate();

            //Text Can Write
            title.setEnabled(true);
            content.setEnabled(true);

            //Change Toolbar Title
            getSupportActionBar().setTitle("Edit Note");

            //Show Item Toolbar
            favourite.setVisible(true);
            remainder.setVisible(true);
            edit.setVisible(false);

            //Edit Time To Current Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
            Date date1 = new Date();
            date.setText(formatter.format(date1));

        }
        return true;
    }

}
