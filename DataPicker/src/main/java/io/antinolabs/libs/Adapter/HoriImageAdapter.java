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

import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;

public class HoriImageAdapter extends RecyclerView.Adapter<HoriImageAdapter.MyViewHolder>{

    private Context ctx;
    private ArrayList<String> paths;
    SelectedUrisInterface selectedUrisInterface;

    public HoriImageAdapter(Context ctx, ArrayList<String> paths, SelectedUrisInterface selectedUrisInterface) {
        this.ctx = ctx;
        this.paths = paths;
        this.selectedUrisInterface = selectedUrisInterface;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem, removeImg;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item_hori);
            removeImg = itemView.findViewById(R.id.remove_image);
            removeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedUrisInterface.removeImages(paths.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public HoriImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoriImageAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hori_item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoriImageAdapter.MyViewHolder holder, int position) {
        Glide.with(ctx).load(paths.get(position)).
                error(android.R.drawable.alert_dark_frame).
                into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}
