package com.example.noteapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Fragment.CalenderDialog;
import com.example.noteapp.Fragment.FavouriteActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView = null;
    RecyclerView gridRecyclerView =null;
    FloatingActionButton floatingActionButton;

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NoteAdapter noteAdapter;
    Grid_Note_Adapter grid_note_adapter;

    SharedPreferences sharedPreferences;

    MenuItem grid, linear, search;
    Calendar calendar;

    private FirebaseRecyclerOptions <NoteModel> options;
    private FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_View);
        //gridRecyclerView = (RecyclerView)findViewById(R.id.recycler_View1);
        //gridRecyclerView.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("note");

        //Short RecyclerView (Stack)
        sortRecyclerView();

        //Retrieve Data from firebase database
        retrieveData();

        //Setup more drawer layout toolbar and Toolbar menu
        moreDrawerLayoutToolbar();

        //Setup Bottom nav with Floating Action
        bottomNavWithFloatingAction();

        //Handle Floating Action
        handleFloatingAction();

        //Handle Bottom Navigation view
        handleBottomNavigation();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EditText search = findViewById(R.id.text_search);
        search.setVisibility(View.INVISIBLE);

        gridLayoutManager = new GridLayoutManager(this , 2);
    }


    //Short RecyclerView (Stack)
    public void sortRecyclerView(){
        sharedPreferences = getSharedPreferences("SortSetting", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "newest");
        if(mSorting.equals("newest")){
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
        }else if(mSorting.equals("oldest")){
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }
    }

    //Retrieve Data from Firebase
    public void retrieveData(){
        recyclerView.setLayoutManager(linearLayoutManager);

        options = new FirebaseRecyclerOptions.Builder<NoteModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("note"), NoteModel.class).build();

        noteAdapter = new NoteAdapter(options);

        recyclerView.setAdapter(noteAdapter);


    }


    //Setup More drawer layout toolbar and Toolbar menu
    public void moreDrawerLayoutToolbar(){
        //More button show drawer layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("HomePage");
        Log.d("d","click");
        drawerLayout= findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener((drawerToggle));
        drawerToggle.syncState();
    }


    //Setup bottom Nav with floating Action
    public void bottomNavWithFloatingAction(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false); //set middle bottom nav menu is enabled
    }


    //Handle Floating Action
    public void handleFloatingAction(){
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //Handle Click BottomNavigationView
    public void handleBottomNavigation(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()== R.id.id_bottom_home){
                }else if(item.getItemId()== R.id.id_bottom_calender){
                    //Show Dialog DatePicker
                    DialogFragment datePicker = new CalenderDialog();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                }else if(item.getItemId()==R.id.ic_favourite){
                    //Shoe Fragment Activity
                    Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                }else if(item.getItemId()==R.id.id_bottom_more){
                    //Show Drawer Layout
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            }
        });
    }


    //Setup toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        search = menu.findItem(R.id.id_text_search);
        SearchView searchView = (SearchView)search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        //Hide Grid View Toolbar
        grid = menu.findItem(R.id.id_text_grid_view);
        linear = menu.findItem(R.id.id_text_linear_view);
        grid.setVisible(false);

        return true;
    }


    //Search Item
    private void processSearch(String s){
        options = new FirebaseRecyclerOptions.Builder<NoteModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("note").orderByChild("title").startAt(s).endAt(s+"\uf8ff "),NoteModel.class)
                .build();
        noteAdapter = new NoteAdapter(options);
        noteAdapter.startListening();
        recyclerView.setAdapter(noteAdapter);
    }


    //Handle Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.id_text_linear_view ){
            //grid.setVisible(true); //Show Grid toolbar
            //linear.setVisible(false); //Hide Linear Toolbar


        }/*else if(item.getItemId()==R.id.id_text_grid_view ){
            grid.setVisible(false); //Hide Grid toolbar
            linear.setVisible(true); //Show Linear Toolbar
            //gridRecyclerView.setVisibility(View.GONE);

        }*/
        return true;
    }


    //DatePicker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
        //grid_note_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
        //grid_note_adapter.stopListening();
    }





    //Setup recycler view
    /*public void recyclerView(){
        recyclerView = findViewById(R.id.recycler_View);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        noteAdapter = new NoteAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);//adapter have reference with recyclerview
    }*/



}

