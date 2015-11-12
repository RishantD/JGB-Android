package com.jesusgandhiandbebe.models;

/**
 * Created by rishant_dwivedi on 11/12/15.
 */
public class Lobby {

    public String name;
    public String timestamp;

    /**
     * Creates a lobby
     * @param name of lobby
     * @param timestamp of when created
     */
    public Lobby (String name, String timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }
}
