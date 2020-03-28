package io.antinolabs.libs.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.antinolabs.libs.Adapter.ImageAdapter;
import io.antinolabs.libs.BottomSheetPickerFragment;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.Constants;
import io.antinolabs.libs.Utils.ImageUtils;
import io.antinolabs.libs.models.DataModel;

public class ImageFragment extends Fragment {
    private static final int ARG_PARAM1 = 0;
    public static final String ARG_OBJECT = "object";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<DataModel> imagePaths = new ArrayList<>();
    private String mParam2;
    ImageAdapter imageAdapter;
    RecyclerView recyclerView;
    SelectedUrisInterface selectedUrisInterface;

    public ImageFragment(SelectedUrisInterface selectedUrisInterface) {
        this.selectedUrisInterface = selectedUrisInterface;
    }

    public static ImageFragment newInstance(SelectedUrisInterface selectedUrisInterface, String param2) {
        ImageFragment fragment = new ImageFragment(selectedUrisInterface);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rc_gallery);
    recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

    if (mParam2.equals("Images")) {
        imagePaths = ImageUtils.getAllImagesPath(this.getActivity());
        if (imagePaths.size() > 0) {
            DataModel dataModel = new DataModel("", Constants.CAMERA_IMAGE);
            imagePaths.add(0, dataModel);
            this.imageAdapter = new ImageAdapter(this.getContext(), imagePaths, selectedUrisInterface);
            recyclerView.setAdapter(imageAdapter);
        }
    }
    }
}
