package com.jesusgandhiandbebe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.jesusgandhiandbebe.api.RestAPI;
import com.jesusgandhiandbebe.models.Lobbies;
import com.jesusgandhiandbebe.models.Lobby;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get reference to lobby list item to creat a click handler to take a picture of the lobby
        CardView lobby = (CardView) findViewById(R.id.card_view);
        lobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        // get reference to action button to create a click handler to start activity to create Lobby
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CreateLobbyActivity.class));
            }
        });

        // Create call to backend to receive all lobbies that the individual is a part of
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        service.getLobbies(AccessToken.getCurrentAccessToken().getToken(), new Callback<Lobbies>() {
            @Override
            public void onResponse(Response<Lobbies> response, Retrofit retrofit) {
                // Parse the response and add to a local array of lobbies
                Lobbies lbbi = new Lobbies();
                for (int i = 0; i < response.body().lobbies.size(); i++) {
                    // Create new lobby from response
                    Lobby newLobby = new Lobby(response.body().lobbies.get(i).name, response.body().lobbies.get(i).timestamp);
                    lbbi.addLobby(newLobby);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
