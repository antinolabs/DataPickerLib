package io.antinolabs.libs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.antinolabs.libs.Adapter.BottomViewPagerAdapter;
import io.antinolabs.libs.Adapter.HoriImageAdapter;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.Utils.Utils;
import io.antinolabs.libs.models.DataModel;

import static android.app.Activity.RESULT_OK;

public class BottomSheetPickerFragment extends BottomSheetDialogFragment implements View.OnClickListener, SelectedUrisInterface {

  public BaseBuilder builder;
  private RecyclerView horiRecyclerView;
  FragmentPagerAdapter fragmentPagerAdapter;
  TextView bottomsheetTvHeading;
  private View contentView;
  private PagerTabStrip pagerTabStrip;
  TextView emptyHolderTv;
  HoriImageAdapter horiImageAdapter;
  ViewPager bottomViewPager;
  ArrayList<Uri> selectedImages = new ArrayList<>();
  private Button doneBtn;
  private static final int REQUEST_IMAGE_CAPTURE = 2;
  static final int REQUEST_VIDEO_CAPTURE = 0;

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

  private ArrayList<DataModel> getAllImages(Activity activity){
    Utils utils = new Utils();
    return Utils.getAllImagesPath(activity);
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void setupDialog(@NonNull final Dialog dialog, int style) {
    super.setupDialog(dialog, style);
    contentView = View.inflate(getContext(), R.layout.content_view, null);
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
    initViews(view);
  }

  private void initViews(View view) {

    emptyHolderTv = view.findViewById(R.id.selected_photos_empty);
    horiRecyclerView = view.findViewById(R.id.horizontal_recycler);
    horiRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
    horiImageAdapter = new HoriImageAdapter(getActivity(),selectedImages,this);
    horiRecyclerView.setAdapter(horiImageAdapter);
    doneBtn = view.findViewById(R.id.btn_done);
    doneBtn.setOnClickListener(this);
    //initialize fragments
    fragmentPagerAdapter = new BottomViewPagerAdapter(getChildFragmentManager(), this);
    bottomViewPager = view.findViewById(R.id.bottom_view_pager);
    bottomViewPager.setAdapter(fragmentPagerAdapter);
    //seting TextprogramaticallyHeading
    bottomsheetTvHeading = view.findViewById(R.id.tv_title);
    bottomsheetTvHeading.setText(builder.setTextHeading);
    //setting TextProgramaticallyClosingButton
    doneBtn.setText(builder.setTextClosing);
    //pagerTabStrip DynamicCodeFunctionality
    pagerTabStrip = view.findViewById(R.id.pager_header);
    pagerTabStrip.setBackgroundColor(builder.colorcodeBackGround);
    pagerTabStrip.setDrawFullUnderline(false);
    pagerTabStrip.setTextColor(builder.colorCodePagerstripText);
    pagerTabStrip.setTabIndicatorColor(builder.colorCodePagerstripUnderline);
    emptyHolderTv.setText(builder.selectedEmptyText);
    emptyHolderTv.setTextColor(builder.selectedcoloremptyText);
    //
    if(builder.checked)
    {
      Log.d("Builder1", "onViewCreated: "+builder.checked);
      Toast.makeText(getActivity(), builder.ToastText, Toast.LENGTH_SHORT).show();
      builder.checked=false;
    }

  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_done) {
      dismiss();
    }
  }

  @Override
  public void selectedImages(String uri) {
    selectedImages.add(Uri.parse(uri));
    builder.onImageSelectedListener.onImageSelected(Uri.parse(uri));
    emptyHolderTv.setVisibility(View.GONE);
    horiRecyclerView.invalidate();
    horiImageAdapter.notifyDataSetChanged();
  }

  @Override
  public void removeImages(String uri) {
    selectedImages.remove(Uri.parse(uri));
    if (!(selectedImages.size() >0))
      emptyHolderTv.setVisibility(View.VISIBLE);
    horiRecyclerView.invalidate();
    horiImageAdapter.notifyDataSetChanged();
  }

  @Override
  public void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  @Override
  public void dispatchTakeVideoIntent() {
    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
      startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      try {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        if (imageBitmap != null) {
          Uri uri = Utils.getImageUri(getContext(), imageBitmap);
          selectedImages(uri.toString());
          horiImageAdapter.notifyDataSetChanged();
        }
      }
      catch (NullPointerException e){
      }
    }
    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
      Uri videoUri = data.getData();
      selectedImages(videoUri.toString());
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
    OnMultiImageSelectedListener onMultiImageSelectedListener;
    private String setTextHeading, setTextClosing, selectedEmptyText, ToastText;
    boolean vedioVariable, imageVariable,checked;
    private int colorcodeBackGround, colorCodePagerstripUnderline, colorCodePagerstripText, selectedcoloremptyText;

    BaseBuilder(@NonNull FragmentActivity fragmentActivity) {
      this.fragmentActivity = fragmentActivity;
    }

    public T setSelectMaxCount(int selectMaxCount) {
      this.selectMaxCount = selectMaxCount;
      return (T) this;
    }

    public T setTextNameBottomSheetHeading(String textName) {
         this.setTextHeading = textName;
      return (T) this;
    }

    public T setTextNameBottomSheetHeadingClose(String textNameCloseHeading) {
      this.setTextClosing = textNameCloseHeading;
      return (T) this;
    }

    public T setOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
      this.onImageSelectedListener = onImageSelectedListener;
      return (T) this;
    }

    public T setPagerStripBackgroundColor(int colorcodeBackGround) {
      this.colorcodeBackGround = colorcodeBackGround;
      return (T) this;
    }

    public T setPagerTabIndicatorColor(int colorCode) {
      this.colorCodePagerstripUnderline = colorCode;
      return (T) this;
    }

    public T setPagerTabTextColor(int colorCodePagerstripText) {
      this.colorCodePagerstripText = colorCodePagerstripText;
      return (T) this;
    }
    public T selectedImagePhotoEmptyText(String selectedemptyText) {
      this.selectedEmptyText = selectedemptyText;
      return (T) this;
    }

    public T selectedImagesEnable(boolean imagevariable) {
      this.imageVariable = imagevariable;
      bothDisable();
      return (T) this;
    }

    public T selectedVideosEnable(boolean videovariable) {
      this.vedioVariable = videovariable;
      bothDisable();
      return (T) this;
    }

    public T selectedColorEmptyText(int selectedColoremptyText) {
      this.selectedcoloremptyText = selectedColoremptyText;
      return (T) this;
    }

    public void bothDisable()
    {
      if(!imageVariable&&!vedioVariable)
      {
        Log.d("Builder2", "onViewCreated: "+imageVariable+" "+vedioVariable);
        ToastText= "Please Enable Vedio or Image atleast one";
        checked = true;
      }
    }

    public BottomSheetPickerFragment create() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        && ContextCompat.checkSelfPermission(fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        throw new RuntimeException("Missing required WRITE_EXTERNAL_STORAGE permission. Did you remember to request it first?");
      }

      try {
        if (onImageSelectedListener == null && onMultiImageSelectedListener == null) {
          throw new RuntimeException
                  ("You have to use setOnImageSelectedListener() or setOnMultiImageSelectedListener() for receive selected Uri");
        }
        if (ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          throw new RuntimeException
                  ("Please check external storage permission.");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      BottomSheetPickerFragment customBottomSheetDialogFragment = new BottomSheetPickerFragment();
      customBottomSheetDialogFragment.builder = this;
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
