package io.antinolabs.libs.Interfaces;

import java.util.List;

public interface SelectedUrisInterface {
    public void selectedImages(String uri);

    public void selectedImages(List<String> uri);
}
package io.antinolabs.libs.Interfaces;

import android.net.Uri;

public interface SelectedUrisInterface {
    public void selectedImages(String uri);
    public void removeImages(String uri);
}
