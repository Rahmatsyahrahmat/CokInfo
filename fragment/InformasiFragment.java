package com.tutor.cokinfo.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.activity.AdminMainActivity;
import com.tutor.cokinfo.adapter.PostinganAdapter;
import com.tutor.cokinfo.model.Postingan;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformasiFragment extends Fragment {


    public InformasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_informasi, container, false);
        final ProgressBar progressBar = rootView.findViewById(R.id.infromasiProgress);
        final RecyclerView recyclerView = rootView.findViewById(R.id.informasiRecycvlerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        FirebaseDatabase.getInstance().getReference().child("Postingan").addValueEventListener(new ValueEventListener() {
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
                RecyclerView.Adapter adapter = new PostinganAdapter(postingans,getContext(),getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
