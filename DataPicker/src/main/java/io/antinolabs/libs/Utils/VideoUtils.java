package io.antinolabs.libs.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class VideoUtils {

    public static ArrayList<ModelVideo> getAllVideosPath(Activity activity){
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name,column_id,thum;
        ArrayList listOfAllVideos = new ArrayList();
        ArrayList listofAllVideosthum = new ArrayList();
        String absolutePathOfVideo = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfVideo = cursor.getString(column_index_data);
            ModelVideo obj_model = new ModelVideo();
            obj_model.setBooleanselected(false);
            obj_model.setStrpath(absolutePathOfVideo);
            obj_model.setStrthumb(cursor.getString(thum));

            listOfAllVideos.add(obj_model);
        }


        return listOfAllVideos;

    }

}
