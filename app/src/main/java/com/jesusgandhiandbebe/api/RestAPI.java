package com.jesusgandhiandbebe.api;

import com.jesusgandhiandbebe.models.Lobbies;
import com.jesusgandhiandbebe.models.Lobby;
import com.jesusgandhiandbebe.models.Picture;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by rishant_dwivedi on 11/12/15.
 */
public interface RestAPI {

    /**
     * Route for backend to get Lobbies
     * @param token of facebook logged in user
     * @param cb the action to perform
     */
    @GET("/api/getLobbies")
    void getLobbies(@Header("X-Facebook-Token") String token, Callback<Lobbies> cb);

    /**
     * Route for backend to post the picture
     * @param token of facebook logged in user
     * @param picture the picture to post
     * @param cb the action to perform afterwards
     */
    @POST("/api/postPicture")
    void postPicture(@Header("X-Facebook-Token") String token, @Body Picture picture, Callback<String> cb);

    /**
     * Route for backend to post a new lobby
     * @param token of facebook logged in user
     * @param lobby the new lobby to add
     * @param cb the action to perform afterwards
     */
    @POST("/api/postLobby")
    void postLobby(@Header("X-Facebook-Token") String token, @Body Lobby lobby, Callback<String> cb);
}
