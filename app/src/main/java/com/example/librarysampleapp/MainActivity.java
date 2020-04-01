package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.DataPicker;

public class MainActivity extends AppCompatActivity implements BottomSheetPickerFragment.OnImageSelectedListener, BottomSheetPickerFragment.OnMultiImageSelectedListener {

    int PICK_FROM_GALLERY = 0;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      final ImageView mainimage = findViewById(R.id.imageIv);

    Button buttonOpen = findViewById(R.id.buttonOpen);
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
                    .show((BottomSheetPickerFragment.OnImageSelectedListener) MainActivity.this);
        }
    });
  }

    @Override
    public void onImagesSelected(List<Uri> uriList) {

    }

    @Override
    public void onImageSelected(Uri uri) {
        Log.d("*1234*", "onImageSelected: "+uri);
       // mainimage.setImageURI(uri);
    }
}
