package com.example.android.karinafebriyana_1202150277_studycase5;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private LinkedList<ToDo> mWordList = new LinkedList<>();
    private int mCount = 0;

    private RecyclerView mRecyclerView; //deklarasi variable
    private RecyclerViewAdapter mAdapter;
    private DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(this); //untuk mengecek database

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//menambahkan menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) { //menampilkan dialog untuk merubah warna
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("Change");
            dialog.setCancelable(true);

            final RadioButton rdRed = (RadioButton) dialog.findViewById(R.id.rdRed);//mengaktifkan radiobutton untuk memilih warna
            final RadioButton rdBlue = (RadioButton) dialog.findViewById(R.id.rdBlue);
            final RadioButton rdGreen = (RadioButton) dialog.findViewById(R.id.rdGreen);
            Button btnChange = (Button)dialog.findViewById(R.id.btnChange);
            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rdRed.isChecked()){ //ketika radio button di klik maka warna akan berubah sesuai dengan warna yang dipilih
                        mRecyclerView.setBackgroundResource(R.color.redBackgroud);
                        Toast.makeText(view.getContext(),"Red Choosen",Toast.LENGTH_SHORT).show();
                    }
                    if (rdBlue.isChecked()){
                        mRecyclerView.setBackgroundResource(R.color.blueBackgroud);
                        Toast.makeText(view.getContext(),"Blue Choosen",Toast.LENGTH_SHORT).show();
                    }
                    if (rdGreen.isChecked()){
                        mRecyclerView.setBackgroundResource(R.color.greenBackgroud);
                        Toast.makeText(view.getContext(),"Green Choosen",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(view.getContext(),"Changed",Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show(); //menampilkan dialog
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoAdd(View view) {
        Intent intent = new Intent(this, AddTodo.class); //pindah ke AddToDo class
//        startActivity(intent);
        startActivityForResult(intent, 1); //start activity
    }

    public void setRecyclerView(){ //mengaktifkan RecyclerView
        mWordList = databaseHandler.findAll(); //get handle ke RecycleView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview); // Membuat adapter dan supply data agar bisa ditampilkan
        mAdapter = new RecyclerViewAdapter(this, mWordList);
        mRecyclerView.setAdapter(mAdapter); //menghubungkan adapter dengan recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SwipeHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //untuk intent data
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            Log.d("new name : ",data.getStringExtra("name"));
            Log.d("new desc : ",data.getStringExtra("desc"));
            Log.d("new priority : ",data.getStringExtra("priority"));
            databaseHandler.save(new ToDo(data.getStringExtra("name"), data.getStringExtra("desc"), data.getStringExtra("priority")));
        }
        setRecyclerView();
        mAdapter.notifyDataSetChanged();
    }
}
