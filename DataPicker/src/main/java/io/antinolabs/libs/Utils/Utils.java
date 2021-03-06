package io.antinolabs.libs.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import io.antinolabs.libs.models.DataModel;

public class Utils {

  /**
   * Getting All Images Path.
   *
   * @param activity
   *            the activity
   * @return ArrayList with images Path
   */
  public static ArrayList<DataModel> getAllImagesPath(Activity activity) {
    ArrayList<DataModel> images;

    Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String bucketName = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;

    images = prepareGallaryData(activity, externalUri, bucketName, Constants.IMAGE);
    Collections.reverse(images);

    return images;
  }

  public static ArrayList<DataModel> getAllVideosPath(Activity activity){
    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    String bucketName = MediaStore.Video.Media.BUCKET_DISPLAY_NAME;

    return prepareGallaryData(activity, uri, bucketName, Constants.VIDEO);
  }

  private static ArrayList<DataModel> prepareGallaryData(Activity activity, Uri uri, String bucketName, int type){
    Cursor cursor;
    int column_index_data;
    String absolutePathOfImage = null;
    ArrayList<DataModel> paths = new ArrayList<>();
    String[] projection = { MediaStore.MediaColumns.DATA,
      bucketName};

    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

    cursor = activity.getContentResolver().query(uri, projection, null
      ,null,orderBy + " ASC");

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);
      DataModel dataModel = new DataModel(
        absolutePathOfImage,
        type);
      paths.add(dataModel);
    }
    return paths;
  }

  public static Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }

  public static String getRealPathFromURI(Context inContext, Uri uri) {
    String path = "";
    if (inContext.getContentResolver() != null) {
      Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        path = cursor.getString(idx);
        cursor.close();
      }
    }
    return path;
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

  public static void showToast(Context context, String msg){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
  }
}
