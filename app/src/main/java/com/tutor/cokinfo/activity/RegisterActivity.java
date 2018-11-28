package com.tutor.cokinfo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Member;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, username, nama, password, konfirmasiPassword, tanggalLahir, nomorTelepon;
    private Spinner jenisKelamin;
    private Button daftar;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView();
        incomce();
    }
    private void bindView(){
        email = findViewById(R.id.registerEmail);
        username = findViewById(R.id.registerUsername);
        nama = findViewById(R.id.registerNama);
        password = findViewById(R.id.registerPassword);
        konfirmasiPassword = findViewById(R.id.registerKonfirmasiPassword);
        tanggalLahir = findViewById(R.id.resgisterTanggalLahir);
        nomorTelepon = findViewById(R.id.registerNomorTelepon);
        jenisKelamin = findViewById(R.id.registerJenisKelamin);
        daftar = findViewById(R.id.registerDaftar);
    }
    private void incomce(){
        daftar.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
    }
    private void register(){
        if (isNull(email)){
            toast("Email tidak boleh kosong");
            return;
        }
        if (isNull(username)){
            toast("Username tidak boleh kosong");
            return;
        }
        if (isNull(nama)){
            toast("Nama tidak boleh kosong");
            return;
        }
        if (isNull(password)){
            toast("Password tidak boleh kosong");
            return;
        }
        if (isNull(konfirmasiPassword)){
            toast("Konfirmasi Password tidak boleh kosong");
            return;
        }
        if (isNull(tanggalLahir)){
            toast("Tanggal Lahir tidak boleh kosong");
            return;
        }
        if (isNull(nomorTelepon)){
            toast("Nomor Telepon tidak boleh kosong");
            return;
        }

        if (password.getText().toString().length()<8){
            toast("Password minimal 8 karakter");
        }
        else if (!password.getText().toString().equals(konfirmasiPassword.getText().toString())){
            toast("Password tidak sesuai");
        }
        else {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Registrasi");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        toast("Registrasi Berhasil");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        Member user = new Member(username.getText().toString(),nama.getText().toString(),tanggalLahir.getText().toString(),jenisKelamin.getSelectedItem().toString(),nomorTelepon.getText().toString());

                        databaseReference.child("User").child(firebaseUser.getUid()).setValue(user);
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                    }else if(!isConnected()){
                        progressDialog.dismiss();
                        toast("Koneksi tidak tersedia");
                    }else if(!task.isSuccessful()){
                        progressDialog.dismiss();
                        toast("Gagal registrasi");
                    }
                    else
                        toast("Akun sudah tersedia");
                }
            });
        }

    }
    private boolean isNull(EditText et){
        return TextUtils.isEmpty(et.getText().toString().trim());
    }
    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        register();
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
    }
}
