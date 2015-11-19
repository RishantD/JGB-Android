package com.jesusgandhiandbebe.models;

import java.util.List;

/**
 *
 * Created by rishant_dwivedi on 11/12/15.
 */
public class Lobby {

    public String _id;
    public String createdAt;
    public String name;
    public String creator;
    public List<User> users;

    public Lobby(String _id, String createdAt, String name, String creator, List<User>  users) {
        this._id = _id;
        this.createdAt = createdAt;
        this.name = name;
        this.creator = creator;
        this.users = users;
    }
}
