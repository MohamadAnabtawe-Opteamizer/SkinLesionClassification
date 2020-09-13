package com.opteamizer.android.skinlesionclassification;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button cameraButton;
    private Button uploadButton;
    private ImageView imageToClassify;
    private ImageClassifier classifier;
    private TextView labels;
    private static final int RESULT_LOAD_IMAGE = 1;
    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        cameraButton = view.findViewById(R.id.btn_camera);
        uploadButton = view.findViewById(R.id.btn_upload);
        imageToClassify = view.findViewById(R.id.image_to_classify);
        labels = view.findViewById(R.id.tv_labels);
        cameraButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        try {
            classifier = new ImageClassifierFloatMobileNet(getActivity());
        } catch (IOException e) {
            Log.d(TAG, "Failed to load", e);
            classifier = null;
        }
    }

        @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_camera){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, Camera2Fragment.newInstance())
                    .addToBackStack( "camera2_fragment" )
                    .commit();
        } else if(v.getId() == R.id.btn_upload){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageToClassify.setImageURI(selectedImage);
            Bitmap bitmap = imageViewToBitmap(imageToClassify);
            SpannableStringBuilder textToShow = new SpannableStringBuilder();
            classifier.classifyFrame(bitmap, textToShow);
            bitmap.recycle();
            showToast(textToShow);
        }
    }

    private Bitmap imageViewToBitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

    @Override
    public void onDestroy() {
        if (classifier != null) {
            classifier.close();
        }
        super.onDestroy();
    }

    private void showToast(SpannableStringBuilder builder) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            labels.setText(builder, TextView.BufferType.SPANNABLE);
                        }
                    });
        }
    }
}