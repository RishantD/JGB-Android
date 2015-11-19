package com.jesusgandhiandbebe.api;

import com.jesusgandhiandbebe.models.Lobbies;
import com.jesusgandhiandbebe.models.Lobby;
import com.jesusgandhiandbebe.models.Picture;
import com.jesusgandhiandbebe.models.Response;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 *
 * Created by rishant_dwivedi on 11/12/15.
 */
public interface RestAPI {

    /**
     * Route for backend to get Lobbies
     * @param id of facebook logged in user
     */
    @GET("api/Lobbies/get")
    Call<Lobbies> getLobbies(@Header("X-Facebook-ID") String id);

    /**
     * Route for backend to post the picture
     * @param id of facebook logged in user
     * @param picture the picture to post
     */
    @POST("api/Pictures/add")
    Call<Response> postPicture(@Header("X-Facebook-ID") String id, @Body Picture picture);

    /**
     * Route for backend to post a new lobby
     * @param id of facebook logged in user
     * @param lobby the new lobby to add
     */
    @POST("api/Lobbies/create")
    Call<Response> postLobby(@Header("X-Facebook-ID") String id, @Body Lobby lobby);
}
