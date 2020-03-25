package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;

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
      .setSelectMaxCount(2)
      .show(new BottomSheetPickerFragment.OnImageSelectedListener() {
        @Override
        public void onImageSelected(Uri uri) {

        }
      });
  }
}
