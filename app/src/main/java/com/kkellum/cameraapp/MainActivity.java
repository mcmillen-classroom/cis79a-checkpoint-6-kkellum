package com.kkellum.cameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mimage_view;
    private Button mcamera_button;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String currentPhotoPath;
    private Notification.Action.Builder data;
    private Uri mCurrentPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mimage_view = (ImageView) findViewById(R.id. image_view);

        mcamera_button = (Button) findViewById(R.id. camera_button);

        mcamera_button.setOnClickListener(this);

        private File createImageFile()throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;
            try {
                image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }
        private void dispatchTakePictureIntent(); {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView imageView;
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }


    @Override

    public void onClick(View view) {

    }

    public MainActivity(ImageView mimage_view) {
        this.mimage_view = mimage_view;
    }

    private void dispatchSharePicture()
    {
        File image = new File(mCurrentPhotoUri.getPath());
        Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", image);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.setType("image/jpeg");

        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_pic)));

        // Create the text message with a string
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        boolean textMessage = false;
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        sendIntent.setType("text/plain");

// Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }

    }



    private void dispatchEmailPicture()
    {
        File image = new File(mCurrentPhotoUri.getPath());
        Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", image);

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out my pic!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Taken using my CameraApp.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, photoURI);

        if (emailIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(emailIntent);
        }
        else
        {
            Toast.makeText(this, "No email app configured.", Toast.LENGTH_LONG).show();
        }
    }


}



