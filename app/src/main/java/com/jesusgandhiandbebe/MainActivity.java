package com.jesusgandhiandbebe;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
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

    final List<Lobby> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get reference to action button to create a click handler to start activity to create Lobby
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(new Explode());
                    startActivity(new Intent(MainActivity.this, CreateLobbyActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                } else {
                    startActivity(new Intent(MainActivity.this, CreateLobbyActivity.class));
                }
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lobby_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        Intent intent = getIntent();
        boolean success = intent.getBooleanExtra("CameraSuccess", false);
        if (success) {
            Snackbar.make(recyclerView, "Picture Sent!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String fbId = prefs.getString(Constants.FB_ID_PREFS_KEY, "");

        loadData(null, recyclerView, fbId);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                synchronized (recyclerView) {
                    recyclerView.notify();
                }
                loadData(mSwipeRefreshLayout, recyclerView, fbId);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int pos = viewHolder.getAdapterPosition();
                data.remove(pos);
                synchronized (recyclerView){
                    recyclerView.notify();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadData(final SwipeRefreshLayout mSwipeRefreshLayout, final RecyclerView recyclerView, String fbId) {
        // Create call to backend to receive all lobbies that the individual is a part of
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        Call<Lobbies> c = service.getLobbies(fbId);

        //Makes an async call to the backend to get lobbies
        c.enqueue(new Callback<Lobbies>() {
            @Override
            public void onResponse(Response<Lobbies> response, Retrofit retrofit) {
                Log.d("/api/Lobbies/get", Integer.toString(response.code()));

                for (int i = 0; i < response.body().data.size(); i++) {
                    // Create new lobby from response
                    Lobby lobbyElement = response.body().data.get(i);
                    //add lobbies to a data object to attach to an adapter
                    data.add(new Lobby(lobbyElement._id, lobbyElement.createdAt, lobbyElement.name,
                            lobbyElement.creator, lobbyElement.users));


                    if (mSwipeRefreshLayout != null)
                        mSwipeRefreshLayout.setRefreshing(false);

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

                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
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
