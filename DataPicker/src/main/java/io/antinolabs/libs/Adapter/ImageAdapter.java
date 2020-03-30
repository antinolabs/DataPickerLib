package io.antinolabs.libs.Adapter;

import android.content.Context;
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
import io.antinolabs.libs.Utils.Constants;
import io.antinolabs.libs.Utils.ImageUtils;
import io.antinolabs.libs.models.DataModel;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
  private Context ctx;
  private ArrayList<DataModel> paths;
  SelectedUrisInterface selectedUrisInterface;


  public ImageAdapter(Context ctx, ArrayList<DataModel> paths, SelectedUrisInterface selectedUrisInterface) {
    this.ctx = ctx;
    this.paths = paths;
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
    //Bitmap bmp = ImageUtils.getBitmapFromPath(paths.get(position));
    holder.imgItem.setTag(position);
    /*Uri bmp = Uri.fromFile(new File(paths.get(position)));
    if(bmp != null){*/

    if(paths.get(position).getFileType()  == Constants.IMAGE){
      Glide.with(ctx).load(paths.get(position).getPath()).
              error(android.R.drawable.alert_dark_frame).
              into(holder.imgItem);
    }
    else if(paths.get(position).getFileType() == Constants.VIDEO){

    }
    else if(paths.get(position).getFileType() == Constants.CAMERA_IMAGE){
      holder.imgItem.setBackgroundColor(ctx.getResources().getColor(R.color.semi_transparent));
      Glide.with(ctx).load(R.drawable.ic_camera_alt_grey_24dp).into(holder.imgItem);
    }
    else{
      //handle video camera case
    }
   // }

  }

  @Override
  public int getItemCount() {
    return paths.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgItem, selectedItem;
    public MyViewHolder(@NonNull final View itemView) {
      super(itemView);
      imgItem = itemView.findViewById(R.id.img_item);
      selectedItem = itemView.findViewById(R.id.selected_iv);
      imgItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (paths.get(getAdapterPosition()).getFileType() == Constants.CAMERA_IMAGE) {
              selectedUrisInterface.dispatchTakePictureIntent();
          }
          else {
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
