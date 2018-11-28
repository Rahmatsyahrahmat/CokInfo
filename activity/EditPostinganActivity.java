package com.tutor.cokinfo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Postingan;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditPostinganActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {
    private EditText judul, jenisCoklt, alamat, harga;
    private Button simpan, gantiFoto;
    private ImageView imageView;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private Bitmap bitmap;

    private Postingan postingan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_postingan);
        bindView();
        income();
    }
    private void bindView(){
        judul = findViewById(R.id.editPostinganJudul);
        jenisCoklt = findViewById(R.id.editPostinganJenisCoklat);
        alamat = findViewById(R.id.editPostinganAlamat);
        harga = findViewById(R.id.editPostinganHarga);
        simpan = findViewById(R.id.editPostinganTambah);
        gantiFoto = findViewById(R.id.editPostinganTambahFoto);
        imageView = findViewById(R.id.editPostinganImage);
        progressDialog = new ProgressDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
    }
    private void income(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        postingan = (Postingan) getIntent().getExtras().getSerializable("postingan");

        judul.setText(postingan.getJudul());
        jenisCoklt.setText(postingan.getJenisCoklat());
        alamat.setText(postingan.getAlamat());
        harga.setText(postingan.getHarga());

        try {
            final File localFile = File.createTempFile("Images", "bmp");
            storageReference.child("Photo Postingan").child(postingan.getId()+".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (!isFinishing())
                        Glide.with(EditPostinganActivity.this).load(bitmap).into(imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        simpan.setOnClickListener(this);
        gantiFoto.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editPostinganTambah:
                simpan();
                break;
            case R.id.editPostinganTambahFoto:
                PickImageDialog.build(new PickSetup().setButtonOrientation(LinearLayoutCompat.HORIZONTAL)).show(this);
                break;
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError()!=null){
            bitmap = pickResult.getBitmap();
            imageView.setImageBitmap(bitmap);
        }
    }
    private void simpan(){
        progressDialog.show();
        databaseReference.child("Postingan").child(postingan.getId()).child("judul").setValue(judul.getText().toString());
        databaseReference.child("Postingan").child(postingan.getId()).child("alamat").setValue(alamat.getText().toString());
        databaseReference.child("Postingan").child(postingan.getId()).child("harga").setValue(harga.getText().toString());
        databaseReference.child("Postingan").child(postingan.getId()).child("jenisCoklat").setValue(jenisCoklt.getText().toString());
        databaseReference.child("Postingan").child(postingan.getId()).child("foto").setValue(bitmap);
        FirebaseStorage.getInstance().getReference().child("Photo Postingan").child(postingan.getId()+".jpg").delete();
        uploadFoto(bitmap,postingan.getId());
        progressDialog.dismiss();
        startActivity(new Intent(this, AdminMainActivity.class));
        finish();
    }
    private void uploadFoto(Bitmap foto,String id){
        StorageReference mountainImagesRef = storageReference.child("Photo Postingan/" + id + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

            }
        });
    }
}
