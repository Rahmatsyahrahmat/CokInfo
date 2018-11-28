package com.tutor.cokinfo.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Timer;
import java.util.TimerTask;

public class TambahPostinganActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {
    private EditText judul, jenisCoklt, alamat, harga;
    private Button tmabah, tambahFoto;
    private ImageView imageView;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_postingan);
        bindView();
        income();
    }
    private void bindView(){
        judul = findViewById(R.id.tambahPostinganJudul);
        jenisCoklt = findViewById(R.id.tambahPostinganJenisCoklat);
        alamat = findViewById(R.id.tambahPostinganAlamat);
        harga = findViewById(R.id.tambahPostinganHarga);
        tmabah = findViewById(R.id.tambahPostinganTambah);
        tambahFoto = findViewById(R.id.tambahPostinganTambahFoto);
        imageView = findViewById(R.id.tambahPostinganImage);
        progressDialog = new ProgressDialog(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
    }
    private void income(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        tmabah.setOnClickListener(this);
        tambahFoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tambahPostinganTambah:
                upload();
                break;
            case R.id.tambahPostinganTambahFoto:
                PickImageDialog.build(new PickSetup().setButtonOrientation(LinearLayoutCompat.HORIZONTAL)).show(this);
                break;
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError()==null){
            bitmap = pickResult.getBitmap();
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            toast("Gagal mengambil Gambar");
        }
    }
    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    private void upload(){
        if (isNull(judul)){
            toast("Judul tidak boleh kosong");
            return;
        }
        if (isNull(jenisCoklt)){
            toast("Jenis Coklat tidak boleh kosong");
            return;
        }
        if (isNull(alamat)){
            toast("Alamat tidak boleh kosong");
            return;
        }
        if (isNull(harga)){
            toast("Harga tidak boleh kosong");
            return;
        }
        if (bitmap==null){
            toast("Ambil gambar terlebih dahulu");
            return;
        }
        progressDialog.show();
        databaseReference.child("Postingan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    i = Integer.valueOf(d.getKey());
                }
                final int finalI = i+1;
                Postingan postingan = new Postingan(finalI+"",judul.getText().toString(),jenisCoklt.getText().toString(),alamat.getText().toString(),harga.getText().toString(),bitmap);
                databaseReference.child("Postingan").child(finalI+"").setValue(postingan);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        uploadFoto(bitmap, finalI +"");
                    }
                }, 1000);
                toast("Berhasil ditambahkan");
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean isNull(EditText et){
        return TextUtils.isEmpty(et.getText().toString().trim());
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
