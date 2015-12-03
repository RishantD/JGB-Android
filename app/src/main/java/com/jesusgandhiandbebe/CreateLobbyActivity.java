package com.jesusgandhiandbebe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateLobbyActivity extends AppCompatActivity {

    private List<String> friends_fbIds = new ArrayList<>();
    private List<String> friends_Names = new ArrayList<>();
    private static Date start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());

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

        final TextView timePicker = (TextView) findViewById(R.id.event_time);
        final TextView datePicker = (TextView) findViewById(R.id.event_day);

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v);
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);

        start = calendar.getTime();
    }

    public void createLobby(View view) {

        EditText eventNameTV = (EditText) findViewById(R.id.event_name);

        // Creates a call to the backend to create a new lobby with the specified criteria of name and time
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestAPI service = retrofit.create(RestAPI.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fbId = prefs.getString(Constants.FB_ID_PREFS_KEY, "");
        String name = prefs.getString(Constants.NAME_PREFS_KEY, "");

        // Create a list of the friends and add to the users list
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < friends_fbIds.size(); i++) {
            userList.add(new User(friends_fbIds.get(i)));
        }
        Users users = new Users(userList);

        // Create new lobby
        Lobby lobby = new Lobby(null, start.toString(), eventNameTV.getText().toString(), name, userList);
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

    public void showTimePicker(View view) {
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePicker(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            DateFormat time = DateFormat.getTimeInstance();
            final TextView button = (TextView) getActivity().findViewById(R.id.event_time);
            start = calendar.getTime();
            button.setText(time.format(start));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            DateFormat date = DateFormat.getDateInstance();
            final TextView button = (TextView) getActivity().findViewById(R.id.event_day);
            start = calendar.getTime();
            button.setText(date.format(start));
        }
    }
}
