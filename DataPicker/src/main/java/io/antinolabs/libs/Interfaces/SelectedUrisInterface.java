package io.antinolabs.libs.Interfaces;

import android.net.Uri;

public interface SelectedUrisInterface {

    public void selectedImages(String uri);

    public void removeImages(String uri);

    public void dispatchTakePictureIntent();
}
