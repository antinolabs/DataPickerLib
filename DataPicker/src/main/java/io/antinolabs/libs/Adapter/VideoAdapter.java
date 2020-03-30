package io.antinolabs.libs.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.ModelVideo;
import io.antinolabs.libs.models.DataModel;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private Context ctx;
    private ArrayList<DataModel> paths;
    SelectedUrisInterface selectedUrisInterface;

    public VideoAdapter(Context context, ArrayList<DataModel> imagePaths, SelectedUrisInterface selectedUrisInterface) {
        this.ctx = context;
        this.paths = imagePaths;
        this.selectedUrisInterface = selectedUrisInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgitemgallery.setTag(position);
        int PlayStopButtonState = (int) holder.imgitemgallery.getTag();
        if (PlayStopButtonState == 0) {
            holder.imgitemgallery.setImageResource(R.drawable.ic_camera_alt_black_24dp);
        } else {
            Glide.with(ctx).load(paths.get(position).getPath()).
                    error(android.R.drawable.stat_notify_error).
                    into(holder.imgitemgallery);
            holder.imgitemgallery.setTag(1);
        }

    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgitemgallery;
        TextView textView;
        int pos = 0;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgitemgallery = itemView.findViewById(R.id.video_view);
            textView = itemView.findViewById(R.id.text);
            imgitemgallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedUrisInterface.selectedImages(paths.get(pos).getPath());
                }
            });
        }
    }
}
