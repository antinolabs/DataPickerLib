package io.antinolabs.libs;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class DataPicker extends BottomSheetPickerFragment {
  private Context context;


  public static Builder with(FragmentActivity fragmentActivity) {
    return new Builder(fragmentActivity);
  }

  public static class Builder extends BaseBuilder<Builder> {

    private Builder(FragmentActivity fragmentActivity) {
      super(fragmentActivity);
    }

    public void show(OnImageSelectedListener onImageSelectedListener) {
      this.onImageSelectedListener = onImageSelectedListener;
      create().show(fragmentActivity.getSupportFragmentManager());
    }
  }
}
