/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: PhotosFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents a PhotosFragment, a fragment that displays images
 *          from the camera roll using a content provider and sets onclick listeners for
 *          each image in order to save the image to a meal.
 */

package com.example.nutritiontracking;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    int width;

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


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    @Override
    public void onResume() {
        super.onResume();
        new PhotosTask().execute();
    }
    /**
     * Creates a PhotosTask (AsyncTask) to use a content provider to get no more than 20 photos
     * from the camera roll to select as the photo for the meal.
     */

    private class PhotosTask extends AsyncTask<Void, Void, ArrayList<String>>{

        /**
         * Fetches and displays the gallery images using a content provider. Gets each image in
         * the user's camera roll and adds it to an arraylist of all image strings.
         */
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> imageStrs = new ArrayList<>();
            getWidthInPixels(inflatedView);

            //order data by date
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            //get all data in Cursor by sorting in DESC order
            Cursor query = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID},
                    null, null, orderBy + " DESC");


            for (int i = 0; i < query.getCount(); i++) {
                if (i >= 20){
                    break;
                }
                query.moveToPosition(i);
                int dataColumnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA);
                String imageString = query.getString(dataColumnIndex);

                imageStrs.add(imageString);

            }
            query.close();
            return imageStrs;
        }

        /**
         * Updates the UI to display each image by adding an ImageView for each image. Sets an onClicklistener
         * for each image, such that if the image is clicked on, it will be assigned to the meal and saved.
         */
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            for (String imageString : strings){
                Bitmap bitmap = BitmapFactory.decodeFile(imageString);

                ImageView iv = new ImageView(getActivity());
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, width);
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

                iv.setImageBitmap(bitmap);

                Bitmap bitmapCopy = bitmap;
                iv.setOnClickListener(new View.OnClickListener() {

                    final Bitmap finalBitmap = bitmapCopy;

                    @Override
                    public void onClick(View view) {
                        onClickOpenMealSummary(finalBitmap);
                    }
                });
                ll.addView(iv);
            }
        }
    }

    /**
     * Sets the width value to be equal to the width of the screen / 3.
     * This defines how large the images are displayed so
     * that a column of 3 images will fill the width.
     */
    protected void getWidthInPixels(View v) {
        int offset = v.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels - offset)/ 3) - 50;
    }

    /**
     * Opens a new fragment to show the meal summary, with the selected image
     * @param bitmap
     */
    public void onClickOpenMealSummary(Bitmap bitmap){
        MainActivity.currMeal.setBitmap(bitmap);
        Fragment mealSummaryFrag = new MealSummaryFragment(MainActivity.currMeal);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
        fragmentTransaction.commit();
    }

}