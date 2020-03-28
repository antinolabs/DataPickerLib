package io.antinolabs.libs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.ImageUtils;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
  private Context ctx;
  private ArrayList<String> paths;
  SelectedUrisInterface selectedUrisInterface;
  int currentPosition=1;




  public ImageAdapter(Context context, ArrayList<String> imagePaths, SelectedUrisInterface selectedUrisInterface) {
    this.ctx = context;
    this.paths = imagePaths;
    this.selectedUrisInterface = selectedUrisInterface;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new MyViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_image,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
   holder.imgitemgallery.setTag(position);
      int PlayStopButtonState = (int) holder.imgitemgallery.getTag();
      if (PlayStopButtonState == 0) {
        holder.imgitemgallery.setImageResource(R.drawable.ic_camera_alt_black_24dp);
      } else {
        Glide.with(ctx).load(paths.get(position)).
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
    int pos = 0;
    public MyViewHolder(@NonNull final View itemView) {
    ImageView imgItem, selectedItem;
    int pos = 0;
    public MyViewHolder(@NonNull final View itemView) {
      super(itemView);
      imgitemgallery = itemView.findViewById(R.id.img_item_gallery);
      imgitemgallery.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          pos = (int) imgitemgallery.getTag();
          if(pos==0)
          {
          }
          selectedUrisInterface.selectedImages(paths.get(pos));
        }
      });
      imgItem = itemView.findViewById(R.id.img_item);
      selectedItem = itemView.findViewById(R.id.selected_iv);
      imgItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          pos = (int) imgItem.getTag();
          if (selectedItem.getVisibility()==View.GONE){
            selectedUrisInterface.selectedImages(paths.get(pos));
          selectedItem.setVisibility(View.VISIBLE);}
          else{selectedUrisInterface.removeImages(paths.get(pos));
            selectedItem.setVisibility(View.GONE);}
        }
      });
    }
  }
}
