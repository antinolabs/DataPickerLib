package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
  private final static int MY_PERMISSIONS_REQUEST_READ_CAMERA_STORAGE = 101;

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
          // Here, thisActivity is the current activity
          if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
          && ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
          && ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

              ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                  Manifest.permission.CAMERA,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE,
                  Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CAMERA_STORAGE);

          } else {
            showDataPicker();
          }
        }
    });
  }

  private void showDataPicker(){
    DataPicker
      .with(MainActivity.this)
      .setSelectMaxCount(2)
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
  }

  public void setImages(List<Uri> imageList){
      MultiImageAdapter multiImageAdapter = new MultiImageAdapter(getBaseContext(),imageList);
      imageRecycler.setAdapter(multiImageAdapter);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_READ_CAMERA_STORAGE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

          } else {
            showDataPicker();
          }
        } else {
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
        }
        return;
      }

      // other 'case' lines to check for other
      // permissions this app might request.
    }
  }
}
