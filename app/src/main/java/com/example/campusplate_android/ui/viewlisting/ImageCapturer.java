package com.example.campusplate_android.ui.viewlisting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.campusplate_android.ui.addlisting.AddListingFragment;

import java.util.Objects;

public class ImageCapturer extends AppCompatActivity {


    ImageView imageView;

    public void captureImage(Context context, Fragment fragment){
        if(ContextCompat.checkSelfPermission(context , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //reference to data so that it can be sent back to add listing fragment

        intent.putExtra("data", intent);

        setResult(100, intent);

        fragment.requireActivity().startActivityFromFragment(fragment, intent, 100);
    }
}
