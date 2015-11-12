package com.jesusgandhiandbebe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.AccessToken;
import com.jesusgandhiandbebe.api.RestAPI;
import com.jesusgandhiandbebe.models.Lobby;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);


    }

    public void createLobby(View view) {
        // Creates a call to the backend to create a new lobby with the specified criteria of name and time
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .build();

        RestAPI service = retrofit.create(RestAPI.class);
        // Create new lobby
        Lobby lobby = new Lobby("Party", "12:00");
        service.postLobby(AccessToken.getCurrentAccessToken().getToken(), lobby, new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
