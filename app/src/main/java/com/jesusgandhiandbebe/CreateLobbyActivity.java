package com.jesusgandhiandbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.jesusgandhiandbebe.adapters.FriendListAdapter;
import com.jesusgandhiandbebe.api.RestAPI;
import com.jesusgandhiandbebe.models.Lobby;
import com.jesusgandhiandbebe.models.User;
import com.jesusgandhiandbebe.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateLobbyActivity extends AppCompatActivity {

    private List<String> friends_fbIds = new ArrayList<>();
    private List<String> friends_Names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    JSONObject friends = object.getJSONObject("friends");
                    JSONArray friendData = friends.getJSONArray("data");
                    if (friendData != null) {
                        for (int i = 0; i < friendData.length(); i++) {
                            friends_fbIds.add(friendData.getJSONObject(i).getString("id"));
                            friends_Names.add(friendData.getJSONObject(i).getString("name"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FriendListAdapter adapter = new FriendListAdapter(friends_Names);
                recyclerView.setAdapter(adapter);

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void createLobby(View view) {

        EditText eventNameTV = (EditText) findViewById(R.id.event_name);
        EditText eventTimeTV = (EditText) findViewById(R.id.event_time);

        // Creates a call to the backend to create a new lobby with the specified criteria of name and time
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fbId = prefs.getString(Constants.FB_ID_PREFS_KEY, "");
        String name = prefs.getString(Constants.NAME_PREFS_KEY, "");

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < friends_fbIds.size(); i++) {
            userList.add(new User(friends_fbIds.get(i)));
        }
        Users users = new Users(userList);

        // Create new lobby
        Lobby lobby = new Lobby(null, eventTimeTV.getText().toString(), eventNameTV.getText().toString(), name, userList);
        Call<com.jesusgandhiandbebe.models.Response> c = service.postLobby(fbId, lobby);
        c.enqueue(new Callback<com.jesusgandhiandbebe.models.Response>() {
            @Override
            public void onResponse(Response<com.jesusgandhiandbebe.models.Response> response, Retrofit retrofit) {
                Log.d("/api/Lobbies/create", Integer.toString(response.code()));

                startActivity(new Intent(CreateLobbyActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("/api/Lobbies/create", t.getMessage());
            }
        });
    }
}
