package com.neosao.truedates.screens;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.AndroidMultiPartEntity;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.neosao.truedates.configs.Utils.getIndexOfImageView;

public class UploadProfileImage extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViews;
    UserModel user;
    LocalPref localPref;
    ArrayList<Image> images = new ArrayList<>();
    Image[] selectedImages = new Image[6];
    ImageView tappedImageView;
    Button continueBtn;

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


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel newUser = localPref.getUser();
                 if(null != newUser && null != newUser.getMemberPhotos() && Utils.getPhotoCount(newUser.getMemberPhotos()) < 4)
                 {
                     Toast.makeText(getBaseContext(),"Select at least 4 images", Toast.LENGTH_LONG).show();
                     return;
                 }
                startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
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
            if(null != images.get(0))
            {
                new UploadImageTask(images.get(0)).execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < Utils.getPhotoCount(user.getMemberPhotos()) && i<6; i++) {
            if(null != user.getMemberPhotos()[i] && null != user.getMemberPhotos()[i].getMemberPhoto())
                Glide.with(getBaseContext())
                        .load(user.getMemberPhotos()[i].getMemberPhoto())
                        .into(imageViews[i]);
        }
    }

    private class UploadImageTask extends AsyncTask<Void,Void,String>{

        Image imageToBeUploaded;
        SweetAlertDialog dialog;
        public UploadImageTask(Image imageToBeUploaded) {
            this.imageToBeUploaded = imageToBeUploaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(UploadProfileImage.this, "Uploading image...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(API.UPLOAD_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });


                File sourceFile = new File(imageToBeUploaded.getPath());

                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("userId",
                        new StringBody(user.getUserId()));
                entity.addPart("index",
                        new StringBody(String.valueOf(getIndexOfImageView(tappedImageView))));

                httppost.setEntity(entity);


                Log.e("check","User id : "+ user.getUserId());
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            finally {

                dialog.dismissWithAnimation();

            }
            Log.e("check","upload response : "+ responseString);

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            Log.e("check","1 : "+responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
                if(null != obj && obj.has("message"))
                    Toast.makeText(getBaseContext(),obj.getString("message"),Toast.LENGTH_LONG).show();

                if(null != obj && obj.has("status") && obj.getString("status").equals("200"))
                {
                    selectedImages[getIndexOfImageView(tappedImageView)] = null;
                    Log.e("check","status");
                    if(obj.has("result") && obj.getJSONObject("result").has("memberPhoto"))
                    {
                        MemberPhotos photos = new Gson().fromJson(obj.getJSONObject("result").getJSONObject("memberPhoto").toString(), MemberPhotos.class);
                        if(null == user.getMemberPhotos() || user.getMemberPhotos().length < 9)
                            user.setMemberPhotos( new MemberPhotos[9]);

                        Log.e("check","photo : "+photos.toString());

                        user.getMemberPhotos()[getIndexOfImageView(tappedImageView)] = photos;
                        Log.e("check","photo arr length "+Utils.getPhotoCount(user.getMemberPhotos()));

                        localPref.saveUser(user);
                    }
                }
                else
                {
                    selectedImages[getIndexOfImageView(tappedImageView)] = null;
                    Glide.with(getBaseContext())
                            .load(R.drawable.dashed_border)
                            .into(tappedImageView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("check","err: "+e.getLocalizedMessage());
            }
        }
    }
}