package io.antinolabs.libs.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.ImageUtils;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
  private Context ctx;
  private ArrayList<String> paths;

  public ImageAdapter(Context ctx, ArrayList<String> paths) {
    this.ctx = ctx;
    this.paths = paths;
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
    Uri bmp = Uri.fromFile(new File(paths.get(position)));
    if(bmp != null){
      Glide.with(ctx).load(bmp).into(holder.imgItem);
    }
  }

  @Override
  public int getItemCount() {
    return paths.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imgItem;
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      imgItem = itemView.findViewById(R.id.img_item);
    }
  }
}
