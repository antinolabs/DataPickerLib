package io.antinolabs.libs.Interfaces;

public interface SelectedUrisInterface {

    void selectedImages(String uri);

    void removeImages(String uri);

    void dispatchTakePictureIntent();

    void dispatchTakeVideoIntent();

    int selectedImagesCount();

}
