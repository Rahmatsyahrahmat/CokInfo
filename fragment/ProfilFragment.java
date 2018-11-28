package com.tutor.cokinfo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.activity.EditProfilActivity;
import com.tutor.cokinfo.model.Member;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private TextView nama, username, jenisKelamin, tanngalLahir, nomorTelepon;
    private Button edit;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        nama = rootView.findViewById(R.id.profilNama);
        username = rootView.findViewById(R.id.profilUsername);
        jenisKelamin = rootView.findViewById(R.id.profilJenisKelamin);
        tanngalLahir = rootView.findViewById(R.id.profilTanggalLahir);
        nomorTelepon = rootView.findViewById(R.id.profilNomorTelepon);
        edit = rootView.findViewById(R.id.profilEdit);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("User").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Member user = new Member(dataSnapshot.child("username").getValue().toString(),dataSnapshot.child("nama").getValue().toString(),dataSnapshot.child("tanggalLahir").getValue().toString(),dataSnapshot.child("jenisKelamin").getValue().toString(),dataSnapshot.child("nomorTelepon").getValue().toString());

                nama.setText(user.getNama());
                username.setText(user.getUsername());
                jenisKelamin.setText(user.getJenisKelamin());
                tanngalLahir.setText(user.getTanggalLahir());
                nomorTelepon.setText(user.getNomorTelepon());

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),EditProfilActivity.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
