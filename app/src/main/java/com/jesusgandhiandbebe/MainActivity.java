package com.jesusgandhiandbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jesusgandhiandbebe.adapters.LobbyListAdapter;
import com.jesusgandhiandbebe.api.RestAPI;
import com.jesusgandhiandbebe.models.Lobbies;
import com.jesusgandhiandbebe.models.Lobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get reference to action button to create a click handler to start activity to create Lobby
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CreateLobbyActivity.class));
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lobby_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fbId = prefs.getString(Constants.FB_ID_PREFS_KEY, "");

        // Create call to backend to receive all lobbies that the individual is a part of
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        Call<Lobbies> c = service.getLobbies(fbId);

        final List<Lobby> data = new ArrayList<>();

        c.enqueue(new Callback<Lobbies>() {
            @Override
            public void onResponse(Response<Lobbies> response, Retrofit retrofit) {
                Log.d("/api/Lobbies/get", Integer.toString(response.code()));

                for (int i = 0; i < response.body().data.size(); i++) {
                    // Create new lobby from response
                    Lobby lobbyElement = response.body().data.get(i);

                    data.add(new Lobby(lobbyElement._id, lobbyElement.createdAt, lobbyElement.name,
                            lobbyElement.creator, lobbyElement.users));


                }

                LobbyListAdapter adapter = new LobbyListAdapter(data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t) {
                if(t != null)
                    Log.e("/api/Lobbies/get", t.getMessage());
                else
                    Log.e("/api/Lobbies/get", "Oops");
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
