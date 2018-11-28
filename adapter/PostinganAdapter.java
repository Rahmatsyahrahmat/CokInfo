package com.tutor.cokinfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tutor.cokinfo.R;
import com.tutor.cokinfo.activity.AdminDetailPostinganActivity;
import com.tutor.cokinfo.activity.MemberDetailPostinganActivity;
import com.tutor.cokinfo.model.Postingan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PostinganAdapter extends RecyclerView.Adapter<PostinganAdapter.ViewHolder> {
    private ArrayList<Postingan> postingans;
    private Context context;
    private Activity activity;
    public PostinganAdapter(ArrayList<Postingan> postingans, Context context, Activity activity){
        this.postingans = postingans;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_postingan,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Postingan postingan = postingans.get(i);

        viewHolder.judul.setText(postingan.getJudul());
        viewHolder.harga.setText(postingan.getHarga());
        StorageReference gallery = FirebaseStorage.getInstance().getReference().child("Photo Postingan");
        final Bitmap[] my_image = new Bitmap[1];
        try {
            final File localFile = File.createTempFile("Images", "bmp");
            gallery.child(postingan.getId()+".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (!activity.isFinishing())
                        Glide.with(activity).load(my_image[0]).into(viewHolder.imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("Admin",Context.MODE_PRIVATE);
                if (sharedPreferences.getString("isAdmin","false").equals("true")){
                    Intent intent = new Intent(context,AdminDetailPostinganActivity.class);
                    intent.putExtra("postingan",postingan);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context,MemberDetailPostinganActivity.class);
                    intent.putExtra("postingan",postingan);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postingans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView judul, harga;
        View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.viewPostinganImage);
            judul = itemView.findViewById(R.id.viewPostinganJudul);
            harga = itemView.findViewById(R.id.viewPostinganHarga);
            container = itemView.findViewById(R.id.viewPostinganContainer);
        }
    }
}
