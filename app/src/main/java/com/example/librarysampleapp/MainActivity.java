package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.DataPicker;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    DataPicker
      .with(MainActivity.this)
      .setSelectMaxCount(4)
      .setTextNameBottomSheetHeading("Hello")
      .setTextNameBottomSheetHeadingClose("Fini")
      .setPagerStripBackgroundColor(Color.rgb(120, 240, 124))
            .setPagerTabTextColor(Color.rgb(120, 120, 240))
      .setPagerTabIndicatorColor(Color.rgb(220, 120,120 ))
            .selectedImagePhotoEmptyText("Showing Image")
            .selectedColorEmptyText(Color.rgb(240, 120, 120))
            .selectedImagesEnable(true)
            .selectedVideosEnable(true)
      .show(new BottomSheetPickerFragment.OnImageSelectedListener() {
        @Override
        public void onImageSelected(Uri uri) {

        }
      });

  }
}
