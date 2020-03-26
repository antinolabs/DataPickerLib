package com.example.librarysampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.DataPicker;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button button = findViewById(R.id.buttonAdd);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataPicker
                    .with(MainActivity.this)
                    .setSelectMaxCount(2)
                    .show(new BottomSheetPickerFragment.OnImageSelectedListener() {
                        @Override
                        public void onImageSelected(Uri uri) {

                        }
                    });
        }
    });

  }
}
