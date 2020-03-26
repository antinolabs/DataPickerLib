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
import android.widget.TextView;

import java.util.ArrayList;

import io.antinolabs.libs.Adapter.ImageAdapter;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.ImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    private static final int ARG_PARAM1 = 0;
    public static final String ARG_OBJECT = "object";
    private static final String ARG_PARAM2 = "param2";
    private int mParam1;
    private String mParam2;
    RecyclerView recyclerView;

    public ImageFragment() {
    }

    public static ImageFragment newInstance(int param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
       // args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
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
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 3);
    recyclerView.setLayoutManager(layoutManager);

    ArrayList<String> imagePaths = ImageUtils.getAllImagesPath(this.getActivity());
    if(imagePaths.size() > 0){
      ImageAdapter imageAdapter = new ImageAdapter(this.getContext(), imagePaths);
      recyclerView.setAdapter(imageAdapter);
      recyclerView.invalidate();
      imageAdapter.notifyDataSetChanged();
    }
    }
}
