package com.tutor.cokinfo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Admin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private Button login;
    private TextView regster;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        income();
        SharedPreferences sharedPreferences = getSharedPreferences("Admin",Context.MODE_PRIVATE);
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
        }
        if (sharedPreferences.getString("isAdmin","false").equals("true")){
            startActivity(new Intent(this,AdminMainActivity.class));
        }

    }
    private void bindView(){
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginMasuk);
        regster = findViewById(R.id.loginRegister);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Log in");
    }
    private void income(){
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        regster.setOnClickListener(this);
    }
    private void login(){
        if (isNull(email)){
            toast("Email tidak boleh kosong");
            return;
        }
        if (isNull(password)){
            toast("Password tidak boleh kosong");
            return;
        }
        Admin admin = new Admin();
        if (email.getText().toString().equals(admin.getEmail())&&password.getText().toString().equals(admin.getPassword())){
            SharedPreferences.Editor editor = getSharedPreferences("Admin",Context.MODE_PRIVATE).edit();
            editor.putString("isAdmin","true");
            editor.apply();
            startActivity(new Intent(this,AdminMainActivity.class));
            return;
        }
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    progressDialog.dismiss();
                }
                else  if (!isConnected()){
                    toast("Koneksi tidak tersedia");
                    progressDialog.dismiss();
                }
                else {
                    toast("Gagal Log In");
                    progressDialog.dismiss();
                }
            }
        });
    }
    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    private boolean isNull(EditText et){
        return TextUtils.isEmpty(et.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginMasuk:
                login();
                break;
            case R.id.loginRegister:
                startActivity(new Intent(this,RegisterActivity.class));

        }
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
