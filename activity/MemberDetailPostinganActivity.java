package com.tutor.cokinfo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Postingan;

import java.io.File;
import java.io.IOException;

public class MemberDetailPostinganActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView judul, alamat, harga, jenis;
    private ImageView imageView;
    private EditText komentar;
    private ImageButton kirimKomentar;
    private ListView listView;

    private DatabaseReference databaseReference;

    private Postingan postingan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail_postingan);
        bindView();
        income();
    }
    private void bindView(){
        judul = findViewById(R.id.memberDetailPostinganJudul);
        alamat = findViewById(R.id.memberDetailPostinganAlamat);
        harga = findViewById(R.id.memberDetailPostinganHarga);
        jenis = findViewById(R.id.memberDetailPostinganJenisCoklat);
        imageView = findViewById(R.id.memberDetailPostinganImage);
        komentar = findViewById(R.id.memberDetailPostinganTulisKomentar);
        kirimKomentar = findViewById(R.id.memberDetailPostinganKirimKomentar);
        listView = findViewById(R.id.memberDetailPostinganKomentar);
    }
    private void income(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postingan = (Postingan) getIntent().getExtras().getSerializable("postingan");

        kirimKomentar.setOnClickListener(this);

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
                        Glide.with(MemberDetailPostinganActivity.this).load(my_image[0]).into(imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseReference.child("Postingan").child(postingan.getId()).child("komentar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDetailPostinganActivity.this, android.R.layout.simple_list_item_1);
                adapter.clear();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    adapter.add(d.getValue().toString());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(komentar.getText().toString().trim())){
            Toast.makeText(this,"Isikan komentar terlebih dahulu",Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child("Postingan").child(postingan.getId()).child("komentar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long i = dataSnapshot.getChildrenCount()+1;
                databaseReference.child("Postingan").child(postingan.getId()).child("komentar").child(i+"").setValue(komentar.getText().toString());
                Toast.makeText(MemberDetailPostinganActivity.this,"Komentar Ditambahkan",Toast.LENGTH_SHORT).show();
                komentar.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
