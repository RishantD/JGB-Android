package com.jesusgandhiandbebe.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishant_dwivedi on 11/12/15.
 */
public class Lobbies {

    public ArrayList<Lobby> lobbies;

    /**
     * Creates a new lobby through initilization of the main array
     */
    public Lobbies() {
        this.lobbies = new ArrayList<Lobby>();
    }

    /**
     * Pushes to lobby
     * @param newLobby
     */
    public void addLobby(Lobby newLobby) {
        this.lobbies.add(newLobby);
    }
}
