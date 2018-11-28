package com.tutor.cokinfo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Postingan;

import java.io.File;
import java.io.IOException;

public class AdminDetailPostinganActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView judul, alamat, harga, jenis;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button hapus, edit;

    private DatabaseReference databaseReference;

    private Postingan postingan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_postingan);
        bindView();
        income();
    }
    private void bindView(){
        judul = findViewById(R.id.adminDetailPostinganJudul);
        alamat = findViewById(R.id.adminDetailPostinganJenisAlamat);
        harga = findViewById(R.id.adminDetailPostinganJenisHarga);
        jenis = findViewById(R.id.adminDetailPostinganJenisCoklat);
        imageView = findViewById(R.id.adminDetailPostinganImage);
        recyclerView = findViewById(R.id.adminDetailPostinganJenisKomentar);
        hapus = findViewById(R.id.adminDetailPostinganJenisHapus);
        edit = findViewById(R.id.adminDetailPostinganJenisEdit);
    }
    private void income(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        hapus.setOnClickListener(this);
        edit.setOnClickListener(this);
        postingan = (Postingan) getIntent().getExtras().getSerializable("postingan");

        judul.setText(postingan.getJudul());
        alamat.setText(postingan.getAlamat());
        harga.setText(postingan.getHarga());
        jenis.setText(postingan.getJenisCoklat());
        StorageReference gallery = FirebaseStorage.getInstance().getReference().child("Photo Postingan");
        final Bitmap[] my_image = new Bitmap[1];
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            gallery.child(postingan.getId()+".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (!isFinishing())
                        Glide.with(AdminDetailPostinganActivity.this).load(my_image[0]).into(imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adminDetailPostinganJenisHapus:
                hapus();
                break;
            case R.id.adminDetailPostinganJenisEdit:
                Intent intent = new Intent(this,EditPostinganActivity.class);
                intent.putExtra("postingan",postingan);
                startActivity(intent);
                break;
        }
    }
    private void hapus(){
        databaseReference.child("Postingan").child(postingan.getId()).removeValue();
        FirebaseStorage.getInstance().getReference().child("Photo Postingan").child(postingan.getId()+".jpg").delete();
        finish();
    }
}
