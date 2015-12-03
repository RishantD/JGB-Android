package com.jesusgandhiandbebe.models;

/**
 * Picture Schema for json object
 */
public class Picture {

    public String name;
    public String lobbyId;
    public byte[] picture;
    public String createdAt;

    public Picture(String name, String lobbyId, byte[] picture, String createdAt) {
        this.name = name;
        this.lobbyId = lobbyId;
        this.picture = picture;
        this.createdAt = createdAt;
    }
}
