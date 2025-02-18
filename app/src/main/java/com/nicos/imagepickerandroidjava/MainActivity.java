package com.nicos.imagepickerandroidjava;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwnerKt;

import com.nicos.imagepickerandroid.image_picker.ImagePicker;
import com.nicos.imagepickerandroid.image_picker.ImagePickerInterface;
import com.nicos.imagepickerandroid.utils.image_helper_methods.ScaleBitmapModel;
import com.nicos.imagepickerandroidjava.adapters.ListImagesAdapter;
import com.nicos.imagepickerandroidjava.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ImagePickerInterface {

    private ActivityMainBinding binding;
    private ListImagesAdapter listImageAdapter;
    private ImagePicker imagePicker;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initImagePicker();
        initAdapter();
        initListeners();
    }

    private void initAdapter() {
        listImageAdapter = new ListImagesAdapter();
        binding.recyclerView.setAdapter(listImageAdapter);
    }

    private void initImagePicker() {
        imagePicker = new ImagePicker(
                this, //activity instance
                null, // fragment instance
                LifecycleOwnerKt.getLifecycleScope(this), // mandatory - coroutine scope from activity or fragment
                new ScaleBitmapModel(100, 100), // optional, change the scale for image, by default is null
                new ScaleBitmapModel(100, 100), // optional, change the scale for image, by default is null
                new ScaleBitmapModel(100, 100), // optional, change the scale for image, by default is null
                true, // optional, by default is false
                true, // optional, by default is false
                true, // optional, by default is false
                this // call back interface
        );
        imagePicker.initPickSingleImageFromGalleryResultLauncher();
        imagePicker.initPickMultipleImagesFromGalleryResultLauncher(9);
        imagePicker.initTakePhotoWithCameraResultLauncher();
        imagePicker.initPickSingleVideoFromGalleryResultLauncher();
    }

    private void initListeners() {
        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickSingleImageFromGallery();
            }
        });
        binding.pickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickMultipleImagesFromGallery();
            }
        });
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.takeSinglePhotoWithCamera();
            }
        });
        binding.pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickSingleVideoFromGallery();
            }
        });
    }

    @Override
    public void onGallerySingleVideo(@Nullable Uri uri) {
        if (uri != null) binding.video.setVideoURI(uri);
        binding.video.start();
    }


    @Override
    public void onCameraImageWithBase64Value(@Nullable Bitmap bitmap, @Nullable String s) {
        if (bitmap != null) binding.image.setImageBitmap(bitmap);
    }

    @Override
    public void onGallerySingleImageWithBase64Value(@Nullable Bitmap bitmap, @Nullable Uri uri, @Nullable String s) {
        if (bitmap != null) binding.image.setImageBitmap(bitmap);
    }

    @Override
    public void onMultipleGalleryImagesWithBase64Value(@Nullable List<Bitmap> list, @Nullable List<Uri> list1, @Nullable List<String> list2) {
        if (list != null) listImageAdapter.loadData(list);
    }

    @Override
    public void onGallerySingleImage(@Nullable Bitmap bitmap, @Nullable Uri uri) {

    }

    @Override
    public void onMultipleGalleryImages(@Nullable List<Bitmap> list, @Nullable List<Uri> list1) {

    }

    @Override
    public void onCameraImage(@Nullable Bitmap bitmap) {

    }
}