package io.antinolabs.libs.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.antinolabs.libs.Adapter.ImageAdapter;
import io.antinolabs.libs.Adapter.VideoAdapter;
import io.antinolabs.libs.Interfaces.SelectedUrisInterface;
import io.antinolabs.libs.R;
import io.antinolabs.libs.Utils.Constants;
import io.antinolabs.libs.Utils.ImageUtils;
import io.antinolabs.libs.Utils.ModelVideo;
import io.antinolabs.libs.Utils.VideoUtils;
import io.antinolabs.libs.models.DataModel;

public class ImageFragment extends Fragment {
    private static final int ARG_PARAM1 = 0;
    public static final String ARG_OBJECT = "object";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<DataModel> imagePaths = new ArrayList<>();
    private String mParam2;
    ImageAdapter imageAdapter;
    VideoAdapter videoAdapter;
    RecyclerView recyclerView;
    SelectedUrisInterface selectedUrisInterface;
    private boolean imagevariable,videovariable;

    public ImageFragment(SelectedUrisInterface selectedUrisInterface,boolean imagevariable,boolean videovariable) {
        this.selectedUrisInterface = selectedUrisInterface;
        this.imagevariable=imagevariable;
        this.videovariable=videovariable;
    }

    public static ImageFragment newInstance(SelectedUrisInterface selectedUrisInterface, String param2,boolean imagevariable,boolean videovariable) {
        Log.d(
                "ImageFragment", "newInstance: "+imagevariable+" " +videovariable);
        ImageFragment fragment = new ImageFragment(selectedUrisInterface,imagevariable,videovariable);
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
        Log.e("Size:", String.valueOf(imagePaths.size()));
        if (imagePaths.size() > 0 && imageVariable) {
            DataModel dataModel = new DataModel("", Constants.CAMERA_IMAGE);
            imagePaths.add(0, dataModel);
            this.imageAdapter = new ImageAdapter(this.getContext(), imagePaths, selectedUrisInterface);
            recyclerView.setAdapter(imageAdapter);
        }
       else if(mParam2.equals("Videos"))
        {
            Log.d("Videos", "onViewCreated: "+mParam2);
            if(videovariable)
            {
                ArrayList<ModelVideo> videoPaths = VideoUtils.getAllVideosPath(this.getActivity());
                Log.d("videoPaths", "onViewCreated: "+videoPaths.get(0).getStrpath());
                if (videoPaths.size() > 0) {
                    this.videoAdapter = new VideoAdapter(this.getContext(), videoPaths, selectedUrisInterface);
                    recyclerView.setAdapter(videoAdapter);
                }
            }
        }
    }
}
