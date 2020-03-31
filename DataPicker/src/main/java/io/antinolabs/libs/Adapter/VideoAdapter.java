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
import io.antinolabs.libs.Utils.Constants;
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
                .inflate(R.layout.video_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgitemgallery.setTag(position);
        if (paths.get(position).getFileType() == Constants.CAMERA_VIDEO) {
            holder.imgitemgallery.setBackgroundColor(ctx.getResources().getColor(R.color.semi_transparent));
            holder.imgitemgallery.setPadding(130,130,130,130);
            holder.imgitemgallery.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(ctx).load(R.drawable.ic_videocam_grey_24dp).into(holder.imgitemgallery);
        } else {
            holder.videoPlayIv.setVisibility(View.VISIBLE);
            Glide.with(ctx).load(paths.get(position).getPath()).
                    error(android.R.drawable.stat_notify_error).
                    into(holder.imgitemgallery);
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgitemgallery, selectedItem, videoPlayIv;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgitemgallery = itemView.findViewById(R.id.video_view);
            selectedItem = itemView.findViewById(R.id.selected_iv_video);
            videoPlayIv = itemView.findViewById(R.id.video_play_iv);
            imgitemgallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paths.get(getAdapterPosition()).getFileType() == Constants.CAMERA_VIDEO) {
                        selectedUrisInterface.dispatchTakeVideoIntent();
                    } else {
                        if (selectedItem.getVisibility() == View.GONE) {
                            selectedUrisInterface.selectedImages(paths.get(getAdapterPosition()).getPath());
                            selectedItem.setVisibility(View.VISIBLE);
                        } else {
                            selectedUrisInterface.removeImages(paths.get(getAdapterPosition()).getPath());
                            selectedItem.setVisibility(View.GONE);
                        }
                    }
                    }
                });
            }
        }
    }
