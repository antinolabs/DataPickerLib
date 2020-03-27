package io.antinolabs.libs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.antinolabs.libs.Adapter.BottomViewPagerAdapter;
import io.antinolabs.libs.Adapter.HoriImageAdapter;
import io.antinolabs.libs.Adapter.ImageAdapter;
import io.antinolabs.libs.Fragments.ImageFragment;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.Utils.ImageUtils;

public class BottomSheetPickerFragment extends BottomSheetDialogFragment implements View.OnClickListener, SelectedUrisInterface {

  public BaseBuilder builder;
  private RecyclerView horiRecyclerView;
  FragmentPagerAdapter fragmentPagerAdapter;
  TextView emptyHolderTv;
  HoriImageAdapter horiImageAdapter;
  ViewPager bottomViewPager;
  ArrayList<String> selectedImages = new ArrayList<>();
  private Button doneBtn;
  private static final int PICK_FROM_GALLERY = 1;
  private RecyclerView.LayoutManager layoutManager;
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

  private ArrayList<String> getAllImages(Activity activity){
    ImageUtils imageUtils = new ImageUtils();
    return ImageUtils.getAllImagesPath(activity);
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void setupDialog(@NonNull final Dialog dialog, int style) {
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.content_view, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    emptyHolderTv = view.findViewById(R.id.selected_photos_empty);
    horiRecyclerView = view.findViewById(R.id.horizontal_recycler);
    horiRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
    horiImageAdapter = new HoriImageAdapter(getActivity(),selectedImages, this);
    horiRecyclerView.setAdapter(horiImageAdapter);
    doneBtn = view.findViewById(R.id.btn_done);
    doneBtn.setOnClickListener(this);
    fragmentPagerAdapter = new BottomViewPagerAdapter(getChildFragmentManager(), this);
    bottomViewPager = view.findViewById(R.id.bottom_view_pager);
    bottomViewPager.setAdapter(fragmentPagerAdapter);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_done) {
      dismiss();
    }
  }

  @Override
  public void selectedImages(String uri) {
    selectedImages.add(uri);
    emptyHolderTv.setVisibility(View.GONE);
    horiRecyclerView.invalidate();
    horiImageAdapter.notifyDataSetChanged();
  }

  @Override
  public void removeImages(String uri) {
    selectedImages.remove(uri);
    if (!(selectedImages.size() >0))
      emptyHolderTv.setVisibility(View.VISIBLE);
    horiRecyclerView.invalidate();
    horiImageAdapter.notifyDataSetChanged();
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
    OnMultiImageSelectedListener onMultiImageSelectedListener;

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

      try {
        if (onImageSelectedListener == null && onMultiImageSelectedListener == null) {
        throw new RuntimeException
                ("You have to use setOnImageSelectedListener() or setOnMultiImageSelectedListener() for receive selected Uri");
      }
        if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(fragmentActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        }
        else {
          BottomSheetPickerFragment customBottomSheetDialogFragment = new BottomSheetPickerFragment();
          customBottomSheetDialogFragment.builder = this;
          return customBottomSheetDialogFragment;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MediaType.IMAGE, MediaType.VIDEO})
    public @interface MediaType {
      int IMAGE = 1;
      int VIDEO = 2;
    }
  }
}
