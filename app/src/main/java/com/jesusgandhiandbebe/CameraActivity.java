package com.jesusgandhiandbebe;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.jesusgandhiandbebe.api.RestAPI;
import com.jesusgandhiandbebe.models.Picture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CameraActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String lobbyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent extras = getIntent();
        lobbyID = extras.getStringExtra(Constants.LOBBY_ID_KEY);

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Code to add picture to database
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RestAPI service = retrofit.create(RestAPI.class);


                // Parses the picture into a byte array by:
                // Converting to a bitmap and then using the bytes from the conversion to
                // instantiate a new byte array
                Bitmap b = (Bitmap) data.getExtras().get("data");

                int bytes = 0;
                if (b != null) {
                    bytes = b.getByteCount();
                }

                ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                if (b != null) {
                    b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
                }

                byte[] array = buffer.array();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (b != null) {
                    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
                }

                //byte[] encoded = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
                byte[] encoded = baos.toByteArray();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CameraActivity.this);
                String fbId = prefs.getString(Constants.FB_ID_PREFS_KEY, "");
                String name = prefs.getString(Constants.NAME_PREFS_KEY, "");

                // Create timestamp for when picture was taken
                Calendar c  = Calendar.getInstance();
                String date = c.getTime().toString();

                // Creates a new picture
                Picture picture = new Picture(name, lobbyID, encoded, date);
                Call<com.jesusgandhiandbebe.models.Response> callback = service.postPicture(fbId, picture);
                callback.enqueue(new Callback<com.jesusgandhiandbebe.models.Response>() {
                    @Override
                    public void onResponse(Response<com.jesusgandhiandbebe.models.Response> response, Retrofit retrofit) {
                        Log.d("/api/Pictures/add", Integer.toString(response.code()));

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("/api/Pictures/add", t.getMessage());
                    }
                });

                startActivity(new Intent(CameraActivity.this, MainActivity.class).putExtra("CameraSuccess", true));
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

    }
}
