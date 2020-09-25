package com.neosao.truedates.screens;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.configs.VolleyMultipartRequest;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadProfileImage extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViews;
    UserModel user;
    LocalPref localPref;
    ArrayList<Image> images = new ArrayList<>();
    Image[] selectedImages = new Image[6];
    ImageView tappedImageView;
    Button continueBtn;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        localPref = new LocalPref(getBaseContext());
        user = localPref.getUser();
        continueBtn = findViewById(R.id.continueBtn);

        imageViews = new ImageView[]{
                findViewById(R.id.profileImage1),
                findViewById(R.id.profileImage2),
                findViewById(R.id.profileImage3),
                findViewById(R.id.profileImage4),
                findViewById(R.id.profileImage5),
                findViewById(R.id.profileImage6)
        };
        for (ImageView iv : imageViews)
            iv.setOnClickListener(this);

        dialog = Utils.getProgress(UploadProfileImage.this, "Uploading image...");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedImageCount = 0;
                for (Image i : selectedImages)
                    if (null != i)
                        selectedImageCount++;

                 if(selectedImageCount<4)
                 {
                     Toast.makeText(getBaseContext(),"Select atleast 4 images", Toast.LENGTH_LONG).show();
                     return;
                 }
                dialog.show();
                uploadImages(0);
            }
        });
    }

    private void uploadImages(final int index) {
        if (index > 5) {
            if (dialog.isShowing())
                dialog.dismissWithAnimation();
            startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return;
        }
        if (null != selectedImages[index]) {
            // upload image

            try {
                final Image imageToBeUploaded = selectedImages[index];
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageToBeUploaded.getUri());

                Log.e("check", "Uploading : " + imageToBeUploaded.getPath());
                Log.e("check", "Uploading : " + imageToBeUploaded.getName());

                //our custom volley request
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, API.UPLOAD_IMAGE,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
                                    JSONObject obj = new JSONObject(new String(response.data));
                                    Log.e("check", obj.toString());
                                    if (obj.has("message"))
                                        Toast.makeText(getBaseContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    ArrayList<MemberPhotos> photosArrayList = user.getMemberPhotos();
                                    if (null == photosArrayList)
                                        photosArrayList = new ArrayList<>();
                                    photosArrayList.add(new MemberPhotos(imageToBeUploaded.getName(), imageToBeUploaded.getPath()));
                                    user.setMemberPhotos(photosArrayList);
                                    localPref.saveUser(user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                uploadImages(index + 1);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                uploadImages(index + 1);
                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is tags
                     * */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", user.getUserId());
                        return params;
                    }

                    /*
                     * Here we are passing image by renaming it with a unique name
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        long imagename = System.currentTimeMillis();
                        params.put("file", new VolleyMultipartRequest.DataPart(imageToBeUploaded.getName(), getFileDataFromDrawable(bitmap)));
                        return params;
                    }
                };

                //adding the request to volley
                Volley.newRequestQueue(this).add(volleyMultipartRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else
            uploadImages(index + 1);


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onClick(View view) {
        tappedImageView = (ImageView) view;
        new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        ImagePicker.with(UploadProfileImage.this)
                                .setFolderMode(true)
                                .setFolderTitle("Album")
                                .setDirectoryName("True Dates")
                                .setMultipleMode(false)
                                .setShowNumberIndicator(true)
                                .setMaxSize(1)
                                .setBackgroundColor("#ffffff")
                                .setStatusBarColor("#E0E0E0")
                                .setToolbarColor("#ffffff")
                                .setToolbarIconColor("#FF6F8B")
                                .setToolbarTextColor("#FF6F8B")
                                .setProgressBarColor("#FF6F8B")
                                .setIndicatorColor("#FF6F8B")
                                .setShowCamera(true)
                                .setDoneTitle("Select")
                                .setLimitMessage("You can select up to 10 images")
                                .setSelectedImages(images)
                                .setRequestCode(100)
                                .start();
                    }
                })
                .whenPermissionsRefused(new PermissionsRefusedListener() {
                    @Override
                    public void onPermissionsRefused(String[] permissions) {
                        Toast.makeText(getBaseContext(), "We need your permission to read the image", Toast.LENGTH_LONG).show();
                    }
                })
                .execute(UploadProfileImage.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            images = ImagePicker.getImages(data);
            selectedImages[getIndexOfImageView(tappedImageView)] = images.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Glide.with(getBaseContext())
                        .load(images.get(0).getUri())
                        .into(tappedImageView);
            } else {
                Glide.with(getBaseContext())
                        .load(images.get(0).getPath())
                        .into(tappedImageView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getIndexOfImageView(ImageView view) {
        switch (view.getId()) {
            case R.id.profileImage1:
                return 0;
            case R.id.profileImage2:
                return 1;
            case R.id.profileImage3:
                return 2;
            case R.id.profileImage4:
                return 3;
            case R.id.profileImage5:
                return 4;
            case R.id.profileImage6:
                return 5;
        }
        return 0;
    }


}