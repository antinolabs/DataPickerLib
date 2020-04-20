package io.antinolabs.libs;

import androidx.fragment.app.FragmentActivity;

public class DataPicker extends BottomSheetPickerFragment {

  public static Builder with(FragmentActivity fragmentActivity) {
    return new Builder(fragmentActivity);
  }

  public static class Builder extends BaseBuilder<Builder> {

    private Builder(FragmentActivity fragmentActivity) {
      super(fragmentActivity);
    }

    public void show(OnMultiImageSelectedListener onMultiImageSelectedListener) {
      this.onMultiImageSelectedListener = onMultiImageSelectedListener;
      create().show(fragmentActivity.getSupportFragmentManager(), false);
    }
  }
}
