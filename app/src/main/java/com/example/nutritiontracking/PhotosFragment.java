package com.example.nutritiontracking;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private AppCompatActivity containerActivity = null;
    private View inflatedView = null;
    LinearLayout gridView;

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_photos, container, false);
        gridView = inflatedView.findViewById(R.id.grid_images);
        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAndDisplayGalleryImages(getActivity());
    }

    public ArrayList<String> fetchAndDisplayGalleryImages(Activity context) {
        ArrayList<String> galleryImageUrls;

        //get all columns of type images
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        //order data by date
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        //get all data in Cursor by sorting in DESC order
        Cursor query = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID},
                null, null, orderBy + " DESC");

        galleryImageUrls = new ArrayList<String>();

        for (int i = 0; i < query.getCount(); i++) {
            query.moveToPosition(i);
            int dataColumnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA);
            String imageString = query.getString(dataColumnIndex);
            galleryImageUrls.add(imageString);
            ImageView iv = new ImageView(context);
            Bitmap bitmap = BitmapFactory.decodeFile(imageString);
            iv.setImageBitmap(bitmap);
            iv.setPadding(20,20,20,20);
            gridView.addView(iv);
        }
        query.close();
        return galleryImageUrls;
    }
}