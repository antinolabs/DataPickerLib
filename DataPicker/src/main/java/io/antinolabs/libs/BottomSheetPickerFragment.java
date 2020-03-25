package io.antinolabs.libs;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class BottomSheetPickerFragment extends BottomSheetDialogFragment {

  public BaseBuilder builder;
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback(){

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }
  };

  public void show(FragmentManager fragmentManager) {
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.add(this, getTag());
    ft.commitAllowingStateLoss();
  }


  @Override
  public void setupDialog(@NonNull Dialog dialog, int style) {
    super.setupDialog(dialog, style);

    View contentView = View.inflate(getContext(), R.layout.content_view, null);
    dialog.setContentView(contentView);
    CoordinatorLayout.LayoutParams layoutParams =
      (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
    CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
    if (behavior instanceof BottomSheetBehavior) {
      ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
      ((BottomSheetBehavior) behavior).setPeekHeight(600);

    }
  }

  public interface OnMultiImageSelectedListener {
    void onImagesSelected(List<Uri> uriList);
  }

  public interface OnImageSelectedListener {
    void onImageSelected(Uri uri);
  }

  public abstract static class BaseBuilder<T extends BaseBuilder> {

    FragmentActivity fragmentActivity;
    int selectMaxCount;
    OnImageSelectedListener onImageSelectedListener;

    BaseBuilder(@NonNull FragmentActivity fragmentActivity) {

      this.fragmentActivity = fragmentActivity;
    }

    public T setSelectMaxCount(int selectMaxCount) {
      this.selectMaxCount = selectMaxCount;
      return (T) this;
    }

    public T setOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
      this.onImageSelectedListener = onImageSelectedListener;
      return (T) this;
    }

    public BottomSheetPickerFragment create() {
      /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        && ContextCompat.checkSelfPermission(fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        throw new RuntimeException("Missing required WRITE_EXTERNAL_STORAGE permission. Did you remember to request it first?");
      }*/

      /*if (onImageSelectedListener == null && onMultiImageSelectedListener == null) {
        throw new RuntimeException("You have to use setOnImageSelectedListener() or setOnMultiImageSelectedListener() for receive selected Uri");
      }*/

      BottomSheetPickerFragment customBottomSheetDialogFragment = new BottomSheetPickerFragment();
      customBottomSheetDialogFragment.builder = (T) this;
      return customBottomSheetDialogFragment;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MediaType.IMAGE, MediaType.VIDEO})
    public @interface MediaType {
      int IMAGE = 1;
      int VIDEO = 2;
    }


  }
}
