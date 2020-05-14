package io.antinolabs.libs.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.Constants;
import io.antinolabs.libs.Utils.Utils;
import io.antinolabs.libs.models.DataModel;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
  private final int maxCount;
  private Context ctx;
  private ArrayList<DataModel> paths;
  private SelectedUrisInterface selectedUrisInterface;
  private int posSingle = -1;

  public ImageAdapter(Context ctx, ArrayList<DataModel> paths, SelectedUrisInterface selectedUrisInterface) {
    this.ctx = ctx;
    this.paths = paths;
    this.selectedUrisInterface = selectedUrisInterface;

    maxCount = BottomSheetPickerFragment.builder.selectMaxCount;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new MyViewHolder(LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_image,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    if(paths.get(position).getFileType()  == Constants.IMAGE){
      Glide.with(ctx).load(new File(paths.get(position).getPath())).
        error(android.R.drawable.alert_dark_frame).
        centerCrop().
        dontAnimate().
        into(holder.imgItem);

      if(paths.get(position).isSelected())
        holder.selectedItem.setVisibility(View.VISIBLE);
      else
        holder.selectedItem.setVisibility(View.GONE);

    }
    else if(paths.get(position).getFileType() == Constants.CAMERA_IMAGE){
      holder.imgItem.setBackgroundColor(ctx.getResources().getColor(R.color.accent));
      holder.imgItem.setPadding(85,85,85,85);
      holder.imgItem.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_camera_alt_grey_24dp));
    }
    else if (paths.get(position).getFileType() == Constants.CAMERA_VIDEO) {
      holder.imgItem.setBackgroundColor(ctx.getResources().getColor(R.color.accent));
      holder.imgItem.setPadding(130,130,130,130);
      holder.imgItem.setScaleType(ImageView.ScaleType.FIT_XY);

      holder.imgItem.setPadding(85,85,85,85);
      holder.imgItem.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_videocam_grey_24dp));

    } else {
      holder.videoView.setVisibility(View.VISIBLE);
      Glide.with(ctx).load(paths.get(position).getPath()).
        error(android.R.drawable.stat_notify_error).
        into(holder.imgItem);

      if(paths.get(position).isSelected())
        holder.selectedItem.setVisibility(View.VISIBLE);
      else
        holder.selectedItem.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return paths.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgItem, selectedItem, videoView;
    public MyViewHolder(@NonNull final View itemView) {
      super(itemView);
      imgItem = itemView.findViewById(R.id.img_item);
      selectedItem = itemView.findViewById(R.id.selected_iv);
      videoView = itemView.findViewById(R.id.video_play_iv);
      imgItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (paths.get(getAdapterPosition()).getFileType() == Constants.CAMERA_IMAGE) {
            if(isAllowedToSelectMoreImages(selectedUrisInterface))
              selectedUrisInterface.dispatchTakePictureIntent();
            else
              Utils.showToast(ctx, "You can only select " + maxCount + " images/videos");
          }
          else if(paths.get(getAdapterPosition()).getFileType() == Constants.CAMERA_VIDEO){
            if(isAllowedToSelectMoreImages(selectedUrisInterface))
              selectedUrisInterface.dispatchTakeVideoIntent();
            else
              Utils.showToast(ctx, "You can only select " + maxCount + " images/videos");
          }
          else {
            if (!paths.get(getAdapterPosition()).isSelected()) {
                if(isAllowedToSelectMoreImages(selectedUrisInterface))
                  select(View.GONE, getAdapterPosition());
                else
                  Utils.showToast(ctx, "You can only select " + maxCount + " images/videos");

            }
            else
              select(View.VISIBLE, getAdapterPosition());
          }
        }
      });
    }

    private void select(int visibility, int pos){
      if(maxCount != 1){
        changeView(visibility, pos);
      }
      else{
        if(posSingle > -1){
          if(getAdapterPosition() != posSingle){
            changeView(View.VISIBLE, posSingle);
            changeView(View.GONE, getAdapterPosition());
          }else{
            if(paths.get(getAdapterPosition()).isSelected()){
              changeView(View.VISIBLE, posSingle);
            }else{
              changeView(View.GONE, getAdapterPosition());
            }
          }
          if(posSingle == getAdapterPosition())
            posSingle = -1;
          else
            posSingle = getAdapterPosition();
        }
        else{
          changeView(View.GONE, getAdapterPosition());
          posSingle = getAdapterPosition();
        }
      }
    }

    private void changeView(int visibility, int pos){
      if(visibility == View.GONE) {
        Log.d("Visibility: ", "GONE");
        selectedUrisInterface.selectedImages(paths.get(pos).getPath());
        paths.get(getAdapterPosition()).setSelected(true);
      }else {
        Log.d("Visibility: ", "VISIBLE");
        selectedUrisInterface.removeImages(paths.get(pos).getPath());
        paths.get(getAdapterPosition()).setSelected(false);
      }

      notifyItemChanged(getAdapterPosition());
    }


    private boolean isAllowedToSelectMoreImages(SelectedUrisInterface selectedUrisInterface){
      return selectedUrisInterface.selectedImagesCount() < maxCount;
    }
  }
}
