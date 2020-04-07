package io.antinolabs.libs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.antinolabs.libs.Adapter.ImageAdapter;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.Utils.Constants;
import io.antinolabs.libs.Utils.Utils;
import io.antinolabs.libs.models.DataModel;

import static android.app.Activity.RESULT_OK;

public class BottomSheetPickerFragment extends BottomSheetDialogFragment implements View.OnClickListener, SelectedUrisInterface {

  private BaseBuilder builder;
  private RecyclerView recyclerView;
  private TextView bottomsheetTvHeading, tabImage, tabVideo;
  private View contentView;
  private TextView emptyHolderTv;
  private Uri singleImageUri;
  private ArrayList<Uri> selectedImages = new ArrayList<>();
  private HorizontalScrollView scHorizontalView;
  private Button doneBtn;
  private ImageAdapter imageAdapter, videoAdapter;
  private ViewGroup.MarginLayoutParams layoutParams;
  private LinearLayout linearLayoutHorizontalImages;
  private static final int REQUEST_IMAGE_CAPTURE = 2;
  private static final int REQUEST_VIDEO_CAPTURE = 0;

  private ArrayList<DataModel> imagePaths, videoPaths;

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

    //getData
    imagePaths = Utils.getAllImagesPath(this.getActivity());
    videoPaths = Utils.getAllVideosPath(this.getActivity());

    DataModel dataModelImage = new DataModel("", Constants.CAMERA_IMAGE);
    imagePaths.add(0, dataModelImage);

    DataModel dataModelVideo = new DataModel("", Constants.CAMERA_VIDEO);
    videoPaths.add(0, dataModelVideo);
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
    doneBtn = view.findViewById(R.id.btn_done);
    doneBtn.setOnClickListener(this);

    //seting TextprogramaticallyHeading
    bottomsheetTvHeading = view.findViewById(R.id.tv_title);
    bottomsheetTvHeading.setText(builder.setTextHeading);
    //setting TextProgramaticallyClosingButton
    doneBtn.setText(builder.setTextClosing);

    emptyHolderTv.setText(builder.selectedEmptyText);
    emptyHolderTv.setTextColor(builder.selectedcoloremptyText);

    recyclerView = view.findViewById(R.id.rc_gallery);
    recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

    //tabs
    tabImage = view.findViewById(R.id.tab_image);
    tabVideo = view.findViewById(R.id.tab_video);
    tabImage.setOnClickListener(this);
    tabVideo.setOnClickListener(this);
    //

    //add scrollview
    scHorizontalView = view.findViewById(R.id.sv_horizontal);
    linearLayoutHorizontalImages = view.findViewById(R.id.linear_horizontal_images);
    layoutParams = new ViewGroup.MarginLayoutParams(130, 130);
    layoutParams.setMargins(2,2,2,2);

    if(builder.checked)
    {
      Log.d("Builder1", "onViewCreated: " + builder.checked);
      Toast.makeText(getActivity(), builder.ToastText, Toast.LENGTH_SHORT).show();
      builder.checked=false;
    }

    switchTabs(BaseBuilder.MediaType.IMAGE);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_done) {
      try {
        builder.onImageSelectedListener.onImageSelected(singleImageUri);
      }
      catch (Exception ex){
        builder.onMultiImageSelectedListener.onImagesSelected(selectedImages);
      }
      dismiss();
    }
    else if(v.getId() == R.id.tab_image){
      switchTabs(BaseBuilder.MediaType.IMAGE);
    }
    else if(v.getId() == R.id.tab_video){
      switchTabs(BaseBuilder.MediaType.VIDEO);
    }
  }

  private void switchTabs(int type) {
    if(type == BaseBuilder.MediaType.IMAGE){
      tabImage.setTypeface(null, Typeface.BOLD);
      tabVideo.setTypeface(null, Typeface.NORMAL);
      setImageRecyclerView();
    }
    else if(type == BaseBuilder.MediaType.VIDEO){
      tabVideo.setTypeface(null, Typeface.BOLD);
      tabImage.setTypeface(null, Typeface.NORMAL);
      setVideoRecyclerView();
    }
  }

  private void setImageRecyclerView() {
    if(imageAdapter == null)
      imageAdapter = new ImageAdapter(this.getContext(), imagePaths, this);
      recyclerView.setAdapter(imageAdapter);
  }

  private void setVideoRecyclerView() {
    if(videoAdapter == null)
      videoAdapter = new ImageAdapter(this.getContext(), videoPaths, this);
      recyclerView.setAdapter(videoAdapter);
  }

  @Override
  public void selectedImages(String uri) {
    selectedImages.add(Uri.parse(uri));
    this.singleImageUri = Uri.parse(uri);
    emptyHolderTv.setVisibility(View.GONE);

    ImageView imageView = new ImageView(getContext());
    imageView.setLayoutParams(layoutParams);
    imageView.setTag(uri);

    Glide.with(getContext()).load(uri).
      error(android.R.drawable.alert_dark_frame).
      centerCrop().
      into(imageView);

    linearLayoutHorizontalImages.addView(imageView);
  }

  @Override
  public void removeImages(String uri) {
    int pos = selectedImages.indexOf(Uri.parse(uri));
    if(pos > -1)
      if(linearLayoutHorizontalImages.getChildAt(pos).getTag().equals(uri))
        linearLayoutHorizontalImages.removeViewAt(pos);

    selectedImages.remove(Uri.parse(uri));
    if (!(selectedImages.size() > 0))
      emptyHolderTv.setVisibility(View.VISIBLE);
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
