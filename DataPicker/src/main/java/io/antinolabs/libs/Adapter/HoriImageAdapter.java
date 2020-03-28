package io.antinolabs.libs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.antinolabs.libs.R;

public class HoriImageAdapter extends RecyclerView.Adapter<HoriImageAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> paths;

    public HoriImageAdapter(Context ctx, ArrayList<String> paths) {
        this.ctx = ctx;
        this.paths = paths;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgitemgallery;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgitemgallery = itemView.findViewById(R.id.img_item_gallery);
        }
    }

    @NonNull
    @Override
    public HoriImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoriImageAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoriImageAdapter.MyViewHolder holder, int position) {
        holder.imgitemgallery.setTag(position);

            Glide.with(ctx).load(paths.get(position)).
                    error(android.R.drawable.stat_notify_error).
                    into(holder.imgitemgallery);


    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}
