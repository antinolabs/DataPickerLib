package io.antinolabs.libs.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class ImageUtils {

  /**
   * Getting All Images Path.
   *
   * @param activity
   *            the activity
   * @return ArrayList with images Path
   */
  public static ArrayList<String> getAllImagesPath(Activity activity) {
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    String absolutePathOfImage = null;
    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = { MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

    cursor = activity.getContentResolver().query(uri, projection, null,
            null, null);

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);

      listOfAllImages.add(absolutePathOfImage);
    }
    return listOfAllImages;
  }

  /**
   * Converting String path to Bitmap.
   *
   * @param path
   *            the file path
   * @return Bitmap
   */
  public static Bitmap getBitmapFromPath(String path){
    File imgFile = new  File(path);

    if(imgFile.exists()){
      return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }
    return null;
  }
}
