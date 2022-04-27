package com.example.nutritiontracking;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    int width;

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
        getWidthInPixels(inflatedView);
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
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, width);
            iv.setLayoutParams(lp);
            //iv.setPadding();
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.currMeal.setPhotoURI(photoUri);
                    onClickOpenMealSummary(photoUri);
                }
            });
            ll.addView(iv);
        }
        query.close();
        return galleryImageUrls;
    }


    protected void getWidthInPixels(View v) {
        int offset = v.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels - offset)/ 3) - 50;
    }

    public void onClickOpenMealSummary(Uri uri){
        MainActivity.currMeal.setPhotoURI(uri);
        Fragment mealSummaryFrag = new MealSummaryFragment(MainActivity.currMeal);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
        fragmentTransaction.commit();
    }

}