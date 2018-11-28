package com.tutor.cokinfo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.model.Member;

public class EditProfilActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, nama, tanggalLahir, nomorTelepon,jenisKelamin;
    private Button edit;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        bindView();
        income();
    }
    private void bindView(){
        username = findViewById(R.id.editProfilUsername);
        nama = findViewById(R.id.editProfilNama);
        tanggalLahir = findViewById(R.id.editProfilTanggalLahir);
        nomorTelepon = findViewById(R.id.editProfilNomorTelepon);
        jenisKelamin = findViewById(R.id.editProfilJenisKelamin);
        edit = findViewById(R.id.editProfilSimpan);
    }
    private void income(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        edit.setOnClickListener(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = (Member)getIntent().getExtras().getSerializable("User");

        username.setText(user.getUsername());
        nama.setText(user.getNama());
        tanggalLahir.setText(user.getTanggalLahir());
        nomorTelepon.setText(user.getNomorTelepon());
        jenisKelamin.setText(user.getJenisKelamin());
    }

    @Override
    public void onClick(View v) {
        user = new Member(username.getText().toString(),nama.getText().toString(),tanggalLahir.getText().toString(),jenisKelamin.getText().toString(),nomorTelepon.getText().toString());
        databaseReference.child("User").child(firebaseUser.getUid()).setValue(user);
        finish();
    }
}
