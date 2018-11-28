package com.tutor.cokinfo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.adapter.PostinganAdapter;
import com.tutor.cokinfo.model.Admin;
import com.tutor.cokinfo.model.Postingan;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        bindVew();
        income();
    }
    private void bindVew(){
        floatingActionButton = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.adminRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.adminPorgress);
    }
    private void income(){
        floatingActionButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Postingan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Postingan> postingans = new ArrayList<>();
                postingans.clear();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    String id = d.getKey(),
                            judul = d.child("judul").getValue().toString(),
                            jenisCoklat = d.child("jenisCoklat").getValue().toString(),
                            alamat = d.child("alamat").getValue().toString(),
                            harga = d.child("harga").getValue().toString();
                    postingans.add(new Postingan(id,judul,jenisCoklat,alamat,harga));
                }
                progressBar.setVisibility(View.GONE);
                adapter = new PostinganAdapter(postingans,AdminMainActivity.this,AdminMainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,TambahPostinganActivity.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences("Admin",Context.MODE_PRIVATE).edit();
        editor.putString("isAdmin","false");
        editor.apply();
        startActivity(new Intent(this,LoginActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
