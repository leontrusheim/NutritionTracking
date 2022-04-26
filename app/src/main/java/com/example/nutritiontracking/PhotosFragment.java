package com.example.nutritiontracking;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private AppCompatActivity containerActivity = null;
    private View inflatedView = null;
    GridLayout ll;

    public PhotosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_photos, container, false);
        ll = inflatedView.findViewById(R.id.grid_images);
        return inflatedView;
    }

    public void setContainerActivity(AppCompatActivity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

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
            Uri photoUri = Uri.parse(imageString);
            System.out.println(photoUri.toString());
            ImageView iv = new ImageView(context);
            System.out.println(imageString);
            iv.setImageURI(photoUri);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT)
            iv.setPadding(20,20,20,20);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll.addView(iv);
        }
        query.close();
        return galleryImageUrls;
    }

}