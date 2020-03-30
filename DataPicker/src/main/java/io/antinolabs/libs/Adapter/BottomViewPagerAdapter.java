package io.antinolabs.libs.Adapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.Fragments.ImageFragment;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;

public class BottomViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    SelectedUrisInterface selectedUrisInterface;

    public BottomViewPagerAdapter(@NonNull FragmentManager fm, BottomSheetPickerFragment selectedUrisInterface) {
        super(fm);
        this.selectedUrisInterface = selectedUrisInterface;
    }

    @NonNull
    @Override
    public ImageFragment getItem(int position) {
        switch (position) {
            case 0:
                return ImageFragment.newInstance(selectedUrisInterface, "Images");
            case 1:
                return ImageFragment.newInstance(selectedUrisInterface, "Videos");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Images";
        else
            return "Videos";
    }
}
