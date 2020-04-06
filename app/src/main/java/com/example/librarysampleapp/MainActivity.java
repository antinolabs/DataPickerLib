package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.DataPicker;

public class MainActivity extends AppCompatActivity {

    int PICK_FROM_GALLERY = 0;
    ImageView mainImage;
    Button buttonOpen;
    RecyclerView imageRecycler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mainImage = findViewById(R.id.imageIv);
    buttonOpen = findViewById(R.id.buttonOpen);
    imageRecycler = findViewById(R.id.imageRecycler);
    buttonOpen.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            }
            DataPicker
                    .with(MainActivity.this)
                    .setSelectMaxCount(4)
                    .setTextNameBottomSheetHeading("Select Media")
                    .setTextNameBottomSheetHeadingClose("Done")
                    .setPagerTabTextColor(getResources().getColor(android.R.color.black))
                    .selectedImagePhotoEmptyText("No media selected")
                    .selectedColorEmptyText(Color.rgb(240, 120, 120))
                    .selectedImagesEnable(true)
                    .selectedVideosEnable(true)
                    .show(new BottomSheetPickerFragment.OnMultiImageSelectedListener() {
                        @Override
                        public void onImagesSelected(final List<Uri> uriList) {
                            for (int i = 0; i < uriList.size(); i++) {
                                mainImage.setVisibility(View.GONE);
                                setImages(uriList);
                            }
                        }
                    });
                    /*.show(new BottomSheetPickerFragment.OnImageSelectedListener() {
                        @Override
                        public void onImageSelected(Uri uri) {
                            mainImage.setVisibility(View.VISIBLE);
                            mainImage.setImageURI(uri);
                        }
                    });*/

        }
    });
  }

  public void setImages(List<Uri> imageList){
      MultiImageAdapter multiImageAdapter = new MultiImageAdapter(getBaseContext(),imageList);
      imageRecycler.setAdapter(multiImageAdapter);
  }
}
