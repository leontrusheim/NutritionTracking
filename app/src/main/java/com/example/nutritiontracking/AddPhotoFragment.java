package com.example.nutritiontracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPhotoFragment extends Fragment {

    Uri photoURI;
    String currentPhotoPath;
    int REQUEST_IMAGE_CAPTURE = 1;
    Meal meal;


    public AddPhotoFragment(Meal meal) {
        this.meal = meal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_photo, container, false);
        Button b = v.findViewById(R.id.captureButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTakeCameraPhoto(view);
            }
        });

        Button b2 = v.findViewById(R.id.photosButton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGetPhotos(view);
            }
        });
        return v;
    }

    /**
     * Creates an image file
     * @return an image file to write bitmap data to, in order to save the Camera's photo
     */
    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Creates an intent to open the camera, calls a method to create a
     * file to store camera data in, gets the file's Uri, and then starts
     * the camera intent activity to take a photo.
     *
     * @param view -- the view that is clicked on (an ImageView). This view will be updated
     *             with the new image taken with the camera.
     */
    public void onClickTakeCameraPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.rypittner.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("image_id", view.getId());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Is called after the camera has captured an image. This method updates
     * the ImageView with the newly taken image.
     * @param data -- the intent that started the camera activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE){ //TODO && resultCode == RESULT_OK){
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            Bundle args = new Bundle();
            args.putString("PATH", photoURI.toString());
            meal.setPhotoURI(photoURI);
            Fragment mealSummaryFrag = new MealSummaryFragment(meal);
            mealSummaryFrag.setArguments(args);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
            fragmentTransaction.commit();
            //ImageView iv = findViewById(image_index);

            //iv.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Compresses Bitmap screenshot data into a file, saving the screenshot as
     * a file
     * @param imageBitmap -- a Bitmap, the screenshot of the collage
     * @param photoPath -- a string, representing a path to the photo File to
     *                  save the bitmap data to, before attaching it to the email
     */
    private void saveImage(Bitmap imageBitmap, String photoPath) {
        try {
            int quality = 100;
            FileOutputStream fos = new FileOutputStream(photoPath);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickGetPhotos(View v){
        Fragment photosFragment = new PhotosFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, photosFragment);
        fragmentTransaction.commit();
    }
}