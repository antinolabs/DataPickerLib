package com.example.librarysampleapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ImagesViewHolder> {

    Context context;
    List<Uri> imageUris;

    public MultiImageAdapter(Context context, List<Uri> imageUris){
        this.context = context;
        this.imageUris = imageUris;
    }
    class ImagesViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgs_item);
        }
    }
    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.imageView.setImageURI(imageUris.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }
}
